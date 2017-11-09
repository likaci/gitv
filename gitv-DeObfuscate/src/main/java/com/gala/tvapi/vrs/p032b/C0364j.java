package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.C0212a;
import com.gala.tvapi.tv2.TVApiBase;
import java.util.List;

public final class C0364j extends C0212a {
    private String f1228a = null;
    private boolean f1229a;
    private boolean f1230b;

    public C0364j(String str, boolean z) {
        this.f1228a = str;
        this.f1229a = true;
        this.f1230b = z;
    }

    public final String build(String... params) {
        int i = 0;
        if (params == null || params.length <= 0) {
            return C0212a.m573a(this.f1228a, TVApiBase.getTVApiProperty().getAuthId());
        }
        int length = params.length;
        if (this.f1230b) {
            length++;
        }
        if (this.f1229a) {
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
        return C0212a.m573a(this.f1228a, strArr);
    }

    public final List<String> header() {
        return null;
    }
}
