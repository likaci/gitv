package com.gala.video.app.epg.init.task;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap.Config;
import android.os.Build.VERSION;
import android.util.Log;
import android.webkit.WebView;
import com.gala.download.DownloaderAPI;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.sdk.player.Locale;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.PluginType;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.tv.voice.service.ResourceSemanticTranslator;
import com.gala.tv.voice.service.ResourceSemanticTranslator.Filter;
import com.gala.tvapi.TVApiConfig;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.video.app.epg.R.array;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.app.epg.home.ads.controller.StartScreenVideoAd;
import com.gala.video.app.epg.home.data.tool.GroupDetailABTestListener;
import com.gala.video.app.epg.preference.StartUpPreferrence;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.utils.VersionUtils;
import com.gala.video.app.epg.voice.config.VoiceAppConfig;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.voice.IVoiceStartUpPrepareListener;
import com.gala.video.lib.framework.coreservice.voice.VoiceStartup;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.configs.DeviceManager;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.common.configs.PluginConstants;
import com.gala.video.lib.share.common.configs.ServerConfig;
import com.gala.video.lib.share.ifimpl.logrecord.preference.LogRecordPreference;
import com.gala.video.lib.share.ifimpl.logrecord.utils.CrashUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder.LoginCallbackRecorderListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.uikit.cache.UikitDataCache;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.AdsClient;
import com.xcrash.crashreporter.CrashReporter;
import com.xcrash.crashreporter.generic.CrashReportParamsBuilder;
import com.xcrash.crashreporter.generic.ICrashCallback;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;

public class CommonInitTask implements Runnable {
    private static final String TAG = "startup/CommonInitTask";
    private String mDefaultUserId = "";
    private final LoginCallbackRecorderListener mLoginListener = new LoginCallbackRecorderListener() {
        public void onLogout(String uid) {
            GetInterfaceTools.getIHistoryCacheManager().clearLoginUserDb();
            GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListForNoLogin();
        }

        public void onLogin(String uid) {
            GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListFromCloud();
        }
    };

    public void run() {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        ImageProviderApi.getImageProvider().setEnableScale(true);
        DeviceManager.initialize(AppRuntimeEnv.get().getApplicationContext());
        if ("true".equalsIgnoreCase(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_VOICE, "false"))) {
            VoiceStartup.initialize(VoiceAppConfig.getVoiceListenerList(), new IVoiceStartUpPrepareListener() {
                public void onPrepare(Filter filter) {
                    new ResourceSemanticTranslator(AppRuntimeEnv.get().getApplicationContext(), array.class, filter).prepare();
                }
            });
        }
        LoginCallbackRecorder.get().addListener(this.mLoginListener);
        registerNetworkStatusChanged();
        initTVApi();
        initNetWorkManager();
        LogUtils.setDebug(Project.getInstance().getControl().debugMode());
        initPingback(context);
        DownloaderAPI.getDownloader().initialize(context, this.mDefaultUserId);
        DownloaderAPI.getDownloader().setGifLimitSize(1024);
        ImageProviderApi.getImageProvider().initialize(context, this.mDefaultUserId);
        ImageProviderApi.getImageProvider().setMemoryCacheSize(5242880);
        ImageProviderApi.getImageProvider().setBitmapPoolSize(3145728);
        ImageProviderApi.getImageProvider().setEnableFullPathCacheKey(false);
        ImageProviderApi.getImageProvider().setThreadSize(HomeDataConfig.LOW_PERFORMANCE_DEVICE ? 2 : 4);
        LogUtils.i(TAG, "set ImageProvider config RGB_565");
        ImageProviderApi.getImageProvider().setDecodeConfig(Config.RGB_565);
        GetInterfaceTools.getIGalaAccountManager().setAccountType();
        UikitDataCache.getInstance().register();
        initCrashMode(context);
        initPlugin();
        initAdsClient();
        GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListFromCloudDelay();
        VersionUtils.get().markOpenApp(context);
        if (VersionUtils.get().isAfterUpdate()) {
            new WebView(context).clearCache(true);
        }
    }

    private void initNetWorkManager() {
        NetWorkManager.getInstance().initNetWorkManager(AppRuntimeEnv.get().getApplicationContext(), Project.getInstance().getBuild().getDomainName());
    }

    public void initCrashMode(Context context) {
        if (Project.getInstance().getBuild().isInitCrashHandler()) {
            CrashReporter.getInstance().init(context, new CrashReportParamsBuilder().setCallback(new ICrashCallback() {
                public JSONObject getAppData(String s, boolean b, int i) {
                    return null;
                }

                public void onCrash(JSONObject jsonObject, int type, String javaMessage) {
                    try {
                        String errorReason = "";
                        String activityName = "";
                        String crashType = ISearchConstant.TVSRCHSOURCE_OTHER;
                        String excptnnm = "";
                        Log.i(CommonInitTask.TAG, "type =" + type);
                        String backtrace = "crash backtrace";
                        if (type == 3) {
                            Log.i(CommonInitTask.TAG, "java crash");
                            CharSequence backtrace2 = (String) jsonObject.get("CrashMsg");
                            if (StringUtils.isEmpty(backtrace2)) {
                                Log.i(CommonInitTask.TAG, "backtrace is null ");
                            } else if (CommonInitTask.this.isHomeCrash(backtrace2)) {
                                CommonInitTask.this.clearHomeCacheWhenCrash();
                            }
                            backtrace = CrashUtils.getFormatBacktrace(backtrace2);
                            activityName = CrashUtils.getCrashActivityName(backtrace);
                            crashType = "JAVA";
                        } else if (type == 2) {
                            Log.i(CommonInitTask.TAG, "ANR");
                            backtrace = (String) jsonObject.get("Backtrace");
                            crashType = "ANR";
                            CommonInitTask.this.handleAdCrash();
                        } else if (type == 1) {
                            backtrace = (String) jsonObject.get("Backtrace");
                            Log.i(CommonInitTask.TAG, "Native crash");
                            crashType = "NATIVE";
                            CommonInitTask.this.handleAdCrash();
                        }
                        excptnnm = CrashUtils.getExceptionName(backtrace, crashType);
                        Log.i(CommonInitTask.TAG, "crash backtrace = " + backtrace);
                        Log.i(CommonInitTask.TAG, "crash backtrace length = " + backtrace.length());
                        LogRecordPreference.saveCrashType(AppRuntimeEnv.get().getApplicationContext(), crashType);
                        LogRecordPreference.saveException(AppRuntimeEnv.get().getApplicationContext(), excptnnm);
                        LogRecordPreference.saveCrashMeminfo(AppRuntimeEnv.get().getApplicationContext(), DeviceUtils.getMemoryPrint());
                        Log.v(CommonInitTask.TAG, "crashType = " + crashType);
                        Log.v(CommonInitTask.TAG, "excptnnm = " + excptnnm);
                        String crashDetail = backtrace;
                        if (!StringUtils.isEmpty((CharSequence) crashDetail) && crashDetail.length() >= 200) {
                            crashDetail = crashDetail.substring(0, 200);
                        }
                        LogRecordPreference.saveCrashDetail(AppRuntimeEnv.get().getApplicationContext(), crashDetail);
                        if (!StringUtils.isEmpty((CharSequence) backtrace) && backtrace.length() >= 102400) {
                            backtrace = backtrace.substring(0, 102400);
                        }
                        errorReason = backtrace;
                        Log.i(CommonInitTask.TAG, "errorReason:" + backtrace);
                        String filepath = (String) jsonObject.get("Path");
                        Log.i(CommonInitTask.TAG, "crash file path = " + filepath);
                        String eventID = PingBackUtils.createEventId();
                        LogRecordPreference.saveEventId(AppRuntimeEnv.get().getApplicationContext(), eventID);
                        long leftdatasize = CrashUtils.getLeftDataSize();
                        Log.i(CommonInitTask.TAG, "leftdatasize  = " + leftdatasize);
                        PingBackParams params = new PingBackParams();
                        params.add(Keys.T, "0").add("ec", "303").add("pfec", "").add(Keys.ERREASON, errorReason).add("activity", activityName).add(Keys.EXCPTNNM, excptnnm).add(Keys.LEFTDATASIZE, leftdatasize + "").add(Keys.CRASHTYPE, crashType).add("e", eventID);
                        for (AbsPluginProvider each : PluginManager.instance().getProviders()) {
                            Log.v(CommonInitTask.TAG, "each.getId() = " + each.getId() + " ,each.getVersionName() = " + each.getVersionName());
                            if (!(StringUtils.isEmpty(each.getId()) || StringUtils.isEmpty(each.getVersionName()))) {
                                params.add(each.getId(), each.getVersionName());
                            }
                        }
                        PingBack.getInstance().postPingBackToLongYuan(params.build());
                        GetInterfaceTools.getILogRecordProvider().notifySaveLogFile();
                        LogRecordPreference.saveCrashFilePath(AppRuntimeEnv.get().getApplicationContext(), filepath);
                        Thread.sleep(1000);
                        CommonInitTask.this.finishActivity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public boolean disableUploadCrash() {
                    return true;
                }
            }).enableFullLog(true).setLogSize(500).disableLogcat(true).build());
        }
    }

    public void initTVApi() {
        TVApiConfig.setDomain(Project.getInstance().getBuild().getDomainName());
        TVApiProperty property = TVApiBase.getTVApiProperty();
        property.setDebugFlag(ServerConfig.isTVApiDebugEnabled());
        property.setOSVersion(VERSION.RELEASE.toString());
        property.setCheckYinHe(EpgAppConfig.shouldAuthMac());
        property.setContext(AppRuntimeEnv.get().getApplicationContext());
        property.setShowLiveFlag(Project.getInstance().getBuild().isShowLive());
        property.setShowVipFlag(true);
        property.setCacheDeviceCheckFlag(Project.getInstance().getBuild().shouldCacheDeviceCheck());
        property.setIpAddress(DeviceUtils.getIpAddress());
        property.setHardware(DeviceUtils.getHardwareInfo());
        property.setMemorySize(String.valueOf(DeviceUtils.getTotalMemory()));
        property.setHostVersion(Project.getInstance().getBuild().getShowVersion());
        TVApiBase.createRegisterKey(DeviceUtils.getMacAddr(), Project.getInstance().getBuild().getVrsUUID(), Project.getInstance().getBuild().getVersionString());
        setDefaultUserId(property.getAnonymity());
        GroupDetailABTestListener.get().setListener();
        com.gala.tvapi.tv3.TVApiConfig config = com.gala.tvapi.tv3.TVApiConfig.get();
        config.setContext(AppRuntimeEnv.get().getApplicationContext());
        config.setApkVersion(Project.getInstance().getBuild().getVersionString());
        config.setUuid(Project.getInstance().getBuild().getVrsUUID());
        config.setMac(DeviceUtils.getMacAddr());
        config.setHardware(DeviceUtils.getHardwareInfo());
        config.setMemorySize(DeviceUtils.getTotalMemory());
        config.setHostVersion(Project.getInstance().getBuild().getShowVersion());
    }

    private void registerNetworkStatusChanged() {
        NetWorkManager.getInstance().registerStateChangedListener(new OnNetStateChangedListener() {
            public void onStateChanged(int from, int to) {
                LogUtils.d(CommonInitTask.TAG, "registerNetworkStatusChanged from " + from + " to " + to);
                switch (to) {
                    case 1:
                    case 2:
                        CommonInitTask.this.initTVApi();
                        CommonInitTask.this.initPingback(AppRuntimeEnv.get().getApplicationContext());
                        return;
                    default:
                        return;
                }
            }
        });
    }

    private void initPlugin() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initPlugin<<()");
        }
        Map<String, String> extras = new HashMap();
        boolean openPluginIOBalance = SettingPlayPreference.isOpenPluginIOBalance(ResourceUtil.getContext());
        extras.put(FileUtils.OPEN_PLUGIN_BALANCE, openPluginIOBalance ? "0" : "1");
        String domain = Project.getInstance().getBuild().getDomainName();
        String apkVersion = Project.getInstance().getBuild().getApkThirdVersionCode();
        String hostVersion = Project.getInstance().getBuild().getVersionString();
        AppInfo appInfo = new AppInfo();
        appInfo.putPluginType("pluginplayer", PluginType.DEFAULT_TYPE);
        appInfo.putPluginType(PluginConstants.CROSSWALKPLUGIN_ID, PluginType.DEFAULT_TYPE);
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            appInfo.putPluginType(PluginConstants.LOGRECORDPLUGIN_ID, PluginType.DEFAULT_TYPE);
        } else {
            appInfo.putPluginType(PluginConstants.LOGRECORDPLUGIN_ID, PluginType.DEFAULT_SINGLE_PROCESS_TYPE);
        }
        appInfo.putPluginType(PluginConstants.INTERTRUST_DRM_PLUGIN_ID, PluginType.EMPTY_TYPE);
        appInfo.setHostAllowDebug(AppClientUtils.isDebugMode());
        appInfo.putExtras(extras);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initailizePlugin: hostVersion=" + hostVersion + ", domain=" + domain + ", apkVersion=" + apkVersion + "isMulti =" + Project.getInstance().getBuild().supportPlayerMultiProcess() + "isPlayerProcess =" + GetInterfaceTools.getIInit().isPlayerProcess());
        }
        PluginManager.initizlie(AppRuntimeEnv.get().getApplicationContext(), appInfo, Project.getInstance().getBuild().supportPlayerMultiProcess());
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initPlugin>>()" + openPluginIOBalance);
        }
    }

    public void initPingback(Context context) {
        LogUtils.d(TAG, "initPingBack");
        GetInterfaceTools.getIGalaVipManager().setPingBackVipAct();
        PingBackInitParams initParams = new PingBackInitParams();
        initParams.sIsDebug = Project.getInstance().getBuild().isPingbackDebug();
        initParams.sDomain = Project.getInstance().getBuild().getDomainName();
        initParams.sP2 = Project.getInstance().getBuild().getPingbackP2();
        initParams.sAppVersion = AppClientUtils.getClientVersion();
        initParams.sUUID = Project.getInstance().getBuild().getVrsUUID();
        initParams.sHostVer = Project.getInstance().getBuild().getShowVersion();
        initParams.sAnonymityId = this.mDefaultUserId;
        initParams.sDeviceId = TVApiBase.getTVApiProperty().getPassportDeviceId();
        initParams.sIsNewUser = SystemConfigPreference.isNewUser(context);
        boolean z = Project.getInstance().getBuild().isLitchi() || Project.getInstance().getBuild().isGitvUI();
        initParams.sIsSendYinHePingBack = z;
        initParams.sMod = Locale.CHINESE_SIMPLIFIED;
        PingBack.getInstance().initialize(context, initParams);
    }

    public String getDefaultUserId() {
        return this.mDefaultUserId;
    }

    private void setDefaultUserId(String defaultUserId) {
        this.mDefaultUserId = defaultUserId;
        AppRuntimeEnv.get().setDefaultUserId(defaultUserId);
    }

    private void initAdsClient() {
        CharSequence domainName = Project.getInstance().getBuild().getDomainName();
        if (!StringUtils.isEmpty(domainName)) {
            AdsClient.setTvDomain(domainName);
        }
    }

    private boolean isStartVideoCrash() {
        return StartScreenVideoAd.isVideoAdPlaying();
    }

    private void handleAdCrash() {
        if (isStartVideoCrash()) {
            int adCrashTimes = StartUpPreferrence.getCrashTimes(AppRuntimeEnv.get().getApplicationContext(), "start_up_video_crash_" + AppClientUtils.getVersionHeader());
            LogUtils.e(TAG, "ad crash " + adCrashTimes);
            StartUpPreferrence.saveCrashTimes(AppRuntimeEnv.get().getApplicationContext(), "start_up_video_crash_" + AppClientUtils.getVersionHeader(), adCrashTimes + 1);
        }
    }

    private boolean isHomeCrash(String message) {
        return !StringUtils.isEmpty((CharSequence) message) && (message.contains("com.gala.video.app.epg.home") || message.contains("com.gala.video.lib.share.uikit"));
    }

    private void clearHomeCacheWhenCrash() {
        deleteDir(new File(AppRuntimeEnv.get().getApplicationContext().getFilesDir() + "/" + "home/home_cache/"));
    }

    private void deleteDir(File dir) {
        if (dir != null) {
            if (dir.isDirectory()) {
                for (File child : dir.listFiles()) {
                    deleteDir(child);
                }
            }
            LogUtils.d(TAG, "delete child dir= " + dir + ",ret = " + dir.delete());
        }
    }

    public void finishActivity() {
        List<Activity> list = AppRuntimeEnv.get().getActivityList();
        LogUtils.e(TAG, "finishActivity =====  " + list.size());
        for (Activity activity : list) {
            if (activity != null) {
                activity.finish();
            }
        }
    }
}
