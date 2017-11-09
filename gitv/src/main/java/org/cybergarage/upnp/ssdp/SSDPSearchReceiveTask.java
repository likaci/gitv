package org.cybergarage.upnp.ssdp;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import org.cybergarage.upnp.device.MAN;
import org.cybergarage.util.Debug;

public class SSDPSearchReceiveTask implements Runnable {
    public static final String TAG = new StringBuilder(String.valueOf(SSDPSearchReceiveTask.class.getSimpleName())).append(": ").toString();
    private static byte[] sSSdpRecv = new byte[1024];
    private String mLocalAddress;
    SSDPSearchSocket mSearchSock;
    private DatagramSocket mSocket;

    public SSDPSearchReceiveTask(SSDPSearchSocket searchSock, DatagramSocket socket, String localAddress) {
        this.mSearchSock = searchSock;
        this.mSocket = socket;
        this.mLocalAddress = localAddress;
    }

    public void run() {
        while (true) {
            Thread.yield();
            try {
                SocketAddress remoteAddress = receive();
                if (sSSdpRecv != null && Util.hasCode(sSSdpRecv, MAN.DISCOVER_BYTE)) {
                    this.mSearchSock.performSearchListener(sSSdpRecv, this.mLocalAddress, remoteAddress);
                }
            } catch (IOException e) {
                Debug.message(TAG + "Device receive M-Search Thread exit");
                e.printStackTrace();
                return;
            }
        }
    }

    private SocketAddress receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(sSSdpRecv, sSSdpRecv.length);
        if (this.mSocket != null) {
            this.mSocket.receive(packet);
            return packet.getSocketAddress();
        }
        throw new IOException("socket has already been closed.");
    }
}
