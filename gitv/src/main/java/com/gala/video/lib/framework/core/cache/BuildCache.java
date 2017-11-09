package com.gala.video.lib.framework.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BuildCache {
    private Map<String, Object> mConfigMap;

    private static class SingletonHelper {
        private static BuildCache instance = new BuildCache();

        private SingletonHelper() {
        }
    }

    public static BuildCache getInstance() {
        return SingletonHelper.instance;
    }

    private BuildCache() {
        this.mConfigMap = new HashMap();
    }

    public Map<String, Object> getMap() {
        return this.mConfigMap;
    }

    public void putString(String key, String value) {
        synchronized (this) {
            this.mConfigMap.put(key, value);
        }
    }

    public void putBoolean(String key, boolean value) {
        synchronized (this) {
            this.mConfigMap.put(key, Boolean.valueOf(value));
        }
    }

    public String getString(String key, String defValue) {
        String v;
        synchronized (this) {
            v = (String) this.mConfigMap.get(key);
            if (v == null) {
                v = defValue;
            }
        }
        return v;
    }

    public boolean getBoolean(String key, boolean defValue) {
        synchronized (this) {
            Boolean v = (Boolean) this.mConfigMap.get(key);
            if (v != null) {
                defValue = v.booleanValue();
            }
        }
        return defValue;
    }

    public Set<String> getKeys() {
        return this.mConfigMap.keySet();
    }
}
