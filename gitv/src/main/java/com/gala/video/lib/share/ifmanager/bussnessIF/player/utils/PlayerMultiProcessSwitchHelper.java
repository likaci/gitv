package com.gala.video.lib.share.ifmanager.bussnessIF.player.utils;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.system.preference.AppPreference;

public class PlayerMultiProcessSwitchHelper {
    public static final String SHARED_PREF_PLAYER_MULTI_PROC = "player_multi_proc";
    public static final int SWITCH_AUTO = 0;
    public static final int SWITCH_CLOSE = 2;
    public static final int SWITCH_OPEN = 1;

    public static void saveSwitchValue(boolean value) {
        new AppPreference(AppRuntimeEnv.get().getApplicationContext(), SHARED_PREF_PLAYER_MULTI_PROC).save(SHARED_PREF_PLAYER_MULTI_PROC, value);
    }

    public static boolean getSwitchValue() {
        return new AppPreference(AppRuntimeEnv.get().getApplicationContext(), SHARED_PREF_PLAYER_MULTI_PROC).getBoolean(SHARED_PREF_PLAYER_MULTI_PROC, false);
    }

    public static void debugPlayerMultiProcess() {
        int debugValue = PlayerDebugUtils.getPlayerMultiProcSwitcher();
        if (debugValue == 1) {
            saveSwitchValue(true);
        } else if (debugValue == 2) {
            saveSwitchValue(false);
        }
    }
}
