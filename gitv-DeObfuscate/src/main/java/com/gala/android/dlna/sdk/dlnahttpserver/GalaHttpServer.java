package com.gala.android.dlna.sdk.dlnahttpserver;

import com.gala.android.dlna.sdk.mediarenderer.ControlPointConnectRendererListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Vector;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPRequestListener;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;

public class GalaHttpServer implements Runnable {
    public static final int DEFAULT_PORT = 80;
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final String NAME = "HTTP";
    public static final String VERSION = "1.0";
    private InetAddress bindAddr;
    private int bindPort;
    public Vector<String> client;
    private ListenerList controlPointListenerList;
    private ListenerList httpRequestListenerList;
    private Thread httpServerThread;
    private ServerSocket serverSock;
    protected int timeout;

    public static String getName() {
        String osName = System.getProperty("os.name");
        return new StringBuilder(String.valueOf(osName)).append("/").append(System.getProperty("os.version")).append(" ").append("HTTP").append("/").append("1.0").toString();
    }

    public GalaHttpServer() {
        this.client = null;
        this.serverSock = null;
        this.bindAddr = null;
        this.bindPort = 0;
        this.timeout = 60000;
        this.httpRequestListenerList = new ListenerList();
        this.controlPointListenerList = new ListenerList();
        this.httpServerThread = null;
        this.serverSock = null;
        this.client = new Vector();
    }

    public synchronized void addClient(String ip) {
        if (ip != null) {
            if (ip.length() >= 7) {
                int n = 0;
                while (n < this.client.size()) {
                    try {
                        if (ip.equals(this.client.get(n))) {
                            break;
                        }
                        n++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.client.add(ip);
            }
        }
    }

    public synchronized void removeClient(String ip) {
        int n = 0;
        while (n < this.client.size()) {
            try {
                if (ip.equals(this.client.get(n))) {
                    this.client.remove(n);
                }
                n++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized Vector<String> getClientList() {
        Vector<String> clientList;
        clientList = new Vector();
        for (int n = 0; n < this.client.size(); n++) {
            clientList.add((String) this.client.get(n));
        }
        return clientList;
    }

    public ServerSocket getServerSock() {
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
        try {
            this.serverSock = new ServerSocket(this.bindPort, 0, this.bindAddr);
            this.serverSock.setPerformancePreferences(2, 3, 1);
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
            this.bindAddr = InetAddress.getByName(addr);
            this.bindPort = port;
            this.serverSock = new ServerSocket(this.bindPort, 0, this.bindAddr);
            this.serverSock.setPerformancePreferences(2, 3, 1);
            return true;
        } catch (IOException e) {
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

    public Socket accept() {
        if (this.serverSock == null) {
            return null;
        }
        try {
            Socket sock = this.serverSock.accept();
            sock.setOOBInline(false);
            sock.setTrafficClass(16);
            sock.setTcpNoDelay(true);
            sock.setPerformancePreferences(2, 3, 1);
            sock.setKeepAlive(true);
            return sock;
        } catch (Exception e) {
            Debug.warning(e);
            return null;
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

    public Thread getHttpServerThread() {
        return this.httpServerThread;
    }

    public void run() {
        if (isOpened()) {
            SocketAddress addr = this.serverSock.getLocalSocketAddress();
            Debug.message("[GalaHttpServer] Thread start...ServerAddr=" + addr);
            Vector<Socket> clients = new Vector();
            Thread thisThread = Thread.currentThread();
            while (this.httpServerThread == thisThread) {
                Thread.yield();
                try {
                    Debug.message("[GalaHttpServer] Wait for connecting...HTTPServer=" + addr);
                    Socket sock = accept();
                    if (sock == null) {
                        Debug.message("[GalaHttpServer] [Error] Accept() failure...[sock == null]");
                        break;
                    }
                    Debug.message("[GalaHttpServer] Remote client connected...ClientAddr=" + sock.getRemoteSocketAddress());
                    clients.add(sock);
                    Debug.message("[GalaHttpServer] Create thread to handle connection...ClientAddr=" + sock.getRemoteSocketAddress());
                    new GalaHttpServerThread(this, sock).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Iterator<Socket> it = clients.iterator();
            while (it.hasNext()) {
                try {
                    ((Socket) it.next()).close();
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            }
            Debug.message("[GalaHttpServer] Thread exit...ServerAddr=" + addr);
            return;
        }
        Debug.message("[GalaHttpServer] [Error] Thread exit...[httpSock.open() == false]");
    }

    public boolean start() {
        StringBuffer name = new StringBuffer("igala.QuicklyHTTPServer/");
        name.append(this.serverSock.getLocalSocketAddress());
        this.httpServerThread = new Thread(this, name.toString());
        this.httpServerThread.start();
        return true;
    }

    public boolean stop() {
        if (this.client != null) {
            this.client.clear();
        }
        this.httpServerThread = null;
        return true;
    }
}
