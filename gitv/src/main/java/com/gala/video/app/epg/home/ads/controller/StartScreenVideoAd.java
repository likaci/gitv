package com.gala.video.app.epg.home.ads.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.ViewGroup;
import com.gala.sdk.event.AdSpecialEvent;
import com.gala.sdk.event.AdSpecialEvent.EventType;
import com.gala.sdk.event.OnAdSpecialEventListener;
import com.gala.sdk.player.AdCacheManager;
import com.gala.sdk.player.AdCacheManager.AdCacheItem;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.epg.home.ads.AdCacheManagerProxy;
import com.gala.video.app.epg.home.controller.HomeController;
import com.gala.video.app.epg.home.controller.UIEventType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.utils.Locker;
import com.gala.video.app.epg.preference.StartUpPreferrence;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.player.feature.PluginStateChangedListener;
import com.gala.video.app.stub.SharedAction;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.app.stub.suportv4.LocalBroadcastManager;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.mcto.ads.AdsClient;
import com.mcto.ads.CupidAd;
import com.mcto.ads.constants.ClickThroughType;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.HashMap;
import java.util.Map;

public class StartScreenVideoAd implements IStartScreenAd {
    private static final String ADID_CLICK_THROUGH_TYPE = "clickThroughType";
    private static final String ADID_CLICK_THROUGH_URL = "clickThroughUrl";
    private static final String ADID_KEY = "adId";
    private static final String TAG = "home/StartScreenVideoAd";
    private static final int TIME_OUT = 3500;
    private static boolean sIsAdPlaying = false;
    private ViewGroup mAdContainer;
    private int mAdId = -1;
    private HashMap<String, Object> mAdParams;
    private LocalBroadcastManager mBroadCastManager;
    private AdStatusCallBack mCallBack;
    private AdsClient mClient;
    private Context mContext;
    private long mCreatePlayerReadyTime = 0;
    private boolean mEnableJump = false;
    private boolean mHasAd = false;
    private boolean mIsAdPrepared = false;
    private boolean mIsTimeOut = false;
    private long mLoadPluginStartTime = 0;
    private Locker mLocker = new Locker();
    private Handler mMainHandler;
    private MyObserver mObserver = new MyObserver() {
        public void update(String event) {
            LogUtils.d(StartScreenVideoAd.TAG, "receive message " + event);
            StartScreenVideoAd.this.mLocker.complete();
        }
    };
    private Runnable mOnlyLoadPluginRunnable = new Runnable() {
        public void run() {
            LogUtils.d(StartScreenVideoAd.TAG, "load player plugin");
            GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeatureOnlyInitJava();
            StartScreenVideoAd.this.sendLoadPluginCostPingback(SystemClock.elapsedRealtime() - StartScreenVideoAd.this.mLoadPluginStartTime);
            Intent intent = new Intent();
            intent.setAction(SharedAction.PLAYER_PLUGIN_LOAD_COMPLETED);
            StartScreenVideoAd.this.mBroadCastManager.sendBroadcast(intent);
        }
    };
    private String mPingbackBlock = "";
    private String mPlayUrl = "";
    private IGalaVideoPlayer mPlayer;
    private OnAdSpecialEventListener mSpecialEventListener = new OnAdSpecialEventListener() {
        public void onAdSpecialEvent(AdSpecialEvent event) {
            LogUtils.d(StartScreenVideoAd.TAG, "onAdSpecialEvent event=" + event);
            if (event != null && event.getEventType() == EventType.STARTUP_AD_REDIRECT) {
                StartScreenVideoAd.this.mClient.onAdClicked(StartScreenVideoAd.this.mAdId);
            }
        }
    };
    private long mStartPlayTime = 0;
    private long mStartRequestTime = 0;
    private Handler mTimeHandler;
    private Runnable mTimeRunnable = new Runnable() {
        public void run() {
            StartScreenVideoAd.this.mIsTimeOut = true;
            if (StartScreenVideoAd.this.mCallBack != null && !StartScreenVideoAd.this.mIsAdPrepared) {
                LogUtils.w(StartScreenVideoAd.TAG, "[StartScreenVideoAd-Performance]on ad play time out");
                StartScreenVideoAd.this.mCallBack.onTimeOut();
                HomeController.sUIEvent.post(UIEventType.BUILD_UI_RESUME, null);
                HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "ad_start_vid").addItem("pfec", PingbackConstants.ACT_MIXER_TIMEOUT).addItem(Keys.T, "0").setOthersNull().post();
                if (StartScreenVideoAd.this.mPlayer != null) {
                    StartScreenVideoAd.this.mPlayer.release();
                    StartScreenVideoAd.this.mAdContainer.removeAllViews();
                    StartScreenVideoAd.this.mPlayer = null;
                }
            }
        }
    };
    private OnPlayerStateChangedListener mVideoStateListener = new OnPlayerStateChangedListener() {
        public void onVideoStarted(IVideo iVideo) {
            LogUtils.d(StartScreenVideoAd.TAG, "onVideoStarted");
        }

        public void onVideoSwitched(IVideo iVideo, int type) {
        }

        public void onPlaybackFinished() {
            LogUtils.d(StartScreenVideoAd.TAG, "onPlaybackFinished");
            if (StartScreenVideoAd.this.mCallBack != null) {
                StartScreenVideoAd.this.mCallBack.onFinished();
            }
        }

        public boolean onError(IVideo iVideo, ISdkError iSdkError) {
            LogUtils.d(StartScreenVideoAd.TAG, "onError ");
            HomeController.sUIEvent.post(UIEventType.BUILD_UI_RESUME, null);
            HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "ad_start_vid").addItem("pfec", "wrong").addItem(Keys.T, "0").setOthersNull().post();
            return false;
        }

        public void onAdStarted() {
            LogUtils.d(StartScreenVideoAd.TAG, "onAdStarted");
        }

        public void onAdEnd() {
            LogUtils.d(StartScreenVideoAd.TAG, "on ad play end play cost : " + (System.currentTimeMillis() - StartScreenVideoAd.this.mStartPlayTime) + " ms");
            if (StartScreenVideoAd.this.mCallBack != null) {
                StartScreenVideoAd.this.mCallBack.onFinished();
            }
            StartScreenVideoAd.this.mPlayer = null;
            StartScreenVideoAd.this.mClient.flushCupidPingback();
        }

        public void onScreenModeSwitched(ScreenMode screenMode) {
        }

        public void onPrepared() {
            long currentTime = System.currentTimeMillis();
            long totalCost = currentTime - StartScreenVideoAd.this.mStartPlayTime;
            LogUtils.d(StartScreenVideoAd.TAG, "[StartScreenVideoAd-Performance]MediaPlayer.onPrepared timeCost=" + (currentTime - StartScreenVideoAd.this.mCreatePlayerReadyTime));
            LogUtils.d(StartScreenVideoAd.TAG, "onPrepared,prepared time cost : " + totalCost + " ms");
            HomeController.sUIEvent.post(UIEventType.BUILD_UI_RESUME, null);
            StartScreenVideoAd.this.mIsAdPrepared = true;
            StartScreenVideoAd.this.sendPrepareCostPingback(totalCost);
            StartScreenVideoAd.this.mPlayer.start(0);
            StartScreenVideoAd.this.show();
        }
    };

    private class LoadPlayerFeatureRunnable implements Runnable {
        private ViewGroup mContainer;

        public LoadPlayerFeatureRunnable(ViewGroup container) {
            this.mContainer = container;
        }

        public void run() {
            long start = SystemClock.elapsedRealtime();
            GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeatureOnlyInitJava();
            LogUtils.d(StartScreenVideoAd.TAG, "getPlayerFeatureOnlyInitJava cost " + (SystemClock.elapsedRealtime() - start));
            StartScreenVideoAd.this.mMainHandler.post(new StartPlayerRunnable(this.mContainer));
        }
    }

    private class StartPlayerRunnable implements Runnable {
        private ViewGroup mContainer;

        public StartPlayerRunnable(ViewGroup container) {
            this.mContainer = container;
        }

        public void run() {
            StartScreenVideoAd.this.startPlayer(this.mContainer);
        }
    }

    public StartScreenVideoAd(Context context, AdsClient client, String url, CupidAd cupidAd, Map<String, Object> params, long startRequestTime) {
        this.mContext = context;
        this.mStartRequestTime = startRequestTime;
        this.mAdParams = deepCloneMap(params);
        this.mPlayUrl = url;
        this.mClient = client;
        parseClickInfo(cupidAd);
        if (cupidAd != null) {
            this.mAdId = cupidAd.getAdId();
            this.mAdParams.put(ADID_KEY, Integer.valueOf(this.mAdId));
        }
        if (!(params == null || params.get(AdsConstants.AD_VIDEO_SKIPPABLE) == null)) {
            this.mEnableJump = "true".equals(params.get(AdsConstants.AD_VIDEO_SKIPPABLE).toString());
        }
        this.mTimeHandler = new Handler(Looper.getMainLooper());
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mBroadCastManager = LocalBroadcastManager.getInstance(context);
    }

    public void loadData(long requestInterval) {
        LogUtils.d(TAG, "load video ad data");
        GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.PLAY_PLUGIN_LOAD_SUCCESS, this.mObserver);
        this.mLocker.takeOrWait();
        LogUtils.d(TAG, ">>has video ad playUrl: " + this.mPlayUrl + ",adid = " + this.mAdId);
        long startTime = SystemClock.elapsedRealtime();
        AdCacheManager adCacheManager = AdCacheManagerProxy.getInstance();
        AdCacheItem adCache = new AdCacheItem(this.mPlayUrl, 1);
        boolean hasAd = adCacheManager.isCached(adCache);
        if (!hasAd) {
            adCacheManager.addTask(adCache);
            adCacheManager.setCurrentRunningState(0);
        }
        sendPingback(requestInterval, hasAd);
        LogUtils.d(TAG, "[StartScreenVideoAd-Performance]loadData timeCost=" + (SystemClock.elapsedRealtime() - startTime));
        LogUtils.d(TAG, "<<has video ad : " + hasAd);
        int adCrashTimes = StartUpPreferrence.getCrashTimes(AppRuntimeEnv.get().getApplicationContext(), "start_up_video_crash_" + AppClientUtils.getVersionHeader());
        if (hasAd && adCrashTimes < 3) {
            this.mHasAd = true;
            this.mCallBack.onAdPrepared();
            this.mLoadPluginStartTime = SystemClock.elapsedRealtime();
            Intent intent = new Intent();
            intent.setAction(SharedAction.VIDEO_AD_LOAD_COMPLETED);
            this.mBroadCastManager.sendBroadcast(intent);
            new Thread8K(this.mOnlyLoadPluginRunnable).start();
        }
    }

    private void sendPingback(long elapse, boolean hasAd) {
        HomePingbackFactory.instance().createPingback(CommonPingback.AD_DATA_REQUEST_PINGBACK).addItem(Keys.RI, "ad_startapk").addItem("st", hasAd ? "vid_local" : "vid_download").addItem("td", String.valueOf(elapse)).addItem("r", "vid").addItem(Keys.T, "11").addItem("ct", "150619_request").setOthersNull().post();
    }

    public void showAd(ViewGroup container) {
        prepare(container);
    }

    private void show() {
        LogUtils.d(TAG, "showAd");
        if (this.mAdContainer != null) {
            this.mAdContainer.bringToFront();
        }
        if (this.mClient != null) {
            this.mClient.onAdStarted(this.mAdId);
        }
        HomePingbackFactory.instance().createPingback(ShowPingback.START_AD_PAGE_SHOW_PINGBACK).addItem("qtcurl", "ad_start").addItem("block", this.mEnableJump ? "ad_start_vid" : this.mPingbackBlock).addItem(Keys.ISACT, this.mEnableJump ? "1" : "0").addItem("td", String.valueOf(SystemClock.elapsedRealtime() - this.mStartRequestTime)).post();
        PingBackCollectionFieldUtils.setIncomeSrc("others");
    }

    public void stop() {
        sIsAdPlaying = false;
    }

    public static boolean isVideoAdPlaying() {
        return sIsAdPlaying;
    }

    public boolean enableJump() {
        return this.mEnableJump;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        sendPingback(keyEvent);
        if (this.mPlayer == null) {
            return false;
        }
        boolean handled = this.mPlayer.handleKeyEvent(keyEvent);
        LogUtils.d(TAG, "dispatch keyevent to player,return : " + handled);
        return handled;
    }

    private void sendPingback(KeyEvent keyEvent) {
        if (keyEvent.getAction() != 0) {
            return;
        }
        if (keyEvent.getKeyCode() == 20 || keyEvent.getKeyCode() == 22) {
            String rseat = "";
            if (keyEvent.getKeyCode() == 20) {
                rseat = ScreenSaverPingBack.SEAT_KEY_DOWN;
            } else if (keyEvent.getKeyCode() == 22) {
                rseat = ScreenSaverPingBack.SEAT_KEY_RIGHT;
            }
            HomePingbackFactory.instance().createPingback(ClickPingback.START_AD_PAGE_CLICK_PINGBACK).addItem("rpage", "ad_start").addItem("block", this.mEnableJump ? "ad_start_vid" : this.mPingbackBlock).addItem("rt", "i").addItem(Keys.ISACT, this.mEnableJump ? "1" : "0").addItem("rseat", rseat).setOthersNull().post();
        }
    }

    public void setAdStatusCallBack(AdStatusCallBack callBack) {
        this.mCallBack = callBack;
    }

    private void sendPrepareCostPingback(long cost) {
        HomePingbackFactory.instance().createPingback(CommonPingback.LOAD_FINISHED_PINGBACK).addItem("td", String.valueOf(cost)).addItem(Keys.LDTYPE, "ad_startapk_vid_content").addItem(Keys.T, "11").addItem("ct", "160602_load").setOthersNull().post();
    }

    private void sendLoadPluginCostPingback(long cost) {
        HomePingbackFactory.instance().createPingback(CommonPingback.LOAD_FINISHED_PINGBACK).addItem("td", String.valueOf(cost)).addItem(Keys.LDTYPE, "ad_startapk_vid_plugin").addItem(Keys.T, "11").addItem("ct", "160602_load").setOthersNull().post();
    }

    private void prepare(ViewGroup container) {
        LogUtils.d(TAG, "prepare");
        if (this.mHasAd) {
            HomeController.sUIEvent.post(UIEventType.BUILD_UI_PAUSE, null);
            sIsAdPlaying = true;
            this.mAdContainer = container;
            this.mStartPlayTime = System.currentTimeMillis();
            this.mTimeHandler.postDelayed(this.mTimeRunnable, 3500);
            new Thread8K(new LoadPlayerFeatureRunnable(container)).start();
            return;
        }
        this.mCallBack.onError();
    }

    private void parseClickInfo(CupidAd cupidAd) {
        String clickTypeStr = PluginStateChangedListener.ERROR_TYPE_DEFAULT;
        String throughUrl = "";
        if (cupidAd != null) {
            ClickThroughType clickThroughType = cupidAd.getClickThroughType();
            throughUrl = cupidAd.getClickThroughUrl();
            LogUtils.d(TAG, "video click through type = " + clickThroughType);
            switch (clickThroughType) {
                case DEFAULT:
                    clickTypeStr = "WEBVIEW";
                    this.mPingbackBlock = "ad_jump_defalt";
                    break;
                case WEBVIEW:
                    clickTypeStr = "WEBVIEW";
                    this.mPingbackBlock = "ad_start_vid_jump_h5";
                    break;
                case IMAGE:
                    clickTypeStr = "IMAGE";
                    this.mPingbackBlock = "ad_start_vid_jump_pic";
                    break;
                case VIDEO:
                    clickTypeStr = PluginStateChangedListener.ERROR_TYPE_DEFAULT;
                    this.mPingbackBlock = "ad_start_vid";
                    break;
                case CAROUSEL_STATION:
                    this.mPingbackBlock = "ad_start_vid";
                    clickTypeStr = PluginStateChangedListener.ERROR_TYPE_DEFAULT;
                    break;
                default:
                    this.mPingbackBlock = "ad_start_vid";
                    LogUtils.d(TAG, "unsupported click type");
                    break;
            }
            this.mAdParams.put("clickThroughType", clickTypeStr);
            this.mAdParams.put("clickThroughUrl", throughUrl);
        }
    }

    private HashMap<String, Object> deepCloneMap(Map<String, Object> map) {
        HashMap<String, Object> result = new HashMap();
        if (map != null) {
            result.putAll(map);
        }
        return result;
    }

    private void startPlayer(ViewGroup container) {
        if (!this.mIsTimeOut) {
            container.removeAllViews();
            LogUtils.d(TAG, "container@" + container);
            container.setVisibility(0);
            LogUtils.d(TAG, "start ad play play url: " + this.mPlayUrl);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videoType", SourceType.STARTUP_AD);
            bundle.putSerializable("url", this.mPlayUrl);
            bundle.putSerializable(PlayerIntentConfig2.INTENT_PARAM_STARTUP_AD_JSON, this.mAdParams);
            this.mPlayer = GetInterfaceTools.getGalaVideoPlayerGenerator().createVideoPlayer(container.getContext(), container, bundle, this.mVideoStateListener, ScreenMode.FULLSCREEN, null, new WindowZoomRatio(true, WindowZoomRatio.WINDOW_ZOOM_RATIO_4_BY_3_BIG), null, this.mSpecialEventListener);
            this.mCreatePlayerReadyTime = System.currentTimeMillis();
            LogUtils.d(TAG, "[StartScreenVideoAd-Performance]PlayerFeature.createPlayer timeCost=" + (this.mCreatePlayerReadyTime - this.mStartPlayTime));
        }
    }
}
