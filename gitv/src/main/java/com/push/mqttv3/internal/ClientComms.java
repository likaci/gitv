package com.push.mqttv3.internal;

import com.gala.sdk.player.ISdkError;
import com.push.mqttv3.MqttCallback;
import com.push.mqttv3.MqttClientPersistence;
import com.push.mqttv3.MqttDeliveryToken;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttPersistenceException;
import com.push.mqttv3.MqttTopic;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttConnack;
import com.push.mqttv3.internal.wire.MqttConnect;
import com.push.mqttv3.internal.wire.MqttDisconnect;
import com.push.mqttv3.internal.wire.MqttPublish;
import com.push.mqttv3.internal.wire.MqttWireMessage;
import java.io.IOException;

public class ClientComms {
    private CommsCallback callback;
    private ClientState clientState;
    private boolean connected;
    private int connectionTimeoutSecs;
    private DestinationProvider destinationProvider;
    private Thread disconnectThread = null;
    private boolean disconnecting = false;
    private NetworkModule networkModule;
    private MqttClientPersistence persistence;
    private CommsReceiver receiver;
    private CommsSender sender;
    private CommsTokenStore tokenStore;
    private Trace trace;

    public ClientComms(DestinationProvider destinationProvider, MqttClientPersistence persistence, Trace trace) throws MqttException {
        this.trace = trace;
        this.callback = new CommsCallback(trace, this);
        this.connected = false;
        this.tokenStore = new CommsTokenStore(this.trace);
        this.destinationProvider = destinationProvider;
        this.clientState = new ClientState(trace, persistence, this.tokenStore, this.callback);
        this.persistence = persistence;
    }

    private MqttDeliveryTokenImpl internalSend(MqttWireMessage message) throws MqttException {
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 200, new Object[]{message.getClass().getName()});
        }
        if (this.disconnecting || !this.connected) {
            this.trace.trace((byte) 1, 208, new Object[]{new Boolean(this.disconnecting), new Boolean(this.connected)});
            throw ExceptionHelper.createMqttException(32104);
        }
        MqttDeliveryTokenImpl token = this.clientState.send(message);
        if (message instanceof MqttPublish) {
            try {
                this.clientState.incrementWaitingTokens();
                token.waitUntilSent();
                this.clientState.decrementWaitingTokens();
            } catch (MqttException me) {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 202, null, me);
                }
                this.clientState.undo((MqttPublish) message);
                throw me;
            } catch (Throwable th) {
                this.clientState.decrementWaitingTokens();
            }
        }
        return token;
    }

    public void sendAndWait(MqttWireMessage message) throws MqttException {
        internalSend(message).waitForCompletion((long) (this.connectionTimeoutSecs * 1000));
    }

    public MqttDeliveryTokenImpl sendNoWait(MqttWireMessage message) throws MqttException {
        return internalSend(message);
    }

    public MqttConnack connect(MqttConnect connect, int connectionTimeoutSecs, long keepAliveSecs, boolean cleanSession) throws MqttException {
        if (this.connected) {
            this.trace.trace((byte) 1, 207);
            throw ExceptionHelper.createMqttException(32100);
        }
        this.disconnecting = false;
        this.connectionTimeoutSecs = connectionTimeoutSecs;
        this.clientState.setKeepAliveSecs(keepAliveSecs);
        this.clientState.setCleanSession(cleanSession);
        try {
            this.networkModule.start();
            this.receiver = new CommsReceiver(this.trace, this, this.clientState, this.tokenStore, this.networkModule.getInputStream());
            this.receiver.start();
            this.sender = new CommsSender(this.trace, this, this.clientState, this.tokenStore, this.networkModule.getOutputStream());
            this.sender.start();
            this.callback.start();
            try {
                MqttWireMessage ack = this.clientState.send(connect).waitForResponse((long) (connectionTimeoutSecs * 1000));
                if (ack == null) {
                    this.trace.trace((byte) 1, 203);
                    this.persistence.close();
                    throw ExceptionHelper.createMqttException(32000);
                } else if (ack instanceof MqttConnack) {
                    MqttConnack cack = (MqttConnack) ack;
                    if (cack.getReturnCode() != 0) {
                        this.trace.trace((byte) 1, 204, new Object[]{new Integer(cack.getReturnCode())});
                        this.persistence.close();
                        this.disconnectThread = Thread.currentThread();
                        shutdownConnection(null);
                        throw ExceptionHelper.createMqttException(cack.getReturnCode());
                    }
                    this.connected = true;
                    return (MqttConnack) ack;
                } else {
                    this.trace.trace((byte) 1, ISdkError.MODULE_SERVER_TV, new Object[]{ack});
                    this.persistence.close();
                    throw ExceptionHelper.createMqttException(6);
                }
            } catch (MqttException ex) {
                this.trace.trace((byte) 1, 206, null, ex);
                shutdownConnection(null);
                throw ex;
            }
        } catch (Throwable ex2) {
            this.trace.trace((byte) 1, 209, null, ex2);
            this.persistence.close();
            throw ExceptionHelper.createMqttException(ex2);
        } catch (MqttException ex3) {
            this.trace.trace((byte) 1, 212, null, ex3);
            this.persistence.close();
            throw ex3;
        }
    }

    public void shutdownConnection(MqttException reason) {
        if (this.disconnectThread == null || this.disconnectThread.equals(Thread.currentThread())) {
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 201, new Object[]{new Boolean(this.disconnecting)}, reason);
            }
            if (this.disconnecting) {
                this.connected = false;
                return;
            }
            boolean wasConnected = this.connected;
            this.disconnecting = true;
            this.clientState.disconnecting(reason);
            try {
                this.callback.stop();
            } catch (IOException e) {
            }
            try {
                this.networkModule.stop();
            } catch (IOException e2) {
            }
            try {
                this.receiver.stop();
            } catch (IOException e3) {
            }
            this.clientState.disconnected(reason);
            try {
                this.sender.stop();
            } catch (IOException e4) {
            }
            this.connected = false;
            if (wasConnected && reason != null) {
                this.callback.connectionLost(reason);
            }
        }
    }

    public void disconnect(MqttDisconnect disconnect, long quiesceTimeout) throws MqttException {
        if (!this.connected) {
            this.trace.trace((byte) 1, 211);
            throw ExceptionHelper.createMqttException(32101);
        } else if (Thread.currentThread() == this.callback.getThread()) {
            this.trace.trace((byte) 1, 210);
            throw ExceptionHelper.createMqttException(32107);
        } else {
            this.clientState.quiesce(quiesceTimeout);
            this.receiver.setDisconnecting(true);
            try {
                this.disconnectThread = Thread.currentThread();
                sendNoWait(disconnect).waitUntilSent();
                shutdownConnection(null);
                this.disconnectThread = null;
            } catch (MqttException ex) {
                throw ex;
            } catch (Throwable th) {
                shutdownConnection(null);
                this.disconnectThread = null;
            }
        }
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setCallback(MqttCallback mqttCallback) {
        this.callback.setCallback(mqttCallback);
    }

    protected MqttTopic getTopic(String topic) {
        return this.destinationProvider.getTopic(topic);
    }

    public void setNetworkModule(NetworkModule networkModule) {
        this.networkModule = networkModule;
    }

    public MqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.tokenStore.getOutstandingTokens();
    }

    protected void deliveryComplete(MqttPublish msg) throws MqttPersistenceException {
        this.clientState.deliveryComplete(msg);
    }
}
