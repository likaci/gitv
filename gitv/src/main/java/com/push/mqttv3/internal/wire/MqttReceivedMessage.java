package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttMessage;

public class MqttReceivedMessage extends MqttMessage {
    private long messageId;

    public void setMessageId(long msgId) {
        this.messageId = msgId;
    }

    public long getMessageId() {
        return this.messageId;
    }

    public MqttReceivedMessage(byte[] payload) {
        super(payload);
    }

    public void setDuplicate(boolean value) {
        super.setDuplicate(value);
    }
}
