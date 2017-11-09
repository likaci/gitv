package com.gala.video.app.player.ui.overlay.contents;

public class ContentsCreatorFactory implements IContentsCreatorFactory {
    private static IContentsCreatorFactory sContentsCreatorFactory;

    private ContentsCreatorFactory() {
    }

    public static synchronized IContentsCreatorFactory instance() {
        IContentsCreatorFactory iContentsCreatorFactory;
        synchronized (ContentsCreatorFactory.class) {
            if (sContentsCreatorFactory == null) {
                sContentsCreatorFactory = new ContentsCreatorFactory();
            }
            iContentsCreatorFactory = sContentsCreatorFactory;
        }
        return iContentsCreatorFactory;
    }

    public IContentsCreator getSelectionContentCreator() {
        return null;
    }

    public IContentsCreator getMenuPanelContentCreator() {
        return new MenuPanelContentsCreator();
    }

    public IContentsCreator getDetailContentCreator() {
        return null;
    }
}
