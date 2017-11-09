package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.model.MixinVideo;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiResult;

public final class i<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        T apiResultRecommendListQipu = new ApiResultRecommendListQipu();
        apiResultRecommendListQipu.mixinVideos = JSON.parseArray(str, MixinVideo.class);
        return apiResultRecommendListQipu;
    }
}
