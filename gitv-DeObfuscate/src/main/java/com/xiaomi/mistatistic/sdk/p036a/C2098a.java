package com.xiaomi.mistatistic.sdk.p036a;

import android.os.SystemClock;
import com.xiaomi.mistatistic.sdk.controller.C2119j;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.Map;

public class C2098a extends HttpURLConnection {
    private long f2143a = SystemClock.elapsedRealtime();
    private int f2144b = -1;
    private boolean f2145c = false;
    private C2101d f2146d;
    private C2100c f2147e;
    private HttpURLConnection f2148f;

    public C2098a(HttpURLConnection httpURLConnection) {
        super(httpURLConnection.getURL());
        this.f2148f = httpURLConnection;
    }

    private int m1747c() {
        int i = 0;
        int a = this.f2147e != null ? this.f2147e.m1757a() : 0;
        if (this.f2146d != null) {
            i = this.f2146d.m1759a();
        }
        return ((a + 1100) + i) + getURL().toString().getBytes().length;
    }

    public void m1748a() {
        m1751b();
    }

    public void m1749a(long j) {
        this.f2143a = j;
    }

    void m1750a(Exception exception) {
        if (!this.f2145c) {
            this.f2145c = true;
            C2119j.m1824a().m1831a(new HttpEvent(getURL().toString(), exception.getClass().getSimpleName()));
        }
    }

    public void addRequestProperty(String str, String str2) {
        this.f2148f.addRequestProperty(str, str2);
    }

    void m1751b() {
        if (!this.f2145c) {
            this.f2145c = true;
            C2119j.m1824a().m1831a(this.f2144b == -1 ? new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.f2143a) : new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.f2143a, (long) m1747c(), this.f2144b));
        }
    }

    public void connect() {
        try {
            this.f2148f.connect();
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public void disconnect() {
        this.f2148f.disconnect();
    }

    public boolean getAllowUserInteraction() {
        return this.f2148f.getAllowUserInteraction();
    }

    public int getConnectTimeout() {
        return this.f2148f.getConnectTimeout();
    }

    public Object getContent() {
        try {
            return this.f2148f.getContent();
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public Object getContent(Class[] clsArr) {
        try {
            return this.f2148f.getContent(clsArr);
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public String getContentEncoding() {
        return this.f2148f.getContentEncoding();
    }

    public int getContentLength() {
        return this.f2148f.getContentLength();
    }

    public String getContentType() {
        return this.f2148f.getContentType();
    }

    public long getDate() {
        return this.f2148f.getDate();
    }

    public boolean getDefaultUseCaches() {
        return this.f2148f.getDefaultUseCaches();
    }

    public boolean getDoInput() {
        return this.f2148f.getDoInput();
    }

    public boolean getDoOutput() {
        return this.f2148f.getDoOutput();
    }

    public InputStream getErrorStream() {
        return this.f2148f.getErrorStream();
    }

    public long getExpiration() {
        return this.f2148f.getExpiration();
    }

    public String getHeaderField(int i) {
        return this.f2148f.getHeaderField(i);
    }

    public String getHeaderField(String str) {
        return this.f2148f.getHeaderField(str);
    }

    public long getHeaderFieldDate(String str, long j) {
        return this.f2148f.getHeaderFieldDate(str, j);
    }

    public int getHeaderFieldInt(String str, int i) {
        return this.f2148f.getHeaderFieldInt(str, i);
    }

    public String getHeaderFieldKey(int i) {
        return this.f2148f.getHeaderFieldKey(i);
    }

    public Map getHeaderFields() {
        return this.f2148f.getHeaderFields();
    }

    public long getIfModifiedSince() {
        return this.f2148f.getIfModifiedSince();
    }

    public InputStream getInputStream() {
        try {
            this.f2147e = new C2100c(this, this.f2148f.getInputStream());
            return this.f2147e;
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public boolean getInstanceFollowRedirects() {
        return this.f2148f.getInstanceFollowRedirects();
    }

    public long getLastModified() {
        return this.f2148f.getLastModified();
    }

    public OutputStream getOutputStream() {
        try {
            this.f2146d = new C2101d(this, this.f2148f.getOutputStream());
            return this.f2146d;
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public Permission getPermission() {
        try {
            return this.f2148f.getPermission();
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public int getReadTimeout() {
        return this.f2148f.getReadTimeout();
    }

    public String getRequestMethod() {
        return this.f2148f.getRequestMethod();
    }

    public Map getRequestProperties() {
        return this.f2148f.getRequestProperties();
    }

    public String getRequestProperty(String str) {
        return this.f2148f.getRequestProperty(str);
    }

    public int getResponseCode() {
        try {
            this.f2144b = this.f2148f.getResponseCode();
            return this.f2144b;
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public String getResponseMessage() {
        try {
            return this.f2148f.getResponseMessage();
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public URL getURL() {
        return this.f2148f.getURL();
    }

    public boolean getUseCaches() {
        return this.f2148f.getUseCaches();
    }

    public void setAllowUserInteraction(boolean z) {
        this.f2148f.setAllowUserInteraction(z);
    }

    public void setChunkedStreamingMode(int i) {
        this.f2148f.setChunkedStreamingMode(i);
    }

    public void setConnectTimeout(int i) {
        this.f2148f.setConnectTimeout(i);
    }

    public void setDefaultUseCaches(boolean z) {
        this.f2148f.setDefaultUseCaches(z);
    }

    public void setDoInput(boolean z) {
        this.f2148f.setDoInput(z);
    }

    public void setDoOutput(boolean z) {
        this.f2148f.setDoOutput(z);
    }

    public void setFixedLengthStreamingMode(int i) {
        this.f2148f.setFixedLengthStreamingMode(i);
    }

    public void setFixedLengthStreamingMode(long j) {
        try {
            this.f2148f.getClass().getDeclaredMethod("setFixedLengthStreamingMode", new Class[]{Long.TYPE}).invoke(this.f2148f, new Object[]{Long.valueOf(j)});
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public void setIfModifiedSince(long j) {
        this.f2148f.setIfModifiedSince(j);
    }

    public void setInstanceFollowRedirects(boolean z) {
        this.f2148f.setInstanceFollowRedirects(z);
    }

    public void setReadTimeout(int i) {
        this.f2148f.setReadTimeout(i);
    }

    public void setRequestMethod(String str) {
        try {
            this.f2148f.setRequestMethod(str);
        } catch (Exception e) {
            m1750a(e);
            throw e;
        }
    }

    public void setRequestProperty(String str, String str2) {
        this.f2148f.setRequestProperty(str, str2);
    }

    public void setUseCaches(boolean z) {
        this.f2148f.setUseCaches(z);
    }

    public String toString() {
        return this.f2148f.toString();
    }

    public boolean usingProxy() {
        return this.f2148f.usingProxy();
    }
}
