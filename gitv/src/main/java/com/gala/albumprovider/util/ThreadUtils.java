package com.gala.albumprovider.util;

import android.os.AsyncTask;
import android.os.Looper;
import android.os.Process;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
    private static ExecutorService a = Executors.newCachedThreadPool(f70a);
    private static final ThreadFactory f70a = new ThreadFactory() {
        private final AtomicInteger a = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "[Async QY Pool]#" + this.a.getAndIncrement());
        }
    };

    private static class Task extends AsyncTask<Void, Void, Void> {
        private Runnable a;

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return a((Void[]) x0);
        }

        public Task(Runnable run) {
            this.a = run;
        }

        protected Void a(Void... voidArr) {
            if (this.a != null) {
                Process.setThreadPriority(10);
                this.a.run();
            }
            this.a = null;
            return null;
        }
    }

    public static ExecutorService getThreadPool() {
        return a;
    }

    public static void init() {
        if (isUIThread()) {
            Process.setThreadPriority(-19);
            new Task(null).executeOnExecutor(a, new Void[0]);
        }
    }

    public static void execute(Runnable runnable) {
        if (runnable != null) {
            new Task(runnable).executeOnExecutor(a, new Void[0]);
        }
    }

    public static void shutdown() {
        if (a != null) {
            a.shutdown();
            a = null;
        }
    }

    public static long getUIThreadId() {
        return Looper.getMainLooper().getThread().getId();
    }

    public static boolean isUIThread() {
        return getUIThreadId() == Thread.currentThread().getId();
    }
}
