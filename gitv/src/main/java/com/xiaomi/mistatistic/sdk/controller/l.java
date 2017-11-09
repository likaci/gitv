package com.xiaomi.mistatistic.sdk.controller;

import java.util.Collection;

class l implements e {
    final /* synthetic */ k a;

    l(k kVar) {
        this.a = kVar;
    }

    public void a() {
        if (this.a.a.c()) {
            try {
                Collection b = this.a.a.b();
                if (this.a.a.f()) {
                    synchronized (this.a.a.d) {
                        this.a.a.d.removeAll(b);
                    }
                }
            } catch (Throwable e) {
                new o().a("", e);
            } catch (Throwable e2) {
                new o().a("", e2);
            }
        }
    }
}
