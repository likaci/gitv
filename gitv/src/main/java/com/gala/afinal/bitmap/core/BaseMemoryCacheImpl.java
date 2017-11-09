package com.gala.afinal.bitmap.core;

import com.gala.afinal.utils.Utils;
import com.gala.imageprovider.private.a;

public class BaseMemoryCacheImpl implements IMemoryCache {
    private final LruInBitmapMemoryCache<String, a> a;

    class AnonymousClass1 extends LruInBitmapMemoryCache<String, a> {
        AnonymousClass1(int x0) {
            super(x0);
        }

        protected final /* synthetic */ int a(Object obj) {
            return Utils.getBitmapSize(((a) obj).a());
        }
    }

    public BaseMemoryCacheImpl(int size) {
        this.a = new AnonymousClass1(size);
    }

    public void put(String key, a bitmap) {
        this.a.put(key, bitmap);
    }

    public a get(String key) {
        return (a) this.a.get(key);
    }

    public void evictAll() {
        this.a.evictAll();
    }

    public void remove(String key) {
        this.a.remove(key);
    }
}
