package com.gala.video.app.epg.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class ActivityUtils {
    public static void replaceFragment(FragmentManager fragmentManager, Fragment fragment, int frameId) {
        checkNotNull(fragmentManager);
        checkNotNull(fragment);
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static <T> T checkNotNull(T reference) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException();
    }

    public static <T> T checkNotNull(T reference, String message) {
        if (reference != null) {
            return reference;
        }
        throw new NullPointerException(message);
    }
}
