package com.gala.video.app.epg.startup;

import com.gala.video.app.epg.AppStartCostTime;
import com.gala.video.app.epg.AppStartMode;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.stub.HostBuild;
import com.gala.video.app.stub.StartUpInfo;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.system.preference.AppPreference;

public class StartUpCostInfoProvider {
    private static final String TAG = "StartUpCostInfoProvider";
    public static volatile long mGalaApplicationCostTime = -1;
    private static volatile boolean mHasSendPingback = false;
    public static volatile long mHomeBuildCompletedTime = -1;
    public static volatile long mPreViewCompletedTime = -1;
    public static volatile long mStartUpCompletedTime = -1;
    private volatile boolean mIsHomeBuildCompleted = false;
    private volatile boolean mIsPreviewCompleted = false;

    private static class SingletonHelper {
        private static StartUpCostInfoProvider instance = new StartUpCostInfoProvider();

        private SingletonHelper() {
        }
    }

    public static StartUpCostInfoProvider getInstance() {
        return SingletonHelper.instance;
    }

    public void onPreviewCompleted(long time) {
        LogUtils.m1568d(TAG, "onPreviewCompleted, preview complete time : " + time);
        this.mIsPreviewCompleted = true;
        mPreViewCompletedTime = time;
        if (checkStartUpCompleted()) {
            onStartUpCompleted(time);
        }
    }

    public void onHomeBuildCompleted(long time) {
        LogUtils.m1568d(TAG, "onHomeBuildCompleted, home build complete time : " + time);
        this.mIsHomeBuildCompleted = true;
        mHomeBuildCompletedTime = time;
        if (checkStartUpCompleted()) {
            onStartUpCompleted(time);
        }
    }

    private void onStartUpCompleted(long time) {
        mStartUpCompletedTime = time;
        LogUtils.m1568d(TAG, "onStartUpCompleted, start up complete time : " + mStartUpCompletedTime);
        if (!mHasSendPingback) {
            sendStartupPingback();
        }
    }

    private boolean checkStartUpCompleted() {
        LogUtils.m1568d(TAG, "checkStartUpCompleted, is preview completed : " + this.mIsPreviewCompleted + ", is home build completed : " + this.mIsHomeBuildCompleted);
        return this.mIsPreviewCompleted && this.mIsHomeBuildCompleted;
    }

    private void sendStartupPingback() {
        LogUtils.m1568d(TAG, "sendStartupPingback");
        String START_UP_PINGBACK = "StartUpPingback";
        String APK_VERSION = "apk_version";
        AppPreference preference = new AppPreference(AppRuntimeEnv.get().getApplicationContext(), "StartUpPingback");
        String td = String.valueOf(AppStartCostTime.getStartCostTime());
        String homeBuildTd = String.valueOf(AppStartCostTime.getHomeBuildCostTime());
        String isPlugin = AppStartMode.IS_PLUGIN_MODE ? "1" : "0";
        String isFirstOpen = "0";
        CharSequence oldApkVersionStr = preference.get("apk_version", "");
        String currentAPKVersionStr = HostBuild.getHostVersion();
        LogUtils.m1568d(TAG, "old version :" + oldApkVersionStr + ", current apk version : " + currentAPKVersionStr);
        if (StringUtils.isEmpty(oldApkVersionStr)) {
            isFirstOpen = "1";
        } else if (oldApkVersionStr.equals(currentAPKVersionStr)) {
            isFirstOpen = "0";
        } else {
            isFirstOpen = "1";
        }
        HomePingbackFactory.instance().createPingback(CommonPingback.START_UP_PINGBACK).addItem(Keys.f2035T, "11").addItem("ct", "160602_load").addItem("td", td).addItem(Keys.HOMEBUILDTD, homeBuildTd).addItem(Keys.ISPLUGIN, isPlugin).addItem(Keys.FIRSTOPEN, isFirstOpen).addItem(Keys.LDTYPE, "homepage").addItem(Keys.FIRSTLOAD, StartUpInfo.get().getPluginState()).post();
        mHasSendPingback = true;
        if (StringUtils.isEmpty(oldApkVersionStr) || !oldApkVersionStr.equals(currentAPKVersionStr)) {
            preference.save("apk_version", currentAPKVersionStr);
        }
    }
}
