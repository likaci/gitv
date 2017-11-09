package com.gala.video.lib.share.uikit.protocol;

import android.view.View;

public interface ViewControler<DT, V extends View> {
    void bindView(DT dt, V v);

    void hideView(DT dt, V v);

    void showView(DT dt, V v);

    void unbindView(DT dt, V v);
}
