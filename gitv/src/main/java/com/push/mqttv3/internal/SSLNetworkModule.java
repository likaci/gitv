package com.push.mqttv3.internal;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.internal.trace.Trace;
import java.io.IOException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class SSLNetworkModule extends TCPNetworkModule {
    private String[] enabledCiphers;
    private int handshakeTimeoutSecs;

    public SSLNetworkModule(Trace trace, SSLSocketFactory factory, String host, int port) {
        super(trace, factory, host, port);
    }

    public String[] getEnabledCiphers() {
        return this.enabledCiphers;
    }

    public void setEnabledCiphers(String[] enabledCiphers) {
        this.enabledCiphers = enabledCiphers;
        if (this.socket != null && enabledCiphers != null) {
            if (this.trace.isOn()) {
                String ciphers = "";
                for (int i = 0; i < enabledCiphers.length; i++) {
                    if (i > 0) {
                        ciphers = ciphers + ",";
                    }
                    ciphers = ciphers + enabledCiphers[i];
                }
                this.trace.trace((byte) 1, 260, new Object[]{ciphers});
            }
            ((SSLSocket) this.socket).setEnabledCipherSuites(enabledCiphers);
        }
    }

    public void setSSLhandshakeTimeout(int timeout) {
        this.handshakeTimeoutSecs = timeout;
    }

    public void start() throws IOException, MqttException {
        super.start();
        setEnabledCiphers(this.enabledCiphers);
        int soTimeout = this.socket.getSoTimeout();
        if (soTimeout == 0) {
            this.socket.setSoTimeout(this.handshakeTimeoutSecs * 1000);
        }
        ((SSLSocket) this.socket).startHandshake();
        this.socket.setSoTimeout(soTimeout);
    }
}
