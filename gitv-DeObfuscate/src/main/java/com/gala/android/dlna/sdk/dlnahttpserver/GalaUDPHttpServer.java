package com.gala.android.dlna.sdk.dlnahttpserver;

import com.gala.android.dlna.sdk.mediarenderer.ControlPointConnectRendererListener;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;

public class GalaUDPHttpServer implements Runnable {
    public static final int DEFAULT_PORT = 80;
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final String NAME = "HTTP";
    public static final String VERSION = "1.0";
    private InetAddress bindAddr;
    private int bindPort;
    private ListenerList controlPointListenerList;
    private ListenerList httpRequestListenerList;
    private Thread httpServerThread;
    private DatagramSocket serverSock;
    protected int timeout;

    public static String getName() {
        String osName = System.getProperty("os.name");
        return new StringBuilder(String.valueOf(osName)).append("/").append(System.getProperty("os.version")).append(" ").append("HTTP").append("/").append("1.0").toString();
    }

    public GalaUDPHttpServer() {
        this.serverSock = null;
        this.bindAddr = null;
        this.bindPort = 0;
        this.timeout = 60000;
        this.httpRequestListenerList = new ListenerList();
        this.controlPointListenerList = new ListenerList();
        this.httpServerThread = null;
        this.serverSock = null;
    }

    public DatagramSocket getServerSock() {
        return this.serverSock;
    }

    public String getBindAddress() {
        if (this.bindAddr == null) {
            return "";
        }
        return this.bindAddr.toString();
    }

    public int getBindPort() {
        return this.bindPort;
    }

    public synchronized int getTimeout() {
        return this.timeout;
    }

    public synchronized void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean open(InetAddress addr, int port) {
        if (this.serverSock != null) {
            return true;
        }
        if (addr == null) {
            return false;
        }
        try {
            this.serverSock = new DatagramSocket(new InetSocketAddress(addr.getHostAddress(), port));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean open(String addr, int port) {
        if (this.serverSock != null) {
            return true;
        }
        try {
            InetSocketAddress socketAddress = new InetSocketAddress(addr, port);
            this.bindAddr = InetAddress.getByName(addr);
            this.bindPort = port;
            this.serverSock = new DatagramSocket(socketAddress);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean shutDown() {
        if (this.serverSock == null) {
            return true;
        }
        try {
            this.serverSock.close();
            this.serverSock = null;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean close() {
        if (this.serverSock == null) {
            return true;
        }
        try {
            this.serverSock.close();
            this.serverSock = null;
            this.bindAddr = null;
            this.bindPort = 0;
            return true;
        } catch (Exception e) {
            Debug.warning(e);
            return false;
        }
    }

    public boolean isOpened() {
        return this.serverSock != null;
    }

    public void addRequestListener(HTTPRequestListener listener) {
        this.httpRequestListenerList.add(listener);
    }

    public void removeRequestListener(HTTPRequestListener listener) {
        this.httpRequestListenerList.remove(listener);
    }

    public void performRequestListener(HTTPRequest httpReq) {
        int listenerSize = this.httpRequestListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            ((HTTPRequestListener) this.httpRequestListenerList.get(n)).httpRequestRecieved(httpReq);
        }
    }

    public void addControlPointConnectRendererListener(ControlPointConnectRendererListener listener) {
        this.controlPointListenerList.add(listener);
    }

    public void removeControlPointConnectRendererListener(ControlPointConnectRendererListener listener) {
        this.controlPointListenerList.remove(listener);
    }

    public void performControlPointConnectRendererListener(boolean isConnect) {
        int listenerSize = this.controlPointListenerList.size();
        for (int n = 0; n < listenerSize; n++) {
            ControlPointConnectRendererListener listener = (ControlPointConnectRendererListener) this.controlPointListenerList.get(n);
            if (listener != null) {
                listener.onReceiveDeviceConnect(isConnect);
            }
        }
    }

    public void run() {
        if (isOpened()) {
            Thread thisThread = Thread.currentThread();
            while (this.httpServerThread == thisThread) {
                Thread.yield();
                while (true) {
                    HTTPRequest httpReq = new HTTPRequest();
                    if (!httpReq.readQuickly(this.serverSock)) {
                        shutDown();
                        if (!open(this.bindAddr, this.bindPort)) {
                            break;
                        }
                    }
                    if (!httpReq.getIsSingleSend() && RacingStrategy.isMessageOk(httpReq)) {
                        performRequestListener(httpReq);
                    }
                }
                Debug.warning("UDP Quickly Channel Died!");
            }
        }
    }

    public boolean start() {
        StringBuffer name = new StringBuffer("igala.QuicklyHTTPServer/");
        name.append(this.serverSock.getLocalSocketAddress());
        this.httpServerThread = new Thread(this, name.toString());
        this.httpServerThread.start();
        return true;
    }

    public boolean stop() {
        this.httpServerThread = null;
        return true;
    }
}
