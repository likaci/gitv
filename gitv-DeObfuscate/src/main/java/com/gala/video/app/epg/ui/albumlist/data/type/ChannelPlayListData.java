package com.gala.video.app.epg.ui.albumlist.data.type;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.video.app.epg.ui.albumlist.data.factory.EPGImageUrlProvider;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class ChannelPlayListData<T> implements IData<ChannelPlayListLabel> {
    private int mIndexOfCurPage;
    private int mLocationPage;
    private ChannelPlayListLabel mPlayListLabel;

    public ChannelPlayListData(ChannelPlayListLabel label, int locationPage, AlbumInfoModel model) {
        this.mPlayListLabel = label;
        this.mLocationPage = locationPage;
    }

    public void click(Context context, Object albumInfoModel) {
        if (albumInfoModel == null || !(albumInfoModel instanceof AlbumInfoModel)) {
            LogUtils.m1571e(getClass().getSimpleName(), "click --- albumInfoModel  = " + albumInfoModel);
            return;
        }
        AlbumInfoModel infoModel = (AlbumInfoModel) albumInfoModel;
        ItemUtils.openDetailOrPlay(context, this.mPlayListLabel, infoModel.getFrom(), infoModel.getBuySource());
        if (!AlbumInfoFactory.isStarPage(infoModel.getPageType())) {
            QAPingback.albumClickPingback(this.mPlayListLabel.id, this.mPlayListLabel.channelId, this.mPlayListLabel.id, infoModel);
        }
    }

    public String getImageUrl(int type) {
        return EPGImageUrlProvider.getAlbumImageUrl(this.mPlayListLabel);
    }

    public String getField(int type) {
        switch (type) {
            case 1:
                return this.mPlayListLabel.id;
            case 2:
                return String.valueOf(this.mPlayListLabel.channelId);
            default:
                return null;
        }
    }

    public Album getAlbum() {
        return null;
    }

    public ResourceType getResourceType() {
        return null;
    }

    public String getText(int type) {
        switch (type) {
            case 3:
                return this.mPlayListLabel.name;
            default:
                return null;
        }
    }

    public boolean getCornerStatus(int type) {
        return false;
    }

    public void setIndexOfCurPage(int indexOfCurPage) {
        this.mIndexOfCurPage = indexOfCurPage;
    }

    public void setShowingCard(boolean b) {
    }

    public boolean isShowingCard() {
        return false;
    }

    public ChannelPlayListLabel getData() {
        return this.mPlayListLabel;
    }
}
