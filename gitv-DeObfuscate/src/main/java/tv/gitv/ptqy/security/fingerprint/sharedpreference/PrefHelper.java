package tv.gitv.ptqy.security.fingerprint.sharedpreference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import java.util.Set;

public class PrefHelper {
    public static Set<String> getStringSet(Context context, String prefKey, Set<String> defaultValue) {
        return getSharedPreferences(context).getStringSet(prefKey, defaultValue);
    }

    public static boolean getBoolean(Context context, String prefKey, boolean defaultValue) {
        return getSharedPreferences(context).getBoolean(prefKey, defaultValue);
    }

    public static float getFloat(Context context, String prefKey, float defaultValue) {
        return getSharedPreferences(context).getFloat(prefKey, defaultValue);
    }

    public static int getInt(Context context, String prefKey, int defaultValue) {
        return getSharedPreferences(context).getInt(prefKey, defaultValue);
    }

    public static long getLong(Context context, String prefKey, long defaultValue) {
        return getSharedPreferences(context).getLong(prefKey, defaultValue);
    }

    public static String getString(Context context, String prefKey, String defaultValue) {
        return getSharedPreferences(context).getString(prefKey, defaultValue);
    }

    public static void putStringSet(Context context, String prefKey, Set<String> value) {
        getSharedPreferences(context).edit().putStringSet(prefKey, value).commit();
    }

    public static void putBoolean(Context context, String prefKey, boolean value) {
        getSharedPreferences(context).edit().putBoolean(prefKey, value).commit();
    }

    public static void putFloat(Context context, String prefKey, float value) {
        getSharedPreferences(context).edit().putFloat(prefKey, value).commit();
    }

    public static void putInt(Context context, String prefKey, int value) {
        getSharedPreferences(context).edit().putInt(prefKey, value).commit();
    }

    public static void putLong(Context context, String prefKey, long value) {
        getSharedPreferences(context).edit().putLong(prefKey, value).commit();
    }

    public static void putString(Context context, String prefKey, String value) {
        getSharedPreferences(context).edit().putString(prefKey, value).commit();
    }

    public static void remove(Context context, String prefKey) {
        getSharedPreferences(context).edit().remove(prefKey).commit();
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return getSharedPreferences(context, null);
    }

    public static SharedPreferences getSharedPreferences(Context context, String prefName) {
        return TextUtils.isEmpty(prefName) ? PreferenceManager.getDefaultSharedPreferences(context) : context.getSharedPreferences(prefName, 0);
    }

    public static void registerOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener) {
        getSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterOnSharedPreferenceChangeListener(Context context, OnSharedPreferenceChangeListener listener) {
        getSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
    }
}
