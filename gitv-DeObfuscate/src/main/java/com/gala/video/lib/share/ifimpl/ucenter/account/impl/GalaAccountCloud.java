package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.UserType;
import com.gala.tvapi.vrs.BOSSHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.PassportTVHelper;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.model.CommonUserInfo;
import com.gala.tvapi.vrs.model.DeadLine;
import com.gala.tvapi.vrs.model.GalaVipInfo;
import com.gala.tvapi.vrs.model.User;
import com.gala.tvapi.vrs.result.ApiResultActivationCodeInfo;
import com.gala.tvapi.vrs.result.ApiResultData;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;
import com.gala.tvapi.vrs.result.ApiResultUserInfo;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.helper.AccountAdsHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.IActivationCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.ILoginCallback;
import com.gala.video.lib.share.pingback.PingBackParams;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;
import tv.gitv.ptqy.security.fingerprint.FingerPrintManager;
import tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack;

class GalaAccountCloud {
    private static final String LOG_TAG = "GalaAccountCloud";
    private GalaAccountLocal mLocal = new GalaAccountLocal();
    private PassportPreference mPPreference = PassportPreference.get();

    GalaAccountCloud() {
    }

    UserResponseBean updateUserInfo() {
        final String cookie = this.mLocal.getAuthCookie();
        final UserResponseBean responseBean = new UserResponseBean();
        PassportTVHelper.userInfo.callSync(new IVrsCallback<ApiResultUserInfo>() {
            public void onSuccess(ApiResultUserInfo userInfo) {
                LogUtils.m1568d(GalaAccountCloud.LOG_TAG, ">>>>>PassportTVHelper.userInfo.callSync---onSuccess");
                responseBean.setRespResult(true);
                if (userInfo != null) {
                    User user = userInfo.getUser();
                    GalaAccountCloud.this.saveVipInfo(user);
                    if (user != null) {
                        responseBean.setCookie(cookie);
                        responseBean.setUserType(user.getUserType());
                        responseBean.setInsecureAccount(user.isInsecureAccount());
                        CommonUserInfo info = user.userinfo;
                        if (info != null) {
                            responseBean.setAccount(info.user_name);
                            responseBean.setNickName(info.nickname);
                            responseBean.setPhone(info.phone);
                            responseBean.setUid(info.uid);
                        }
                    }
                    GalaAccountCloud.this.saveUserInfo(responseBean.getCookie(), responseBean.getUid(), responseBean.getAccount(), responseBean.getNickName(), responseBean.getPhone());
                }
            }

            public void onException(ApiException exception) {
                LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "PassportTVHelper.userInfo", exception);
                responseBean.setRespResult(false);
                responseBean.setException(exception);
            }
        }, cookie);
        return responseBean;
    }

    void loginByScan(String token, final ILoginCallback callback) {
        PassportTVHelper.checkTVLogin.call(new IVrsCallback<ApiResultUserInfo>() {
            public void onSuccess(ApiResultUserInfo resultUserInfo) {
                UserInfoBean mUserInfo = new UserInfoBean();
                mUserInfo.setCookie(resultUserInfo.getUser().authcookie);
                mUserInfo.setAccount(resultUserInfo.getUser().userinfo.user_name);
                mUserInfo.setName(resultUserInfo.getUser().userinfo.nickname);
                mUserInfo.setPhone(resultUserInfo.getUser().userinfo.phone);
                AccountLoginHelper.mergeHistoryAndCollect(mUserInfo.getCookie());
                GalaAccountCloud.this.saveUserInfo(mUserInfo.getCookie(), resultUserInfo.getUser().userinfo.uid, mUserInfo.getAccount(), mUserInfo.getName(), mUserInfo.getPhone());
                UserResponseBean respBean = GalaAccountCloud.this.loginByScan();
                if (respBean == null) {
                    return;
                }
                if (respBean.getRespResult()) {
                    callback.onLoginSuccess(mUserInfo);
                } else {
                    callback.onLoginFail(null);
                }
            }

            public void onException(ApiException exception) {
                callback.onLoginFail(exception);
            }
        }, token);
    }

    UserResponseBean loginByKeyInput(String account, String pwd, String verifyCode, final ILoginCallback callback) {
        final UserResponseBean respBean = new UserResponseBean();
        PassportTVHelper.loginWithCode.call(new IVrsCallback<ApiResultUserInfo>() {
            public void onSuccess(ApiResultUserInfo userInfo) {
                LogUtils.m1568d(GalaAccountCloud.LOG_TAG, ">>>>>PassportTVHelper.loginWithCode.callSync---onSuccess");
                respBean.setRespResult(true);
                GalaAccountCloud.this.onAccountLoginSuccess(userInfo, respBean, callback);
            }

            public void onException(ApiException exception) {
                LogUtils.m1573e(GalaAccountCloud.LOG_TAG, ">>>>>PassportTVHelper.loginWithCode.callSync---onException---code:", exception.getCode());
                respBean.setRespResult(false);
                respBean.setException(exception);
                LoginPingbackUtils.getInstance().errorPingback("tvlogin", exception != null ? exception.getCode() : "", "PassportTVHelper.loginWithCode", exception);
                callback.onLoginFail(exception);
            }
        }, account, pwd, verifyCode, DeviceUtils.getMacAddr(), "");
        return respBean;
    }

    UserResponseBean registerByInput(String account, String pwd, String message, final ILoginCallback callback) {
        final UserResponseBean respBean = new UserResponseBean();
        PassportTVHelper.registerByPhoneNew.call(new IVrsCallback<ApiResultUserInfo>() {
            public void onSuccess(ApiResultUserInfo userInfo) {
                respBean.setRespResult(true);
                GalaAccountCloud.this.onAccountLoginSuccess(userInfo, respBean, callback);
            }

            public void onException(ApiException exception) {
                respBean.setRespResult(false);
                respBean.setException(exception);
                LoginPingbackUtils.getInstance().errorPingback("tvsignup", exception != null ? exception.getCode() : "", "PassportTVHelper.registerByPhoneNew", exception);
                callback.onLoginFail(exception);
            }
        }, message, account, pwd, DeviceUtils.getMacAddr());
        return respBean;
    }

    void loginForH5(LoginParam4H5 param) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        saveUserInfo(param.cookie, param.uid, param.account, param.nickName, "");
        AccountLoginHelper.mergeHistoryAndCollect(param.cookie);
        this.mPPreference.setVipDate(context, param.vipDate);
        this.mLocal.saveUserTypeForH5(param.userTypeH5, param.isLitchiH5);
        this.mLocal.setAccountType();
        LoginPingbackUtils.getInstance().logSucc(LoginConstant.A_LOGINQR_BUY, "");
        LoginCallbackRecorder.get().notifyLogin(param.uid);
        checkVipAccount(param.cookie);
        AccountLoginHelper.hideAndRestartScreenSaver(context);
    }

    void saveAccountInfoForH5(LoginParam4H5 param) {
        this.mLocal.saveAccountInfoForH5(param);
        TVApiBase.getTVApiProperty().setUid(param.uid);
        checkVipAccount(param.cookie);
        LoginCallbackRecorder.get().notifyLogin(param.uid);
    }

    void buyProductByActivationCode(String activationCode, String verificationCode, final IActivationCallback callback) {
        final String cookie = this.mPPreference.getCookie(AppRuntimeEnv.get().getApplicationContext());
        BOSSHelper.buyProductByActivationCode.call(new IVrsCallback<ApiResultActivationCodeInfo>() {
            public void onSuccess(ApiResultActivationCodeInfo codeInfo) {
                UserResponseBean respBean = GalaAccountCloud.this.updateUserInfo();
                GalaAccountCloud.this.checkVipAccount(cookie);
                callback.onSuccess(respBean);
            }

            public void onException(ApiException exception) {
                LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "BOSSHelper.buyProductByActivationCode", exception);
                callback.onException(exception);
            }
        }, activationCode, verificationCode, cookie);
    }

    void buyProductByActivationCodeOTT(String activationCode, String verificationCode, final IActivationCallback callback) {
        final String cookie = this.mPPreference.getCookie(AppRuntimeEnv.get().getApplicationContext());
        BOSSHelper.buyProductByActivationCodeOTT.call(new IVrsCallback<ApiResultActivationCodeInfo>() {
            public void onSuccess(ApiResultActivationCodeInfo codeInfo) {
                UserResponseBean respBean = GalaAccountCloud.this.updateUserInfo();
                GalaAccountCloud.this.checkVipAccount(cookie);
                PingBackInitParams params = PingBack.getInstance().getPingbackInitParams();
                params.sIsVipAct = "0";
                PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), params);
                callback.onSuccess(respBean);
            }

            public void onException(ApiException exception) {
                LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "BOSSHelper.buyProductByActivationCode", exception);
                callback.onException(exception);
            }
        }, activationCode, verificationCode, cookie, AccountLoginHelper.getLimitCodeByCustomerName());
    }

    boolean logOut(Context context, String s1, String lgttype) {
        if (StringUtils.isEmpty(PassportPreference.get().getCookie(context))) {
            return false;
        }
        this.mPPreference.clear(context);
        LoginPingbackUtils.getInstance().logOut(s1, lgttype);
        LoginCallbackRecorder.get().notifyLogout(null);
        AccountAdsHelper.clearSkipAd(context);
        return true;
    }

    void saveVipInfo(User user) {
        LogUtils.m1570d(LOG_TAG, "--- setVipUser---", ",getUserType = ", user.getUserType());
        Context context = AppRuntimeEnv.get().getApplicationContext();
        UserType userType = user.getUserType();
        if (userType != null) {
            this.mPPreference.setExpired(context, userType.isExpire());
            this.mPPreference.setUserType(context, userType);
        }
        if (user.userinfo != null) {
            this.mPPreference.setUID(context, user.userinfo.uid);
            TVApiBase.getTVApiProperty().setUid(user.userinfo.uid);
        }
        GetInterfaceTools.getIGalaAccountManager().setAccountType();
        GalaVipInfo vipInfo = user.getGalaVipInfo();
        if (vipInfo != null) {
            DeadLine deadLine = vipInfo.getDeadLine();
            if (deadLine != null) {
                this.mPPreference.setVipDate(context, deadLine.date);
                this.mPPreference.setVipTimeStamp(context, deadLine.f1331t);
                LogUtils.m1570d(LOG_TAG, ">>>>> vip deadLine: {", deadLine.f1331t, ", ", deadLine.date, "}");
            }
        }
    }

    void updateUserTypeForPlayer(String json) {
        try {
            LogUtils.m1570d(LOG_TAG, ">>>>>updateUserTypeForPlayer json: ", json);
            Context context = AppRuntimeEnv.get().getApplicationContext();
            JSONObject obj = new JSONObject(json);
            boolean litchi = obj.getBoolean("litchi");
            boolean isLitchiOverdue = obj.getBoolean("isLitchiOverdue");
            boolean platinum = obj.getBoolean("platinum");
            boolean gold = obj.getBoolean(LoginConstant.CLICK_RESEAT_GETGOLD);
            boolean silver = obj.getBoolean("silver");
            boolean member = obj.getBoolean("member");
            boolean expire = obj.getBoolean("expire");
            UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
            if (userType == null) {
                userType = new UserType();
            }
            userType.setLitchi(litchi);
            userType.setLitchiOverdue(isLitchiOverdue);
            userType.setPlatinum(platinum);
            userType.setGold(gold);
            userType.setSilver(silver);
            userType.setMember(member);
            userType.setExpire(expire);
            this.mPPreference.setUserType(context, userType);
            GetInterfaceTools.getIGalaAccountManager().setAccountType();
        } catch (JSONException e) {
            LogUtils.m1573e(LOG_TAG, ">>>>>updateUserTypeForPlayer json parse error: ", json);
        }
    }

    private void saveUserInfo(String cookie, String uid, String account, String name, String phone) {
        TVApiBase.getTVApiProperty().setUid(uid);
        this.mLocal.saveUserInfo(cookie, uid, account, name, phone);
    }

    private UserResponseBean loginByScan() {
        UserResponseBean responseBean = updateUserInfo();
        if (responseBean != null) {
            if (responseBean.getRespResult()) {
                LoginCallbackRecorder.get().notifyLogin(responseBean.getUid());
                if (!StringUtils.isEmpty(responseBean.getCookie())) {
                    checkVipAccount(responseBean.getCookie());
                }
            } else {
                LogUtils.m1571e(LOG_TAG, ">>>>> GetInterfaceTools.getIGalaAccountManager().updateUserInfo --- return onException");
            }
        }
        return responseBean;
    }

    private void checkVipAccount(final String cookie) {
        UserHelper.checkVipAccount.callSync(new IVrsCallback<ApiResultKeepaliveInterval>() {
            public void onSuccess(ApiResultKeepaliveInterval result) {
                LogUtils.m1570d(GalaAccountCloud.LOG_TAG, ">>>>>UserHelper.checkVipAccount.callSync---onSuccess, cookie:", cookie);
                AccountAdsHelper.handelCheckVipAccount(result);
            }

            public void onException(ApiException exception) {
                LogUtils.m1573e(GalaAccountCloud.LOG_TAG, ">>>>>UserHelper.checkVipAccount.callSync---onException, code:", exception.getCode());
                LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "UserHelper.checkVipAccount", exception);
                AccountAdsHelper.handelCheckVipAccountException(exception);
            }
        }, cookie);
    }

    private void onAccountLoginSuccess(ApiResultUserInfo userInfo, UserResponseBean respBean, ILoginCallback callback) {
        if (userInfo != null) {
            User user = userInfo.getUser();
            if (user != null) {
                respBean.setCookie(user.authcookie);
                respBean.setUserType(user.getUserType());
                respBean.setInsecureAccount(user.isInsecureAccount());
                CommonUserInfo info = user.userinfo;
                if (info != null) {
                    respBean.setAccount(info.user_name);
                    respBean.setNickName(info.nickname);
                    respBean.setPhone(info.phone);
                    respBean.setUid(info.uid);
                }
            }
            saveUserInfo(respBean.getCookie(), respBean.getUid(), respBean.getAccount(), respBean.getNickName(), respBean.getPhone());
            AccountLoginHelper.mergeHistoryAndCollect(respBean.getCookie());
            updateUserInfo();
            LoginCallbackRecorder.get().notifyLogin(respBean.getUid());
            if (!StringUtils.isEmpty(respBean.getCookie())) {
                checkVipAccount(respBean.getCookie());
            }
        }
        UserInfoBean bean = new UserInfoBean();
        bean.setCookie(respBean.getCookie());
        bean.setAccount(respBean.getAccount());
        bean.setName(respBean.getNickName());
        bean.setPhone(respBean.getPhone());
        callback.onLoginSuccess(bean);
    }

    void renewCookie(final IVrsCallback<ApiResultData> callback) {
        final Context context = AppRuntimeEnv.get().getApplicationContext();
        try {
            final String evninfo = FingerPrintManager.getInstance().getEnvInfo(context);
            FingerPrintManager.getInstance().getFingerPrint(context, new FingerPrintCallBack() {

                class C17401 implements IVrsCallback<ApiResultData> {
                    C17401() {
                    }

                    public void onSuccess(ApiResultData arg0) {
                        LogUtils.m1568d(GalaAccountCloud.LOG_TAG, "renewcookie=" + arg0.data);
                        GalaAccountCloud.this.mLocal.saveAuthCookie(arg0.data, context);
                    }

                    public void onException(ApiException arg0) {
                        callback.onException(arg0);
                    }
                }

                public void onSuccess(String fingerPrint) {
                    LogUtils.m1568d(GalaAccountCloud.LOG_TAG, "FingerPrintManager:success, length = " + fingerPrint.length() + " " + fingerPrint);
                    PassportTVHelper.renew_authcookie.call(new C17401(), GalaAccountCloud.this.mLocal.getAuthCookie(), fingerPrint, evninfo);
                }

                public void onFailed(String message) {
                    LogUtils.m1568d(GalaAccountCloud.LOG_TAG, "FingerPrintManager:failure, " + message);
                }
            });
        } catch (Throwable e) {
            String abi;
            if (VERSION.SDK_INT >= 21) {
                abi = Arrays.toString(Build.SUPPORTED_ABIS);
            } else {
                abi = Arrays.toString(new String[]{Build.CPU_ABI, Build.CPU_ABI2});
            }
            PingBackParams pingBackParams = new PingBackParams();
            pingBackParams.add("fingerprintcrash", abi);
            PingBack.getInstance().postPingBackToLongYuan(pingBackParams.build());
            LogUtils.m1568d(LOG_TAG, "FingerPrintManager:FingerPrintException, " + e.getMessage());
            e.printStackTrace();
        }
    }
}
