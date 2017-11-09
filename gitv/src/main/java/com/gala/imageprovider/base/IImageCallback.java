package com.gala.imageprovider.base;

import android.graphics.Bitmap;

public interface IImageCallback {
    void onFailure(ImageRequest imageRequest, Exception exception);

    void onSuccess(ImageRequest imageRequest, Bitmap bitmap);
}
