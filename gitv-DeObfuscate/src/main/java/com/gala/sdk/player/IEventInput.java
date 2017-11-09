package com.gala.sdk.player;

import android.view.KeyEvent;
import android.view.View;

public interface IEventInput {

    public interface OnUserSeekListener {
        void onProgressChanged(View view, int i);

        void onSeekBegin(View view, int i);

        void onSeekEnd(View view, int i);
    }

    boolean dispatchKeyEvent(KeyEvent keyEvent);

    int getProgress();

    void setMaxProgress(int i, int i2);

    void setOnUserPlayPauseListener(OnUserPlayPauseListener onUserPlayPauseListener);

    void setOnUserSeekListener(OnUserSeekListener onUserSeekListener);

    void setProgress(int i);

    void setSeekEnabled(boolean z);

    void setSourceType(SourceType sourceType);
}
