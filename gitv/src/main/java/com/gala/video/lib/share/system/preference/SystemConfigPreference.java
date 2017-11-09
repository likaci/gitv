package com.gala.video.lib.share.system.preference;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;

public class SystemConfigPreference {
    private static final String BACKGROUND_VERSION = "background_version";
    private static final String BACK_TAB_TOAST_COUNT = "back_tab_toast_count";
    private static final String CHANNEL_TAG_INDEX_FAVORTEABLE_COMMENT = "channel_tag_index_comment";
    private static final String CHANNEL_TAG_INDEX_HOTPLAY = "channel_tag_index_hotplay";
    private static final String CHANNEL_TAG_INDEX_UPDATE_IN_7DAYS = "channel_tag_index_update_in_7days";
    private static final String DISABLE_PLAYER_SAFEMODE = "disable_player_safemode";
    private static final String DLNA_DMR_SET_DEVICE_RENDER_UUID = "dlna_dmr_set_device_render_uuid";
    private static final String GALA_SELF_SETTING_BACKGROUND_DAY = "gala_setting_background_day";
    private static final String GALA_SELF_SETTING_BACKGROUND_NIGHT = "gala_setting_background_night";
    private static final String HAVE_P2P = "have_p2p";
    private static final String HAVE_RECOMMEND = "have_recommend";
    private static final String IS_SHOW_FOOT_LEAD = "show_foot_lead";
    private static final String JS_EXCUTOR_JAR_VERSION = "js_excutor_jar_version";
    private static final String LAST_INSTALL_VERSION = "last_install_version";
    private static final String LOADING_IMAGE_VERSION = "loading_image_version";
    private static final String NAME = "SYSTEM_CONFIG";
    private static final String NEW_USER = "newuser";
    private static final String OPRATION_HELP_NUM_KEY = "opration_help_num_key";
    private static final String PLAYER_CONFIG_CACHED_JSON_PATH = "player_config_cached_json_path";
    private static final String PLAYER_CONFIG_REMOTE_JS_PATH = "player_config_remote_js_path";
    private static final String PLAYER_SUPPORT_DOLBY = "player_support_dolby";
    private static final String PLAYER_SUPPORT_H265 = "player_support_h265";
    private static final String SAVED_MOBILE = "saved_mobile";
    public static final String SETTING_BACKGROUND_DAY = "day_setting_bg_";
    public static final String SETTING_BACKGROUND_DAY_DEFAULT = "default_day_local";
    public static final String SETTING_BACKGROUND_NIGHT = "night_setting_bg_";
    public static final String SETTING_BACKGROUND_NIGHT_DEFAULT = "default_night_local";
    private static final String TAB_TOAST_COUNT = "tab_click_count";
    private static final String TAG = "SystemConfigPreference";
    private static final String UPDATE_DIALOG_COUNT = "update_dialog_count";

    public static boolean isNewUser(Context ctx) {
        AppPreference preference = new AppPreference(ctx, NAME);
        boolean isNewUser = preference.getBoolean(NEW_USER, true);
        if (isNewUser) {
            preference.save(NEW_USER, false);
        }
        return isNewUser;
    }

    public static void setLoadingImageVersion(Context ctx, int version) {
        new AppPreference(ctx, NAME).save(LOADING_IMAGE_VERSION, version);
    }

    public static int getLoadingImageVersion(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(LOADING_IMAGE_VERSION, -1);
    }

    public static void setBackgroundImgVersion(Context ctx, int version) {
        new AppPreference(ctx, NAME).save(BACKGROUND_VERSION, version);
    }

    public static int getBackgroundImgVersion(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(BACKGROUND_VERSION, 0);
    }

    public static void setSavedMobile(Context ctx, String mobile) {
        new AppPreference(ctx, NAME).save(SAVED_MOBILE, mobile);
    }

    public static String getSavedMobile(Context ctx) {
        return new AppPreference(ctx, NAME).get(SAVED_MOBILE);
    }

    public static void setSavedChannelTagIndex_HOTPLAY(Context ctx, int index) {
        new AppPreference(ctx, NAME).save(CHANNEL_TAG_INDEX_HOTPLAY, index);
    }

    public static int getLastTimeChannelTagIndex_HOTPLAY(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(CHANNEL_TAG_INDEX_HOTPLAY, 0);
    }

    public static void setSavedChannelTagIndex_FAVORTEABLE_COMMENT(Context ctx, int index) {
        new AppPreference(ctx, NAME).save(CHANNEL_TAG_INDEX_FAVORTEABLE_COMMENT, index);
    }

    public static int getLastTimeChannelTagIndex_FAVORTEABLE_COMMENT(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(CHANNEL_TAG_INDEX_FAVORTEABLE_COMMENT, 0);
    }

    public static void setSavedChannelTagIndex_UPDATE_IN_7DAYS(Context ctx, int index) {
        new AppPreference(ctx, NAME).save(CHANNEL_TAG_INDEX_UPDATE_IN_7DAYS, index);
    }

    public static int getLastTimeChannelTagIndex_UPDATE_IN_7DAYS(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(CHANNEL_TAG_INDEX_UPDATE_IN_7DAYS, 0);
    }

    public static void setDmrSetDeviceRenderUuid(Context ctx, String uuid) {
        new AppPreference(ctx, NAME).save(DLNA_DMR_SET_DEVICE_RENDER_UUID, uuid);
    }

    public static String getDmrSetDeviceRenderUuid(Context ctx) {
        return new AppPreference(ctx, NAME).get(DLNA_DMR_SET_DEVICE_RENDER_UUID, "");
    }

    public static String getDayModeBackground(Context ctx) {
        return new AppPreference(ctx, NAME).get(GALA_SELF_SETTING_BACKGROUND_DAY, SETTING_BACKGROUND_DAY_DEFAULT);
    }

    public static String getNightModeBackground(Context ctx) {
        return new AppPreference(ctx, NAME).get(GALA_SELF_SETTING_BACKGROUND_NIGHT, SETTING_BACKGROUND_NIGHT_DEFAULT);
    }

    public static void setDayModeBackground(Context ctx, String drawableName) {
        LogUtils.d(TAG, "setBackgroundSelectedDrawableName-- drawableName = " + drawableName);
        new AppPreference(ctx, NAME).save(GALA_SELF_SETTING_BACKGROUND_DAY, drawableName);
    }

    public static void setNightModeBackground(Context ctx, String drawableName) {
        LogUtils.d(TAG, "setBackgroundSelectedDrawableName-- drawableName = " + drawableName);
        new AppPreference(ctx, NAME).save(GALA_SELF_SETTING_BACKGROUND_NIGHT, drawableName);
    }

    public static void setOprationHelpNum(Context ctx, int num) {
        new AppPreference(ctx, NAME).save(OPRATION_HELP_NUM_KEY, num);
    }

    public static int getOprationHelpNum(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(OPRATION_HELP_NUM_KEY, 0);
    }

    public static boolean isRecommend(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(HAVE_RECOMMEND, false);
    }

    public static void setRecommend(Context ctx, boolean isRecommend) {
        new AppPreference(ctx, NAME).save(HAVE_RECOMMEND, isRecommend);
    }

    public static boolean isShowFootLead(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(IS_SHOW_FOOT_LEAD, true);
    }

    public static void setShowFootLead(Context ctx, boolean isShow) {
        new AppPreference(ctx, NAME).save(IS_SHOW_FOOT_LEAD, isShow);
    }

    public static boolean isOpenHCDN(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(HAVE_P2P, true);
    }

    public static void setOpenHCDN(Context ctx, boolean hasP2P) {
        LogUtils.d(TAG, "setOpenHCDN(hasP2P=" + hasP2P + ")");
        new AppPreference(ctx, NAME).save(HAVE_P2P, hasP2P);
    }

    public static boolean disablePlayerSafeMode(Context ctx) {
        boolean isDisabled = new AppPreference(ctx, NAME).getBoolean(DISABLE_PLAYER_SAFEMODE, false);
        if (PlayerDebugUtils.getPlayerTypeOverrideValue() == 2) {
            return false;
        }
        if (!LogUtils.mIsDebug) {
            return isDisabled;
        }
        LogUtils.d(TAG, "disablePlayerSafeMode = " + isDisabled);
        return isDisabled;
    }

    public static void setDisableNativePlayerSafeMode(Context ctx, boolean isDisabled) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setDisableNativePlayerSafeMode(" + isDisabled + ")");
        }
        new AppPreference(ctx, NAME).save(DISABLE_PLAYER_SAFEMODE, isDisabled);
    }

    public static boolean isPlayerCoreSupportDolby(Context ctx) {
        return new AppPreference(ctx, NAME).getBoolean(PLAYER_SUPPORT_DOLBY, false);
    }

    public static void setPlayerCoreSupportDolby(Context ctx, boolean supportDolby) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setPlayerCoreSupportDolby(" + supportDolby + ")");
        }
        new AppPreference(ctx, NAME).save(PLAYER_SUPPORT_DOLBY, supportDolby);
    }

    public static boolean isPlayerCoreSupportH265(Context ctx) {
        boolean isSupportH265 = new AppPreference(ctx, NAME).getBoolean(PLAYER_SUPPORT_H265, false);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isPlayerCoreSupportH265 = " + isSupportH265);
        }
        return isSupportH265;
    }

    public static void setPlayerCoreSupportH265(Context ctx, boolean supportH265) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setPlayerCoreSupportH265(" + supportH265 + ")");
        }
        new AppPreference(ctx, NAME).save(PLAYER_SUPPORT_H265, supportH265);
    }

    public static void setPlayerConfigCachedJsonResultPath(Context ctx, String path) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setPlayerConfigCachedJsonResultPath(" + path + ")");
        }
        new AppPreference(ctx, NAME).save(PLAYER_CONFIG_CACHED_JSON_PATH, path);
    }

    public static String getPlayerConfigCachedJsonResultPath(Context ctx) {
        String path = new AppPreference(ctx, NAME).get(PLAYER_CONFIG_CACHED_JSON_PATH);
        if (StringUtils.isEmpty((CharSequence) path)) {
            path = null;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPlayerConfigCachedJsonResultPath = " + path);
        }
        return path;
    }

    public static void setPlayerConfigRemoteJsPath(Context ctx, String path) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setPlayerConfigRemoteJsPath(" + path + ")");
        }
        new AppPreference(ctx, NAME).save(PLAYER_CONFIG_REMOTE_JS_PATH, path);
    }

    public static String getPlayerConfigRemoteJsPath(Context ctx) {
        String path = new AppPreference(ctx, NAME).get(PLAYER_CONFIG_REMOTE_JS_PATH);
        if (StringUtils.isEmpty((CharSequence) path)) {
            path = null;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPlayerConfigRemoteJsPath = " + path);
        }
        return path;
    }

    public static void setJsExcutorJarVersion(Context ctx, String version) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setJsExcutorJarVersion(" + version + ")");
        }
        new AppPreference(ctx, NAME).save(JS_EXCUTOR_JAR_VERSION, version);
    }

    public static String getJsExcutorJarVersion(Context ctx) {
        String version = new AppPreference(ctx, NAME).get(JS_EXCUTOR_JAR_VERSION);
        if (StringUtils.isEmpty((CharSequence) version)) {
            version = null;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getJsExcutorJarVersion = " + version);
        }
        return version;
    }

    public static void setUpdateDialogCount(Context ctx, int count) {
        new AppPreference(ctx, NAME).save(UPDATE_DIALOG_COUNT, count);
    }

    public static int getUpdateDialogCount(Context ctx) {
        int count = new AppPreference(ctx, NAME).getInt(UPDATE_DIALOG_COUNT, 1);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getUpdateDialogCount = " + count);
        }
        return count;
    }

    public static String getLastInstallVersion(Context ctx) {
        return new AppPreference(ctx, NAME).get(LAST_INSTALL_VERSION);
    }

    public static void setLastInstallVersion(Context ctx, String version) {
        new AppPreference(ctx, NAME).save(LAST_INSTALL_VERSION, version);
    }

    public static void saveTabToastCount(Context ctx, int count) {
        new AppPreference(ctx, NAME).save(TAB_TOAST_COUNT, count);
    }

    public static int getTabToastCount(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(TAB_TOAST_COUNT, 0);
    }

    public static void saveBackTabToastCount(Context ctx, int count) {
        new AppPreference(ctx, NAME).save(BACK_TAB_TOAST_COUNT, count);
    }

    public static int getBackTabToastCount(Context ctx) {
        return new AppPreference(ctx, NAME).getInt(BACK_TAB_TOAST_COUNT, 0);
    }
}
