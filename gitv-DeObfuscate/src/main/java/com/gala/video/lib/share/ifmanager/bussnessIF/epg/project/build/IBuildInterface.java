package com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IBuildInterface extends IInterfaceWrapper {

    public static abstract class Wrapper implements IBuildInterface {
        public Object getInterface() {
            return this;
        }

        public static IBuildInterface asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IBuildInterface)) {
                return null;
            }
            return (IBuildInterface) wrapper;
        }
    }

    boolean enableExtraPage();

    String getAdPlayerId();

    String getApkThirdVersionCode();

    String getAppStorePkgName();

    String getBroadcastActions();

    String getBuildTime();

    String getCustomerName();

    String[] getCustomerPkgName();

    String getDomainName();

    String getForceOpen4kFlag();

    String getKeyboardType();

    String getMediaPlayerTypeConfig();

    String getOpenApiOldUuid();

    String getOpenapiFeatureList();

    String getPackageName();

    String getPingbackP2();

    String getProductName();

    String getShowVersion();

    String getThirdVersion();

    String getUIVersionName();

    int getVersionCode();

    String getVersionName();

    String getVersionString();

    String getVrsUUID();

    boolean isCheckMonkeyOpen();

    boolean isCollectNetDocInfoWhenPlaybackError();

    boolean isDisableServiceBootup();

    boolean isEnableDolby();

    boolean isEnableH265();

    boolean isEnableHCDNPreDeploy();

    boolean isEnabledVipAnimation();

    boolean isForceAdvanceMode();

    boolean isGitvUI();

    boolean isHomeVersion();

    boolean isInitCrashHandler();

    boolean isIsSupportScreenSaver();

    boolean isLitchi();

    boolean isMatchDevice();

    boolean isNewAppUpgrade();

    boolean isNoLogoUI();

    boolean isOpenAPIVersion();

    boolean isOpenCheckInFun();

    boolean isOpenKeyboardLogin();

    boolean isOpenMessageCenter();

    boolean isOpenTestPerformance();

    boolean isOpencheckInRecommend();

    boolean isPingbackDebug();

    boolean isPreferSystemPlayerFor4K();

    boolean isShowLive();

    boolean isSupportAlbumDetailWindowPlay();

    boolean isSupportAndroidCache();

    boolean isSupportCarousel();

    boolean isSupportContentProvider();

    boolean isSupportDesktopManage();

    boolean isSupportMonkeyTest();

    boolean isSupportRecommendApp();

    boolean isSupportSkin();

    boolean isSupportSmallWindowPlay();

    boolean isSupportSubscribe();

    boolean isSupportVipRightsActivation();

    boolean isSupportVoice();

    boolean isSupportVoiceTest();

    boolean isTestErrorCodeAndUpgrade();

    boolean isUseAlbumListCache();

    boolean isUsePlayerWhiteList();

    boolean shouldAuthMac();

    boolean shouldCacheDeviceCheck();

    boolean shouldShowVolume();

    boolean supportPlayerMultiProcess();
}
