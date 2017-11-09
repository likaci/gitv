package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;

public class MqttDisconnect extends MqttWireMessage {
    public MqttDisconnect() {
        super(MqttWireMessage.MESSAGE_TYPE_DISCONNECT);
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    protected byte[] getVariableHeader() throws MqttException {
        return new byte[0];
    }

    public boolean isMessageIdRequired() {
        return false;
    }
}
