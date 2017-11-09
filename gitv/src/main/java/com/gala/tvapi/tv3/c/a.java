package com.gala.tvapi.tv3.c;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public final class a {
    private static final ExecutorService a = Executors.newFixedThreadPool(2, new ThreadFactory() {
        private AtomicInteger a = new AtomicInteger(0);

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "TVAPI:" + this.a.incrementAndGet() + "/2");
        }
    });

    public static ExecutorService a() {
        return a;
    }
}
