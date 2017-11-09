package com.tvos.apps.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.PowerManager;
import android.os.Process;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import org.cybergarage.soap.SOAP;

public final class SysUtils {
    private static final int FLAG_IS_GAME = 33554432;
    private static final String TAG = "SysUtils";
    private static String mMacAddr;
    private static long systemReserveSpaceSize = 0;

    public static void setSystemReserveSpaceSize(long size) {
        systemReserveSpaceSize = size;
    }

    public static void exitApp() {
        Process.killProcess(Process.myPid());
    }

    public static Long getSysTime() {
        return Long.valueOf(System.currentTimeMillis());
    }

    public static String getFomatMacAddr(Context context) {
        return getMacAddr(context).replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", "");
    }

    public static String getMd5FormatMacAddr(Context context) {
        return MD5Utils.MD5(getFomatMacAddr(context).toUpperCase());
    }

    public static String getMacAddr(Context context) {
        if (!StringUtils.isEmpty(mMacAddr)) {
            return mMacAddr;
        }
        String macAddress = getEthMAC();
        if (StringUtils.isEmpty(macAddress)) {
            macAddress = getWifiMac(context);
        }
        mMacAddr = macAddress;
        LogUtils.m1737d(TAG, "get mac address : " + macAddress);
        return macAddress;
    }

    public static String getWirelessIpAddress(Context context) {
        int ipAddr = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress();
        StringBuffer ipBuf = new StringBuffer();
        ipAddr >>>= 8;
        ipAddr >>>= 8;
        ipBuf.append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append((ipAddr >>> 8) & 255);
        return ipBuf.toString();
    }

    public static String getEthMAC() {
        Exception e1;
        Throwable th;
        Reader reader = null;
        StringBuffer sbuffer = new StringBuffer();
        try {
            char[] buffer = new char[20];
            Reader reader2 = new InputStreamReader(new FileInputStream("/sys/class/net/eth0/address"));
            while (true) {
                try {
                    int charread = reader2.read(buffer);
                    if (charread == -1) {
                        break;
                    } else if (charread != buffer.length || buffer[buffer.length - 1] == '\r') {
                        for (int i = 0; i < charread; i++) {
                            if (buffer[i] != '\r') {
                                sbuffer.append(buffer[i]);
                            }
                        }
                    }
                } catch (Exception e) {
                    e1 = e;
                    reader = reader2;
                } catch (Throwable th2) {
                    th = th2;
                    reader = reader2;
                }
            }
            if (reader2 != null) {
                try {
                    reader2.close();
                    reader = reader2;
                } catch (Exception e2) {
                    reader = reader2;
                }
            }
        } catch (Exception e3) {
            e1 = e3;
            try {
                LogUtils.m1738e(TAG, e1.getMessage());
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e4) {
                    }
                }
                return sbuffer.toString().trim();
            } catch (Throwable th3) {
                th = th3;
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e5) {
                    }
                }
                throw th;
            }
        }
        return sbuffer.toString().trim();
    }

    public static String getWifiMac(Context context) {
        String wifiMacAddress;
        if (VERSION.SDK_INT < 23) {
            wifiMacAddress = getWifiMacByAPI(context);
        } else {
            wifiMacAddress = getWifiMacByReflection();
        }
        if (!TextUtils.isEmpty(wifiMacAddress)) {
            wifiMacAddress = wifiMacAddress.toUpperCase();
        }
        Log.i(TAG, "get wifi mac address is " + wifiMacAddress);
        return wifiMacAddress;
    }

    private static String getWifiMacByAPI(Context context) {
        String wifiMacAddress = null;
        try {
            WifiManager wifiMgr = (WifiManager) context.getSystemService("wifi");
            if (wifiMgr != null) {
                WifiInfo info = wifiMgr.getConnectionInfo();
                if (info != null) {
                    wifiMacAddress = info.getMacAddress();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wifiMacAddress;
    }

    private static String getWifiMacByReflection() {
        String wifiMacAddress = null;
        try {
            String interfaceName = "wlan0";
            Method getMethod = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class, String.class});
            if (getMethod != null) {
                interfaceName = (String) getMethod.invoke(null, new Object[]{"wifi.interface", interfaceName});
            }
            for (NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                if (intf.getName().equalsIgnoreCase(interfaceName)) {
                    byte[] mac = intf.getHardwareAddress();
                    if (!(mac == null || mac.length == 0)) {
                        wifiMacAddress = analyseMacAddress(mac);
                    }
                    return wifiMacAddress;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return wifiMacAddress;
    }

    private static String analyseMacAddress(byte[] mac) {
        if (mac == null || mac.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        int length = mac.length;
        for (int i = 0; i < length; i++) {
            buf.append(String.format("%02X:", new Object[]{Byte.valueOf(mac[i])}));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    public static String getFirmver(Context ctx) {
        return VERSION.RELEASE;
    }

    public static String getTime(Long currentTimeMillis) {
        return new SimpleDateFormat("HH:mm").format(new Date(currentTimeMillis.longValue()));
    }

    public static boolean isAppForeground(Context context, String packageName) {
        if (context == null || packageName == null) {
            return false;
        }
        String topAppPkgName = getTopAppPkgName(context);
        if (topAppPkgName != null) {
            return topAppPkgName.equals(packageName);
        }
        return false;
    }

    public static String getDNS() {
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            Properties properties = new Properties();
            properties.load(process.getInputStream());
            String dns = properties.getProperty("[net.dns1]", "");
            if (!StringUtils.isEmpty(dns) && dns.length() > 6) {
                return dns.substring(1, dns.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean hasEnoughFreeSpace(long needSpace) {
        long freeSpace = getFreeSpace();
        Log.d(TAG, "needSpace : " + needSpace + "         freeSpace :" + freeSpace);
        if (freeSpace > needSpace) {
            return true;
        }
        return false;
    }

    public static long getFreeSpace() {
        if (!checkSDCard()) {
            return 0;
        }
        StatFs statFs = new StatFs(getExternalStoragePath());
        long actualAvailableSpace = (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) - systemReserveSpaceSize;
        if (actualAvailableSpace < 0) {
            actualAvailableSpace = 0;
        }
        LogUtil.m1727i("actualAvailableSpace " + actualAvailableSpace);
        return actualAvailableSpace;
    }

    private static long getFreeSpaceByPath(String path) {
        StatFs statFs = new StatFs(path);
        long actualAvailableSpace = (((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize())) - systemReserveSpaceSize;
        if (actualAvailableSpace < 0) {
            actualAvailableSpace = 0;
        }
        LogUtil.m1727i("actualAvailableSpace " + actualAvailableSpace);
        return actualAvailableSpace;
    }

    public static long getFreeSpace(String destPath) {
        if (destPath != null) {
            LogUtil.m1727i("destPath " + destPath);
            String externalStoragePath = getExternalStoragePath();
            if (externalStoragePath != null && (destPath.startsWith(externalStoragePath) || destPath.startsWith("/sdcard/"))) {
                return getFreeSpace();
            }
            if (!false && destPath.startsWith(getDataStoragePath())) {
                return getFreeSpaceByPath(getDataStoragePath());
            }
        }
        return 0;
    }

    public static boolean checkSDCard() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            return true;
        }
        return false;
    }

    public static String getExternalStoragePath() {
        if ("mounted".equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return null;
    }

    public static String getDataStoragePath() {
        return Environment.getDataDirectory().getPath();
    }

    public static boolean checkNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivity == null) {
            return false;
        }
        NetworkInfo[] info = connectivity.getAllNetworkInfo();
        if (info == null) {
            return false;
        }
        for (NetworkInfo state : info) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public static int getApkVersion(String pkgName, Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(pkgName, 8192).versionCode;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static String getApkVersionName(String pkgName, Context context) {
        if (context != null) {
            try {
                return context.getPackageManager().getPackageInfo(pkgName, 8192).versionName;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static long getDataSpareQuantity() {
        StatFs sf = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        return (((long) sf.getBlockSize()) * ((long) sf.getAvailableBlocks())) - 2097152;
    }

    public static long getSDCardSpareQuantity() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return -1;
        }
        StatFs sf = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return (((long) sf.getBlockSize()) * ((long) sf.getAvailableBlocks())) - 2097152;
    }

    public static boolean isScreenOn(Context context) {
        boolean screenOn = false;
        PowerManager pm = (PowerManager) context.getSystemService("power");
        if (pm != null) {
            screenOn = pm.isInteractive();
        }
        Log.i(TAG, "isScreenOn, " + screenOn);
        return screenOn;
    }

    public static boolean hasGameForeground(Context context) {
        boolean hasGame = isGame(getTopAppPkgName(context), context);
        Log.d(TAG, "hasGameForeground " + hasGame);
        return hasGame;
    }

    @SuppressLint({"InlinedApi"})
    public static boolean isGame(String packageName, Context context) {
        if (packageName == null) {
            return false;
        }
        packageName = packageName.trim();
        if (packageName.equals("")) {
            return false;
        }
        boolean res = false;
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, 128);
            if (appInfo == null) {
                return false;
            }
            if (appInfo.metaData != null) {
                res = appInfo.metaData.getBoolean("isGame");
            }
            if (res) {
                return true;
            }
            if ((appInfo.flags & FLAG_IS_GAME) != 0) {
                return true;
            }
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getTopAppPkgName(Context context) {
        String topPackageName = "";
        if (VERSION.SDK_INT >= 21) {
            List<UsageStats> stats = ((UsageStatsManager) context.getSystemService("usagestats")).queryUsageStats(0, 0, System.currentTimeMillis());
            if (stats != null) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(Long.valueOf(usageStats.getLastTimeUsed()), usageStats);
                }
                if (!(mySortedMap == null || mySortedMap.isEmpty())) {
                    topPackageName = ((UsageStats) mySortedMap.get(mySortedMap.lastKey())).getPackageName();
                }
            }
        } else {
            topPackageName = ((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
        }
        Log.d(TAG, "getTopAppPkgName = " + topPackageName + " , Build.VERSION.SDK_INT = " + VERSION.SDK_INT);
        return topPackageName;
    }

    public static void forceStopApp(Context context, String pkgName) {
        ActivityManager am = (ActivityManager) context.getSystemService("activity");
        try {
            Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", new Class[]{String.class}).invoke(am, new Object[]{pkgName});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isAppRunning(Context context, String pkgName) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(pkgName, 0);
            Log.d(TAG, "isAppRunning, info = " + info);
            if (info == null) {
                return false;
            }
            Log.d(TAG, "isAppRunning, info.flags = " + info.flags);
            if ((info.flags & 2097152) == 0) {
                return true;
            }
            return false;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
