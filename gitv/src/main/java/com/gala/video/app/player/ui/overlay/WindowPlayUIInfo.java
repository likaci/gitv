package com.gala.video.app.player.ui.overlay;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class WindowPlayUIInfo {
    private LayoutParams mVideoLayoutParams;
    private ViewGroup mVideoParent;

    public WindowPlayUIInfo(ViewGroup viewGroup, LayoutParams params) {
        this.mVideoLayoutParams = params;
        this.mVideoParent = viewGroup;
    }

    public ViewGroup getVideoViewParent() {
        return this.mVideoParent;
    }

    public LayoutParams getVideoViewPlayLayoutParams() {
        return this.mVideoLayoutParams;
    }
}
