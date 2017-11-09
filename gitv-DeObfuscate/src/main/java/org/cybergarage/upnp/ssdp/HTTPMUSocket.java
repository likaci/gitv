package org.cybergarage.upnp.ssdp;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.util.Debug;

public class HTTPMUSocket {
    private static final String TAG = new StringBuilder(String.valueOf(HTTPMUSocket.class.getSimpleName())).append(": ").toString();
    private DatagramSocket expandReceiveBroadcastSocket = null;
    private MulticastSocket mSendMulticastSocket = null;
    private DatagramSocket receiveBroadcastSocket = null;
    private MulticastSocket receiveMulticastSocket = null;
    private DatagramSocket sendBroadcastSocket = null;
    private InetSocketAddress ssdpMultiGroup = null;
    private NetworkInterface ssdpMultiIf = null;

    public HTTPMUSocket(String addr, int port, String bindAddr) {
        open(addr, port, bindAddr);
    }

    public String getLocalAddress() {
        if (this.ssdpMultiGroup == null || this.ssdpMultiIf == null) {
            return "";
        }
        InetAddress mcastAddr = this.ssdpMultiGroup.getAddress();
        Enumeration addrs = this.ssdpMultiIf.getInetAddresses();
        while (addrs.hasMoreElements()) {
            InetAddress addr = (InetAddress) addrs.nextElement();
            if ((mcastAddr instanceof Inet6Address) && (addr instanceof Inet6Address)) {
                return addr.getHostAddress();
            }
            if ((mcastAddr instanceof Inet4Address) && (addr instanceof Inet4Address)) {
                return addr.getHostAddress();
            }
        }
        return "";
    }

    public int getMulticastPort() {
        return this.ssdpMultiGroup.getPort();
    }

    public int getLocalPort() {
        return this.receiveMulticastSocket.getLocalPort();
    }

    public MulticastSocket getSocket() {
        return this.receiveMulticastSocket;
    }

    public InetAddress getMulticastInetAddress() {
        return this.ssdpMultiGroup.getAddress();
    }

    public String getMulticastAddress() {
        return getMulticastInetAddress().getHostAddress();
    }

    public boolean openReceiveMulticastSocket(String addr, int port, InetAddress bindAddr) {
        try {
            this.receiveMulticastSocket = new MulticastSocket(null);
            this.receiveMulticastSocket.setReuseAddress(true);
            this.receiveMulticastSocket.bind(new InetSocketAddress(port));
            this.ssdpMultiGroup = new InetSocketAddress(InetAddress.getByName(addr), port);
            this.ssdpMultiIf = NetworkInterface.getByInetAddress(bindAddr);
            this.receiveMulticastSocket.joinGroup(this.ssdpMultiGroup, this.ssdpMultiIf);
            Debug.message(TAG + "addr = " + addr + " port = " + port + " bindAddr = " + bindAddr);
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Fail to open the SSDP multicast port");
            Debug.warning(e);
            return false;
        }
    }

    public boolean openReceiveBroadcastSocket() {
        try {
            this.receiveBroadcastSocket = new DatagramSocket(null);
            this.receiveBroadcastSocket.setReuseAddress(true);
            this.receiveBroadcastSocket.bind(new InetSocketAddress(SSDP.IGALA_BROADCAST_PORT));
            this.expandReceiveBroadcastSocket = new DatagramSocket(null);
            this.expandReceiveBroadcastSocket.setReuseAddress(true);
            this.expandReceiveBroadcastSocket.bind(new InetSocketAddress(SSDP.PORT));
            Debug.message(TAG + "the result of open 39390 port socket is: " + this.receiveBroadcastSocket.getReuseAddress() + ", and the result of open 1900 port socket is: " + this.expandReceiveBroadcastSocket.getReuseAddress());
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Fail to open the SSDP broad port");
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeReceiveBroadcastSocket() {
        if (this.receiveBroadcastSocket == null && this.expandReceiveBroadcastSocket == null) {
            return true;
        }
        try {
            if (this.receiveBroadcastSocket != null) {
                this.receiveBroadcastSocket.close();
                this.receiveBroadcastSocket = null;
            }
            if (this.expandReceiveBroadcastSocket == null) {
                return true;
            }
            this.expandReceiveBroadcastSocket.close();
            this.expandReceiveBroadcastSocket = null;
            return true;
        } catch (Exception e) {
            Debug.message("+++++Fail to open the SSDP broad port");
            Debug.warning(e);
            return false;
        }
    }

    public boolean open(String addr, int port, String bindAddr) {
        try {
            return openReceiveMulticastSocket(addr, port, InetAddress.getByName(bindAddr));
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public boolean openSendBroadcastSocket() {
        try {
            if (this.sendBroadcastSocket == null) {
                this.sendBroadcastSocket = new DatagramSocket();
            }
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Fail to open the send broadcast socket");
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeSendMulticastSocket() {
        if (this.mSendMulticastSocket == null) {
            return true;
        }
        try {
            this.mSendMulticastSocket.close();
            this.mSendMulticastSocket = null;
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Fail to close the send multicast socket");
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeSendBroadcastSocket() {
        if (this.sendBroadcastSocket == null) {
            return true;
        }
        try {
            this.sendBroadcastSocket.close();
            this.sendBroadcastSocket = null;
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Fail to close the send broadcast socket");
            e.printStackTrace();
            return false;
        }
    }

    public boolean closeReceiveMulticastSocket() {
        if (this.receiveMulticastSocket == null) {
            return true;
        }
        try {
            this.receiveMulticastSocket.leaveGroup(this.ssdpMultiGroup, this.ssdpMultiIf);
            this.receiveMulticastSocket.close();
            this.receiveMulticastSocket = null;
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Fail to close the receive multicast socket");
            e.printStackTrace();
            return false;
        }
    }

    public boolean send(String msg, String bindAddr, int bindPort) {
        MulticastSocket msock;
        if (bindAddr == null || bindPort <= 0) {
            msock = new MulticastSocket();
        } else {
            try {
                msock = new MulticastSocket(null);
                msock.bind(new InetSocketAddress(bindAddr, bindPort));
            } catch (Exception e) {
                Debug.message(TAG + "Send multicast packet failed");
                e.printStackTrace();
                return false;
            }
        }
        DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, this.ssdpMultiGroup);
        msock.setTimeToLive(UPnP.getTimeToLive());
        msock.send(dgmPacket);
        msock.close();
        return true;
    }

    public boolean sendMulticast(String msg) {
        try {
            if (this.mSendMulticastSocket == null) {
                this.mSendMulticastSocket = new MulticastSocket();
            }
            DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, this.ssdpMultiGroup);
            this.mSendMulticastSocket.setTimeToLive(UPnP.getTimeToLive());
            this.mSendMulticastSocket.send(dgmPacket);
            return true;
        } catch (Exception e) {
            Debug.message(TAG + "Send multicast packet failed");
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendBroadcast(String msg) {
        try {
            this.sendBroadcastSocket.send(new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName(SSDP.BROADCAST_ADDRESS), SSDP.IGALA_BROADCAST_PORT));
        } catch (Exception e) {
            Debug.message(TAG + "Send broadcast packet failed");
            e.printStackTrace();
        }
        return true;
    }

    public boolean send(String msg) {
        return sendMulticast(msg);
    }

    public boolean post(String req, String bindAddr, int bindPort) {
        if (bindAddr == null || bindPort <= 0) {
            return sendMulticast(req);
        }
        return send(req, bindAddr, bindPort);
    }

    public SSDPPacket receive() throws IOException {
        byte[] ssdvRecvBuf = new byte[1024];
        SSDPPacket recvPacket = new SSDPPacket(ssdvRecvBuf, ssdvRecvBuf.length);
        recvPacket.setLocalAddress(getLocalAddress());
        if (this.receiveMulticastSocket != null) {
            this.receiveMulticastSocket.receive(recvPacket.getDatagramPacket());
            Debug.message(TAG + "Receive multicast packet...[" + recvPacket.getRemoteAddress() + "] [" + recvPacket.getMAN() + recvPacket.getNTS() + AlbumEnterFactory.SIGN_STR);
            recvPacket.setTimeStamp(System.currentTimeMillis());
            return recvPacket;
        }
        throw new IOException("Multicast socket has already been closed.");
    }

    public MulticastSocket getMulticastSocket() {
        return this.receiveMulticastSocket;
    }

    public DatagramSocket getBroadcastSocket(int port) {
        if (port == SSDP.PORT) {
            return this.expandReceiveBroadcastSocket;
        }
        if (port == SSDP.IGALA_BROADCAST_PORT) {
            return this.receiveBroadcastSocket;
        }
        return null;
    }
}
