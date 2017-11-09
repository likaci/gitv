package com.xiaomi.mistatistic.sdk;

import com.xiaomi.mistatistic.sdk.controller.HttpEventFilter;
import com.xiaomi.mistatistic.sdk.controller.j;
import com.xiaomi.mistatistic.sdk.controller.o;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLStreamHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class URLStatsRecorder {
    private static final Map a = new HashMap();
    private static final List b = new ArrayList();
    private static final List c = new ArrayList();
    private static final List d = new ArrayList();
    private static final List e = new ArrayList();

    static {
        b.add("file");
        b.add("ftp");
        b.add("http");
        b.add("https");
        b.add("jar");
        c.add("http");
    }

    public static void addHttpEvent(HttpEvent httpEvent) {
        j.a().a(httpEvent);
    }

    public static void dump() {
        for (HttpEvent httpEvent : j.a().b()) {
            try {
                System.out.println("EVENT: " + httpEvent.getUrl() + ": " + httpEvent.toJSON().toString());
            } catch (JSONException e) {
            }
        }
    }

    public static boolean enableAutoRecord() {
        try {
            for (String url : b) {
                URL url2 = new URL(url, "www.xiaomi.com", "");
            }
            Field declaredField = URL.class.getDeclaredField("streamHandlers");
            declaredField.setAccessible(true);
            Hashtable hashtable = (Hashtable) declaredField.get(null);
            for (String str : b) {
                a.put(str, (URLStreamHandler) hashtable.get(str));
            }
            URL.setURLStreamHandlerFactory(new c());
            return true;
        } catch (Throwable th) {
            new o().a("failed to enable url interceptor", th);
            return false;
        }
    }

    public static void setEventFilter(HttpEventFilter httpEventFilter) {
        j.a().a(httpEventFilter);
    }
}
