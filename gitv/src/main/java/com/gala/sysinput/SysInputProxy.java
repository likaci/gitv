package com.gala.sysinput;

import com.gala.multiscreen.dmr.util.MSLog;

public class SysInputProxy {
    public static boolean isEnable() {
        boolean enable = false;
        if (SysInput.sLoadSuccess) {
            enable = SysInput.isEnable();
        }
        MSLog.log("SysInputProxy.isEnable() load libso " + SysInput.sLoadSuccess + ", return " + enable);
        return enable;
    }

    public static void reset() {
        if (SysInput.sLoadSuccess) {
            SysInput.reset();
        }
    }

    public static void setKeyEvent(int keyEvent) {
        if (SysInput.sLoadSuccess) {
            SysInput.setKeyEvent(keyEvent);
        }
    }
}
