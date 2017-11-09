package com.gala.video.app.player.feature;

import android.content.Context;
import com.gala.sdk.player.IPlayerFeature;
import com.gala.video.app.player.feature.PlayerCommand.IConnectListener;
import com.gala.video.app.player.feature.PlayerCommand.ServiceConnectState;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.Wrapper;

public class RemotePlayerFeatureProvider extends Wrapper {
    private static final String TAG = "PlayerMultiProcessClient";
    private static RemotePlayerFeatureProvider sInstance;
    private IConnectListener mConnectListener;
    private Context mContext;

    private RemotePlayerFeatureProvider() {
    }

    public static synchronized RemotePlayerFeatureProvider getInstance() {
        RemotePlayerFeatureProvider remotePlayerFeatureProvider;
        synchronized (RemotePlayerFeatureProvider.class) {
            if (sInstance == null) {
                sInstance = new RemotePlayerFeatureProvider();
            }
            remotePlayerFeatureProvider = sInstance;
        }
        return remotePlayerFeatureProvider;
    }

    public void initailize(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "initailize()");
        }
        this.mContext = context;
        PlayerMultiProcessClient.getInstance().initialize(context);
        PlayerMultiProcessClient.getInstance().connect();
    }

    public String getLog(int logType) {
        return PlayerMultiProcessClient.getInstance().getLog(logType);
    }

    public void updateDeviceCheckInfo(String apiKey, String authId) {
        PlayerMultiProcessClient.getInstance().updateDeviceCheckInfo(apiKey, authId);
    }

    public void updateAuthorization(String authorization) {
        PlayerMultiProcessClient.getInstance().updateAuthorization(authorization);
    }

    public void loadPlayerFeatureAsync(Context context, OnStateChangedListener listener, final boolean showLoading) {
        final OnStateChangedListener mListener = listener;
        final Context mContext = context;
        ServiceConnectState mCurrentState = PlayerMultiProcessClient.getInstance().getCurrentState();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadPlayerFeatureAsync(), mCurrentState=" + mCurrentState);
        }
        this.mConnectListener = new IConnectListener() {
            public void onStateChange(ServiceConnectState state) {
                switch (state) {
                    case CONNECTED:
                        PlayerMultiProcessClient.getInstance().loadPlayerFeatureAsync(mContext, mListener, showLoading);
                        return;
                    default:
                        return;
                }
            }
        };
        switch (mCurrentState) {
            case CONNECTED:
                PlayerMultiProcessClient.getInstance().loadPlayerFeatureAsync(context, listener, showLoading);
                return;
            case IDLE:
                PlayerMultiProcessClient.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext());
                break;
            case INITIALIZED:
                break;
            default:
                return;
        }
        PlayerMultiProcessClient.getInstance().setConnectListener(this.mConnectListener);
        PlayerMultiProcessClient.getInstance().connect();
    }

    public String toString() {
        return "RemotePlayerFeatureProvider@" + hashCode();
    }

    public void enableHCDNPreDeploy(boolean enable) {
        PlayerMultiProcessClient.getInstance().enableHCDNPreDeploy(enable);
    }

    public void setHCDNCleanAvailable(boolean available) {
        PlayerMultiProcessClient.getInstance().setHCDNCleanAvailable(available);
    }

    public String getPlayerModulesVersion() {
        return PlayerMultiProcessClient.getInstance().getPlayerModulesVersion();
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
