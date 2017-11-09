package org.cybergarage.upnp.ssdp;

import org.cybergarage.net.HostInterface;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPNotifySocket extends HTTPMUSocket {
    private static String mBindAddr;
    public static volatile SSDPNotifySocket mSsdpSock = null;
    private ControlPoint controlPoint = null;
    private SSDPPacketReceiveThreadPool mSsdpPacketReceiveThreadPool;
    private boolean useIPv6Address;

    public static SSDPNotifySocket getInstance(String bindAddr) {
        if (mSsdpSock == null) {
            synchronized (SSDPNotifySocket.class) {
                if (mSsdpSock == null) {
                    mSsdpSock = new SSDPNotifySocket(bindAddr);
                    mBindAddr = bindAddr;
                }
            }
        }
        if (!(mBindAddr == null || mBindAddr.equals(bindAddr))) {
            mSsdpSock.useIPv6Address = HostInterface.isIPv6Address(bindAddr);
            mSsdpSock.open(HostInterface.isIPv6Address(bindAddr) ? SSDP.getIPv6Address() : SSDP.ADDRESS, SSDP.PORT, bindAddr);
            mSsdpSock.setControlPoint(null);
            mBindAddr = bindAddr;
        }
        return mSsdpSock;
    }

    public SSDPNotifySocket(String bindAddr) {
        this.useIPv6Address = HostInterface.isIPv6Address(bindAddr);
        open(HostInterface.isIPv6Address(bindAddr) ? SSDP.getIPv6Address() : SSDP.ADDRESS, SSDP.PORT, bindAddr);
        setControlPoint(null);
        this.mSsdpPacketReceiveThreadPool = SSDPPacketReceiveThreadPool.getInstance();
    }

    public void setControlPoint(ControlPoint ctrlp) {
        this.controlPoint = ctrlp;
    }

    public ControlPoint getControlPoint() {
        return this.controlPoint;
    }

    public boolean post(SSDPNotifyRequest req, String bindAddr, int bindPort) {
        if (req == null) {
            Debug.message("SSDPNotifyRequest is null");
            return false;
        } else if (this.useIPv6Address) {
            req.setHost(SSDP.getIPv6Address());
            return post(req.toString(), bindAddr, bindPort);
        } else {
            req.setHost(SSDP.ADDRESS, SSDP.PORT);
            boolean multicastResult = post(req.toString(), bindAddr, bindPort);
            req.setHost(SSDP.BROADCAST_ADDRESS, SSDP.IGALA_BROADCAST_PORT);
            boolean broadcastResult = sendBroadcast(req.toString());
            Debug.message("Send multicast request is " + multicastResult + ", and send broadcast result is " + broadcastResult);
            if (multicastResult || broadcastResult) {
                return true;
            }
            return false;
        }
    }

    public void start() {
        this.mSsdpPacketReceiveThreadPool.execute(new SSDPPacketReceiveTask(getControlPoint(), getMulticastSocket(), getLocalAddress()));
        this.mSsdpPacketReceiveThreadPool.execute(new SSDPPacketReceiveTask(getControlPoint(), getBroadcastSocket(SSDP.PORT), getLocalAddress()));
        this.mSsdpPacketReceiveThreadPool.execute(new SSDPPacketReceiveTask(getControlPoint(), getBroadcastSocket(SSDP.IGALA_BROADCAST_PORT), getLocalAddress()));
    }

    public void stop() {
        closeReceiveMulticastSocket();
        closeReceiveBroadcastSocket();
        closeSendBroadcastSocket();
        closeSendMulticastSocket();
        this.mSsdpPacketReceiveThreadPool.stop();
    }
}
