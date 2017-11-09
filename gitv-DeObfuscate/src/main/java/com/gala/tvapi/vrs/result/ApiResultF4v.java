package com.gala.tvapi.vrs.result;

import com.gala.tvapi.p008b.C0214a;
import com.gala.video.api.ApiResult;

public class ApiResultF4v extends ApiResult {
    public String f1344l = "";
    public String f1345m = "";
    public String f1346s = "";
    public String f1347t = "";
    public String time = "";
    public String f1348v = "";
    public String f1349z = "";

    public boolean isSEmpty() {
        return C0214a.m592a(this.f1346s);
    }

    public String getS() {
        return this.f1346s;
    }
}
