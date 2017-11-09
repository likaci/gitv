package com.gala.appmanager.appinfo;

public class AppOperation {
    public static final int PACKAGE_ADDED = 0;
    public static final int PACKAGE_MOVED = 3;
    public static final int PACKAGE_PRINSTALLED_ADDED = 4;
    public static final int PACKAGE_REMOVED = 1;
    public static final int PACKAGE_REPLEACED = 2;
    public static final int PACKAGE_UPDATED_REMOVED = 5;
    private int f365a = -1;
    private AppInfo f366a;
    private int f367b = -1;

    public AppOperation(int index, int type) {
        this.f365a = index;
        this.f367b = type;
    }

    public AppInfo getApp() {
        return this.f366a;
    }

    public int getType() {
        return this.f367b;
    }

    public int getIndex() {
        return this.f365a;
    }

    public void setApp(AppInfo app) {
        this.f366a = app;
    }
}
