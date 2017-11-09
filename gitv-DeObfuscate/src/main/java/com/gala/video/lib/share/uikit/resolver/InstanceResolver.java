package com.gala.video.lib.share.uikit.resolver;

public abstract class InstanceResolver<T> extends BaseResolver<T, T> {
    public T create(int type) {
        return this.mSparseArray.get(type);
    }
}
