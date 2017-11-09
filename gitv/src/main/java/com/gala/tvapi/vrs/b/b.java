package com.gala.tvapi.vrs.b;

import com.gala.tvapi.b.a;
import com.gala.tvapi.b.k.c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;

public final class b extends i {
    private String a = null;

    public b(String str) {
        this.a = str;
    }

    public final String a(PlatformType platformType, String... strArr) {
        if (strArr == null || strArr.length < 5) {
            return TVApiTool.parseLicenceUrl(this.a);
        }
        com.gala.tvapi.b.b aVar = new a();
        if (platformType == PlatformType.ANDROID_PHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                aVar = new com.gala.tvapi.b.k.a();
            } else {
                aVar = new com.gala.tvapi.b.g.a();
            }
        } else if (platformType == PlatformType.IPHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                aVar = new c();
            } else {
                aVar = new com.gala.tvapi.b.g.b();
            }
        }
        String d = aVar.d();
        String b = aVar.b();
        String c = aVar.c();
        String[] strArr2 = new String[(strArr.length + 4)];
        strArr2[0] = i.a(strArr[0]);
        strArr2[1] = i.a(strArr[1]);
        long currentTimeMillis = System.currentTimeMillis();
        strArr2[2] = i.a(String.valueOf(currentTimeMillis));
        strArr2[3] = i.a(String.valueOf(((((int) (currentTimeMillis % 1000)) * ((int) (((double) currentTimeMillis) / Math.pow(10.0d, (double) (String.valueOf(currentTimeMillis).length() - 2))))) + 100) + Integer.valueOf(strArr[2]).intValue()));
        strArr2[4] = i.a(strArr[3]);
        b = strArr2[0] + "_afbe8fd3d73448c9_" + strArr2[1] + "_" + strArr2[2] + "_" + strArr2[3] + "_" + b;
        strArr2[5] = c;
        strArr2[6] = i.a(a.a(b));
        strArr2[7] = i.a(strArr[4]);
        strArr2[8] = i.a(TVApiBase.getTVApiProperty().getPassportDeviceId());
        return i.a(d, strArr2);
    }
}
