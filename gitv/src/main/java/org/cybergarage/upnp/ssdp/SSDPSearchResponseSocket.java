package org.cybergarage.upnp.ssdp;

import java.net.DatagramSocket;
import org.cybergarage.upnp.ControlPoint;
import org.cybergarage.util.Debug;

public class SSDPSearchResponseSocket extends HTTPUSocket {
    private static final String TAG = new StringBuilder(String.valueOf(SSDPSearchResponseSocket.class.getSimpleName())).toString();
    private static SSDPSearchResponseSocket socket = null;
    private ControlPoint controlPoint = null;
    private Thread deviceSearchResponseThread = null;

    public static SSDPSearchResponseSocket getInstance() {
        if (socket == null) {
            socket = new SSDPSearchResponseSocket();
        }
        return socket;
    }

    public SSDPSearchResponseSocket() {
        setControlPoint(null);
    }

    public SSDPSearchResponseSocket(String bindAddr, int port) {
        super(bindAddr, port);
        setControlPoint(null);
    }

    public void setControlPoint(ControlPoint ctrlp) {
        this.controlPoint = ctrlp;
    }

    public ControlPoint getControlPoint() {
        return this.controlPoint;
    }

    public void start() {
        StringBuffer name = new StringBuffer("igala.SSDPSearchResponseSocket/");
        DatagramSocket s = getDatagramSocket();
        if (s == null) {
            Debug.message(TAG + "socket is null.");
        }
        if (s.getLocalAddress() != null) {
            name.append(s.getLocalAddress()).append(':');
            name.append(s.getLocalPort());
        }
        interruptThread();
        this.deviceSearchResponseThread = new Thread(new SSDPSearchResponseTask(getControlPoint(), s, getLocalAddress()));
        this.deviceSearchResponseThread.start();
    }

    public void stop() {
        interruptThread();
        close();
    }

    public void interruptThread() {
        if (this.deviceSearchResponseThread != null && this.deviceSearchResponseThread.isAlive()) {
            this.deviceSearchResponseThread.interrupt();
        }
        this.deviceSearchResponseThread = null;
    }

    public boolean post(String addr, int port, SSDPSearchResponse res) {
        return post(addr, port, res.getHeader());
    }

    public boolean post(String addr, int port, SSDPSearchRequest req) {
        return post(addr, port, req.toString());
    }
}
