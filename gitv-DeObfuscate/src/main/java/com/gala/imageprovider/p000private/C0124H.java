package com.gala.imageprovider.p000private;

public class C0124H extends Thread {
    private static int f542a;

    private static synchronized int m283a() {
        int i;
        synchronized (C0124H.class) {
            i = f542a;
            f542a = i + 1;
        }
        return i;
    }

    public C0124H() {
        super(null, null, "Thread8K-" + C0124H.m283a(), 8192);
    }

    public C0124H(Runnable runnable, String str) {
        super(null, runnable, str, 8192);
    }
}
