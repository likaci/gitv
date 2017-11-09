package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;

public class MqttPingReq extends MqttWireMessage {
    public MqttPingReq() {
        super(MqttWireMessage.MESSAGE_TYPE_PINGREQ);
    }

    public boolean isMessageIdRequired() {
        return false;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }
}
