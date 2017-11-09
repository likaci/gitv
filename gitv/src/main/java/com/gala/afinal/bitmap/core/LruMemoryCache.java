package com.gala.afinal.bitmap.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class LruMemoryCache<K, V> {
    private int a;
    private final LinkedHashMap<K, V> f31a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.b = maxSize;
        this.f31a = new LinkedHashMap(0, 0.75f, true);
    }

    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v = this.f31a.get(key);
            if (v != null) {
                this.e++;
                return v;
            }
            this.f++;
            return null;
        }
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V put;
        synchronized (this) {
            this.c++;
            this.a += a(key, value);
            put = this.f31a.put(key, value);
            if (put != null) {
                this.a -= a(key, put);
            }
        }
        if (put != null) {
            a(false, key, put);
        }
        a(this.b);
        return put;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(int r5) {
        /*
        r4 = this;
    L_0x0000:
        monitor-enter(r4);
        r0 = r4.a;	 Catch:{ all -> 0x0033 }
        if (r0 < 0) goto L_0x0011;
    L_0x0005:
        r0 = r4.f31a;	 Catch:{ all -> 0x0033 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0036;
    L_0x000d:
        r0 = r4.a;	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0036;
    L_0x0011:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0033 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0033 }
        r1.<init>();	 Catch:{ all -> 0x0033 }
        r2 = r4.getClass();	 Catch:{ all -> 0x0033 }
        r2 = r2.getName();	 Catch:{ all -> 0x0033 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0033 }
        r2 = ".sizeOf() is reporting inconsistent results!";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0033 }
        r1 = r1.toString();	 Catch:{ all -> 0x0033 }
        r0.<init>(r1);	 Catch:{ all -> 0x0033 }
        throw r0;	 Catch:{ all -> 0x0033 }
    L_0x0033:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
    L_0x0036:
        r0 = r4.a;	 Catch:{ all -> 0x0033 }
        if (r0 <= r5) goto L_0x0042;
    L_0x003a:
        r0 = r4.f31a;	 Catch:{ all -> 0x0033 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0044;
    L_0x0042:
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0044:
        r0 = r4.f31a;	 Catch:{ all -> 0x0033 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0033 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0033 }
        r0 = r0.next();	 Catch:{ all -> 0x0033 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0033 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0033 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0033 }
        r2 = r4.f31a;	 Catch:{ all -> 0x0033 }
        r2.remove(r1);	 Catch:{ all -> 0x0033 }
        r2 = r4.a;	 Catch:{ all -> 0x0033 }
        r3 = r4.a(r1, r0);	 Catch:{ all -> 0x0033 }
        r2 = r2 - r3;
        r4.a = r2;	 Catch:{ all -> 0x0033 }
        r2 = r4.d;	 Catch:{ all -> 0x0033 }
        r2 = r2 + 1;
        r4.d = r2;	 Catch:{ all -> 0x0033 }
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        r2 = 1;
        r4.a(r2, r1, r0);
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.afinal.bitmap.core.LruMemoryCache.a(int):void");
    }

    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        V remove;
        synchronized (this) {
            remove = this.f31a.remove(key);
            if (remove != null) {
                this.a -= a(key, remove);
            }
        }
        if (remove != null) {
            a(false, key, remove);
        }
        return remove;
    }

    protected void a(boolean z, K k, V v) {
    }

    private int a(K k, V v) {
        int a = a((Object) v);
        if (a >= 0) {
            return a;
        }
        throw new IllegalStateException("Negative size: " + k + SearchCriteria.EQ + v);
    }

    protected int a(V v) {
        return 1;
    }

    public final void evictAll() {
        a(-1);
    }

    public final synchronized int size() {
        return this.a;
    }

    public final synchronized int maxSize() {
        return this.b;
    }

    public final synchronized int hitCount() {
        return this.e;
    }

    public final synchronized int missCount() {
        return this.f;
    }

    public final synchronized int createCount() {
        return 0;
    }

    public final synchronized int putCount() {
        return this.c;
    }

    public final synchronized int evictionCount() {
        return this.d;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.f31a);
    }

    public final synchronized String toString() {
        String format;
        int i = 0;
        synchronized (this) {
            int i2 = this.e + this.f;
            if (i2 != 0) {
                i = (this.e * 100) / i2;
            }
            format = String.format("LruMemoryCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.b), Integer.valueOf(this.e), Integer.valueOf(this.f), Integer.valueOf(i)});
        }
        return format;
    }
}
