package com.gala.android.dlna.sdk.mediarenderer.service;

import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.ConnectionInfo;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.ConnectionInfoList;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.ConnectionManagerConstStr;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceInterface;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;

public class ConnectionManager extends Service implements ActionListener, QueryListener, ServiceInterface {
    private ConnectionInfoList conInfoList = new ConnectionInfoList();
    private int maxConnectionID = 0;
    private MediaRenderer mediaRenderer;
    private Mutex mutex = new Mutex();

    public ConnectionManager(MediaRenderer render) {
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

    public int getNextConnectionID() {
        lock();
        this.maxConnectionID++;
        unlock();
        return this.maxConnectionID;
    }

    public ConnectionInfoList getConnectionInfoList() {
        return this.conInfoList;
    }

    public ConnectionInfo getConnectionInfo(int id) {
        int size = this.conInfoList.size();
        for (int n = 0; n < size; n++) {
            ConnectionInfo info = this.conInfoList.getConnectionInfo(n);
            if (info.getID() == id) {
                return info;
            }
        }
        return null;
    }

    public void addConnectionInfo(ConnectionInfo info) {
        lock();
        this.conInfoList.add(info);
        unlock();
    }

    public void removeConnectionInfo(int id) {
        lock();
        int size = this.conInfoList.size();
        for (int n = 0; n < size; n++) {
            ConnectionInfo info = this.conInfoList.getConnectionInfo(n);
            if (info.getID() == id) {
                this.conInfoList.remove(info);
                break;
            }
        }
        unlock();
    }

    public void removeConnectionInfo(ConnectionInfo info) {
        lock();
        this.conInfoList.remove(info);
        unlock();
    }

    public boolean actionControlReceived(Action action) {
        String actionName = action.getName();
        if (actionName == null) {
            action.setStatus(UPnPStatus.INVALID_ACTION);
            return false;
        }
        boolean isActionSuccess;
        if ("GetProtocolInfo".equals(actionName)) {
            Debug.message("Process CM actionControlReceived() action: " + actionName);
            action.getArgument("Sink").setValue("http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_PRO,http-get:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_SP_G726,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_FULL,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_MED,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVMED_BASE,http-get:*:audio/L16;rate=44100;channels=1:DLNA.ORG_PN=LPCM,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_PAL,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_NTSC,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVHIGH_PRO,http-get:*:audio/L16;rate=44100;channels=2:DLNA.ORG_PN=LPCM,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_SM,http-get:*:video/x-ms-asf:DLNA.ORG_PN=VC1_ASF_AP_L1_WMA,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMDRM_WMABASE,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVHIGH_FULL,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMAFULL,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMABASE,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPLL_BASE,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_NTSC_XAC3,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMDRM_WMVSPLL_BASE,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPML_BASE,http-get:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_ASP_L5_SO_G726,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_LRG,http-get:*:audio/mpeg:DLNA.ORG_PN=MP3,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG_PS_PAL_XAC3,http-get:*:audio/x-ms-wma:DLNA.ORG_PN=WMAPRO,http-get:*:video/mpeg:DLNA.ORG_PN=MPEG1,http-get:*:image/jpeg:DLNA.ORG_PN=JPEG_TN,http-get:*:video/x-ms-asf:DLNA.ORG_PN=MPEG4_P2_ASF_ASP_L4_SO_G726,http-get:*:audio/L16;rate=48000;channels=2:DLNA.ORG_PN=LPCM,http-get:*:audio/mpeg:DLNA.ORG_PN=MP3X,http-get:*:video/x-ms-wmv:DLNA.ORG_PN=WMVSPML_MP3,http-get:*:video/x-ms-wmv:*,http-get:*:image/png:*,http-get:*:audio/mp3:*,http-get:*:audio/mpeg:*,http-get:*:audio/mpeg3:*,http-get:*:video/mp4:*,http-get:*:video/avi:*");
            action.getArgument("Source").setValue("NOT_IMPLEMENTED");
            isActionSuccess = true;
        } else if ("GetCurrentConnectionIDs".equals(actionName)) {
            Debug.message("Process CM actionControlReceived() action: " + actionName);
            isActionSuccess = getCurrentConnectionIDs(action);
            action.getArgument("ConnectionIDs").setValue("0");
        } else if ("GetCurrentConnectionInfo".equals(actionName)) {
            Debug.message("Process CM actionControlReceived() action: " + actionName);
            isActionSuccess = getCurrentConnectionInfo(action);
            action.getArgument("RcsID").setValue("0");
            action.getArgument("AVTransportID").setValue("0");
            action.getArgument("PeerConnectionManager").setValue("");
            action.getArgument("PeerConnectionID").setValue("-1");
            action.getArgument("Direction").setValue("Input");
            action.getArgument("Status").setValue("Unknown");
        } else {
            Debug.message("Unknown CM actionControlReceived() action: " + actionName);
            isActionSuccess = false;
            action.setStatus(UPnPStatus.INVALID_ACTION);
        }
        MediaRenderer dmr = getMediaRenderer();
        if (dmr != null) {
            ActionListener listener = dmr.getActionListener();
            if (listener != null) {
                listener.actionControlReceived(action);
            }
        }
        return isActionSuccess;
    }

    private boolean getCurrentConnectionIDs(Action action) {
        String conIDs = "";
        lock();
        int size = this.conInfoList.size();
        for (int n = 0; n < size; n++) {
            ConnectionInfo info = this.conInfoList.getConnectionInfo(n);
            if (n > 0) {
                conIDs = new StringBuilder(String.valueOf(conIDs)).append(",").toString();
            }
            conIDs = new StringBuilder(String.valueOf(conIDs)).append(Integer.toString(info.getID())).toString();
        }
        action.getArgument("ConnectionIDs").setValue(conIDs);
        unlock();
        return true;
    }

    private boolean getCurrentConnectionInfo(Action action) {
        int id = action.getArgument("RcsID").getIntegerValue();
        lock();
        ConnectionInfo info = getConnectionInfo(id);
        if (info != null) {
            action.getArgument("RcsID").setValue(info.getRcsID());
            action.getArgument("AVTransportID").setValue(info.getAVTransportID());
            action.getArgument("PeerConnectionManager").setValue(info.getPeerConnectionManager());
            action.getArgument("PeerConnectionID").setValue(info.getPeerConnectionID());
            action.getArgument("Direction").setValue(info.getDirection());
            action.getArgument("Status").setValue(info.getStatus());
        } else {
            action.getArgument("RcsID").setValue(-1);
            action.getArgument("AVTransportID").setValue(-1);
            action.getArgument("PeerConnectionManager").setValue("");
            action.getArgument("PeerConnectionID").setValue(-1);
            action.getArgument("Direction").setValue("Output");
            action.getArgument("Status").setValue("Unknown");
        }
        unlock();
        return true;
    }

    public boolean queryControlReceived(StateVariable stateVar) {
        return false;
    }

    public void initService() {
        setServiceType("urn:schemas-upnp-org:service:ConnectionManager:1");
        setServiceID(ConnectionManagerConstStr.SERVICE_ID);
        setSCPDURL(ConnectionManagerConstStr.SCPDURL);
        setControlURL(ConnectionManagerConstStr.CONTROL_URL);
        setEventSubURL(ConnectionManagerConstStr.EVENTSUB_URL);
        try {
            loadSCPD(ConnectionManagerConstStr.SCPD);
        } catch (InvalidDescriptionException e) {
            Debug.message(e.toString());
        }
    }
}
