package com.gala.video.app.player.ui.overlay.contents;

public interface IContentsCreatorFactory {
    IContentsCreator getDetailContentCreator();

    IContentsCreator getMenuPanelContentCreator();

    IContentsCreator getSelectionContentCreator();
}
