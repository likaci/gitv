package com.gala.afinal.bitmap.core;

import com.gala.afinal.utils.Utils;
import com.gala.imageprovider.p000private.C0125a;

public class BaseMemoryCacheImpl implements IMemoryCache {
    private final LruInBitmapMemoryCache<String, C0125a> f8a;

    class C00311 extends LruInBitmapMemoryCache<String, C0125a> {
        C00311(int x0) {
            super(x0);
        }

        protected final /* synthetic */ int mo311a(Object obj) {
            return Utils.getBitmapSize(((C0125a) obj).m284a());
        }
    }

    public BaseMemoryCacheImpl(int size) {
        this.f8a = new C00311(size);
    }

    public void put(String key, C0125a bitmap) {
        this.f8a.put(key, bitmap);
    }

    public C0125a get(String key) {
        return (C0125a) this.f8a.get(key);
    }

    public void evictAll() {
        this.f8a.evictAll();
    }

    public void remove(String key) {
        this.f8a.remove(key);
    }
}
