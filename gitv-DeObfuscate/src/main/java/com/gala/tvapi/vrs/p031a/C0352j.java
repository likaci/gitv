package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.result.ApiResultViewership;
import com.gala.video.api.ApiResult;
import java.util.List;
import java.util.Map;

public final class C0352j<T extends ApiResult> extends C0336k<T> {

    class C03511 extends TypeReference<List<Map<String, String>>> {
        C03511() {
        }
    }

    public final T mo850a(String str, Class<T> cls) {
        List list = (List) JSON.parseObject(str, new C03511(), new Feature[0]);
        if (list == null || list.size() <= 0) {
            return null;
        }
        T apiResultViewership = new ApiResultViewership();
        apiResultViewership.data = list;
        return apiResultViewership;
    }
}
