package com.gala.video.lib.share.common.model.player;

import com.gala.sdk.player.PlayParams;
import com.gala.tvapi.tv2.model.Album;

public class AlbumDetailPlayParamBuilder extends AbsPlayParamBuilder {
    public Album mAlbumInfo = null;
    public boolean mClearTaskFlag = false;
    public boolean mContinueNextVideo = true;
    public boolean mIsComplete = true;
    public PlayParams mParam;

    public AlbumDetailPlayParamBuilder setAlbumInfo(Album albumInfo) {
        this.mAlbumInfo = albumInfo;
        return this;
    }

    public AlbumDetailPlayParamBuilder setPlayParam(PlayParams param) {
        this.mParam = param;
        return this;
    }

    public AlbumDetailPlayParamBuilder setIsComplete(boolean isComplete) {
        this.mIsComplete = isComplete;
        return this;
    }

    public AlbumDetailPlayParamBuilder setClearTaskFlag(boolean clearTaskFlag) {
        this.mClearTaskFlag = clearTaskFlag;
        return this;
    }

    public AlbumDetailPlayParamBuilder setContinueNextVideo(boolean continueNextVideo) {
        this.mContinueNextVideo = continueNextVideo;
        return this;
    }
}
