package com.gala.video.app.player.feature.drm;

import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.LoadProviderException;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.PluginConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;

public class IntertrustDrmPluginProvider {
    private static final String DEFAULT_VERSION = "0.710.0.0";
    private static final int INTERVAL = 30000;
    private static final int MAX_ACCESS_COUNT = 3;
    private static final int MAX_DURATION = 3600000;
    private static final String TAG = "IntertrustDrmPluginProvider";
    private static AccessToken mAccessToken = new AccessToken(3600000, 3, 30000);
    private static HostPluginInfo mHPluginInfo = null;
    private static String sModulePath = "";

    public static String getIntertrustDrmModulePath() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> getIntertrustDrmModulePath");
        }
        if (mAccessToken.isAllowedAccess()) {
            mAccessToken.increaseAccessCount();
            if (PlayerDebugUtils.testIntertrustDrmPluginFail()) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "<< getIntertrustDrmModulePath: failed, debug load intertrust drm plugin failed.");
                }
                return "";
            }
            mHPluginInfo = new HostPluginInfo(PluginConstants.INTERTRUST_DRM_PLUGIN_ID, Project.getInstance().getBuild().getVersionString());
            mHPluginInfo.setPluginVersion(DEFAULT_VERSION);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, ">> IntertrustDrmPluginProvider " + mHPluginInfo);
            }
            AbsPluginProvider provider = PluginManager.instance().getProvider(mHPluginInfo);
            if (provider == null) {
                Result<AbsPluginProvider> result = PluginManager.instance().loadProvider(mHPluginInfo);
                if (result != null) {
                    if (result.getExceptions().size() > 0) {
                        Throwable t = ((LoadProviderException) result.getExceptions().get(0)).getThrowable();
                        if (t != null) {
                            t.printStackTrace();
                        }
                    }
                    if (result.getData() != null) {
                        sModulePath = ((AbsPluginProvider) result.getData()).getPluginFilePath();
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(TAG, "getIntertrustDrmModulePath: success, libPath=" + sModulePath);
                        }
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getIntertrustDrmModulePath: failed, result.getData() is null");
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.e(TAG, "getIntertrustDrmModulePath: failed, result is null");
                }
            } else {
                sModulePath = provider.getPluginFilePath();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "<< getIntertrustDrmModulePath: return " + sModulePath);
            }
            return sModulePath;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< getIntertrustDrmModulePath: failed, not allowed access.");
        }
        return "";
    }

    public static boolean isPluginLoadSuccess() {
        return !StringUtils.isEmpty(sModulePath);
    }
}
