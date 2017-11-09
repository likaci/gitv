package com.gala.video.app.player.albumdetail.data;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.ViewGroup;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.player.albumdetail.ui.IDetailOverlay;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.pingback.AlbumDetailPingbackUtils;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.item.Item;
import java.util.ArrayList;
import java.util.List;

public class DetailAllViewActionPolicy extends ActionPolicy {
    public static String TAG = "DetailAllViewActionPolicy";
    private DetailPageManage mBindPageManager;
    private IDetailOverlay mDetailOverlay;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public DetailAllViewActionPolicy(DetailPageManage detailPageManage, IDetailOverlay detailOverlay) {
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
        this.mDetailOverlay.setAllViewPlayGif();
        this.mBindPageManager.getAllViewEngine().getPage().hideLoading();
    }

    private void requestDefaultFocus(ViewGroup parent) {
        parent.requestFocus();
    }

    private Item getItem(int position) {
        if (position >= this.mDetailOverlay.getAllViewEngine().getPage().getItemCount()) {
            return null;
        }
        return this.mDetailOverlay.getAllViewEngine().getPage().getItem(position);
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
        BaseActionModel baseActionModel = item.getModel().getActionModel();
        this.mDetailOverlay.startPlayFromAllView(true);
        PlayParams playParams = new PlayParams();
        if (baseActionModel instanceof AlbumVideoLiveActionModel) {
            if (card.getModel().mSource.equals(Source.TRAILERS) || card.getModel().mSource.equals(Source.ABOUT_TOPIC)) {
                String sourceType = card.getModel().mSource;
                List<Album> trailerlist = new ArrayList();
                ItemInfoModel[][] itemInfoModels = card.getModel().getItemInfoModels();
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
                String cardLine = "" + ClickPingbackUtils.getLine(card.getParent(), item.getParent(), item);
                String allline = "" + card.getAllLine();
                AlbumDetailPingbackUtils.getInstance().setEntryAllTitle(item.getModel().getCuteViewData("ID_TITLE", "text"));
                ClickPingbackUtils.itemClickForPingbackPost(ClickPingbackUtils.composeCommonItemPingMap(parent.getContext(), pos + 1, card.getModel().mCardId, cardLine, allline, item));
                this.mDetailOverlay.clearAlbumListDefaultSelectedTextColor();
                this.mDetailOverlay.startTrailers(playParams);
                this.mDetailOverlay.setCurrentFocusView(null);
                this.mDetailOverlay.playTraAllView();
                baseActionModel.setCanAction(false);
            } else {
                if (this.mDetailOverlay.getIntentModel() != null) {
                    this.mDetailOverlay.getIntentModel().setPlayIndex(pos);
                }
                baseActionModel.setCanAction(true);
                ((AlbumVideoLiveActionModel) baseActionModel).setIntentModel(this.mDetailOverlay.getIntentModel());
            }
            CardFocusHelper.forceVisible(parent.getContext(), false);
        }
    }
}
