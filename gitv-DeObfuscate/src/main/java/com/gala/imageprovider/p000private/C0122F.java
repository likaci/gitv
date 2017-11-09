package com.gala.imageprovider.p000private;

import java.util.concurrent.ConcurrentHashMap;

public final class C0122F {
    private static ConcurrentHashMap<String, Object> f540a = new ConcurrentHashMap();

    public static boolean m278a() {
        String str = "gala.imageprovider.debug.log";
        if (f540a.containsKey(str)) {
            return ((Boolean) f540a.get(str)).booleanValue();
        }
        f540a.put(str, Boolean.valueOf(false));
        return false;
    }

    public static void m277a() {
        f540a.clear();
    }
}
