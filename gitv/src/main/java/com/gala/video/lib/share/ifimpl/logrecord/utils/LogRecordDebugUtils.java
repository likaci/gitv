package com.gala.video.lib.share.ifimpl.logrecord.utils;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;

public class LogRecordDebugUtils {
    public static final String PROP_LOGRECORD_TEST_EXCEPTION_F00001 = "gala.logrecord.test.ex.f00001";
    public static final String PROP_LOGRECORD_TEST_EXCEPTION_F00002 = "gala.logrecord.test.ex.f00002";
    public static final String PROP_LOGRECORD_TEST_EXCEPTION_F00003 = "gala.logrecord.test.ex.f00003";
    public static final String PROP_LOGRECORD_TEST_EXCEPTION_F10000 = "gala.logrecord.test.ex.f10000";
    private static final String TAG = "LogRecord/Utils/LogRecordDebugUtils";

    static {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "allowDebug() returns false");
        }
    }

    private static boolean allowDebug() {
        return false;
    }

    public static boolean testLogRecordExceptionForF10000() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_LOGRECORD_TEST_EXCEPTION_F10000, false) : false;
    }

    public static boolean testLogRecordExceptionForF00001() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_LOGRECORD_TEST_EXCEPTION_F00001, false) : false;
    }

    public static boolean testLogRecordExceptionForF00002() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_LOGRECORD_TEST_EXCEPTION_F00002, false) : false;
    }

    public static boolean testLogRecordExceptionForF00003() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_LOGRECORD_TEST_EXCEPTION_F00003, false) : false;
    }
}
