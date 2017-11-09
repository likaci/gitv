package com.xiaomi.mistatistic.sdk.controller;

class ad implements C2105e {
    final /* synthetic */ int f2188a;
    final /* synthetic */ long f2189b;
    final /* synthetic */ ab f2190c;

    ad(ab abVar, int i, long j) {
        this.f2190c = abVar;
        this.f2188a = i;
        this.f2189b = j;
    }

    public void mo4534a() {
        this.f2190c.f2184b = this.f2188a;
        if (this.f2190c.f2184b == 4) {
            this.f2190c.f2185c = this.f2189b;
        } else {
            this.f2190c.f2185c = -1;
        }
        C2128s.m1854b(C2111a.m1779a(), "upload_policy", this.f2190c.f2184b);
        if (this.f2190c.f2184b == 4) {
            C2128s.m1855b(C2111a.m1779a(), "upload_interval", this.f2190c.f2185c);
            C2112b.m1802a().m1806a(new ae(this), this.f2190c.f2185c);
        }
    }
}
