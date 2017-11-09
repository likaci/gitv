package com.gala.video.api.http;

public final class C0485a extends Thread {
    private static int f1873a;

    private static synchronized int m1521a() {
        int i;
        synchronized (C0485a.class) {
            i = f1873a;
            f1873a = i + 1;
        }
        return i;
    }

    public C0485a() {
        super(null, null, "Thread8K-" + C0485a.m1521a(), 8192);
    }

    public C0485a(Runnable runnable, String str) {
        super(null, runnable, str, 8192);
    }
}
