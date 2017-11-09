package com.push.mqttv3.internal;

import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.push.mqttv3.MqttException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.cybergarage.http.HTTP;

public class LocalNetworkModule implements NetworkModule {
    private Class LocalListener;
    private String brokerName;
    private Object localAdapter;

    public LocalNetworkModule(String brokerName) {
        this.brokerName = brokerName;
    }

    public void start() throws IOException, MqttException {
        if (ExceptionHelper.isClassAvailable("com.ibm.mqttdirect.modules.local.bindings.LocalListener")) {
            try {
                this.LocalListener = Class.forName("com.ibm.mqttdirect.modules.local.bindings.LocalListener");
                this.localAdapter = this.LocalListener.getMethod(MultiScreenParams.DLNA_PHONE_CONTROLL_CONNECT, new Class[]{String.class}).invoke(null, new Object[]{this.brokerName});
            } catch (Exception e) {
            }
            if (this.localAdapter == null) {
                throw ExceptionHelper.createMqttException(32103);
            }
            return;
        }
        throw ExceptionHelper.createMqttException(32103);
    }

    public InputStream getInputStream() throws IOException {
        InputStream stream = null;
        try {
            return (InputStream) this.LocalListener.getMethod("getClientInputStream", new Class[0]).invoke(this.localAdapter, new Object[0]);
        } catch (Exception e) {
            return stream;
        }
    }

    public OutputStream getOutputStream() throws IOException {
        OutputStream stream = null;
        try {
            return (OutputStream) this.LocalListener.getMethod("getClientOutputStream", new Class[0]).invoke(this.localAdapter, new Object[0]);
        } catch (Exception e) {
            return stream;
        }
    }

    public void stop() throws IOException {
        if (this.localAdapter != null) {
            try {
                this.LocalListener.getMethod(HTTP.CLOSE, new Class[0]).invoke(this.localAdapter, new Object[0]);
            } catch (Exception e) {
            }
        }
    }
}
