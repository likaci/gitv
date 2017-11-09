package com.qiyi.tv.client.feature.playstate;

public interface PlayStateManager {
    boolean isRunning();

    void setOnPlayStateChangedListener(OnPlayStateChangedListener onPlayStateChangedListener);

    void start();

    void stop();
}
