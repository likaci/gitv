package com.gala.video.app.player.ui.overlay;

public interface IUIHolder<T> {
    String getTag();

    T getWrappedContent();
}
