package com.push.mqttv3.internal;

import com.push.mqttv3.MqttDeliveryToken;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttAck;
import com.push.mqttv3.internal.wire.MqttConnack;
import com.push.mqttv3.internal.wire.MqttInputStream;
import com.push.mqttv3.internal.wire.MqttWireMessage;
import java.io.IOException;
import java.io.InputStream;

public class CommsReceiver implements Runnable {
    private ClientComms clientComms = null;
    private ClientState clientState = null;
    private boolean disconnecting = false;
    private MqttInputStream in;
    private Object lifecycle = new Object();
    private boolean running = false;
    private CommsTokenStore tokenStore = null;
    private Trace trace;

    public CommsReceiver(Trace trace, ClientComms clientComms, ClientState clientState, CommsTokenStore tokenStore, InputStream in) {
        this.in = new MqttInputStream(in);
        this.clientComms = clientComms;
        this.clientState = clientState;
        this.tokenStore = tokenStore;
        this.trace = trace;
    }

    public void start() {
        if (!this.running) {
            this.running = true;
            new Thread(this, "Micro Client Comms Receiver").start();
        }
    }

    public void stop() throws IOException {
        synchronized (this.lifecycle) {
            this.trace.trace((byte) 1, 850);
            if (this.running) {
                this.running = false;
                try {
                    this.trace.trace((byte) 1, 851);
                    this.lifecycle.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void run() {
        while (this.running && this.in != null) {
            try {
                this.trace.trace((byte) 1, 852);
                MqttWireMessage message = this.in.readMqttWireMessage();
                if (message instanceof MqttAck) {
                    MqttDeliveryToken token = this.tokenStore.getToken(message);
                    if (token != null) {
                        synchronized (token) {
                            this.clientState.notifyReceived(message);
                            if ((message instanceof MqttConnack) && ((MqttConnack) message).getReturnCode() != 0) {
                                synchronized (this.lifecycle) {
                                    this.running = false;
                                }
                            }
                        }
                    } else {
                        this.clientState.notifyReceived(message);
                    }
                } else {
                    this.clientState.notifyReceived(message);
                }
            } catch (MqttException ex) {
                this.running = false;
                if (this.clientComms != null) {
                    this.clientComms.shutdownConnection(ex);
                }
            } catch (IOException ioe) {
                this.trace.trace((byte) 1, 853, null, ioe);
                this.running = false;
                if (this.disconnecting) {
                    if (this.clientComms != null) {
                        this.clientComms.shutdownConnection(null);
                    }
                } else if (this.clientComms != null) {
                    this.clientComms.shutdownConnection(new MqttException(32109, ioe));
                }
            }
        }
        synchronized (this.lifecycle) {
            this.trace.trace((byte) 1, 854);
            this.lifecycle.notifyAll();
        }
    }

    public boolean isRunning() {
        return this.running;
    }

    public void setDisconnecting(boolean disconnecting) {
        this.trace.trace((byte) 1, 855, new Object[]{new Boolean(disconnecting)});
        this.disconnecting = disconnecting;
    }
}
