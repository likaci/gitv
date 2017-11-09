package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.gala.tvapi.vrs.model.MultiChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultMultiChannelLabels;
import com.gala.video.api.ApiResult;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.LinkedHashMap;
import java.util.Map;

public final class C0344f<T extends ApiResult> extends C0336k<T> {

    class C03431 extends TypeReference<LinkedHashMap<String, MultiChannelLabels>> {
        C03431() {
        }
    }

    public final T mo850a(String str, Class<T> cls) {
        T apiResultMultiChannelLabels = new ApiResultMultiChannelLabels();
        JSONObject parseObject = JSON.parseObject(str);
        apiResultMultiChannelLabels.code = parseObject.getString(PingbackConstants.CODE);
        apiResultMultiChannelLabels.setData((Map) JSON.parseObject(parseObject.getString("data"), new C03431(), new Feature[0]));
        return apiResultMultiChannelLabels;
    }
}
