package com.gala.tvapi.vrs.b;

import com.gala.tvapi.a;
import java.util.List;

public final class e extends a {
    private String a = null;

    public e(String str) {
        this.a = str;
    }

    public final String build(String... args) {
        String a = a.a(this.a, args);
        if (a == null || !a.contains("?")) {
            return a + "?qypid=-1_5201";
        }
        return a + "&qypid=-1_5201";
    }

    public final List<String> header() {
        return null;
    }
}
