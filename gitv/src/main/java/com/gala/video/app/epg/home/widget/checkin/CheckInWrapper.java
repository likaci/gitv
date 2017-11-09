package com.gala.video.app.epg.home.widget.checkin;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.gala.tvapi.tv3.ApiException;
import com.gala.tvapi.tv3.IApiCallback;
import com.gala.tvapi.tv3.ITVApi;
import com.gala.tvapi.tv3.result.SignResult;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarItemView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.utils.ResourceUtil;

public class CheckInWrapper {
    private static final String TAG = "CheckInWrapper";
    private ActionBarItemView mCheckInItem;
    private final Handler mHandler;
    private ActionBarItemView mMyChildView;
    private onCheckInListener mOnCheckInListener;
    private IStatusListener mScreenSaverListener;

    public interface onCheckInListener {
        void onError();

        void onSuccess();
    }

    public CheckInWrapper(ActionBarItemView checkInItem) {
        this.mHandler = new Handler(Looper.myLooper()) {
            public void handleMessage(Message msg) {
                CheckInWrapper.this.showCheckInMsg();
            }
        };
        this.mOnCheckInListener = new onCheckInListener() {
            public void onSuccess() {
                LogUtils.i(CheckInWrapper.TAG, "onSuccess mCheckInItem: " + CheckInWrapper.this.mCheckInItem);
                CheckInTimeHelper.saveShowState(true);
                CheckInTimeHelper.savePrefToday();
                CheckInWrapper.this.mHandler.removeMessages(0);
                CheckInWrapper.this.mHandler.sendEmptyMessage(0);
            }

            public void onError() {
                LogUtils.e(CheckInWrapper.TAG, "onError: ");
            }
        };
        this.mScreenSaverListener = new IStatusListener() {
            public void onStart() {
            }

            public void onStop() {
                LogUtils.i(CheckInWrapper.TAG, "onStop mScreenSaverListener: ");
                boolean isEqualDayTime = CheckInTimeHelper.isEqualDayTime();
                LogUtils.i(CheckInWrapper.TAG, "onStop mScreenSaverListener isEqualDayTime: " + isEqualDayTime);
                if (!isEqualDayTime) {
                    CheckInWrapper.this.start();
                }
            }
        };
        LogUtils.i(TAG, "CheckInWrapper checkInItem: " + checkInItem);
        this.mCheckInItem = checkInItem;
    }

    public CheckInWrapper(ActionBarItemView checkInItem, ActionBarItemView myChildView) {
        this(checkInItem);
        this.mMyChildView = myChildView;
    }

    public void start() {
        LogUtils.i(TAG, "start: ");
        CheckInTimeHelper.savePrefToday();
        setOnCheckInListener();
    }

    public void register() {
        LogUtils.i(TAG, "register: ");
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenSaverListener);
    }

    public void unRegister() {
        LogUtils.i(TAG, "unRegister: ");
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenSaverListener);
    }

    private void showCheckInMsg() {
        if (this.mCheckInItem != null) {
            this.mCheckInItem.setMessageVisible(0);
        }
        if (this.mMyChildView != null) {
            this.mMyChildView.setMessageVisible(8);
        }
    }

    private void setOnCheckInListener() {
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(ResourceUtil.getContext());
        LogUtils.i(TAG, "setOnCheckInListener isLogin: " + isLogin);
        if (isLogin) {
            callSyncSign();
        } else {
            this.mOnCheckInListener.onSuccess();
        }
    }

    private void callSyncSign() {
        String cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        String uid = GetInterfaceTools.getIGalaAccountManager().getUID();
        ITVApi.signApi().callAsync(new IApiCallback<SignResult>() {
            public void onSuccess(SignResult apiResult) {
                if (apiResult == null) {
                    LogUtils.e(CheckInWrapper.TAG, "onSuccess apiResult is null ");
                    return;
                }
                int state = apiResult.status;
                int currSignDays = apiResult.currSignDays;
                boolean isReachLimit = apiResult.isReachLimit();
                LogUtils.i(CheckInWrapper.TAG, "onSuccess state: " + state + ",currSignDays:" + currSignDays + ",isReachLimit:" + isReachLimit);
                if (!isReachLimit && state == 0) {
                    CheckInWrapper.this.mOnCheckInListener.onSuccess();
                }
            }

            public void onException(ApiException e) {
                boolean checkE = e != null;
                LogUtils.e(CheckInWrapper.TAG, "onException getCode: " + (checkE ? e.getCode() : ""));
                LogUtils.e(CheckInWrapper.TAG, "onException getException: " + (checkE ? e.getException() : ""));
                LogUtils.e(CheckInWrapper.TAG, "onException getHttpCode: " + (checkE ? Integer.valueOf(e.getHttpCode()) : ""));
            }
        }, cookie, uid, "0");
    }
}
