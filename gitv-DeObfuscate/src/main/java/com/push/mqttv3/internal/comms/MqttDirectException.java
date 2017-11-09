package com.push.mqttv3.internal.comms;

public class MqttDirectException extends Exception {
    protected Object[] inserts = null;
    protected Throwable linkt = null;
    protected long msgId = 0;

    public MqttDirectException(String reason, Throwable theCause) {
        super(reason);
        this.linkt = theCause;
    }

    public MqttDirectException(long theMsgId, Object[] theInserts) {
        this.msgId = theMsgId;
        this.inserts = theInserts;
    }

    public MqttDirectException(long theMsgId, Object[] theInserts, Throwable cause) {
        this.msgId = theMsgId;
        this.inserts = theInserts;
        this.linkt = cause;
    }

    public Throwable getCause() {
        return this.linkt;
    }

    public Object[] getInserts() {
        return this.inserts;
    }

    public long getMsgId() {
        return this.msgId;
    }
}
