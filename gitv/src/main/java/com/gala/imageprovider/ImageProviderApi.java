package com.gala.imageprovider;

import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.private.E;

public class ImageProviderApi {
    public static IImageProvider getImageProvider() {
        return E.a();
    }
}
