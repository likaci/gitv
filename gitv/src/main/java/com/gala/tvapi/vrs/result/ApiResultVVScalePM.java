package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.VVScale;
import com.gala.video.api.ApiResult;

public class ApiResultVVScalePM extends ApiResult {
    public VVScale data = null;

    public void setData(VVScale scale) {
        this.data = scale;
    }

    public VVScale getData() {
        return this.data;
    }
}
