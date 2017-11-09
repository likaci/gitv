package com.gala.video.app.epg.uikit.ui.multisubject;

import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.epg.home.data.constants.HomeConstants;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.multisubject.model.MultiSubjectInfoModel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.MultiSubjectPingbackUitls;
import com.gala.video.lib.share.uikit.action.model.BannerAdActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class MultiSubjectPingbackActionPolicy extends ActionPolicy {
    private String TAG = "MultiSubjectPingbackActionPolicy";
    private SparseArray<Long> mBannerAdShowList = new SparseArray();
    private int mConstant = 1000;
    private MultiSubjectInfoModel mMultiSubjectInfoModel;
    private Page mPage;
    private SparseArray<Long> mShowList = new SparseArray();
    private MyObserver mStartPreviewObserver = new MyObserver() {
        public void update(String event) {
            LogUtils.d(MultiSubjectPingbackActionPolicy.this.TAG, "checkAndSendBannerAdShowPingback, mStartPreviewObserver, show preview completed");
            for (int i = 0; i < MultiSubjectPingbackActionPolicy.this.mBannerAdShowList.size(); i++) {
                long bannerAdId = ((Long) MultiSubjectPingbackActionPolicy.this.mBannerAdShowList.get(MultiSubjectPingbackActionPolicy.this.mBannerAdShowList.keyAt(i))).longValue();
                AdsClientUtils.getInstance().onAdStarted((int) bannerAdId);
                LogUtils.d(MultiSubjectPingbackActionPolicy.this.TAG, "checkAndSendBannerAdShowPingback, mStartPreviewObserver, send banner ad show pingback, onAdStarted:" + bannerAdId);
            }
        }
    };

    public MultiSubjectPingbackActionPolicy(Page page, MultiSubjectInfoModel multiSubjectInfoModel) {
        this.mPage = page;
        this.mMultiSubjectInfoModel = multiSubjectInfoModel;
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

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
        MultiSubjectPingbackUitls.getInstance().setS1(this.mMultiSubjectInfoModel.getBuysource());
        MultiSubjectPingbackUitls.getInstance().setS2(this.mMultiSubjectInfoModel.getFrom());
        MultiSubjectPingbackUitls.getInstance().setE(this.mMultiSubjectInfoModel.getE());
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
                if (!isItemViewVisiable(cast(parent), i)) {
                    this.mShowList.remove(key);
                } else if (this.mShowList.indexOfKey(key) < 0) {
                    this.mShowList.append(key, Long.valueOf(System.currentTimeMillis()));
                }
                checkAndSendBannerAdShowPingback(card, item);
            }
        } catch (Exception e) {
            LogUtils.e(this.TAG, "initTimestamp.Exception e.getMessage()= " + e.getMessage());
            e.printStackTrace();
        }
    }

    boolean isItemViewVisiable(BlocksView blocksView, int position) {
        View view = blocksView.getViewByPosition(position);
        int top = view.getTop() - blocksView.getScrollY();
        int bottom = view.getBottom() - blocksView.getScrollY();
        if ((view instanceof StandardItemView) && ((StandardItemView) view).isTitleoutType()) {
            bottom -= ResourceUtil.getPx(34);
        }
        int parentHeight = blocksView.getHeight();
        if (top < 0 || top >= parentHeight || bottom <= 0 || bottom > parentHeight) {
            return false;
        }
        return true;
    }

    private void checkAndSendBannerAdShowPingback(Card card, Item item) {
        boolean partialVisible = item.isVisible(false);
        boolean fullyVisible = item.isVisible(true);
        ItemInfoModel itemInfoModel = item.getModel();
        if (itemInfoModel != null) {
            BaseActionModel baseActionModel = itemInfoModel.getActionModel();
            if (baseActionModel != null) {
                if (ItemDataType.BANNER_IMAGE_AD.equals(baseActionModel.getItemType())) {
                    int key = (card.getLine() * this.mConstant) + item.getLine();
                    if (!partialVisible && !fullyVisible) {
                        this.mBannerAdShowList.remove(key);
                        LogUtils.d(this.TAG, "checkAndSendBannerAdShowPingback, banner ad is not visible now");
                    } else if (this.mBannerAdShowList.indexOfKey(key) < 0) {
                        int bannerAdId = ((BannerAdActionModel) baseActionModel).getCommonAdData().getAdId();
                        this.mBannerAdShowList.append(key, Long.valueOf((long) bannerAdId));
                        if (HomeConstants.mIsStartPreViewFinished) {
                            AdsClientUtils.getInstance().onAdStarted(bannerAdId);
                            LogUtils.d(this.TAG, "checkAndSendBannerAdShowPingback, send banner ad show pingback, onAdStarted:" + bannerAdId);
                            return;
                        }
                        LogUtils.d(this.TAG, "checkAndSendBannerAdShowPingback, start preview page not finished, not send banner show pingback");
                    }
                }
            }
        }
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        onSendMultiSubjectCardShowPingback(cast(parent), this.mPage, true);
    }

    public void onItemAnimatorFinished(ViewGroup parent) {
        initTimestamp(parent);
    }

    void clearShowList() {
        this.mShowList.clear();
    }

    public int getLine(Page page, Card currentCard, Item item) {
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

    public void onSendMultiSubjectCardShowPingback(BlocksView blocksView, Page page, boolean isScroll) {
        try {
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
                            if (((isScroll && !isItemViewVisiable(blocksView, i)) || !isScroll) && this.mShowList.indexOfKey(key) >= 0) {
                                if (System.currentTimeMillis() - ((Long) this.mShowList.get(key)).longValue() > 500) {
                                    int line = getLine(page, card, item);
                                    CardInfoModel cardInfoModel = card.getModel();
                                    String block = cardInfoModel.mCardId;
                                    String qpid = cardInfoModel.mCardId;
                                    if (TextUtils.equals("banner", cardInfoModel.mSource)) {
                                        block = "通栏广告";
                                        qpid = "通栏广告";
                                    }
                                    GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.MULTISUJECT_CARD_SHOW_PINGBACK).addItem("qtcurl", MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI).addItem("block", block).addItem("qpid", qpid).addItem("e", this.mMultiSubjectInfoModel.getE()).addItem("line", (line + 1) + "").addItem("s2", this.mMultiSubjectInfoModel.getFrom()).addItem("plid", this.mMultiSubjectInfoModel.getItemId()).post();
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
        } catch (Exception e) {
            LogUtils.e(this.TAG, "onSendCardShowPingback.Exception e.getMessage()= " + e.getMessage());
            e.printStackTrace();
        }
    }
}
