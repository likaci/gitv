package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttPubComp extends MqttAck {
    public MqttPubComp(byte info, byte[] data) throws IOException {
        super((byte) 7);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        this.msgId = dis.readLong();
        dis.close();
    }

    public MqttPubComp(MqttPublish publish) {
        super((byte) 7);
        this.msgId = publish.getMessageId();
    }

    public MqttPubComp(Long msgId) {
        super((byte) 7);
        this.msgId = msgId.longValue();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }
}
