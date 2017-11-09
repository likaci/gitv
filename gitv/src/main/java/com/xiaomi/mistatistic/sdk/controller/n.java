package com.xiaomi.mistatistic.sdk.controller;

import com.xiaomi.mistatistic.sdk.data.a;
import com.xiaomi.mistatistic.sdk.data.b;
import com.xiaomi.mistatistic.sdk.data.c;
import com.xiaomi.mistatistic.sdk.data.f;
import com.xiaomi.mistatistic.sdk.data.g;
import com.xiaomi.mistatistic.sdk.data.h;

class n implements e {
    private a a;

    public n(a aVar) {
        this.a = aVar;
    }

    public void a() {
        h a = this.a.a();
        i iVar = new i();
        if ((this.a instanceof f) || (this.a instanceof g) || (this.a instanceof c) || (this.a instanceof b)) {
            iVar.a(a);
        } else {
            String str = a.c;
            String str2 = a.a;
            h a2 = iVar.a(str2, str);
            if (a2 == null || !a.d.equals(a2.d)) {
                iVar.a(a);
            } else {
                iVar.a(str, str2, a.e);
            }
        }
        if (ab.a().d()) {
            new t().a();
        }
    }
}
