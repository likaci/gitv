package com.gala.sdk.player;

public interface IBufferOverlay {

    public interface OnBufferlistener {
        void updateBufferProgress(int i);

        void updateNetSpeed(long j);
    }

    void hideBuffering();

    void setBufferPercent(int i);

    void setNetSpeed(long j);

    void showBuffering();
}
