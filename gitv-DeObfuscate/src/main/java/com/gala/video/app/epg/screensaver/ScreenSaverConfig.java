package com.gala.video.app.epg.screensaver;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;

class ScreenSaverConfig {
    private static final long EIGHT_DELAY_TIME = 480000;
    private static final long ERROR_DELAY_TIME = -1024;
    private static final long FOUR_DELAY_TIME = 240000;
    private static final long TWELVE_DELAY_TIME = 720000;
    private boolean mEnable = true;
    private boolean mIsEnableInSettings = true;
    private long mScreenSaverDelayTime = FOUR_DELAY_TIME;

    ScreenSaverConfig() {
    }

    boolean isEnableInSetting() {
        return this.mIsEnableInSettings;
    }

    boolean isEnable() {
        return this.mEnable;
    }

    void setEnable(boolean v) {
        this.mEnable = v;
    }

    long getScreenSaverDelayTime() {
        return this.mScreenSaverDelayTime;
    }

    void readScreenSaverSettingData() {
        this.mScreenSaverDelayTime = FOUR_DELAY_TIME;
        this.mIsEnableInSettings = true;
        String info = SettingSharepreference.getResultScreenSaver(AppRuntimeEnv.get().getApplicationContext());
        if (StringUtils.equals(info, "8分钟")) {
            this.mScreenSaverDelayTime = EIGHT_DELAY_TIME;
        } else if (StringUtils.equals(info, "12分钟")) {
            this.mScreenSaverDelayTime = TWELVE_DELAY_TIME;
        } else if (StringUtils.equals(info, "关闭")) {
            this.mIsEnableInSettings = false;
            this.mScreenSaverDelayTime = ERROR_DELAY_TIME;
        }
    }

    @Deprecated
    private String convertTimeInfo() {
        String time = "240000";
        String info = SettingSharepreference.getResultScreenSaver(AppRuntimeEnv.get().getApplicationContext());
        if (StringUtils.equals(info, "8分钟")) {
            return "480000";
        }
        if (StringUtils.equals(info, "12分钟")) {
            return "720000";
        }
        if (StringUtils.equals(info, "关闭")) {
            return "0";
        }
        return time;
    }
}
