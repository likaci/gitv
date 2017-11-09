package org.cybergarage.http;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Iterator;
import java.util.Vector;
import org.cybergarage.util.Debug;
import org.cybergarage.util.ListenerList;

public class HTTPServer implements Runnable {
    public static final int DEFAULT_PORT = 80;
    public static final int DEFAULT_TIMEOUT = 60000;
    public static final String NAME = "HTTP";
    public static final String VERSION = "1.0";
    private InetAddress bindAddr;
    private int bindPort;
    private ListenerList httpRequestListenerList;
    private Thread httpServerThread;
    private ServerSocket serverSock;
    protected int timeout;

    public static String getName() {
        String osName = System.getProperty("os.name");
        return new StringBuilder(String.valueOf(osName)).append("/").append(System.getProperty("os.version")).append(" ").append("HTTP").append("/").append("1.0").toString();
    }

    public HTTPServer() {
        this.serverSock = null;
        this.bindAddr = null;
        this.bindPort = 0;
        this.timeout = 60000;
        this.httpRequestListenerList = new ListenerList();
        this.httpServerThread = null;
        this.serverSock = null;
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
            sock.setPerformancePreferences(2, 3, 1);
            sock.setTrafficClass(16);
            sock.setOOBInline(false);
            sock.setTcpNoDelay(true);
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

    public Thread getHttpServerThread() {
        return this.httpServerThread;
    }

    public void run() {
        if (isOpened()) {
            SocketAddress addr = this.serverSock.getLocalSocketAddress();
            Debug.message("[HTTPServer] Thread start...ServerAddr=" + addr);
            Vector<Socket> clients = new Vector();
            Thread thisThread = Thread.currentThread();
            while (this.httpServerThread == thisThread) {
                Thread.yield();
                try {
                    Debug.message("[HTTPServer] Wait for connecting...HTTPServer=" + addr);
                    Socket sock = accept();
                    if (sock == null) {
                        Debug.message("[HTTPServer] [Error] Accept() failure...[sock == null]");
                        break;
                    }
                    Debug.message("[HTTPServer] Remote client connected...ClientAddr=" + sock.getRemoteSocketAddress());
                    clients.add(sock);
                    Debug.message("[HTTPServer] Create thread to handle connection...ClientAddr=" + sock.getRemoteSocketAddress());
                    new HTTPServerThread(this, sock).start();
                    Debug.message("httpServThread ...");
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
            Debug.message("[HTTPServer] Thread exit...ServerAddr=" + addr);
            return;
        }
        Debug.message("[HTTPServer] [Error] thread exit...[serverSock == null]");
    }

    public boolean start() {
        StringBuffer name = new StringBuffer("igala.HTTPServer/");
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
