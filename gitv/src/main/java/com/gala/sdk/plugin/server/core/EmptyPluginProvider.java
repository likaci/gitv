package com.gala.sdk.plugin.server.core;

import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.IFeature;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.storage.PluginInfo;
import java.util.List;

public class EmptyPluginProvider extends AbsPluginProvider {
    private static final String TAG = "EmptyPluginProvider";
    private final PluginInfo mPluginInfo;

    public EmptyPluginProvider(PluginInfo info) {
        if (Log.VERBOSE) {
            Log.v(TAG, "EmptyPluginProvider(info=" + info + ")");
        }
        this.mPluginInfo = info;
    }

    public String getId() {
        return this.mPluginInfo.getId();
    }

    public String getName() {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getVersionName() {
        return this.mPluginInfo.getVersionName();
    }

    public List<IFeature> getFeatures() {
        return null;
    }
}
