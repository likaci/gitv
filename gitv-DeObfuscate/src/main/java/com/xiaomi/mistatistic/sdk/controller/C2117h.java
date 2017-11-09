package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.text.TextUtils;

class C2117h implements C2105e {
    private Context f2204a;

    public C2117h(Context context) {
        this.f2204a = context;
    }

    public void mo4534a() {
        String a = C2128s.m1852a(this.f2204a, "device_id", "");
        if (TextUtils.isEmpty(a)) {
            C2116g.f2203a = C2116g.m1807a(this.f2204a);
            C2128s.m1856b(this.f2204a, "device_id", C2116g.f2203a);
            return;
        }
        C2116g.f2203a = a;
    }
}
