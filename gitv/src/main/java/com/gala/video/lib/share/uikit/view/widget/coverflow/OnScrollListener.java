package com.gala.video.lib.share.uikit.view.widget.coverflow;

import android.view.View;

public interface OnScrollListener {
    void onChildVisibilityChange(View view, int i, int i2, int i3);

    void onLayoutChanged(View view, int i, int i2);

    void onScrollStateChanged(boolean z);
}
