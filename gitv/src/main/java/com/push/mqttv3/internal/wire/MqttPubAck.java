package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttPubAck extends MqttAck {
    public MqttPubAck(byte info, byte[] data) throws IOException {
        super((byte) 4);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        this.msgId = dis.readLong();
        dis.close();
    }

    public MqttPubAck(MqttPublish publish) {
        super((byte) 4);
        this.msgId = publish.getMessageId();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
