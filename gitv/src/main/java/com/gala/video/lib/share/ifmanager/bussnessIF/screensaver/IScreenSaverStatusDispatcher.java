package com.gala.video.lib.share.ifmanager.bussnessIF.screensaver;

public interface IScreenSaverStatusDispatcher {

    public interface IStatusListener {
        void onStart();

        void onStop();
    }

    void register(IStatusListener iStatusListener);

    void unRegister(IStatusListener iStatusListener);
}
