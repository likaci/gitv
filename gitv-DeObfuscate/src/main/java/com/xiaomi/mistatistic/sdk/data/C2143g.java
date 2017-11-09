package com.xiaomi.mistatistic.sdk.data;

public class C2143g extends C2137a {
    private long f2239b;
    private long f2240c;
    private String f2241d;

    public C2143g(long j, long j2, String str) {
        this.f2239b = j;
        this.f2240c = j2;
        this.f2241d = str;
    }

    public C2144h mo4537a() {
        C2144h c2144h = new C2144h();
        c2144h.f2242a = m1891b();
        c2144h.f2243b = this.a;
        c2144h.f2246e = this.f2239b + "," + this.f2240c;
        c2144h.f2247f = this.f2241d;
        return c2144h;
    }

    public String m1891b() {
        return "mistat_session";
    }
}
