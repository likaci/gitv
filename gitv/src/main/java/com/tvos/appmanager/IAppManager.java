package com.tvos.appmanager;

import com.tvos.appmanager.model.IAppInfo;
import com.tvos.appmanager.model.StorageStatus;
import java.util.List;
import java.util.Observer;

public interface IAppManager {
    void addObserver(Observer observer);

    void deleteObserver(Observer observer);

    IAppInfo getAppInfoByPath(String str);

    IAppInfo getAppInfoByPkg(String str);

    List<IAppInfo> getInstalledAppList(int i);

    List<IAppInfo> getInstalledApps();

    List<IAppInfo> getInstalledApps(int i);

    IAppInfo getOriginAppInfoByPkg(String str);

    void getPkgSize(String str, IGetAppSpaceListener iGetAppSpaceListener);

    List<IAppInfo> getRecentAppList();

    long getRunningTimeByPkg(String str);

    StorageStatus getStorageStatus();

    boolean installApp(String str);

    boolean installApp(String str, boolean z);

    boolean isAppRunningOnTop(String str);

    void release();

    void setBlackPkgList(List<String> list);

    boolean startApp(String str);

    boolean startApp(String str, boolean z);

    boolean startApp(String str, boolean z, String str2);

    boolean uninstallApp(String str);

    boolean uninstallApp(String str, boolean z);
}
