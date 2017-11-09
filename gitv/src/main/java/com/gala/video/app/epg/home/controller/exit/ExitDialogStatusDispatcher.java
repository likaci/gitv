package com.gala.video.app.epg.home.controller.exit;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ExitDialogStatusDispatcher {
    private Set<IExitDialogStatusListener> mIExitDialogStatusListeners = new CopyOnWriteArraySet();

    private static class InstanceHolder {
        private static final ExitDialogStatusDispatcher INSTANCE = new ExitDialogStatusDispatcher();

        private InstanceHolder() {
        }
    }

    public static ExitDialogStatusDispatcher get() {
        return InstanceHolder.INSTANCE;
    }

    public synchronized void register(IExitDialogStatusListener dispatcher) {
        this.mIExitDialogStatusListeners.add(dispatcher);
    }

    public synchronized void unregister(IExitDialogStatusListener dispatcher) {
        this.mIExitDialogStatusListeners.remove(dispatcher);
    }

    public void onShow() {
        if (this.mIExitDialogStatusListeners != null && this.mIExitDialogStatusListeners.size() != 0) {
            for (IExitDialogStatusListener listener : this.mIExitDialogStatusListeners) {
                listener.onExitDialogShow();
            }
        }
    }

    public void onDismiss() {
        if (this.mIExitDialogStatusListeners != null && this.mIExitDialogStatusListeners.size() != 0) {
            for (IExitDialogStatusListener listener : this.mIExitDialogStatusListeners) {
                listener.onExitDialogDismiss();
            }
        }
    }
}
