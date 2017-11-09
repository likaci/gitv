package com.xiaomi.mistatistic.sdk.p036a;

import android.os.SystemClock;
import com.xiaomi.mistatistic.sdk.controller.C2119j;
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

public class C2099b extends HttpsURLConnection {
    private long f2149a;
    private int f2150b = -1;
    private boolean f2151c = false;
    private HttpsURLConnection f2152d;

    public C2099b(HttpsURLConnection httpsURLConnection) {
        super(httpsURLConnection.getURL());
        this.f2152d = httpsURLConnection;
        this.f2149a = SystemClock.elapsedRealtime();
    }

    public void m1752a() {
        m1755b();
    }

    public void m1753a(long j) {
        this.f2149a = j;
    }

    void m1754a(Exception exception) {
        if (!this.f2151c) {
            this.f2151c = true;
            C2119j.m1824a().m1831a(new HttpEvent(getURL().toString(), exception.getClass().getSimpleName()));
        }
    }

    public void addRequestProperty(String str, String str2) {
        this.f2152d.addRequestProperty(str, str2);
    }

    public void m1755b() {
        if (!this.f2151c) {
            this.f2151c = true;
            C2119j.m1824a().m1831a(this.f2150b == -1 ? new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.f2149a) : new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.f2149a, 0, this.f2150b));
        }
    }

    public void connect() {
        try {
            this.f2152d.connect();
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public void disconnect() {
        this.f2152d.disconnect();
        m1755b();
    }

    public boolean getAllowUserInteraction() {
        return this.f2152d.getAllowUserInteraction();
    }

    public String getCipherSuite() {
        return this.f2152d.getCipherSuite();
    }

    public int getConnectTimeout() {
        return this.f2152d.getConnectTimeout();
    }

    public Object getContent() {
        return this.f2152d.getContent();
    }

    public Object getContent(Class[] clsArr) {
        try {
            return this.f2152d.getContent(clsArr);
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public String getContentEncoding() {
        return this.f2152d.getContentEncoding();
    }

    public int getContentLength() {
        return this.f2152d.getContentLength();
    }

    public String getContentType() {
        return this.f2152d.getContentType();
    }

    public long getDate() {
        return this.f2152d.getDate();
    }

    public boolean getDefaultUseCaches() {
        return this.f2152d.getDefaultUseCaches();
    }

    public boolean getDoInput() {
        return this.f2152d.getDoInput();
    }

    public boolean getDoOutput() {
        return this.f2152d.getDoOutput();
    }

    public InputStream getErrorStream() {
        return this.f2152d.getErrorStream();
    }

    public long getExpiration() {
        return this.f2152d.getExpiration();
    }

    public String getHeaderField(int i) {
        return this.f2152d.getHeaderField(i);
    }

    public String getHeaderField(String str) {
        return this.f2152d.getHeaderField(str);
    }

    public long getHeaderFieldDate(String str, long j) {
        return this.f2152d.getHeaderFieldDate(str, j);
    }

    public int getHeaderFieldInt(String str, int i) {
        return this.f2152d.getHeaderFieldInt(str, i);
    }

    public String getHeaderFieldKey(int i) {
        return this.f2152d.getHeaderFieldKey(i);
    }

    public Map getHeaderFields() {
        return this.f2152d.getHeaderFields();
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.f2152d.getHostnameVerifier();
    }

    public long getIfModifiedSince() {
        return this.f2152d.getIfModifiedSince();
    }

    public InputStream getInputStream() {
        try {
            return new C2100c(this, this.f2152d.getInputStream());
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public boolean getInstanceFollowRedirects() {
        return this.f2152d.getInstanceFollowRedirects();
    }

    public long getLastModified() {
        return this.f2152d.getLastModified();
    }

    public Certificate[] getLocalCertificates() {
        return this.f2152d.getLocalCertificates();
    }

    public Principal getLocalPrincipal() {
        return this.f2152d.getLocalPrincipal();
    }

    public OutputStream getOutputStream() {
        try {
            return new C2101d(this, this.f2152d.getOutputStream());
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public Principal getPeerPrincipal() {
        try {
            return this.f2152d.getPeerPrincipal();
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public Permission getPermission() {
        try {
            return this.f2152d.getPermission();
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public int getReadTimeout() {
        return this.f2152d.getReadTimeout();
    }

    public String getRequestMethod() {
        return this.f2152d.getRequestMethod();
    }

    public Map getRequestProperties() {
        return this.f2152d.getRequestProperties();
    }

    public String getRequestProperty(String str) {
        return this.f2152d.getRequestProperty(str);
    }

    public int getResponseCode() {
        try {
            this.f2150b = this.f2152d.getResponseCode();
            return this.f2150b;
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public String getResponseMessage() {
        try {
            return this.f2152d.getResponseMessage();
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public SSLSocketFactory getSSLSocketFactory() {
        return this.f2152d.getSSLSocketFactory();
    }

    public Certificate[] getServerCertificates() {
        try {
            return this.f2152d.getServerCertificates();
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public URL getURL() {
        return this.f2152d.getURL();
    }

    public boolean getUseCaches() {
        return this.f2152d.getUseCaches();
    }

    public void setAllowUserInteraction(boolean z) {
        this.f2152d.setAllowUserInteraction(z);
    }

    public void setChunkedStreamingMode(int i) {
        this.f2152d.setChunkedStreamingMode(i);
    }

    public void setConnectTimeout(int i) {
        this.f2152d.setConnectTimeout(i);
    }

    public void setDefaultUseCaches(boolean z) {
        this.f2152d.setDefaultUseCaches(z);
    }

    public void setDoInput(boolean z) {
        this.f2152d.setDoInput(z);
    }

    public void setDoOutput(boolean z) {
        this.f2152d.setDoOutput(z);
    }

    public void setFixedLengthStreamingMode(int i) {
        this.f2152d.setFixedLengthStreamingMode(i);
    }

    public void setFixedLengthStreamingMode(long j) {
        try {
            this.f2152d.getClass().getMethod("setFixedLengthStreamingMode", new Class[]{Long.TYPE}).invoke(this.f2152d, new Object[]{Long.valueOf(j)});
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public void setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.f2152d.setHostnameVerifier(hostnameVerifier);
    }

    public void setIfModifiedSince(long j) {
        this.f2152d.setIfModifiedSince(j);
    }

    public void setInstanceFollowRedirects(boolean z) {
        this.f2152d.setInstanceFollowRedirects(z);
    }

    public void setReadTimeout(int i) {
        this.f2152d.setReadTimeout(i);
    }

    public void setRequestMethod(String str) {
        try {
            this.f2152d.setRequestMethod(str);
        } catch (Exception e) {
            m1754a(e);
            throw e;
        }
    }

    public void setRequestProperty(String str, String str2) {
        this.f2152d.setRequestProperty(str, str2);
    }

    public void setSSLSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.f2152d.setSSLSocketFactory(sSLSocketFactory);
    }

    public void setUseCaches(boolean z) {
        this.f2152d.setUseCaches(z);
    }

    public String toString() {
        return this.f2152d.toString();
    }

    public boolean usingProxy() {
        return this.f2152d.usingProxy();
    }
}
