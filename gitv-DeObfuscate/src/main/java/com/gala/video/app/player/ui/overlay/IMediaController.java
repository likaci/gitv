package com.gala.video.app.player.ui.overlay;

import android.view.KeyEvent;
import com.gala.sdk.player.ISeekOverlay;
import com.gala.sdk.player.IThreeDimensional;
import com.gala.sdk.player.data.IVideo;

public interface IMediaController extends IThreeDimensional, ISeekOverlay {
    void clearMediaControllerState();

    boolean dispatchKeyEvent(KeyEvent keyEvent);

    void hide();

    void hideBottomAndTop(int i);

    void hideBrightnessPanel();

    void hideBuffering();

    void hideGuideTip();

    void hidePlayOverFlow(boolean z, long j);

    void hideTip();

    void hideVolumePanel();

    boolean isShown();

    void setBufferPercent(int i);

    void setNetSpeed(long j);

    void setPrimary(boolean z);

    void setSubtitle(String str);

    void setVideo(IVideo iVideo);

    void showAdPlaying(int i);

    void showBottomAndTop(int i);

    void showBrightnessPanel(int i);

    void showBuffering();

    void showMiddleAdEnd();

    void showPanel(int i);

    void showPaused();

    void showPlayOverFlow(boolean z, long j);

    void showPlaying(boolean z);

    boolean showSeekBar();

    void showTip(TipWrapper tipWrapper);

    void showVolumePanel(int i);

    void updateBitStreamDefinition(String str);
}
