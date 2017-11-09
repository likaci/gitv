package com.gala.video.app.player.utils;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.common.widget.QToast.QToastStatusListener;

public class PlayerToastHelper {
    private static final String TAG = "Player/Utils/PlayerToastHelper";
    private static boolean sEnableShow = true;
    private static boolean sIsTimedToastShown = false;

    static class C15781 implements QToastStatusListener {
        C15781() {
        }

        public void onShow() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerToastHelper.TAG, "onPlayerToastVisibilityChangeListener.onShow()");
            }
            PlayerToastHelper.sIsTimedToastShown = true;
        }

        public void onHide() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerToastHelper.TAG, "onPlayerToastVisibilityChangeListener.onHide()");
            }
            PlayerToastHelper.sIsTimedToastShown = false;
            QToast.unregisterStatusListener();
        }
    }

    private static void showToast(Context context, String msg, int duration, boolean cancelPrev) {
        if (sEnableShow) {
            QToast.makeTextAndShow(context, (CharSequence) msg, duration);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "showToast(), Toast enabled = " + sEnableShow + ", return.");
        }
    }

    public static void showToast(Context context, String msg, int duration) {
        showToast(context, msg, duration, true);
    }

    public static void showToast(Context context, int msgResId, int duration) {
        showToast(context, context.getResources().getString(msgResId), duration, true);
    }

    public static void showNetDiagnoseToast(Context context, String msg, int duration) {
        if (sEnableShow) {
            QToast.registerStatusListener(new C15781());
            QToast.makeTextAndShow(context, (CharSequence) msg, duration);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "showTimedToast(), Toast enabled = " + sEnableShow + ", return.");
        }
    }

    public static void hidePlayerToast() {
        QToast.hidePreToast();
    }

    public static boolean isLagToastShown() {
        return sIsTimedToastShown;
    }

    public static void setToastEnabled(boolean isEnabled) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setToastEnabled = " + isEnabled);
        }
        sEnableShow = isEnabled;
    }
}
