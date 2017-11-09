package org.cybergarage.http;

import java.net.Socket;
import org.cybergarage.util.Debug;
import org.cybergarage.util.Mutex;

public class HTTPServerThread extends Thread {
    private HTTPServer httpServer;
    private Mutex mutex = new Mutex();
    private Socket sock;

    public HTTPServerThread(HTTPServer httpServer, Socket sock) {
        super("igala.HTTPServerThread");
        this.httpServer = httpServer;
        this.sock = sock;
    }

    public void lock() {
        this.mutex.lock();
    }

    public void unlock() {
        this.mutex.unlock();
    }

    public void run() {
        if (this.sock == null) {
            Debug.message("[HTTPServerThread] [Error] Thread exit...[sock == null]");
            return;
        }
        HTTPSocket httpSock = new HTTPSocket(this.sock);
        if (httpSock.open()) {
            Debug.message("[HTTPServerThread] Thread start...ClientAddr=" + this.sock.getRemoteSocketAddress());
            HTTPRequest httpReq = new HTTPRequest();
            httpReq.setSocket(httpSock);
            while (this.httpServer.getHttpServerThread() != null) {
                if (!httpReq.read()) {
                    Debug.message("[HTTPServerThread] Exit thread [httpReq.read() == false]...ClientAddr=" + this.sock.getRemoteSocketAddress());
                    break;
                }
                this.httpServer.performRequestListener(httpReq);
                if (!httpReq.isKeepAlive()) {
                    Debug.message("[HTTPServerThread] Exit thread [httpReq.isKeepAlive() == false]...ClientAddr=" + this.sock.getRemoteSocketAddress());
                    break;
                }
            }
            Debug.message("[HTTPServerThread] Thread exit...ClientAddr=" + this.sock.getRemoteSocketAddress());
            httpSock.close();
            return;
        }
        Debug.message("[HTTPServerThread] [Error] Thread exit...[httpSock.open() == false]");
    }
}
