package com.gala.video.app.epg;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.IPingbackValueProvider;
import com.gala.pingback.PingbackItem;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.app.epg.home.ads.AdImageResTaskStrategy;
import com.gala.video.app.epg.home.ads.controller.StartScreenAdHandler;
import com.gala.video.app.epg.home.ads.view.GlobalExitAppDialog;
import com.gala.video.app.epg.home.controller.HomeController;
import com.gala.video.app.epg.home.controller.Tactic;
import com.gala.video.app.epg.home.controller.activity.ActivityLifeCycleDispatcher;
import com.gala.video.app.epg.home.controller.key.KeyEventDispatcher;
import com.gala.video.app.epg.home.data.constants.HomeConstants;
import com.gala.video.app.epg.home.data.hdata.DataRequestTaskStrategy;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.view.ViewDebug;
import com.gala.video.app.epg.home.widget.menufloatlayer.ui.MenuFloatLayerWindow;
import com.gala.video.app.epg.home.widget.tabhost.TabBarHost;
import com.gala.video.app.epg.screensaver.ScreenSaverStartTool;
import com.gala.video.app.epg.startup.BaseStartUpPresenter;
import com.gala.video.app.epg.startup.BaseStartUpPresenter.onPreviewCompleteListener;
import com.gala.video.app.epg.startup.StartUpCostInfoProvider;
import com.gala.video.app.epg.ui.imsg.dialog.MsgDialogHelper;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ProcessHelper;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.configs.IntentConfig;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifimpl.databus.HomeEvent;
import com.gala.video.lib.share.ifimpl.ucenter.account.helper.AccountInfoTipHelper;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade.IUpdateManager.UpdateOperation;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.pingback.PingbackContext;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import org.greenrobot.eventbus.EventBus;

public class HomeActivity extends QMultiScreenActivity implements IPingbackContext {
    private static final String TAG = "home/HomeActivity";
    private CardFocusHelper mCardFocusHelper;
    private boolean mDisablePreview = false;
    private GlobalExitAppDialog mExitDialog;
    private boolean mFirstload = false;
    private View mFocusedView;
    private HomeController mHomeController;
    private boolean mIsPaused = false;
    private boolean mIsShowPreviewComplete = false;
    private onPreviewCompleteListener mOnPreviewCompletedListener = new C05068();
    private final IPingbackContext mPingbackContext = new PingbackContext();
    private View mPlaceHolder;
    private View mRootView;
    private boolean mShowExitDialog = false;
    private MyObserver mStartUpErrorObserver = new C05036();
    private BaseStartUpPresenter mStartupPresenter = null;
    private TabBarHost mTabHosts;
    private View mTableSetting;
    private int mTargetPage = -1;
    private MyObserver mUpdateActionBarObserver = new C05057();
    private MyObserver mUpgradeObserver = new C05015();
    private MenuFloatLayerWindow menuFloatLayerWindow = null;

    class C04961 implements OnDismissListener {
        C04961() {
        }

        public void onDismiss(DialogInterface dialog) {
            HomeActivity.this.mShowExitDialog = false;
        }
    }

    class C04972 implements OnClickListener {
        C04972() {
        }

        public void onClick(View v) {
            HomeActivity.this.onExitApp();
        }
    }

    class C04983 implements OnClickListener {
        C04983() {
        }

        public void onClick(View v) {
            HomeActivity.this.mShowExitDialog = false;
        }
    }

    class C04994 implements UpdateOperation {
        C04994() {
        }

        public void exitApp() {
            PingBackParams params = new PingBackParams();
            params.add(Keys.f2035T, "14").add("r", Values.value00001);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
            HomeActivity.this.onExitApp();
        }

        public void cancelUpdate() {
        }
    }

    class C05015 implements MyObserver {

        class C05001 implements Runnable {
            C05001() {
            }

            public void run() {
                HomeActivity.this.onReceiveUpgradeEvent();
            }
        }

        C05015() {
        }

        public void update(String event) {
            LogUtils.m1568d(HomeActivity.TAG, "receive upgrade event");
            HomeActivity.this.runOnUiThread(new C05001());
        }
    }

    class C05036 implements MyObserver {

        class C05021 implements Runnable {
            C05021() {
            }

            public void run() {
                LogUtils.m1568d(HomeActivity.TAG, "receive start up event ");
                HomeActivity.this.onReceiveStartUpEvent(DeviceCheckModel.getInstance().getErrorEvent());
            }
        }

        C05036() {
        }

        public void update(String event) {
            HomeActivity.this.runOnUiThread(new C05021());
        }
    }

    class C05057 implements MyObserver {

        class C05041 implements Runnable {
            C05041() {
            }

            public void run() {
                HomeActivity.this.mHomeController.updateActionBar();
            }
        }

        C05057() {
        }

        public void update(String event) {
            HomeActivity.this.runOnUiThread(new C05041());
        }
    }

    class C05068 implements onPreviewCompleteListener {
        C05068() {
        }

        public void onStartCompleted(boolean nextActivity) {
            boolean z = true;
            LogUtils.m1568d(HomeActivity.TAG, "preview show completed,next activity = " + nextActivity);
            HomeConstants.mIsStartPreViewFinished = true;
            HomeActivity.this.mHomeController.getActionBarPresenter().startVipAnimation(false);
            HomeActivity.this.mIsShowPreviewComplete = true;
            GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.STARTUP_UPGRADE_EVENT, HomeActivity.this.mUpgradeObserver);
            GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.STARTUP_ERROR_EVENT, HomeActivity.this.mStartUpErrorObserver);
            GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.SHOW_PREVIEW_COMPLETED);
            EventBus.getDefault().postSticky(new HomeEvent(IDataBus.SHOW_PREVIEW_COMPLETED));
            AccountInfoTipHelper.checkUserInfo(HomeActivity.this);
            HomeActivity.this.mStartupPresenter = null;
            MsgDialogHelper msgDialogHelper = MsgDialogHelper.get();
            if (nextActivity) {
                z = false;
            }
            msgDialogHelper.setHomeActivityFlag(z, HomeActivity.this);
            StartUpCostInfoProvider.getInstance().onPreviewCompleted(SystemClock.elapsedRealtime());
        }
    }

    class C05079 implements PopupWindow.OnDismissListener {
        C05079() {
        }

        public void onDismiss() {
            if (Build.MODEL.toLowerCase().contains("mibox3")) {
                HomeActivity.this.restoreFocus();
            }
            HomeActivity.this.menuFloatLayerWindow = null;
        }
    }

    private View getRootView() {
        if (this.mRootView == null) {
            this.mRootView = findViewById(16908290);
        }
        return this.mRootView;
    }

    protected void onCreate(Bundle savedInstanceState) {
        isHomeStarted = true;
        setTheme(C0508R.style.AppThemeBlack);
        super.onCreate(savedInstanceState);
        long start = SystemClock.elapsedRealtime();
        if (!AppStartMode.IS_PLUGIN_MODE) {
            StartScreenAdHandler.instance().init(this);
            StartScreenAdHandler.instance().start();
        }
        setPingbackPage(PingbackPage.HomePage);
        AppRuntimeEnv.get().setStartTime(SystemClock.elapsedRealtime());
        getWindow().setFormat(-2);
        UpdateManager.getInstance().reset();
        setContentView(C0508R.layout.epg_activity_home_new);
        this.mCardFocusHelper = new CardFocusHelper(findViewById(C0508R.id.card_focus));
        LayoutParams lp = getRootView().findViewById(C0508R.id.epg_pager).getLayoutParams();
        if (lp instanceof MarginLayoutParams) {
            this.mCardFocusHelper.setInvisiableMarginTop(((MarginLayoutParams) lp).topMargin);
        }
        parseIntent(getIntent());
        this.mHomeController = new HomeController(this.mTargetPage);
        this.mHomeController.init(this, getRootView());
        startDownLoadAdResource();
        this.mTabHosts = (TabBarHost) getRootView().findViewById(C0508R.id.epg_tab_host);
        this.mPlaceHolder = getRootView().findViewById(C0508R.id.epg_placeholder);
        this.mTableSetting = getRootView().findViewById(C0508R.id.epg_home_tab_setting_view);
        DataRequestTaskStrategy.getInstance().notifyHomeKeyEvent();
        if (this.mDisablePreview) {
            this.mOnPreviewCompletedListener.onStartCompleted(false);
            LogUtils.m1568d(TAG, "onCreate, preview complete >>> onStartCompleted()");
        } else {
            this.mStartupPresenter = new StartupPresenter(this);
            this.mStartupPresenter.setOnShowPreViewListener(this.mOnPreviewCompletedListener);
            this.mStartupPresenter.start();
            this.mStartupPresenter.show((FrameLayout) getRootView());
        }
        this.mFirstload = true;
        LogUtils.m1568d(TAG, "home activity create cost " + (SystemClock.elapsedRealtime() - start) + " ms");
        sendStartupPingback();
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            PingBackUtils.setTabSrc("tab_" + HomePingbackSender.getInstance().getTabName());
        }
        if (this.mIsPaused) {
            if (hasFocus && this.mIsPaused) {
                restoreFocus();
                this.mIsPaused = false;
            }
        } else if (hasFocus) {
            restoreFocus();
        } else {
            this.mFocusedView = this.mRootView.findFocus();
            this.mPlaceHolder.setFocusable(true);
            this.mPlaceHolder.requestFocus();
        }
        LogUtils.m1568d(TAG, "onWindowFocusChanged hasFocus = " + hasFocus);
    }

    private void restoreFocus() {
        if (this.mFocusedView != null && this.mPlaceHolder.isFocused()) {
            LogUtils.m1568d(TAG, "focus view getWindowVisibility = " + this.mFocusedView.getWindowVisibility() + ",focused view = " + this.mFocusedView);
            if (this.mFocusedView.getWindowVisibility() == 0) {
                this.mFocusedView.requestFocus();
            } else if (this.mTabHosts != null) {
                this.mTabHosts.requestChildFocus(this.mTabHosts.getFocusChildIndex());
            }
            this.mFocusedView = null;
        }
        this.mPlaceHolder.setFocusable(false);
    }

    private void showExitDialog() {
        if (!this.mShowExitDialog) {
            this.mExitDialog = new GlobalExitAppDialog(this);
            this.mExitDialog.setOnDismissListener(new C04961());
            this.mExitDialog.setOnBtnClickListener(new C04972(), new C04983());
            this.mExitDialog.show();
            this.mShowExitDialog = true;
        }
    }

    private void showExitUpdateDialog() {
        LogUtils.m1568d(TAG, "showExitUpdateDialog()");
        UpdateManager.getInstance().showExitUpdateDialog(this, new C04994());
    }

    public void onBackPressed() {
        LogUtils.m1568d(TAG, "on back key pressed");
        if (!this.mIsShowPreviewComplete) {
            popExitDialog();
        } else if (!this.mTabHosts.hasFocus() && !this.mTableSetting.hasFocus()) {
            this.mHomeController.backToTop();
            this.mTabHosts.requestChildFocus(this.mTabHosts.getCurrentChildIndex());
            HomePingbackFactory.instance().createPingback(ClickPingback.PRESS_BACK_KEY_CLICK_PINGBACK).addItem("r", "back").addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "back").addItem("rt", "i").addItem("count", HomePingbackSender.getInstance().getTabIndex()).addItem("rseat", "back").setOthersNull().post();
        } else if (this.mTabHosts.getDefaultFocusIndex() != this.mTabHosts.getCurrentChildIndex()) {
            this.mTabHosts.reset();
            this.mTabHosts.requestChildFocus(this.mTabHosts.getDefaultFocusIndex());
        } else {
            popExitDialog();
        }
    }

    private void popExitDialog() {
        if (!Project.getInstance().getBuild().isHomeVersion()) {
            if (UpdateManager.getInstance().isNeedShowExitUpdateDialog()) {
                showExitUpdateDialog();
            } else {
                showExitDialog();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        if (this.mFirstload) {
            new ScreenSaverStartTool().StartSS();
        }
        GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.UPDADE_ACTION_BAR, this.mUpdateActionBarObserver);
        if (this.mIsShowPreviewComplete) {
            GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.STARTUP_UPGRADE_EVENT, this.mUpgradeObserver);
            GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.STARTUP_ERROR_EVENT, this.mStartUpErrorObserver);
            MsgDialogHelper.get().setHomeActivityFlag(true, this);
        }
        Project.getInstance().getConfig().initHomeStart(this);
        DataRequestTaskStrategy.getInstance().notifyHomeResumed();
        ActivityLifeCycleDispatcher.get().onResume();
        UikitDataCache.getInstance().setHomeActivityState(true);
        PingBackUtils.setTabSrc("tab_" + HomePingbackSender.getInstance().getTabName());
    }

    protected void onPause() {
        super.onPause();
        NetworkStatePresenter.getInstance().onStop();
        this.mIsPaused = true;
        this.mFirstload = false;
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.UPDADE_ACTION_BAR, this.mUpdateActionBarObserver);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.STARTUP_UPGRADE_EVENT, this.mUpgradeObserver);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.STARTUP_ERROR_EVENT, this.mStartUpErrorObserver);
        Project.getInstance().getConfig().initHomeEnd();
        ActivityLifeCycleDispatcher.get().onPause();
        MsgDialogHelper.get().setHomeActivityFlag(false, null);
    }

    protected boolean isUsingSystemWallPaper() {
        return false;
    }

    public void onReceiveUpgradeEvent() {
        showUpdateDialog(false);
    }

    private void onReceiveStartUpEvent(ErrorEvent event) {
        this.mHomeController.onStartUpEvent(event);
    }

    public String onActionNotifyEvent(RequestKind kind, String message) {
        LogUtils.m1568d(TAG, "dlna event,onActionNotifyEvent kind(" + kind + ")");
        this.mHomeController.onDlnaActionNotifyEvent(kind);
        return null;
    }

    public void onActionFlingEvent(KeyKind kind) {
        LogUtils.m1568d(TAG, "dlna event,onActionFlingEvent,KeyKind(" + kind + ")");
        MultiScreen.get().sendSysKey(this, kind);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if ((this.mStartupPresenter != null && this.mStartupPresenter.dispatchKeyEvent(event)) || KeyEventDispatcher.get().onKeyDown(event)) {
            return true;
        }
        DataRequestTaskStrategy.getInstance().notifyHomeKeyEvent();
        UikitDataCache.getInstance().notifyHomeKeyEvent();
        if (event.getKeyCode() == 82) {
            onMenuKeyPressedForMenuFloatLayer(event);
        }
        return super.handleKeyEvent(event);
    }

    public Notify onPhoneSync() {
        LogUtils.m1568d(TAG, "dlna event,onPhoneSync");
        return null;
    }

    public boolean onKeyChanged(int keycode) {
        LogUtils.m1568d(TAG, "dlna event,onKeyChanged,keycode(" + keycode + ")");
        return false;
    }

    public void onActionScrollEvent(KeyKind keyKind) {
        LogUtils.m1568d(TAG, "dlna event,onKeyChanged,keyKind(" + keyKind + ")");
    }

    protected void onNewIntent(Intent intent) {
        LogUtils.m1568d(TAG, "onNewIntent()");
        super.onNewIntent(intent);
        reset();
        onHomeKeyPressed();
    }

    private void reset() {
        if (Project.getInstance().getBuild().isHomeVersion()) {
            this.mTabHosts.reset();
            this.mTabHosts.requestChildFocus(this.mTabHosts.getDefaultFocusIndex());
        }
    }

    protected View getBackgroundContainer() {
        return getRootView();
    }

    public void setBackground(View container) {
        super.setBackground(container);
        GetInterfaceTools.getIBackgroundManager().setBackgroundDrawable(SystemConfigPreference.getNightModeBackground(getApplicationContext()));
        if (this.mTabHosts != null) {
            this.mTabHosts.updateFadeView(Project.getInstance().getControl().getBackgroundDrawable());
        }
    }

    protected void onStart() {
        super.onStart();
        LogUtils.m1568d(TAG, "onStart");
        this.mHomeController.onStart();
        ActivityLifeCycleDispatcher.get().onStart();
        NetworkStatePresenter.getInstance().setContext(this);
    }

    protected void onStop() {
        boolean isDevicesMiBox = false;
        super.onStop();
        this.mHomeController.onStop();
        DataRequestTaskStrategy.getInstance().notifyHomeStopped();
        UikitDataCache.getInstance().setHomeActivityState(false);
        ActivityLifeCycleDispatcher.get().onStop();
        String model = Build.MODEL;
        LogUtils.m1568d(TAG, "model：" + model);
        LogUtils.m1568d(TAG, "isLoaderWEBActivity：" + isLoaderWEBActivity);
        if (model.toLowerCase().equals("mibox3") || model.toLowerCase().equals("mibox3s")) {
            isDevicesMiBox = true;
        }
        if (model != null && isDevicesMiBox && !isLoaderWEBActivity) {
            try {
                cleanMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onDestroy() {
        LogUtils.m1568d(TAG, "onDestroy()");
        super.onDestroy();
        ActivityLifeCycleDispatcher.get().onDestroy();
        this.mHomeController.destroy();
        if (this.mStartupPresenter != null) {
            this.mStartupPresenter.setOnShowPreViewListener(null);
        }
        Tactic.setUIController(null);
        Tactic.setUIEvnet(null);
        isHomeStarted = false;
        this.mCardFocusHelper.destroy();
    }

    public void onExitApp() {
        LogUtils.m1568d(TAG, "onExitApp()");
        super.onExitApp();
        try {
            GetInterfaceTools.getILogRecordProvider().getLogCore().release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (GetInterfaceTools.isPlayerLoaded()) {
            CreateInterfaceTools.createPlayerExitHelper().clearCarouselSharePre(this);
        }
        GetInterfaceTools.getStartupDataLoader().stop();
        DataRequestTaskStrategy.getInstance().clear();
        GetInterfaceTools.getIScreenSaver().stop();
        GetInterfaceTools.getISubscribeProvider().reset();
        int pid = ProcessHelper.getInstance().getProcessPid(getPackageName() + ":player");
        if (pid > 0) {
            Process.killProcess(pid);
        }
        Process.killProcess(Process.myPid());
    }

    private void startDownLoadAdResource() {
        AdImageResTaskStrategy.getInstance().fetchAdImageRes();
    }

    private void parseIntent(Intent intent) {
        if (intent != null) {
            this.mDisablePreview = intent.getBooleanExtra(IntentConfig.DISABLE_START_PREVIEW, false);
            this.mTargetPage = intent.getIntExtra(IntentConfig.OPENAPI_HOME_TARGET_PAGE, -1);
            LogUtils.m1568d(TAG, "receive intent disable preview = " + this.mDisablePreview + ",priority page = " + this.mTargetPage);
        }
    }

    public void setItem(String s, PingbackItem pingbackItem) {
        this.mPingbackContext.setItem(s, pingbackItem);
    }

    public PingbackItem getItem(String s) {
        return this.mPingbackContext.getItem(s);
    }

    public void setPingbackValueProvider(IPingbackValueProvider provider) {
        this.mPingbackContext.setPingbackValueProvider(provider);
    }

    private void sendStartupPingback() {
        HomePingbackFactory.instance().createPingback(CommonPingback.START_PINGBACK).addItem(Keys.f2035T, "3").addItem("r", Values.value00001).addItem(Keys.LAUNCHER, PingBackUtils.getLauncherPackageName(AppRuntimeEnv.get().getApplicationContext())).addItem("td", String.valueOf(SystemClock.elapsedRealtime() - AppRuntimeEnv.get().getStartTime())).addItem(Keys.AUTO_START, "").post();
    }

    private void onMenuKeyPressedForMenuFloatLayer(KeyEvent keyEvent) {
        if (keyEvent.getAction() == 0) {
            HomePingbackFactory.instance().createPingback(ClickPingback.PRESS_MENU_KEY_PINGBACK).addItem("r", "menu").addItem("block", "menu").addItem("rt", "i").addItem("rseat", "menu").addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
            if (this.menuFloatLayerWindow == null) {
                this.menuFloatLayerWindow = new MenuFloatLayerWindow(this);
                this.menuFloatLayerWindow.show(getRootView());
                this.menuFloatLayerWindow.setOnDismissListener(new C05079());
                return;
            }
            LogUtils.m1571e(TAG, "onMenuKeyPressed, MenuFloatLayerWindow has not been recycled, menuFloatLayerWindow = " + this.menuFloatLayerWindow);
        }
    }

    private void onHomeKeyPressed() {
        if (this.menuFloatLayerWindow != null && this.menuFloatLayerWindow.isShowing()) {
            this.menuFloatLayerWindow.dismiss();
        }
        if (this.mExitDialog != null && this.mExitDialog.isShowing()) {
            this.mExitDialog.dismiss();
        }
    }

    protected boolean consumeKeyEvent(KeyEvent event) {
        if (event.getAction() != 1 || !Tactic.getAnimating()) {
            return false;
        }
        if (!ViewDebug.DBG) {
            return true;
        }
        LogUtils.m1574i(TAG, "Animating : dispatchKeyEvent ignore ACTION_UP event");
        return true;
    }
}
