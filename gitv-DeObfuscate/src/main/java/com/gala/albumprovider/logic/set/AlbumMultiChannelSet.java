package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.p001private.C0061a;
import com.gala.albumprovider.p001private.C0062b;
import com.gala.albumprovider.p001private.C0064d;
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

public class AlbumMultiChannelSet extends C0045h {
    private int f92a = 0;
    private QLayoutKind f93a = QLayoutKind.MIXING;
    private Tag f94a;
    private final String f95a = "AlbumProvider";
    private List<Tag> f96a = new ArrayList();
    private int f97b = 0;
    private String f98b;
    private int f99c = 0;
    private String f100c;
    private String f101d;
    private String f102e;
    private String f103f;

    private class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        private int f88a;
        private IAlbumCallback f89a;
        final /* synthetic */ AlbumMultiChannelSet f90a;
        private int f91b;

        ApiCallback(AlbumMultiChannelSet albumMultiChannelSet, int pageIndex, int pageSize, IAlbumCallback albumCallback) {
            this.f90a = albumMultiChannelSet;
            this.f88a = pageIndex;
            this.f91b = pageSize;
            this.f89a = albumCallback;
        }

        public void onSuccess(ApiResultAlbumList albumList) {
            if (albumList != null) {
                String b;
                this.f90a.f92a = albumList.total;
                if (albumList.docs == null || albumList.docs.equals("")) {
                    this.f90a.f97b = albumList.total;
                } else {
                    this.f90a.f97b = Integer.valueOf(albumList.docs).intValue();
                }
                this.f90a.f92a = SetTool.trimAlbumSetCount(this.f88a, this.f91b, albumList.getAlbumList(), this.f90a.f92a);
                this.f90a.f97b = SetTool.trimAlbumSetCount(this.f88a, this.f91b, albumList.getAlbumList(), this.f90a.f97b);
                if (albumList.chnList != null) {
                    b = (this.f90a.f92a.equals("0") || this.f90a.f92a.equals("")) ? this.f90a.f97b : this.f90a.f92a;
                    this.f90a.f92a.add(new Tag("0", LibString.DefaultTagName, SourceTool.VIRTUAL_CHANNEL_TAG, SetTool.setLayoutKind(b)));
                    for (ChnList chnList : albumList.chnList) {
                        this.f90a.f92a.add(new Tag(String.valueOf(chnList.chnId), chnList.chnName, SourceTool.VIRTUAL_CHANNEL_TAG, SetTool.setLayoutKind(String.valueOf(chnList.chnId))));
                    }
                }
                if (albumList.data != null) {
                    b = this.f90a.m28a();
                    if (C0064d.m122a().m126a(b) && this.f88a == 1) {
                        C0062b a = C0064d.m122a().m127a(b, true);
                        C0061a c0061a = null;
                        if (a != null) {
                            if (this.f90a.f102e.equals("0") || this.f90a.f102e.equals("")) {
                                USALog.m147d((Object) "Add Cache All Tag Data");
                                c0061a = a.m101a();
                            } else if (this.f90a.f102e.contains("11;sort") && a != null && a.m100b() != null && a.m100b().m93a()) {
                                USALog.m147d((Object) "Add Cache hotest Data");
                                c0061a = a.m100b();
                            } else if (this.f90a.f102e.contains("4;sort") && a != null && a.m113c() != null && a.m113c().m93a()) {
                                USALog.m147d((Object) "Add Cache newest Data");
                                c0061a = a.m113c();
                            }
                            if (c0061a != null) {
                                c0061a.m96a(albumList.getAlbumList());
                                c0061a.m95a(this.f90a.f92a);
                                c0061a.m99b(this.f90a.f97b);
                            }
                        }
                    }
                    this.f89a.onSuccess(this.f88a, albumList.getAlbumList());
                    return;
                }
                this.f89a.onFailure(this.f88a, new ApiException("data is null!"));
                return;
            }
            this.f89a.onFailure(this.f88a, new ApiException("data is null!"));
        }

        public void onException(ApiException ex) {
            if ("E000012".equals(ex.getCode())) {
                this.f89a.onSuccess(this.f88a, new ArrayList());
            } else {
                this.f89a.onFailure(this.f88a, ex);
            }
        }
    }

    public AlbumMultiChannelSet(String channelId, String channelFromId, String channelName, Tag tag, int channelType) {
        this.f101d = channelName;
        if (tag != null) {
            if (tag.getID().equals("0")) {
                this.f102e = "";
            } else {
                this.f102e = tag.getID();
            }
            this.f103f = tag.getName();
            this.f94a = tag;
        }
        this.f98b = channelId;
        this.f100c = channelFromId;
        this.f93a = SetTool.setLayoutKind(channelId);
        this.f99c = channelType;
    }

    public AlbumMultiChannelSet(String channelId, String channelName, Tag tag) {
        this.f98b = channelId;
        this.f101d = channelName;
        if (tag != null) {
            this.f102e = tag.getID();
            this.f103f = tag.getName();
            this.f94a = tag;
        }
        this.f93a = SetTool.setLayoutKind(channelId);
    }

    public String getTagId() {
        return this.f102e;
    }

    public String getTagName() {
        return this.f103f;
    }

    public int getAlbumCount() {
        return this.f92a;
    }

    public int getSearchCount() {
        return this.f97b;
    }

    public QLayoutKind getLayoutKind() {
        switch (this.f99c) {
            case 1:
                if (!this.f98b.equals("0") && !this.f98b.equals("")) {
                    return SetTool.setLayoutKind(this.f98b);
                }
                if (this.f100c.equals("0") || this.f100c.equals("")) {
                    return QLayoutKind.PORTRAIT;
                }
                return SetTool.setLayoutKind(this.f100c);
            case 2:
                return this.f94a.getLayout();
            default:
                return this.f93a;
        }
    }

    public String getChannelName() {
        return this.f101d;
    }

    public void loadDataAsync(final int pageIndex, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumMultiChannelSet f82a;

            public void run() {
                this.f82a.m31a(pageIndex, pageSize, albumCallback);
            }
        });
    }

    private void m31a(final int i, int i2, final IAlbumCallback iAlbumCallback) {
        if (iAlbumCallback == null) {
            throw new NullPointerException("A callback is needed for AlbumProvider");
        }
        USALog.m147d("channelId: " + this.f98b + " channelFromId: " + this.f100c);
        if (AlbumProviderApi.getAlbumProvider().getProperty().isDebug()) {
            if (this.f103f.equals(LibString.NewestTagName)) {
                iAlbumCallback.onFailure(i, new ApiException("", "", "-50", "http://data2.itv.ptqy.gitv.tv/itv/albumList/608/1//%3Bzone%2C%3Bmust%2C%3Bmust%2C%3Bperiod%2C11%3Bsort/1/120"));
                return;
            } else if (this.f103f.equals(LibString.HotTagName)) {
                iAlbumCallback.onFailure(i, new ApiException("", "E000054", ErrorEvent.HTTP_CODE_SUCCESS, "http://data2.itv.ptqy.gitv.tv/itv/albumList/608/1//%3Bzone%2C%3Bmust%2C%3Bmust%2C%3Bperiod%2C11%3Bsort/1/120"));
                return;
            }
        }
        String a = m28a();
        if (C0064d.m122a().m126a(a) && i == 1) {
            C0062b a2 = C0064d.m122a().m127a(a, false);
            if (a2 != null) {
                C0061a c0061a = null;
                if (this.f102e.equals("0") || this.f102e.equals("")) {
                    USALog.m147d((Object) "All tag-channel");
                    c0061a = a2.m101a();
                } else if (this.f103f.equals(LibString.HotTagName)) {
                    USALog.m147d((Object) "hotest tag-channel");
                    c0061a = a2.m100b();
                } else if (this.f103f.equals(LibString.NewestTagName)) {
                    USALog.m147d((Object) "newest tag-channel");
                    c0061a = a2.m113c();
                }
                if (c0061a != null) {
                    final List a3 = c0061a.m93a();
                    this.f92a = c0061a.m93a();
                    this.f97b = c0061a.m98b();
                    if (a3 != null && a3.size() > 0) {
                        AlbumProviderApi.getAlbumProvider().getProperty().getExecutorService().execute(new Runnable(this) {
                            final /* synthetic */ AlbumMultiChannelSet f86a;

                            public void run() {
                                iAlbumCallback.onSuccess(i, a3);
                            }
                        });
                        return;
                    }
                }
            }
        }
        switch (this.f99c) {
            case 2:
                TVApi.albumList.call(new ApiCallback(this, i, i2, iAlbumCallback), this.f94a.channelId, this.f100c, this.f102e, String.valueOf(i), String.valueOf(i2));
                return;
            default:
                TVApi.albumList.call(new ApiCallback(this, i, i2, iAlbumCallback), this.f98b, this.f100c, this.f102e, String.valueOf(i), String.valueOf(i2));
                return;
        }
    }

    private String m28a() {
        String str = this.f98b;
        if (this.f100c == null || this.f100c.equals("")) {
            return str;
        }
        return this.f100c;
    }

    public List<Tag> getTagList() {
        return this.f96a;
    }
}
