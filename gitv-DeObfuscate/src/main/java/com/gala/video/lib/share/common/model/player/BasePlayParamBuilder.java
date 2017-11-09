package com.gala.video.lib.share.common.model.player;

import com.gala.sdk.player.PlayParams;
import com.gala.tvapi.tv2.model.Album;

public class BasePlayParamBuilder extends AbsPlayParamBuilder {
    public Album mAlbumInfo = null;
    public boolean mClearTaskFlag = false;
    public boolean mContinueNextVideo = true;
    public Album mDetailOriAlbum = null;
    public int mPlayOrder = 0;
    public PlayParams mPlayParams = null;

    public BasePlayParamBuilder setAlbumInfo(Album albumInfo) {
        this.mAlbumInfo = albumInfo;
        return this;
    }

    public BasePlayParamBuilder setPlayOrder(int playOrder) {
        this.mPlayOrder = playOrder;
        return this;
    }

    public BasePlayParamBuilder setPlayParams(PlayParams playParams) {
        this.mPlayParams = playParams;
        return this;
    }

    public BasePlayParamBuilder setClearTaskFlag(boolean clearTaskFlag) {
        this.mClearTaskFlag = clearTaskFlag;
        return this;
    }

    public BasePlayParamBuilder setContinueNextVideo(boolean continueNextVideo) {
        this.mContinueNextVideo = continueNextVideo;
        return this;
    }

    public BasePlayParamBuilder setDetailOriAlbum(Album oriAlbum) {
        this.mDetailOriAlbum = oriAlbum;
        return this;
    }
}
