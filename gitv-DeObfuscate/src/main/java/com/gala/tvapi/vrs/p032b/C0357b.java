package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.p008b.C0213b;
import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.p008b.p015g.C0226a;
import com.gala.tvapi.p008b.p015g.C0227b;
import com.gala.tvapi.p008b.p019k.C0233a;
import com.gala.tvapi.p008b.p019k.C0235c;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;

public final class C0357b extends C0355i {
    private String f1221a = null;

    public C0357b(String str) {
        this.f1221a = str;
    }

    public final String mo866a(PlatformType platformType, String... strArr) {
        if (strArr == null || strArr.length < 5) {
            return TVApiTool.parseLicenceUrl(this.f1221a);
        }
        C0213b c0214a = new C0214a();
        if (platformType == PlatformType.ANDROID_PHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                c0214a = new C0233a();
            } else {
                c0214a = new C0226a();
            }
        } else if (platformType == PlatformType.IPHONE) {
            if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                c0214a = new C0235c();
            } else {
                c0214a = new C0227b();
            }
        }
        String d = c0214a.mo832d();
        String b = c0214a.mo830b();
        String c = c0214a.mo831c();
        String[] strArr2 = new String[(strArr.length + 4)];
        strArr2[0] = C0355i.m780a(strArr[0]);
        strArr2[1] = C0355i.m780a(strArr[1]);
        long currentTimeMillis = System.currentTimeMillis();
        strArr2[2] = C0355i.m780a(String.valueOf(currentTimeMillis));
        strArr2[3] = C0355i.m780a(String.valueOf(((((int) (currentTimeMillis % 1000)) * ((int) (((double) currentTimeMillis) / Math.pow(10.0d, (double) (String.valueOf(currentTimeMillis).length() - 2))))) + 100) + Integer.valueOf(strArr[2]).intValue()));
        strArr2[4] = C0355i.m780a(strArr[3]);
        b = strArr2[0] + "_afbe8fd3d73448c9_" + strArr2[1] + "_" + strArr2[2] + "_" + strArr2[3] + "_" + b;
        strArr2[5] = c;
        strArr2[6] = C0355i.m780a(C0214a.m580a(b));
        strArr2[7] = C0355i.m780a(strArr[4]);
        strArr2[8] = C0355i.m780a(TVApiBase.getTVApiProperty().getPassportDeviceId());
        return C0355i.m781a(d, strArr2);
    }
}
