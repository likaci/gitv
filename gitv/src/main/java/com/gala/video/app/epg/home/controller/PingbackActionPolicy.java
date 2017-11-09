package com.gala.video.app.epg.home.controller;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.app.epg.home.data.constants.HomeConstants;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.action.model.BannerAdActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.card.CoverFlowCard;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.loader.data.AppRequest;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.constants.AdCard;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class PingbackActionPolicy extends ActionPolicy {
    private String TAG = "PingbackActionPolicy";
    private SparseArray<Long> mBannerAdShowList = new SparseArray();
    private int mConstant = 1000;
    private SparseArray<Long> mInvocationBannerAdShowList = new SparseArray();
    private Page mPage;
    private SparseArray<Long> mShowList = new SparseArray();
    private MyObserver mStartPreviewObserver = new MyObserver() {
        public void update(String event) {
            LogUtils.d(PingbackActionPolicy.this.TAG, "checkAndSendBannerAdShowPingback, mStartPreviewObserver, show preview completed");
            for (int i = 0; i < PingbackActionPolicy.this.mBannerAdShowList.size(); i++) {
                long bannerAdId = ((Long) PingbackActionPolicy.this.mBannerAdShowList.get(PingbackActionPolicy.this.mBannerAdShowList.keyAt(i))).longValue();
                AdsClientUtils.getInstance().onAdStarted((int) bannerAdId);
                LogUtils.d(PingbackActionPolicy.this.TAG, "checkAndSendBannerAdShowPingback, mStartPreviewObserver, send banner ad show pingback, onAdStarted:" + bannerAdId);
            }
        }
    };

    public PingbackActionPolicy(Page page) {
        this.mPage = page;
        if (page.isDefaultPage()) {
            GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.SHOW_PREVIEW_COMPLETED, this.mStartPreviewObserver);
            LogUtils.d(this.TAG, "PingbackActionPolicy, current page is default page, register show_preview_completed");
        }
    }

    public void onFirstLayout(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void onScrollStop(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void initTimestamp(ViewGroup parent) {
        try {
            int first = cast(parent).getFirstAttachedPosition();
            int last = cast(parent).getLastAttachedPosition();
            for (int i = first; i <= last; i++) {
                if (i < 0) {
                    LogUtils.w(this.TAG, "initTimestamp. i <0 ;i = " + i);
                    return;
                }
                Item item = this.mPage.getItem(i);
                if (item == null) {
                    LogUtils.w(this.TAG, "initTimestamp. item==null. i = " + i);
                    return;
                }
                Card card = item.getParent();
                if (card == null) {
                    LogUtils.w(this.TAG, "initTimestamp. card==null. i = " + i);
                    return;
                }
                int key = (card.getLine() * this.mConstant) + item.getLine();
                if (!isItemViewVisiable(cast(parent), i, true)) {
                    this.mShowList.remove(key);
                } else if (this.mShowList.indexOfKey(key) < 0) {
                    this.mShowList.append(key, Long.valueOf(System.currentTimeMillis()));
                }
                countBannerAdInventory(card, item, cast(parent), i);
                checkAndSendBannerAdShowPingback(card, item, cast(parent), i);
            }
        } catch (Exception e) {
            LogUtils.e(this.TAG, "initTimestamp.Exception e.getMessage()= " + e.getMessage());
            e.printStackTrace();
        }
    }

    boolean isItemViewVisiable(BlocksView blocksView, int position, boolean isFullyVisible) {
        View view = blocksView.getViewByPosition(position);
        int top = view.getTop() - blocksView.getScrollY();
        int bottom = view.getBottom() - blocksView.getScrollY();
        if ((view instanceof StandardItemView) && ((StandardItemView) view).isTitleoutType()) {
            bottom -= ResourceUtil.getPx(34);
        }
        int parentHeight = blocksView.getHeight();
        if (isFullyVisible) {
            if (top < 0 || top >= parentHeight || bottom <= 0 || bottom > parentHeight) {
                return false;
            }
            return true;
        } else if (top >= 0 && top < parentHeight) {
            return true;
        } else {
            if (bottom <= 0 || bottom > parentHeight) {
                return false;
            }
            return true;
        }
    }

    private void countBannerAdInventory(Card card, Item item, BlocksView blocksView, int position) {
        int key = (card.getLine() * this.mConstant) + item.getLine();
        if (!isItemViewVisiable(blocksView, position, false)) {
            this.mInvocationBannerAdShowList.remove(key);
        } else if (this.mInvocationBannerAdShowList.indexOfKey(key) < 0) {
            this.mInvocationBannerAdShowList.append(key, Long.valueOf(System.currentTimeMillis()));
            int resultId = UikitSourceDataCache.readBannerAdId();
            String defaultTabResourceGroupId = "";
            List<TabModel> tabModelList = TabProvider.getInstance().getTabInfo();
            if (!ListUtils.isEmpty((List) tabModelList)) {
                for (TabModel tabModel : tabModelList) {
                    if (tabModel != null && tabModel.isFocusTab()) {
                        defaultTabResourceGroupId = tabModel.getResourceGroupId();
                    }
                }
            }
            int line = card.getLine() + 1;
            int itemLine = item.getLine() + 1;
            for (Entry<String, Integer> entry : UikitSourceDataCache.getInvocationBannerMap(defaultTabResourceGroupId).entrySet()) {
                String adZoneId = (String) entry.getKey();
                if (((Integer) entry.getValue()).intValue() == line) {
                    Map<String, Object> properties = new HashMap();
                    properties.put("adZoneId", adZoneId);
                    AdsClientUtils.getInstance().onAdCardShowWithProperties(resultId, AdCard.AD_CARD_TV_BANNER, properties);
                    String str = this.TAG;
                    StringBuilder append = new StringBuilder().append("countBannerAdInventory, item@").append(item.hashCode()).append(", line = ").append(line).append(", itemLine = ").append(itemLine).append(", adZoneId = ");
                    if (adZoneId == null) {
                        adZoneId = "";
                    }
                    LogUtils.d(str, append.append(adZoneId).append(", ad resultId = ").append(resultId).toString());
                }
            }
        }
    }

    private void checkAndSendBannerAdShowPingback(Card card, Item item, BlocksView blocksView, int pos) {
        boolean fullyVisible = isItemViewVisiable(blocksView, pos, false);
        ItemInfoModel itemInfoModel = item.getModel();
        if (itemInfoModel != null) {
            BaseActionModel baseActionModel = itemInfoModel.getActionModel();
            if (baseActionModel != null) {
                if (ItemDataType.BANNER_IMAGE_AD.equals(baseActionModel.getItemType())) {
                    int key = (card.getLine() * this.mConstant) + item.getLine();
                    int bannerAdId = ((BannerAdActionModel) baseActionModel).getCommonAdData().getAdId();
                    if (!fullyVisible) {
                        this.mBannerAdShowList.remove(key);
                        LogUtils.d(this.TAG, "checkAndSendBannerAdShowPingback, banner ad (id = " + bannerAdId + ")is not visible now");
                    } else if (this.mBannerAdShowList.indexOfKey(key) < 0) {
                        this.mBannerAdShowList.append(key, Long.valueOf((long) bannerAdId));
                        if (HomeConstants.mIsStartPreViewFinished) {
                            AdsClientUtils.getInstance().onAdStarted(bannerAdId);
                            LogUtils.d(this.TAG, "checkAndSendBannerAdShowPingback, send banner ad show pingback, item line : " + item.getLine() + ", card line : " + card.getLine() + ", onAdStarted:" + bannerAdId);
                            return;
                        }
                        LogUtils.d(this.TAG, "checkAndSendBannerAdShowPingback, start preview page not finished, not send banner show pingback");
                    }
                }
            }
        }
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        onSendCardShowPingback(cast(parent), this.mPage, false, true);
    }

    public void onItemAnimatorFinished(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void onSendCardShowPingback(BlocksView blocksView, Page page, boolean isPageSwitch, boolean isScroll) {
        String e;
        try {
            e = getCardShowEValue(isPageSwitch);
            String count = getCardShowCountValue(isPageSwitch);
            String qtcurl = getCardShowQTCurlValue(isPageSwitch);
            String adCount = getCardShowADCountValue();
            String c1 = getCardShowC1Value();
            String qpid = "";
            int first = blocksView.getFirstAttachedPosition();
            int last = blocksView.getLastAttachedPosition();
            int i = first;
            while (i <= last) {
                if (i < 0) {
                    LogUtils.w(this.TAG, "onSendCardShowPingback. i <0 ;i = " + i);
                } else {
                    Item item = page.getItem(i);
                    if (item == null) {
                        LogUtils.w(this.TAG, "onSendCardShowPingback. item==null. i = " + i);
                    } else {
                        Card card = item.getParent();
                        if (card == null) {
                            LogUtils.w(this.TAG, "onSendCardShowPingback. card==null. i = " + i);
                        } else {
                            int key = (card.getLine() * this.mConstant) + item.getLine();
                            if (((isScroll && !isItemViewVisiable(blocksView, i, true)) || !isScroll) && this.mShowList.indexOfKey(key) >= 0) {
                                if (System.currentTimeMillis() - ((Long) this.mShowList.get(key)).longValue() > 500) {
                                    int resourceIndex;
                                    int resourceShownIndex;
                                    int line = getLine(page, card, item);
                                    int cardLine = item.getLine();
                                    int allLine = card.getAllLine();
                                    CardInfoModel cardInfoModel = card.getModel();
                                    List<CardInfoModel> dataList = card.getParent().getModel();
                                    if (dataList.isEmpty()) {
                                        LogUtils.w(this.TAG, "onSendCardShowPingback. CardInfoModel List isEmpty ; dataIndex = -1");
                                        resourceIndex = -1;
                                    } else {
                                        resourceIndex = dataList.indexOf(cardInfoModel);
                                    }
                                    List<Card> cardList = card.getParent().getCards();
                                    if (cardList.isEmpty()) {
                                        LogUtils.w(this.TAG, "onSendCardShowPingback. cardList List isEmpty ; cardIndex = -1");
                                        resourceShownIndex = -1;
                                    } else {
                                        resourceShownIndex = cardList.indexOf(card);
                                    }
                                    String block = cardInfoModel.mCardId;
                                    if (cardInfoModel.getCardType() == (short) 102) {
                                        adCount = String.valueOf(CoverFlowCard.adShowCount);
                                    } else {
                                        if (TextUtils.equals(Source.APPLICATION, cardInfoModel.mSource)) {
                                            switch (AppRequest.checkApp()) {
                                                case 1:
                                                    block = "全部应用";
                                                    break;
                                                case 2:
                                                    block = "应用推荐";
                                                    break;
                                                case 3:
                                                    block = "应用";
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
                                        if (TextUtils.equals("banner", cardInfoModel.mSource)) {
                                            block = "通栏广告";
                                            qpid = "通栏广告";
                                            LogUtils.d(this.TAG, "send banner ad show ping back");
                                        }
                                    }
                                    GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.HOME_CARD_SHOW_PINGBACK).addItem("qtcurl", qtcurl).addItem("block", block).addItem("qpid", qpid).addItem("c1", c1).addItem("resource", (resourceIndex + 1) + "").addItem(Keys.RESOURCE_SHOWN, (resourceShownIndex + 1) + "").addItem("line", (line + 1) + "").addItem(Keys.CARDLINE, (cardLine + 1) + "").addItem("e", e).addItem("count", count).addItem("adcount", adCount).addItem(Keys.ALLLINE, allLine + "").post();
                                    if (cardInfoModel.getCardType() == (short) 102) {
                                        CoverFlowCard.adShowCount = 0;
                                    }
                                }
                                this.mShowList.remove(key);
                            }
                        }
                    }
                }
                i++;
            }
            if (!isScroll) {
                clearShowList();
            }
        } catch (String e2) {
            LogUtils.e(this.TAG, "onSendCardShowPingback.Exception e.getMessage()= " + e2.getMessage());
            e2.printStackTrace();
        }
    }

    protected String getCardShowC1Value() {
        return "";
    }

    protected String getCardShowADCountValue() {
        return "0";
    }

    protected String getCardShowEValue(boolean isPageSwitch) {
        if (isPageSwitch) {
            return HomePingbackSender.getInstance().getPreTabE();
        }
        return HomePingbackSender.getInstance().getCurTabE();
    }

    protected String getCardShowQTCurlValue(boolean isPageSwitch) {
        String tabName;
        if (isPageSwitch) {
            tabName = HomePingbackSender.getInstance().getPreTabName();
        } else {
            tabName = HomePingbackSender.getInstance().getTabName();
        }
        return "tab_" + tabName;
    }

    protected String getCardShowCountValue(boolean isPageSwitch) {
        if (isPageSwitch) {
            return HomePingbackSender.getInstance().getPreTabIndex();
        }
        return HomePingbackSender.getInstance().getTabIndex();
    }

    private void clearShowList() {
        this.mShowList.clear();
    }

    private int getLine(Page page, Card currentCard, Item item) {
        int line;
        synchronized (page) {
            line = 0;
            List<Card> cardList = page.getCards();
            int size = cardList.size();
            for (int i = 0; i < size; i++) {
                Card card = (Card) cardList.get(i);
                if (card == null) {
                    LogUtils.w(this.TAG, "getLine. card==null.");
                } else if (currentCard == card) {
                    break;
                } else {
                    line += card.getAllLine();
                }
            }
            line += item.getLine();
        }
        return line;
    }

    public void onCoverFlowAdShow(BlocksView parent) {
        try {
            int first = cast((ViewGroup) parent).getFirstAttachedPosition();
            Item item = this.mPage.getItem(first);
            if (item != null) {
                Card card = item.getParent();
                if (card != null && (card instanceof CoverFlowCard) && isItemViewVisiable(cast((ViewGroup) parent), first, false)) {
                    LogUtils.e(this.TAG, "onCoverFlowAdShow,first=" + first + ",card=" + card);
                    ((CoverFlowCard) card).sendAdPingback();
                }
            }
        } catch (Exception e) {
            LogUtils.e(this.TAG, "onCoverFlowAdShow.Exception e.getMessage()= " + e.getMessage());
            e.printStackTrace();
        }
    }
}
