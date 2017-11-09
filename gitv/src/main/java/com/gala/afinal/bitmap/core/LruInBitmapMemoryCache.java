package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.private.a;

public class LruInBitmapMemoryCache<K, V> extends LruMemoryCache<K, V> {
    private LruBitmapPool a = LruBitmapPoolImpl.get().getPool();

    public LruInBitmapMemoryCache(int maxSize) {
        super(maxSize);
    }

    protected final void a(boolean z, K k, V v) {
        if (this.a != null && z && (v instanceof a) && (k instanceof String) && ((a) v).a()) {
            this.a.put((String) k, (a) v);
        }
    }
}
