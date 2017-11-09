package com.gala.video.lib.share.project.plugin;

import android.content.Context;
import com.gala.video.lib.share.project.Project;
import java.util.Map;

public class PluginEnvProvider implements IPluginEnv {
    public String getPluginVersion(String pluginName) {
        return Project.getInstance().getBuild().getVersionString();
    }

    public String getPackageNameForAction(Context context) {
        return context.getPackageName();
    }

    public Map<String, String> getPluginVersions() {
        return null;
    }
}
