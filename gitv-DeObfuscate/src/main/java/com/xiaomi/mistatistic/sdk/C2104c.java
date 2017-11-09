package com.xiaomi.mistatistic.sdk;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

final class C2104c implements URLStreamHandlerFactory {
    C2104c() {
    }

    public URLStreamHandler createURLStreamHandler(String str) {
        return URLStatsRecorder.f2138a.containsKey(str) ? new C2136d((URLStreamHandler) URLStatsRecorder.f2138a.get(str)) : null;
    }
}
