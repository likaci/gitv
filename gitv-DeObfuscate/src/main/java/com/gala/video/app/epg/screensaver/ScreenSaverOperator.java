package com.gala.video.app.epg.screensaver;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import com.gala.video.app.epg.screensaver.ScreenSaverAdAnimation.AdCallback;
import com.gala.video.app.epg.screensaver.ScreenSaverImageAnimation.ScreenSaverPreEndCallback;
import com.gala.video.app.epg.screensaver.ScreenSaverWindow.OnKeyListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterAd;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverAnimation.IRegisterImage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverAdClick;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverBeforeFadeIn;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.IScreenSaverClick;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverModel;
import com.gala.video.lib.share.project.Project;
import java.util.List;

class ScreenSaverOperator extends Wrapper implements IScreenSaverOperate {
    private static final int DOWNLOAD_SCREENSAVER_AD_TASK = 100;
    private static final int DOWNLOAD_SCREENSAVER_IMAGE_TASK = 101;
    private static final int SHOW_SCREEN_SAVER = 102;
    private static final long START_DOWNLOAD_SCREEN_SAVER_DELAY = 120000;
    private static final String TAG = "ScreenSaverOperator";
    private static final long TEN_SECONDS_TIME = 10000;
    private Activity mActivity;
    private AdCallback mAdEndCallback;
    private ScreenSaverConfig mConfig;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler;
    private IScreenSaverAdClick mScreenSaverAdClickListener;
    private ScreenSaverAdModel mScreenSaverAdModel;
    private OnKeyListener mScreenSaverAdOnKeyListener;
    private ScreenSaverAdProvider mScreenSaverAdProvider;
    private ScreenSaverPreEndCallback mScreenSaverCallback;
    private IScreenSaverClick mScreenSaverClickListener;
    private ScreenSaverModel mScreenSaverImageModel;
    private ScreenSaverImageProvider mScreenSaverImgProvider;
    private OnKeyListener mScreenSaverOnKeyListener;
    private ScreenSaverWindow mWindow;

    class C07812 implements AdCallback {
        C07812() {
        }

        public void onAdEnd() {
            ScreenSaverOperator.this.mScreenSaverAdModel = null;
            LogUtils.m1568d(ScreenSaverOperator.TAG, "mAdEndCallback, onAdEnd, screen saver advertisement loop finished");
            ScreenSaverOperator.this.mScreenSaverAdProvider.reset();
            ScreenSaverOperator.this.mHandler.sendEmptyMessage(102);
            AdsClientUtils.getInstance().flushCupidPingback();
            AdsClientUtils.getInstance().sendAdPingBacks();
        }

        public void onAdPlay(int currentIndex, ScreenSaverAdModel data, boolean isShowQr) {
            LogUtils.m1568d(ScreenSaverOperator.TAG, "mAdEndCallback, onAdPlay, started ad id = " + data.getAdId());
            ScreenSaverOperator.this.mScreenSaverAdModel = data;
            AdsClientUtils.getInstance().onAdStarted(data.getAdId());
        }
    }

    class C07823 implements ScreenSaverPreEndCallback {
        C07823() {
        }

        public void onEachScreenSaverShow(ScreenSaverModel model) {
            ScreenSaverOperator.this.mScreenSaverImageModel = model;
        }

        public void onScreenSaverEnd() {
            LogUtils.m1568d(ScreenSaverOperator.TAG, "mScreenSaverCallback, onScreenSaverEnd, screen saver finished ");
            ScreenSaverOperator.this.mHandler.sendEmptyMessage(102);
        }

        public void onScreenSaverPreEnd() {
            LogUtils.m1568d(ScreenSaverOperator.TAG, "mScreenSaverCallback, onScreenSaverPreEnd, screen saver nearly finished, start download advertisement");
            ScreenSaverOperator.this.mHandler.sendEmptyMessage(100);
        }
    }

    class C07834 implements OnKeyListener {
        C07834() {
        }

        public boolean onKeyEvent(KeyEvent event) {
            boolean result = false;
            if (event.getAction() == 1) {
                LogUtils.m1568d(ScreenSaverOperator.TAG, "screensaver ad click, onKeyEvent(): event.getAction - " + event.getAction());
                if (!(ScreenSaverOperator.this.mScreenSaverAdClickListener == null || ScreenSaverOperator.this.mWindow == null || !ScreenSaverOperator.this.mWindow.isShowingAd())) {
                    result = ScreenSaverOperator.this.mScreenSaverAdClickListener.onKeyEvent(event, ScreenSaverOperator.this.mScreenSaverAdModel, ScreenSaverOperator.this.mActivity);
                }
                new ScreenSaverPingbackSender().sendPingback(event, ScreenSaverOperator.this.mWindow, ScreenSaverOperator.this.mScreenSaverAdModel);
                ScreenSaverOperator.this.clearPingBackCount();
                ScreenSaverOperator.this.reset();
            }
            return result;
        }
    }

    class C07845 implements OnKeyListener {
        C07845() {
        }

        public boolean onKeyEvent(KeyEvent event) {
            LogUtils.m1574i(ScreenSaverOperator.TAG, "onKeyEvent(), screen saver image, event.getAction - " + event.getAction());
            boolean result = false;
            if (event.getAction() == 1) {
                if (!(ScreenSaverOperator.this.mScreenSaverClickListener == null || ScreenSaverOperator.this.mWindow == null || !ScreenSaverOperator.this.mWindow.isShowingImage())) {
                    result = ScreenSaverOperator.this.mScreenSaverClickListener.onKeyEvent(event, ScreenSaverOperator.this.mScreenSaverImageModel, ScreenSaverOperator.this.mActivity);
                }
                ScreenSaverOperator.this.clearPingBackCount();
                ScreenSaverOperator.this.reset();
            }
            return result;
        }
    }

    ScreenSaverOperator() {
        this.mWindow = null;
        this.mScreenSaverImgProvider = null;
        this.mScreenSaverAdProvider = null;
        this.mScreenSaverAdClickListener = null;
        this.mScreenSaverClickListener = null;
        this.mScreenSaverAdModel = null;
        this.mScreenSaverImageModel = null;
        this.mHandler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 100:
                        if (ScreenSaverOperator.this.mScreenSaverImgProvider.getLocalDataSize() > 0) {
                            ScreenSaverOperator.this.mScreenSaverAdProvider = new ScreenSaverAdProvider();
                            ScreenSaverOperator.this.mScreenSaverAdProvider.fetchAdData();
                            LogUtils.m1568d(ScreenSaverOperator.TAG, "handleMessage, message = DOWNLOAD_SCREENSAVER_AD_TASK, start download screensaver Ad");
                            return;
                        }
                        LogUtils.m1577w(ScreenSaverOperator.TAG, "handleMessage, message = DOWNLOAD_SCREENSAVER_AD_TASK, screensaver image data is empty");
                        return;
                    case 101:
                        LogUtils.m1568d(ScreenSaverOperator.TAG, "handleMessage, message = DOWNLOAD_SCREENSAVER_IMAGE_TASK");
                        ScreenSaverOperator.this.mScreenSaverImgProvider.download();
                        return;
                    case 102:
                        LogUtils.m1568d(ScreenSaverOperator.TAG, "handleMessage, message = SHOW_SCREEN_SAVER");
                        int screenSaverImageSize = ScreenSaverOperator.this.mScreenSaverImgProvider.getLocalDataSize();
                        boolean isScreenSaverAdPrepared = ScreenSaverOperator.this.mScreenSaverAdProvider.isAdPrepared();
                        if (screenSaverImageSize <= 0 || !isScreenSaverAdPrepared) {
                            LogUtils.m1577w(ScreenSaverOperator.TAG, "handleMessage, message = SHOW_SCREEN_SAVER, is screensaver ad prepared :" + isScreenSaverAdPrepared + " screensaver image size " + screenSaverImageSize);
                            if (screenSaverImageSize > 0) {
                                ScreenSaverOperator.this.showScreenSaverImage();
                                return;
                            }
                            LogUtils.m1577w(ScreenSaverOperator.TAG, "handleMessage, message = SHOW_SCREEN_SAVER,screensaver image size is 0");
                            ScreenSaverOperator.this.reStart();
                            return;
                        }
                        ScreenSaverOperator.this.showScreenSaverAd(ScreenSaverOperator.this.mScreenSaverAdProvider.getAdData());
                        return;
                    default:
                        return;
                }
            }
        };
        this.mAdEndCallback = new C07812();
        this.mScreenSaverCallback = new C07823();
        this.mScreenSaverAdOnKeyListener = new C07834();
        this.mScreenSaverOnKeyListener = new C07845();
        this.mScreenSaverImgProvider = ScreenSaverImageProvider.getInstance();
        this.mScreenSaverAdProvider = new ScreenSaverAdProvider();
        this.mConfig = new ScreenSaverConfig();
    }

    public void setCurrentActivity(Activity activity) {
        LogUtils.m1568d(TAG, "setCurrentActivity = " + activity);
        this.mActivity = activity;
        if (this.mWindow != null && isShowScreenSaver()) {
            hideScreenSaver();
        }
        this.mWindow = new ScreenSaverWindow(this.mActivity);
        reStart();
    }

    public Activity getCurActivity() {
        return this.mActivity;
    }

    private void showScreenSaverAd(List<ScreenSaverAdModel> adModelList) {
        LogUtils.m1568d(TAG, "showScreenSaverAd, Ad info size : " + ListUtils.getCount((List) adModelList));
        if (this.mActivity == null || this.mActivity.isFinishing()) {
            String str = TAG;
            StringBuilder append = new StringBuilder().append("showScreenSaverAd, ").append(this.mActivity == null ? "mActivity is null" : "");
            String str2 = (this.mActivity == null || !this.mActivity.isFinishing()) ? "" : "mActivity.isFinishing()";
            LogUtils.m1577w(str, append.append(str2).toString());
            return;
        }
        if (this.mWindow == null) {
            this.mWindow = new ScreenSaverWindow(this.mActivity);
        }
        this.mWindow.setOnKeyListener(this.mScreenSaverAdOnKeyListener);
        this.mWindow.setCurrentWindowStyle(0);
        this.mWindow.setAdPlayCallback(this.mAdEndCallback);
        this.mWindow.showAnimationAd(adModelList);
    }

    private void showScreenSaverImage() {
        if (this.mActivity == null || this.mActivity.isFinishing()) {
            String str = TAG;
            StringBuilder append = new StringBuilder().append("showScreenSaverImage, ").append(this.mActivity == null ? "mActivity is null" : "");
            String str2 = (this.mActivity == null || !this.mActivity.isFinishing()) ? "" : "mActivity.isFinishing()";
            LogUtils.m1577w(str, append.append(str2).toString());
            return;
        }
        if (this.mWindow == null) {
            this.mWindow = new ScreenSaverWindow(this.mActivity);
        }
        this.mScreenSaverImgProvider.prepare();
        this.mWindow.setCurrentWindowStyle(1);
        this.mWindow.setScreenSaverCallback(this.mScreenSaverCallback);
        this.mWindow.setOnKeyListener(this.mScreenSaverOnKeyListener);
        if (!this.mWindow.isShowingImage()) {
            this.mWindow.showAnimation(this.mScreenSaverImgProvider);
            LogUtils.m1568d(TAG, "showScreenSaverImage, show screen saver image");
        }
    }

    private void commonStart() {
        this.mConfig.readScreenSaverSettingData();
        long screenSaverDelayTime = this.mConfig.getScreenSaverDelayTime();
        if (this.mConfig.isEnableInSetting() && this.mConfig.isEnable() && screenSaverDelayTime > 0) {
            this.mHandler.sendEmptyMessageDelayed(100, screenSaverDelayTime - TEN_SECONDS_TIME);
            this.mHandler.sendEmptyMessageDelayed(102, screenSaverDelayTime);
            return;
        }
        LogUtils.m1568d(TAG, "commonStart, " + (!this.mConfig.isEnableInSetting() ? " has closed screensaver in settings " : "") + (!this.mConfig.isEnable() ? " has closed screensaver in code " : "") + (screenSaverDelayTime <= 0 ? " screensaver DelayTime < 0" : ""));
    }

    public void start() {
        if (Project.getInstance().getBuild().isIsSupportScreenSaver()) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler.sendEmptyMessageDelayed(101, 120000);
            commonStart();
            return;
        }
        LogUtils.m1568d(TAG, "start, not support screensaver");
    }

    public void reStart() {
        if (Project.getInstance().getBuild().isIsSupportScreenSaver()) {
            this.mHandler.removeMessages(100);
            this.mHandler.removeMessages(102);
            commonStart();
        }
    }

    public void stop() {
        LogUtils.m1568d(TAG, "stop");
        this.mHandler.removeMessages(100);
        this.mHandler.removeMessages(102);
        hideScreenSaver();
        this.mActivity = null;
    }

    public void setScreenSaverEnable(boolean enable) {
        LogUtils.m1568d(TAG, "setScreenSaverEnable, enable =" + enable);
        this.mConfig.setEnable(enable);
        if (!enable) {
            LogUtils.m1568d(TAG, "setScreenSaverEnable, remove messages in message queue, is screensaver enable : " + enable);
            this.mHandler.removeMessages(100);
            this.mHandler.removeMessages(102);
        }
    }

    public boolean isShowScreenSaver() {
        return this.mWindow != null && this.mWindow.isShowing();
    }

    public void hideScreenSaver() {
        LogUtils.m1568d(TAG, "hideScreenSaver");
        try {
            if (this.mWindow != null && isShowScreenSaver()) {
                this.mWindow.dismiss();
                this.mWindow = null;
            }
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "hideScreenSaver Exception", e);
        }
    }

    private void clearPingBackCount() {
        if (this.mWindow != null) {
            this.mWindow.clearPingBackCount();
        }
    }

    private void reset() {
        hideScreenSaver();
        reStart();
    }

    public boolean isApplicationBroughtToBackground() {
        String playerActivity = "PlayerActivity";
        String detailActivity = "AlbumDetailActivity";
        String newsDetailActivity = "NewsDetailActivity";
        List<RunningTaskInfo> tasks = ((ActivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("activity")).getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = ((RunningTaskInfo) tasks.get(0)).topActivity;
            if (!topActivity.getPackageName().equals(Project.getInstance().getBuild().getPackageName())) {
                return true;
            }
            if (Project.getInstance().getBuild().supportPlayerMultiProcess() && GetInterfaceTools.getIInit().isMainProcess()) {
                String topClassName = topActivity.getClassName();
                if (topClassName.contains("PlayerActivity") || topClassName.contains("AlbumDetailActivity") || topClassName.contains("NewsDetailActivity")) {
                    return true;
                }
            }
        }
        return false;
    }

    public IRegisterAd getAdRegister() {
        return ScreenSaverWindow.getAdRegister();
    }

    public IRegisterImage getImgRegister() {
        return ScreenSaverWindow.getImgRegister();
    }

    public IScreenSaverStatusDispatcher getStatusDispatcher() {
        return ScreenSaverWindow.getStatusDispatcher();
    }

    public void setScreenSaverAdClickListener(IScreenSaverAdClick listener) {
        this.mScreenSaverAdClickListener = listener;
    }

    public void setScreenSaverClickListener(IScreenSaverClick listener) {
        this.mScreenSaverClickListener = listener;
    }

    public void setScreenSaverBeforeFadeInCallBack(IScreenSaverBeforeFadeIn callBack) {
        this.mWindow.setScreenSaverBeforeFadeInCallBack(callBack);
    }

    public void exitHomeVersionScreenSaver(Context context) {
        String ACTION_EXIT_SCREENSAVER = "com.android.skyworth.screensave.request.dismiss";
        context.sendBroadcast(new Intent("com.android.skyworth.screensave.request.dismiss"));
    }
}
