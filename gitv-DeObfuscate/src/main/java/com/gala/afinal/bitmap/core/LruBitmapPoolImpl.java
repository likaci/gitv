package com.gala.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import com.gala.afinal.utils.Utils;
import com.gala.imageprovider.p000private.C0125a;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruBitmapPoolImpl {
    private static LruBitmapPoolImpl f54a;
    private LruBitmapPool f55a;

    class C00331 extends LruBitmapPool {
        C00331(int x0) {
            super(x0);
        }

        protected final int mo316a(C0125a c0125a) {
            return Utils.getBitmapSize(c0125a.m284a());
        }
    }

    private LruBitmapPoolImpl(int size) {
        this.f55a = new C00331(size);
    }

    public static LruBitmapPoolImpl getInstance(int size) {
        if (f54a == null) {
            f54a = new LruBitmapPoolImpl(size);
        }
        return f54a;
    }

    public static LruBitmapPoolImpl get() {
        return f54a;
    }

    public LruBitmapPool getPool() {
        return this.f55a;
    }

    public void put(String key, C0125a bitmap) {
        this.f55a.put(key, bitmap);
    }

    public Bitmap getBitmap(Options options) {
        if (this.f55a.getPool().size() <= 0 || options.inSampleSize != 1) {
            return null;
        }
        Bitmap bitmap;
        LinkedHashMap pool = this.f55a.getPool();
        synchronized (pool) {
            for (Entry entry : pool.entrySet()) {
                Bitmap a = ((C0125a) entry.getValue()).m284a();
                if (a != null) {
                    Object obj;
                    if (VERSION.SDK_INT < 19) {
                        obj = (a.getWidth() == options.outWidth && a.getHeight() == options.outHeight && options.inSampleSize == 1) ? 1 : null;
                    } else {
                        int i;
                        int i2 = (options.outHeight / options.inSampleSize) * (options.outWidth / options.inSampleSize);
                        Config config = a.getConfig();
                        if (config == Config.ARGB_8888) {
                            i = 4;
                        } else if (config == Config.RGB_565) {
                            i = 2;
                        } else if (config == Config.ARGB_4444) {
                            i = 2;
                        } else {
                            config = Config.ALPHA_8;
                            i = 1;
                        }
                        if (i * i2 <= a.getAllocationByteCount()) {
                            i = 1;
                        } else {
                            obj = null;
                        }
                    }
                    if (obj != null) {
                        remove((String) entry.getKey());
                        bitmap = a;
                        break;
                    }
                }
            }
            bitmap = null;
        }
        return bitmap;
    }

    public void evictAll() {
        this.f55a.evictAll();
    }

    public C0125a remove(String key) {
        return this.f55a.remove(key);
    }
}
