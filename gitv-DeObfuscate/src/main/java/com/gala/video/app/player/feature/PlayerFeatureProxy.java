package com.gala.video.app.player.feature;

import android.content.Context;
import com.gala.sdk.player.IPlayerFeature;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.Wrapper;
import com.gala.video.lib.share.project.Project;

public class PlayerFeatureProxy extends Wrapper {
    private static final String TAG = "PlayerFeatureManager";
    private static PlayerFeatureProxy instance;
    private IPlayerFeatureProxy mPlayerProvider;

    private PlayerFeatureProxy() {
    }

    public static IPlayerFeatureProxy getInstance() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getInstance(), instance=" + instance);
        }
        if (instance == null) {
            instance = new PlayerFeatureProxy();
        }
        return instance;
    }

    public void initailize(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initailize()");
        }
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            this.mPlayerProvider = RemotePlayerFeatureProvider.getInstance();
        } else {
            this.mPlayerProvider = LocalPlayerFeatureProvider.getInstance();
        }
        this.mPlayerProvider.initailize(context);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "feature=" + this.mPlayerProvider);
        }
    }

    public void loadPlayerFeatureAsync(Context context, OnStateChangedListener listener, boolean showLoading) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "(),mPlayerProvider=" + this.mPlayerProvider + ",listener=" + listener + ",context=" + context + "showLoading" + showLoading);
        }
        if (this.mPlayerProvider != null) {
            this.mPlayerProvider.loadPlayerFeatureAsync(context, listener, showLoading);
        } else {
            listener.onFailed();
        }
    }

    public String getLog(int logType) {
        if (this.mPlayerProvider != null) {
            return this.mPlayerProvider.getLog(logType);
        }
        return null;
    }

    public void updateDeviceCheckInfo(String apiKey, String authId) {
        if (this.mPlayerProvider != null) {
            this.mPlayerProvider.updateDeviceCheckInfo(apiKey, authId);
        }
    }

    public void updateAuthorization(String authorization) {
        if (this.mPlayerProvider != null) {
            this.mPlayerProvider.updateAuthorization(authorization);
        }
    }

    public void enableHCDNPreDeploy(boolean enable) {
        if (this.mPlayerProvider != null) {
            this.mPlayerProvider.enableHCDNPreDeploy(enable);
        }
    }

    public void setHCDNCleanAvailable(boolean available) {
        if (this.mPlayerProvider != null) {
            this.mPlayerProvider.setHCDNCleanAvailable(available);
        }
    }

    public String getPlayerModulesVersion() {
        if (this.mPlayerProvider != null) {
            this.mPlayerProvider.getPlayerModulesVersion();
        }
        return "";
    }

    public boolean isPlayerAlready() {
        if (this.mPlayerProvider != null) {
            return this.mPlayerProvider.isPlayerAlready();
        }
        return false;
    }

    public IPlayerFeature getPlayerFeature() {
        if (this.mPlayerProvider != null) {
            return this.mPlayerProvider.getPlayerFeature();
        }
        return null;
    }

    public IPlayerFeature getPlayerFeatureOnlyInitJava() {
        if (this.mPlayerProvider != null) {
            return this.mPlayerProvider.getPlayerFeatureOnlyInitJava();
        }
        return null;
    }
}
