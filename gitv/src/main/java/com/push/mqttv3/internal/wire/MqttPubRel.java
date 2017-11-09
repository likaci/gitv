package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttPubRel extends MqttPersistableWireMessage {
    public MqttPubRel(MqttPubRec pubRec) {
        super((byte) 6);
        setMessageId(pubRec.getMessageId());
    }

    public MqttPubRel(byte info, byte[] data) throws IOException {
        super((byte) 6);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        this.msgId = dis.readLong();
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return encodeMessageId();
    }

    protected byte getMessageInfo() {
        return (byte) ((this.duplicate ? 8 : 0) | 2);
    }
}
