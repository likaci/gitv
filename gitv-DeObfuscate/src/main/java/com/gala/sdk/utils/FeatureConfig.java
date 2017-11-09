package com.gala.sdk.utils;

import java.util.Map;

public class FeatureConfig {
    public static String parseHostUUID(Map<String, String> extras) {
        if (extras == null) {
            return null;
        }
        return (String) extras.get("host_uuid");
    }

    public static void setHostUUID(Map<String, String> extras, String hostUUID) {
        if (extras != null) {
            extras.put("host_uuid", hostUUID);
        }
    }

    public static String parseChannelVersion(Map<String, String> extras) {
        if (extras == null) {
            return null;
        }
        return (String) extras.get("channel_version");
    }

    public static void setChannelVersion(Map<String, String> extras, String channelVersion) {
        if (extras != null) {
            extras.put("channel_version", channelVersion);
        }
    }

    public static String parsePluginUUID(Map<String, String> extras) {
        if (extras == null) {
            return null;
        }
        return (String) extras.get("plugin_uuid");
    }

    public static void setPluginUUID(Map<String, String> extras, String pluginUUID) {
        if (extras != null) {
            extras.put("plugin_uuid", pluginUUID);
        }
    }

    public static String parseDomain(Map<String, String> extras) {
        if (extras == null) {
            return null;
        }
        return (String) extras.get("host_domain");
    }

    public static void setDomain(Map<String, String> extras, String domain) {
        if (extras != null) {
            extras.put("host_domain", domain);
        }
    }

    public static String parseFeatureFlags(Map<String, String> extras) {
        if (extras == null) {
            return null;
        }
        return (String) extras.get("feature_flags");
    }

    public static void setFeatureFlags(Map<String, String> extras, String flags) {
        if (extras != null) {
            extras.put("feature_flags", flags);
        }
    }

    public static void setDeviceInfo(Map<String, String> extras, String deviceInfo) {
        if (extras != null) {
            extras.put("device_info", deviceInfo);
        }
    }

    public static String parseDeviceInfo(Map<String, String> extras) {
        if (extras == null) {
            return null;
        }
        return (String) extras.get("device_info");
    }
}
