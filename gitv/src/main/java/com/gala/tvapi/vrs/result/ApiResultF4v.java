package com.gala.tvapi.vrs.result;

import com.gala.tvapi.b.a;
import com.gala.video.api.ApiResult;

public class ApiResultF4v extends ApiResult {
    public String l = "";
    public String m = "";
    public String s = "";
    public String t = "";
    public String time = "";
    public String v = "";
    public String z = "";

    public boolean isSEmpty() {
        return a.a(this.s);
    }

    public String getS() {
        return this.s;
    }
}
