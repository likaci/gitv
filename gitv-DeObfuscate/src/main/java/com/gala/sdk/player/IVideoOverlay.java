package com.gala.sdk.player;

import android.view.ViewGroup;

public interface IVideoOverlay {
    void changeParent(ViewGroup viewGroup);

    VideoSurfaceView getVideoSurfaceView();
}
