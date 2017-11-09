package com.gala.video.app.epg.web.subject.api;

import com.gala.tvapi.tv2.model.Album;

class WebAlbumInfo {
    private Album mAlbum;

    public WebAlbumInfo(Album album) {
        this.mAlbum = album;
    }

    public Album getAlbum() {
        return this.mAlbum;
    }

    public String getAlbumId() {
        return this.mAlbum != null ? this.mAlbum.live_channelId : "";
    }

    public String getLiveChannelId() {
        return this.mAlbum != null ? this.mAlbum.live_channelId : "";
    }

    public String getTVQid() {
        return this.mAlbum != null ? this.mAlbum.tvQid : "";
    }

    public boolean isPurchase() {
        return this.mAlbum == null ? false : this.mAlbum.isPurchase();
    }
}
