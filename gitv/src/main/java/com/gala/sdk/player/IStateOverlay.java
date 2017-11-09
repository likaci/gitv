package com.gala.sdk.player;

public interface IStateOverlay {
    void showAdPlaying(int i);

    void showCompleted();

    void showLoading(String str);

    void showMiddleAdEnd();

    void showMiddleAdPlaying(int i);

    void showPaused();

    void showPlaying(boolean z);

    void showStopped();
}
