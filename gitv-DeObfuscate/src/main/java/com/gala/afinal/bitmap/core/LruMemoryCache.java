package com.gala.afinal.bitmap.core;

import java.util.LinkedHashMap;
import java.util.Map;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class LruMemoryCache<K, V> {
    private int f0a;
    private final LinkedHashMap<K, V> f1a;
    private int f2b;
    private int f3c;
    private int f4d;
    private int f5e;
    private int f6f;

    public LruMemoryCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.f2b = maxSize;
        this.f1a = new LinkedHashMap(0, 0.75f, true);
    }

    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this) {
            V v = this.f1a.get(key);
            if (v != null) {
                this.f5e++;
                return v;
            }
            this.f6f++;
            return null;
        }
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V put;
        synchronized (this) {
            this.f3c++;
            this.f0a += m0a(key, value);
            put = this.f1a.put(key, value);
            if (put != null) {
                this.f0a -= m0a(key, put);
            }
        }
        if (put != null) {
            mo310a(false, key, put);
        }
        m1a(this.f2b);
        return put;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m1a(int r5) {
        /*
        r4 = this;
    L_0x0000:
        monitor-enter(r4);
        r0 = r4.f0a;	 Catch:{ all -> 0x0033 }
        if (r0 < 0) goto L_0x0011;
    L_0x0005:
        r0 = r4.f1a;	 Catch:{ all -> 0x0033 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0036;
    L_0x000d:
        r0 = r4.f0a;	 Catch:{ all -> 0x0033 }
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
        r0 = r4.f0a;	 Catch:{ all -> 0x0033 }
        if (r0 <= r5) goto L_0x0042;
    L_0x003a:
        r0 = r4.f1a;	 Catch:{ all -> 0x0033 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0033 }
        if (r0 == 0) goto L_0x0044;
    L_0x0042:
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        return;
    L_0x0044:
        r0 = r4.f1a;	 Catch:{ all -> 0x0033 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0033 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0033 }
        r0 = r0.next();	 Catch:{ all -> 0x0033 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0033 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0033 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0033 }
        r2 = r4.f1a;	 Catch:{ all -> 0x0033 }
        r2.remove(r1);	 Catch:{ all -> 0x0033 }
        r2 = r4.f0a;	 Catch:{ all -> 0x0033 }
        r3 = r4.m0a(r1, r0);	 Catch:{ all -> 0x0033 }
        r2 = r2 - r3;
        r4.f0a = r2;	 Catch:{ all -> 0x0033 }
        r2 = r4.f4d;	 Catch:{ all -> 0x0033 }
        r2 = r2 + 1;
        r4.f4d = r2;	 Catch:{ all -> 0x0033 }
        monitor-exit(r4);	 Catch:{ all -> 0x0033 }
        r2 = 1;
        r4.mo310a(r2, r1, r0);
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
            remove = this.f1a.remove(key);
            if (remove != null) {
                this.f0a -= m0a(key, remove);
            }
        }
        if (remove != null) {
            mo310a(false, key, remove);
        }
        return remove;
    }

    protected void mo310a(boolean z, K k, V v) {
    }

    private int m0a(K k, V v) {
        int a = mo311a((Object) v);
        if (a >= 0) {
            return a;
        }
        throw new IllegalStateException("Negative size: " + k + SearchCriteria.EQ + v);
    }

    protected int mo311a(V v) {
        return 1;
    }

    public final void evictAll() {
        m1a(-1);
    }

    public final synchronized int size() {
        return this.f0a;
    }

    public final synchronized int maxSize() {
        return this.f2b;
    }

    public final synchronized int hitCount() {
        return this.f5e;
    }

    public final synchronized int missCount() {
        return this.f6f;
    }

    public final synchronized int createCount() {
        return 0;
    }

    public final synchronized int putCount() {
        return this.f3c;
    }

    public final synchronized int evictionCount() {
        return this.f4d;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.f1a);
    }

    public final synchronized String toString() {
        String format;
        int i = 0;
        synchronized (this) {
            int i2 = this.f5e + this.f6f;
            if (i2 != 0) {
                i = (this.f5e * 100) / i2;
            }
            format = String.format("LruMemoryCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.f2b), Integer.valueOf(this.f5e), Integer.valueOf(this.f6f), Integer.valueOf(i)});
        }
        return format;
    }
}
