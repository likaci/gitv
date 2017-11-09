package com.gala.video.app.player.feature;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.gala.sdk.player.IPlayerFeature;
import com.gala.video.app.player.feature.IPlayerMultiProcessBinder.Stub;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;

public class PlayerMultiProcessService extends Service {
    private static final String TAG = "PlayerMultiProcessService";
    private static PlayerMultiProcessBinder sBinder;
    private IPlayerFeature mPlayerFeature;

    private class PlayerMultiProcessBinder extends Stub {
        public PlayerMultiProcessBinder() {
            PlayerMultiProcessService.this.initialize();
        }

        public Bundle invoke(Bundle params) throws RemoteException {
            Bundle result = new Bundle();
            String cmd = params.getString(PlayerCommand.CMD);
            if (LogUtils.mIsDebug) {
                LogUtils.d(PlayerMultiProcessService.TAG, "invoke, cmd=" + cmd);
            }
            if (PlayerCommand.LOAD_PLUGIN_ASYNC.equals(cmd)) {
                PlayerMultiProcessService.this.mPlayerFeature = PlayerFeatureProvider.instance().getPlayerFeature();
                if (PlayerMultiProcessService.this.mPlayerFeature != null) {
                    result.putBoolean(PlayerCommand.PARCELABLE_RESULT, true);
                } else {
                    result.putBoolean(PlayerCommand.PARCELABLE_RESULT, false);
                    result.putInt(PlayerCommand.LOAD_FAILED_COUNT, PlayerFeatureProvider.instance().getFailCount());
                }
            } else if (PlayerCommand.IS_SUPPORT_DOLBY.equals(cmd)) {
                result.putBoolean(PlayerCommand.PARCELABLE_RESULT, PlayerMultiProcessService.this.mPlayerFeature.isSupportDolby());
            } else if (PlayerCommand.IS_SUPPORT_H211.equals(cmd)) {
                result.putBoolean(PlayerCommand.PARCELABLE_RESULT, PlayerMultiProcessService.this.mPlayerFeature.isSupportH211());
            } else if (PlayerCommand.GET_PUMA_LOG.equals(cmd)) {
                String log = PlayerMultiProcessService.this.mPlayerFeature.getPlayerLogProviderFactory().createPlayerLogProvider().getLog(params.getInt(PlayerCommand.PLAYER_TYPE));
                if (LogUtils.mIsDebug) {
                    LogUtils.d(PlayerMultiProcessService.TAG, "getPumaLog():\n" + log);
                }
                result.putString(PlayerCommand.PARCELABLE_RESULT, log);
            } else if (PlayerCommand.ENABLE_HCDN_PRE_DEPLOY.equals(cmd)) {
                enable = params.getBoolean(PlayerCommand.EXTRA);
                if (PlayerMultiProcessService.this.mPlayerFeature != null) {
                    PlayerMultiProcessService.this.mPlayerFeature.enableHCDNPreDeploy(enable);
                }
            } else if (PlayerCommand.ENABLE_HCDN_CLEAN_AVAILABLE.equals(cmd)) {
                enable = params.getBoolean(PlayerCommand.EXTRA);
                if (PlayerMultiProcessService.this.mPlayerFeature != null) {
                    PlayerMultiProcessService.this.mPlayerFeature.setHCDNCleanAvailable(enable);
                }
            } else if (PlayerCommand.CMD_UPDATE_DEVICE_CHECK.equals(cmd)) {
                PlayerMultiProcessService.this.mPlayerFeature.updateDeviceCheckInfo(params.getString("APIKEY"), params.getString("AUTHID"));
            } else if (PlayerCommand.GET_PLAYER_MODULE_VERSION.equals(cmd)) {
                String versionStr = PlayerMultiProcessService.this.mPlayerFeature.getPlayerModulesVersion();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(PlayerMultiProcessService.TAG, "getModuleVersion():" + versionStr);
                }
                result.putString(PlayerCommand.PARCELABLE_RESULT, versionStr);
            } else if (PlayerCommand.CMD_UPDATE_AUTHORIZATION.equals(cmd)) {
                PlayerMultiProcessService.this.mPlayerFeature.updateAuthorization(params.getString(PlayerCommand.KEY_AUTHORIZATION));
            }
            return result;
        }
    }

    public void onCreate() {
        super.onCreate();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onStartCommand start");
        }
        return super.onStartCommand(intent, 3, startId);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onStartCommand onBind");
        }
        initBinder(intent);
        return sBinder;
    }

    private void initBinder(Intent intent) {
        if (sBinder == null) {
            sBinder = new PlayerMultiProcessBinder();
        }
    }

    private void initialize() {
        PlayerFeatureProvider.instance().initialize(getApplicationContext());
    }

    private void testDealyLoadFeature() {
        int testDelaySeconds = PlayerDebugUtils.getTestPlayerPluginLoadDelayTime();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> testLoadPluginDelay: testDelayMs=" + testDelaySeconds);
        }
        if (testDelaySeconds > 0) {
            try {
                Thread.sleep((long) (testDelaySeconds * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< testLoadPluginDelay");
        }
    }

    private void loadPlayerFeature() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> loadPluginPlayerFeature: mPlayerFeature=" + this.mPlayerFeature);
        }
        testDealyLoadFeature();
        if (this.mPlayerFeature == null) {
            try {
                this.mPlayerFeature = PlayerFeatureProvider.instance().getPlayerFeature();
            } catch (Exception e) {
                LogUtils.e(TAG, "loadPluginPlayerFeature() fail!", e);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "<< loadPluginPlayerFeature: mPlayerFeature=" + this.mPlayerFeature);
            }
        }
    }
}
