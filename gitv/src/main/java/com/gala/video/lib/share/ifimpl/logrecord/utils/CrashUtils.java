package com.gala.video.lib.share.ifimpl.logrecord.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.preference.LogRecordPreference;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrashUtils {
    public static final int MAX_CRASH_TIME_IN_EVERYDAY = 5;
    private static final String TAG = "Project/CrashUtils";
    private static DateFormat mDateFormat;

    public static String getCrashActivityName(String errorInfo) {
        Long time = Long.valueOf(System.currentTimeMillis());
        String activityName = "";
        if (!StringUtils.isEmpty((CharSequence) errorInfo)) {
            Matcher m = Pattern.compile("[^(QBase)(QMultiScreen)\\.]\\w+(Activity)+").matcher(errorInfo);
            if (m.find()) {
                activityName = m.group();
                Log.v(TAG, "get activity regex = " + activityName);
            } else {
                Log.v(TAG, "regex get activity NO MATCH");
                activityName = "activityName not regex";
            }
            Log.v(TAG, "get activity regex cost time = " + (System.currentTimeMillis() - time.longValue()));
        }
        return activityName;
    }

    public static String getExceptionName(String errorInfo, String crashType) {
        String exceptionName = "";
        if (StringUtils.isEmpty((CharSequence) errorInfo)) {
            return exceptionName;
        }
        return errorInfo.split("\\n")[0];
    }

    public static String getFormatBacktrace(String backTraceInfo) {
        String backTrace = "";
        String pattern = ">>>@-->>>";
        backTrace = backTraceInfo;
        try {
            if (!StringUtils.isEmpty((CharSequence) backTraceInfo)) {
                int nowPos = backTraceInfo.indexOf(pattern) + pattern.length();
                Log.v(TAG, "nowPos : " + nowPos);
                backTrace = backTraceInfo.substring(nowPos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backTrace;
    }

    public static String getCrashReport(Context context) {
        StringBuffer exceptionStr = new StringBuffer();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            exceptionStr.append("package versionName: " + info.versionName + "\n");
            exceptionStr.append("package versionCode: " + info.versionCode + "\n");
            exceptionStr.append("VERSION.RELEASE: " + VERSION.RELEASE + "\n");
            exceptionStr.append("MODEL: " + Build.MODEL + "\n");
            exceptionStr.append("VERSION.SDK_INT: " + VERSION.SDK_INT + "\n");
            exceptionStr.append("DISPLAY: " + Build.DISPLAY + "\n");
            exceptionStr.append("HARDWARE: " + Build.HARDWARE + "\n");
            exceptionStr.append("PRODUCT: " + Build.PRODUCT + "\n");
            return exceptionStr.toString();
        } catch (NameNotFoundException e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    public static void clearCrashFile() {
        File file = getCrashFile();
        if (file != null && file.exists()) {
            file.delete();
        }
    }

    public static File getCrashFile() {
        String filePath = LogRecordPreference.getCrashFilePath(AppRuntimeEnv.get().getApplicationContext());
        Log.v(TAG, "filePath = " + filePath);
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            Log.v(TAG, "file is not  exists");
            return null;
        }
        Log.v(TAG, "file length = " + file.length());
        return file;
    }

    public static boolean isNextDay(Context context, DateFormat format) {
        boolean z = true;
        String lastDay = LogRecordPreference.getFirstCrashTime(context);
        if (TextUtils.isEmpty(lastDay)) {
            return z;
        }
        if (lastDay.equals(format.format(new Date()))) {
            return false;
        }
        try {
            return new Date().after(format.parse(lastDay));
        } catch (ParseException e) {
            e.printStackTrace();
            return z;
        }
    }

    public static long getLeftDataSize() {
        StatFs dataFs = new StatFs(Environment.getDataDirectory().getPath());
        long sizesB = ((long) dataFs.getFreeBlocks()) * ((long) dataFs.getBlockSize());
        long sizeMB = (sizesB / 1024) / 1024;
        Log.v(TAG, "data剩余空间大小：" + sizesB + ", 剩余 " + sizeMB + " M");
        return sizeMB;
    }
}
