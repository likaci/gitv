package com.gala.sdk.player;

import android.view.View;

public interface OnUserPlayPauseListener {
    void onPause(View view);

    void onPlay(View view);

    void onPlayPause(View view);
}
