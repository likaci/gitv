package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IFavouritesAlbumSet;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCollectList;
import com.gala.video.api.ApiException;
import java.util.List;

public class AlbumFavoritesSet extends h implements IFavouritesAlbumSet {
    private int a = 0;
    private QLayoutKind f60a = QLayoutKind.PORTRAIT;

    private class ApiCallbackNew implements IVrsCallback<ApiResultCollectList> {
        private int a;
        private IAlbumCallback f67a;
        final /* synthetic */ AlbumFavoritesSet f68a;

        ApiCallbackNew(AlbumFavoritesSet albumFavoritesSet, int pageNo, IAlbumCallback albumCallback) {
            this.f68a = albumFavoritesSet;
            this.f67a = albumCallback;
            this.a = pageNo;
        }

        public void onSuccess(ApiResultCollectList albumList) {
            this.f68a.a = 0;
            if (!(albumList == null || albumList.getCollectListData() == null)) {
                if (albumList.getCollectListData().qidan_cnt != 0) {
                    this.f68a.a = albumList.getCollectListData().qidan_cnt;
                }
                if (albumList.getCollectListData().cnt != 0) {
                    this.f68a.a = albumList.getCollectListData().cnt;
                }
                List albumList2 = albumList.getAlbumList();
                if (albumList2 != null) {
                    this.f67a.onSuccess(this.a, albumList2);
                    return;
                }
            }
            this.f67a.onFailure(this.a, new ApiException("data is null!"));
        }

        public void onException(ApiException ex) {
            this.f67a.onFailure(0, ex);
        }
    }

    public AlbumFavoritesSet(boolean isLocal) {
    }

    public int getAlbumCount() {
        return this.a;
    }

    public QLayoutKind getLayoutKind() {
        return this.f60a;
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
            final /* synthetic */ AlbumFavoritesSet f62a;

            public void run() {
                IVrsCallback apiCallbackNew = new ApiCallbackNew(this.f62a, i, iAlbumCallback);
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
            final /* synthetic */ AlbumFavoritesSet f65a;

            public void run() {
                IVrsCallback apiCallbackNew = new ApiCallbackNew(this.f65a, i, iAlbumCallback);
                UserHelper.collectListForAnonymity.call(apiCallbackNew, str, String.valueOf(i), String.valueOf(i2));
            }
        });
    }

    public int getSearchCount() {
        return this.a;
    }
}
