package com.push.mqttv3;

import com.push.mqttv3.internal.ClientComms;
import com.push.mqttv3.internal.DestinationProvider;
import com.push.mqttv3.internal.ExceptionHelper;
import com.push.mqttv3.internal.LocalNetworkModule;
import com.push.mqttv3.internal.MemoryPersistence;
import com.push.mqttv3.internal.NetworkModule;
import com.push.mqttv3.internal.SSLNetworkModule;
import com.push.mqttv3.internal.TCPNetworkModule;
import com.push.mqttv3.internal.comms.MqttDirectException;
import com.push.mqttv3.internal.security.SSLSocketFactoryFactory;
import com.push.mqttv3.internal.trace.Trace;
import com.push.mqttv3.internal.wire.MqttConnect;
import com.push.mqttv3.internal.wire.MqttDisconnect;
import com.push.mqttv3.internal.wire.MqttSubscribe;
import com.push.mqttv3.internal.wire.MqttUnsubscribe;
import java.util.Hashtable;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.cybergarage.soap.SOAP;

public class MqttClient implements DestinationProvider {
    private static final int URI_TYPE_LOCAL = 2;
    private static final int URI_TYPE_SSL = 1;
    private static final int URI_TYPE_TCP = 0;
    private String clientId;
    private ClientComms comms;
    private MqttClientPersistence persistence;
    private String serverURI;
    private int serverURIType;
    private Hashtable topics;
    private Trace trace;

    public MqttClient(String serverURI, String clientId) throws MqttException {
        this(serverURI, clientId, new MqttDefaultFilePersistence());
    }

    public MqttClient(String serverURI, String clientId, MqttClientPersistence persistence) throws MqttException {
        if (clientId == null || clientId.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.trace = Trace.getTrace(clientId);
        this.serverURI = serverURI;
        this.serverURIType = validateURI(serverURI);
        this.clientId = clientId;
        this.persistence = persistence;
        if (this.persistence == null) {
            this.persistence = new MemoryPersistence();
        }
        this.trace.trace((byte) 1, 101, new Object[]{clientId, serverURI, persistence});
        this.persistence.open(clientId, serverURI);
        this.comms = new ClientComms(this, this.persistence, this.trace);
        this.persistence.close();
        this.topics = new Hashtable();
    }

    private int validateURI(String serverURI) {
        if (serverURI.startsWith("tcp://")) {
            return 0;
        }
        if (serverURI.startsWith("ssl://")) {
            return 1;
        }
        if (serverURI.startsWith("local://")) {
            return 2;
        }
        throw new IllegalArgumentException();
    }

    protected NetworkModule createNetworkModule(String address, MqttConnectOptions options) throws MqttException {
        MqttDirectException ex;
        SocketFactory factory = options.getSocketFactory();
        String shortAddress;
        String host;
        int port;
        switch (this.serverURIType) {
            case 0:
                shortAddress = address.substring(6);
                host = getHostName(shortAddress);
                port = getPort(shortAddress, 1883);
                if (factory == null || (factory instanceof SSLSocketFactory)) {
                    factory = SocketFactory.getDefault();
                    options.setSocketFactory(factory);
                } else if (factory instanceof SSLSocketFactory) {
                    throw ExceptionHelper.createMqttException(32105);
                }
                return new TCPNetworkModule(this.trace, factory, host, port);
            case 1:
                shortAddress = address.substring(6);
                host = getHostName(shortAddress);
                port = getPort(shortAddress, 8883);
                SSLSocketFactoryFactory factoryFactory = null;
                if (factory == null || !(factory instanceof SSLSocketFactory)) {
                    try {
                        SSLSocketFactoryFactory factoryFactory2 = new SSLSocketFactoryFactory();
                        try {
                            Properties sslClientProps = options.getSSLProperties();
                            if (sslClientProps != null) {
                                factoryFactory2.initialize(sslClientProps, null);
                            }
                            factory = factoryFactory2.createSocketFactory(null);
                            factoryFactory = factoryFactory2;
                        } catch (MqttDirectException e) {
                            ex = e;
                            factoryFactory = factoryFactory2;
                            throw ExceptionHelper.createMqttException(ex.getCause());
                        }
                    } catch (MqttDirectException e2) {
                        ex = e2;
                        throw ExceptionHelper.createMqttException(ex.getCause());
                    }
                } else if (!(factory instanceof SSLSocketFactory)) {
                    throw ExceptionHelper.createMqttException(32105);
                }
                NetworkModule netModule = new SSLNetworkModule(this.trace, (SSLSocketFactory) factory, host, port);
                ((SSLNetworkModule) netModule).setSSLhandshakeTimeout(options.getConnectionTimeout());
                if (factoryFactory == null) {
                    return netModule;
                }
                String[] enabledCiphers = factoryFactory.getEnabledCipherSuites(null);
                if (enabledCiphers == null) {
                    return netModule;
                }
                ((SSLNetworkModule) netModule).setEnabledCiphers(enabledCiphers);
                return netModule;
            case 2:
                return new LocalNetworkModule(address.substring(8));
            default:
                return null;
        }
    }

    private int getPort(String uri, int defaultPort) {
        int portIndex = uri.lastIndexOf(58);
        if (portIndex == -1) {
            return defaultPort;
        }
        return Integer.valueOf(uri.substring(portIndex + 1)).intValue();
    }

    private String getHostName(String uri) {
        int schemeIndex = uri.lastIndexOf(47);
        int portIndex = uri.lastIndexOf(58);
        if (portIndex == -1) {
            portIndex = uri.length();
        }
        return uri.substring(schemeIndex + 1, portIndex);
    }

    public void connect() throws MqttSecurityException, MqttException {
        connect(new MqttConnectOptions());
    }

    public void connect(MqttConnectOptions options) throws MqttSecurityException, MqttException {
        if (isConnected()) {
            throw ExceptionHelper.createMqttException(32100);
        }
        if (this.trace.isOn()) {
            String str;
            Trace trace = this.trace;
            Object[] objArr = new Object[6];
            objArr[0] = new Boolean(options.isCleanSession());
            objArr[1] = new Integer(options.getConnectionTimeout());
            objArr[2] = new Integer(options.getKeepAliveInterval());
            objArr[3] = options.getUserName();
            objArr[4] = options.getPassword() == null ? "[null]" : "[notnull]";
            if (options.getWillMessage() == null) {
                str = "[null]";
            } else {
                str = "[notnull]";
            }
            objArr[5] = str;
            trace.trace((byte) 1, 103, objArr);
        }
        this.comms.setNetworkModule(createNetworkModule(this.serverURI, options));
        this.persistence.open(this.clientId, this.serverURI);
        if (options.isCleanSession()) {
            this.persistence.clear();
        }
        this.comms.connect(new MqttConnect(this.clientId, options.isCleanSession(), options.getKeepAliveInterval(), options.getUserName(), options.getPassword(), options.getWillMessage(), options.getWillDestination()), options.getConnectionTimeout(), (long) options.getKeepAliveInterval(), options.isCleanSession());
    }

    public void disconnect() throws MqttException {
        disconnect(30000);
    }

    public void disconnect(long quiesceTimeout) throws MqttException {
        this.trace.trace((byte) 1, 104, new Object[]{new Long(quiesceTimeout)});
        try {
            this.comms.disconnect(new MqttDisconnect(), quiesceTimeout);
        } catch (MqttException ex) {
            this.trace.trace((byte) 1, 105, null, ex);
            throw ex;
        }
    }

    public boolean isConnected() {
        return this.comms.isConnected();
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getServerURI() {
        return this.serverURI;
    }

    public MqttTopic getTopic(String topic) {
        if (topic.indexOf(35) == -1 && topic.indexOf(43) == -1) {
            MqttTopic result = (MqttTopic) this.topics.get(topic);
            if (result != null) {
                return result;
            }
            result = new MqttTopic(topic, this.comms);
            this.topics.put(topic, result);
            return result;
        }
        throw new IllegalArgumentException();
    }

    public void subscribe(String topicFilter) throws MqttException, MqttSecurityException {
        subscribe(new String[]{topicFilter}, new int[]{1});
    }

    public void subscribe(String[] topicFilters) throws MqttException, MqttSecurityException {
        int[] qos = new int[topicFilters.length];
        for (int i = 0; i < qos.length; i++) {
            qos[i] = 1;
        }
        subscribe(topicFilters, qos);
    }

    public void subscribe(String topicFilter, int qos) throws MqttException, MqttSecurityException {
        subscribe(new String[]{topicFilter}, new int[]{qos});
    }

    public void subscribe(String[] topicFilters, int[] qos) throws MqttException, MqttSecurityException {
        if (topicFilters.length != qos.length) {
            throw new IllegalArgumentException();
        }
        if (this.trace.isOn()) {
            String subs = "";
            for (int i = 0; i < topicFilters.length; i++) {
                if (i > 0) {
                    subs = subs + ", ";
                }
                subs = subs + topicFilters[i] + SOAP.DELIM + qos[i];
            }
            this.trace.trace((byte) 1, 106, new Object[]{subs});
        }
        this.comms.sendAndWait(new MqttSubscribe(topicFilters, qos));
    }

    public void unsubscribe(String topicFilter) throws MqttException {
        unsubscribe(new String[]{topicFilter});
    }

    public void unsubscribe(String[] topicFilters) throws MqttException {
        if (this.trace.isOn()) {
            String subs = "";
            for (int i = 0; i < topicFilters.length; i++) {
                if (i > 0) {
                    subs = subs + ", ";
                }
                subs = subs + topicFilters[i];
            }
            this.trace.trace((byte) 1, 107, new Object[]{subs});
        }
        this.comms.sendAndWait(new MqttUnsubscribe(topicFilters));
    }

    public void setCallback(MqttCallback callback) throws MqttException {
        this.comms.setCallback(callback);
    }

    public static String generateClientId() {
        return System.getProperty("user.name") + "." + System.currentTimeMillis();
    }

    public MqttDeliveryToken[] getPendingDeliveryTokens() {
        return this.comms.getPendingDeliveryTokens();
    }
}
