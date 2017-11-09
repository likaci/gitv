package com.xiaomi.mistatistic.sdk;

import com.xiaomi.mistatistic.sdk.controller.C2119j;
import com.xiaomi.mistatistic.sdk.controller.C2124o;
import com.xiaomi.mistatistic.sdk.controller.HttpEventFilter;
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
    private static final Map f2138a = new HashMap();
    private static final List f2139b = new ArrayList();
    private static final List f2140c = new ArrayList();
    private static final List f2141d = new ArrayList();
    private static final List f2142e = new ArrayList();

    static {
        f2139b.add("file");
        f2139b.add("ftp");
        f2139b.add("http");
        f2139b.add("https");
        f2139b.add("jar");
        f2140c.add("http");
    }

    public static void addHttpEvent(HttpEvent httpEvent) {
        C2119j.m1824a().m1831a(httpEvent);
    }

    public static void dump() {
        for (HttpEvent httpEvent : C2119j.m1824a().m1833b()) {
            try {
                System.out.println("EVENT: " + httpEvent.getUrl() + ": " + httpEvent.toJSON().toString());
            } catch (JSONException e) {
            }
        }
    }

    public static boolean enableAutoRecord() {
        try {
            for (String url : f2139b) {
                URL url2 = new URL(url, "www.xiaomi.com", "");
            }
            Field declaredField = URL.class.getDeclaredField("streamHandlers");
            declaredField.setAccessible(true);
            Hashtable hashtable = (Hashtable) declaredField.get(null);
            for (String str : f2139b) {
                f2138a.put(str, (URLStreamHandler) hashtable.get(str));
            }
            URL.setURLStreamHandlerFactory(new C2104c());
            return true;
        } catch (Throwable th) {
            new C2124o().m1841a("failed to enable url interceptor", th);
            return false;
        }
    }

    public static void setEventFilter(HttpEventFilter httpEventFilter) {
        C2119j.m1824a().m1830a(httpEventFilter);
    }
}
