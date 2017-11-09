package com.gala.sdk.player;

import com.gala.sdk.player.data.IVideo;

public interface OnPlayerStateChangedListener {
    void onAdEnd();

    void onAdStarted();

    boolean onError(IVideo iVideo, ISdkError iSdkError);

    void onPlaybackFinished();

    void onPrepared();

    void onScreenModeSwitched(ScreenMode screenMode);

    void onVideoStarted(IVideo iVideo);

    void onVideoSwitched(IVideo iVideo, int i);
}
