package com.gala.video.app.epg.ui.star.domain;

import com.gala.albumprovider.logic.set.search.SearchPeopleSet.IStarDetailCallback;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.api.SearchStarApi;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.star.data.IDataSource;
import com.gala.video.app.epg.ui.star.data.IDataSource.IStarAlbumCallback;
import com.gala.video.app.epg.ui.star.model.StarTaskParams;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import java.util.List;
import java.util.Map;

public class StarsDataSource implements IDataSource {
    private static StarsDataSource INSTANCE = null;
    private static final String TAG = "EPG/StarsDataSource";
    private SearchStarApi mStarApi;

    public static StarsDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StarsDataSource();
        }
        return INSTANCE;
    }

    public void initApi(AlbumInfoModel infoModel) {
        this.mStarApi = new SearchStarApi(infoModel);
    }

    public void getDetails(final IStarDetailCallback callback) {
        LogUtils.m1568d(TAG, "getDetails() -> start");
        if (this.mStarApi != null) {
            this.mStarApi.loadStarDetailData(new IStarDetailCallback() {
                public void onSuccess(Star star, String arg1) {
                    callback.onSuccess(star, arg1);
                }

                public void onFail(ApiException e) {
                    callback.onFail(e);
                }
            });
            LogUtils.m1568d(TAG, "getDetails() -> end");
        }
    }

    public void getTasks(final IStarAlbumCallback callback) {
        if (this.mStarApi == null) {
            callback.onFail(null);
            return;
        }
        final StarTaskParams params = new StarTaskParams();
        params.start = System.currentTimeMillis();
        this.mStarApi.loadStarAlbumData(new SearchStarApi.IStarAlbumCallback() {
            public void onSuccess(Map<String, List<IData>> map, List<Tag> list) {
                params.end = System.currentTimeMillis();
                LogUtils.m1568d("loadStarAlbumData", "onSuccess --- " + ListUtils.getCount((List) list));
                callback.onSuccess(map, list, params);
            }

            public void onFail(ApiException e) {
                LogUtils.m1571e("loadStarAlbumData", "onFail --- " + e);
                callback.onFail(e);
            }
        });
    }
}
