package com.gala.android.dlna.sdk.controlpoint;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.DeviceName;
import com.gala.android.dlna.sdk.SDKVersion;
import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.SimpleWebServer;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.PrivateServiceConstStr;
import com.gala.android.dlna.sdk.stddmrcontroller.StdDmrFunctionContentProcessor;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.android.dlna.sdk.stddmrcontroller.data.ActionResult;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.share.common.configs.WebConstants;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.StringTokenizer;
import junit.framework.Assert;
import org.cybergarage.http.HTTPRequest.HostUnknownTimeListener;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Action;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.NETWORK_STATUS;
import org.cybergarage.upnp.NetworkMonitor;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.event.Subscription;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.ParserException;

public class MediaControlPoint extends ControlPoint implements DeviceChangeListener, HostUnknownTimeListener {
    private static final int QIMOHTTPRETRYTIME = 5;
    private static Thread mServerThread;
    private static PipedOutputStream mStdIn = null;
    private String HTTPSTRING = WebConstants.WEB_SITE_BASE_HTTP;
    private final long SUBSCRIBED_TIMEOUT = 180;
    private DeviceChangeListener deviceChangeListener = null;
    private boolean isReceiveNotify = false;
    public long lastResponseTime;
    private EventListener mEventListener = new EventListener() {
        public void eventNotifyReceived(String uuid, long seq, String varName, String value) {
            Debug.message("eventNotifyReceived: " + value);
            if (MediaControlPoint.this.currentControlDevice == null) {
                Debug.message("currentControlDevice is null! Reject eventNotify: " + value);
                return;
            }
            try {
                String currentSubUUID = MediaControlPoint.this.currentControlDevice.getPrivateServer().getSID();
                if (currentSubUUID != null && !uuid.equals(currentSubUUID)) {
                    Debug.message("eventNotifyReceived: " + uuid + " rejected!" + " current sub SID: " + currentSubUUID);
                } else if (varName.compareTo(PrivateServiceConstStr.A_ARG_TYPE_NOTIFYMSG) == 0) {
                    Debug.message("current sub SID: " + currentSubUUID + " receive dmr message:" + value);
                    if (MediaControlPoint.this.mNotifyMessageListener != null) {
                        MediaControlPoint.this.mNotifyMessageListener.onReceiveMessage(value);
                    }
                }
            } catch (Exception e) {
                Debug.message("eventNotifyReceived EXCEPTION: " + e.toString());
            }
        }
    };
    private NotifyMessageListener mNotifyMessageListener = null;
    private int mQimoHttpServerPort = 9090;
    private SearchDeviceByIpListener mSearchDeviceByIpListener;
    private Service privateServer = null;

    public interface SearchDeviceByIpListener {
        public static final int SEARCH_RESULT_ALREADY_EXIST = 3;
        public static final int SEARCH_RESULT_NOT_DONGLE = 1;
        public static final int SEARCH_RESULT_NOT_FOUND = 2;
        public static final int SEARCH_RESULT_SUCCESS = 0;

        void onResult(Device device, int i);
    }

    public MediaControlPoint() {
        Debug.message("SDK VERSION: " + SDKVersion.getSDKVersion());
    }

    public DeviceChangeListener getDeviceChangeListener() {
        return this.deviceChangeListener;
    }

    public NETWORK_STATUS getNetworkStatus() {
        return NetworkMonitor.getInstance().getNetworkStatus();
    }

    public void setDeviceChangeListener(DeviceChangeListener deviceChangeListener) {
        if (deviceChangeListener == null && this.deviceChangeListener != null) {
            removeDeviceChangeListener(this.deviceChangeListener);
            this.deviceChangeListener = deviceChangeListener;
        } else if (deviceChangeListener != null) {
            this.deviceChangeListener = deviceChangeListener;
            addDeviceChangeListener(deviceChangeListener);
        }
    }

    public void setSearchDeviceByIpListener(SearchDeviceByIpListener searchDeviceByIpListener) {
        this.mSearchDeviceByIpListener = searchDeviceByIpListener;
    }

    public void searchDeviceByIp(String localIp, String remoteIp) {
        if (TextUtils.isEmpty(localIp) || TextUtils.isEmpty(remoteIp)) {
            Debug.message("input ip error: localIP = " + localIp + " remoteIP = " + remoteIp);
            onSearchByIpResult(null, 2);
        }
        String location = new StringBuilder(WebConstants.WEB_SITE_BASE_HTTP).append(remoteIp).append(":39620/description.xml").toString();
        URL locationUrl = null;
        try {
            locationUrl = new URL(location);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Node rootNode = null;
        int i = 3;
        while (locationUrl != null && rootNode == null && i > 0) {
            try {
                rootNode = UPnP.getXMLParser().parse(locationUrl);
            } catch (ParserException ex) {
                ex.printStackTrace();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            i--;
        }
        int searchResult = 2;
        Device device = null;
        if (rootNode != null) {
            Node deviceNode = rootNode.getNode(Device.ELEM_NAME);
            if (deviceNode != null) {
                Node modelNode = deviceNode.getNode(Device.MODEL_DESCRIPTION);
                String modelDescription = modelNode != null ? modelNode.getValue() : "";
                if (TextUtils.isEmpty(modelDescription) || !modelDescription.contains("TVGUO")) {
                    searchResult = 1;
                } else {
                    device = new Device(deviceNode);
                    if (getDevice(device.getUDN()) == null) {
                        device.setIpAddress(remoteIp);
                        device.setLocation(location);
                        device.setTimeStamp(System.currentTimeMillis());
                        device.setInterfaceAddress(localIp);
                        device.setDeviceName(DeviceName.IGALA_DONGLE);
                        addDeviceByType(device, rootNode);
                        searchResult = 0;
                    } else {
                        searchResult = 3;
                    }
                }
            }
        }
        onSearchByIpResult(device, searchResult);
    }

    private void onSearchByIpResult(Device device, int searchResult) {
        if (this.mSearchDeviceByIpListener != null) {
            this.mSearchDeviceByIpListener.onResult(device, searchResult);
        }
    }

    public void deviceAdded(Device dev) {
        if (this.deviceChangeListener != null) {
            this.deviceChangeListener.deviceAdded(dev);
        }
    }

    public void deviceRemoved(Device dev) {
        if (this.deviceChangeListener != null) {
            this.deviceChangeListener.deviceRemoved(dev);
        }
    }

    public void deviceUpdated(Device dev) {
        if (this.deviceChangeListener != null) {
            this.deviceChangeListener.deviceUpdated(dev);
        }
    }

    public Device getCurrentDevice() {
        return this.currentControlDevice;
    }

    public String getCurrentControlDeviceAddress() {
        String addr = null;
        try {
            if (this.currentControlDevice != null) {
                addr = this.currentControlDevice.getIpAddress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addr;
    }

    public void setOpenRealTimeFunction(boolean isOpen) {
        Debug.message("ERROR！！！！！！！！！！！！！！！！！！！not use setOpenRealTimeFunction: isOpen = " + isOpen);
        isOpenRealTime = isOpen;
    }

    public void setMaxDelayTolerateTime(long maxTimes) {
        Debug.message("ERROR！！！！！！！！！！！！！！！！！！！not use setMaxDelayTolerateTime: maxTimes = " + maxTimes);
        if (maxTimes < 10) {
            maxTimes = 10;
        }
        maxDelayTime = maxTimes;
    }

    public void NotifyDmcSleep(boolean isSleep) {
        Debug.message("NotifyDmcSleep: isSleep = " + isSleep);
        this.isAppSleep = isSleep;
    }

    public synchronized void setCurrentDevice(Device currentDevice, boolean isReceiveMsg) {
        Debug.message("online setCurrentDevice()");
        if (currentDevice != null) {
            if (this.currentControlDevice != null) {
                if (this.currentControlDevice.getUUID().equals(currentDevice.getUUID())) {
                    Debug.message("currentControlDevice == currentDevice");
                } else {
                    unsubscribe(this.currentControlDevice);
                    this.currentControlDevice.clearSendMessageAction();
                }
            }
            this.currentControlDevice = currentDevice;
            this.currentControlDevice.setHostUnknownTimeListener(this);
            if (this.currentControlDevice.getIsSuperQuicklySend()) {
                Debug.message("online setCurrentDevice() p2");
                this.currentControlDevice.beforeHandConnectHost();
            }
            setIsRecevieNotifyMessage(isReceiveMsg);
        } else if (this.currentControlDevice != null) {
            unsubscribe(this.currentControlDevice);
            this.currentControlDevice.clearSendMessageAction();
            this.currentControlDevice = null;
        }
    }

    public void SetSendMessageForLongAsKeepLive(boolean isKeepAlive) {
        this.mLongforKeepAlive = isKeepAlive;
    }

    public long getLastResponseTime() {
        return this.lastResponseTime;
    }

    public synchronized String sendMessage(String infor, boolean isNeedReply) {
        String result;
        Debug.message("do sendMesage [" + infor + AlbumEnterFactory.SIGN_STR);
        try {
            if (this.currentControlDevice == null) {
                Debug.message("Warning! currentControlDevice == null");
            } else if (StdDmrFunctionContentProcessor.isStdDmrCommand(infor)) {
                ActionResult result2;
                if (Util.isStdDmrDevice(this.currentControlDevice)) {
                    result2 = StdDmrFunctionContentProcessor.processFunctionContent(this.currentControlDevice, infor);
                } else {
                    result2 = new ActionResult(false, "", RESULT_DESCRIPTION.FAIL_NO_TARGET_DEVICE);
                }
                result = result2.toString();
            } else {
                boolean iskeepAlive = this.mLongforKeepAlive;
                Action sendMessageAction = this.currentControlDevice.getSendMessageAction(iskeepAlive);
                if (sendMessageAction != null) {
                    sendMessageAction.setKeepAlive(iskeepAlive);
                    sendMessageAction.setArgumentValue("InstanceID", "0");
                    sendMessageAction.setArgumentValue(PrivateServiceConstStr.INFOR, infor);
                    if (!isNeedReply) {
                        result = sendMessageAction.postControlActionNoReply() ? "" : sendMessageAction.postControlActionNoReply() ? "" : null;
                    } else if (sendMessageAction.postControlAction()) {
                        result = sendMessageAction.getArgumentValue("Result");
                        this.lastResponseTime = System.currentTimeMillis();
                    } else {
                        if (sendMessageAction.getStatus().getCode() == 0) {
                            Debug.message("sendMesage [" + infor + "] fail,retry...");
                            if (sendMessageAction.postControlAction()) {
                                result = sendMessageAction.getArgumentValue("Result");
                                this.lastResponseTime = System.currentTimeMillis();
                            } else {
                                Debug.message("sendMesage retry failed.");
                            }
                        }
                        Debug.message("sendMessage [" + infor + "] fail, remove currentControlDevice...");
                        removeDevice(getDevice(this.currentControlDevice.getRootNode()));
                        this.currentControlDevice = null;
                        result = null;
                    }
                } else {
                    Debug.message("sendMesage error:sendMessageAction is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result = null;
        return result;
    }

    public boolean sendMessage(byte data) {
        try {
            if (this.currentControlDevice != null) {
                String mydata = getConstructionData(data);
                boolean udpsucce = this.currentControlDevice.quicklySendUDPMessage(mydata);
                return this.currentControlDevice.quicklySendTCPMessage(mydata);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendUDPMessage(byte data) {
        try {
            if (this.currentControlDevice != null) {
                return this.currentControlDevice.quicklySendUDPMessage(getConstructionData(data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendMessageBySingle(byte data) {
        try {
            if (this.currentControlDevice != null) {
                return this.currentControlDevice.quicklySendMessage(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String sendMessage(String infor, Byte data, boolean isNeedReply) {
        try {
            if (this.currentControlDevice == null) {
                return null;
            }
            if (!this.currentControlDevice.getIsSuperQuicklySend()) {
                return sendMessage(infor, isNeedReply);
            }
            if (data == null) {
                return sendMessage(infor, isNeedReply);
            }
            if (sendMessage(data.byteValue())) {
                return "";
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean unsubscribePrivateService() {
        try {
            if (this.currentControlDevice != null) {
                this.privateServer = this.currentControlDevice.getPrivateServer();
                if (this.privateServer != null) {
                    if (!isSubscribed(this.privateServer)) {
                        return true;
                    }
                    if (unsubscribe(this.privateServer)) {
                        Debug.message("currentdev uuid: " + this.currentControlDevice.getUUID() + " unsubscribePrivateService OK");
                        return true;
                    }
                    Debug.message("currentdev uuid: " + this.currentControlDevice.getUUID() + " unsubscribePrivateService failed");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unsubscribePrivateService(String uuid) {
        Device dev = getDevice(new StringBuilder(Subscription.UUID).append(uuid).toString());
        if (dev != null) {
            try {
                this.privateServer = dev.getPrivateServer();
                if (this.privateServer != null) {
                    if (!isSubscribed(this.privateServer)) {
                        return true;
                    }
                    Debug.message("unsub currentDev SID: " + this.privateServer.getSID());
                    if (unsubscribe(this.privateServer)) {
                        Debug.message("dev uuid: " + uuid + " unsubscribePrivateService OK");
                        return true;
                    }
                    Debug.message("dev uuid: " + uuid + " unsubscribePrivateService failed");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean subscribePrivateService() {
        try {
            if (this.currentControlDevice == null) {
                return false;
            }
            this.privateServer = this.currentControlDevice.getPrivateServer();
            if (this.privateServer == null) {
                return false;
            }
            if (isSubscribed(this.privateServer)) {
                Debug.message("currentDev uuid: " + this.currentControlDevice.getUUID() + "already subscribePrivateService. Current SID: " + this.privateServer.getSID());
                return true;
            } else if (subscribe(this.privateServer, 180)) {
                Debug.message("currentDev uuid: " + this.currentControlDevice.getUUID() + " subscribePrivateService OK SID: " + this.privateServer.getSID());
                return true;
            } else {
                Debug.message("currentDev uuid: " + this.currentControlDevice.getUUID() + " subscribePrivateService receive DMR message failed");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean setIsRecevieNotifyMessage(boolean isRecevied) {
        Debug.message("setIsRecevieNotifyMessage: isRecevied = " + isRecevied);
        this.isReceiveNotify = isRecevied;
        if (this.isReceiveNotify) {
            if (this.currentControlDevice == null) {
                return false;
            }
            this.privateServer = this.currentControlDevice.getPrivateServer();
            if (this.privateServer == null) {
                return false;
            }
            if (isSubscribed(this.privateServer)) {
                return true;
            }
            if (subscribe(this.privateServer, 180)) {
                return true;
            }
            Debug.message("DMC set receive dmr message failure");
            return false;
        } else if (this.currentControlDevice == null) {
            return false;
        } else {
            this.privateServer = this.currentControlDevice.getPrivateServer();
            if (this.privateServer == null) {
                return false;
            }
            if (!isSubscribed(this.privateServer)) {
                return true;
            }
            if (unsubscribe(this.privateServer)) {
                return true;
            }
            Debug.message("DMC set no receive dmr message");
            return false;
        }
    }

    public void setReceiveNotifyMessageListener(NotifyMessageListener notifyMessageListener) {
        if (notifyMessageListener == null) {
            removeEventListener(this.mEventListener);
            this.mNotifyMessageListener = null;
            return;
        }
        this.mNotifyMessageListener = notifyMessageListener;
        addEventListener(this.mEventListener);
    }

    public void hostUnknownTimes(int times) {
        if (times > 3) {
            Debug.message("hostUnknownTimes() : " + times);
        }
    }

    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Debug.message(ex.toString());
        }
        return null;
    }

    private boolean QimoHttpServerPort(int port) {
        String addr = getLocalIpAddress();
        if (addr == null || addr.length() < 1) {
            return false;
        }
        Debug.message("++++QimoHttpServerPort addr: " + addr);
        try {
            ServerSocket serverSock = new ServerSocket(port, 0, InetAddress.getByName(addr));
            try {
                serverSock.close();
                return true;
            } catch (Exception e) {
                Debug.warning(e);
                ServerSocket serverSocket = serverSock;
                return false;
            }
        } catch (Exception e2) {
            Debug.warning(e2);
            return false;
        }
    }

    public boolean StartQimoWebServer() {
        int retryCnt = 0;
        int qimoPort = this.mQimoHttpServerPort;
        boolean ret = QimoHttpServerPort(this.mQimoHttpServerPort);
        while (!ret) {
            retryCnt++;
            if (5 < retryCnt) {
                return false;
            }
            qimoPort = this.mQimoHttpServerPort + 1;
            ret = QimoHttpServerPort(qimoPort);
            Debug.message("++++StartQimoWebServer try port: " + qimoPort + " ret: " + ret);
        }
        this.mQimoHttpServerPort = qimoPort;
        Debug.message("++++StartQimoWebServer port: " + this.mQimoHttpServerPort);
        if (this.mQimoHttpServerPort == -1) {
            Debug.message("++++StartQimoWebServer failed");
            return false;
        }
        mStdIn = new PipedOutputStream();
        try {
            System.setIn(new PipedInputStream(mStdIn));
            mServerThread = new Thread(new Runnable() {
                public void run() {
                    SimpleWebServer.main(new String[]{"--port", String.valueOf(MediaControlPoint.this.mQimoHttpServerPort), "--dir", "/"});
                }
            });
            mServerThread.start();
            Thread.sleep(100);
            Debug.message("----StartQimoWebServer");
            return true;
        } catch (Exception e) {
            Debug.message("----Exception in StartQimoWebServer");
            e.printStackTrace();
            return false;
        }
    }

    public boolean StopQimoWebServer() {
        Debug.message("++++StopQimoWebServer");
        try {
            if (mStdIn != null) {
                mStdIn.write("\n\n".getBytes());
                mServerThread.join(2000);
                Assert.assertFalse(mServerThread.isAlive());
            }
            Debug.message("----StopQimoWebServer");
            return true;
        } catch (Exception e) {
            Debug.message("----Exception in StopQimoWebServer");
            e.printStackTrace();
            return false;
        }
    }

    public String encodeURL(String uri) {
        String newUri = "";
        StringTokenizer st = new StringTokenizer(uri, "/ ", true);
        while (st.hasMoreTokens()) {
            String tok = st.nextToken();
            if (tok.equals("/")) {
                newUri = new StringBuilder(String.valueOf(newUri)).append("/").toString();
            } else if (tok.equals(" ")) {
                newUri = new StringBuilder(String.valueOf(newUri)).append("%20").toString();
            } else {
                try {
                    newUri = new StringBuilder(String.valueOf(newUri)).append(URLEncoder.encode(tok, "UTF-8")).toString();
                } catch (UnsupportedEncodingException e) {
                    Debug.message("++++encodeURL UnsupportedEncodingException");
                }
            }
        }
        return newUri;
    }

    public String GetQimoFileAddress(String LocalWIFIIP, String FilePath) {
        String Port = String.valueOf(this.mQimoHttpServerPort);
        StringBuffer mQimoFileAddress = new StringBuffer();
        if (FilePath != null) {
            mQimoFileAddress = mQimoFileAddress.append(this.HTTPSTRING + LocalWIFIIP + SOAP.DELIM + Port + encodeURL(FilePath));
        }
        Debug.message("++++GetQimoFileAddress" + mQimoFileAddress.toString());
        return mQimoFileAddress.toString();
    }

    public String GetQimoFileAddress(String FilePath) {
        String Port = String.valueOf(this.mQimoHttpServerPort);
        StringBuffer mQimoFileAddress = new StringBuffer();
        String addr = getLocalIpAddress();
        if (FilePath != null) {
            mQimoFileAddress = mQimoFileAddress.append(this.HTTPSTRING + addr + SOAP.DELIM + Port + encodeURL(FilePath));
        }
        Debug.message("++++GetQimoFileAddress" + mQimoFileAddress.toString());
        return mQimoFileAddress.toString();
    }

    public boolean start() {
        setSubscriberTimeout(180);
        return super.start();
    }

    public boolean stop() {
        return super.stop();
    }
}
