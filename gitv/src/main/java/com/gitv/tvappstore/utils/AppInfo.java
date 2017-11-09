package com.gitv.tvappstore.utils;

import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AppInfo {
    private String mAbsolutePath;
    private String mAppName;
    private String mClassName;
    private Drawable mIcon;
    private boolean mIsSystemApp;
    private boolean mIsUninstall;
    private boolean mIsUpdateSysApp;
    private String mPackageName;

    public AppInfo(ResolveInfo info) {
        boolean z = true;
        this.mAbsolutePath = null;
        this.mIsSystemApp = (info.activityInfo.applicationInfo.flags & 1) != 0;
        if ((info.activityInfo.applicationInfo.flags & 128) == 0) {
            z = false;
        }
        this.mIsUpdateSysApp = z;
        this.mClassName = info.activityInfo.name;
        this.mPackageName = info.activityInfo.packageName;
        this.mIsUninstall = false;
    }

    public AppInfo(ApplicationInfo info) {
        this.mAbsolutePath = null;
        this.mIsSystemApp = false;
        this.mIsUpdateSysApp = false;
        this.mIsUninstall = true;
        this.mPackageName = info.packageName;
    }

    public void setAppIcon(Drawable icon) {
        this.mIcon = icon;
    }

    public void setIsSystemApp(boolean isSys) {
        this.mIsSystemApp = isSys;
    }

    public void setAppName(String name) {
        this.mAppName = name;
    }

    public void setAppClassName(String clsName) {
        this.mClassName = clsName;
    }

    public void setAppPakcageName(String pkgName) {
        this.mPackageName = pkgName;
    }

    public boolean getIsSystem() {
        return this.mIsSystemApp;
    }

    public boolean isUpdateSystem() {
        return this.mIsUpdateSysApp;
    }

    public Drawable getAppIcon() {
        return this.mIcon;
    }

    public String getAppName() {
        return this.mAppName;
    }

    public String getAppClassName() {
        return this.mClassName;
    }

    public String getAppPackageName() {
        return this.mPackageName;
    }

    public String getApkAbsolutePath() {
        return this.mAbsolutePath;
    }

    public void setApkAbsolutePath(String path) {
        this.mAbsolutePath = path;
    }

    public boolean isUninstalled() {
        return this.mIsUninstall;
    }

    public void setUninstalled(boolean uninstall) {
        this.mIsUninstall = uninstall;
    }

    public void setUpdateSystem(boolean update) {
        this.mIsUpdateSysApp = update;
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Config config;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (drawable.getOpacity() != -1) {
            config = Config.ARGB_8888;
        } else {
            config = Config.RGB_565;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (w >= width) {
            return drawable;
        }
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w) / ((float) width);
        float scaleHeight = ((float) h) / ((float) height);
        float scale = scaleWidth;
        if (scaleWidth > scaleHeight) {
            scale = scaleHeight;
        }
        matrix.postScale(scale, scale);
        return new BitmapDrawable(Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true));
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AppInfo)) {
            return false;
        }
        String pkgName = ((AppInfo) o).getAppPackageName();
        if (pkgName == null || "".equals(pkgName)) {
            return false;
        }
        return pkgName.equals(this.mPackageName);
    }
}
