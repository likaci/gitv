package com.push.mqttv3.internal;

import com.gala.sdk.player.IMediaPlayer;
import com.push.mqttv3.MqttCallback;
import com.push.mqttv3.MqttDeliveryToken;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttTopic;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttPubAck;
import com.push.mqttv3.internal.wire.MqttPubComp;
import com.push.mqttv3.internal.wire.MqttPublish;
import java.io.IOException;
import java.util.Vector;
import org.cybergarage.upnp.UPnPStatus;

public class CommsCallback implements Runnable {
    private static int INBOUND_QUEUE_SIZE = 10;
    private Thread callbackThread;
    private ClientComms clientComms;
    private Vector completeQueue;
    private boolean invoking = false;
    private Object lifecycle = new Object();
    private Vector messageQueue;
    private MqttCallback mqttCallback;
    private boolean quiescing = false;
    private boolean running = false;
    private Object spaceAvailable = new Object();
    private Trace trace;
    private Object workAvailable = new Object();

    CommsCallback(Trace trace, ClientComms clientComms) {
        this.trace = trace;
        this.clientComms = clientComms;
        this.messageQueue = new Vector(INBOUND_QUEUE_SIZE);
        this.completeQueue = new Vector(INBOUND_QUEUE_SIZE);
    }

    public void start() {
        if (!this.running) {
            this.running = true;
            this.quiescing = false;
            this.callbackThread = new Thread(this, "Micro Client Callback");
            this.callbackThread.start();
        }
    }

    public void stop() throws IOException {
        if (this.running) {
            this.trace.trace((byte) 1, IMediaPlayer.AD_INFO_QUESTIONNAIRE_AD_READY);
            this.running = false;
            if (!Thread.currentThread().equals(this.callbackThread)) {
                try {
                    synchronized (this.lifecycle) {
                        synchronized (this.workAvailable) {
                            this.trace.trace((byte) 1, UPnPStatus.TRANSATION_NOT_AVAILABLE);
                            this.workAvailable.notifyAll();
                        }
                        this.trace.trace((byte) 1, 702);
                        this.lifecycle.wait();
                    }
                } catch (InterruptedException e) {
                }
            }
            this.trace.trace((byte) 1, 703);
        }
    }

    public void setCallback(MqttCallback mqttCallback) {
        this.mqttCallback = mqttCallback;
    }

    public void run() {
        while (this.running) {
            try {
                synchronized (this.workAvailable) {
                    if (this.messageQueue.isEmpty() && this.completeQueue.isEmpty()) {
                        this.trace.trace((byte) 1, 704);
                        this.workAvailable.wait();
                    }
                }
            } catch (InterruptedException e) {
            }
            if (this.running) {
                if (!(this.completeQueue.isEmpty() || this.mqttCallback == null)) {
                    MqttDeliveryToken token = (MqttDeliveryToken) this.completeQueue.elementAt(0);
                    this.completeQueue.removeElementAt(0);
                    if (this.trace.isOn()) {
                        this.trace.trace((byte) 1, 705, new Object[]{token});
                    }
                    this.mqttCallback.deliveryComplete(token);
                }
                if (!this.messageQueue.isEmpty()) {
                    if (this.quiescing) {
                        this.messageQueue.clear();
                    } else if (this.clientComms.isConnected()) {
                        this.invoking = true;
                        MqttPublish message = (MqttPublish) this.messageQueue.elementAt(0);
                        this.messageQueue.removeElementAt(0);
                        handleMessage(message);
                        this.invoking = false;
                    }
                }
            }
            synchronized (this.spaceAvailable) {
                this.trace.trace((byte) 1, 706);
                this.spaceAvailable.notifyAll();
            }
        }
        this.messageQueue.clear();
        synchronized (this.lifecycle) {
            this.trace.trace((byte) 1, 707);
            this.lifecycle.notifyAll();
        }
    }

    public void connectionLost(Throwable cause) {
        if (this.mqttCallback != null) {
            this.trace.trace((byte) 1, 708, null, cause);
            this.mqttCallback.connectionLost(cause);
        }
    }

    public void messageArrived(MqttPublish sendMessage) {
        if (this.mqttCallback != null) {
            synchronized (this.spaceAvailable) {
                if (!this.quiescing && this.messageQueue.size() >= INBOUND_QUEUE_SIZE) {
                    try {
                        this.trace.trace((byte) 1, 709);
                        this.spaceAvailable.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (!this.quiescing) {
                this.messageQueue.addElement(sendMessage);
                synchronized (this.workAvailable) {
                    this.trace.trace((byte) 1, 710);
                    this.workAvailable.notifyAll();
                }
            }
        }
    }

    public void quiesce() {
        this.quiescing = true;
        synchronized (this.spaceAvailable) {
            this.trace.trace((byte) 1, 711);
            this.spaceAvailable.notifyAll();
        }
        synchronized (this.spaceAvailable) {
            if (this.invoking) {
                try {
                    this.trace.trace((byte) 1, 712);
                    this.spaceAvailable.wait();
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private void handleMessage(MqttPublish publishMessage) {
        if (this.clientComms.isConnected() && this.mqttCallback != null) {
            String destName = publishMessage.getTopicName();
            MqttTopic destination = null;
            if (destName != null) {
                destination = this.clientComms.getTopic(destName);
            }
            try {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 713, new Object[]{destination.getName(), new Long(publishMessage.getMessageId())});
                }
                this.mqttCallback.messageArrived(destination, publishMessage.getMessage());
                if (publishMessage.getMessage().getQos() == 1) {
                    this.clientComms.sendNoWait(new MqttPubAck(publishMessage));
                } else if (publishMessage.getMessage().getQos() == 2) {
                    this.clientComms.deliveryComplete(publishMessage);
                    this.clientComms.sendNoWait(new MqttPubComp(publishMessage));
                }
            } catch (Throwable ex) {
                this.trace.trace((byte) 1, 714, null, ex);
                this.clientComms.shutdownConnection(new MqttException(ex));
            }
        }
    }

    public void deliveryComplete(MqttDeliveryToken token) {
        if (this.mqttCallback != null) {
            this.completeQueue.addElement(token);
            synchronized (this.workAvailable) {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 715, new Object[]{token});
                }
                this.workAvailable.notifyAll();
            }
        }
    }

    protected Thread getThread() {
        return this.callbackThread;
    }
}
