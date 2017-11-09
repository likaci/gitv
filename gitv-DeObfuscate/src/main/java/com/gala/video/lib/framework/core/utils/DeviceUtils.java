package com.gala.video.lib.framework.core.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Instrumentation;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug.MemoryInfo;
import android.os.Environment;
import android.os.StatFs;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.tvos.apps.utils.DateUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;

@TargetApi(16)
public final class DeviceUtils {
    private static final String CPU_INFO_PATH = "/proc/cpuinfo";
    private static final String TAG = "SysUtils";
    private static int cpuCores = 0;
    private static String mMacAddr;
    private static int memTotal = 0;
    private static long sElapsedRealTimeMillis = 0;
    private static long sServerTimeMillis = 0;

    static class C16092 implements FileFilter {
        C16092() {
        }

        public boolean accept(File pathname) {
            return Pattern.matches("cpu[0-9]", pathname.getName());
        }
    }

    public enum MacTypeEnum {
        MAC_DEFAULT,
        MAC_ETH,
        MAC_WIFI
    }

    public static String getCpuInfo() {
        Exception e;
        Throwable th;
        StringBuilder info = new StringBuilder();
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            FileReader fr2 = new FileReader(CPU_INFO_PATH);
            try {
                String line;
                BufferedReader localBufferedReader2 = new BufferedReader(fr2, 8192);
                do {
                    try {
                        line = localBufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        line = line.toLowerCase(Locale.ENGLISH);
                    } catch (Exception e2) {
                        e = e2;
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    } catch (Throwable th2) {
                        th = th2;
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    }
                } while (!line.contains("hardware"));
                info.append(line);
                if (localBufferedReader2 != null) {
                    try {
                        localBufferedReader2.close();
                    } catch (IOException e3) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e3);
                        }
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    }
                }
                if (fr2 != null) {
                    fr2.close();
                }
                localBufferedReader = localBufferedReader2;
                fr = fr2;
            } catch (Exception e4) {
                e = e4;
                fr = fr2;
                try {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e);
                    }
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (IOException e32) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e32);
                            }
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "getCpuInfo: info=" + info);
                    }
                    return info.toString();
                } catch (Throwable th3) {
                    th = th3;
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (IOException e322) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e322);
                            }
                            throw th;
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                fr = fr2;
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            if (LogUtils.mIsDebug) {
                LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e);
            }
            if (localBufferedReader != null) {
                localBufferedReader.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "getCpuInfo: info=" + info);
            }
            return info.toString();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getCpuInfo: info=" + info);
        }
        return info.toString();
    }

    public static String getFormattedMacAddr() {
        return getMacAddr().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", "");
    }

    public static String getMd5FormatMacAddr() {
        return MD5Util.MD5(getFormattedMacAddr().toUpperCase());
    }

    public static String getMacAddr(MacTypeEnum macTypeEnum) {
        if (macTypeEnum == MacTypeEnum.MAC_DEFAULT && !StringUtils.isEmpty(mMacAddr) && !mMacAddr.equals("00:00:00:00:00:00")) {
            return mMacAddr;
        }
        String macAddress = "";
        switch (macTypeEnum) {
            case MAC_DEFAULT:
                if (StringUtils.isEmpty(mMacAddr) || mMacAddr.equals("00:00:00:00:00:00")) {
                    mMacAddr = getEthMAC();
                    Log.v(TAG, "getEthMAC address = " + mMacAddr);
                    if (StringUtils.isEmpty(mMacAddr) || mMacAddr.equals("00:00:00:00:00:00")) {
                        mMacAddr = getWifiMAC(AppRuntimeEnv.get().getApplicationContext());
                        Log.v(TAG, "getWifiMAC address = " + mMacAddr);
                    }
                    LogUtils.m1568d(TAG, "MAC_DEFAULT get mac address : " + mMacAddr);
                    if (mMacAddr == null) {
                        mMacAddr = "";
                        break;
                    }
                }
                return mMacAddr;
                break;
            case MAC_ETH:
                macAddress = getEthMAC();
                if (!StringUtils.isEmpty((CharSequence) macAddress)) {
                    macAddress = macAddress.toLowerCase();
                }
                LogUtils.m1568d(TAG, "MAC_ETH get mac address : " + macAddress);
                return macAddress;
            case MAC_WIFI:
                macAddress = getWifiMAC(AppRuntimeEnv.get().getApplicationContext());
                if (!StringUtils.isEmpty((CharSequence) macAddress)) {
                    macAddress = macAddress.toLowerCase();
                }
                LogUtils.m1568d(TAG, "MAC_WIFI get mac address : " + macAddress);
                return macAddress;
        }
        LogUtils.m1568d(TAG, "getMacAddr method end : " + mMacAddr);
        return mMacAddr.toLowerCase();
    }

    public static String getMacAddr() {
        return getMacAddr(MacTypeEnum.MAC_DEFAULT);
    }

    public static String getWirelessIpAddress(Context context) {
        int ipAddr = ((WifiManager) context.getSystemService("wifi")).getConnectionInfo().getIpAddress();
        StringBuffer ipBuf = new StringBuffer();
        ipAddr >>>= 8;
        ipAddr >>>= 8;
        ipBuf.append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append(ipAddr & 255).append('.').append((ipAddr >>> 8) & 255);
        return ipBuf.toString();
    }

    public static String getIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) en.nextElement();
                if (netInterface.getName().toLowerCase().equals("eth0") || netInterface.getName().toLowerCase().equals("wlan0")) {
                    Enumeration<InetAddress> enumIpAddr = netInterface.getInetAddresses();
                    while (enumIpAddr.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::")) {
                                return ipaddress;
                            }
                        }
                    }
                    continue;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
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
                LogUtils.m1571e(TAG, e1.getMessage());
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
        String wifiMac = null;
        try {
            WifiManager wifiMgr = (WifiManager) ctx.getSystemService("wifi");
            WifiInfo info = wifiMgr == null ? null : wifiMgr.getConnectionInfo();
            if (info != null) {
                wifiMac = info.getMacAddress();
            }
            Log.v(TAG, "getWifiMAC wifiMac = " + wifiMac);
            if (VERSION.SDK_INT < 23) {
                return wifiMac;
            }
            if ("02:00:00:00:00:00".equals(wifiMac) || "00:00:00:00:00:00".equals(wifiMac)) {
                return getAndroidMWifiMAC();
            }
            return wifiMac;
        } catch (Exception e) {
            e.printStackTrace();
            return "00:00:00:00:00:00";
        }
    }

    private static String getAndroidMWifiMAC() {
        Log.v(TAG, "getAndroidMWifiMAC");
        String wifiMMac = null;
        String wifiName = getSystemProperties("wifi.interface");
        Log.v(TAG, "wifiName = " + wifiName);
        try {
            byte[] addr = NetworkInterface.getByName(wifiName).getHardwareAddress();
            if (!(addr == null || addr.length == 0)) {
                StringBuilder buf = new StringBuilder();
                int length = addr.length;
                for (int i = 0; i < length; i++) {
                    buf.append(String.format("%02X:", new Object[]{Byte.valueOf(addr[i])}));
                }
                Log.v(TAG, "getHardwareAddress buf =  " + buf.toString());
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                wifiMMac = buf.toString();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return wifiMMac;
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

    public static long getDataSpareQuantity() {
        long blockSize;
        long availCount;
        StatFs sf = new StatFs(Environment.getDataDirectory().getAbsolutePath());
        if (VERSION.SDK_INT < 18) {
            blockSize = (long) sf.getBlockSize();
            availCount = (long) sf.getAvailableBlocks();
        } else {
            blockSize = sf.getBlockSizeLong();
            availCount = sf.getAvailableBlocksLong();
        }
        return (blockSize * availCount) - 2097152;
    }

    public static long getSDCardSpareQuantity() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            if (sdcardDir != null && sdcardDir.exists()) {
                try {
                    long blockSize;
                    long availCount;
                    StatFs sf = new StatFs(sdcardDir.getPath());
                    if (VERSION.SDK_INT < 18) {
                        blockSize = (long) sf.getBlockSize();
                        availCount = (long) sf.getAvailableBlocks();
                    } else {
                        blockSize = sf.getBlockSizeLong();
                        availCount = sf.getAvailableBlocksLong();
                    }
                    return (blockSize * availCount) - 2097152;
                } catch (Exception e) {
                    LogUtils.m1569d(TAG, "getSDCardSpareQuantity exception ", e);
                    return -1;
                } catch (Throwable th) {
                    return -1;
                }
            }
        }
        return -1;
    }

    public static String getTime(Long currentTimeMillis) {
        return new SimpleDateFormat("HH:mm").format(new Date(currentTimeMillis.longValue()));
    }

    public static String getDNS() {
        try {
            Process process = Runtime.getRuntime().exec("getprop");
            Properties properties = new Properties();
            properties.load(process.getInputStream());
            CharSequence dns = properties.getProperty("[net.dns1]", "");
            if (!StringUtils.isEmpty(dns) && dns.length() > 6) {
                return dns.substring(1, dns.length() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String getSystemProperties(String key) {
        String value = "";
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            value = (String) systemProperties.getMethod("get", new Class[]{String.class, String.class}).invoke(systemProperties, new Object[]{key, ""});
            LogUtils.m1568d(TAG, "get SystemProperties key: " + key + " value : " + value);
            return value;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return value;
        } catch (NoSuchMethodException nsme) {
            nsme.printStackTrace();
            return value;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return value;
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            return value;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            return value;
        } catch (Exception e5) {
            e5.printStackTrace();
            return value;
        }
    }

    public static void updateServerTimeMillis(long serverTime) {
        sServerTimeMillis = serverTime;
        sElapsedRealTimeMillis = SystemClock.elapsedRealtime();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, " update server time server time : " + serverTime + " elapsed time = " + sElapsedRealTimeMillis);
        }
    }

    public static long getServerTimeMillis() {
        long elapsedRealTime = SystemClock.elapsedRealtime();
        long curTimeMillis = System.currentTimeMillis();
        long ret = sServerTimeMillis == 0 ? curTimeMillis : (sServerTimeMillis + elapsedRealTime) - sElapsedRealTimeMillis;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getServerTimeMillis: currentTimeMillis=" + curTimeMillis + ", serverTimeMillis=" + sServerTimeMillis + ", elapsed=" + sElapsedRealTimeMillis + "~" + elapsedRealTime + ", ret=" + ret);
        }
        return ret;
    }

    public static String getServerCurrentDate() {
        return new SimpleDateFormat(DateUtil.PATTERN_STANDARD08W).format(new Date(sServerTimeMillis));
    }

    public static String getCurrentTime() {
        String result = "";
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(getServerTimeMillis()));
    }

    public static void sendKeyCode(final int keyCode) {
        new Thread8K("DeviceUtils") {
            public void run() {
                try {
                    new Instrumentation().sendKeyDownUpSync(keyCode);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(DeviceUtils.TAG, "sendKeyCode() keyCode( " + keyCode + " )");
                    }
                } catch (Exception e) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1571e(DeviceUtils.TAG, e.toString());
                    }
                }
            }
        }.start();
    }

    public static int getCpuCoreNums() {
        if (cpuCores == 0) {
            try {
                cpuCores = new File("/sys/devices/system/cpu/").listFiles(new C16092()).length;
            } catch (Exception e) {
                cpuCores = 1;
            }
        }
        return cpuCores;
    }

    public static int getTotalMemory() {
        Exception e;
        Throwable th;
        if (memTotal > 0) {
            return memTotal;
        }
        BufferedReader br = null;
        try {
            BufferedReader br2 = new BufferedReader(new FileReader("/proc/meminfo"), 8);
            try {
                String content = br2.readLine();
                if (content != null) {
                    memTotal = (int) (((long) Integer.parseInt(content.substring(content.indexOf(58) + 1, content.indexOf(107)).trim())) / 1024);
                }
                if (br2 != null) {
                    try {
                        br2.close();
                        br = br2;
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        br = br2;
                    }
                }
            } catch (Exception e3) {
                e = e3;
                br = br2;
                try {
                    e.printStackTrace();
                    memTotal = 0;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return memTotal;
                } catch (Throwable th2) {
                    th = th2;
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                br = br2;
                if (br != null) {
                    br.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            memTotal = 0;
            if (br != null) {
                br.close();
            }
            return memTotal;
        }
        return memTotal;
    }

    public static boolean isSdCardAvailable() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return false;
        }
        LogUtils.m1574i(TAG, "sdcard exist");
        return true;
    }

    public static String getPlatModel() {
        return Build.MODEL;
    }

    public static String getProdModel() {
        return Build.PRODUCT;
    }

    public static int getOsVer() {
        return VERSION.SDK_INT;
    }

    public static String getHardwareInfo() {
        Exception e;
        Throwable th;
        StringBuilder info = new StringBuilder();
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            FileReader fr2 = new FileReader(CPU_INFO_PATH);
            try {
                String line;
                BufferedReader localBufferedReader2 = new BufferedReader(fr2, 8192);
                do {
                    try {
                        line = localBufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        line = line.toLowerCase(Locale.ENGLISH);
                    } catch (Exception e2) {
                        e = e2;
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    } catch (Throwable th2) {
                        th = th2;
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    }
                } while (!line.contains("hardware"));
                info.append(line.replaceAll(" ", "").replaceAll(HTTP.TAB, "").replaceAll("hardware:", ""));
                if (localBufferedReader2 != null) {
                    try {
                        localBufferedReader2.close();
                    } catch (IOException e3) {
                        Log.e(TAG, "getHardwareInfo: exception happened:", e3);
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    }
                }
                if (fr2 != null) {
                    fr2.close();
                }
                localBufferedReader = localBufferedReader2;
                fr = fr2;
            } catch (Exception e4) {
                e = e4;
                fr = fr2;
                try {
                    Log.e(TAG, "getHardwareInfo: exception happened:", e);
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (IOException e32) {
                            Log.e(TAG, "getHardwareInfo: exception happened:", e32);
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    Log.d(TAG, "getHardwareInfo: info=" + info);
                    return UrlUtils.urlEncode(info.toString());
                } catch (Throwable th3) {
                    th = th3;
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (IOException e322) {
                            Log.e(TAG, "getHardwareInfo: exception happened:", e322);
                            throw th;
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                fr = fr2;
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            Log.e(TAG, "getHardwareInfo: exception happened:", e);
            if (localBufferedReader != null) {
                localBufferedReader.close();
            }
            if (fr != null) {
                fr.close();
            }
            Log.d(TAG, "getHardwareInfo: info=" + info);
            return UrlUtils.urlEncode(info.toString());
        }
        Log.d(TAG, "getHardwareInfo: info=" + info);
        return UrlUtils.urlEncode(info.toString());
    }

    public static String getDisplayMetrics(Context context) {
        if (context == null) {
            return "";
        }
        int disWidth = context.getResources().getDisplayMetrics().widthPixels;
        int disHeight = context.getResources().getDisplayMetrics().heightPixels;
        return String.format("%dx%d", new Object[]{Integer.valueOf(disWidth), Integer.valueOf(disHeight)});
    }

    public static int[] getDeviceSize(Context context) {
        int[] ret = new int[2];
        if (context instanceof Activity) {
            if (VERSION.SDK_INT < 13) {
                ret[0] = ((Activity) context).getWindowManager().getDefaultDisplay().getWidth();
                ret[1] = ((Activity) context).getWindowManager().getDefaultDisplay().getHeight();
            } else {
                DisplayMetrics dm = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
                ret[0] = dm.widthPixels;
                ret[1] = dm.heightPixels;
            }
        }
        return ret;
    }

    public static String getMemoryPrint() {
        StringBuffer strbuf = new StringBuffer(">>> Memory Info <<<");
        try {
            ActivityManager am = (ActivityManager) AppRuntimeEnv.get().getApplicationContext().getSystemService("activity");
            for (RunningAppProcessInfo runningAppProcessInfo : am.getRunningAppProcesses()) {
                if (runningAppProcessInfo.processName.indexOf(AppRuntimeEnv.get().getApplicationContext().getPackageName()) != -1) {
                    MemoryInfo[] self_mi = am.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
                    strbuf.append("\n proccess Name:").append(runningAppProcessInfo.processName).append("\n pid:").append(runningAppProcessInfo.pid).append("\n dalvikPrivateDirty:").append(self_mi[0].dalvikPrivateDirty).append("\n dalvikPss:").append(self_mi[0].dalvikPss).append("\n dalvikSharedDirty:").append(self_mi[0].dalvikSharedDirty).append("\n nativePrivateDirty:").append(self_mi[0].nativePrivateDirty).append("\n nativePss:").append(self_mi[0].nativePss).append("\n nativeSharedDirty:").append(self_mi[0].nativeSharedDirty).append("\n otherPrivateDirty:").append(self_mi[0].otherPrivateDirty).append("\n otherPss:").append(self_mi[0].otherPss).append("\n otherSharedDirty:").append(self_mi[0].otherSharedDirty).append("\n TotalPrivateDirty:").append(self_mi[0].getTotalPrivateDirty()).append("\n TotalPss:").append(self_mi[0].getTotalPss()).append("\n TotalSharedDirty:").append(self_mi[0].getTotalSharedDirty());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strbuf.toString();
    }
}
