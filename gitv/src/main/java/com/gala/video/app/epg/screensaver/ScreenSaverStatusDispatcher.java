package com.gala.video.app.epg.screensaver;

import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import java.util.HashSet;
import java.util.Set;

class ScreenSaverStatusDispatcher implements IScreenSaverStatusDispatcher {
    private Set<IStatusListener> mIScreenSaverStatusListeners = new HashSet();

    ScreenSaverStatusDispatcher() {
    }

    public void register(IStatusListener listener) {
        this.mIScreenSaverStatusListeners.add(listener);
    }

    public void unRegister(IStatusListener listener) {
        this.mIScreenSaverStatusListeners.remove(listener);
    }

    void start() {
        for (IStatusListener listener : this.mIScreenSaverStatusListeners) {
            listener.onStart();
        }
    }

    void stop() {
        for (IStatusListener listener : this.mIScreenSaverStatusListeners) {
            listener.onStop();
        }
    }
}
