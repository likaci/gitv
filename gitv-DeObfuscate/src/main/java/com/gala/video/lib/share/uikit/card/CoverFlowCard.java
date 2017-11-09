package com.gala.video.lib.share.uikit.card;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import com.gala.sdk.plugin.PluginType;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.LinearLayout;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.uikit.action.ActionModelFactory;
import com.gala.video.lib.share.uikit.action.model.AdActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card.CardActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.Item.CornerBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.item.CoverFlowItem;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.uikit.view.widget.coverflow.OnScrollListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CoverFlowCard extends Card {
    private static final boolean AD_TEST_DEBUG = true;
    private static final int MSG_REQUEST_AD = 100;
    private static final String TAG = "CoverFlowCard";
    public static volatile int adShowCount = 0;
    private List<Item> adItemList = new ArrayList();
    private boolean hasAdsLoaded = false;
    private boolean hasRegister = false;
    private ActionPolicy mActionPolicy = new CoverFlowActionPolicy(this);
    private CardFocusStatus mCardFocusStatus = CardFocusStatus.DEFAULT;
    private MyObserver mFocusImageAdDataObserver = new C17872();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    CoverFlowCard.this.fetchFocusAdData();
                    return;
                default:
                    return;
            }
        }
    };
    private CoverFlowItem mItem;
    private IStatusListener mScreenSaverListener = new C17894();
    private ScrollListener mScrollListener = new ScrollListener();
    private MyObserver mStartPreViewObserver = new C17883();
    private List<Integer> mTempAdIdPoolList = new ArrayList();

    class C17872 implements MyObserver {

        class C17861 implements Runnable {
            C17861() {
            }

            public void run() {
                CoverFlowCard.this.updateFocusImageAd();
            }
        }

        C17872() {
        }

        public void update(String event) {
            CoverFlowCard.this.mHandler.post(new C17861());
        }
    }

    class C17883 implements MyObserver {
        C17883() {
        }

        public void update(String event) {
            LogUtils.m1585d(CoverFlowCard.TAG, "CoverFlowCard@" + CoverFlowCard.this.hashCode() + ", mStartPreViewObserver->>update, start preview completed");
            CoverFlowCard.this.checkCoverFlowMsg();
            CoverFlowCard.this.sendAdPingback();
        }
    }

    class C17894 implements IStatusListener {
        C17894() {
        }

        public void onStart() {
            LogUtils.m1585d(CoverFlowCard.TAG, "CoverFlowCard@" + CoverFlowCard.this.hashCode() + ", mScreenSaverListener-->>onStart, screen saver show");
            CoverFlowCard.this.stopCoverFlowMsg(CoverFlowCard.this.mItem);
        }

        public void onStop() {
            LogUtils.m1585d(CoverFlowCard.TAG, "CoverFlowCard@" + CoverFlowCard.this.hashCode() + ", mScreenSaverListener-->>onStop, screen saver has dismissed");
            CoverFlowCard.this.checkCoverFlowMsg();
        }
    }

    private enum CardFocusStatus {
        DEFAULT(PluginType.DEFAULT_TYPE),
        HAS_FOCUS("has_focus"),
        LOST_FOCUS("lost_focus");
        
        private String value;

        private CardFocusStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    private class CoverFlowActionPolicy extends CardActionPolicy {
        CoverFlowActionPolicy(Card card) {
            super(card);
        }

        public void onScrollStop(ViewGroup parent) {
            CoverFlowCard.this.checkCoverFlowMsg();
        }

        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            int pos = CoverFlowCard.this.mItem.getFocusChildIndex();
            if (ListUtils.isLegal(CoverFlowCard.this.mItem.getItems(), pos)) {
                performClick(parent, pos, pos != 0 ? pos : ListUtils.getCount(CoverFlowCard.this.mItem.getItems()), (Item) CoverFlowCard.this.mItem.getItems().get(pos));
            }
        }

        public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
            super.onFocusPositionChanged(parent, position, hasFocus);
            CoverFlowCard.this.mCardFocusStatus = hasFocus ? CardFocusStatus.HAS_FOCUS : CardFocusStatus.LOST_FOCUS;
            LogUtils.m1585d(CoverFlowCard.TAG, "CoverFlowCard@" + hashCode() + ", onFocusPositionChanged, position :" + position + ", hasFocus : " + hasFocus);
            if (hasFocus) {
                CoverFlowCard.this.sendAdPingback();
            }
        }
    }

    private class ScrollListener implements OnScrollListener {
        private ScrollListener() {
        }

        public void onLayoutChanged(View view, int index, int visibility) {
        }

        public void onScrollStateChanged(boolean start) {
        }

        public void onChildVisibilityChange(View child, int index, int curVisibility, int oldVisibility) {
            int size = CoverFlowCard.this.mItem.getItems().size();
            if (index >= 0 && index <= size - 1) {
                ItemInfoModel itemInfoModel = CoverFlowCard.this.mItem.getItem(index).getModel();
                if (itemInfoModel != null) {
                    BaseActionModel baseActionModel = itemInfoModel.getActionModel();
                    if (baseActionModel != null) {
                        if (ItemDataType.FOCUS_IMAGE_AD.equals(baseActionModel.getItemType())) {
                            int focusImageAdId = ((AdActionModel) baseActionModel).getCommonAdData().getAdId();
                            if ((oldVisibility == 0 || oldVisibility == 1) && curVisibility == 2) {
                                LogUtils.m1585d(CoverFlowCard.TAG, "CoverFlowCard@" + CoverFlowCard.this.hashCode() + ", ScrollListener-->>onChildVisibilityChange" + ", focus image ad item is fully visible now!!!");
                                CoverFlowCard.this.mTempAdIdPoolList.add(Integer.valueOf(focusImageAdId));
                                CoverFlowCard.this.sendFocusImageAdShowPingback(focusImageAdId);
                            }
                            if (oldVisibility != 2) {
                                return;
                            }
                            if (curVisibility == 0 || curVisibility == 1) {
                                LogUtils.m1585d(CoverFlowCard.TAG, "CoverFlowCard@" + CoverFlowCard.this.hashCode() + ", ScrollListener-->>onChildVisibilityChange" + ", focus image ad item is not fully visible now!!!");
                                if (CoverFlowCard.this.mTempAdIdPoolList.contains(Integer.valueOf(focusImageAdId))) {
                                    CoverFlowCard.this.mTempAdIdPoolList.remove(Integer.valueOf(focusImageAdId));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public CoverFlowCard() {
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenSaverListener);
    }

    public void parserItems(CardInfoModel cardInfoModel) {
        if (this.mItem == null) {
            this.mItem = new CoverFlowItem();
        }
        super.parserItems(cardInfoModel);
        this.mItem.setItems(getItems());
        this.mItem.setCardModel(getModel());
        short pl = this.mItem.getCardModel().getBodyPaddingLeft();
        short width = this.mItem.getCardModel().getWidth();
        LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ",CoverFlowItem=" + this.mItem.hashCode() + ", parserItems" + ", card pl: " + pl + ", card width : " + width);
        this.mItem.setWidth(width);
        this.mItem.assignParent(this);
        setItems(Collections.singletonList(this.mItem));
    }

    public CoverFlowItem getItem() {
        return this.mItem;
    }

    public BlockLayout onCreateBlockLayout() {
        LinearLayout layout = new LinearLayout();
        layout.setItemCount(1);
        return layout;
    }

    public void setLine(int line) {
        super.setLine(line);
    }

    public void start() {
        super.start();
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenSaverListener);
        registerObserver();
    }

    public void stop() {
        super.stop();
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenSaverListener);
        removeObserver();
    }

    public void destroy() {
        super.destroy();
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenSaverListener);
        removeObserver();
    }

    private void registerObserver() {
        if (!this.hasRegister) {
            boolean isDefaultPage = getParent().isDefaultPage();
            boolean isFirstLine = getLine() == 0;
            boolean isCatchData = this.mItem.getCardModel().isCacheData;
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", start" + ", current CoverFlowCard is located in default tab :" + isDefaultPage + ", is located in first line : " + isFirstLine + ", is cache data : " + isCatchData);
            if (isDefaultPage && isFirstLine && !isCatchData) {
                GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.FOCUS_IMAGE_AD_DOWNLOAD_COMPLETE, this.mFocusImageAdDataObserver);
                LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", start, register observer : " + IDataBus.FOCUS_IMAGE_AD_DOWNLOAD_COMPLETE);
                this.mItem.addOnScrollListener(this.mScrollListener);
                GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.SHOW_PREVIEW_COMPLETED, this.mStartPreViewObserver);
                if (!this.hasAdsLoaded) {
                    fetchFocusAdData();
                    this.hasAdsLoaded = true;
                }
                this.hasRegister = true;
            }
        }
        sendAdPingback();
    }

    public void sendAdPingback() {
        for (Integer intValue : this.mTempAdIdPoolList) {
            int id = intValue.intValue();
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", start, switch to current CoverFlowCard" + ", focus image fully show, send ad show pingback");
            sendFocusImageAdShowPingback(id);
        }
    }

    private void removeObserver() {
        boolean isFirstLine;
        boolean isDefaultPage = getParent().isDefaultPage();
        if (getLine() == 0) {
            isFirstLine = true;
        } else {
            isFirstLine = false;
        }
        LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", stop" + ", current CoverFlowCard is located in default tab :" + isDefaultPage + ", is located in first line : " + isFirstLine);
        if (isDefaultPage && isFirstLine) {
            GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.FOCUS_IMAGE_AD_DOWNLOAD_COMPLETE, this.mFocusImageAdDataObserver);
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", stop, unRegister observer : " + IDataBus.FOCUS_IMAGE_AD_DOWNLOAD_COMPLETE);
            this.mItem.removeOnScrollListener(this.mScrollListener);
            GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.SHOW_PREVIEW_COMPLETED, this.mStartPreViewObserver);
            this.hasRegister = false;
        }
    }

    public ActionPolicy getActionPolicy() {
        return this.mActionPolicy;
    }

    private void fetchFocusAdData() {
        LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", fetchFocusAdData, request focus image ad data...");
        GetInterfaceTools.getIHomeFocusImageTask().execute();
    }

    private void updateFocusImageAd() {
        List<HomeFocusImageAdModel> adModelList = GetInterfaceTools.getIHomeFocusImageAdProvider().getFocusAdModelList();
        int count = ListUtils.getCount((List) adModelList);
        LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", updateFocusImageAd,size :" + count);
        LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", receive focus ad data, size :" + count);
        if (count != 0) {
            this.adItemList.clear();
            for (HomeFocusImageAdModel adModel : adModelList) {
                ItemInfoModel itemInfoModel = new ItemInfoModel();
                itemInfoModel.setId(String.valueOf(itemInfoModel.hashCode()));
                int rawAdWidth = adModel.getWidth();
                int rawAdHeight = adModel.getHeight();
                LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", ad raw width :" + rawAdWidth + ", ad raw height : " + rawAdHeight);
                short height = this.mCardInfoModel.getItemInfoModels()[0][0].getHeight();
                LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", CoverFlowCard item height, :" + height);
                int width = (rawAdWidth * height) / rawAdHeight;
                itemInfoModel.setHeight(height);
                itemInfoModel.setWidth((short) width);
                itemInfoModel.setItemType(UIKitConfig.ITEM_TYPE_STANDARD);
                itemInfoModel.setStyle(this.mCardInfoModel.getItemInfoModels()[0][1].getStyle());
                HashMap<String, HashMap<String, String>> mCuteViewDatas = new HashMap();
                HashMap<String, String> map = new HashMap();
                map.put("value", adModel.getImageUrl());
                mCuteViewDatas.put("ID_IMAGE", map);
                LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", ad image url: " + adModel.getImageUrl());
                if ("true".equals(adModel.getNeedAdBadge())) {
                    HashMap<String, String> map3 = new HashMap();
                    map3.put("value", CornerBuildTool.CORNER_AD);
                    mCuteViewDatas.put("ID_CORNER_L_T", map3);
                }
                HashMap<String, String> map2 = new HashMap();
                map2.put("text", adModel.getTitle());
                mCuteViewDatas.put("ID_TITLE", map2);
                itemInfoModel.setCuteViewDatas(mCuteViewDatas);
                itemInfoModel.setActionModel(ActionModelFactory.createAdActionModel(adModel));
                this.adItemList.add(parserItem(itemInfoModel));
            }
            this.mItem.updateAds(this.adItemList);
        }
    }

    private boolean sendFocusImageAdShowPingback(int focusImageAdId) {
        boolean isShowingScreenSaver = GetInterfaceTools.getIScreenSaver().isShowScreenSaver();
        boolean isStartPreViewFinished = GetInterfaceTools.getIHomeConstants().isIsStartPreViewFinished();
        boolean childVisible = isChildVisible(this.mItem, false);
        if (!childVisible || isShowingScreenSaver || !isStartPreViewFinished) {
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", sendFocusImageAdShowPingback, is showing screensaver : " + isShowingScreenSaver + "," + "childVisible=" + childVisible + ",isStartPreViewFinished=" + isStartPreViewFinished);
            return false;
        } else if (this.mCardFocusStatus == CardFocusStatus.LOST_FOCUS) {
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", sendFocusImageAdShowPingback, current card has lost focus" + ", do not send ad show pingback");
            return false;
        } else {
            AdsClientUtils.getInstance().onAdStarted(focusImageAdId);
            AdsClientUtils.getInstance().sendAdPingBacks();
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + ", sendFocusImageAdShowPingback, send focus image ad show pingback ,onAdStarted : " + focusImageAdId);
            adShowCount++;
            return true;
        }
    }

    public void checkCoverFlowMsg() {
        boolean z = false;
        if (this.mItem != null && this.mItem.getView() != null) {
            boolean childVisible = isChildVisible(this.mItem, false);
            boolean isShowingScreenSaver = GetInterfaceTools.getIScreenSaver().isShowScreenSaver();
            boolean isStartPreViewFinished = GetInterfaceTools.getIHomeConstants().isIsStartPreViewFinished();
            String str = TAG;
            StringBuilder append = new StringBuilder().append("CoverFlowCard@").append(hashCode()).append(",checkCoverFlowMsg,childVisible=").append(childVisible).append(",!isShowingScreenSaver=");
            if (!isShowingScreenSaver) {
                z = true;
            }
            LogUtils.m1585d(str, append.append(z).append(",isStartPreViewFinished=").append(isStartPreViewFinished).append(",item=").append(this.mItem).append(",item.getView()=").append(this.mItem.getView()).toString());
            if (childVisible && !isShowingScreenSaver && isStartPreViewFinished) {
                this.mItem.getView().sendDelayedMessage();
            } else {
                this.mItem.getView().removeDelayedMessage();
            }
        }
    }

    private void stopCoverFlowMsg(CoverFlowItem item) {
        if (item != null && item.getView() != null) {
            LogUtils.m1585d(TAG, "CoverFlowCard@" + hashCode() + "stopCoverFlowMsg,item=" + item + ",item.getView()=" + item.getView());
            item.getView().removeDelayedMessage();
        }
    }
}
