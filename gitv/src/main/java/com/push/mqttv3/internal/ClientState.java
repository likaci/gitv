package com.push.mqttv3.internal;

import com.gala.sdk.player.IMediaPlayer;
import com.push.mqttv3.MqttClientPersistence;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttMessage;
import com.push.mqttv3.MqttPersistable;
import com.push.mqttv3.MqttPersistenceException;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttAck;
import com.push.mqttv3.internal.wire.MqttConnack;
import com.push.mqttv3.internal.wire.MqttConnect;
import com.push.mqttv3.internal.wire.MqttDisconnect;
import com.push.mqttv3.internal.wire.MqttPingReq;
import com.push.mqttv3.internal.wire.MqttPingResp;
import com.push.mqttv3.internal.wire.MqttPubAck;
import com.push.mqttv3.internal.wire.MqttPubComp;
import com.push.mqttv3.internal.wire.MqttPubRec;
import com.push.mqttv3.internal.wire.MqttPubRel;
import com.push.mqttv3.internal.wire.MqttPublish;
import com.push.mqttv3.internal.wire.MqttSubscribe;
import com.push.mqttv3.internal.wire.MqttUnsubscribe;
import com.push.mqttv3.internal.wire.MqttWireMessage;
import java.io.EOFException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

public class ClientState {
    private static final int MAX_MSG_ID = 65535;
    private static final long MIN_MSG_ID = 1;
    private static final String PERSISTENCE_CONFIRMED_PREFIX = "sc-";
    private static final String PERSISTENCE_RECEIVED_PREFIX = "r-";
    private static final String PERSISTENCE_SENT_PREFIX = "s-";
    private int actualInFlight = 0;
    private CommsCallback callback = null;
    private boolean cleanSession;
    private boolean connectFailed = false;
    private boolean connected = false;
    private int inFlightPubRels = 0;
    private Hashtable inUseMsgIds;
    private Hashtable inboundQoS2 = null;
    private long keepAlive;
    private long lastInboundActivity = 0;
    private long lastOutboundActivity = 0;
    private int maxInflight = 10;
    private Long nextMsgId = Long.valueOf(0);
    private Hashtable outboundQoS1 = null;
    private Hashtable outboundQoS2 = null;
    private Vector pendingFlows;
    private Vector pendingMessages;
    private MqttClientPersistence persistence;
    private MqttWireMessage pingCommand;
    private boolean pingOutstanding = false;
    private Object queueLock = new Object();
    private Object quiesceLock = new Object();
    private boolean quiescing = false;
    private boolean sentConnect = false;
    private CommsTokenStore tokenStore;
    private Trace trace;
    private int waitingTokens = 0;
    private Object waitingTokensLock = new Object();

    protected ClientState(Trace trace, MqttClientPersistence persistence, CommsTokenStore tokenStore, CommsCallback callback) throws MqttException {
        this.trace = trace;
        this.inUseMsgIds = new Hashtable();
        this.pendingMessages = new Vector(this.maxInflight);
        this.pendingFlows = new Vector();
        this.outboundQoS2 = new Hashtable();
        this.outboundQoS1 = new Hashtable();
        this.inboundQoS2 = new Hashtable();
        this.pingCommand = new MqttPingReq();
        this.inFlightPubRels = 0;
        this.actualInFlight = 0;
        this.persistence = persistence;
        this.callback = callback;
        this.tokenStore = tokenStore;
        restoreState();
    }

    protected void setKeepAliveSecs(long keepAliveSecs) {
        this.keepAlive = 1000 * keepAliveSecs;
    }

    protected void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }

    private String getSendPersistenceKey(MqttWireMessage message) {
        return PERSISTENCE_SENT_PREFIX + message.getMessageId();
    }

    private String getSendConfirmPersistenceKey(MqttWireMessage message) {
        return PERSISTENCE_CONFIRMED_PREFIX + message.getMessageId();
    }

    private String getReceivedPersistenceKey(MqttWireMessage message) {
        return PERSISTENCE_RECEIVED_PREFIX + message.getMessageId();
    }

    protected void clearState() throws MqttException {
        this.trace.trace((byte) 1, 603);
        this.persistence.clear();
        this.inUseMsgIds.clear();
        this.pendingMessages.clear();
        this.pendingFlows.clear();
        this.outboundQoS2.clear();
        this.outboundQoS1.clear();
        this.inboundQoS2.clear();
        this.tokenStore.clear();
    }

    private MqttWireMessage restoreMessage(String key, MqttPersistable persistable) throws MqttException {
        MqttWireMessage message = null;
        try {
            message = MqttWireMessage.createWireMessage(persistable);
        } catch (MqttException ex) {
            this.trace.trace((byte) 1, 602, new Object[]{key}, ex);
            if (!(ex.getCause() instanceof EOFException)) {
                throw ex;
            } else if (key != null) {
                this.persistence.remove(key);
            }
        }
        this.trace.trace((byte) 1, 601, new Object[]{key, message});
        return message;
    }

    private void insertInOrder(Vector list, MqttWireMessage newMsg) {
        long newMsgId = newMsg.getMessageId();
        for (int i = 0; i < list.size(); i++) {
            if (((MqttWireMessage) list.elementAt(i)).getMessageId() > newMsgId) {
                list.insertElementAt(newMsg, i);
                return;
            }
        }
        list.addElement(newMsg);
    }

    private Vector reOrder(Vector list) {
        Vector newList = new Vector();
        if (list.size() != 0) {
            int i;
            long previousMsgId = 0;
            long largestGap = 0;
            long largestGapMsgIdPosInList = 0;
            for (i = 0; i < list.size(); i++) {
                long currentMsgId = ((MqttWireMessage) list.elementAt(i)).getMessageId();
                if (currentMsgId - previousMsgId > largestGap) {
                    largestGap = currentMsgId - previousMsgId;
                    largestGapMsgIdPosInList = (long) i;
                }
                previousMsgId = currentMsgId;
            }
            if ((65535 - previousMsgId) + ((MqttWireMessage) list.elementAt(0)).getMessageId() > largestGap) {
                largestGapMsgIdPosInList = 0;
            }
            for (long i2 = largestGapMsgIdPosInList; i2 < ((long) list.size()); i2 += MIN_MSG_ID) {
                newList.addElement(list.elementAt((int) i2));
            }
            for (i = 0; ((long) i) < largestGapMsgIdPosInList; i++) {
                newList.addElement(list.elementAt(i));
            }
        }
        return newList;
    }

    protected void restoreState() throws MqttException {
        Enumeration messageKeys = this.persistence.keys();
        long highestMsgId = this.nextMsgId.longValue();
        Vector orphanedPubRels = new Vector();
        this.trace.trace((byte) 1, IMediaPlayer.AD_INFO_VIP_NO_AD);
        while (messageKeys.hasMoreElements()) {
            String key = (String) messageKeys.nextElement();
            MqttWireMessage message = restoreMessage(key, this.persistence.get(key));
            if (message != null) {
                if (key.startsWith(PERSISTENCE_RECEIVED_PREFIX)) {
                    this.trace.trace((byte) 1, 604, new Object[]{key, message});
                    this.inboundQoS2.put(new Long(message.getMessageId()), message);
                } else if (key.startsWith(PERSISTENCE_SENT_PREFIX)) {
                    MqttPublish sendMessage = (MqttPublish) message;
                    highestMsgId = Math.max(sendMessage.getMessageId(), highestMsgId);
                    if (this.persistence.containsKey(getSendConfirmPersistenceKey(sendMessage))) {
                        MqttPubRel confirmMessage = (MqttPubRel) restoreMessage(key, this.persistence.get(getSendConfirmPersistenceKey(sendMessage)));
                        if (confirmMessage != null) {
                            this.trace.trace((byte) 1, 605, new Object[]{key, message});
                            this.outboundQoS2.put(new Long(confirmMessage.getMessageId()), confirmMessage);
                        } else {
                            this.trace.trace((byte) 1, 606, new Object[]{key, message});
                        }
                    } else if (sendMessage.getMessage().getQos() == 2) {
                        this.trace.trace((byte) 1, 607, new Object[]{key, message});
                        this.outboundQoS2.put(new Long(sendMessage.getMessageId()), sendMessage);
                    } else {
                        this.trace.trace((byte) 1, 608, new Object[]{key, message});
                        this.outboundQoS1.put(new Long(sendMessage.getMessageId()), sendMessage);
                    }
                    this.tokenStore.restoreToken(sendMessage);
                    this.inUseMsgIds.put(new Long(sendMessage.getMessageId()), new Long(sendMessage.getMessageId()));
                } else if (key.startsWith(PERSISTENCE_CONFIRMED_PREFIX)) {
                    if (!this.persistence.containsKey(getSendPersistenceKey((MqttPubRel) message))) {
                        orphanedPubRels.addElement(key);
                    }
                }
            }
        }
        messageKeys = orphanedPubRels.elements();
        while (messageKeys.hasMoreElements()) {
            key = (String) messageKeys.nextElement();
            this.trace.trace((byte) 1, 609, new Object[]{key});
            this.persistence.remove(key);
        }
        this.nextMsgId = Long.valueOf(highestMsgId);
    }

    private void restoreInflightMessages() {
        this.pendingMessages = new Vector(this.maxInflight);
        this.pendingFlows = new Vector();
        Enumeration keys = this.outboundQoS2.keys();
        while (keys.hasMoreElements()) {
            Object msg = this.outboundQoS2.get(keys.nextElement());
            if (msg instanceof MqttPublish) {
                this.trace.trace((byte) 1, 610, new Object[]{key});
                insertInOrder(this.pendingMessages, (MqttPublish) msg);
            } else if (msg instanceof MqttPubRel) {
                this.trace.trace((byte) 1, 611, new Object[]{key});
                insertInOrder(this.pendingFlows, (MqttPubRel) msg);
            }
        }
        keys = this.outboundQoS1.keys();
        while (keys.hasMoreElements()) {
            MqttPublish msg2 = (MqttPublish) this.outboundQoS1.get(keys.nextElement());
            this.trace.trace((byte) 1, 612, new Object[]{key});
            insertInOrder(this.pendingMessages, msg2);
        }
        this.pendingFlows = reOrder(this.pendingFlows);
        this.pendingMessages = reOrder(this.pendingMessages);
    }

    public MqttDeliveryTokenImpl send(MqttWireMessage message) throws MqttException {
        MqttDeliveryTokenImpl token = null;
        if (message instanceof MqttConnect) {
            this.sentConnect = false;
            this.connectFailed = false;
        }
        if (message.isMessageIdRequired() && message.getMessageId() == 0) {
            message.setMessageId(getNextMessageId());
        }
        if (message instanceof MqttPublish) {
            synchronized (this.queueLock) {
                if (this.quiescing) {
                    if (this.trace.isOn()) {
                        this.trace.trace((byte) 1, 613, new Object[]{message});
                    }
                    throw ExceptionHelper.createMqttException(32102);
                }
                MqttMessage innerMessage = ((MqttPublish) message).getMessage();
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 612, new Object[]{new Long(message.getMessageId()), new Integer(innerMessage.getQos()), message});
                }
                switch (innerMessage.getQos()) {
                    case 1:
                        this.outboundQoS1.put(new Long(message.getMessageId()), message);
                        this.persistence.put(getSendPersistenceKey(message), (MqttPublish) message);
                        break;
                    case 2:
                        this.outboundQoS2.put(new Long(message.getMessageId()), message);
                        this.persistence.put(getSendPersistenceKey(message), (MqttPublish) message);
                        break;
                }
                this.pendingMessages.addElement(message);
                token = this.tokenStore.saveToken(message);
                this.queueLock.notifyAll();
            }
        } else if (message instanceof MqttConnect) {
            synchronized (this.queueLock) {
                this.pendingFlows.insertElementAt(message, 0);
                token = this.tokenStore.saveToken(message);
                this.queueLock.notifyAll();
            }
        } else if (this.quiescing && ((message instanceof MqttSubscribe) || (message instanceof MqttUnsubscribe))) {
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 614, new Object[]{message});
            }
            throw ExceptionHelper.createMqttException(32102);
        } else {
            if (message instanceof MqttPingReq) {
                this.pingCommand = message;
            } else if (message instanceof MqttPubRel) {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 615, new Object[]{new Long(message.getMessageId())});
                }
                this.outboundQoS2.put(new Long(message.getMessageId()), message);
                this.persistence.put(getSendConfirmPersistenceKey(message), (MqttPubRel) message);
            } else if (message instanceof MqttPubComp) {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 616, new Object[]{new Long(message.getMessageId())});
                }
                this.persistence.remove(getReceivedPersistenceKey(message));
            }
            synchronized (this.queueLock) {
                this.pendingFlows.addElement(message);
                if (!(message instanceof MqttAck)) {
                    token = this.tokenStore.saveToken(message);
                }
                if (message instanceof MqttPubRel) {
                    this.inFlightPubRels++;
                    if (this.trace.isOn()) {
                        this.trace.trace((byte) 1, 617, new Object[]{new Integer(this.inFlightPubRels)});
                    }
                }
                this.queueLock.notifyAll();
            }
        }
        return token;
    }

    protected void undo(MqttPublish message) throws MqttPersistenceException {
        synchronized (this.queueLock) {
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 618, new Object[]{new Integer(message.getMessage().getQos()), new Long(message.getMessageId())});
            }
            if (message.getMessage().getQos() == 1) {
                this.outboundQoS1.remove(new Long(message.getMessageId()));
            } else {
                this.outboundQoS2.remove(new Long(message.getMessageId()));
            }
            this.pendingMessages.removeElement(message);
            this.persistence.remove(getSendPersistenceKey(message));
            this.tokenStore.removeToken(message);
        }
    }

    private MqttWireMessage checkForActivity() throws MqttException {
        if (System.currentTimeMillis() - this.lastOutboundActivity < this.keepAlive && System.currentTimeMillis() - this.lastInboundActivity < this.keepAlive) {
            return null;
        }
        if (this.pingOutstanding) {
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 619, new Object[]{new Long(this.keepAlive), new Long(this.lastOutboundActivity), new Long(this.lastInboundActivity)});
            }
            throw ExceptionHelper.createMqttException(32000);
        }
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 620, new Object[]{new Long(this.keepAlive), new Long(this.lastOutboundActivity), new Long(this.lastInboundActivity)});
        }
        this.pingOutstanding = true;
        MqttWireMessage result = this.pingCommand;
        this.tokenStore.saveToken(result);
        return result;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected com.push.mqttv3.internal.wire.MqttWireMessage get() throws com.push.mqttv3.MqttException {
        /*
        r16 = this;
        r6 = 0;
        r15 = 0;
        r14 = 1;
        r4 = 0;
        r0 = r16;
        r7 = r0.queueLock;
        monitor-enter(r7);
        r0 = r16;
        r5 = r0.sentConnect;	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x0040;
    L_0x000f:
        r0 = r16;
        r5 = r0.connectFailed;	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x0040;
    L_0x0015:
        r0 = r16;
        r5 = r0.connected;	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x0040;
    L_0x001b:
        r0 = r16;
        r5 = r0.trace;	 Catch:{ all -> 0x00ba }
        r8 = 1;
        r9 = 648; // 0x288 float:9.08E-43 double:3.2E-321;
        r5.trace(r8, r9);	 Catch:{ all -> 0x00ba }
        monitor-exit(r7);	 Catch:{ all -> 0x00ba }
        r5 = r6;
    L_0x0027:
        return r5;
    L_0x0028:
        r0 = r16;
        r5 = r0.pendingMessages;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x0097;
    L_0x0032:
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x0097;
    L_0x003c:
        r4 = r16.checkForActivity();	 Catch:{ all -> 0x00ba }
    L_0x0040:
        if (r4 != 0) goto L_0x0150;
    L_0x0042:
        r0 = r16;
        r5 = r0.pendingMessages;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x006b;
    L_0x004c:
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x006b;
    L_0x0056:
        r0 = r16;
        r5 = r0.trace;	 Catch:{ InterruptedException -> 0x0186 }
        r8 = 1;
        r9 = 644; // 0x284 float:9.02E-43 double:3.18E-321;
        r5.trace(r8, r9);	 Catch:{ InterruptedException -> 0x0186 }
        r0 = r16;
        r5 = r0.queueLock;	 Catch:{ InterruptedException -> 0x0186 }
        r0 = r16;
        r8 = r0.keepAlive;	 Catch:{ InterruptedException -> 0x0186 }
        r5.wait(r8);	 Catch:{ InterruptedException -> 0x0186 }
    L_0x006b:
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x0084;
    L_0x0075:
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r8 = 0;
        r5 = r5.elementAt(r8);	 Catch:{ all -> 0x00ba }
        r5 = (com.push.mqttv3.internal.wire.MqttWireMessage) r5;	 Catch:{ all -> 0x00ba }
        r5 = r5 instanceof com.push.mqttv3.internal.wire.MqttConnect;	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x0028;
    L_0x0084:
        r0 = r16;
        r5 = r0.connected;	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x0028;
    L_0x008a:
        r0 = r16;
        r5 = r0.trace;	 Catch:{ all -> 0x00ba }
        r8 = 1;
        r9 = 621; // 0x26d float:8.7E-43 double:3.07E-321;
        r5.trace(r8, r9);	 Catch:{ all -> 0x00ba }
        monitor-exit(r7);	 Catch:{ all -> 0x00ba }
        r5 = r6;
        goto L_0x0027;
    L_0x0097:
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x00bd;
    L_0x00a1:
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r8 = 0;
        r5 = r5.elementAt(r8);	 Catch:{ all -> 0x00ba }
        r0 = r5;
        r0 = (com.push.mqttv3.internal.wire.MqttWireMessage) r0;	 Catch:{ all -> 0x00ba }
        r4 = r0;
        r0 = r16;
        r5 = r0.pendingFlows;	 Catch:{ all -> 0x00ba }
        r8 = 0;
        r5.removeElementAt(r8);	 Catch:{ all -> 0x00ba }
        r16.checkQuiesceLock();	 Catch:{ all -> 0x00ba }
        goto L_0x0040;
    L_0x00ba:
        r5 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x00ba }
        throw r5;
    L_0x00bd:
        r0 = r16;
        r5 = r0.pendingMessages;	 Catch:{ all -> 0x00ba }
        r5 = r5.isEmpty();	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x0040;
    L_0x00c7:
        r0 = r16;
        r5 = r0.actualInFlight;	 Catch:{ all -> 0x00ba }
        r0 = r16;
        r8 = r0.maxInflight;	 Catch:{ all -> 0x00ba }
        if (r5 != r8) goto L_0x00fa;
    L_0x00d1:
        r0 = r16;
        r5 = r0.trace;	 Catch:{ all -> 0x00ba }
        r8 = 1;
        r9 = 622; // 0x26e float:8.72E-43 double:3.073E-321;
        r5.trace(r8, r9);	 Catch:{ all -> 0x00ba }
        r0 = r16;
        r5 = r0.queueLock;	 Catch:{ InterruptedException -> 0x0183 }
        r0 = r16;
        r8 = r0.keepAlive;	 Catch:{ InterruptedException -> 0x0183 }
        r5.wait(r8);	 Catch:{ InterruptedException -> 0x0183 }
    L_0x00e6:
        r0 = r16;
        r5 = r0.connected;	 Catch:{ all -> 0x00ba }
        if (r5 != 0) goto L_0x00fa;
    L_0x00ec:
        r0 = r16;
        r5 = r0.trace;	 Catch:{ all -> 0x00ba }
        r8 = 1;
        r9 = 647; // 0x287 float:9.07E-43 double:3.197E-321;
        r5.trace(r8, r9);	 Catch:{ all -> 0x00ba }
        monitor-exit(r7);	 Catch:{ all -> 0x00ba }
        r5 = r6;
        goto L_0x0027;
    L_0x00fa:
        r0 = r16;
        r5 = r0.actualInFlight;	 Catch:{ all -> 0x00ba }
        r0 = r16;
        r8 = r0.maxInflight;	 Catch:{ all -> 0x00ba }
        if (r5 >= r8) goto L_0x0040;
    L_0x0104:
        r0 = r16;
        r5 = r0.pendingMessages;	 Catch:{ all -> 0x00ba }
        r8 = 0;
        r5 = r5.elementAt(r8);	 Catch:{ all -> 0x00ba }
        r0 = r5;
        r0 = (com.push.mqttv3.internal.wire.MqttWireMessage) r0;	 Catch:{ all -> 0x00ba }
        r4 = r0;
        r0 = r16;
        r5 = r0.pendingMessages;	 Catch:{ all -> 0x00ba }
        r8 = 0;
        r5.removeElementAt(r8);	 Catch:{ all -> 0x00ba }
        if (r4 != 0) goto L_0x0121;
    L_0x011b:
        r4 = r16.checkForActivity();	 Catch:{ all -> 0x00ba }
        goto L_0x0040;
    L_0x0121:
        r0 = r16;
        r5 = r0.actualInFlight;	 Catch:{ all -> 0x00ba }
        r5 = r5 + 1;
        r0 = r16;
        r0.actualInFlight = r5;	 Catch:{ all -> 0x00ba }
        r0 = r16;
        r5 = r0.trace;	 Catch:{ all -> 0x00ba }
        r5 = r5.isOn();	 Catch:{ all -> 0x00ba }
        if (r5 == 0) goto L_0x0040;
    L_0x0135:
        r0 = r16;
        r5 = r0.trace;	 Catch:{ all -> 0x00ba }
        r8 = 1;
        r9 = 623; // 0x26f float:8.73E-43 double:3.08E-321;
        r10 = 1;
        r10 = new java.lang.Object[r10];	 Catch:{ all -> 0x00ba }
        r11 = 0;
        r12 = new java.lang.Integer;	 Catch:{ all -> 0x00ba }
        r0 = r16;
        r13 = r0.actualInFlight;	 Catch:{ all -> 0x00ba }
        r12.<init>(r13);	 Catch:{ all -> 0x00ba }
        r10[r11] = r12;	 Catch:{ all -> 0x00ba }
        r5.trace(r8, r9, r10);	 Catch:{ all -> 0x00ba }
        goto L_0x0040;
    L_0x0150:
        monitor-exit(r7);	 Catch:{ all -> 0x00ba }
        r0 = r16;
        r5 = r0.trace;
        r5 = r5.isOn();
        if (r5 == 0) goto L_0x0178;
    L_0x015b:
        r2 = 0;
        if (r4 == 0) goto L_0x0163;
    L_0x015f:
        r2 = r4.getMessageId();
    L_0x0163:
        r0 = r16;
        r5 = r0.trace;
        r6 = 624; // 0x270 float:8.74E-43 double:3.083E-321;
        r7 = 2;
        r7 = new java.lang.Object[r7];
        r7[r15] = r4;
        r8 = new java.lang.Long;
        r8.<init>(r2);
        r7[r14] = r8;
        r5.trace(r14, r6, r7);
    L_0x0178:
        r5 = r4 instanceof com.push.mqttv3.internal.wire.MqttConnect;
        if (r5 == 0) goto L_0x0180;
    L_0x017c:
        r0 = r16;
        r0.sentConnect = r14;
    L_0x0180:
        r5 = r4;
        goto L_0x0027;
    L_0x0183:
        r5 = move-exception;
        goto L_0x00e6;
    L_0x0186:
        r5 = move-exception;
        goto L_0x006b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.push.mqttv3.internal.ClientState.get():com.push.mqttv3.internal.wire.MqttWireMessage");
    }

    public void setKeepAliveInterval(long interval) {
        this.keepAlive = interval;
    }

    protected void notifySent(MqttWireMessage message) {
        this.lastOutboundActivity = System.currentTimeMillis();
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 625, new Object[]{message});
        }
        MqttDeliveryTokenImpl token = this.tokenStore.getToken(message);
        token.notifySent();
        if ((message instanceof MqttPublish) && ((MqttPublish) message).getMessage().getQos() == 0) {
            token.notifyReceived(null);
            this.tokenStore.removeToken(message);
            this.callback.deliveryComplete(token);
            decrementInFlight();
            releaseMessageId(Long.valueOf(message.getMessageId()));
        }
        if (message instanceof MqttDisconnect) {
            this.tokenStore.removeToken(message);
        }
    }

    private void decrementInFlight() {
        synchronized (this.queueLock) {
            this.actualInFlight--;
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 646, new Object[]{new Integer(this.actualInFlight)});
            }
            if (!checkQuiesceLock()) {
                this.queueLock.notifyAll();
            }
        }
    }

    private boolean checkQuiesceLock() {
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 626, new Object[]{new Boolean(this.quiescing), new Integer(this.actualInFlight), new Integer(this.pendingFlows.size()), new Integer(this.inFlightPubRels)});
        }
        if (!this.quiescing || this.actualInFlight != 0 || this.pendingFlows.size() != 0 || this.inFlightPubRels != 0) {
            return false;
        }
        synchronized (this.quiesceLock) {
            this.quiesceLock.notifyAll();
        }
        return true;
    }

    protected void notifyReceived(MqttWireMessage message) throws MqttException {
        this.lastInboundActivity = System.currentTimeMillis();
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 627, new Object[]{message, new Long(message.getMessageId())});
        }
        if (message instanceof MqttAck) {
            MqttAck ack = (MqttAck) message;
            MqttDeliveryTokenImpl token = this.tokenStore.getToken(message);
            if ((ack instanceof MqttPubRec) && this.outboundQoS2.containsKey(new Long(ack.getMessageId()))) {
                send(new MqttPubRel((MqttPubRec) ack));
                return;
            }
            if (ack instanceof MqttPubAck) {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 628, new Object[]{new Long(ack.getMessageId())});
                }
                this.persistence.remove(getSendPersistenceKey(message));
                this.outboundQoS1.remove(new Long(ack.getMessageId()));
            } else if (ack instanceof MqttPubComp) {
                this.outboundQoS2.remove(new Long(ack.getMessageId()));
                this.persistence.remove(getSendPersistenceKey(message));
                this.persistence.remove(getSendConfirmPersistenceKey(message));
                this.inFlightPubRels--;
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 645, new Object[]{new Long(ack.getMessageId()), new Integer(this.inFlightPubRels)});
                }
            }
            releaseMessageId(Long.valueOf(message.getMessageId()));
            if ((ack instanceof MqttPubAck) || (ack instanceof MqttPubRec) || (ack instanceof MqttPubComp)) {
                decrementInFlight();
            }
            if (ack instanceof MqttPingResp) {
                this.trace.trace((byte) 1, 629);
                this.pingOutstanding = false;
            } else if (message instanceof MqttConnack) {
                if (((MqttConnack) message).getReturnCode() == 0) {
                    if (this.cleanSession) {
                        clearState();
                    }
                    this.inFlightPubRels = 0;
                    this.actualInFlight = 0;
                    restoreInflightMessages();
                    connected();
                } else {
                    this.connectFailed = true;
                }
                synchronized (this.queueLock) {
                    this.queueLock.notifyAll();
                }
            }
            this.tokenStore.responseReceived((MqttAck) message);
            if ((ack instanceof MqttPubAck) || (ack instanceof MqttPubComp)) {
                this.callback.deliveryComplete(token);
            }
            checkQuiesceLock();
        } else if (!this.quiescing) {
            if (message instanceof MqttPublish) {
                MqttPublish send = (MqttPublish) message;
                switch (send.getMessage().getQos()) {
                    case 0:
                    case 1:
                        if (this.callback != null) {
                            this.callback.messageArrived(send);
                            return;
                        }
                        return;
                    case 2:
                        if (this.trace.isOn()) {
                            this.trace.trace((byte) 1, 630, new Object[]{new Long(send.getMessageId())});
                        }
                        this.persistence.put(getReceivedPersistenceKey(message), (MqttPublish) message);
                        this.inboundQoS2.put(new Long(send.getMessageId()), send);
                        send(new MqttPubRec(send));
                        return;
                    default:
                        return;
                }
            } else if (message instanceof MqttPubRel) {
                MqttPublish sendMsg = (MqttPublish) this.inboundQoS2.get(new Long(message.getMessageId()));
                if (sendMsg == null) {
                    send(new MqttPubComp(Long.valueOf(message.getMessageId())));
                } else if (this.callback != null) {
                    this.callback.messageArrived(sendMsg);
                }
            }
        }
    }

    public void connected() {
        this.trace.trace((byte) 1, 631);
        this.connected = true;
    }

    public void disconnecting(MqttException reason) {
        this.trace.trace((byte) 1, 632, null, reason);
        synchronized (this.queueLock) {
            this.queueLock.notifyAll();
        }
        this.tokenStore.noMoreResponses(reason);
    }

    public void disconnected(MqttException reason) {
        this.trace.trace((byte) 1, 633, null, reason);
        this.connected = false;
        synchronized (this.queueLock) {
            this.queueLock.notifyAll();
        }
        try {
            if (this.cleanSession) {
                clearState();
            }
            this.pendingMessages.clear();
            this.pendingFlows.clear();
            synchronized (this.waitingTokensLock) {
                if (this.trace.isOn()) {
                    this.trace.trace((byte) 1, 634, new Object[]{new Integer(this.waitingTokens)});
                }
                while (this.waitingTokens > 0) {
                    try {
                        this.waitingTokensLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            this.trace.trace((byte) 1, 635);
            this.persistence.close();
        } catch (MqttException e2) {
        }
    }

    private synchronized void releaseMessageId(Long msgId) {
        this.inUseMsgIds.remove(new Long(msgId.longValue()));
    }

    private synchronized long getNextMessageId() throws MqttException {
        long startingMessageId = this.nextMsgId.longValue();
        int loopCount = 0;
        do {
            Long l = this.nextMsgId;
            this.nextMsgId = Long.valueOf(this.nextMsgId.longValue() + MIN_MSG_ID);
            if (this.nextMsgId.longValue() > 65535) {
                this.nextMsgId = Long.valueOf(MIN_MSG_ID);
            }
            if (this.nextMsgId.longValue() == startingMessageId) {
                loopCount++;
                if (loopCount == 2) {
                    throw ExceptionHelper.createMqttException(32001);
                }
            }
        } while (this.inUseMsgIds.containsKey(new Long(this.nextMsgId.longValue())));
        Long id = new Long(this.nextMsgId.longValue());
        this.inUseMsgIds.put(id, id);
        return this.nextMsgId.longValue();
    }

    private void cleanUpQueue(Vector queue) {
        this.trace.trace((byte) 1, 636);
        Enumeration e = queue.elements();
        MqttException ex = ExceptionHelper.createMqttException(32102);
        while (e.hasMoreElements()) {
            MqttWireMessage message = (MqttWireMessage) e.nextElement();
            MqttDeliveryTokenImpl token = this.tokenStore.getToken(message);
            Long messageId = new Long(message.getMessageId());
            if (this.outboundQoS2.containsKey(messageId)) {
                this.outboundQoS2.remove(messageId);
            }
            if (token != null) {
                token.notifyException(ex);
                this.tokenStore.removeToken(message);
            }
            queue.removeElement(message);
        }
    }

    public void quiesce(long timeout) {
        this.trace.trace((byte) 1, 637, new Object[]{new Long(timeout)});
        if (timeout > 0) {
            synchronized (this.queueLock) {
                this.quiescing = true;
            }
            this.callback.quiesce();
            synchronized (this.queueLock) {
                this.trace.trace((byte) 1, 638);
                this.queueLock.notifyAll();
            }
            synchronized (this.quiesceLock) {
                try {
                    if (this.actualInFlight > 0 || this.pendingFlows.size() > 0 || this.inFlightPubRels > 0) {
                        if (this.trace.isOn()) {
                            this.trace.trace((byte) 1, 639, new Object[]{new Integer(this.actualInFlight), new Integer(this.pendingFlows.size()), new Integer(this.inFlightPubRels)});
                        }
                        this.quiesceLock.wait(timeout);
                        this.trace.trace((byte) 1, 640);
                    }
                } catch (InterruptedException e) {
                }
            }
            synchronized (this.queueLock) {
                cleanUpQueue(this.pendingMessages);
                cleanUpQueue(this.pendingFlows);
                this.quiescing = false;
                this.actualInFlight = 0;
            }
        }
    }

    protected void deliveryComplete(MqttPublish message) throws MqttPersistenceException {
        if (this.trace.isOn()) {
            this.trace.trace((byte) 1, 641, new Object[]{new Long(message.getMessageId())});
        }
        this.persistence.remove(getReceivedPersistenceKey(message));
        this.inboundQoS2.remove(new Long(message.getMessageId()));
    }

    protected void incrementWaitingTokens() {
        synchronized (this.waitingTokensLock) {
            this.waitingTokens++;
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 642, new Object[]{new Integer(this.waitingTokens)});
            }
        }
    }

    protected void decrementWaitingTokens() {
        synchronized (this.waitingTokensLock) {
            this.waitingTokens--;
            if (this.trace.isOn()) {
                this.trace.trace((byte) 1, 643, new Object[]{new Integer(this.waitingTokens)});
            }
            if (this.waitingTokens == 0) {
                this.waitingTokensLock.notifyAll();
            }
        }
    }
}
