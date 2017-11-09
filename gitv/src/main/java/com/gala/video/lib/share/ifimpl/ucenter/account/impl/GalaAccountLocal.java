package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import android.content.Context;
import com.gala.tvapi.type.UserType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;

class GalaAccountLocal {
    private static final String LOG_TAG = "GalaAccountLocal";
    private PassportPreference mPPreference = PassportPreference.get();

    GalaAccountLocal() {
    }

    boolean isLogin(Context context) {
        CharSequence cookie = this.mPPreference.getCookie(context);
        CharSequence name = this.mPPreference.getUserName(context);
        CharSequence account = this.mPPreference.getUserAccount(context);
        if (StringUtils.isEmpty(cookie) || StringUtils.isEmpty(name) || StringUtils.isEmpty(account)) {
            return false;
        }
        return true;
    }

    String getAuthCookie() {
        return this.mPPreference.getCookie(AppRuntimeEnv.get().getApplicationContext());
    }

    void saveAuthCookie(String cookie, Context context) {
        this.mPPreference.setCookie(context, cookie);
    }

    UserType getUserType() {
        return this.mPPreference.getUserType(AppRuntimeEnv.get().getApplicationContext());
    }

    long getVipTimeStamp() {
        return this.mPPreference.getVipTimeStamp(AppRuntimeEnv.get().getApplicationContext());
    }

    String getVipDate() {
        return this.mPPreference.getVipDate(AppRuntimeEnv.get().getApplicationContext());
    }

    String getUID() {
        return this.mPPreference.getUID(AppRuntimeEnv.get().getApplicationContext());
    }

    boolean getExpired() {
        return this.mPPreference.getExpired(AppRuntimeEnv.get().getApplicationContext());
    }

    String getUserAccount() {
        return this.mPPreference.getUserAccount(AppRuntimeEnv.get().getApplicationContext());
    }

    String getUserName() {
        return this.mPPreference.getUserName(AppRuntimeEnv.get().getApplicationContext());
    }

    void setAccountType() {
        Context ctx = AppRuntimeEnv.get().getApplicationContext();
        String type = "";
        if (!StringUtils.isEmpty(this.mPPreference.getCookie(ctx))) {
            UserType userType = this.mPPreference.getUserType(ctx);
            if (userType == null) {
                LogUtils.e(LOG_TAG, ">>>>>PassportPreference.getUserType() ---- null!!!");
                return;
            } else if (userType.isLitchi()) {
                if (userType.isGold() && !userType.isExpire()) {
                    type = "5,3";
                } else if (userType.isPlatinum() && !userType.isExpire()) {
                    type = "5,4";
                } else if (!userType.isSilver() || userType.isExpire()) {
                    type = "5";
                } else {
                    type = "5,2";
                }
            } else if (userType.isExpire()) {
                type = "0";
            } else if (userType.isGold()) {
                type = "3";
            } else if (userType.isPlatinum()) {
                type = "4";
            } else if (userType.isSilver()) {
                type = "2";
            } else {
                type = "-1";
            }
        }
        LogUtils.d(LOG_TAG, "GalaPingBack.get().setAccountType(type) -----", type);
        this.mPPreference.setHu(ctx, type);
    }

    String getHu() {
        return this.mPPreference.getHu(AppRuntimeEnv.get().getApplicationContext());
    }

    boolean getIsLitchiVipForH5() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType != null) {
            return userType.isLitchi();
        }
        return false;
    }

    int getUserTypeForH5() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType == null) {
            if (GetInterfaceTools.getIGalaAccountManager().getAuthCookie().isEmpty()) {
                return -1;
            }
            return 0;
        } else if (userType.isExpire()) {
            return 1;
        } else {
            if (userType.isPlatinum()) {
                return 5;
            }
            if (userType.isGold()) {
                return 3;
            }
            if (userType.isSilver()) {
                return 4;
            }
            if (userType.isPhoneMonth()) {
                return 6;
            }
            return userType.isMember() ? 0 : 0;
        }
    }

    boolean isVip() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType == null || (!userType.isPlatinum() && !userType.isLitchi())) {
            return false;
        }
        return true;
    }

    String getUserPhone() {
        return this.mPPreference.getUserPhone(AppRuntimeEnv.get().getApplicationContext());
    }

    void saveUserInfo(String cookie, String uid, String account, String name, String phone) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        this.mPPreference.setCookie(context, cookie);
        this.mPPreference.setUserName(context, name);
        this.mPPreference.setUserPhone(context, phone);
        this.mPPreference.setUserAccount(context, account);
        this.mPPreference.setUID(context, uid);
    }

    void saveAccountInfoForH5(LoginParam4H5 param) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        this.mPPreference.setCookie(context, param.cookie);
        this.mPPreference.setUID(context, param.uid);
        this.mPPreference.setUserAccount(context, param.account);
        this.mPPreference.setUserName(context, param.nickName);
        saveUserTypeForH5(param.userTypeH5, param.isLitchiH5);
        setAccountType();
    }

    void saveUserTypeForH5(int userTypeParam, boolean isLitchiVip) {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType == null) {
            userType = new UserType();
        }
        LogUtils.d(LOG_TAG, ">>>>>H5 return - isLitchi:", Boolean.valueOf(isLitchiVip), "---return userType:", Integer.valueOf(userTypeParam));
        userType.setLitchi(isLitchiVip);
        switch (userTypeParam) {
            case 0:
                userType.setExpire(false);
                userType.setGold(false);
                userType.setMember(false);
                userType.setPhoneMonth(false);
                userType.setPlatinum(false);
                userType.setSilver(false);
                break;
            case 1:
                userType.setExpire(true);
                break;
            case 3:
                userType.setGold(true);
                break;
            case 4:
                userType.setSilver(true);
                break;
            case 5:
                userType.setPlatinum(true);
                break;
            case 6:
                userType.setPhoneMonth(true);
                break;
        }
        this.mPPreference.setUserType(context, userType);
    }
}
