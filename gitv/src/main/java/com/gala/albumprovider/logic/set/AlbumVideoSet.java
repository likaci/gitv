package com.gala.albumprovider.logic.set;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultEpisodeList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import java.util.ArrayList;
import java.util.List;

public class AlbumVideoSet extends h {
    private int a = 0;
    private Tag f118a;
    private String f119a;
    private String b;
    private final String c = "AlbumProvider";
    private String d;

    private class ApiCallback implements IApiCallback<ApiResultEpisodeList> {
        private int a;
        private IAlbumCallback f122a;
        final /* synthetic */ AlbumVideoSet f123a;

        public ApiCallback(AlbumVideoSet albumVideoSet, int pageNo, IAlbumCallback albumCallback) {
            this.f123a = albumVideoSet;
            this.a = pageNo;
            this.f122a = albumCallback;
        }

        public void onException(ApiException ex) {
            if ("E000012".equals(ex.getCode())) {
                this.f122a.onSuccess(this.a, new ArrayList());
            } else {
                this.f122a.onFailure(this.a, ex);
            }
        }

        public void onSuccess(ApiResultEpisodeList albumList) {
            this.f123a.a = albumList.total;
            List<Album> albumList2 = albumList.getAlbumList();
            if (albumList2 == null || albumList2.size() <= 0) {
                this.f122a.onFailure(this.a, new ApiException("albumList is null"));
                return;
            }
            USALog.d("channelId: " + this.f123a.f119a + " list size : " + albumList2.size());
            for (Album album : albumList2) {
                try {
                    album.sourceCode = this.f123a.f118a.getSource();
                    if (album.isSourceType()) {
                        album.isSeries = 1;
                    }
                    album.qpId = this.f123a.f118a;
                    album.chnId = Integer.valueOf(this.f123a.f119a).intValue();
                } catch (Exception e) {
                }
            }
            this.f122a.onSuccess(this.a, albumList2);
        }
    }

    public AlbumVideoSet(Tag tag) {
        if (tag != null) {
            this.b = tag.getID();
            this.f118a = tag;
            this.d = tag.getName();
        }
        this.f119a = tag.channelId_forLive;
    }

    public String getTagId() {
        return this.b;
    }

    public String getTagName() {
        return this.d;
    }

    public int getAlbumCount() {
        return this.a;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f118a != null) {
            return this.f118a.getLayout();
        }
        return SetTool.setLayoutKind(this.f119a);
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ AlbumVideoSet f121a;

            public void run() {
                TVApi.episodeList.call(new ApiCallback(this.f121a, pageNo, albumCallback), this.f121a.f118a, this.f121a.f118a.getSource(), "0", String.valueOf(pageNo), String.valueOf(pageSize));
            }
        });
    }
}
