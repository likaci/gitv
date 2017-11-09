package com.gala.video.api.log;

public class ApiEngineLog {
    private static IApiEngineLogReport a;
    private static boolean f468a = false;

    public static void setDebugEnabled(boolean flag) {
        f468a = flag;
    }

    public static boolean getDebugEnabled() {
        return f468a;
    }

    public static void setApiEngineReport(IApiEngineLogReport report) {
        a = report;
    }

    public static void d(String tag, Object object) {
        if (f468a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("D/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("D/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (a != null) {
                a.reportLog(str);
            }
        }
    }

    public static void i(String tag, Object object) {
        if (f468a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("I/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("I/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (a != null) {
                a.reportLog(str);
            }
        }
    }

    public static void w(String tag, Object object) {
        if (f468a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("W/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("W/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (a != null) {
                a.reportLog(str);
            }
        }
    }

    public static void e(String tag, Object object) {
        if (f468a) {
            String str = object == null ? " the object to dump is null." : " " + object.toString();
            System.out.printf("E/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str}).println();
            str = String.format("E/(%s, %s):%s", new Object[]{Thread.currentThread().getName(), tag, str});
            if (a != null) {
                a.reportLog(str);
            }
        }
    }
}
