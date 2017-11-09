package com.gala.video.app.epg.preference;

import android.content.Context;
import com.gala.video.lib.share.system.preference.AppPreference;

public class StartUpPreferrence {
    public static final String KEY_START_UP_VIDEO_CRACH_TIME = "start_up_video_crash";
    private static final String NAME = "StartUpPreference";

    public static void saveCrashTimes(Context context, String Key, int count) {
        new AppPreference(context, NAME).save(Key, count);
    }

    public static int getCrashTimes(Context context, String Key) {
        return new AppPreference(context, NAME).getInt(Key, 0);
    }
}
