package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.C0212a;
import com.gala.tvapi.tools.TVApiTool;
import java.util.List;

public final class C0361f extends C0212a {
    private String f1225a;

    public C0361f(String str) {
        this.f1225a = str;
    }

    public final String build(String... params) {
        if (params == null || params.length != 3) {
            return TVApiTool.parseLicenceUrl(this.f1225a);
        }
        String[] strArr = new String[4];
        for (int i = 0; i < params.length; i++) {
            strArr[i] = C0212a.m572a(params[i]);
        }
        strArr[3] = "1,2,7";
        return C0212a.m573a(this.f1225a, strArr);
    }

    public final List<String> header() {
        return null;
    }
}
