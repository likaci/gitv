package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.private.a;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class SoftMemoryCacheImpl implements IMemoryCache {
    private final HashMap<String, SoftReference<a>> a = new HashMap();

    public SoftMemoryCacheImpl(int i) {
    }

    public void put(String key, a bitmap) {
        this.a.put(key, new SoftReference(bitmap));
    }

    public a get(String key) {
        SoftReference softReference = (SoftReference) this.a.get(key);
        if (softReference != null) {
            return (a) softReference.get();
        }
        return null;
    }

    public void evictAll() {
        this.a.clear();
    }

    public void remove(String key) {
        this.a.remove(key);
    }
}
