package com.xiaomi.mistatistic.sdk.data;

public class f extends a {
    private String b;

    public f(String str) {
        this.b = str;
    }

    public h a() {
        h hVar = new h();
        hVar.a = b();
        hVar.b = this.a;
        hVar.e = this.b;
        return hVar;
    }

    public String b() {
        return "mistat_pv";
    }
}
