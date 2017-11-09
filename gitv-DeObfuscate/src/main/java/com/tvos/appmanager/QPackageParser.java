package com.tvos.appmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.util.DisplayMetrics;
import android.util.Log;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.tvos.appmanager.model.AppInfo;
import com.tvos.appmanager.util.TimeUtil;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class QPackageParser {
    private static final String TAG = "QPackageParser";
    private Context mContext;

    public QPackageParser(Context context) {
        this.mContext = context;
    }

    public AppInfo getAppInfoByPath(String appFilePath) {
        Log.d(TAG, "parseAppInfoByPath:" + appFilePath);
        PackageManager pm = this.mContext.getPackageManager();
        PackageInfo pInfo = pm.getPackageArchiveInfo(appFilePath, 0);
        if (pInfo == null) {
            return null;
        }
        AppInfo appInfo = new AppInfo();
        appInfo.setAppName(pInfo.applicationInfo.loadLabel(pm).toString());
        appInfo.setAppIcon(pInfo.applicationInfo.loadIcon(pm));
        appInfo.setAppInstalledTime(TimeUtil.LongToString(pInfo.firstInstallTime));
        if (pInfo.applicationInfo.sourceDir == null) {
            appInfo.setAppPath(appFilePath);
        } else {
            appInfo.setAppPath(pInfo.applicationInfo.sourceDir);
        }
        File appFile = new File(appInfo.getAppPath());
        if (appFile.exists()) {
            appInfo.setAppSize(appFile.length());
        }
        appInfo.setAppVersion(pInfo.versionName);
        appInfo.setAppVersionCode(pInfo.versionCode);
        appInfo.setPkgName(pInfo.packageName);
        if ((pInfo.applicationInfo.flags & 1) != 0) {
            appInfo.setSystemApp(true);
            return appInfo;
        }
        appInfo.setSystemApp(false);
        return appInfo;
    }

    public AppInfo getAppInfoByPathInReflect(String appFilePath) {
        Object pkgParserPkg;
        Log.d(TAG, "getAppInfoByPathInReflect---" + appFilePath);
        AppInfo appInfo = new AppInfo();
        String PATH_PackageParser = "android.content.pm.PackageParser";
        String PATH_AssetManager = "android.content.res.AssetManager";
        new DisplayMetrics().setToDefaults();
        Class pkgParserClass = null;
        try {
            pkgParserClass = Class.forName(PATH_PackageParser);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class[] typeArgs = new Class[]{String.class};
        Object[] valueArgs = new Object[]{appFilePath};
        ApplicationInfo info = null;
        String appVersion = null;
        int appVersionCode = 0;
        String appPath = null;
        String pkgName = null;
        Field field = null;
        Field field2 = null;
        Field field3 = null;
        Field pkgNameField = null;
        Constructor pkgParserCt;
        Object pkgParser;
        Method pkgParser_parsePackageMtd;
        if (VERSION.SDK_INT >= 21) {
            try {
                pkgParserCt = pkgParserClass.getConstructor(new Class[0]);
                if (pkgParserCt == null) {
                    return null;
                }
                pkgParser = pkgParserCt.newInstance(new Object[0]);
                if (pkgParser == null) {
                    return null;
                }
                pkgParser_parsePackageMtd = pkgParserClass.getDeclaredMethod("parsePackage", new Class[]{File.class, Integer.TYPE});
                if (pkgParser_parsePackageMtd == null) {
                    return null;
                }
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, new Object[]{new File(appFilePath), Integer.valueOf(0)});
                if (pkgParserPkg == null) {
                    return null;
                }
            } catch (NoSuchMethodException e2) {
                e2.printStackTrace();
                return null;
            } catch (Exception e3) {
                e3.printStackTrace();
                return null;
            }
        }
        try {
            pkgParserCt = pkgParserClass.getConstructor(typeArgs);
            if (pkgParserCt == null) {
                return null;
            }
            pkgParser = pkgParserCt.newInstance(valueArgs);
            if (pkgParser == null) {
                return null;
            }
            pkgParser_parsePackageMtd = pkgParserClass.getDeclaredMethod("parsePackage", new Class[]{File.class, String.class, DisplayMetrics.class, Integer.TYPE});
            if (pkgParser_parsePackageMtd == null) {
                return null;
            }
            pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, new Object[]{new File(appFilePath), appFilePath, metrics, Integer.valueOf(0)});
            if (pkgParserPkg == null) {
                return null;
            }
            Field appPathField = pkgParserPkg.getClass().getDeclaredField("mPath");
            if (appFilePath != null) {
                appPath = (String) appPathField.get(pkgParserPkg);
            }
        } catch (NoSuchMethodException e22) {
            e22.printStackTrace();
            return null;
        } catch (Exception e32) {
            e32.printStackTrace();
            return null;
        }
        try {
            field = pkgParserPkg.getClass().getDeclaredField("applicationInfo");
            field2 = pkgParserPkg.getClass().getDeclaredField("mVersionName");
            field3 = pkgParserPkg.getClass().getDeclaredField("mVersionCode");
            pkgNameField = pkgParserPkg.getClass().getDeclaredField(SettingConstants.ACTION_TYPE_PACKAGE_NAME);
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        }
        if (field != null) {
            try {
                info = (ApplicationInfo) field.get(pkgParserPkg);
            } catch (Exception e322) {
                e322.printStackTrace();
            }
        }
        if (field3 != null) {
            appVersion = (String) field2.get(pkgParserPkg);
        }
        if (field3 != null) {
            appVersionCode = field3.getInt(pkgParserPkg);
        }
        if (pkgNameField != null) {
            pkgName = (String) pkgNameField.get(pkgParserPkg);
        }
        if (info != null) {
            Log.d(TAG, "Application isn't null");
            try {
                Class assetMagCls = Class.forName(PATH_AssetManager);
                if (assetMagCls != null) {
                    Constructor assetMagCt = assetMagCls.getConstructor(null);
                    if (assetMagCt != null) {
                        Object assetMag = assetMagCt.newInstance(null);
                        if (assetMag != null) {
                            Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", new Class[]{String.class});
                            valueArgs = new Object[]{appFilePath};
                            if (assetMag_addAssetPathMtd != null) {
                                assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);
                                Resources res = this.mContext.getResources();
                                Constructor resCt = Resources.class.getConstructor(new Class[]{assetMag.getClass(), DisplayMetrics.class, Configuration.class});
                                if (resCt != null) {
                                    Resources resObj = (Resources) resCt.newInstance(new Object[]{assetMag, res.getDisplayMetrics(), res.getConfiguration()});
                                    if (info.labelRes != 0) {
                                        appInfo.setAppName(resObj.getText(info.labelRes).toString());
                                    } else {
                                        CharSequence label = info.nonLocalizedLabel;
                                        if (label != null) {
                                            appInfo.setAppName(label.toString());
                                        }
                                    }
                                    appInfo.setAppIcon(resObj.getDrawable(info.icon));
                                }
                            }
                        }
                    }
                }
            } catch (Exception e3222) {
                e3222.printStackTrace();
            }
        }
        if (appPath != null) {
            appFilePath = appPath;
        }
        appInfo.setAppPath(appFilePath);
        File appFile = new File(appInfo.getAppPath());
        if (appFile.exists()) {
            appInfo.setAppSize(appFile.length());
        }
        appInfo.setAppVersion(appVersion);
        appInfo.setAppVersionCode(appVersionCode);
        appInfo.setPkgName(pkgName);
        if (info == null || (info.flags & 1) == 0) {
            appInfo.setSystemApp(false);
            return appInfo;
        }
        appInfo.setSystemApp(true);
        return appInfo;
    }
}
