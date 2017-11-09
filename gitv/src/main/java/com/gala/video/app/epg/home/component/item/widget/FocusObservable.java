package com.gala.video.app.epg.home.component.item.widget;

import android.view.View.OnFocusChangeListener;

public interface FocusObservable {
    void registerFocusChangeListener(OnFocusChangeListener onFocusChangeListener);

    void unregisterFocusChangeListener(OnFocusChangeListener onFocusChangeListener);
}
