package org.cybergarage.upnp.ssdp;

import java.net.DatagramSocket;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPSearchResponseTask implements Runnable {
    private static final String TAG = new StringBuilder(String.valueOf(SSDPSearchResponseTask.class.getSimpleName())).append(": ").toString();
    private ControlPoint mControlPoint;
    private String mLocalAddress;
    private DatagramSocket mSocket;

    public SSDPSearchResponseTask(ControlPoint controlPoint, DatagramSocket socket, String localAddress) {
        this.mControlPoint = controlPoint;
        this.mSocket = socket;
        this.mLocalAddress = localAddress;
    }

    public void run() {
        while (true) {
            Thread.yield();
            SSDPPacket packet = receive();
            if (packet != null) {
                if (this.mControlPoint != null) {
                    Debug.message(TAG + "searchResponseReceived get: " + packet.getLocation());
                    this.mControlPoint.searchResponseReceived(packet);
                }
            } else {
                return;
            }
        }
    }

    public SSDPPacket receive() {
        byte[] ssdvRecvBuf = new byte[1024];
        SSDPPacket recvPacket = new SSDPPacket(ssdvRecvBuf, ssdvRecvBuf.length);
        recvPacket.setLocalAddress(this.mLocalAddress);
        try {
            this.mSocket.receive(recvPacket.getDatagramPacket());
            recvPacket.setTimeStamp(System.currentTimeMillis());
            return recvPacket;
        } catch (Exception e) {
            Debug.message("SSDPPacket receive exception e = " + e.toString());
            return null;
        }
    }
}
