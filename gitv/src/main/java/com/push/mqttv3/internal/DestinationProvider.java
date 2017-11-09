package com.push.mqttv3.internal;

import com.push.mqttv3.MqttTopic;

public interface DestinationProvider {
    MqttTopic getTopic(String str);
}
