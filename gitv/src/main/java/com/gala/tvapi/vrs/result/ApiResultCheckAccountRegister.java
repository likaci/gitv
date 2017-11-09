package com.gala.tvapi.vrs.result;

import com.gala.video.api.ApiResult;

public class ApiResultCheckAccountRegister extends ApiResult {
    public boolean data;

    public boolean isAccountRegister() {
        return this.data;
    }
}
