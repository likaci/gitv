package com.gala.afinal;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import com.gala.afinal.bitmap.core.BitmapCache;
import com.gala.afinal.bitmap.core.BitmapCache.ImageCacheParams;
import com.gala.afinal.bitmap.core.BitmapDisplayConfig;
import com.gala.afinal.bitmap.core.BitmapProcess;
import com.gala.afinal.bitmap.display.Displayer;
import com.gala.afinal.bitmap.display.SimpleDisplayer;
import com.gala.afinal.bitmap.download.Downloader;
import com.gala.afinal.bitmap.download.SimpleDownloader;
import com.gala.afinal.core.AsyncTask;
import com.gala.afinal.utils.Utils;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.private.G;
import com.gala.imageprovider.private.a;
import com.gala.imageprovider.private.b;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FinalBitmap {
    protected static final List<BitmapLoadTask> mBitmapLoadTaskList = new CopyOnWriteArrayList();
    private static FinalBitmap mFinalBitmap;
    private final String TAG = "FinalBitmap";
    private ExecutorService bitmapLoadAndDisplayExecutor;
    private BitmapProcess mBitmapProcess;
    private FinalBitmapConfig mConfig;
    private Context mContext;
    private BitmapCache mImageCache;
    private boolean mInit = false;
    private boolean mPauseWork = false;
    private final Object mPauseWorkLock = new Object();

    class BitmapLoadTask extends AsyncTask<Object, Void, Bitmap> {
        private String TASK_TAG = "Afinal/BitmapLoadTask@";
        private final IImageCallback imageCallback;
        private final ImageRequest imageRequest;
        private boolean mExitTasksEarly = false;

        public BitmapLoadTask(ImageRequest request, IImageCallback callback) {
            this.imageCallback = callback;
            this.imageRequest = request;
            this.TASK_TAG += Integer.toHexString(hashCode());
        }

        protected Bitmap doInBackground(Object... objArr) {
            Bitmap bitmap = null;
            synchronized (FinalBitmap.this.mPauseWorkLock) {
                while (FinalBitmap.this.mPauseWork && !isCancelled()) {
                    try {
                        FinalBitmap.this.mPauseWorkLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
            if (!this.mExitTasksEarly) {
                if (!(isCancelled() || this.mExitTasksEarly)) {
                    bitmap = FinalBitmap.this.processBitmap(this.imageRequest, this.TASK_TAG);
                }
                if (bitmap != null) {
                    FinalBitmap.this.mImageCache.addToMemoryCache(b.a(this.imageRequest), new a(bitmap));
                }
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled() || this.mExitTasksEarly) {
                bitmap = null;
            }
            if (bitmap != null) {
                this.imageCallback.onSuccess(this.imageRequest, bitmap);
            } else {
                this.imageCallback.onFailure(this.imageRequest, null);
            }
            FinalBitmap.mBitmapLoadTaskList.remove(this);
        }

        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            FinalBitmap.mBitmapLoadTaskList.remove(this);
            synchronized (FinalBitmap.this.mPauseWorkLock) {
                FinalBitmap.this.mPauseWorkLock.notifyAll();
            }
        }
    }

    class CacheExecutecTask extends AsyncTask<Object, Void, Void> {
        public static final int MESSAGE_CLEAR = 1;
        public static final int MESSAGE_CLEAR_DISK = 3;
        public static final int MESSAGE_CLEAR_KEY = 4;
        public static final int MESSAGE_CLEAR_KEY_IN_DISK = 5;
        public static final int MESSAGE_CLOSE = 2;

        private CacheExecutecTask() {
        }

        protected Void doInBackground(Object... params) {
            switch (((Integer) params[0]).intValue()) {
                case 1:
                    FinalBitmap.this.clearCacheInternalInBackgroud();
                    break;
                case 2:
                    FinalBitmap.this.closeCacheInternalInBackgroud();
                    break;
                case 3:
                    FinalBitmap.this.clearDiskCacheInBackgroud();
                    break;
                case 4:
                    FinalBitmap.this.clearCacheInBackgroud(String.valueOf(params[1]));
                    break;
                case 5:
                    FinalBitmap.this.clearDiskCacheInBackgroud(String.valueOf(params[1]));
                    break;
            }
            return null;
        }
    }

    class FinalBitmapConfig {
        public int bitmapPoolSize;
        public String cachePath;
        public BitmapDisplayConfig defaultDisplayConfig = new BitmapDisplayConfig();
        public int diskCacheCount;
        public int diskCacheSize;
        public Downloader downloader;
        public int memCacheSize;
        public float memCacheSizePercent;
        public int poolSize = 2;
        public boolean recycleImmediately = true;

        public FinalBitmapConfig(Context context) {
            this.defaultDisplayConfig.setAnimation(null);
            this.defaultDisplayConfig.setAnimationType(1);
            this.defaultDisplayConfig.setBitmapHeight(0);
            this.defaultDisplayConfig.setBitmapWidth(0);
        }
    }

    private FinalBitmap(Context context) {
        this.mContext = context;
        this.mConfig = new FinalBitmapConfig(context);
        configDiskCachePath(Utils.getDiskCacheDir(context, "afinalCache").getAbsolutePath());
        configDisplayer(new SimpleDisplayer());
        configDownlader(new SimpleDownloader());
    }

    public static synchronized FinalBitmap create(Context ctx) {
        FinalBitmap finalBitmap;
        synchronized (FinalBitmap.class) {
            if (mFinalBitmap == null) {
                mFinalBitmap = new FinalBitmap(ctx.getApplicationContext());
            }
            finalBitmap = mFinalBitmap;
        }
        return finalBitmap;
    }

    public FinalBitmap configLoadingImage(Bitmap bitmap) {
        this.mConfig.defaultDisplayConfig.setLoadingBitmap(bitmap);
        return this;
    }

    public FinalBitmap configLoadingImage(int resId) {
        this.mConfig.defaultDisplayConfig.setLoadingBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), resId));
        return this;
    }

    public FinalBitmap configLoadfailImage(Bitmap bitmap) {
        this.mConfig.defaultDisplayConfig.setLoadfailBitmap(bitmap);
        return this;
    }

    public FinalBitmap configLoadfailImage(int resId) {
        this.mConfig.defaultDisplayConfig.setLoadfailBitmap(BitmapFactory.decodeResource(this.mContext.getResources(), resId));
        return this;
    }

    public FinalBitmap configBitmapMaxHeight(int bitmapHeight) {
        this.mConfig.defaultDisplayConfig.setBitmapHeight(bitmapHeight);
        return this;
    }

    public FinalBitmap configBitmapMaxWidth(int bitmapWidth) {
        this.mConfig.defaultDisplayConfig.setBitmapWidth(bitmapWidth);
        return this;
    }

    public FinalBitmap configDownlader(Downloader downlader) {
        this.mConfig.downloader = downlader;
        return this;
    }

    public FinalBitmap configDisplayer(Displayer displayer) {
        return this;
    }

    public FinalBitmap configDiskCachePath(String strPath) {
        if (!TextUtils.isEmpty(strPath)) {
            this.mConfig.cachePath = strPath;
        }
        return this;
    }

    public FinalBitmap configMemoryCacheSize(int size) {
        this.mConfig.memCacheSize = size;
        return this;
    }

    public FinalBitmap configBitmapPoolSize(int size) {
        this.mConfig.bitmapPoolSize = size;
        return this;
    }

    public FinalBitmap configMemoryCachePercent(float percent) {
        this.mConfig.memCacheSizePercent = percent;
        return this;
    }

    public FinalBitmap configDiskCacheSize(int size) {
        this.mConfig.diskCacheSize = size;
        return this;
    }

    public FinalBitmap configDiskCacheCount(int count) {
        this.mConfig.diskCacheCount = count;
        return this;
    }

    public FinalBitmap configBitmapLoadThreadSize(int size) {
        if (size > 0) {
            this.mConfig.poolSize = size;
        }
        return this;
    }

    public FinalBitmap configRecycleImmediately(boolean recycleImmediately) {
        this.mConfig.recycleImmediately = recycleImmediately;
        return this;
    }

    private FinalBitmap init() {
        if (!this.mInit) {
            ImageCacheParams imageCacheParams = new ImageCacheParams(this.mConfig.cachePath);
            if (this.mConfig.bitmapPoolSize > 0 && this.mConfig.bitmapPoolSize < 5242880) {
                imageCacheParams.bitmapPoolSize = this.mConfig.bitmapPoolSize;
            }
            if (((double) this.mConfig.memCacheSizePercent) > 0.05d && ((double) this.mConfig.memCacheSizePercent) < 0.8d) {
                imageCacheParams.setMemCacheSizePercent(this.mContext, this.mConfig.memCacheSizePercent);
            } else if (this.mConfig.memCacheSize > 2097152) {
                G.a("FinalBitmap", ">>>>>afinal - set memory cache size: " + this.mConfig.memCacheSize);
                imageCacheParams.setMemCacheSize(this.mConfig.memCacheSize);
            } else {
                G.a("FinalBitmap", ">>>>>afinal - set memory cache size [default]");
                imageCacheParams.setMemCacheSizePercent(this.mContext, 0.1f);
            }
            if (this.mConfig.diskCacheSize > 5242880) {
                G.a("FinalBitmap", ">>>>>afinal - set disk cache size: " + this.mConfig.diskCacheSize);
                imageCacheParams.setDiskCacheSize(this.mConfig.diskCacheSize);
            } else {
                G.a("FinalBitmap", ">>>>>afinal - set disk cache size [default 50M]");
            }
            if (this.mConfig.diskCacheCount > 50) {
                G.a("FinalBitmap", ">>>>>afinal - set disk cache count: " + this.mConfig.diskCacheCount);
                imageCacheParams.setDiskCacheCount(this.mConfig.diskCacheCount);
            } else {
                G.a("FinalBitmap", ">>>>>afinal - set disk cache count [default]");
            }
            configRecycleImmediately(false);
            imageCacheParams.setRecycleImmediately(this.mConfig.recycleImmediately);
            this.mImageCache = new BitmapCache(imageCacheParams);
            G.a("FinalBitmap", ">>>>>ThreadPool size=" + this.mConfig.poolSize);
            this.bitmapLoadAndDisplayExecutor = Executors.newFixedThreadPool(this.mConfig.poolSize, new ThreadFactory() {
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r);
                    thread.setPriority(1);
                    return thread;
                }
            });
            this.mBitmapProcess = new BitmapProcess(this.mConfig.downloader, this.mImageCache);
            this.mInit = true;
        }
        return this;
    }

    private void clearCacheInternalInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.clearCache();
        }
    }

    private void clearDiskCacheInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.clearDiskCache();
        }
    }

    private void clearCacheInBackgroud(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearCache(key);
        }
    }

    private void clearDiskCacheInBackgroud(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearDiskCache(key);
        }
    }

    private void closeCacheInternalInBackgroud() {
        if (this.mImageCache != null) {
            this.mImageCache.close();
            this.mImageCache = null;
            mFinalBitmap = null;
        }
    }

    private Bitmap processBitmap(ImageRequest request, String tag) {
        if (this.mBitmapProcess != null) {
            return this.mBitmapProcess.getBitmap(request, tag);
        }
        return null;
    }

    public Bitmap getBitmapFromCache(ImageRequest imageRequest) {
        Bitmap bitmapFromMemoryCache = getBitmapFromMemoryCache(b.a(imageRequest));
        if (bitmapFromMemoryCache == null) {
            return getBitmapFromDiskCache(imageRequest);
        }
        return bitmapFromMemoryCache;
    }

    public Bitmap getBitmapFromMemoryCache(String key) {
        return this.mImageCache.getBitmapFromMemoryCache(key);
    }

    public void setBitmapRecycle(String key) {
        this.mImageCache.setBitmapRecycle(key);
    }

    public Bitmap getBitmapFromDiskCache(ImageRequest imageRequest) {
        return getBitmapFromDiskCache(imageRequest, null);
    }

    public Bitmap getBitmapFromDiskCache(ImageRequest imageRequest, BitmapDisplayConfig bitmapDisplayConfig) {
        return this.mBitmapProcess.getFromDisk(imageRequest, "FinalBitmap");
    }

    public void onDestroy() {
        closeCache();
    }

    public void clearCache() {
        new CacheExecutecTask().execute(Integer.valueOf(1));
    }

    public void clearCache(String key) {
        new CacheExecutecTask().execute(Integer.valueOf(4), key);
    }

    public void clearMemoryCache() {
        if (this.mImageCache != null) {
            this.mImageCache.clearMemoryCache();
        }
    }

    public void clearMemoryCache(String key) {
        if (this.mImageCache != null) {
            this.mImageCache.clearMemoryCache(key);
        }
    }

    public void clearDiskCache() {
        new CacheExecutecTask().execute(Integer.valueOf(3));
    }

    public void clearDiskCache(String key) {
        new CacheExecutecTask().execute(Integer.valueOf(5), key);
    }

    public void closeCache() {
        new CacheExecutecTask().execute(Integer.valueOf(2));
    }

    public void exitTasksEarly(boolean exitTasksEarly) {
        if (exitTasksEarly) {
            pauseWork(false);
        }
    }

    public void pauseWork(boolean pauseWork) {
        synchronized (this.mPauseWorkLock) {
            this.mPauseWork = pauseWork;
            if (!this.mPauseWork) {
                this.mPauseWorkLock.notifyAll();
            }
        }
    }

    public void loadBitmap(ImageRequest request, IImageCallback imageCallback) {
        if (!this.mInit) {
            init();
        }
        Bitmap bitmap = null;
        if (this.mImageCache != null) {
            bitmap = this.mImageCache.getBitmapFromMemoryCache(b.a(request));
        }
        if (bitmap != null) {
            imageCallback.onSuccess(request, bitmap);
            return;
        }
        BitmapLoadTask bitmapLoadTask = new BitmapLoadTask(request, imageCallback);
        if (request.getShouldBeKilled()) {
            mBitmapLoadTaskList.add(bitmapLoadTask);
        }
        bitmapLoadTask.executeOnExecutor(this.bitmapLoadAndDisplayExecutor, request, imageCallback);
    }

    public void stopAllTasks() {
        G.a("FinalBitmap", ">>>>>afinal - stopAllTasks");
        for (BitmapLoadTask bitmapLoadTask : mBitmapLoadTaskList) {
            bitmapLoadTask.cancel(true);
            bitmapLoadTask.imageRequest.setStopFlag(true);
        }
    }
}
