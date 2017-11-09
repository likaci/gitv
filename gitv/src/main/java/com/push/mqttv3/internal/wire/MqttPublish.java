package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MqttPublish extends MqttPersistableWireMessage {
    public static final byte DESTINATION_TYPE_TOPIC = (byte) 0;
    private byte[] encodedPayload;
    private MqttReceivedMessage message;
    private String topicName;

    public MqttPublish(String name, MqttReceivedMessage message) {
        super((byte) 3);
        this.encodedPayload = null;
        this.topicName = name;
        this.message = message;
    }

    public MqttPublish(byte info, byte[] data) throws MqttException, IOException {
        super((byte) 3);
        this.encodedPayload = null;
        this.message = new MqttReceivedMessage();
        this.message.setQos((info >> 1) & 3);
        if ((info & 1) == 1) {
            this.message.setRetained(true);
        }
        if ((info & 8) == 8) {
            this.message.setDuplicate(true);
        }
        CountingInputStream counter = new CountingInputStream(new ByteArrayInputStream(data));
        DataInputStream dis = new DataInputStream(counter);
        this.topicName = dis.readUTF();
        if (this.message.getQos() > 0) {
            this.msgId = dis.readLong();
            this.message.setMessageId(this.msgId);
        }
        dis.close();
        byte[] payload = new byte[(data.length - counter.getCounter())];
        dis.readFully(payload);
        this.message.setPayload(payload);
    }

    protected byte getMessageInfo() {
        byte info = (byte) (this.message.getQos() << 1);
        if (this.message.isRetained()) {
            info = (byte) (info | 1);
        }
        if (this.message.isDuplicate()) {
            return (byte) (info | 8);
        }
        return info;
    }

    public String getTopicName() {
        return this.topicName;
    }

    public MqttReceivedMessage getMessage() {
        return this.message;
    }

    protected static byte[] encodePayload(MqttMessage message) throws MqttException {
        return message.getPayload();
    }

    public byte[] getPayload() throws MqttException {
        if (this.encodedPayload == null) {
            this.encodedPayload = encodePayload(this.message);
        }
        return this.encodedPayload;
    }

    public int getPayloadLength() {
        int length = 0;
        try {
            return getPayload().length;
        } catch (MqttException e) {
            return length;
        }
    }

    public void setMessageId(int msgId) {
        super.setMessageId((long) msgId);
        if (this.message instanceof MqttReceivedMessage) {
            this.message.setMessageId((long) msgId);
        }
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(this.topicName);
            if (this.message.getQos() > 0) {
                dos.writeLong(this.msgId);
            }
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    public boolean isMessageIdRequired() {
        return true;
    }
}
