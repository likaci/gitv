package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.p000private.C0125a;

public class LruInBitmapMemoryCache<K, V> extends LruMemoryCache<K, V> {
    private LruBitmapPool f7a = LruBitmapPoolImpl.get().getPool();

    public LruInBitmapMemoryCache(int maxSize) {
        super(maxSize);
    }

    protected final void mo310a(boolean z, K k, V v) {
        if (this.f7a != null && z && (v instanceof C0125a) && (k instanceof String) && ((C0125a) v).m284a()) {
            this.f7a.put((String) k, (C0125a) v);
        }
    }
}
