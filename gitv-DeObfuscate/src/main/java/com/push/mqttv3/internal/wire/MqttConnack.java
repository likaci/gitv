package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class MqttConnack extends MqttAck {
    private int returnCode;

    public MqttConnack(byte info, byte[] variableHeader) throws IOException {
        super((byte) 2);
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(variableHeader));
        dis.readByte();
        this.returnCode = dis.readUnsignedByte();
        dis.close();
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    public boolean isMessageIdRequired() {
        return false;
    }
}
