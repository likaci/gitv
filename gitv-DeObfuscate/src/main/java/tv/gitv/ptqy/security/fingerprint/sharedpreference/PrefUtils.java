package tv.gitv.ptqy.security.fingerprint.sharedpreference;

import android.content.Context;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;

public class PrefUtils {
    public static void setFingerprint(Context context, String dfp) {
        PrefHelper.putString(context, Consts.SEG_DFP, dfp);
    }

    public static String getFingerprint(Context context) {
        return PrefHelper.getString(context, Consts.SEG_DFP, null);
    }

    public static void removeFingerprint(Context context) {
        PrefHelper.remove(context, Consts.SEG_DFP);
    }

    public static void setStoreTime(Context context, long storeTime) {
        PrefHelper.putLong(context, Consts.SEG_STORE_TIME, storeTime);
    }

    public static long getStoreTime(Context context) {
        return PrefHelper.getLong(context, Consts.SEG_STORE_TIME, 0);
    }

    public static void removeStoreTime(Context context) {
        PrefHelper.remove(context, Consts.SEG_STORE_TIME);
    }

    public static void setExpireTime(Context context, long expTime) {
        PrefHelper.putLong(context, Consts.SEG_EXP_TIME, expTime);
    }

    public static long getExpireTime(Context context) {
        return PrefHelper.getLong(context, Consts.SEG_EXP_TIME, 0);
    }

    public static void removeExpireTime(Context context) {
        PrefHelper.remove(context, Consts.SEG_EXP_TIME);
    }

    public static void setUploadPingbackTime(Context context, long uploadTime) {
        PrefHelper.putLong(context, "up_pingback_time", uploadTime);
    }

    public static long getUploadPingbackTime(Context context) {
        return PrefHelper.getLong(context, "up_pingback_time", 0);
    }

    public static void setErrorCache(Context context, String errorLog) {
        PrefHelper.putString(context, "error_log", errorLog);
    }

    public static String getErrorCache(Context context) {
        return PrefHelper.getString(context, "error_log", "");
    }
}
