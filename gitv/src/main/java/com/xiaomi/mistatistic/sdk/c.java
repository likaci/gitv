package com.xiaomi.mistatistic.sdk;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

final class c implements URLStreamHandlerFactory {
    c() {
    }

    public URLStreamHandler createURLStreamHandler(String str) {
        return URLStatsRecorder.a.containsKey(str) ? new d((URLStreamHandler) URLStatsRecorder.a.get(str)) : null;
    }
}
