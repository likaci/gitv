package com.gala.afinal.bitmap.core;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import com.gala.afinal.utils.Utils;
import com.gala.imageprovider.p000private.C0123G;
import com.gala.imageprovider.p000private.C0125a;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BitmapCache {
    private ImageCacheParams f9a;
    private DiskCache f10a;
    private IMemoryCache f11a;

    public static class ImageCacheParams {
        public int bitmapPoolSize = 5242880;
        public int diskCacheCount = 10000;
        public File diskCacheDir;
        public boolean diskCacheEnabled = true;
        public int diskCacheSize = 52428800;
        public int memCacheSize = 16777216;
        public boolean memoryCacheEnabled = true;
        public boolean recycleImmediately = true;

        public ImageCacheParams(File diskCacheDir) {
            this.diskCacheDir = diskCacheDir;
        }

        public ImageCacheParams(String diskCacheDir) {
            this.diskCacheDir = new File(diskCacheDir);
        }

        public void setMemCacheSizePercent(Context context, float percent) {
            if (percent < 0.05f || percent > 0.8f) {
                throw new IllegalArgumentException("setMemCacheSizePercent - percent must be between 0.05 and 0.8 (inclusive)");
            }
            this.memCacheSize = Math.round(((((float) ((ActivityManager) context.getSystemService("activity")).getMemoryClass()) * percent) * 1024.0f) * 1024.0f);
            if (this.memCacheSize < 8388608) {
                this.memCacheSize = 8388608;
            }
            if (this.memCacheSize > 52428800) {
                this.memCacheSize = 52428800;
            }
            C0123G.m279a("FinalBitmap/BitmapCache", ">>>>>【afinal】, set memory cache size " + this.memCacheSize);
        }

        public void setMemCacheSize(int memCacheSize) {
            this.memCacheSize = memCacheSize;
        }

        public void setBitmapPoolSize(int bitmapPoolSize) {
            this.bitmapPoolSize = bitmapPoolSize;
        }

        public void setDiskCacheSize(int diskCacheSize) {
            this.diskCacheSize = diskCacheSize;
            C0123G.m279a("FinalBitmap/BitmapCache", ">>>>>【afinal】 , set disk cache size " + diskCacheSize);
        }

        public void setDiskCacheCount(int diskCacheCount) {
            this.diskCacheCount = diskCacheCount;
        }

        public void setRecycleImmediately(boolean recycleImmediately) {
            this.recycleImmediately = recycleImmediately;
        }
    }

    public BitmapCache(ImageCacheParams cacheParams) {
        this.f9a = cacheParams;
        if (this.f9a.memoryCacheEnabled) {
            LruBitmapPoolImpl.getInstance(this.f9a.bitmapPoolSize);
            if (this.f9a.recycleImmediately) {
                this.f11a = new SoftMemoryCacheImpl(this.f9a.memCacheSize);
            } else {
                this.f11a = new BaseMemoryCacheImpl(this.f9a.memCacheSize);
            }
        }
        if (cacheParams.diskCacheEnabled) {
            try {
                this.f10a = new DiskCache(this.f9a.diskCacheDir.getAbsolutePath(), this.f9a.diskCacheCount, this.f9a.diskCacheSize, false);
            } catch (IOException e) {
            }
        }
    }

    public void addToMemoryCache(String url, C0125a bitmapModel) {
        if (url != null && bitmapModel != null) {
            this.f11a.put(url, bitmapModel);
        }
    }

    public void addToDiskCache(String url, byte[] data) {
        if (this.f10a != null && url != null && data != null) {
            byte[] makeKey = Utils.makeKey(url);
            long crc64Long = Utils.crc64Long(makeKey);
            ByteBuffer allocate = ByteBuffer.allocate(makeKey.length + data.length);
            allocate.put(makeKey);
            allocate.put(data);
            synchronized (this.f10a) {
                try {
                    this.f10a.insert(crc64Long, allocate.array());
                } catch (IOException e) {
                }
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean getImageData(java.lang.String r6, com.gala.afinal.bitmap.core.BytesBufferPool.BytesBuffer r7) {
        /*
        r5 = this;
        r0 = 0;
        r1 = r5.f10a;
        if (r1 != 0) goto L_0x0006;
    L_0x0005:
        return r0;
    L_0x0006:
        r1 = com.gala.afinal.utils.Utils.makeKey(r6);
        r2 = com.gala.afinal.utils.Utils.crc64Long(r1);
        r4 = new com.gala.afinal.bitmap.core.DiskCache$LookupRequest;	 Catch:{ IOException -> 0x0029 }
        r4.<init>();	 Catch:{ IOException -> 0x0029 }
        r4.key = r2;	 Catch:{ IOException -> 0x0029 }
        r2 = r7.data;	 Catch:{ IOException -> 0x0029 }
        r4.buffer = r2;	 Catch:{ IOException -> 0x0029 }
        r2 = r5.f10a;	 Catch:{ IOException -> 0x0029 }
        monitor-enter(r2);	 Catch:{ IOException -> 0x0029 }
        r3 = r5.f10a;	 Catch:{ all -> 0x0026 }
        r3 = r3.lookup(r4);	 Catch:{ all -> 0x0026 }
        if (r3 != 0) goto L_0x002b;
    L_0x0024:
        monitor-exit(r2);	 Catch:{ all -> 0x0026 }
        goto L_0x0005;
    L_0x0026:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ IOException -> 0x0029 }
        throw r1;	 Catch:{ IOException -> 0x0029 }
    L_0x0029:
        r1 = move-exception;
        goto L_0x0005;
    L_0x002b:
        monitor-exit(r2);	 Catch:{ IOException -> 0x0029 }
        r2 = r4.buffer;	 Catch:{ IOException -> 0x0029 }
        r2 = com.gala.afinal.utils.Utils.isSameKey(r1, r2);	 Catch:{ IOException -> 0x0029 }
        if (r2 == 0) goto L_0x0005;
    L_0x0034:
        r2 = r4.buffer;	 Catch:{ IOException -> 0x0029 }
        r7.data = r2;	 Catch:{ IOException -> 0x0029 }
        r1 = r1.length;	 Catch:{ IOException -> 0x0029 }
        r7.offset = r1;	 Catch:{ IOException -> 0x0029 }
        r1 = r4.length;	 Catch:{ IOException -> 0x0029 }
        r2 = r7.offset;	 Catch:{ IOException -> 0x0029 }
        r1 = r1 - r2;
        r7.length = r1;	 Catch:{ IOException -> 0x0029 }
        r0 = 1;
        goto L_0x0005;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.afinal.bitmap.core.BitmapCache.getImageData(java.lang.String, com.gala.afinal.bitmap.core.BytesBufferPool$BytesBuffer):boolean");
    }

    public Bitmap getBitmapFromMemoryCache(String data) {
        if (this.f11a != null) {
            C0125a c0125a = this.f11a.get(data);
            if (c0125a != null) {
                c0125a.m285a(false);
                return c0125a.m284a();
            }
        }
        return null;
    }

    public void setBitmapRecycle(String data) {
        if (this.f11a != null && this.f11a.get(data) != null) {
            C0125a c0125a = this.f11a.get(data);
            if (c0125a != null) {
                c0125a.m285a(true);
            }
        }
    }

    public void clearCache() {
        clearMemoryCache();
        clearDiskCache();
    }

    public void clearDiskCache() {
        if (this.f10a != null) {
            this.f10a.delete();
        }
    }

    public void clearMemoryCache() {
        if (this.f11a != null) {
            this.f11a.evictAll();
        }
    }

    public void clearCache(String key) {
        clearMemoryCache(key);
        clearDiskCache(key);
    }

    public void clearDiskCache(String key) {
        addToDiskCache(key, new byte[0]);
    }

    public void clearMemoryCache(String key) {
        if (this.f11a != null) {
            this.f11a.remove(key);
        }
    }

    public void close() {
        if (this.f10a != null) {
            this.f10a.close();
        }
    }
}
