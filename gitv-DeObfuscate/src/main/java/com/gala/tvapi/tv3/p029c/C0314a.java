package com.gala.tvapi.tv3.p029c;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class C0314a {
    private static final ExecutorService f1106a = Executors.newFixedThreadPool(2, new C03131());

    static class C03131 implements ThreadFactory {
        private AtomicInteger f1105a = new AtomicInteger(0);

        C03131() {
        }

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "TVAPI:" + this.f1105a.incrementAndGet() + "/2");
        }
    }

    public static ExecutorService m728a() {
        return f1106a;
    }
}
