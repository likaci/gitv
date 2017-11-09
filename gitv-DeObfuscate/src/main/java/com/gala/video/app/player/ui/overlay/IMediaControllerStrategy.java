package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.view.View;

public interface IMediaControllerStrategy extends IMediaController {
    void clearAd();

    void clearHideViewMessageQueue();

    MediaControllerStateBean getMediaControllerBean();

    void handleUpdateTimeMessage();

    void initView(Context context, View view);

    void setMediaControllerBean(MediaControllerStateBean mediaControllerStateBean);

    void setOnAdStateListener(OnAdStateListener onAdStateListener);

    void switchScreen(boolean z, float f);
}
