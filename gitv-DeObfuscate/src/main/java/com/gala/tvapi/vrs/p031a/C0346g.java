package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.model.ProgramCarousel;
import com.gala.tvapi.vrs.result.ApiResultProgramListCarousel;
import com.gala.video.api.ApiResult;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class C0346g<T extends ApiResult> extends C0336k<T> {

    class C03451 extends TypeReference<LinkedHashMap<String, List<ProgramCarousel>>> {
        C03451() {
        }
    }

    public final T mo850a(String str, Class<T> cls) {
        T apiResultProgramListCarousel = new ApiResultProgramListCarousel();
        JSONObject parseObject = JSON.parseObject(str);
        apiResultProgramListCarousel.code = parseObject.getString(PingbackConstants.CODE);
        apiResultProgramListCarousel.programMap = (Map) JSON.parseObject(parseObject.getString("programs"), new C03451(), new Feature[0]);
        return apiResultProgramListCarousel;
    }
}
