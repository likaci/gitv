package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.tvapi.vrs.result.ApiResultDailyLabels;
import com.gala.video.api.ApiResult;

public final class C0338b<T extends ApiResult> extends C0336k<T> {
    public final T mo850a(String str, Class<T> cls) {
        T apiResultDailyLabels = new ApiResultDailyLabels();
        apiResultDailyLabels.setLables(JSON.parseArray(str, DailyLabel.class));
        return apiResultDailyLabels;
    }
}
