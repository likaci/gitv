package com.xiaomi.mistatistic.sdk.controller;

class ad implements e {
    final /* synthetic */ int a;
    final /* synthetic */ long b;
    final /* synthetic */ ab c;

    ad(ab abVar, int i, long j) {
        this.c = abVar;
        this.a = i;
        this.b = j;
    }

    public void a() {
        this.c.b = this.a;
        if (this.c.b == 4) {
            this.c.c = this.b;
        } else {
            this.c.c = -1;
        }
        s.b(a.a(), "upload_policy", this.c.b);
        if (this.c.b == 4) {
            s.b(a.a(), "upload_interval", this.c.c);
            b.a().a(new ae(this), this.c.c);
        }
    }
}
