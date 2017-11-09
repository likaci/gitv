package com.gala.video.app.player.controller;

import android.content.Context;
import android.os.Bundle;
import java.lang.ref.WeakReference;
import java.util.Map.Entry;
import java.util.WeakHashMap;

public class ActivityEventDispatcher {
    private static final String TAG = "Detail/Controller/ActivityEventDispatcher";
    private static ActivityEventDispatcher sInstance;
    WeakHashMap<Context, WeakReference<AbsActivityEventListener>> SListenersMap = new WeakHashMap();

    private ActivityEventDispatcher() {
    }

    public static synchronized ActivityEventDispatcher instance() {
        ActivityEventDispatcher activityEventDispatcher;
        synchronized (ActivityEventDispatcher.class) {
            if (sInstance == null) {
                sInstance = new ActivityEventDispatcher();
            }
            activityEventDispatcher = sInstance;
        }
        return activityEventDispatcher;
    }

    public void register(Context context, AbsActivityEventListener listener) {
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

    public static synchronized void release() {
        synchronized (ActivityEventDispatcher.class) {
            sInstance = null;
        }
    }

    public void onCreate(Context context, Bundle bundle) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onCreated(bundle);
                    return;
                }
                return;
            }
        }
    }

    public void onStart(Context context) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onStarted();
                    return;
                }
                return;
            }
        }
    }

    public void onResume(Context context, int resultCode) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onResumed(resultCode);
                    return;
                }
                return;
            }
        }
    }

    public void onFinishing(Context context) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onFinishing();
                    return;
                }
                return;
            }
        }
    }

    public void onPause(Context context) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onPaused();
                    return;
                }
                return;
            }
        }
    }

    public void onStop(Context context) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onStopped();
                    return;
                }
                return;
            }
        }
    }

    public void onDestroy(Context context) {
        for (Entry<Context, WeakReference<AbsActivityEventListener>> entry : this.SListenersMap.entrySet()) {
            if (context == entry.getKey()) {
                AbsActivityEventListener callback = (AbsActivityEventListener) ((WeakReference) entry.getValue()).get();
                if (callback != null) {
                    callback.onDestroyed();
                    return;
                }
                return;
            }
        }
    }
}
