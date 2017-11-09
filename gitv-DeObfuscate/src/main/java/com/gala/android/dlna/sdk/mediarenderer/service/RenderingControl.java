package com.gala.android.dlna.sdk.mediarenderer.service;

import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.mediarenderer.StandardDLNAListener;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.RenderingControlConstStr;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Argument;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceInterface;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;

public class RenderingControl extends Service implements ActionListener, QueryListener, ServiceInterface {
    private MediaRenderer mediaRenderer;
    private Mutex mutex = new Mutex();

    public RenderingControl(MediaRenderer render) {
        setMediaRenderer(render);
        initService();
        setActionListener(this);
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
        boolean isActionSuccess;
        MediaRenderer dmr = getMediaRenderer();
        int instanceID;
        String channel;
        StandardDLNAListener standardDLNAListener;
        if (actionName.equals(RenderingControlConstStr.GETMUTE)) {
            int i;
            Debug.message("Process RC actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            channel = action.getArgument(RenderingControlConstStr.CHANNEL).getValue();
            Boolean outCurrentMute = Boolean.valueOf(false);
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    outCurrentMute = Boolean.valueOf(standardDLNAListener.GetMute(instanceID, channel));
                }
            }
            Argument argument = action.getArgument(RenderingControlConstStr.CURRENTMUTE);
            if (outCurrentMute.booleanValue()) {
                i = 1;
            } else {
                i = 0;
            }
            argument.setValue(i);
        } else if (actionName.equals(RenderingControlConstStr.GETVOLUME)) {
            Debug.message("Process RC actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            channel = action.getArgument(RenderingControlConstStr.CHANNEL).getValue();
            Integer outCurrentVolume = Integer.valueOf(0);
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    outCurrentVolume = Integer.valueOf(standardDLNAListener.GetVolume(instanceID, channel));
                }
            }
            action.getArgument(RenderingControlConstStr.CURRENTVOLUME).setValue(outCurrentVolume.intValue());
        } else if (actionName.equals(RenderingControlConstStr.SETMUTE)) {
            Debug.message("Process RC actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            channel = action.getArgument(RenderingControlConstStr.CHANNEL).getValue();
            int desireMute = action.getArgument(RenderingControlConstStr.DESIREDMUTE).getIntegerValue();
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    standardDLNAListener.SetMute(instanceID, channel, desireMute != 0);
                }
            }
        } else if (actionName.equals(RenderingControlConstStr.SETVOLUME)) {
            Debug.message("Process RC actionControlReceived() action: " + actionName);
            instanceID = action.getArgument("InstanceID").getIntegerValue();
            channel = action.getArgument(RenderingControlConstStr.CHANNEL).getValue();
            int desiredVolume = action.getArgument(RenderingControlConstStr.DESIREDVOLUME).getIntegerValue();
            isActionSuccess = true;
            if (dmr != null) {
                standardDLNAListener = dmr.getStandardDLNAListener();
                if (standardDLNAListener != null) {
                    standardDLNAListener.SetVolume(instanceID, channel, desiredVolume);
                }
            }
        } else {
            Debug.message("Unknown RC actionControlReceived() action: " + actionName);
            isActionSuccess = false;
            action.setStatus(UPnPStatus.INVALID_ACTION);
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
        setServiceType("urn:schemas-upnp-org:service:RenderingControl:1");
        setServiceID(RenderingControlConstStr.SERVICE_ID);
        setControlURL(RenderingControlConstStr.CONTROL_URL);
        setSCPDURL(RenderingControlConstStr.SCPDURL);
        setEventSubURL(RenderingControlConstStr.EVENTSUB_URL);
        try {
            loadSCPD(RenderingControlConstStr.SCPD);
        } catch (InvalidDescriptionException e) {
            Debug.message(e.toString());
        }
    }
}
