package org.cybergarage.upnp.xml;

import com.gala.android.dlna.sdk.dlnahttpserver.GalaHttpServerList;
import java.io.File;
import java.net.InetAddress;
import org.cybergarage.http.HTTPServerList;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.device.Advertiser;
import org.cybergarage.upnp.ssdp.SSDP;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.upnp.ssdp.SSDPSearchSocketList;
import org.cybergarage.util.ListenerList;

public class DeviceData extends NodeData {
    private Advertiser advertiser = null;
    private ListenerList controlActionListenerList = new ListenerList();
    private File descriptionFile = null;
    private String descriptionURI = null;
    private int deviceType = 0;
    private int deviceVersion = 0;
    private int galaVersion = -1;
    private int galahttpPort = 0;
    private GalaHttpServerList galahttpServerList = null;
    private InetAddress[] httpBinds = null;
    private int httpPort = Device.HTTP_DEFAULT_PORT;
    private HTTPServerList httpServerList = null;
    private int leaseTime = 30;
    private String location = "";
    private InetAddress[] ssdpBinds = null;
    private String ssdpMulticastIPv4 = SSDP.ADDRESS;
    private String ssdpMulticastIPv6 = SSDP.getIPv6Address();
    private SSDPPacket ssdpPacket = null;
    private int ssdpPort = SSDP.PORT;
    private SSDPSearchSocketList ssdpSearchSocketList = null;
    private int udpgalahttpPort = 0;

    public File getDescriptionFile() {
        return this.descriptionFile;
    }

    public String getDescriptionURI() {
        return this.descriptionURI;
    }

    public void setDescriptionFile(File descriptionFile) {
        this.descriptionFile = descriptionFile;
    }

    public void setDescriptionURI(String descriptionURI) {
        this.descriptionURI = descriptionURI;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLeaseTime() {
        return this.leaseTime;
    }

    public void setLeaseTime(int val) {
        this.leaseTime = val;
    }

    public int getGalaDeviceType() {
        return this.deviceType;
    }

    public void setGalaDeviceType(int type) {
        this.deviceType = type;
    }

    public int getGalaVersion() {
        return this.galaVersion;
    }

    public void setGalaVersion(int version) {
        this.galaVersion = version;
    }

    public int getGalaDeviceVersion() {
        return this.deviceVersion;
    }

    public void setGalaDeviceVersion(int version) {
        this.deviceVersion = version;
    }

    public HTTPServerList getHTTPServerList() {
        if (this.httpServerList == null) {
            this.httpServerList = new HTTPServerList(this.httpBinds, this.httpPort);
        }
        return this.httpServerList;
    }

    public int getGalahttpPort() {
        return this.galahttpPort;
    }

    public void setGalahttpPort(int galahttpPort) {
        this.galahttpPort = galahttpPort;
    }

    public int getUdpgalahttpPort() {
        return this.udpgalahttpPort;
    }

    public void setUdpgalahttpPort(int udpgalahttpPort) {
        this.udpgalahttpPort = udpgalahttpPort;
    }

    public GalaHttpServerList getGalaHttpServerList() {
        this.galahttpPort = this.httpPort + 1;
        if (this.galahttpServerList == null) {
            this.galahttpServerList = new GalaHttpServerList(this.httpBinds, this.galahttpPort);
        }
        return this.galahttpServerList;
    }

    public void setHTTPBindAddress(InetAddress[] inets) {
        this.httpBinds = inets;
    }

    public InetAddress[] getHTTPBindAddress() {
        return this.httpBinds;
    }

    public int getHTTPPort() {
        return this.httpPort;
    }

    public void setHTTPPort(int port) {
        this.httpPort = port;
    }

    public ListenerList getControlActionListenerList() {
        return this.controlActionListenerList;
    }

    public SSDPSearchSocketList getSSDPSearchSocketList() {
        if (this.ssdpSearchSocketList == null) {
            this.ssdpSearchSocketList = new SSDPSearchSocketList(this.ssdpBinds, this.ssdpPort, this.ssdpMulticastIPv4, this.ssdpMulticastIPv6);
        }
        return this.ssdpSearchSocketList;
    }

    public void setSSDPPort(int port) {
        this.ssdpPort = port;
    }

    public int getSSDPPort() {
        return this.ssdpPort;
    }

    public void setSSDPBindAddress(InetAddress[] inets) {
        this.ssdpBinds = inets;
    }

    public InetAddress[] getSSDPBindAddress() {
        return this.ssdpBinds;
    }

    public void setMulticastIPv4Address(String ip) {
        this.ssdpMulticastIPv4 = ip;
    }

    public String getMulticastIPv4Address() {
        return this.ssdpMulticastIPv4;
    }

    public void setMulticastIPv6Address(String ip) {
        this.ssdpMulticastIPv6 = ip;
    }

    public String getMulticastIPv6Address() {
        return this.ssdpMulticastIPv6;
    }

    public SSDPPacket getSSDPPacket() {
        return this.ssdpPacket;
    }

    public void setSSDPPacket(SSDPPacket packet) {
        this.ssdpPacket = packet;
    }

    public void setAdvertiser(Advertiser adv) {
        this.advertiser = adv;
    }

    public Advertiser getAdvertiser() {
        return this.advertiser;
    }
}
