package com.gala.tvapi.vrs.p031a;

import com.alibaba.fastjson.JSON;
import com.gala.tvapi.vrs.model.Province;
import com.gala.tvapi.vrs.result.ApiResultAreaList;
import com.gala.video.api.ApiResult;

public final class C0353l<T extends ApiResult> extends C0336k<T> {
    public final T mo850a(String str, Class<T> cls) {
        T apiResultAreaList = new ApiResultAreaList();
        apiResultAreaList.setProvinceList(JSON.parseArray(str, Province.class));
        return apiResultAreaList;
    }
}
