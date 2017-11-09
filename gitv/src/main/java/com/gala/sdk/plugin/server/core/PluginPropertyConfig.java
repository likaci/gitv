package com.gala.sdk.plugin.server.core;

import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import com.gala.sdk.plugin.server.utils.Util;
import java.util.Map;

public class PluginPropertyConfig {
    public static final long DEFAULT_UPGRADE_INTERVAL = 86400000;
    private static final String PROPERTY_NEED_AUTO_UPGRADE = "auto_upgrade";
    private static final String PROPERTY_UPGRADE_INTERVAL = "upgrade_interval";
    private static final String TAG = "PluginPropertyConfig";

    public static void setUpgradeInterval(PluginHelper helper, long interval) {
        if (Log.VERBOSE) {
            Log.v(TAG, "setUpgradeInterval(interval=" + interval + ")");
        }
        helper.setProperty(AbsPluginProvider.PLUGIN_ID_ALL, PROPERTY_UPGRADE_INTERVAL, Long.valueOf(interval));
    }

    public static long getUpgradeInterval(PluginHelper helper) {
        long interval = Util.parseLong(getPropertyValue(helper, AbsPluginProvider.PLUGIN_ID_ALL, PROPERTY_UPGRADE_INTERVAL), 86400000);
        if (PluginDebugUtils.needDebug(DEBUG_PROPERTY.NEED_DEBUG_UPGRADE_INTERAVL)) {
            interval = (long) PluginDebugUtils.getInt(DEBUG_PROPERTY.UPGRADE_INTERVAL);
        }
        if (interval <= 0) {
            interval = 86400000;
        }
        if (Log.VERBOSE) {
            Log.v(TAG, "getUpgradeInterval() return " + interval);
        }
        return interval;
    }

    public static void setAutoUpgrade(PluginHelper helper, boolean autoUpgrade) {
        if (Log.VERBOSE) {
            Log.v(TAG, "setAutoUpgrade(autoUpgrade=" + autoUpgrade + ")");
        }
        helper.setProperty(AbsPluginProvider.PLUGIN_ID_ALL, PROPERTY_NEED_AUTO_UPGRADE, Boolean.valueOf(autoUpgrade));
    }

    public static boolean autoUpgrade(PluginHelper helper, boolean defaultValue) {
        boolean autoUpgrade = Util.parseBoolean(getPropertyValue(helper, AbsPluginProvider.PLUGIN_ID_ALL, PROPERTY_NEED_AUTO_UPGRADE), defaultValue);
        if (Log.VERBOSE) {
            Log.v(TAG, "autoUpgrade() return " + autoUpgrade);
        }
        return autoUpgrade;
    }

    private static Object getPropertyValue(PluginHelper helper, String pluginId, String propertyKey) {
        Map<String, PluginProperty> propertys = helper.getPropertys();
        String id = null;
        if (containProperty(propertys, pluginId, propertyKey)) {
            id = pluginId;
        } else if (containProperty(propertys, AbsPluginProvider.PLUGIN_ID_ALL, propertyKey)) {
            id = AbsPluginProvider.PLUGIN_ID_ALL;
        }
        if (id != null) {
            return ((PluginProperty) propertys.get(id)).getProperty(propertyKey);
        }
        return null;
    }

    private static boolean containProperty(Map<String, PluginProperty> propertys, String pluginId, String propertyKey) {
        if (propertys == null || !propertys.containsKey(pluginId)) {
            return false;
        }
        return ((PluginProperty) propertys.get(pluginId)).containProperty(propertyKey);
    }
}
