package com.gala.sdk.plugin.server.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Util {
    public static boolean isEmpty(String value) {
        if (value == null || value.trim().length() <= 0) {
            return true;
        }
        return false;
    }

    public static boolean equals(String str1, String str2) {
        if (isEmpty(str1)) {
            return isEmpty(str2);
        }
        return str1.equals(str2);
    }

    public static JSONObject toJsonObj(String json) {
        if (isEmpty(json)) {
            return null;
        }
        try {
            return new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int get(JSONObject json, String key, int defaultValue) {
        int value = defaultValue;
        if (json != null) {
            return json.optInt(key, defaultValue);
        }
        return value;
    }

    public static String get(JSONObject json, String key, String defaultValue) {
        String value = defaultValue;
        if (json != null) {
            return json.optString(key, defaultValue);
        }
        return value;
    }

    public static boolean get(JSONObject json, String key, boolean defaultValue) {
        boolean value = defaultValue;
        if (json != null) {
            return json.optBoolean(key, defaultValue);
        }
        return value;
    }

    public static void putJson(JSONObject jsonObject, String key, int value) {
        if (jsonObject != null) {
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putJson(JSONObject jsonObject, String key, String value) {
        if (jsonObject != null) {
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void putJson(JSONObject jsonObject, String key, boolean value) {
        if (jsonObject != null) {
            try {
                jsonObject.put(key, value);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static int compareToVersion(String lftVersionName, String rgtVersionName) {
        if (equals(lftVersionName, rgtVersionName)) {
            return 0;
        }
        if (isEmpty(rgtVersionName)) {
            return -1;
        }
        if (isEmpty(lftVersionName)) {
            return 1;
        }
        String[] lftVersions = lftVersionName.split("\\.");
        String[] rgtVersions = rgtVersionName.split("\\.");
        if (lftVersions == rgtVersions) {
            return 0;
        }
        if (rgtVersions == null) {
            return -1;
        }
        if (lftVersions == null) {
            return 1;
        }
        if (lftVersions.length > rgtVersions.length) {
            return -1;
        }
        if (lftVersions.length < rgtVersions.length) {
            return 1;
        }
        for (int i = 0; i < lftVersions.length; i++) {
            int l = parseInt(lftVersions[i], -1);
            int r = parseInt(rgtVersions[i], -1);
            if (l > r) {
                return -1;
            }
            if (l < r) {
                return 1;
            }
        }
        return 0;
    }

    public static int parseInt(String value, int defaultValue) {
        int ret = defaultValue;
        if (isEmpty(value)) {
            return ret;
        }
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean parseBoolean(Object value, boolean defaultValue) {
        boolean ret = defaultValue;
        if (value instanceof Boolean) {
            return ((Boolean) value).booleanValue();
        }
        return ret;
    }

    public static long parseLong(Object value, long defaultValue) {
        long ret = defaultValue;
        if (value == null || !(value instanceof Long)) {
            return ret;
        }
        return ((Long) value).longValue();
    }
}
