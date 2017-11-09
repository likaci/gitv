package com.tvos.appmanager;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.IPackageInstallObserver.Stub;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.push.mqttv3.internal.ClientDefaults;
import com.tvos.appmanager.model.AppInfo;
import com.tvos.appmanager.model.IAppInfo;
import com.tvos.appmanager.sort.AppSortUtil;
import com.tvos.appmanager.util.RunCommandUtil;
import com.tvos.appmanager.util.TimeUtil;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class QPackageManager {
    public static final int DELETE_ALL_USERS = 2;
    public static final String DELETE_FAILED_ACTION = "android.intent.action.package_delete_failed";
    public static final int DELETE_KEEP_DATA = 1;
    public static final int DELETE_SYSTEM_APP = 4;
    private static final int FLAG_ACTIVITY_SUPPORT_PREVIEW = 8192;
    private static final int FLAG_ISSYSTEMAPPNEEDED_NO = 0;
    private static final int FLAG_ISSYSTEMAPPNEEDED_YES = 1;
    public static final int INSTALL_ALLOW_TEST = 4;
    public static final String INSTALL_FAILED_ACTION = "android.intent.action.package_install_failed";
    public static final int INSTALL_FORWARD_LOCK = 1;
    public static final int INSTALL_REPLACE_EXISTING = 2;
    private static final String TAG = "QPackageManager";
    private Context mContext;

    public QPackageManager(Context context) {
        this.mContext = context;
    }

    public List<IAppInfo> getUserAppInfoList(List<String> blackPkgNameList, int sortMethod) {
        return getInstalledAppInfoList(0, blackPkgNameList, sortMethod, true);
    }

    public List<IAppInfo> getSystemAppInfoList(List<String> blackPkgNameList, int sortMethod) {
        return getInstalledAppInfoList(1, blackPkgNameList, sortMethod, true);
    }

    public List<IAppInfo> getAllAppInfoList(List<String> filterPkgNameList, int sortMethod) {
        return getInstalledAppInfoList(-1, filterPkgNameList, sortMethod, true);
    }

    public List<String> getSystemPkgNameList() {
        List<String> pkgNameList = new ArrayList();
        for (PackageInfo pInfo : this.mContext.getPackageManager().getInstalledPackages(8192)) {
            if ((pInfo.applicationInfo.flags & 1) != 0) {
                pkgNameList.add(pInfo.packageName);
            }
        }
        return pkgNameList;
    }

    public AppInfo getAppInfoByPkgName(String pkgName, boolean isNeedIcon) {
        PackageManager pm = this.mContext.getPackageManager();
        AppInfo appInfo = null;
        try {
            PackageInfo pkgInfo = pm.getPackageInfo(pkgName, 8192);
            if (pkgInfo == null) {
                return null;
            }
            AppInfo appInfo2 = new AppInfo();
            try {
                appInfo2.setAppName(pkgInfo.applicationInfo.loadLabel(pm).toString());
                try {
                    appInfo2.setAppName_py(appInfo2.getAppName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (isNeedIcon) {
                    appInfo2.setAppIcon(pkgInfo.applicationInfo.loadIcon(pm));
                }
                appInfo2.setAppInstalledTime(TimeUtil.LongToString(pkgInfo.firstInstallTime));
                appInfo2.setAppPath(pkgInfo.applicationInfo.sourceDir);
                File file = new File(appInfo2.getAppPath());
                if (file.exists()) {
                    appInfo2.setAppSize(file.length());
                }
                appInfo2.setAppVersion(pkgInfo.versionName);
                appInfo2.setAppVersionCode(pkgInfo.versionCode);
                appInfo2.setPkgName(pkgInfo.packageName);
                if ((pkgInfo.applicationInfo.flags & 1) != 0) {
                    appInfo2.setSystemApp(true);
                    appInfo = appInfo2;
                } else {
                    appInfo2.setSystemApp(false);
                    appInfo = appInfo2;
                }
            } catch (NameNotFoundException e2) {
                appInfo = appInfo2;
                Log.d(TAG, "package " + pkgName + "not found");
                return appInfo;
            }
            return appInfo;
        } catch (NameNotFoundException e3) {
            Log.d(TAG, "package " + pkgName + "not found");
            return appInfo;
        }
    }

    public List<IAppInfo> getInstalledAppInfoList(int isSystemAppNeeded, List<String> pkgNameList, int sortMethod, boolean needLoadIcon) {
        boolean needFilter = true;
        if (pkgNameList == null || pkgNameList.isEmpty()) {
            needFilter = false;
        }
        PackageManager pm = this.mContext.getPackageManager();
        List<PackageInfo> pInfos = pm.getInstalledPackages(8192);
        List<IAppInfo> appInfos = new ArrayList();
        for (PackageInfo pInfo : pInfos) {
            if (canLaunchPkg(pInfo.packageName)) {
                if (pInfo.packageName != null && needFilter) {
                    boolean shouldContinue = false;
                    for (String pkgName : pkgNameList) {
                        if (pInfo.packageName.startsWith(pkgName) && !pInfo.packageName.equals(AppConstants.ANDROID_SETTINGS_NAME)) {
                            shouldContinue = true;
                            break;
                        }
                    }
                    if (shouldContinue) {
                    }
                }
                AppInfo appInfo = new AppInfo();
                appInfo.setAppName(pInfo.applicationInfo.loadLabel(pm).toString());
                try {
                    appInfo.setAppName_py(appInfo.getAppName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (needLoadIcon) {
                    appInfo.setAppIcon(pInfo.applicationInfo.loadIcon(pm));
                }
                appInfo.setAppInstalledTime(TimeUtil.LongToString(pInfo.firstInstallTime));
                appInfo.setAppPath(pInfo.applicationInfo.sourceDir);
                appInfo.setAppSize((long) Integer.valueOf((int) new File(appInfo.getAppPath()).length()).intValue());
                appInfo.setAppVersion(pInfo.versionName);
                appInfo.setAppVersionCode(pInfo.versionCode);
                appInfo.setPkgName(pInfo.packageName);
                if ((pInfo.applicationInfo.flags & 1) != 0) {
                    appInfo.setSystemApp(true);
                    if (isSystemAppNeeded != 0) {
                        appInfos.add(appInfo);
                    }
                } else {
                    appInfo.setSystemApp(false);
                    if (isSystemAppNeeded != 1) {
                        appInfos.add(appInfo);
                    }
                }
            }
        }
        switch (sortMethod) {
            case 1:
                return AppSortUtil.sortAppListByInstallTime(appInfos);
            default:
                return appInfos;
        }
    }

    public boolean startApp(String pkgName) {
        try {
            Intent launchIntent = getLauncherIntent(pkgName);
            if (launchIntent == null) {
                return false;
            }
            this.mContext.startActivity(launchIntent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public Intent getLauncherIntent(String pkgName) {
        Exception e;
        Intent launchIntent = null;
        try {
            Intent intent = new Intent();
            intent.setPackage(pkgName);
            intent.addCategory("android.intent.category.LEANBACK_LAUNCHER");
            intent.setAction("android.intent.action.MAIN");
            List<ResolveInfo> resolveInfos = this.mContext.getPackageManager().queryIntentActivities(intent, 64);
            if (resolveInfos == null || resolveInfos.size() == 0) {
                launchIntent = this.mContext.getPackageManager().getLaunchIntentForPackage(pkgName);
                if (launchIntent != null) {
                    launchIntent.addFlags(ClientDefaults.MAX_MSG_SIZE);
                }
                return launchIntent;
            }
            ComponentName componentName = new ComponentName(pkgName, ((ResolveInfo) resolveInfos.get(0)).activityInfo.name);
            Intent launchIntent2 = new Intent();
            try {
                launchIntent2.setComponent(componentName);
                launchIntent = launchIntent2;
            } catch (Exception e2) {
                e = e2;
                launchIntent = launchIntent2;
                e.printStackTrace();
                if (launchIntent != null) {
                    launchIntent.addFlags(ClientDefaults.MAX_MSG_SIZE);
                }
                return launchIntent;
            }
            if (launchIntent != null) {
                launchIntent.addFlags(ClientDefaults.MAX_MSG_SIZE);
            }
            return launchIntent;
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            if (launchIntent != null) {
                launchIntent.addFlags(ClientDefaults.MAX_MSG_SIZE);
            }
            return launchIntent;
        }
    }

    public boolean startApp(String pkgName, boolean needPreview) {
        try {
            Intent launchIntent = getLauncherIntent(pkgName);
            if (launchIntent == null) {
                return false;
            }
            if (needPreview) {
                launchIntent.addFlags(8192);
            }
            this.mContext.startActivity(launchIntent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean startApp(String pkgName, boolean needPreview, String args) {
        try {
            Intent launchIntent = getLauncherIntent(pkgName);
            if (launchIntent == null) {
                return false;
            }
            launchIntent.putExtra("data", args);
            if (needPreview) {
                launchIntent.addFlags(8192);
            }
            this.mContext.startActivity(launchIntent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public boolean install(String path) {
        if (!silentInstall(path)) {
            normalIntsall(path);
        }
        return true;
    }

    public boolean install(String path, boolean isSilent) {
        if (!isSilent) {
            return normalIntsall(path);
        }
        if (silentInstall(path)) {
            return true;
        }
        return normalIntsall(path);
    }

    public boolean silentInstall(final String path) {
        try {
            if (RunCommandUtil.runCommand("chmod 775 " + path + "\n")) {
                PackageManager pm = this.mContext.getPackageManager();
                Method installPackageMethod = getMethod(PackageManager.class, "installPackage", new Class[]{Uri.class, IPackageInstallObserver.class, Integer.TYPE, String.class});
                IPackageInstallObserver observer = new Stub() {
                    public void packageInstalled(String packageName, int returnCode) throws RemoteException {
                        if (returnCode != 1) {
                            if (packageName == null) {
                                AppInfo info = new QPackageParser(QPackageManager.this.mContext).getAppInfoByPathInReflect(path);
                                if (info != null) {
                                    packageName = info.getPkgName();
                                }
                            }
                            if (packageName != null) {
                                Log.d(QPackageManager.TAG, "install " + packageName + SOAP.DELIM + returnCode);
                                Intent intent = new Intent(QPackageManager.INSTALL_FAILED_ACTION);
                                intent.putExtra(SettingConstants.ACTION_TYPE_PACKAGE_NAME, packageName);
                                intent.putExtra(SOAP.ERROR_CODE, returnCode);
                                UserHandle userHandle = QPackageManager.this.getUserHandle_ALL();
                                if (userHandle != null) {
                                    QPackageManager.this.mContext.sendBroadcastAsUser(intent, userHandle);
                                }
                            }
                        }
                    }
                };
                installPackageMethod.invoke(pm, new Object[]{getPackageUri(path), observer, Integer.valueOf(2), null});
                return true;
            }
            Log.d(TAG, "no permission");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean normalIntsall(String path) {
        try {
            Intent intent = new Intent();
            intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
            intent.setAction("android.intent.action.VIEW");
            intent.setDataAndType(getPackageUri(path), "application/vnd.android.package-archive");
            this.mContext.startActivity(intent);
            return true;
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean uninstall(String pkgName) {
        if (silentUninstall(pkgName)) {
            return true;
        }
        return normalUninstall(pkgName);
    }

    public boolean uninstall(String pkgName, boolean isSilent) {
        if (!isSilent) {
            return normalUninstall(pkgName);
        }
        if (silentUninstall(pkgName)) {
            return true;
        }
        return normalUninstall(pkgName);
    }

    public boolean normalUninstall(String pkgName) {
        try {
            Intent intent = new Intent("android.intent.action.DELETE", Uri.parse("package:" + pkgName));
            intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
            this.mContext.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean silentUninstall(String packageName) {
        try {
            Log.d(TAG, "slient uninstall :" + packageName);
            PackageManager pm = this.mContext.getPackageManager();
            Method installPackageMethod = getMethod(PackageManager.class, "deletePackage", new Class[]{String.class, IPackageDeleteObserver.class, Integer.TYPE});
            IPackageDeleteObserver observer = new IPackageDeleteObserver.Stub() {
                public void packageDeleted(String packageName, int returnCode) throws RemoteException {
                    Log.d(QPackageManager.TAG, "uninstall" + packageName + SOAP.DELIM + returnCode);
                    if (returnCode != 1) {
                        Intent intent = new Intent(QPackageManager.DELETE_FAILED_ACTION);
                        intent.putExtra(SettingConstants.ACTION_TYPE_PACKAGE_NAME, packageName);
                        intent.putExtra(SOAP.ERROR_CODE, returnCode);
                        UserHandle userHandle = QPackageManager.this.getUserHandle_ALL();
                        if (userHandle != null) {
                            QPackageManager.this.mContext.sendBroadcastAsUser(intent, userHandle);
                        }
                    }
                }
            };
            installPackageMethod.invoke(pm, new Object[]{packageName, observer, Integer.valueOf(2)});
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private UserHandle getUserHandle_ALL() {
        try {
            Object object = UserHandle.class.getField("ALL").get(null);
            if (object != null) {
                return (UserHandle) object;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (IllegalArgumentException e3) {
            e3.printStackTrace();
        }
        return null;
    }

    private Method getMethod(Class<?> cls, String methodName) {
        Method method = null;
        try {
            Method[] methods = cls.getMethods();
            for (int i = 0; i < methods.length; i++) {
                if (methods[i].getName().equals(methodName)) {
                    method = cls.getMethod(methodName, methods[i].getParameterTypes());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return method;
    }

    private Method getMethod(Class<?> cls, String methodName, Class<?>[] types) {
        try {
            return cls.getMethod(methodName, types);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Uri getPackageUri(String path) {
        return Uri.fromFile(new File(path));
    }

    private boolean canLaunchPkg(String pkgName) {
        if (this.mContext.getPackageManager().getLaunchIntentForPackage(pkgName) != null) {
            return true;
        }
        return false;
    }
}
