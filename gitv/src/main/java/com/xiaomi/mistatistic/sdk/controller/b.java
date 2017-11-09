package com.xiaomi.mistatistic.sdk.controller;

import android.os.Handler;
import java.util.ArrayList;

public class b {
    private static b a = null;
    private static b b = null;
    private volatile Handler c;
    private ArrayList d = new ArrayList();

    private b(String str) {
        new f(this, str).start();
    }

    public static synchronized b a() {
        b bVar;
        synchronized (b.class) {
            if (a == null) {
                a = new b("local_job_dispatcher");
            }
            bVar = a;
        }
        return bVar;
    }

    public static synchronized b b() {
        b bVar;
        synchronized (b.class) {
            if (b == null) {
                b = new b("remote_job_dispatcher");
            }
            bVar = b;
        }
        return bVar;
    }

    public void a(e eVar) {
        synchronized (this.d) {
            if (this.c == null) {
                this.d.add(eVar);
            } else {
                this.c.post(new c(this, eVar));
            }
        }
    }

    public void a(e eVar, long j) {
        if (this.c != null) {
            this.c.postDelayed(new d(this, eVar), j);
        }
    }
}
