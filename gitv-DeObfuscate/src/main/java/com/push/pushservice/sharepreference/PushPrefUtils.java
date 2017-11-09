package com.push.pushservice.sharepreference;

import android.content.Context;
import android.text.TextUtils;
import com.push.pushservice.utils.DataUtil;
import com.push.pushservice.utils.LogUtils;

public class PushPrefUtils {
    private static final String APPID_KEY = "appId";
    private static final String APPVER_KEY = "appVer";
    private static final String APP_INFO_LIST_CHECK = "app_info_list_check";
    private static final String APP_INFO_LIST_JSON = "app_info_list_json";
    private static final String DEVICE_ID_PREF_KEY = "IM_Push_DeviceId";
    private static final String GLOBAL_VID_KEY = "globalVid";
    private static final String MSGID = "msgid";
    private static final String PACKAGE_NAME_KEY = "package_name";
    private static final String TAG = "PrefUtils";

    public static synchronized String getDeviceId(Context context) {
        String str;
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "getDeviceId error context = null");
                str = "";
            } else {
                str = PushPrefHelper.getString(context, DEVICE_ID_PREF_KEY, "");
            }
        }
        return str;
    }

    public static synchronized void setDeviceId(Context context, String deviceId) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setDeviceId error context = null");
            } else if (TextUtils.isEmpty(deviceId)) {
                LogUtils.logd(TAG, "setDeviceId error deviceId = null");
            } else {
                PushPrefHelper.putString(context, DEVICE_ID_PREF_KEY, deviceId);
            }
        }
    }

    public static synchronized String getAppInfoList(Context context) {
        String str;
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "getAppInfoList error context = null");
                str = "";
            } else {
                str = PushPrefHelper.getString(context, APP_INFO_LIST_JSON, "");
            }
        }
        return str;
    }

    public static synchronized void setAppInfoList(Context context, String appInfoList) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setAppInfoList error context = null");
            } else if (TextUtils.isEmpty(appInfoList)) {
                LogUtils.logd(TAG, "setAppInfoList error appInfoList = null");
            } else {
                PushPrefHelper.putString(context, APP_INFO_LIST_JSON, appInfoList);
            }
        }
    }

    public static synchronized String getAppInfoCheck(Context context) {
        String str;
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "getAppInfoCheck error context = null");
                str = "";
            } else {
                str = PushPrefHelper.getString(context, APP_INFO_LIST_CHECK, "");
            }
        }
        return str;
    }

    public static synchronized void setAppInfoCheck(Context context, String check) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setAppInfoCheck error context = null");
            } else if (TextUtils.isEmpty(check)) {
                LogUtils.logd(TAG, "setAppInfoCheck error appInfoList = null");
            } else {
                PushPrefHelper.putString(context, APP_INFO_LIST_CHECK, check);
            }
        }
    }

    public static synchronized int getAppId(Context context) {
        int defaultValue;
        synchronized (PushPrefUtils.class) {
            defaultValue = -1;
            if (context == null) {
                LogUtils.logd(TAG, "getAppId error context = null");
            } else {
                defaultValue = PushPrefHelper.getInt(context, APPID_KEY, -1);
            }
        }
        return defaultValue;
    }

    public static synchronized void setAppId(Context context, int appId) {
        synchronized (PushPrefUtils.class) {
            if (context == null || appId < 0) {
                LogUtils.logd(TAG, "setAppId error context = null appId = " + appId);
            } else {
                PushPrefHelper.putInt(context, APPID_KEY, appId);
            }
        }
    }

    public static synchronized long getMsgId(Context context) {
        long j = 0;
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "getMsgId error context = null");
            } else {
                j = PushPrefHelper.getLong(context, "msgid", 0);
            }
        }
        return j;
    }

    public static synchronized void setMsgId(Context context, long msgId) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setMsgId error context = null");
            } else {
                PushPrefHelper.putLong(context, "msgid", msgId);
            }
        }
    }

    public static synchronized String getAppVer(Context context) {
        String defaultValue;
        synchronized (PushPrefUtils.class) {
            defaultValue = "";
            if (context == null) {
                LogUtils.logd(TAG, "getAppVer error context = null");
            } else {
                defaultValue = PushPrefHelper.getString(context, "appVer", defaultValue);
            }
        }
        return defaultValue;
    }

    public static synchronized void setAppVer(Context context, String appVer) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setAppVer error context = null");
            } else if (TextUtils.isEmpty(appVer)) {
                LogUtils.logd(TAG, "setAppVer error appVer = null");
            } else {
                PushPrefHelper.putString(context, "appVer", appVer);
            }
        }
    }

    public static synchronized String getGlobalDeviceId(Context context) {
        String str;
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "getGlobalDeviceId error context = null");
                str = "";
            } else {
                str = PushPrefHelper.getString(context, GLOBAL_VID_KEY, "");
            }
        }
        return str;
    }

    public static synchronized void setGlobalDeviceId(Context context, String deviceId) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setGlobalDeviceId error context = null");
            } else if (TextUtils.isEmpty(deviceId)) {
                LogUtils.logd(TAG, "setGlobalDeviceId error deviceId = null");
            } else {
                PushPrefHelper.putString(context, GLOBAL_VID_KEY, deviceId);
            }
        }
    }

    public static synchronized String getPackageName(Context context) {
        String str;
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "getPackageName error context = null");
                str = "";
            } else {
                str = PushPrefHelper.getString(context, "package_name", "");
            }
        }
        return str;
    }

    public static synchronized void setPackageName(Context context, String packageName) {
        synchronized (PushPrefUtils.class) {
            if (context == null) {
                LogUtils.logd(TAG, "setPackageName error context = null");
            } else {
                PushPrefHelper.putString(context, "package_name", DataUtil.getNotNullString(packageName));
            }
        }
    }
}
