package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.result.ApiResultMap;
import com.gala.video.api.ApiResult;
import java.util.Map;

public final class e<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        if (cls != ApiResultMap.class) {
            return null;
        }
        T apiResultMap = new ApiResultMap();
        apiResultMap.dataLong = (Map) JSON.parseObject(str, new TypeReference<Map<String, Long>>() {
        }, new Feature[0]);
        return apiResultMap;
    }
}
