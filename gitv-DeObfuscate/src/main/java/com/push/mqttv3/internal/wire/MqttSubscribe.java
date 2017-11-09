package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class MqttSubscribe extends MqttWireMessage {
    private String[] names;
    private int[] qos;

    public MqttSubscribe(String[] names, int[] qos) {
        super((byte) 8);
        this.names = names;
        this.qos = qos;
    }

    protected byte getMessageInfo() {
        return (byte) ((this.duplicate ? 8 : 0) | 2);
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeLong(this.msgId);
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            for (int i = 0; i < this.names.length; i++) {
                dos.writeUTF(this.names[i]);
                dos.writeByte(this.qos[i]);
            }
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isRetryable() {
        return true;
    }
}
