package com.gala.sdk.utils;

public class Log {
    public static boolean DEBUG = true;
    private static LogPrinter a = new LogPrinterImpl();

    public interface LogPrinter {
        void d(String str, String str2);

        void d(String str, String str2, Throwable th);

        void e(String str, String str2);

        void e(String str, String str2, Throwable th);

        void v(String str, String str2);

        void v(String str, String str2, Throwable th);

        void w(String str, String str2);

        void w(String str, String str2, Throwable th);
    }

    public static class LogPrinterImpl implements LogPrinter {
        public void v(String tag, String msg) {
            android.util.Log.v(tag, msg);
        }

        public void v(String tag, String msg, Throwable throwable) {
            android.util.Log.v(tag, msg, throwable);
        }

        public void d(String tag, String msg) {
            android.util.Log.d(tag, msg);
        }

        public void d(String tag, String msg, Throwable throwable) {
            android.util.Log.d(tag, msg, throwable);
        }

        public void w(String tag, String msg) {
            android.util.Log.w(tag, msg);
        }

        public void w(String tag, String msg, Throwable throwable) {
            android.util.Log.w(tag, msg, throwable);
        }

        public void e(String tag, String msg) {
            android.util.Log.e(tag, msg);
        }

        public void e(String tag, String msg, Throwable throwable) {
            android.util.Log.e(tag, msg, throwable);
        }
    }

    public static synchronized void setPrinter(LogPrinter printer) {
        synchronized (Log.class) {
            if (printer == null) {
                a = new LogPrinterImpl();
            } else {
                a = printer;
            }
        }
    }

    public static void v(String tag, String msg) {
        a.v(tag, msg);
    }

    public static void v(String tag, String msg, Throwable throwable) {
        a.v(tag, msg, throwable);
    }

    public static void d(String tag, String msg) {
        a.d(tag, msg);
    }

    public static void d(String tag, String msg, Throwable throwable) {
        a.d(tag, msg, throwable);
    }

    public static void w(String tag, String msg) {
        a.w(tag, msg);
    }

    public static void w(String tag, String msg, Throwable throwable) {
        a.w(tag, msg, throwable);
    }

    public static void e(String tag, String msg) {
        a.e(tag, msg);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        a.e(tag, msg, throwable);
    }
}
