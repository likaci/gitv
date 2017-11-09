package com.gala.sdk.utils;

import com.gala.report.LogRecord;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.HashMap;

public class QosLogUtils {
    private static final Converter<Object> a = new Converter<Object>() {
        public final String convert(Object obj) {
            if (obj == null) {
                return null;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.w("QosLogUtils", "Using default converter for " + obj + " is NOT recommended");
            }
            return obj.toString();
        }
    };
    private static final HashMap<Class<?>, Converter<?>> f362a = new HashMap();

    public interface Converter<Type> {
        String convert(Type type);
    }

    public interface Convertable {
        String convert();
    }

    private QosLogUtils() {
    }

    public static void d(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(a(tag), msg);
        }
        LogRecord.d(a(tag), msg);
    }

    public static void d(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(a(tag), msg, t);
        }
        LogRecord.d(a(tag), msg, t);
    }

    public static void i(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(a(tag), msg);
        }
        LogRecord.i(a(tag), msg);
    }

    public static void i(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.i(a(tag), msg, t);
        }
        LogRecord.i(a(tag), msg, t);
    }

    public static void w(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(a(tag), msg);
        }
        LogRecord.w(a(tag), msg);
    }

    public static void w(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(a(tag), msg, t);
        }
        LogRecord.w(a(tag), msg, t);
    }

    public static void e(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(a(tag), msg);
        }
        LogRecord.e(a(tag), msg);
    }

    public static void e(String tag, String msg, Throwable t) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(a(tag), msg, t);
        }
        LogRecord.e(a(tag), msg, t);
    }

    private static String a(String str) {
        return "[Qos] " + str;
    }

    public static void registerConverter(Class<?> clazz, Converter<?> c) {
        f362a.put(clazz, c);
    }

    public static String convertToString(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Convertable) {
            return ((Convertable) obj).convert();
        }
        Converter converter = (Converter) f362a.get(obj.getClass());
        if (converter == null) {
            converter = a;
        }
        return converter.convert(obj);
    }
}
