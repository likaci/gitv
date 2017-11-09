package com.gala.tvapi.tv2.result;

import com.gala.tvapi.b.a;
import com.gala.video.api.ApiResult;

public class ApiResultPlayFlag extends ApiResult {
    public String version = "";

    public boolean getPlayFlag() {
        return !a.a(this.version);
    }
}
