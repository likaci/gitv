package com.gala.video.app.epg;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.app.epg.home.ads.controller.StartScreenAdHandler;
import com.gala.video.app.epg.startup.BaseStartUpPresenter;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class StartupPresenter extends BaseStartUpPresenter {
    private static long AD_REQUEST_INTERVAL = 1000;
    private static final String TAG = "StartupPresent";
    private Context mContext;
    private FrameLayout mDefaultWelcomeView;
    private ImageView mDynamicView;
    private Handler mStartUpHandler;
    private boolean mWelcomeCompleted = false;

    private class StartUpHandler extends Handler {
        private StartUpHandler() {
        }

        public void handleMessage(Message msg) {
            LogUtils.m1568d(StartupPresenter.TAG, "start up handler,receive msg : " + msg.what + ",arg1 = " + msg.arg1);
            switch (msg.what) {
                case 256:
                    break;
                case 512:
                    StartupPresenter.this.mDynamicView = null;
                    StartupPresenter.this.mDefaultWelcomeView = null;
                    StartupPresenter.this.onPreviewFinished();
                    return;
                case 768:
                    boolean isAdReady = StartScreenAdHandler.instance().hasAd() && EpgAppConfig.isShowScreenAd();
                    LogUtils.m1568d(StartupPresenter.TAG, "is ad ready " + isAdReady);
                    removeMessages(768);
                    StartupPresenter.this.mWelcomeCompleted = true;
                    if (isAdReady) {
                        StartupPresenter.this.showAd();
                        return;
                    } else if (StartupPresenter.this.getStageTwoDynamic() != null) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(StartupPresenter.TAG, "mStartOperateBitmap != null");
                        }
                        StartupPresenter.this.showOperateImage();
                        sendEmptyMessageDelayed(256, 3000);
                        return;
                    }
                    break;
                default:
                    return;
            }
            StartupPresenter.this.initGuideBoot();
            StartupPresenter.this.handleStageTow();
        }
    }

    public StartupPresenter(Context context) {
        super(context);
        this.mContext = context;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "StartupPresent constructor class@ " + getClass().getName());
        }
        this.mStartUpHandler = new StartUpHandler();
        init(context, this.mStartUpHandler);
        initViews(context);
    }

    public void start() {
        load();
    }

    private void initViews(Context context) {
        this.mDefaultWelcomeView = (FrameLayout) this.mRootView.findViewById(C0508R.id.epg_default_welcome);
        this.mDynamicView = (ImageView) this.mRootView.findViewById(C0508R.id.epg_dynamic_welcome);
        this.mDefaultWelcomeView.setVisibility(0);
        this.mDynamicView.setVisibility(8);
        this.mDefaultWelcomeView.addView(LayoutInflater.from(context).inflate(EpgAppConfig.getWelcomeLaoutId(), null, false));
    }

    private void load() {
        Message msg_show_welcome_completed = this.mStartUpHandler.obtainMessage();
        msg_show_welcome_completed.what = 768;
        long interval = SystemClock.elapsedRealtime() - StartScreenAdHandler.instance().getStartRequestTime();
        LogUtils.m1568d(TAG, "load interval = " + interval + " ms");
        if (interval < 0) {
            this.mStartUpHandler.sendMessageDelayed(msg_show_welcome_completed, 1000);
        } else if (interval < AD_REQUEST_INTERVAL) {
            this.mStartUpHandler.sendMessageDelayed(msg_show_welcome_completed, AD_REQUEST_INTERVAL - interval);
        } else {
            this.mStartUpHandler.sendMessage(msg_show_welcome_completed);
        }
    }
}
