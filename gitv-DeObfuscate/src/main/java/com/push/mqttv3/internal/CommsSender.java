package com.push.mqttv3.internal;

import com.gala.sdk.player.IMediaPlayer;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttAck;
import com.push.mqttv3.internal.wire.MqttDisconnect;
import com.push.mqttv3.internal.wire.MqttOutputStream;
import com.push.mqttv3.internal.wire.MqttWireMessage;
import java.io.IOException;
import java.io.OutputStream;

public class CommsSender implements Runnable {
    private ClientComms clientComms = null;
    private ClientState clientState = null;
    private Object lifecycle = new Object();
    private MqttOutputStream out;
    private boolean running = false;
    private CommsTokenStore tokenStore = null;
    private Trace trace;

    public CommsSender(Trace trace, ClientComms clientComms, ClientState clientState, CommsTokenStore tokenStore, OutputStream out) {
        this.trace = trace;
        this.out = new MqttOutputStream(out);
        this.clientComms = clientComms;
        this.clientState = clientState;
        this.tokenStore = tokenStore;
    }

    public void start() {
        if (!this.running) {
            this.running = true;
            new Thread(this, "Micro Client Comms Sender").start();
        }
    }

    public void stop() throws IOException {
        synchronized (this.lifecycle) {
            this.trace.trace((byte) 1, IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
            if (this.running) {
                this.running = false;
                try {
                    this.trace.trace((byte) 1, IMediaPlayer.AD_INFO_OVERLAY_PURCHASE_SUCCESS);
                    this.lifecycle.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void run() {
        MqttWireMessage message = null;
        while (this.running && this.out != null) {
            try {
                this.trace.trace((byte) 1, 802);
                message = this.clientState.get();
                if (message != null) {
                    if (message instanceof MqttAck) {
                        this.out.write(message);
                        this.out.flush();
                    } else {
                        synchronized (this.tokenStore.getToken(message)) {
                            this.out.write(message);
                            this.out.flush();
                            this.clientState.notifySent(message);
                        }
                    }
                    if (message instanceof MqttDisconnect) {
                        synchronized (this.lifecycle) {
                            this.trace.trace((byte) 1, 803);
                            this.running = false;
                        }
                    }
                } else {
                    synchronized (this.lifecycle) {
                        this.running = false;
                    }
                }
            } catch (MqttException me) {
                synchronized (this.lifecycle) {
                    this.running = false;
                    this.clientComms.shutdownConnection(me);
                }
            } catch (Exception ioe) {
                this.trace.trace((byte) 1, 804, null, ioe);
                if (message != null && (message instanceof MqttDisconnect)) {
                    this.clientState.notifySent(message);
                }
                this.running = false;
                this.clientComms.shutdownConnection(new MqttException(32109, ioe));
            }
        }
        synchronized (this.lifecycle) {
            this.trace.trace((byte) 1, 805);
            this.lifecycle.notifyAll();
        }
    }

    public boolean isRunning() {
        return this.running;
    }
}
