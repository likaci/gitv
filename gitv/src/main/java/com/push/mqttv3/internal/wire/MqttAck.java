package com.push.mqttv3.internal.wire;

public abstract class MqttAck extends MqttWireMessage {
    public MqttAck(byte type) {
        super(type);
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }
}
