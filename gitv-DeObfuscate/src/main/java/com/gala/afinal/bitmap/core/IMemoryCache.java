package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.p000private.C0125a;

public interface IMemoryCache {
    void evictAll();

    C0125a get(String str);

    void put(String str, C0125a c0125a);

    void remove(String str);
}
