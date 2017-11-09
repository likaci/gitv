package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback;

import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;

public interface IActivationCallback {
    void onException(ApiException apiException);

    void onSuccess(UserResponseBean userResponseBean);
}
