package com.xiaomi.mistatistic.sdk.controller;

import java.util.Collection;

class C2121l implements C2105e {
    final /* synthetic */ C2120k f2212a;

    C2121l(C2120k c2120k) {
        this.f2212a = c2120k;
    }

    public void mo4534a() {
        if (this.f2212a.f2211a.m1834c()) {
            try {
                Collection b = this.f2212a.f2211a.m1833b();
                if (this.f2212a.f2211a.m1829f()) {
                    synchronized (this.f2212a.f2211a.f2209d) {
                        this.f2212a.f2211a.f2209d.removeAll(b);
                    }
                }
            } catch (Throwable e) {
                new C2124o().m1841a("", e);
            } catch (Throwable e2) {
                new C2124o().m1841a("", e2);
            }
        }
    }
}
