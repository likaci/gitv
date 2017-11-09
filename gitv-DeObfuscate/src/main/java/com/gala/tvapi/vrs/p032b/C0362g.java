package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.C0212a;
import com.gala.tvapi.p008b.C0213b;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import java.util.List;

public final class C0362g extends C0212a {
    private String f1226a;

    public C0362g(String str) {
        this.f1226a = str;
    }

    public final String build(String... strings) {
        int i;
        int i2;
        int i3 = 0;
        if (strings == null || strings.length <= 0) {
            i = 6;
            i2 = 0;
        } else {
            i2 = strings.length;
            i = i2 + 6;
        }
        String[] strArr = new String[i];
        while (i3 < i2) {
            strArr[i3] = strings[i3];
            i3++;
        }
        strArr[i2] = TVApiBase.getTVApiProperty().isShowVip() ? "0" : "1";
        C0213b a = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform());
        strArr[i2 + 1] = TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN ? "zh_tw" : "";
        strArr[i2 + 2] = TVApiBase.getTVApiProperty().getVersion();
        if (TVApiBase.getTVApiProperty().isShowLive()) {
            if (TVApiBase.getTVApiProperty().isShowVip()) {
                strArr[i2 + 3] = "5.0";
            } else {
                strArr[i2 + 3] = "2.0";
            }
        } else if (TVApiBase.getTVApiProperty().isShowVip()) {
            strArr[i2 + 3] = "2.0";
        } else {
            strArr[i2 + 3] = "1.0";
        }
        strArr[i2 + 4] = TVApiBase.getTVApiProperty().getAuthId();
        strArr[i2 + 5] = a.mo829a();
        String a2 = C0212a.m573a(this.f1226a, strArr);
        String passportId = TVApiBase.getTVApiProperty().getPassportId();
        if (passportId == null) {
            return a2;
        }
        if (passportId.equals("")) {
            passportId = "0";
        }
        return a2 + "&passportId=" + passportId;
    }

    public final List<String> header() {
        return null;
    }
}
