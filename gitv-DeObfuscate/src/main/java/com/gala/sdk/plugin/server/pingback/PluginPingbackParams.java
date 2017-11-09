package com.gala.sdk.plugin.server.pingback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PluginPingbackParams implements Serializable {
    public static final String DELETE_DEX = "dex";
    public static final String DELETE_OLDCOUNT = "oldcount";
    public static final String DELETE_OLDNAME_LIST = "oldnamelist";
    public static final String DELETE_TD = "td";
    public static final String DOWNLOAD_NEWVERSION = "newsdkv";
    public static final String DOWNLOAD_OLDVERSION = "oldsdkv";
    public static final String DOWNLOAD_SUCCESS = "success";
    public static final String PINGBACK_CT_PLUGIN_DELETE_OLD_FILE = "170510_plugindeleteold";
    public static final String PINGBACK_CT_PLUGIN_DOWNLOAD = "161209_plugindownload";
    public static final String PINGBACK_T = "11";
    public static final String PLUGINID = "pluginid";
    private Map<String, String> mPingBackParamsMap = new HashMap(20);

    public PluginPingbackParams add(String key, String value) {
        this.mPingBackParamsMap.put(key, value);
        return this;
    }

    public String get(String key) {
        return (String) this.mPingBackParamsMap.get(key);
    }
}
