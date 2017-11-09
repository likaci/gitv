package com.gala.sdk.plugin.server;

import android.content.Context;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.server.core.PluginHelper;
import com.gala.sdk.plugin.server.core.PluginPropertyConfig;
import com.gala.sdk.plugin.server.pingback.IPluginDeleteListener;
import com.gala.sdk.plugin.server.pingback.IPluginDownloadListener;
import com.gala.sdk.plugin.server.utils.CpuLoadBalance;
import com.gala.sdk.plugin.server.utils.FileUtils;
import java.util.List;

public class PluginManager {
    public static final int CODE_SUCCESS = 0;
    public static final int CODE_UNKNOWN = 1;
    public static final int CODE_VERSION_NOT_MATCH = 2;
    public static final String DEFAULT_PLUGIN_VERSION = "0.0.0.0";
    private static final String TAG = "PluginManager";
    private static PluginManager sInstance;
    private final PluginHelper mHelper;
    private Context mHostApplicationContext = null;

    public static synchronized void initizlie(Context context, AppInfo info) {
        synchronized (PluginManager.class) {
            if (sInstance == null) {
                sInstance = new PluginManager(context, info, false);
                CpuLoadBalance.getInstance();
            }
        }
    }

    public static synchronized void initizlie(Context context, AppInfo info, boolean isMultiProcess) {
        synchronized (PluginManager.class) {
            if (sInstance == null) {
                sInstance = new PluginManager(context, info, isMultiProcess);
            }
        }
    }

    public static synchronized void release() {
        synchronized (PluginManager.class) {
            sInstance = null;
        }
    }

    public static PluginManager instance() {
        return sInstance;
    }

    private PluginManager(Context hostContext, AppInfo info, boolean isMultiProcess) {
        this.mHostApplicationContext = hostContext.getApplicationContext();
        this.mHelper = new PluginHelper(this.mHostApplicationContext, new AppInfo(info), isMultiProcess);
    }

    public AbsPluginProvider getProvider(HostPluginInfo hPluginInfo) {
        if (Log.DEBUG) {
            Log.d(TAG, "getProvider<<(hPluginInfo=" + hPluginInfo + ")");
        }
        AbsPluginProvider provider = this.mHelper.getProvider(hPluginInfo.getPluginId());
        if (Log.DEBUG) {
            Log.d(TAG, "getProvider>>() return " + provider);
        }
        return provider;
    }

    public synchronized Result<AbsPluginProvider> loadProvider(HostPluginInfo hPluginInfo) {
        Result<AbsPluginProvider> result;
        if (Log.DEBUG) {
            Log.d(TAG, "loadProvider<<(hPluginInfo=" + hPluginInfo + ")");
        }
        result = this.mHelper.loadProvider(hPluginInfo);
        result.setDelayTime(FileUtils.sDelaySumTime);
        if (Log.DEBUG) {
            Log.d(TAG, "loadProvider>>() return " + result);
        }
        return result;
    }

    public List<AbsPluginProvider> getProviders() {
        if (Log.DEBUG) {
            Log.d(TAG, "getProviders<<()");
        }
        List<AbsPluginProvider> providers = this.mHelper.getProviders();
        if (Log.DEBUG) {
            Log.d(TAG, "getProviders>>() return " + providers);
        }
        return providers;
    }

    public void setUpgradeInterval(long interval) {
        PluginPropertyConfig.setUpgradeInterval(this.mHelper, interval);
    }

    public long getUpgradeInterval() {
        return PluginPropertyConfig.getUpgradeInterval(this.mHelper);
    }

    public void setAutoUpgrade(boolean autoUpgrade) {
        PluginPropertyConfig.setAutoUpgrade(this.mHelper, autoUpgrade);
    }

    public boolean autoUpgrade(boolean defaultValue) {
        return PluginPropertyConfig.autoUpgrade(this.mHelper, defaultValue);
    }

    public void setDownloadListener(IPluginDownloadListener listener) {
        this.mHelper.setDownloadListener(listener);
    }

    public void setDeleteListener(IPluginDeleteListener listener) {
        this.mHelper.setDeleteListener(listener);
    }
}
