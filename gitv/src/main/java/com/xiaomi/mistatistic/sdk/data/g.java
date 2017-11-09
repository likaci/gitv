package com.xiaomi.mistatistic.sdk.data;

public class g extends a {
    private long b;
    private long c;
    private String d;

    public g(long j, long j2, String str) {
        this.b = j;
        this.c = j2;
        this.d = str;
    }

    public h a() {
        h hVar = new h();
        hVar.a = b();
        hVar.b = this.a;
        hVar.e = this.b + "," + this.c;
        hVar.f = this.d;
        return hVar;
    }

    public String b() {
        return "mistat_session";
    }
}
