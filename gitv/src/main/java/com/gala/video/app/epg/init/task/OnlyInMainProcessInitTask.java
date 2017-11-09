package com.gala.video.app.epg.init.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.gala.albumprovider.AlbumProviderApi;
import com.gala.video.app.epg.home.data.hdata.DataRequestTaskStrategy;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.AdsClient;

public class OnlyInMainProcessInitTask implements Runnable {
    private static final String TAG = "startup/OnlyInMainProcessInitTask";
    private BroadcastReceiver mScreenReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.SCREEN_ON".equals(intent.getAction())) {
                Project.getInstance().getConfig().onScreenOnEvent(context);
            }
        }
    };

    public void run() {
        LogUtils.d(TAG, ">> execute in main process and main thread");
        Context context = AppRuntimeEnv.get().getApplicationContext();
        DataRequestTaskStrategy.getInstance().start();
        GetInterfaceTools.getStartupDataLoader().forceLoad(false);
        registerScreenReceiver(context);
        AlbumProviderApi.getAlbumProvider().setContext(context);
        AlbumProviderApi.getAlbumProvider().getProperty().setDebugFlag(Project.getInstance().getBuild().isTestErrorCodeAndUpgrade());
        AdsClient.initialise(context, false);
        GetInterfaceTools.getIThemeZipHelper().init();
        LogUtils.d(TAG, "<< execute in main process and main thread");
    }

    private void registerScreenReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_ON");
        filter.addAction("android.intent.action.SCREEN_OFF");
        context.registerReceiver(this.mScreenReceiver, filter);
    }
}
