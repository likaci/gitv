package com.gala.video.lib.framework.core.utils;

import android.os.Looper;
import com.gala.video.app.stub.Thread8K;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
    private static ExecutorService mPool = Executors.newCachedThreadPool(sThreadFactory);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread8K(r, "[ThreadUtils]#" + this.mCount.getAndIncrement());
        }
    };

    public static ExecutorService getThreadPool() {
        return mPool;
    }

    public static void execute(Runnable runnable) {
        if (runnable != null) {
            mPool.execute(runnable);
        }
    }

    public static void shutdown() {
        if (mPool != null) {
            mPool.shutdown();
            mPool = null;
        }
    }

    public static long getUIThreadId() {
        return Looper.getMainLooper().getThread().getId();
    }

    public static boolean isUIThread() {
        return getUIThreadId() == Thread.currentThread().getId();
    }
}
