package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.result.ApiResultMap;
import com.gala.video.api.ApiResult;
import java.util.Map;

public final class C0342e<T extends ApiResult> extends C0336k<T> {

    class C03411 extends TypeReference<Map<String, Long>> {
        C03411() {
        }
    }

    public final T mo850a(String str, Class<T> cls) {
        if (cls != ApiResultMap.class) {
            return null;
        }
        T apiResultMap = new ApiResultMap();
        apiResultMap.dataLong = (Map) JSON.parseObject(str, new C03411(), new Feature[0]);
        return apiResultMap;
    }
}
