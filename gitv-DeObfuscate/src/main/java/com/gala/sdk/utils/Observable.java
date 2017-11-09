package com.gala.sdk.utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class Observable<T> {
    protected final CopyOnWriteArrayList<T> mListeners = new CopyOnWriteArrayList();

    public final boolean addListener(T listener) {
        boolean z = false;
        if (!this.mListeners.contains(listener)) {
            z = this.mListeners.add(listener);
        }
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/Observable<T>", "addListener(" + listener + ") return " + z);
        }
        return z;
    }

    public final boolean removeListener(T listener) {
        boolean remove = this.mListeners.remove(listener);
        if (Log.DEBUG) {
            Log.m454d("PlayerUtils/Observable<T>", "removeListener(" + listener + ") return " + remove);
        }
        return remove;
    }

    public final void clear() {
        this.mListeners.clear();
    }
}
