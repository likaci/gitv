package com.gala.video.app.player.albumdetail.data;

import android.content.Context;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.app.player.albumdetail.AlbumDetailActivity;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.action.model.BannerAdActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
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
import java.util.List;

public class PingbackActionPolicy extends ActionPolicy {
    private String TAG = "Albumdetail/Data/PingbackActionPolicy";
    private boolean isAllView = false;
    private AlbumInfo mAlbumInfo;
    private SparseArray<Long> mBannerAdShowList = new SparseArray();
    private int mConstant = 1000;
    private Context mContext;
    private Page mPage;
    private SparseArray<Long> mShowList = new SparseArray();
    private MyObserver mStartPreviewObserver = new C12941();

    class C12941 implements MyObserver {
        C12941() {
        }

        public void update(String event) {
            LogUtils.m1568d(PingbackActionPolicy.this.TAG, "checkAndSendBannerAdShowPingback, mStartPreviewObserver, show preview completed");
            for (int i = 0; i < PingbackActionPolicy.this.mBannerAdShowList.size(); i++) {
                long bannerAdId = ((Long) PingbackActionPolicy.this.mBannerAdShowList.get(PingbackActionPolicy.this.mBannerAdShowList.keyAt(i))).longValue();
                AdsClientUtils.getInstance().onAdStarted((int) bannerAdId);
                LogUtils.m1568d(PingbackActionPolicy.this.TAG, "checkAndSendBannerAdShowPingback, mStartPreviewObserver, send banner ad show pingback, onAdStarted:" + bannerAdId);
            }
        }
    }

    public PingbackActionPolicy(Page page, Context context) {
        this.mContext = context;
        this.mPage = page;
        if (page.isDefaultPage()) {
            GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.SHOW_PREVIEW_COMPLETED, this.mStartPreviewObserver);
            LogUtils.m1568d(this.TAG, "PingbackActionPolicy, current page is default page, register show_preview_completed");
        }
    }

    public void setAlbumInfo(AlbumInfo Info) {
        this.mAlbumInfo = Info;
        String now_qpid = String.valueOf(Info.getAlbumId());
        PingBackCollectionFieldUtils.setNow_c1(String.valueOf(Info.getChannelId()));
        PingBackCollectionFieldUtils.setNow_qpid(now_qpid);
    }

    public void setisAllView(boolean allview) {
        this.isAllView = allview;
    }

    public void onFirstLayout(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void onScrollStop(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void initTimestamp(ViewGroup parent) {
        LogUtils.m1577w(this.TAG, "initTimestamp");
        try {
            int first = cast(parent).getFirstAttachedPosition();
            int last = cast(parent).getLastAttachedPosition();
            for (int i = first; i <= last; i++) {
                if (i < 0) {
                    LogUtils.m1577w(this.TAG, "initTimestamp. i <0 ;i = " + i);
                    return;
                }
                Item item = this.mPage.getItem(i);
                if (item == null) {
                    LogUtils.m1577w(this.TAG, "initTimestamp. item==null. i = " + i);
                    return;
                }
                Card card = item.getParent();
                if (card == null) {
                    LogUtils.m1577w(this.TAG, "initTimestamp. card==null. i = " + i);
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
            LogUtils.m1571e(this.TAG, "initTimestamp.Exception e.getMessage()= " + e.getMessage());
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
                        LogUtils.m1568d(this.TAG, "checkAndSendBannerAdShowPingback, banner ad is not visible now");
                    } else if (this.mBannerAdShowList.indexOfKey(key) < 0) {
                        int bannerAdId = ((BannerAdActionModel) baseActionModel).getCommonAdData().getAdId();
                        this.mBannerAdShowList.append(key, Long.valueOf((long) bannerAdId));
                        AdsClientUtils.getInstance().onAdStarted(bannerAdId);
                        LogUtils.m1568d(this.TAG, "checkAndSendBannerAdShowPingback, send banner ad show pingback, onAdStarted:" + bannerAdId);
                    }
                }
            }
        }
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        if (!this.isAllView) {
            onSendCardShowPingback(cast(parent), this.mPage, true, false, false);
        }
    }

    public void onItemAnimatorFinished(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void onSendCardShowPingback(BlocksView blocksView, Page page, boolean isScroll, boolean isStopSend, boolean isIgnoreTime) {
        LogRecordUtils.logd(this.TAG, "isIgnoreTime = " + isIgnoreTime + " isStopSend = " + isStopSend + " isScroll = " + isScroll);
        String e;
        try {
            if (this.mAlbumInfo == null || this.mAlbumInfo.getAlbum() == null) {
                LogRecordUtils.logd(this.TAG, "sendCardShowPingback, data invalid!!!");
                return;
            }
            String area = "";
            String bstp = "1";
            String c1 = String.valueOf(this.mAlbumInfo.getChannelId());
            String rfr = ((AlbumDetailActivity) this.mContext).getItem("rfr").getValue();
            String now_c1 = String.valueOf(this.mAlbumInfo.getChannelId());
            e = ((AlbumDetailActivity) this.mContext).getItem("e").getValue();
            String count = "";
            String qtcurl = "detail";
            String adCount = "0";
            String qpid = "";
            int first = blocksView.getFirstAttachedPosition();
            int last = blocksView.getLastAttachedPosition();
            int i = first;
            while (i <= last) {
                if (i < 0) {
                    LogUtils.m1577w(this.TAG, "onSendCardShowPingback. i <0 ;i = " + i);
                } else {
                    Item item = page.getItem(i);
                    if (item == null) {
                        LogUtils.m1577w(this.TAG, "onSendCardShowPingback. item==null. i = " + i);
                    } else {
                        Card card = item.getParent();
                        if (card == null) {
                            LogUtils.m1577w(this.TAG, "onSendCardShowPingback. card==null. i = " + i);
                        } else {
                            int key = (card.getLine() * this.mConstant) + item.getLine();
                            if (this.isAllView) {
                                qtcurl = "all_detail";
                                qpid = card.getModel().mCardId;
                            } else {
                                qpid = this.mAlbumInfo.getAlbum().tvQid;
                            }
                            if (((isScroll && !isItemViewVisiable(blocksView, i)) || !isScroll) && this.mShowList.indexOfKey(key) >= 0) {
                                if (System.currentTimeMillis() - ((Long) this.mShowList.get(key)).longValue() > 500 || isIgnoreTime) {
                                    int line = getLine(page, card, item);
                                    int cardLine = item.getLine();
                                    int allLine = card.getAllLine();
                                    CardInfoModel cardInfoModel = card.getModel();
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
                                        }
                                    }
                                    PingBackCollectionFieldUtils.setE(e);
                                    PingBackCollectionFieldUtils.setRfr(rfr);
                                    PingBackCollectionFieldUtils.setNow_c1(now_c1);
                                    LogUtils.m1568d(this.TAG, "send pingback:  QTCURL = " + qtcurl + " ;BLOCK = " + block + " ;QPID=" + qpid + " ;LINE=" + (line + 1) + " ;CARDLINE=" + (cardLine + 1) + " ;E=" + e + " ;COUNT=" + count + " ;AD_COUNT =" + adCount + " ;ALLLINE =" + allLine);
                                    if (card.getType() == 119 && line == 0) {
                                        LogUtils.m1568d(this.TAG, "detail view top card not send pingback");
                                    } else {
                                        GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.DETAIL_CARD_SHOW_PINGBACK).addItem("bstp", bstp).addItem("c1", c1).addItem("rfr", rfr).addItem("now_c1", now_c1).addItem("area", area).addItem("qtcurl", qtcurl).addItem("block", block).addItem("qpid", qpid).addItem("line", (line + 1) + "").addItem(Keys.CARDLINE, (cardLine + 1) + "").addItem("e", e).addItem("count", count).addItem("adcount", adCount).addItem(Keys.ALLLINE, allLine + "").post();
                                        if (isStopSend) {
                                            if (!isScroll) {
                                                clearShowList();
                                            }
                                        }
                                    }
                                }
                                LogUtils.m1577w(this.TAG, "time too closed ");
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
            LogUtils.m1571e(this.TAG, "onSendCardShowPingback.Exception e.getMessage()= " + e2.getMessage());
            e2.printStackTrace();
        }
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
                    LogUtils.m1577w(this.TAG, "getLine. card==null.");
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
}
