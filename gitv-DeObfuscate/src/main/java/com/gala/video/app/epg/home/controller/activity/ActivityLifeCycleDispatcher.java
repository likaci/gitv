package com.gala.video.app.epg.home.controller.activity;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ActivityLifeCycleDispatcher {
    private Set<IActivityLifeCycle> mDispatcher = new CopyOnWriteArraySet();

    private static class InstanceHolder {
        private static final ActivityLifeCycleDispatcher INSTANCE = new ActivityLifeCycleDispatcher();

        private InstanceHolder() {
        }
    }

    public static ActivityLifeCycleDispatcher get() {
        return InstanceHolder.INSTANCE;
    }

    public synchronized void register(IActivityLifeCycle dispatcher) {
        this.mDispatcher.add(dispatcher);
    }

    public synchronized void unregister(IActivityLifeCycle dispatcher) {
        this.mDispatcher.remove(dispatcher);
    }

    public void onStart() {
        if (this.mDispatcher != null && this.mDispatcher.size() != 0) {
            for (IActivityLifeCycle dispatcher : this.mDispatcher) {
                dispatcher.onActivityStart();
            }
        }
    }

    public void onResume() {
        if (this.mDispatcher != null && this.mDispatcher.size() != 0) {
            for (IActivityLifeCycle dispatcher : this.mDispatcher) {
                dispatcher.onActivityResume();
            }
        }
    }

    public void onPause() {
        if (this.mDispatcher != null && this.mDispatcher.size() != 0) {
            for (IActivityLifeCycle dispatcher : this.mDispatcher) {
                dispatcher.onActivityPause();
            }
        }
    }

    public void onStop() {
        if (this.mDispatcher != null && this.mDispatcher.size() != 0) {
            for (IActivityLifeCycle dispatcher : this.mDispatcher) {
                dispatcher.onActivityStop();
            }
        }
    }

    public void onDestroy() {
        if (this.mDispatcher != null && this.mDispatcher.size() != 0) {
            for (IActivityLifeCycle dispatcher : this.mDispatcher) {
                dispatcher.onActivityDestroy();
            }
        }
    }
}
