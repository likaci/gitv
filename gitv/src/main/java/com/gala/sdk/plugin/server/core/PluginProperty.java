package com.gala.sdk.plugin.server.core;

import java.util.HashMap;
import java.util.Map;

public class PluginProperty {
    private final Map<String, Object> mPropertys = new HashMap();

    public void putProperty(String propertyKey, Object propertyValue) {
        this.mPropertys.put(propertyKey, propertyValue);
    }

    public boolean containProperty(String propertyKey) {
        return this.mPropertys.containsKey(propertyKey);
    }

    public Object getProperty(String propertyKey) {
        return this.mPropertys.get(propertyKey);
    }
}
