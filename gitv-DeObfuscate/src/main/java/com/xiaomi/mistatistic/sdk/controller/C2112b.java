package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import java.util.ArrayList;

public class C2112b {
    private static C2112b f2194a = null;
    private static C2112b f2195b = null;
    private volatile Handler f2196c;
    private ArrayList f2197d = new ArrayList();

    private C2112b(String str) {
        new C2115f(this, str).start();
    }

    public static synchronized C2112b m1802a() {
        C2112b c2112b;
        synchronized (C2112b.class) {
            if (f2194a == null) {
                f2194a = new C2112b("local_job_dispatcher");
            }
            c2112b = f2194a;
        }
        return c2112b;
    }

    public static synchronized C2112b m1804b() {
        C2112b c2112b;
        synchronized (C2112b.class) {
            if (f2195b == null) {
                f2195b = new C2112b("remote_job_dispatcher");
            }
            c2112b = f2195b;
        }
        return c2112b;
    }

    public void m1805a(C2105e c2105e) {
        synchronized (this.f2197d) {
            if (this.f2196c == null) {
                this.f2197d.add(c2105e);
            } else {
                this.f2196c.post(new C2113c(this, c2105e));
            }
        }
    }

    public void m1806a(C2105e c2105e, long j) {
        if (this.f2196c != null) {
            this.f2196c.postDelayed(new C2114d(this, c2105e), j);
        }
    }
}
