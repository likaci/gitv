package com.gala.sdk.plugin.server.core;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.AbsPluginProvider.OnExceptionListener;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.LoadProviderException;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.PluginType;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.ResultCode.ERROR_TYPE;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.sdk.plugin.server.pingback.IPluginDeleteListener;
import com.gala.sdk.plugin.server.pingback.IPluginDownloadListener;
import com.gala.sdk.plugin.server.pingback.PluginPingbackParams;
import com.gala.sdk.plugin.server.storage.PluginInfo;
import com.gala.sdk.plugin.server.storage.StorageManager;
import com.gala.sdk.plugin.server.upgrade.UpgradeInfo;
import com.gala.sdk.plugin.server.upgrade.UpgradeManager;
import com.gala.sdk.plugin.server.utils.DataUtils;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.ListUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import com.gala.sdk.plugin.server.utils.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PluginHelper {
    private static final String TAG = "PluginHelper";
    private static final String WORK_THREAD_NAME = "plugin_check_upgrade_thread";
    private final AppInfo mAppInfo;
    IPluginDeleteListener mDeleteListener;
    IPluginDownloadListener mDownloadListener;
    private final Context mHostApplicationContext;
    private final OnExceptionListener mOnExceptionListener = new C01731();
    private final Map<String, PluginProperty> mPropertys = new HashMap();
    private final PluginProviderBuilder mProviderBuilder;
    private final Map<String, AbsPluginProvider> mProviders;
    private final StorageManager mStorageManager;
    private final TimerHandler mTimerHandler;
    private final UpgradeManager mUpgradeManager;
    private final HandlerThread mWorkThread;

    class C01731 implements OnExceptionListener {
        C01731() {
        }

        public void onException(AbsPluginProvider provider, Throwable e) {
            if (Log.DEBUG) {
                Log.m430d(PluginHelper.TAG, "onException() provider=" + provider + ", e=" + e);
            }
            increaseErrorCount(provider);
        }

        private void increaseErrorCount(AbsPluginProvider provider) {
        }
    }

    private class TimerHandler extends Handler {
        private static final long CHECK_INTERVAL_TIME_1H = 3600000;
        private static final String MSG_BUNDLE_HOSTPLUGININFO_KEY = "host_plugin_info";
        final long firstUpgradeDelayTime = Math.min(this.minInterval, 300000);
        final long minInterval = PluginPropertyConfig.getUpgradeInterval(PluginHelper.this);

        public TimerHandler(Looper looper) {
            super(looper);
        }

        public void startTimer(HostPluginInfo hPluginInfo) {
            if (Log.VERBOSE) {
                Log.m434v(PluginHelper.TAG, "startTimer=" + hPluginInfo + ")");
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable(MSG_BUNDLE_HOSTPLUGININFO_KEY, hPluginInfo);
            Message msg = new Message();
            msg.what = hPluginInfo.hashCode();
            msg.setData(bundle);
            sendMessageDelayed(msg, this.firstUpgradeDelayTime);
        }

        public void handleMessage(Message msg) {
            if (Log.VERBOSE) {
                Log.m434v(PluginHelper.TAG, "handleMessage<<(msg=" + msg + ")");
            }
            HostPluginInfo hPluginInfo = (HostPluginInfo) msg.getData().getParcelable(MSG_BUNDLE_HOSTPLUGININFO_KEY);
            if (hPluginInfo != null) {
                checkUpgrade(hPluginInfo);
                Bundle bundleNew = new Bundle();
                bundleNew.putParcelable(MSG_BUNDLE_HOSTPLUGININFO_KEY, hPluginInfo);
                Message msgNew = new Message();
                msgNew.what = hPluginInfo.hashCode();
                msgNew.setData(bundleNew);
                sendMessageDelayed(msgNew, 86400000);
            } else if (Log.VERBOSE) {
                Log.m432e(PluginHelper.TAG, "handleMessage error (msg=" + msg + ")");
            }
            if (Log.VERBOSE) {
                Log.m434v(PluginHelper.TAG, "handleMessage>>()");
            }
        }

        private void checkUpgrade(HostPluginInfo hPluginInfo) {
            if (Log.VERBOSE) {
                Log.m434v(PluginHelper.TAG, "checkUpgrade<<(hPluginInfo=" + hPluginInfo + ")");
            }
            UpgradeInfo upgradeInfo = null;
            PluginInfo info = null;
            AbsPluginProvider provider = PluginHelper.this.getProvider(hPluginInfo.getPluginId());
            if (provider != null) {
                try {
                    upgradeInfo = PluginHelper.this.mUpgradeManager.checkUpgrade(provider.getVersionName(), hPluginInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (UpgradeInfo.isVaild(upgradeInfo)) {
                try {
                    info = PluginHelper.this.downloadPlugin(hPluginInfo, upgradeInfo);
                } catch (Throwable e2) {
                    e2.printStackTrace();
                }
            }
            if (Log.VERBOSE) {
                Log.m434v(PluginHelper.TAG, "checkUpgrade>>() return " + info);
            }
        }
    }

    public PluginHelper(Context context, AppInfo info, boolean isMultiProcess) {
        boolean z = false;
        this.mHostApplicationContext = context;
        this.mAppInfo = info;
        this.mProviders = new HashMap();
        this.mWorkThread = new HandlerThread(WORK_THREAD_NAME);
        this.mWorkThread.start();
        this.mTimerHandler = new TimerHandler(this.mWorkThread.getLooper());
        String balance = (String) this.mAppInfo.getExtras().get(FileUtils.OPEN_PLUGIN_BALANCE);
        if (!Util.isEmpty(balance) && Util.equals(balance, "1")) {
            FileUtils.sOpenBalance = false;
        }
        if (this.mAppInfo != null) {
            z = this.mAppInfo.getHostAllowDebug();
        }
        PluginDebugUtils.setDebug(z);
        this.mUpgradeManager = new UpgradeManager(this.mHostApplicationContext);
        StorageManager.initizlie(this.mHostApplicationContext, this.mAppInfo, isMultiProcess);
        this.mStorageManager = StorageManager.instance();
        this.mProviderBuilder = PluginProviderBuilder.initizlie(this.mHostApplicationContext, this.mAppInfo);
        deleteOldVerionFile();
    }

    private void deleteOldVerionFile() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "deleteOldFile<<()");
        }
        FileUtils.deleteFile(FileUtils.toFilePath(StorageManager.instance().getPluginFileRootPath(), "plugin"));
        File file = this.mHostApplicationContext.getDir(PluginPingbackParams.DELETE_DEX, 0);
        if (file != null) {
            FileUtils.deleteFile(file.getAbsolutePath());
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            FileUtils.deleteFile(FileUtils.toFilePath(StorageManager.instance().getPluginFileRootPathSd(), "plugin"));
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "deleteOldFile>>() return ");
        }
    }

    public AbsPluginProvider getProvider(String pluginId) {
        AbsPluginProvider find;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getProvider<<(pluginId=" + pluginId + ")");
        }
        synchronized (this.mProviders) {
            find = (AbsPluginProvider) this.mProviders.get(pluginId);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getProvider>>() return " + find);
        }
        return find;
    }

    private void addProviders(AbsPluginProvider provider) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "addProviders<<(provider=" + provider + ")");
        }
        if (provider != null) {
            synchronized (this.mProviders) {
                this.mProviders.put(provider.getId(), provider);
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "addProviders>>()");
        }
    }

    public List<AbsPluginProvider> getProviders() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getProviders<<()");
        }
        List<AbsPluginProvider> providers = new ArrayList();
        for (String pluginId : this.mAppInfo.getPluginTypes().keySet()) {
            AbsPluginProvider provider = getProvider(pluginId);
            if (provider != null) {
                providers.add(provider);
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getProviders>>() return " + providers);
        }
        return providers;
    }

    private Map<String, String> getProvidersVersionInfo() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getProvidersVersionInfo<<()");
        }
        Map<String, String> versionInfos = new HashMap();
        for (String pluginId : this.mAppInfo.getPluginTypes().keySet()) {
            AbsPluginProvider provider = getProvider(pluginId);
            if (provider != null && provider.canBeUpgrade()) {
                versionInfos.put(pluginId, provider.getVersionName());
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getProvidersVersionInfo>>() return " + versionInfos);
        }
        return versionInfos;
    }

    public Result<AbsPluginProvider> loadProvider(HostPluginInfo hPlugininfo) {
        int code = 1;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadProvider<<(hPlugininfo=" + hPlugininfo + ")");
        }
        List<LoadProviderException> exceptionList = new ArrayList();
        AbsPluginProvider provider = getProvider(hPlugininfo.getPluginId());
        deleteOldFile(hPlugininfo);
        int period = -1;
        if (provider == null) {
            period = 0;
            try {
                provider = loadLocalProvider(hPlugininfo);
            } catch (Throwable e) {
                exceptionList.add(new LoadProviderException(ERROR_TYPE.ERROR_LOAD_LOCAL, e));
            }
        }
        if (provider == null) {
            try {
                if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.PLAYER_PLUGIN_PERIOD_TWO_ASSET_FAIL)) {
                    throw new Exception("debug period two fail");
                }
                period = 1;
                provider = loadAssetsProvider(hPlugininfo, false);
            } catch (Throwable e1) {
                exceptionList.add(new LoadProviderException(ERROR_TYPE.ERROR_LOAD_ASSETS_SD, e1));
            }
        }
        if (provider == null) {
            try {
                if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.PLAYER_PLUGIN_PERIOD_FOUR_LOAD_DOWNLOAD)) {
                    throw new Exception("debug period four fail");
                }
                period = 2;
                provider = loadDownloadProvider(hPlugininfo);
            } catch (Throwable e2) {
                exceptionList.add(new LoadProviderException(ERROR_TYPE.ERROR_LOAD_DOWNLOAD, e2));
            }
        }
        if (provider != null) {
            addProviders(provider);
            this.mTimerHandler.startTimer(hPlugininfo);
        }
        if (provider == null) {
            code = 0;
        }
        Result<AbsPluginProvider> result = new Result(code, period, provider, exceptionList);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadProvider>>() return " + result);
        }
        return result;
    }

    private AbsPluginProvider loadLocalProvider(HostPluginInfo hPluginInfo) throws Throwable {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadLocalProvider<<(hPluginInfo=" + hPluginInfo + ")");
        }
        AbsPluginProvider provider = null;
        List<PluginInfo> list = this.mStorageManager.loadPluginInfos(hPluginInfo, true);
        List justOneList = new ArrayList();
        int i = 0;
        while (i < list.size()) {
            PluginInfo info = (PluginInfo) list.get(i);
            try {
                if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.PLAYER_PLUGIN_PERIOD_ONE_LOCAL_FAIL)) {
                    throw new Exception("debug period one fail");
                }
                provider = loadProvider(hPluginInfo, info);
                if (provider != null) {
                    if (Log.VERBOSE) {
                        Log.m434v(TAG, "add new one");
                    }
                    justOneList.add(info);
                    if (ListUtils.getCount(justOneList) <= 0) {
                        for (i = 0; i < list.size(); i++) {
                            info = (PluginInfo) list.get(i);
                            if (!Util.equals(info.getVersionName(), ((PluginInfo) justOneList.get(0)).getVersionName())) {
                                if (Log.VERBOSE) {
                                    Log.m434v(TAG, "remove old" + info);
                                }
                                this.mStorageManager.removePluginFiles(hPluginInfo, info);
                            }
                        }
                        if (ListUtils.getCount(justOneList) <= 0) {
                            if (provider == null) {
                                this.mStorageManager.savePluginInfos(hPluginInfo, justOneList);
                            } else {
                                if (Log.VERBOSE) {
                                    Log.m434v(TAG, "localHistory why error?, clean file");
                                }
                                this.mStorageManager.removePluginFiles(hPluginInfo, (PluginInfo) justOneList.get(0));
                            }
                        } else if (Log.VERBOSE) {
                            Log.m434v(TAG, "load local histroy error");
                        }
                        if (Log.VERBOSE) {
                            Log.m434v(TAG, "loadLocalProvider>>() return " + provider);
                        }
                        return provider;
                    }
                    if (Log.VERBOSE) {
                        Log.m434v(TAG, "load local histroy info null ");
                    }
                    throw new Exception("load local histroy info null");
                }
                i++;
            } catch (Throwable th) {
                this.mStorageManager.removePluginFiles(hPluginInfo, info);
            }
        }
        if (ListUtils.getCount(justOneList) <= 0) {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "load local histroy info null ");
            }
            throw new Exception("load local histroy info null");
        }
        for (i = 0; i < list.size(); i++) {
            info = (PluginInfo) list.get(i);
            if (!Util.equals(info.getVersionName(), ((PluginInfo) justOneList.get(0)).getVersionName())) {
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "remove old" + info);
                }
                this.mStorageManager.removePluginFiles(hPluginInfo, info);
            }
        }
        if (ListUtils.getCount(justOneList) <= 0) {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "load local histroy error");
            }
        } else if (provider == null) {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "localHistory why error?, clean file");
            }
            this.mStorageManager.removePluginFiles(hPluginInfo, (PluginInfo) justOneList.get(0));
        } else {
            this.mStorageManager.savePluginInfos(hPluginInfo, justOneList);
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadLocalProvider>>() return " + provider);
        }
        return provider;
    }

    private AbsPluginProvider loadAssetsProvider(HostPluginInfo hPluginInfo, boolean useSd) throws Throwable {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadAssetsProvider<<(hPluginInfo=" + hPluginInfo + ")");
        }
        PluginInfo info = this.mStorageManager.loadPluginInfoFromAssets(hPluginInfo, useSd);
        if (info != null) {
            AbsPluginProvider provider = loadProvider(hPluginInfo, info);
            if (provider != null) {
                this.mStorageManager.savePluginInfo(info, hPluginInfo);
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "loadAssetsProvider>>() return " + provider);
            }
            return provider;
        }
        throw new Exception("plugin info is null !?");
    }

    private AbsPluginProvider loadDownloadProvider(HostPluginInfo hPluginInfo) throws Throwable {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadDownloadProvider<<(hPluginInfo=" + hPluginInfo + ")");
        }
        AbsPluginProvider provider = null;
        UpgradeInfo upgradeInfo = this.mUpgradeManager.checkUpgrade(null, hPluginInfo);
        if (UpgradeInfo.isVaild(upgradeInfo)) {
            PluginInfo info = downloadPlugin(hPluginInfo, upgradeInfo);
            if (info != null) {
                provider = loadProvider(hPluginInfo, info);
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadDownloadProvider>>() return " + provider);
        }
        return provider;
    }

    private AbsPluginProvider loadProvider(HostPluginInfo hPluginInfo, PluginInfo info) throws Throwable {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadProvider<<(info=" + info + ")");
        }
        AbsPluginProvider provider = null;
        if (info != null) {
            if (PluginType.EMPTY_TYPE.equals((String) this.mAppInfo.getPluginTypes().get(info.getId()))) {
                provider = this.mProviderBuilder.createEmptyProvider(info);
            } else {
                provider = this.mProviderBuilder.createDefaultProvider(info);
            }
            if (PluginDebugUtils.needThrowable(DEBUG_PROPERTY.LAST_FAILED)) {
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "plugin load failed at last time!!(for debug!)");
                }
                provider = null;
            }
            if (provider != null) {
                provider.setOnExceptionListener(this.mOnExceptionListener);
            } else {
                this.mStorageManager.removePluginFiles(hPluginInfo, info);
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "loadProvider>>() return " + provider);
        }
        return provider;
    }

    private PluginInfo downloadPlugin(HostPluginInfo hPluginInfo, UpgradeInfo upgradeInfo) throws Throwable {
        String oldVersion;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "downloadPlugin>>() hPluginInfo" + hPluginInfo + "upgradeInfo" + upgradeInfo);
        }
        PluginInfo pluginInfo = null;
        String newVersion = upgradeInfo.getVersion();
        if (getProvider(hPluginInfo.getPluginId()) == null) {
            oldVersion = hPluginInfo.getPluginVersion();
        } else {
            oldVersion = getProvider(hPluginInfo.getPluginId()).getVersionName();
        }
        PluginInfo find = this.mStorageManager.loadPluginInfo(hPluginInfo, newVersion);
        if (find == null) {
            String FileName = hPluginInfo.getPluginId() + "-download-" + newVersion + "." + FileUtils.getExtensionName(FileUtils.getURLFileName(upgradeInfo.getUrl()));
            if (Log.VERBOSE) {
                Log.m434v(TAG, "downloadPlugin>>() FileName" + FileName);
            }
            find = this.mStorageManager.parsePluginInfo(hPluginInfo, newVersion, FileName);
            boolean success = this.mStorageManager.startDownload(DataUtils.covertToDownloadInfo(upgradeInfo, find.getPath()), find.getId());
            if (this.mDownloadListener != null) {
                PluginPingbackParams params = new PluginPingbackParams();
                params.add(PluginPingbackParams.PLUGINID, hPluginInfo.getPluginId()).add(PluginPingbackParams.DOWNLOAD_OLDVERSION, oldVersion).add(PluginPingbackParams.DOWNLOAD_NEWVERSION, newVersion).add("success", success ? "1" : "0");
                this.mDownloadListener.downloaded(params);
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "mDownloadListener send");
                }
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "loadProvider>>() sendCustomT11 upgrade ");
            }
            if (success) {
                if (this.mStorageManager.copySoLibToHost(find)) {
                    pluginInfo = find;
                } else {
                    this.mStorageManager.removePluginFiles(hPluginInfo, find);
                }
                if (pluginInfo != null) {
                    this.mStorageManager.savePluginInfo(pluginInfo, hPluginInfo);
                }
            }
        } else {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "downloadPlugin>> find!");
            }
            pluginInfo = find;
        }
        if (!PluginDebugUtils.needThrowable(DEBUG_PROPERTY.OVER_ERROR_COUNT)) {
            return pluginInfo;
        }
        throw new Exception("download count is Over MaxCount!! (maxCount=2)(for debug!)");
    }

    private void deleteOldFile(HostPluginInfo hPluginInfo) {
        StringBuilder oldNameList;
        String td;
        if (Log.VERBOSE) {
            Log.m434v(TAG, "deleteOldFile<<()" + hPluginInfo);
        }
        long nowTime = SystemClock.elapsedRealtime();
        String nowFileRootPath = StorageManager.instance().getPluginFileRootPath();
        String nowPluginRootPath = "";
        if (Util.isEmpty(hPluginInfo.getHostVersion())) {
            nowPluginRootPath = FileUtils.toFilePath(nowFileRootPath, hPluginInfo.getPluginId(), PluginManager.DEFAULT_PLUGIN_VERSION);
        } else {
            nowPluginRootPath = FileUtils.toFilePath(nowFileRootPath, hPluginInfo.getPluginId(), hPluginInfo.getHostVersion());
        }
        List<String> deleteName = FileUtils.deleteExportFile(nowPluginRootPath);
        if (!ListUtils.isEmpty((List) deleteName)) {
            if (Log.VERBOSE) {
                Log.m434v(TAG, "Has old file , delete app_dex");
            }
            boolean deleteOldDex = false;
            if (VERSION.SDK_INT >= 21) {
                deleteOldDex = true;
            } else if (hPluginInfo != null) {
                File file = this.mHostApplicationContext.getDir(hPluginInfo.getPluginId(), 0);
                if (file != null) {
                    deleteOldDex = FileUtils.deleteFile(file.getAbsolutePath());
                }
            }
            oldNameList = new StringBuilder();
            for (String old : deleteName) {
                oldNameList.append(old).append(",");
            }
            td = String.valueOf(SystemClock.elapsedRealtime() - nowTime);
            if (this.mDeleteListener != null) {
                PluginPingbackParams params = new PluginPingbackParams();
                params.add(PluginPingbackParams.PLUGINID, hPluginInfo.getPluginId()).add(PluginPingbackParams.DELETE_DEX, deleteOldDex ? "1" : "0").add(PluginPingbackParams.DELETE_OLDCOUNT, String.valueOf(0)).add(PluginPingbackParams.DELETE_OLDNAME_LIST, oldNameList.toString()).add("td", td);
                this.mDeleteListener.deletedOldFile(params);
                if (Log.VERBOSE) {
                    Log.m434v(TAG, "mDeleteListener send");
                }
            }
        }
        if ("mounted".equals(Environment.getExternalStorageState())) {
            String nowFileRootPathSd = StorageManager.instance().getPluginFileRootPathSd();
            String nowPluginRootPathSd = "";
            if (Util.isEmpty(hPluginInfo.getHostVersion())) {
                nowPluginRootPathSd = FileUtils.toFilePath(nowFileRootPathSd, hPluginInfo.getPluginId(), PluginManager.DEFAULT_PLUGIN_VERSION);
            } else {
                nowPluginRootPathSd = FileUtils.toFilePath(nowFileRootPathSd, hPluginInfo.getPluginId(), hPluginInfo.getHostVersion());
            }
            if (!ListUtils.isEmpty(FileUtils.deleteExportFile(nowPluginRootPathSd))) {
                oldNameList = new StringBuilder();
                for (String old2 : deleteName) {
                    oldNameList.append(old2).append(",");
                }
                td = String.valueOf(SystemClock.elapsedRealtime() - nowTime);
                if (this.mDeleteListener != null) {
                    params = new PluginPingbackParams();
                    params.add(PluginPingbackParams.PLUGINID, hPluginInfo.getPluginId()).add(PluginPingbackParams.DELETE_DEX, "-1").add(PluginPingbackParams.DELETE_OLDCOUNT, String.valueOf(0)).add(PluginPingbackParams.DELETE_OLDNAME_LIST, oldNameList.toString()).add("td", td);
                    this.mDeleteListener.deletedOldFile(params);
                    if (Log.VERBOSE) {
                        Log.m434v(TAG, "mDeleteListener send");
                    }
                }
            }
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "deleteOldFile>>() return ");
        }
    }

    public void setDownloadListener(IPluginDownloadListener listener) {
        this.mDownloadListener = listener;
    }

    public void setDeleteListener(IPluginDeleteListener listener) {
        this.mDeleteListener = listener;
    }

    public void setProperty(String pluginId, String propertyKey, Object propertyValue) {
        synchronized (this.mPropertys) {
            PluginProperty property = (PluginProperty) this.mPropertys.get(pluginId);
            if (property == null) {
                property = new PluginProperty();
                this.mPropertys.put(pluginId, property);
            }
            property.putProperty(propertyKey, propertyValue);
        }
    }

    public Map<String, PluginProperty> getPropertys() {
        Throwable th;
        synchronized (this.mPropertys) {
            try {
                Map<String, PluginProperty> propertys = new HashMap(this.mPropertys);
                try {
                    return propertys;
                } catch (Throwable th2) {
                    th = th2;
                    Map<String, PluginProperty> map = propertys;
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }
}
