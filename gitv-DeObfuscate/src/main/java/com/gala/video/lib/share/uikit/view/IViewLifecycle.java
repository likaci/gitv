package com.gala.video.lib.share.uikit.view;

public interface IViewLifecycle<T> {
    void onBind(T t);

    void onHide(T t);

    void onShow(T t);

    void onUnbind(T t);
}
