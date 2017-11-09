package com.gala.video.lib.share.ifimpl.ucenter.account.vipRight;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.system.preference.AppPreference;

public class VipRightsPreference {
    private static final String ACCOUNT_ACTIVATION_STATE = "account_activation_state";
    private static final String ACTIVATION_ACCOUNT = "activation_account";
    private static final String ACTIVATION_FEEDBACK_STATE = "activation_feedback_state";
    private static final String NAME = "vip_rights_activate_pref";
    private static final String TAG = "system/VipRightsActivatePreference";
    private static VipRightsPreference mInstance;

    public static VipRightsPreference get() {
        if (mInstance == null) {
            mInstance = new VipRightsPreference();
        }
        return mInstance;
    }

    int getAccountActivationState(Context context) {
        int value = new AppPreference(context, NAME).getInt(ACCOUNT_ACTIVATION_STATE, 0);
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "getAccountActivationState()=" + value);
        }
        return value;
    }

    void setAccountActivationState(Context context, int state) {
        AppPreference preference = new AppPreference(context, NAME);
        preference.save(ACCOUNT_ACTIVATION_STATE, state);
        String account = GetInterfaceTools.getIGalaAccountManager().getUserAccount();
        if (account.length() > 50) {
            account = account.substring(0, 50);
        }
        preference.save(ACTIVATION_ACCOUNT, account);
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "setAccountActivationState() state = " + state + ", account = " + account);
        }
    }

    int getActivationFeedbackState(Context context) {
        int value = new AppPreference(context, NAME).getInt(ACTIVATION_FEEDBACK_STATE, -1);
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "getActivationFeedbackState()=" + value);
        }
        return value;
    }

    void setActivationFeedbackState(Context context, int state) {
        new AppPreference(context, NAME).save(ACTIVATION_FEEDBACK_STATE, state);
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "setActivationFeedbackState() = " + state);
        }
    }

    String getActivationAccount(Context context) {
        String account = new AppPreference(context, NAME).get(ACTIVATION_ACCOUNT, "");
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "getActivationAccount() = " + account);
        }
        return account;
    }
}
