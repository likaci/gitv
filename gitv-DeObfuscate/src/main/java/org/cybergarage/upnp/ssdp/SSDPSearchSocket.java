package org.cybergarage.upnp.ssdp;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.SocketAddress;
import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.device.SearchListener;
import org.cybergarage.util.ListenerList;

public class SSDPSearchSocket extends HTTPMUSocket {
    private ListenerList deviceSearchListenerList = new ListenerList();
    private SSDPPacketReceiveThreadPool mSsdpPacketReceiveThreadPool;
    private boolean useIPv6Address;

    public SSDPSearchSocket(String bindAddr, int port, String multicast) {
        open(bindAddr, multicast);
        this.mSsdpPacketReceiveThreadPool = SSDPPacketReceiveThreadPool.getInstance();
    }

    public SSDPSearchSocket(InetAddress bindAddr) {
        if (bindAddr.getAddress().length != 4) {
            open((Inet6Address) bindAddr);
        } else {
            open((Inet4Address) bindAddr);
        }
    }

    public boolean open(Inet4Address bindAddr) {
        this.useIPv6Address = false;
        return openReceiveMulticastSocket(SSDP.ADDRESS, SSDP.PORT, bindAddr);
    }

    public boolean open(Inet6Address bindAddr) {
        this.useIPv6Address = true;
        return openReceiveMulticastSocket(SSDP.getIPv6Address(), SSDP.PORT, bindAddr);
    }

    public boolean open(String bind, String multicast) {
        if (HostInterface.isIPv6Address(bind) && HostInterface.isIPv6Address(multicast)) {
            this.useIPv6Address = true;
        } else if (HostInterface.isIPv4Address(bind) && HostInterface.isIPv4Address(multicast)) {
            this.useIPv6Address = false;
        } else {
            throw new IllegalArgumentException("Cannot open a UDP Socket for IPv6 address on IPv4 interface or viceversa");
        }
        return open(multicast, SSDP.PORT, bind);
    }

    public boolean open(String bindAddr) {
        String addr = SSDP.ADDRESS;
        this.useIPv6Address = false;
        if (HostInterface.isIPv6Address(bindAddr)) {
            addr = SSDP.getIPv6Address();
            this.useIPv6Address = true;
        }
        return open(addr, SSDP.PORT, bindAddr);
    }

    public void addSearchListener(SearchListener listener) {
        this.deviceSearchListenerList.add(listener);
    }

    public void removeSearchListener(SearchListener listener) {
        this.deviceSearchListenerList.remove(listener);
    }

    public void performSearchListener(byte[] ssdpRecvByte, String localAddress, SocketAddress remoteAddress) {
        int listenerSize = this.deviceSearchListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            ((SearchListener) this.deviceSearchListenerList.get(n)).ssdpSearchReceived(ssdpRecvByte, localAddress, remoteAddress);
        }
    }

    public void start() {
        StringBuffer name = new StringBuffer("igala.SSDPSearchSocket/");
        String localAddr = getLocalAddress();
        if (localAddr != null && localAddr.length() > 0) {
            name.append(getLocalAddress()).append(':');
            name.append(getLocalPort()).append(" -> ");
            name.append(getMulticastAddress()).append(':');
            name.append(getMulticastPort());
        }
        this.mSsdpPacketReceiveThreadPool.execute(new SSDPSearchReceiveTask(this, getMulticastSocket(), getLocalAddress()));
    }

    public void stop() {
        closeReceiveMulticastSocket();
        this.mSsdpPacketReceiveThreadPool.stop();
    }
}
