package com.push.mqttv3.internal.comms;

public class MqttSSLInitException extends MqttDirectException {
    public MqttSSLInitException(String reason, Throwable theCause) {
        super(reason, theCause);
    }

    public MqttSSLInitException(long theMsgId, Object[] theInserts) {
        super(theMsgId, theInserts);
    }

    public MqttSSLInitException(long theMsgId, Object[] theInserts, Throwable cause) {
        super(theMsgId, theInserts, cause);
    }
}
