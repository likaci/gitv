package com.gala.albumprovider.util;

import android.os.Process;
import android.util.Log;

public class USALog {
    public static boolean mIsDebug = true;

    public static void setDebug(boolean isDebug) {
        mIsDebug = true;
        if (isDebug) {
            i("AlbumProvider", "log is open");
        } else {
            i("AlbumProvider", "log is close");
        }
        mIsDebug = isDebug;
    }

    public static void d(Object object) {
        try {
            if (mIsDebug) {
                Log.d("AlbumProvider", a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void i(Object object) {
        try {
            if (mIsDebug) {
                Log.i("AlbumProvider", a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void w(Object object) {
        try {
            if (mIsDebug) {
                Log.w("AlbumProvider", a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void e(Object object) {
        try {
            if (mIsDebug) {
                Log.e("AlbumProvider", a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void d(Object... logs) {
        if (a(logs) != null && logs != null && logs.length > 0) {
            d(logs[0].toString(), r0);
        }
    }

    public static void i(Object... logs) {
        if (a(logs) != null && logs != null && logs.length > 0) {
            i(logs[0].toString(), r0);
        }
    }

    public static void w(Object... logs) {
        if (mIsDebug && a(logs) != null && logs != null && logs.length > 0) {
            w(logs[0].toString(), r0);
        }
    }

    public static void e(Object... logs) {
        if (mIsDebug && a(logs) != null && logs != null && logs.length > 0) {
            e(logs[0].toString(), r0);
        }
    }

    public static void d(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.d(tag, a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void i(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.i(tag, a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void w(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.w(tag, a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void e(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.e(tag, a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    private static String a(Object... objArr) {
        String stringBuilder;
        int i = 1;
        synchronized (objArr) {
            if (objArr != null) {
                if (objArr.length > 1) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    int length = objArr.length;
                    while (i < length) {
                        stringBuilder2.append(objArr[i]).append(" ");
                        i++;
                    }
                    stringBuilder = stringBuilder2.toString();
                }
            }
            stringBuilder = null;
        }
        return stringBuilder;
    }

    private static String a(String str) {
        return "[TID " + Process.myTid() + "] " + str;
    }
}
