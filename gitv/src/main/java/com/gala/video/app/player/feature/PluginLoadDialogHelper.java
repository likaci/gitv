package com.gala.video.app.player.feature;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.overlay.PluginLoadingDialog;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.project.Project;

public class PluginLoadDialogHelper {
    private static final String TAG = "Player/PluginLoadDialogHelper";
    private Context mContext;
    private String mErrorType = PluginStateChangedListener.ERROR_TYPE_DEFAULT;
    private String mEventId;
    private IFeedbackDialogController mFeedBackController;
    private GlobalDialog mLoadFailDialog;
    private PluginLoadingDialog mLoadingDialog;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private OnCancelListener mOnLoadingCancelListener;
    private OnCancelListener mOnRetryCancelListener;
    private OnClickListener mRetryClickListener;

    private class MyOnKeyListener implements OnKeyListener {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (event.getAction() == 0) {
                switch (keyCode) {
                    case 4:
                        dialog.dismiss();
                        if (dialog.equals(PluginLoadDialogHelper.this.mLoadingDialog) && PluginLoadDialogHelper.this.mOnLoadingCancelListener != null) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(PluginLoadDialogHelper.TAG, "loading onCancel!!");
                            }
                            PluginLoadDialogHelper.this.mOnLoadingCancelListener.onCancel(dialog);
                            return true;
                        } else if (!dialog.equals(PluginLoadDialogHelper.this.mLoadFailDialog) || PluginLoadDialogHelper.this.mOnRetryCancelListener == null) {
                            return true;
                        } else {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(PluginLoadDialogHelper.TAG, "Retry onCancel!!");
                            }
                            PluginLoadDialogHelper.this.mOnRetryCancelListener.onCancel(dialog);
                            return true;
                        }
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 82:
                        return true;
                }
            }
            return false;
        }
    }

    public PluginLoadDialogHelper(Context context) {
        this.mContext = context;
        this.mFeedBackController = CreateInterfaceTools.createFeedbackDialogController();
        this.mFeedBackController.init(context, null);
    }

    public void setRetryClickListener(OnClickListener listener) {
        this.mRetryClickListener = listener;
    }

    public void setRetryCancelListener(OnCancelListener listener) {
        this.mOnRetryCancelListener = listener;
    }

    public void setLoadingCancelListener(OnCancelListener listener) {
        this.mOnLoadingCancelListener = listener;
    }

    public boolean equalContext(Context target) {
        if (this.mContext != null) {
            return this.mContext.equals(target);
        }
        return target == null;
    }

    public void showLoadingDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> showLoadingDialog() context=" + this.mContext);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                if (PluginLoadDialogHelper.this.mLoadingDialog == null || !PluginLoadDialogHelper.this.mLoadingDialog.isShowing()) {
                    PluginLoadDialogHelper.this.mLoadingDialog = new PluginLoadingDialog(PluginLoadDialogHelper.this.mContext, R.style.plugindialog);
                    PluginLoadDialogHelper.this.mLoadingDialog.setOnKeyListener(new MyOnKeyListener());
                    PluginLoadDialogHelper.this.mLoadingDialog.show();
                    LayoutParams lp = new LayoutParams();
                    lp.copyFrom(PluginLoadDialogHelper.this.mLoadingDialog.getWindow().getAttributes());
                    lp.width = -1;
                    lp.height = -1;
                    if (PluginLoadDialogHelper.this.mLoadingDialog != null && PluginLoadDialogHelper.this.mLoadingDialog.isShowing()) {
                        PluginLoadDialogHelper.this.mLoadingDialog.getWindow().setAttributes(lp);
                    }
                }
            }
        };
        if (ThreadUtils.isUIThread()) {
            runnable.run();
        } else {
            this.mMainHandler.post(runnable);
        }
    }

    public void dissmissLoadingDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> dissmissLoadingDialog() context=" + this.mContext);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                if (PluginLoadDialogHelper.this.mLoadingDialog != null && PluginLoadDialogHelper.this.mLoadingDialog.isShowing()) {
                    PluginLoadDialogHelper.this.mLoadingDialog.dismiss();
                }
            }
        };
        if (ThreadUtils.isUIThread()) {
            runnable.run();
        } else {
            this.mMainHandler.post(runnable);
        }
    }

    public boolean isLoadingDialogShow() {
        boolean isShow = this.mLoadingDialog != null ? this.mLoadingDialog.isShowing() : false;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> isLoadingDialogShow() context=" + this.mContext + " return " + isShow);
        }
        return isShow;
    }

    public void showFailedDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> showFailedDialog() context=" + this.mContext);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                if (PluginLoadDialogHelper.this.mLoadFailDialog == null || !PluginLoadDialogHelper.this.mLoadFailDialog.isShowing()) {
                    PluginLoadDialogHelper.this.mLoadFailDialog = Project.getInstance().getControl().getGlobalDialog(PluginLoadDialogHelper.this.mContext);
                    String msg = PluginLoadDialogHelper.this.mContext.getResources().getString(R.string.player_plugin_loaded_failed);
                    String retry = PluginLoadDialogHelper.this.mContext.getResources().getString(R.string.player_plugin_retry_load);
                    PluginLoadDialogHelper.this.mLoadFailDialog.setOnKeyListener(new MyOnKeyListener());
                    PluginLoadDialogHelper.this.mLoadFailDialog.setParams(msg, retry, new OnClickListener() {
                        public void onClick(View v) {
                            PluginLoadDialogHelper.this.mLoadFailDialog.dismiss();
                            if (PluginLoadDialogHelper.this.mRetryClickListener != null) {
                                PluginLoadDialogHelper.this.mRetryClickListener.onClick(v);
                            }
                        }
                    });
                    PluginLoadDialogHelper.this.mLoadFailDialog.show();
                }
            }
        };
        if (ThreadUtils.isUIThread()) {
            runnable.run();
        } else {
            this.mMainHandler.post(runnable);
        }
    }

    public void dissmissFailedDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> dissmissFailedDialog() context=" + this.mContext);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                if (PluginLoadDialogHelper.this.mLoadFailDialog != null && PluginLoadDialogHelper.this.mLoadFailDialog.isShowing()) {
                    PluginLoadDialogHelper.this.mLoadFailDialog.dismiss();
                }
            }
        };
        if (ThreadUtils.isUIThread()) {
            runnable.run();
        } else {
            this.mMainHandler.post(runnable);
        }
    }

    public void showFeedBackDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> showFeedBackDialog() context=" + this.mContext);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                String errorMsg;
                String retry = PluginLoadDialogHelper.this.mContext.getResources().getString(R.string.player_plugin_still_retry_load);
                PluginLoadDialogHelper.this.mFeedBackController.clearCurrentDialog();
                PluginLoadDialogHelper.this.mFeedBackController.setEventID(PluginLoadDialogHelper.this.mEventId);
                FeedBackModel model = new FeedBackModel();
                if (StringUtils.equals(PluginLoadDialogHelper.this.mErrorType, PluginStateChangedListener.ERROR_TYPE_NO_SPACE)) {
                    errorMsg = PluginLoadDialogHelper.this.mContext.getResources().getString(R.string.player_plugin_loaded_failed_no_space);
                } else {
                    errorMsg = PluginLoadDialogHelper.this.mContext.getResources().getString(R.string.player_plugin_loaded_failed_over_three);
                }
                model.setErrorMsg(errorMsg);
                PluginLoadDialogHelper.this.mFeedBackController.showQRDialog(model, null, null, null, retry, new OnClickListener() {
                    public void onClick(View v) {
                        PluginLoadDialogHelper.this.mFeedBackController.clearCurrentDialog();
                        if (PluginLoadDialogHelper.this.mRetryClickListener != null) {
                            PluginLoadDialogHelper.this.mRetryClickListener.onClick(v);
                        }
                    }
                });
            }
        };
        if (ThreadUtils.isUIThread()) {
            runnable.run();
        } else {
            this.mMainHandler.post(runnable);
        }
    }

    public void setErrorType(String errorType) {
        this.mErrorType = errorType;
    }

    public void setEventId(String eventId) {
        this.mEventId = eventId;
    }

    public void dissmissFeedbackDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> dissmissFeedbackDialog() context=" + this.mContext);
        }
        Runnable runnable = new Runnable() {
            public void run() {
                PluginLoadDialogHelper.this.mFeedBackController.clearCurrentDialog();
            }
        };
        if (ThreadUtils.isUIThread()) {
            runnable.run();
        } else {
            this.mMainHandler.post(runnable);
        }
    }
}
