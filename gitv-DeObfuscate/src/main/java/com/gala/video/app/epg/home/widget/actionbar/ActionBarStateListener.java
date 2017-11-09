package com.gala.video.app.epg.home.widget.actionbar;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

public interface ActionBarStateListener {
    boolean dispatchKeyEvent(KeyEvent keyEvent);

    ActionBarPageType getActionBarPageType();

    int getCount();

    int getItemId(int i);

    View getView(int i, View view, ViewGroup viewGroup);

    void onChildFocusChanged(View view, boolean z);

    void onClick(View view, int i);
}
