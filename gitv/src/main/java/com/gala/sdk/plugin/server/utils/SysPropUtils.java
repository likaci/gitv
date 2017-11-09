package com.gala.sdk.plugin.server.utils;

import com.gala.sdk.plugin.Log;
import java.lang.reflect.Method;

public final class SysPropUtils {
    private static final Method METHOD_SYSPROP_GET;
    private static final Method METHOD_SYSPROP_GETBOOLEAN;
    private static final Method METHOD_SYSPROP_GETINT;
    private static final String TAG = "GalaUtils/SysPropUtils";

    static {
        Method methodGetInt = null;
        Method methodGetBoolean = null;
        Method methodGet = null;
        try {
            Class<?> systemPropertiesClass = Class.forName("android.os.SystemProperties");
            methodGetInt = systemPropertiesClass.getDeclaredMethod("getInt", new Class[]{String.class, Integer.TYPE});
            methodGetInt.setAccessible(true);
            methodGetBoolean = systemPropertiesClass.getDeclaredMethod("getBoolean", new Class[]{String.class, Boolean.TYPE});
            methodGetBoolean.setAccessible(true);
            methodGet = systemPropertiesClass.getDeclaredMethod("get", new Class[]{String.class, String.class});
            methodGet.setAccessible(true);
            METHOD_SYSPROP_GETINT = methodGetInt;
            METHOD_SYSPROP_GETBOOLEAN = methodGetBoolean;
            METHOD_SYSPROP_GET = methodGet;
        } catch (Exception e) {
            if (Log.DEBUG) {
                Log.e(TAG, "exception during reflection:", e);
            }
            METHOD_SYSPROP_GETINT = methodGetInt;
            METHOD_SYSPROP_GETBOOLEAN = methodGetBoolean;
            METHOD_SYSPROP_GET = methodGet;
        } catch (Throwable th) {
            METHOD_SYSPROP_GETINT = methodGetInt;
            METHOD_SYSPROP_GETBOOLEAN = methodGetBoolean;
            METHOD_SYSPROP_GET = methodGet;
        }
    }

    private SysPropUtils() {
    }

    public static int getInt(String key, int def) {
        return ((Integer) safeInvoke(METHOD_SYSPROP_GETINT, key, Integer.valueOf(def))).intValue();
    }

    public static boolean getBoolean(String key, boolean def) {
        return ((Boolean) safeInvoke(METHOD_SYSPROP_GETBOOLEAN, key, Boolean.valueOf(def))).booleanValue();
    }

    public static String get(String key, String def) {
        return (String) safeInvoke(METHOD_SYSPROP_GET, key, def);
    }

    private static Object safeInvoke(Method method, String key, Object def) {
        if (Log.VERBOSE) {
            Log.d(TAG, ">> safeInvoke(method:" + method + ", key:" + key + ", def:" + def + ")");
        }
        if (method != null) {
            Object ret = def;
            try {
                ret = method.invoke(null, new Object[]{key, def});
            } catch (Exception e) {
                if (Log.DEBUG) {
                    Log.e(TAG, "safeInvoke: exception happened: ", e);
                }
            }
            if (Log.DEBUG) {
                Log.d(TAG, "<< safeInvoke: return=" + ret);
            }
            return ret;
        } else if (!Log.DEBUG) {
            return def;
        } else {
            Log.e(TAG, "<< safeInvoke: reflection failed");
            return def;
        }
    }
}
