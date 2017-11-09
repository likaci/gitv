package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttPubRec extends MqttAck {
    public MqttPubRec(byte info, byte[] data) throws IOException {
        super((byte) 5);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        this.msgId = dis.readLong();
        dis.close();
    }

    public MqttPubRec(MqttPublish publish) {
        super((byte) 5);
        this.msgId = publish.getMessageId();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
