package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.p000private.C0123G;
import com.gala.imageprovider.p000private.C0125a;
import java.util.LinkedHashMap;
import java.util.Map;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class LruBitmapPool {
    private int f47a;
    private final LinkedHashMap<String, C0125a> f48a;
    private int f49b;
    private int f50c;
    private int f51d;
    private int f52e;
    private int f53f;

    public LruBitmapPool(int maxSize) {
        C0123G.m279a("ImageProvider/afinal/LruBitmapPool", ">>>>>【afinal】， LruBitmapPool size is " + maxSize);
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.f49b = maxSize;
        this.f48a = new LinkedHashMap(0, 0.75f, true);
    }

    public final C0125a get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this.f48a) {
            C0125a c0125a = (C0125a) this.f48a.get(key);
            if (c0125a != null) {
                this.f52e++;
                return c0125a;
            }
            this.f53f++;
            return null;
        }
    }

    public final C0125a put(String key, C0125a value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        C0125a c0125a;
        synchronized (this.f48a) {
            this.f50c++;
            this.f47a += m20a(key, value);
            c0125a = (C0125a) this.f48a.put(key, value);
            if (c0125a != null) {
                this.f47a -= m20a(key, c0125a);
            }
        }
        m21a(this.f49b);
        return c0125a;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void m21a(int r5) {
        /*
        r4 = this;
    L_0x0000:
        r2 = r4.f48a;
        monitor-enter(r2);
        r0 = r4.f47a;	 Catch:{ all -> 0x0035 }
        if (r0 < 0) goto L_0x0013;
    L_0x0007:
        r0 = r4.f48a;	 Catch:{ all -> 0x0035 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0035 }
        if (r0 == 0) goto L_0x0038;
    L_0x000f:
        r0 = r4.f47a;	 Catch:{ all -> 0x0035 }
        if (r0 == 0) goto L_0x0038;
    L_0x0013:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0035 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0035 }
        r1.<init>();	 Catch:{ all -> 0x0035 }
        r3 = r4.getClass();	 Catch:{ all -> 0x0035 }
        r3 = r3.getName();	 Catch:{ all -> 0x0035 }
        r1 = r1.append(r3);	 Catch:{ all -> 0x0035 }
        r3 = ".sizeOf() is reporting inconsistent results!";
        r1 = r1.append(r3);	 Catch:{ all -> 0x0035 }
        r1 = r1.toString();	 Catch:{ all -> 0x0035 }
        r0.<init>(r1);	 Catch:{ all -> 0x0035 }
        throw r0;	 Catch:{ all -> 0x0035 }
    L_0x0035:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
    L_0x0038:
        r0 = r4.f47a;	 Catch:{ all -> 0x0035 }
        if (r0 <= r5) goto L_0x0044;
    L_0x003c:
        r0 = r4.f48a;	 Catch:{ all -> 0x0035 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0035 }
        if (r0 == 0) goto L_0x0046;
    L_0x0044:
        monitor-exit(r2);	 Catch:{ all -> 0x0035 }
        return;
    L_0x0046:
        r0 = r4.f48a;	 Catch:{ all -> 0x0035 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0035 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0035 }
        r0 = r0.next();	 Catch:{ all -> 0x0035 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0035 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0035 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0035 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0035 }
        r0 = (com.gala.imageprovider.p000private.C0125a) r0;	 Catch:{ all -> 0x0035 }
        r3 = r4.f48a;	 Catch:{ all -> 0x0035 }
        r3.remove(r1);	 Catch:{ all -> 0x0035 }
        r3 = r4.f47a;	 Catch:{ all -> 0x0035 }
        r0 = r4.m20a(r1, r0);	 Catch:{ all -> 0x0035 }
        r0 = r3 - r0;
        r4.f47a = r0;	 Catch:{ all -> 0x0035 }
        r0 = r4.f51d;	 Catch:{ all -> 0x0035 }
        r0 = r0 + 1;
        r4.f51d = r0;	 Catch:{ all -> 0x0035 }
        monitor-exit(r2);	 Catch:{ all -> 0x0035 }
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.afinal.bitmap.core.LruBitmapPool.a(int):void");
    }

    public final C0125a remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        C0125a c0125a;
        synchronized (this.f48a) {
            c0125a = (C0125a) this.f48a.remove(key);
            if (c0125a != null) {
                this.f47a -= m20a(key, c0125a);
            }
        }
        return c0125a;
    }

    private int m20a(String str, C0125a c0125a) {
        int a = mo316a(c0125a);
        if (a >= 0) {
            return a;
        }
        throw new IllegalStateException("Negative size: " + str + SearchCriteria.EQ + c0125a);
    }

    protected int mo316a(C0125a c0125a) {
        return 1;
    }

    public final void evictAll() {
        m21a(-1);
    }

    public final synchronized int size() {
        return this.f47a;
    }

    public final synchronized int maxSize() {
        return this.f49b;
    }

    public final synchronized int hitCount() {
        return this.f52e;
    }

    public final synchronized int missCount() {
        return this.f53f;
    }

    public final synchronized int createCount() {
        return 0;
    }

    public final synchronized int putCount() {
        return this.f50c;
    }

    public final synchronized int evictionCount() {
        return this.f51d;
    }

    public final synchronized Map<String, C0125a> snapshot() {
        return new LinkedHashMap(this.f48a);
    }

    public final synchronized String toString() {
        String format;
        int i = 0;
        synchronized (this) {
            int i2 = this.f52e + this.f53f;
            if (i2 != 0) {
                i = (this.f52e * 100) / i2;
            }
            format = String.format("LruMemoryCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.f49b), Integer.valueOf(this.f52e), Integer.valueOf(this.f53f), Integer.valueOf(i)});
        }
        return format;
    }

    public final LinkedHashMap<String, C0125a> getPool() {
        return this.f48a;
    }
}
