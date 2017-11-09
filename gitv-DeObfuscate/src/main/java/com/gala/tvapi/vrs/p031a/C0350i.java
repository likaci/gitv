package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.model.MixinVideo;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiResult;

public final class C0350i<T extends ApiResult> extends C0336k<T> {
    public final T mo850a(String str, Class<T> cls) {
        T apiResultRecommendListQipu = new ApiResultRecommendListQipu();
        apiResultRecommendListQipu.mixinVideos = JSON.parseArray(str, MixinVideo.class);
        return apiResultRecommendListQipu;
    }
}
