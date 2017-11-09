package com.gala.video.app.epg.project.build;

import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.gala.video.lib.share.project.Project;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class BuildHelper {
    public static final String TAG = "BuildHelper";

    public static String getVersionString() {
        IBuildInterface build = Project.getInstance().getBuild();
        return build.getVersionName() + "." + build.getThirdVersion() + "." + build.getVersionCode();
    }

    public static void parseAppParameters() {
        try {
            InputStream stream = AppRuntimeEnv.get().getApplicationContext().getAssets().open("app.cfg");
            Properties properties = new Properties();
            properties.load(stream);
            Set<String> keys = BuildCache.getInstance().getKeys();
            LogUtils.m1568d(TAG, "properties=" + properties.toString());
            for (String key : keys) {
                String value = properties.getProperty(key, "");
                if (!"".equals(value)) {
                    LogUtils.m1568d(TAG, "key=" + key + ":value=" + value);
                    BuildCache.getInstance().putString(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
