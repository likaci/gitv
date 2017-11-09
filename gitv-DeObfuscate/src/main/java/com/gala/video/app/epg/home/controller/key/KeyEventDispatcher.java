package com.gala.video.app.epg.home.controller.key;

import android.view.KeyEvent;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.HashSet;
import java.util.Set;

public class KeyEventDispatcher {
    private static final String TAG = "home/KeyEventDispatcher";
    private static KeyEventDispatcher sInstance = new KeyEventDispatcher();
    private Set<IKeyDispatcher> mDispatcher = new HashSet();

    private static class InstanceHolder {
        private static final KeyEventDispatcher INSTANCE = new KeyEventDispatcher();

        private InstanceHolder() {
        }
    }

    public static KeyEventDispatcher get() {
        return InstanceHolder.INSTANCE;
    }

    public synchronized void resisterKeyDispatcher(IKeyDispatcher dispatcher) {
        this.mDispatcher.add(dispatcher);
    }

    public synchronized void unResisterKeyDispatcher(IKeyDispatcher dispatcher) {
        this.mDispatcher.remove(dispatcher);
    }

    public boolean hasInDispatcher(IKeyDispatcher dispatcher) {
        return this.mDispatcher.contains(dispatcher);
    }

    public boolean onKeyDown(KeyEvent event) {
        boolean result = false;
        if (this.mDispatcher == null || this.mDispatcher.size() <= 0) {
            return false;
        }
        for (IKeyDispatcher dispatcher : this.mDispatcher) {
            result |= dispatcher.onKeyEvent(event);
            LogUtils.m1568d(TAG, "onKeyDown dispatcher result : " + result);
        }
        return result;
    }
}
