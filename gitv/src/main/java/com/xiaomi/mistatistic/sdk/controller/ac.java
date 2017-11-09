package com.xiaomi.mistatistic.sdk.controller;

class ac implements e {
    final /* synthetic */ ab a;

    ac(ab abVar) {
        this.a = abVar;
    }

    public void a() {
        this.a.b = s.a(a.a(), "upload_policy", 3);
        if (this.a.b == 4) {
            this.a.c = s.a(a.a(), "upload_interval", 86400000);
        } else {
            this.a.c = -1;
        }
        if (this.a.b == 3) {
            new t().a();
        }
    }
}
