package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.a;
import com.gala.albumprovider.private.b;
import com.gala.albumprovider.private.d;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ChnList;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.ArrayList;
import java.util.List;

public class AlbumMultiChannelSet extends h {
    private int a = 0;
    private QLayoutKind f74a = QLayoutKind.MIXING;
    private Tag f75a;
    private final String f76a = "AlbumProvider";
    private List<Tag> f77a = new ArrayList();
    private int b = 0;
    private String f78b;
    private int c = 0;
    private String f79c;
    private String d;
    private String e;
    private String f;

    private class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        private int a;
        private IAlbumCallback f85a;
        final /* synthetic */ AlbumMultiChannelSet f86a;
        private int b;

        ApiCallback(AlbumMultiChannelSet albumMultiChannelSet, int pageIndex, int pageSize, IAlbumCallback albumCallback) {
            this.f86a = albumMultiChannelSet;
            this.a = pageIndex;
            this.b = pageSize;
            this.f85a = albumCallback;
        }

        public void onSuccess(ApiResultAlbumList albumList) {
            if (albumList != null) {
                String b;
                this.f86a.a = albumList.total;
                if (albumList.docs == null || albumList.docs.equals("")) {
                    this.f86a.b = albumList.total;
                } else {
                    this.f86a.b = Integer.valueOf(albumList.docs).intValue();
                }
                this.f86a.a = SetTool.trimAlbumSetCount(this.a, this.b, albumList.getAlbumList(), this.f86a.a);
                this.f86a.b = SetTool.trimAlbumSetCount(this.a, this.b, albumList.getAlbumList(), this.f86a.b);
                if (albumList.chnList != null) {
                    b = (this.f86a.a.equals("0") || this.f86a.a.equals("")) ? this.f86a.b : this.f86a.a;
                    this.f86a.a.add(new Tag("0", LibString.DefaultTagName, SourceTool.VIRTUAL_CHANNEL_TAG, SetTool.setLayoutKind(b)));
                    for (ChnList chnList : albumList.chnList) {
                        this.f86a.a.add(new Tag(String.valueOf(chnList.chnId), chnList.chnName, SourceTool.VIRTUAL_CHANNEL_TAG, SetTool.setLayoutKind(String.valueOf(chnList.chnId))));
                    }
                }
                if (albumList.data != null) {
                    b = this.f86a.a();
                    if (d.a().a(b) && this.a == 1) {
                        b a = d.a().a(b, true);
                        a aVar = null;
                        if (a != null) {
                            if (this.f86a.e.equals("0") || this.f86a.e.equals("")) {
                                USALog.d((Object) "Add Cache All Tag Data");
                                aVar = a.a();
                            } else if (this.f86a.e.contains("11;sort") && a != null && a.b() != null && a.b().a()) {
                                USALog.d((Object) "Add Cache hotest Data");
                                aVar = a.b();
                            } else if (this.f86a.e.contains("4;sort") && a != null && a.c() != null && a.c().a()) {
                                USALog.d((Object) "Add Cache newest Data");
                                aVar = a.c();
                            }
                            if (aVar != null) {
                                aVar.a(albumList.getAlbumList());
                                aVar.a(this.f86a.a);
                                aVar.b(this.f86a.b);
                            }
                        }
                    }
                    this.f85a.onSuccess(this.a, albumList.getAlbumList());
                    return;
                }
                this.f85a.onFailure(this.a, new ApiException("data is null!"));
                return;
            }
            this.f85a.onFailure(this.a, new ApiException("data is null!"));
        }

        public void onException(ApiException ex) {
            if ("E000012".equals(ex.getCode())) {
                this.f85a.onSuccess(this.a, new ArrayList());
            } else {
                this.f85a.onFailure(this.a, ex);
            }
        }
    }

    public AlbumMultiChannelSet(String channelId, String channelFromId, String channelName, Tag tag, int channelType) {
        this.d = channelName;
        if (tag != null) {
            if (tag.getID().equals("0")) {
                this.e = "";
            } else {
                this.e = tag.getID();
            }
            this.f = tag.getName();
            this.f75a = tag;
        }
        this.f78b = channelId;
        this.f79c = channelFromId;
        this.f74a = SetTool.setLayoutKind(channelId);
        this.c = channelType;
    }

    public AlbumMultiChannelSet(String channelId, String channelName, Tag tag) {
        this.f78b = channelId;
        this.d = channelName;
        if (tag != null) {
            this.e = tag.getID();
            this.f = tag.getName();
            this.f75a = tag;
        }
        this.f74a = SetTool.setLayoutKind(channelId);
    }

    public String getTagId() {
        return this.e;
    }

    public String getTagName() {
        return this.f;
    }

    public int getAlbumCount() {
        return this.a;
    }

    public int getSearchCount() {
        return this.b;
    }

    public QLayoutKind getLayoutKind() {
        switch (this.c) {
            case 1:
                if (!this.f78b.equals("0") && !this.f78b.equals("")) {
                    return SetTool.setLayoutKind(this.f78b);
                }
                if (this.f79c.equals("0") || this.f79c.equals("")) {
                    return QLayoutKind.PORTRAIT;
                }
                return SetTool.setLayoutKind(this.f79c);
            case 2:
                return this.f75a.getLayout();
            default:
                return this.f74a;
        }
    }

    public String getChannelName() {
        return this.d;
    }

    public void loadDataAsync(final int pageIndex, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumMultiChannelSet f81a;

            public void run() {
                this.f81a.a(pageIndex, pageSize, albumCallback);
            }
        });
    }

    private void a(final int i, int i2, final IAlbumCallback iAlbumCallback) {
        if (iAlbumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        USALog.d("channelId: " + this.f78b + " channelFromId: " + this.f79c);
        if (AlbumProviderApi.getAlbumProvider().getProperty().isDebug()) {
            if (this.f.equals(LibString.NewestTagName)) {
                iAlbumCallback.onFailure(i, new ApiException("", "", "-50", "http://data2.itv.ptqy.gitv.tv/itv/albumList/608/1//%3Bzone%2C%3Bmust%2C%3Bmust%2C%3Bperiod%2C11%3Bsort/1/120"));
                return;
            } else if (this.f.equals(LibString.HotTagName)) {
                iAlbumCallback.onFailure(i, new ApiException("", "E000054", ErrorEvent.HTTP_CODE_SUCCESS, "http://data2.itv.ptqy.gitv.tv/itv/albumList/608/1//%3Bzone%2C%3Bmust%2C%3Bmust%2C%3Bperiod%2C11%3Bsort/1/120"));
                return;
            }
        }
        String a = a();
        if (d.a().a(a) && i == 1) {
            b a2 = d.a().a(a, false);
            if (a2 != null) {
                a aVar = null;
                if (this.e.equals("0") || this.e.equals("")) {
                    USALog.d((Object) "All tag-channel");
                    aVar = a2.a();
                } else if (this.f.equals(LibString.HotTagName)) {
                    USALog.d((Object) "hotest tag-channel");
                    aVar = a2.b();
                } else if (this.f.equals(LibString.NewestTagName)) {
                    USALog.d((Object) "newest tag-channel");
                    aVar = a2.c();
                }
                if (aVar != null) {
                    final List a3 = aVar.a();
                    this.a = aVar.a();
                    this.b = aVar.b();
                    if (a3 != null && a3.size() > 0) {
                        AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                            final /* synthetic */ AlbumMultiChannelSet f83a;

                            public void run() {
                                iAlbumCallback.onSuccess(i, a3);
                            }
                        });
                        return;
                    }
                }
            }
        }
        switch (this.c) {
            case 2:
                TVApi.albumList.call(new ApiCallback(this, i, i2, iAlbumCallback), this.f75a.channelId, this.f79c, this.e, String.valueOf(i), String.valueOf(i2));
                return;
            default:
                TVApi.albumList.call(new ApiCallback(this, i, i2, iAlbumCallback), this.f78b, this.f79c, this.e, String.valueOf(i), String.valueOf(i2));
                return;
        }
    }

    private String a() {
        String str = this.f78b;
        if (this.f79c == null || this.f79c.equals("")) {
            return str;
        }
        return this.f79c;
    }

    public List<Tag> getTagList() {
        return this.f77a;
    }
}
