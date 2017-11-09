package com.gala.video.widget.util;

import android.os.Process;
import android.util.Log;

public class LogUtils {
    private static final int LEAST_LENGTH = 1;
    private static final String SEPARATOR = ",";
    private static final String TAG = "LogUtils";
    public static boolean mIsDebug = true;

    public static void setDebug(boolean isDebug) {
        mIsDebug = true;
        if (isDebug) {
            m1604i(TAG, "log is open");
        } else {
            m1604i(TAG, "log is close");
        }
        mIsDebug = isDebug;
    }

    public static void m1598d(String tag, Object object) {
        try {
            Log.d(tag, wrapLog(object.toString()));
        } catch (Exception e) {
        }
    }

    public static void m1604i(String tag, Object object) {
        try {
            Log.i(tag, wrapLog(object.toString()));
        } catch (Exception e) {
        }
    }

    public static void m1607w(String tag, Object object) {
        try {
            if (mIsDebug) {
                Log.w(tag, wrapLog(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m1601e(String tag, Object object) {
        try {
            if (mIsDebug) {
                Log.e(tag, wrapLog(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m1600d(Object... logs) {
        String packageLog = packageLog(logs);
        if (packageLog != null && logs != null && logs.length > 0) {
            m1598d(logs[0].toString(), packageLog);
        }
    }

    public static void m1606i(Object... logs) {
        String packageLog = packageLog(logs);
        if (packageLog != null && logs != null && logs.length > 0) {
            m1604i(logs[0].toString(), packageLog);
        }
    }

    public static void m1609w(Object... logs) {
        if (mIsDebug) {
            String packageLog = packageLog(logs);
            if (packageLog != null && logs != null && logs.length > 0) {
                m1607w(logs[0].toString(), packageLog);
            }
        }
    }

    public static void m1603e(Object... logs) {
        if (mIsDebug) {
            String packageLog = packageLog(logs);
            if (packageLog != null && logs != null && logs.length > 0) {
                m1601e(logs[0].toString(), packageLog);
            }
        }
    }

    public static void m1599d(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.d(tag, wrapLog(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void m1605i(String tag, Object object, Throwable t) {
        try {
            Log.i(tag, wrapLog(object.toString()), t);
        } catch (Exception e) {
        }
    }

    public static void m1608w(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.w(tag, wrapLog(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void m1602e(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.e(tag, wrapLog(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String packageLog(java.lang.Object... r5) {
        /*
        monitor-enter(r5);
        if (r5 == 0) goto L_0x0007;
    L_0x0003:
        r3 = r5.length;	 Catch:{ all -> 0x0019 }
        r4 = 1;
        if (r3 > r4) goto L_0x000a;
    L_0x0007:
        monitor-exit(r5);	 Catch:{ all -> 0x0019 }
        r3 = 0;
    L_0x0009:
        return r3;
    L_0x000a:
        r0 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0019 }
        r0.<init>();	 Catch:{ all -> 0x0019 }
        r2 = r5.length;	 Catch:{ all -> 0x0019 }
        r1 = 1;
    L_0x0011:
        if (r1 < r2) goto L_0x001c;
    L_0x0013:
        r3 = r0.toString();	 Catch:{ all -> 0x0019 }
        monitor-exit(r5);	 Catch:{ all -> 0x0019 }
        goto L_0x0009;
    L_0x0019:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0019 }
        throw r3;
    L_0x001c:
        r3 = r5[r1];	 Catch:{ all -> 0x0019 }
        r3 = r0.append(r3);	 Catch:{ all -> 0x0019 }
        r4 = " ";
        r3.append(r4);	 Catch:{ all -> 0x0019 }
        r1 = r1 + 1;
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.widget.util.LogUtils.packageLog(java.lang.Object[]):java.lang.String");
    }

    private static String wrapLog(String log) {
        return "[TID " + Process.myTid() + "] " + log;
    }
}
