package com.gala.video.crosswalkinterface;

import android.view.View;

public interface IXWalkView {
    public static final int CROSSWALK_PLUGIN = 1;

    void addJavascriptInterface(Object obj, String str);

    boolean canGoBack();

    View getXWalkView();

    void goBack();

    void load(String str, String str2);

    void onDestroy();

    void onHide();

    void onShow();

    void pauseTimers();

    void removeAllViews();

    void resumeTimers();

    void setBackgroundColor(int i);

    void setFocusable(boolean z);

    void setResourceClient(IXWalkResourceClient iXWalkResourceClient);

    void setUIClient(IXWalkUIClient iXWalkUIClient);

    void setVisibility(int i);

    void setZOrderOnTop(boolean z);
}
