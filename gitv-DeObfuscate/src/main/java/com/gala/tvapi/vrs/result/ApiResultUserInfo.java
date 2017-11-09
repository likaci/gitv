package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.User;
import com.gala.video.api.ApiResult;

public class ApiResultUserInfo extends ApiResult {
    public User data = null;

    public void setData(User info) {
        this.data = info;
    }

    public User getUser() {
        return this.data;
    }
}
