package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.ISubscribeSet;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultSubScribeList;
import com.gala.video.api.ApiException;
import java.util.ArrayList;
import java.util.List;

public class AlbumSubscribeSet extends C0045h implements ISubscribeSet {
    private int f129a = 0;
    private List<Album> f130a = new ArrayList();
    private int f131b = 0;
    private int f132c = 0;

    private class ApiCallback implements IVrsCallback<ApiResultSubScribeList> {
        private IAlbumCallback f126a;
        final /* synthetic */ AlbumSubscribeSet f127a;
        private String f128a;

        ApiCallback(AlbumSubscribeSet albumSubscribeSet, IAlbumCallback albumCallback, String type) {
            this.f127a = albumSubscribeSet;
            this.f126a = albumCallback;
            this.f128a = type;
        }

        public void onException(ApiException e) {
            USALog.m150e("onException -- type  = " + this.f128a);
            m48a(this.f128a, 1);
            if ((this.f127a.f131b == 1 && this.f127a.f132c != 1) || (this.f127a.f132c == 1 && this.f127a.f131b != 1)) {
                this.f126a.onFailure(0, e);
            }
        }

        public void onSuccess(ApiResultSubScribeList list) {
            USALog.m147d("onSuccess -- type = " + this.f128a);
            if (list != null) {
                List albumList = list.getAlbumList();
                if (albumList == null) {
                    m48a(this.f128a, 2);
                } else if (albumList.size() > 0) {
                    m49a(albumList, this.f128a);
                    m48a(this.f128a, 3);
                    if (this.f127a.f131b.size() <= 0) {
                        this.f127a.f131b.addAll(albumList);
                    } else if (this.f128a.equals("1")) {
                        this.f127a.f131b.addAll(0, albumList);
                    } else {
                        this.f127a.f131b.addAll(albumList);
                    }
                } else {
                    m48a(this.f128a, 2);
                }
            } else {
                m48a(this.f128a, 2);
            }
            if (this.f127a.f131b > 1 && this.f127a.f132c > 1) {
                this.f126a.onSuccess(0, this.f127a.f131b);
            }
        }

        private void m49a(List<Album> list, String str) {
            boolean equals = "1".equals(str);
            for (Album album : list) {
                album.end = equals;
            }
        }

        private void m48a(String str, int i) {
            if (str.equals("1")) {
                this.f127a.f131b = i;
            }
            if (str.equals("0")) {
                this.f127a.f132c = i;
            }
        }
    }

    public int getAlbumCount() {
        return this.f129a;
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
        return this.f129a;
    }

    public void loadDataNewAsync(String cookie, IAlbumCallback albumCallback) {
        this.f132c = 0;
        this.f131b = 0;
        IVrsCallback apiCallback = new ApiCallback(this, albumCallback, "1");
        UserHelper.subscribeList.call(apiCallback, cookie, "1", "50", "1", "latestUpdate");
        apiCallback = new ApiCallback(this, albumCallback, "0");
        UserHelper.subscribeList.call(apiCallback, cookie, "1", "50", "0", "subscribeTime");
    }
}
