package com.push.mqttv3.internal;

import com.push.mqttv3.MqttException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface NetworkModule {
    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    void start() throws IOException, MqttException;

    void stop() throws IOException;
}
