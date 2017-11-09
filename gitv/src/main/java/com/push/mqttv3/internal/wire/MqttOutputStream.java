package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MqttOutputStream extends OutputStream {
    private DataOutputStream out;

    public MqttOutputStream(OutputStream out) {
        this.out = new DataOutputStream(out);
    }

    public void close() throws IOException {
        this.out.close();
    }

    public void flush() throws IOException {
        this.out.flush();
    }

    public void write(byte[] b) throws IOException {
        this.out.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        this.out.write(b, off, len);
    }

    public void write(int b) throws IOException {
        this.out.write(b);
    }

    public void write(MqttWireMessage message) throws IOException, MqttException {
        byte[] bytes = message.getHeader();
        byte[] pl = message.getPayload();
        this.out.write(message.getHeader());
        this.out.write(message.getPayload());
    }
}
