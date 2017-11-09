package com.gala.video.app.player.ui.config;

import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.config.style.common.EpisodeListUIStyle;
import com.gala.video.app.player.ui.config.style.common.EpisodeListUIStyleForMenuPanel;
import com.gala.video.app.player.ui.config.style.common.ExitDialogPortGalleryUIStyle;
import com.gala.video.app.player.ui.config.style.common.IGalleryUIStyle;
import com.gala.video.app.player.ui.config.style.common.MenuLandGalleryUIStyle;
import com.gala.video.app.player.ui.config.style.common.PlayerMenuPanelUIStyle;

public class CommonUIStyle {
    protected IEpisodeListUIStyle mEpisodeListUIStyle = new EpisodeListUIStyle();
    protected IEpisodeListUIStyle mEpisodeListUIStyleForMenuPanel = new EpisodeListUIStyleForMenuPanel();
    protected IGalleryUIStyle mExitDialogPortGalleryUIStyle = new ExitDialogPortGalleryUIStyle();
    protected IGalleryUIStyle mMenuLandGalleryUIStyle = new MenuLandGalleryUIStyle();
    protected IPlayerMenuPanelUIStyle mPlayerMenuPanelUIStyle = new PlayerMenuPanelUIStyle();

    public IEpisodeListUIStyle getEpisodeListUIStyle() {
        return this.mEpisodeListUIStyle;
    }

    public IEpisodeListUIStyle getEpisodeListUIStyleForMenuPanel() {
        return this.mEpisodeListUIStyleForMenuPanel;
    }

    public IPlayerMenuPanelUIStyle getPlayerMenuPanelUIStyle() {
        return this.mPlayerMenuPanelUIStyle;
    }

    public IGalleryUIStyle getMenuLandGalleryUIStyle() {
        return this.mMenuLandGalleryUIStyle;
    }

    public IGalleryUIStyle getExitDialogPortGalleryUIStyle() {
        return this.mExitDialogPortGalleryUIStyle;
    }
}
