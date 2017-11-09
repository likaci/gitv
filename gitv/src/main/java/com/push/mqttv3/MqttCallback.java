package com.push.mqttv3;

import com.push.mqttv3.internal.wire.MqttReceivedMessage;

public interface MqttCallback {
    void connectionLost(Throwable th);

    void deliveryComplete(MqttDeliveryToken mqttDeliveryToken);

    void messageArrived(MqttTopic mqttTopic, MqttReceivedMessage mqttReceivedMessage) throws Exception;
}
