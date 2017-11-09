package com.gala.tvapi.vrs.b;

import com.gala.tvapi.a;
import com.gala.tvapi.b.b;
import com.gala.tvapi.b.c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class d extends a {
    private String a = null;

    public d(String str) {
        this.a = str;
    }

    public final String build(String... params) {
        String str = this.a;
        if (params == null || params.length < 6) {
            return TVApiTool.parseLicenceUrl(str);
        }
        String str2;
        b a = c.a(TVApiBase.getTVApiProperty().getPlatform());
        String str3 = "2391461978";
        String str4 = "04022001010000000000";
        if (a != null) {
            str = a.d();
            str3 = a.b();
            str4 = a.c();
            str2 = str;
            str = str3;
            str3 = str4;
        } else {
            str2 = str;
            str = str3;
            str3 = str4;
        }
        String[] strArr = new String[(params.length + 4)];
        strArr[0] = a.a(params[0]);
        strArr[1] = a.a(params[1]);
        long currentTimeMillis = System.currentTimeMillis();
        strArr[2] = a.a(String.valueOf(currentTimeMillis));
        strArr[3] = a.a(String.valueOf(((((int) (currentTimeMillis % 1000)) * ((int) (((double) currentTimeMillis) / Math.pow(10.0d, (double) (String.valueOf(currentTimeMillis).length() - 2))))) + 100) + Integer.valueOf(params[2]).intValue()));
        strArr[4] = a.a(params[3]);
        str = strArr[0] + "_afbe8fd3d73448c9_" + strArr[1] + "_" + strArr[2] + "_" + strArr[3] + "_" + str;
        strArr[5] = str3;
        strArr[6] = a.a(com.gala.tvapi.b.a.a(str));
        strArr[7] = a.a(params[4]);
        strArr[8] = a.a(params[5].equals("") ? TVApiBase.getTVApiProperty().getPassportDeviceId() : params[5]);
        return String.format(TVApiTool.parseLicenceUrl(str2), (Object[]) strArr);
    }

    public final List<String> header() {
        return null;
    }
}
