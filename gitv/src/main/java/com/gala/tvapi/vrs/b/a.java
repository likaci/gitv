package com.gala.tvapi.vrs.b;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.core.f;

public final class a extends i {
    private String a = null;

    public a(String str) {
        this.a = str;
    }

    public final String a(PlatformType platformType, String... strArr) {
        String c;
        if (this.a.contains("keepalive.action") && strArr != null && strArr.length == 3) {
            c = f.c(strArr[0], strArr[1], strArr[2], platformType.getAgentType());
            return i.a(this.a, strArr[0], platformType.getAgentType(), strArr[1], strArr[2], c);
        } else if (!this.a.contains("secure_check_vip.action") || strArr == null || strArr.length != 1) {
            return null;
        } else {
            c = f.e(strArr[0], platformType.getAgentType());
            return i.a(this.a, strArr[0], platformType.getAgentType(), c, TVApiBase.getTVApiProperty().getPassportDeviceId());
        }
    }
}
