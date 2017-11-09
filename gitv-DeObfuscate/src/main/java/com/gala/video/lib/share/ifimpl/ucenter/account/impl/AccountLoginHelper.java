package com.gala.video.lib.share.ifimpl.ucenter.account.impl;

import android.content.Context;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.project.Project;

public class AccountLoginHelper {
    private static final String[][] CUSTOMER_LIMITCODE_SET;
    private static final String LOG_TAG = "AccountLoginHelper";
    private static IVrsCallback<ApiResultCode> iVrsFavouriteCallback = new C17322();
    private static IVrsCallback<ApiResultCode> iVrsHistoryCallback = new C17311();

    static class C17311 implements IVrsCallback<ApiResultCode> {
        C17311() {
        }

        public void onSuccess(ApiResultCode arg0) {
            AccountLoginHelper.onMergeData(true, true);
        }

        public void onException(ApiException exception) {
            LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "UserHelper.mergeHistory", exception);
            AccountLoginHelper.onMergeData(true, false);
        }
    }

    static class C17322 implements IVrsCallback<ApiResultCode> {
        C17322() {
        }

        public void onSuccess(ApiResultCode arg0) {
            AccountLoginHelper.onMergeData(false, true);
        }

        public void onException(ApiException exception) {
            LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "UserHelper.mergeCollects", exception);
            AccountLoginHelper.onMergeData(false, false);
        }
    }

    public static String getCommErrorTip(ApiException exception) {
        String tip = AppRuntimeEnv.get().getApplicationContext().getResources().getString(C1632R.string.error_comm_tip);
        if (exception == null || StringUtils.isEmpty(exception.getCode())) {
            return tip;
        }
        return tip + "(" + exception.getCode() + ")";
    }

    public static void hideAndRestartScreenSaver(Context context) {
        if (Project.getInstance().getBuild().isHomeVersion()) {
            GetInterfaceTools.getIScreenSaver().exitHomeVersionScreenSaver(context);
            return;
        }
        IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
        if (iOperate.isShowScreenSaver()) {
            iOperate.hideScreenSaver();
        }
        iOperate.reStart();
    }

    static void mergeHistoryAndCollect(String authCookie) {
        UserHelper.mergeHistory.call(iVrsHistoryCallback, authCookie, AppRuntimeEnv.get().getDefaultUserId());
        UserHelper.mergeCollects.call(iVrsFavouriteCallback, authCookie, AppRuntimeEnv.get().getDefaultUserId());
        LogUtils.m1570d(LOG_TAG, "saveUserInfo 【authCookie = ", authCookie, "】");
    }

    static {
        String[][] strArr = new String[1][];
        strArr[0] = new String[]{"ark.**guo", "F001"};
        CUSTOMER_LIMITCODE_SET = strArr;
    }

    private static void onMergeData(boolean isHistory, boolean isSuccess) {
        if (isHistory && isSuccess) {
            LogUtils.m1568d(LOG_TAG, "merge play history success");
            GetInterfaceTools.getIHistoryCacheManager().mergeDeviceAndCloudHistory();
        }
    }

    public static String getUserPhone(String phone) {
        if (StringUtils.isEmpty((CharSequence) phone) || phone.length() != 11) {
            return "";
        }
        String pre = phone.substring(0, 3);
        return pre + "****" + phone.substring(phone.length() - 4);
    }

    static String getLimitCodeByCustomerName() {
        String mCustomerName = Project.getInstance().getBuild().getCustomerName();
        for (int i = 0; i < CUSTOMER_LIMITCODE_SET.length; i++) {
            if (CUSTOMER_LIMITCODE_SET[i][0].equals(mCustomerName)) {
                LogUtils.m1568d(LOG_TAG, "mCustomName=" + mCustomerName);
                LogUtils.m1568d(LOG_TAG, "limitcode=" + CUSTOMER_LIMITCODE_SET[i][1]);
                return CUSTOMER_LIMITCODE_SET[i][1];
            }
        }
        return "";
    }
}
