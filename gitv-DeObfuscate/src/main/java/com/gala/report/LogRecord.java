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

    public static void m398v(String s, String s1) {
    }

    public static void m392d(String s, String s1) {
    }

    public static void m396i(String s, String s1) {
    }

    public static void m400w(String s, String s1) {
    }

    public static void m394e(String s, String s1) {
    }

    public static void m390a(String s, String s1) {
    }

    public static void m399v(String s, String s1, Throwable throwable) {
    }

    public static void m393d(String s, String s1, Throwable throwable) {
    }

    public static void m397i(String s, String s1, Throwable throwable) {
    }

    public static void m401w(String s, String s1, Throwable throwable) {
    }

    public static void m395e(String s, String s1, Throwable throwable) {
    }

    public static void m391a(String s, String s1, Throwable throwable) {
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
