package com.xiaomi.mistatistic.sdk;

import android.os.SystemClock;
import com.xiaomi.mistatistic.sdk.controller.C2119j;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import com.xiaomi.mistatistic.sdk.p036a.C2098a;
import com.xiaomi.mistatistic.sdk.p036a.C2099b;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import javax.net.ssl.HttpsURLConnection;

class C2136d extends URLStreamHandler {
    private URLStreamHandler f2229a;

    public C2136d(URLStreamHandler uRLStreamHandler) {
        this.f2229a = uRLStreamHandler;
    }

    protected URLConnection openConnection(URL url) {
        try {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Method declaredMethod = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[]{URL.class});
            declaredMethod.setAccessible(true);
            URLConnection uRLConnection = (URLConnection) declaredMethod.invoke(this.f2229a, new Object[]{url});
            URLConnection c2099b;
            if (uRLConnection instanceof HttpsURLConnection) {
                c2099b = new C2099b((HttpsURLConnection) uRLConnection);
                c2099b.m1753a(elapsedRealtime);
                return c2099b;
            } else if (!(uRLConnection instanceof HttpURLConnection)) {
                return uRLConnection;
            } else {
                c2099b = new C2098a((HttpURLConnection) uRLConnection);
                c2099b.m1749a(elapsedRealtime);
                return c2099b;
            }
        } catch (Exception e) {
            C2119j.m1824a().m1831a(new HttpEvent(url.toString(), e.getClass().getSimpleName()));
            throw new IOException();
        }
    }
}
