package com.gala.video.app.player;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.IPlayerActivity;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.utils.performance.GlobalPerformanceTracker;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.video.app.player.multiscreen.MultiEventHelper;
import com.gala.video.app.player.perftracker.LeftPerformanceMonitor;
import com.gala.video.app.player.provider.GalaPlayerPageProvider;
import com.gala.video.app.player.utils.VideoChecker;
import com.gala.video.app.player.utils.debug.DebugOptionsCache;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.widget.util.HomeMonitorHelper;
import com.gala.video.widget.util.HomeMonitorHelper.OnHomePressedListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class BasePlayActivity extends QMultiScreenActivity implements OnPlayerStateChangedListener, IPlayerActivity {
    private static final String KEY_BUNDLE = "KEY_BUNDLE";
    private static final int RESULT_CODE_NO_LOGIN = 0;
    private static final int RESULT_CODE_SUCCESS = 1;
    private static final String TAG = "Player/BasePlayActivity";
    private static final String TARGET_SEEK_POS = "target_seek_pos";
    private boolean mActivityPaused;
    private ViewGroup mContentView;
    protected IGalaVideoPlayer mGalaVideoPlayer = null;
    private boolean mHasNewIntent = false;
    private HomeMonitorHelper mHomeMonitorHelper;
    private boolean mIsIllegalCreated = false;
    protected boolean mIsPluginReady = true;
    private boolean mIsRegisterHomeMonitor = false;
    private boolean mIsReleasedPlayer = true;
    private MountReceiver mMountReceiver = new MountReceiver();
    protected IMultiEventHelper mMultiEventHelper = null;
    private int mResultCode = -1;
    private SourceType mSourceType;
    protected boolean mSupportWindow = false;
    private int mTargetSeekPos = -1;

    class C12751 implements OnHomePressedListener {
        C12751() {
        }

        public void onHomePressed() {
            BasePlayActivity.this.log("HomeMonitor home key pressed");
            BasePlayActivity.this.finish();
        }
    }

    class MountReceiver extends BroadcastReceiver {
        MountReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            LogUtils.m1571e(BasePlayActivity.TAG, "onReceive action=" + intent.getAction());
            LogUtils.m1571e(BasePlayActivity.TAG, "onReceive path=" + intent.getData().getPath());
            String diskpath = intent.getData().getPath();
            if (!(BasePlayActivity.this.mGalaVideoPlayer == null || BasePlayActivity.this.mGalaVideoPlayer.getVideo() == null)) {
                BasePlayActivity.this.mGalaVideoPlayer.getVideo().getTvId();
            }
            LogUtils.m1568d(BasePlayActivity.TAG, "currentVideoPath: " + null);
            if (BasePlayActivity.this.mGalaVideoPlayer != null && intent.getAction().equals("android.intent.action.MEDIA_EJECT") && BasePlayActivity.this.isCurVideoDiskEjected(diskpath, null)) {
                BasePlayActivity.this.mGalaVideoPlayer.finish();
            }
        }
    }

    protected abstract void onCreate();

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("KEY_BUNDLE", getIntent().getExtras());
    }

    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready()) {
            if (!Project.getInstance().getBuild().supportPlayerMultiProcess()) {
                if (savedInstanceState != null) {
                    getIntent().putExtras(savedInstanceState.getBundle("KEY_BUNDLE"));
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "onCreate :  getIntent().getExtras() = " + getIntent().getExtras());
                    }
                } else if (!GalaPlayerPageProvider.restoreIntentExtras(getIntent())) {
                    return;
                }
            }
            onCreate();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "[PERF-LOADING]tm_activity.create");
            }
            String eventId = getIntent().getStringExtra("eventId");
            GlobalPerformanceTracker.instance().recordPerformanceStepEnd(eventId, GlobalPerformanceTracker.ACTIVITY_CREATE_STEP);
            GlobalPerformanceTracker.instance().recordPerformanceStepStart(eventId, GlobalPerformanceTracker.PLAYER_PREF_INIT_STEP);
            this.mContentView = new FrameLayout(this);
            setContentView(this.mContentView);
            if (init(getIntent(), savedInstanceState)) {
                hideVolumeUI();
                IntentFilter iFilter = new IntentFilter();
                iFilter.addAction("android.intent.action.MEDIA_EJECT");
                iFilter.addDataScheme("file");
                registerReceiver(this.mMountReceiver, iFilter);
                registerHomeKeyForLauncher();
                return;
            }
            this.mIsIllegalCreated = true;
            finish();
            return;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "player plugin is not ready when onCreate, finish current activity !");
        }
        this.mIsIllegalCreated = true;
        this.mIsPluginReady = false;
        finish();
    }

    protected void onNewIntent(Intent intent) {
        LogUtils.m1568d(TAG, "onNewIntent()");
        GalaPlayerPageProvider.restoreIntentExtras(intent);
        super.onNewIntent(intent);
        if (this.mGalaVideoPlayer != null) {
            this.mGalaVideoPlayer.setOnMultiScreenStateChangeListener(null);
        }
        setIntent(intent);
        registerHomeKeyForLauncher();
        this.mHasNewIntent = true;
    }

    public void finish() {
        log("finish");
        super.finish();
    }

    private boolean init(Intent intent, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.mTargetSeekPos = savedInstanceState.getInt(TARGET_SEEK_POS, -1);
        } else {
            this.mTargetSeekPos = -1;
        }
        Bundle bundle = intent.getExtras();
        log("init: get bundle=" + bundle);
        if (bundle == null || bundle.isEmpty()) {
            Uri playUri = intent.getData();
            log("init: get playUri=" + playUri);
            if (playUri == null || playUri.getPath() == null) {
                return false;
            }
            bundle = createPlayBundleByUri(playUri);
            log("init: get bundle after createPlayBundleByUri=" + bundle);
        }
        this.mSourceType = getSourceTypeFromBundle(bundle);
        if (this.mSourceType == SourceType.PUSH) {
            TVApiBase.setOverSeaFlag(bundle.getBoolean("open_for_oversea", false));
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "PUSH VIDEO start, reset oversea flag!!!");
            }
        }
        bundle.putInt(PlayerIntentConfig2.INTENT_PARAM_RESULT_CODE, this.mResultCode);
        bundle.putString(PlayerIntentConfig2.INTENT_PARAM_PAGENAME, "player");
        this.mMultiEventHelper = new MultiEventHelper();
        this.mGalaVideoPlayer = GetInterfaceTools.getGalaVideoPlayerGenerator().createVideoPlayer(this, this.mContentView, bundle, this, ScreenMode.FULLSCREEN, null, null, this.mMultiEventHelper, null);
        this.mIsReleasedPlayer = false;
        return true;
    }

    private SourceType getSourceTypeFromBundle(Bundle bundle) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getSourceTypeFromBundle(" + bundle + ")");
        }
        if (bundle == null) {
            return SourceType.COMMON;
        }
        SourceType sourceTypeObj = bundle.get("videoType");
        if (sourceTypeObj == null) {
            return SourceType.COMMON;
        }
        if (sourceTypeObj instanceof SourceType) {
            return sourceTypeObj;
        }
        return SourceType.getByInt(((Integer) bundle.get("videoType")).intValue());
    }

    private Bundle createPlayBundleByUri(Uri uri) {
        Bundle bundle = new Bundle();
        String vrsAlbumId = uri.getQueryParameter("vrsAlbumId");
        String vrsTvId = uri.getQueryParameter("vrsTvId");
        String videoProgress = uri.getQueryParameter("history");
        bundle.putString("vrsAlbumId", vrsAlbumId);
        bundle.putString("vrsTvId", vrsTvId);
        bundle.putString("history", videoProgress);
        bundle.putString("from", "openAPI");
        bundle.putInt("videoType", SourceType.OUTSIDE.ordinal());
        return bundle;
    }

    private void hideVolumeUI() {
        sendBroadcast(new Intent("com.skyworthdigital.action.HIDE_VOLUME_UI"));
    }

    private synchronized void registerHomeKeyForLauncher() {
        if (!this.mIsRegisterHomeMonitor) {
            this.mHomeMonitorHelper = new HomeMonitorHelper(new C12751(), this);
            this.mIsRegisterHomeMonitor = true;
        }
    }

    protected void onResume() {
        super.onResume();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onResume() mTargetSeekPos=" + this.mTargetSeekPos);
        }
        if (this.mActivityPaused) {
            getIntent().putExtra(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, SystemClock.uptimeMillis());
            if (this.mIsReleasedPlayer) {
                if (!init(getIntent(), null)) {
                    this.mIsIllegalCreated = true;
                    finish();
                    return;
                }
            } else if (this.mGalaVideoPlayer == null || !this.mGalaVideoPlayer.isSleeping() || this.mResultCode != 0 || this.mHasNewIntent) {
                releasePlayer();
                if (!init(getIntent(), null)) {
                    this.mIsIllegalCreated = true;
                    finish();
                    return;
                }
            } else {
                this.mGalaVideoPlayer.wakeUp();
            }
        }
        this.mActivityPaused = false;
        this.mHasNewIntent = false;
        GetInterfaceTools.getIScreenSaver().exitHomeVersionScreenSaver(this);
    }

    public void onActionScrollEvent(KeyKind keyKind) {
        if (this.mMultiEventHelper != null) {
            this.mMultiEventHelper.onDlnaKeyEvent(DlnaKeyEvent.SCROLL, keyKind);
        }
    }

    public void onActionFlingEvent(KeyKind keyKind) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onActionFlingEvent(" + keyKind + ")");
        }
        if (this.mMultiEventHelper != null && !this.mMultiEventHelper.onDlnaKeyEvent(DlnaKeyEvent.FLING, keyKind)) {
            super.onActionFlingEvent(keyKind);
        }
    }

    public String onActionNotifyEvent(RequestKind kind, String message) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onActionNotifyEvent(" + kind + ", " + message + ")");
        }
        return null;
    }

    protected void onStop() {
        super.onStop();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onStop()");
        }
        if (!this.mIsIllegalCreated) {
        }
    }

    protected void onPause() {
        Album album;
        Intent intent;
        super.onPause();
        if (this.mSourceType == SourceType.PUSH) {
            TVApiBase.clearOverSeaFlag();
            LogUtils.m1568d(TAG, "PUSH VIDEO complete, clear oversea flag!!!");
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onPause()");
        }
        if (isLeftFloatingWindowEnabled(getApplicationContext())) {
            LeftPerformanceMonitor.getInstance(getApplicationContext()).stopPerf();
        }
        if (!(this.mGalaVideoPlayer == null || this.mGalaVideoPlayer.getVideo() == null || this.mGalaVideoPlayer.getSourceType() == SourceType.CAROUSEL || this.mGalaVideoPlayer.getSourceType() == SourceType.LIVE || this.mGalaVideoPlayer.getSourceType() == SourceType.PUSH)) {
            album = this.mGalaVideoPlayer.getVideo().getAlbum();
            intent = getIntent();
            album.playTime = -1;
            if (this.mGalaVideoPlayer.getSourceType() == SourceType.COMMON) {
                if (this.mGalaVideoPlayer.isCompleted()) {
                    HistoryInfo historyInfo = GetInterfaceTools.getIHistoryCacheManager().getAlbumHistory(album.qpId);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "onRun: local history info=" + historyInfo);
                    }
                    if (historyInfo == null) {
                        album.time = "";
                    } else {
                        int playOrder = historyInfo.getPlayOrder();
                        if (playOrder < 1) {
                            playOrder = 1;
                        }
                        String tvName = historyInfo.getAlbum().tvName;
                        album.order = playOrder;
                        String tvQid = historyInfo.getTvId();
                        if (VideoChecker.isValidTvId(tvQid)) {
                            album.tvQid = tvQid;
                            album.tvName = tvName;
                            album.time = historyInfo.getAlbum().time;
                            album.playTime = historyInfo.getAlbum().playTime;
                        }
                    }
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "onPause() currentAlbum=" + DataUtils.albumInfoToString(album));
                }
                intent.putExtra("episodePlayOrder", album.order);
            } else if (this.mGalaVideoPlayer.getSourceType() == SourceType.BO_DAN) {
                PlayParams sourceParams = (PlayParams) intent.getExtras().getSerializable("play_list_info");
                log("onPause: sourceParams" + sourceParams);
                if (sourceParams != null) {
                    List albumList = sourceParams.continuePlayList;
                    if (ListUtils.getCount(albumList) > 0) {
                        for (int i = 0; i < albumList.size(); i++) {
                            if (StringUtils.equals(((Album) albumList.get(i)).tvQid, album.tvQid)) {
                                sourceParams.playIndex = i;
                                log("find and put" + sourceParams);
                                intent.putExtra("play_list_info", sourceParams);
                                break;
                            }
                        }
                    }
                }
            }
            intent.putExtra("albumInfo", album);
        }
        if (this.mGalaVideoPlayer != null && this.mGalaVideoPlayer.isPlaying() && this.mGalaVideoPlayer.getVideo() != null && this.mGalaVideoPlayer.getSourceType() == SourceType.CAROUSEL) {
            TVChannelCarousel channel = this.mGalaVideoPlayer.getVideo().getCarouselChannel();
            if (channel != null) {
                album = new Album();
                album.live_channelId = String.valueOf(channel.id);
                album.chnName = channel.name;
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "onPause() currentCarouselChannel=" + channel.name);
                }
                intent = getIntent();
                intent.putExtra("albumInfo", album);
                intent.putExtra("carouselChannel", channel);
            }
        }
        this.mActivityPaused = true;
        if (!this.mIsIllegalCreated) {
            boolean isFinishing = isFinishing();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "isFinishing = " + isFinishing);
            }
            if (isFinishing) {
                releasePlayer();
                if (this.mGalaVideoPlayer != null) {
                    this.mGalaVideoPlayer.release();
                }
            } else if (this.mGalaVideoPlayer != null) {
                this.mGalaVideoPlayer.sleep();
            }
        }
    }

    private boolean isTemporaryPause() {
        boolean isTemporaryPause = false;
        try {
            Field f = Activity.class.getDeclaredField("mTemporaryPause");
            f.setAccessible(true);
            isTemporaryPause = f.getBoolean(this);
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "isTemporaryPause: reflection exception:", e);
        }
        return isTemporaryPause;
    }

    private void releasePlayer() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "releasePlayer()");
        }
        if (this.mGalaVideoPlayer != null) {
            this.mIsReleasedPlayer = true;
            this.mGalaVideoPlayer.release();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onActivityResult(resultCode=" + resultCode + ", requestCode=" + requestCode + ", data=" + data + ")");
        }
        getIntent().putExtra(PlayerIntentConfig2.INTENT_PARAM_OPEN_PAY_PAGE, false);
        this.mResultCode = resultCode;
        switch (requestCode) {
            case 1:
            case 2:
                if (resultCode != 1) {
                    finish();
                    return;
                }
                return;
            default:
                return;
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onDestroy()");
        }
        if (!this.mIsIllegalCreated) {
            if (this.mGalaVideoPlayer != null) {
                this.mGalaVideoPlayer.release();
            }
            unregisterReceiver(this.mMountReceiver);
            synchronized (this) {
                if (this.mHomeMonitorHelper != null) {
                    this.mHomeMonitorHelper.onDestory();
                }
                this.mIsRegisterHomeMonitor = false;
            }
        }
    }

    private boolean isLeftFloatingWindowEnabled(Context context) {
        return DebugOptionsCache.isPerfLeftFloatingWindowEnabled(context);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "BasePLayerActivity/List<AbsVoiceAction> getSupportedVoices()");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        if (this.mMultiEventHelper == null) {
            return actions;
        }
        if (this.mGalaVideoPlayer == null || !this.mGalaVideoPlayer.getVideo().isSourceType()) {
            return this.mMultiEventHelper.getSupportedVoices(actions);
        }
        return this.mMultiEventHelper.getSupportedVoicesWithoutPreAndNext(actions);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (this.mGalaVideoPlayer != null && this.mGalaVideoPlayer.handleKeyEvent(event)) {
            return true;
        }
        try {
            return super.handleKeyEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isCurVideoDiskEjected(String diskPath, String moviePath) {
        boolean ret;
        if (TextUtils.isEmpty(diskPath) || TextUtils.isEmpty(moviePath)) {
            ret = false;
        } else {
            ret = moviePath.startsWith(diskPath);
        }
        LogUtils.m1568d(TAG, "=====>isCurrentDiskEjected: " + ret);
        return ret;
    }

    protected void log(String mes) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, mes);
        }
    }
}
