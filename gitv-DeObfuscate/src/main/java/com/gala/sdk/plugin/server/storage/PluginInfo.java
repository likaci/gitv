package com.gala.sdk.plugin.server.storage;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.ListUtils;
import com.gala.sdk.plugin.server.utils.Util;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PluginInfo implements Parcelable {
    public static Comparator<PluginInfo> COMPARATOR = new C01783();
    public static final Creator<PluginInfo> CREATOR = new C01772();
    public static final int ERROR_MAX_COUNT = 2;
    private static final String KEY_CLASS_NAME = "plugin_class_name";
    private static final String KEY_CONTAIN_SO = "contain_so";
    private static final String KEY_ERROR_COUNT = "load_error_count";
    private static final String KEY_LIB_FOLDER = "plugin_lib_folder";
    private static final String KEY_LOAD_SUCCESS = "load_success";
    private static final String KEY_PATH = "plugin_path";
    private static final String KEY_PATH_SD = "plugin_path_sd";
    private static final String KEY_PLUGIN_ID = "plugin_id";
    private static final String KEY_SO_COUNT = "so_count";
    private static final String KEY_TRY_SD = "plugin_try_sd";
    private static final String KEY_VERSION_NAME = "plugin_versionname";
    private static final String TAG = "PluginInfo";
    private String mClassName;
    private boolean mContainSoFile;
    private String mId;
    private String mLibFolder;
    private String mPath;
    private String mPathSd;
    private boolean mShouldTrySd;
    private int mSoFileCount;
    private final FileFilter mSoFileFilter;
    private String mVersionName;

    class C01761 implements FileFilter {
        C01761() {
        }

        public boolean accept(File file) {
            return file != null && file.exists() && file.isFile() && SoLibHelper.isSoFile(file.getName()) && file.length() > 0;
        }
    }

    static class C01772 implements Creator<PluginInfo> {
        C01772() {
        }

        public PluginInfo createFromParcel(Parcel parcel) {
            if (Log.VERBOSE) {
                Log.m434v(PluginInfo.TAG, "createFromParcel<< " + parcel);
            }
            Bundle bundle = parcel.readBundle();
            bundle.setClassLoader(PluginInfo.class.getClassLoader());
            return new PluginInfo(bundle);
        }

        public PluginInfo[] newArray(int size) {
            return new PluginInfo[size];
        }
    }

    static class C01783 implements Comparator<PluginInfo> {
        C01783() {
        }

        public int compare(PluginInfo lft, PluginInfo rgt) {
            if (lft == rgt) {
                return 0;
            }
            if (rgt == null) {
                return -1;
            }
            if (lft == null) {
                return 1;
            }
            return Util.compareToVersion(lft.getVersionName(), rgt.getVersionName());
        }
    }

    public PluginInfo(String id, String versionName, String className, String path) {
        this.mShouldTrySd = false;
        this.mSoFileFilter = new C01761();
        this.mId = id;
        this.mVersionName = versionName;
        this.mClassName = className;
        this.mPath = path;
        int last = path.lastIndexOf(".");
        if (last > 0) {
            this.mLibFolder = path.substring(0, last) + "-libs";
        }
    }

    public void setTrySd(boolean shouldtry) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "setTrySd=" + shouldtry + ")");
        }
        this.mShouldTrySd = shouldtry;
    }

    public boolean shouldTrySd() {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "shouldTrySd=" + this.mShouldTrySd + ")");
        }
        return this.mShouldTrySd;
    }

    public void setPluginPathSd(String sdpath) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "setPluginPathSd=" + sdpath + ")");
        }
        this.mPathSd = sdpath;
    }

    private PluginInfo(JSONObject json) {
        this.mShouldTrySd = false;
        this.mSoFileFilter = new C01761();
        if (Log.VERBOSE) {
            Log.m434v(TAG, "PluginInfo<<(json=" + json.toString() + ")");
        }
        this.mId = Util.get(json, KEY_PLUGIN_ID, "");
        this.mVersionName = Util.get(json, KEY_VERSION_NAME, "");
        this.mClassName = Util.get(json, KEY_CLASS_NAME, "");
        this.mPath = Util.get(json, KEY_PATH, "");
        this.mPathSd = Util.get(json, KEY_PATH_SD, "");
        this.mLibFolder = Util.get(json, KEY_LIB_FOLDER, "");
        this.mContainSoFile = Util.get(json, KEY_CONTAIN_SO, false);
        this.mSoFileCount = Util.get(json, KEY_SO_COUNT, 0);
        this.mShouldTrySd = Util.get(json, KEY_TRY_SD, false);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "PluginInfo>>() return " + toString());
        }
    }

    private PluginInfo(Bundle bundle) {
        this.mShouldTrySd = false;
        this.mSoFileFilter = new C01761();
        if (Log.VERBOSE) {
            Log.m434v(TAG, "PluginInfo<<(bundle=" + bundle.toString() + ")");
        }
        this.mId = bundle.getString(KEY_PLUGIN_ID, "");
        this.mVersionName = bundle.getString(KEY_VERSION_NAME, "");
        this.mClassName = bundle.getString(KEY_CLASS_NAME, "");
        this.mPath = bundle.getString(KEY_PATH, "");
        this.mPathSd = bundle.getString(KEY_PATH_SD, "");
        this.mLibFolder = bundle.getString(KEY_LIB_FOLDER, "");
        this.mContainSoFile = bundle.getBoolean(KEY_CONTAIN_SO, false);
        this.mSoFileCount = bundle.getInt(KEY_SO_COUNT, 0);
        this.mShouldTrySd = bundle.getBoolean(KEY_TRY_SD, false);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "PluginInfo>>() return " + toString());
        }
    }

    public String getId() {
        return this.mId;
    }

    public String getVersionName() {
        return this.mVersionName;
    }

    public String getClassName() {
        return this.mClassName;
    }

    public String getPath() {
        return this.mShouldTrySd ? this.mPathSd : this.mPath;
    }

    public String getLibFolder() {
        return this.mLibFolder;
    }

    public boolean containSoFile() {
        return this.mContainSoFile;
    }

    public void setContainSoFile(boolean contain) {
        this.mContainSoFile = contain;
    }

    public int getSoFileCount() {
        return this.mSoFileCount;
    }

    public void setSoFileCount(int soFileCount) {
        this.mSoFileCount = soFileCount;
    }

    public boolean checkFileComplete() {
        boolean isFileComplete = false;
        if (FileUtils.exists(this.mPath)) {
            isFileComplete = this.mContainSoFile ? FileUtils.getFileCount(this.mLibFolder, this.mSoFileFilter) == this.mSoFileCount : true;
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "checkFileComplete>>() return " + isFileComplete);
        }
        return isFileComplete;
    }

    private JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        Util.putJson(jsonObject, KEY_PLUGIN_ID, this.mId);
        Util.putJson(jsonObject, KEY_VERSION_NAME, this.mVersionName);
        Util.putJson(jsonObject, KEY_CLASS_NAME, this.mClassName);
        Util.putJson(jsonObject, KEY_PATH, this.mPath);
        Util.putJson(jsonObject, KEY_PATH_SD, this.mPathSd);
        Util.putJson(jsonObject, KEY_LIB_FOLDER, this.mLibFolder);
        Util.putJson(jsonObject, KEY_CONTAIN_SO, this.mContainSoFile);
        Util.putJson(jsonObject, KEY_SO_COUNT, this.mSoFileCount);
        Util.putJson(jsonObject, KEY_TRY_SD, this.mShouldTrySd);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "toJson>>() return " + jsonObject);
        }
        return jsonObject;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_PLUGIN_ID, this.mId);
        bundle.putString(KEY_VERSION_NAME, this.mVersionName);
        bundle.putString(KEY_CLASS_NAME, this.mClassName);
        bundle.putString(KEY_PATH, this.mPath);
        bundle.putString(KEY_PATH_SD, this.mPathSd);
        bundle.putString(KEY_LIB_FOLDER, this.mLibFolder);
        bundle.putBoolean(KEY_CONTAIN_SO, this.mContainSoFile);
        bundle.putInt(KEY_SO_COUNT, this.mSoFileCount);
        bundle.putBoolean(KEY_TRY_SD, this.mShouldTrySd);
        dest.writeBundle(bundle);
        if (Log.VERBOSE) {
            Log.m434v(TAG, "writeToParcel>>() return " + dest + flags);
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PluginInfo(").append("id=").append(this.mId).append(", versionName=").append(this.mVersionName).append(", className=").append(this.mClassName).append(", path=").append(this.mPath).append(", pathSd=").append(this.mPathSd).append(", libFolder=").append(this.mLibFolder).append(", containSo=").append(this.mContainSoFile).append(", soFileCount=").append(this.mSoFileCount).append(")");
        return builder.toString();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof PluginInfo)) {
            return false;
        }
        PluginInfo target = (PluginInfo) o;
        return Util.equals(this.mId, target.getId()) && Util.equals(this.mVersionName, target.getVersionName());
    }

    public static List<PluginInfo> getListFromJson(String json) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getListFromJson<<(json=" + json + ")");
        }
        List<PluginInfo> list = new ArrayList();
        if (!Util.isEmpty(json)) {
            try {
                JSONArray array = new JSONArray(json);
                int length = array.length();
                for (int i = 0; i < length; i++) {
                    list.add(new PluginInfo(array.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (Log.VERBOSE) {
                Log.m434v(TAG, "getListFromJson>>() return " + list);
            }
        }
        return list;
    }

    public static String getJsonFromList(List<PluginInfo> list) {
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getJsonFromList<<(list=" + list + ")");
        }
        String json = null;
        if (!ListUtils.isEmpty((List) list)) {
            JSONArray array = new JSONArray();
            for (PluginInfo info : list) {
                array.put(info.toJson());
            }
            json = array.toString();
        }
        if (Log.VERBOSE) {
            Log.m434v(TAG, "getJsonFromList>>() return " + json);
        }
        return json;
    }
}
