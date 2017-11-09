package com.gala.video.app.epg.feedback;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class FeedBackActivationStateTimer extends Timer {
    private static final String LOG_TAG = "FeedBackActivationStateTimer";
    private static final String[] TIMEINTERVAL = new String[]{"0", "1", "3", "5", "20", "40", "60", "120", "240"};
    private static FeedBackActivationStateTimer mInstance;
    private int count = 0;
    private boolean isRunning = false;
    private final OnNetStateChangedListener mNetStateChangedListener = new OnNetStateChangedListener() {
        public void onStateChanged(int oldState, int newState) {
            LogUtils.e(FeedBackActivationStateTimer.LOG_TAG, "network state changed net state = " + newState);
            switch (newState) {
                case 1:
                case 2:
                    FeedBackActivationStateTimer.this.startFeedbackTask();
                    NetWorkManager.getInstance().unRegisterStateChangedListener(FeedBackActivationStateTimer.this.mNetStateChangedListener);
                    return;
                default:
                    return;
            }
        }
    };
    private int minutes = 0;
    private int retryTimes = 7;

    class FeedBackTimerTask extends TimerTask {
        FeedBackTimerTask() {
        }

        public void run() {
            FeedBackActivationStateTimer.this.isRunning = true;
            LogUtils.d(FeedBackActivationStateTimer.LOG_TAG, "timerTask-->minutes= " + FeedBackActivationStateTimer.this.minutes + " count=" + FeedBackActivationStateTimer.this.count);
            if (FeedBackActivationStateTimer.this.count < FeedBackActivationStateTimer.this.retryTimes && Arrays.asList(FeedBackActivationStateTimer.TIMEINTERVAL).contains(String.valueOf(FeedBackActivationStateTimer.this.minutes))) {
                FeedBackActivationStateTimer.this.count = FeedBackActivationStateTimer.this.count + 1;
                TVApi.feedbackState.call(new IApiCallback<ApiResultCode>() {
                    public void onSuccess(ApiResultCode apiResult) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.e(FeedBackActivationStateTimer.LOG_TAG, "feedbackState onSuccess result=" + apiResult.getCode());
                        }
                        if ("N000000".equals(apiResult.code)) {
                            GetInterfaceTools.getIGalaVipManager().setActivationFeedbackState(1);
                            FeedBackActivationStateTimer.this.cancel();
                        }
                    }

                    public void onException(ApiException e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.e(FeedBackActivationStateTimer.LOG_TAG, "feedbackState onException e=" + e);
                        }
                        NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                            public void getStateResult(int state) {
                                LogUtils.e(FeedBackActivationStateTimer.LOG_TAG, "onNetworkState---" + state);
                                switch (state) {
                                    case 0:
                                    case 3:
                                    case 4:
                                        FeedBackTimerTask.this.cancel();
                                        FeedBackActivationStateTimer.this.isRunning = false;
                                        FeedBackActivationStateTimer.this.minutes = 0;
                                        NetWorkManager.getInstance().registerStateChangedListener(FeedBackActivationStateTimer.this.mNetStateChangedListener);
                                        return;
                                    default:
                                        return;
                                }
                            }
                        });
                    }
                }, GetInterfaceTools.getIGalaVipManager().getActivationAccount(), String.valueOf(FeedBackActivationStateTimer.this.count));
            } else if (FeedBackActivationStateTimer.this.count >= FeedBackActivationStateTimer.this.retryTimes) {
                FeedBackActivationStateTimer.this.cancel();
            }
            FeedBackActivationStateTimer.this.minutes = FeedBackActivationStateTimer.this.minutes + 1;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public int getRetryTimes() {
        return this.retryTimes;
    }

    private FeedBackActivationStateTimer() {
    }

    public static FeedBackActivationStateTimer get() {
        if (mInstance == null) {
            mInstance = new FeedBackActivationStateTimer();
        }
        return mInstance;
    }

    public void startFeedbackTask() {
        schedule(new FeedBackTimerTask(), 500, 60000);
    }

    public void cancel() {
        super.cancel();
        super.purge();
        this.isRunning = false;
        this.count = 0;
        this.minutes = 0;
        mInstance = null;
    }
}
