package com.tvos.apps.utils;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PropUtil {
    private static final String CLASS_NAME_SYSTEMPROPERTIES = "android.os.SystemProperties";
    private static final String TAG = "PropUtil";

    public static void setProp(String key, String value) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Log.d(TAG, "setProp, key = " + key + " , value = " + value);
        Class<?> clazz = Class.forName(CLASS_NAME_SYSTEMPROPERTIES);
        Method method = clazz.getMethod("set", new Class[]{String.class, String.class});
        if (method != null) {
            method.invoke(clazz, new Object[]{key, value});
        }
    }

    public static String getProp(String key) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = Class.forName(CLASS_NAME_SYSTEMPROPERTIES);
        Method method = clazz.getMethod("get", new Class[]{String.class});
        String value = null;
        if (method != null) {
            value = (String) method.invoke(clazz, new Object[]{key});
        }
        Log.d(TAG, "getProp, key = " + key + " , value = " + value);
        return value;
    }
}
