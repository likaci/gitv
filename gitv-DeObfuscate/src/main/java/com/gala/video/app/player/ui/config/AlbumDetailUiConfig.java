package com.gala.video.app.player.ui.config;

import android.view.View;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;

public class AlbumDetailUiConfig implements IAlbumDetailUiConfig {
    public IEpisodeListUIStyle getEpisodeUiConfig() {
        return PlayerAppConfig.getUIStyle().getEpisodeListUIStyle();
    }

    public boolean isZoomEnabled() {
        return Project.getInstance().getControl().isOpenAnimation();
    }

    public boolean isCommentEnabled() {
        return false;
    }

    public void setDetailTittle(View rootview, String name) {
    }

    public int getAlbumDetailLayoutId() {
        return PlayerAppConfig.getAlbumDetailLayoutId();
    }

    public int getCommentWidthNormalResId() {
        return C1291R.dimen.album_detail_button_width;
    }

    public int getCommentWidthSelResId() {
        return C1291R.dimen.dimen_223dp;
    }

    public boolean isEnableWindowPlay() {
        return Project.getInstance().getBuild().isSupportSmallWindowPlay() && PlayerDebugUtils.isAlbumDetailPageShowPlay() && Project.getInstance().getBuild().isSupportAlbumDetailWindowPlay();
    }
}
