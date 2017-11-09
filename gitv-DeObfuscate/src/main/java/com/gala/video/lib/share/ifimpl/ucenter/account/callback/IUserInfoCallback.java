package com.gala.video.lib.share.ifimpl.ucenter.account.callback;

import com.gala.video.api.ApiException;

public interface IUserInfoCallback {
    void onUserInfoCorrect();

    void onUserInfoException(ApiException apiException);
}
