package com.gala.video.app.stub.host;

import android.content.Context;

public class HostPreferenceHelper {
    private static final String NAME = "hostPreference";
    private static final String NEW_USER = "newhostuser";
    private static final String START_TIME = "startuptime";

    public static boolean isNewUser(Context ctx) {
        Preference preference = new Preference(ctx, NAME);
        boolean isNewUser = preference.getBoolean(NEW_USER, true);
        if (isNewUser) {
            preference.save(NEW_USER, false);
        }
        return isNewUser;
    }

    public static void setStartTime(Context ctx, long time) {
        new Preference(ctx, NAME).save(START_TIME, time);
    }

    public static void setIsNewHost(Context ctx) {
        new Preference(ctx, NAME).save(NEW_USER, true);
    }

    public static boolean getIsNewUserWithoutSave(Context ctx) {
        return new Preference(ctx, NAME).getBoolean(NEW_USER, true);
    }

    public static void setNewUserWithFirstStart(Context ctx) {
        new Preference(ctx, NAME).save(NEW_USER, false);
    }
}
