package com.gala.tvapi.vrs.a;

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

public final class f<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        T apiResultMultiChannelLabels = new ApiResultMultiChannelLabels();
        JSONObject parseObject = JSON.parseObject(str);
        apiResultMultiChannelLabels.code = parseObject.getString(PingbackConstants.CODE);
        apiResultMultiChannelLabels.setData((Map) JSON.parseObject(parseObject.getString("data"), new TypeReference<LinkedHashMap<String, MultiChannelLabels>>() {
        }, new Feature[0]));
        return apiResultMultiChannelLabels;
    }
}
