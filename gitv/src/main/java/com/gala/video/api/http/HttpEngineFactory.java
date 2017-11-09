package com.gala.video.api.http;

public class HttpEngineFactory {
    private static final IHttpEngine a = new d("TVApi");

    public static IHttpEngine defaultEngine() {
        return a;
    }

    public static IHttpEngine newEngine(String name, int threadCount) {
        return new d(name, threadCount);
    }

    public static IHttpEngine newCommonEngine(String name, int threadCount) {
        return new d(name, threadCount);
    }

    public static IHttpEngine newEngineDelay(String name, int threadCount, boolean isDelay) {
        return new d(name, threadCount, isDelay);
    }
}
