package com.xiaomi.mistatistic.sdk.data;

public class C2142f extends C2137a {
    private String f2238b;

    public C2142f(String str) {
        this.f2238b = str;
    }

    public C2144h mo4537a() {
        C2144h c2144h = new C2144h();
        c2144h.f2242a = m1889b();
        c2144h.f2243b = this.a;
        c2144h.f2246e = this.f2238b;
        return c2144h;
    }

    public String m1889b() {
        return "mistat_pv";
    }
}
