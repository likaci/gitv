package com.gala.tvapi.vrs.b;

import com.gala.tvapi.a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class h extends a {
    private String a = null;

    public h(String str) {
        this.a = str;
    }

    public final String build(String... params) {
        if (this.a.contains("m=%s") || this.a.contains("deviceId=%s")) {
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
                return a.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), str2, str, TVApiBase.getTVApiProperty().getVersion());
            }
            String str4 = "";
            for (String str5 : params) {
                str4 = str4 + "&resourceId=" + str5;
            }
            return a.a(this.a, TVApiBase.getTVApiProperty().getAuthId(), str2, str, TVApiBase.getTVApiProperty().getVersion()) + str4;
        } else if (params == null || params.length <= 0) {
            return TVApiTool.parseLicenceUrl(this.a);
        } else {
            return a.a(this.a, params);
        }
    }

    public final List<String> header() {
        return null;
    }
}
