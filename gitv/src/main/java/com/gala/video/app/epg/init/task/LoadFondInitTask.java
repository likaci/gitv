package com.gala.video.app.epg.init.task;

import android.content.Context;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.font.FontManager;

public class LoadFondInitTask implements Runnable {
    private static final String TAG = "startup/LoadFondInitTask";

    public void run() {
        LogUtils.d(TAG, ">>gala application font start load.");
        Context context = AppRuntimeEnv.get().getApplicationContext();
        FontManager.getInstance().replaceSystemDefaultFontFromAsset(context);
        CloudUtils.setTypeface(FontManager.getInstance().getTypeface(context));
        CloudUtilsGala.setTypeface(FontManager.getInstance().getTypeface(context));
        LogUtils.d(TAG, "<<gala application font end load.");
    }
}
