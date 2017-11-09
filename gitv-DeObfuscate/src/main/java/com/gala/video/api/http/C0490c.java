package com.gala.video.api.http;

import com.gala.video.api.log.ApiEngineLog;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public final class C0490c implements X509TrustManager {
    private X509TrustManager f1889a;

    public C0490c() {
        try {
            KeyStore instance = KeyStore.getInstance("AndroidCAStore");
            instance.load(null, null);
            TrustManagerFactory instance2 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance2.init(instance);
            TrustManager[] trustManagers = instance2.getTrustManagers();
            if (trustManagers != null && trustManagers.length > 0) {
                for (int i = 0; i < trustManagers.length; i++) {
                    if (trustManagers[i] instanceof X509TrustManager) {
                        this.f1889a = (X509TrustManager) trustManagers[i];
                        return;
                    }
                }
            }
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e2) {
            e2.printStackTrace();
        } catch (CertificateException e3) {
            e3.printStackTrace();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }

    public final void checkClientTrusted(X509Certificate[] x509CertificateArr, String str) throws CertificateException {
    }

    public final void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        try {
            if (this.f1889a != null) {
                this.f1889a.checkServerTrusted(chain, authType);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            Throwable th = e;
            while (th != null) {
                if ((th instanceof CertificateExpiredException) || (th instanceof CertificateNotYetValidException)) {
                    ApiEngineLog.m1531e("HttpsTrustManager", "Certificate Exception has happened");
                    return;
                }
                th = th.getCause();
            }
            throw e;
        }
    }

    public final X509Certificate[] getAcceptedIssuers() {
        if (this.f1889a != null) {
            return this.f1889a.getAcceptedIssuers();
        }
        return null;
    }
}
