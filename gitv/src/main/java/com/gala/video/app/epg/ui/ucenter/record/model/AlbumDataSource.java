package com.gala.video.app.epg.ui.ucenter.record.model;

import com.gala.albumprovider.model.Tag;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import java.util.List;

public interface AlbumDataSource {
    List<Tag> getBarLists();

    int getCurPage();

    int getTotalCount();

    void loadAlbumData(OnAlbumFetchedListener onAlbumFetchedListener);

    void loadLabelData(OnLabelFetchedListener onLabelFetchedListener);

    void notifyPageType();

    void resetDataApi(Tag tag);

    void setAlbumInfoModel(AlbumInfoModel albumInfoModel);
}
