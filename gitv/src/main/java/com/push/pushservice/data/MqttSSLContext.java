package com.push.pushservice.data;

import android.content.Context;
import com.push.pushservice.utils.LogUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class MqttSSLContext {
    private static final String CLIENT_KEY_KEYSTORE = "BKS";
    private static final String PROTOCOL = "TLS";
    public static final String TAG = "PushService";
    public static MqttSSLContext instance = null;
    private final SSLContext _serverContext;

    public static MqttSSLContext getInstance(Context context) {
        if (instance == null) {
            instance = new MqttSSLContext(context);
        }
        return instance;
    }

    private MqttSSLContext(Context context) {
        SSLContext serverContext = null;
        try {
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            if (algorithm == null) {
                algorithm = "X509";
            }
            String keyStoreFilePassword = "passw0rd";
            KeyStore kks = KeyStore.getInstance(CLIENT_KEY_KEYSTORE);
            if (kks == null || context == null) {
                LogUtils.logd("PushService", "X509 算法没有相应实现");
            } else {
                InputStream fin = null;
                if (null == null) {
                    fin = new ByteArrayInputStream(KeyStoreData.keyStoreArray);
                    LogUtils.logd("PushService", "use keyStoreArray data! not from the asset file");
                }
                if (fin != null) {
                    kks.load(fin, keyStoreFilePassword.toCharArray());
                    TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
                    tmf.init(kks);
                    KeyManagerFactory kmf = KeyManagerFactory.getInstance(algorithm);
                    kmf.init(kks, "123456".toCharArray());
                    serverContext = SSLContext.getInstance("TLS");
                    serverContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                    fin.close();
                }
            }
            this._serverContext = serverContext;
        } catch (Exception e) {
            throw new Error("Failed to initialize the server-side SSLContext", e);
        } catch (Exception ex) {
            try {
                ex.printStackTrace();
                LogUtils.logd("PushService", "Error initializing SslContextManager." + ex.getMessage());
            } finally {
                this._serverContext = null;
            }
        }
    }

    public SSLContext serverContext() {
        return this._serverContext;
    }
}
