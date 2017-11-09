package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.result.ApiResultViewership;
import com.gala.video.api.ApiResult;
import java.util.List;
import java.util.Map;

public final class j<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        List list = (List) JSON.parseObject(str, new TypeReference<List<Map<String, String>>>() {
        }, new Feature[0]);
        if (list == null || list.size() <= 0) {
            return null;
        }
        T apiResultViewership = new ApiResultViewership();
        apiResultViewership.data = list;
        return apiResultViewership;
    }
}
