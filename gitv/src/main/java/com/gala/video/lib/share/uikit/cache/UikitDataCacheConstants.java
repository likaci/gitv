package com.gala.video.lib.share.uikit.cache;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import java.io.File;

public class UikitDataCacheConstants {
    public static String PATH = (AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "home/home_cache/");
}
