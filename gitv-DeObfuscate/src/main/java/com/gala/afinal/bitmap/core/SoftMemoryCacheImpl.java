package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.p000private.C0125a;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class SoftMemoryCacheImpl implements IMemoryCache {
    private final HashMap<String, SoftReference<C0125a>> f56a = new HashMap();

    public SoftMemoryCacheImpl(int i) {
    }

    public void put(String key, C0125a bitmap) {
        this.f56a.put(key, new SoftReference(bitmap));
    }

    public C0125a get(String key) {
        SoftReference softReference = (SoftReference) this.f56a.get(key);
        if (softReference != null) {
            return (C0125a) softReference.get();
        }
        return null;
    }

    public void evictAll() {
        this.f56a.clear();
    }

    public void remove(String key) {
        this.f56a.remove(key);
    }
}
