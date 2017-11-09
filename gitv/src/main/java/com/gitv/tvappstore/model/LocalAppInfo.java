package com.gitv.tvappstore.model;

import android.graphics.drawable.Drawable;

public class LocalAppInfo {
    private Drawable appIcon;
    private String appName;
    private boolean hasUpdate;
    private boolean isNew;
    private String pkgName;

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getPkgName() {
        return this.pkgName;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isNew() {
        return this.isNew;
    }

    public void setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public boolean hasUpdate() {
        return this.hasUpdate;
    }
}
