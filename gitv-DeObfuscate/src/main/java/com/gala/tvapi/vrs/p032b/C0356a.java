package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.core.C0376f;

public final class C0356a extends C0355i {
    private String f1220a = null;

    public C0356a(String str) {
        this.f1220a = str;
    }

    public final String mo866a(PlatformType platformType, String... strArr) {
        String c;
        if (this.f1220a.contains("keepalive.action") && strArr != null && strArr.length == 3) {
            c = C0376f.m824c(strArr[0], strArr[1], strArr[2], platformType.getAgentType());
            return C0355i.m781a(this.f1220a, strArr[0], platformType.getAgentType(), strArr[1], strArr[2], c);
        } else if (!this.f1220a.contains("secure_check_vip.action") || strArr == null || strArr.length != 1) {
            return null;
        } else {
            c = C0376f.m828e(strArr[0], platformType.getAgentType());
            return C0355i.m781a(this.f1220a, strArr[0], platformType.getAgentType(), c, TVApiBase.getTVApiProperty().getPassportDeviceId());
        }
    }
}
