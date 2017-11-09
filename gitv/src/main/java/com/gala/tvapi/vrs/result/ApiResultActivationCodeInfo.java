package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ActivationCode;
import com.gala.video.api.ApiResult;

public class ApiResultActivationCodeInfo extends ApiResult {
    public ActivationCode data = null;

    public void setData(ActivationCode info) {
        this.data = info;
    }

    public ActivationCode getActivationInfo() {
        return this.data;
    }
}
