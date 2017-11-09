package org.cybergarage.upnp.ssdp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.cybergarage.util.Debug;

public class HTTPUSocket {
    private String localAddr = "";
    private DatagramSocket ssdpUniSock = null;

    public DatagramSocket getDatagramSocket() {
        return this.ssdpUniSock;
    }

    public HTTPUSocket() {
        open();
    }

    public HTTPUSocket(String bindAddr, int bindPort) {
        open(bindAddr, bindPort);
    }

    public HTTPUSocket(int bindPort) {
        open(bindPort);
    }

    public void setLocalAddress(String addr) {
        this.localAddr = addr;
    }

    public DatagramSocket getUDPSocket() {
        return this.ssdpUniSock;
    }

    public String getLocalAddress() {
        if (this.localAddr.length() > 0) {
            return this.localAddr;
        }
        return this.ssdpUniSock.getLocalAddress().getHostAddress();
    }

    public boolean open() {
        close();
        try {
            this.ssdpUniSock = new DatagramSocket();
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public boolean open(String bindAddr, int bindPort) {
        close();
        try {
            InetSocketAddress bindInetAddr = new InetSocketAddress(InetAddress.getByName(bindAddr), bindPort);
            this.ssdpUniSock = new DatagramSocket(null);
            this.ssdpUniSock.setReuseAddress(true);
            this.ssdpUniSock.bind(bindInetAddr);
            System.out.println("HTTPUSocket REUSEADDR is enabled: " + this.ssdpUniSock.getReuseAddress());
            setLocalAddress(bindAddr);
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public boolean open(int bindPort) {
        close();
        try {
            InetSocketAddress bindSock = new InetSocketAddress(bindPort);
            this.ssdpUniSock = new DatagramSocket(null);
            this.ssdpUniSock.setReuseAddress(true);
            this.ssdpUniSock.bind(bindSock);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean close() {
        if (this.ssdpUniSock == null) {
            return true;
        }
        try {
            this.ssdpUniSock.close();
            this.ssdpUniSock = null;
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public boolean post(String addr, int port, String msg) {
        if (addr == null || msg == null) {
            return false;
        }
        try {
            DatagramPacket dgmPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, InetAddress.getByName(addr), port);
            if (this.ssdpUniSock == null || dgmPacket == null) {
                Debug.warning("ssdpUniSock == null && dgmPacket== null");
                return false;
            }
            this.ssdpUniSock.send(dgmPacket);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public SSDPPacket receive() {
        byte[] ssdvRecvBuf = new byte[1024];
        SSDPPacket recvPacket = new SSDPPacket(ssdvRecvBuf, ssdvRecvBuf.length);
        recvPacket.setLocalAddress(getLocalAddress());
        try {
            this.ssdpUniSock.receive(recvPacket.getDatagramPacket());
            recvPacket.setTimeStamp(System.currentTimeMillis());
            return recvPacket;
        } catch (Exception e) {
            Debug.message("SSDPPacket receive exception e = " + e.toString());
            return null;
        }
    }
}
