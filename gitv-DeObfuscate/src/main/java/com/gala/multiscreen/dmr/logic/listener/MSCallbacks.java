package com.gala.multiscreen.dmr.logic.listener;

import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.multiscreen.dmr.IStandardMSCallback;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;
import com.gala.multiscreen.dmr.util.RunningState;

public class MSCallbacks {
    private static IStandardMSCallback gDlnaCallback;
    private static IGalaMSExpand gGalaCallback;

    public static void registerGalaMS(IGalaMSExpand serverCallback) {
        MSLog.log("registerGalaMS registerGalaMS = " + serverCallback, LogType.PARAMETER);
        gGalaCallback = serverCallback;
        if (RunningState.isPhoneConnected) {
            gGalaCallback.onNotifyEvent(RequestKind.ONLINE, null);
        }
    }

    public static void unregisterGalaMS() {
        MSLog.log("unregisterGalaMS()", LogType.PARAMETER);
        gGalaCallback = null;
    }

    public static IGalaMSExpand getGalaMS() {
        return gGalaCallback;
    }

    public static void registerStandardMS(IStandardMSCallback dlnaCallback) {
        gDlnaCallback = dlnaCallback;
    }

    public static void unregisterStandardMS() {
        gDlnaCallback = null;
    }

    public static IStandardMSCallback getStandardMS() {
        return gDlnaCallback;
    }
}
