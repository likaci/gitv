package com.gala.afinal.bitmap.core;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.os.Build.VERSION;
import com.gala.afinal.utils.Utils;
import com.gala.imageprovider.private.a;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LruBitmapPoolImpl {
    private static LruBitmapPoolImpl a;
    private LruBitmapPool f47a;

    class AnonymousClass1 extends LruBitmapPool {
        AnonymousClass1(int x0) {
            super(x0);
        }

        protected final int a(a aVar) {
            return Utils.getBitmapSize(aVar.a());
        }
    }

    private LruBitmapPoolImpl(int size) {
        this.f47a = new AnonymousClass1(size);
    }

    public static LruBitmapPoolImpl getInstance(int size) {
        if (a == null) {
            a = new LruBitmapPoolImpl(size);
        }
        return a;
    }

    public static LruBitmapPoolImpl get() {
        return a;
    }

    public LruBitmapPool getPool() {
        return this.f47a;
    }

    public void put(String key, a bitmap) {
        this.f47a.put(key, bitmap);
    }

    public Bitmap getBitmap(Options options) {
        if (this.f47a.getPool().size() <= 0 || options.inSampleSize != 1) {
            return null;
        }
        Bitmap bitmap;
        LinkedHashMap pool = this.f47a.getPool();
        synchronized (pool) {
            for (Entry entry : pool.entrySet()) {
                Bitmap a = ((a) entry.getValue()).a();
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
        this.f47a.evictAll();
    }

    public a remove(String key) {
        return this.f47a.remove(key);
    }
}
