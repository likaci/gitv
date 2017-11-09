package com.gala.video.app.epg.ui.albumlist.data.api;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.logic.set.AlbumFavoritesSet;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import java.lang.ref.WeakReference;
import java.util.List;

public class FavouriteApi extends BaseDataApi {
    private AlbumFavoritesSet favouritesAlbumSet = ((AlbumFavoritesSet) this.mAlbumSet);
    private String mCookie = UserUtil.getCookie();
    private boolean mLogin = UserUtil.isLogin();

    private static class FavCallBack implements IAlbumCallback {
        private OnAlbumFetchedListener localDataListener;
        private WeakReference<FavouriteApi> mOuter;
        private long time1 = System.currentTimeMillis();

        public FavCallBack(FavouriteApi outer, OnAlbumFetchedListener dataListener) {
            this.mOuter = new WeakReference(outer);
            this.localDataListener = dataListener;
        }

        public void onSuccess(int arg0, List<Album> list) {
            FavouriteApi outer = (FavouriteApi) this.mOuter.get();
            if (outer != null) {
                DebugUtils.limitNetSpeed();
                outer.logAndRecord(FavouriteApi.NOLOG ? null : "FavouriteApi---success--CurPageIndex = " + outer.mCurPageIndex + "--timeToken = " + (System.currentTimeMillis() - this.time1));
                outer.mOriginalList = list;
                outer.handleOnDataSuccess(AlbumDataMakeupFactory.get().dataListMakeup(list, outer.getLayoutKind(), outer.mCurPageIndex, outer.mInfoModel), this.localDataListener);
            }
        }

        public void onFailure(int arg0, ApiException e) {
            FavouriteApi outer = (FavouriteApi) this.mOuter.get();
            if (outer != null) {
                outer.logAndRecord(FavouriteApi.NOLOG ? null : "FavouriteApi---fail--e=" + e + "--CurPageIndex = " + outer.mCurPageIndex + "--timeToken = " + (System.currentTimeMillis() - this.time1));
                outer.handleDataApiOnDataFail(e, this.localDataListener);
            }
        }
    }

    public FavouriteApi(AlbumInfoModel apiParams) {
        super(apiParams);
    }

    public void loadAlbumData(OnAlbumFetchedListener dataListener) {
        if (isNeedLoad()) {
            this.mIsLoading = true;
            this.mLoadingTag = this.mNewTag;
            logAndRecord(NOLOG ? null : "loadAlbumData---CurPageIndex = " + this.mCurPageIndex + "--mPerLoadCount" + getEachPageCount() + "--mLogin=" + this.mLogin + "--mCookie=" + this.mCookie);
            if (this.mLogin) {
                this.favouritesAlbumSet.loadDataNewAsync(this.mCookie, this.mCurPageIndex, getEachPageCount(), new FavCallBack(this, dataListener));
            } else {
                this.favouritesAlbumSet.loadNoLoginDataNewAsync(this.mCookie, this.mCurPageIndex, getEachPageCount(), new FavCallBack(this, dataListener));
            }
        }
    }

    protected IAlbumSource getAlbumSource() {
        return this.mAlbumProvider.getFavouritesAlbumSource();
    }

    public void loadLabelData(OnLabelFetchedListener fetchingLabel) {
    }

    protected int getOriginalPage() {
        return 0;
    }

    protected int getEachPageCount() {
        return 60;
    }

    public int getSelectType() {
        return 1;
    }

    public int getRecommendType() {
        return 0;
    }

    protected void resetChildrenApi() {
    }

    protected void setTotalCount() {
        this.mTotalItemCount = this.favouritesAlbumSet.getAlbumCount();
        this.mDisplayCount = this.favouritesAlbumSet.getSearchCount();
    }

    public int getCurPage() {
        return this.mCurPageIndex;
    }

    protected String getLogCatTag() {
        return "FavouriteApi";
    }
}
