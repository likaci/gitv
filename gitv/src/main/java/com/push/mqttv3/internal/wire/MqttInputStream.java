package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.internal.ExceptionHelper;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MqttInputStream extends InputStream {
    private DataInputStream in;

    public MqttInputStream(InputStream in) {
        this.in = new DataInputStream(in);
    }

    public int read() throws IOException {
        return this.in.read();
    }

    public int available() throws IOException {
        return this.in.available();
    }

    public void close() throws IOException {
        this.in.close();
    }

    public MqttWireMessage readMqttWireMessage() throws IOException, MqttException {
        ByteArrayOutputStream bais = new ByteArrayOutputStream();
        byte first = this.in.readByte();
        byte type = (byte) ((first >>> 4) & 15);
        if (type < (byte) 1 || type > MqttWireMessage.MESSAGE_TYPE_DISCONNECT) {
            throw ExceptionHelper.createMqttException(32108);
        }
        long remLen = MqttWireMessage.readMBI(this.in).getValue();
        bais.write(first);
        bais.write(MqttWireMessage.encodeMBI(remLen));
        byte[] packet = new byte[((int) (((long) bais.size()) + remLen))];
        this.in.readFully(packet, bais.size(), packet.length - bais.size());
        byte[] header = bais.toByteArray();
        System.arraycopy(header, 0, packet, 0, header.length);
        return MqttWireMessage.createWireMessage(packet);
    }
}
