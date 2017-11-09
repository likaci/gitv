package com.tvos.appdetailpage.utils;

public class TagKeyUtil {
    private static final int TAG_KEY_ORIGINAL = Integer.MAX_VALUE;
    private static int mCount = 232;

    public static int generateTagKey() {
        int i = mCount + 1;
        mCount = i;
        return TAG_KEY_ORIGINAL - i;
    }
}
