package com.gala.video.app.player.utils;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.IPlayerProfile;
import com.gala.sdk.player.Locale;
import com.gala.sdk.player.Platform;
import com.gala.sdk.player.constants.PlayerCodecType;
import com.gala.sdk.utils.MyLogUtils;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.UserType;
import com.gala.tvapi.vrs.model.User;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.config.PlayerConfigManager;
import com.gala.video.app.player.ui.config.AlbumDetailUiConfig;
import com.gala.video.app.player.utils.debug.DebugOptionsCache;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.helper.AccountAdsHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.LoginParam4H5;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.webview.utils.WebSDKConstants;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPlayerProfile implements IPlayerProfile {
    public static final Map<String, String> DEVICE_SHOULD_REST_SURFACE_MAP = new HashMap();
    private static final String TAG = "MyPlayerProfile";
    public static boolean sNetworkManagerReturned;

    static {
        DEVICE_SHOULD_REST_SURFACE_MAP.put("M321", "Huawei");
    }

    public void clearPassportPreference(Context context) {
        GetInterfaceTools.getIGalaAccountManager().logOut(context, "", LoginConstant.LGTTYPE_EXCEPTION);
    }

    public boolean isForceAdvanceMode() {
        return Project.getInstance().getBuild().isForceAdvanceMode();
    }

    public boolean isPushVideoByTvPlatform() {
        IDynamicResult dataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        return dataModel != null ? dataModel.getIsPushVideoByTvPlatform() : false;
    }

    public boolean isNetworkAvaliable() {
        if (!Project.getInstance().getBuild().supportPlayerMultiProcess() || sNetworkManagerReturned) {
            int state = NetWorkManager.getInstance().getNetState();
            boolean available = state == 1 || state == 2;
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "isNetworkAvaliable() returns " + available + ", supportPlayerMultiProcess=" + Project.getInstance().getBuild().supportPlayerMultiProcess() + ", sNetworkManagerReturned=" + sNetworkManagerReturned + ", state=" + state);
            }
            return available;
        }
        NetWorkManager.getInstance().checkNetWork(new StateCallback() {
            public void getStateResult(int state) {
                MyPlayerProfile.sNetworkManagerReturned = true;
            }
        });
        if (!LogUtils.mIsDebug) {
            return true;
        }
        LogUtils.d(TAG, "isNetworkAvaliable() returns true, (multi-process and networkmanager not returned)");
        return true;
    }

    public boolean isDebug() {
        return AppClientUtils.isDebugMode();
    }

    public List<BitStream> filterDefinitions(List<BitStream> bitStreamList) {
        return PlayerAppConfig.filterDefinitions(bitStreamList);
    }

    public boolean shouldResetSurface() {
        boolean ret = false;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "shouldResetSurface: model=" + Build.MODEL);
        }
        for (String model : DEVICE_SHOULD_REST_SURFACE_MAP.keySet()) {
            if (model != null && model.equalsIgnoreCase(Build.MODEL)) {
                String brand = (String) DEVICE_SHOULD_REST_SURFACE_MAP.get(model);
                if (brand != null && brand.equalsIgnoreCase(Build.BRAND)) {
                    ret = true;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "shouldResetSurface: return " + ret);
        }
        return ret;
    }

    public void setScreenSaverEnable(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setScreenSaverEnable() enable=" + enable);
        }
        IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
        iOperate.setScreenSaverEnable(enable);
        if (enable) {
            iOperate.reStart();
        }
    }

    public String getCookie() {
        return GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
    }

    public String getDefaultUserId() {
        return AppRuntimeEnv.get().getDefaultUserId();
    }

    public String getDeviceIp() {
        return AppRuntimeEnv.get().getDeviceIp();
    }

    public String getMediaPlayerTypeConfig() {
        int configPlayerType = StringUtils.parseInt(Project.getInstance().getBuild().getMediaPlayerTypeConfig());
        if (configPlayerType == 1 || configPlayerType == 5) {
            return Project.getInstance().getBuild().getMediaPlayerTypeConfig();
        }
        int playerType = 0;
        if (Project.getInstance().getBuild().isUsePlayerWhiteList()) {
            playerType = PlayerConfigManager.getPlayerConfig().getPlayerTypeConfig();
        }
        if (playerType == 0) {
            return Project.getInstance().getBuild().getMediaPlayerTypeConfig();
        }
        return String.valueOf(playerType);
    }

    public boolean isPayBeforePreview() {
        IDynamicQDataProvider provider = GetInterfaceTools.getIDynamicQDataProvider();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isPayBeforePreview():provider.getDynamicQDataModel=" + provider.getDynamicQDataModel().getPayBeforePreview());
        }
        if (provider.getDynamicQDataModel() != null) {
            return provider.getDynamicQDataModel().getPayBeforePreview();
        }
        return false;
    }

    public boolean isPayAfterPreview() {
        IDynamicQDataProvider provider = GetInterfaceTools.getIDynamicQDataProvider();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isPayAfterPreview():provider.getDynamicQDataModel=" + provider.getDynamicQDataModel().getPayAfterPreview());
        }
        if (provider.getDynamicQDataModel() != null) {
            return provider.getDynamicQDataModel().getPayAfterPreview();
        }
        return false;
    }

    public boolean isDefaultSettingDolbyOrH211(int setting) {
        return false;
    }

    public boolean isDolbyByNativePlayer() {
        return false;
    }

    public boolean isH211ByNativePlayer() {
        return false;
    }

    public PlatformType getApiPlatformType() {
        return PlatformType.NORMAL;
    }

    public boolean isDisableSafeMode() {
        boolean isDisableSafeMode = false;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            isDisableSafeMode = model.getIsDisableSafeMode();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isDisableSafeMode() return " + isDisableSafeMode);
        }
        return isDisableSafeMode;
    }

    public boolean isDisableAdvanceMode() {
        boolean isDisableAdvanceMode = false;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            isDisableAdvanceMode = model.getDisableNativePlayerAdvancedMode();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isDisableAdvanceMode() return " + isDisableAdvanceMode);
        }
        return isDisableAdvanceMode;
    }

    public boolean isOpenHCDN(Context context) {
        return SystemConfigPreference.isOpenHCDN(context);
    }

    public boolean getExpired() {
        return GetInterfaceTools.getIGalaAccountManager().getExpired();
    }

    public String getMd5FormatMacAddr() {
        return DeviceUtils.getMd5FormatMacAddr();
    }

    public int getMaxMemorySizeForBuffer() {
        return PlayerAppConfig.getNativeMediaPlayerMemoryBuffer();
    }

    public String getMacAddress() {
        return DeviceUtils.getMacAddr();
    }

    public BitStream getBitStreamSetting() {
        int definition = PlayerAppConfig.getDefaultStreamType();
        int audioType = PlayerAppConfig.getAudioTypeSetting();
        BitStream bitStream = new BitStream(definition);
        bitStream.setAudioType(audioType);
        return bitStream;
    }

    public PlayerCodecType getDecodeType() {
        return Project.getInstance().getConfig().getDecodeType();
    }

    public String getAppVersion() {
        return Project.getInstance().getBuild().getVersionString();
    }

    public boolean checkBlockingOperation() {
        return PlayerAppConfig.isStartCheckThread4NativePlayer();
    }

    public boolean canSeekBeforeStart() {
        if (!DebugOptionsCache.isMovieSeekAfterStart()) {
            return PlayerConfigManager.getPlayerConfig().canSeekBeforeStart();
        }
        MyLogUtils.d(TAG, "canSeekBeforeStart from DebugOptions!");
        return false;
    }

    public int getSurfaceFormat() {
        int surfaceFormat;
        boolean shouldChangeSurfaceFormat = Project.getInstance().getConfig().shouldChangeSurfaceFormat();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getSurfaceFormat(), shouldChangeSurfaceFormat from package script:" + shouldChangeSurfaceFormat);
        }
        if (shouldChangeSurfaceFormat) {
            surfaceFormat = 1;
        } else {
            surfaceFormat = PlayerConfigManager.getPlayerConfig().getSurfaceFormat();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getSurfaceFormat(), return " + surfaceFormat);
        }
        return surfaceFormat;
    }

    public boolean isRomIntegratedVersion() {
        return PlayerAppConfig.isRomIntegratedVersion();
    }

    public Rect getScreenSize(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return new Rect(0, 0, metrics.widthPixels, metrics.heightPixels);
    }

    public Context getContext() {
        return AppRuntimeEnv.get().getApplicationContext();
    }

    public boolean getStretchPlaybackToFullScreen() {
        return PlayerAppConfig.getStretchPlaybackToFullScreen();
    }

    public UserType getUserType() {
        return GetInterfaceTools.getIGalaAccountManager().getUserType();
    }

    public void setUserTypeJson(String s) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setUserTypeJson(" + s + ")");
        }
        GetInterfaceTools.getIGalaAccountManager().updateUserTypeForPlayer(s);
    }

    public String getDomainName() {
        return Project.getInstance().getBuild().getDomainName();
    }

    public Context getAppContext() {
        return AppRuntimeEnv.get().getApplicationContext();
    }

    public Platform getPlayerPlatform() {
        return Platform.TV;
    }

    public boolean isPushVideo() {
        return false;
    }

    public String getPassportDeviceId() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPassportDeviceId(), deviceId=" + TVApiBase.getTVApiProperty().getPassportDeviceId());
        }
        return TVApiBase.getTVApiProperty().getPassportDeviceId();
    }

    public int getRetryTimesInPlaying() {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (result == null) {
            return 3;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getRetryTimesInPlaying: retry_times_after_started=" + result.getRetryTimesAfterStarted());
        }
        return result.getRetryTimesAfterStarted();
    }

    public int getRetryTimesInPreparing() {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (result == null) {
            return 3;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getRetryTimesInPreparing: retry_times_before_started=" + result.getRetryTimesBeforeStarted());
        }
        return result.getRetryTimesBeforeStarted();
    }

    public String getPushVFlag() {
        return null;
    }

    public int getFixedSizeTypeForSurfaceHolder() {
        return PlayerConfigManager.getPlayerConfig().getFixedSizeTypeForSurfaceHolder();
    }

    public boolean isDisableP2PUpload() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        boolean isDisableP2PUpload = model != null ? model.getIsDisableP2PUpload() : false;
        LogUtils.d(TAG, "isDisableP2PUpload=" + isDisableP2PUpload);
        return isDisableP2PUpload;
    }

    public String getCdnDispatchParam() {
        return "-1";
    }

    public String getPingbackP2() {
        return Project.getInstance().getBuild().getPingbackP2();
    }

    public boolean isMediaPlayerPauseBeforeSeek() {
        return PlayerConfigManager.getPlayerConfig().isMediaPlayerPauseBeforeSeek();
    }

    public String getUid() {
        return GetInterfaceTools.getIGalaAccountManager().getUID();
    }

    public String getVersionCode() {
        return Project.getInstance().getBuild().getVersionString();
    }

    public float getVideoViewScale() {
        return Project.getInstance().getConfig().getVideoViewScale();
    }

    public String getUuid() {
        return Project.getInstance().getBuild().getVrsUUID();
    }

    public boolean isCheckPushVipVideo() {
        return Project.getInstance().getConfig().isCheckPushVipVideo();
    }

    public boolean isDeviceSupport4kStream() {
        return PlayerConfigManager.getPlayerConfig().isSupport4K();
    }

    public boolean isEnableH211() {
        if (DebugOptionsCache.isEnableH211()) {
            MyLogUtils.d(TAG, "isEnableH211 from DebugOptions");
            return true;
        }
        boolean isDynamicSupportH211;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            isDynamicSupportH211 = model.getIsSupportH265();
        } else {
            isDynamicSupportH211 = true;
        }
        boolean isDeviceSupportH211 = PlayerConfigManager.getPlayerConfig().isSupportH211();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dynamicResultSupportH211 = " + isDynamicSupportH211 + ", deviceSupportH211=" + isDeviceSupportH211);
        }
        if (isDynamicSupportH211 && isDeviceSupportH211) {
            return true;
        }
        return false;
    }

    public boolean isLogin(Context context) {
        return GetInterfaceTools.getIGalaAccountManager().isLogin(context);
    }

    public boolean isOpen4kStream() {
        boolean ret;
        String flag = Project.getInstance().getBuild().getForceOpen4kFlag();
        if (flag.equals("1")) {
            ret = true;
        } else if (flag.equals("-1")) {
            ret = false;
        } else {
            ret = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getSupport4k();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isOpen4kStream ret=" + ret + ", flag=" + flag);
        }
        return ret;
    }

    public boolean isPreferNativePlayerSafeModeFor4K() {
        boolean isPreferNativePlayerSafeModeFor4K = PlayerAppConfig.isPreferNativePlayerSafeModeFor4K();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isPreferSystemFor4K, return " + isPreferNativePlayerSafeModeFor4K);
        }
        return isPreferNativePlayerSafeModeFor4K;
    }

    public boolean isPreferSystemPlayerFor4K() {
        return PlayerAppConfig.isPreferSystemPlayerFor4K();
    }

    public boolean isShowInnerStorage() {
        return PlayerAppConfig.isShowInnerStorage();
    }

    public boolean isShowVIP() {
        return GetInterfaceTools.getIDynamicQDataProvider().isSupportVip();
    }

    public boolean isSkipAdUser(Context context) {
        return AccountAdsHelper.isShouldSkipAd(context);
    }

    public boolean isSupport4kH211Stream() {
        return Project.getInstance().getConfig().is4kH265StreamSupported();
    }

    public boolean isSupportAndroidCache() {
        return Project.getInstance().getBuild().isSupportAndroidCache();
    }

    public boolean isSupportNetDiagnose() {
        return PlayerAppConfig.isSupportNetDiagnose();
    }

    public boolean shouldSkipVideoHeaderAndTail() {
        return PlayerAppConfig.shouldSkipVideoHeaderAndTail();
    }

    public void setSkipVideoHeaderAndTail(boolean isSkip) {
        PlayerAppConfig.setSkipVideoHeaderAndTail(isSkip);
    }

    public void setBitStreamSetting(BitStream bitStream) {
        int definition = bitStream.getDefinition();
        int audioType = bitStream.getAudioType();
        PlayerAppConfig.setDefaultStreamType(definition);
        PlayerAppConfig.setAudioTypeSetting(audioType);
    }

    public boolean filterStereo3DKeyEvent(KeyEvent event) {
        return Project.getInstance().getConfig().filterStereo3DKeyEvent(event);
    }

    public boolean isSupport2DTo3DFor4k() {
        return PlayerAppConfig.isSupport2DTo3DFor4k();
    }

    public boolean is2DTo3DModel() {
        return PlayerAppConfig.is2DTo3DModel();
    }

    public void open2DTo3D() {
    }

    public void close2DTo3D() {
    }

    public void onStereo3DBegun() {
        Project.getInstance().getConfig().onStereo3DBegun();
    }

    public void onStereo3DFinished() {
        Project.getInstance().getConfig().onStereo3DFinished();
    }

    public boolean isNeedShowFullScreenHint() {
        return PlayerAppConfig.isNeedShowFullScreenHint();
    }

    public void saveVipInfo(User user, Context context) {
        GetInterfaceTools.getIGalaAccountManager().saveVipInfo(user);
    }

    public boolean showEpisodeAsGallery(int chnId) {
        int[] channelIds = PlayerAppConfig.showEpisodeAsGallery();
        boolean ret = false;
        for (int i : channelIds) {
            if (chnId == i) {
                ret = true;
                break;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isEpisodesWithImageChannelIds: ret=" + ret);
        }
        return ret;
    }

    public boolean isCollectNetDoctorInfoOnError() {
        boolean dynamicSwitcherEnable = true;
        boolean configEnable = Project.getInstance().getBuild().isCollectNetDocInfoWhenPlaybackError();
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null && model.getIsDisableNDUpload()) {
            dynamicSwitcherEnable = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isCollectNetDoctorInfoOnError(), configEnable:" + configEnable + "dynamicSwitcherEnable:" + dynamicSwitcherEnable);
        }
        if (configEnable) {
            return dynamicSwitcherEnable;
        }
        return false;
    }

    public boolean isEnableDolby() {
        boolean isEnableDolby = Project.getInstance().getConfig().isEnableDolby();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "profile isEnableDolby =  return " + isEnableDolby);
        }
        return isEnableDolby;
    }

    public Map<String, String> getPingbackParams() {
        return new HashMap<String, String>() {
            {
                put("window_disable", Project.getInstance().getBuild().isSupportSmallWindowPlay() ? "0" : "1");
                put(WebSDKConstants.PARAM_KEY_P2, Project.getInstance().getBuild().getPingbackP2());
            }
        };
    }

    public boolean isEnableHCDNPreDeploy() {
        boolean enable = true;
        if (Project.getInstance().getBuild().isEnableHCDNPreDeploy()) {
            IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            if (model != null && model.getIsDisableHCDNPreDeploy()) {
                enable = false;
            }
            LogUtils.d(TAG, "isEnableHCDNPreDeploy(), glay scale switcher return " + enable);
            return enable;
        }
        LogUtils.d(TAG, "isEnableHCDNPreDeploy(), config return false");
        return false;
    }

    public String getWebViewJsonForAd() {
        return GetInterfaceTools.getWebJsonParmsProvider().getJson();
    }

    public void onLoginSuccess(Map<String, String> info) {
        if (info != null) {
            String cookie = (String) info.get(WebSDKConstants.PARAM_KEY_COOKIE);
            String uid = (String) info.get(WebSDKConstants.PARAM_KEY_UID);
            String account = (String) info.get("account");
            String nickName = (String) info.get("nickName");
            String vipDate = (String) info.get("vipDate");
            int userTypeH5 = StringUtils.parseInt((String) info.get("userTypeH5"));
            boolean isLitchiH5 = StringUtils.equals((CharSequence) info.get("isLitchiH5"), "true");
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onLoginSuccess() cookie:" + cookie + ", uid=" + uid + ", account=" + account + ", nickName=" + ", vipDate=" + vipDate + ", userTypeH5=" + userTypeH5 + ", isLitchiH5=" + isLitchiH5);
            }
            LoginParam4H5 param = new LoginParam4H5();
            param.cookie = cookie;
            param.uid = uid;
            param.account = account;
            param.nickName = nickName;
            param.vipDate = vipDate;
            param.userTypeH5 = userTypeH5;
            param.isLitchiH5 = isLitchiH5;
            GetInterfaceTools.getIGalaAccountManager().loginForH5(param);
        }
    }

    public void onPurchaseSuccess(Map<String, String> info) {
        if (info != null) {
            String cookie = (String) info.get(WebSDKConstants.PARAM_KEY_COOKIE);
            String uid = (String) info.get(WebSDKConstants.PARAM_KEY_UID);
            String account = (String) info.get("account");
            String nickName = (String) info.get("nickName");
            String vipDate = (String) info.get("vipDate");
            int userTypeH5 = StringUtils.parseInt((String) info.get("userTypeH5"));
            boolean isLitchiH5 = StringUtils.equals((CharSequence) info.get("isLitchiH5"), "true");
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onPurchaseSuccess() cookie:" + cookie + ", uid=" + uid + ", account=" + account + ", nickName=" + ", vipDate=" + vipDate + ", userTypeH5=" + userTypeH5 + ", isLitchiH5=" + isLitchiH5);
            }
            LoginParam4H5 param = new LoginParam4H5();
            param.cookie = cookie;
            param.uid = uid;
            param.account = account;
            param.nickName = nickName;
            param.vipDate = vipDate;
            param.userTypeH5 = userTypeH5;
            param.isLitchiH5 = isLitchiH5;
            GetInterfaceTools.getIGalaAccountManager().saveAccountInfoForH5(param);
        }
    }

    public int getCupidAppId() {
        return 1;
    }

    public String getCupidLocale() {
        return Locale.CHINESE_SIMPLIFIED;
    }

    public boolean useNativePlayerForCarousel() {
        return PlayerConfigManager.getPlayerConfig().isUseNativePlayerCarousel();
    }

    public boolean isOpenDrmRootCheck() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        boolean isOpenDrmRootCheck = model != null ? model.getIsOpenRootCheck() : false;
        LogUtils.d(TAG, "isOpenDrmRootCheck=" + isOpenDrmRootCheck);
        return isOpenDrmRootCheck;
    }

    public boolean isSupportWindowPlay() {
        return new AlbumDetailUiConfig().isEnableWindowPlay();
    }

    public boolean isOpenAdVipGuide() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        boolean isOpenAdVipGuide = model != null ? model.getIsOpenAdVipGuide() : false;
        LogUtils.d(TAG, "isOpenAdVipGuide=" + isOpenAdVipGuide);
        return isOpenAdVipGuide;
    }

    public String getAdVipGuideTipText() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String adGuideTipText = model != null ? model.getAdGuideBecomeVipText() : "";
        LogUtils.d(TAG, "getAdGuideTipText=" + adGuideTipText);
        return adGuideTipText;
    }

    public boolean isDisableADCache() {
        if (DebugOptionsCache.isCloseADCache()) {
            MyLogUtils.d(TAG, "isDisableADCache from DebugOption");
            return true;
        }
        boolean isDynamicDisableADCache;
        boolean isDeviceDisableADCache = PlayerConfigManager.getPlayerConfig().isDisableADCache();
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            isDynamicDisableADCache = model.getIsDisableAdCache();
        } else {
            isDynamicDisableADCache = true;
        }
        LogUtils.d(TAG, "isDisableADCache() isDynamicDisableADCache=" + isDynamicDisableADCache + ", isDeviceDisableADCache=" + isDeviceDisableADCache);
        if (isDynamicDisableADCache || isDeviceDisableADCache) {
            return true;
        }
        return false;
    }

    public boolean isEnableLocalServerF4v2Hls() {
        boolean isEnableLocalServer = true;
        if (DebugOptionsCache.isEnableADLocalServer()) {
            MyLogUtils.d(TAG, "isEnableLocalServerF4v2Hls from DebugOption");
        } else {
            boolean isGrayScaleOpen;
            IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            if (model != null) {
                isGrayScaleOpen = model.enablePlayerLocalServerF4v2Hls();
            } else {
                isGrayScaleOpen = false;
            }
            boolean isPlayerConfigOpen = PlayerConfigManager.getPlayerConfig().enablePlayerLocalServerF4v2Hls();
            if (!(isGrayScaleOpen && isPlayerConfigOpen)) {
                isEnableLocalServer = false;
            }
            MyLogUtils.d(TAG, "isEnableLocalServer(), isPlayerConfigOpen:" + isPlayerConfigOpen + ", isGrayScaleOpen:" + isGrayScaleOpen + ", return " + isEnableLocalServer);
        }
        return isEnableLocalServer;
    }

    public boolean isEnableLocalServerConvergeStream() {
        boolean isGrayScaleOpen;
        boolean isEnableLocalServer;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            isGrayScaleOpen = model.enablePlayerLocalServerStream();
        } else {
            isGrayScaleOpen = false;
        }
        boolean isPlayerConfigOpen = PlayerConfigManager.getPlayerConfig().enablePlayerLocalServerConvergeStream();
        if (isGrayScaleOpen && isPlayerConfigOpen) {
            isEnableLocalServer = true;
        } else {
            isEnableLocalServer = false;
        }
        MyLogUtils.d(TAG, "isEnableLocalServer(), isPlayerConfigOpen:" + isPlayerConfigOpen + ", isGrayScaleOpen:" + isGrayScaleOpen + ", return " + isEnableLocalServer);
        return isEnableLocalServer;
    }

    public boolean isSelectionPanelShown() {
        return PlayerAppConfig.isSelectionPanelShown();
    }

    public boolean isPingbackDebugMode() {
        return PlayerAppConfig.isPingbackDebugMode();
    }

    public boolean shouldUpdateSurfaceViewAfterStart() {
        return PlayerConfigManager.getPlayerConfig().shouldUpdateSurfaceViewAfterStart();
    }

    public boolean useFileDescriptorForLocalPlayback() {
        return PlayerConfigManager.getPlayerConfig().useFileDescriptorForLocalPlayback();
    }

    public void updateSkipAdState(Context context, boolean isSkipAd) {
        AccountAdsHelper.updateSkipAdState(context, isSkipAd);
    }

    public boolean isDeviceSupportDolbyVision() {
        boolean isDeviceSupportDolbyVision = PlayerConfigManager.getPlayerConfig().isSupportDolbyVision();
        MyLogUtils.d(TAG, "isDeviceSupportDolbyVision=" + isDeviceSupportDolbyVision);
        return isDeviceSupportDolbyVision;
    }

    public boolean isDeviceSupportHDR10() {
        boolean isDeviceSupportHDR10 = PlayerConfigManager.getPlayerConfig().isSupportHDR10();
        MyLogUtils.d(TAG, "isDeviceSupportHDR10=" + isDeviceSupportHDR10);
        return isDeviceSupportHDR10;
    }

    public boolean isOpenHDRByUserPreference() {
        return PlayerAppConfig.isOpenHDR();
    }

    public boolean isDisableAdCaster() {
        return AdCasterSwitchHelper.isDisableAdCaster();
    }

    public int getSkipAdCount() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model == null) {
            return -1;
        }
        MyLogUtils.d(TAG, "getSkipAdCount():" + model.getAdSkipFrequency());
        return model.getAdSkipFrequency();
    }

    public boolean canSeekBeforeStartForAD() {
        if (DebugOptionsCache.isADSeekAfterStart()) {
            MyLogUtils.d(TAG, "canSeekBeforeStartForAD from DebugOptions");
            return false;
        }
        boolean canSeekBeforeStartForAD = PlayerConfigManager.getPlayerConfig().canSeekBeforeStartAD();
        MyLogUtils.d(TAG, "canSeekBeforeStartForAD=" + canSeekBeforeStartForAD);
        return canSeekBeforeStartForAD;
    }

    public boolean isOpenPluginIOBalance() {
        return PlayerAppConfig.isOpenPluginIOBalance();
    }

    public boolean isEnableHcdnMultiProcess() {
        boolean isPlayerConfigEnable;
        boolean isEnableMultiProcess;
        IDynamicResult dataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        boolean isGrayScaleEnable = dataModel != null ? !dataModel.disableHcdnDaemon() : true;
        if (PlayerConfigManager.getPlayerConfig().disableHcdnMultiProcess()) {
            isPlayerConfigEnable = false;
        } else {
            isPlayerConfigEnable = true;
        }
        if (isGrayScaleEnable && isPlayerConfigEnable) {
            isEnableMultiProcess = true;
        } else {
            isEnableMultiProcess = false;
        }
        MyLogUtils.d(TAG, "isEnableHcdnMultiProcess(), isGrayScaleEnable:" + isGrayScaleEnable + ", isPlayerConfigEnable:" + isPlayerConfigEnable + ", return " + isEnableMultiProcess);
        return isEnableMultiProcess;
    }

    public String getUniPlayerDataConfigJson() {
        return PlayerConfigManager.getPlayerConfig().getUniPlayerDataConfigJson();
    }

    public String getPlayerConfigJson() {
        return PlayerConfigManager.getPlayerConfig().getJsonConfig();
    }

    public String getPlayerTipCollections() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model == null) {
            return null;
        }
        String str = model.getPlayerTipCollections();
        MyLogUtils.d(TAG, "getPlayerTipCollections():" + str);
        return str;
    }
}
