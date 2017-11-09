package com.gala.tvapi.tv2.result;

import com.gala.tvapi.p008b.C0214a;
import com.gala.video.api.ApiResult;

public class ApiResultPlayFlag extends ApiResult {
    public String version = "";

    public boolean getPlayFlag() {
        return !C0214a.m592a(this.version);
    }
}
