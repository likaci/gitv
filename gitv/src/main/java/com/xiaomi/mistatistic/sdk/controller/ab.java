package com.xiaomi.mistatistic.sdk.controller;

public class ab {
    private static ab a = null;
    private int b = 3;
    private long c;
    private long d;

    private ab() {
    }

    public static synchronized ab a() {
        ab abVar;
        synchronized (ab.class) {
            if (a == null) {
                a = new ab();
            }
            abVar = a;
        }
        return abVar;
    }

    public void a(int i, long j) {
        b.a().a(new ad(this, i, j));
    }

    public void b() {
        b.a().a(new ac(this));
    }

    public void c() {
        this.d = System.currentTimeMillis();
        if (this.b == 4) {
            b.a().a(new af(this), this.c);
        }
        b.b().a(new ag(this));
    }

    public boolean d() {
        if (t.b()) {
            return false;
        }
        switch (this.b) {
            case 0:
                return true;
            case 1:
                return q.a(a.a());
            case 2:
                return new i().d() >= 50;
            case 4:
                return System.currentTimeMillis() - this.d > this.c;
            default:
                return false;
        }
    }

    public long e() {
        return this.c;
    }
}
