package com.gala.video.app.player.albumdetail.data;

import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.data.DetailDataProvider;

public class AlbumVideoItem {
    private AlbumInfo mAlbumInfo;
    private DetailDataProvider mDataProvider;
    private SourceType mSourceType;
    private IVideo mVideo;

    public DetailDataProvider getDataProvider() {
        return this.mDataProvider;
    }

    public void setDataProvider(DetailDataProvider dataProvider) {
        this.mDataProvider = dataProvider;
    }

    public AlbumVideoItem(AlbumInfo albumInfo, IVideo video, SourceType sourceType) {
        this.mAlbumInfo = albumInfo;
        this.mVideo = video;
        this.mSourceType = sourceType;
    }

    public IVideo getVideo() {
        return this.mVideo;
    }

    public AlbumInfo getAlbumInfo() {
        return this.mAlbumInfo;
    }

    public SourceType getSourceType() {
        return this.mSourceType;
    }
}
