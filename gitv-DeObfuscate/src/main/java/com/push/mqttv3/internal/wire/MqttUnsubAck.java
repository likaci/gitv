package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttUnsubAck extends MqttAck {
    public MqttUnsubAck(byte info, byte[] data) throws IOException {
        super((byte) 11);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        this.msgId = dis.readLong();
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }
}
