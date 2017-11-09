package com.xcrash.crashreporter.utils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class JobManager {
    private static final String TAG = "Xcrash.Job";
    private static JobManager instance;
    private ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private JobManager() {
    }

    public static synchronized JobManager getInstance() {
        JobManager jobManager;
        synchronized (JobManager.class) {
            if (instance == null) {
                instance = new JobManager();
            }
            jobManager = instance;
        }
        return jobManager;
    }

    public final synchronized boolean postRunnable(Runnable runnable) {
        boolean success;
        success = false;
        synchronized (this) {
            if (!isExcutorOn()) {
                DebugLog.d(TAG, "Async handler was closed");
            } else if (runnable == null) {
                DebugLog.d(TAG, "Task is null.");
            } else {
                DebugLog.i(TAG, "Post a normal task");
                try {
                    this.executorService.execute(runnable);
                    success = true;
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        return success;
    }

    public final synchronized boolean isExcutorOn() {
        boolean on;
        on = (this.executorService == null || this.executorService.isShutdown()) ? false : true;
        return on;
    }
}
