package com.xiaomi.mistatistic.sdk.controller;

import android.text.TextUtils;
import com.xiaomi.mistatistic.sdk.controller.a.c;

class u implements c {
    final /* synthetic */ t a;

    u(t tVar) {
        this.a = tVar;
    }

    public void a(String str, long j) {
        if (TextUtils.isEmpty(str)) {
            t.b = false;
            return;
        }
        this.a.a = j;
        this.a.a(str);
    }
}
