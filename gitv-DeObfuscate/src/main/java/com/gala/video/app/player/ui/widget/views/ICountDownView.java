package com.gala.video.app.player.ui.widget.views;

import com.gala.video.app.player.ui.IScreenUISwitcher;

public interface ICountDownView extends IScreenUISwitcher {

    public interface OnVisibilityChangeListener {
        void onHidden();

        void onShown();
    }

    void hide();

    void setOnVisibilityChangeListener(OnVisibilityChangeListener onVisibilityChangeListener);

    void setThreeDimensional(boolean z);

    void show(int i);
}
