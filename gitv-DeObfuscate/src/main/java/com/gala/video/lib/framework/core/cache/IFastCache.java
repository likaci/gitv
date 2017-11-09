package com.gala.video.lib.framework.core.cache;

public interface IFastCache<T> {
    void delete(String str);

    T get();

    byte[] get(String str);

    T save(String str);

    void save(String str, String str2);
}
