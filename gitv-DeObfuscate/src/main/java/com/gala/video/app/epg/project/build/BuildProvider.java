package com.gala.video.app.epg.project.build;

import android.content.Context;
import android.content.pm.PackageInfo;
import com.gala.appmanager.GalaAppManager;
import com.gala.video.app.stub.HostBuild;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface.Wrapper;

public class BuildProvider extends Wrapper {
    private static final String APP_XML_PATH = "/system/etc/position.xml";
    private static final String CUSTOMER_PKGNAME_SPLIT = ";";
    private static final String GITV_DOMAIN_NAME = "ptqy.gitv.tv";
    private BuildInit mBuildInit = new BuildInit();
    private Context mContext = AppRuntimeEnv.get().getApplicationContext();

    public BuildProvider() {
        setup();
    }

    private void setup() {
        GalaAppManager.setApkType(isHomeVersion());
        if (isHomeVersion()) {
            GalaAppManager.setXmlPath(APP_XML_PATH);
        }
    }

    public boolean isMatchDevice() {
        return this.mBuildInit.isMatchDevice();
    }

    private boolean getBoolean(String name) {
        return "true".equalsIgnoreCase(name);
    }

    public String getVrsUUID() {
        return BuildCache.getInstance().getString(BuildConstance.VRS_UUID, BuildDefaultDocument.VRS_UUID);
    }

    public String getPackageName() {
        String buildCfgPkgName = BuildCache.getInstance().getString(BuildConstance.APK_PACKAGE_NAME, AppEnvConstant.DEF_PKG_NAME);
        return StringUtils.isEmpty(buildCfgPkgName.trim()) ? AppEnvConstant.DEF_PKG_NAME : buildCfgPkgName;
    }

    public boolean isSupportAndroidCache() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_ANDROIDCACHE, "false"));
    }

    public String getVersionString() {
        return BuildHelper.getVersionString();
    }

    public String getShowVersion() {
        return AppClientUtils.getVersion(this.mContext, true, "", getThirdVersion(), getVersionCode() + "");
    }

    public String getVersionName() {
        String versionName = "7.6.999.6905";
        PackageInfo pi = getPackageInfo();
        if (pi != null) {
            return pi.versionName;
        }
        return versionName;
    }

    public String getThirdVersion() {
        return BuildCache.getInstance().getString(BuildConstance.APK_VERSION, "0");
    }

    public int getVersionCode() {
        int versionCode = 0;
        String revision = BuildCache.getInstance().getString(BuildConstance.SVN_REVISION, "");
        if ("".equals(revision)) {
            PackageInfo pi = getPackageInfo();
            if (pi != null) {
                return pi.versionCode;
            }
            return versionCode;
        }
        try {
            return Integer.valueOf(revision).intValue();
        } catch (Exception e) {
            LogUtils.m1571e("BuildProvider", e);
            return versionCode;
        }
    }

    public boolean isNewAppUpgrade() {
        return false;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            pi = this.mContext.getPackageManager().getPackageInfo(this.mContext.getPackageName(), 0);
        } catch (Exception e) {
        }
        return pi;
    }

    public boolean isUsePlayerWhiteList() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.PLAYER_USE_WHITE_LIST, "true"));
    }

    public boolean isOpenMessageCenter() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ISOPEN_MESSAGE_CENTER, "true"));
    }

    public boolean isOpenCheckInFun() {
        return getBoolean(BuildCache.getInstance().getString("APK_ISOPEN_CHECKIN_FUN", "true"));
    }

    public boolean isOpencheckInRecommend() {
        return getBoolean(BuildCache.getInstance().getString("APK_ISOPEN_CHECKIN_RECOMMEND", "true"));
    }

    public boolean supportPlayerMultiProcess() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_PLAYER_MULTI_PROCESS, "false"));
    }

    public String getApkThirdVersionCode() {
        return BuildCache.getInstance().getString(BuildConstance.APK_VERSION, "0");
    }

    public String getCustomerName() {
        return BuildCache.getInstance().getString(BuildConstance.APK_CUSTOMER, "gala");
    }

    public String getProductName() {
        return BuildCache.getInstance().getString(BuildConstance.APK_PRODUCT, BuildDefaultDocument.APK_PRODUCT);
    }

    public boolean isHomeVersion() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ISHOME, "false"));
    }

    public boolean isOpenTestPerformance() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_OPEN_TEST_PERFORMANCE, "false"));
    }

    public boolean isOpenKeyboardLogin() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ISOPEN_KEYBOARDLOGIN, "false"));
    }

    public boolean isSupportContentProvider() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.SUPPORT_SETTING_CONTENTPROVIDER, "true"));
    }

    public boolean isDisableServiceBootup() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_DISABLE_SERVICE_BOOTUP, "false"));
    }

    public boolean shouldShowVolume() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SHOW_VOLUME, "true"));
    }

    public String getAppStorePkgName() {
        return HostBuild.getStorePkgName();
    }

    public boolean isIsSupportScreenSaver() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_SCREENSAVER, "true")) && GetInterfaceTools.getIInit().isMainProcess();
    }

    public boolean isSupportAlbumDetailWindowPlay() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_ALBUM_DETAIL_WINDOW_PLAY, "true"));
    }

    public String getMediaPlayerTypeConfig() {
        return BuildCache.getInstance().getString(BuildConstance.APK_MEDIAPLAYERTYPE, "3");
    }

    public String getBuildTime() {
        return BuildCache.getInstance().getString(BuildConstance.APK_BUILD_TIME, "");
    }

    public String getDomainName() {
        if (getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_OTHER_DOAIN, "true"))) {
            return (isGitvUI() || isLitchi() || isNoLogoUI()) ? "ptqy.gitv.tv" : BuildCache.getInstance().getString(BuildConstance.APK_DOMAIN_NAME, "ptqy.gitv.tv");
        } else {
            return BuildCache.getInstance().getString(BuildConstance.APK_DOMAIN_NAME, "ptqy.gitv.tv");
        }
    }

    public String getPingbackP2() {
        CharSequence p2 = BuildCache.getInstance().getString(BuildConstance.APK_PINGBACK_P2, "");
        if (StringUtils.isEmpty(p2)) {
            return BaseLineEdition.getPingbackP2(BuildCache.getInstance().getString(BuildConstance.APK_UI_STYLE, BuildDefaultDocument.APK_UI_STYLE));
        }
        return p2;
    }

    public String getOpenApiOldUuid() {
        return BuildCache.getInstance().getString(BuildConstance.APK_OPENAPI_OLD_UUID, "");
    }

    public String[] getCustomerPkgName() {
        return HostBuild.getCustomerPackage().split(CUSTOMER_PKGNAME_SPLIT);
    }

    public boolean isLitchi() {
        return BaseLineEdition.LITCHI.getEditionName().equalsIgnoreCase(BuildCache.getInstance().getString(BuildConstance.APK_UI_STYLE, BuildDefaultDocument.APK_UI_STYLE));
    }

    public String getForceOpen4kFlag() {
        return BuildCache.getInstance().getString(BuildConstance.APK_FORCEOPEN_4K, "0");
    }

    public boolean isEnableDolby() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_DOBY, "true"));
    }

    public boolean isEnableH265() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_H265, "false"));
    }

    public String getUIVersionName() {
        return BuildCache.getInstance().getString(BuildConstance.APK_UI_STYLE, BuildDefaultDocument.APK_UI_STYLE);
    }

    public boolean isGitvUI() {
        return BaseLineEdition.GITV.getEditionName().equalsIgnoreCase(BuildCache.getInstance().getString(BuildConstance.APK_UI_STYLE, BuildDefaultDocument.APK_UI_STYLE));
    }

    public boolean isNoLogoUI() {
        return BaseLineEdition.NO_LOGO.getEditionName().equalsIgnoreCase(BuildDefaultDocument.APK_UI_STYLE);
    }

    public boolean isTestErrorCodeAndUpgrade() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_TEST_ERROR_CODE_AND_UPGRADE, "false"));
    }

    public boolean isCheckMonkeyOpen() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APP_IS_CHECK_MONKEY, "false"));
    }

    public boolean isShowLive() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SHOW_LIVE, "true"));
    }

    public String getKeyboardType() {
        return BuildCache.getInstance().getString(BuildConstance.APK_KEYBORAD_TYPE, "2");
    }

    public boolean isForceAdvanceMode() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.FORCE_ADV_MODE, "false"));
    }

    public boolean isOpenAPIVersion() {
        return !StringUtils.isEmpty(BuildCache.getInstance().getString(BuildConstance.APK_OPENAPI_FEATURE_LIST, ""));
    }

    public boolean shouldCacheDeviceCheck() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_CACHE_DEVICE_CHECK, "false"));
    }

    public boolean isCollectNetDocInfoWhenPlaybackError() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.PLAYER_OPEN_NETDOCTOR_ONERROR, "true"));
    }

    public boolean isSupportSkin() {
        return !isHomeVersion();
    }

    public boolean isSupportVipRightsActivation() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_OPEN_VIPRIGHTS, "false"));
    }

    public boolean isSupportSubscribe() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_SUPPORT_SUBSCRIBE, "true"));
    }

    public boolean isSupportDesktopManage() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_SUPPORT_DESKTOP_MANAGE, "true"));
    }

    public boolean isSupportCarousel() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_SUPPORT_CAROUSEL, "true"));
    }

    public boolean isSupportRecommendApp() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_SUPPORT_RECOMMEND_APP, "true"));
    }

    public boolean isPingbackDebug() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_PINGBACK_DEBUG, "false"));
    }

    public boolean isEnableHCDNPreDeploy() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ENABLE_HCDN_PREDEPLOY, "true"));
    }

    public boolean isInitCrashHandler() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ISINIT_CRASHHANDLER, "true"));
    }

    public boolean isSupportMonkeyTest() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_IS_SUPPORT_MONKEY_TEST, "false"));
    }

    public boolean isSupportSmallWindowPlay() {
        if (BuildCache.getInstance().getString(BuildConstance.APK_CUSTOMER, "gala").contains("qianhuanmojing")) {
            return false;
        }
        if (GetInterfaceTools.isPlayerLoaded()) {
            return GetInterfaceTools.getPlayerConfigProvider().isSupportSmallWindowPlay();
        }
        return false;
    }

    public String getAdPlayerId() {
        return BuildCache.getInstance().getString(BuildConstance.AD_PLAYER_ID, "qc_100001_100145");
    }

    public boolean isPreferSystemPlayerFor4K() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_PREFER_SYSTEMPLAYER_FOR_4K, "false"));
    }

    public boolean enableExtraPage() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ENABLE_EXTRA_PAGE, "false"));
    }

    public boolean isUseAlbumListCache() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_USE_ALBUM_LIST_CACHE, "true"));
    }

    public boolean shouldAuthMac() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SHOULD_AUTH_MAC, "false"));
    }

    public boolean isSupportVoice() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_VOICE, "false"));
    }

    public boolean isSupportVoiceTest() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_VOICE_TEST, "false"));
    }

    public boolean isEnabledVipAnimation() {
        return getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_ENABLE_VIP_ANIMATION, "true"));
    }

    public String getBroadcastActions() {
        return BuildCache.getInstance().getString(BuildConstance.APK_BROADCAST_ACTIONS, "ACTION_DETAIL");
    }

    public String getOpenapiFeatureList() {
        return BuildCache.getInstance().getString(BuildConstance.APK_OPENAPI_FEATURE_LIST, "");
    }
}
