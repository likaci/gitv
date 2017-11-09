package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.C0212a;
import com.gala.tvapi.p008b.C0213b;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class C0358c extends C0212a {
    private String f1222a = null;

    public C0358c(String str) {
        this.f1222a = str;
    }

    public final String build(String... params) {
        String str = this.f1222a;
        if (params == null || params.length < 4) {
            return TVApiTool.parseLicenceUrl(str);
        }
        C0213b a = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform());
        String str2 = "2391461978";
        String str3 = "04022001010000000000";
        if (a != null) {
            str2 = a.mo830b();
            str3 = a.mo831c();
        }
        String[] strArr = new String[(params.length + 4)];
        strArr[0] = C0212a.m572a(params[0]);
        strArr[1] = C0212a.m572a(String.valueOf(System.currentTimeMillis()));
        strArr[2] = C0212a.m572a(params[1]);
        strArr[3] = C0212a.m572a(params[2]);
        strArr[4] = C0212a.m572a(TVApiBase.getTVApiProperty().getVersion());
        strArr[5] = str3;
        strArr[6] = C0212a.m572a(C0214a.m580a(strArr[0] + "_afbe8fd3d73448c9_" + strArr[1] + "_" + str2));
        strArr[7] = C0212a.m572a(params[3]);
        return C0212a.m573a(str, strArr);
    }

    public final List<String> header() {
        return null;
    }
}
