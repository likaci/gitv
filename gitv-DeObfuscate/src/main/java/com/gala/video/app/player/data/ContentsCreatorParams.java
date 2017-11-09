package com.gala.video.app.player.data;

import android.view.View;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.config.style.common.IGalleryUIStyle;

public class ContentsCreatorParams {
    private final String TAG = ("ContentsCreatorParams@" + Integer.toHexString(hashCode()));
    private IAlbumDetailUiConfig mDetailUIConfig;
    private IEpisodeListUIStyle mEpisodeUIStyle;
    private IGalleryUIStyle mGalleryUIStyle;
    private IPlayerMenuPanelUIStyle mMenuPanelUIStyle;
    private IVideo mVideo;
    private View mViewRoot;

    public ContentsCreatorParams setVideo(IVideo video) {
        this.mVideo = video;
        return this;
    }

    public ContentsCreatorParams setEpisodeUIStyle(IEpisodeListUIStyle style) {
        this.mEpisodeUIStyle = style;
        return this;
    }

    public ContentsCreatorParams setGalleryUIStyle(IGalleryUIStyle style) {
        this.mGalleryUIStyle = style;
        return this;
    }

    public ContentsCreatorParams setMenuPanelUIStyle(IPlayerMenuPanelUIStyle style) {
        this.mMenuPanelUIStyle = style;
        return this;
    }

    public IVideo getVideo() {
        return this.mVideo;
    }

    public IEpisodeListUIStyle getEpisodeUIStyle() {
        return this.mEpisodeUIStyle;
    }

    public IGalleryUIStyle getGalleryUIStyle() {
        return this.mGalleryUIStyle;
    }

    public IPlayerMenuPanelUIStyle getmMenuPanelUIStyle() {
        return this.mMenuPanelUIStyle;
    }
}
