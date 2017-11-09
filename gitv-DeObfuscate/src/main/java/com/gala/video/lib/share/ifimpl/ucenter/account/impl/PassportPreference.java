package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import android.content.Context;
import com.gala.tvapi.type.UserType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreferenceProvider;

public class PassportPreference {
    private static final String COOKIE = "cookie";
    private static final String EXPIRED = "expired";
    private static final String HU = "hu";
    private static final String LOGINDB = "logindb";
    private static final String LOG_TAG = "EPG/system/PassportPreference";
    private static final String PHONE = "phone";
    private static final String TIMESTAMP = "timestamp";
    private static final String UID = "uid";
    private static final String USERACCOUNT = "useraccount";
    private static final String USERNAME = "username";
    private static final String USERTYPE = "usertype";
    private static final String VIPDATE = "vipdate";
    private static PassportPreference mPPreference;
    private static AppPreferenceProvider mPreference;

    private static AppPreferenceProvider getPreferenceInstance(Context ctx) {
        if (mPreference == null) {
            mPreference = AppPreferenceProvider.get(ctx, LOGINDB, Project.getInstance().getBuild().supportPlayerMultiProcess());
        }
        return mPreference;
    }

    static PassportPreference get() {
        if (mPPreference == null) {
            mPPreference = new PassportPreference();
        }
        return mPPreference;
    }

    void setUserName(Context ctx, String name) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save(USERNAME, name);
    }

    String getUserName(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.get(USERNAME);
    }

    void setUserAccount(Context ctx, String account) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save(USERACCOUNT, account);
    }

    String getUserAccount(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.get(USERACCOUNT);
    }

    void setExpired(Context ctx, boolean isVip) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save(EXPIRED, isVip);
    }

    boolean getExpired(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.getBoolean(EXPIRED, false);
    }

    void setCookie(Context ctx, String cookie) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save("cookie", cookie);
    }

    String getCookie(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.get("cookie");
    }

    public void setUID(Context ctx, String uid) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save("uid", uid);
    }

    String getUID(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.get("uid");
    }

    void clear(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.clear();
    }

    void setUserType(Context ctx, UserType userType) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save("usertype", userType.toJsonString());
    }

    public UserType getUserType(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        String json = "";
        CharSequence json2;
        try {
            json2 = mPreference.get("usertype");
            LogUtils.m1570d(LOG_TAG, ">>>>>UserType-json:", json2);
            if (StringUtils.isEmpty(json2)) {
                return null;
            }
            return UserType.parseString(json2);
        } catch (Exception e1) {
            LogUtils.m1571e(LOG_TAG, ">>>>>> mPreference.getString(USERTYPE) exception");
            e1.printStackTrace();
            try {
                int type = mPreference.getInt("usertype", -1);
                UserType userType = new UserType();
                userType.setExpire(false);
                userType.setGold(false);
                userType.setLitchi(false);
                userType.setMember(false);
                userType.setPhoneMonth(false);
                userType.setPlatinum(false);
                userType.setSilver(false);
                switch (type) {
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
                setUserType(ctx, userType);
                json2 = userType.toJsonString();
                if (StringUtils.isEmpty(json2)) {
                    return null;
                }
                return UserType.parseString(json2);
            } catch (Exception e12) {
                LogUtils.m1571e(LOG_TAG, ">>>>>> mPreference.getString(USERTYPE) exception");
                clear(ctx);
                e12.printStackTrace();
                return null;
            }
        }
    }

    void setVipDate(Context ctx, String date) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save(VIPDATE, date);
    }

    String getVipDate(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.get(VIPDATE);
    }

    String getDefaultId(Context ctx) {
        return AppRuntimeEnv.get().getDefaultUserId();
    }

    void setVipTimeStamp(Context ctx, String date) {
        long result = Long.parseLong(date);
        mPreference = getPreferenceInstance(ctx);
        mPreference.save(TIMESTAMP, result);
    }

    long getVipTimeStamp(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.getLong(TIMESTAMP, -1);
    }

    String getUserPhone(Context context) {
        mPreference = getPreferenceInstance(context);
        return mPreference.get("phone");
    }

    void setUserPhone(Context ctx, String phone) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save("phone", phone);
    }

    void setHu(Context ctx, String hu) {
        mPreference = getPreferenceInstance(ctx);
        mPreference.save(HU, hu);
    }

    String getHu(Context ctx) {
        mPreference = getPreferenceInstance(ctx);
        return mPreference.get(HU);
    }
}
