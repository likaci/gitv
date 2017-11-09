package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class MqttUnsubscribe extends MqttWireMessage {
    private String[] names;

    public MqttUnsubscribe(String[] names) {
        super((byte) 10);
        this.names = names;
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
            for (String writeUTF : this.names) {
                dos.writeUTF(writeUTF);
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
