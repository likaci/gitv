package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.ISubscribeSet;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultSubScribeList;
import com.gala.video.api.ApiException;
import java.util.ArrayList;
import java.util.List;

public class AlbumSubscribeSet extends h implements ISubscribeSet {
    private int a = 0;
    private List<Album> f115a = new ArrayList();
    private int b = 0;
    private int c = 0;

    private class ApiCallback implements IVrsCallback<ApiResultSubScribeList> {
        private IAlbumCallback a;
        final /* synthetic */ AlbumSubscribeSet f116a;
        private String f117a;

        ApiCallback(AlbumSubscribeSet albumSubscribeSet, IAlbumCallback albumCallback, String type) {
            this.f116a = albumSubscribeSet;
            this.a = albumCallback;
            this.f117a = type;
        }

        public void onException(ApiException e) {
            USALog.e("onException -- type  = " + this.f117a);
            a(this.f117a, 1);
            if ((this.f116a.b == 1 && this.f116a.c != 1) || (this.f116a.c == 1 && this.f116a.b != 1)) {
                this.a.onFailure(0, e);
            }
        }

        public void onSuccess(ApiResultSubScribeList list) {
            USALog.d("onSuccess -- type = " + this.f117a);
            if (list != null) {
                List albumList = list.getAlbumList();
                if (albumList == null) {
                    a(this.f117a, 2);
                } else if (albumList.size() > 0) {
                    a(albumList, this.f117a);
                    a(this.f117a, 3);
                    if (this.f116a.b.size() <= 0) {
                        this.f116a.b.addAll(albumList);
                    } else if (this.f117a.equals("1")) {
                        this.f116a.b.addAll(0, albumList);
                    } else {
                        this.f116a.b.addAll(albumList);
                    }
                } else {
                    a(this.f117a, 2);
                }
            } else {
                a(this.f117a, 2);
            }
            if (this.f116a.b > 1 && this.f116a.c > 1) {
                this.a.onSuccess(0, this.f116a.b);
            }
        }

        private void a(List<Album> list, String str) {
            boolean equals = "1".equals(str);
            for (Album album : list) {
                album.end = equals;
            }
        }

        private void a(String str, int i) {
            if (str.equals("1")) {
                this.f116a.b = i;
            }
            if (str.equals("0")) {
                this.f116a.c = i;
            }
        }
    }

    public int getAlbumCount() {
        return this.a;
    }

    public QLayoutKind getLayoutKind() {
        return QLayoutKind.PORTRAIT;
    }

    public void loadDataAsync(int pageNo, int pageSize, IAlbumCallback albumCallback) {
        if (albumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
    }

    public int getSearchCount() {
        return this.a;
    }

    public void loadDataNewAsync(String cookie, IAlbumCallback albumCallback) {
        this.c = 0;
        this.b = 0;
        IVrsCallback apiCallback = new ApiCallback(this, albumCallback, "1");
        UserHelper.subscribeList.call(apiCallback, cookie, "1", "50", "1", "latestUpdate");
        apiCallback = new ApiCallback(this, albumCallback, "0");
        UserHelper.subscribeList.call(apiCallback, cookie, "1", "50", "0", "subscribeTime");
    }
}
