package com.gala.video.lib.framework.core.utils;

import com.gala.video.app.stub.Thread8K;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NameExecutors {
    public static ExecutorService newSingleThreadScheduledExecutor(final String threadname) {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread8K(r, threadname + "#" + this.mCount.getAndIncrement());
            }
        });
    }

    public static ExecutorService newCachedThreadPool(final String threadname) {
        return Executors.newCachedThreadPool(new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread8K(r, threadname + "#" + this.mCount.getAndIncrement());
            }
        });
    }

    public static ExecutorService newFixedThreadPool(int nThreads, final String threadname) {
        return Executors.newFixedThreadPool(nThreads, new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread8K(r, threadname + "#" + this.mCount.getAndIncrement());
            }
        });
    }

    public static ExecutorService newSingleThreadExecutor(final String threadname) {
        return Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread8K(r, threadname + "#" + this.mCount.getAndIncrement());
            }
        });
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, final String threadname) {
        return Executors.newScheduledThreadPool(corePoolSize, new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                return new Thread8K(r, threadname + "#" + this.mCount.getAndIncrement());
            }
        });
    }
}
