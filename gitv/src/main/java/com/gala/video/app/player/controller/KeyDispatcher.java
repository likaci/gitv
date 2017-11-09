package com.gala.video.app.player.controller;

import android.view.KeyEvent;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class KeyDispatcher {
    private static final String TAG = "Detail/Controller/KeyDispatcher";
    private static KeyDispatcher sInstance;
    private List<IKeyEventListener> mListeners = new CopyOnWriteArrayList();

    private KeyDispatcher() {
    }

    public static synchronized KeyDispatcher instance() {
        KeyDispatcher keyDispatcher;
        synchronized (KeyDispatcher.class) {
            if (sInstance == null) {
                sInstance = new KeyDispatcher();
            }
            keyDispatcher = sInstance;
        }
        return keyDispatcher;
    }

    public void register(IKeyEventListener listener) {
        if (listener != null) {
            this.mListeners.add(listener);
        }
        LogRecordUtils.logd(TAG, "<< register, mListeners=" + this.mListeners);
    }

    public void unregister(IKeyEventListener listener) {
        if (listener != null) {
            this.mListeners.remove(listener);
        }
    }

    public void clear() {
        this.mListeners.clear();
    }

    public static synchronized void release() {
        synchronized (KeyDispatcher.class) {
            sInstance = null;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        for (IKeyEventListener each : this.mListeners) {
            if (each.dispatchKeyEvent(event)) {
                return true;
            }
        }
        return false;
    }
}
