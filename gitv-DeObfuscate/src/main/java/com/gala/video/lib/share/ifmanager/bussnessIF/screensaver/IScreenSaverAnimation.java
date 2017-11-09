package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver;

import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.ScreenSaverAdModel;

public interface IScreenSaverAnimation {

    public interface IAd {
        void beforeFadeIn(ScreenSaverAdModel screenSaverAdModel);
    }

    public interface IImage {
        void beforeFadeIn(boolean z);
    }

    public interface IRegisterAd {
        void register(IAd iAd);

        void unregister(IAd iAd);
    }

    public interface IRegisterImage {
        void register(IImage iImage);

        void unregister(IImage iImage);
    }
}
