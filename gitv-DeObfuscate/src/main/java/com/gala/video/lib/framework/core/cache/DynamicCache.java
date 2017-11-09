package com.gala.video.lib.framework.core.cache;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DynamicCache implements Serializable {
    private static final long serialVersionUID = 1;
    private final Map<String, Object> mDynamicMap;

    private static class SingletonHelper {
        private static DynamicCache instance = new DynamicCache();

        private SingletonHelper() {
        }
    }

    private DynamicCache() {
        this.mDynamicMap = new HashMap();
    }

    public static DynamicCache get() {
        return SingletonHelper.instance;
    }

    public void put(String key, Object value) {
        synchronized (this) {
            try {
                this.mDynamicMap.put(key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putAll(Map<String, Object> map) {
        synchronized (this) {
            try {
                this.mDynamicMap.putAll(map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putAll(DynamicCache config) {
        synchronized (this) {
            try {
                this.mDynamicMap.putAll(config.mDynamicMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void putInt(String key, int value) {
        put(key, Integer.valueOf(value));
    }

    public void putBoolean(String key, boolean value) {
        put(key, Boolean.valueOf(value));
    }

    public void putLong(String key, long value) {
        put(key, Long.valueOf(value));
    }

    public void putFloat(String key, float value) {
        put(key, Float.valueOf(value));
    }

    public void putString(String key, String value) {
        put(key, value);
    }

    public int getInt(String key, int defValue) {
        synchronized (this) {
            Integer v = (Integer) this.mDynamicMap.get(key);
            if (v != null) {
                defValue = v.intValue();
            }
        }
        return defValue;
    }

    public long getLong(String key, long defValue) {
        synchronized (this) {
            Long v = (Long) this.mDynamicMap.get(key);
            if (v != null) {
                defValue = v.longValue();
            }
        }
        return defValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        synchronized (this) {
            Boolean v = (Boolean) this.mDynamicMap.get(key);
            if (v != null) {
                defValue = v.booleanValue();
            }
        }
        return defValue;
    }

    public float getFloat(String key, float defValue) {
        synchronized (this) {
            Float v = (Float) this.mDynamicMap.get(key);
            if (v != null) {
                defValue = v.floatValue();
            }
        }
        return defValue;
    }

    public String getString(String key, String defValue) {
        String v;
        synchronized (this) {
            v = (String) this.mDynamicMap.get(key);
            if (v == null) {
                v = defValue;
            }
        }
        return v;
    }

    public Object get(String key) {
        Object obj;
        synchronized (this) {
            obj = this.mDynamicMap.get(key);
        }
        return obj;
    }

    public void remove(String key) {
        synchronized (this) {
            this.mDynamicMap.remove(key);
        }
    }

    public Map<String, Object> getDynamicMap() {
        return this.mDynamicMap;
    }
}
