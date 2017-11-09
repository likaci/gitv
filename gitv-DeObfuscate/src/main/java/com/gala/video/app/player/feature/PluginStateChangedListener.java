package com.gala.video.app.player.feature;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import java.util.concurrent.atomic.AtomicBoolean;

public class PluginStateChangedListener implements OnStateChangedListener {
    public static final String ERROR_TYPE_DEFAULT = "DEFAULT";
    public static final String ERROR_TYPE_NO_SPACE = "ENOSPC";
    private static String TAG = "OnStateChangedListener";
    private Context mActivity;
    private final OnCancelListener mCancelListener = new C14283();
    private AtomicBoolean mCanceled = new AtomicBoolean(false);
    private int mFailCount = 0;
    private Handler mHandler;
    private OnStateChangedListener mListener;
    private PluginLoadDialogHelper mPluginDialogHelper;
    private final OnClickListener mRetryClick = new C14294();

    class C14261 implements Runnable {
        C14261() {
        }

        public void run() {
            PluginStateChangedListener.this.mListener.onFailed();
        }
    }

    class C14272 implements Runnable {
        C14272() {
        }

        public void run() {
            PluginStateChangedListener.this.mListener.onSuccess();
        }
    }

    class C14283 implements OnCancelListener {
        C14283() {
        }

        public void onCancel(DialogInterface dialog) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PluginStateChangedListener.TAG, "loadDialog onCanceled!! mylistener=" + PluginStateChangedListener.this.mListener);
            }
            PluginStateChangedListener.this.mCanceled.set(true);
            if (PluginStateChangedListener.this.mListener != null) {
                PluginStateChangedListener.this.mListener.onCanceled();
            }
        }
    }

    class C14294 implements OnClickListener {
        C14294() {
        }

        public void onClick(View v) {
            GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(PluginStateChangedListener.this.mActivity, PluginStateChangedListener.this.mListener, true);
        }
    }

    public PluginStateChangedListener(Context activity, Looper looper, OnStateChangedListener listener) {
        this.mActivity = activity;
        if (this.mPluginDialogHelper == null || !this.mPluginDialogHelper.equalContext(this.mActivity)) {
            if (this.mPluginDialogHelper != null) {
                this.mPluginDialogHelper.dissmissLoadingDialog();
                this.mPluginDialogHelper.dissmissFailedDialog();
                this.mPluginDialogHelper.dissmissFeedbackDialog();
            }
            this.mPluginDialogHelper = new PluginLoadDialogHelper(this.mActivity);
        }
        if (looper != null) {
            this.mHandler = new Handler(looper);
        }
        this.mListener = listener;
    }

    public void setFailCount(int failCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "failCount" + failCount);
        }
        this.mFailCount = failCount;
    }

    public void setErrorType(String errorType) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "errorType" + errorType);
        }
        this.mPluginDialogHelper.setErrorType(errorType);
    }

    public void setEventId(String eventId) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setEventId" + eventId);
        }
        this.mPluginDialogHelper.setEventId(eventId);
    }

    public void onLoading() {
        this.mPluginDialogHelper.setLoadingCancelListener(this.mCancelListener);
        this.mPluginDialogHelper.showLoadingDialog();
    }

    public void onFailed() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "loadPlayerPluginAsync() onFailed!! canceld=" + this.mCanceled + ", mylistener=" + this + "mFailCount" + this.mFailCount);
        }
        if (!this.mCanceled.get()) {
            this.mPluginDialogHelper.dissmissLoadingDialog();
            this.mPluginDialogHelper.setRetryClickListener(this.mRetryClick);
            this.mPluginDialogHelper.setRetryCancelListener(this.mCancelListener);
            if (this.mFailCount < 3) {
                this.mPluginDialogHelper.dissmissFeedbackDialog();
                this.mPluginDialogHelper.showFailedDialog();
            } else {
                this.mPluginDialogHelper.dissmissFailedDialog();
                this.mPluginDialogHelper.showFeedBackDialog();
            }
            if (this.mHandler != null && this.mListener != null) {
                this.mHandler.post(new C14261());
            } else if (this.mListener != null) {
                this.mListener.onFailed();
            }
        }
    }

    public void onSuccess() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "loadPlayerPluginAsync() onSuccess!! canceld=" + this.mCanceled + ", mylistener=" + this);
        }
        if (!this.mCanceled.get()) {
            this.mPluginDialogHelper.dissmissFailedDialog();
            this.mPluginDialogHelper.dissmissFeedbackDialog();
            if (this.mHandler != null && this.mListener != null) {
                this.mHandler.post(new C14272());
            } else if (this.mListener != null) {
                this.mListener.onSuccess();
            }
        }
    }

    public void onCanceled() {
        if (this.mListener != null) {
            this.mListener.onCanceled();
        }
    }
}
