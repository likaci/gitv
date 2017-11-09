package com.gala.tvapi.vrs.b;

import com.gala.tvapi.a;
import com.gala.tvapi.tools.TVApiTool;
import java.util.List;

public final class f extends a {
    private String a;

    public f(String str) {
        this.a = str;
    }

    public final String build(String... params) {
        if (params == null || params.length != 3) {
            return TVApiTool.parseLicenceUrl(this.a);
        }
        String[] strArr = new String[4];
        for (int i = 0; i < params.length; i++) {
            strArr[i] = a.a(params[i]);
        }
        strArr[3] = "1,2,7";
        return a.a(this.a, strArr);
    }

    public final List<String> header() {
        return null;
    }
}
