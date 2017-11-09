package com.push.mqttv3;

import com.push.mqttv3.internal.ClientComms;
import com.push.mqttv3.internal.wire.MqttPublish;
import com.push.mqttv3.internal.wire.MqttReceivedMessage;

public class MqttTopic {
    private ClientComms comms;
    private String name;

    MqttTopic(String name, ClientComms comms) {
        this.comms = comms;
        this.name = name;
    }

    public MqttDeliveryToken publish(byte[] payload, int qos, boolean retained) throws MqttException, MqttPersistenceException {
        MqttReceivedMessage message = new MqttReceivedMessage(payload);
        message.setQos(qos);
        message.setRetained(retained);
        return publish(message);
    }

    public MqttDeliveryToken publish(MqttReceivedMessage message) throws MqttException, MqttPersistenceException {
        return this.comms.sendNoWait(createPublish(message));
    }

    public String getName() {
        return this.name;
    }

    private MqttPublish createPublish(MqttReceivedMessage message) {
        return new MqttPublish(getName(), message);
    }

    public String toString() {
        return getName();
    }
}
