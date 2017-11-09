package com.gala.sdk.player;

import com.gala.sdk.player.data.IVideo;

public interface IProjectEventReporter {
    void onCompleted();

    void onError();

    void onPaused();

    void onPrepared();

    void onPreparing(IVideo iVideo);

    void onStarted();

    void onStopped();

    void onStopping();
}
