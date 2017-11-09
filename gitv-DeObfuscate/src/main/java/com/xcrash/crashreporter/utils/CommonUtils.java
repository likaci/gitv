package com.xcrash.crashreporter.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import org.json.JSONException;
import org.json.JSONObject;

public class CommonUtils {
    public static final String CHAR_SET = "UTF-8";
    public static final String QIYI_REACT = ":silk";
    public static final String QIYI_WEBVIEW = ":webview";
    public static final String TAG = "CommonUtils";
    public static final int TRACE_VALID_TIME = 60000;

    public static String getCrashDirectory(Context context) {
        if (context == null) {
            return null;
        }
        String dir = context.getFilesDir().getAbsolutePath() + File.separator + "app" + File.separator + "crash";
        File file = new File(dir);
        if (file.exists()) {
            return dir;
        }
        file.mkdirs();
        return dir;
    }

    public static File getTraceFile(String process) {
        Throwable th;
        File traceFile = new File("/data/anr/traces.txt");
        if (isTraceFileValid(traceFile)) {
            return traceFile;
        }
        DebugLog.m1740d(TAG, "can't find valid /data/anr/traces.txt");
        traceFile = new File("/data/anr/traces_" + process + ".txt");
        if (isTraceFileValid(traceFile)) {
            return traceFile;
        }
        String tracePath;
        DebugLog.m1740d(TAG, "can't find valid ", backupTrace);
        BufferedReader buf = null;
        try {
            Process propProc = new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/getprop", "dalvik.vm.stack-trace-file"}).redirectErrorStream(true).start();
            try {
                BufferedReader buf2 = new BufferedReader(new InputStreamReader(propProc.getInputStream()), 100);
                try {
                    tracePath = buf2.readLine();
                    Log.i(TAG, "getprop returned " + String.valueOf(tracePath));
                    if (buf2 != null) {
                        try {
                            buf2.close();
                        } catch (IOException e) {
                            buf = buf2;
                            tracePath = "";
                            return tracePath != null ? null : null;
                        }
                    }
                    propProc.destroy();
                } catch (Throwable th2) {
                    th = th2;
                    buf = buf2;
                    if (buf != null) {
                        buf.close();
                    }
                    propProc.destroy();
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                if (buf != null) {
                    buf.close();
                }
                propProc.destroy();
                throw th;
            }
        } catch (IOException e2) {
            tracePath = "";
            if (tracePath != null) {
            }
        }
        if (tracePath != null && tracePath.length() > 0) {
            Log.d(TAG, "trace file path " + tracePath);
            traceFile = new File(tracePath);
            if (isTraceFileValid(traceFile)) {
                return traceFile;
            }
            return null;
        }
    }

    public static boolean isTraceFileValid(File traceFile) {
        return traceFile.isFile() && traceFile.canRead() && System.currentTimeMillis() - traceFile.lastModified() < 60000;
    }

    public static String inputStreamToString(InputStream is) {
        Exception e;
        Throwable th;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            try {
                for (String s = br2.readLine(); s != null; s = br2.readLine()) {
                    sb.append(s + "\n");
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
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    return sb.toString();
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
            if (br != null) {
                br.close();
            }
            return sb.toString();
        }
        return sb.toString();
    }

    public static long getTotalMemorySize(Context context) {
        if (VERSION.SDK_INT >= 16) {
            MemoryInfo mi = new MemoryInfo();
            ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
            return mi.totalMem;
        }
        try {
            String memoryLine = readFileStr("/proc/meminfo");
            return ((long) Integer.parseInt(memoryLine.substring(memoryLine.indexOf("MemTotal:")).replaceAll("\\D+", ""))) * 1024;
        } catch (Exception e) {
            return 0;
        }
    }

    public static long getUsedMemorySize(Context context) {
        MemoryInfo mi = new MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(mi);
        return getTotalMemorySize(context) - mi.availMem;
    }

    public static long getTotalDataSize() {
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return ((long) stat.getBlockCount()) * ((long) stat.getBlockSize());
        } catch (Throwable th) {
            return 0;
        }
    }

    public static long getUsedDataSize() {
        return getTotalDataSize() - getAvailableDataSize();
    }

    public static long getAvailableDataSize() {
        try {
            StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
            return ((long) stat.getAvailableBlocks()) * ((long) stat.getBlockSize());
        } catch (Throwable th) {
            return 0;
        }
    }

    public static String getStoragePath(Context mContext, boolean is_removale) {
        try {
            StorageManager mStorageManager = (StorageManager) mContext.getSystemService("storage");
            Class<?> storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList", new Class[0]);
            Method getPath = storageVolumeClazz.getMethod("getPath", new Class[0]);
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable", new Class[0]);
            Object result = getVolumeList.invoke(mStorageManager, new Object[0]);
            int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement, new Object[0]);
                if (is_removale == ((Boolean) isRemovable.invoke(storageVolumeElement, new Object[0])).booleanValue()) {
                    return path;
                }
            }
        } catch (Throwable th) {
        }
        return null;
    }

    public static long getTotalInternalSdcardSize() {
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return ((long) stat.getBlockSize()) * ((long) stat.getBlockCount());
        } catch (Throwable th) {
            return 0;
        }
    }

    public static long getUsedInternalSdcardSize() {
        try {
            StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
            return (((long) stat.getBlockCount()) - ((long) stat.getAvailableBlocks())) * ((long) stat.getBlockSize());
        } catch (Throwable th) {
            return 0;
        }
    }

    public static long getTotalExternalSdcardSize(Context context) {
        String path = getStoragePath(context, true);
        if (path == null) {
            return 0;
        }
        try {
            StatFs stat = new StatFs(path);
            return ((long) stat.getBlockSize()) * ((long) stat.getBlockCount());
        } catch (Throwable th) {
            return 0;
        }
    }

    public static long getUsedExternalSdcardSize(Context context) {
        String path = getStoragePath(context, true);
        if (path == null) {
            return 0;
        }
        try {
            StatFs stat = new StatFs(path);
            return (((long) stat.getBlockCount()) - ((long) stat.getAvailableBlocks())) * ((long) stat.getBlockSize());
        } catch (Throwable th) {
            return 0;
        }
    }

    public static String getBuddyInfo() {
        return readFileStr("/proc/buddyinfo");
    }

    public static String getOnlineCpu() {
        return readFileStr("/sys/devices/system/cpu/online");
    }

    public static String getOfflineCpu() {
        return readFileStr("/sys/devices/system/cpu/offline");
    }

    public static String getLoadAverage() {
        return readFileStr("/proc/loadavg");
    }

    public static String readFileStr(String filePath) {
        IOException e;
        Throwable th;
        FileReader fr = null;
        BufferedReader br = null;
        try {
            FileReader fr2 = new FileReader(filePath);
            try {
                BufferedReader br2 = new BufferedReader(fr2, 2048);
                try {
                    String fileStr = br2.readLine();
                    if (br2 != null) {
                        try {
                            br2.close();
                        } catch (IOException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (fr2 != null) {
                        try {
                            fr2.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    br = br2;
                    fr = fr2;
                    return fileStr;
                } catch (IOException e3) {
                    e22 = e3;
                    br = br2;
                    fr = fr2;
                    try {
                        e22.printStackTrace();
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (fr != null) {
                            try {
                                fr.close();
                            } catch (IOException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                        return "";
                    } catch (Throwable th2) {
                        th = th2;
                        if (br != null) {
                            try {
                                br.close();
                            } catch (IOException e22222) {
                                e22222.printStackTrace();
                            }
                        }
                        if (fr != null) {
                            try {
                                fr.close();
                            } catch (IOException e222222) {
                                e222222.printStackTrace();
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    br = br2;
                    fr = fr2;
                    if (br != null) {
                        br.close();
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    throw th;
                }
            } catch (IOException e4) {
                e222222 = e4;
                fr = fr2;
                e222222.printStackTrace();
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
                return "";
            } catch (Throwable th4) {
                th = th4;
                fr = fr2;
                if (br != null) {
                    br.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
        } catch (IOException e5) {
            e222222 = e5;
            e222222.printStackTrace();
            if (br != null) {
                br.close();
            }
            if (fr != null) {
                fr.close();
            }
            return "";
        }
    }

    public static void fillDeviceinfo(Context context, JSONObject jsonObject) {
        try {
            jsonObject.put("TotalDisk", String.valueOf(getTotalDataSize()));
            jsonObject.put("UsedDisk", String.valueOf(getUsedDataSize()));
            jsonObject.put("TotalSdcard", String.valueOf(getTotalInternalSdcardSize()));
            jsonObject.put("UsedSdcard", String.valueOf(getUsedInternalSdcardSize()));
            jsonObject.put("TotalExSdcard", String.valueOf(getTotalExternalSdcardSize(context)));
            jsonObject.put("UsedExSdcard", String.valueOf(getUsedExternalSdcardSize(context)));
            jsonObject.put("Fingerprint", Build.FINGERPRINT);
            jsonObject.put("ApiLevel", String.valueOf(VERSION.SDK_INT));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillDeviceRuntimeInfo(Context context, JSONObject jsonObject) {
        try {
            jsonObject.put("TotalMemory", String.valueOf(getTotalMemorySize(context)));
            jsonObject.put("UsedMemory", String.valueOf(getUsedMemorySize(context)));
            jsonObject.put("Buddyinfo", getBuddyInfo());
            jsonObject.put("CpuOnline", getOnlineCpu());
            jsonObject.put("CpuOffline", getOfflineCpu());
            jsonObject.put("CpuLoadavg", getLoadAverage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void fillDeviceinfoForANR(Context context, JSONObject jsonObject) {
        try {
            jsonObject.put("tmem", String.valueOf(getTotalMemorySize(context)));
            jsonObject.put("umem", String.valueOf(getUsedMemorySize(context)));
            jsonObject.put("tds", String.valueOf(getTotalDataSize()));
            jsonObject.put("uds", String.valueOf(getUsedDataSize()));
            jsonObject.put("tsds", String.valueOf(getTotalInternalSdcardSize()));
            jsonObject.put("usds", String.valueOf(getUsedInternalSdcardSize()));
            jsonObject.put("texsds", String.valueOf(getTotalExternalSdcardSize(context)));
            jsonObject.put("uexsds", String.valueOf(getUsedExternalSdcardSize(context)));
            jsonObject.put("fingerp", Build.FINGERPRINT);
            jsonObject.put("apilevel", String.valueOf(VERSION.SDK_INT));
            jsonObject.put("buddyinfo", getBuddyInfo());
            jsonObject.put("cpuon", getOnlineCpu());
            jsonObject.put("cpuoff", getOfflineCpu());
            jsonObject.put("loadavg", getLoadAverage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getVivoVersion() {
        Exception e;
        Throwable th;
        Process propProc = null;
        BufferedReader buf = null;
        String vivoVersion = "";
        try {
            propProc = new ProcessBuilder(new String[0]).command(new String[]{"/system/bin/getprop", "ro.vivo.product.version"}).redirectErrorStream(true).start();
            BufferedReader buf2 = new BufferedReader(new InputStreamReader(propProc.getInputStream()), 100);
            try {
                vivoVersion = buf2.readLine();
                if (buf2 != null) {
                    try {
                        buf2.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (propProc != null) {
                    propProc.destroy();
                    buf = buf2;
                }
            } catch (Exception e3) {
                e = e3;
                buf = buf2;
                try {
                    e.printStackTrace();
                    if (buf != null) {
                        try {
                            buf.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (propProc != null) {
                        propProc.destroy();
                    }
                    return vivoVersion;
                } catch (Throwable th2) {
                    th = th2;
                    if (buf != null) {
                        try {
                            buf.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                        }
                    }
                    if (propProc != null) {
                        propProc.destroy();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                buf = buf2;
                if (buf != null) {
                    buf.close();
                }
                if (propProc != null) {
                    propProc.destroy();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (buf != null) {
                buf.close();
            }
            if (propProc != null) {
                propProc.destroy();
            }
            return vivoVersion;
        }
        return vivoVersion;
    }

    public static String getHardwareInfo() {
        Exception e;
        Throwable th;
        Process proc = null;
        BufferedReader propBr = null;
        BufferedReader cpuBr = null;
        FileReader fr = null;
        try {
            proc = Runtime.getRuntime().exec("getprop");
            BufferedReader propBr2 = new BufferedReader(new InputStreamReader(proc.getInputStream(), "UTF-8"));
            try {
                StringBuilder sb = new StringBuilder();
                String s = propBr2.readLine();
                while (s != null) {
                    if (s.contains("cpu.abi") || s.contains("product.model")) {
                        sb.append(s + "\n");
                    }
                    s = propBr2.readLine();
                }
                FileReader fr2 = new FileReader("/proc/cpuinfo");
                try {
                    BufferedReader cpuBr2 = new BufferedReader(fr2);
                    try {
                        for (s = cpuBr2.readLine(); s != null; s = cpuBr2.readLine()) {
                            if (s.contains("Hardware")) {
                                sb.append(s + "\n");
                            }
                        }
                        String stringBuilder = sb.toString();
                        if (propBr2 != null) {
                            try {
                                propBr2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (cpuBr2 != null) {
                            try {
                                cpuBr2.close();
                            } catch (IOException e22) {
                                e22.printStackTrace();
                            }
                        }
                        if (fr2 != null) {
                            try {
                                fr2.close();
                            } catch (IOException e222) {
                                e222.printStackTrace();
                            }
                        }
                        if (proc != null) {
                            proc.destroy();
                        }
                        fr = fr2;
                        cpuBr = cpuBr2;
                        propBr = propBr2;
                        return stringBuilder;
                    } catch (Exception e3) {
                        e = e3;
                        fr = fr2;
                        cpuBr = cpuBr2;
                        propBr = propBr2;
                        try {
                            e.printStackTrace();
                            if (propBr != null) {
                                try {
                                    propBr.close();
                                } catch (IOException e2222) {
                                    e2222.printStackTrace();
                                }
                            }
                            if (cpuBr != null) {
                                try {
                                    cpuBr.close();
                                } catch (IOException e22222) {
                                    e22222.printStackTrace();
                                }
                            }
                            if (fr != null) {
                                try {
                                    fr.close();
                                } catch (IOException e222222) {
                                    e222222.printStackTrace();
                                }
                            }
                            if (proc != null) {
                                proc.destroy();
                            }
                            return "";
                        } catch (Throwable th2) {
                            th = th2;
                            if (propBr != null) {
                                try {
                                    propBr.close();
                                } catch (IOException e2222222) {
                                    e2222222.printStackTrace();
                                }
                            }
                            if (cpuBr != null) {
                                try {
                                    cpuBr.close();
                                } catch (IOException e22222222) {
                                    e22222222.printStackTrace();
                                }
                            }
                            if (fr != null) {
                                try {
                                    fr.close();
                                } catch (IOException e222222222) {
                                    e222222222.printStackTrace();
                                }
                            }
                            if (proc != null) {
                                proc.destroy();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fr = fr2;
                        cpuBr = cpuBr2;
                        propBr = propBr2;
                        if (propBr != null) {
                            propBr.close();
                        }
                        if (cpuBr != null) {
                            cpuBr.close();
                        }
                        if (fr != null) {
                            fr.close();
                        }
                        if (proc != null) {
                            proc.destroy();
                        }
                        throw th;
                    }
                } catch (Exception e4) {
                    e = e4;
                    fr = fr2;
                    propBr = propBr2;
                    e.printStackTrace();
                    if (propBr != null) {
                        propBr.close();
                    }
                    if (cpuBr != null) {
                        cpuBr.close();
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    if (proc != null) {
                        proc.destroy();
                    }
                    return "";
                } catch (Throwable th4) {
                    th = th4;
                    fr = fr2;
                    propBr = propBr2;
                    if (propBr != null) {
                        propBr.close();
                    }
                    if (cpuBr != null) {
                        cpuBr.close();
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    if (proc != null) {
                        proc.destroy();
                    }
                    throw th;
                }
            } catch (Exception e5) {
                e = e5;
                propBr = propBr2;
                e.printStackTrace();
                if (propBr != null) {
                    propBr.close();
                }
                if (cpuBr != null) {
                    cpuBr.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (proc != null) {
                    proc.destroy();
                }
                return "";
            } catch (Throwable th5) {
                th = th5;
                propBr = propBr2;
                if (propBr != null) {
                    propBr.close();
                }
                if (cpuBr != null) {
                    cpuBr.close();
                }
                if (fr != null) {
                    fr.close();
                }
                if (proc != null) {
                    proc.destroy();
                }
                throw th;
            }
        } catch (Exception e6) {
            e = e6;
            e.printStackTrace();
            if (propBr != null) {
                propBr.close();
            }
            if (cpuBr != null) {
                cpuBr.close();
            }
            if (fr != null) {
                fr.close();
            }
            if (proc != null) {
                proc.destroy();
            }
            return "";
        }
    }

    public static boolean isRnProcess(Context context, String processName) {
        if (TextUtils.equals(processName, context.getPackageName() + QIYI_REACT)) {
            return true;
        }
        return false;
    }

    public static boolean isWebViewProcess(Context context, String processName) {
        if (TextUtils.equals(processName, context.getPackageName() + QIYI_WEBVIEW)) {
            return true;
        }
        return false;
    }
}
