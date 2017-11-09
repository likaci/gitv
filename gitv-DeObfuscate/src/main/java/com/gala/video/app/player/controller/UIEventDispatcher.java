package com.gala.video.app.player.controller;

import android.content.Context;
import java.lang.ref.WeakReference;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class UIEventDispatcher {
    private static final String TAG = "Detail/Controller/UIEventDispatcher";
    private static UIEventDispatcher sInstance;
    WeakHashMap<Context, WeakReference<IUIEventListener>> SListenersMap = new WeakHashMap();

    private UIEventDispatcher() {
    }

    public static synchronized UIEventDispatcher instance() {
        UIEventDispatcher uIEventDispatcher;
        synchronized (UIEventDispatcher.class) {
            if (sInstance == null) {
                sInstance = new UIEventDispatcher();
            }
            uIEventDispatcher = sInstance;
        }
        return uIEventDispatcher;
    }

    public void register(Context context, IUIEventListener listener) {
        if (listener != null) {
            this.SListenersMap.put(context, new WeakReference(listener));
        }
    }

    public void unregister(Context context) {
        if (context != null) {
            this.SListenersMap.remove(context);
        }
    }

    public void clear() {
        this.SListenersMap.clear();
    }

    public void post(Context context, int eventType, Object data) {
        for (Entry<Context, WeakReference<IUIEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                IUIEventListener callback = (IUIEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onEvent(eventType, data);
                    return;
                }
                return;
            }
        }
    }

    public static synchronized void release() {
        synchronized (UIEventDispatcher.class) {
            sInstance = null;
        }
    }
}
