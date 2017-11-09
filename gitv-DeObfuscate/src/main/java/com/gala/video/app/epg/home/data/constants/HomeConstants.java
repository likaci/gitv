package com.gala.video.app.epg.home.data.constants;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.homeconstants.IHomeConstants.Wrapper;

public final class HomeConstants extends Wrapper {
    public static final int TAB_APP_LOCAL = 5;
    public static final int TAB_APP_OPERATE = 7;
    public static final int TAB_APP_STORE = 6;
    public static final int TAB_CAROUSEL = 1;
    public static final int TAB_CHANNEL = 4;
    public static final int TAB_DYNAMIC_TEMPLATE_10 = 10;
    public static final int TAB_DYNAMIC_TEMPLATE_9 = 9;
    public static final int TAB_PAGE_BASE_INDEX = 1;
    public static final int TAB_RECOMMEND = 2;
    public static final int TAB_RECOMMEND_CHANNEL = 3;
    public static final int TAB_SETTING = 8;
    public static final int TAB_SKYWORTH_MUSIC = 11;
    private static final HomeConstants mInstance = new HomeConstants();
    public static boolean mIsStartPreViewFinished = false;

    public static HomeConstants getInstance() {
        return mInstance;
    }

    public boolean isIsStartPreViewFinished() {
        return mIsStartPreViewFinished;
    }
}
