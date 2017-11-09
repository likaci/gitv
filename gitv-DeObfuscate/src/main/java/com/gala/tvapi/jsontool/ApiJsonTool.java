package com.gala.tvapi.jsontool;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;

public class ApiJsonTool {
    private static ApiJsonTool f919a = new ApiJsonTool();

    public static ApiJsonTool get() {
        if (f919a == null) {
            f919a = new ApiJsonTool();
        }
        return f919a;
    }

    public ApiResultKeepaliveInterval parseAliveJson(String json) {
        return (ApiResultKeepaliveInterval) JSON.parseObject(json, ApiResultKeepaliveInterval.class);
    }
}
