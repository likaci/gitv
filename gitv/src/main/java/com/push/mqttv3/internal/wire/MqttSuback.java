package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttSuback extends MqttAck {
    private int[] grantedQos;

    public MqttSuback(byte info, byte[] data) throws IOException {
        super((byte) 9);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
        this.msgId = dis.readLong();
        int index = 0;
        this.grantedQos = new int[(data.length - 8)];
        for (int qos = dis.read(); qos != -1; qos = dis.read()) {
            this.grantedQos[index] = qos;
            index++;
        }
        dis.close();
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }
}
