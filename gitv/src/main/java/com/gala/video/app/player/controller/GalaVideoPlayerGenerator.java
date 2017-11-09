package com.gala.video.app.player.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.gala.sdk.event.OnAdSpecialEventListener;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.GalaVideoPlayerParams;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.IGalaVideoPlayerFactory;
import com.gala.sdk.player.INetDiagnoseProvider;
import com.gala.sdk.player.INetDiagnoseProvider.INetDiagnoseCallback;
import com.gala.sdk.player.IPlayerProfile;
import com.gala.sdk.player.IProjectEventReporter;
import com.gala.sdk.player.ISceneActionProvider;
import com.gala.sdk.player.IVideoOverlay;
import com.gala.sdk.player.OnCarouselProgramClickListener;
import com.gala.sdk.player.OnErrorFinishedListener;
import com.gala.sdk.player.OnHistoryRecorderListener;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.OnRedirectOutPageListener;
import com.gala.sdk.player.OnReleaseListener;
import com.gala.sdk.player.OnShowHintListener;
import com.gala.sdk.player.OnShowHintListener.HintType;
import com.gala.sdk.player.OnUpdateLayoutListener;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IHistoryFetcher;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.IErrorStrategy;
import com.gala.sdk.player.ui.IPlayerOverlay;
import com.gala.sdk.utils.performance.GlobalPerformanceTracker;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.PlayerActivity;
import com.gala.video.app.player.R;
import com.gala.video.app.player.SProjectEventReporter;
import com.gala.video.app.player.controller.error.FullscreenErrorStrategy;
import com.gala.video.app.player.controller.error.WindowedErrorStrategy;
import com.gala.video.app.player.feature.drm.IntertrustDrmPluginProvider;
import com.gala.video.app.player.init.task.IntertrustDrmPluginLoadTask;
import com.gala.video.app.player.perftracker.PerformanceMonitor;
import com.gala.video.app.player.perftracker.PlayerTrackerConfig;
import com.gala.video.app.player.ui.carousel.CarouselPlayerOverlay;
import com.gala.video.app.player.ui.overlay.PlayerOverlay;
import com.gala.video.app.player.ui.widget.GalaPlayerView;
import com.gala.video.app.player.ui.widget.GalaPlayerView.ViewMode;
import com.gala.video.app.player.utils.PlayerToastHelper;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDWrapperOperate;
import com.gala.video.lib.framework.coreservice.netdiagnose.INetDiagnoseController;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseController;
import com.gala.video.lib.share.ifimpl.netdiagnose.model.CDNNetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.CarouselHistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IGalaVideoPlayerGenerator.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperEventInput;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperPlayerOverlay;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDataUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo.Builder;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GalaVideoPlayerGenerator extends Wrapper {
    private static final String TAG = "Player/GalaVideoPlayerGenerator";
    private static GalaVideoPlayerGenerator instance;
    private static OnHistoryRecorderListener mHistoryRecorderListener = new OnHistoryRecorderListener() {
        public void onWatchTrackAddPlayRecord(Album album, boolean isWeekend) {
            long now = DeviceUtils.getServerTimeMillis() / 1000;
            HistoryInfo historyInfo = new Builder(AppClientUtils.getCookie(AppRuntimeEnv.get().getApplicationContext())).album(album).addedTime(now).uploadTime(now).build();
            if (LogUtils.mIsDebug) {
                LogUtils.d(GalaVideoPlayerGenerator.TAG, "addPlayRecord(), putAndSend(historyInfo = " + historyInfo + ")");
            }
            GetInterfaceTools.getIHistoryCacheManager().uploadHistory(historyInfo);
        }

        public void onAddLocalCarouselPlayRecord(String channelId, String channelName, String channelTableNo, long startTime, long endTime) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GalaVideoPlayerGenerator.TAG, "addLocalCarouselPlayRecordchannelId = " + channelId + "channelName=" + channelName + "channelTableNo=" + channelTableNo + "startTime=" + startTime + "endTime=" + endTime);
            }
            CarouselHistoryInfo info = new CarouselHistoryInfo(channelId, channelTableNo, channelName);
            info.setStartTime(startTime);
            info.setEndTime(endTime);
            if (LogUtils.mIsDebug) {
                LogUtils.d(GalaVideoPlayerGenerator.TAG, InterfaceKey.EPG_CHCM + info);
            }
            GetInterfaceTools.getICarouselHistoryCacheManager().put(info);
        }
    };
    private static INetDiagnoseProvider mNetDiagnoseProvider = new INetDiagnoseProvider() {
        private static final String TAG_L = "Player/NetDiagnoseProvider";
        private INetDiagnoseCallback mCallback;
        private INDDoneListener mCdnListener = new INDDoneListener() {
            public void onFinish(Map<String, Object> resultMap) {
                NetDiagnoseInfo result = (NetDiagnoseInfo) resultMap.get("data");
                AnonymousClass3.this.addToLogRecord(AnonymousClass3.TAG_L, "mCdnListener response = " + result.getDnsResult());
            }
        };
        private INDDoneListener mCollectListener = new INDDoneListener() {
            public void onFinish(Map<String, Object> resultMap) {
                NetDiagnoseInfo result = (NetDiagnoseInfo) resultMap.get("data");
                AnonymousClass3.this.addToLogRecord(AnonymousClass3.TAG_L, "mCollectListener response = " + result.getCollectionResult());
                if (AnonymousClass3.this.mCallback != null) {
                    AnonymousClass3.this.mCallback.onDiagnoseResult(result.getCollectionResult());
                }
            }
        };
        private INDUploadCallback mUploadcallback = new INDUploadCallback() {
            public void uploadNetDiagnoseDone() {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(GalaVideoPlayerGenerator.TAG, "uploadNetDiagnoseDone");
                }
            }
        };

        public void getDiagnoseInfoAsync(INetDiagnoseCallback callback, IVideo video, Context context, int playerType, IPlayerProfile profile) {
            if (callback != null && video != null && context != null && profile != null) {
                CDNNetDiagnoseInfo info = new CDNNetDiagnoseInfo(video.getAlbum(), profile.getCookie(), profile.getUid(), profile.getUserType(), 0);
                this.mCallback = callback;
                INetDiagnoseController controller = new NetDiagnoseController(context, info);
                List<INDWrapperOperate> mWrapperList = new ArrayList();
                mWrapperList.clear();
                mWrapperList.add(NetDiagnoseCheckTools.getCdnWrapper(info, playerType, this.mCdnListener, this.mUploadcallback));
                mWrapperList.add(NetDiagnoseCheckTools.getCollectInfoWrapper(info, this.mCollectListener));
                controller.startCheckEx(mWrapperList);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(TAG_L, "getDiagnoseInfoAsync: callback=" + callback + ", video=" + video + ", context=" + context + ", profile=" + profile);
            }
        }

        private synchronized void addToLogRecord(String... params) {
            StringBuilder sb = new StringBuilder();
            for (String param : params) {
                sb.append(" ").append(param);
            }
        }
    };
    private static OnCarouselProgramClickListener mOnCarouselProgramClickListener = new OnCarouselProgramClickListener() {
        public void startPlayActivity(Context context, Album albumInfo, String from, String buySource, String tabSource) {
            BasePlayParamBuilder builder = new BasePlayParamBuilder();
            PlayParams playParam = new PlayParams();
            playParam.sourceType = SourceType.OUTSIDE;
            builder.setPlayParams(playParam);
            builder.setAlbumInfo(albumInfo);
            builder.setFrom(from);
            builder.setBuySource(buySource);
            builder.setTabSource(tabSource);
            GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
        }

        public void startAlbumDetailActivity(Context context, Album albumInfo, String from, int flags, String buySource, String tabSource) {
            AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
            builder.setClearTaskFlag((32768 & flags) != 0);
            builder.setAlbumInfo(albumInfo);
            builder.setFrom(from);
            builder.setBuySource(buySource);
            builder.setTabSource(tabSource);
            PlayParams param = new PlayParams();
            param.sourceType = SourceType.OUTSIDE;
            builder.setPlayParam(param);
            GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(context, builder);
        }
    };
    private static OnErrorFinishedListener mOnErrorFinishedListener = new OnErrorFinishedListener() {
        public void onHandleErrorFinished(IGalaVideoPlayer player, Context context) {
            if (player != null && player.getVideo() != null) {
                if (!(context instanceof PlayerActivity) || player.getVideo().getSourceType() == SourceType.CAROUSEL) {
                    player.handleErrorFinished();
                    return;
                }
                Activity activity = (Activity) context;
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    };
    private static OnReleaseListener mReleaseListener = new OnReleaseListener() {
        public void onRelease() {
            Project.getInstance().getConfig().onStereo3DFinished();
        }
    };

    private static class MyPlayCoreRedirectOutPageListener implements OnRedirectOutPageListener {
        private Context mContext;

        private MyPlayCoreRedirectOutPageListener(Context context) {
            this.mContext = context;
        }

        public void redirectToBuyPage(int enterType, String buySource, Album albumInfo) {
            if (this.mContext instanceof Activity) {
                Activity activity = this.mContext;
                int requestCode = 0;
                String buyFrom = "";
                switch (enterType) {
                    case 1:
                        requestCode = 0;
                        buyFrom = "before_trial";
                        break;
                    case 2:
                        requestCode = 1;
                        buyFrom = "after_trial";
                        break;
                    case 5:
                        requestCode = 2;
                        buyFrom = "vip_noplay_jump";
                        break;
                    case 6:
                        requestCode = 3;
                        buyFrom = "trialling";
                        break;
                    case 17:
                        buyFrom = "ad_jump";
                        break;
                    case 18:
                    case 19:
                    case 20:
                        requestCode = 4;
                        buyFrom = "pay_stream";
                        break;
                }
                String eventId = activity.getIntent().getStringExtra("eventId");
                String from = activity.getIntent().getStringExtra("from");
                String state = "";
                if (albumInfo.isLive == 1) {
                    if (StringUtils.parse(albumInfo.sliveTime, -1) < DeviceUtils.getServerTimeMillis()) {
                        state = WebConstants.STATE_ONAIR;
                    } else {
                        state = WebConstants.STATE_COMING;
                    }
                }
                WebIntentParams params = new WebIntentParams();
                params.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
                params.pageType = 1;
                params.enterType = enterType;
                params.from = from;
                params.buySource = buySource;
                if (enterType != 17) {
                    params.albumInfo = albumInfo;
                }
                params.requestCode = requestCode;
                params.eventId = eventId;
                params.state = state;
                params.buyFrom = buyFrom;
                if (LogUtils.mIsDebug) {
                    LogUtils.d(GalaVideoPlayerGenerator.TAG, "onStrategy eventId=" + eventId + ", from=" + from + ", state=" + state + ", enterType=" + enterType + ", incomesrc=" + params.incomesrc);
                }
                GetInterfaceTools.getWebEntry().startPurchasePage(activity, params);
            }
        }

        public void redirectToLoginPage(int type, BitStream bs) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(GalaVideoPlayerGenerator.TAG, "redirectToLoginPage type is " + type + " /" + bs);
            }
            if (bs != null) {
                String mBitStremText = PlayerDataUtils.getBitStreamString(this.mContext, bs);
                QToast.makeTextAndShow(this.mContext, String.format(this.mContext.getResources().getString(R.string.player_login_tip_toast), new Object[]{mBitStremText}), 5000);
            }
            GetInterfaceTools.getLoginProvider().startLoginActivity(this.mContext, type == 1 ? LoginConstant.S1_FROM_LOGIN_BITSTREAM : "ralogtips", 5, 21);
        }
    }

    private static class MyShowHintListener implements OnShowHintListener {
        private static final int TOAST_CHECK_NET_DURATION = 8000;
        private Context mContext;

        public MyShowHintListener(Context context) {
            this.mContext = context;
        }

        public void onShowHint(HintType type) {
            switch (type) {
                case NET_CHECK:
                    PlayerToastHelper.showNetDiagnoseToast(this.mContext, this.mContext.getResources().getString(R.string.player_toast_check_net), TOAST_CHECK_NET_DURATION);
                    return;
                case BUFFER_LAG:
                    PlayerToastHelper.showToast(this.mContext, this.mContext.getResources().getString(R.string.player_toast_lag), (int) QToast.LENGTH_LONG);
                    return;
                default:
                    return;
            }
        }
    }

    private static class MyUpdateLayoutListener implements OnUpdateLayoutListener {
        private GalaPlayerView mPlayerView;

        public MyUpdateLayoutListener(GalaPlayerView view) {
            this.mPlayerView = view;
        }

        public void onUpdateLayout(LayoutParams params) {
            if (this.mPlayerView != null) {
                this.mPlayerView.setLayoutParams(params);
            }
        }
    }

    public static GalaVideoPlayerGenerator getInstance() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getInstance(), instance=" + instance);
        }
        if (instance == null) {
            instance = new GalaVideoPlayerGenerator();
        }
        return instance;
    }

    public IGalaVideoPlayer createVideoPlayer(Context context, ViewGroup container, Bundle bundle, OnPlayerStateChangedListener videoStateListener, ScreenMode initialMode, LayoutParams initialParams, WindowZoomRatio windowZoomRatio, IMultiEventHelper multiEventHelper, OnAdSpecialEventListener onSpecialEventListener) {
        return createGalaVideoPlayer(context, container, bundle, videoStateListener, initialMode, initialParams, windowZoomRatio, multiEventHelper, onSpecialEventListener);
    }

    private static IGalaVideoPlayer createGalaVideoPlayer(Context context, ViewGroup container, Bundle bundle, OnPlayerStateChangedListener videoStateListener, ScreenMode initialMode, LayoutParams initialParams, WindowZoomRatio windowZoomRatio, IMultiEventHelper multiEventHelper, OnAdSpecialEventListener onSpecialEventListener) {
        SourceType playType;
        IErrorStrategy currentErrorStrategy;
        IGalaVideoPlayerFactory galaVideoPlayerFactory;
        Context appContext = context != null ? context.getApplicationContext() : null;
        initialMode = checkInitialScreenModes(initialMode);
        bundle.putSerializable(PlayerIntentConfig2.INTENT_PARAM_INITSCREENMODE, initialMode);
        initialParams = checkInitialLayoutParams(initialParams);
        GlobalPerformanceTracker.instance().initialize(new PerformanceMonitor(appContext, new PlayerTrackerConfig()));
        String incomeSrc = PingBackCollectionFieldUtils.getIncomeSrc();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createGalaVideoPlayer, incomesrc=" + incomeSrc);
        }
        bundle.putString("income_source", incomeSrc);
        String eventId = bundle.getString("eventId");
        if (StringUtils.isEmpty((CharSequence) eventId)) {
            eventId = PingBackUtils.createEventId();
            bundle.putString("eventId", eventId);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "[PERF-LOADING]tm_player.prefinit");
        }
        GlobalPerformanceTracker.instance().recordPerformanceStepEnd(eventId, GlobalPerformanceTracker.PLAYER_PREF_INIT_STEP);
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(eventId, GlobalPerformanceTracker.PLAYER_UI_INIT_STEP);
        long pageInitTime = bundle.getLong(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, -1);
        long now = SystemClock.uptimeMillis();
        if (pageInitTime == -1) {
            pageInitTime = now;
        }
        SourceType sourceTypeObj = bundle.get("videoType");
        if (sourceTypeObj == null) {
            playType = SourceType.COMMON;
        } else if (sourceTypeObj instanceof SourceType) {
            playType = sourceTypeObj;
        } else {
            playType = SourceType.getByInt(((Integer) bundle.get("videoType")).intValue());
        }
        if (playType != SourceType.STARTUP_AD) {
            tryLoadIntertrustDrmPlugin();
        }
        View galaPlayerView = new GalaPlayerView(context, playType == SourceType.CAROUSEL ? ViewMode.CAROUSEL : ViewMode.COMMON);
        galaPlayerView.setBackgroundColor(-16777216);
        IHistoryFetcher historyFetcher = new HistoryFetcher();
        if (container != null) {
            int count = container.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = container.getChildAt(i);
                if (child instanceof GalaPlayerView) {
                    container.removeView(child);
                }
            }
            container.addView(galaPlayerView, initialParams);
        }
        boolean isSupportWindow = initialMode == ScreenMode.WINDOWED;
        float zoomRatio = windowZoomRatio != null ? windowZoomRatio.getResultRatio(appContext, initialParams) : 1.0f;
        ISuperPlayerOverlay overlay = createMovieOverlay(playType, galaPlayerView, isSupportWindow, zoomRatio);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "[PERF-LOADING]tm_player-ui.init");
        }
        GlobalPerformanceTracker.instance().recordPerformanceStepEnd(eventId, GlobalPerformanceTracker.PLAYER_UI_INIT_STEP);
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(eventId, GlobalPerformanceTracker.CONTROLLER_INIT_STEP);
        ISuperEventInput eventInput = new EventInput(context, overlay);
        IProjectEventReporter reporter = new SProjectEventReporter().getInstance();
        IFeedbackDialogController feedBackController = CreateInterfaceTools.createFeedbackDialogController();
        feedBackController.init(context, null);
        IErrorStrategy fullErrorStrategy = setupErrorStrategy(context, ScreenMode.FULLSCREEN, feedBackController, overlay);
        IErrorStrategy windowErrorStrategy = setupErrorStrategy(context, ScreenMode.WINDOWED, feedBackController, overlay);
        if (initialMode == ScreenMode.FULLSCREEN) {
            currentErrorStrategy = fullErrorStrategy;
        } else {
            currentErrorStrategy = windowErrorStrategy;
        }
        OnShowHintListener myShowHintListener = new MyShowHintListener(context);
        if (playType == SourceType.STARTUP_AD) {
            galaVideoPlayerFactory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeatureOnlyInitJava().getGalaVideoPlayerFactory();
        } else {
            galaVideoPlayerFactory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getGalaVideoPlayerFactory();
        }
        GalaVideoPlayerParams params = new GalaVideoPlayerParams();
        params.set(1000, context).set(1001, overlay).set(1002, historyFetcher).set(1003, eventInput).set(GalaVideoPlayerParams.PROJ_EVENT_REPORTER, reporter).set(1006, mHistoryRecorderListener).set(1007, myShowHintListener).set(1008, currentErrorStrategy).set(1009, fullErrorStrategy).set(GalaVideoPlayerParams.WINDOW_ERROR_STRATEGY, windowErrorStrategy).set(GalaVideoPlayerParams.NET_DIAGNOSE_PROVIDER, mNetDiagnoseProvider).set(GalaVideoPlayerParams.BUNDLE, bundle).set(GalaVideoPlayerParams.PLAYER_STATE_LISTENER, videoStateListener).set(GalaVideoPlayerParams.INITIAL_SCREEN_MODE, initialMode).set(GalaVideoPlayerParams.INITIAL_LAYOUT_PARAMS, initialParams).set(GalaVideoPlayerParams.WINDOW_ZOOM_RATIO, Float.valueOf(zoomRatio)).set(GalaVideoPlayerParams.ERROR_FINISH_LISTENER, mOnErrorFinishedListener).set(GalaVideoPlayerParams.SPECIAL_EVENT_LISTENER, onSpecialEventListener);
        IGalaVideoPlayer galaVideoPlayer = galaVideoPlayerFactory.createVideoPlayer(params);
        ISceneActionProvider playerSceneProvider = galaVideoPlayer.getSceneActionProvider();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createVideoPlayer() playerSceneProvider=" + playerSceneProvider);
        }
        if (multiEventHelper != null) {
            multiEventHelper.setContext(context);
            multiEventHelper.setOverlay(overlay);
            multiEventHelper.setEventInput(eventInput);
            multiEventHelper.registerPlayer(galaVideoPlayer);
            if (playType == SourceType.PUSH) {
                multiEventHelper.registerPlayStateListener(galaVideoPlayer);
            }
            multiEventHelper.addSceneActionProvider(playerSceneProvider);
        }
        galaVideoPlayer.setOnRedirectOutPageListener(new MyPlayCoreRedirectOutPageListener(context));
        galaVideoPlayer.setOnUpdateLayoutListener(new MyUpdateLayoutListener(galaPlayerView));
        galaVideoPlayer.setOnReleaseListener(mReleaseListener);
        galaVideoPlayer.setOnCarouselProgramClickListener(mOnCarouselProgramClickListener);
        return galaVideoPlayer;
    }

    private static ScreenMode checkInitialScreenModes(ScreenMode initialMode) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "checkInitialScreenModes: initial mode=" + initialMode);
        }
        return initialMode != null ? initialMode : ScreenMode.FULLSCREEN;
    }

    private static LayoutParams checkInitialLayoutParams(LayoutParams initialParams) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "checkInitialLayoutParams: initial params=" + initialParams);
        }
        return initialParams != null ? initialParams : new LayoutParams(-1, -1);
    }

    private static ISuperPlayerOverlay createMovieOverlay(SourceType playType, GalaPlayerView playerView, boolean supportWindowPlay, float windowZoomRatio) {
        if (playType == SourceType.CAROUSEL) {
            return new CarouselPlayerOverlay(playerView, supportWindowPlay, windowZoomRatio);
        }
        return new PlayerOverlay(playerView, supportWindowPlay, windowZoomRatio);
    }

    private static IErrorStrategy setupErrorStrategy(Context context, ScreenMode mode, IFeedbackDialogController feedBackController, IPlayerOverlay overlay) {
        return mode == ScreenMode.FULLSCREEN ? new FullscreenErrorStrategy(context, feedBackController, overlay) : new WindowedErrorStrategy(context, feedBackController, overlay);
    }

    public SurfaceView getSurfaceViewPlayerUsed(IGalaVideoPlayer player) {
        IVideoOverlay videoOverlay;
        if (player != null) {
            videoOverlay = player.getVideoOverlay();
        } else {
            videoOverlay = null;
        }
        if (videoOverlay != null) {
            return videoOverlay.getVideoSurfaceView();
        }
        return null;
    }

    private static void tryLoadIntertrustDrmPlugin() {
        if (!IntertrustDrmPluginProvider.isPluginLoadSuccess()) {
            new Thread(new IntertrustDrmPluginLoadTask(), "IntertrustDrmPluginLoadTask").start();
        }
    }
}
