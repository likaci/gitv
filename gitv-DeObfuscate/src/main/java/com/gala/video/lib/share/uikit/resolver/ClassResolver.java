package com.gala.video.lib.share.uikit.resolver;

import com.gala.video.albumlist.utils.LOG;

public abstract class ClassResolver<T> extends BaseResolver<Class<? extends T>, T> {
    private static final String TAG = "ClassResolver";

    static final class TypeNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 1025446032091216588L;

        TypeNotFoundException(String message) {
            super(message);
        }
    }

    public T create(int type) {
        Class<? extends T> clz = (Class) this.mSparseArray.get(type);
        if (clz != null) {
            try {
                return clz.newInstance();
            } catch (Exception e) {
                LOG.m870d(TAG, e.toString());
            }
        }
        return null;
    }
}
