package com.gala.video.app.epg.ui.albumlist.widget;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public interface WidgetStatusListener {
    void onItemClick(ViewGroup viewGroup, View view, int i);

    void onItemSelectChange(View view, int i, boolean z);

    void onItemTouch(View view, MotionEvent motionEvent, int i);

    void onLoseFocus(ViewGroup viewGroup, View view, int i);
}
