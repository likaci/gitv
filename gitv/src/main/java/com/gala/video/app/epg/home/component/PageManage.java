package com.gala.video.app.epg.home.component;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.app.epg.home.component.card.NCarouselCard;
import com.gala.video.app.epg.home.component.item.NCarouselItem;
import com.gala.video.app.epg.home.component.item.NCarouselView;
import com.gala.video.app.epg.home.controller.HomeActionPolicy;
import com.gala.video.app.epg.home.controller.PingbackActionPolicy;
import com.gala.video.app.epg.home.data.TabData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.uikit.item.DailyNewsItem;
import com.gala.video.app.epg.uikit.view.DailyNewsItemView;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.loader.IUikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PageManage {
    private static final int RETRY_DELAY = 1000;
    private static Handler mHandler = new Handler(Looper.myLooper());
    private long BIND_THREAD_MAX = 4;
    private String TAG = "PageManage";
    private boolean binded = false;
    public boolean isinited = false;
    public ActionPolicy mActionPolicy;
    private long mBindThreadNum = 0;
    public final Semaphore mBuildLock = new Semaphore(1);
    public boolean mBuilded = false;
    private PingbackActionPolicy mCardShowPingbackActionPolicy;
    public BlocksView mChild = null;
    private UIKitEngine mEngine;
    public boolean mIsDefault = false;
    public boolean mIsLoading = false;
    public boolean mIsUpdate = false;
    private long mLoadSuccessTime;
    IUikitDataLoader mLoader;
    public boolean mNoData = false;
    private int mPageIndex = -1;
    private long mPageStartShowTime;
    private Runnable mRetryRunnable = new Runnable() {
        public void run() {
            if (NetworkUtils.isNetworkAvaliable()) {
                PageManage.this.mLoader.firstCardList();
                LogUtils.d(PageManage.this.TAG, "retry");
                PageManage.this.mIsLoading = true;
            }
        }
    };
    public boolean mSelected = false;
    private long mStartLoadTime;
    public boolean mStarted = false;
    public TabData mTabdata;
    public Context mcontext;
    public boolean misVip = false;

    public PageManage(int pageIndex) {
        this.mPageIndex = pageIndex;
    }

    public BlocksView init(Context context) {
        if (!this.isinited) {
            EventBus.getDefault().register(this);
            this.mcontext = context;
            this.mChild = new BlocksView(context);
            this.mChild.setPadding(0, ResourceUtil.getPx(43), 0, ResourceUtil.getPx(30));
            this.mEngine = UIKitEngine.newInstance(context);
            this.mEngine.setIsDefaultPage(this.mIsDefault);
            this.mEngine.getUIKitBuilder().registerSpecialItem(218, NCarouselItem.class, NCarouselView.class);
            this.mEngine.getUIKitBuilder().registerSpecialCard(108, NCarouselCard.class);
            this.mEngine.getUIKitBuilder().registerSpecialItem(216, DailyNewsItem.class, DailyNewsItemView.class);
            this.mCardShowPingbackActionPolicy = new PingbackActionPolicy(this.mEngine.getPage());
            this.mEngine.getPage().registerActionPolicy(this.mCardShowPingbackActionPolicy);
            this.isinited = true;
        }
        return this.mChild;
    }

    public void initLoading() {
        CardInfoModel loadingcardmodel = new CardInfoModel();
        if (!(this.binded || this.mChild == null)) {
            this.mEngine.bindView(this.mChild);
            this.binded = true;
        }
        List<CardInfoModel> cards = new ArrayList(1);
        cards.add(loadingcardmodel);
        loadingcardmodel.setCardType((short) 999);
        loadingcardmodel.setBodyHeight(ResourceUtil.getPxShort(300));
        this.mEngine.setData(cards);
        LogUtils.d(this.TAG, "initLoading");
    }

    public void loadData(TabModel tab) {
        LogUtils.d(this.TAG, "loadData tab info :" + tab + "Engine id:" + this.mEngine.getId());
        this.mStartLoadTime = SystemClock.elapsedRealtime();
        this.mIsLoading = true;
        if (tab != null) {
            initLoading();
            if (this.mIsDefault) {
                this.mLoader = new UikitDataLoader(0, tab.getResourceGroupId(), this.mEngine.getId(), this.mPageIndex);
                this.mLoader.register();
                this.mLoader.setVipLoader(this.misVip);
                this.mLoader.setBannerAdId(70001);
                this.mLoader.setChannelId(tab.getChannelId());
                this.mLoader.firstCardList();
                return;
            }
            this.mLoader = new UikitDataLoader(2, tab.getResourceGroupId(), this.mEngine.getId(), this.mPageIndex);
            this.mLoader.register();
            this.mLoader.setVipLoader(this.misVip);
            this.mLoader.setChannelId(tab.getChannelId());
            this.mLoader.firstCardList();
        }
    }

    public void reloadData() {
        if (this.mLoader != null) {
            this.mLoader.firstCardList();
        }
    }

    public void updatePageData(List<CardInfoModel> cards) {
        bindDataSource(cards);
    }

    public void appendPageData(List<CardInfoModel> cards) {
        this.mEngine.appendData(cards);
    }

    public void updateCardData(CardInfoModel cards) {
        this.mEngine.updateCaredModel(cards);
    }

    public void bindDataSource(List<CardInfoModel> cards) {
        if (!this.binded) {
            this.mEngine.bindView(this.mChild);
            this.binded = true;
        }
        LogUtils.d(this.TAG, "bindDataSource Engine id " + this.mEngine.getId());
        this.mEngine.setData(cards);
    }

    public void setActionPolicy(ActionPolicy actionPolicy) {
        if (this.mActionPolicy != null) {
            this.mEngine.getPage().unregisterActionPolicy(this.mActionPolicy);
        }
        this.mActionPolicy = actionPolicy;
        if (this.mEngine != null) {
            this.mEngine.getPage().registerActionPolicy(this.mActionPolicy);
        }
    }

    public void onPostEvent(UikitEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onUikitEvent(UikitEvent event) {
        if (this.mEngine != null && event.uikitEngineId == this.mEngine.getId()) {
            LogUtils.d(this.TAG, "receive loader event: " + event + ",page index = " + this.mPageIndex);
            switch (event.eventType) {
                case 0:
                    LogUtils.d(this.TAG, "onUikitEvent LOADER_PAGE_INIT:" + event.layoutChange);
                    if (event.layoutChange == 1) {
                        if (event.cardList != null) {
                            this.mLoadSuccessTime = SystemClock.elapsedRealtime();
                            this.mNoData = false;
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(this.TAG, "[start performance] load page data cost " + (this.mLoadSuccessTime - this.mStartLoadTime) + " ms" + ", pageindex = " + this.mPageIndex);
                            }
                            bindDataSource(event.cardList);
                        } else {
                            LogUtils.e(this.TAG, "initPageAction card list null");
                        }
                    } else if (event.layoutChange == 0) {
                        LogUtils.i(this.TAG, "initPageAction NODATA!!! ,page index = " + this.mPageIndex);
                        this.mNoData = true;
                        if (this.mActionPolicy != null && (this.mActionPolicy instanceof HomeActionPolicy)) {
                            ((HomeActionPolicy) this.mActionPolicy).onNOData();
                        }
                    }
                    this.mIsLoading = false;
                    return;
                case 32:
                    LogUtils.d(this.TAG, "onUikitEvent LOADER_SET_CARDS-" + event.sourceId);
                    bindDataSource(event.cardList);
                    return;
                case 33:
                    LogUtils.d(this.TAG, "onUikitEvent LOADER_ADD_CARDS-" + event.sourceId + "-pageNo-" + event.pageNo);
                    appendPageData(event.cardList);
                    return;
                case 34:
                    LogUtils.d(this.TAG, "onUikitEvent LOADER_UPDATE_CARD card id-" + event.cardInfoModel.getId() + "-pageNo-" + event.pageNo + "-source-" + event.cardInfoModel.mSource);
                    updateCardData(event.cardInfoModel);
                    return;
                default:
                    return;
            }
        }
    }

    public void createDefault(int index) {
        CardInfoModel loadingcardmodel = new CardInfoModel();
        loadingcardmodel.setCardType((short) 999);
        loadingcardmodel.setBodyHeight(ResourceUtil.getPxShort(200));
        List<CardInfoModel> cards = new ArrayList(2);
        cards.add(loadingcardmodel);
        if (index == 1) {
            cards.add(CardInfoBuildTool.buildDefaultSettingCard(SettingConstants.ID_CONCERN_WECHAT, this.mEngine.getId()));
            this.mIsDefault = true;
        }
        bindDataSource(cards);
        LogUtils.d(this.TAG, "createDefault");
    }

    public void cleanDefault() {
        this.mEngine.getPage().unregisterAllActionPolicy();
        destroy();
    }

    public void destroy() {
        EventBus.getDefault().unregister(this);
    }

    public UIKitEngine getUIkitEngine() {
        return this.mEngine;
    }

    public BlocksView getBlocksView() {
        return this.mChild;
    }

    public void pageScrollStart() {
    }

    public void pageScrollEnd() {
        if (this.mBuilded) {
            ImageProviderApi.getImageProvider().stopAllTasks();
            this.mEngine.stop();
            this.mStarted = false;
        }
    }

    public void onPageIn() {
        LogUtils.d(this.TAG, "onPageIn mBuilded:" + this.mBuilded + "mStarted:" + this.mStarted);
        UikitDataCache.getInstance().setCurrentUikitEngineId(this.mEngine.getId());
        handleTabResourceRetry();
        if (this.mBuilded && !this.mStarted) {
            this.mEngine.start();
            this.mStarted = true;
        }
        this.mSelected = true;
        if (this.mTabdata != null) {
            onPagePingbackStartTime();
            PingBackUtils.setTabSrc("tab_" + this.mTabdata.getTitle());
            PingBackUtils.setTabName(this.mTabdata.getTitle());
            HomePingbackSender.getInstance().setCurTabData(this.mTabdata);
            HomePingbackSender.getInstance().setCurTabE();
            HomePingbackSender.getInstance().setTabIndex((((HomeActionPolicy) this.mActionPolicy).getPageindex() + 1) + "");
            if (this.mCardShowPingbackActionPolicy != null) {
                this.mCardShowPingbackActionPolicy.initTimestamp(this.mChild);
            }
        }
    }

    public void reset() {
    }

    public void onPageOut() {
        if (this.mBuilded) {
            this.mEngine.getPage().backToTop();
        }
        mHandler.removeCallbacksAndMessages(this.mRetryRunnable);
        this.mSelected = false;
        HomePingbackSender.getInstance().setPreTabData(this.mTabdata);
        HomePingbackSender.getInstance().setPreTabIndex((((HomeActionPolicy) this.mActionPolicy).getPageindex() + 1) + "");
        sendPageShowPingback(true);
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mChild, this.mEngine.getPage(), true, false);
        }
    }

    public void onActivityIn() {
        LogUtils.d(this.TAG, "onActivityIn mStarted:" + this.mStarted + "mSelected:" + this.mSelected);
        if (this.mEngine != null && this.mSelected) {
            this.mEngine.start();
            this.mStarted = true;
        }
        if (this.mSelected) {
            onPagePingbackStartTime();
            if (this.mCardShowPingbackActionPolicy != null) {
                this.mCardShowPingbackActionPolicy.initTimestamp(this.mChild);
            }
        }
        if (this.mSelected && this.mIsDefault && this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.onCoverFlowAdShow(this.mChild);
        }
    }

    public void onActivityOut() {
        LogUtils.d(this.TAG, "onActivityOut " + this.mSelected);
        this.mStarted = false;
        if (this.mSelected) {
            this.mEngine.hide();
            sendPageShowPingback(false);
            if (this.mCardShowPingbackActionPolicy != null) {
                this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mChild, this.mEngine.getPage(), false, false);
            }
        }
    }

    public void onScreenSaverDismiss() {
        onPagePingbackStartTime();
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mChild);
        }
    }

    public void onScreenSaverShow() {
        sendPageShowPingback(false);
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mChild, this.mEngine.getPage(), false, false);
        }
    }

    public void onExitDialogDismiss() {
        onPagePingbackStartTime();
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.initTimestamp(this.mChild);
        }
    }

    public void onExitDialogShow() {
        sendPageShowPingback(false);
        if (this.mCardShowPingbackActionPolicy != null) {
            this.mCardShowPingbackActionPolicy.onSendCardShowPingback(this.mChild, this.mEngine.getPage(), false, false);
        }
    }

    private void onPagePingbackStartTime() {
        this.mPageStartShowTime = SystemClock.elapsedRealtime();
    }

    private void sendPageShowPingback(boolean isPageSwitch) {
        long td = SystemClock.elapsedRealtime() - this.mPageStartShowTime;
        if (td >= 500) {
            String e;
            String tabName;
            String count;
            if (isPageSwitch) {
                e = HomePingbackSender.getInstance().getPreTabE();
            } else {
                e = HomePingbackSender.getInstance().getCurTabE();
            }
            if (isPageSwitch) {
                tabName = HomePingbackSender.getInstance().getPreTabName();
            } else {
                tabName = HomePingbackSender.getInstance().getTabName();
            }
            if (isPageSwitch) {
                count = HomePingbackSender.getInstance().getPreTabIndex();
            } else {
                count = HomePingbackSender.getInstance().getTabIndex();
            }
            GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.PAGE_SHOW_PINGBACK).addItem("qtcurl", "tab_" + tabName).addItem("block", "tab_" + tabName).addItem("e", e).addItem("td", String.valueOf(td)).addItem("count", count).post();
        }
    }

    public long getLoadedTime() {
        return this.mLoadSuccessTime;
    }

    public void handleTabResourceRetry() {
        if (shouldRetry()) {
            LogUtils.d(this.TAG, "should retry");
            mHandler.removeCallbacksAndMessages(this.mRetryRunnable);
            mHandler.postDelayed(this.mRetryRunnable, 1000);
        }
    }

    private boolean shouldRetry() {
        return this.mNoData && !this.mIsLoading;
    }
}
