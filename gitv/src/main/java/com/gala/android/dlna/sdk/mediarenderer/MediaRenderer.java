package com.gala.android.dlna.sdk.mediarenderer;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.SDKVersion;
import com.gala.android.dlna.sdk.mediarenderer.service.AVTransport;
import com.gala.android.dlna.sdk.mediarenderer.service.ConnectionManager;
import com.gala.android.dlna.sdk.mediarenderer.service.PrivateService;
import com.gala.android.dlna.sdk.mediarenderer.service.RenderingControl;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.PrivateServiceConstStr;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.android.sdk.dlna.keeper.DmrInfor;
import com.gala.android.sdk.dlna.keeper.DmrInforKeeper;
import com.gala.video.webview.utils.WebSDKConstants;
import java.io.File;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.ServiceStateTable;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.util.Debug;

public class MediaRenderer extends Device {
    public static final int DEFAULT_HTTP_PORT = 39620;
    public static final int DEFAULT_HTTP_PORT_EXTRA = 39640;
    public static final String DEVICE_IGALARENDERER_TYPE = new StringBuilder(ST.URN_DEVICE).append(Util.getTag(true)).append("MediaRenderer:1").toString();
    public static final String DEVICE_TYPE = "urn:schemas-upnp-org:device:MediaRenderer:1";
    public static final String DMR_VERSION = "DMR-1.50";
    private static final String notifyStateVar = "A_ARG_TYPE_NOTIFYMSG";
    private ActionListener actionListener;
    private AVTransport avTrans;
    private ConnectionManager conMan;
    private GalaDLNAListener galaDLNAListener = null;
    private LastChangeListener mLastChangeListener = new DMRLastChangeListener();
    private StateVariable notifyStateVariable = null;
    private Service privateServer = null;
    private PrivateService privateService;
    private RenderingControl renCon;
    private StandardDLNAListener standardDLNAListener = null;

    public interface LastChangeListener {
        void lastChange(String str, String str2, Object obj);
    }

    final class DMRLastChangeListener implements LastChangeListener {
        DMRLastChangeListener() {
        }

        public void lastChange(String service, String name, Object value) {
            Debug.message("lastChange() service: " + service + " name: " + name + " value: " + value);
            if (MediaRenderer.this.isAVTransportService(service)) {
                MediaRenderer.this.getAVTransport().getStateVariable(name).setValue((String) value, false);
            } else if (MediaRenderer.this.isConnectionManagerService(service)) {
                MediaRenderer.this.getConnectionManager().getStateVariable(name).setValue((String) value, false);
            } else if (MediaRenderer.this.isRenderingControlService(service)) {
                MediaRenderer.this.getRenderingControl().getStateVariable(name).setValue((String) value, false);
            }
        }
    }

    public MediaRenderer(String descriptionFileName) throws InvalidDescriptionException {
        super(new File(descriptionFileName));
        Debug.message("SDK VERSION: " + SDKVersion.getSDKVersion());
        initialize();
    }

    public MediaRenderer(int major, int minor) {
        super(major, minor);
        clear();
        setDeviceType("urn:schemas-upnp-org:device:MediaRenderer:1");
        setDLNADOC(DMR_VERSION);
    }

    public MediaRenderer() {
        super(1, 0);
        clear();
        setDeviceType("urn:schemas-upnp-org:device:MediaRenderer:1");
        setDLNADOC(DMR_VERSION);
    }

    public void setDmrLogPath(String path) {
        if (path != null && path.length() != 0) {
            DmrInforKeeper.getInstance().setLogPath(path);
        }
    }

    public final LastChangeListener getLastChangeListener() {
        return this.mLastChangeListener;
    }

    public StandardDLNAListener getStandardDLNAListener() {
        return this.standardDLNAListener;
    }

    public void setStandardDLNAListener(StandardDLNAListener standardDLNAListener) {
        this.standardDLNAListener = standardDLNAListener;
    }

    public GalaDLNAListener getGalaDLNAListener() {
        return this.galaDLNAListener;
    }

    public void setGalaDLNAListener(GalaDLNAListener galaDLNAListener) {
        this.galaDLNAListener = galaDLNAListener;
    }

    public void initUUID() {
        DmrInfor dmrInfor = DmrInforKeeper.getInstance().getDmrInfor(getPackageName());
        if (dmrInfor == null || TextUtils.isEmpty(dmrInfor.getUuid())) {
            String uuid = UPnP.createUUID();
            setUUID(uuid);
            dmrInfor = new DmrInfor();
            dmrInfor.setUuid(uuid);
            DmrInforKeeper.getInstance().SaveDmrInfor(dmrInfor, getPackageName());
        } else {
            setUUID(dmrInfor.getUuid());
        }
        Debug.message("initUUID:" + getUUID());
        setUDN(getUUID());
    }

    public void initialize() {
        UPnP.setEnable(9);
        setInterfaceAddress(HostInterface.getHostAddress(0));
        if (getPackageName().length() > 1) {
            setHTTPPort(DEFAULT_HTTP_PORT_EXTRA);
        } else {
            setHTTPPort(DEFAULT_HTTP_PORT);
        }
        initUUID();
        this.renCon = new RenderingControl(this);
        this.conMan = new ConnectionManager(this);
        this.avTrans = AVTransport.getInstance(this);
        this.privateService = new PrivateService(this);
        addService(this.renCon);
        addService(this.conMan);
        addService(this.avTrans);
        addService(this.privateService);
    }

    public ConnectionManager getConnectionManager() {
        return this.conMan;
    }

    public RenderingControl getRenderingControl() {
        return this.renCon;
    }

    public AVTransport getAVTransport() {
        return this.avTrans;
    }

    public PrivateService getPrivateService() {
        return this.privateService;
    }

    public void setInterfaceAddress(String ifaddr) {
        HostInterface.setInterface(ifaddr);
    }

    public String getInterfaceAddress() {
        return HostInterface.getInterface();
    }

    public void setActionListener(ActionListener listener) {
        this.actionListener = listener;
    }

    public ActionListener getActionListener() {
        return this.actionListener;
    }

    public synchronized boolean start() {
        Debug.message("MediaRenderer start SDK VERSION: " + SDKVersion.getSDKVersion());
        return super.start();
    }

    public synchronized boolean stop() {
        Debug.message("MediaRenderer stop SDK VERSION: " + SDKVersion.getSDKVersion());
        if (this.privateService != null) {
            this.privateService.stopNotifyThreads();
        }
        if (this.avTrans != null) {
            this.avTrans.stopNotifyThreads();
        }
        if (this.conMan != null) {
            this.conMan.stopNotifyThreads();
        }
        if (this.renCon != null) {
            this.renCon.stopNotifyThreads();
        }
        return super.stop();
    }

    public void NotifyMessage(String notifyMsg) {
        if (this.notifyStateVariable == null) {
            this.privateServer = getService(PrivateServiceConstStr.SERVICE_TYPE);
            if (this.privateServer != null) {
                ServiceStateTable stateTable = this.privateServer.getServiceStateTable();
                int tableSize = stateTable.size();
                for (int n = 0; n < tableSize; n++) {
                    StateVariable var = stateTable.getStateVariable(n);
                    if (var.getStateVariableNode().getNodeValue(WebSDKConstants.PARAM_KEY_PL_NAME).compareTo("A_ARG_TYPE_NOTIFYMSG") == 0) {
                        this.notifyStateVariable = var;
                        Debug.message("NofityMessage sub send: " + notifyMsg);
                        var.setValue(notifyMsg, false);
                        return;
                    }
                }
                return;
            }
            return;
        }
        this.notifyStateVariable.setValue(notifyMsg, false);
    }

    public void NotifyMessage(String notifyMsg, boolean external) {
        if (this.notifyStateVariable == null) {
            this.privateServer = getService(PrivateServiceConstStr.SERVICE_TYPE);
            if (this.privateServer != null) {
                ServiceStateTable stateTable = this.privateServer.getServiceStateTable();
                int tableSize = stateTable.size();
                for (int n = 0; n < tableSize; n++) {
                    StateVariable var = stateTable.getStateVariable(n);
                    if (var.getStateVariableNode().getNodeValue(WebSDKConstants.PARAM_KEY_PL_NAME).compareTo("A_ARG_TYPE_NOTIFYMSG") == 0) {
                        this.notifyStateVariable = var;
                        Debug.message("NofityMessage sub send: " + notifyMsg);
                        var.setValue(notifyMsg, external);
                        return;
                    }
                }
                return;
            }
            return;
        }
        this.notifyStateVariable.setValue(notifyMsg, external);
    }

    private boolean isAVTransportService(String service) {
        return "urn:schemas-upnp-org:service:AVTransport:1".equals(service);
    }

    private boolean isConnectionManagerService(String service) {
        return "urn:schemas-upnp-org:service:ConnectionManager:1".equals(service);
    }

    private boolean isRenderingControlService(String service) {
        return "urn:schemas-upnp-org:service:RenderingControl:1".equals(service);
    }
}
