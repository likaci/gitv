package com.gala.multiscreen.dmr.util;

import android.util.Log;
import org.cybergarage.util.Debug;

public class MSLog {

    public enum LogType {
        MS_FROM_PHONE,
        MS_TO_PHONE,
        PARAMETER,
        ERROR,
        QUICK_MSG,
        BASE,
        SEEK
    }

    public static void log(String message) {
        Log.e("MultiScreen", message);
    }

    public static void log(String msg, LogType logType) {
        switch (logType) {
            case PARAMETER:
                Log.e("MultiScreen/SetBaseParameter", msg);
                return;
            case MS_FROM_PHONE:
                Log.e("MultiScreen/MSG/PhoneSendMessage", msg);
                return;
            case MS_TO_PHONE:
                Log.e("MultiScreen/MSG/SendMessageToPhone", msg);
                return;
            case ERROR:
                Log.e("MultiScreen/Error", msg);
                return;
            case QUICK_MSG:
                Log.e("MultiScreen/MSG/QucikMessage", msg);
                return;
            case BASE:
                Log.e("MultiScreen/Base", msg);
                return;
            case SEEK:
                Log.e("MultiScreen/Seek", msg);
                return;
            default:
                return;
        }
    }

    public static void setDlnaLogEnabled(boolean isEnabled) {
        log("isEnabled=" + isEnabled, LogType.PARAMETER);
        if (isEnabled) {
            Debug.on();
        } else {
            Debug.off();
        }
        log("org.cybergarage.util.Debug=" + Debug.isOn(), LogType.PARAMETER);
    }
}
