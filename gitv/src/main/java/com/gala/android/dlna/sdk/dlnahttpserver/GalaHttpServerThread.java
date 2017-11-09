package com.gala.android.dlna.sdk.dlnahttpserver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.http.HTTPSocket;
import org.cybergarage.util.Debug;

public class GalaHttpServerThread extends Thread {
    public static int ConnectCount = 0;
    private GalaHttpServer httpServer;
    private Socket sock;

    public GalaHttpServerThread(GalaHttpServer httpServer, Socket sock) {
        super("igala.QuicklyHTTPServerThread");
        this.httpServer = httpServer;
        this.sock = sock;
    }

    public void run() {
        if (this.sock == null) {
            Debug.message("[GalaHttpServerThread] [Error] Thread exit...[sock == null]");
            return;
        }
        HTTPSocket httpSock = new HTTPSocket(this.sock);
        if (httpSock.open()) {
            Debug.message("[GalaHttpServerThread] Thread start...ClientAddr=" + this.sock.getRemoteSocketAddress());
            String client_ip = this.sock.getInetAddress().getHostAddress();
            this.httpServer.addClient(client_ip);
            Debug.message("client_ip: " + client_ip);
            HTTPRequest httpReq = new HTTPRequest();
            httpReq.setSocket(httpSock);
            BufferedReader reader = new BufferedReader(new InputStreamReader(httpSock.getInputStream()));
            synchronized (GalaHttpServerThread.class) {
                if (ConnectCount == 0) {
                    this.httpServer.performControlPointConnectRendererListener(true);
                }
                ConnectCount++;
            }
            while (this.httpServer.getHttpServerThread() != null) {
                this.httpServer.performControlPointConnectRendererListener(true);
                if (!httpReq.readQuickly(reader)) {
                    Debug.message("[GalaHttpServerThread] Exit thread [httpReq.read() == false]...ClientAddr=" + this.sock.getRemoteSocketAddress());
                    break;
                } else if (httpReq.getIsKeepAlive()) {
                    this.httpServer.addClient(client_ip);
                } else if (httpReq.getIsSingleSend()) {
                    this.httpServer.performRequestListener(httpReq);
                } else if (RacingStrategy.isMessageOk(httpReq)) {
                    this.httpServer.performRequestListener(httpReq);
                }
            }
            Debug.message("[GalaHttpServerThread] Thread exit...ClientAddr=" + this.sock.getRemoteSocketAddress());
            this.httpServer.removeClient(client_ip);
            httpSock.close();
            synchronized (GalaHttpServerThread.class) {
                ConnectCount--;
                if (ConnectCount <= 0) {
                    this.httpServer.performControlPointConnectRendererListener(false);
                }
            }
            return;
        }
        Debug.message("[GalaHttpServerThread] [Error] Thread exit...[httpSock.open() == false]");
    }
}
