package com.gala.imageprovider.private;

import android.os.Process;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class s {
    private int a = 2;
    private long f306a = 5;
    protected x f307a = new x();
    protected z f308a = new z(this.c);
    private DiscardOldestPolicy f309a = new DiscardOldestPolicy() {
        public final void rejectedExecution(Runnable r, ThreadPoolExecutor threadPoolExecutor) {
            if (r instanceof C) {
                ((C) r).a(new y("loading picture was canceled, because tasks too much!"));
            }
        }
    };
    protected ThreadPoolExecutor f310a;
    private AtomicInteger f311a = new AtomicInteger(0);
    private int b = 2;
    private int c = 200;
    private int d = 19;

    s() {
        if (this.f310a == null) {
            this.f310a = new ThreadPoolExecutor(this.a, this.b, this.f306a, TimeUnit.SECONDS, this.f308a.a(), this.f309a);
            this.f310a.setThreadFactory(new ThreadFactory(this) {
                final /* synthetic */ s a;

                {
                    this.a = r1;
                }

                public final Thread newThread(Runnable arg0) {
                    Thread anonymousClass1 = new H(this, arg0, "ImageProvider-" + this.a.d.getAndIncrement()) {
                        private /* synthetic */ AnonymousClass1 a;

                        public final void run() {
                            Process.setThreadPriority(this.a.a.d);
                            if (G.a) {
                                G.a("Downloader/ThreadPool", ">>>>> thread priority setting is LOW, value=" + Thread.currentThread().getPriority() + "(java)/" + Process.getThreadPriority(Process.myTid()) + "(android)");
                            }
                            super.run();
                        }
                    };
                    if (anonymousClass1.isDaemon()) {
                        anonymousClass1.setDaemon(false);
                    }
                    return anonymousClass1;
                }
            });
        }
    }
}
