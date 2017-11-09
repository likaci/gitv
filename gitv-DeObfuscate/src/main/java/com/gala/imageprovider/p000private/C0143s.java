package com.gala.imageprovider.p000private;

import android.os.Process;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class C0143s {
    private int f589a = 2;
    private long f590a = 5;
    protected C0152x f591a = new C0152x();
    protected C0154z f592a = new C0154z(this.f597c);
    private DiscardOldestPolicy f593a = new C01472();
    protected ThreadPoolExecutor f594a;
    private AtomicInteger f595a = new AtomicInteger(0);
    private int f596b = 2;
    private int f597c = 200;
    private int f598d = 19;

    class C01461 implements ThreadFactory {
        final /* synthetic */ C0143s f600a;

        C01461(C0143s c0143s) {
            this.f600a = c0143s;
        }

        public final Thread newThread(Runnable arg0) {
            Thread c01451 = new C0124H(this, arg0, "ImageProvider-" + this.f600a.f598d.getAndIncrement()) {
                private /* synthetic */ C01461 f599a;

                public final void run() {
                    Process.setThreadPriority(this.f599a.f600a.f598d);
                    if (C0123G.f541a) {
                        C0123G.m279a("Downloader/ThreadPool", ">>>>> thread priority setting is LOW, value=" + Thread.currentThread().getPriority() + "(java)/" + Process.getThreadPriority(Process.myTid()) + "(android)");
                    }
                    super.run();
                }
            };
            if (c01451.isDaemon()) {
                c01451.setDaemon(false);
            }
            return c01451;
        }
    }

    class C01472 extends DiscardOldestPolicy {
        C01472() {
        }

        public final void rejectedExecution(Runnable r, ThreadPoolExecutor threadPoolExecutor) {
            if (r instanceof C0114C) {
                ((C0114C) r).mo631a(new C0153y("loading picture was canceled, because tasks too much!"));
            }
        }
    }

    C0143s() {
        if (this.f594a == null) {
            this.f594a = new ThreadPoolExecutor(this.f589a, this.f596b, this.f590a, TimeUnit.SECONDS, this.f592a.m389a(), this.f593a);
            this.f594a.setThreadFactory(new C01461(this));
        }
    }
}
