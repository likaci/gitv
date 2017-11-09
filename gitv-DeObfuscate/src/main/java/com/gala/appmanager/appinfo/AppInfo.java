package com.gala.appmanager.appinfo;

import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.gala.appmanager.GalaAppManager;

public class AppInfo {
    private Drawable f357a;
    private String f358a;
    private boolean f359a;
    private String f360b;
    private boolean f361b;
    private String f362c;
    private boolean f363c;
    private String f364d;

    public AppInfo(ResolveInfo info) {
        boolean z = true;
        this.f364d = null;
        this.f359a = (info.activityInfo.applicationInfo.flags & 1) != 0;
        if ((info.activityInfo.applicationInfo.flags & 128) == 0) {
            z = false;
        }
        this.f361b = z;
        this.f360b = info.activityInfo.name;
        this.f362c = info.activityInfo.packageName;
        this.f363c = false;
    }

    public AppInfo(ApplicationInfo info) {
        this.f364d = null;
        this.f359a = false;
        this.f361b = false;
        this.f363c = true;
        this.f362c = info.packageName;
    }

    public void setIsSystemApp(boolean isSys) {
        this.f359a = isSys;
    }

    public void setAppIcon(Drawable icon) {
        int[] iconSize = GalaAppManager.getIconSize();
        if (iconSize[0] <= 0 || iconSize[1] <= 0 || icon == null) {
            this.f357a = icon;
        } else {
            this.f357a = m224a(icon, iconSize[0], iconSize[1]);
        }
    }

    public void setAppName(String name) {
        this.f358a = name;
    }

    public void setAppClassName(String clsName) {
        this.f360b = clsName;
    }

    public void setAppPakcageName(String pkgName) {
        this.f362c = pkgName;
    }

    public boolean getIsSystem() {
        return this.f359a;
    }

    public boolean isUpdateSystem() {
        return this.f361b;
    }

    public Drawable getAppIcon() {
        return this.f357a;
    }

    public String getAppName() {
        return this.f358a;
    }

    public String getAppClassName() {
        return this.f360b;
    }

    public String getAppPackageName() {
        return this.f362c;
    }

    public String getApkAbsolutePath() {
        return this.f364d;
    }

    public void setApkAbsolutePath(String path) {
        this.f364d = path;
    }

    public boolean isUninstalled() {
        return this.f363c;
    }

    public void setUninstalled(boolean uninstall) {
        this.f363c = uninstall;
    }

    public void setUpdateSystem(boolean update) {
        this.f361b = update;
    }

    private Bitmap m223a(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    private Drawable m224a(Drawable drawable, int i, int i2) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (i >= intrinsicWidth) {
            return drawable;
        }
        Bitmap a = m223a(drawable);
        Matrix matrix = new Matrix();
        float f = ((float) i) / ((float) intrinsicWidth);
        float f2 = ((float) i2) / ((float) intrinsicHeight);
        if (f <= f2) {
            f2 = f;
        }
        matrix.postScale(f2, f2);
        return new BitmapDrawable(Bitmap.createBitmap(a, 0, 0, intrinsicWidth, intrinsicHeight, matrix, true));
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof AppInfo)) {
            return false;
        }
        String appPackageName = ((AppInfo) o).getAppPackageName();
        if (appPackageName == null || "".equals(appPackageName)) {
            return false;
        }
        return appPackageName.equals(this.f362c);
    }
}
