package com.gala.video.app.player.feature;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.gala.video.app.player.feature.IPlayerMultiProcessBinder.Stub;
import com.gala.video.app.player.feature.PlayerCommand.IConnectListener;
import com.gala.video.app.player.feature.PlayerCommand.ServiceConnectState;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.IntentUtils;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerMultiProcessClient {
    private static final String LOAD_PLUGIN_THREAD = "load-playerplugin";
    private static final String SERVICE_ACTION_NAME = "com.gala.video.PlayerMultiProcessService";
    private static final String TAG = "PlayerMultiProcessClient";
    private static PlayerMultiProcessClient mInstance;
    private IConnectListener mConnectListener;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(PlayerMultiProcessClient.TAG, "disconnect ");
            }
            PlayerMultiProcessClient.this.mCurrentState = ServiceConnectState.IDLE;
            if (PlayerMultiProcessClient.this.mConnectListener != null) {
                PlayerMultiProcessClient.this.mConnectListener.onStateChange(ServiceConnectState.IDLE);
            }
            PlayerMultiProcessClient.this.mLoadProvderResult = false;
            PlayerMultiProcessClient.this.mPlayerMultiProcessBinder = null;
            PlayerMultiProcessClient.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext());
            PlayerMultiProcessClient.getInstance().connect();
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(PlayerMultiProcessClient.TAG, "connect success");
            }
            PlayerMultiProcessClient.this.mPlayerMultiProcessBinder = Stub.asInterface(service);
            PlayerMultiProcessClient.this.mCurrentState = ServiceConnectState.CONNECTED;
            if (PlayerMultiProcessClient.this.mConnectListener != null) {
                PlayerMultiProcessClient.this.mConnectListener.onStateChange(ServiceConnectState.CONNECTED);
            }
        }
    };
    private Context mContext;
    private ServiceConnectState mCurrentState = ServiceConnectState.IDLE;
    private AtomicInteger mFailCount = new AtomicInteger(0);
    private boolean mLoadProvderResult;
    private IPlayerMultiProcessBinder mPlayerMultiProcessBinder;
    private WorkHandler mWorkHandler;

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            OnStateChangedListener listener = null;
            if (msg.obj != null && (msg.obj instanceof OnStateChangedListener)) {
                listener = msg.obj;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(PlayerMultiProcessClient.TAG, ">> handleMessage(), listener=" + listener);
            }
            if (PlayerMultiProcessClient.this.loadPlayerFeature()) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(PlayerMultiProcessClient.TAG, ">> handleMessage() onSuccess!!");
                }
                if (listener != null) {
                    PlayerMultiProcessClient.this.mLoadProvderResult = true;
                    listener.onSuccess();
                    SystemConfigPreference.setPlayerCoreSupportDolby(PlayerMultiProcessClient.this.mContext, PlayerMultiProcessClient.this.isSupportDolby());
                    SystemConfigPreference.setPlayerCoreSupportH265(PlayerMultiProcessClient.this.mContext, PlayerMultiProcessClient.this.isSupportH211());
                    return;
                }
                return;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(PlayerMultiProcessClient.TAG, ">> handleMessage() onFailed!!");
            }
            if (listener != null) {
                listener.onFailed();
            }
        }
    }

    private PlayerMultiProcessClient() {
    }

    public synchronized void setConnectListener(IConnectListener listener) {
        this.mConnectListener = listener;
    }

    public synchronized void initialize(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> initialize");
        }
        this.mContext = context;
        HandlerThread loadPluginThread = new HandlerThread(LOAD_PLUGIN_THREAD);
        loadPluginThread.setName("loadPluginThread");
        loadPluginThread.start();
        this.mWorkHandler = new WorkHandler(loadPluginThread.getLooper());
        this.mCurrentState = ServiceConnectState.INITIALIZED;
        if (this.mConnectListener != null) {
            this.mConnectListener.onStateChange(ServiceConnectState.INITIALIZED);
        }
    }

    public static synchronized PlayerMultiProcessClient getInstance() {
        PlayerMultiProcessClient playerMultiProcessClient;
        synchronized (PlayerMultiProcessClient.class) {
            if (mInstance == null) {
                mInstance = new PlayerMultiProcessClient();
            }
            playerMultiProcessClient = mInstance;
        }
        return playerMultiProcessClient;
    }

    public ServiceConnectState getCurrentState() {
        return this.mCurrentState;
    }

    public final synchronized void connect() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "connect() start");
        }
        this.mCurrentState = ServiceConnectState.CONNECTING;
        if (this.mConnectListener != null) {
            this.mConnectListener.onStateChange(ServiceConnectState.CONNECTING);
        }
        Intent intent = new Intent(IntentUtils.getActionName("com.gala.video.PlayerMultiProcessService"));
        intent.setPackage(Project.getInstance().getBuild().getPackageName());
        this.mContext.bindService(intent, this.mConnection, 1);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "connect() end");
        }
    }

    public final synchronized Bundle invoke(Bundle params) {
        Bundle bundle;
        bundle = null;
        try {
            bundle = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return bundle;
    }

    public synchronized void loadPlayerFeatureAsync(Context context, OnStateChangedListener listener, boolean showLoading) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadPlayerFeatureAsync() start");
        }
        if (this.mLoadProvderResult) {
            listener.onSuccess();
        } else {
            Message msg = Message.obtain();
            PluginStateChangedListener myListener = new PluginStateChangedListener(context, Looper.myLooper(), listener);
            myListener.setFailCount(this.mFailCount.get());
            msg.obj = myListener;
            if (showLoading) {
                myListener.onLoading();
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "noLoading");
            }
            this.mWorkHandler.sendMessage(msg);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "loadPlayerFeatureAsync() end");
            }
        }
    }

    private synchronized boolean loadPlayerFeature() {
        Bundle result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadPlayerFeature() start");
        }
        result = null;
        Bundle params = new Bundle();
        params.putString(PlayerCommand.CMD, PlayerCommand.LOAD_PLUGIN_ASYNC);
        try {
            result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.mFailCount.set(result.getInt(PlayerCommand.LOAD_FAILED_COUNT));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadPlayerFeature result=" + result.getBoolean(PlayerCommand.PARCELABLE_RESULT));
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "loadPlayerFeature() end");
        }
        return result.getBoolean(PlayerCommand.PARCELABLE_RESULT);
    }

    public synchronized boolean isSupportDolby() {
        Bundle result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isSupportDolby() start");
        }
        Bundle params = new Bundle();
        result = null;
        params.putString(PlayerCommand.CMD, PlayerCommand.IS_SUPPORT_DOLBY);
        try {
            result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isSupportDolby() end");
        }
        return result.getBoolean(PlayerCommand.PARCELABLE_RESULT);
    }

    public synchronized boolean isSupportH211() {
        Bundle result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isSupportH211() start");
        }
        Bundle params = new Bundle();
        result = null;
        params.putString(PlayerCommand.CMD, PlayerCommand.IS_SUPPORT_H211);
        try {
            result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isSupportH211() end");
        }
        return result.getBoolean(PlayerCommand.PARCELABLE_RESULT);
    }

    public synchronized String getLog(int logType) {
        Bundle result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getLog() start type: " + logType);
        }
        Bundle params = new Bundle();
        result = null;
        params.putString(PlayerCommand.CMD, PlayerCommand.GET_PUMA_LOG);
        params.putInt(PlayerCommand.PLAYER_TYPE, logType);
        try {
            result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPumaLog() end");
        }
        return result.getString(PlayerCommand.PARCELABLE_RESULT);
    }

    public synchronized void updateDeviceCheckInfo(String apiKey, String authId) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateDeviceCheckInfo()");
        }
        Bundle params = new Bundle();
        params.putString(PlayerCommand.CMD, PlayerCommand.CMD_UPDATE_DEVICE_CHECK);
        params.putString("APIKEY", apiKey);
        params.putString("AUTHID", authId);
        try {
            Bundle result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateDeviceCheckInfo() end");
        }
    }

    public synchronized void updateAuthorization(String authorization) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateAuthorization()");
        }
        Bundle params = new Bundle();
        params.putString(PlayerCommand.CMD, PlayerCommand.CMD_UPDATE_AUTHORIZATION);
        params.putString(PlayerCommand.KEY_AUTHORIZATION, authorization);
        try {
            Bundle result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateAuthorization() end");
        }
    }

    public synchronized void enableHCDNPreDeploy(boolean enable) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "enableHCDNPreDeploy(enable: " + enable + ")");
        }
        Bundle params = new Bundle();
        params.putString(PlayerCommand.CMD, PlayerCommand.ENABLE_HCDN_PRE_DEPLOY);
        params.putBoolean(PlayerCommand.EXTRA, enable);
        try {
            this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "enableHCDNPreDeploy(enable: " + enable + "), end");
        }
    }

    public synchronized void setHCDNCleanAvailable(boolean available) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setHCDNCleanAvailable(available: " + available + ")");
        }
        Bundle params = new Bundle();
        params.putString(PlayerCommand.CMD, PlayerCommand.ENABLE_HCDN_CLEAN_AVAILABLE);
        params.putBoolean(PlayerCommand.EXTRA, available);
        try {
            this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setHCDNCleanAvailable(available: " + available + "), end");
        }
    }

    public synchronized String getPlayerModulesVersion() {
        Bundle result;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPlayerModulesVersion() start ");
        }
        Bundle params = new Bundle();
        result = null;
        params.putString(PlayerCommand.CMD, PlayerCommand.GET_PLAYER_MODULE_VERSION);
        try {
            result = this.mPlayerMultiProcessBinder.invoke(params);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPlayerModulesVersion() end");
        }
        return result.getString(PlayerCommand.PARCELABLE_RESULT);
    }
}
