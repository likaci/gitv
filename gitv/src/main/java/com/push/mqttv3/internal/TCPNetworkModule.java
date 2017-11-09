package com.push.mqttv3.internal;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.internal.trace.Trace;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import javax.net.SocketFactory;

public class TCPNetworkModule implements NetworkModule {
    private SocketFactory factory;
    private String host;
    private int port;
    protected Socket socket;
    protected Trace trace;

    public TCPNetworkModule(Trace trace, SocketFactory factory, String host, int port) {
        this.factory = factory;
        this.host = host;
        this.port = port;
        this.trace = trace;
    }

    public void start() throws IOException, MqttException {
        try {
            this.socket = this.factory.createSocket(this.host, this.port);
            this.socket.setTcpNoDelay(true);
        } catch (ConnectException ex) {
            this.trace.trace((byte) 1, 250, null, ex);
            throw ExceptionHelper.createMqttException(32103);
        }
    }

    public InputStream getInputStream() throws IOException {
        return this.socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.socket.getOutputStream();
    }

    public void stop() throws IOException {
        if (this.socket != null) {
            this.socket.close();
        }
    }
}
