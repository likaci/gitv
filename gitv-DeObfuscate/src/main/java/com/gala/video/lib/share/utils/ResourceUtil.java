package com.gala.video.lib.share.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.WindowManager;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class ResourceUtil {
    public static final int NINEPATCH_BORDER = 0;
    private static final SparseIntArray sColorCache = new SparseIntArray(30);
    private static final SparseArray<String> sColorLength6Cache = new SparseArray(12);
    private static final SparseArray<String> sColorLength8Cache = new SparseArray(12);
    private static final SparseIntArray sDimenCache = new SparseIntArray(80);
    private static final SparseIntArray sDimenPixelCache = new SparseIntArray(45);
    private static int sScreenHeight = 0;
    private static int sScreenWidth = 0;
    private static float scale = (((float) getResource().getDisplayMetrics().widthPixels) / 1920.0f);

    public static int getPx(int pixel) {
        if (pixel == 0) {
            return 0;
        }
        return (int) Math.ceil((double) (((float) pixel) * scale));
    }

    public static short getPxShort(int pixel) {
        if (pixel == 0) {
            return (short) 0;
        }
        return (short) ((int) Math.ceil((double) (((float) pixel) * scale)));
    }

    public static String getStr(int resId) {
        if (resId == 0) {
            return null;
        }
        return getResource().getString(resId);
    }

    public static String getStr(int resId, Object... formatArgs) {
        if (resId == 0) {
            return null;
        }
        return getResource().getString(resId, formatArgs);
    }

    public static int getDimen(int dimen) {
        if (dimen == 0) {
            return 0;
        }
        int value = sDimenCache.get(dimen, Integer.MIN_VALUE);
        if (value != Integer.MIN_VALUE) {
            return value;
        }
        int dimension = (int) getResource().getDimension(dimen);
        sDimenCache.put(dimen, dimension);
        return dimension;
    }

    public static int getDimensionPixelSize(int dimen) {
        if (dimen == 0) {
            return 0;
        }
        int value = sDimenPixelCache.get(dimen, Integer.MIN_VALUE);
        if (value != Integer.MIN_VALUE) {
            return value;
        }
        int dimension = getResource().getDimensionPixelSize(dimen);
        sDimenPixelCache.put(dimen, dimension);
        return dimension;
    }

    public static int getColor(int resId) {
        if (resId == 0) {
            return 0;
        }
        int value = sColorCache.get(resId, Integer.MIN_VALUE);
        if (value != Integer.MIN_VALUE) {
            return value;
        }
        int color = getResource().getColor(resId);
        sColorCache.put(resId, color);
        return color;
    }

    public static String getColorLength6(int resId) {
        if (resId == 0) {
            return null;
        }
        String value = (String) sColorLength6Cache.get(resId);
        if (value != null) {
            return value;
        }
        String length8 = getColorLength8(resId);
        String length6 = StringUtils.getLength(length8) != 8 ? "" : length8.substring(2);
        sColorLength6Cache.put(resId, length6);
        return length6;
    }

    public static String getColorLength8(int resId) {
        if (resId == 0) {
            return null;
        }
        String value = (String) sColorLength8Cache.get(resId);
        if (value != null) {
            return value;
        }
        String length8 = Integer.toHexString(getColor(resId));
        sColorLength8Cache.put(resId, length8);
        return length8;
    }

    public static ColorStateList getColorStateList(int resId) {
        if (resId == 0) {
            return null;
        }
        return getResource().getColorStateList(resId);
    }

    public static Drawable getDrawable(int resId) {
        if (resId == 0) {
            return null;
        }
        return getResource().getDrawable(resId);
    }

    public static Drawable getDrawableFromResidStr(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        Resources r = getResource();
        return r.getDrawable(r.getIdentifier(jsonStr, "drawable", CloudUtilsGala.getPackageName()));
    }

    public static Resources getResource() {
        return getContext().getResources();
    }

    public static Context getContext() {
        return AppRuntimeEnv.get().getApplicationContext();
    }

    public static int getScreenHeight() {
        if (sScreenHeight <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
            sScreenHeight = dm.heightPixels;
        }
        return sScreenHeight;
    }

    public static int getScreenWidth() {
        if (sScreenWidth <= 0) {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(dm);
            sScreenWidth = dm.widthPixels;
        }
        return sScreenWidth;
    }

    public static void clearColorCache() {
        sColorCache.clear();
        sColorLength6Cache.clear();
        sColorLength8Cache.clear();
    }
}
