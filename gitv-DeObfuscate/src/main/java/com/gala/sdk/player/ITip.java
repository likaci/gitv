package com.gala.sdk.player;

public interface ITip {
    Runnable getRunnable();

    int getShowDuration();

    TipExtra getTipExtra();

    TipType getTipType();
}
