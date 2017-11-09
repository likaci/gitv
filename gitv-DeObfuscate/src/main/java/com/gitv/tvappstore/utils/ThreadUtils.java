package com.gitv.tvappstore.utils;

import android.os.AsyncTask;
import android.os.Looper;
import android.os.Process;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
    private static ExecutorService mPool = Executors.newCachedThreadPool(sThreadFactory);
    private static final ThreadFactory sThreadFactory = new C19041();

    class C19041 implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);

        C19041() {
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, "[Async QY Pool]#" + this.mCount.getAndIncrement());
        }
    }

    private static class Task extends AsyncTask<Void, Void, Void> {
        private Runnable runnable;

        public Task(Runnable run) {
            this.runnable = run;
        }

        protected Void doInBackground(Void... params) {
            if (this.runnable != null) {
                Process.setThreadPriority(10);
                this.runnable.run();
            }
            this.runnable = null;
            return null;
        }
    }

    public static ExecutorService getThreadPool() {
        return mPool;
    }

    public static void init() {
        if (isUIThread()) {
            Process.setThreadPriority(-19);
            new Task(null).executeOnExecutor(mPool, new Void[0]);
        }
    }

    public static void execute(Runnable runnable) {
        if (runnable != null) {
            new Task(runnable).executeOnExecutor(mPool, new Void[0]);
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
