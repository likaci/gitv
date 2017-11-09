package org.cybergarage.upnp;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.DeviceName;
import com.gala.android.dlna.sdk.SDKVersion;
import com.gala.android.dlna.sdk.controlpoint.DeviceType;
import com.gala.android.dlna.sdk.controlpoint.MediaControlPoint;
import com.gala.android.dlna.sdk.controlpoint.TVGuoDescription;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.android.sdk.dlna.keeper.ControlPointKeeper;
import com.gala.android.sdk.dlna.keeper.DmcInforKeeper;
import com.gala.android.sdk.dlna.keeper.DmrInfor;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.net.HostInterface;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.control.RenewSubscriber;
import org.cybergarage.upnp.device.AddDeviceFailedListener;
import org.cybergarage.upnp.device.DeviceChangeListener;
import org.cybergarage.upnp.device.Disposer;
import org.cybergarage.upnp.device.NotifyListener;
import org.cybergarage.upnp.device.SearchResponseListener;
import org.cybergarage.upnp.device.USN;
import org.cybergarage.upnp.event.EventListener;
import org.cybergarage.upnp.event.NotifyRequest;
import org.cybergarage.upnp.event.Property;
import org.cybergarage.upnp.event.PropertyList;
import org.cybergarage.upnp.event.SubscriptionRequest;
import org.cybergarage.upnp.event.SubscriptionResponse;
import org.cybergarage.upnp.ssdp.SSDPNotifySocketList;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchRequest;
import org.cybergarage.upnp.ssdp.SSDPSearchResponseSocketList;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;
import org.cybergarage.util.Mutex;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.NodeList;

public class ControlPoint implements HTTPRequestListener {
    private static final int DEFAULT_EVENTSUB_PORT = 8058;
    private static final String DEFAULT_EVENTSUB_URI = "/evetSub";
    private static final int DEFAULT_EXPIRED_DEVICE_MONITORING_INTERVAL = 5;
    private static final int DEFAULT_SSDP_PORT = 53204;
    private static final String DEVICE_FOUND_FAILED_INFO_FORMAT = "udn=%s&isIGALA=%s&server=%s&myName=%s&cause=%s";
    private static final String DLNA_DEVICE_TYPE = "urn:schemas-upnp-org:device:MediaRenderer:1";
    private static final String DLNA_MEDIARENDERER = "MediaRenderer";
    private static final String DLNA_MEDIASERVER = "MediaServer";
    private static final String DLNA_ROUTER = "InternetGatewayDevice";
    private static final String DLNA_URN = "urn:schemas-upnp-org:device:";
    private static final String EXTERNAL_VALUE = "external";
    private static final String LOG_TAG = ControlPoint.class.getSimpleName();
    public static boolean isOpenRealTime = false;
    public static long maxDelayTime = -1;
    private int DEFAULT_SEARCH_INTERVAL;
    private NodeList allDevNodeList;
    public Device currentControlDevice;
    private NodeList devNodeList;
    ListenerList deviceChangeListenerList;
    private Disposer deviceDisposer;
    private ListenerList deviceNotifyListenerList;
    private ListenerList deviceSearchResponseListenerList;
    private ListenerList eventListenerList;
    private String eventSubURI;
    private long expiredDeviceMonitoringInterval;
    private DeviceType findDeviceType;
    private int httpPort;
    private HTTPServerList httpServerList;
    public boolean isAppSleep;
    private AddDeviceFailedListener mAddDeviceFailedListener;
    public boolean mExternalGID;
    private final HashSet<String> mFoundFaiedDeviceList;
    public boolean mLongforKeepAlive;
    private final HashSet<String> mRouterDeviceList;
    private Mutex mutex;
    private boolean nmprMode;
    private RenewSubscriber renewSubscriber;
    private int searchMx;
    private SSDPNotifySocketList ssdpNotifySocketList;
    private int ssdpPort;
    private SSDPSearchResponseSocketList ssdpSearchResponseSocketList;
    private long subTimeout;
    private Object userData;
    private String uuid;
    private List<String> whiteList;

    static {
        UPnP.initialize();
    }

    public void setExternalApp(boolean external) {
        this.mExternalGID = external;
    }

    public boolean getExternalApp() {
        return this.mExternalGID;
    }

    public void setSubscriberTimeout(long time) {
        this.subTimeout = time;
    }

    public DeviceType getFindDeviceType() {
        return this.findDeviceType;
    }

    public void setFindDeviceType(DeviceType findDeviceType) {
        if (findDeviceType == null) {
            findDeviceType = DeviceType.MEDIA_ALL;
        }
        this.findDeviceType = findDeviceType;
    }

    private SSDPNotifySocketList getSSDPNotifySocketList() {
        return this.ssdpNotifySocketList;
    }

    private SSDPSearchResponseSocketList getSSDPSearchResponseSocketList() {
        return this.ssdpSearchResponseSocketList;
    }

    public ControlPoint(int ssdpPort, int httpPort, InetAddress[] binds) {
        this.findDeviceType = DeviceType.MEDIA_RENDERER;
        this.currentControlDevice = null;
        this.isAppSleep = false;
        this.mLongforKeepAlive = true;
        this.DEFAULT_SEARCH_INTERVAL = 5000;
        this.subTimeout = -1;
        this.mExternalGID = false;
        this.whiteList = null;
        this.uuid = null;
        this.mutex = new Mutex();
        this.ssdpPort = 0;
        this.httpPort = 0;
        this.devNodeList = new NodeList();
        this.allDevNodeList = new NodeList();
        this.mRouterDeviceList = new HashSet();
        this.mFoundFaiedDeviceList = new HashSet();
        this.deviceNotifyListenerList = new ListenerList();
        this.deviceSearchResponseListenerList = new ListenerList();
        this.deviceChangeListenerList = new ListenerList();
        this.searchMx = 10;
        this.httpServerList = new HTTPServerList();
        this.eventListenerList = new ListenerList();
        this.eventSubURI = DEFAULT_EVENTSUB_URI;
        this.userData = null;
        this.ssdpNotifySocketList = new SSDPNotifySocketList(binds);
        this.ssdpSearchResponseSocketList = new SSDPSearchResponseSocketList(binds);
        setSSDPPort(ssdpPort);
        setHTTPPort(httpPort);
        setDeviceDisposer(null);
        setExpiredDeviceMonitoringInterval(5);
        setNMPRMode(false);
        setRenewSubscriber(null);
    }

    public ControlPoint(int ssdpPort, int httpPort) {
        this(ssdpPort, httpPort, null);
    }

    public ControlPoint() {
        this(DEFAULT_SSDP_PORT, DEFAULT_EVENTSUB_PORT);
    }

    public void finalize() {
        stop();
    }

    public void setExternalFilesDir(String ExternalFilesDir) {
        ControlPointKeeper.getInstance().setExternalFilesDir(ExternalFilesDir);
        DmcInforKeeper.getInstance().setExternalFilesDir(ExternalFilesDir);
        Debug.message("setExternalFilesDir" + ExternalFilesDir);
    }

    private List<String> getwhiteList() {
        if (this.whiteList == null) {
            this.whiteList = new ArrayList();
            this.whiteList.add(Util.getTag(true) + Util.FUNCTION_TAG_DLNA);
            this.whiteList.add("NewDLNA");
        }
        return this.whiteList;
    }

    public boolean isInWhiteList(String serverName) {
        if (serverName == null || serverName.length() == 0) {
            return false;
        }
        for (String name : getwhiteList()) {
            if (serverName.toLowerCase() != null && serverName.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getUUID() {
        if (this.uuid == null) {
            this.uuid = ControlPointKeeper.getInstance().getUUID();
            if (this.uuid == null || this.uuid.length() == 0) {
                this.uuid = UPnP.createUUID();
                ControlPointKeeper.getInstance().Save(this.uuid);
            }
        }
        return this.uuid;
    }

    public String getConstructionData(byte data) {
        return getUUID() + "#" + (System.currentTimeMillis() + 0) + "#" + ((char) data) + "\n";
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public int getSSDPPort() {
        return this.ssdpPort;
    }

    public void setSSDPPort(int port) {
        this.ssdpPort = port;
    }

    public int getHTTPPort() {
        return this.httpPort;
    }

    public void setHTTPPort(int port) {
        this.httpPort = port;
    }

    public void setNMPRMode(boolean flag) {
        this.nmprMode = flag;
    }

    public boolean isNMPRMode() {
        return this.nmprMode;
    }

    private void addDevice(Node rootNode) {
        if (this.devNodeList == null) {
            this.devNodeList = new NodeList();
        }
        this.devNodeList.add(rootNode);
    }

    private void addDeviceToAllDeviceList(Node rootNode) {
        if (this.allDevNodeList == null) {
            this.allDevNodeList = new NodeList();
        }
        this.allDevNodeList.add(rootNode);
    }

    private void addRouterDevice(String udn) {
        this.mRouterDeviceList.add(udn);
    }

    private boolean isInRouterDeviceList(String udn) {
        if (this.mRouterDeviceList.isEmpty()) {
            return false;
        }
        return this.mRouterDeviceList.contains(udn);
    }

    private void addFoundFailedDevice(String udn) {
        this.mFoundFaiedDeviceList.add(udn);
    }

    private boolean isInFoundFailedDeviceList(String udn) {
        if (this.mFoundFaiedDeviceList.isEmpty()) {
            return false;
        }
        return this.mFoundFaiedDeviceList.contains(udn);
    }

    private boolean isMediaRenderer(Device device) {
        return device.getDeviceType().contains("urn:schemas-upnp-org:device:MediaRenderer:1");
    }

    private boolean isGALAMediaRenderer(Device device) {
        return device.getDeviceType().contains(Device.IGALA_DEVICE);
    }

    protected void addDeviceByType(Device rootDev, Node rootNode) {
        Debug.message("addDeviceByType() ");
        addDeviceToAllDeviceList(rootNode);
        if (rootDev != null && rootDev.isGalaServer()) {
            addDevice(rootNode);
            performAddDeviceListener(rootDev);
        }
    }

    private void addStandardDLNADevice(SSDPPacket ssdpPacket, String udn, String location) {
        Debug.message(LOG_TAG + "Add standard DLNA device udn: " + udn + " device remote address: " + ssdpPacket.getRemoteAddress());
        Device devInAllDeviceList = getDeviceByAllDeviceList(udn);
        if (devInAllDeviceList != null) {
            devInAllDeviceList.setSSDPPacket(ssdpPacket);
            String friendName = ssdpPacket.getFriendlyName();
            if (TextUtils.isEmpty(friendName)) {
                try {
                    friendName = UPnP.getXMLParser().parse(new URL(location)).getNode(Device.ELEM_NAME).getNodeValue("friendlyName");
                } catch (Exception ex) {
                    Debug.warning(ex);
                    ex.printStackTrace();
                    return;
                }
            }
            Device device = getDevice(udn);
            boolean isFriendNameChange = (TextUtils.isEmpty(friendName) || friendName.equals(devInAllDeviceList.getFriendlyName())) ? false : true;
            if (device != null) {
                device.setSSDPPacket(ssdpPacket);
                if (isFriendNameChange) {
                    device.setInternalFriendlyName(friendName);
                }
            }
            if (isFriendNameChange) {
                devInAllDeviceList.setInternalFriendlyName(friendName);
                performUpdatedDeviceListener(devInAllDeviceList);
                return;
            }
            return;
        }
        try {
            Debug.message(LOG_TAG + "addStandardDLNADevice location: " + location);
            Node rootNode = UPnP.getXMLParser().parse(new URL(location));
            Device rootDev = getDevice(rootNode);
            if (rootDev != null) {
                rootDev.setSSDPPacket(ssdpPacket);
                if (rootDev.getDeviceType().contains(DLNA_ROUTER)) {
                    addRouterDevice(udn);
                } else if (Util.isStdDmrDevice(rootDev)) {
                    if (getFindDeviceType() == DeviceType.MEDIA_RENDERER) {
                        if (!(rootDev.getDeviceType().contains("urn:schemas-upnp-org:device:") && rootDev.getDeviceType().contains(DLNA_MEDIARENDERER))) {
                            return;
                        }
                    } else if (getFindDeviceType() == DeviceType.MEDIA_SERVER) {
                        if (!rootDev.getDeviceType().contains("urn:schemas-upnp-org:device:")) {
                            return;
                        }
                        if (!rootDev.getDeviceType().contains(DLNA_MEDIASERVER)) {
                            return;
                        }
                    }
                    if (rootDev.isSupportService(Service.SERVICE_TYPE_AVTRANSPORT)) {
                        addDeviceToAllDeviceList(rootNode);
                        addDevice(rootNode);
                        rootDev.setDeviceName(DeviceName.MEDIA_RENDERER);
                        performAddDeviceListener(rootDev);
                        return;
                    }
                    Debug.message(LOG_TAG + "the device not support AVTransport,IGNORE!");
                }
            }
        } catch (Exception me) {
            Debug.warning(ssdpPacket.toString());
            Debug.warning(me);
            onAddDeviceFailed(ssdpPacket, udn, me.toString());
        } catch (Exception pe) {
            onAddDeviceFailed(ssdpPacket, udn, pe.toString());
            Debug.warning(ssdpPacket.toString());
            Debug.warning(pe);
        }
    }

    private synchronized void addDevice(SSDPPacket ssdpPacket) {
        Debug.message(LOG_TAG + "Current search type: " + getFindDeviceType());
        if (ssdpPacket == null) {
            Debug.message(LOG_TAG + "Ssdp packet is null,IGNOR!");
        } else if (DeviceType.MEDIA_GALA.equals(getFindDeviceType()) && !ssdpPacket.isGalaServer()) {
            Debug.message(LOG_TAG + "Search MEDIA_GALA But is not GalaServer,IGNOR!");
        } else if (ssdpPacket.isRootDevice()) {
            String udn = USN.getUDN(ssdpPacket.getUSN());
            String l = ssdpPacket.getLocation();
            Debug.message(LOG_TAG + "addDevice(): udn =" + udn + "location= " + l);
            if (ssdpPacket.isRounterServer() || isInRouterDeviceList(udn)) {
                Debug.message(LOG_TAG + "Is RounterServer,IGNOR!");
            } else if (isInWhiteList(ssdpPacket.getServer())) {
                Device dev = getDevice(udn);
                if (dev != null) {
                    dev.setSSDPPacket(ssdpPacket);
                    if (!(ssdpPacket.getFriendlyName() == null || "".equals(ssdpPacket.getFriendlyName()) || dev.getFriendlyName().compareTo(ssdpPacket.getFriendlyName()) == 0)) {
                        dev.setInternalFriendlyName(ssdpPacket.getFriendlyName());
                        performUpdatedDeviceListener(dev);
                    }
                } else {
                    try {
                        Node rootNode;
                        Device rootDev;
                        if (ssdpPacket.isGalaServer()) {
                            Debug.message(LOG_TAG + "addDevice() p1");
                            DmrInfor dmrInfor = DmcInforKeeper.getInstance().getDmrInfor(udn);
                            if (dmrInfor == null || dmrInfor.getFileMd5().compareTo(ssdpPacket.getFileMd5()) != 0) {
                                Debug.message(LOG_TAG + "addDevice() p2");
                                if (ssdpPacket.getGalaDeviceType() == DeviceName.IGALA_DONGLE) {
                                    rootNode = UPnP.getXMLParser().parse(TVGuoDescription.construct(ssdpPacket.getFriendlyName(), ssdpPacket.getUSN().split("::")[0]));
                                } else {
                                    rootNode = UPnP.getXMLParser().parse(new URL(ssdpPacket.getLocation()));
                                }
                                rootDev = getDevice(rootNode);
                                if (rootDev != null) {
                                    rootDev.setSSDPPacket(ssdpPacket);
                                    rootDev.setDeviceVersion(ssdpPacket.getGalaVersion());
                                    rootDev.setDeviceName(ssdpPacket.getGalaDeviceType());
                                    rootDev.setGalaDeviceVersion(ssdpPacket.getGalaDeviceVersion());
                                    Debug.message(LOG_TAG + "addDevice() devFriendlyname: " + rootDev.getFriendlyName() + " IGALADEVICE:" + rootDev.getDeviceName() + " IGALAVERSION:" + rootDev.getDeviceVersion() + "DEVICEVERSION:" + rootDev.getGalaDeviceVersion());
                                    addDeviceByType(rootDev, rootNode);
                                    DmrInfor saveDmrInfor = new DmrInfor();
                                    saveDmrInfor.setUuid(udn);
                                    saveDmrInfor.setFileMd5(ssdpPacket.getFileMd5());
                                    saveDmrInfor.setDescriptionFileXml(rootDev.getDescriptionXml());
                                    DmcInforKeeper.getInstance().SaveDmrInfor(saveDmrInfor);
                                }
                            } else if (dmrInfor != null && dmrInfor.getFileMd5().compareTo(ssdpPacket.getFileMd5()) == 0) {
                                Debug.message(LOG_TAG + "addDevice() p3");
                                rootNode = UPnP.getXMLParser().parse(dmrInfor.getDescriptionFileXml());
                                rootDev = getDevice(rootNode);
                                if (rootDev != null) {
                                    rootDev.setSSDPPacket(ssdpPacket);
                                    rootDev.setDeviceVersion(ssdpPacket.getGalaVersion());
                                    rootDev.setDeviceName(ssdpPacket.getGalaDeviceType());
                                    rootDev.setGalaDeviceVersion(ssdpPacket.getGalaDeviceVersion());
                                    rootDev.setInternalFriendlyName(ssdpPacket.getFriendlyName());
                                    addDeviceByType(rootDev, rootNode);
                                }
                            }
                        }
                        Debug.message(LOG_TAG + "addDevice() p4");
                        rootNode = UPnP.getXMLParser().parse(new URL(ssdpPacket.getLocation()));
                        rootDev = getDevice(rootNode);
                        if (rootDev != null) {
                            rootDev.setSSDPPacket(ssdpPacket);
                            addDeviceByType(rootDev, rootNode);
                        }
                    } catch (Exception e) {
                        onAddDeviceFailed(ssdpPacket, udn, e.toString());
                        e.printStackTrace();
                        Debug.warning(ssdpPacket.toString());
                    }
                }
            } else {
                Debug.message(LOG_TAG + "Is not in WhiteList,IGNOR!");
                if (getFindDeviceType() != DeviceType.MEDIA_GALA) {
                    addStandardDLNADevice(ssdpPacket, udn, l);
                }
            }
        } else {
            Debug.message(LOG_TAG + "Is not RootDevice,IGNOR!");
        }
    }

    protected Device getDevice(Node rootNode) {
        if (rootNode == null) {
            return null;
        }
        Node devNode = rootNode.getNode(Device.ELEM_NAME);
        if (devNode != null) {
            return new Device(rootNode, devNode);
        }
        return null;
    }

    public DeviceList getDeviceList() {
        DeviceList devList = new DeviceList();
        int nRoots = this.devNodeList.size();
        for (int n = 0; n < nRoots; n++) {
            Device dev = getDevice(this.devNodeList.getNode(n));
            if (dev != null) {
                devList.add(dev);
            }
        }
        return devList;
    }

    public Device getDevice(String name) {
        int nRoots = this.devNodeList.size();
        for (int n = 0; n < nRoots; n++) {
            Device dev = getDevice(this.devNodeList.getNode(n));
            if (dev != null) {
                if (dev.isDevice(name)) {
                    return dev;
                }
                Device cdev = dev.getDevice(name);
                if (cdev != null) {
                    return cdev;
                }
            }
        }
        return null;
    }

    public Device getDeviceByAllDeviceList(String name) {
        int nRoots = this.allDevNodeList.size();
        for (int n = 0; n < nRoots; n++) {
            Device dev = getDevice(this.allDevNodeList.getNode(n));
            if (dev != null) {
                if (dev.isDevice(name)) {
                    return dev;
                }
                Device cdev = dev.getDevice(name);
                if (cdev != null) {
                    return cdev;
                }
            }
        }
        return null;
    }

    public boolean hasDevice(String name) {
        return getDevice(name) != null;
    }

    private synchronized void removeDevice(Node rootNode) {
        Device dev = getDevice(rootNode);
        if (dev != null) {
            Service service = dev.getPrivateServer();
            if (service != null) {
                Debug.message("++++removeDevice() " + dev.getFriendlyName() + " clear SID: " + service.getSID());
                service.clearSID();
            }
        }
        Debug.message("++++removeDevice() " + rootNode.getName() + " " + dev.getFriendlyName());
        if (hasDevice(dev.getUDN())) {
            Node tempNode;
            if (dev != null) {
                if (dev.isRootDevice()) {
                    tempNode = rootNode;
                    Debug.message("++++removeDevice() " + dev.getFriendlyName() + " " + dev.getLocation() + " " + dev.getUDN());
                    this.devNodeList.remove(rootNode);
                    if (this.allDevNodeList != null) {
                        this.allDevNodeList.remove(tempNode);
                    }
                    performRemoveDeviceListener(dev);
                }
            }
            Debug.message("!!!!!!!!!!!!!! removeDevice error dev" + dev + " dev.isRootDevice():" + dev.isRootDevice());
            tempNode = rootNode;
            this.devNodeList.remove(rootNode);
            if (this.allDevNodeList != null) {
                this.allDevNodeList.remove(tempNode);
            }
        } else {
            Debug.message("++++removeDevice() skip! " + dev.getFriendlyName() + " already removed.");
        }
    }

    protected void removeDevice(Device dev) {
        if (dev != null) {
            removeDevice(dev.getRootNode());
        }
    }

    protected void removeDevice(String name) {
        Device dev = getDevice(name);
        if (dev == null) {
            Debug.message("removeDevice: device [" + name + "] not found");
        } else {
            removeDevice(dev);
        }
    }

    private synchronized void removeDevice(SSDPPacket packet) {
        String usn = packet.getUSN();
        if (usn == null || "".equals(usn)) {
            Debug.message("++++20150615 removeDevice(SSDPPacket packet) usn == null || usn ==  ");
        } else {
            String udn = USN.getUDN(usn);
            if (udn == null || "".equals(udn)) {
                Debug.message("++++20150615 removeDevice(SSDPPacket packet) udn == null || udn ==  ");
            } else {
                removeDevice(udn);
            }
        }
    }

    private boolean isConnect(String address, int port) {
        Exception e;
        Throwable th;
        Socket socket = null;
        try {
            Socket socket2 = new Socket();
            try {
                socket2.connect(new InetSocketAddress(address, port), 2500);
                try {
                    socket2.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                return true;
            } catch (Exception e3) {
                e2 = e3;
                socket = socket2;
                try {
                    e2.printStackTrace();
                    Debug.message("connect server failur,must be Expired!");
                    try {
                        socket.close();
                    } catch (Exception e22) {
                        e22.printStackTrace();
                    }
                    return false;
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        socket.close();
                    } catch (Exception e222) {
                        e222.printStackTrace();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                socket = socket2;
                socket.close();
                throw th;
            }
        } catch (Exception e4) {
            e222 = e4;
            e222.printStackTrace();
            Debug.message("connect server failur,must be Expired!");
            socket.close();
            return false;
        }
    }

    public boolean isCurrentDeviceConnected() {
        MediaControlPoint controlPoint = (MediaControlPoint) this;
        if (System.currentTimeMillis() - controlPoint.getLastResponseTime() > IOpenApiCommandHolder.OAA_CONNECT_INTERVAL && controlPoint.sendMessage("test", true) == null) {
            return false;
        }
        return true;
    }

    public void removeExpiredDevices() {
        int n;
        Debug.message("removeExpiredDevices: check expired devices...");
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        Device[] dev = new Device[devCnt];
        for (n = 0; n < devCnt; n++) {
            dev[n] = devList.getDevice(n);
        }
        n = 0;
        while (n < devCnt) {
            if (this.currentControlDevice != null && this.currentControlDevice.getRootNode() == dev[n].getRootNode()) {
                Debug.message("Check current device connection before expiration check: " + this.currentControlDevice.getFriendlyName());
                if (isCurrentDeviceConnected()) {
                    try {
                        this.currentControlDevice.sendDataToHostTokeepAlive(getConstructionData((byte) 17));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (dev[n].isExpired()) {
                try {
                    String location = dev[n].getLocation();
                    URL locationUrl = new URL(location);
                    String adderss = locationUrl.getHost();
                    int port = locationUrl.getPort();
                    Debug.message("location:" + location + " host:" + adderss + " port:" + port);
                    if (isConnect(adderss, port)) {
                        Device tempDev = getDevice(dev[n].getUDN());
                        if (tempDev != null) {
                            long timeStamp = System.currentTimeMillis();
                            if (tempDev.getSSDPPacket() != null) {
                                Debug.message("device expired,but connected! update timestamp!！");
                                tempDev.getSSDPPacket().setTimeStamp(timeStamp);
                            }
                            tempDev.setTimeStamp(timeStamp);
                        }
                    } else {
                        Debug.message("Remove expired device: " + dev[n].getFriendlyName());
                        removeDevice(dev[n]);
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    Debug.message("Remove expired device: " + dev[n].getFriendlyName());
                    removeDevice(dev[n]);
                }
            }
            n++;
        }
    }

    public void setExpiredDeviceMonitoringInterval(long interval) {
        this.expiredDeviceMonitoringInterval = interval;
    }

    public long getExpiredDeviceMonitoringInterval() {
        return this.expiredDeviceMonitoringInterval;
    }

    public void setDeviceDisposer(Disposer disposer) {
        this.deviceDisposer = disposer;
    }

    public Disposer getDeviceDisposer() {
        return this.deviceDisposer;
    }

    public void addNotifyListener(NotifyListener listener) {
        if (listener != null) {
            this.deviceNotifyListenerList.add(listener);
        }
    }

    public void removeNotifyListener(NotifyListener listener) {
        if (listener != null) {
            this.deviceNotifyListenerList.remove(listener);
        }
    }

    public void performNotifyListener(SSDPPacket ssdpPacket) {
        int listenerSize = this.deviceNotifyListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            try {
                ((NotifyListener) this.deviceNotifyListenerList.get(n)).deviceNotifyReceived(ssdpPacket);
            } catch (Exception e) {
                Debug.warning("NotifyListener returned an error:", e);
            }
        }
    }

    public void addSearchResponseListener(SearchResponseListener listener) {
        this.deviceSearchResponseListenerList.add(listener);
    }

    public void removeSearchResponseListener(SearchResponseListener listener) {
        this.deviceSearchResponseListenerList.remove(listener);
    }

    public void performSearchResponseListener(SSDPPacket ssdpPacket) {
        int listenerSize = this.deviceSearchResponseListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            try {
                ((SearchResponseListener) this.deviceSearchResponseListenerList.get(n)).deviceSearchResponseReceived(ssdpPacket);
            } catch (Exception e) {
                Debug.warning("SearchResponseListener returned an error:", e);
            }
        }
    }

    public void addDeviceChangeListener(DeviceChangeListener listener) {
        this.deviceChangeListenerList.add(listener);
    }

    public void removeDeviceChangeListener(DeviceChangeListener listener) {
        this.deviceChangeListenerList.remove(listener);
    }

    public void performAddDeviceListener(Device dev) {
        int n;
        Debug.message("++++20150615performAddDeviceListener() device number:" + getDeviceList().size() + " deviceChange number:" + this.deviceChangeListenerList.size());
        int nRoots = this.devNodeList.size();
        for (n = 0; n < nRoots; n++) {
            Device mdev = getDevice(this.devNodeList.getNode(n));
            Debug.message("++++20150615performAddDeviceListener() " + n + SOAP.DELIM + mdev.getFriendlyName() + " " + mdev.getLocation() + " " + mdev.getUDN());
        }
        int listenerSize = this.deviceChangeListenerList.size();
        for (n = 0; n < listenerSize; n++) {
            ((DeviceChangeListener) this.deviceChangeListenerList.get(n)).deviceAdded(dev);
        }
    }

    public void performUpdatedDeviceListener(Device dev) {
        int listenerSize = this.deviceChangeListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            ((DeviceChangeListener) this.deviceChangeListenerList.get(n)).deviceUpdated(dev);
        }
    }

    public void performRemoveDeviceListener(Device dev) {
        int n;
        int listenerSize = this.deviceChangeListenerList.size();
        Debug.message("++++20150615performRemoveDeviceListener() device number:" + getDeviceList().size() + " deviceChange number:" + this.deviceChangeListenerList.size());
        int nRoots = this.devNodeList.size();
        for (n = 0; n < nRoots; n++) {
            Device mdev = getDevice(this.devNodeList.getNode(n));
            Debug.message("++++20150615performRemoveDeviceListener() " + n + SOAP.DELIM + mdev.getFriendlyName() + " " + mdev.getLocation() + " " + mdev.getUDN());
        }
        for (n = 0; n < listenerSize; n++) {
            ((DeviceChangeListener) this.deviceChangeListenerList.get(n)).deviceRemoved(dev);
        }
    }

    public void notifyReceived(SSDPPacket packet) {
        if (packet.isAlive()) {
            Debug.message("notifyReceived() " + packet.getMyName() + " " + packet.getNTS());
            addDevice(packet);
        } else if (packet.isByeBye()) {
            Debug.message("notifyReceived() " + packet.getMyName() + " " + packet.getNTS());
            removeDevice(packet);
        }
        performNotifyListener(packet);
    }

    public void searchResponseReceived(SSDPPacket packet) {
        addDevice(packet);
        performSearchResponseListener(packet);
    }

    public int getSearchMx() {
        return this.searchMx;
    }

    public void setSearchMx(int mx) {
        this.searchMx = mx;
    }

    public void search(String target, int mx) {
        getSSDPSearchResponseSocketList().post(new SSDPSearchRequest(target, mx));
    }

    public void search(String target) {
        search(target, 10);
    }

    public synchronized void search() {
        Debug.message("++++ControlPoint search");
        search("upnp:rootdevice", 10);
        Debug.message("----ControlPoint search");
    }

    private HTTPServerList getHTTPServerList() {
        return this.httpServerList;
    }

    public void httpRequestRecieved(HTTPRequest httpReq) {
        if (Debug.isOn()) {
            httpReq.print();
        }
        if (httpReq.isNotifyRequest()) {
            NotifyRequest notifyReq = new NotifyRequest(httpReq);
            String uuid = notifyReq.getSID();
            long seq = notifyReq.getSEQ();
            PropertyList props = notifyReq.getPropertyList();
            int propCnt = props.size();
            for (int n = 0; n < propCnt; n++) {
                Property prop = props.getProperty(n);
                performEventListener(uuid, seq, prop.getName(), prop.getValue());
            }
            httpReq.returnOK();
            return;
        }
        httpReq.returnBadRequest();
    }

    public void addEventListener(EventListener listener) {
        this.eventListenerList.add(listener);
    }

    public void removeEventListener(EventListener listener) {
        this.eventListenerList.remove(listener);
    }

    public void performEventListener(String uuid, long seq, String name, String value) {
        int listenerSize = this.eventListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            ((EventListener) this.eventListenerList.get(n)).eventNotifyReceived(uuid, seq, name, value);
        }
    }

    public String getEventSubURI() {
        return this.eventSubURI;
    }

    public void setEventSubURI(String url) {
        this.eventSubURI = url;
    }

    private String getEventSubCallbackURL(String host) {
        return HostInterface.getHostURL(host, getHTTPPort(), getEventSubURI());
    }

    public boolean subscribe(Service service, long timeout) {
        if (service.isSubscribed()) {
            return subscribe(service, service.getSID(), timeout);
        }
        Device rootDev = service.getRootDevice();
        if (rootDev == null) {
            return false;
        }
        String ifAddress = rootDev.getInterfaceAddress();
        SubscriptionRequest subReq = new SubscriptionRequest();
        if (getExternalApp()) {
            Debug.message("sub: sub external for GUOAPP");
            subReq.setGID(EXTERNAL_VALUE);
        }
        subReq.setSubscribeRequest(service, getEventSubCallbackURL(ifAddress), timeout);
        if (Debug.isOn()) {
            subReq.print();
        }
        SubscriptionResponse subRes = subReq.post();
        if (Debug.isOn()) {
            subRes.print();
        }
        if (!subRes.isSuccessful()) {
            return false;
        }
        service.setSID(subRes.getSID());
        service.setTimeout(subRes.getTimeout());
        return true;
    }

    public boolean resubscribe(Service service, long timeout) {
        Device rootDev = service.getRootDevice();
        if (rootDev == null) {
            return false;
        }
        String ifAddress = rootDev.getInterfaceAddress();
        SubscriptionRequest subReq = new SubscriptionRequest();
        if (getExternalApp()) {
            Debug.message("sub: sub external for GUOAPP");
            subReq.setGID(EXTERNAL_VALUE);
        }
        subReq.setSubscribeRequest(service, getEventSubCallbackURL(ifAddress), timeout);
        if (Debug.isOn()) {
            subReq.print();
        }
        SubscriptionResponse subRes = subReq.post();
        if (Debug.isOn()) {
            subRes.print();
        }
        if (!subRes.isSuccessful()) {
            return false;
        }
        service.setSID(subRes.getSID());
        service.setTimeout(subRes.getTimeout());
        return true;
    }

    public boolean subscribe(Service service) {
        return subscribe(service, -1);
    }

    public boolean subscribe(Service service, String sid, long timeout) {
        SubscriptionRequest subReq = new SubscriptionRequest();
        if (getExternalApp()) {
            Debug.message("sub: renew sub external for GUOAPP");
            subReq.setGID(EXTERNAL_VALUE);
        }
        subReq.setRenewRequest(service, sid, timeout);
        if (Debug.isOn()) {
            subReq.print();
        }
        SubscriptionResponse subRes = subReq.post();
        if (Debug.isOn()) {
            subRes.print();
        }
        if (!subRes.isSuccessful()) {
            return false;
        }
        service.setSID(subRes.getSID());
        service.setTimeout(subRes.getTimeout());
        return true;
    }

    public boolean subscribe(Service service, String sid) {
        return subscribe(service, sid, -1);
    }

    public boolean isSubscribed(Service service) {
        if (service == null) {
            return false;
        }
        return service.isSubscribed();
    }

    public boolean unsubscribe(Service service) {
        SubscriptionRequest subReq = new SubscriptionRequest();
        if (getExternalApp()) {
            Debug.message("sub: unsub external for GUOAPP");
            subReq.setGID(EXTERNAL_VALUE);
        }
        subReq.setUnsubscribeRequest(service);
        if (!subReq.post().isSuccessful()) {
            return false;
        }
        service.clearSID();
        return true;
    }

    public void unsubscribe(Device device) {
        if (device == null) {
            Debug.message("[Error] unsubscribe dev == null");
            return;
        }
        int n;
        ServiceList serviceList = device.getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (service.hasSID()) {
                unsubscribe(service);
            }
        }
        DeviceList childDevList = device.getDeviceList();
        int childDevCnt = childDevList.size();
        for (n = 0; n < childDevCnt; n++) {
            unsubscribe(childDevList.getDevice(n));
        }
    }

    public void unsubscribe() {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++) {
            unsubscribe(devList.getDevice(n));
        }
    }

    public Service getSubscriberService(String uuid) {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++) {
            Service service = devList.getDevice(n).getSubscriberService(uuid);
            if (service != null) {
                return service;
            }
        }
        return null;
    }

    public void renewSubscriberService(Device dev, long timeout) {
        int n;
        ServiceList serviceList = dev.getServiceList();
        int serviceCnt = serviceList.size();
        for (n = 0; n < serviceCnt; n++) {
            Service service = serviceList.getService(n);
            if (service.isSubscribed() && !subscribe(service, service.getSID(), timeout)) {
                resubscribe(service, timeout);
            }
        }
        DeviceList cdevList = dev.getDeviceList();
        int cdevCnt = cdevList.size();
        for (n = 0; n < cdevCnt; n++) {
            renewSubscriberService(cdevList.getDevice(n), timeout);
        }
    }

    public void renewSubscriberService(long timeout) {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        for (int n = 0; n < devCnt; n++) {
            renewSubscriberService(devList.getDevice(n), timeout);
        }
    }

    public void renewSubscriberService() {
        renewSubscriberService(-1);
    }

    public void setRenewSubscriber(RenewSubscriber sub) {
        this.renewSubscriber = sub;
    }

    public RenewSubscriber getRenewSubscriber() {
        return this.renewSubscriber;
    }

    public boolean start(String target, int mx) {
        Debug.message("MediaControlPoint start SDK VERSION: " + SDKVersion.getSDKVersion());
        stop();
        int retryCnt = 0;
        int bindPort = getHTTPPort();
        HTTPServerList httpServerList = getHTTPServerList();
        while (!httpServerList.open(bindPort)) {
            retryCnt++;
            if (100 < retryCnt) {
                return false;
            }
            setHTTPPort(bindPort + 1);
            bindPort = getHTTPPort();
        }
        httpServerList.addRequestListener(this);
        httpServerList.start();
        SSDPNotifySocketList ssdpNotifySocketList = getSSDPNotifySocketList();
        if (!ssdpNotifySocketList.open()) {
            return false;
        }
        ssdpNotifySocketList.setControlPoint(this);
        ssdpNotifySocketList.start();
        int ssdpPort = getSSDPPort();
        retryCnt = 0;
        SSDPSearchResponseSocketList ssdpSearchResponseSocketList = getSSDPSearchResponseSocketList();
        while (!ssdpSearchResponseSocketList.open(ssdpPort)) {
            retryCnt++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Debug.message("ssdpSearchResponseSocketList.open retry" + retryCnt);
            if (100 < retryCnt) {
                return false;
            }
            setSSDPPort(ssdpPort + 10);
            ssdpPort = getSSDPPort();
        }
        ssdpSearchResponseSocketList.setControlPoint(this);
        ssdpSearchResponseSocketList.start();
        for (int i = 0; i < 3; i++) {
            search(target, mx);
        }
        Disposer disposer = new Disposer(this);
        setDeviceDisposer(disposer);
        disposer.start("Disposer");
        if (isNMPRMode()) {
            RenewSubscriber renewSub = new RenewSubscriber(this);
            setRenewSubscriber(renewSub);
            renewSub.setSubscriberTimeout(this.subTimeout);
            renewSub.start("RenewSubscriber");
        }
        Debug.message("MediaControlPoint start SDK VERSION [DONE]: " + SDKVersion.getSDKVersion());
        return true;
    }

    public boolean start(String target) {
        return start(target, 10);
    }

    public boolean start() {
        return start("upnp:rootdevice", 10);
    }

    public boolean stop() {
        Debug.message("MediaControlPoint stop SDK VERSION: " + SDKVersion.getSDKVersion());
        SSDPNotifySocketList ssdpNotifySocketList = getSSDPNotifySocketList();
        ssdpNotifySocketList.stop();
        ssdpNotifySocketList.close();
        ssdpNotifySocketList.clear();
        SSDPSearchResponseSocketList ssdpSearchResponseSocketList = getSSDPSearchResponseSocketList();
        ssdpSearchResponseSocketList.stop();
        ssdpSearchResponseSocketList.close();
        ssdpSearchResponseSocketList.clear();
        HTTPServerList httpServerList = getHTTPServerList();
        httpServerList.stop();
        httpServerList.close();
        httpServerList.clear();
        if (this.currentControlDevice != null) {
            this.currentControlDevice.closeConnectHost();
        }
        Disposer disposer = getDeviceDisposer();
        if (disposer != null) {
            Debug.message("Stop Disposer Thread...");
            disposer.stop();
            setDeviceDisposer(null);
        }
        RenewSubscriber renewSub = getRenewSubscriber();
        if (renewSub != null) {
            Debug.message("Stop RenewSubscriber Thread...");
            renewSub.stop();
            setRenewSubscriber(null);
        }
        unsubscribe();
        if (this.allDevNodeList != null) {
            this.allDevNodeList.clear();
        }
        if (this.devNodeList != null) {
            this.devNodeList.clear();
        }
        Debug.message("MediaControlPoint stop SDK VERSION [DONE]: " + SDKVersion.getSDKVersion());
        this.currentControlDevice = null;
        return true;
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }

    public void print() {
        DeviceList devList = getDeviceList();
        int devCnt = devList.size();
        Debug.message("Device Num = " + devCnt);
        for (int n = 0; n < devCnt; n++) {
            Device dev = devList.getDevice(n);
            Debug.message("[" + n + "] " + dev.getFriendlyName() + ", " + dev.getLeaseTime() + ", " + dev.getElapsedTime());
        }
    }

    public void setAddDeviceFailedListener(AddDeviceFailedListener listener) {
        this.mAddDeviceFailedListener = listener;
    }

    private void onAddDeviceFailed(SSDPPacket ssdpPacket, String udn, String cause) {
        if (ssdpPacket != null && !TextUtils.isEmpty(udn) && this.mAddDeviceFailedListener != null && !isInFoundFailedDeviceList(udn)) {
            this.mAddDeviceFailedListener.onAddDeviceFailed(String.format(DEVICE_FOUND_FAILED_INFO_FORMAT, new Object[]{udn, Boolean.valueOf(ssdpPacket.isGalaServer()), ssdpPacket.getServer(), ssdpPacket.getFriendlyName(), cause}));
            addFoundFailedDevice(udn);
        }
    }
}
