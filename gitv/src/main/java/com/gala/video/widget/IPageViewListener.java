package com.gala.video.widget;

import android.view.View;
import android.widget.AdapterView;

public interface IPageViewListener {
    void onItemClick(AdapterView<?> adapterView, View view, int i, long j);

    void onItemSelected(AdapterView<?> adapterView, View view, int i, long j);
}
