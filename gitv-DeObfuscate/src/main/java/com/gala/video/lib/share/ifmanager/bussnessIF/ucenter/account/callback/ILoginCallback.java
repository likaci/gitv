package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback;

import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;

public interface ILoginCallback {
    void onLoginFail(ApiException apiException);

    void onLoginSuccess(UserInfoBean userInfoBean);
}
