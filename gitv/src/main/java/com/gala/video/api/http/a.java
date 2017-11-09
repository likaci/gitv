package com.gala.video.api.http;

public final class a extends Thread {
    private static int a;

    private static synchronized int a() {
        int i;
        synchronized (a.class) {
            i = a;
            a = i + 1;
        }
        return i;
    }

    public a() {
        super(null, null, "Thread8K-" + a(), 8192);
    }

    public a(Runnable runnable, String str) {
        super(null, runnable, str, 8192);
    }
}
