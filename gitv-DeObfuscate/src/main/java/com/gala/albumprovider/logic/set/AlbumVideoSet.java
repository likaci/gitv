package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultEpisodeList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import java.util.ArrayList;
import java.util.List;

public class AlbumVideoSet extends C0045h {
    private int f140a = 0;
    private Tag f141a;
    private String f142a;
    private String f143b;
    private final String f144c = "AlbumProvider";
    private String f145d;

    private class ApiCallback implements IApiCallback<ApiResultEpisodeList> {
        private int f137a;
        private IAlbumCallback f138a;
        final /* synthetic */ AlbumVideoSet f139a;

        public ApiCallback(AlbumVideoSet albumVideoSet, int pageNo, IAlbumCallback albumCallback) {
            this.f139a = albumVideoSet;
            this.f137a = pageNo;
            this.f138a = albumCallback;
        }

        public void onException(ApiException ex) {
            if ("E000012".equals(ex.getCode())) {
                this.f138a.onSuccess(this.f137a, new ArrayList());
            } else {
                this.f138a.onFailure(this.f137a, ex);
            }
        }

        public void onSuccess(ApiResultEpisodeList albumList) {
            this.f139a.f140a = albumList.total;
            List<Album> albumList2 = albumList.getAlbumList();
            if (albumList2 == null || albumList2.size() <= 0) {
                this.f138a.onFailure(this.f137a, new ApiException("albumList is null"));
                return;
            }
            USALog.m147d("channelId: " + this.f139a.f142a + " list size : " + albumList2.size());
            for (Album album : albumList2) {
                try {
                    album.sourceCode = this.f139a.f141a.getSource();
                    if (album.isSourceType()) {
                        album.isSeries = 1;
                    }
                    album.qpId = this.f139a.f141a;
                    album.chnId = Integer.valueOf(this.f139a.f142a).intValue();
                } catch (Exception e) {
                }
            }
            this.f138a.onSuccess(this.f137a, albumList2);
        }
    }

    public AlbumVideoSet(Tag tag) {
        if (tag != null) {
            this.f143b = tag.getID();
            this.f141a = tag;
            this.f145d = tag.getName();
        }
        this.f142a = tag.channelId_forLive;
    }

    public String getTagId() {
        return this.f143b;
    }

    public String getTagName() {
        return this.f145d;
    }

    public int getAlbumCount() {
        return this.f140a;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f141a != null) {
            return this.f141a.getLayout();
        }
        return SetTool.setLayoutKind(this.f142a);
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumVideoSet f135a;

            public void run() {
                TVApi.episodeList.call(new ApiCallback(this.f135a, pageNo, albumCallback), this.f135a.f141a, this.f135a.f141a.getSource(), "0", String.valueOf(pageNo), String.valueOf(pageSize));
            }
        });
    }
}
