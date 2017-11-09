package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.tvapi.vrs.result.ApiResultDailyLabels;
import com.gala.video.api.ApiResult;

public final class b<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        T apiResultDailyLabels = new ApiResultDailyLabels();
        apiResultDailyLabels.setLables(JSON.parseArray(str, DailyLabel.class));
        return apiResultDailyLabels;
    }
}
