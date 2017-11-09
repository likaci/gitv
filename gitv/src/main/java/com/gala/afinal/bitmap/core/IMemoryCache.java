package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.private.a;

public interface IMemoryCache {
    void evictAll();

    a get(String str);

    void put(String str, a aVar);

    void remove(String str);
}
