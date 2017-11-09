package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.C0212a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class C0363h extends C0212a {
    private String f1227a = null;

    public C0363h(String str) {
        this.f1227a = str;
    }

    public final String build(String... params) {
        if (this.f1227a.contains("m=%s") || this.f1227a.contains("deviceId=%s")) {
            String str;
            String str2 = TVApiBase.getTVApiProperty().isShowVip() ? "0" : "1";
            String str3 = "1.0";
            if (!TVApiBase.getTVApiProperty().isShowLive()) {
                str = str3;
            } else if (TVApiBase.getTVApiProperty().isShowVip()) {
                str = "3.0";
            } else {
                str = "2.0";
            }
            if (params == null || params.length <= 0) {
                return C0212a.m573a(this.f1227a, TVApiBase.getTVApiProperty().getAuthId(), str2, str, TVApiBase.getTVApiProperty().getVersion());
            }
            String str4 = "";
            for (String str5 : params) {
                str4 = str4 + "&resourceId=" + str5;
            }
            return C0212a.m573a(this.f1227a, TVApiBase.getTVApiProperty().getAuthId(), str2, str, TVApiBase.getTVApiProperty().getVersion()) + str4;
        } else if (params == null || params.length <= 0) {
            return TVApiTool.parseLicenceUrl(this.f1227a);
        } else {
            return C0212a.m573a(this.f1227a, params);
        }
    }

    public final List<String> header() {
        return null;
    }
}
