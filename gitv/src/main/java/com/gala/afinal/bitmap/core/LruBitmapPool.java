package com.gala.afinal.bitmap.core;

import com.gala.imageprovider.private.G;
import com.gala.imageprovider.private.a;
import java.util.LinkedHashMap;
import java.util.Map;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class LruBitmapPool {
    private int a;
    private final LinkedHashMap<String, a> f32a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;

    public LruBitmapPool(int maxSize) {
        G.a("ImageProvider/afinal/LruBitmapPool", ">>>>>【afinal】， LruBitmapPool size is " + maxSize);
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.b = maxSize;
        this.f32a = new LinkedHashMap(0, 0.75f, true);
    }

    public final a get(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        synchronized (this.f32a) {
            a aVar = (a) this.f32a.get(key);
            if (aVar != null) {
                this.e++;
                return aVar;
            }
            this.f++;
            return null;
        }
    }

    public final a put(String key, a value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        a aVar;
        synchronized (this.f32a) {
            this.c++;
            this.a += a(key, value);
            aVar = (a) this.f32a.put(key, value);
            if (aVar != null) {
                this.a -= a(key, aVar);
            }
        }
        a(this.b);
        return aVar;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(int r5) {
        /*
        r4 = this;
    L_0x0000:
        r2 = r4.f32a;
        monitor-enter(r2);
        r0 = r4.a;	 Catch:{ all -> 0x0035 }
        if (r0 < 0) goto L_0x0013;
    L_0x0007:
        r0 = r4.f32a;	 Catch:{ all -> 0x0035 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0035 }
        if (r0 == 0) goto L_0x0038;
    L_0x000f:
        r0 = r4.a;	 Catch:{ all -> 0x0035 }
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
        r0 = r4.a;	 Catch:{ all -> 0x0035 }
        if (r0 <= r5) goto L_0x0044;
    L_0x003c:
        r0 = r4.f32a;	 Catch:{ all -> 0x0035 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0035 }
        if (r0 == 0) goto L_0x0046;
    L_0x0044:
        monitor-exit(r2);	 Catch:{ all -> 0x0035 }
        return;
    L_0x0046:
        r0 = r4.f32a;	 Catch:{ all -> 0x0035 }
        r0 = r0.entrySet();	 Catch:{ all -> 0x0035 }
        r0 = r0.iterator();	 Catch:{ all -> 0x0035 }
        r0 = r0.next();	 Catch:{ all -> 0x0035 }
        r0 = (java.util.Map.Entry) r0;	 Catch:{ all -> 0x0035 }
        r1 = r0.getKey();	 Catch:{ all -> 0x0035 }
        r1 = (java.lang.String) r1;	 Catch:{ all -> 0x0035 }
        r0 = r0.getValue();	 Catch:{ all -> 0x0035 }
        r0 = (com.gala.imageprovider.private.a) r0;	 Catch:{ all -> 0x0035 }
        r3 = r4.f32a;	 Catch:{ all -> 0x0035 }
        r3.remove(r1);	 Catch:{ all -> 0x0035 }
        r3 = r4.a;	 Catch:{ all -> 0x0035 }
        r0 = r4.a(r1, r0);	 Catch:{ all -> 0x0035 }
        r0 = r3 - r0;
        r4.a = r0;	 Catch:{ all -> 0x0035 }
        r0 = r4.d;	 Catch:{ all -> 0x0035 }
        r0 = r0 + 1;
        r4.d = r0;	 Catch:{ all -> 0x0035 }
        monitor-exit(r2);	 Catch:{ all -> 0x0035 }
        goto L_0x0000;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.afinal.bitmap.core.LruBitmapPool.a(int):void");
    }

    public final a remove(String key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        a aVar;
        synchronized (this.f32a) {
            aVar = (a) this.f32a.remove(key);
            if (aVar != null) {
                this.a -= a(key, aVar);
            }
        }
        return aVar;
    }

    private int a(String str, a aVar) {
        int a = a(aVar);
        if (a >= 0) {
            return a;
        }
        throw new IllegalStateException("Negative size: " + str + SearchCriteria.EQ + aVar);
    }

    protected int a(a aVar) {
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

    public final synchronized Map<String, a> snapshot() {
        return new LinkedHashMap(this.f32a);
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

    public final LinkedHashMap<String, a> getPool() {
        return this.f32a;
    }
}
