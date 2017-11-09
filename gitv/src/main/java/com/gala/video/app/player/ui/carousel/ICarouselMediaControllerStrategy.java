package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.view.View;
import com.gala.video.app.player.ui.overlay.MediaControllerStateBean;

public interface ICarouselMediaControllerStrategy extends ICarouselMediaController {
    MediaControllerStateBean getMediaControllerBean();

    void hideCarouselChannelListOverlay();

    void initView(Context context, View view);

    void setMediaControllerBean(MediaControllerStateBean mediaControllerStateBean);

    void showCarouselChannelListOverlay();

    void switchScreen(boolean z, float f);
}
