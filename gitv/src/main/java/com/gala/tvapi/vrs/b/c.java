package com.gala.tvapi.vrs.b;

import com.gala.tvapi.a;
import com.gala.tvapi.b.b;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class c extends a {
    private String a = null;

    public c(String str) {
        this.a = str;
    }

    public final String build(String... params) {
        String str = this.a;
        if (params == null || params.length < 4) {
            return TVApiTool.parseLicenceUrl(str);
        }
        b a = com.gala.tvapi.b.c.a(TVApiBase.getTVApiProperty().getPlatform());
        String str2 = "2391461978";
        String str3 = "04022001010000000000";
        if (a != null) {
            str2 = a.b();
            str3 = a.c();
        }
        String[] strArr = new String[(params.length + 4)];
        strArr[0] = a.a(params[0]);
        strArr[1] = a.a(String.valueOf(System.currentTimeMillis()));
        strArr[2] = a.a(params[1]);
        strArr[3] = a.a(params[2]);
        strArr[4] = a.a(TVApiBase.getTVApiProperty().getVersion());
        strArr[5] = str3;
        strArr[6] = a.a(com.gala.tvapi.b.a.a(strArr[0] + "_afbe8fd3d73448c9_" + strArr[1] + "_" + str2));
        strArr[7] = a.a(params[3]);
        return a.a(str, strArr);
    }

    public final List<String> header() {
        return null;
    }
}
