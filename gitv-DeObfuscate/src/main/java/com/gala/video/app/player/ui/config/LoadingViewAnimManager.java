package com.gala.video.app.player.ui.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerConfigManager;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class LoadingViewAnimManager {
    private static final int FRAMEANIM_INTERVAL_TIME = 100;
    private static final int SCREEN_HEIGHT_1080P = 1080;
    private static final int SCREEN_WIDTH_1080P = 1920;
    private static final String TAG = "Player/Ui/LoadingviewAnimManager";
    private static LoadingViewAnimManager instance;
    private static AnimationDrawable sAnim;
    private static int sAnimHeight;
    private static int sAnimWidth;
    private static String sBackgroundPath;
    private static BitmapDrawable sBitmapDrawable;
    private static TreeMap<String, String> sTreeMap;
    private int mBackgroundMaxHeight;
    private int mBackgroundMaxWidth;
    private Context mContext;
    private int mNewHeight;
    private int mNewWidth;

    private class FetchAnimThread extends Thread8K {
        private AnimationDrawable mFrameAnim = null;
        private TreeMap<String, String> mTreeMap;

        public FetchAnimThread(TreeMap<String, String> treeMap) {
            this.mTreeMap = treeMap;
        }

        public FetchAnimThread() {
            super("FetchAnimThread");
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(LoadingViewAnimManager.TAG, "FetchAnimThread treeMap=" + this.mTreeMap);
            }
            if (this.mTreeMap != null) {
                List<Bitmap> list = LoadingViewAnimManager.this.getLoadingImage(this.mTreeMap);
                if (list != null) {
                    this.mFrameAnim = new AnimationDrawable();
                    for (Bitmap bitmap : list) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(LoadingViewAnimManager.TAG, "FetchAnimThread bitmap=" + bitmap);
                        }
                        this.mFrameAnim.addFrame(LoadingViewAnimManager.this.getBimapDrawable(bitmap), 100);
                    }
                }
                LoadingViewAnimManager.sAnim = this.mFrameAnim;
            }
        }
    }

    class FetchBackgroundThread extends Thread8K {
        private BitmapDrawable mBitmapDrawable;
        private String mPath;

        public FetchBackgroundThread(String bgPath) {
            this.mPath = bgPath;
        }

        public FetchBackgroundThread() {
            super("FetchBackgroundThread");
        }

        public void run() {
            Bitmap bitmap = LoadingViewAnimManager.this.createTargetBitmap(this.mPath, LoadingViewAnimManager.this.mBackgroundMaxWidth, LoadingViewAnimManager.this.mBackgroundMaxHeight, ScaleType.CENTER_INSIDE, Config.ARGB_8888);
            if (bitmap != null) {
                this.mBitmapDrawable = new BitmapDrawable(LoadingViewAnimManager.this.mContext.getResources(), bitmap);
                LoadingViewAnimManager.this.setBackground(this.mBitmapDrawable);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(LoadingViewAnimManager.TAG, "FetchBackgroundThread run() drawable=" + this.mBitmapDrawable);
                }
            }
        }
    }

    private LoadingViewAnimManager(Context context) {
        this.mContext = context;
    }

    public static synchronized LoadingViewAnimManager getInstance(Context context) {
        LoadingViewAnimManager loadingViewAnimManager;
        synchronized (LoadingViewAnimManager.class) {
            if (instance == null) {
                instance = new LoadingViewAnimManager(context);
            }
            loadingViewAnimManager = instance;
        }
        return loadingViewAnimManager;
    }

    public void setAnimPath(TreeMap<String, String> treemap) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setAnimPath newMap=" + treemap + ", oldMap" + sTreeMap);
        }
        if (!PlayerConfigManager.getPlayerConfig().isSupportAnimation()) {
            return;
        }
        if (treemap == null && sTreeMap != null) {
            initAnimParam();
        } else if (sTreeMap == null && treemap != null) {
            sTreeMap = treemap;
            thread = new FetchAnimThread(treemap);
            thread.setName("loadingviewmanager");
            thread.setPriority(1);
            thread.start();
        } else if (sTreeMap != null && treemap != null && !((String) sTreeMap.get("1")).equals((String) treemap.get("1"))) {
            thread = new FetchAnimThread(treemap);
            thread.setName("loadingviewmanager");
            thread.setPriority(1);
            thread.start();
            sTreeMap = treemap;
        }
    }

    private void initAnimParam() {
        sAnimWidth = getPixelFromDimensIdSafe(C1291R.dimen.loading_anim_width);
        sAnimHeight = getPixelFromDimensIdSafe(C1291R.dimen.loading_anim_height);
        sAnim = null;
    }

    public synchronized int getAnimWidth() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getAnimWidth sAnimWidth=" + sAnimWidth);
        }
        if (sAnimWidth == 0) {
            sAnimWidth = getPixelFromDimensIdSafe(C1291R.dimen.loading_anim_width);
        }
        return sAnimWidth;
    }

    public synchronized int getAnimHeight() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getAnimWidth getAnimHeight=" + sAnimHeight);
        }
        if (sAnimHeight == 0) {
            sAnimHeight = getPixelFromDimensIdSafe(C1291R.dimen.loading_anim_height);
        }
        return sAnimHeight;
    }

    public synchronized AnimationDrawable getAnim() {
        if (sAnim == null) {
            if (PlayerConfigManager.getPlayerConfig().isSupportAnimation()) {
                sAnim = (AnimationDrawable) this.mContext.getResources().getDrawable(C1291R.drawable.player_loading_animation_new);
            } else {
                sAnim = (AnimationDrawable) this.mContext.getResources().getDrawable(C1291R.drawable.player_loading_animation_single);
            }
        }
        return sAnim;
    }

    private List<Bitmap> getLoadingImage(TreeMap<String, String> map) {
        List<Bitmap> list = null;
        checkPictureSize((String) map.get("1"));
        if (this.mNewHeight > 0 && this.mNewWidth > 0) {
            list = new ArrayList();
            for (Object obj : map.keySet()) {
                String cachePath = (String) map.get(obj);
                Bitmap image = createTargetBitmap(cachePath, this.mNewWidth, this.mNewHeight, ScaleType.CENTER_INSIDE, Config.ARGB_4444);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "getLoadingImage cachePath=" + cachePath);
                }
                if (image != null) {
                    list.add(image);
                }
            }
        }
        return list;
    }

    private void checkPictureSize(String path) {
        double maxHeight = (double) (0.66f * ((float) this.mContext.getResources().getDisplayMetrics().heightPixels));
        double maxWidth = (double) (0.7f * ((float) this.mContext.getResources().getDisplayMetrics().widthPixels));
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int width = options.outWidth;
        int height = options.outHeight;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "checkPictureSize width=" + width + ", height=" + height);
        }
        double ratio = 0.0d;
        double xratio;
        double yratio;
        if (((double) width) > maxWidth && ((double) height) > maxHeight) {
            xratio = ((double) width) / maxWidth;
            yratio = ((double) height) / maxHeight;
            ratio = xratio > yratio ? xratio : yratio;
        } else if (((double) width) >= maxWidth) {
            ratio = ((double) width) / maxWidth;
        } else if (((double) height) >= maxHeight) {
            ratio = ((double) height) / maxHeight;
        } else if (((double) height) < maxHeight && ((double) width) < maxWidth) {
            xratio = ((double) width) / maxWidth;
            yratio = ((double) height) / maxHeight;
            ratio = xratio > yratio ? xratio : yratio;
        }
        this.mNewWidth = (int) (((double) width) / ratio);
        this.mNewHeight = (int) (((double) height) / ratio);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "checkPictureSize mNewWidth=" + this.mNewWidth + ", mNewHeight=" + this.mNewHeight);
        }
        if (this.mNewHeight > 0 && this.mNewWidth > 0) {
            sAnimHeight = this.mNewHeight;
            sAnimWidth = this.mNewWidth;
        }
    }

    private Bitmap createTargetBitmap(String path, int targetWidth, int targetHeight, ScaleType type, Config config) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createTargetBitmap: target w/h=" + targetWidth + "/" + targetHeight + ", scale type=" + type);
        }
        try {
            float scaledWidth;
            float scaledHeight;
            Options dbo = new Options();
            dbo.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, dbo);
            int nativeWidth = dbo.outWidth;
            int nativeHeight = dbo.outHeight;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createTargetBitmap: original w/h=" + nativeWidth + "/" + nativeHeight);
            }
            Options options = createBasicOptions(config);
            if (nativeWidth > targetWidth || nativeHeight > targetHeight) {
                float dx = ((float) nativeWidth) / ((float) targetWidth);
                float dy = ((float) nativeHeight) / ((float) targetHeight);
                float scale = type == ScaleType.CENTER_INSIDE ? Math.max(dx, dy) : Math.min(dx, dy);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "createTargetBitmap: scale=" + scale);
                }
                scaledWidth = ((float) nativeWidth) / scale;
                scaledHeight = ((float) nativeHeight) / scale;
                options.inSampleSize = scale > 1.0f ? Math.round(scale) : 1;
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "createTargetBitmap: inSampleSize=" + options.inSampleSize);
                }
            } else {
                scaledWidth = (float) targetWidth;
                scaledHeight = (float) targetHeight;
                options.inSampleSize = 1;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createTargetBitmap: scaled w/h=" + scaledWidth + "/" + scaledHeight);
            }
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            if (!LogUtils.mIsDebug) {
                return bitmap;
            }
            LogUtils.m1568d(TAG, "<< createTargetBitmap: result bitmap=" + bitmap);
            return bitmap;
        } catch (OutOfMemoryError ex) {
            LogUtils.m1572e(TAG, "<< createTargetBitmap: OOM", ex);
            return null;
        }
    }

    private static Options createBasicOptions(Config config) {
        Options options = new Options();
        if (config != null) {
            options.inPreferredConfig = config;
            if (config != Config.ARGB_8888) {
                options.inDither = true;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "createBasicOptions() return " + options);
        }
        return options;
    }

    private BitmapDrawable getBimapDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(this.mContext.getResources(), bitmap);
        }
        return null;
    }

    private int getPixelFromDimensIdSafe(int dimenId) {
        if (dimenId != 0) {
            return this.mContext.getResources().getDimensionPixelSize(dimenId);
        }
        return 0;
    }

    public void setBackgroundPath(String bgPath) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setBackgroundPath newPath=" + bgPath + ", oldPath=" + sBackgroundPath);
        }
        if (bgPath == null) {
            sBackgroundPath = null;
        } else if (sBackgroundPath == null || !sBackgroundPath.equals(bgPath)) {
            initBackgroundSize();
            FetchBackgroundThread thread = new FetchBackgroundThread(bgPath);
            thread.setName("FetchBackgroundThread");
            thread.start();
            sBackgroundPath = bgPath;
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setBackgroundPath() same path");
        }
    }

    private void initBackgroundSize() {
        this.mBackgroundMaxWidth = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_600dp);
        this.mBackgroundMaxHeight = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_337dp);
    }

    public synchronized void setBackground(BitmapDrawable drawable) {
        sBitmapDrawable = drawable;
    }

    public synchronized BitmapDrawable getBackground() {
        if (sBitmapDrawable == null) {
            sBitmapDrawable = (BitmapDrawable) this.mContext.getResources().getDrawable(C1291R.drawable.share_loadingview_bg);
        }
        return sBitmapDrawable;
    }
}
