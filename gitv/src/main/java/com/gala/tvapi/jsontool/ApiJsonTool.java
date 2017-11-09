package com.gala.tvapi.jsontool;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;

public class ApiJsonTool {
    private static ApiJsonTool a = new ApiJsonTool();

    public static ApiJsonTool get() {
        if (a == null) {
            a = new ApiJsonTool();
        }
        return a;
    }

    public ApiResultKeepaliveInterval parseAliveJson(String json) {
        return (ApiResultKeepaliveInterval) JSON.parseObject(json, ApiResultKeepaliveInterval.class);
    }
}
