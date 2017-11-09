package org.cybergarage.upnp.ssdp;

import java.io.IOException;
import java.net.DatagramSocket;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPPacketReceiveTask implements Runnable {
    public static final String TAG = new StringBuilder(String.valueOf(SSDPPacketReceiveTask.class.getSimpleName())).append(": ").toString();
    private ControlPoint mControlPoint;
    private String mLocalAddress;
    private DatagramSocket mSocket;

    public SSDPPacketReceiveTask(ControlPoint controlPoint, DatagramSocket socket, String localAddress) {
        this.mControlPoint = controlPoint;
        this.mSocket = socket;
        this.mLocalAddress = localAddress;
    }

    public void run() {
        while (true) {
            Thread.yield();
            try {
                SSDPPacket packet = receive();
                if (packet != null) {
                    Debug.message(TAG + "notifyReceived sender = " + packet.getRemoteAddress());
                    if (this.mControlPoint != null) {
                        this.mControlPoint.notifyReceived(packet);
                    }
                }
            } catch (IOException e) {
                Debug.message(TAG + "deviceNotifyThread exit");
                e.printStackTrace();
                return;
            }
        }
    }

    private SSDPPacket receive() throws IOException {
        byte[] ssdvRecvBuf = new byte[1024];
        SSDPPacket recvPacket = new SSDPPacket(ssdvRecvBuf, ssdvRecvBuf.length);
        recvPacket.setLocalAddress(this.mLocalAddress);
        if (this.mSocket != null) {
            this.mSocket.receive(recvPacket.getDatagramPacket());
            recvPacket.setTimeStamp(System.currentTimeMillis());
            return recvPacket;
        }
        throw new IOException("broadcast socket has already been closed.");
    }
}
