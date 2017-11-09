package com.gala.video.lib.share.ifimpl.logrecord.preference;

import android.content.Context;
import com.gala.video.lib.share.system.preference.AppPreference;

public class LogRecordPreference {
    private static final String CRASHDETAIL = "crashDetail";
    private static final String CRASHMEMINFO = "crashMeminfo";
    private static final String CRASHTYPE = "crashType";
    private static final String CRASH_FILE_PATH = "crash_File_Path";
    private static final String EVENT_ID = "eventID";
    private static final String EXCEPTION = "exception";
    private static final String FIRST_CRASH_TIME = "first_crash_time";
    private static final String LAST_APK_VERSION = "last_apk_version";
    private static final String LOGRECORD_OPEN = "logrecord_open";
    private static final String NAME = "logrecord";
    private static final String PLUGIN_NAME = "LOGRECORD-PRIVATE";
    public static final String START_RECORD_TIME = "start_record_time";
    private static final String TODAY_UPLOAD_TIMES = "today_upload_times";

    public static void saveLogrecordOpen(Context ctx, boolean isOpen) {
        new AppPreference(ctx, "logrecord").save(LOGRECORD_OPEN, isOpen);
    }

    public static void saveCrashType(Context ctx, String crashType) {
        new AppPreference(ctx, "logrecord").save(CRASHTYPE, crashType);
    }

    public static void saveException(Context ctx, String exception) {
        new AppPreference(ctx, "logrecord").save(EXCEPTION, exception);
    }

    public static void saveCrashDetail(Context ctx, String crashDetail) {
        new AppPreference(ctx, "logrecord").save(CRASHDETAIL, crashDetail);
    }

    public static void saveCrashMeminfo(Context ctx, String meminfo) {
        new AppPreference(ctx, "logrecord").save(CRASHMEMINFO, meminfo);
    }

    public static void saveLastApkVersion(Context ctx, String apkversion) {
        new AppPreference(ctx, "logrecord").save(LAST_APK_VERSION, apkversion);
    }

    public static void saveEventId(Context ctx, String eventId) {
        new AppPreference(ctx, "logrecord").save(EVENT_ID, eventId);
    }

    public static void saveCrashFilePath(Context ctx, String filePath) {
        new AppPreference(ctx, "logrecord").save(CRASH_FILE_PATH, filePath);
    }

    public static void saveTodayUploadTimes(Context ctx, int uploadTimes) {
        new AppPreference(ctx, "logrecord").save(TODAY_UPLOAD_TIMES, uploadTimes);
    }

    public static void saveFirstCrashTime(Context ctx, String crashTime) {
        new AppPreference(ctx, "logrecord").save(FIRST_CRASH_TIME, crashTime);
    }

    public static String getEventId(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(EVENT_ID);
    }

    public static String getCrashFilePath(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(CRASH_FILE_PATH);
    }

    public static String getCrashMeminfo(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(CRASHMEMINFO);
    }

    public static int getTodayUploadTimes(Context ctx) {
        return new AppPreference(ctx, "logrecord").getInt(TODAY_UPLOAD_TIMES, 0);
    }

    public static String getFirstCrashTime(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(FIRST_CRASH_TIME);
    }

    public static String getLastApkVersion(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(LAST_APK_VERSION);
    }

    public static String getCrashType(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(CRASHTYPE);
    }

    public static String getException(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(EXCEPTION);
    }

    public static String getCrashDetail(Context ctx) {
        return new AppPreference(ctx, "logrecord").get(CRASHDETAIL);
    }

    public static boolean getLogrecordOpen(Context ctx) {
        return new AppPreference(ctx, "logrecord").getBoolean(LOGRECORD_OPEN, true);
    }

    public static void clear(Context ctx) {
        new AppPreference(ctx, "logrecord").clear();
    }

    public static long getLastStart(Context ctx) {
        return new AppPreference(ctx, PLUGIN_NAME).getLong(START_RECORD_TIME, 0);
    }
}
