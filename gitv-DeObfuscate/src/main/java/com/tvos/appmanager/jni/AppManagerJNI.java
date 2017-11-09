package com.tvos.appmanager.jni;

import android.content.Context;
import android.os.Handler;
import com.tvos.appmanager.AppManager;
import com.tvos.appmanager.IAppManager;
import com.tvos.appmanager.model.IAppInfo;
import com.tvos.appmanager.model.StorageStatus;
import java.util.List;

public class AppManagerJNI {
    private static Context context;
    private static Handler mHandler = new Handler();
    private static IAppManager manager;
    private static AppManagerObserver observer;

    private static native void init();

    public static native void updatePkgStatus(String str, int i);

    static {
        System.loadLibrary("AppManager");
    }

    public static void setContext(Context ctx) {
        context = ctx;
        manager = AppManager.createAppManager(context);
        observer = new AppManagerObserver();
        manager.addObserver(observer);
        init();
    }

    public static void setBlackPkgList(List<String> blackPkgList) {
        if (manager != null) {
            manager.setBlackPkgList(blackPkgList);
        }
    }

    public static void finialize() {
        if (manager != null) {
            manager.deleteObserver(observer);
            observer = null;
            manager.release();
            manager = null;
        }
    }

    public static boolean installApp(String pkgname, boolean isSilent) {
        if (manager != null) {
            return manager.installApp(pkgname, isSilent);
        }
        return false;
    }

    public static boolean startApp(String pkgName) {
        if (manager != null) {
            return manager.startApp(pkgName);
        }
        return false;
    }

    public static boolean startAppWithPreview(String pkgName) {
        if (manager != null) {
            return manager.startApp(pkgName, true);
        }
        return false;
    }

    public static IAppInfo getAppInfoByPath(String pkgPath) {
        if (manager != null) {
            return manager.getAppInfoByPath(pkgPath);
        }
        return null;
    }

    public static List<IAppInfo> getInstalledApps(int sortMethod) {
        if (manager != null) {
            return manager.getInstalledApps(sortMethod);
        }
        return null;
    }

    public static List<IAppInfo> getInstalledAppList(int sortMethod) {
        if (manager != null) {
            return manager.getInstalledAppList(sortMethod);
        }
        return null;
    }

    public static List<IAppInfo> getRecenctAppList() {
        if (manager != null) {
            return manager.getRecentAppList();
        }
        return null;
    }

    public static IAppInfo getAppInfoByPkgName(String pkgName) {
        if (manager != null) {
            return manager.getOriginAppInfoByPkg(pkgName);
        }
        return null;
    }

    public static boolean uninstallApp(String pkgName, boolean isSilent) {
        if (manager != null) {
            return manager.uninstallApp(pkgName, isSilent);
        }
        return false;
    }

    public static StorageStatus getStorageStatus() {
        if (manager != null) {
            return manager.getStorageStatus();
        }
        return null;
    }

    public static long getRunningTimeByPkg(String pkgName) {
        if (manager != null) {
            return manager.getRunningTimeByPkg(pkgName);
        }
        return 0;
    }
}
