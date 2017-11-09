package com.gala.video.app.player;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import com.gala.video.app.player.init.PlayerInitFactory;
import com.gala.video.app.player.init.PlayerInterfaceFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.BroadcastConfigPlayer;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.InterfaceManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiInitHelper;
import com.gala.video.lib.share.project.Project;

public class PlayerApplication extends Application {
    private static final String TAG = "GalaApplication";
    private LayoutInflater mLayoutInflater;

    public void onCreate() {
        super.onCreate();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">>gala application create start");
        }
        init();
    }

    public void init() {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        InterfaceManager.get().register(InterfaceKey.PLAYER_IFF, new PlayerInterfaceFactory());
        new IOpenApiInitHelper().getPlayerHelper().init(context, StringUtils.parseStringtoList(Project.getInstance().getBuild().getOpenapiFeatureList()));
        BroadcastConfigPlayer.initialize(context, StringUtils.parseStringtoList(Project.getInstance().getBuild().getBroadcastActions())).initBroadcastFeatures();
        GetInterfaceTools.getHCDNController().initialize();
        GetInterfaceTools.getIInit().execute(PlayerInitFactory.makeUpPlayerPluginInitTask());
        GetInterfaceTools.getIInit().execute(PlayerInitFactory.makeUpPlayerPingbackManagerInitTask());
        GetInterfaceTools.getIInit().execute(PlayerInitFactory.makeUpPlayerConfigJsInitTask());
        GetInterfaceTools.getIInit().execute(PlayerInitFactory.makeUpIntertrustDrmPluginInitTask());
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<<gala player application create end");
        }
        GetInterfaceTools.getDataBus().postStickyEvent(IDataBus.PLAY_PLUGIN_LOAD_SUCCESS);
    }
}
