package com.gala.video.app.player.albumdetail.data.loader;

import java.lang.ref.WeakReference;

public abstract class BaseWeakListener<D> {
    WeakReference<D> mOuter;

    public BaseWeakListener(D outer) {
        this.mOuter = new WeakReference(outer);
    }

    public D get() {
        return this.mOuter.get();
    }
}
