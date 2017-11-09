package com.gala.sdk.plugin;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.HashMap;
import java.util.Map;

public class AppInfo implements Parcelable {
    public static Creator<AppInfo> CREATOR = new C01701();
    private static final String KEY_EXTRAS = "extras";
    private static final String KEY_PLUGINTYPES = "plugin_types";
    private final Map<String, String> mExtras = new HashMap();
    private boolean mHostAllowDebug;
    private final Map<String, String> mPluginTypes = new HashMap();

    static class C01701 implements Creator<AppInfo> {
        C01701() {
        }

        public AppInfo createFromParcel(Parcel source) {
            return new AppInfo((Bundle) source.readParcelable(AppInfo.class.getClassLoader()));
        }

        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    }

    public AppInfo(AppInfo info) {
        copy(info);
    }

    AppInfo(Bundle bundle) {
        if (bundle != null) {
            Bundle pluginTypes = bundle.getBundle(KEY_PLUGINTYPES);
            this.mPluginTypes.clear();
            this.mPluginTypes.putAll(parseMap(pluginTypes));
            Bundle extras = bundle.getBundle(KEY_EXTRAS);
            this.mExtras.clear();
            this.mExtras.putAll(parseMap(extras));
        }
    }

    public void setHostAllowDebug(boolean allow) {
        this.mHostAllowDebug = allow;
    }

    public boolean getHostAllowDebug() {
        return this.mHostAllowDebug;
    }

    public void putPluginTypes(Map<String, String> pluginTypes) {
        this.mPluginTypes.putAll(pluginTypes);
    }

    public void putPluginType(String id, String type) {
        this.mPluginTypes.put(id, type);
    }

    public Map<String, String> getPluginTypes() {
        return new HashMap(this.mPluginTypes);
    }

    public void putExtras(Map<String, String> extras) {
        this.mExtras.putAll(extras);
    }

    public Map<String, String> getExtras() {
        return new HashMap(this.mExtras);
    }

    private void copy(AppInfo info) {
        if (info != null) {
            this.mHostAllowDebug = info.getHostAllowDebug();
            this.mPluginTypes.clear();
            this.mPluginTypes.putAll(info.getPluginTypes());
            this.mExtras.clear();
            this.mExtras.putAll(info.getExtras());
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("AppInfo(").append("mHostAllowDebug=").append(this.mHostAllowDebug).append(")");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Bundle pluginTypes = new Bundle();
        putMap(pluginTypes, this.mPluginTypes);
        Bundle extras = new Bundle();
        putMap(extras, this.mExtras);
        Bundle bundle = new Bundle();
        bundle.putBundle(KEY_PLUGINTYPES, pluginTypes);
        bundle.putBundle(KEY_EXTRAS, extras);
        dest.writeParcelable(bundle, 0);
    }

    private static void putMap(Bundle bundle, Map<String, String> map) {
        if (bundle != null && map != null && !map.isEmpty()) {
            for (String key : map.keySet()) {
                bundle.putString(key, (String) map.get(key));
            }
        }
    }

    private static Map<String, String> parseMap(Bundle bundle) {
        Map<String, String> mapRet = new HashMap();
        if (!(bundle == null || bundle.isEmpty())) {
            for (String key : bundle.keySet()) {
                String value = bundle.getString(key, null);
                if (value != null) {
                    mapRet.put(key, value);
                }
            }
        }
        return mapRet;
    }
}
