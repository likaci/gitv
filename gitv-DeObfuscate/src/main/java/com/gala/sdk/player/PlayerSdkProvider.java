package com.gala.sdk.player;

import android.content.Context;
import android.util.Log;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.PluginType;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.server.PluginManager;

class PlayerSdkProvider {
    private static final String PLAYER_SDK_CLASS_NAME = "com.gala.video.player.SdkFeature";
    private static final String PLUGIN_SDK_API_ID = "pluginplayer";
    private static final String TAG = "PlayerSdk/PlayerSdkProvider";
    private static PlayerSdkProvider sInstance;
    private Context mAppContext;
    private PlayerSdk mPlayerSdk;

    private PlayerSdkProvider() {
    }

    public static synchronized PlayerSdkProvider getInstance() {
        PlayerSdkProvider playerSdkProvider;
        synchronized (PlayerSdkProvider.class) {
            if (sInstance == null) {
                sInstance = new PlayerSdkProvider();
            }
            playerSdkProvider = sInstance;
        }
        return playerSdkProvider;
    }

    public void initialize(Context context, Parameter params) {
        this.mAppContext = context.getApplicationContext();
        switch (Build.getBuildType()) {
            case 1:
                this.mPlayerSdk = createInstanceFromPlugin(params);
                return;
            default:
                this.mPlayerSdk = createInstanceFromJar(params);
                return;
        }
    }

    public PlayerSdk getPlayerSdkInstance() {
        return this.mPlayerSdk;
    }

    private PlayerSdk createInstanceFromJar(Parameter params) {
        Context context;
        ClassLoader classLoader;
        PlayerSdk playerSdk = null;
        Log.d(TAG, ">> createInstanceFromJar");
        Object object = params != null ? params.getObject("o_plugin_context") : null;
        if (object instanceof Context) {
            context = (Context) object;
        } else {
            context = null;
        }
        if (context != null) {
            classLoader = context.getClassLoader();
        } else {
            classLoader = null;
        }
        if (classLoader != null) {
            try {
                ISdkFeature iSdkFeature = (ISdkFeature) classLoader.loadClass(PLAYER_SDK_CLASS_NAME).getMethod("instance", new Class[]{Context.class, AbsPluginProvider.class, Integer.TYPE}).invoke(null, new Object[]{null, null, Integer.valueOf(0)});
                if (iSdkFeature != null) {
                    playerSdk = iSdkFeature.getPlayerSdkInstance();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "<< createInstanceFromJar: return " + playerSdk);
        return playerSdk;
    }

    private PlayerSdk createInstanceFromPlugin(Parameter parameter) {
        PlayerSdk playerSdkInstance;
        Log.d(TAG, ">> createInstanceFromPlugin");
        AppInfo appInfo = new AppInfo();
        appInfo.putPluginType("pluginplayer", PluginType.DEFAULT_TYPE);
        PluginManager.initizlie(this.mAppContext, appInfo);
        try {
            Result loadProvider = PluginManager.instance().loadProvider(new HostPluginInfo("pluginplayer", getVersion()));
            loadProvider.getCode();
            if (loadProvider.getData() != null) {
                ISdkFeature iSdkFeature = (ISdkFeature) ((AbsPluginProvider) loadProvider.getData()).getFeature(2);
                playerSdkInstance = iSdkFeature != null ? iSdkFeature.getPlayerSdkInstance() : null;
                Log.d(TAG, "<< createInstanceFromPlugin: return " + playerSdkInstance);
                return playerSdkInstance;
            }
        } catch (Throwable e) {
            Log.e(TAG, "loadPluginPlayerFeature() fail!", e);
        }
        playerSdkInstance = null;
        Log.d(TAG, "<< createInstanceFromPlugin: return " + playerSdkInstance);
        return playerSdkInstance;
    }

    private String getVersion() {
        return "2.1.0" + "12345";
    }
}
