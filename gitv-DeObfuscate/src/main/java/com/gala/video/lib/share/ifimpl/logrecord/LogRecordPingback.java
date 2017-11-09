package com.gala.video.lib.share.ifimpl.logrecord;

import android.util.Log;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.LoadProviderException;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.ResultCode.ERROR_TYPE;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.sdk.plugin.server.pingback.PluginPingbackParams;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.PluginConstants;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import java.util.HashMap;
import java.util.Map;

class LogRecordPingback {
    private static final String TAG = "LogRecordPingback";
    private HostPluginInfo mHostPluginInfo = null;
    private int mIsFirstLoad;
    private PluginManager mPluginManager;

    public LogRecordPingback(PluginManager pluginManager, int isFirstLoad) {
        this.mPluginManager = pluginManager;
        this.mIsFirstLoad = isFirstLoad;
        this.mHostPluginInfo = new HostPluginInfo(PluginConstants.LOGRECORDPLUGIN_ID, Project.getInstance().getBuild().getVersionString());
    }

    public void sendLoadPluginPingback(Result<AbsPluginProvider> result, String pluginId, long td) {
        Log.v(TAG, "pluginId = " + pluginId + ", loadLogRecordFeature cost time = " + td);
        Map<String, String> pingbackInfos = new HashMap();
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
        pingbackInfos.put(Keys.FIRSTLOAD, String.valueOf(this.mIsFirstLoad));
        pingbackInfos.put("td", String.valueOf(td));
        pingbackInfos.put("st", st);
        pingbackInfos.put("sdkv", mSdkVersion);
        pingbackInfos.put(PluginPingbackParams.PLUGINID, pluginId);
        pingbackInfos.put("ct", "160225_pluginload");
        pingbackInfos.put(Keys.f2035T, "11");
        Log.d(TAG, "sendLoadPluginPingback: info=[" + pingbackInfos + AlbumEnterFactory.SIGN_STR);
        PingBack.getInstance().postPingBackToLongYuan(pingbackInfos);
    }

    private String getMsgFromThrowable(Throwable throwable) {
        return throwable != null ? throwable.toString() : "";
    }
}
