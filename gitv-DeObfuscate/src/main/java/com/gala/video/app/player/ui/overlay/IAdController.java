package com.gala.video.app.player.ui.overlay;

import android.view.KeyEvent;
import com.gala.sdk.player.ITip;
import com.gala.video.app.player.ui.IScreenUISwitcher;

public interface IAdController extends IScreenUISwitcher {
    boolean dispatchKeyEvent(KeyEvent keyEvent);

    void hideAd();

    void hideAdUnderBuffer();

    void hideCornerAd();

    void hidePauseAd();

    void hideTopLayout();

    void setMediaBean(MediaControllerStateBean mediaControllerStateBean);

    void setThreeDimensional(boolean z);

    void setTipContent(ITip iTip);

    void showPauseAd();

    void showStartAdWithTip(int i);

    void showStartAdWithoutTip(int i);

    void showTopLayout();
}
