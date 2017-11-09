package com.gala.video.app.epg.ui.albumlist.data.type;

import android.content.Context;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.sdk.player.PlayParams;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.home.component.item.corner.HomeCornerProvider;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.factory.EPGImageUrlProvider;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;

public class ChannelLabelData<T> implements IData<ChannelLabel> {
    private int mIndexOfCurPage;
    private boolean mIsShowingCard;
    private ChannelLabel mLabel;
    private QLayoutKind mLayout;
    private int mLocationPage;

    public ChannelLabelData(ChannelLabel label, QLayoutKind layout, int locationPage, AlbumInfoModel model) {
        this.mLabel = label;
        this.mLayout = layout;
        this.mLocationPage = locationPage;
    }

    public void click(Context context, Object albumInfoModel) {
        if (albumInfoModel == null || !(albumInfoModel instanceof AlbumInfoModel)) {
            LogUtils.m1571e(getClass().getSimpleName(), "click --- albumInfoModel  = " + albumInfoModel);
            return;
        }
        AlbumInfoModel infoModel = (AlbumInfoModel) albumInfoModel;
        PlayParams playParams = null;
        if ((AlbumInfoFactory.isNewVipChannel(infoModel.getChannelId()) || AlbumInfoFactory.isLiveChannel(infoModel.getChannelId(), infoModel.getPageType())) && SourceTool.PLAYLIST_TYPE.equalsIgnoreCase(infoModel.getDataTagResourceType())) {
            playParams = new PlayParams();
            playParams.playListId = infoModel.getDataTagId();
        } else if (ResourceType.COLLECTION.equals(this.mLabel.getType())) {
            playParams = new PlayParams();
            playParams.playListId = this.mLabel.id;
        }
        if (ResourceType.DIY.equals(this.mLabel.getType())) {
            WebIntentParams w = new WebIntentParams();
            w.pageUrl = this.mLabel.itemPageUrl;
            w.enterType = 13;
            w.buyFrom = "rec";
            w.from = infoModel.getFrom();
            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, w);
        } else {
            ItemUtils.openDetailOrPlay(context, this.mLabel, getText(3), infoModel.getFrom(), infoModel.getBuySource(), playParams);
        }
        if (IAlbumConfig.UNIQUE_CHANNEL_RECOMMEND1.equals(infoModel.getIdentification()) || IAlbumConfig.UNIQUE_CHANNEL_RECOMMEND2.equals(infoModel.getIdentification())) {
            QAPingback.clickChannelLabelItem(infoModel, this.mLabel);
        } else if (!AlbumInfoFactory.isStarPage(infoModel.getPageType())) {
            QAPingback.channelItemClick(getAlbum(), infoModel, this.mIndexOfCurPage);
        }
    }

    public String getImageUrl(int type) {
        return EPGImageUrlProvider.getAlbumImageUrl(this.mLabel, type, this.mLayout, this.mIsShowingCard);
    }

    public String getField(int type) {
        if (this.mLabel == null) {
            return null;
        }
        switch (type) {
            case 1:
                return this.mLabel.albumQipuId;
            case 2:
                return String.valueOf(this.mLabel.channelId);
            case 3:
                return this.mLabel.tvQipuId;
            case 4:
                return getAlbum().eventId;
            case 5:
                return this.mLabel.name;
            default:
                return null;
        }
    }

    public Album getAlbum() {
        return GetInterfaceTools.getCornerProvider().getRealAlbum(this.mLabel);
    }

    public ResourceType getResourceType() {
        return this.mLabel.getType();
    }

    public String getText(int type) {
        switch (type) {
            case 1:
                return GetInterfaceTools.getCornerProvider().getBigViewTitle(this.mLabel);
            case 2:
                return this.mLabel.name;
            case 3:
                return GetInterfaceTools.getCornerProvider().getTitle(this.mLabel, this.mLayout);
            case 8:
                return HomeCornerProvider.getCornerDesc(this.mLabel);
            case 9:
                return ResourceType.COLLECTION.equals(this.mLabel.getType()) ? "" : GetInterfaceTools.getCornerProvider().getScoreRB(getAlbum());
            case 10:
                return ResourceType.COLLECTION.equals(this.mLabel.getType()) ? "" : GetInterfaceTools.getCornerProvider().getDescLB(getAlbum(), this.mLayout);
            case 11:
                return ResourceType.COLLECTION.equals(this.mLabel.getType()) ? "" : GetInterfaceTools.getCornerProvider().getDescRB(getAlbum(), this.mLayout);
            default:
                return null;
        }
    }

    public boolean getCornerStatus(int type) {
        return GetInterfaceTools.getCornerProvider().getCornerInfo(this.mLabel, type);
    }

    public void setIndexOfCurPage(int indexOfCurPage) {
        this.mIndexOfCurPage = indexOfCurPage;
    }

    public void setShowingCard(boolean showingCard) {
        this.mIsShowingCard = showingCard;
    }

    public boolean isShowingCard() {
        return this.mIsShowingCard;
    }

    public ChannelLabel getData() {
        return this.mLabel;
    }
}
