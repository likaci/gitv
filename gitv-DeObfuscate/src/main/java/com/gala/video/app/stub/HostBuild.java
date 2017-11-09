package com.gala.video.app.stub;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;
import org.json.JSONObject;

public class HostBuild {
    private static final String TAG = "HostBuild";
    private static String sApkVersion = "";
    private static String sCustomer = "";
    private static String sCustomerPackage = "";
    private static String sDataVersion = "";
    private static String sPackage = "";
    private static String sPingback = "";
    private static String sStorePkgName = "";
    private static String sStyle = "";
    private static String sUUID = "";
    private static String sVersionCode = "";
    private static String sVersionName = "";

    public static void load(Context context) {
        loadVersion(context);
        loadConfig(context);
    }

    private static void loadConfig(Context context) {
        try {
            InputStream builtinManifestStream = context.getAssets().open("app.cfg");
            int builtinSize = builtinManifestStream.available();
            byte[] buffer = new byte[builtinSize];
            builtinManifestStream.read(buffer);
            builtinManifestStream.close();
            parser(new String(buffer, 0, builtinSize));
        } catch (IOException e) {
            Log.d(TAG, "load config exception = ", e);
        }
    }

    private static void loadVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 16384);
            sVersionName = info.versionName;
            sVersionCode = info.versionCode + "";
            Log.d(TAG, "versionCode = " + sVersionCode);
            Log.d(TAG, "versionName = " + sVersionName);
        } catch (Exception e) {
            Log.d(TAG, "package manager load version exception = " + e);
        }
    }

    private static void parser(String json) {
        try {
            JSONObject data = new JSONObject(json);
            sUUID = data.getString("UUID");
            sApkVersion = data.getString(BuildConstance.APK_VERSION);
            sPackage = data.getString(BuildConstance.APK_PACKAGE_NAME);
            sPingback = data.getString(BuildConstance.APK_PINGBACK_P2);
            sStyle = data.getString(BuildConstance.APK_UI_STYLE);
            sCustomer = data.getString(BuildConstance.APK_CUSTOMER);
            sDataVersion = data.getString(BuildConstance.DATA_VERSION);
            sCustomerPackage = data.getString(BuildConstance.APK_CUSTOMER_PACKAGES);
            sStorePkgName = data.getString(BuildConstance.APP_STORE_PKG_NAME);
        } catch (JSONException e) {
            Log.d(TAG, "parse app cfg exception = ", e);
        }
    }

    public static String getCustomerPackage() {
        return sCustomerPackage;
    }

    public static String getStorePkgName() {
        return sStorePkgName;
    }

    public static String getUUID() {
        return sUUID;
    }

    public static String getPackageName() {
        return sPackage;
    }

    public static String getPingback() {
        return sPingback;
    }

    public static String getUIStyle() {
        return sStyle;
    }

    public static String getCustomer() {
        return sCustomer;
    }

    public static String getVersionName() {
        return sVersionName;
    }

    public static String getVersionCode() {
        return sVersionCode;
    }

    public static String getApkVersion() {
        return sApkVersion;
    }

    public static String getDataVersion() {
        return sDataVersion;
    }

    public static String getHostVersion() {
        return sVersionName + "." + sApkVersion + "." + sVersionCode;
    }
}
