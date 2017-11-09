package com.gala.video.app.player;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.KeyEvent;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.model.msg.Video;
import com.gala.multiscreen.dmr.model.type.Action;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.IPingbackValueProvider;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackItem;
import com.gala.pingback.PingbackStore.C0164E;
import com.gala.pingback.PingbackStore.EC;
import com.gala.pingback.PingbackStore.HCDN;
import com.gala.pingback.PingbackStore.ISPLAYERSTART;
import com.gala.pingback.PingbackStore.LOCALTIME;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PFEC;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.pingback.PingbackStore.RFR;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.ST;
import com.gala.pingback.PingbackStore.TD;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.widget.ExitPlayerPageDialog;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.app.player.utils.PlayerToastHelper;
import com.gala.video.app.player.utils.RCMultiKeyEventUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.IAppDownloadManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.KeyValue;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.ErrorUtils;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackContext;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.util.ArrayList;
import java.util.List;

public class PlayerActivity extends BasePlayActivity implements IPingbackContext {
    private static final int DELAY = 5000;
    public static final String PAGE_STATE_DETAIL_ERROR = "detailError";
    public static final String PAGE_STATE_PLAYER_ADPLAYING = "playerAdPlayling";
    public static final String PAGE_STATE_PLAYER_ERROR = "playerError";
    public static final String PAGE_STATE_PLAYER_LOADING = "playerLoading";
    public static final String PAGE_STATE_PLAYER_START = "playerStart";
    public static final String PAGE_STATE_UNKNOWN = "unknown";
    private static final String TAG = "PlayerActivity";
    private long mCallTime;
    private String mEc = "";
    private final boolean mEnableExtraPage = PlayerAppConfig.enableExtraPage();
    private boolean mFromDetailEpisode = false;
    private MyHandler mHandler;
    private boolean mIsAppDownloadComplete = false;
    private boolean mIsFirstPlay = true;
    private boolean mIsFirstPlayStarted;
    private KeyValue mKeyValue = new KeyValue();
    private boolean mNeedRefreshDetail = false;
    private Album mNowAlbum = null;
    private String mPageState = "unknown";
    private String mPfec = "";
    private final IPingbackContext mPingbackContext = new PingbackContext();
    private boolean mPreparedExit = false;
    private RCMultiKeyEventUtils mRCMultiKeyEventUtils;
    private PlayParams mSourceParams = null;
    private String mTip;

    class C12901 implements Runnable {
        C12901() {
        }

        public void run() {
            IAppDownloadManager appDownloadManager = CreateInterfaceTools.createAppDownloadManager();
            if (appDownloadManager != null) {
                appDownloadManager.startInstall();
            }
        }
    }

    private static class MyHandler extends Handler {
        private PlayerActivity context;

        public MyHandler(PlayerActivity a) {
            this.context = a;
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            this.context.mPreparedExit = false;
        }
    }

    protected void onCreate() {
        getWindow().setFormat(-2);
        getWindow().addFlags(128);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            setTheme(C1291R.style.AppTheme);
            log("onCreate: setTheme for home version");
        }
        initPingBack();
        this.mSourceParams = (PlayParams) getIntent().getExtras().getSerializable("play_list_info");
        log("onCreate: mSourceParams" + this.mSourceParams);
        if (this.mSourceParams != null) {
            this.mFromDetailEpisode = this.mSourceParams.isDetailEpisode;
        }
        this.mSupportWindow = false;
        this.mHandler = new MyHandler(this);
        this.mTip = getString(C1291R.string.str_exit_tip);
    }

    private void initPingBack() {
        this.mCallTime = getIntent().getLongExtra(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, -1);
        PingbackItem hcdn = HCDN.HCDN_TYPE(SystemConfigPreference.isOpenHCDN(this) ? "1" : "0");
        setItem("e", C0164E.E_ID(getIntent().getStringExtra("eventId")));
        setItem("hcdn", hcdn);
        setItem("rpage", RPAGE.PLAYER);
        long td = -1;
        if (this.mCallTime > 0) {
            td = SystemClock.uptimeMillis() - this.mCallTime;
        }
        PingbackFactory.instance().createPingback(1).addItem(getItem("e")).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).addItem(getItem("hcdn")).addItem(RPAGE.PLAYER).addItem(TD.TD_TYPE(String.valueOf(td))).post();
        updatePageState(PAGE_STATE_PLAYER_LOADING);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event == null || event.getKeyCode() < 7 || event.getKeyCode() > 16) {
            return super.handleKeyEvent(event);
        }
        if (this.mKeyValue.getReservedKeys().size() < 1) {
            this.mMultiEventHelper.onGetSceneAction(this.mKeyValue);
            this.mRCMultiKeyEventUtils = RCMultiKeyEventUtils.getInstance();
            this.mRCMultiKeyEventUtils.initialize(getApplicationContext(), this.mKeyValue);
        }
        if (this.mGalaVideoPlayer.getSourceVideo() == null) {
            return true;
        }
        Album album = this.mGalaVideoPlayer.getSourceVideo().getAlbum();
        if (album == null || !album.isSeries() || album.isSourceType() || this.mGalaVideoPlayer.getVideo() == null || this.mGalaVideoPlayer.getVideo().getAlbum() == null) {
            return true;
        }
        this.mRCMultiKeyEventUtils.onKeyDown(event, this.mGalaVideoPlayer.getVideo().getAlbum().order);
        return true;
    }

    public void onVideoSwitched(IVideo video, int type) {
        log("onVideoSwitched type" + type);
        if (type == 1 || ((type == 6 && this.mFromDetailEpisode) || type == 7 || type == 5 || type == 12 || type == 11)) {
            this.mNeedRefreshDetail = true;
            this.mNowAlbum = video.getAlbum();
            log("onVideoSwitched mNowAlbum" + this.mNowAlbum);
        }
    }

    public void finish() {
        super.finish();
        log("onVideoSwitched finish" + this.mNeedRefreshDetail);
        if (this.mNeedRefreshDetail) {
            AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
            builder.setAlbumInfo(this.mNowAlbum);
            builder.setFrom(getIntent().getExtras().getString("from"));
            builder.setTabSource(getIntent().getExtras().getString(IntentConfig2.INTENT_PARAM_TAB_SOURCE));
            builder.setBuySource(getIntent().getExtras().getString("buy_source"));
            builder.setClearTaskFlag(false);
            builder.setIsComplete(true);
            GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(this, builder);
            log("onVideoSwitched finish go");
        }
    }

    public void onPlaybackFinished() {
    }

    public boolean onError(IVideo video, ISdkError error) {
        updatePageState(PAGE_STATE_PLAYER_ERROR);
        this.mEc = "";
        this.mPfec = ErrorUtils.getPfEc(error);
        return false;
    }

    public void onScreenModeSwitched(ScreenMode newMode) {
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mIsPluginReady) {
            long td = -1;
            if (this.mCallTime > 0) {
                td = SystemClock.uptimeMillis() - this.mCallTime;
            }
            if (this.mIsAppDownloadComplete) {
                this.mIsAppDownloadComplete = false;
                this.mHandler.postDelayed(new C12901(), 1000);
            }
            PingbackFactory.instance().createPingback(14).addItem(TD.TD_TYPE(String.valueOf(td))).addItem(ST.ST_TYPE(this.mPageState)).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).addItem(EC.ST_TYPE(this.mEc)).addItem(PFEC.ST_TYPE(this.mPfec)).addItem(ISPLAYERSTART.ST_TYPE(this.mIsFirstPlayStarted ? "1" : "0")).addItem(getItem("e")).addItem(getItem("rpage")).addItem(getItem("hcdn")).post();
        }
    }

    public void onVideoStarted(IVideo video) {
        updatePageState(PAGE_STATE_PLAYER_START);
        this.mIsFirstPlay = false;
    }

    protected boolean needCheckUpdate() {
        return false;
    }

    private static boolean isMonkeyBackgroundRunning(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isMonkeyBackgroundRunning = " + ActivityManager.isUserAMonkey());
        }
        return ActivityManager.isUserAMonkey();
    }

    public void onBackPressed() {
        boolean needExitDialog;
        String from = getIntent().getStringExtra("from");
        if (from == null) {
            from = "";
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> onBackPressed, from=" + from);
        }
        if (this.mEnableExtraPage && (from.startsWith("openAPI") || from.equals(IntentConfig2.FROM_EXIT_DIALOG))) {
            needExitDialog = true;
        } else {
            needExitDialog = false;
        }
        if (needExitDialog && AppClientUtils.isBaseActivity(this, getClass().getName())) {
            exitWithDialog();
        } else if (this.mPreparedExit || PlayerAppConfig.isPlayerExitWhenPressBackOnce()) {
            super.onBackPressed();
        } else {
            this.mPreparedExit = true;
            PlayerToastHelper.showToast((Context) this, this.mTip, 5000);
            this.mHandler.sendEmptyMessageDelayed(0, IOpenApiCommandHolder.OAA_CONNECT_INTERVAL);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< onBackPressed");
        }
    }

    private void exitWithDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> exitWithDialog.");
        }
        Album album = (Album) getIntent().getSerializableExtra("albumInfo");
        String now_c1 = album == null ? "" : Integer.toString(album.chnId);
        String now_qpid = album == null ? "" : album.qpId;
        setItem("qtcurl", QTCURL.PLAYER);
        setItem("rfr", RFR.NULL);
        setItem("rpage", RPAGE.PLAYER);
        setItem("now_c1", NOW_C1.NOW_C1_TYPE(now_c1));
        setItem("now_qpid", NOW_QPID.NOW_QPID_TYPE(now_qpid));
        PingBackCollectionFieldUtils.setNow_qpid(now_qpid);
        PingBackCollectionFieldUtils.setNow_c1(now_c1);
        new ExitPlayerPageDialog(this).show();
    }

    public void onAdStarted() {
        updatePageState(PAGE_STATE_PLAYER_ADPLAYING);
    }

    public void updatePageState(String pageState) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updatePageState() pageState= " + pageState + ", mIsFirstPlay=" + this.mIsFirstPlay);
        }
        if (this.mIsFirstPlay) {
            this.mPageState = pageState;
            if (StringUtils.equals(pageState, PAGE_STATE_PLAYER_START)) {
                this.mIsFirstPlayStarted = true;
            }
        }
    }

    public Notify onPhoneSync() {
        Notify notify = this.mMultiEventHelper != null ? this.mMultiEventHelper.onPhoneSync() : null;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onResult(), ret = " + notify);
        }
        return notify;
    }

    public boolean onActionChanged(Action action) {
        if (action != Action.BACK) {
            return false;
        }
        finish();
        return true;
    }

    public boolean onKeyChanged(int keycode) {
        return this.mMultiEventHelper != null && this.mMultiEventHelper.onKeyChanged(keycode);
    }

    protected void onStop() {
        super.onStop();
        if (Project.getInstance().getControl().releasePlayerOnStop()) {
            finish();
        }
    }

    public boolean onResolutionChanged(String newRes) {
        return this.mMultiEventHelper != null && this.mMultiEventHelper.onResolutionChanged(newRes);
    }

    public boolean onSeekChanged(long newPosition) {
        return this.mMultiEventHelper != null && this.mMultiEventHelper.onSeekChanged(newPosition);
    }

    public long getPlayPosition() {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.getPlayPosition() : 0;
    }

    protected boolean onPushPlayList(List<Video> playList) {
        LogRecordUtils.logd(TAG, "onPushPlayList: playList.size" + (playList != null ? Integer.valueOf(playList.size()) : "0"));
        if (!(this.mGalaVideoPlayer == null || ListUtils.isEmpty((List) playList))) {
            List<Album> albumList = new ArrayList();
            for (Video video : playList) {
                Album album = new Album();
                album.tvQid = video.tvid;
                album.qpId = video.aid;
                if (StringUtils.equals(video.ctype, "3")) {
                    album.isLive = 1;
                }
                albumList.add(album);
            }
            this.mGalaVideoPlayer.setPushPlaylist(albumList);
        }
        return false;
    }

    public PingbackItem getItem(String key) {
        return this.mPingbackContext.getItem(key);
    }

    public void setItem(String key, PingbackItem item) {
        this.mPingbackContext.setItem(key, item);
    }

    public void setPingbackValueProvider(IPingbackValueProvider provider) {
        this.mPingbackContext.setPingbackValueProvider(provider);
    }

    public void onAdEnd() {
    }

    public void onPrepared() {
    }

    protected void startInstallApplication() {
        this.mIsAppDownloadComplete = true;
    }
}
