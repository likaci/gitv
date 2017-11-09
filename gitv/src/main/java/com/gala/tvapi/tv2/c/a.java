package com.gala.tvapi.tv2.c;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.video.api.ApiResult;

public final class a<T extends ApiResult> extends c<T> {
    public final synchronized String a(String str) {
        return TVApiTool.parseLicenceUrl(str);
    }
}
