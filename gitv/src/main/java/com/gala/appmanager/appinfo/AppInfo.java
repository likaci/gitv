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
    private Drawable a;
    private String f217a;
    private boolean f218a;
    private String b;
    private boolean f219b;
    private String c;
    private boolean f220c;
    private String d;

    public AppInfo(ResolveInfo info) {
        boolean z = true;
        this.d = null;
        this.f218a = (info.activityInfo.applicationInfo.flags & 1) != 0;
        if ((info.activityInfo.applicationInfo.flags & 128) == 0) {
            z = false;
        }
        this.f219b = z;
        this.b = info.activityInfo.name;
        this.c = info.activityInfo.packageName;
        this.f220c = false;
    }

    public AppInfo(ApplicationInfo info) {
        this.d = null;
        this.f218a = false;
        this.f219b = false;
        this.f220c = true;
        this.c = info.packageName;
    }

    public void setIsSystemApp(boolean isSys) {
        this.f218a = isSys;
    }

    public void setAppIcon(Drawable icon) {
        int[] iconSize = GalaAppManager.getIconSize();
        if (iconSize[0] <= 0 || iconSize[1] <= 0 || icon == null) {
            this.a = icon;
        } else {
            this.a = a(icon, iconSize[0], iconSize[1]);
        }
    }

    public void setAppName(String name) {
        this.f217a = name;
    }

    public void setAppClassName(String clsName) {
        this.b = clsName;
    }

    public void setAppPakcageName(String pkgName) {
        this.c = pkgName;
    }

    public boolean getIsSystem() {
        return this.f218a;
    }

    public boolean isUpdateSystem() {
        return this.f219b;
    }

    public Drawable getAppIcon() {
        return this.a;
    }

    public String getAppName() {
        return this.f217a;
    }

    public String getAppClassName() {
        return this.b;
    }

    public String getAppPackageName() {
        return this.c;
    }

    public String getApkAbsolutePath() {
        return this.d;
    }

    public void setApkAbsolutePath(String path) {
        this.d = path;
    }

    public boolean isUninstalled() {
        return this.f220c;
    }

    public void setUninstalled(boolean uninstall) {
        this.f220c = uninstall;
    }

    public void setUpdateSystem(boolean update) {
        this.f219b = update;
    }

    private Bitmap a(Drawable drawable) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    private Drawable a(Drawable drawable, int i, int i2) {
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        if (i >= intrinsicWidth) {
            return drawable;
        }
        Bitmap a = a(drawable);
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
        return appPackageName.equals(this.c);
    }
}
