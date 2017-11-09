package com.gala.video.app.epg.ui.ucenter.record.model;

import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.utils.LOG;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.api.ChannelApi;
import com.gala.video.app.epg.ui.albumlist.data.api.FavouriteApi;
import com.gala.video.app.epg.ui.albumlist.data.api.PlayHistoryApi;
import com.gala.video.app.epg.ui.albumlist.data.api.SubscribeApi;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class AlbumDataImpl implements AlbumDataSource {
    private BaseDataApi mDataApi;
    private AlbumInfoModel mInfoModel;
    private String mPageType;

    public void setAlbumInfoModel(AlbumInfoModel infoModel) {
        this.mInfoModel = infoModel;
        initDataInfo();
    }

    private void initDataInfo() {
        this.mPageType = this.mInfoModel.getPageType();
        LOG.d("mPageType = " + this.mPageType);
        LOG.d("IAlbumConfig.CHANNEL_PAGE = channel_page");
        if (IAlbumConfig.CHANNEL_PAGE.equals(this.mPageType)) {
            this.mDataApi = new ChannelApi(this.mInfoModel);
        } else if (IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY.equals(this.mPageType)) {
            this.mDataApi = new PlayHistoryApi(this.mInfoModel);
        } else if (IAlbumConfig.UNIQUE_FOOT_FAVOURITE.equals(this.mPageType)) {
            this.mDataApi = new FavouriteApi(this.mInfoModel);
        } else if (IAlbumConfig.UNIQUE_FOOT_SUBSCRIBLE.equals(this.mPageType)) {
            this.mDataApi = new SubscribeApi(this.mInfoModel);
        }
    }

    public void loadLabelData(OnLabelFetchedListener labelListener) {
    }

    public void loadAlbumData(OnAlbumFetchedListener albumListener) {
        this.mDataApi.loadAlbumData(albumListener);
    }

    public void resetDataApi(Tag tag) {
        this.mDataApi.resetApi(tag);
    }

    public void notifyPageType() {
        initDataInfo();
    }

    public int getTotalCount() {
        return this.mDataApi.getTotalCount();
    }

    public int getCurPage() {
        return this.mDataApi.getCurPage();
    }

    public List<Tag> getBarLists() {
        return getBarLists(getBarAllLists(), this.mInfoModel.getLocation4Playhistory());
    }

    private List<Tag> getBarLists(List<Tag> list, int location) {
        boolean hasFavourite = EpgAppConfig.isAddFavourite();
        if (location == 2) {
            hasFavourite = false;
        }
        if (!hasFavourite) {
            list.remove(list.size() - 1);
        }
        return list;
    }

    private List<Tag> getBarAllLists() {
        List<Tag> list = new ArrayList();
        Tag tag = new Tag();
        tag.setName(IFootEnum.PLAY_HISTORY_ALL_STR);
        tag.setLevel(1);
        list.add(tag);
        Tag tagAll = new Tag();
        tagAll.setName(IFootEnum.PLAY_HISTORY_LONG_STR);
        list.add(tagAll);
        if (Project.getInstance().getBuild().isSupportSubscribe()) {
            Tag tagSub = new Tag();
            tagSub.setName(IFootEnum.SUBSCRIBE_STR);
            tagSub.setLevel(1);
            list.add(tagSub);
        }
        Tag tagFav = new Tag();
        tagFav.setName(IFootEnum.FAVOURITE_STR);
        tagFav.setLevel(1);
        list.add(tagFav);
        return list;
    }
}
