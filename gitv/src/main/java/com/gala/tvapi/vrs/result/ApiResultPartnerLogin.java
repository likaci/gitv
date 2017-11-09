package com.gala.tvapi.vrs.result;

import com.gala.tvapi.type.UserType;
import com.gala.tvapi.vrs.model.PartnerLogin;
import com.gala.video.api.ApiResult;

public class ApiResultPartnerLogin extends ApiResult {
    public PartnerLogin data;

    public String getCookie() {
        if (this.data == null || this.data.loginUserInfo == null) {
            return "";
        }
        return this.data.loginUserInfo.authcookie;
    }

    public String getUid() {
        if (this.data == null || this.data.loginUserInfo == null) {
            return "";
        }
        return this.data.loginUserInfo.uid;
    }

    public UserType getUserType() {
        if (this.data == null || this.data.loginUserInfo == null || this.data.loginUserInfo.userinfo == null || this.data.loginUserInfo.userinfo.vip_info == null) {
            return new UserType();
        }
        return this.data.loginUserInfo.userinfo.vip_info.getUserType();
    }
}
