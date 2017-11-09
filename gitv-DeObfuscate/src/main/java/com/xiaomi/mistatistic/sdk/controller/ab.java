package com.xiaomi.mistatistic.sdk.controller;

public class ab {
    private static ab f2183a = null;
    private int f2184b = 3;
    private long f2185c;
    private long f2186d;

    private ab() {
    }

    public static synchronized ab m1789a() {
        ab abVar;
        synchronized (ab.class) {
            if (f2183a == null) {
                f2183a = new ab();
            }
            abVar = f2183a;
        }
        return abVar;
    }

    public void m1791a(int i, long j) {
        C2112b.m1802a().m1805a(new ad(this, i, j));
    }

    public void m1792b() {
        C2112b.m1802a().m1805a(new ac(this));
    }

    public void m1793c() {
        this.f2186d = System.currentTimeMillis();
        if (this.f2184b == 4) {
            C2112b.m1802a().m1806a(new af(this), this.f2185c);
        }
        C2112b.m1804b().m1805a(new ag(this));
    }

    public boolean m1794d() {
        if (C2129t.m1863b()) {
            return false;
        }
        switch (this.f2184b) {
            case 0:
                return true;
            case 1:
                return C2126q.m1846a(C2111a.m1779a());
            case 2:
                return new C2118i().m1823d() >= 50;
            case 4:
                return System.currentTimeMillis() - this.f2186d > this.f2185c;
            default:
                return false;
        }
    }

    public long m1795e() {
        return this.f2185c;
    }
}
