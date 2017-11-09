package com.tvos.appmanager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.IPackageStatsObserver.Stub;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.tvos.appmanager.data.AppDBUtil;
import com.tvos.appmanager.data.GameDBUtil;
import com.tvos.appmanager.model.AppInfo;
import com.tvos.appmanager.model.IAppInfo;
import com.tvos.appmanager.model.StorageStatus;
import com.tvos.appmanager.util.PropUtil;
import com.tvos.appmanager.util.StorageUtil;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import org.cybergarage.soap.SOAP;

public class AppManager extends Observable implements IAppManager {
    public static final int ACTION_DELETE_FAILED = 4;
    public static final int ACTION_INSTALL_FAILED = 3;
    public static final int ACTION_PACKAGE_ADDED = 0;
    public static final int ACTION_PACKAGE_REMOVED = 1;
    public static final int ACTION_PACKAGE_REPLACED = 2;
    private static final String TAG = "AppManager";
    private static AppManager instance = null;
    private static Context mContext;
    private AppBroadcastReceiver mAppBroadcastReceiver;
    private AppDBUtil mAppDBUtil = null;
    private List<String> mBlackPkgList = null;
    private GameDBUtil mGameDBUtil = null;
    private QPackageManager mPackageManager;
    private QPackageParser mPackageParser;

    class C20611 implements Runnable {
        C20611() {
        }

        public void run() {
            Log.d(AppManager.TAG, "init appListInDB thread start");
            try {
                if (PropUtil.getProp(PropUtil.PROP_OTADEBUG).equals("develop")) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (AppManager.this.mGameDBUtil != null) {
                List<String> gamePkgNameList = AppManager.this.mGameDBUtil.getInstalledGameList();
                if (AppManager.this.mPackageManager != null) {
                    List<IAppInfo> installedAppList = AppManager.this.mPackageManager.getAllAppInfoList(AppManager.this.mBlackPkgList, -1);
                    if (installedAppList != null && installedAppList.size() > 0) {
                        for (IAppInfo info : installedAppList) {
                            AppInfo appInfo = (AppInfo) info;
                            AppInfo appInfoInDB;
                            if (!appInfo.isSystemApp()) {
                                if (gamePkgNameList != null) {
                                }
                                if (AppManager.this.mAppDBUtil != null) {
                                    appInfoInDB = AppManager.this.mAppDBUtil.findAppInfoByPkgName(appInfo.getPkgName());
                                    if (appInfoInDB != null) {
                                        appInfo.setAppInstalledTime(appInfoInDB.getAppInstalledTime());
                                        appInfo.setStatus(appInfoInDB.getStatus());
                                        appInfo.setRunningTime(appInfoInDB.getRunningTime());
                                        appInfo.setStartTime(appInfoInDB.getStartTime());
                                        if (AppManager.this.mAppDBUtil != null) {
                                            AppManager.this.mAppDBUtil.update(appInfo);
                                        } else {
                                            return;
                                        }
                                    }
                                    appInfo.setStatus(1);
                                    if (AppManager.this.mAppDBUtil != null) {
                                        AppManager.this.mAppDBUtil.insert(appInfo);
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            } else if (gamePkgNameList != null || gamePkgNameList.size() <= 0 || !gamePkgNameList.contains(appInfo.getPkgName())) {
                                if (AppManager.this.mAppDBUtil != null) {
                                    appInfoInDB = AppManager.this.mAppDBUtil.findAppInfoByPkgName(appInfo.getPkgName());
                                    if (appInfoInDB != null) {
                                        appInfo.setStatus(1);
                                        if (AppManager.this.mAppDBUtil != null) {
                                            AppManager.this.mAppDBUtil.insert(appInfo);
                                        } else {
                                            return;
                                        }
                                    }
                                    appInfo.setAppInstalledTime(appInfoInDB.getAppInstalledTime());
                                    appInfo.setStatus(appInfoInDB.getStatus());
                                    appInfo.setRunningTime(appInfoInDB.getRunningTime());
                                    appInfo.setStartTime(appInfoInDB.getStartTime());
                                    if (AppManager.this.mAppDBUtil != null) {
                                        AppManager.this.mAppDBUtil.update(appInfo);
                                    } else {
                                        return;
                                    }
                                }
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private class AppBroadcastReceiver extends BroadcastReceiver {
        private static final String TAG = "AppBroadcastReceiver";

        private AppBroadcastReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "mAppBroadcastReceiver ---onReceive() action = " + action);
            Bundle bundle = new Bundle();
            String pkgName;
            if (action.equals(QPackageManager.INSTALL_FAILED_ACTION)) {
                pkgName = intent.getStringExtra(SettingConstants.ACTION_TYPE_PACKAGE_NAME);
                int errorCode = intent.getIntExtra(SOAP.ERROR_CODE, -2);
                bundle.putInt("action", 3);
                bundle.putInt(SOAP.ERROR_CODE, errorCode);
            } else if (action.equals(QPackageManager.DELETE_FAILED_ACTION)) {
                pkgName = intent.getStringExtra(SettingConstants.ACTION_TYPE_PACKAGE_NAME);
                bundle.putInt("action", 4);
            } else {
                pkgName = getReceiverPackageName(intent.getDataString(), 8);
                Log.d(TAG, "onReceive---" + pkgName);
                bundle.putString(SettingConstants.ACTION_TYPE_PACKAGE_NAME, pkgName);
                if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                    bundle.putInt("action", 0);
                    boolean isInBlackPkgList = false;
                    if (AppManager.this.mBlackPkgList != null && AppManager.this.mBlackPkgList.size() > 0 && AppManager.this.mBlackPkgList.contains(pkgName)) {
                        isInBlackPkgList = true;
                    }
                    if (!(isInBlackPkgList || AppManager.this.mGameDBUtil.isGame(pkgName))) {
                        Log.d(TAG, "add appInfo---" + pkgName);
                        AppInfo appInfo = (AppInfo) AppManager.this.getOriginAppInfoByPkg(pkgName);
                        appInfo.setStatus(1);
                        AppManager.this.mAppDBUtil.insert(appInfo);
                    }
                } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                    bundle.putInt("action", 1);
                    boolean isReplaceing = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
                    bundle.putBoolean("android.intent.extra.REPLACING", isReplaceing);
                    if (!isReplaceing) {
                        AppManager.this.deleteInstalledRecord(pkgName);
                    }
                } else if ("android.intent.action.PACKAGE_REPLACED".equals(action)) {
                    bundle.putInt("action", 2);
                }
            }
            AppManager.this.setChanged();
            AppManager.this.notifyObservers(bundle);
        }

        private String getReceiverPackageName(String dataString, int start) {
            if (dataString != null && !dataString.isEmpty()) {
                return dataString.substring(start);
            }
            Log.d(TAG, "getReceiverPackageName() packageName is null");
            return null;
        }
    }

    public static synchronized IAppManager createAppManager(Context context) {
        IAppManager iAppManager;
        synchronized (AppManager.class) {
            if (instance == null) {
                mContext = context;
                instance = new AppManager();
                instance.init();
            }
            iAppManager = instance;
        }
        return iAppManager;
    }

    public static IAppManager getInstance() {
        return instance;
    }

    public void setBlackPkgList(List<String> blackPkgList) {
        Log.d(TAG, "setBlackPkgList");
        this.mBlackPkgList = blackPkgList;
        if (this.mBlackPkgList == null) {
            this.mBlackPkgList = new ArrayList();
        }
        this.mBlackPkgList.add(AppConstants.ANDROID_PKG_NAME);
        for (String blackPkgName : this.mBlackPkgList) {
            this.mAppDBUtil.delete(blackPkgName);
        }
    }

    private void init() {
        registerBroadCast();
        this.mBlackPkgList = new ArrayList();
        this.mBlackPkgList.add(AppConstants.ANDROID_PKG_NAME);
        this.mPackageManager = new QPackageManager(mContext);
        this.mPackageParser = new QPackageParser(mContext);
        this.mGameDBUtil = new GameDBUtil(mContext);
        this.mAppDBUtil = new AppDBUtil(mContext);
        initAppListInDB();
    }

    private void initAppListInDB() {
        new Thread(new C20611()).start();
    }

    public void release() {
        this.mPackageManager = null;
        this.mPackageParser = null;
        unregisterBroadCast();
        if (this.mAppDBUtil != null) {
            this.mAppDBUtil.release();
            this.mAppDBUtil = null;
        }
        mContext = null;
        instance = null;
    }

    public List<IAppInfo> getInstalledApps() {
        Log.d(TAG, "getInstalledApps");
        return this.mPackageManager.getAllAppInfoList(this.mBlackPkgList, -1);
    }

    public List<IAppInfo> getInstalledApps(int sortMethod) {
        return this.mPackageManager.getAllAppInfoList(this.mBlackPkgList, sortMethod);
    }

    public List<IAppInfo> getInstalledAppList(int sortMethod) {
        return this.mAppDBUtil.getAllRecord(sortMethod);
    }

    public List<IAppInfo> getRecentAppList() {
        return this.mAppDBUtil.getStartedApp();
    }

    public IAppInfo getAppInfoByPkg(String pkgName) {
        return this.mAppDBUtil.findAppInfoByPkgName(pkgName);
    }

    public IAppInfo getOriginAppInfoByPkg(String pkgName) {
        return this.mPackageManager.getAppInfoByPkgName(pkgName, true);
    }

    public IAppInfo getAppInfoByPath(String pkgPath) {
        Log.d(TAG, "getAppInfoByPath");
        AppInfo appInfo = this.mPackageParser.getAppInfoByPathInReflect(pkgPath);
        if (appInfo == null) {
            return null;
        }
        AppInfo installedAppInfo = this.mPackageManager.getAppInfoByPkgName(appInfo.getPkgName(), false);
        if (installedAppInfo == null) {
            return appInfo;
        }
        appInfo.setSystemApp(installedAppInfo.isSystemApp());
        appInfo.setAppInstalledTime(installedAppInfo.getAppInstalledTime());
        return appInfo;
    }

    public boolean startApp(String pkgName) {
        Log.d(TAG, "startApp " + pkgName);
        if (!this.mPackageManager.startApp(pkgName)) {
            return false;
        }
        this.mAppDBUtil.update(pkgName, 2, System.currentTimeMillis());
        return true;
    }

    public boolean startApp(String pkgName, boolean needPreview) {
        Log.d(TAG, "startApp " + pkgName + "needPreview" + needPreview);
        if (!this.mPackageManager.startApp(pkgName, needPreview)) {
            return false;
        }
        this.mAppDBUtil.update(pkgName, 2, System.currentTimeMillis());
        return true;
    }

    public boolean startApp(String pkgName, boolean needPreview, String args) {
        Log.d(TAG, "startAppWithArgs " + pkgName + "needPreview" + needPreview);
        if (!this.mPackageManager.startApp(pkgName, needPreview, args)) {
            return false;
        }
        this.mAppDBUtil.update(pkgName, 2, System.currentTimeMillis());
        return true;
    }

    public boolean installApp(String pkgPath) {
        return installApp(pkgPath, true);
    }

    public boolean installApp(String pkgPath, boolean isSilent) {
        Log.d(TAG, "installApp " + pkgPath);
        return this.mPackageManager.install(pkgPath, isSilent);
    }

    public boolean uninstallApp(String pkgName) {
        return uninstallApp(pkgName, true);
    }

    public boolean uninstallApp(String pkgName, boolean isSilent) {
        Log.d(TAG, "uninstallApp " + pkgName);
        return this.mPackageManager.uninstall(pkgName, isSilent);
    }

    private void registerBroadCast() {
        Log.d(TAG, "initBroadcastReceiver()");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addAction(QPackageManager.INSTALL_FAILED_ACTION);
        filter.addAction(QPackageManager.DELETE_FAILED_ACTION);
        filter.addDataScheme("package");
        this.mAppBroadcastReceiver = new AppBroadcastReceiver();
        try {
            mContext.registerReceiver(this.mAppBroadcastReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterBroadCast() {
        Log.d(TAG, "unregisterBroadCast");
        try {
            mContext.unregisterReceiver(this.mAppBroadcastReceiver);
            this.mAppBroadcastReceiver = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPkgSize(final String pkgName, final IGetAppSpaceListener listener) {
        Method method = null;
        try {
            method = PackageManager.class.getMethod("getPackageSizeInfo", new Class[]{String.class, IPackageStatsObserver.class});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        if (method != null) {
            try {
                method.invoke(mContext.getPackageManager(), new Object[]{pkgName, new Stub() {
                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                        if (succeeded && pStats != null) {
                            Long size = Long.valueOf(((((((pStats.cacheSize + pStats.codeSize) + pStats.dataSize) + pStats.externalCacheSize) + pStats.externalCodeSize) + pStats.externalDataSize) + pStats.externalMediaSize) + pStats.externalObbSize);
                            Log.d(AppManager.TAG, "pkgSize --- " + pkgName + " --- " + size);
                            if (listener != null) {
                                listener.OnGetSuccess(pkgName, size.longValue());
                            }
                        }
                    }
                }});
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
    }

    public StorageStatus getStorageStatus() {
        return StorageUtil.getStatus();
    }

    private void deleteInstalledRecord(String pkgName) {
        this.mAppDBUtil.delete(pkgName);
    }

    @Deprecated
    public long getRunningTimeByPkg(String pkgName) {
        return 0;
    }

    public boolean isAppRunningOnTop(String pkgName) {
        for (RunningTaskInfo info : ((ActivityManager) mContext.getSystemService("activity")).getRunningTasks(100)) {
            if (info.topActivity.getPackageName().equals(pkgName) && info.baseActivity.getPackageName().equals(pkgName)) {
                return true;
            }
        }
        return false;
    }
}
