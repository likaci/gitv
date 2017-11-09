package com.gala.sdk.utils;

import com.gala.report.LogRecord;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.HashMap;

public class QosLogUtils {
    private static final Converter<Object> f712a = new C01821();
    private static final HashMap<Class<?>, Converter<?>> f713a = new HashMap();

    public interface Converter<Type> {
        String convert(Type type);
    }

    static class C01821 implements Converter<Object> {
        C01821() {
        }

        public final String convert(Object obj) {
            if (obj == null) {
                return null;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w("QosLogUtils", "Using default converter for " + obj + " is NOT recommended");
            }
            return obj.toString();
        }
    }

    public interface Convertable {
        String convert();
    }

    private QosLogUtils() {
    }

    public static void m477d(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(m476a(tag), msg);
        }
        LogRecord.m392d(m476a(tag), msg);
    }

    public static void m478d(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1569d(m476a(tag), msg, t);
        }
        LogRecord.m393d(m476a(tag), msg, t);
    }

    public static void m481i(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(m476a(tag), msg);
        }
        LogRecord.m396i(m476a(tag), msg);
    }

    public static void m482i(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1575i(m476a(tag), msg, t);
        }
        LogRecord.m397i(m476a(tag), msg, t);
    }

    public static void m483w(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(m476a(tag), msg);
        }
        LogRecord.m400w(m476a(tag), msg);
    }

    public static void m484w(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1578w(m476a(tag), msg, t);
        }
        LogRecord.m401w(m476a(tag), msg, t);
    }

    public static void m479e(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(m476a(tag), msg);
        }
        LogRecord.m394e(m476a(tag), msg);
    }

    public static void m480e(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1572e(m476a(tag), msg, t);
        }
        LogRecord.m395e(m476a(tag), msg, t);
    }

    private static String m476a(String str) {
        return "[Qos] " + str;
    }

    public static void registerConverter(Class<?> clazz, Converter<?> c) {
        f713a.put(clazz, c);
    }

    public static String convertToString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Convertable) {
            return ((Convertable) obj).convert();
        }
        Converter converter = (Converter) f713a.get(obj.getClass());
        if (converter == null) {
            converter = f712a;
        }
        return converter.convert(obj);
    }
}
