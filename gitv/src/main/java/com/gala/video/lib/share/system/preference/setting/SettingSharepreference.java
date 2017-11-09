package com.gala.video.lib.share.system.preference.setting;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.system.preference.AppPreference;

public class SettingSharepreference {
    private static final String DEFAULT_DEVICE_NAME = "客厅的电视";
    private static final String DEFAULT_DEVICE_NAME_SUFFIX = "电视";
    private static final String DEFAULT_SCREEN_TIME = "4分钟";
    private static final String PREFERENCE_NAME = "setting_preferences_name";
    private static final String RESULT_BEGINEND = "play_begin_end";
    private static final String RESULT_DEVICE = "device_name";
    private static final String RESULT_DEVICE_SUFFIX = "device_name_suffix";
    private static final String RESULT_DORMANT = "device_dormant";
    private static final String RESULT_FRAMERATIO = "play_frameratio";
    private static final String RESULT_NETWORK_SPEED = "speed";
    private static final String RESULT_SCOPECONTROL = "scope_control";
    private static final String RESULT_SCREEN = "device_screen";
    private static final String RESULT_SCREEN_SAVER = "screen_saver";
    private static final String RESULT_SHARPNESS = "play_sharpness";
    private static final String RESULT_VOICEPATTERN = "voice_pattern";
    private static String mScreenSaverInfo = "";

    public static void saveNetSpeedResult(Context context, String result) {
        new AppPreference(context, PREFERENCE_NAME).save("speed", result);
    }

    public static void saveDeviceNameResult(Context context, String result) {
        if (MultiScreen.get().isSupportMS()) {
            MultiScreen.get().setDeviceName(result);
        }
        new AppPreference(context, PREFERENCE_NAME).save(RESULT_DEVICE, result);
    }

    public static void saveDeviceNameSuffix(Context context, String result) {
        new AppPreference(context, PREFERENCE_NAME).save(RESULT_DEVICE_SUFFIX, result);
    }

    public static void setResultScreenSaver(Context context, String result) {
        new AppPreference(context, PREFERENCE_NAME).save(RESULT_SCREEN_SAVER, result);
        mScreenSaverInfo = result;
    }

    public static String getResultScreenSaver(Context context) {
        if (StringUtils.isEmpty(mScreenSaverInfo)) {
            mScreenSaverInfo = new AppPreference(context, PREFERENCE_NAME).get(RESULT_SCREEN_SAVER, DEFAULT_SCREEN_TIME);
        }
        return mScreenSaverInfo;
    }

    public static String getSharpness(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_SHARPNESS, "");
    }

    public static boolean getBeginEnd(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).getBoolean(RESULT_BEGINEND, false);
    }

    public static String getFrameRatio(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_FRAMERATIO, "");
    }

    public static String getVoicePattern(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_VOICEPATTERN, "");
    }

    public static String getScopeControl(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_SCOPECONTROL, "");
    }

    public static String getNetworkSpeedResult(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get("speed", "");
    }

    public static String getDeviceName(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_DEVICE, DEFAULT_DEVICE_NAME);
    }

    public static String getDeviceNameSuffix(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_DEVICE_SUFFIX, DEFAULT_DEVICE_NAME_SUFFIX);
    }

    public static String getDeviceScreen(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_SCREEN, "");
    }

    public static String getDeviceDormant(Context context) {
        return new AppPreference(context, PREFERENCE_NAME).get(RESULT_DORMANT, "");
    }
}
