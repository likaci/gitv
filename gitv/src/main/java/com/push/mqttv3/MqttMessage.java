package com.push.mqttv3;

public class MqttMessage {
    private boolean dup = false;
    private boolean mutable = true;
    private byte[] payload;
    private int qos = 1;
    private boolean retained = false;

    protected static void validateQos(int qos) {
        if (qos < 0 || qos > 2) {
            throw new IllegalArgumentException();
        }
    }

    public MqttMessage() {
        setPayload(new byte[0]);
    }

    public MqttMessage(byte[] payload) {
        setPayload(payload);
    }

    public byte[] getPayload() throws MqttException {
        return this.payload;
    }

    public void clearPayload() {
        checkMutable();
        this.payload = new byte[0];
    }

    public void setPayload(byte[] payload) {
        checkMutable();
        this.payload = payload;
    }

    public boolean isRetained() {
        return this.retained;
    }

    public void setRetained(boolean retained) {
        checkMutable();
        this.retained = retained;
    }

    public int getQos() {
        return this.qos;
    }

    public void setQos(int qos) {
        checkMutable();
        validateQos(qos);
        this.qos = qos;
    }

    public String toString() {
        return new String(this.payload);
    }

    protected void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    protected void checkMutable() throws IllegalStateException {
        if (!this.mutable) {
            throw new IllegalStateException();
        }
    }

    protected void setDuplicate(boolean dup) {
        this.dup = dup;
    }

    public boolean isDuplicate() {
        return this.dup;
    }
}
