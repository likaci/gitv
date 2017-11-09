package com.gala.video.app.epg.home.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.appdownload.utils.AppUtils;
import com.gala.video.app.epg.home.component.PageManage;
import com.gala.video.app.epg.home.contract.PromotionContract.Presenter;
import com.gala.video.app.epg.home.contract.PromotionContract.View;
import com.gala.video.app.epg.home.data.HomeDataCenter;
import com.gala.video.app.epg.home.data.bus.IHomeDataObserver;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.hdata.task.HomePageInitTask;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.presenter.ActionBarPresenter;
import com.gala.video.app.epg.home.presenter.MainPagePresenter;
import com.gala.video.app.epg.home.presenter.StatusPresenter;
import com.gala.video.app.epg.home.promotion.PromotionPresenter;
import com.gala.video.app.epg.home.utils.Locker;
import com.gala.video.app.epg.home.utils.PromotionUtil;
import com.gala.video.app.epg.home.widget.TabChangingDialog;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarItemView;
import com.gala.video.app.epg.home.widget.actionbar.CheckInBuildEvent;
import com.gala.video.app.epg.home.widget.pager.ScrollViewPager;
import com.gala.video.app.epg.home.widget.tabhost.TabBarHost;
import com.gala.video.app.epg.startup.StartUpCostInfoProvider;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.stub.StartUpInfo;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.tvos.appdetailpage.client.Constants;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeController implements View {
    private static final int AUTO_TAB_ORDERED = 0;
    private static final int BUILD_COMPLETED = 100;
    private static final int BUILD_DEFAULT_COMPLETED = 94;
    private static final int BUILD_DEFAULT_TAB_COMPLETED = 95;
    private static final int BUILD_TAB_COMPLETED = 98;
    private static final int MANUAL_TAB_ORDERED = 1;
    private static final String TAG = "HomeController";
    private static final int UPDATE_TAB_COMPLETED = 97;
    private static final int UPDATE_TAB_ORDER = 96;
    private static final int WAIT_DEFAULT_TIMEOUT = 18000;
    private static boolean mHasSendChinaPokerShowPingback = false;
    public static boolean mIsBuildUICompleted = false;
    public static UIEvent sUIEvent;
    private ActionBarPresenter mActionBarPresenter;
    private Locker mBuildCompletedLock = new Locker();
    private Locker mBuildDefaultLock = new Locker();
    private Context mContext;
    private TabChangingDialog mDialog;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            LogUtils.d(HomeController.TAG, "home controller message = " + msg.what + ",arg1 = " + msg.arg1 + ",arg2 = " + msg.arg2);
            switch (msg.what) {
                case HomeController.BUILD_DEFAULT_COMPLETED /*94*/:
                    HomeController.this.mBuildDefaultLock.complete();
                    break;
                case 95:
                    HomeController.this.mMainPagePresenter = new MainPagePresenter(HomeController.this.mContext, HomeController.this.mRootView, HomeController.this.mActionBarPresenter, HomeController.sUIEvent, HomeController.this.mTargetPage, HomeController.this.mPromotionPresenter);
                    if (HomeController.this.mIsDefault) {
                        HomeController.this.mMainPagePresenter.buildTabHost(HomeController.this.mUIController.mPageTabs, HomeController.this.mUIController.mTotalTabCount);
                        LogUtils.d(HomeController.TAG, "BUILD_DEFAULT_TAB_COMPLETED111111");
                        HomeController.this.mMainPagePresenter.buildPages(HomeController.this.mUIController);
                        LogUtils.d(HomeController.TAG, "BUILD_DEFAULT_TAB_COMPLETED222222");
                        break;
                    }
                    break;
                case 96:
                    if (TabProvider.getInstance().getTabInfo() != null) {
                        HomeController.this.mMainPagePresenter.buildTabHost(HomeController.this.mUIController.mPageTabs, HomeController.this.mUIController.mTotalTabCount);
                        HomeController.this.mMainPagePresenter.buildPages(HomeController.this.mUIController);
                        HomeController.this.mMainPagePresenter.initFocusTracer(HomeController.this.mUIController.mPageTabs, HomeController.this.mUIController.mTotalTabCount);
                        HomeController.this.requestDefaultFocus();
                        break;
                    }
                    LogUtils.e(HomeController.TAG, "updateTab name fail tabList is null");
                    return;
                case 97:
                    if (HomeController.this.mMainPagePresenter != null) {
                        HomeController.this.mMainPagePresenter.buildTabHost(HomeController.this.mUIController.mPageTabs, HomeController.this.mUIController.mTotalTabCount);
                        HomeController.this.mMainPagePresenter.buildPages(HomeController.this.mUIController);
                    }
                    HomeController.this.requestDefaultFocus();
                    HomeController.this.mUIController.destroyAllTab();
                    break;
                case 98:
                    if (HomeController.this.mMainPagePresenter == null) {
                        HomeController.this.mMainPagePresenter = new MainPagePresenter(HomeController.this.mContext, HomeController.this.mRootView, HomeController.this.mActionBarPresenter, HomeController.sUIEvent, HomeController.this.mTargetPage, HomeController.this.mPromotionPresenter);
                    }
                    LogUtils.d(HomeController.TAG, "BUILD_TAB_COMPLETED1111");
                    HomeController.this.mMainPagePresenter.buildTabHost(HomeController.this.mUIController.mPageTabs, HomeController.this.mUIController.mTotalTabCount);
                    LogUtils.d(HomeController.TAG, "BUILD_TAB_COMPLETED22222");
                    HomeController.this.mMainPagePresenter.buildPages(HomeController.this.mUIController);
                    HomeController.this.mUIController.loadData();
                    break;
                case 100:
                    if (HomeController.this.mIsDefault && HomeController.this.mMainPagePresenter == null) {
                        HomeController.this.mMainPagePresenter = new MainPagePresenter(HomeController.this.mContext, HomeController.this.mRootView, HomeController.this.mActionBarPresenter, HomeController.sUIEvent, HomeController.this.mTargetPage, HomeController.this.mPromotionPresenter);
                    }
                    if (msg.arg1 == HomeController.this.mMainPagePresenter.getDefaultTab()) {
                        StartUpCostInfoProvider.getInstance().onHomeBuildCompleted(SystemClock.elapsedRealtime());
                        LogUtils.d(HomeController.TAG, "[start performance] loadPlugin cost " + StartUpInfo.get().getPluginLoadInterval() + " ms,", "show home page completed cost " + (SystemClock.elapsedRealtime() - StartUpInfo.get().getStartTime()) + " ms");
                        HomeController.this.requestDefaultFocus();
                        HomeController.this.sendChinaPokerEntryShowPingback();
                        break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private boolean mIsDefault = false;
    private boolean mIsHomeRefresh = true;
    private ImageView mLeftShaderImg;
    private MainPagePresenter mMainPagePresenter;
    private Presenter mPromotionPresenter;
    private ImageView mRightShaderImg;
    private android.view.View mRootView;
    private StatusPresenter mStatePresenter;
    private TabBarHost mTabHosts;
    private IHomeDataObserver mTabInfoChangeListener = new IHomeDataObserver() {
        public void update(WidgetChangeStatus status, HomeModel data) {
            LogUtils.d(HomeController.TAG, "TAB_INFOChange status:" + status + data);
            switch (status) {
                case InitChange:
                    HomeController.this.onInitTabMessage();
                    return;
                case TabLayoutChange:
                case TabLayoutChangeManual:
                    HomeController.this.onTabLayoutChangeMessage(status);
                    return;
                case TabDataChange:
                    HomeController.this.onTabDataChangeMessage(0);
                    return;
                case TabOrderChangeManual:
                    HomeController.this.onTabDataChangeMessage(1);
                    return;
                case Default:
                    HomeController.this.onDefaultTabMessage();
                    return;
                case NoChange:
                    HomeController.this.reloadNoDataPage();
                    return;
                case TAB_FOCUS_RESET:
                    HomeController.this.requestDefaultFocus();
                    return;
                default:
                    return;
            }
        }
    };
    private int mTargetPage = -1;
    private RelativeLayout mTopLayout;
    private int mTopRecommendAppType = -1;
    private UIController mUIController;
    private ScrollViewPager mViewPager;
    private PromotionMessage message;
    private ActionBarItemView promotionView;

    public HomeController(int targetPage) {
        this.mTargetPage = targetPage;
    }

    public void init(Context context, android.view.View rootView) {
        this.mContext = context;
        this.mRootView = rootView;
        sUIEvent = new UIEvent();
        this.mUIController = new UIController(this);
        this.mUIController.initPageTabs();
        Tactic.setUIController(this.mUIController);
        Tactic.setUIEvnet(sUIEvent);
        this.mTabHosts = (TabBarHost) this.mRootView.findViewById(R.id.epg_tab_host);
        this.mTopLayout = (RelativeLayout) this.mRootView.findViewById(R.id.epg_top_layout);
        this.mViewPager = (ScrollViewPager) this.mRootView.findViewById(R.id.epg_pager);
        this.mViewPager.setTabBarHost(this.mTabHosts);
        this.mLeftShaderImg = (ImageView) this.mRootView.findViewById(R.id.epg_tab_host_leftimage);
        this.mRightShaderImg = (ImageView) this.mRootView.findViewById(R.id.epg_tab_host_rightimage);
        this.mStatePresenter = new StatusPresenter(this.mRootView, this.mContext);
        this.mActionBarPresenter = new ActionBarPresenter(this.mContext, this.mRootView);
        this.promotionView = (ActionBarItemView) rootView.findViewById(R.id.tv_third_part_app);
        this.mPromotionPresenter = new PromotionPresenter(context, this);
        HomeDataCenter.clearObserver();
        HomeDataCenter.registerObserver(HomeDataType.TAB_INFO, this.mTabInfoChangeListener);
        HomeDataCenter.initialize(context);
        invokeHomeInitTask();
        GetInterfaceTools.getStartupDataLoader().forceLoad(true);
        HomePingbackSender.getInstance().registerScreenSaverListener();
        EventBus.getDefault().register(this);
        initPromotion();
        this.mPromotionPresenter.start();
    }

    private void onDefaultTabMessage() {
        this.mUIController.createDefaultALLTab(this.mContext);
        this.mIsDefault = true;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(95));
    }

    private void onInitTabMessage() {
        long start = SystemClock.elapsedRealtime();
        if (this.mIsDefault) {
            boolean ret = this.mBuildDefaultLock.takeOrWait(18000);
            this.mIsDefault = false;
            LogUtils.d(TAG, "onInitTabMessage build default completed : " + ret);
            this.mUIController.cleanDefault();
        }
        this.mUIController.createALLTab(this.mContext);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(98));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "[start performance] tab create cost " + (SystemClock.elapsedRealtime() - start) + " ms");
        }
    }

    private void onTabDataChangeMessage(int mode) {
        if (!mIsBuildUICompleted && mode == 1) {
            showTabManualDialog();
        }
        if (this.mMainPagePresenter != null) {
            this.mMainPagePresenter.backToTop(this.mUIController.mPageTabs, this.mUIController.mTotalTabCount);
        }
        this.mBuildCompletedLock.takeOrWait();
        this.mUIController.updateALLTab(this.mContext);
        if (mode == 1) {
            cancelTabManualDialog();
        }
        Message msg = this.mHandler.obtainMessage(96);
        msg.arg1 = mode;
        this.mHandler.sendMessage(msg);
    }

    public void backToTop() {
        if (this.mMainPagePresenter != null) {
            this.mMainPagePresenter.backToTop(this.mUIController.mPageTabs, this.mUIController.mTotalTabCount);
        }
    }

    private synchronized void onTabLayoutChangeMessage(WidgetChangeStatus status) {
        TabProvider tabProvider = TabProvider.getInstance();
        if (status == WidgetChangeStatus.TabLayoutChangeManual) {
            showTabManualDialog();
        }
        this.mMainPagePresenter.backToTop(this.mUIController.mPageTabs, this.mUIController.mTotalTabCount);
        List<TabModel> tabList = tabProvider.getTabInfo();
        this.mBuildCompletedLock.takeOrWait(18000);
        if (status == WidgetChangeStatus.TabLayoutChangeManual) {
            cancelTabManualDialog();
        }
        this.mUIController.updateALLTab(this.mContext, tabList);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(97));
    }

    private void showTabManualDialog() {
        if (ThreadUtils.isUIThread()) {
            this.mDialog = new TabChangingDialog(this.mContext);
            this.mDialog.show();
            return;
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                LogUtils.d(HomeController.TAG, "showTabManualDialog");
                HomeController.this.mDialog = new TabChangingDialog(HomeController.this.mContext);
                HomeController.this.mDialog.show();
            }
        });
    }

    private void cancelTabManualDialog() {
        if (!ThreadUtils.isUIThread()) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (HomeController.this.mDialog != null) {
                        HomeController.this.mDialog.dismiss();
                        HomeController.this.mDialog = null;
                    }
                }
            });
        } else if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    public void buildOnComplete(int pageIndex) {
        if (this.mIsDefault) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(BUILD_DEFAULT_COMPLETED));
            return;
        }
        Message msg = this.mHandler.obtainMessage(100);
        msg.arg1 = pageIndex;
        this.mHandler.sendMessage(msg);
        mIsBuildUICompleted = checkIfBuildAllCompleted();
        if (mIsBuildUICompleted) {
            this.mBuildCompletedLock.complete();
        }
        unLock(pageIndex);
    }

    public void onStart() {
        sUIEvent.post(UIEventType.CAROUSEL_CARD_UPDATE, null);
        this.mStatePresenter.onStart();
        this.mActionBarPresenter.onStart();
        this.mIsHomeRefresh = true;
    }

    public void onStop() {
        this.mStatePresenter.onStop();
        this.mActionBarPresenter.onStop();
        this.mIsHomeRefresh = false;
        this.mPromotionPresenter.onStop();
    }

    public void requestDefaultFocus() {
        if (this.mMainPagePresenter != null) {
            this.mViewPager.post(new Runnable() {
                public void run() {
                    int i = 0;
                    while (i < HomeController.this.mUIController.mTotalTabCount) {
                        if (i != HomeController.this.mViewPager.getCurrentItem() && HomeController.this.mUIController.mPageTabs[i].mBuilded) {
                        }
                        i++;
                    }
                    HomeController.this.mTabHosts.reset();
                    HomeController.this.mMainPagePresenter.requestDefaultFocus();
                }
            });
        }
    }

    public void onDlnaActionNotifyEvent(RequestKind kind) {
        this.mStatePresenter.onDlnaActionNotifyEvent(kind);
    }

    public void onStartUpEvent(ErrorEvent event) {
        this.mStatePresenter.onStartUpEvent(event);
    }

    public PageManage getCurPage() {
        if (this.mMainPagePresenter == null) {
            return null;
        }
        return this.mMainPagePresenter.getCurrentPage();
    }

    public MainPagePresenter getMainPagePresenter() {
        return this.mMainPagePresenter;
    }

    public ActionBarPresenter getActionBarPresenter() {
        return this.mActionBarPresenter;
    }

    public UIController getUIController() {
        return this.mUIController;
    }

    private void invokeHomeInitTask() {
        new Thread8K(new Runnable() {
            public void run() {
                new HomePageInitTask(HomeController.this.mTargetPage).run();
            }
        }).start();
    }

    public void updateActionBar() {
        this.mActionBarPresenter.update();
    }

    private void lock(int pageIndex) {
        try {
            this.mUIController.mPageTabs[pageIndex].mBuildLock.acquire();
        } catch (InterruptedException e) {
            LogUtils.d(TAG, "exception :", e);
        }
    }

    private void unLock(int pageIndex) {
        this.mUIController.mPageTabs[pageIndex].mBuildLock.release();
    }

    public boolean checkIfBuildAllCompleted() {
        boolean ret = true;
        for (int i = 0; i < this.mUIController.mTotalTabCount; i++) {
            PageManage page = this.mUIController.mPageTabs[i];
            if (!page.mBuilded && !page.mNoData) {
                ret = false;
                break;
            }
        }
        if (ret && !mIsBuildUICompleted) {
            sUIEvent.post(1, null);
            LogUtils.i(TAG, "build ui: all page build complete!!!, build count: " + this.mUIController.mTotalTabCount);
        }
        return ret;
    }

    private void reloadNoDataPage() {
        for (int i = 0; i < this.mUIController.mTotalTabCount; i++) {
            PageManage page = this.mUIController.mPageTabs[i];
            if (page.mNoData) {
                LogUtils.i(TAG, "reloadNoDataPage index" + i);
                page.handleTabResourceRetry();
            }
        }
    }

    public void destroy() {
        this.mPromotionPresenter.destroy();
    }

    private void initPromotion() {
        this.promotionView.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (hasFocus) {
                    HomeController.this.mPromotionPresenter.setPreFocusId(v.getId());
                    HomeController.this.promotionView.setTextColor(HomeController.this.mContext.getResources().getColor(R.color.action_bar_text_focus));
                } else {
                    HomeController.this.promotionView.setTextColor(HomeController.this.mContext.getResources().getColor(R.color.action_bar_text_normal));
                }
                HomeController.this.mPromotionPresenter.setOnFocus(true);
                AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 180);
            }
        });
        this.promotionView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(android.view.View v, int keyCode, KeyEvent event) {
                return HomeController.this.mPromotionPresenter.onKey(v, keyCode, event);
            }
        });
        this.promotionView.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                PromotionAppInfo promotionAppInfo = HomeController.this.message.getDocument().getAppInfo();
                promotionAppInfo.setAppType(HomeController.this.message.getType());
                ItemUtils.startApp(HomeController.this.mContext, promotionAppInfo);
                String state = AppUtils.isInstalled(HomeController.this.mContext, HomeController.this.message.getDocument().getAppInfo().getAppPckName()) ? Constants.PINGBACK_ACTION_INSTALL_DONE : Constants.PINGBACK_ACTION_UNINSTALL_DONE;
                String rseat = HomeController.this.message.getDocument().getEntryDocument();
                HomePingbackFactory.instance().createPingback(ClickPingback.ACTION_BAR_CLICK_PINGBACK).addItem("r", rseat).addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "top").addItem("rseat", rseat).addItem("state", state).addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
            }
        });
        this.promotionView.setTextColor(this.mContext.getResources().getColor(R.color.action_bar_text_normal));
        this.promotionView.setIconDrawableWidth(R.dimen.dimen_23dp);
        setIconVisibleAndMaxText(8);
        setRecommendVisibilityAndFocus(8);
    }

    public void showPromotion(PromotionMessage message) {
        this.message = message;
        PromotionAppInfo appInfo = PromotionUtil.getPromotionAppInfo(message);
        if (appInfo == null) {
            setRecommendVisibilityAndFocus(8);
            return;
        }
        String appName = PromotionUtil.getPromotionDocument(message).getEntryDocument();
        if (isAppRecommendDataIllLegal(appName, appInfo.getAppDownloadUrl(), appInfo.getAppPckName())) {
            setRecommendVisibilityAndFocus(8);
            return;
        }
        setRecommendVisibilityAndFocus(0);
        this.promotionView.setText(appName);
        showAppRecommendIcon(appInfo.getIcon());
        if (2 == message.getType()) {
            this.mTopRecommendAppType = 2;
        }
    }

    private void sendChinaPokerEntryShowPingback() {
        if (this.mTopRecommendAppType == 2 && !mHasSendChinaPokerShowPingback) {
            HomePingbackFactory.instance().createPingback(ShowPingback.CHINA_POKER_ENTRY_SHOW_PINGBACK).addItem("qtcurl", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "top").addItem("rseat", "斗地主").post();
            mHasSendChinaPokerShowPingback = true;
        }
    }

    private void setRecommendVisibilityAndFocus(int visibility) {
        this.promotionView.setVisibility(visibility);
        setPromotionFocus(visibility, this.promotionView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRecommendFocusEvent(CheckInBuildEvent event) {
        LogUtils.i(TAG, "setRecommendFocusEvent: " + this.promotionView.getVisibility());
        setPromotionFocus(this.promotionView.getVisibility(), this.promotionView);
        this.mMainPagePresenter.setActionBarNextFocusDownId();
        this.mActionBarPresenter.updateVipViewText();
        this.mActionBarPresenter.updateCheckInView();
    }

    private boolean isAppRecommendDataIllLegal(String appName, String appDownloadUrl, String packageName) {
        return TextUtils.isEmpty(appName) || TextUtils.isEmpty(appDownloadUrl) || TextUtils.isEmpty(packageName);
    }

    private void showAppRecommendIcon(String icon) {
        if (TextUtils.isEmpty(icon)) {
            setIconVisibleAndMaxText(8);
            return;
        }
        LogUtils.i(TAG, "icon=" + icon);
        ImageRequest imageRequest = new ImageRequest(icon);
        imageRequest.setTargetWidth((int) this.mContext.getResources().getDimension(R.dimen.dimen_21dp));
        imageRequest.setTargetHeight((int) this.mContext.getResources().getDimension(R.dimen.dimen_21dp));
        imageRequest.setShouldBeKilled(false);
        ImageProviderApi.getImageProvider().loadImage(imageRequest, new IImageCallback() {
            public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
                HomeController.this.promotionView.setImageBitmap(bitmap);
                HomeController.this.setIconVisibleAndMaxText(0);
                LogUtils.i(HomeController.TAG, "icon show = success");
            }

            public void onFailure(ImageRequest imageRequest, Exception e) {
                HomeController.this.setIconVisibleAndMaxText(8);
                LogUtils.i(HomeController.TAG, "icon show = failed");
            }
        });
    }

    private void setIconVisibleAndMaxText(int visibility) {
        if (visibility == 8) {
            this.promotionView.setIconVisibily(8);
            this.promotionView.setMaxTextCount(5);
        } else {
            this.promotionView.setIconVisibily(0);
            this.promotionView.setMaxTextCount(4);
        }
        String name = this.promotionView.getName();
        if (!TextUtils.isEmpty(name)) {
            this.promotionView.setText(name);
        }
    }

    public void setPromotionFocus(int Visibility, android.view.View promotionView) {
        if (Visibility == 0) {
            this.mActionBarPresenter.setLastFocusRightViewId(promotionView.getId());
            promotionView.setNextFocusLeftId(this.mActionBarPresenter.getLastViewId());
            promotionView.setNextFocusRightId(promotionView.getId());
            return;
        }
        this.mActionBarPresenter.setLastFocusHimself();
    }

    public Context getContext() {
        return this.mContext;
    }

    public void invisiblePromotion() {
        this.promotionView.setVisibility(8);
    }

    public void setNextFocusDownId(int id) {
        this.promotionView.setNextFocusDownId(id);
    }

    public boolean isVisibility() {
        return this.promotionView.getVisibility() == 0;
    }
}
