package com.gala.android.dlna.sdk.mediarenderer.service;

import com.gala.android.dlna.sdk.mediarenderer.GalaDLNAListener;
import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.PrivateServiceConstStr;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceInterface;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.util.Mutex;

public class PrivateService extends Service implements ActionListener, QueryListener, ServiceInterface {
    private GalaDLNAListener galaDLNAListener = null;
    private MediaRenderer mediaRenderer;
    private Mutex mutex = new Mutex();

    public PrivateService(MediaRenderer render) {
        setMediaRenderer(render);
        initService();
        setActionListener(this);
    }

    public GalaDLNAListener getGalaDLNAListener() {
        return this.galaDLNAListener;
    }

    public void setGalaDLNAListener(GalaDLNAListener galaDLNAListener) {
        this.galaDLNAListener = galaDLNAListener;
    }

    private void setMediaRenderer(MediaRenderer render) {
        this.mediaRenderer = render;
    }

    public MediaRenderer getMediaRenderer() {
        return this.mediaRenderer;
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public boolean actionControlReceived(Action action) {
        String actionName = action.getName();
        if (actionName == null) {
            return false;
        }
        boolean isActionSuccess = false;
        MediaRenderer dmr = getMediaRenderer();
        if (actionName.equals(PrivateServiceConstStr.SEND_MESSAGE)) {
            int instanceID = action.getArgument("InstanceID").getIntegerValue();
            String infor = action.getArgument(PrivateServiceConstStr.INFOR).getValue();
            isActionSuccess = true;
            StringBuffer outResult = new StringBuffer();
            if (dmr != null) {
                GalaDLNAListener galaDLNAListener = dmr.getGalaDLNAListener();
                if (galaDLNAListener != null) {
                    galaDLNAListener.onReceiveSendMessage(instanceID, infor, outResult);
                }
            }
            action.getArgument("Result").setValue(outResult.toString());
        }
        if (dmr == null) {
            return isActionSuccess;
        }
        ActionListener listener = dmr.getActionListener();
        if (listener == null) {
            return isActionSuccess;
        }
        listener.actionControlReceived(action);
        return isActionSuccess;
    }

    public boolean queryControlReceived(StateVariable stateVar) {
        return false;
    }

    public void initService() {
        setServiceType(PrivateServiceConstStr.SERVICE_TYPE);
        setServiceID(PrivateServiceConstStr.SERVICE_ID);
        setControlURL(PrivateServiceConstStr.CONTROL_URL);
        setSCPDURL(PrivateServiceConstStr.SCPDURL);
        setEventSubURL(PrivateServiceConstStr.EVENTSUB_URL);
        try {
            loadSCPD(PrivateServiceConstStr.SCPD);
        } catch (InvalidDescriptionException e) {
            e.printStackTrace();
        }
    }
}
