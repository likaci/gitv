package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IFavouritesAlbumSet;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCollectList;
import com.gala.video.api.ApiException;
import java.util.List;

public class AlbumFavoritesSet extends C0045h implements IFavouritesAlbumSet {
    private int f78a = 0;
    private QLayoutKind f79a = QLayoutKind.PORTRAIT;

    private class ApiCallbackNew implements IVrsCallback<ApiResultCollectList> {
        private int f75a;
        private IAlbumCallback f76a;
        final /* synthetic */ AlbumFavoritesSet f77a;

        ApiCallbackNew(AlbumFavoritesSet albumFavoritesSet, int pageNo, IAlbumCallback albumCallback) {
            this.f77a = albumFavoritesSet;
            this.f76a = albumCallback;
            this.f75a = pageNo;
        }

        public void onSuccess(ApiResultCollectList albumList) {
            this.f77a.f78a = 0;
            if (!(albumList == null || albumList.getCollectListData() == null)) {
                if (albumList.getCollectListData().qidan_cnt != 0) {
                    this.f77a.f78a = albumList.getCollectListData().qidan_cnt;
                }
                if (albumList.getCollectListData().cnt != 0) {
                    this.f77a.f78a = albumList.getCollectListData().cnt;
                }
                List albumList2 = albumList.getAlbumList();
                if (albumList2 != null) {
                    this.f76a.onSuccess(this.f75a, albumList2);
                    return;
                }
            }
            this.f76a.onFailure(this.f75a, new ApiException("data is null!"));
        }

        public void onException(ApiException ex) {
            this.f76a.onFailure(0, ex);
        }
    }

    public AlbumFavoritesSet(boolean isLocal) {
    }

    public int getAlbumCount() {
        return this.f78a;
    }

    public QLayoutKind getLayoutKind() {
        return this.f79a;
    }

    public void loadDataNewAsync(String cookie, int pageNo, int pageSize, IAlbumCallback albumCallback) {
        if (albumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        final int i = pageNo;
        final IAlbumCallback iAlbumCallback = albumCallback;
        final String str = cookie;
        final int i2 = pageSize;
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumFavoritesSet f67a;

            public void run() {
                IVrsCallback apiCallbackNew = new ApiCallbackNew(this.f67a, i, iAlbumCallback);
                UserHelper.collectList.call(apiCallbackNew, str, String.valueOf(i), String.valueOf(i2));
            }
        });
    }

    public void loadNoLoginDataNewAsync(String ckuid, int pageNo, int pageSize, IAlbumCallback albumCallback) {
        if (albumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        final int i = pageNo;
        final IAlbumCallback iAlbumCallback = albumCallback;
        final String str = ckuid;
        final int i2 = pageSize;
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumFavoritesSet f72a;

            public void run() {
                IVrsCallback apiCallbackNew = new ApiCallbackNew(this.f72a, i, iAlbumCallback);
                UserHelper.collectListForAnonymity.call(apiCallbackNew, str, String.valueOf(i), String.valueOf(i2));
            }
        });
    }

    public int getSearchCount() {
        return this.f78a;
    }
}
