package com.xiaomi.mistatistic.sdk;

import android.os.SystemClock;
import com.xiaomi.mistatistic.sdk.a.a;
import com.xiaomi.mistatistic.sdk.a.b;
import com.xiaomi.mistatistic.sdk.controller.j;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import javax.net.ssl.HttpsURLConnection;

class d extends URLStreamHandler {
    private URLStreamHandler a;

    public d(URLStreamHandler uRLStreamHandler) {
        this.a = uRLStreamHandler;
    }

    protected URLConnection openConnection(URL url) {
        try {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            Method declaredMethod = URLStreamHandler.class.getDeclaredMethod("openConnection", new Class[]{URL.class});
            declaredMethod.setAccessible(true);
            URLConnection uRLConnection = (URLConnection) declaredMethod.invoke(this.a, new Object[]{url});
            URLConnection bVar;
            if (uRLConnection instanceof HttpsURLConnection) {
                bVar = new b((HttpsURLConnection) uRLConnection);
                bVar.a(elapsedRealtime);
                return bVar;
            } else if (!(uRLConnection instanceof HttpURLConnection)) {
                return uRLConnection;
            } else {
                bVar = new a((HttpURLConnection) uRLConnection);
                bVar.a(elapsedRealtime);
                return bVar;
            }
        } catch (Exception e) {
            j.a().a(new HttpEvent(url.toString(), e.getClass().getSimpleName()));
            throw new IOException();
        }
    }
}
