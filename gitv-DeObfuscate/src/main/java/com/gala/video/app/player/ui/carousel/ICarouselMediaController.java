package com.gala.video.app.player.ui.carousel;

import android.view.KeyEvent;
import com.gala.video.app.player.ui.overlay.TipWrapper;

public interface ICarouselMediaController {
    void clearMediaControllerState();

    boolean dispatchKeyEvent(KeyEvent keyEvent);

    void hide();

    void hideBrightnessPanel();

    void hideBuffering();

    void hidePlayOverFlow(boolean z, long j);

    void hideTip();

    void hideVolumePanel();

    boolean isShown();

    void setBufferPercent(int i);

    void setNetSpeed(long j);

    void showAdPlaying(int i);

    void showBrightnessPanel(int i);

    void showBuffering();

    void showPlayOverFlow(boolean z, long j);

    void showPlaying(boolean z);

    void showTip(TipWrapper tipWrapper);

    void showVolumePanel(int i);
}
