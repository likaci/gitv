package com.xiaomi.mistatistic.sdk.controller;

import com.xiaomi.mistatistic.sdk.controller.p037a.C2107b;
import com.xiaomi.mistatistic.sdk.controller.p037a.C2109d;

public class C2129t {
    private static boolean f2217b = false;
    private long f2218a;

    private void m1860a(String str) {
        C2112b.m1804b().m1805a(new C2109d(str, new C2131v(this)));
    }

    public static boolean m1863b() {
        return f2217b;
    }

    private void m1864c() {
        C2112b.m1802a().m1805a(new C2107b(ab.m1789a().m1795e(), new C2130u(this)));
    }

    private void m1865d() {
        C2112b.m1802a().m1805a(new C2132w(this));
    }

    public void m1866a() {
        f2217b = true;
        m1864c();
        ab.m1789a().m1793c();
    }
}
