package com.gala.sdk.plugin;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.gala.sdk.plugin.server.PluginManager;

public class HostPluginInfo implements Parcelable {
    public static Creator<HostPluginInfo> CREATOR = new Creator<HostPluginInfo>() {
        public HostPluginInfo createFromParcel(Parcel source) {
            return new HostPluginInfo((Bundle) source.readParcelable(HostPluginInfo.class.getClassLoader()));
        }

        public HostPluginInfo[] newArray(int size) {
            return new HostPluginInfo[size];
        }
    };
    private static final String KEY_HOST_VERSION = "host_version";
    private static final String KEY_PLUGIN_ID = "plugin_id";
    private static final String KEY_PLUGIN_VERSION = "host_plugin_default_version";
    private String mHostVersion;
    private String mPluginId;
    private String mPluginVersion;

    public HostPluginInfo(String pluginId, String hostVersion) {
        this.mHostVersion = hostVersion;
        this.mPluginId = pluginId;
        this.mPluginVersion = PluginManager.DEFAULT_PLUGIN_VERSION;
    }

    public HostPluginInfo(HostPluginInfo info) {
        copy(info);
    }

    public String getPluginId() {
        return this.mPluginId;
    }

    public String getPluginVersion() {
        return this.mPluginVersion;
    }

    public void setPluginVersion(String mPluginDefaultVersion) {
        this.mPluginVersion = mPluginDefaultVersion;
    }

    HostPluginInfo(Bundle bundle) {
        if (bundle != null) {
            this.mHostVersion = bundle.getString(KEY_HOST_VERSION, "");
            this.mPluginId = bundle.getString(KEY_PLUGIN_ID, "");
            this.mPluginVersion = bundle.getString(KEY_PLUGIN_VERSION, "");
        }
    }

    public String getHostVersion() {
        return this.mHostVersion;
    }

    private void copy(HostPluginInfo info) {
        if (info != null) {
            this.mHostVersion = info.getHostVersion();
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("HostPluginInfo(").append(hashCode()).append(",PluginId=").append(this.mPluginId).append(",hostVersion=").append(this.mHostVersion).append(",PluginVersion=").append(this.mPluginVersion).append(")");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_HOST_VERSION, this.mHostVersion);
        bundle.putString(KEY_PLUGIN_ID, this.mPluginId);
        bundle.putString(KEY_PLUGIN_VERSION, this.mPluginVersion);
        dest.writeParcelable(bundle, 0);
    }
}
