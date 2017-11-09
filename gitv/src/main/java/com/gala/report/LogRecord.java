package com.gala.report;

import android.util.Log;
import com.gala.report.core.log.ILogCore;
import com.gala.report.msghandler.IMsgHandlerCore;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;

public class LogRecord {
    private static final String TAG = "LogRecord";
    private static ILogCore mLogCore;
    private static IMsgHandlerCore mMsgHandlerCore;

    public static void v(String s, String s1) {
    }

    public static void d(String s, String s1) {
    }

    public static void i(String s, String s1) {
    }

    public static void w(String s, String s1) {
    }

    public static void e(String s, String s1) {
    }

    public static void a(String s, String s1) {
    }

    public static void v(String s, String s1, Throwable throwable) {
    }

    public static void d(String s, String s1, Throwable throwable) {
    }

    public static void i(String s, String s1, Throwable throwable) {
    }

    public static void w(String s, String s1, Throwable throwable) {
    }

    public static void e(String s, String s1, Throwable throwable) {
    }

    public static void a(String s, String s1, Throwable throwable) {
    }

    public static void setMsgHandlerCore(IMsgHandlerCore msgHandlerCore) {
        mMsgHandlerCore = msgHandlerCore;
    }

    public static void sendHostStatus(HOSTMODULE hostmodule, HOSTSTATUS hoststatus) {
        if (mMsgHandlerCore != null) {
            mMsgHandlerCore.sendHostStatus(hostmodule, hoststatus);
        } else {
            Log.v(TAG, "mMsgHandlerCore is null");
        }
    }

    public static void sendPushMessage(String content) {
        if (mMsgHandlerCore != null) {
            mMsgHandlerCore.sendPushMessage(content);
        } else {
            Log.v(TAG, "mMsgHandlerCore is null");
        }
    }

    public static void setLogCore(ILogCore logcore) {
        mLogCore = logcore;
    }

    public static void snapShot() {
        if (mLogCore != null) {
            mLogCore.snapShot();
        } else {
            Log.v(TAG, "mLogCore is null");
        }
    }
}
