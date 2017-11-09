package com.gala.sdk.player;

import java.util.HashMap;
import java.util.Map;

public class Parameter {
    private Map<String, String> mMapParams = new HashMap();
    private Map<String, Object> mObjectMapParams = new HashMap();

    public static class Keys {
        public static final String B_AD_SHOW_COUNTDOWN = "b_ad_show_countdown";
        public static final String I_DELAY_INIT_MS = "i_init_delay_ms";
        public static final String S_AD_HINT_HIDE_PAUSE = "s_ad_hint_hide_pause";
        public static final String S_AD_HINT_SHOW_CLICK_THROUGH = "s_ad_hint_show_clickthrough";
        public static final String S_AD_HINT_SKIP_AD = "s_ad_hint_skip_ad";
        public static final String S_APP_VERSION = "s_app_version";
        public static final String S_CDN_DISPATCH_PARAM = "s_cdn_dispatch";
        public static final String S_DEVICE_INFO = "s_device_info";
    }

    public Map<String, String> getAllParams() {
        return this.mMapParams;
    }

    public Parameter setObject(String key, Object obj) {
        this.mObjectMapParams.put(key, obj);
        return this;
    }

    public Object getObject(String key) {
        return this.mObjectMapParams.get(key);
    }

    public Parameter addGroupParams(Map<String, String> paramMap) {
        this.mMapParams.putAll(paramMap);
        return this;
    }

    public Parameter setInt32(String key, int value) {
        this.mMapParams.put(key, String.valueOf(value));
        return this;
    }

    public Parameter setInt64(String key, long value) {
        this.mMapParams.put(key, String.valueOf(value));
        return this;
    }

    public Parameter setFloat(String key, float value) {
        this.mMapParams.put(key, String.valueOf(value));
        return this;
    }

    public Parameter setDouble(String key, double value) {
        this.mMapParams.put(key, String.valueOf(value));
        return this;
    }

    public Parameter setBoolean(String key, boolean value) {
        this.mMapParams.put(key, String.valueOf(value));
        return this;
    }

    public Parameter setString(String key, String value) {
        this.mMapParams.put(key, value);
        return this;
    }

    public int getInt32(String key, int defaultValue) {
        return parseInt(getString(key), defaultValue);
    }

    public long getInt64(String key, long defaultValue) {
        return parseLong(getString(key), defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return parseFloat(getString(key), defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        return parseDouble(getString(key), defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return parseBoolean(getString(key), defaultValue);
    }

    public String getString(String key, String defaultValue) {
        String str = (String) this.mMapParams.get(key);
        return str == null ? defaultValue : str;
    }

    public int getInt32(String key) {
        return getInt32(key, 0);
    }

    public long getInt64(String key) {
        return getInt64(key, 0);
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0d);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public String getString(String key) {
        String str = (String) this.mMapParams.get(key);
        return str == null ? "" : str;
    }

    private static int parseInt(String str, int defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Integer.parseInt(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    private static long parseLong(String str, long defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Long.parseLong(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    private static float parseFloat(String str, float defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Float.parseFloat(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    private static double parseDouble(String str, double defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Double.parseDouble(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    private static boolean parseBoolean(String str, boolean defaultValue) {
        if (!(str == null || str.isEmpty())) {
            try {
                defaultValue = Boolean.parseBoolean(str.trim());
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }
}
