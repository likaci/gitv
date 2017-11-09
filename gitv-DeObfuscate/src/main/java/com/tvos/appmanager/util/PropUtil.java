package com.tvos.appmanager.util;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropUtil {
    public static final String PROP_OTADEBUG = "persist.ubootenv.otadebug";
    private static final String TAG = "PropUtil";

    public static void setProp(String key, String value) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = Class.forName("android.os.SystemProperties");
        Method method = clazz.getMethod("set", new Class[]{String.class, String.class});
        if (method != null) {
            method.invoke(clazz, new Object[]{key, value});
        }
    }

    public static String getProp(String key) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = Class.forName("android.os.SystemProperties");
        Method method = clazz.getMethod("get", new Class[]{String.class});
        if (method == null) {
            return null;
        }
        Object value = method.invoke(clazz, new Object[]{key});
        Log.d("RootCmdUtils", (String) value);
        return (String) value;
    }
}
