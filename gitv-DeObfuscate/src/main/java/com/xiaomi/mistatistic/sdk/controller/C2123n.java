package com.xiaomi.mistatistic.sdk.controller;

import com.xiaomi.mistatistic.sdk.data.C2137a;
import com.xiaomi.mistatistic.sdk.data.C2139b;
import com.xiaomi.mistatistic.sdk.data.C2140c;
import com.xiaomi.mistatistic.sdk.data.C2142f;
import com.xiaomi.mistatistic.sdk.data.C2143g;
import com.xiaomi.mistatistic.sdk.data.C2144h;

class C2123n implements C2105e {
    private C2137a f2213a;

    public C2123n(C2137a c2137a) {
        this.f2213a = c2137a;
    }

    public void mo4534a() {
        C2144h a = this.f2213a.mo4537a();
        C2118i c2118i = new C2118i();
        if ((this.f2213a instanceof C2142f) || (this.f2213a instanceof C2143g) || (this.f2213a instanceof C2140c) || (this.f2213a instanceof C2139b)) {
            c2118i.m1819a(a);
        } else {
            String str = a.f2244c;
            String str2 = a.f2242a;
            C2144h a2 = c2118i.m1817a(str2, str);
            if (a2 == null || !a.f2245d.equals(a2.f2245d)) {
                c2118i.m1819a(a);
            } else {
                c2118i.m1820a(str, str2, a.f2246e);
            }
        }
        if (ab.m1789a().m1794d()) {
            new C2129t().m1866a();
        }
    }
}
