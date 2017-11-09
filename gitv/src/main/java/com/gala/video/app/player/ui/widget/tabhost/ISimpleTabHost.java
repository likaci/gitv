package com.gala.video.app.player.ui.widget.tabhost;

import android.view.View;

public interface ISimpleTabHost {

    public interface OnTabChangedListener {
        void onTabChanged(int i);
    }

    public interface OnTabFocusChangedListener {
        void onTabFocusChanged(View view, boolean z);
    }

    void setAdapter(SimpleTabHostAdapter simpleTabHostAdapter);

    void setOnTabChangedListener(OnTabChangedListener onTabChangedListener);

    void setOnTabFocusChangedListener(OnTabFocusChangedListener onTabFocusChangedListener);
}
