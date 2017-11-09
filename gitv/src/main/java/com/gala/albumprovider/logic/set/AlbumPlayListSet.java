package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.private.b;
import com.gala.albumprovider.private.c;
import com.gala.albumprovider.private.d;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import java.util.List;

public class AlbumPlayListSet extends h {
    private int a = 0;
    private QLayoutKind f99a = QLayoutKind.PORTRAIT;
    private String f100a = "";
    private boolean f101a = false;
    private QLayoutKind b = QLayoutKind.PORTRAIT;
    private String f102b = "";
    private boolean f103b = true;
    private String c = "";
    private boolean f104c = false;
    private String d;

    private class ApiCallback implements IVrsCallback<ApiResultPlayListQipu> {
        private int a;
        private IAlbumCallback f110a;
        final /* synthetic */ AlbumPlayListSet f111a;
        private int b;

        ApiCallback(AlbumPlayListSet albumPlayListSet, IAlbumCallback albumCallback, int pageNo, int pageSize) {
            this.f111a = albumPlayListSet;
            this.f110a = albumCallback;
            this.a = pageNo;
            this.b = pageSize;
        }

        public void onSuccess(ApiResultPlayListQipu albumList) {
            if (albumList == null || albumList.getPlayListQipu() == null) {
                this.f110a.onFailure(this.a, new ApiException("data is null!"));
            } else if (albumList.data != null) {
                if (albumList.data.imageStyle.equals("0")) {
                    this.f111a.f99a = QLayoutKind.LANDSCAPE;
                } else {
                    this.f111a.f99a = QLayoutKind.PORTRAIT;
                }
                this.f111a.c = albumList.getPlayListQipu().tvBackgroundUrl;
                if (this.a != 0) {
                    this.f111a.a = SetTool.trimAlbumSetCount(1, this.b, albumList.getAlbumList(), albumList.data.size);
                } else if (albumList.getAlbumList() != null) {
                    this.f111a.a = albumList.getAlbumList().size();
                }
                if ((this.a == 0 || this.a == 1) && d.a().a(this.f111a.a)) {
                    b a = d.a().a(this.f111a.a);
                    if (a != null && a.a(this.f111a.f100a)) {
                        a = d.a().a(this.f111a.a, true);
                        USALog.d((Object) "Add cache play list data");
                        c cVar = new c();
                        cVar.a(this.f111a.c);
                        cVar.a(this.f111a.a);
                        cVar.a(this.f111a.a);
                        cVar.a(albumList.getAlbumList());
                        a.a(this.f111a.f100a, cVar);
                    }
                }
                this.f110a.onSuccess(this.a, albumList.getAlbumList());
            }
        }

        public void onException(ApiException ex) {
            this.f110a.onFailure(1, ex);
        }
    }

    public AlbumPlayListSet(String channelId, String playListId, String name, boolean isRun, boolean isFree) {
        this.f100a = playListId;
        this.f102b = name;
        this.f101a = isRun;
        this.f103b = isFree;
        this.d = channelId;
    }

    public AlbumPlayListSet(String channelId, String playListId, String name, boolean isRun, boolean isFree, QLayoutKind layout) {
        this.f100a = playListId;
        this.f102b = name;
        this.f101a = isRun;
        this.f103b = isFree;
        this.b = layout;
        this.d = channelId;
    }

    public AlbumPlayListSet(String channelId, String playListId, String name, boolean isRun, boolean isFree, QLayoutKind layout, boolean isUpdateLayout) {
        this.f100a = playListId;
        this.f102b = name;
        this.f101a = isRun;
        this.f103b = isFree;
        this.b = layout;
        this.f104c = isUpdateLayout;
        this.d = channelId;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f104c) {
            return this.f99a;
        }
        return this.b;
    }

    public boolean isRunPlayList() {
        return this.f101a;
    }

    public String getBackgroundImage() {
        return this.c;
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IAlbumCallback callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumPlayListSet f106a;

            public void run() {
                this.f106a.a(pageNo, pageSize, callback);
            }
        });
    }

    private void a(final int i, int i2, final IAlbumCallback iAlbumCallback) {
        if (iAlbumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        if ((i == 0 || i == 1) && d.a().a(this.d)) {
            b a = d.a().a(this.d);
            if (a != null && a.a(this.f100a)) {
                c a2 = d.a().a(this.d, false).a(this.f100a);
                if (a2 != null) {
                    final List a3 = a2.a();
                    if (a3 != null && a3.size() > 0) {
                        USALog.d((Object) "Get cache play list data");
                        this.f99a = a2.a();
                        this.c = a2.a();
                        this.a = a2.a();
                        AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                            final /* synthetic */ AlbumPlayListSet f108a;

                            public void run() {
                                iAlbumCallback.onSuccess(i, a3);
                            }
                        });
                        return;
                    }
                }
            }
        }
        String str = this.f103b ? "1" : "0";
        if (i == 0) {
            VrsHelper.playListQipu.call(new ApiCallback(this, iAlbumCallback, i, Integer.MAX_VALUE), this.f100a, str);
        } else {
            VrsHelper.playListQipuPage.call(new ApiCallback(this, iAlbumCallback, i, i2), this.f100a, String.valueOf(i), String.valueOf(i2), str);
        }
    }

    public String getTagId() {
        return this.f100a;
    }

    public String getTagName() {
        return this.f102b;
    }

    public int getAlbumCount() {
        return this.a;
    }

    public String getBackground() {
        return this.c;
    }

    public int getSearchCount() {
        return this.a;
    }
}
