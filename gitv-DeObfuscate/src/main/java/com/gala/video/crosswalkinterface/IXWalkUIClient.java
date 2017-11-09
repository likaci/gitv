package com.gala.video.crosswalkinterface;

import android.view.KeyEvent;

public interface IXWalkUIClient {
    boolean onConsoleMessage(IXWalkView iXWalkView, String str, int i, String str2);

    void onFullscreenToggled(IXWalkView iXWalkView, boolean z);

    void onPageLoadStarted(IXWalkView iXWalkView, String str);

    void onPageLoadStopped(IXWalkView iXWalkView, String str, String str2);

    boolean shouldOverrideKeyEvent(IXWalkView iXWalkView, KeyEvent keyEvent);
}
