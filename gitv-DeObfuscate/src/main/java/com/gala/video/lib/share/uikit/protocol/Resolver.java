package com.gala.video.lib.share.uikit.protocol;

public interface Resolver<T, O> {
    public static final int UNKNOWN = -1;

    O create(int i);

    T get(int i);

    void register(int i, T t);

    int size();

    int type(T t);
}
