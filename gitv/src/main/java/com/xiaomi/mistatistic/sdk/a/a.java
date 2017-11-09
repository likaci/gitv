package com.xiaomi.mistatistic.sdk.a;

import android.os.SystemClock;
import com.xiaomi.mistatistic.sdk.controller.j;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Permission;
import java.util.Map;

public class a extends HttpURLConnection {
    private long a = SystemClock.elapsedRealtime();
    private int b = -1;
    private boolean c = false;
    private d d;
    private c e;
    private HttpURLConnection f;

    public a(HttpURLConnection httpURLConnection) {
        super(httpURLConnection.getURL());
        this.f = httpURLConnection;
    }

    private int c() {
        int i = 0;
        int a = this.e != null ? this.e.a() : 0;
        if (this.d != null) {
            i = this.d.a();
        }
        return ((a + 1100) + i) + getURL().toString().getBytes().length;
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
        this.f.addRequestProperty(str, str2);
    }

    void b() {
        if (!this.c) {
            this.c = true;
            j.a().a(this.b == -1 ? new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.a) : new HttpEvent(getURL().toString(), SystemClock.elapsedRealtime() - this.a, (long) c(), this.b));
        }
    }

    public void connect() {
        try {
            this.f.connect();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void disconnect() {
        this.f.disconnect();
    }

    public boolean getAllowUserInteraction() {
        return this.f.getAllowUserInteraction();
    }

    public int getConnectTimeout() {
        return this.f.getConnectTimeout();
    }

    public Object getContent() {
        try {
            return this.f.getContent();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public Object getContent(Class[] clsArr) {
        try {
            return this.f.getContent(clsArr);
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public String getContentEncoding() {
        return this.f.getContentEncoding();
    }

    public int getContentLength() {
        return this.f.getContentLength();
    }

    public String getContentType() {
        return this.f.getContentType();
    }

    public long getDate() {
        return this.f.getDate();
    }

    public boolean getDefaultUseCaches() {
        return this.f.getDefaultUseCaches();
    }

    public boolean getDoInput() {
        return this.f.getDoInput();
    }

    public boolean getDoOutput() {
        return this.f.getDoOutput();
    }

    public InputStream getErrorStream() {
        return this.f.getErrorStream();
    }

    public long getExpiration() {
        return this.f.getExpiration();
    }

    public String getHeaderField(int i) {
        return this.f.getHeaderField(i);
    }

    public String getHeaderField(String str) {
        return this.f.getHeaderField(str);
    }

    public long getHeaderFieldDate(String str, long j) {
        return this.f.getHeaderFieldDate(str, j);
    }

    public int getHeaderFieldInt(String str, int i) {
        return this.f.getHeaderFieldInt(str, i);
    }

    public String getHeaderFieldKey(int i) {
        return this.f.getHeaderFieldKey(i);
    }

    public Map getHeaderFields() {
        return this.f.getHeaderFields();
    }

    public long getIfModifiedSince() {
        return this.f.getIfModifiedSince();
    }

    public InputStream getInputStream() {
        try {
            this.e = new c(this, this.f.getInputStream());
            return this.e;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public boolean getInstanceFollowRedirects() {
        return this.f.getInstanceFollowRedirects();
    }

    public long getLastModified() {
        return this.f.getLastModified();
    }

    public OutputStream getOutputStream() {
        try {
            this.d = new d(this, this.f.getOutputStream());
            return this.d;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public Permission getPermission() {
        try {
            return this.f.getPermission();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public int getReadTimeout() {
        return this.f.getReadTimeout();
    }

    public String getRequestMethod() {
        return this.f.getRequestMethod();
    }

    public Map getRequestProperties() {
        return this.f.getRequestProperties();
    }

    public String getRequestProperty(String str) {
        return this.f.getRequestProperty(str);
    }

    public int getResponseCode() {
        try {
            this.b = this.f.getResponseCode();
            return this.b;
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public String getResponseMessage() {
        try {
            return this.f.getResponseMessage();
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public URL getURL() {
        return this.f.getURL();
    }

    public boolean getUseCaches() {
        return this.f.getUseCaches();
    }

    public void setAllowUserInteraction(boolean z) {
        this.f.setAllowUserInteraction(z);
    }

    public void setChunkedStreamingMode(int i) {
        this.f.setChunkedStreamingMode(i);
    }

    public void setConnectTimeout(int i) {
        this.f.setConnectTimeout(i);
    }

    public void setDefaultUseCaches(boolean z) {
        this.f.setDefaultUseCaches(z);
    }

    public void setDoInput(boolean z) {
        this.f.setDoInput(z);
    }

    public void setDoOutput(boolean z) {
        this.f.setDoOutput(z);
    }

    public void setFixedLengthStreamingMode(int i) {
        this.f.setFixedLengthStreamingMode(i);
    }

    public void setFixedLengthStreamingMode(long j) {
        try {
            this.f.getClass().getDeclaredMethod("setFixedLengthStreamingMode", new Class[]{Long.TYPE}).invoke(this.f, new Object[]{Long.valueOf(j)});
        } catch (Throwable e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public void setIfModifiedSince(long j) {
        this.f.setIfModifiedSince(j);
    }

    public void setInstanceFollowRedirects(boolean z) {
        this.f.setInstanceFollowRedirects(z);
    }

    public void setReadTimeout(int i) {
        this.f.setReadTimeout(i);
    }

    public void setRequestMethod(String str) {
        try {
            this.f.setRequestMethod(str);
        } catch (Exception e) {
            a(e);
            throw e;
        }
    }

    public void setRequestProperty(String str, String str2) {
        this.f.setRequestProperty(str, str2);
    }

    public void setUseCaches(boolean z) {
        this.f.setUseCaches(z);
    }

    public String toString() {
        return this.f.toString();
    }

    public boolean usingProxy() {
        return this.f.usingProxy();
    }
}
