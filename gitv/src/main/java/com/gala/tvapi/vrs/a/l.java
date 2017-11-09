package com.gala.tvapi.vrs.a;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.model.Province;
import com.gala.tvapi.vrs.result.ApiResultAreaList;
import com.gala.video.api.ApiResult;

public final class l<T extends ApiResult> extends k<T> {
    public final T a(String str, Class<T> cls) {
        T apiResultAreaList = new ApiResultAreaList();
        apiResultAreaList.setProvinceList(JSON.parseArray(str, Province.class));
        return apiResultAreaList;
    }
}
