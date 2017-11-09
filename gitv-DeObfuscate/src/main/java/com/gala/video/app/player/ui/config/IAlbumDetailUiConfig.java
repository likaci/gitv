package com.gala.video.app.player.ui.config;

import android.view.View;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;

public interface IAlbumDetailUiConfig {
    int getAlbumDetailLayoutId();

    int getCommentWidthNormalResId();

    int getCommentWidthSelResId();

    IEpisodeListUIStyle getEpisodeUiConfig();

    boolean isCommentEnabled();

    boolean isEnableWindowPlay();

    boolean isZoomEnabled();

    void setDetailTittle(View view, String str);
}
