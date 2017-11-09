package com.gala.video.app.epg.multiscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public class MultiScreenReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        MultiScreenStartTool.start(AppRuntimeEnv.get().getApplicationContext());
        MSLog.log("MultiScreenReceiver");
    }
}
