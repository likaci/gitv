package com.gala.video.lib.share.uikit.protocol;

public interface ServiceManager {
    <T> T getService(Class<T> cls);

    <T> void register(Class<T> cls, T t);
}
