package com.gala.video.lib.share.ifimpl.web.provider;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.LoadProviderException;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.ResultCode.ERROR_TYPE;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.sdk.plugin.server.pingback.PluginPingbackParams;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.crosswalkinterface.IXWalkPlugin;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.PluginConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.web.IWebPluginProvider;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import java.util.HashMap;
import java.util.Map;

public class WebPluginProvider implements IWebPluginProvider {
    private static final String DEFAULT_VERSION = "0.0.0.74";
    private static final String LOAD_PLUGIN_THREAD = "load-webplugin";
    private static final int MSG_LOAD_NODIALOG = 1;
    private static final int PARAM_DELAYED_TIME = 10000;
    public static final String TAG = "EPG/web/WebPluginProvider";
    private static WebPluginProvider mInstance;
    private HostPluginInfo mHostPluginInfo;
    private IXWalkPlugin mIXWalkPluginFeature;
    private PluginManager mPluginManager;

    private class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            WebPluginProvider.this.onLoadWebFeature();
        }
    }

    public static synchronized IWebPluginProvider getInstance() {
        IWebPluginProvider iWebPluginProvider;
        synchronized (WebPluginProvider.class) {
            if (mInstance == null) {
                mInstance = new WebPluginProvider();
            }
            iWebPluginProvider = mInstance;
        }
        return iWebPluginProvider;
    }

    private WebPluginProvider() {
        this.mHostPluginInfo = null;
        this.mHostPluginInfo = new HostPluginInfo(PluginConstants.CROSSWALKPLUGIN_ID, Project.getInstance().getBuild().getVersionString());
        this.mHostPluginInfo.setPluginVersion(DEFAULT_VERSION);
    }

    public void load() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> start init!!");
        }
        this.mPluginManager = PluginManager.instance();
        HandlerThread loadPluginThread = new HandlerThread(LOAD_PLUGIN_THREAD);
        loadPluginThread.start();
        new MyHandler(loadPluginThread.getLooper()).sendEmptyMessageDelayed(1, 10000);
    }

    public synchronized IXWalkPlugin getIXWalkPlugin() {
        if (this.mIXWalkPluginFeature == null) {
            LogUtils.m1568d(TAG, ">> getIXWalkPlugin: IXWalkPlugin=" + this.mIXWalkPluginFeature);
        }
        return this.mIXWalkPluginFeature;
    }

    private void onLoadWebFeature() {
        LogUtils.m1568d(TAG, ">> onLoadWebFeature() start: mIXWalkPluginFeature=" + this.mIXWalkPluginFeature);
        if (this.mIXWalkPluginFeature == null) {
            IXWalkPlugin feature = null;
            try {
                long start = SystemClock.uptimeMillis();
                Result<AbsPluginProvider> result = this.mPluginManager.loadProvider(this.mHostPluginInfo);
                if (!(result == null || result.getData() == null)) {
                    LogUtils.m1568d(TAG, ">> onLoadWebFeature() -> result is not null!");
                    feature = (IXWalkPlugin) ((AbsPluginProvider) result.getData()).getFeature(1);
                }
                long costTime = SystemClock.uptimeMillis() - start;
                if (feature == null) {
                    LogUtils.m1571e(TAG, "CrossWalk Plugin feature is null!");
                } else if (!feature.isPluginReady()) {
                    LogUtils.m1571e(TAG, ">> onLoadWebFeature() ->  copy file is not Completed !");
                    feature = null;
                }
                this.mIXWalkPluginFeature = feature;
                sendLoadPluginPingback(result, PluginConstants.CROSSWALKPLUGIN_ID, costTime);
            } catch (Exception e) {
                LogUtils.m1573e(TAG, ">> onLoadWebFeature() -> fail!", e.getMessage());
            }
            LogUtils.m1568d(TAG, ">> onLoadWebFeature() end: mIXWalkPluginFeature=" + this.mIXWalkPluginFeature);
        }
    }

    public boolean isAlready() {
        return this.mIXWalkPluginFeature != null;
    }

    private void sendLoadPluginPingback(Result<AbsPluginProvider> result, String pluginId, long td) {
        LogUtils.m1568d(TAG, "pluginId = " + pluginId + ", loadCrossWalkFeature cost time = " + td);
        Map<String, String> pingbackInfos = new HashMap();
        String enableCrosswalk = isAlready() ? "1" : "0";
        String st = String.valueOf(result.getCode());
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_ASSETS, "");
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_DOWNLOAD, "");
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_LOCAL, "");
        for (LoadProviderException exception : result.getExceptions()) {
            pingbackInfos.put(exception.getType(), getMsgFromThrowable(exception.getThrowable()));
        }
        String mSdkVersion = "";
        if (!(this.mPluginManager == null || this.mPluginManager.getProvider(this.mHostPluginInfo) == null)) {
            mSdkVersion = this.mPluginManager.getProvider(this.mHostPluginInfo).getVersionName();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "sendLoadPluginPingback pluginManager:" + this.mPluginManager + ", provider=" + (this.mPluginManager != null ? this.mPluginManager.getProvider(this.mHostPluginInfo) : ""));
        }
        pingbackInfos.put("enableCrosswalk", enableCrosswalk);
        pingbackInfos.put("td", String.valueOf(td));
        pingbackInfos.put("st", st);
        pingbackInfos.put("sdkv", mSdkVersion);
        pingbackInfos.put(PluginPingbackParams.PLUGINID, pluginId);
        pingbackInfos.put("ct", "160225_pluginload");
        pingbackInfos.put(Keys.f2035T, "11");
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "sendLoadPluginPingback: info=[" + pingbackInfos + AlbumEnterFactory.SIGN_STR);
        }
        PingBack.getInstance().postPingBackToLongYuan(pingbackInfos);
    }

    private String getMsgFromThrowable(Throwable throwable) {
        return throwable != null ? throwable.toString() : "";
    }
}
