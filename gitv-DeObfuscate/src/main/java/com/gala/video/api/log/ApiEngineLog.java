package com.gala.video.api.log;

public class ApiEngineLog {
    private static IApiEngineLogReport f1893a;
    private static boolean f1894a = false;

    public static void setDebugEnabled(boolean flag) {
        f1894a = flag;
    }

    public static boolean getDebugEnabled() {
        return f1894a;
    }

    public static void setApiEngineReport(IApiEngineLogReport report) {
        f1893a = report;
    }

    public static void m1530d(String tag, Object object) {
        if (f1894a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("D/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("D/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (f1893a != null) {
                f1893a.reportLog(str);
            }
        }
    }

    public static void m1532i(String tag, Object object) {
        if (f1894a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("I/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("I/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (f1893a != null) {
                f1893a.reportLog(str);
            }
        }
    }

    public static void m1533w(String tag, Object object) {
        if (f1894a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("W/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("W/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (f1893a != null) {
                f1893a.reportLog(str);
            }
        }
    }

    public static void m1531e(String tag, Object object) {
        if (f1894a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("E/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("E/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (f1893a != null) {
                f1893a.reportLog(str);
            }
        }
    }
}
