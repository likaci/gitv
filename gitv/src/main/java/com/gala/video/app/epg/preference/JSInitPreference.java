package com.gala.video.app.epg.preference;

import android.content.Context;
import com.gala.video.lib.share.system.preference.AppPreference;

public class JSInitPreference {
    private static final String FIRST_LOAD_DATE = "first_load_date";
    private static final String NAME = "jsConfigInit";

    public static void saveFirstLoadDate(Context ctx, boolean isFirst) {
        new AppPreference(ctx, NAME).save(FIRST_LOAD_DATE, isFirst);
    }

    public static boolean isFirstLoadDate(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(FIRST_LOAD_DATE, true);
    }
}
