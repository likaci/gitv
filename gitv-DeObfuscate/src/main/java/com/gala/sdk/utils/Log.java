package com.gala.sdk.utils;

public class Log {
    public static boolean DEBUG = true;
    private static LogPrinter f701a = new LogPrinterImpl();

    public interface LogPrinter {
        void mo772d(String str, String str2);

        void mo773d(String str, String str2, Throwable th);

        void mo774e(String str, String str2);

        void mo775e(String str, String str2, Throwable th);

        void mo776v(String str, String str2);

        void mo777v(String str, String str2, Throwable th);

        void mo778w(String str, String str2);

        void mo779w(String str, String str2, Throwable th);
    }

    public static class LogPrinterImpl implements LogPrinter {
        public void mo776v(String tag, String msg) {
            android.util.Log.v(tag, msg);
        }

        public void mo777v(String tag, String msg, Throwable throwable) {
            android.util.Log.v(tag, msg, throwable);
        }

        public void mo772d(String tag, String msg) {
            android.util.Log.d(tag, msg);
        }

        public void mo773d(String tag, String msg, Throwable throwable) {
            android.util.Log.d(tag, msg, throwable);
        }

        public void mo778w(String tag, String msg) {
            android.util.Log.w(tag, msg);
        }

        public void mo779w(String tag, String msg, Throwable throwable) {
            android.util.Log.w(tag, msg, throwable);
        }

        public void mo774e(String tag, String msg) {
            android.util.Log.e(tag, msg);
        }

        public void mo775e(String tag, String msg, Throwable throwable) {
            android.util.Log.e(tag, msg, throwable);
        }
    }

    public static synchronized void setPrinter(LogPrinter printer) {
        synchronized (Log.class) {
            if (printer == null) {
                f701a = new LogPrinterImpl();
            } else {
                f701a = printer;
            }
        }
    }

    public static void m458v(String tag, String msg) {
        f701a.mo776v(tag, msg);
    }

    public static void m459v(String tag, String msg, Throwable throwable) {
        f701a.mo777v(tag, msg, throwable);
    }

    public static void m454d(String tag, String msg) {
        f701a.mo772d(tag, msg);
    }

    public static void m455d(String tag, String msg, Throwable throwable) {
        f701a.mo773d(tag, msg, throwable);
    }

    public static void m460w(String tag, String msg) {
        f701a.mo778w(tag, msg);
    }

    public static void m461w(String tag, String msg, Throwable throwable) {
        f701a.mo779w(tag, msg, throwable);
    }

    public static void m456e(String tag, String msg) {
        f701a.mo774e(tag, msg);
    }

    public static void m457e(String tag, String msg, Throwable throwable) {
        f701a.mo775e(tag, msg, throwable);
    }
}
