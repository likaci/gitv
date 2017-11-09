package com.push.mqttv3;

import java.util.Properties;
import javax.net.SocketFactory;

public class MqttConnectOptions {
    private boolean cleanSession = true;
    private int connectionTimeout = 30;
    private int keepAliveInterval = 240;
    private char[] password;
    private SocketFactory socketFactory;
    private Properties sslClientProps = null;
    private String userName;
    private MqttTopic willDestination = null;
    private MqttMessage willMessage = null;

    public char[] getPassword() {
        return this.password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setWill(MqttTopic topic, byte[] payload, int qos, boolean retained) {
        validateWill(topic, payload);
        setWill(topic, new MqttMessage(payload), qos, retained);
    }

    private void validateWill(MqttTopic dest, Object payload) {
        if (dest == null || payload == null) {
            throw new IllegalArgumentException();
        }
    }

    private void setWill(MqttTopic topic, MqttMessage msg, int qos, boolean retained) {
        this.willDestination = topic;
        this.willMessage = msg;
        this.willMessage.setQos(qos);
        this.willMessage.setRetained(retained);
        this.willMessage.setMutable(false);
    }

    public int getKeepAliveInterval() {
        return this.keepAliveInterval;
    }

    public void setKeepAliveInterval(int keepAliveInterval) {
        if (keepAliveInterval < 10) {
            throw new IllegalArgumentException();
        }
        this.keepAliveInterval = keepAliveInterval;
    }

    public int getConnectionTimeout() {
        return this.connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        if (connectionTimeout < 10) {
            throw new IllegalArgumentException();
        }
        this.connectionTimeout = connectionTimeout;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public void setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
    }

    public MqttTopic getWillDestination() {
        return this.willDestination;
    }

    public MqttMessage getWillMessage() {
        return this.willMessage;
    }

    public Properties getSSLProperties() {
        return this.sslClientProps;
    }

    public void setSSLProperties(Properties props) {
        this.sslClientProps = props;
    }

    public boolean isCleanSession() {
        return this.cleanSession;
    }

    public void setCleanSession(boolean cleanSession) {
        this.cleanSession = cleanSession;
    }
}
