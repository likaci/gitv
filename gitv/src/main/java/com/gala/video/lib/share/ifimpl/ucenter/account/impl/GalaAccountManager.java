package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import android.content.Context;
import com.gala.tvapi.type.UserType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.model.User;
import com.gala.tvapi.vrs.result.ApiResultData;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaAccountManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.IActivationCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.ILoginCallback;

public class GalaAccountManager extends Wrapper {
    private static final String LOG_TAG = "EPG/system/GalaAccountManager";

    GalaAccountManager() {
    }

    public UserResponseBean updateUserInfo() {
        return new GalaAccountCloud().updateUserInfo();
    }

    public void renewCookie(IVrsCallback<ApiResultData> callback) {
        new GalaAccountCloud().renewCookie(callback);
    }

    public void loginByScan(String token, ILoginCallback callback) {
        LogUtils.i(LOG_TAG, "checkQRLoad() --------- ");
        new GalaAccountCloud().loginByScan(token, callback);
    }

    public UserResponseBean loginByKeyInput(String account, String pwd, String verifyCode, ILoginCallback callback) {
        return new GalaAccountCloud().loginByKeyInput(account, pwd, verifyCode, callback);
    }

    public UserResponseBean registerByInput(String account, String pwd, String message, ILoginCallback callback) {
        return new GalaAccountCloud().registerByInput(account, pwd, message, callback);
    }

    public void loginForH5(LoginParam4H5 param) {
        LogUtils.d(LOG_TAG, ">>>>>loginForH5:{", param, "}");
        if (AppRuntimeEnv.get().getApplicationContext() == null) {
            LogUtils.e(LOG_TAG, ">>>>>AppRuntimeEnv.get().getApplicationContext()---return null");
        } else {
            new GalaAccountCloud().loginForH5(param);
        }
    }

    public void saveAccountInfoForH5(LoginParam4H5 param) {
        LogUtils.d(LOG_TAG, ">>>>>saveAccountInfoForH5:{", param, "}");
        new GalaAccountCloud().saveAccountInfoForH5(param);
    }

    public void buyProductByActivationCode(String activationCode, String verificationCode, IActivationCallback callback) {
        new GalaAccountCloud().buyProductByActivationCode(activationCode, verificationCode, callback);
    }

    public void buyProductByActivationCodeOTT(String activationCode, String verificationCode, IActivationCallback callback) {
        new GalaAccountCloud().buyProductByActivationCodeOTT(activationCode, verificationCode, callback);
    }

    public void updateUserTypeForPlayer(String json) {
        new GalaAccountCloud().updateUserTypeForPlayer(json);
    }

    public boolean logOut(Context context, String s1, String lgttype) {
        if (context == null) {
            return false;
        }
        LogUtils.d(LOG_TAG, ">>>>>logOut");
        return new GalaAccountCloud().logOut(context, s1, lgttype);
    }

    public boolean isLogin(Context context) {
        if (context == null) {
            return false;
        }
        return new GalaAccountLocal().isLogin(context);
    }

    public String getAuthCookie() {
        return new GalaAccountLocal().getAuthCookie();
    }

    public UserType getUserType() {
        return new GalaAccountLocal().getUserType();
    }

    public long getVipTimeStamp() {
        return new GalaAccountLocal().getVipTimeStamp();
    }

    public String getVipDate() {
        return new GalaAccountLocal().getVipDate();
    }

    public String getUID() {
        return new GalaAccountLocal().getUID();
    }

    public boolean getExpired() {
        return new GalaAccountLocal().getExpired();
    }

    public String getUserAccount() {
        return new GalaAccountLocal().getUserAccount();
    }

    public String getUserName() {
        return new GalaAccountLocal().getUserName();
    }

    public void setAccountType() {
        new GalaAccountLocal().setAccountType();
    }

    public String getHu() {
        return new GalaAccountLocal().getHu();
    }

    public void saveVipInfo(User user) {
        if (user != null) {
            new GalaAccountCloud().saveVipInfo(user);
        } else {
            LogUtils.e(LOG_TAG, "User is null");
        }
    }

    public boolean getIsLitchiVipForH5() {
        return new GalaAccountLocal().getIsLitchiVipForH5();
    }

    public int getUserTypeForH5() {
        return new GalaAccountLocal().getUserTypeForH5();
    }

    public boolean isVip() {
        return new GalaAccountLocal().isVip();
    }

    public String getUserPhone() {
        return new GalaAccountLocal().getUserPhone();
    }
}
