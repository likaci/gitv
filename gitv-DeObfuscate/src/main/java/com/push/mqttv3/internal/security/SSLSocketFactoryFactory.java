package com.push.mqttv3.internal.security;

import com.push.mqttv3.internal.comms.MqttDirectException;
import com.push.mqttv3.internal.comms.MqttSSLInitException;
import com.push.mqttv3.internal.logging.Logger;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class SSLSocketFactoryFactory {
    public static final String CIPHERSUITES = "com.ibm.ssl.enabledCipherSuites";
    private static final String CLASS_NAME = "SSLSocketFactoryFactory";
    public static final String CLIENTAUTH = "com.ibm.ssl.clientAuthentication";
    public static final String DEFAULT_PROTOCOL = "TLS";
    public static final String JSSEPROVIDER = "com.ibm.ssl.contextProvider";
    public static final String KEYSTORE = "com.ibm.ssl.keyStore";
    public static final String KEYSTOREMGR = "com.ibm.ssl.keyManager";
    public static final String KEYSTOREPROVIDER = "com.ibm.ssl.keyStoreProvider";
    public static final String KEYSTOREPWD = "com.ibm.ssl.keyStorePassword";
    public static final String KEYSTORETYPE = "com.ibm.ssl.keyStoreType";
    public static final String SSLPROTOCOL = "com.ibm.ssl.protocol";
    public static final String SYSKEYMGRALGO = "ssl.KeyManagerFactory.algorithm";
    public static final String SYSKEYSTORE = "javax.net.ssl.keyStore";
    public static final String SYSKEYSTOREPWD = "javax.net.ssl.keyStorePassword";
    public static final String SYSKEYSTORETYPE = "javax.net.ssl.keyStoreType";
    public static final String SYSTRUSTMGRALGO = "ssl.TrustManagerFactory.algorithm";
    public static final String SYSTRUSTSTORE = "javax.net.ssl.trustStore";
    public static final String SYSTRUSTSTOREPWD = "javax.net.ssl.trustStorePassword";
    public static final String SYSTRUSTSTORETYPE = "javax.net.ssl.trustStoreType";
    public static final String TRUSTSTORE = "com.ibm.ssl.trustStore";
    public static final String TRUSTSTOREMGR = "com.ibm.ssl.trustManager";
    public static final String TRUSTSTOREPROVIDER = "com.ibm.ssl.trustStoreProvider";
    public static final String TRUSTSTOREPWD = "com.ibm.ssl.trustStorePassword";
    public static final String TRUSTSTORETYPE = "com.ibm.ssl.trustStoreType";
    private static final byte[] key = new byte[]{(byte) -99, (byte) -89, (byte) -39, Byte.MIN_VALUE, (byte) 5, (byte) -72, (byte) -119, (byte) -100};
    private static final String[] propertyKeys = new String[]{SSLPROTOCOL, JSSEPROVIDER, KEYSTORE, KEYSTOREPWD, KEYSTORETYPE, KEYSTOREPROVIDER, KEYSTOREMGR, TRUSTSTORE, TRUSTSTOREPWD, TRUSTSTORETYPE, TRUSTSTOREPROVIDER, TRUSTSTOREMGR, CIPHERSUITES, CLIENTAUTH};
    private static final String xorTag = "{xor}";
    private Hashtable configs;
    private Properties defaultProperties;
    private Logger logger;

    public static boolean isSupportedOnJVM() throws LinkageError, ExceptionInInitializerError {
        try {
            Class.forName("javax.net.ssl.SSLServerSocketFactory");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public SSLSocketFactoryFactory() {
        this.logger = null;
        this.configs = new Hashtable();
    }

    public SSLSocketFactoryFactory(Logger logger) {
        this();
        this.logger = logger;
    }

    private boolean keyValid(String key) {
        int i = 0;
        while (i < propertyKeys.length && !propertyKeys[i].equals(key)) {
            i++;
        }
        return i < propertyKeys.length;
    }

    private void checkPropertyKeys(Properties properties) throws IllegalArgumentException {
        for (String k : properties.keySet()) {
            if (!keyValid(k)) {
                throw new IllegalArgumentException(k + " is not a valid IBM SSL property key.");
            }
        }
    }

    public static char[] toChar(byte[] b) {
        if (b == null) {
            return null;
        }
        char[] c = new char[(b.length / 2)];
        int i = 0;
        int j = 0;
        while (i < b.length) {
            int j2 = j + 1;
            int i2 = i + 1;
            i = i2 + 1;
            c[j] = (char) ((b[i] & 255) + ((b[i2] & 255) << 8));
            j = j2;
        }
        return c;
    }

    public static byte[] toByte(char[] c) {
        if (c == null) {
            return null;
        }
        byte[] b = new byte[(c.length * 2)];
        int i = 0;
        int j = 0;
        while (j < c.length) {
            int i2 = i + 1;
            b[i] = (byte) (c[j] & 255);
            i = i2 + 1;
            int j2 = j + 1;
            b[i2] = (byte) ((c[j] >> 8) & 255);
            j = j2;
        }
        return b;
    }

    public static String obfuscate(char[] password) {
        if (password == null) {
            return null;
        }
        byte[] bytes = toByte(password);
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) ((bytes[i] ^ key[i % key.length]) & 255);
        }
        return xorTag + new String(SimpleBase64Encoder.encode(bytes));
    }

    public static char[] deObfuscate(String ePassword) {
        if (ePassword == null) {
            return null;
        }
        try {
            byte[] bytes = SimpleBase64Encoder.decode(ePassword.substring(xorTag.length()));
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) ((bytes[i] ^ key[i % key.length]) & 255);
            }
            return toChar(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    public static String packCipherSuites(String[] ciphers) {
        if (ciphers == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < ciphers.length; i++) {
            buf.append(ciphers[i]);
            if (i < ciphers.length - 1) {
                buf.append(',');
            }
        }
        return buf.toString();
    }

    public static String[] unpackCipherSuites(String ciphers) {
        if (ciphers == null) {
            return null;
        }
        Vector c = new Vector();
        int i = ciphers.indexOf(44);
        int j = 0;
        while (i > -1) {
            c.add(ciphers.substring(j, i));
            j = i + 1;
            i = ciphers.indexOf(44, j);
        }
        c.add(ciphers.substring(j));
        String[] s = new String[c.size()];
        c.toArray(s);
        return s;
    }

    private void convertPassword(Properties p) {
        String pw = p.getProperty(KEYSTOREPWD);
        if (!(pw == null || pw.startsWith(xorTag))) {
            p.put(KEYSTOREPWD, obfuscate(pw.toCharArray()));
        }
        pw = p.getProperty(TRUSTSTOREPWD);
        if (pw != null && !pw.startsWith(xorTag)) {
            p.put(TRUSTSTOREPWD, obfuscate(pw.toCharArray()));
        }
    }

    public void initialize(Properties props, String configID) throws IllegalArgumentException {
        checkPropertyKeys(props);
        Properties p = new Properties();
        p.putAll(props);
        convertPassword(p);
        if (configID != null) {
            this.configs.put(configID, p);
        } else {
            this.defaultProperties = p;
        }
    }

    public void merge(Properties props, String configID) throws IllegalArgumentException {
        checkPropertyKeys(props);
        Properties properties = this.defaultProperties;
        if (configID == null) {
            properties = (Properties) this.configs.get(configID);
        }
        if (properties == null) {
            properties = new Properties();
        }
        convertPassword(props);
        properties.putAll(props);
        if (configID != null) {
            this.configs.put(configID, properties);
        } else {
            this.defaultProperties = properties;
        }
    }

    public boolean remove(String configID) {
        if (configID != null) {
            return this.configs.remove(configID) != null;
        } else {
            if (this.defaultProperties == null) {
                return false;
            }
            this.defaultProperties = null;
            return true;
        }
    }

    public Properties getConfiguration(String configID) {
        Properties properties;
        if (configID == null) {
            properties = this.defaultProperties;
        } else {
            properties = this.configs.get(configID);
        }
        return properties;
    }

    private String getProperty(String configID, String ibmKey, String sysProperty) {
        String res = getPropertyFromConfig(configID, ibmKey);
        if (res != null) {
            return res;
        }
        if (sysProperty != null) {
            res = System.getProperty(sysProperty);
        }
        return res;
    }

    private String getPropertyFromConfig(String configID, String ibmKey) {
        String str = null;
        Properties p = null;
        if (configID != null) {
            p = (Properties) this.configs.get(configID);
        }
        if (p != null) {
            str = p.getProperty(ibmKey);
            if (str != null) {
                return str;
            }
        }
        p = this.defaultProperties;
        if (p != null) {
            str = p.getProperty(ibmKey);
            if (str != null) {
                return str;
            }
        }
        return str;
    }

    public String getSSLProtocol(String configID) {
        return getProperty(configID, SSLPROTOCOL, null);
    }

    public String getJSSEProvider(String configID) {
        return getProperty(configID, JSSEPROVIDER, null);
    }

    private KeyStore getXPDKeystore() throws MqttDirectException {
        try {
            Class secPlatClass = Class.forName("com.ibm.rcp.security.auth.SecurePlatform");
            Object secPlat = secPlatClass.getMethod("getKeyStore", new Class[0]).invoke(null, new Object[0]);
            if (((Boolean) secPlatClass.getMethod("isLoggedIn", new Class[0]).invoke(secPlat, new Object[0])).booleanValue()) {
                return (KeyStore) secPlatClass.getMethod("getKeyStore", new Class[0]).invoke(secPlat, new Object[0]);
            }
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IllegalAccessException e2) {
            throw new MqttSSLInitException(3026, new Object[]{e2.getLocalizedMessage()}, e2);
        } catch (SecurityException e3) {
            throw new MqttSSLInitException(3026, new Object[]{e3.getLocalizedMessage()}, e3);
        } catch (NoSuchMethodException e4) {
            throw new MqttSSLInitException(3026, new Object[]{e4.getLocalizedMessage()}, e4);
        } catch (IllegalArgumentException e5) {
            throw new MqttSSLInitException(3026, new Object[]{e5.getLocalizedMessage()}, e5);
        } catch (InvocationTargetException e6) {
            throw new MqttSSLInitException(3026, new Object[]{e6.getLocalizedMessage()}, e6);
        }
    }

    public String getKeyStore(String configID) throws MqttDirectException {
        String ibmKey = KEYSTORE;
        String sysProperty = SYSKEYSTORE;
        String res = getPropertyFromConfig(configID, ibmKey);
        if (res != null) {
            return res;
        }
        if (ibmKey != null && ibmKey.equals(KEYSTORE) && getXPDKeystore() != null) {
            return "Lotus Expeditor";
        }
        if (sysProperty != null) {
            res = System.getProperty(sysProperty);
        }
        return res;
    }

    public char[] getKeyStorePassword(String configID) {
        String pw = getProperty(configID, KEYSTOREPWD, SYSKEYSTOREPWD);
        if (pw == null) {
            return null;
        }
        if (pw.startsWith(xorTag)) {
            return deObfuscate(pw);
        }
        return pw.toCharArray();
    }

    public String getKeyStoreType(String configID) {
        return getProperty(configID, KEYSTORETYPE, SYSKEYSTORETYPE);
    }

    public String getKeyStoreProvider(String configID) {
        return getProperty(configID, KEYSTOREPROVIDER, null);
    }

    public String getKeyManager(String configID) {
        return getProperty(configID, KEYSTOREMGR, SYSKEYMGRALGO);
    }

    public String getTrustStore(String configID) {
        return getProperty(configID, TRUSTSTORE, SYSTRUSTSTORE);
    }

    public char[] getTrustStorePassword(String configID) {
        String pw = getProperty(configID, TRUSTSTOREPWD, SYSTRUSTSTOREPWD);
        if (pw == null) {
            return null;
        }
        if (pw.startsWith(xorTag)) {
            return deObfuscate(pw);
        }
        return pw.toCharArray();
    }

    public String getTrustStoreType(String configID) {
        return getProperty(configID, TRUSTSTORETYPE, null);
    }

    public String getTrustStoreProvider(String configID) {
        return getProperty(configID, TRUSTSTOREPROVIDER, null);
    }

    public String getTrustManager(String configID) {
        return getProperty(configID, TRUSTSTOREMGR, SYSTRUSTMGRALGO);
    }

    public String[] getEnabledCipherSuites(String configID) {
        return unpackCipherSuites(getProperty(configID, CIPHERSUITES, null));
    }

    public boolean getClientAuthentication(String configID) {
        String auth = getProperty(configID, CLIENTAUTH, null);
        if (auth != null) {
            return Boolean.valueOf(auth).booleanValue();
        }
        return false;
    }

    private SSLContext getSSLContext(String configID) throws MqttDirectException {
        SSLContext ctx;
        String str;
        String METHOD_NAME = "getSSLContext";
        String protocol = getSSLProtocol(configID);
        if (protocol == null) {
            protocol = DEFAULT_PROTOCOL;
        }
        if (this.logger != null) {
            Logger logger = this.logger;
            String str2 = CLASS_NAME;
            String str3 = "getSSLContext";
            String str4 = "12000";
            Object[] objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = protocol;
            logger.fine(str2, str3, str4, objArr);
        }
        String provider = getJSSEProvider(configID);
        if (provider == null) {
            try {
                ctx = SSLContext.getInstance(protocol);
            } catch (KeyStoreException e) {
                throw new MqttSSLInitException(3027, null, e);
            } catch (CertificateException e2) {
                throw new MqttSSLInitException(3028, null, e2);
            } catch (FileNotFoundException e3) {
                throw new MqttSSLInitException(3029, null, e3);
            } catch (IOException e4) {
                throw new MqttSSLInitException(3030, null, e4);
            } catch (KeyStoreException e5) {
                throw new MqttSSLInitException(3016, null, e5);
            } catch (CertificateException e22) {
                throw new MqttSSLInitException(3019, null, e22);
            } catch (FileNotFoundException e32) {
                throw new MqttSSLInitException(3020, null, e32);
            } catch (IOException e42) {
                throw new MqttSSLInitException(3021, null, e42);
            } catch (UnrecoverableKeyException e6) {
                throw new MqttSSLInitException(3022, null, e6);
            } catch (NoSuchAlgorithmException e7) {
                throw new MqttSSLInitException(3018, null, e7);
            } catch (NoSuchProviderException e8) {
                throw new MqttSSLInitException(3023, null, e8);
            } catch (KeyManagementException e9) {
                throw new MqttSSLInitException(3024, null, e9);
            }
        }
        ctx = SSLContext.getInstance(protocol, provider);
        if (this.logger != null) {
            logger = this.logger;
            str2 = CLASS_NAME;
            str3 = "getSSLContext";
            str4 = "12001";
            objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = ctx.getProvider().getName();
            logger.fine(str2, str3, str4, objArr);
        }
        String keyStoreName = getProperty(configID, KEYSTORE, null);
        KeyStore keyStore = null;
        KeyManager[] keyMgr = null;
        if (keyStoreName == null) {
            keyStore = getXPDKeystore();
            if (this.logger != null) {
                if (keyStore == null) {
                    logger = this.logger;
                    str2 = CLASS_NAME;
                    str3 = "getSSLContext";
                    str4 = "12002";
                    objArr = new Object[1];
                    objArr[0] = configID != null ? configID : "null (broker defaults)";
                    logger.fine(str2, str3, str4, objArr);
                } else {
                    logger = this.logger;
                    str2 = CLASS_NAME;
                    str3 = "getSSLContext";
                    str4 = "12003";
                    objArr = new Object[1];
                    if (configID != null) {
                        str = configID;
                    } else {
                        str = "null (broker defaults)";
                    }
                    objArr[0] = str;
                    logger.fine(str2, str3, str4, objArr);
                }
            }
        }
        if (keyStore == null) {
            if (keyStoreName == null) {
                keyStoreName = getProperty(configID, KEYSTORE, SYSKEYSTORE);
            }
            if (this.logger != null) {
                logger = this.logger;
                str2 = CLASS_NAME;
                str3 = "getSSLContext";
                str4 = "12004";
                objArr = new Object[2];
                objArr[0] = configID != null ? configID : "null (broker defaults)";
                objArr[1] = keyStoreName != null ? keyStoreName : "null";
                logger.fine(str2, str3, str4, objArr);
            }
            char[] keyStorePwd = getKeyStorePassword(configID);
            if (this.logger != null) {
                logger = this.logger;
                str2 = CLASS_NAME;
                str3 = "getSSLContext";
                str4 = "12005";
                objArr = new Object[2];
                objArr[0] = configID != null ? configID : "null (broker defaults)";
                objArr[1] = keyStorePwd != null ? obfuscate(keyStorePwd) : "null";
                logger.fine(str2, str3, str4, objArr);
            }
            String keyStoreType = getKeyStoreType(configID);
            if (keyStoreType == null) {
                keyStoreType = KeyStore.getDefaultType();
            }
            if (this.logger != null) {
                logger = this.logger;
                str2 = CLASS_NAME;
                str3 = "getSSLContext";
                str4 = "12006";
                objArr = new Object[2];
                objArr[0] = configID != null ? configID : "null (broker defaults)";
                if (keyStoreType != null) {
                    str = keyStoreType;
                } else {
                    str = "null";
                }
                objArr[1] = str;
                logger.fine(str2, str3, str4, objArr);
            }
            String keyMgrAlgo = KeyManagerFactory.getDefaultAlgorithm();
            String keyMgrProvider = getKeyStoreProvider(configID);
            String keyManager = getKeyManager(configID);
            if (keyManager != null) {
                keyMgrAlgo = keyManager;
            }
            if (!(keyStoreName == null || keyStoreType == null || keyStorePwd == null || keyMgrAlgo == null)) {
                KeyManagerFactory keyMgrFact;
                keyStore = KeyStore.getInstance(keyStoreType);
                keyStore.load(new FileInputStream(keyStoreName), keyStorePwd);
                if (keyMgrProvider != null) {
                    keyMgrFact = KeyManagerFactory.getInstance(keyMgrAlgo, keyMgrProvider);
                } else {
                    keyMgrFact = KeyManagerFactory.getInstance(keyMgrAlgo);
                }
                if (this.logger != null) {
                    logger = this.logger;
                    str2 = CLASS_NAME;
                    str3 = "getSSLContext";
                    str4 = "12010";
                    objArr = new Object[2];
                    objArr[0] = configID != null ? configID : "null (broker defaults)";
                    if (keyMgrAlgo == null) {
                        keyMgrAlgo = "null";
                    }
                    objArr[1] = keyMgrAlgo;
                    logger.fine(str2, str3, str4, objArr);
                    logger = this.logger;
                    str2 = CLASS_NAME;
                    str3 = "getSSLContext";
                    str4 = "12009";
                    objArr = new Object[2];
                    objArr[0] = configID != null ? configID : "null (broker defaults)";
                    objArr[1] = keyMgrFact.getProvider().getName();
                    logger.fine(str2, str3, str4, objArr);
                }
                keyMgrFact.init(keyStore, keyStorePwd);
                keyMgr = keyMgrFact.getKeyManagers();
            }
        }
        String trustStoreName = getTrustStore(configID);
        if (this.logger != null) {
            logger = this.logger;
            str2 = CLASS_NAME;
            str3 = "getSSLContext";
            str4 = "12011";
            objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = trustStoreName != null ? trustStoreName : "null";
            logger.fine(str2, str3, str4, objArr);
        }
        TrustManager[] trustMgr = null;
        char[] trustStorePwd = getTrustStorePassword(configID);
        if (this.logger != null) {
            logger = this.logger;
            str2 = CLASS_NAME;
            str3 = "getSSLContext";
            str4 = "12012";
            objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = trustStorePwd != null ? obfuscate(trustStorePwd) : "null";
            logger.fine(str2, str3, str4, objArr);
        }
        String trustStoreType = getTrustStoreType(configID);
        if (trustStoreType == null) {
            trustStoreType = KeyStore.getDefaultType();
        }
        if (this.logger != null) {
            logger = this.logger;
            str2 = CLASS_NAME;
            str3 = "getSSLContext";
            str4 = "12013";
            objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            if (trustStoreType != null) {
                str = trustStoreType;
            } else {
                str = "null";
            }
            objArr[1] = str;
            logger.fine(str2, str3, str4, objArr);
        }
        String trustMgrAlgo = TrustManagerFactory.getDefaultAlgorithm();
        String trustMgrProvider = getTrustStoreProvider(configID);
        String trustManager = getTrustManager(configID);
        if (trustManager != null) {
            trustMgrAlgo = trustManager;
        }
        if (!(trustStoreName == null || trustStoreType == null || trustStorePwd == null || trustMgrAlgo == null)) {
            TrustManagerFactory trustMgrFact;
            KeyStore trustStore = KeyStore.getInstance(trustStoreType);
            trustStore.load(new FileInputStream(trustStoreName), trustStorePwd);
            if (trustMgrProvider != null) {
                trustMgrFact = TrustManagerFactory.getInstance(trustMgrAlgo, trustMgrProvider);
            } else {
                trustMgrFact = TrustManagerFactory.getInstance(trustMgrAlgo);
            }
            if (this.logger != null) {
                logger = this.logger;
                str2 = CLASS_NAME;
                str3 = "getSSLContext";
                str4 = "12017";
                objArr = new Object[2];
                objArr[0] = configID != null ? configID : "null (broker defaults)";
                if (trustMgrAlgo == null) {
                    trustMgrAlgo = "null";
                }
                objArr[1] = trustMgrAlgo;
                logger.fine(str2, str3, str4, objArr);
                Logger logger2 = this.logger;
                String str5 = CLASS_NAME;
                str2 = "getSSLContext";
                str3 = "12016";
                Object[] objArr2 = new Object[2];
                if (configID == null) {
                    configID = "null (broker defaults)";
                }
                objArr2[0] = configID;
                objArr2[1] = trustMgrFact.getProvider().getName();
                logger2.fine(str5, str2, str3, objArr2);
            }
            trustMgrFact.init(trustStore);
            trustMgr = trustMgrFact.getTrustManagers();
        }
        ctx.init(keyMgr, trustMgr, null);
        return ctx;
    }

    public SSLServerSocketFactory createServerSocketFactory(String configID) throws MqttDirectException {
        String METHOD_NAME = "createServerSocketFactory";
        SSLContext ctx = getSSLContext(configID);
        if (this.logger != null) {
            Logger logger = this.logger;
            String str = CLASS_NAME;
            String str2 = "createServerSocketFactory";
            String str3 = "12018";
            Object[] objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = getEnabledCipherSuites(configID) != null ? getProperty(configID, CIPHERSUITES, null) : "null (using platform-enabled cipher suites)";
            logger.fine(str, str2, str3, objArr);
            logger = this.logger;
            str = CLASS_NAME;
            str2 = "createServerSocketFactory";
            str3 = "12019";
            objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = new Boolean(getClientAuthentication(configID)).toString();
            logger.fine(str, str2, str3, objArr);
        }
        return ctx.getServerSocketFactory();
    }

    public SSLSocketFactory createSocketFactory(String configID) throws MqttDirectException {
        String METHOD_NAME = "createSocketFactory";
        SSLContext ctx = getSSLContext(configID);
        if (this.logger != null) {
            Logger logger = this.logger;
            String str = CLASS_NAME;
            String str2 = "createSocketFactory";
            String str3 = "12020";
            Object[] objArr = new Object[2];
            objArr[0] = configID != null ? configID : "null (broker defaults)";
            objArr[1] = getEnabledCipherSuites(configID) != null ? getProperty(configID, CIPHERSUITES, null) : "null (using platform-enabled cipher suites)";
            logger.fine(str, str2, str3, objArr);
        }
        return ctx.getSocketFactory();
    }
}
