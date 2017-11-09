package com.gala.video.app.epg.web.core;

import com.gala.video.app.epg.web.function.IPlayerListener;
import com.gala.video.app.epg.web.function.WebFunContract.IFunPlayer;

public class FunctionPlayer implements IFunPlayer {
    private IPlayerListener mPlayCallback;

    public FunctionPlayer(IPlayerListener uiCallback) {
        this.mPlayCallback = uiCallback;
    }

    public void onAlbumSelected(String albumInfo) {
        if (this.mPlayCallback != null) {
            this.mPlayCallback.onAlbumSelected(albumInfo);
        }
    }

    public void startWindowPlay(String playInfo) {
        if (this.mPlayCallback != null) {
            this.mPlayCallback.startWindowPlay(playInfo);
        }
    }

    public void switchPlay(String playInfo) {
        if (this.mPlayCallback != null) {
            this.mPlayCallback.switchPlay(playInfo);
        }
    }

    public void switchScreenMode(String mode) {
        if (this.mPlayCallback != null) {
            this.mPlayCallback.switchScreenMode(mode);
        }
    }

    public void checkLiveInfo(String albumInfo) {
        if (this.mPlayCallback != null) {
            this.mPlayCallback.checkLiveInfo(albumInfo);
        }
    }
}
