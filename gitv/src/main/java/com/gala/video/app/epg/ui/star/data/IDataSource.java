package com.gala.video.app.epg.ui.star.data;

import com.gala.albumprovider.logic.set.search.SearchPeopleSet.IStarDetailCallback;
import com.gala.albumprovider.model.Tag;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.star.model.StarTaskParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.List;
import java.util.Map;

public interface IDataSource {

    public interface IStarAlbumCallback {
        void onFail(ApiException apiException);

        void onSuccess(Map<String, List<IData>> map, List<Tag> list, StarTaskParams starTaskParams);
    }

    void getDetails(IStarDetailCallback iStarDetailCallback);

    void getTasks(IStarAlbumCallback iStarAlbumCallback);

    void initApi(AlbumInfoModel albumInfoModel);
}
