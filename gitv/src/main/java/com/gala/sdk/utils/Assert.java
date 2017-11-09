package com.gala.sdk.utils;

public class Assert {
    private Assert() {
    }

    public static void assertTrue(boolean value, String msg) {
        if (!value) {
            throw new RuntimeException("assertTrue(" + value + ") fail! " + msg);
        } else if (Log.DEBUG) {
            Log.d("PlayerUtils/Assert", "assertTrue() pass! msg=" + msg);
        }
    }
}
