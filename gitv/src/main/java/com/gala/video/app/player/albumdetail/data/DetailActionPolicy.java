package com.gala.video.app.player.albumdetail.data;

import android.util.Log;
import android.view.ViewGroup;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.albumlist.utils.LOG;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.ui.IDetailOverlay;
import com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.pingback.AlbumDetailPingbackUtils;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class DetailActionPolicy extends ActionPolicy {
    private static final int CACHE_CARD_SIZE = 4;
    public static String TAG = "DetailActionPolicy";
    private DetailPageManage mBindPageManager;
    private IDetailOverlay mDetailOverlay;
    private boolean mRequestDefaultFocus = true;

    public DetailActionPolicy(DetailPageManage detailPageManage, IDetailOverlay detailOverlay) {
        this.mBindPageManager = detailPageManage;
        this.mDetailOverlay = detailOverlay;
    }

    public void onFirstLayout(ViewGroup parent) {
        LogUtils.d(TAG, "onFirstLayout ,is started = " + this.mBindPageManager.mstarted);
        if (!this.mBindPageManager.mstarted) {
            this.mBindPageManager.getUIkitEngine().start();
            this.mBindPageManager.mstarted = true;
        }
        requestDefaultFocus(parent);
        postScrollTopEvent(parent);
    }

    private void postScrollStopEvent(int position) {
        LogUtils.d(TAG, "postScrollStopEvent ");
        Card card = this.mBindPageManager.getUIkitEngine().getPage().getItem(position).getParent();
        UikitEvent uiEvent = new UikitEvent();
        uiEvent.eventType = 18;
        uiEvent.uikitEngineId = this.mBindPageManager.getUIkitEngine().getId();
        uiEvent.cardInfoModel = card.getModel();
        this.mBindPageManager.onPostEvent(uiEvent);
        LOG.d("UIKIT_SCROLL_PLACE");
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        LogUtils.d(TAG, "onScroll ");
        Page page = this.mBindPageManager.getUIkitEngine().getPage();
        Card card = page.getItem(cast(parent).getFocusPosition()).getParent();
        if (card != null && page.shouldLoadMore()) {
            synchronized (page) {
                List<Card> cardList = page.getCards();
                Card lastCard = (Card) cardList.get(cardList.size() - 1);
                LogUtils.d(TAG, "lastCard =  " + lastCard.getModel());
                LogUtils.d(TAG, "cardList.indexOf(card) =  " + cardList.indexOf(card));
                LogUtils.d(TAG, " cardList.size() =  " + cardList.size());
                if (lastCard != null && cardList.size() - cardList.indexOf(card) <= 4) {
                    LogUtils.d(TAG, "begin to load ");
                    UikitEvent uiEvent = new UikitEvent();
                    uiEvent.eventType = 70;
                    uiEvent.uikitEngineId = this.mBindPageManager.getUIkitEngine().getId();
                    uiEvent.cardInfoModel = lastCard.getModel();
                    this.mBindPageManager.onPostEvent(uiEvent);
                    LOG.d("UIKIT_ADD_DETAIL_CARDS");
                }
            }
        }
    }

    public void onScrollStop(ViewGroup parent) {
        LogUtils.d(TAG, "onScrollStop ");
        ((DetailOverlay) this.mDetailOverlay).onScrollStop(parent);
        super.onScrollStop(parent);
        postScrollStopEvent(((BlocksView) parent).getFocusPosition());
        postScrollTopEvent(parent);
    }

    public void onScrollBefore(ViewGroup parent, ViewHolder holder) {
        LogRecordUtils.logd(TAG, "onScrollBefore() ");
        ((DetailOverlay) this.mDetailOverlay).onScrollBefore(parent, holder);
    }

    public void onScrollStart(ViewGroup parent) {
        LogRecordUtils.logd(TAG, ">> onScrollStart");
        ((DetailOverlay) this.mDetailOverlay).onScrollStart(parent);
    }

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
        int pos = holder.getLayoutPosition();
        Log.v(TAG, "pos = " + pos);
        Item item = getItem(pos);
        if (item == null) {
            Log.v(TAG, "onItemClick ,item is null");
            return;
        }
        Log.v(TAG, "onItemClick ,item is not null");
        Card card = item.getParent();
        pos -= card.getBlockLayout().getFirstPosition();
        Log.v(TAG, "new pos = " + pos);
        this.mDetailOverlay.startPlayFromAllView(false);
        BaseActionModel baseActionModel = item.getModel().getActionModel();
        if (baseActionModel.getItemType() == ItemDataType.ENTER_ALL) {
            NetworkStatePresenter.getInstance().setContext(parent.getContext());
            if (NetworkStatePresenter.getInstance().checkStateIllegal()) {
                clickPingback(parent, pos, item, card);
                this.mBindPageManager.showAllView(card.getModel());
                this.mDetailOverlay.showAllViews();
                baseActionModel.setCanAction(false);
                CardFocusHelper.forceVisible(parent.getContext(), false);
                return;
            }
            return;
        }
        baseActionModel.setCanAction(true);
        PlayParams playParams = new PlayParams();
        if (baseActionModel instanceof AlbumVideoLiveActionModel) {
            if (card.getModel().mSource.equals(Source.TRAILERS) || card.getModel().mSource.equals(Source.ABOUT_TOPIC)) {
                String sourceType = card.getModel().mSource;
                List<Album> trailerlist = new ArrayList();
                List<Album> albumList = (List) this.mBindPageManager.getAllEnterMap().get(sourceType);
                CardInfoModel cardInfoModel = CardInfoBuildTool.buildAlbumCard(card.getModel(), albumList, true);
                if (cardInfoModel != null) {
                    ItemInfoModel[][] itemInfoModels = cardInfoModel.getItemInfoModels();
                    int i = 0;
                    while (i < itemInfoModels.length) {
                        int j = 0;
                        while (j < itemInfoModels[i].length) {
                            if (!(itemInfoModels[i][j] == null || itemInfoModels[i][j].getActionModel() == null)) {
                                trailerlist.add(((AlbumVideoLiveActionModel) itemInfoModels[i][j].getActionModel()).getLabel().getVideo());
                            }
                            j++;
                        }
                        i++;
                    }
                } else {
                    trailerlist = albumList;
                }
                playParams.continuePlayList = trailerlist;
                playParams.playListId = "";
                playParams.playIndex = pos;
                if (Source.TRAILERS.equals(sourceType)) {
                    playParams.sourceType = SourceType.DETAIL_TRAILERS;
                    playParams.isDetailTrailer = true;
                } else if (Source.ABOUT_TOPIC.equals(sourceType)) {
                    playParams.sourceType = SourceType.DETAIL_RELATED;
                    playParams.isDetailRelated = true;
                }
                this.mDetailOverlay.getIntentModel().setSourceType(sourceType);
                this.mDetailOverlay.getIntentModel().setAlbumList(trailerlist);
            }
            if (this.mDetailOverlay.isWindowPlay() && (card.getModel().mSource.equals(Source.TRAILERS) || card.getModel().mSource.equals(Source.ABOUT_TOPIC))) {
                NetworkStatePresenter.getInstance().setContext(parent.getContext());
                if (NetworkStatePresenter.getInstance().checkStateIllegal()) {
                    clickPingback(parent, pos, item, card);
                    this.mDetailOverlay.startTrailers(playParams);
                    this.mDetailOverlay.setCurrentFocusView(null);
                    this.mDetailOverlay.clearAlbumListDefaultSelectedTextColor();
                    baseActionModel.setCanAction(false);
                    CardFocusHelper.forceVisible(parent.getContext(), false);
                    return;
                }
                return;
            }
            if (this.mDetailOverlay.getIntentModel() != null) {
                this.mDetailOverlay.getIntentModel().setPlayIndex(pos);
            }
            baseActionModel.setCanAction(true);
            ((AlbumVideoLiveActionModel) baseActionModel).setIntentModel(this.mDetailOverlay.getIntentModel());
        }
    }

    private void clickPingback(ViewGroup parent, int pos, Item item, Card card) {
        String cardLine = "" + ClickPingbackUtils.getLine(card.getParent(), item.getParent(), item);
        String allline = "" + card.getAllLine();
        AlbumDetailPingbackUtils.getInstance().setEntryAllTitle(item.getModel().getCuteViewData("ID_TITLE", "text"));
        ClickPingbackUtils.itemClickForPingbackPost(ClickPingbackUtils.composeCommonItemPingMap(parent.getContext(), pos + 1, card.getModel().mCardId, cardLine, allline, item));
    }

    private Item getItem(int position) {
        if (position >= this.mDetailOverlay.getEngine().getPage().getItemCount()) {
            return null;
        }
        return this.mDetailOverlay.getEngine().getPage().getItem(position);
    }

    public void onFocusLost(ViewGroup parent, ViewHolder holder) {
        LogUtils.d(TAG, "onFocusLost ");
        this.mRequestDefaultFocus = false;
    }

    public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
        LogUtils.d(TAG, "onFocusPositionChanged ,position = " + position + " hasFocus = " + hasFocus);
        if (position == 0 && !hasFocus) {
            LogUtils.d(TAG, "top  onFocusPositionChanged");
            this.mDetailOverlay.getEngine().getPage().setTopBarHeight(ResourceUtil.getDimen(R.dimen.dimen_03dp));
        } else if (position == 0 && hasFocus) {
            this.mDetailOverlay.getEngine().getPage().setTopBarHeight(ResourceUtil.getDimen(R.dimen.dimen_0dp));
            ((DetailOverlay) this.mDetailOverlay).dealWithBasicCardVisible();
        } else if (hasFocus) {
            this.mDetailOverlay.getEngine().getPage().setTopBarHeight(1);
        }
    }

    public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
        LogUtils.d(TAG, "onItemFocusChanged ");
    }

    private int getCardIndex(int position) {
        if (this.mBindPageManager != null) {
            return this.mBindPageManager.getUIkitEngine().getPage().getItem(position).getParent().getLine();
        }
        return 0;
    }

    private boolean isPageScrolled() {
        return ((DetailOverlay) this.mDetailOverlay).isViewScrolled();
    }

    private void postScrollTopEvent(ViewGroup parent) {
        LogUtils.d(TAG, "postScrollTopEvent ");
        if (this.mBindPageManager.getUIkitEngine().getPage().isOnTop()) {
            UikitEvent uiEvent = new UikitEvent();
            uiEvent.eventType = 16;
            uiEvent.uikitEngineId = this.mBindPageManager.getUIkitEngine().getId();
            this.mBindPageManager.onPostEvent(uiEvent);
            LOG.d("UITKI_SCROLL_TOP");
        }
    }

    private void requestDefaultFocus(ViewGroup parent) {
        if (this.mRequestDefaultFocus && !isPageScrolled()) {
            parent.requestFocus();
        }
    }
}
