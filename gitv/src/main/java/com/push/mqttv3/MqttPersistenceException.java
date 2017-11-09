package com.push.mqttv3;

public class MqttPersistenceException extends MqttException {
    public static final short REASON_CODE_PERSISTENCE_IN_USE = (short) 32200;
    private static final long serialVersionUID = 300;

    public MqttPersistenceException() {
        super(0);
    }

    public MqttPersistenceException(int reasonCode) {
        super(reasonCode);
    }

    public MqttPersistenceException(Throwable cause) {
        super(cause);
    }
}
