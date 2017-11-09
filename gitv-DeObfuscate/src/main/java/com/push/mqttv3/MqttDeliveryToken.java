package com.push.mqttv3;

public interface MqttDeliveryToken {
    MqttMessage getMessage() throws MqttException;

    boolean isComplete();

    void waitForCompletion() throws MqttException, MqttSecurityException;

    void waitForCompletion(long j) throws MqttException, MqttSecurityException;
}
