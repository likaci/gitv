package com.push.mqttv3.internal.wire;

import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttMessage;
import com.push.mqttv3.MqttTopic;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class MqttConnect extends MqttWireMessage {
    private boolean cleanSession;
    private String clientId;
    private int keepAliveInterval;
    private char[] password;
    private String userName;
    private MqttTopic willDestination;
    private MqttMessage willMessage;

    public MqttConnect(String clientId, boolean cleanSession, int keepAliveInterval, String userName, char[] password, MqttMessage willMessage, MqttTopic willDestination) {
        super((byte) 1);
        this.clientId = clientId;
        this.cleanSession = cleanSession;
        this.keepAliveInterval = keepAliveInterval;
        this.userName = userName;
        this.password = password;
        this.willMessage = willMessage;
        this.willDestination = willDestination;
    }

    protected byte getMessageInfo() {
        return (byte) 0;
    }

    public boolean isCleanSession() {
        return this.cleanSession;
    }

    protected byte[] getVariableHeader() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF("MQIsdp");
            dos.write(3);
            byte connectFlags = (byte) 0;
            if (this.cleanSession) {
                connectFlags = (byte) 2;
            }
            if (this.willMessage != null) {
                connectFlags = (byte) ((this.willMessage.getQos() << 3) | ((byte) (connectFlags | 4)));
                if (this.willMessage.isRetained()) {
                    connectFlags = (byte) (connectFlags | 32);
                }
            }
            if (this.userName != null) {
                connectFlags = (byte) (connectFlags | 128);
                if (this.password != null) {
                    connectFlags = (byte) (connectFlags | 64);
                }
            }
            dos.write(connectFlags);
            dos.writeShort(this.keepAliveInterval);
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ioe) {
            throw new MqttException(ioe);
        }
    }

    public byte[] getPayload() throws MqttException {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeUTF(this.clientId);
            if (this.willMessage != null) {
                dos.writeUTF(this.willDestination.getName());
                dos.writeShort(this.willMessage.getPayload().length);
                dos.write(this.willMessage.getPayload());
            }
            if (this.userName != null) {
                encodeUTF8(dos, this.userName);
                if (this.password != null) {
                    dos.writeUTF(new String(this.password));
                }
            }
            dos.flush();
            return baos.toByteArray();
        } catch (Throwable ex) {
            throw new MqttException(ex);
        }
    }

    protected void encodeUTF8(DataOutputStream dos, String stringToEncode) throws MqttException {
        try {
            byte[] encodedString = stringToEncode.getBytes("UTF-8");
            byte byte2 = (byte) ((encodedString.length >>> 0) & 255);
            dos.write((byte) ((encodedString.length >>> 8) & 255));
            dos.write(byte2);
            dos.write(encodedString);
        } catch (Throwable ex) {
            throw new MqttException(ex);
        } catch (Throwable ex2) {
            throw new MqttException(ex2);
        }
    }

    public boolean isMessageIdRequired() {
        return false;
    }
}
