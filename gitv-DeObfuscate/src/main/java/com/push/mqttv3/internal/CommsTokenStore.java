package com.push.mqttv3.internal;

import com.gala.sdk.player.TipType;
import com.push.mqttv3.MqttDeliveryToken;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttAck;
import com.push.mqttv3.internal.wire.MqttConnack;
import com.push.mqttv3.internal.wire.MqttConnect;
import com.push.mqttv3.internal.wire.MqttDisconnect;
import com.push.mqttv3.internal.wire.MqttPingReq;
import com.push.mqttv3.internal.wire.MqttPingResp;
import com.push.mqttv3.internal.wire.MqttPubRel;
import com.push.mqttv3.internal.wire.MqttPublish;
import com.push.mqttv3.internal.wire.MqttWireMessage;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class CommsTokenStore {
    private MqttDeliveryTokenImpl connectToken;
    private MqttDeliveryTokenImpl disconnectToken;
    private boolean noMoreResponses = false;
    private MqttException noMoreResponsesException = null;
    private MqttDeliveryTokenImpl pingToken;
    private Hashtable tokens = new Hashtable();
    private Trace trace;

    public CommsTokenStore(Trace trace) {
        this.trace = trace;
        this.pingToken = new MqttDeliveryTokenImpl(trace);
        this.connectToken = new MqttDeliveryTokenImpl(trace);
        this.disconnectToken = new MqttDeliveryTokenImpl(trace);
    }

    public MqttDeliveryTokenImpl getToken(MqttWireMessage message) {
        if (message instanceof MqttAck) {
            return getTokenForAck((MqttAck) message);
        }
        Object obj;
        if (message instanceof MqttPingReq) {
            obj = this.pingToken;
        } else if (message instanceof MqttConnect) {
            obj = this.connectToken;
        } else if (message instanceof MqttDisconnect) {
            obj = this.disconnectToken;
        } else {
            obj = new Long(message.getMessageId());
        }
        return (MqttDeliveryTokenImpl) this.tokens.get(obj);
    }

    private MqttDeliveryTokenImpl getTokenForAck(MqttWireMessage message) {
        if (message instanceof MqttPingResp) {
            return this.pingToken;
        }
        if (message instanceof MqttConnack) {
            return this.connectToken;
        }
        return (MqttDeliveryTokenImpl) this.tokens.get(new Long(message.getMessageId()));
    }

    public MqttDeliveryTokenImpl removeToken(MqttWireMessage message) {
        Object obj;
        if (message instanceof MqttConnack) {
            obj = this.connectToken;
        } else if (message instanceof MqttDisconnect) {
            obj = this.disconnectToken;
        } else {
            obj = new Long(message.getMessageId());
        }
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 301, new Object[]{message, obj});
        }
        return (MqttDeliveryTokenImpl) this.tokens.remove(obj);
    }

    protected MqttDeliveryTokenImpl restoreToken(MqttPublish message) {
        MqttDeliveryTokenImpl token;
        Long key = new Long(message.getMessageId());
        if (this.tokens.containsKey(key)) {
            token = (MqttDeliveryTokenImpl) this.tokens.get(key);
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 302, new Object[]{message, key, token});
            }
        } else {
            token = new MqttDeliveryTokenImpl(this.trace, message);
            this.tokens.put(key, token);
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 303, new Object[]{message, key, token});
            }
        }
        return token;
    }

    protected MqttDeliveryTokenImpl saveToken(MqttWireMessage message) {
        MqttDeliveryTokenImpl token;
        Object obj;
        if (message instanceof MqttPingReq) {
            token = this.pingToken;
            obj = token;
        } else if (message instanceof MqttConnect) {
            this.noMoreResponses = false;
            this.noMoreResponsesException = null;
            this.connectToken = new MqttDeliveryTokenImpl(this.trace);
            token = this.connectToken;
            key = token;
        } else if (message instanceof MqttDisconnect) {
            this.disconnectToken = new MqttDeliveryTokenImpl(this.trace);
            token = this.disconnectToken;
            key = token;
        } else if (message instanceof MqttPubRel) {
            obj = new Long(message.getMessageId());
            token = getToken(message);
        } else if (message instanceof MqttPublish) {
            obj = new Long(message.getMessageId());
            token = new MqttDeliveryTokenImpl(this.trace, (MqttPublish) message);
        } else {
            obj = new Long(message.getMessageId());
            token = new MqttDeliveryTokenImpl(this.trace);
        }
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 300, new Object[]{message, obj, token.toString()});
        }
        this.tokens.put(obj, token);
        if (this.noMoreResponses) {
            token.notifyException(this.noMoreResponsesException);
        }
        return token;
    }

    protected void responseReceived(MqttAck ack) {
        MqttDeliveryTokenImpl token = getTokenForAck(ack);
        removeToken(ack);
        if (token != null) {
            token.notifyReceived(ack);
        }
    }

    protected void noMoreResponses(MqttException reason) {
        this.noMoreResponses = true;
        this.noMoreResponsesException = reason;
        Enumeration enumeration = this.tokens.elements();
        this.trace.trace((byte) 1, TipType.CONCRETE_TYPE_REPLAY_PLAYNEXT, null, reason);
        while (enumeration.hasMoreElements()) {
            Object token = enumeration.nextElement();
            if (token != null) {
                synchronized (token) {
                    ((MqttDeliveryTokenImpl) token).notifyException(reason);
                }
            }
        }
    }

    public MqttDeliveryToken[] getOutstandingTokens() {
        Vector list = new Vector();
        Enumeration enumeration = this.tokens.elements();
        while (enumeration.hasMoreElements()) {
            MqttDeliveryToken token = (MqttDeliveryToken) enumeration.nextElement();
            if (!(token == null || token.equals(this.pingToken) || token.equals(this.connectToken) || token.equals(this.disconnectToken))) {
                list.addElement(token);
            }
        }
        MqttDeliveryToken[] result = new MqttDeliveryToken[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = (MqttDeliveryToken) list.elementAt(i);
        }
        return result;
    }

    public void clear() {
        this.trace.trace((byte) 1, TipType.CONCRETE_TYPE_HISTORY);
        this.tokens.clear();
    }
}
