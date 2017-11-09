package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.text.TextUtils;

class h implements e {
    private Context a;

    public h(Context context) {
        this.a = context;
    }

    public void a() {
        String a = s.a(this.a, "device_id", "");
        if (TextUtils.isEmpty(a)) {
            g.a = g.a(this.a);
            s.b(this.a, "device_id", g.a);
            return;
        }
        g.a = a;
    }
}
