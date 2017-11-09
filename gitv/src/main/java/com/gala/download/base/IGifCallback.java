package com.gala.download.base;

import pl.droidsonroids.gif.GifDrawable;

public interface IGifCallback {
    void onFailure(FileRequest fileRequest, Exception exception);

    void onSuccess(FileRequest fileRequest, GifDrawable gifDrawable);
}
