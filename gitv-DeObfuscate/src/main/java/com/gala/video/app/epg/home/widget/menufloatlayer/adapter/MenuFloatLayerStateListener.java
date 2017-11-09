package com.gala.video.app.epg.home.widget.menufloatlayer.adapter;

import android.view.View;
import android.view.ViewGroup;

public interface MenuFloatLayerStateListener {
    int getCount();

    int getItemId(int i);

    View getView(int i, View view, ViewGroup viewGroup);

    void onChildFocusChanged(View view, boolean z);

    void onClick(View view);
}
