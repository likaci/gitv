package com.gala.albumprovider.util;

import android.os.Process;
import android.util.Log;

public class USALog {
    public static boolean mIsDebug = true;

    public static void setDebug(boolean isDebug) {
        mIsDebug = true;
        if (isDebug) {
            m155i("AlbumProvider", "log is open");
        } else {
            m155i("AlbumProvider", "log is close");
        }
        mIsDebug = isDebug;
    }

    public static void m147d(Object object) {
        try {
            if (mIsDebug) {
                Log.d("AlbumProvider", m145a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m153i(Object object) {
        try {
            if (mIsDebug) {
                Log.i("AlbumProvider", m145a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m156w(Object object) {
        try {
            if (mIsDebug) {
                Log.w("AlbumProvider", m145a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m150e(Object object) {
        try {
            if (mIsDebug) {
                Log.e("AlbumProvider", m145a(object.toString()));
            }
        } catch (Exception e) {
        }
    }

    public static void m149d(Object... logs) {
        if (m146a(logs) != null && logs != null && logs.length > 0) {
            m149d(logs[0].toString(), r0);
        }
    }

    public static void m155i(Object... logs) {
        if (m146a(logs) != null && logs != null && logs.length > 0) {
            m155i(logs[0].toString(), r0);
        }
    }

    public static void m158w(Object... logs) {
        if (mIsDebug && m146a(logs) != null && logs != null && logs.length > 0) {
            m158w(logs[0].toString(), r0);
        }
    }

    public static void m152e(Object... logs) {
        if (mIsDebug && m146a(logs) != null && logs != null && logs.length > 0) {
            m152e(logs[0].toString(), r0);
        }
    }

    public static void m148d(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.d(tag, m145a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void m154i(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.i(tag, m145a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void m157w(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.w(tag, m145a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    public static void m151e(String tag, Object object, Throwable t) {
        try {
            if (mIsDebug) {
                Log.e(tag, m145a(object.toString()), t);
            }
        } catch (Exception e) {
        }
    }

    private static String m146a(Object... objArr) {
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

    private static String m145a(String str) {
        return "[TID " + Process.myTid() + "] " + str;
    }
}
