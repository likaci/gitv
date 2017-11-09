package com.gala.video.app.player.albumdetail;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.IPingbackValueProvider;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackItem;
import com.gala.pingback.PingbackStore.CONTENT_TYPE;
import com.gala.pingback.PingbackStore.CUSTOMERPAGEEXIT.ITEM_CT;
import com.gala.pingback.PingbackStore.CUSTOMERPAGELOADED;
import com.gala.pingback.PingbackStore.E;
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
import com.gala.pingback.PingbackStore.S2;
import com.gala.pingback.PingbackStore.ST;
import com.gala.pingback.PingbackStore.TD;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.utils.performance.GlobalPerformanceTracker;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.controller.ActivityEventDispatcher;
import com.gala.video.app.player.controller.DataDispatcher;
import com.gala.video.app.player.controller.DetailViewPresenter;
import com.gala.video.app.player.controller.KeyDispatcher;
import com.gala.video.app.player.controller.MultiScreenEventDispatcher;
import com.gala.video.app.player.controller.UIEventDispatcher;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.data.DetailDataCacheManager;
import com.gala.video.app.player.provider.GalaPlayerPageProvider;
import com.gala.video.app.player.ui.config.AlbumDetailUiConfig;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.ui.widget.ExitPlayerPageDialog;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.pingback.AlbumDetailPingbackUtils;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackContext;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.widget.util.HomeMonitorHelper;
import com.gala.video.widget.util.HomeMonitorHelper.OnHomePressedListener;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailActivity extends QMultiScreenActivity implements IPingbackContext {
    private final String TAG = ("Detail/AlbumDetailActivity@" + Integer.toHexString(hashCode()));
    private int mActivityResultCode;
    private long mCallTime = -1;
    private DetailOverlay mDetailOverlay;
    private DetailViewPresenter mDetailPresenter;
    private final boolean mEnableExtraPage = PlayerAppConfig.enableExtraPage();
    private boolean mFinishedByOnCreate;
    private HomeMonitorHelper mHomeMonitorHelper;
    private boolean mIsRegisterHomeMonitor = false;
    private boolean mPageCreated = false;
    private long mPageExitStartTime;
    private boolean mPageLoadedAlreadySend = false;
    private final IPingbackContext mPingbackContext = new PingbackContext();
    private View mRootView;
    private IStatusListener mScreenSaverStatusListener = new IStatusListener() {
        public void onStart() {
            if (AlbumDetailActivity.this.mDetailOverlay != null) {
                AlbumDetailActivity.this.mDetailOverlay.notifyScreenSaverStart();
            }
        }

        public void onStop() {
            if (AlbumDetailActivity.this.mDetailOverlay != null) {
                AlbumDetailActivity.this.mDetailOverlay.notifyScreenSaverStop();
            }
        }
    };
    private IAlbumDetailUiConfig mUiConfig = new AlbumDetailUiConfig();
    private boolean mWindowHasFocus = false;

    protected void onSaveInstanceState(Bundle outState) {
        LogRecordUtils.logd(this.TAG, ">> onSaveInstanceState, outState=" + outState);
        getIntent().putExtra("albumdetailvideo", "");
        super.onSaveInstanceState(outState);
        outState.putBundle(DetailConstants.KEY_BUNDLE, getIntent().getExtras());
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        LogRecordUtils.logd(this.TAG, ">> onRestoreInstanceState, savedInstanceState=" + savedInstanceState);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogRecordUtils.logd(this.TAG, ">> onActivityResult, resultCode=" + resultCode);
        getIntent().putExtra(PlayerIntentConfig2.INTENT_PARAM_OPEN_PAY_PAGE, false);
        this.mActivityResultCode = resultCode;
    }

    protected void onCreate(Bundle savedInstanceState) {
        LogRecordUtils.logd(this.TAG, ">> onCreate, savedInstanceState=" + savedInstanceState);
        super.onCreate(savedInstanceState);
        setPingbackPage(PingbackPage.AlbumDetail);
        Intent passedIntent = getIntent();
        if (savedInstanceState != null) {
            passedIntent.putExtras(savedInstanceState.getBundle(DetailConstants.KEY_BUNDLE));
            LogRecordUtils.logd(this.TAG, "onCreate :  getIntent().getExtras() = " + passedIntent.getExtras());
            if (!GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready()) {
                this.mFinishedByOnCreate = true;
                finish();
                return;
            }
        } else if (!(Project.getInstance().getBuild().supportPlayerMultiProcess() || GalaPlayerPageProvider.restoreIntentExtras(passedIntent))) {
            LogRecordUtils.logd(this.TAG, "onCreate :  can't restore ");
            if (!GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready()) {
                this.mFinishedByOnCreate = true;
                finish();
                return;
            }
        }
        this.mFinishedByOnCreate = false;
        LogRecordUtils.logd(this.TAG, "[PERF-LOADING]tm_activity.create");
        String eventId = passedIntent.getStringExtra("eventId");
        GlobalPerformanceTracker.instance().recordPerformanceStepEnd(eventId, GlobalPerformanceTracker.ACTIVITY_CREATE_STEP);
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(eventId, GlobalPerformanceTracker.PLAYER_PREF_INIT_STEP);
        getWindow().setFormat(-2);
        getWindow().addFlags(128);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            setTheme(R.style.AppTheme);
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onCreate: setTheme for home version");
            }
        }
        setContentView(this.mUiConfig.getAlbumDetailLayoutId());
        this.mDetailOverlay = new DetailOverlay(this, getRootView(), this.mUiConfig);
        this.mDetailPresenter = new DetailViewPresenter(this, this.mDetailOverlay, getIntent());
        this.mDetailPresenter.initialize();
        initPingBack(passedIntent);
        registerHomeKeyForLauncher();
        this.mPageCreated = true;
    }

    protected void onStart() {
        LogRecordUtils.logd(this.TAG, ">> onStart begin");
        super.onStart();
        if (this.mFinishedByOnCreate) {
            LogRecordUtils.logd(this.TAG, ">> onStart mFinishedByOnCreate is true and return");
            return;
        }
        ActivityEventDispatcher.instance().onStart(this);
        LogRecordUtils.logd(this.TAG, ">> onStart end");
    }

    protected void onResume() {
        LogRecordUtils.logd(this.TAG, ">> onResume");
        super.onResume();
        if (this.mFinishedByOnCreate) {
            LogRecordUtils.logd(this.TAG, ">> onResume mFinishedByOnCreate is true and return");
            return;
        }
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenSaverStatusListener);
        ActivityEventDispatcher.instance().onResume(this, this.mActivityResultCode);
        LogRecordUtils.logd(this.TAG, "<< onResume end");
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        if (this.mFinishedByOnCreate) {
            LogRecordUtils.logd(this.TAG, ">> onWindowFocusChanged, hasFocus=" + hasFocus + ", isFinishing()=" + isFinishing() + ", but we finish in onCreate, so return");
            return;
        }
        LogRecordUtils.logd(this.TAG, ">> onWindowFocusChanged, hasFocus=" + hasFocus + ", isFinishing()=" + isFinishing());
        if (hasFocus) {
            this.mWindowHasFocus = true;
            sendCustomerPageLoadedPingback();
            return;
        }
        this.mWindowHasFocus = false;
        if (isFinishing()) {
            sendCustomerPageExitPingback();
        }
    }

    protected void onPause() {
        super.onPause();
        if (this.mFinishedByOnCreate) {
            LogRecordUtils.logd(this.TAG, ">> onPause mFinishedByOnCreate is true and return");
            return;
        }
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenSaverStatusListener);
        ActivityEventDispatcher.instance().onPause(this);
        if (isFinishing()) {
            LogRecordUtils.logd(this.TAG, ">> onPause, isFinishing() = true");
            ActivityEventDispatcher.instance().onFinishing(this);
            this.mDetailPresenter = null;
            this.mDetailOverlay = null;
        } else {
            LogRecordUtils.logd(this.TAG, ">> onPause, isFinishing() = false");
        }
        LogRecordUtils.logd(this.TAG, "<< onPause end");
    }

    protected void onStop() {
        LogRecordUtils.logd(this.TAG, ">> onStop begin");
        super.onStop();
        if (this.mFinishedByOnCreate) {
            LogRecordUtils.logd(this.TAG, ">> onStop mFinishedByOnCreate is true and return");
            return;
        }
        if (Project.getInstance().getControl().releasePlayerOnStop()) {
            finish();
        }
        LogRecordUtils.logd(this.TAG, "<< onStop end");
    }

    protected void onDestroy() {
        LogRecordUtils.logd(this.TAG, ">> onDestroy");
        super.onDestroy();
        if (this.mFinishedByOnCreate) {
            LogRecordUtils.logd(this.TAG, ">> onDestroy mFinishedByOnCreate is true and return");
            return;
        }
        if (!(this.mDetailPresenter == null && this.mDetailOverlay == null)) {
            LogRecordUtils.logd(this.TAG, ">> onDestroy, release all resources.");
            ActivityEventDispatcher.instance().onFinishing(this);
            this.mDetailPresenter = null;
            this.mDetailOverlay = null;
        }
        ((ViewGroup) getRootView()).removeAllViews();
        this.mRootView = null;
        DetailDataCacheManager.instance().clearCurrentDetailInfo();
        synchronized (this) {
            if (this.mHomeMonitorHelper != null) {
                this.mHomeMonitorHelper.onDestory();
            }
            this.mIsRegisterHomeMonitor = false;
        }
        if (this.mPageCreated) {
            sendPageExitPingback();
        }
        LogRecordUtils.logd(this.TAG, "<< onDestroy end");
    }

    protected void startInstallApplication() {
        if (this.mDetailOverlay != null) {
            LogRecordUtils.logd(this.TAG, "startInstallApplication: CurrentScreenMode -> " + this.mDetailOverlay.getCurrentScreenMode());
            if (this.mDetailOverlay.getCurrentScreenMode() == ScreenMode.WINDOWED) {
                startInstallApp();
            } else {
                this.mDetailOverlay.setAppDownloadComplete(true);
            }
        }
    }

    private void releaseDispatchers() {
        DataDispatcher.release();
        UIEventDispatcher.release();
        ActivityEventDispatcher.release();
        KeyDispatcher.release();
        MultiScreenEventDispatcher.release();
    }

    protected View getBackgroundContainer() {
        return getRootView();
    }

    private View getRootView() {
        if (this.mRootView == null) {
            this.mRootView = findViewById(16908290);
            LogRecordUtils.logd(this.TAG, "getRootView()");
        }
        return this.mRootView;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (!this.mWindowHasFocus) {
            LogRecordUtils.logd(this.TAG, "handleKeyEvent, this window does not has focus, block key event.");
            return true;
        } else if (isFinishing()) {
            LogRecordUtils.logd(this.TAG, ">> activity is finishing, no need to handle any key.");
            return true;
        } else if (event.getAction() == 1) {
            return super.handleKeyEvent(event);
        } else {
            if (KeyDispatcher.instance().dispatchKeyEvent(event)) {
                return true;
            }
            if (event.getKeyCode() == 4 && event.getAction() == 0) {
                handleExitEvent();
                return true;
            }
            try {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "handleKeyEvent, unhandled key event=" + event);
                }
                return super.handleKeyEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private void handleExitEvent() {
        String from = getIntent().getStringExtra("from");
        if (from == null) {
            from = "";
        }
        LogRecordUtils.logd(this.TAG, ">> handleExitEvent, mFrom=" + from + ", mEnableExtraPage=" + this.mEnableExtraPage);
        boolean needExitDialog = this.mEnableExtraPage && (from.startsWith("openAPI") || from.equals(IntentConfig2.FROM_EXIT_DIALOG));
        if (needExitDialog && AppClientUtils.isBaseActivity(this, getClass().getName())) {
            showExitDialog();
        } else {
            finish();
        }
    }

    private void showExitDialog() {
        LogRecordUtils.logd(this.TAG, ">> showExitDialog");
        IVideo current = this.mDetailPresenter.getCurrentVideo();
        String now_c1 = current != null ? Integer.toString(current.getChannelId()) : "";
        String now_qpid = current != null ? current.getAlbumId() : "";
        setItem("qtcurl", QTCURL.DETAIL);
        setItem("rpage", RPAGE.DETAIL);
        setItem("now_c1", NOW_C1.NOW_C1_TYPE(now_c1));
        setItem("now_qpid", NOW_QPID.NOW_QPID_TYPE(now_qpid));
        PingBackCollectionFieldUtils.setNow_qpid(now_qpid);
        PingBackCollectionFieldUtils.setNow_c1(now_c1);
        new ExitPlayerPageDialog(this).show();
    }

    public void finish() {
        LogRecordUtils.logd(this.TAG, ">> finish");
        this.mPageExitStartTime = SystemClock.uptimeMillis();
        super.finish();
        LogRecordUtils.logd(this.TAG, "<< finish");
    }

    private synchronized void registerHomeKeyForLauncher() {
        if (!this.mIsRegisterHomeMonitor) {
            this.mHomeMonitorHelper = new HomeMonitorHelper(new OnHomePressedListener() {
                public void onHomePressed() {
                    LogRecordUtils.logd(AlbumDetailActivity.this.TAG, "HomeMonitor home key pressed");
                    AlbumDetailActivity.this.finish();
                }
            }, this);
            this.mIsRegisterHomeMonitor = true;
        }
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        LogRecordUtils.logd(this.TAG, "(AblumDeltalActivity)/getSupportedVoices()");
        List<AbsVoiceAction> actions = new ArrayList();
        if (this.mDetailPresenter != null) {
            return this.mDetailPresenter.getSupportedVoices(actions);
        }
        return actions;
    }

    public Notify onPhoneSync() {
        return MultiScreenEventDispatcher.instance().onPhoneSync();
    }

    public boolean onKeyChanged(int keycode) {
        return MultiScreenEventDispatcher.instance().onKeyChanged(keycode);
    }

    public boolean onResolutionChanged(String newRes) {
        return MultiScreenEventDispatcher.instance().onResolutionChanged(newRes);
    }

    public boolean onSeekChanged(long newPosition) {
        return MultiScreenEventDispatcher.instance().onSeekChanged(newPosition);
    }

    public long getPlayPosition() {
        return MultiScreenEventDispatcher.instance().getPlayPosition();
    }

    public void setItem(String key, PingbackItem item) {
        this.mPingbackContext.setItem(key, item);
    }

    public PingbackItem getItem(String key) {
        return this.mPingbackContext.getItem(key);
    }

    public void setPingbackValueProvider(IPingbackValueProvider provider) {
        this.mPingbackContext.setPingbackValueProvider(provider);
    }

    private void initPingBack(Intent passedIntent) {
        this.mCallTime = passedIntent.getLongExtra(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, -1);
        PingbackItem e = E.E_ID(passedIntent.getStringExtra("eventId"));
        PingbackItem hcdn = HCDN.HCDN_TYPE(SystemConfigPreference.isOpenHCDN(this) ? "1" : "0");
        String s2 = passedIntent.getStringExtra("from");
        AlbumDetailPingbackUtils.getInstance().setS2(s2);
        AlbumDetailPingbackUtils.getInstance().setAllViewS2("detail");
        setItem("e", e);
        setItem("hcdn", hcdn);
        setItem("rpage", RPAGE.DETAIL);
        setItem("rfr", RFR.RESOURCE);
        setItem("s2", S2.ITEM(s2));
        PingBackCollectionFieldUtils.setE(getItem("e").getValue());
        PingBackCollectionFieldUtils.setRfr(getItem("rfr").getValue());
        long td = -1;
        if (this.mCallTime > 0) {
            td = SystemClock.uptimeMillis() - this.mCallTime;
        }
        PingbackFactory.instance().createPingback(1).addItem(getItem("e")).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).addItem(getItem("hcdn")).addItem(getItem("rpage")).addItem(TD.TD_TYPE(String.valueOf(td))).post();
    }

    private void sendCustomerPageExitPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> sendCustomerPageExitPingback");
        }
        long now = SystemClock.uptimeMillis();
        long td = -1;
        if (this.mPageExitStartTime > 0) {
            td = now - this.mPageExitStartTime;
        }
        PingbackFactory.instance().createPingback(43).addItem(ITEM_CT.EXIT).addItem(TD.TD_TYPE(Long.toString(td))).addItem(CONTENT_TYPE.ITEM("")).addItem(this.mPingbackContext.getItem("hcdn")).addItem(this.mPingbackContext.getItem("e")).addItem(ST.ST_TYPE("")).addItem(ISPLAYERSTART.ST_TYPE("")).post();
    }

    private void sendCustomerPageLoadedPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> sendCustomerPageLoadedPingback");
        }
        if (!this.mPageLoadedAlreadySend) {
            long now = SystemClock.uptimeMillis();
            long td = -1;
            if (this.mCallTime > 0) {
                td = now - this.mCallTime;
            }
            PingbackFactory.instance().createPingback(42).addItem(CUSTOMERPAGELOADED.ITEM_CT.LOADED).addItem(TD.TD_TYPE(Long.toString(td))).addItem(CONTENT_TYPE.ITEM("")).addItem(this.mPingbackContext.getItem("hcdn")).addItem(this.mPingbackContext.getItem("e")).post();
            this.mPageLoadedAlreadySend = true;
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendCustomerPageLoadedPingback, send already.");
        }
    }

    private void sendPageExitPingback() {
        long td = -1;
        if (this.mCallTime > 0) {
            td = SystemClock.uptimeMillis() - this.mCallTime;
        }
        PingbackFactory.instance().createPingback(14).addItem(getItem("e")).addItem(TD.TD_TYPE(String.valueOf(td))).addItem(ST.ST_TYPE("")).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).addItem(EC.ST_TYPE("")).addItem(PFEC.ST_TYPE("")).addItem(ISPLAYERSTART.ST_TYPE("")).addItem(getItem("rpage")).addItem(getItem("hcdn")).post();
    }
}
