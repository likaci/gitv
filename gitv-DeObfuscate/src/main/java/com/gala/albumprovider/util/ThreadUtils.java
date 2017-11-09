package com.gala.albumprovider.util;

import android.os.AsyncTask;
import android.os.Looper;
import android.os.Process;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
    private static ExecutorService f294a = Executors.newCachedThreadPool(f295a);
    private static final ThreadFactory f295a = new C00681();

    static class C00681 implements ThreadFactory {
        private final AtomicInteger f292a = new AtomicInteger(1);

        C00681() {
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, "[Async QY Pool]#" + this.f292a.getAndIncrement());
        }
    }

    private static class Task extends AsyncTask<Void, Void, Void> {
        private Runnable f293a;

        protected /* synthetic */ Object doInBackground(Object[] x0) {
            return m144a((Void[]) x0);
        }

        public Task(Runnable run) {
            this.f293a = run;
        }

        protected Void m144a(Void... voidArr) {
            if (this.f293a != null) {
                Process.setThreadPriority(10);
                this.f293a.run();
            }
            this.f293a = null;
            return null;
        }
    }

    public static ExecutorService getThreadPool() {
        return f294a;
    }

    public static void init() {
        if (isUIThread()) {
            Process.setThreadPriority(-19);
            new Task(null).executeOnExecutor(f294a, new Void[0]);
        }
    }

    public static void execute(Runnable runnable) {
        if (runnable != null) {
            new Task(runnable).executeOnExecutor(f294a, new Void[0]);
        }
    }

    public static void shutdown() {
        if (f294a != null) {
            f294a.shutdown();
            f294a = null;
        }
    }

    public static long getUIThreadId() {
        return Looper.getMainLooper().getThread().getId();
    }

    public static boolean isUIThread() {
        return getUIThreadId() == Thread.currentThread().getId();
    }
}
