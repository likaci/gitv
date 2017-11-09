package com.gala.video.app.epg.preference;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.AppPreference;

public class GuidePreference {
    private static final String GUIDE_ISFIRST_INSTALL = "isFirstInstall_v62";
    private static final String NAME = "guide_pref";
    private static final String SCENE_GUIDE = "start_up_scene_guide_desktop_v7_8";
    private static final String SIGIN_GUIDE = "sign_key";
    private static final String START_COUNT = "sign_start_count";
    private static final String TAG = "EPG/system/GuidePreference";

    public static boolean isShowGuideLoad(Context context) {
        AppPreference preference = new AppPreference(context, NAME);
        LogUtils.m1574i(TAG, "isShowGuideLead()");
        return preference.getBoolean(GUIDE_ISFIRST_INSTALL, true);
    }

    public static void setShowGuideLoad(Context context, boolean isShow) {
        new AppPreference(context, NAME).save(GUIDE_ISFIRST_INSTALL, isShow);
        LogUtils.m1574i(TAG, "setShowGuideLead() -> isShow:" + isShow);
    }

    public static boolean shouldShowSceneGuide(Context context) {
        return new AppPreference(context, NAME).getBoolean(SCENE_GUIDE, true);
    }

    public static void saveShowSceneGuide(Context context, boolean isShow) {
        new AppPreference(context, NAME).save(SCENE_GUIDE, isShow);
    }

    public static boolean shouldShowSignGuide(Context context) {
        return new AppPreference(context, NAME).getBoolean(SIGIN_GUIDE, true);
    }

    public static void saveShowSignGuide(Context context, boolean isShow) {
        new AppPreference(context, NAME).save(SIGIN_GUIDE, isShow);
    }

    public static void saveStartCount(Context context, int count) {
        new AppPreference(context, NAME).save(START_COUNT, count);
    }

    public static int getStartCount(Context context) {
        return new AppPreference(context, NAME).getInt(START_COUNT, 0);
    }
}
