package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account;

import android.content.Context;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.model.User;
import com.gala.tvapi.vrs.result.ApiResultData;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.IActivationCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.ILoginCallback;

public interface IGalaAccountCloud {
    void buyProductByActivationCode(String str, String str2, IActivationCallback iActivationCallback);

    void buyProductByActivationCodeOTT(String str, String str2, IActivationCallback iActivationCallback);

    boolean logOut(Context context, String str, String str2);

    UserResponseBean loginByKeyInput(String str, String str2, String str3, ILoginCallback iLoginCallback);

    void loginByScan(String str, ILoginCallback iLoginCallback);

    void loginForH5(LoginParam4H5 loginParam4H5);

    UserResponseBean registerByInput(String str, String str2, String str3, ILoginCallback iLoginCallback);

    void renewCookie(IVrsCallback<ApiResultData> iVrsCallback);

    void saveAccountInfoForH5(LoginParam4H5 loginParam4H5);

    void saveVipInfo(User user);

    UserResponseBean updateUserInfo();

    void updateUserTypeForPlayer(String str);
}
