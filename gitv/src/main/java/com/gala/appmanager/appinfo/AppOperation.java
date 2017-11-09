package com.gala.appmanager.appinfo;

public class AppOperation {
    public static final int PACKAGE_ADDED = 0;
    public static final int PACKAGE_MOVED = 3;
    public static final int PACKAGE_PRINSTALLED_ADDED = 4;
    public static final int PACKAGE_REMOVED = 1;
    public static final int PACKAGE_REPLEACED = 2;
    public static final int PACKAGE_UPDATED_REMOVED = 5;
    private int a = -1;
    private AppInfo f221a;
    private int b = -1;

    public AppOperation(int index, int type) {
        this.a = index;
        this.b = type;
    }

    public AppInfo getApp() {
        return this.f221a;
    }

    public int getType() {
        return this.b;
    }

    public int getIndex() {
        return this.a;
    }

    public void setApp(AppInfo app) {
        this.f221a = app;
    }
}
