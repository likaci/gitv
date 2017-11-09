package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.C0212a;
import java.util.List;

public final class C0360e extends C0212a {
    private String f1224a = null;

    public C0360e(String str) {
        this.f1224a = str;
    }

    public final String build(String... args) {
        String a = C0212a.m573a(this.f1224a, args);
        if (a == null || !a.contains("?")) {
            return a + "?qypid=-1_5201";
        }
        return a + "&qypid=-1_5201";
    }

    public final List<String> header() {
        return null;
    }
}
