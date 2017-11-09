package com.gala.sdk.plugin;

import android.content.Context;
import java.util.List;
import java.util.Map;

public abstract class AbsPluginProvider {
    public static final String PARAM_KEY_FILE_PATH = "file_path";
    public static final String PARAM_KEY_LIB_DIR = "lib_dir";
    public static final String PLUGIN_ID_ALL = "all";
    private static final String TAG = "AbsPluginProvider";
    private AppInfo mAppInfo;
    private Context mContext;
    private Context mHostContext;
    private String mLibDir;
    private OnExceptionListener mListener;
    private String mPluginFilePath;

    public interface OnExceptionListener {
        void onException(AbsPluginProvider absPluginProvider, Throwable th);
    }

    public abstract String getDescription();

    public abstract List<IFeature> getFeatures();

    public abstract String getId();

    public abstract String getName();

    public abstract String getVersionName();

    public final void initialize(Context hostContext, Context context, Map<String, String> params, AppInfo info) {
        String str;
        if (Log.DEBUG) {
            Log.m430d(TAG, "initialize<<(hostContext=" + hostContext + ", context=" + context + ", params=" + params + ", info=" + info + ")");
        }
        this.mHostContext = hostContext;
        this.mContext = context;
        if (params != null) {
            str = (String) params.get(PARAM_KEY_LIB_DIR);
        } else {
            str = null;
        }
        this.mLibDir = str;
        if (params != null) {
            str = (String) params.get(PARAM_KEY_FILE_PATH);
        } else {
            str = null;
        }
        this.mPluginFilePath = str;
        this.mAppInfo = info;
        onInitialize();
    }

    public final void setOnExceptionListener(OnExceptionListener listener) {
        this.mListener = listener;
    }

    public final void release() {
        onRelease();
    }

    public final Context getContext() {
        return this.mContext;
    }

    public final String getLibDir() {
        return this.mLibDir;
    }

    public final String getPluginFilePath() {
        return this.mPluginFilePath;
    }

    public final AppInfo getAppInfo() {
        return this.mAppInfo;
    }

    public final IFeature getFeature(int featureType) {
        if (Log.DEBUG) {
            Log.m430d(TAG, "getFeature<<(featureType=" + featureType + ")");
        }
        IFeature find = null;
        List<IFeature> featureList = getFeatures();
        if (featureList != null) {
            for (IFeature feature : featureList) {
                if (feature != null && feature.getType() == featureType) {
                    find = feature;
                    break;
                }
            }
        }
        if (Log.DEBUG) {
            Log.m430d(TAG, "getFeature>>() return " + find);
        }
        return find;
    }

    public final void notifyException(Throwable e) {
        if (Log.DEBUG) {
            Log.m430d(TAG, "notifyException<<(e=" + e + ", listener=" + this.mListener + ")");
        }
        if (this.mListener != null) {
            this.mListener.onException(this, e);
        }
        if (Log.DEBUG) {
            Log.m430d(TAG, "notifyException>>()");
        }
    }

    public final Context getHostContext() {
        return this.mHostContext;
    }

    public boolean canBeUpgrade() {
        return true;
    }

    protected void onRelease() {
    }

    protected void onInitialize() {
    }
}
