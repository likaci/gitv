package com.xiaomi.mistatistic.sdk.a;

import android.os.SystemClock;
import com.xiaomi.mistatistic.sdk.controller.j;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.Permission;
import java.security.Principal;
import java.security.cert.Certificate;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

public class b extends HttpsURLConnection {
    private long a;
    private int b = -1;
    private boolean c = false;
    private HttpsURLConnection d;

    public b(HttpsURLConnection httpsURLConnection) {
        super(httpsURLConnection.getURL());
        this.d = httpsURLConnection;
        this.a = SystemClock.elapsedRealtime();
    }

    public void a() {
        b();
    }

    public void a(long j) {
        this.a = j;
    }

    void a(Exception exception) {
        if (!this.c) {
            this.c = true;
            j.a().a(new HttpEvent(getURL().toString(), exception.getClass().getSimpleName()));
        }
    }

    public void addRequestProperty(String str, String str2) {
        this.d.addRequestProperty(str, str2);
    }

    public void b() {
        if (!this.c) {
            this.c = true;
            j.a().a(this.b == -1 ? new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.a) : new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.a, 0, this.b));
        }
    }

    public void connect() {
        try {
            this.d.connect();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void disconnect() {
        this.d.disconnect();
        b();
    }

    public boolean getAllowUserInteraction() {
        return this.d.getAllowUserInteraction();
    }

    public String getCipherSuite() {
        return this.d.getCipherSuite();
    }

    public int getConnectTimeout() {
        return this.d.getConnectTimeout();
    }

    public Object getContent() {
        return this.d.getContent();
    }

    public Object getContent(Class[] clsArr) {
        try {
            return this.d.getContent(clsArr);
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public String getContentEncoding() {
        return this.d.getContentEncoding();
    }

    public int getContentLength() {
        return this.d.getContentLength();
    }

    public String getContentType() {
        return this.d.getContentType();
    }

    public long getDate() {
        return this.d.getDate();
    }

    public boolean getDefaultUseCaches() {
        return this.d.getDefaultUseCaches();
    }

    public boolean getDoInput() {
        return this.d.getDoInput();
    }

    public boolean getDoOutput() {
        return this.d.getDoOutput();
    }

    public InputStream getErrorStream() {
        return this.d.getErrorStream();
    }

    public long getExpiration() {
        return this.d.getExpiration();
    }

    public String getHeaderField(int i) {
        return this.d.getHeaderField(i);
    }

    public String getHeaderField(String str) {
        return this.d.getHeaderField(str);
    }

    public long getHeaderFieldDate(String str, long j) {
        return this.d.getHeaderFieldDate(str, j);
    }

    public int getHeaderFieldInt(String str, int i) {
        return this.d.getHeaderFieldInt(str, i);
    }

    public String getHeaderFieldKey(int i) {
        return this.d.getHeaderFieldKey(i);
    }

    public Map getHeaderFields() {
        return this.d.getHeaderFields();
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.d.getHostnameVerifier();
    }

    public long getIfModifiedSince() {
        return this.d.getIfModifiedSince();
    }

    public InputStream getInputStream() {
        try {
            return new c(this, this.d.getInputStream());
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public boolean getInstanceFollowRedirects() {
        return this.d.getInstanceFollowRedirects();
    }

    public long getLastModified() {
        return this.d.getLastModified();
    }

    public Certificate[] getLocalCertificates() {
        return this.d.getLocalCertificates();
    }

    public Principal getLocalPrincipal() {
        return this.d.getLocalPrincipal();
    }

    public OutputStream getOutputStream() {
        try {
            return new d(this, this.d.getOutputStream());
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public Principal getPeerPrincipal() {
        try {
            return this.d.getPeerPrincipal();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public Permission getPermission() {
        try {
            return this.d.getPermission();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public int getReadTimeout() {
        return this.d.getReadTimeout();
    }

    public String getRequestMethod() {
        return this.d.getRequestMethod();
    }

    public Map getRequestProperties() {
        return this.d.getRequestProperties();
    }

    public String getRequestProperty(String str) {
        return this.d.getRequestProperty(str);
    }

    public int getResponseCode() {
        try {
            this.b = this.d.getResponseCode();
            return this.b;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public String getResponseMessage() {
        try {
            return this.d.getResponseMessage();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return this.d.getSSLSocketFactory();
    }

    public Certificate[] getServerCertificates() {
        try {
            return this.d.getServerCertificates();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public URL getURL() {
        return this.d.getURL();
    }

    public boolean getUseCaches() {
        return this.d.getUseCaches();
    }

    public void setAllowUserInteraction(boolean z) {
        this.d.setAllowUserInteraction(z);
    }

    public void setChunkedStreamingMode(int i) {
        this.d.setChunkedStreamingMode(i);
    }

    public void setConnectTimeout(int i) {
        this.d.setConnectTimeout(i);
    }

    public void setDefaultUseCaches(boolean z) {
        this.d.setDefaultUseCaches(z);
    }

    public void setDoInput(boolean z) {
        this.d.setDoInput(z);
    }

    public void setDoOutput(boolean z) {
        this.d.setDoOutput(z);
    }

    public void setFixedLengthStreamingMode(int i) {
        this.d.setFixedLengthStreamingMode(i);
    }

    public void setFixedLengthStreamingMode(long j) {
        try {
            this.d.getClass().getMethod("setFixedLengthStreamingMode", new Class[]{Long.TYPE}).invoke(this.d, new Object[]{Long.valueOf(j)});
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.d.setHostnameVerifier(hostnameVerifier);
    }

    public void setIfModifiedSince(long j) {
        this.d.setIfModifiedSince(j);
    }

    public void setInstanceFollowRedirects(boolean z) {
        this.d.setInstanceFollowRedirects(z);
    }

    public void setReadTimeout(int i) {
        this.d.setReadTimeout(i);
    }

    public void setRequestMethod(String str) {
        try {
            this.d.setRequestMethod(str);
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void setRequestProperty(String str, String str2) {
        this.d.setRequestProperty(str, str2);
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.d.setSSLSocketFactory(sSLSocketFactory);
    }

    public void setUseCaches(boolean z) {
        this.d.setUseCaches(z);
    }

    public String toString() {
        return this.d.toString();
    }

    public boolean usingProxy() {
        return this.d.usingProxy();
    }
}
