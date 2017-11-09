package com.gala.tvapi.vrs.b;

import com.gala.tvapi.a;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class j extends a {
    private String a = null;
    private boolean f547a;
    private boolean b;

    public j(String str, boolean z) {
        this.a = str;
        this.f547a = true;
        this.b = z;
    }

    public final String build(String... params) {
        int i = 0;
        if (params == null || params.length <= 0) {
            return a.a(this.a, TVApiBase.getTVApiProperty().getAuthId());
        }
        int length = params.length;
        if (this.b) {
            length++;
        }
        if (this.f547a) {
            length++;
        }
        String[] strArr = new String[length];
        while (i < params.length) {
            strArr[i] = params[i];
            i++;
        }
        if (length - params.length == 1) {
            strArr[params.length] = TVApiBase.getTVApiProperty().getAuthId();
        } else if (length - params.length == 2) {
            strArr[params.length] = TVApiBase.getTVApiProperty().getVersion();
            strArr[params.length + 1] = TVApiBase.getTVApiProperty().getAuthId();
        }
        return a.a(this.a, strArr);
    }

    public final List<String> header() {
        return null;
    }
}
