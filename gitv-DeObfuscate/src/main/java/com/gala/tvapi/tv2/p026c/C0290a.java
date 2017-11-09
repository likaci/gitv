package com.gala.tvapi.tv2.p026c;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.video.api.ApiResult;

public final class C0290a<T extends ApiResult> extends C0289c<T> {
    public final synchronized String mo852a(String str) {
        return TVApiTool.parseLicenceUrl(str);
    }
}
