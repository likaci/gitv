package com.push.mqttv3.internal;

import com.push.mqttv3.MqttDeliveryToken;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttMessage;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttAck;
import com.push.mqttv3.internal.wire.MqttPublish;
import com.push.mqttv3.internal.wire.MqttWireMessage;
import org.cybergarage.upnp.UPnPStatus;

public class MqttDeliveryTokenImpl implements MqttDeliveryToken {
    private boolean completed;
    private MqttException exception;
    private MqttMessage message;
    private long msgId;
    private MqttWireMessage response;
    private Object responseLock;
    private boolean sent;
    private Object sentLock;
    private Trace trace;

    MqttDeliveryTokenImpl(Trace trace) {
        this.responseLock = new Object();
        this.sentLock = new Object();
        this.response = null;
        this.exception = null;
        this.sent = false;
        this.completed = false;
        this.msgId = 0;
        this.message = null;
        this.trace = trace;
    }

    MqttDeliveryTokenImpl(Trace trace, MqttPublish send) {
        this.responseLock = new Object();
        this.sentLock = new Object();
        this.response = null;
        this.exception = null;
        this.sent = false;
        this.completed = false;
        this.msgId = 0;
        this.trace = trace;
        this.message = send.getMessage();
        this.msgId = send.getMessageId();
    }

    public void waitForCompletion(long timeout) throws MqttException {
        if (waitForResponse(timeout) == null && !this.completed) {
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 406, new Object[]{new Long(timeout)});
            }
            throw new MqttException(32000);
        }
    }

    public void waitForCompletion() throws MqttException {
        waitForCompletion(-1);
    }

    protected MqttWireMessage waitForResponse() throws MqttException {
        return waitForResponse(-1);
    }

    protected MqttWireMessage waitForResponse(long timeout) throws MqttException {
        synchronized (this.responseLock) {
            if (this.trace.isOn()) {
                Trace trace = this.trace;
                Object[] objArr = new Object[6];
                objArr[0] = this;
                objArr[1] = new Long(timeout);
                objArr[2] = new Boolean(this.sent);
                objArr[3] = new Boolean(this.completed);
                objArr[4] = this.exception == null ? "false" : "true";
                objArr[5] = this.response;
                trace.trace((byte) 1, 400, objArr, this.exception);
            }
            if (this.completed) {
                MqttWireMessage mqttWireMessage = this.response;
                return mqttWireMessage;
            }
            if (this.exception == null) {
                if (timeout == -1) {
                    try {
                        this.responseLock.wait();
                    } catch (InterruptedException e) {
                    }
                } else {
                    this.responseLock.wait(timeout);
                }
            }
            if (this.completed || this.exception == null) {
                this.trace.trace((byte) 1, 402, new Object[]{this.response});
                return this.response;
            }
            MqttException e2 = this.exception;
            this.exception = null;
            this.trace.trace((byte) 1, UPnPStatus.INVALID_ACTION, null, this.exception);
            throw e2;
        }
    }

    protected void waitUntilSent() throws MqttException {
        synchronized (this.sentLock) {
            synchronized (this.responseLock) {
                if (this.exception != null) {
                    throw this.exception;
                }
            }
            if (!this.sent) {
                try {
                    this.sentLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (this.sent) {
            } else if (this.exception == null) {
                throw ExceptionHelper.createMqttException(6);
            } else {
                throw this.exception;
            }
        }
    }

    protected void notifySent() {
        this.trace.trace((byte) 1, UPnPStatus.OUT_OF_SYNC, new Object[]{this});
        synchronized (this.responseLock) {
            this.response = null;
            this.completed = false;
        }
        synchronized (this.sentLock) {
            this.sent = true;
            this.sentLock.notifyAll();
        }
    }

    protected void notifyReceived(MqttWireMessage msg) {
        this.trace.trace((byte) 1, 404, new Object[]{this, msg});
        synchronized (this.responseLock) {
            if (msg instanceof MqttAck) {
                this.message = null;
            }
            this.response = msg;
            this.completed = true;
            this.responseLock.notifyAll();
        }
    }

    protected void notifyException(MqttException exception) {
        this.trace.trace((byte) 1, 405, new Object[]{this}, exception);
        synchronized (this.responseLock) {
            this.exception = exception;
            this.responseLock.notifyAll();
        }
        synchronized (this.sentLock) {
            this.sentLock.notifyAll();
        }
    }

    public MqttMessage getMessage() throws MqttException {
        return this.message;
    }

    public boolean isComplete() {
        return this.completed;
    }

    public long getMessageId() {
        return this.msgId;
    }
}
