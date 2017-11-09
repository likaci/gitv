package com.gala.video.app.epg.project.build;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gala.sdk.plugin.PluginType;
import com.gala.video.app.stub.HostBuild;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerMultiProcessSwitchHelper;
import com.gala.video.lib.share.utils.StreamUtils;
import java.io.Closeable;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildInit {
    private static final String TAG = "BuildInit";
    private String mCommon = "";
    private String mConfigString = "";
    private String mHostUUid = "";
    private boolean mIsMatchDevice = false;
    private String mMediaPlayerType = "3";
    private String mTvInternalVersion = "";

    public BuildInit() {
        init();
    }

    private void prepare() {
        this.mTvInternalVersion = HostBuild.getVersionName();
        BuildCache.getInstance().putString(BuildConstance.VRS_UUID, BuildDefaultDocument.VRS_UUID);
        BuildCache.getInstance().putString(BuildConstance.DATA_VERSION, "");
        BuildCache.getInstance().putString(BuildConstance.SVN_REVISION, "0");
        BuildCache.getInstance().putString(BuildConstance.APK_VERSION, "0");
        BuildCache.getInstance().putString(BuildConstance.APK_CUSTOMER, "gala");
        BuildCache.getInstance().putString(BuildConstance.APK_PRODUCT, BuildDefaultDocument.APK_PRODUCT);
        BuildCache.getInstance().putString(BuildConstance.APK_DOMAIN_NAME, BuildDefaultDocument.APK_DOMAIN_NAME);
        BuildCache.getInstance().putString(BuildConstance.APK_UI_STYLE, BuildDefaultDocument.APK_UI_STYLE);
        BuildCache.getInstance().putString(BuildConstance.APK_PINGBACK_P2, "");
        BuildCache.getInstance().putString(BuildConstance.APK_MEDIAPLAYERTYPE, "3");
        BuildCache.getInstance().putString(BuildConstance.APK_BUILD_TIME, "");
        BuildCache.getInstance().putString(BuildConstance.APK_CUSTOMER_PACKAGES, BuildDefaultDocument.APK_CUSTOMER_PACKAGES);
        BuildCache.getInstance().putString(BuildConstance.APK_OPENAPI_SIGNATURE, "");
        BuildCache.getInstance().putString(BuildConstance.APK_OPENAPI_OLD_UUID, "");
        BuildCache.getInstance().putString(BuildConstance.APK_UNIQUE_SECRET_KEY, "");
        BuildCache.getInstance().putString(BuildConstance.APK_PACKAGE_NAME, BuildDefaultDocument.APK_PACKAGE_NAME);
        BuildCache.getInstance().putString(BuildConstance.APP_STORE_PKG_NAME, "com.gitv.tvappstore");
        BuildCache.getInstance().putString(BuildConstance.APP_GAMESTORE_PKG_NAME, BuildDefaultDocument.APP_GAMESTORE_PKG_NAME);
        BuildCache.getInstance().putString(BuildConstance.APK_KEYBORAD_TYPE, "2");
        BuildCache.getInstance().putString(BuildConstance.AD_PLAYER_ID, "qc_100001_100145");
        BuildCache.getInstance().putString(BuildConstance.APK_FORCEOPEN_4K, "0");
        BuildCache.getInstance().putString(BuildConstance.APK_TV_INTERNAL_VERSION, this.mTvInternalVersion);
        BuildCache.getInstance().putString(BuildConstance.APK_OPENAPI_FEATURE_LIST, "");
        BuildCache.getInstance().putString(BuildConstance.APK_BROADCAST_ACTIONS, "ACTION_DETAIL");
        BuildCache.getInstance().putString(BuildConstance.APK_ISHOME, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SHOW_VOLUME, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_ALBUM_DETAIL_WINDOW_PLAY, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_ISPINGBACKOFFICIAL, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SHOW_VIP, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_DOBY, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_H265, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_DISABLE_SERVICE_BOOTUP, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_ANDROIDCACHE, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_GITV_UI, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_OTHER_DOAIN, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_USE_ALBUM_LIST_CACHE, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SHOULD_AUTH_MAC, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_ENABLE_BISDK, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SHOW_LIVE, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_CRACKED, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_ENABLE_AUTO_START_SETTING, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_INTERNAL_TEST, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_VOICE, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_VOICE_TEST, "false");
        BuildCache.getInstance().putString(BuildConstance.FORCE_ADV_MODE, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_CACHE_HOME_DATA, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_TEST_ERROR_CODE_AND_UPGRADE, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_PREFER_SYSTEMPLAYER_FOR_4K, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_SCREENSAVER, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_OPEN_TEST_PERFORMANCE, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_ISOPEN_KEYBOARDLOGIN, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_ISOPEN_TOPBAR_ANIMATION, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_ISOPEN_MESSAGE_CENTER, "true");
        BuildCache.getInstance().putString("APK_ISOPEN_CHECKIN_FUN", "true");
        BuildCache.getInstance().putString("APK_ISOPEN_CHECKIN_RECOMMEND", "true");
        BuildCache.getInstance().putString(BuildConstance.SUPPORT_SETTING_CONTENTPROVIDER, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_PLAYER_MULTI_PROCESS, "false");
        BuildCache.getInstance().putString(BuildConstance.PLAYER_USE_WHITE_LIST, "true");
        BuildCache.getInstance().putString(BuildConstance.APP_IS_CHECK_MONKEY, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_GUIDE, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_CACHE_DEVICE_CHECK, "false");
        BuildCache.getInstance().putString(BuildConstance.PLAYER_OPEN_NETDOCTOR_ONERROR, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_ENABLE_EXTRA_PAGE, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_ISINIT_CRASHHANDLER, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_OPEN_VIPRIGHTS, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_SUPPORT_SUBSCRIBE, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_SUPPORT_DESKTOP_MANAGE, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_SUPPORT_MONKEY_TEST, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_ENABLE_HCDN_PREDEPLOY, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_PINGBACK_DEBUG, "false");
        BuildCache.getInstance().putString(BuildConstance.APK_SUPPORT_MULTISCREEN, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_ENABLE_VIP_ANIMATION, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_SUPPORT_CAROUSEL, "true");
        BuildCache.getInstance().putString(BuildConstance.APK_IS_SUPPORT_RECOMMEND_APP, "true");
    }

    private int getInt(String name) {
        return StringUtils.parseInt(name);
    }

    private boolean getBoolean(String name) {
        return "true".equalsIgnoreCase(name);
    }

    private void checkMediaPlayerTypeConfig() {
        this.mMediaPlayerType = BuildCache.getInstance().getString(BuildConstance.APK_MEDIAPLAYERTYPE, "3");
        if (!(this.mMediaPlayerType.equals("1") || this.mMediaPlayerType.equals("2") || this.mMediaPlayerType.equals("3") || this.mMediaPlayerType.equals("4") || this.mMediaPlayerType.equals("5"))) {
            this.mMediaPlayerType = String.valueOf(3);
        }
        BuildCache.getInstance().putString(BuildConstance.APK_MEDIAPLAYERTYPE, this.mMediaPlayerType);
    }

    private void init() {
        prepare();
        setup();
        this.mIsMatchDevice = checkModel();
    }

    public boolean isMatchDevice() {
        if (getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_GITV_UI, "false"))) {
            return false;
        }
        return this.mIsMatchDevice;
    }

    private boolean checkModel() {
        String customerConfig = BuildCache.getInstance().getString(BuildConstance.APK_CUSTOMER, "gala");
        boolean ret = false;
        String model = Build.MODEL;
        Log.d(TAG, "config model = " + model);
        if (TextUtils.isEmpty(model)) {
            return 0;
        }
        String customer = null;
        if (model.toUpperCase().startsWith("MIBOX") || model.toUpperCase().startsWith("MITV")) {
            customer = "xiaomi";
        }
        if (!(customerConfig == null || customer == null || (!customerConfig.equals(customer) && !customerConfig.equalsIgnoreCase("channel")))) {
            ret = true;
            BuildCache.getInstance().putString(BuildConstance.APK_CUSTOMER, customer);
        }
        return ret;
    }

    private JSONObject loadConfigData() {
        Exception e;
        JSONObject jsonData = null;
        try {
            InputStream builtinManifestStream = AppRuntimeEnv.get().getApplicationContext().getAssets().open("app.cfg");
            int builtinSize = builtinManifestStream.available();
            byte[] buffer = new byte[builtinSize];
            builtinManifestStream.read(buffer);
            builtinManifestStream.close();
            this.mConfigString = new String(buffer, 0, builtinSize);
            JSONObject jsonData2 = new JSONObject(this.mConfigString);
            try {
                if (jsonData2.getJSONArray(this.mHostUUid) != null) {
                    updateConfig(jsonData2, this.mHostUUid);
                } else {
                    updateConfig(jsonData2, this.mCommon);
                }
                jsonData = jsonData2;
                return jsonData2;
            } catch (Exception e2) {
                e = e2;
                jsonData = jsonData2;
                e.printStackTrace();
                return jsonData;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            return jsonData;
        }
    }

    private void updateConfig(JSONObject data, String tag) {
        try {
            JSONArray jsonArray = data.getJSONArray(tag);
            for (String key : BuildCache.getInstance().getMap().keySet()) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    if (!StringUtils.isEmpty(object.getString(key))) {
                        BuildCache.getInstance().putString(key, object.getString(key));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setup() {
        try {
            List<Appcfg> cfgs = JSON.parseArray(StreamUtils.convertStreamToString(AppRuntimeEnv.get().getApplicationContext().getAssets().open("app_epg.cfg")), Appcfg.class);
            String uuid = HostBuild.getUUID();
            String packageName = HostBuild.getPackageName();
            loadAppConfig(uuid, HostBuild.getUIStyle(), HostBuild.getPingback(), packageName, HostBuild.getCustomer(), HostBuild.getApkVersion(), HostBuild.getVersionCode());
            boolean containerUUid = false;
            Appcfg defaultcfg = null;
            for (Appcfg cfg : cfgs) {
                if (cfg.getUuids() != null) {
                    if (cfg.getUuids().contains(uuid)) {
                        loadAppEpgConfig(cfg);
                        containerUUid = true;
                        break;
                    } else if (cfg.getUuids().contains(PluginType.DEFAULT_TYPE)) {
                        defaultcfg = cfg;
                    }
                }
            }
            if (!containerUUid) {
                if (defaultcfg != null) {
                    loadAppEpgConfig(defaultcfg);
                } else {
                    loadAppEpgConfig(new Appcfg());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkMediaPlayerTypeConfig(Appcfg config) {
        this.mMediaPlayerType = getDefaultValueIfNull(config.getApk_mediaplayertype(), "3");
        if (!(this.mMediaPlayerType.equals("1") || this.mMediaPlayerType.equals("2") || this.mMediaPlayerType.equals("3") || this.mMediaPlayerType.equals("4") || this.mMediaPlayerType.equals("5"))) {
            this.mMediaPlayerType = String.valueOf(3);
        }
        addInBuildCache(BuildConstance.APK_MEDIAPLAYERTYPE, this.mMediaPlayerType);
    }

    private void addInBuildCache(String key, String value) {
        BuildCache.getInstance().putString(key, value);
    }

    private String getDefaultValueIfNull(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    private void loadAppConfig(String uuid, String apkUiStyle, String pingBack, String packageName, String custom, String apkVersion, String versionCode) {
        addInBuildCache(BuildConstance.VRS_UUID, getDefaultValueIfNull(uuid, BuildDefaultDocument.VRS_UUID));
        addInBuildCache(BuildConstance.APK_UI_STYLE, getDefaultValueIfNull(apkUiStyle, BuildDefaultDocument.APK_UI_STYLE));
        addInBuildCache(BuildConstance.APK_PINGBACK_P2, getDefaultValueIfNull(pingBack, ""));
        addInBuildCache(BuildConstance.APK_PACKAGE_NAME, getDefaultValueIfNull(packageName, BuildDefaultDocument.APK_PACKAGE_NAME));
        addInBuildCache(BuildConstance.APK_CUSTOMER, getDefaultValueIfNull(custom, "gala"));
        addInBuildCache(BuildConstance.APK_VERSION, getDefaultValueIfNull(apkVersion, "0"));
        addInBuildCache(BuildConstance.SVN_REVISION, getDefaultValueIfNull(versionCode, "0"));
    }

    private void loadAppEpgConfig(Appcfg config) {
        if (config != null) {
            addInBuildCache(BuildConstance.DATA_VERSION, getDefaultValueIfNull(config.getData_version(), ""));
            addInBuildCache(BuildConstance.APK_PRODUCT, getDefaultValueIfNull(config.getApk_product(), BuildDefaultDocument.APK_PRODUCT));
            addInBuildCache(BuildConstance.APK_DOMAIN_NAME, getDefaultValueIfNull(config.getApk_domain_name(), BuildDefaultDocument.APK_DOMAIN_NAME));
            checkMediaPlayerTypeConfig(config);
            addInBuildCache(BuildConstance.APK_BUILD_TIME, getDefaultValueIfNull(config.getApk_build_time(), ""));
            addInBuildCache(BuildConstance.APK_CUSTOMER_PACKAGES, getDefaultValueIfNull(config.getApk_customer_packages(), BuildDefaultDocument.APK_CUSTOMER_PACKAGES));
            addInBuildCache(BuildConstance.APK_OPENAPI_SIGNATURE, getDefaultValueIfNull(config.getApk_openapi_signature(), ""));
            addInBuildCache(BuildConstance.APK_OPENAPI_OLD_UUID, getDefaultValueIfNull(config.getApk_openapi_old_uuid(), ""));
            addInBuildCache(BuildConstance.APK_UNIQUE_SECRET_KEY, getDefaultValueIfNull(config.getApk_unique_secret_key(), ""));
            addInBuildCache(BuildConstance.APP_STORE_PKG_NAME, getDefaultValueIfNull(config.getApp_store_pkg_name(), "com.gitv.tvappstore"));
            addInBuildCache(BuildConstance.APP_GAMESTORE_PKG_NAME, getDefaultValueIfNull(config.getApp_gamestore_pkg_name(), BuildDefaultDocument.APP_GAMESTORE_PKG_NAME));
            addInBuildCache(BuildConstance.APK_KEYBORAD_TYPE, getDefaultValueIfNull(config.getApk_keyborad_type(), "2"));
            addInBuildCache(BuildConstance.AD_PLAYER_ID, getDefaultValueIfNull(config.getAd_player_id(), "qc_100001_100145"));
            addInBuildCache(BuildConstance.APK_FORCEOPEN_4K, getDefaultValueIfNull(config.getApk_forceopen_4k(), "0"));
            addInBuildCache(BuildConstance.APK_OPENAPI_FEATURE_LIST, getDefaultValueIfNull(config.getApk_openapi_feature_list(), ""));
            addInBuildCache(BuildConstance.APK_BROADCAST_ACTIONS, getDefaultValueIfNull(config.getApk_broadcast_actions(), "ACTION_DETAIL"));
            addInBuildCache(BuildConstance.APK_ISHOME, getDefaultValueIfNull(config.getApk_ishome(), "false"));
            addInBuildCache(BuildConstance.APK_SHOW_VOLUME, getDefaultValueIfNull(config.getApk_show_volume(), "true"));
            addInBuildCache(BuildConstance.APK_SUPPORT_ALBUM_DETAIL_WINDOW_PLAY, getDefaultValueIfNull(config.getApk_support_album_detail_window_play(), "true"));
            addInBuildCache(BuildConstance.APK_ISPINGBACKOFFICIAL, getDefaultValueIfNull(config.getApk_ispingbackofficial(), "true"));
            addInBuildCache(BuildConstance.APK_SHOW_VIP, getDefaultValueIfNull(config.getApk_show_vip(), "true"));
            addInBuildCache(BuildConstance.APK_SUPPORT_DOBY, getDefaultValueIfNull(config.getApk_support_doby(), "true"));
            addInBuildCache(BuildConstance.APK_SUPPORT_H265, getDefaultValueIfNull(config.getApk_support_h265(), "false"));
            addInBuildCache(BuildConstance.APK_DISABLE_SERVICE_BOOTUP, getDefaultValueIfNull(config.getApk_disable_service_bootup(), "false"));
            addInBuildCache(BuildConstance.APK_SUPPORT_ANDROIDCACHE, getDefaultValueIfNull(config.getApk_support_androidcache(), "false"));
            addInBuildCache(BuildConstance.APK_GITV_UI, getDefaultValueIfNull(config.getApk_gitv_ui(), "false"));
            addInBuildCache(BuildConstance.APK_SUPPORT_OTHER_DOAIN, getDefaultValueIfNull(config.getApk_support_other_doain(), "true"));
            addInBuildCache(BuildConstance.APK_USE_ALBUM_LIST_CACHE, getDefaultValueIfNull(config.getApk_use_album_list_cache(), "true"));
            addInBuildCache(BuildConstance.APK_SHOULD_AUTH_MAC, getDefaultValueIfNull(config.getApk_should_auth_mac(), "false"));
            addInBuildCache(BuildConstance.APK_ENABLE_BISDK, getDefaultValueIfNull(config.getApk_enable_bisdk(), "true"));
            addInBuildCache(BuildConstance.APK_SHOW_LIVE, getDefaultValueIfNull(config.getApk_show_live(), "true"));
            addInBuildCache(BuildConstance.APK_IS_CRACKED, getDefaultValueIfNull(config.getApk_is_cracked(), "false"));
            addInBuildCache(BuildConstance.APK_ENABLE_AUTO_START_SETTING, getDefaultValueIfNull(config.getApk_enable_auto_start_setting(), "false"));
            addInBuildCache(BuildConstance.APK_INTERNAL_TEST, getDefaultValueIfNull(config.getApk_internal_test(), "false"));
            addInBuildCache(BuildConstance.APK_SUPPORT_VOICE, getDefaultValueIfNull(config.getApk_support_voice(), "false"));
            addInBuildCache(BuildConstance.APK_SUPPORT_VOICE_TEST, getDefaultValueIfNull(config.getApk_support_voice(), "false"));
            addInBuildCache(BuildConstance.FORCE_ADV_MODE, getDefaultValueIfNull(config.getForce_adv_mode(), "false"));
            addInBuildCache(BuildConstance.APK_CACHE_HOME_DATA, getDefaultValueIfNull(config.getApk_cache_home_data(), "true"));
            addInBuildCache(BuildConstance.APK_TEST_ERROR_CODE_AND_UPGRADE, getDefaultValueIfNull(config.getApk_test_error_code_and_upgrade(), "false"));
            addInBuildCache(BuildConstance.APK_PREFER_SYSTEMPLAYER_FOR_4K, getDefaultValueIfNull(config.getApk_prefer_systemplayer_for_4k(), "false"));
            addInBuildCache(BuildConstance.APK_SUPPORT_SCREENSAVER, getDefaultValueIfNull(config.getApk_support_screensaver(), "true"));
            addInBuildCache(BuildConstance.APK_IS_OPEN_TEST_PERFORMANCE, getDefaultValueIfNull(config.getApk_is_open_test_performance(), "false"));
            addInBuildCache(BuildConstance.APK_ISOPEN_KEYBOARDLOGIN, getDefaultValueIfNull(config.getApk_isopen_keyboardlogin(), "false"));
            addInBuildCache(BuildConstance.APK_ISOPEN_TOPBAR_ANIMATION, getDefaultValueIfNull(config.getApk_isopen_topbar_animation(), "true"));
            addInBuildCache(BuildConstance.APK_ISOPEN_MESSAGE_CENTER, getDefaultValueIfNull(config.getApk_isopen_message_center(), "true"));
            addInBuildCache(BuildConstance.SUPPORT_SETTING_CONTENTPROVIDER, getDefaultValueIfNull(config.getSupport_setting_contentprovider(), "true"));
            initPlayerMultiProcessSwitcher(config);
            addInBuildCache(BuildConstance.PLAYER_USE_WHITE_LIST, getDefaultValueIfNull(config.getPlayer_use_white_list(), "true"));
            addInBuildCache(BuildConstance.APP_IS_CHECK_MONKEY, getDefaultValueIfNull(config.getApp_is_check_monkey(), "false"));
            addInBuildCache(BuildConstance.APK_SUPPORT_GUIDE, getDefaultValueIfNull(config.getApk_support_guide(), "true"));
            addInBuildCache(BuildConstance.APK_CACHE_DEVICE_CHECK, getDefaultValueIfNull(config.getApk_cache_device_check(), "false"));
            addInBuildCache(BuildConstance.PLAYER_OPEN_NETDOCTOR_ONERROR, getDefaultValueIfNull(config.getPlayer_open_netdoctor_onerror(), "true"));
            addInBuildCache(BuildConstance.APK_ENABLE_EXTRA_PAGE, getDefaultValueIfNull(config.getApk_enable_extra_page(), "false"));
            addInBuildCache(BuildConstance.APK_ISINIT_CRASHHANDLER, getDefaultValueIfNull(config.getApk_isinit_crashhandler(), "true"));
            addInBuildCache(BuildConstance.APK_IS_OPEN_VIPRIGHTS, getDefaultValueIfNull(config.getApk_is_open_viprights(), "false"));
            addInBuildCache(BuildConstance.APK_IS_SUPPORT_SUBSCRIBE, getDefaultValueIfNull(config.getApk_is_support_subscribe(), "true"));
            addInBuildCache(BuildConstance.APK_IS_SUPPORT_DESKTOP_MANAGE, getDefaultValueIfNull(config.getApk_is_support_desktop_manage(), "true"));
            addInBuildCache(BuildConstance.APK_IS_SUPPORT_MONKEY_TEST, getDefaultValueIfNull(config.getApk_is_support_monkey_test(), "false"));
            addInBuildCache(BuildConstance.APK_ENABLE_HCDN_PREDEPLOY, getDefaultValueIfNull(config.getApk_enable_hcdn_predeploy(), "true"));
            addInBuildCache(BuildConstance.APK_IS_PINGBACK_DEBUG, getDefaultValueIfNull(config.getApk_is_pingback_debug(), "false"));
            addInBuildCache(BuildConstance.APK_ENABLE_VIP_ANIMATION, getDefaultValueIfNull(config.getApk_enable_vip_animation(), "true"));
            addInBuildCache(BuildConstance.APK_IS_SUPPORT_CAROUSEL, getDefaultValueIfNull(config.getApk_is_support_carousel(), "true"));
            addInBuildCache(BuildConstance.APK_IS_SUPPORT_RECOMMEND_APP, getDefaultValueIfNull(config.getApk_is_support_recommend_app(), "true"));
            addInBuildCache(BuildConstance.APK_SUPPORT_MULTISCREEN, getDefaultValueIfNull(config.getApk_support_multiscreen(), "true"));
        }
    }

    private void initPlayerMultiProcessSwitcher(Appcfg appcfg) {
        boolean isSupportPlayerMultiProcess;
        int configValue = StringUtils.parseInt(getDefaultValueIfNull(appcfg.getApk_support_player_multi_process(), "false"));
        switch (configValue) {
            case 1:
                isSupportPlayerMultiProcess = true;
                break;
            case 2:
                isSupportPlayerMultiProcess = false;
                break;
            default:
                PlayerMultiProcessSwitchHelper.debugPlayerMultiProcess();
                isSupportPlayerMultiProcess = PlayerMultiProcessSwitchHelper.getSwitchValue();
                break;
        }
        addInBuildCache(BuildConstance.APK_SUPPORT_PLAYER_MULTI_PROCESS, getDefaultValueIfNull(appcfg.getApk_support_player_multi_process(), isSupportPlayerMultiProcess + ""));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initPlayerMultiProcessSwitcher: configValue=" + configValue + ", isSupportPlayerMultiProc=" + isSupportPlayerMultiProcess);
        }
    }

    private int getIntProperty(Properties props, String name, int defValue) {
        return StringUtils.parseInt(props.getProperty(name, defValue + ""));
    }

    private static void safeClose(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
