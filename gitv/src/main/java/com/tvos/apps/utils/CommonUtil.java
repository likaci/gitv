package com.tvos.apps.utils;

import java.lang.reflect.Method;
import java.util.List;

public class CommonUtil {
    public static float convertFloatNum(float orignalNum, int decimalCount) {
        return ((float) Math.round(((float) Math.pow(10.0d, (double) decimalCount)) * orignalNum)) / ((float) Math.pow(10.0d, (double) decimalCount));
    }

    public static <T> boolean isEmptyList(List<T> list) {
        if (list == null || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static <T> boolean isArrayEmpty(T[] arr) {
        if (arr == null || arr.length == 0) {
            return true;
        }
        return false;
    }

    public static <T> T clone(T obj) {
        T t = null;
        if (obj != null) {
            Method cloneMethod = null;
            try {
                cloneMethod = obj.getClass().getMethod("clone", new Class[0]);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            }
            if (cloneMethod != null) {
                try {
                    t = cloneMethod.invoke(obj, new Object[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return t;
    }

    public static boolean isLegalIntID(long id) {
        return id > 0;
    }

    public static boolean isLegalIntID(String id) {
        try {
            return isLegalIntID((long) Integer.parseInt(id));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }
}
