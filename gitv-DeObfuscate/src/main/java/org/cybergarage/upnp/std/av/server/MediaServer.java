package org.cybergarage.upnp.std.av.server;

import android.os.Environment;
import java.io.File;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.device.InvalidDescriptionException;
import org.cybergarage.upnp.std.av.server.object.Format;
import org.cybergarage.upnp.std.av.server.object.format.DefaultFormat;
import org.cybergarage.upnp.std.av.server.object.format.JPEGFormat;
import org.cybergarage.upnp.std.av.server.object.format.MPEGFormat;
import org.cybergarage.upnp.std.av.server.object.format.PNGFormat;
import org.cybergarage.util.Debug;

public class MediaServer extends Device {
    public static final int DEFAULT_HTTP_PORT = 38520;
    public static final String DESCRIPTION = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<root xmlns=\"urn:schemas-upnp-org:device-1-0\">\n   <specVersion>\n      <major>1</major>\n      <minor>0</minor>\n   </specVersion>\n   <device>\n      <deviceType>urn:schemas-upnp-org:device:MediaServer:1</deviceType>\n      <friendlyName>Cyber Garage Media Server</friendlyName>\n      <manufacturer>Cyber Garage</manufacturer>\n      <manufacturerURL>http://www.cybergarage.org</manufacturerURL>\n      <modelDescription>Provides content through UPnP ContentDirectory service</modelDescription>\n      <modelName>Cyber Garage Media Server</modelName>\n      <modelNumber>1.0</modelNumber>\n      <modelURL>http://www.cybergarage.org</modelURL>\n      <UDN>uuid:362d9414-31a0-48b6-b684-2b4bd38391d0</UDN>\n      <serviceList>\n         <service>\n            <serviceType>urn:schemas-upnp-org:service:ContentDirectory:1</serviceType>\n            <serviceId>urn:upnp-org:serviceId:urn:schemas-upnp-org:service:ContentDirectory</serviceId>\n            <SCPDURL>/service/ContentDirectory1.xml</SCPDURL>\n            <controlURL>/service/ContentDirectory_control</controlURL>\n            <eventSubURL>/service/ContentDirectory_event</eventSubURL>\n         </service>\n         <service>\n            <serviceType>urn:schemas-upnp-org:service:ConnectionManager:1</serviceType>\n            <serviceId>urn:upnp-org:serviceId:urn:schemas-upnp-org:service:ConnectionManager</serviceId>\n            <SCPDURL>/service/ConnectionManager1.xml</SCPDURL>\n            <controlURL>/service/ConnectionManager_control</controlURL>\n            <eventSubURL>/service/ConnectionManager_event</eventSubURL>\n         </service>\n      </serviceList>\n   </device>\n</root>";
    private static final String DESCRIPTION_FILE_NAME = "description/description.xml";
    public static final String DEVICE_TYPE = "urn:schemas-upnp-org:device:MediaServer:1";
    public static final int STATE_DISCOVERABLE = 1;
    public static final int STATE_UNDISCOVERABLE = 2;
    private static final String TAG = "MediaServer";
    private ContentDirectory conDir;
    private ConnectionManager conMan;
    private boolean isServerStart = false;
    private Object mLock = new Object();
    private final int mPort = 8081;
    private String mRootDir = "/http";
    private SimpleWebServer mServer = null;
    private int mWorkingState = 1;

    class C21951 implements Runnable {
        C21951() {
        }

        public void run() {
            synchronized (MediaServer.this.mLock) {
                try {
                    MediaServer.this.mServer = new SimpleWebServer(MediaServer.this.getLocalIpAddress(), 8081, new File(new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(MediaServer.this.mRootDir).toString()));
                    MediaServer.this.mServer.setCDS(MediaServer.this.conDir);
                    MediaServer.this.mServer.setMediaServer(MediaServer.this);
                    MediaServer.this.mServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public MediaServer(String descriptionFileName) throws InvalidDescriptionException {
        super(new File(descriptionFileName));
        initialize();
    }

    public MediaServer() {
        try {
            initialize(DESCRIPTION, ContentDirectory.SCPD, ConnectionManager.SCPD);
        } catch (InvalidDescriptionException e) {
        }
    }

    public MediaServer(String description, String contentDirectorySCPD, String connectionManagerSCPD) throws InvalidDescriptionException {
        initialize(description, contentDirectorySCPD, connectionManagerSCPD);
    }

    private void initialize(String description, String contentDirectorySCPD, String connectionManagerSCPD) throws InvalidDescriptionException {
        loadDescription(description);
        getService(ContentDirectory.SERVICE_TYPE).loadSCPD(contentDirectorySCPD);
        getService("urn:schemas-upnp-org:service:ConnectionManager:1").loadSCPD(connectionManagerSCPD);
        initialize();
    }

    private void initialize() {
        UPnP.setEnable(9);
        setInterfaceAddress(HostInterface.getHostAddress(0));
        setHTTPPort(DEFAULT_HTTP_PORT);
        this.conDir = new ContentDirectory(this);
        this.conMan = new ConnectionManager(this);
        Service servConDir = getService(ContentDirectory.SERVICE_TYPE);
        servConDir.setActionListener(getContentDirectory());
        servConDir.setQueryListener(getContentDirectory());
        Service servConMan = getService("urn:schemas-upnp-org:service:ConnectionManager:1");
        servConMan.setActionListener(getConnectionManager());
        servConMan.setQueryListener(getConnectionManager());
    }

    protected void finalize() {
        stop();
    }

    public ConnectionManager getConnectionManager() {
        return this.conMan;
    }

    public ContentDirectory getContentDirectory() {
        return this.conDir;
    }

    public void addContentDirectory(Directory dir) {
        getContentDirectory().addDirectory(dir);
    }

    public void removeContentDirectory(String name) {
        getContentDirectory().removeDirectory(name);
    }

    public void removeAllContentDirectories() {
        getContentDirectory().removeAllDirectories();
    }

    public int getNContentDirectories() {
        return getContentDirectory().getNDirectories();
    }

    public Directory getContentDirectory(int n) {
        return getContentDirectory().getDirectory(n);
    }

    public boolean addPlugIn(Format format) {
        return getContentDirectory().addPlugIn(format);
    }

    public void setInterfaceAddress(String ifaddr) {
        HostInterface.setInterface(ifaddr);
    }

    public String getInterfaceAddress() {
        return HostInterface.getInterface();
    }

    public void httpRequestRecieved(HTTPRequest httpReq) {
        String uri = httpReq.getURI();
        Debug.message("uri = " + uri);
        if (uri.startsWith(ContentDirectory.CONTENT_EXPORT_URI)) {
            getContentDirectory().contentExportRequestRecieved(httpReq);
        } else {
            super.httpRequestRecieved(httpReq);
        }
    }

    public synchronized boolean isServiceRunning() {
        return this.isServerStart;
    }

    public synchronized boolean restart() {
        if (!this.isServerStart) {
            initialize();
            initDevicePlugins();
            getContentDirectory().start("ContentDirectory");
            if (this.mWorkingState == 1) {
                super.start();
            }
            startWebServer();
            this.isServerStart = true;
        }
        return true;
    }

    public synchronized boolean start() {
        if (!this.isServerStart) {
            initDevicePlugins();
            getContentDirectory().start("ContentDirectory");
            if (this.mWorkingState == 1) {
                super.start();
            }
            startWebServer();
            this.isServerStart = true;
        }
        return true;
    }

    public synchronized boolean stop() {
        if (this.isServerStart) {
            getContentDirectory().stop();
            if (this.mWorkingState == 1) {
                super.stop();
            }
            stopWebServer();
            this.isServerStart = false;
        }
        return true;
    }

    private void initDevicePlugins() {
        addPlugIn(new MPEGFormat());
        addPlugIn(new PNGFormat());
        addPlugIn(new JPEGFormat());
        addPlugIn(new DefaultFormat());
    }

    public String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !(inetAddress instanceof Inet6Address)) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
    }

    public void update() {
    }

    public void setServerRootDir(String rootDir) {
        this.mRootDir = rootDir;
    }

    public String getServerRootDir() {
        return this.mRootDir;
    }

    public int getServerPort() {
        return 8081;
    }

    private void startWebServer() {
        new Thread(new C21951()).start();
    }

    private void stopWebServer() {
        synchronized (this.mLock) {
            if (this.mServer != null) {
                this.mServer.stop();
            }
        }
    }

    public int getWorkingState() {
        return this.mWorkingState;
    }

    public void setWorkingState(int mWorkingState) {
        this.mWorkingState = mWorkingState;
    }
}
