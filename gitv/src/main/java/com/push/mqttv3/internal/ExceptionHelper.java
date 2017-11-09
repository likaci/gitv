package com.push.mqttv3.internal;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttSecurityException;

public class ExceptionHelper {
    public static MqttException createMqttException(int reasonCode) {
        if (reasonCode == 4 || reasonCode == 5) {
            return new MqttSecurityException(reasonCode);
        }
        return new MqttException(reasonCode);
    }

    public static MqttException createMqttException(Throwable cause) {
        if (cause.getClass().getName().equals("java.security.GeneralSecurityException")) {
            return new MqttSecurityException(cause);
        }
        return new MqttException(cause);
    }

    public static boolean isClassAvailable(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
