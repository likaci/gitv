package tv.gitv.ptqy.security.fingerprint;

public final class LogMgr {
    private static final String DefaultTag = "galarytv_dfp";
    private static final String SEPERATE = "=========";
    private static final boolean mDevelopMode = false;

    private LogMgr() {
    }

    public static boolean isDevelopMode() {
        return false;
    }

    public static int v(String tag, String msg) {
        return 0;
    }

    public static int v(String tag, String msg, Throwable tr) {
        return 0;
    }

    public static int d(String tag, String msg) {
        return 0;
    }

    public static int d(String tag, String msg, Throwable tr) {
        return 0;
    }

    public static int i(String tag, String msg) {
        return 0;
    }

    public static int i(String msg) {
        return 0;
    }

    public static int i(String tag, String msg, Throwable tr) {
        return 0;
    }

    public static void e(String tag, String msg, Throwable tr) {
    }

    public static void d(String tag, Throwable e) {
    }

    public static void e(String tag, Throwable e) {
    }

    public static void e(String tag, String log) {
    }
}
