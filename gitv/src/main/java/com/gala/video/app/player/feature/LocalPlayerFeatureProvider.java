package com.gala.video.app.player.feature;

import android.content.Context;
import com.gala.sdk.player.IPlayerFeature;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.Wrapper;

public class LocalPlayerFeatureProvider extends Wrapper {
    private static String TAG = "LocalPlayerFeatureProvider";
    private static LocalPlayerFeatureProvider sInstance;
    private Context mContext;

    private LocalPlayerFeatureProvider() {
    }

    public static synchronized LocalPlayerFeatureProvider getInstance() {
        LocalPlayerFeatureProvider localPlayerFeatureProvider;
        synchronized (LocalPlayerFeatureProvider.class) {
            if (sInstance == null) {
                sInstance = new LocalPlayerFeatureProvider();
            }
            localPlayerFeatureProvider = sInstance;
        }
        return localPlayerFeatureProvider;
    }

    public void initailize(Context context) {
        this.mContext = context;
        PlayerFeatureProvider.instance().initialize(this.mContext);
    }

    public void loadPlayerFeatureAsync(Context context, OnStateChangedListener listener, boolean showLoading) {
        PlayerFeatureProvider.instance().loadPlayerPluginAsync(context, listener, showLoading);
    }

    public String getLog(int logType) {
        return PlayerFeatureProvider.instance().getPlayerFeature().getPlayerLogProviderFactory().createPlayerLogProvider().getLog(logType);
    }

    public void updateDeviceCheckInfo(String apiKey, String authId) {
        PlayerFeatureProvider.instance().getPlayerFeature().updateDeviceCheckInfo(apiKey, authId);
    }

    public void updateAuthorization(String authorization) {
        PlayerFeatureProvider.instance().getPlayerFeature().updateAuthorization(authorization);
    }

    public String toString() {
        return "LocalPlayerFeatureProvider@" + hashCode();
    }

    public void enableHCDNPreDeploy(boolean enable) {
        if (PlayerFeatureProvider.instance().isPlayerAlready()) {
            PlayerFeatureProvider.instance().getPlayerFeature().enableHCDNPreDeploy(enable);
        }
    }

    public void setHCDNCleanAvailable(boolean available) {
        if (PlayerFeatureProvider.instance().isPlayerAlready()) {
            PlayerFeatureProvider.instance().getPlayerFeature().setHCDNCleanAvailable(available);
        }
    }

    public String getPlayerModulesVersion() {
        return PlayerFeatureProvider.instance().getPlayerFeature().getPlayerModulesVersion();
    }

    public boolean isPlayerAlready() {
        return PlayerFeatureProvider.instance().isPlayerAlready();
    }

    public IPlayerFeature getPlayerFeature() {
        return PlayerFeatureProvider.instance().getPlayerFeature();
    }

    public IPlayerFeature getPlayerFeatureOnlyInitJava() {
        return PlayerFeatureProvider.instance().getPlayerFeatureOnlyInitJava();
    }
}
