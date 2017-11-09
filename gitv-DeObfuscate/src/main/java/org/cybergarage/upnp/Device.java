package org.cybergarage.upnp;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.DeviceName;
import com.gala.android.dlna.sdk.dlnahttpserver.GalaHttpServer;
import com.gala.android.dlna.sdk.dlnahttpserver.GalaHttpServerList;
import com.gala.android.dlna.sdk.dlnahttpserver.GalaUDPHttpServer;
import com.gala.android.dlna.sdk.mediarenderer.ControlPointConnectRendererListener;
import com.gala.android.dlna.sdk.mediarenderer.QuicklySendMessageListener;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.AVTransportConstStr;
import com.gala.android.dlna.sdk.mediarenderer.service.infor.PrivateServiceConstStr;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.URL;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequest.HostUnknownTimeListener;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.net.HostInterface;
import org.cybergarage.soap.SOAP;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.control.ActionListener;
import org.cybergarage.upnp.control.ActionRequest;
import org.cybergarage.upnp.control.ActionResponse;
import org.cybergarage.upnp.control.ControlRequest;
import org.cybergarage.upnp.control.ControlResponse;
import org.cybergarage.upnp.control.QueryListener;
import org.cybergarage.upnp.control.QueryRequest;
import org.cybergarage.upnp.device.Advertiser;
import org.cybergarage.upnp.device.Description;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.device.NTS;
import org.cybergarage.upnp.device.ST;
import org.cybergarage.upnp.device.SearchListener;
import org.cybergarage.upnp.event.Subscriber;
import org.cybergarage.upnp.event.Subscription;
import org.cybergarage.upnp.event.SubscriptionRequest;
import org.cybergarage.upnp.event.SubscriptionResponse;
import org.cybergarage.upnp.ssdp.SSDP;
import org.cybergarage.upnp.ssdp.SSDPNotifyRequest;
import org.cybergarage.upnp.ssdp.SSDPNotifySocket;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchResponse;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocket;
import org.cybergarage.upnp.ssdp.SSDPSearchSocketList;
import org.cybergarage.upnp.xml.DeviceData;
import org.cybergarage.util.Debug;
import org.cybergarage.util.FileUtil;
import org.cybergarage.util.MD5Util;
import org.cybergarage.util.Mutex;
import org.cybergarage.util.TimerUtil;
import org.cybergarage.xml.Node;

public class Device implements HTTPRequestListener, SearchListener {
    public static final String DEFAULT_DESCRIPTION_URI = "/description.xml";
    public static final int DEFAULT_DISCOVERY_WAIT_TIME = 100;
    public static final int DEFAULT_EXPIRED_DEVICE_TIME = 10;
    public static final int DEFAULT_LEASE_TIME = 30;
    public static final int DEFAULT_STARTUP_WAIT_TIME = 1000;
    private static final String DEVICE_TYPE = "deviceType";
    private static final String DLNA_DOC = "dlna:X_DLNADOC";
    public static final int DLNA_SEARCH_LEASE_TIME = 1800;
    public static final String ELEM_NAME = "device";
    private static final String EXTERNAL_VALUE = "external";
    private static final String FRIENDLY_NAME = "friendlyName";
    public static final int HTTP_DEFAULT_PORT = 4004;
    public static final String IGALA_DEVICE = "IGALA";
    private static final String MANUFACTURE = "manufacturer";
    private static final String MANUFACTURER = "manufacturer";
    private static final String MANUFACTURE_URL = "manufacturerURL";
    public static final String MODEL_DESCRIPTION = "modelDescription";
    private static final String MODEL_NAME = "modelName";
    private static final String MODEL_NUMBER = "modelNumber";
    private static final String MODEL_URL = "modelURL";
    private static final String PACKAGE_NAME = "PackageName";
    private static final String SERIAL_NUMBER = "serialNumber";
    private static final String UDN = "UDN";
    private static final String UPC = "UPC";
    public static final String UPNP_ROOTDEVICE = "upnp:rootdevice";
    private static final String URLBASE_NAME = "URLBase";
    private static Calendar cal = Calendar.getInstance();
    private static final String presentationURL = "presentationURL";
    private final String DEVICE_IGALADONGLERENDERER_TYPE;
    private Action GetPositionInfoAction;
    private Action GetTransportInfoAction;
    public final String UUID;
    public int bindssdpPort;
    private Map<String, byte[]> cacheMap;
    private ControlPointConnectRendererListener controlPointConnectRendererListener;
    private String descriptionXmlContent;
    private String descriptionXmlMd5;
    private String devUUID;
    private DeviceConnectStatusListener deviceConnectStatusListener;
    private Node deviceNode;
    private final CopyOnWriteArrayList<SSDPPacket> dmcAddrList;
    private int galaTCPPort;
    private GalaUDPHttpServer galaUDPHttpServer;
    private int galaUDPPort;
    private volatile boolean isRefreshIpList;
    private Boolean isSupperKeepAlive;
    private String mBroadcastDescriptionLocation;
    private String mBroadcastFriendlyName;
    private SSDPNotifyRequest mBroadcastPacket;
    private String mBroadcastPacketMessage;
    private int mDeviceName;
    private int mDeviceVersion;
    private HostUnknownTimeListener mHostUnknownTimeListener;
    private String mIconPath;
    private String mInterfaceAddress;
    private String mIpAddress;
    private String mMulticastDescriptionLocation;
    private String mMulticastFriendlyName;
    private SSDPNotifyRequest mMulticastPacket;
    private String mMulticastPacketMessage;
    private SSDPSearchResponse mResponPacket;
    private String mResponPacketMessage;
    private String mResponseDescriptionLocation;
    private String mResponseFriendlyName;
    private int mSdkVersion;
    private boolean mSdkVersionChanged;
    private ServiceList mServiceList;
    private SSDPPacket mSsdpPacket;
    private long mTimeStamp;
    private Mutex mutex;
    private Service privateServer;
    private HTTPRequest quicklyHttpRequest;
    private boolean quicklySend;
    private QuicklySendMessageListener quicklySendMessageListener;
    private Node rootNode;
    private Action sendMessageAction;
    private Node specVersionNode;
    private Object userData;
    private boolean wirelessMode;

    public boolean isQuicklySend() {
        return this.quicklySend;
    }

    public void setQuicklySend(boolean quicklySend) {
        this.quicklySend = quicklySend;
    }

    public void setIconPath(String iconPath) {
        this.mIconPath = iconPath;
    }

    public QuicklySendMessageListener getQuicklySendMessageListener() {
        return this.quicklySendMessageListener;
    }

    public void setQuicklySendMessageListener(QuicklySendMessageListener quicklySendMessageListener) {
        this.quicklySendMessageListener = quicklySendMessageListener;
    }

    public ControlPointConnectRendererListener getControlPointConnectRendererListener() {
        return this.controlPointConnectRendererListener;
    }

    public void setControlPointConnectRendererListener(ControlPointConnectRendererListener controlPointConnectRendererListener) {
        this.controlPointConnectRendererListener = controlPointConnectRendererListener;
    }

    public boolean getDeviceIsSupperKeepAlive() {
        if (this.isSupperKeepAlive == null) {
            if (getSSDPPacket() != null) {
                this.isSupperKeepAlive = Boolean.valueOf(getSSDPPacket().isSupperConnectKeepAlive());
            } else {
                this.isSupperKeepAlive = Boolean.valueOf(false);
            }
        }
        return this.isSupperKeepAlive.booleanValue();
    }

    public synchronized void clearSendMessageAction() {
        if (this.sendMessageAction != null) {
            this.sendMessageAction.getActionRequest().closeHostSocket();
            this.sendMessageAction = null;
        }
        if (this.quicklyHttpRequest != null) {
            this.quicklyHttpRequest.closeHostQuickly();
            this.quicklyHttpRequest = null;
        }
    }

    public synchronized Action getSendMessageAction(boolean iskeepalive) {
        Action action;
        Service privateService;
        if (iskeepalive) {
            if (this.sendMessageAction == null) {
                privateService = getPrivateServer();
                if (privateService != null) {
                    this.sendMessageAction = privateService.getAction(PrivateServiceConstStr.SEND_MESSAGE);
                }
            }
            action = this.sendMessageAction;
        } else {
            privateService = getPrivateServer();
            action = null;
            if (privateService != null) {
                action = privateService.getAction(PrivateServiceConstStr.SEND_MESSAGE);
            }
        }
        return action;
    }

    public synchronized void clearDLNAAction() {
        this.GetPositionInfoAction = null;
        this.GetTransportInfoAction = null;
    }

    public synchronized Action getGetTransportInfoAction() {
        if (this.GetTransportInfoAction == null) {
            Service AVTransport = getService(AVTransportConstStr.SERVICE_ID);
            if (AVTransport != null) {
                this.GetTransportInfoAction = AVTransport.getAction(AVTransportConstStr.GETTRANSPORTINFO);
            }
        }
        return this.GetTransportInfoAction;
    }

    public Action getGetPositionInfoAction() {
        if (this.GetPositionInfoAction == null) {
            Service AVTransport = getService(AVTransportConstStr.SERVICE_ID);
            if (AVTransport != null) {
                this.GetPositionInfoAction = AVTransport.getAction(AVTransportConstStr.GETPOSITIONINFO);
            }
        }
        return this.GetPositionInfoAction;
    }

    private HTTPRequest getQuicklyHttpRequest() {
        if (this.quicklyHttpRequest == null) {
            this.quicklyHttpRequest = new HTTPRequest();
        }
        return this.quicklyHttpRequest;
    }

    public boolean quicklySendTCPMessage(String data) {
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (httpRequest == null) {
            return false;
        }
        int port = getGalaHTTPPortFromSSDP();
        if (!(this.galaTCPPort == 0 || port == this.galaTCPPort)) {
            httpRequest.closeHostQuickly();
            httpRequest = getQuicklyHttpRequest();
            Debug.message("port change!!!");
        }
        if (port == 0) {
            return false;
        }
        String host = getGalaHostFromSSDP();
        this.galaTCPPort = port;
        return httpRequest.quicklyTCPPost(host, port, data);
    }

    public void setHostUnknownTimeListener(HostUnknownTimeListener listener) {
        this.mHostUnknownTimeListener = listener;
    }

    public void setServerIP(String serverIP) {
        HostInterface.setServerIp(serverIP);
    }

    public boolean quicklySendUDPMessage(String data) {
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (this.mHostUnknownTimeListener != null) {
            httpRequest.setHostUnknownTimeListener(this.mHostUnknownTimeListener);
        }
        if (httpRequest == null) {
            return false;
        }
        int port = getGalaUDPHTTPPortFromSSDP();
        if (!(this.galaUDPPort == 0 || this.galaUDPPort == port)) {
            httpRequest.closeHostQuickly();
            httpRequest = getQuicklyHttpRequest();
            Debug.message("port change!!!");
        }
        if (port == 0) {
            return false;
        }
        String host = getGalaHostFromSSDP();
        this.galaUDPPort = port;
        return httpRequest.quicklyUDPPost(host, port, data);
    }

    public boolean quicklySendMessage(byte data) {
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (httpRequest == null) {
            return false;
        }
        int port = getGalaHTTPPortFromSSDP();
        if (port == 0) {
            return false;
        }
        String host = getGalaHostFromSSDP();
        Debug.message("++++++++quicklySendMessage host " + host + "port " + port);
        return httpRequest.quicklyPost(host, port, data);
    }

    public void beforeHandConnectHost() {
        Debug.message("online beforeHandConnectHost() ");
        if (this.quicklyHttpRequest != null) {
            this.quicklyHttpRequest.closeHostQuickly();
            this.quicklyHttpRequest = null;
        }
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (httpRequest != null) {
            Debug.message("online beforeHandConnectHost() p1 ");
            int port = getGalaHTTPPortFromSSDP();
            if (port != 0) {
                String host = getGalaHostFromSSDP();
                try {
                    URL locationUrl = new URL(getLocation());
                    getSendMessageAction(true).getActionRequest().connectHost(locationUrl.getHost(), locationUrl.getPort(), true);
                    Debug.message("online beforeHandConnectHost() p2 ");
                    httpRequest.connectHostQuickly(host, port);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reconnectQuicklyHost() {
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (httpRequest != null) {
            Debug.message("online reconnectQuicklyHost() p1 ");
            int port = getGalaHTTPPortFromSSDP();
            if (port != 0) {
                String host = getGalaHostFromSSDP();
                try {
                    httpRequest.closeHostQuickly();
                    Debug.message("online reconnectQuicklyHost() p2 ");
                    httpRequest.reconnectHostQuickly(host, port, null);
                } catch (IOException e) {
                }
            }
        }
    }

    public void sendDataToHostTokeepAlive(String data) {
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (httpRequest != null) {
            Debug.message("online sendUrgentData p1 ");
            int port = getGalaHTTPPortFromSSDP();
            if (port != 0) {
                String host = getGalaHostFromSSDP();
                if (host.length() > 0) {
                    try {
                        Debug.message("online sendUrgentData p2 data: " + data);
                        httpRequest.reconnectHostQuickly(host, port, data);
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public void closeConnectHost() {
        HTTPRequest httpRequest = getQuicklyHttpRequest();
        if (httpRequest != null) {
            httpRequest.closeHostQuickly();
        }
    }

    public Service getPrivateServer() {
        if (this.privateServer == null) {
            this.privateServer = getService(PrivateServiceConstStr.SERVICE_ID);
        }
        return this.privateServer;
    }

    public DeviceConnectStatusListener getDeviceConnectStatusListener() {
        return this.deviceConnectStatusListener;
    }

    public void setDeviceConnectStatusListener(DeviceConnectStatusListener deviceConnectStatusListener) {
        this.deviceConnectStatusListener = deviceConnectStatusListener;
    }

    public String getDescriptionXmlContent() {
        return this.descriptionXmlContent;
    }

    public void setDescriptionXmlContent(String descriptionXmlContent) {
        this.descriptionXmlContent = descriptionXmlContent;
    }

    public String getDescriptionXmlMd5() {
        return this.descriptionXmlMd5;
    }

    public void setDescriptionXmlMd5(String descriptionXmlMd5) {
        this.descriptionXmlMd5 = descriptionXmlMd5;
    }

    public synchronized String getDescriptionXml() {
        String str;
        String content = getDescriptionXmlContent();
        if (TextUtils.isEmpty(content) || !content.contains(PrivateServiceConstStr.SERVICE_NAME)) {
            Node rootNode = getRootNode();
            if (rootNode == null) {
                str = "";
            } else {
                rootNode.setNameSpace(RootDescription.ROOT_ELEMENT_NAMESPACE);
                rootNode.setNameSpace(RootDescription.ROOT_ELEMENT_NSDLNA, RootDescription.ROOT_ELEMENT_DLNANAMESPACE);
                setDescriptionXmlContent(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new String())).append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").toString())).append("\n").toString())).append(rootNode.toString()).toString());
            }
        }
        str = getDescriptionXmlContent();
        return str;
    }

    public Node getSpecVersionNode() {
        return this.specVersionNode;
    }

    public void setSpecVersionNode(Node specVersionNode) {
        this.specVersionNode = specVersionNode;
    }

    public Node getRootNode() {
        if (this.rootNode != null) {
            return this.rootNode;
        }
        if (this.deviceNode == null) {
            return null;
        }
        return this.deviceNode.getRootNode();
    }

    public Node getDeviceNode() {
        return this.deviceNode;
    }

    public void setRootNode(Node node) {
        this.rootNode = node;
    }

    public void setDeviceNode(Node node) {
        this.deviceNode = node;
    }

    static {
        UPnP.initialize();
    }

    public Device(Node root, Node device) {
        this.bindssdpPort = 16968;
        this.isRefreshIpList = true;
        this.cacheMap = new HashMap();
        this.mDeviceName = DeviceName.IGALA_BOX;
        this.mSdkVersionChanged = false;
        this.mSdkVersion = 3;
        this.mDeviceVersion = DeviceName.IGALA_DONGLE_V1;
        this.UUID = Subscription.UUID;
        this.DEVICE_IGALADONGLERENDERER_TYPE = "IGALADongleMediaRenderer";
        this.quicklySend = false;
        this.quicklySendMessageListener = null;
        this.controlPointConnectRendererListener = null;
        this.privateServer = null;
        this.sendMessageAction = null;
        this.isSupperKeepAlive = null;
        this.GetPositionInfoAction = null;
        this.GetTransportInfoAction = null;
        this.quicklyHttpRequest = null;
        this.galaTCPPort = 0;
        this.mHostUnknownTimeListener = null;
        this.galaUDPPort = 0;
        this.deviceConnectStatusListener = null;
        this.descriptionXmlContent = "";
        this.descriptionXmlMd5 = "";
        this.galaUDPHttpServer = null;
        this.mutex = new Mutex();
        this.mTimeStamp = 0;
        this.dmcAddrList = new CopyOnWriteArrayList();
        this.mInterfaceAddress = "";
        this.userData = null;
        this.rootNode = root;
        this.deviceNode = device;
        setWirelessMode(true);
    }

    public Device() {
        this.bindssdpPort = 16968;
        this.isRefreshIpList = true;
        this.cacheMap = new HashMap();
        this.mDeviceName = DeviceName.IGALA_BOX;
        this.mSdkVersionChanged = false;
        this.mSdkVersion = 3;
        this.mDeviceVersion = DeviceName.IGALA_DONGLE_V1;
        this.UUID = Subscription.UUID;
        this.DEVICE_IGALADONGLERENDERER_TYPE = "IGALADongleMediaRenderer";
        this.quicklySend = false;
        this.quicklySendMessageListener = null;
        this.controlPointConnectRendererListener = null;
        this.privateServer = null;
        this.sendMessageAction = null;
        this.isSupperKeepAlive = null;
        this.GetPositionInfoAction = null;
        this.GetTransportInfoAction = null;
        this.quicklyHttpRequest = null;
        this.galaTCPPort = 0;
        this.mHostUnknownTimeListener = null;
        this.galaUDPPort = 0;
        this.deviceConnectStatusListener = null;
        this.descriptionXmlContent = "";
        this.descriptionXmlMd5 = "";
        this.galaUDPHttpServer = null;
        this.mutex = new Mutex();
        this.mTimeStamp = 0;
        this.dmcAddrList = new CopyOnWriteArrayList();
        this.mInterfaceAddress = "";
        this.userData = null;
        initDevice(1, 0);
    }

    public Device(int major, int minor) {
        this.bindssdpPort = 16968;
        this.isRefreshIpList = true;
        this.cacheMap = new HashMap();
        this.mDeviceName = DeviceName.IGALA_BOX;
        this.mSdkVersionChanged = false;
        this.mSdkVersion = 3;
        this.mDeviceVersion = DeviceName.IGALA_DONGLE_V1;
        this.UUID = Subscription.UUID;
        this.DEVICE_IGALADONGLERENDERER_TYPE = "IGALADongleMediaRenderer";
        this.quicklySend = false;
        this.quicklySendMessageListener = null;
        this.controlPointConnectRendererListener = null;
        this.privateServer = null;
        this.sendMessageAction = null;
        this.isSupperKeepAlive = null;
        this.GetPositionInfoAction = null;
        this.GetTransportInfoAction = null;
        this.quicklyHttpRequest = null;
        this.galaTCPPort = 0;
        this.mHostUnknownTimeListener = null;
        this.galaUDPPort = 0;
        this.deviceConnectStatusListener = null;
        this.descriptionXmlContent = "";
        this.descriptionXmlMd5 = "";
        this.galaUDPHttpServer = null;
        this.mutex = new Mutex();
        this.mTimeStamp = 0;
        this.dmcAddrList = new CopyOnWriteArrayList();
        this.mInterfaceAddress = "";
        this.userData = null;
        initDevice(major, minor);
    }

    public Device(Node device) {
        this(null, device);
    }

    public Device(File descriptionFile) throws InvalidDescriptionException {
        this(null, null);
        loadDescription(descriptionFile);
    }

    public Device(InputStream input) throws InvalidDescriptionException {
        this(null, null);
        loadDescription(input);
    }

    public Device(String descriptionFileName) throws InvalidDescriptionException {
        this(new File(descriptionFileName));
    }

    public void initDevice(int major, int minor) {
        this.rootNode = new Node(RootDescription.ROOT_ELEMENT);
        this.rootNode.setNameSpace(RootDescription.ROOT_ELEMENT_NAMESPACE);
        this.rootNode.setNameSpace(RootDescription.ROOT_ELEMENT_NSDLNA, RootDescription.ROOT_ELEMENT_DLNANAMESPACE);
        this.specVersionNode = new Node("specVersion");
        Node majorNode = new Node("major");
        majorNode.setValue(major);
        this.specVersionNode.addNode(majorNode);
        Node minorNode = new Node("minor");
        minorNode.setValue(minor);
        this.specVersionNode.addNode(minorNode);
        this.rootNode.addNode(this.specVersionNode);
        this.deviceNode = new Node(ELEM_NAME);
        this.rootNode.addNode(this.deviceNode);
        setWirelessMode(true);
        setDescriptionURI(DEFAULT_DESCRIPTION_URI);
    }

    public void CheckDeviceDes() {
        if (TextUtils.isEmpty(getUDN())) {
            setUDN(getUUID());
        }
        if (TextUtils.isEmpty(getFriendlyName())) {
            setInternalFriendlyName("IGALA_TV");
        }
        if (TextUtils.isEmpty(getManufacture())) {
            setManufacture("igala");
        }
        if (TextUtils.isEmpty(getManufactureURL())) {
            setManufactureURL("http://www.igala.com");
        }
        if (TextUtils.isEmpty(getModelName())) {
            setModelName("IGALA AV Media Renderer Device");
        }
        if (TextUtils.isEmpty(getModelNumber())) {
            setModelNumber("1234");
        }
        if (TextUtils.isEmpty(getModelURL())) {
            setModelURL("http://www.igala.com");
        }
        if (TextUtils.isEmpty(getPackageName())) {
            setPackageName("");
        }
    }

    public boolean start() {
        stop(false);
        CheckDeviceDes();
        int retryCnt = 0;
        int bindPort = getHTTPPort();
        HTTPServerList httpServerList = getHTTPServerList();
        while (!httpServerList.open(bindPort)) {
            retryCnt++;
            if (100 < retryCnt) {
                return false;
            }
            setHTTPPort(bindPort + 10);
            bindPort = getHTTPPort();
        }
        httpServerList.addRequestListener(this);
        httpServerList.start();
        String desStr = getDescriptionXml();
        setDescriptionXmlMd5(MD5Util.getMd5(desStr.getBytes(), desStr.length()));
        if (isQuicklySend()) {
            retryCnt = 0;
            GalaHttpServerList galaHttpServerList = getGalaHttpServerList();
            int galabindPort = getGalaHTTPPort();
            while (!galaHttpServerList.open(galabindPort)) {
                retryCnt++;
                if (100 < retryCnt) {
                    return false;
                }
                setGalaHTTPPort(galabindPort + 1);
                galabindPort = getGalaHTTPPort();
            }
            galaHttpServerList.addRequestListener(this);
            galaHttpServerList.addControlPointConnectListener(this.controlPointConnectRendererListener);
            galaHttpServerList.start();
        }
        if (isQuicklySend() && this.galaUDPHttpServer == null) {
            this.galaUDPHttpServer = new GalaUDPHttpServer();
            setUdpGalaHTTPPort(getGalaHTTPPort() + 1);
            int UDPgalabindPort = getUdpGalaHttpPort();
            int nHostAddrs = HostInterface.getNHostAddresses();
            String[] bindAddresses = new String[nHostAddrs];
            for (int n = 0; n < nHostAddrs; n++) {
                bindAddresses[n] = HostInterface.getHostAddress(n);
            }
            for (int i = 0; i < bindAddresses.length; i++) {
                if (!HostInterface.isIPv6Address(bindAddresses[i])) {
                    retryCnt = 0;
                    while (!this.galaUDPHttpServer.open(bindAddresses[i], UDPgalabindPort)) {
                        retryCnt++;
                        if (100 < retryCnt) {
                            return false;
                        }
                        setUdpGalaHTTPPort(UDPgalabindPort + 1);
                        UDPgalabindPort = getUdpGalaHttpPort();
                    }
                    this.galaUDPHttpServer.addRequestListener(this);
                    this.galaUDPHttpServer.addControlPointConnectRendererListener(this.controlPointConnectRendererListener);
                    this.galaUDPHttpServer.start();
                }
            }
        }
        SSDPSearchSocketList ssdpSearchSockList = getSSDPSearchSocketList();
        if (ssdpSearchSockList.open()) {
            ssdpSearchSockList.addSearchListener(this);
            ssdpSearchSockList.start();
            announce();
            Advertiser adv = new Advertiser(this);
            setAdvertiser(adv);
            adv.start("Advertiser");
            return true;
        }
        Debug.message("ssdpSearchSocketlist is not opening");
        return false;
    }

    public void clear() {
        setInternalFriendlyName("");
        setDescriptionXmlContent("");
        setDescriptionXmlMd5("");
    }

    private boolean stop(boolean doByeBye) {
        Advertiser adv = getAdvertiser();
        if (adv != null) {
            adv.stop();
            setAdvertiser(null);
        }
        if (doByeBye) {
            byebye();
        }
        HTTPServerList httpServerList = getHTTPServerList();
        httpServerList.stop();
        httpServerList.close();
        httpServerList.clear();
        if (isQuicklySend()) {
            GalaHttpServerList galaHttpServerList = getGalaHttpServerList();
            galaHttpServerList.stop();
            galaHttpServerList.close();
            galaHttpServerList.clear();
        }
        if (isQuicklySend() && this.galaUDPHttpServer != null) {
            this.galaUDPHttpServer.removeControlPointConnectRendererListener(this.controlPointConnectRendererListener);
            this.galaUDPHttpServer.close();
            this.galaUDPHttpServer.stop();
            this.galaUDPHttpServer = null;
        }
        SSDPSearchSocketList ssdpSearchSockList = getSSDPSearchSocketList();
        ssdpSearchSockList.stop();
        ssdpSearchSockList.close();
        ssdpSearchSockList.clear();
        if (getDeviceData() != null) {
            getDeviceData().setHTTPBindAddress(null);
        }
        return true;
    }

    public boolean stop() {
        return stop(true);
    }

    public boolean isRunning() {
        return getAdvertiser() != null;
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public String getAbsoluteURL(String urlString, String baseURLStr, String locationURLStr) {
        String result;
        if (TextUtils.isEmpty(urlString)) {
            return "";
        }
        try {
            return new URL(urlString).toString();
        } catch (Exception e) {
            e.printStackTrace();
            if (TextUtils.isEmpty(baseURLStr)) {
                if (!TextUtils.isEmpty(locationURLStr)) {
                    result = contactURLString(locationURLStr, urlString);
                    if (!TextUtils.isEmpty(result)) {
                        return result;
                    }
                }
                Device rootDev = getRootDevice();
                if (rootDev != null) {
                    String location = rootDev.getLocation();
                    baseURLStr = HTTP.getRequestHostURL(HTTP.getHost(location), HTTP.getPort(location));
                }
            }
            if (!TextUtils.isEmpty(baseURLStr)) {
                result = contactURLString(baseURLStr, urlString);
                if (!TextUtils.isEmpty(result)) {
                    return result;
                }
            }
            return urlString;
        }
    }

    private String contactURLString(String locationURLStr, String urlString) {
        if (locationURLStr.endsWith("/") && urlString.startsWith("/")) {
            try {
                return new URL(new StringBuilder(String.valueOf(locationURLStr)).append(urlString.substring(1)).toString()).toString();
            } catch (Exception e) {
                e.printStackTrace();
                return new URL(HTTP.getAbsoluteURL(locationURLStr, urlString)).toString();
            }
        }
        try {
            return new URL(new StringBuilder(String.valueOf(locationURLStr)).append(urlString).toString()).toString();
        } catch (Exception e2) {
            e2.printStackTrace();
            try {
                return new URL(HTTP.getAbsoluteURL(locationURLStr, urlString)).toString();
            } catch (Exception e22) {
                e22.printStackTrace();
                return null;
            }
        }
    }

    public String getAbsoluteURL(String urlString) {
        String baseURLStr = null;
        String locationURLStr = null;
        Device rootDev = getRootDevice();
        if (rootDev != null) {
            baseURLStr = rootDev.getURLBase();
            locationURLStr = rootDev.getLocation();
            int find = locationURLStr.indexOf("/", 10);
            if (find > 0) {
                locationURLStr = locationURLStr.substring(0, find);
            }
        }
        return getAbsoluteURL(urlString, baseURLStr, locationURLStr);
    }

    public void setNMPRMode(boolean flag) {
        Node devNode = getDeviceNode();
        if (devNode != null) {
            if (flag) {
                devNode.setNode(UPnP.INMPR03, "1.0");
                devNode.removeNode(URLBASE_NAME);
                return;
            }
            devNode.removeNode(UPnP.INMPR03);
        }
    }

    public boolean isNMPRMode() {
        Node devNode = getDeviceNode();
        if (devNode == null || devNode.getNode(UPnP.INMPR03) == null) {
            return false;
        }
        return true;
    }

    public void setWirelessMode(boolean flag) {
        this.wirelessMode = flag;
    }

    public boolean isWirelessMode() {
        return this.wirelessMode;
    }

    public int getSSDPAnnounceCount() {
        if (isNMPRMode() && isWirelessMode()) {
            return 5;
        }
        return 1;
    }

    protected void setUUID(String uuid) {
        this.devUUID = uuid;
    }

    public String getUUID() {
        if (this.devUUID != null && this.devUUID.length() > 16) {
            return this.devUUID;
        }
        String udn = this.deviceNode.getNodeValue(UDN);
        if (!udn.startsWith(Subscription.UUID)) {
            return udn;
        }
        String uuid = udn.substring(Subscription.UUID.length(), udn.length());
        setUUID(uuid);
        return uuid;
    }

    public String getIconUrl() {
        IconList iconList = getIconList();
        if (iconList.size() == 0) {
            return null;
        }
        String iconUrl = ((Icon) iconList.get(0)).getURL();
        if (getDeviceName() == DeviceName.IGALA_DONGLE || getDeviceName() == DeviceName.MEDIA_RENDERER) {
            String location = getLocation();
            String iconAbsUrl = location.substring(0, location.length() - DEFAULT_DESCRIPTION_URI.length()) + "/" + iconUrl;
            Debug.message("getIconUrl: For dongle: " + iconAbsUrl);
            return iconAbsUrl;
        }
        Debug.message("getIconUrl: For box that has absolute path in url value: " + iconUrl);
        return iconUrl;
    }

    private void updateUDN() {
        setUDN(new StringBuilder(Subscription.UUID).append(getUUID()).toString());
    }

    public Device getRootDevice() {
        Node rootNode = getRootNode();
        if (rootNode == null) {
            return null;
        }
        Node devNode = rootNode.getNode(ELEM_NAME);
        if (devNode != null) {
            return new Device(rootNode, devNode);
        }
        return null;
    }

    public Device getParentDevice() {
        if (isRootDevice()) {
            return null;
        }
        return new Device(getDeviceNode().getParentNode().getParentNode());
    }

    public void addService(Service s) {
        Node serviceListNode = getDeviceNode().getNode("serviceList");
        if (serviceListNode == null) {
            serviceListNode = new Node("serviceList");
            getDeviceNode().addNode(serviceListNode);
        }
        serviceListNode.addNode(s.getServiceNode());
    }

    public void addDevice(Device d) {
        Node deviceNode = getDeviceNode();
        Node deviceListNode = deviceNode.getNode(DeviceList.ELEM_NAME);
        if (deviceListNode == null) {
            deviceListNode = new Node(DeviceList.ELEM_NAME);
            deviceNode.addNode(deviceListNode);
        }
        deviceListNode.addNode(d.getDeviceNode());
        d.setRootNode(null);
        if (getRootNode() == null) {
            Node root = new Node(RootDescription.ROOT_ELEMENT);
            root.setNameSpace("", RootDescription.ROOT_ELEMENT_NAMESPACE);
            Node spec = new Node("specVersion");
            Node maj = new Node("major");
            maj.setValue("1");
            Node min = new Node("minor");
            min.setValue("0");
            spec.addNode(maj);
            spec.addNode(min);
            root.addNode(spec);
            setRootNode(root);
        }
    }

    private DeviceData getDeviceData() {
        Node node = getDeviceNode();
        DeviceData userData = (DeviceData) node.getUserData();
        if (userData != null) {
            return userData;
        }
        userData = new DeviceData();
        node.setUserData(userData);
        userData.setNode(node);
        return userData;
    }

    private void setDescriptionFile(File file) {
        getDeviceData().setDescriptionFile(file);
    }

    public File getDescriptionFile() {
        return getDeviceData().getDescriptionFile();
    }

    private void setDescriptionURI(String uri) {
        getDeviceData().setDescriptionURI(uri);
    }

    private String getDescriptionURI() {
        return getDeviceData().getDescriptionURI();
    }

    private boolean isDescriptionURI(String uri) {
        String descriptionURI = getDescriptionURI();
        if (uri == null || descriptionURI == null) {
            return false;
        }
        return descriptionURI.equals(uri);
    }

    public String getDescriptionFilePath() {
        File descriptionFile = getDescriptionFile();
        if (descriptionFile == null) {
            return "";
        }
        return descriptionFile.getAbsoluteFile().getParent();
    }

    public boolean loadDescription(InputStream input) throws InvalidDescriptionException {
        try {
            this.rootNode = UPnP.getXMLParser().parse(input);
            if (this.rootNode == null) {
                throw new InvalidDescriptionException(Description.NOROOT_EXCEPTION);
            }
            this.deviceNode = this.rootNode.getNode(ELEM_NAME);
            if (this.deviceNode == null) {
                throw new InvalidDescriptionException(Description.NOROOTDEVICE_EXCEPTION);
            } else if (!initializeLoadedDescription()) {
                return false;
            } else {
                setDescriptionFile(null);
                return true;
            }
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    public boolean loadDescription(String descString) throws InvalidDescriptionException {
        try {
            this.rootNode = UPnP.getXMLParser().parse(descString);
            if (this.rootNode == null) {
                throw new InvalidDescriptionException(Description.NOROOT_EXCEPTION);
            }
            this.deviceNode = this.rootNode.getNode(ELEM_NAME);
            if (this.deviceNode == null) {
                throw new InvalidDescriptionException(Description.NOROOTDEVICE_EXCEPTION);
            } else if (!initializeLoadedDescription()) {
                return false;
            } else {
                setDescriptionFile(null);
                return true;
            }
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    public boolean loadDescription(File file) throws InvalidDescriptionException {
        try {
            this.rootNode = UPnP.getXMLParser().parse(file);
            if (this.rootNode == null) {
                throw new InvalidDescriptionException(Description.NOROOT_EXCEPTION, file);
            }
            this.deviceNode = this.rootNode.getNode(ELEM_NAME);
            if (this.deviceNode == null) {
                throw new InvalidDescriptionException(Description.NOROOTDEVICE_EXCEPTION, file);
            } else if (!initializeLoadedDescription()) {
                return false;
            } else {
                setDescriptionFile(file);
                return true;
            }
        } catch (Exception e) {
            throw new InvalidDescriptionException(e);
        }
    }

    private boolean initializeLoadedDescription() {
        setDescriptionURI(DEFAULT_DESCRIPTION_URI);
        setLeaseTime(30);
        setHTTPPort(HTTP_DEFAULT_PORT);
        if (!hasUDN()) {
            updateUDN();
        }
        return true;
    }

    public static boolean isDeviceNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    public boolean isRootDevice() {
        Node rootNode = getRootNode();
        if (rootNode != null) {
            Node deviceNode = rootNode.getNode(ELEM_NAME);
            if (deviceNode != null) {
                String value = deviceNode.getNodeValue(UDN);
                if (value != null) {
                    return value.equals(getUDN());
                }
            }
        }
        return false;
    }

    public void setSSDPPacket(SSDPPacket packet) {
        getDeviceData().setSSDPPacket(packet);
    }

    public SSDPPacket getSSDPPacket() {
        if (isRootDevice()) {
            return getDeviceData().getSSDPPacket();
        }
        return null;
    }

    public void setLocation(String value) {
        getDeviceData().setLocation(value);
    }

    public String getLocation() {
        SSDPPacket packet = getSSDPPacket();
        if (packet != null) {
            return packet.getLocation();
        }
        return getDeviceData().getLocation();
    }

    public boolean isGalaServer() {
        if (getSSDPPacket() != null) {
            return getSSDPPacket().isGalaServer();
        }
        String model = getModelDescription();
        return TextUtils.isEmpty(model) ? false : model.contains("TVGUO");
    }

    public void setLeaseTime(int value) {
        getDeviceData().setLeaseTime(value);
        Advertiser adv = getAdvertiser();
        if (adv != null) {
            announce();
            adv.restart("Advertiser");
        }
    }

    public int getLeaseTime() {
        SSDPPacket packet = getSSDPPacket();
        if (packet != null) {
            return packet.getLeaseTime();
        }
        return getDeviceData().getLeaseTime();
    }

    public void setTimeStamp(long timeStamp) {
        this.mTimeStamp = timeStamp;
    }

    public long getTimeStamp() {
        SSDPPacket packet = getSSDPPacket();
        if (packet != null) {
            return packet.getTimeStamp();
        }
        return this.mTimeStamp;
    }

    public long getElapsedTime() {
        return (System.currentTimeMillis() - getTimeStamp()) / 1000;
    }

    public boolean isExpired() {
        return getElapsedTime() < 30;
    }

    private void setURLBase(String value) {
        if (isRootDevice()) {
            Node node = getRootNode().getNode(URLBASE_NAME);
            if (node != null) {
                node.setValue(value);
                return;
            }
            node = new Node(URLBASE_NAME);
            node.setValue(value);
            int index = 1;
            if (!getRootNode().hasNodes()) {
                index = 1;
            }
            getRootNode().insertNode(node, index);
        }
    }

    private void updateURLBase(String host) {
        setURLBase(HostInterface.getHostURL(host, getHTTPPort(), ""));
    }

    public String getURLBase() {
        if (!isRootDevice() || getRootNode() == null) {
            return "";
        }
        return getRootNode().getNodeValue(URLBASE_NAME);
    }

    public void setDeviceType(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(DEVICE_TYPE, value);
        }
    }

    public String getDeviceType() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(DEVICE_TYPE);
        }
        return "";
    }

    public boolean isDeviceType(String value) {
        if (value == null) {
            return false;
        }
        return value.equals(getDeviceType());
    }

    public boolean setFriendlyName(String value) {
        if (!setInternalFriendlyName(value)) {
            return false;
        }
        if (this.cacheMap != null) {
            this.cacheMap.clear();
        }
        byebye();
        return true;
    }

    public String getFriendlyName() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(FRIENDLY_NAME);
        }
        return "";
    }

    public String getManufacturer() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue("manufacturer");
        }
        return "";
    }

    boolean setInternalFriendlyName(String value) {
        if (TextUtils.isEmpty(value)) {
            return false;
        }
        Node deviceNode = getDeviceNode();
        if (deviceNode == null) {
            return false;
        }
        deviceNode.setNode(FRIENDLY_NAME, value);
        return true;
    }

    public void setManufacture(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode("manufacturer", value);
        }
    }

    public String getManufacture() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue("manufacturer");
        }
        return "";
    }

    public void setManufactureURL(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(MANUFACTURE_URL, value);
        }
    }

    public String getManufactureURL() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(MANUFACTURE_URL);
        }
        return "";
    }

    public void setModelDescription(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(MODEL_DESCRIPTION, value);
        }
    }

    public String getModelDescription() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(MODEL_DESCRIPTION);
        }
        return "";
    }

    public void setModelName(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(MODEL_NAME, value);
        }
    }

    public String getModelName() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(MODEL_NAME);
        }
        return "";
    }

    public void setModelNumber(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(MODEL_NUMBER, value);
        }
    }

    public String getModelNumber() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(MODEL_NUMBER);
        }
        return "";
    }

    public void setModelURL(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(MODEL_URL, value);
        }
    }

    public String getModelURL() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(MODEL_URL);
        }
        return "";
    }

    public void setDLNADOC(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            Node node = deviceNode.getNode(DLNA_DOC);
            if (node != null) {
                node.setNameSpace(RootDescription.ROOT_ELEMENT_NSDLNA, RootDescription.ROOT_ELEMENT_DLNANAMESPACE);
                node.setValue(value);
                return;
            }
            node = new Node(DLNA_DOC);
            node.setNameSpace(RootDescription.ROOT_ELEMENT_NSDLNA, RootDescription.ROOT_ELEMENT_DLNANAMESPACE);
            node.setValue(value);
            deviceNode.addNode(node);
        }
    }

    public String getDLNADOC() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(DLNA_DOC);
        }
        return "";
    }

    public void setSerialNumber(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(SERIAL_NUMBER, value);
        }
    }

    public String getSerialNumber() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(SERIAL_NUMBER);
        }
        return "";
    }

    public void setUDN(String value) {
        if (!value.contains(Subscription.UUID)) {
            value = new StringBuilder(Subscription.UUID).append(value).toString();
        }
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(UDN, value);
        }
    }

    public String getUDN() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(UDN);
        }
        return "";
    }

    public boolean hasUDN() {
        return !TextUtils.isEmpty(getUDN());
    }

    public void setUPC(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(UPC, value);
        }
    }

    public String getUPC() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(UPC);
        }
        return "";
    }

    public void setPresentationURL(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(presentationURL, value);
        }
    }

    public String getPresentationURL() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(presentationURL);
        }
        return "";
    }

    public void setPackageName(String value) {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            deviceNode.setNode(PACKAGE_NAME, value);
        }
    }

    public String getPackageName() {
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            return deviceNode.getNodeValue(PACKAGE_NAME);
        }
        return "";
    }

    public DeviceList getDeviceList() {
        DeviceList devList = new DeviceList();
        Node deviceNode = getDeviceNode();
        if (deviceNode != null) {
            Node devListNode = deviceNode.getNode(DeviceList.ELEM_NAME);
            if (devListNode != null) {
                int nNode = devListNode.getNNodes();
                for (int n = 0; n < nNode; n++) {
                    Node node = devListNode.getNode(n);
                    if (isDeviceNode(node)) {
                        devList.add(new Device(node));
                    }
                }
            }
        }
        return devList;
    }

    public boolean isDevice(String name) {
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (name.endsWith(getUDN())) {
            return true;
        }
        if (name.equals(getFriendlyName())) {
            return true;
        }
        if (name.endsWith(getDeviceType())) {
            return true;
        }
        return false;
    }

    public Device getDevice(String name) {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++) {
            Device dev = devList.getDevice(n);
            if (dev.isDevice(name)) {
                return dev;
            }
            Device cdev = dev.getDevice(name);
            if (cdev != null) {
                return cdev;
            }
        }
        return null;
    }

    public Device getDeviceByDescriptionURI(String uri) {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++) {
            Device dev = devList.getDevice(n);
            if (dev.isDescriptionURI(uri)) {
                return dev;
            }
            Device cdev = dev.getDeviceByDescriptionURI(uri);
            if (cdev != null) {
                return cdev;
            }
        }
        return null;
    }

    public ServiceList getServiceList() {
        if (this.mServiceList == null) {
            this.mServiceList = new ServiceList();
        }
        if (!this.mServiceList.isEmpty()) {
            return this.mServiceList;
        }
        Node serviceListNode = getDeviceNode().getNode("serviceList");
        if (serviceListNode == null) {
            return this.mServiceList;
        }
        int nNode = serviceListNode.getNNodes();
        for (int n = 0; n < nNode; n++) {
            Node node = serviceListNode.getNode(n);
            if (Service.isServiceNode(node)) {
                this.mServiceList.add(new Service(node));
            }
        }
        return this.mServiceList;
    }

    public boolean isSupportService(String type) {
        ServiceList serviceList = getServiceList();
        int serviceCount = serviceList == null ? 0 : serviceList.size();
        for (int index = 0; index < serviceCount; index++) {
            Service service = (Service) serviceList.get(index);
            if (service != null) {
                String serviceType = service.getServiceType();
                if (!TextUtils.isEmpty(serviceType) && serviceType.contains(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Service getService(String name) {
        int n;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (service.isService(name)) {
                return service;
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            service = devList.getDevice(n).getService(name);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    public Service getServiceBySCPDURL(String searchUrl) {
        int n;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (service.isSCPDURL(searchUrl)) {
                return service;
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            service = devList.getDevice(n).getServiceBySCPDURL(searchUrl);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    public Service getServiceByControlURL(String searchUrl) {
        int n;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (service.isControlURL(searchUrl)) {
                return service;
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            service = devList.getDevice(n).getServiceByControlURL(searchUrl);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    public Service getServiceByEventSubURL(String searchUrl) {
        int n;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (service.isEventSubURL(searchUrl)) {
                return service;
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            service = devList.getDevice(n).getServiceByEventSubURL(searchUrl);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    public Service getSubscriberService(String uuid) {
        int n;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (uuid.equals(service.getSID())) {
                return service;
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            service = devList.getDevice(n).getSubscriberService(uuid);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    public StateVariable getStateVariable(String serviceType, String name) {
        if (serviceType == null && name == null) {
            return null;
        }
        int n;
        StateVariable stateVar;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (serviceType == null || service.getServiceType().equals(serviceType)) {
                stateVar = service.getStateVariable(name);
                if (stateVar != null) {
                    return stateVar;
                }
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            stateVar = devList.getDevice(n).getStateVariable(serviceType, name);
            if (stateVar != null) {
                return stateVar;
            }
        }
        return null;
    }

    public StateVariable getStateVariable(String name) {
        return getStateVariable(null, name);
    }

    public Action getAction(String name) {
        int n;
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            ActionList actionList = serviceList.getService(n).getActionList();
            int actionCnt = actionList.size();
            for (int i = 0; i < actionCnt; i++) {
                Action action = actionList.getAction(i);
                String actionName = action.getName();
                if (actionName != null && actionName.equals(name)) {
                    return action;
                }
            }
        }
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (n = 0; n < devCnt; n++) {
            action = devList.getDevice(n).getAction(name);
            if (action != null) {
                return action;
            }
        }
        return null;
    }

    public void setIconList(IconList iconList) {
        if (iconList != null) {
            Node iconListNode = getDeviceNode().getNode(IconList.ELEM_NAME);
            if (iconListNode == null) {
                iconListNode = new Node(IconList.ELEM_NAME);
            }
            for (int i = 0; i < iconList.size(); i++) {
                iconListNode.addNode(iconList.getIcon(i).getIconNode());
            }
            Node deviceNode = getDeviceNode();
            if (deviceNode != null) {
                deviceNode.addNode(iconListNode);
            }
        }
    }

    public IconList getIconList() {
        IconList iconList = new IconList();
        Node iconListNode = getDeviceNode().getNode(IconList.ELEM_NAME);
        if (iconListNode != null) {
            int nNode = iconListNode.getNNodes();
            for (int n = 0; n < nNode; n++) {
                Node node = iconListNode.getNode(n);
                if (Icon.isIconNode(node)) {
                    iconList.add(new Icon(node));
                }
            }
        }
        return iconList;
    }

    public Icon getIcon(int n) {
        IconList iconList = getIconList();
        if (n >= 0 || iconList.size() - 1 >= n) {
            return iconList.getIcon(n);
        }
        return null;
    }

    public Icon getSmallestIcon() {
        Icon smallestIcon = null;
        IconList iconList = getIconList();
        int iconCount = iconList.size();
        for (int n = 0; n < iconCount; n++) {
            Icon icon = iconList.getIcon(n);
            if (smallestIcon == null) {
                smallestIcon = icon;
            } else if (icon.getWidth() < smallestIcon.getWidth()) {
                smallestIcon = icon;
            }
        }
        return smallestIcon;
    }

    public Vector<String> getClientList() {
        GalaHttpServerList httpServerList = getGalaHttpServerList();
        int nServers = httpServerList.size();
        for (int n = 0; n < nServers; n++) {
            GalaHttpServer server = httpServerList.getHTTPServer(n);
            if (server != null) {
                return server.getClientList();
            }
        }
        return null;
    }

    public String getLocationURL(String host) {
        return HostInterface.getHostURL(host, getHTTPPort(), getDescriptionURI());
    }

    private String getNotifyDeviceNT() {
        if (isRootDevice()) {
            return "upnp:rootdevice";
        }
        return getUDN();
    }

    private String getNotifyDeviceUSN() {
        if (isRootDevice()) {
            return getUDN() + "::" + "upnp:rootdevice";
        }
        return getUDN();
    }

    private String getNotifyDeviceTypeNT() {
        return getDeviceType();
    }

    private String getNotifyDeviceTypeUSN() {
        return getUDN() + "::" + getDeviceType();
    }

    public static final void notifyWait() {
        TimerUtil.waitRandom(100);
    }

    private void initNotifyPacket(SSDPNotifyRequest request, String bindAddr, boolean isMulticast) {
        boolean isUpdate = false;
        if (request == null) {
            request = SSDPNotifyRequest.getInstance();
            request.setServer(UPnP.getServerName());
            request.setNTS(NTS.ALIVE);
            request.setLeaseTime(30);
            request.setConnect(false);
            request.setIGALADEVICE(this.mDeviceName);
            request.setIGALAVERSION(this.mSdkVersion);
            request.setDEVICEVERSION(this.mDeviceVersion);
            request.setFileMd5(getDescriptionXmlMd5());
            request.setNT(getNotifyDeviceNT());
            request.setUSN(getNotifyDeviceUSN());
            request.setIGALAPORT(getGalaHTTPPort());
            request.setIGALAUDPPORT(getUdpGalaHttpPort());
        }
        String firendlyName;
        String location;
        if (isMulticast) {
            firendlyName = getFriendlyName();
            if (this.mMulticastFriendlyName == null || !this.mMulticastFriendlyName.equals(firendlyName)) {
                isUpdate = true;
                this.mMulticastFriendlyName = firendlyName;
                request.setMYNAME(firendlyName);
            }
            location = getLocationURL(bindAddr);
            if (this.mMulticastDescriptionLocation == null || !this.mMulticastDescriptionLocation.equals(location)) {
                isUpdate = true;
                this.mMulticastDescriptionLocation = location;
                request.setLocation(location);
            }
            request.setHost(SSDP.ADDRESS, SSDP.PORT);
        } else {
            firendlyName = getFriendlyName();
            if (this.mBroadcastFriendlyName == null || !this.mBroadcastFriendlyName.equals(firendlyName)) {
                isUpdate = true;
                this.mBroadcastFriendlyName = firendlyName;
                request.setMYNAME(firendlyName);
            }
            location = getLocationURL(bindAddr);
            if (this.mBroadcastDescriptionLocation == null || !this.mBroadcastDescriptionLocation.equals(location)) {
                isUpdate = true;
                this.mBroadcastDescriptionLocation = location;
                request.setLocation(location);
            }
            request.setHost(SSDP.BROADCAST_ADDRESS, SSDP.IGALA_BROADCAST_PORT);
        }
        if (isMulticast) {
            if (isUpdate || this.mMulticastPacketMessage == null) {
                this.mMulticastPacketMessage = request.getHeader().toString();
            }
        } else if (isUpdate || this.mBroadcastPacketMessage == null) {
            this.mBroadcastPacketMessage = request.getHeader().toString();
        }
    }

    public void announce(String bindAddr) {
        if (TextUtils.isEmpty(bindAddr)) {
            Debug.message("announce : bindAddr is null");
            return;
        }
        notifyWait();
        SSDPNotifySocket ssdpSock = SSDPNotifySocket.getInstance(bindAddr);
        ssdpSock.openSendBroadcastSocket();
        if (isRootDevice()) {
            initNotifyPacket(this.mMulticastPacket, bindAddr, true);
            initNotifyPacket(this.mBroadcastPacket, bindAddr, false);
            ssdpSock.post(this.mMulticastPacketMessage, null, -1);
            ssdpSock.sendBroadcast(this.mBroadcastPacketMessage);
        }
        ssdpSock.closeReceiveBroadcastSocket();
        ssdpSock.closeReceiveMulticastSocket();
        if (this.dmcAddrList.size() > 0) {
            for (int i = 0; i < this.dmcAddrList.size(); i++) {
                deviceSearchResponse((SSDPPacket) this.dmcAddrList.get(i));
            }
        }
    }

    public void setIpListRefreshStatus(boolean isRefresh) {
        this.isRefreshIpList = isRefresh;
    }

    public synchronized void announce() {
        String[] bindAddresses;
        int i;
        notifyWait();
        InetAddress[] binds = getDeviceData().getHTTPBindAddress();
        if (binds != null) {
            bindAddresses = new String[binds.length];
            for (i = 0; i < binds.length; i++) {
                bindAddresses[i] = binds[i].getHostAddress();
            }
        } else {
            bindAddresses = HostInterface.getHostIpAddress(this.isRefreshIpList);
            this.isRefreshIpList = false;
        }
        if (bindAddresses == null || bindAddresses.length == 0) {
            Debug.message("announce : bindAddresses is null");
        } else {
            int ssdpCount = getSSDPAnnounceCount();
            int j = 0;
            while (j < bindAddresses.length) {
                if (!(bindAddresses[j] == null || bindAddresses[j].length() == 0)) {
                    for (i = 0; i < ssdpCount; i++) {
                        announce(bindAddresses[j]);
                    }
                }
                j++;
            }
        }
    }

    public void byebye(String bindAddr) {
        Debug.message("byebye:" + bindAddr);
        SSDPNotifySocket ssdpSock = SSDPNotifySocket.getInstance(bindAddr);
        ssdpSock.openSendBroadcastSocket();
        SSDPNotifyRequest ssdpReq = SSDPNotifyRequest.getInstance();
        ssdpReq.setNTS(NTS.BYEBYE);
        if (isRootDevice()) {
            String devNT = getNotifyDeviceNT();
            String devUSN = getNotifyDeviceUSN();
            ssdpReq.setNT(devNT);
            ssdpReq.setUSN(devUSN);
            ssdpSock.post(ssdpReq, null, -1);
        }
        ssdpSock.closeReceiveBroadcastSocket();
        ssdpSock.closeReceiveMulticastSocket();
    }

    public synchronized void byebye() {
        String[] bindAddresses;
        int i;
        InetAddress[] binds = getDeviceData().getHTTPBindAddress();
        if (binds != null) {
            bindAddresses = new String[binds.length];
            for (i = 0; i < binds.length; i++) {
                bindAddresses[i] = binds[i].getHostAddress();
            }
        } else {
            int nHostAddrs = HostInterface.getNHostAddresses();
            bindAddresses = new String[nHostAddrs];
            for (int n = 0; n < nHostAddrs; n++) {
                bindAddresses[n] = HostInterface.getHostAddress(n);
            }
        }
        int j = 0;
        while (j < bindAddresses.length) {
            if (bindAddresses[j] != null && bindAddresses[j].length() > 0) {
                int ssdpCount = getSSDPAnnounceCount();
                for (i = 0; i < ssdpCount; i++) {
                    byebye(bindAddresses[j]);
                }
            }
            j++;
        }
    }

    public void initResponPacket(String st, String usn, String rootDevLocation) {
        if (this.mResponPacket == null) {
            this.mResponPacket = SSDPSearchResponse.getInstance();
            this.mResponPacket.setLeaseTime(getLeaseTime());
            this.mResponPacket.setDate(cal);
            this.mResponPacket.setST(st);
            this.mResponPacket.setUSN(usn);
            this.mResponPacket.setLeaseTime(DLNA_SEARCH_LEASE_TIME);
            this.mResponPacket.setFileMd5(getDescriptionXmlMd5());
            this.mResponPacket.setConnect(false);
            this.mResponPacket.setIGALADEVICE(this.mDeviceName);
            this.mResponPacket.setIGALAVERSION(this.mSdkVersion);
            this.mResponPacket.setDEVICEVERSION(this.mDeviceVersion);
            this.mResponPacket.setIGALAPORT(getGalaHTTPPort());
            this.mResponPacket.setIGALAUDPPORT(getUdpGalaHttpPort());
        }
        boolean isUpdate = false;
        String firendlyName = getFriendlyName();
        if (this.mResponseFriendlyName == null || !this.mResponseFriendlyName.equals(firendlyName)) {
            isUpdate = true;
            this.mResponseFriendlyName = firendlyName;
            this.mResponPacket.setMYNAME(firendlyName);
        }
        if (this.mResponseDescriptionLocation == null || !this.mResponseDescriptionLocation.equals(rootDevLocation)) {
            isUpdate = true;
            this.mResponseDescriptionLocation = rootDevLocation;
            this.mResponPacket.setLocation(rootDevLocation);
        }
        if (isUpdate || this.mResponPacketMessage == null) {
            this.mResponPacketMessage = this.mResponPacket.getHeader().toString();
        }
    }

    public boolean postSearchResponse(SSDPPacket ssdpPacket, String st, String usn) {
        String localAddr = ssdpPacket.getLocalAddress();
        String remoteAddr = ssdpPacket.getRemoteAddress();
        int remotePort = ssdpPacket.getRemotePort();
        Device rootDev = getRootDevice();
        if (rootDev == null) {
            Debug.message("Oops, rootDev null");
            return false;
        } else if (localAddr.equals(remoteAddr)) {
            return true;
        } else {
            SSDPSearchResponseSocket ssdpResSock = SSDPSearchResponseSocket.getInstance();
            if (Debug.isOn() && this.mResponPacket != null) {
                this.mResponPacket.print();
            }
            int ssdpCount = getSSDPAnnounceCount();
            initResponPacket(st, usn, rootDev.getLocationURL(localAddr));
            for (int i = 0; i < ssdpCount; i++) {
                ssdpResSock.post(remoteAddr, remotePort, this.mResponPacketMessage);
            }
            return true;
        }
    }

    public boolean postSearchResponseSimple(SSDPPacket ssdpPacket, String st, String usn) {
        String localAddr = ssdpPacket.getLocalAddress();
        String remoteAddr = ssdpPacket.getRemoteAddress();
        int remotePort = ssdpPacket.getRemotePort();
        Device rootDev = getRootDevice();
        if (rootDev == null) {
            Debug.message("Oops, rootDev null");
            return false;
        }
        String rootDevLocation = rootDev.getLocationURL(localAddr);
        if (localAddr.equals(remoteAddr)) {
            return true;
        }
        SSDPSearchResponse ssdpRes = SSDPSearchResponse.getInstance();
        ssdpRes.setLeaseTime(DLNA_SEARCH_LEASE_TIME);
        ssdpRes.setDate(cal);
        ssdpRes.setST(st);
        ssdpRes.setUSN(usn);
        ssdpRes.setLocation(rootDevLocation);
        SSDPSearchResponseSocket ssdpResSock = SSDPSearchResponseSocket.getInstance();
        if (Debug.isOn()) {
            ssdpRes.print();
        }
        int ssdpCount = getSSDPAnnounceCount();
        for (int i = 0; i < ssdpCount; i++) {
            ssdpResSock.post(remoteAddr, remotePort, ssdpRes);
        }
        return true;
    }

    public void deviceSearchResponse(SSDPPacket ssdpPacket) {
        if (ssdpPacket == null) {
            Debug.message("deviceSearchResponse() ssdppacket is null");
            return;
        }
        int n;
        byte[] message = ssdpPacket.getData();
        boolean isRootDevice = isRootDevice();
        String devUSN = getUDN();
        if (isRootDevice) {
            devUSN = new StringBuilder(String.valueOf(devUSN)).append("::upnp:rootdevice").toString();
        }
        if (Util.hasCode(message, ST.ALL_DEVICE_BYTE)) {
            String devNT = getNotifyDeviceNT();
            int repeatCnt = isRootDevice ? 3 : 2;
            for (n = 0; n < repeatCnt; n++) {
                postSearchResponse(ssdpPacket, devNT, devUSN);
            }
        } else if (Util.hasCode(message, ST.ROOT_DEVICE_BYTE)) {
            if (isRootDevice) {
                postSearchResponse(ssdpPacket, "upnp:rootdevice", devUSN);
            }
        } else if (Util.hasCode(message, ST.UUID_DEVICE_BYTE)) {
            String devUDN = getUDN();
            ssdpST = Util.copyFromByte(ssdpPacket.getData(), HTTP.ST_BYTES, HTTP.CRLF.getBytes());
            if (ssdpST != null && ssdpST.equals(devUDN)) {
                postSearchResponse(ssdpPacket, devUDN, devUSN);
            }
        } else if (Util.hasCode(message, ST.URN_DEVICE_BYTE)) {
            String devType = getDeviceType();
            ssdpST = Util.copyFromByte(ssdpPacket.getData(), HTTP.ST_BYTES, HTTP.CRLF.getBytes());
            if (ssdpST != null && ssdpST.equals(devType)) {
                devUSN = getUDN() + "::" + devType;
                postSearchResponse(ssdpPacket, devType, devUSN);
                postSearchResponseSimple(ssdpPacket, devType, devUSN);
            }
        }
        ServiceList serviceList = getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            serviceList.getService(n).serviceSearchResponse(ssdpPacket);
        }
        DeviceList childDeviceList = getDeviceList();
        int childDeviceCnt = childDeviceList.size();
        for (n = 0; n < childDeviceCnt; n++) {
            childDeviceList.getDevice(n).deviceSearchResponse(ssdpPacket);
        }
    }

    private void addRemoteDmcAddr(byte[] ssdpRecvByte, String localAddress, SocketAddress remoteAddress) {
        if (remoteAddress != null) {
            if (!this.dmcAddrList.isEmpty()) {
                for (int i = 0; i < this.dmcAddrList.size(); i++) {
                    if (this.dmcAddrList.get(i) == null) {
                        Debug.message("addRemoteDmcAddr : dmcDevice is null");
                    } else if (((SSDPPacket) this.dmcAddrList.get(i)).getDatagramPacket() == null) {
                        Debug.message("addRemoteDmcAddr : dmcDevice DatagramPacket is null");
                    } else if (remoteAddress.equals(((SSDPPacket) this.dmcAddrList.get(i)).getDatagramPacket().getSocketAddress())) {
                        this.mSsdpPacket.setData(ssdpRecvByte);
                        this.mSsdpPacket.setLocalAddress(localAddress);
                        this.mSsdpPacket.setTimeStamp(System.currentTimeMillis());
                        return;
                    }
                }
                if (this.dmcAddrList.size() > 2) {
                    this.dmcAddrList.remove(0);
                }
            }
            this.mSsdpPacket = new SSDPPacket(ssdpRecvByte, ssdpRecvByte.length);
            this.mSsdpPacket.getDatagramPacket().setSocketAddress(remoteAddress);
            this.mSsdpPacket.setLocalAddress(localAddress);
            this.mSsdpPacket.setTimeStamp(System.currentTimeMillis());
            this.dmcAddrList.add(this.mSsdpPacket);
        }
    }

    public void deviceSearchReceived(SSDPPacket ssdpPacket) {
    }

    public void ssdpSearchReceived(byte[] ssdpRecvByte, String localAddress, SocketAddress socketAddress) {
        addRemoteDmcAddr(ssdpRecvByte, localAddress, socketAddress);
        deviceSearchResponse(this.mSsdpPacket);
    }

    public void setHTTPPort(int port) {
        getDeviceData().setHTTPPort(port);
    }

    public int getHTTPPort() {
        return getDeviceData().getHTTPPort();
    }

    public void setIGALADEVICE(int deviceName) {
        this.mDeviceName = deviceName;
    }

    public boolean setSdkVersion(int deviceVersion) {
        if (this.mSdkVersionChanged) {
            Debug.message("SDKVersion only can be set once!");
            return false;
        } else if (deviceVersion < 0) {
            Debug.message("SDKVersion can not be less than 0!");
            return false;
        } else {
            this.mSdkVersion = deviceVersion;
            this.mSdkVersionChanged = true;
            return true;
        }
    }

    public void setDONGLEVERSION(int version) {
        this.mDeviceVersion = version;
    }

    public void setDeviceName(int deviceName) {
        getDeviceData().setGalaDeviceType(deviceName);
    }

    public int getDeviceName() {
        return getDeviceData().getGalaDeviceType();
    }

    public void setDeviceVersion(int deviceVersion) {
        getDeviceData().setGalaVersion(deviceVersion);
    }

    public int getDeviceVersion() {
        return getDeviceData().getGalaVersion();
    }

    public void setGalaDeviceVersion(int version) {
        getDeviceData().setGalaDeviceVersion(version);
    }

    public int getGalaDeviceVersion() {
        return getDeviceData().getGalaDeviceVersion();
    }

    public int getGalaHTTPPortFromSSDP() {
        SSDPPacket ssdpPacket = getSSDPPacket();
        if (ssdpPacket != null) {
            setGalaHTTPPort(ssdpPacket.getGalaHttpPort());
        }
        return getGalaHTTPPort();
    }

    public int getGalaUDPHTTPPortFromSSDP() {
        SSDPPacket ssdpPacket = getSSDPPacket();
        if (ssdpPacket != null) {
            setUdpGalaHTTPPort(ssdpPacket.getGalaUDPHttpPort());
        }
        return getUdpGalaHttpPort();
    }

    public boolean getIsSuperQuicklySend() {
        if (getGalaHTTPPortFromSSDP() == 0) {
            return false;
        }
        return true;
    }

    public String getGalaHostFromSSDP() {
        String location = getLocation();
        return TextUtils.isEmpty(location) ? "" : HTTP.getHost(location);
    }

    public int getGalaHTTPPort() {
        return getDeviceData().getGalahttpPort();
    }

    public void setGalaHTTPPort(int port) {
        getDeviceData().setGalahttpPort(port);
    }

    public int getUdpGalaHttpPort() {
        return getDeviceData().getUdpgalahttpPort();
    }

    public void setUdpGalaHTTPPort(int port) {
        getDeviceData().setUdpgalahttpPort(port);
    }

    public void setHTTPBindAddress(InetAddress[] inets) {
        getDeviceData().setHTTPBindAddress(inets);
    }

    public InetAddress[] getHTTPBindAddress() {
        return getDeviceData().getHTTPBindAddress();
    }

    public String getSSDPIPv4MulticastAddress() {
        return getDeviceData().getMulticastIPv4Address();
    }

    public void getSSDPIPv4MulticastAddress(String ip) {
        getDeviceData().setMulticastIPv4Address(ip);
    }

    public String getSSDPIPv6MulticastAddress() {
        return getDeviceData().getMulticastIPv6Address();
    }

    public void getSSDPIPv6MulticastAddress(String ip) {
        getDeviceData().setMulticastIPv6Address(ip);
    }

    public void httpRequestRecieved(HTTPRequest httpReq) {
        if (httpReq.isQuicklyRequest()) {
            if (getQuicklySendMessageListener() != null) {
                byte[] bArray = httpReq.getContent();
                for (int i = 0; i < bArray.length; i++) {
                    Debug.message("content byte [" + i + "] is " + bArray[i]);
                }
                Debug.message("SendMessageReceived done " + httpReq.getContent()[0]);
                this.quicklySendMessageListener.onQuicklySendMessageRecieved(httpReq.getContent()[0]);
            }
        } else if (httpReq.isGetRequest() || httpReq.isHeadRequest()) {
            httpGetRequestRecieved(httpReq);
        } else if (httpReq.isPostRequest()) {
            httpPostRequestRecieved(httpReq);
        } else if (httpReq.isSubscribeRequest() || httpReq.isUnsubscribeRequest()) {
            deviceEventSubscriptionRecieved(new SubscriptionRequest(httpReq));
        } else {
            httpReq.returnBadRequest();
        }
    }

    private synchronized byte[] getDescriptionData(String host) {
        byte[] bArr;
        if (!isNMPRMode()) {
            updateURLBase(host);
        }
        if (getDescriptionXmlContent() == "") {
            Node rootNode = getRootNode();
            if (rootNode == null) {
                bArr = new byte[0];
            } else {
                String desc = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new String())).append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").toString())).append("\n").toString())).append(rootNode.toString()).toString();
                setDescriptionXmlContent(desc);
                Debug.message("getDescriptionData new description: " + desc);
                bArr = desc.getBytes();
            }
        } else {
            bArr = getDescriptionXmlContent().getBytes();
        }
        return bArr;
    }

    private void httpGetRequestRecieved(HTTPRequest httpReq) {
        String uri = httpReq.getURI();
        Debug.message("httpGetRequestRecieved = " + uri);
        if (uri == null) {
            httpReq.returnBadRequest();
            return;
        }
        if (uri.startsWith("/description.xml_urn:")) {
            uri = uri.replace("/description.xml_urn:", "/_urn:");
            Debug.message("redirect uri = " + uri);
        }
        byte[] fileByte = new byte[0];
        Object findObject = null;
        try {
            findObject = this.cacheMap.get(uri);
        } catch (Exception e) {
        }
        if (findObject != null) {
            fileByte = (byte[]) findObject;
        } else if (isDescriptionURI(uri)) {
            String localAddr = httpReq.getLocalAddress();
            if (localAddr == null || localAddr.length() <= 0) {
                localAddr = HostInterface.getInterface();
            }
            fileByte = getDescriptionData(localAddr);
            Debug.message("httpGetRequestReceived fresh cacheMap");
            this.cacheMap.put(uri, fileByte);
        } else {
            Device embDev = getDeviceByDescriptionURI(uri);
            if (embDev != null) {
                fileByte = embDev.getDescriptionData(httpReq.getLocalAddress());
                this.cacheMap.put(uri, fileByte);
            } else {
                Service embService = getServiceBySCPDURL(uri);
                if (embService != null) {
                    Debug.message("uri:" + uri);
                    fileByte = embService.getSCPDData();
                    this.cacheMap.put(uri, fileByte);
                } else if (uri.contains(Icon.ELEM_NAME)) {
                    fileByte = FileUtil.load(this.mIconPath);
                    this.cacheMap.put(uri, fileByte);
                } else {
                    httpReq.returnBadRequest();
                    return;
                }
            }
        }
        HTTPResponse httpRes = new HTTPResponse();
        if (FileUtil.isXMLFileName(uri)) {
            httpRes.setContentType("text/xml; charset=\"utf-8\"");
        } else {
            httpRes.setContentType("image/png");
        }
        httpRes.setStatusCode(200);
        httpRes.setContent(fileByte);
        httpRes.setConnection(HTTP.CLOSE);
        httpReq.post(httpRes);
    }

    private void httpPostRequestRecieved(HTTPRequest httpReq) {
        if (httpReq.isSOAPAction()) {
            soapActionRecieved(httpReq);
        } else {
            httpReq.returnBadRequest();
        }
    }

    private void soapBadActionRecieved(HTTPRequest soapReq) {
        SOAPResponse soapRes = new SOAPResponse();
        soapRes.setStatusCode(400);
        soapReq.post(soapRes);
    }

    private void soapActionRecieved(HTTPRequest soapReq) {
        Service ctlService = getServiceByControlURL(soapReq.getURI());
        if (ctlService != null) {
            deviceControlRequestRecieved(new ActionRequest(soapReq), ctlService);
        } else {
            soapBadActionRecieved(soapReq);
        }
    }

    private void deviceControlRequestRecieved(ControlRequest ctlReq, Service service) {
        if (ctlReq.isQueryControl()) {
            deviceQueryControlRecieved(new QueryRequest(ctlReq), service);
        } else {
            deviceActionControlRecieved(new ActionRequest(ctlReq), service);
        }
    }

    private void invalidActionControlRecieved(ControlRequest ctlReq) {
        ControlResponse actRes = new ActionResponse();
        actRes.setFaultResponse(UPnPStatus.INVALID_ACTION);
        ctlReq.post(actRes);
    }

    private void invalidArgumentsControlRecieved(ControlRequest ctlReq) {
        ControlResponse actRes = new ActionResponse();
        actRes.setFaultResponse(402);
        ctlReq.post(actRes);
    }

    private synchronized void deviceActionControlRecieved(ActionRequest ctlReq, Service service) {
        if (Debug.isOn()) {
            ctlReq.print();
        }
        String actionName = ctlReq.getActionName();
        if (actionName == null || actionName.equals("")) {
            actionName = "";
            Debug.message("Failed to parse the action name...read it from origin data...");
            String[] tempStr = ctlReq.getSOAPAction().split("#");
            if (tempStr.length == 2) {
                actionName = tempStr[1];
            }
        }
        Debug.message("Action Name: " + actionName);
        Action action = service.getAction(actionName);
        if (action == null) {
            invalidActionControlRecieved(ctlReq);
        } else {
            ArgumentList actionArgList = action.getArgumentList();
            ArgumentList reqArgList = ctlReq.getArgumentList();
            if (reqArgList != null) {
                Argument tmpArg = reqArgList.getArgument(AVTransportConstStr.CURRENTURI);
                if (tmpArg != null && tmpArg.getValue().contains("http://:")) {
                    String tmp = tmpArg.getValue().replace("http://:", "http:/" + ctlReq.getSocket().getSocket().getInetAddress().toString() + SOAP.DELIM);
                    Debug.message("Workaroud for QQ music...URI=" + tmp);
                    tmpArg.setValue(tmp);
                }
            }
            if (reqArgList == null) {
                Debug.message("Failed to parse the action node...read it from origin data...");
                if (actionName.equals(AVTransportConstStr.SETAVTRANSPORTURI)) {
                    Debug.message("And yes, this is SetAVTransportURI...");
                    String content = ctlReq.getContentString();
                    String[] tmpStr1 = content.split("<CurrentURI>");
                    if (tmpStr1.length >= 2) {
                        String[] tmpStr2 = tmpStr1[1].split("</CurrentURI>");
                        if (tmpStr2.length >= 1) {
                            Debug.message("Add CurrentURI = " + tmpStr2[0]);
                            reqArgList = new ArgumentList();
                            Argument arg = new Argument();
                            arg.setName("InstanceID");
                            arg.setValue("0");
                            reqArgList.add(arg);
                            arg = new Argument();
                            arg.setName(AVTransportConstStr.CURRENTURI);
                            arg.setValue(tmpStr2[0]);
                            reqArgList.add(arg);
                            String[] tmpStr3 = content.split("<CurrentURIMetaData>");
                            if (tmpStr3.length >= 2) {
                                String[] tmpStr4 = tmpStr3[1].split("</CurrentURIMetaData>");
                                if (tmpStr4.length >= 1) {
                                    Debug.message("Add CurrentURIMetaData = " + tmpStr4[0]);
                                    arg = new Argument();
                                    arg.setName(AVTransportConstStr.CURRENTURIMETADATA);
                                    arg.setValue(tmpStr4[0]);
                                    reqArgList.add(arg);
                                } else {
                                    arg = new Argument();
                                    arg.setName(AVTransportConstStr.CURRENTURIMETADATA);
                                    arg.setValue("");
                                    reqArgList.add(arg);
                                }
                            } else {
                                arg = new Argument();
                                arg.setName(AVTransportConstStr.CURRENTURIMETADATA);
                                arg.setValue("");
                                reqArgList.add(arg);
                            }
                        }
                    }
                }
            }
            if (reqArgList == null) {
                Debug.message("[ERROR] deviceActionControlRecieved reqArgList == null");
                invalidArgumentsControlRecieved(ctlReq);
            } else {
                try {
                    actionArgList.setReqArgs(reqArgList);
                    if (!action.performActionListener(ctlReq)) {
                        invalidActionControlRecieved(ctlReq);
                    }
                } catch (Exception ex) {
                    Debug.message("[ERROR] deviceActionControlRecieved setReqArgs Exception");
                    ex.printStackTrace();
                    invalidArgumentsControlRecieved(ctlReq);
                }
            }
        }
    }

    private void deviceQueryControlRecieved(QueryRequest ctlReq, Service service) {
        if (Debug.isOn()) {
            ctlReq.print();
        }
        String varName = ctlReq.getVarName();
        if (!service.hasStateVariable(varName)) {
            invalidActionControlRecieved(ctlReq);
        } else if (!getStateVariable(varName).performQueryListener(ctlReq, false)) {
            invalidActionControlRecieved(ctlReq);
        }
    }

    private void upnpBadSubscriptionRecieved(SubscriptionRequest subReq, int code) {
        SubscriptionResponse subRes = new SubscriptionResponse();
        subRes.setErrorResponse(code);
        subReq.post(subRes);
    }

    private void deviceEventSubscriptionRecieved(SubscriptionRequest subReq) {
        Service service = getServiceByEventSubURL(subReq.getURI());
        if (service == null) {
            subReq.returnBadRequest();
        } else if (!subReq.hasCallback() && !subReq.hasSID()) {
            upnpBadSubscriptionRecieved(subReq, 412);
        } else if (subReq.isUnsubscribeRequest()) {
            Debug.message("sub: receive unsub");
            deviceEventUnsubscriptionRecieved(service, subReq);
        } else if (subReq.hasCallback()) {
            Debug.message("sub: receive sub");
            deviceEventNewSubscriptionRecieved(service, subReq);
        } else if (subReq.hasSID()) {
            Debug.message("sub: receive resub");
            deviceEventRenewSubscriptionRecieved(service, subReq);
        } else {
            upnpBadSubscriptionRecieved(subReq, 412);
        }
    }

    private void deviceEventNewSubscriptionRecieved(Service service, SubscriptionRequest subReq) {
        String callback = subReq.getCallback();
        try {
            URL url = new URL(callback);
            long timeOut = subReq.getTimeout();
            String sid = Subscription.createSID();
            Subscriber sub = new Subscriber();
            sub.setDeliveryURL(callback);
            sub.setTimeOut(timeOut);
            sub.setSID(sid);
            String gid = subReq.getGID();
            boolean external = false;
            if (gid != null && gid.length() > 0) {
                Debug.message("sub: subscribe received with external true");
                external = true;
            }
            service.addSubscriber(sub, external);
            SubscriptionResponse subRes = new SubscriptionResponse();
            subRes.setStatusCode(200);
            subRes.setSID(sid);
            subRes.setTimeout(timeOut);
            if (Debug.isOn()) {
                subRes.print();
            }
            subReq.post(subRes);
            if (Debug.isOn()) {
                subRes.print();
            }
            service.notifyAllStateVariables(external);
        } catch (Exception e) {
            upnpBadSubscriptionRecieved(subReq, 412);
        }
    }

    private void deviceEventRenewSubscriptionRecieved(Service service, SubscriptionRequest subReq) {
        String sid = subReq.getSID();
        String gid = subReq.getGID();
        boolean external = false;
        if (gid != null && gid.length() > 0) {
            Debug.message("sub: renew subscribe received with external true");
            external = true;
        }
        Subscriber sub = service.getSubscriber(sid, external);
        if (sub == null) {
            upnpBadSubscriptionRecieved(subReq, 412);
            return;
        }
        long timeOut = subReq.getTimeout();
        sub.setTimeOut(timeOut);
        sub.renew();
        SubscriptionResponse subRes = new SubscriptionResponse();
        subRes.setStatusCode(200);
        subRes.setSID(sid);
        subRes.setTimeout(timeOut);
        subReq.post(subRes);
        if (Debug.isOn()) {
            subRes.print();
        }
    }

    private void deviceEventUnsubscriptionRecieved(Service service, SubscriptionRequest subReq) {
        String sid = subReq.getSID();
        String gid = subReq.getGID();
        boolean external = false;
        if (gid != null && gid.length() > 0) {
            Debug.message("sub: renew subscribe received with external true");
            external = true;
        }
        Subscriber sub = service.getSubscriber(sid, external);
        if (sub == null) {
            upnpBadSubscriptionRecieved(subReq, 412);
            return;
        }
        service.removeSubscriber(sub, external);
        SubscriptionResponse subRes = new SubscriptionResponse();
        subRes.setStatusCode(200);
        subReq.post(subRes);
        if (Debug.isOn()) {
            subRes.print();
        }
    }

    private HTTPServerList getHTTPServerList() {
        return getDeviceData().getHTTPServerList();
    }

    private GalaHttpServerList getGalaHttpServerList() {
        return getDeviceData().getGalaHttpServerList();
    }

    public void setSSDPPort(int port) {
        getDeviceData().setSSDPPort(port);
    }

    public int getSSDPPort() {
        return getDeviceData().getSSDPPort();
    }

    public void setSSDPBindAddress(InetAddress[] inets) {
        getDeviceData().setSSDPBindAddress(inets);
    }

    public InetAddress[] getSSDPBindAddress() {
        return getDeviceData().getSSDPBindAddress();
    }

    public void setMulticastIPv4Address(String ip) {
        getDeviceData().setMulticastIPv4Address(ip);
    }

    public String getMulticastIPv4Address() {
        return getDeviceData().getMulticastIPv4Address();
    }

    public void setMulticastIPv6Address(String ip) {
        getDeviceData().setMulticastIPv6Address(ip);
    }

    public String getMulticastIPv6Address() {
        return getDeviceData().getMulticastIPv6Address();
    }

    private SSDPSearchSocketList getSSDPSearchSocketList() {
        return getDeviceData().getSSDPSearchSocketList();
    }

    private void setAdvertiser(Advertiser adv) {
        getDeviceData().setAdvertiser(adv);
    }

    private Advertiser getAdvertiser() {
        return getDeviceData().getAdvertiser();
    }

    public void setInterfaceAddress(String ip) {
        this.mInterfaceAddress = ip;
    }

    public String getInterfaceAddress() {
        SSDPPacket ssdpPacket = getSSDPPacket();
        return ssdpPacket == null ? this.mInterfaceAddress : ssdpPacket.getLocalAddress();
    }

    public void setActionListener(ActionListener listener) {
        ServiceList serviceList = getServiceList();
        int nServices = serviceList.size();
        for (int n = 0; n < nServices; n++) {
            serviceList.getService(n).setActionListener(listener);
        }
    }

    public void setQueryListener(QueryListener listener) {
        ServiceList serviceList = getServiceList();
        int nServices = serviceList.size();
        for (int n = 0; n < nServices; n++) {
            serviceList.getService(n).setQueryListener(listener);
        }
    }

    public void setActionListener(ActionListener listener, boolean includeSubDevices) {
        setActionListener(listener);
        if (includeSubDevices) {
            DeviceList devList = getDeviceList();
            int devCnt = devList.size();
            for (int n = 0; n < devCnt; n++) {
                devList.getDevice(n).setActionListener(listener, true);
            }
        }
    }

    public void setQueryListener(QueryListener listener, boolean includeSubDevices) {
        setQueryListener(listener);
        if (includeSubDevices) {
            DeviceList devList = getDeviceList();
            int devCnt = devList.size();
            for (int n = 0; n < devCnt; n++) {
                devList.getDevice(n).setQueryListener(listener, true);
            }
        }
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }

    public void setIpAddress(String ip) {
        this.mIpAddress = ip;
    }

    public String getIpAddress() {
        String addr = null;
        try {
            addr = getSSDPPacket().getRemoteAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TextUtils.isEmpty(addr) ? this.mIpAddress : addr;
    }
}
