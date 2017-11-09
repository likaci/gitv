package com.gala.video.app.epg.ui.albumlist.data.type;

import android.content.Context;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.sdk.player.PlayParams;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ResourceType;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.albumlist.data.factory.EPGImageUrlProvider;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel.SearchInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class AlbumData<T> implements IData<Album> {
    private final String TAG = "EPG/album4/AlbumData";
    private Album mAlbum;
    private int mIndexOfCurPage;
    private boolean mIsShowingCard;
    private QLayoutKind mLayout;
    private int mLocationPage;
    private String mTitle;
    private String mUrl;

    public AlbumData(Album album, QLayoutKind layout, int locationPage) {
        this.mAlbum = album;
        this.mLayout = layout;
        this.mLocationPage = locationPage;
        if (!DataInfoProvider.isCardData(this.mAlbum)) {
            this.mUrl = getImageUrl(0);
        }
    }

    public void click(Context context, Object albumInfoModel) {
        if (albumInfoModel == null || !(albumInfoModel instanceof AlbumInfoModel)) {
            LogUtils.e("EPG/album4/AlbumData", "click --- albumInfoModel  = " + albumInfoModel);
            return;
        }
        AlbumInfoModel infoModel = (AlbumInfoModel) albumInfoModel;
        String identification = infoModel.getIdentification();
        if (IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY.equals(identification)) {
            clickPlayhistoryItem(context, infoModel);
        } else if (IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD.equals(identification)) {
            clickSearchResultItem(context, infoModel);
        } else {
            PlayParams playParams = null;
            if (AlbumInfoFactory.isNewVipChannel(infoModel.getChannelId()) || AlbumInfoFactory.isLiveChannel(infoModel.getChannelId(), infoModel.getPageType())) {
                if (SourceTool.PLAYLIST_TYPE.equalsIgnoreCase(infoModel.getDataTagResourceType())) {
                    playParams = new PlayParams();
                    playParams.playListId = infoModel.getDataTagId();
                }
            } else if (SourceTool.LABEL_CHANNEL_TAG.equalsIgnoreCase(infoModel.getDataTagType())) {
                playParams = new PlayParams();
                playParams.playListId = infoModel.getDataTagId();
            }
            ItemUtils.openDetailOrPlay(context, (IData) this, infoModel.getFrom(), playParams, infoModel.getBuySource());
            if (!AlbumInfoFactory.isStarPage(infoModel.getPageType())) {
                QAPingback.channelItemClick(this.mAlbum, infoModel, this.mIndexOfCurPage);
            }
        }
        LogUtils.e("EPG/album4/AlbumData", "click ---type=" + identification + ", name : " + (this.mAlbum != null ? this.mAlbum.name : ""));
    }

    public String getImageUrl(int type) {
        if (this.mUrl == null) {
            return EPGImageUrlProvider.getAlbumImageUrl(this, this.mLayout);
        }
        return this.mUrl;
    }

    public String getField(int type) {
        switch (type) {
            case 1:
                return this.mAlbum.qpId;
            case 2:
                return String.valueOf(this.mAlbum.chnId);
            case 3:
                return this.mAlbum.tvQid;
            case 4:
                return this.mAlbum.eventId;
            case 5:
                return this.mAlbum.name;
            case 6:
                return this.mAlbum.subKey;
            case 7:
                return String.valueOf(this.mAlbum.subType);
            default:
                return null;
        }
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public ResourceType getResourceType() {
        return null;
    }

    public String getText(int type) {
        switch (type) {
            case 3:
                if (this.mTitle != null) {
                    return this.mTitle;
                }
                this.mTitle = GetInterfaceTools.getCornerProvider().getTitle(this.mAlbum, this.mLayout);
                return this.mTitle;
            case 9:
                return GetInterfaceTools.getCornerProvider().getScoreRB(this.mAlbum);
            case 10:
                return GetInterfaceTools.getCornerProvider().getDescLB(this.mAlbum, this.mLayout);
            case 11:
                return GetInterfaceTools.getCornerProvider().getDescRB(this.mAlbum, this.mLayout);
            default:
                return null;
        }
    }

    public boolean getCornerStatus(int type) {
        return GetInterfaceTools.getCornerProvider().getCornerInfo(this.mAlbum, type);
    }

    public void setIndexOfCurPage(int indexOfCurPage) {
        this.mIndexOfCurPage = indexOfCurPage;
    }

    private void clickPlayhistoryItem(Context context, AlbumInfoModel infoModel) {
        ItemUtils.startVideoPlay(context, this.mAlbum, infoModel.getFrom(), infoModel.getBuySource());
        QAPingback.albumClickPingback(this.mAlbum.tvQid, this.mAlbum.chnId, "", infoModel);
    }

    private void clickSearchResultItem(Context context, AlbumInfoModel infoModel) {
        SearchInfoModel searchModel = infoModel.getSearchModel();
        String r = "";
        String plid = "";
        if (this.mAlbum.getType() == AlbumType.PEOPLE) {
            AlbumUtils.startSearchResultPage(context, infoModel.getChannelId(), getText(3), searchModel.getClickType(), this.mAlbum.qpId, infoModel.getChannelName());
            r = this.mAlbum.qpId;
        } else {
            PlayParams params = new PlayParams();
            params.playListId = "0".equals(infoModel.getDataTagId()) ? null : infoModel.getDataTagId();
            ItemUtils.openDetailOrPlay(context, (IData) this, infoModel.getFrom(), params, infoModel.getBuySource());
            if (AlbumType.PLAYLIST.equals(this.mAlbum.getType())) {
                r = this.mAlbum.qpId;
                plid = this.mAlbum.qpId;
            } else {
                r = GetInterfaceTools.getAlbumInfoHelper().isSingleType(this.mAlbum) ? this.mAlbum.tvQid : this.mAlbum.qpId;
            }
        }
        QAPingback.searchItemClickPingback(context, this.mAlbum, infoModel.getFocusPosition() + 1, searchModel.getClickType(), searchModel.getKeyWord(), this.mLocationPage, this.mIsShowingCard);
        QAPingback.searchResultClickPingback(r, this.mAlbum.chnId, plid, infoModel);
    }

    public void setShowingCard(boolean showingCard) {
        this.mIsShowingCard = showingCard;
    }

    public boolean isShowingCard() {
        return this.mIsShowingCard;
    }

    public Album getData() {
        return this.mAlbum;
    }
}
