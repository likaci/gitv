package com.gala.video.lib.share.ifmanager.bussnessIF.player.utils;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.utils.DebugApiException;

public class PlayerDebugUtils {
    public static final String FAKE_OFFLINE_PLAYBACK_PATH = "http://local.fake.url/offlined_data.m3u8";
    public static final String FAKE_ONLINE_PLAYBACK_PATH = "http://online.fake.url/online_data.m3u8";
    public static final String PLAYER_TEST_MODIFY_AUTHCOOKIE = "gala.test.player.authcookie";
    public static final String PROP_ALBUM_DETAIL_IS_SHOW_PLAY = "gala.test.detail.windowplay";
    public static final String PROP_COMMON_TEST_API_ALL_E000001 = "gala.test.api.all.e000001";
    public static final String PROP_COMMON_TEST_API_EXP_MSG = "gala.test.api.expmsg";
    public static final String PROP_COMMON_TEST_HTTP_ERROR_CODE = "gala.test.api.http.errorcode";
    public static final String PROP_COMMON_TEST_HTTP_JSON_FAIL = "gala.test.api.http.jsonfail";
    public static final String PROP_FORCE_TV_PLATFORM_AD = "gala.dbg.ad.forcetvplatform";
    public static final String PROP_LIVE_PLAYER_ERROR = "gala.test.player.live.error";
    public static final String PROP_PLAYER_CACHE_ALL_LIVE_BS_URLS = "gala.test.player.cacheliveurls";
    public static final String PROP_PLAYER_CHN_CHANGE_INTERVAL = "gala.test.chn.changetime";
    public static final String PROP_PLAYER_ENABLE_FRONT_QR_AD = "gala.test.player.enableqrad";
    public static final String PROP_PLAYER_IS_SHOW_LIVE = "gala.test.player.live.show";
    public static final String PROP_PLAYER_LIVE_START_TIME = "gala.test.live.starttime";
    public static final String PROP_PLAYER_PERFORMANCE_DEBUG = "gala.test.player.perf.debug";
    public static final String PROP_PLAYER_PLUGIN_LOADING_DELAY_S = "gala.test.loadplugin.delay";
    public static final String PROP_PLAYER_POST_CALLBACK = "gala.test.player.postcallback";
    public static final String PROP_PLAYER_PROXY_LOG = "gala.test.player.enablelog";
    public static final String PROP_PLAYER_SEEK_CHANGE_THRESHOLD = "gala.test.player.seek.threshold";
    public static final String PROP_PLAYER_SEEK_DELAY_BUFFERING = "gala.test.player.buffer.delay";
    public static final String PROP_PLAYER_SEEK_STEP_DELAY = "gala.test.player.step.delay";
    public static final String PROP_PLAYER_SEEK_STEP_LONG = "gala.test.player.seekstep.long";
    public static final String PROP_PLAYER_SEEK_STEP_SHORT = "gala.test.player.seekstep.short";
    public static final String PROP_PLAYER_TEST_API_ALL_E000012 = "gala.test.api.all.e000012";
    public static final String PROP_PLAYER_TEST_API_ALL_E000054 = "gala.test.api.all.e000054";
    public static final String PROP_PLAYER_TEST_API_AUTH_A00013 = "gala.test.api.auth.a00013";
    public static final String PROP_PLAYER_TEST_API_AUTH_E000054 = "gala.test.api.auth.e000054";
    public static final String PROP_PLAYER_TEST_API_AUTH_VIP_USER_INFO_CHANGE = "gala.test.api.vip.infochange";
    public static final String PROP_PLAYER_TEST_API_AUTH_VIP_USER_PASSWORD_CHANGE = "gala.test.api.pwdchange";
    public static final String PROP_PLAYER_TEST_KERNEL_104 = "gala.test.kernel.104";
    public static final String PROP_PLAYER_TEST_KERNEL_BLOCK = "gala.test.kernel.block";
    public static final String PROP_PLAYER_TEST_KERNEL_COMMON_ERRORS = "gala.test.kernel.commonerror";
    public static final String PROP_PLAYER_TEST_OFFLINE_PLAYBACK_ERROR = "gala.test.offline.playerror";
    public static final String PROP_PLAYER_TEST_RADIOGROUP_SCROLL = "gala.test.player.rgscroll";
    public static final String PROP_PLAYER_TEST_SYSTEMPLAYER_ERROR = "gala.test.systemplayer.error";
    public static final String PROP_PLAYER_TEST_TOAST_NETDIAGNOSE_DELAY = "gala.test.netdiag.toast.delay";
    public static final String PROP_PLAYER_TYPE_OVERRIDE_VALUE = "gala.test.player.type.override";
    public static final String PROP_PLAYER_UI_MENUPANEL_AUTOHIDE = "gala.test.playerui.mp.autohide";
    public static final String PROP_PUSH_API_PLATFORM = "gala.dbg.apiplatform";
    public static final String PROP_PUSH_PREFERED_PLATFORM = "gala.dbg.preferedpushplatform";
    public static final String PROP_SUPPORT_PLAYER_MULTI_PROC = "gala.test.multi.process";
    public static final String PROP_TSET_ALBUMDETAIL_GROUPDETAIL_SOURCEDATA = "log.gala.detail.source.data";
    public static final String PROP_TSET_ALBUMDETAIL_GROUPDETAIL_SPECIALDATA = "log.gala.detail.special.data";
    public static final String PROP_TSET_DRM_PLUGIN_FAIL = "gala.drm.plugin.fail";
    public static final String PROP_TSET_HDR_BITSTREAM_TEST = "gala.test.hdr.data";
    public static final String PROP_TSET_HDR_BITSTREAM_VIP_TEST = "gala.test.hdr.vip";
    public static final String PROP_TSET_JSCONFIG_TEST = "gala.test.js.config";
    private static final String TAG = "Player/Lib/Utils/PlayerDebugUtils";

    static {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "allowDebug() returns " + AppClientUtils.isDebugMode());
        }
    }

    private static boolean allowDebug() {
        return AppClientUtils.isDebugMode();
    }

    public static boolean isAlbumDetailPageShowPlay() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_ALBUM_DETAIL_IS_SHOW_PLAY, true) : true;
    }

    public static boolean testApiAuthForE000054() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_AUTH_E000054, false) : false;
    }

    public static boolean testApiAuthForA00013() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_AUTH_A00013, false) : false;
    }

    public static boolean testApiAllForE000012() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_ALL_E000012, false) : false;
    }

    public static boolean testApiAllForE000054() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_ALL_E000054, false) : false;
    }

    public static boolean testApiAuthVipForUserInfoChanged() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_AUTH_VIP_USER_INFO_CHANGE, false) : false;
    }

    public static boolean testApiAuthVipForUserPasswordChanged() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_API_AUTH_VIP_USER_PASSWORD_CHANGE, false) : false;
    }

    public static boolean testKernelFor104() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_KERNEL_104, false) : false;
    }

    public static boolean testKernelForBlock() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_KERNEL_BLOCK, false) : false;
    }

    public static boolean testSystemPlayerForOfflinePlaybackError() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_OFFLINE_PLAYBACK_ERROR, false) : false;
    }

    public static int testSystemPlayerForPlaybackError() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_TEST_SYSTEMPLAYER_ERROR, 0) : 0;
    }

    public static boolean testApiCommonForE000001() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_COMMON_TEST_API_ALL_E000001, false) : false;
    }

    public static int testHttpCommonErrorCode() {
        return allowDebug() ? SysPropUtils.getInt(PROP_COMMON_TEST_HTTP_ERROR_CODE, 0) : 0;
    }

    public static boolean testHttpJsonFail() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_COMMON_TEST_HTTP_JSON_FAIL, false) : false;
    }

    public static int testKernelForCommonErrorCode() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_TEST_KERNEL_COMMON_ERRORS, 0) : 0;
    }

    public static int getPlayerTypeOverrideValue() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_TYPE_OVERRIDE_VALUE, -1) : -1;
    }

    public static boolean menuPanelAutoHideOverride() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_UI_MENUPANEL_AUTOHIDE, true) : true;
    }

    public static String getTestExceptionMsg() {
        return allowDebug() ? SysPropUtils.get(PROP_COMMON_TEST_API_EXP_MSG, null) : null;
    }

    public static DebugApiException createFakeApiException(String message, String code, String httpCode, String url) {
        if (StringUtils.isEmpty((CharSequence) message)) {
            message = getTestExceptionMsg();
        }
        return new DebugApiException(message, code, httpCode, url);
    }

    public static boolean isEnablePerformanceDebug() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_PERFORMANCE_DEBUG, false) : false;
    }

    public static boolean testRadioGroupScroll() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_TEST_RADIOGROUP_SCROLL, false) : false;
    }

    public static boolean testModifyAuthCookie() {
        return allowDebug() ? SysPropUtils.getBoolean(PLAYER_TEST_MODIFY_AUTHCOOKIE, false) : false;
    }

    public static int getNetDiagnoseToastDelay() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_TEST_TOAST_NETDIAGNOSE_DELAY, -1) : -1;
    }

    public static int getSeekStepShort() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_SEEK_STEP_SHORT, -1) : -1;
    }

    public static int getSeekStepLong() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_SEEK_STEP_LONG, -1) : -1;
    }

    public static int getSeekThreshold() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_SEEK_CHANGE_THRESHOLD, -1) : -1;
    }

    public static int getSeekBufferingDelay() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_SEEK_DELAY_BUFFERING, -1) : -1;
    }

    public static int getSeekStepDelay() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_SEEK_STEP_DELAY, -1) : -1;
    }

    public static boolean testPlayerPostCallbacks() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_POST_CALLBACK, true) : true;
    }

    public static boolean enablePostCallbackLog() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_PROXY_LOG, false) : false;
    }

    public static boolean cacheAllLiveBitStreamUrls() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_CACHE_ALL_LIVE_BS_URLS, true) : true;
    }

    public static boolean enableFrontQRAd() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_ENABLE_FRONT_QR_AD, true) : true;
    }

    public static int getLiveError() {
        return allowDebug() ? SysPropUtils.getInt(PROP_LIVE_PLAYER_ERROR, -1) : -1;
    }

    public static String getApiPlatform() {
        return allowDebug() ? SysPropUtils.get(PROP_PUSH_API_PLATFORM, null) : null;
    }

    public static String getPreferedApiPlatform() {
        return allowDebug() ? SysPropUtils.get(PROP_PUSH_PREFERED_PLATFORM, null) : null;
    }

    public static boolean forceTvPlatformAd() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_FORCE_TV_PLATFORM_AD, true) : true;
    }

    public static boolean testPlayerShowLive() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_PLAYER_IS_SHOW_LIVE, false) : false;
    }

    public static long getLiveStartTime() {
        int i = -1;
        if (allowDebug()) {
            i = SysPropUtils.getInt(PROP_PLAYER_LIVE_START_TIME, -1);
        }
        return (long) i;
    }

    public static int getChannelChangeInterval() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_CHN_CHANGE_INTERVAL, -1) : -1;
    }

    public static int getTestPlayerPluginLoadDelayTime() {
        return allowDebug() ? SysPropUtils.getInt(PROP_PLAYER_PLUGIN_LOADING_DELAY_S, -1) : -1;
    }

    public static int getPlayerMultiProcSwitcher() {
        return allowDebug() ? SysPropUtils.getInt(PROP_SUPPORT_PLAYER_MULTI_PROC, 0) : 0;
    }

    public static boolean testIntertrustDrmPluginFail() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_TSET_DRM_PLUGIN_FAIL, false) : false;
    }

    public static boolean testAlbumDetailGroupDetailSpecialData() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_TSET_ALBUMDETAIL_GROUPDETAIL_SPECIALDATA, false) : false;
    }

    public static boolean testAlbumDetailGroupDetailSource() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_TSET_ALBUMDETAIL_GROUPDETAIL_SOURCEDATA, false) : false;
    }

    public static boolean testHDRBitStreamData() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_TSET_HDR_BITSTREAM_TEST, false) : false;
    }

    public static boolean testHDRBitStreamVIP() {
        return allowDebug() ? SysPropUtils.getBoolean(PROP_TSET_HDR_BITSTREAM_VIP_TEST, false) : false;
    }

    public static int testCloudJsConfig() {
        return allowDebug() ? SysPropUtils.getInt(PROP_TSET_JSCONFIG_TEST, 0) : 0;
    }
}
