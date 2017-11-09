package com.xiaomi.mistatistic.sdk.controller;

class ac implements C2105e {
    final /* synthetic */ ab f2187a;

    ac(ab abVar) {
        this.f2187a = abVar;
    }

    public void mo4534a() {
        this.f2187a.f2184b = C2128s.m1850a(C2111a.m1779a(), "upload_policy", 3);
        if (this.f2187a.f2184b == 4) {
            this.f2187a.f2185c = C2128s.m1851a(C2111a.m1779a(), "upload_interval", 86400000);
        } else {
            this.f2187a.f2185c = -1;
        }
        if (this.f2187a.f2184b == 3) {
            new C2129t().m1866a();
        }
    }
}
