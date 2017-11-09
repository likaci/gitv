package com.gala.video.lib.share.project.plugin;

import android.content.Context;
import java.util.Map;

public interface IPluginEnv {
    String getPackageNameForAction(Context context);

    String getPluginVersion(String str);

    Map<String, String> getPluginVersions();
}
