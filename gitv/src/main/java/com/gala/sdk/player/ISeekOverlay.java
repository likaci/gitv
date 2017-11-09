package com.gala.sdk.player;

import com.gala.sdk.player.IEventInput.OnUserSeekListener;

public interface ISeekOverlay extends OnUserSeekListener {
    int getProgress();

    void setHeadAndTailProgress(int i, int i2);

    void setMaxProgress(int i, int i2);

    void setProgress(int i);

    void setSecondaryProgress(int i);
}
