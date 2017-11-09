package com.gala.video.api.http;

public class HttpEngineFactory {
    private static final IHttpEngine f1872a = new C0493d("TVApi");

    public static IHttpEngine defaultEngine() {
        return f1872a;
    }

    public static IHttpEngine newEngine(String name, int threadCount) {
        return new C0493d(name, threadCount);
    }

    public static IHttpEngine newCommonEngine(String name, int threadCount) {
        return new C0493d(name, threadCount);
    }

    public static IHttpEngine newEngineDelay(String name, int threadCount, boolean isDelay) {
        return new C0493d(name, threadCount, isDelay);
    }
}
