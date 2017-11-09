package com.gitv.tvappstore.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.Process;
import android.os.StatFs;
import android.text.TextUtils;
import com.tvos.apps.utils.LogUtils;
import com.tvos.apps.utils.MD5Utils;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import org.cybergarage.soap.SOAP;

public final class SysUtils {
    private static final String[] ERROR_CODES = new String[]{"100", "201", "201", "202", "204", "315001", "201", "201", "201", "201", "315001", "315001", "315001", "315001", "315001", "315001", "100", "315002", "315001", "315001", "315002", "315001", "315001", "100", "315003", "315002", "315002", "201", "315003", "315003", "315003", "315003", "315003", "315003", "315003", "315003", "315003", "202"};
    public static String ERROR_CODE_SERVER = "server error";
    private static final String[] RESPONSE_CODES = new String[]{"E000000", "E000006", "E000007", "E000011", "E000012", "E000013", "E000020", "E000021", "E000022", "E000023", "E000025", "E000026", "E000027", "E000028", "E000029", "E000030", "E000031", "E000033", "E000034", "E000035", "E000036", "E000038", "E000039", "E000040", "E000041", "E000042", "E000044", "E000048", "E000057", "E000058", "E000059", "E000060", "E000061", "E000062", "E000063", "E000064", "E000065", "E000066"};
    public static String SUCCESS_CODE = "success";
    private static final String TAG = "SysUtils";
    public static String devCheckCode = null;
    private static String mApiKey = null;
    private static long mDeltaTime = 0;
    private static String mMacAddr;
    public static String msg_dev = null;

    public static void exitApp() {
        Process.killProcess(Process.myPid());
    }

    public static Long getSysTime() {
        return Long.valueOf(System.currentTimeMillis() + getDeltaTime());
    }

    public static boolean isApiKeyValid() {
        LogUtils.e(TAG, "ApiKey is " + mApiKey);
        if (StringUtils.isEmpty(mApiKey)) {
            return false;
        }
        return true;
    }

    public static void setApiKey(String apiKey) {
        if (!TextUtils.isEmpty(apiKey)) {
            mApiKey = apiKey;
            LogUtils.e(TAG, "setApiKey as " + apiKey);
        }
    }

    public static long getDeltaTime() {
        return mDeltaTime;
    }

    public static void setDeltaTime(long mDeltaTime) {
        mDeltaTime = mDeltaTime;
    }

    public static String getApiKey() {
        return mApiKey;
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
            macAddress = getWifiMAC(context);
        }
        mMacAddr = macAddress;
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
                LogUtils.e(TAG, e1.getMessage());
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

    public static String getWifiMAC(Context ctx) {
        try {
            WifiManager wifiMgr = (WifiManager) ctx.getSystemService("wifi");
            WifiInfo info = wifiMgr == null ? null : wifiMgr.getConnectionInfo();
            if (info != null) {
                return info.getMacAddress();
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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

    public static String genSessionid(Context context) {
        return new StringBuilder(String.valueOf(getFomatMacAddr(context) + (System.currentTimeMillis() / 1000))).append(new Random(System.currentTimeMillis()).nextInt(9999)).toString();
    }

    public static String getFirmver(Context ctx) {
        return VERSION.RELEASE;
    }

    public static String getTime(Long currentTimeMillis) {
        return new SimpleDateFormat("HH:mm").format(new Date(currentTimeMillis.longValue()));
    }

    public static String getErrorCode(String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }
        for (int i = 0; i < RESPONSE_CODES.length; i++) {
            if (key.equals(RESPONSE_CODES[i])) {
                return ERROR_CODES[i];
            }
        }
        return "";
    }

    public static boolean isAppForeground(Context context, String packageName) {
        if (context == null) {
            return false;
        }
        String packageItemName = ((RunningTaskInfo) ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1).get(0)).topActivity.getPackageName();
        if (TextUtils.isEmpty(packageItemName) || !packageItemName.equals(packageName)) {
            return false;
        }
        return true;
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
}
