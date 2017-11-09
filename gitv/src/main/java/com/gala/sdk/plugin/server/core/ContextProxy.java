package com.gala.sdk.plugin.server.core;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.utils.FileUtils;

public class ContextProxy extends ContextWrapper {
    private static final String TAG = "ContextProxy";
    private final AssetManager mAssetManager;
    private ClassLoader mClassLoaderProxy;
    private final Resources mResources;
    private final Theme mTheme;

    public ContextProxy(Context base, String dexPath, ClassLoader classloaderProxy) {
        super(base);
        this.mClassLoaderProxy = classloaderProxy;
        if (FileUtils.exists(dexPath)) {
            this.mAssetManager = createAssetManager(dexPath);
            if (this.mAssetManager != null) {
                this.mResources = createResources(this.mAssetManager);
                this.mTheme = this.mResources.newTheme();
                this.mTheme.setTo(super.getTheme());
                return;
            }
            this.mResources = null;
            this.mTheme = null;
            return;
        }
        this.mAssetManager = null;
        this.mResources = null;
        this.mTheme = null;
    }

    public ClassLoader getClassLoader() {
        Log.e(TAG, "getClassLoader(" + this.mClassLoaderProxy);
        return this.mClassLoaderProxy;
    }

    private AssetManager createAssetManager(String dexPath) {
        try {
            AssetManager assetManager = (AssetManager) AssetManager.class.newInstance();
            assetManager.getClass().getMethod("addAssetPath", new Class[]{String.class}).invoke(assetManager, new Object[]{dexPath});
            return assetManager;
        } catch (Throwable throwable) {
            Log.e(TAG, "createAssetManager(" + dexPath + ") fail!", throwable);
            return null;
        }
    }

    private Resources createResources(AssetManager assetManager) {
        Resources superRes = getBaseContext().getResources();
        return new Resources(assetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
    }

    public Resources getResources() {
        if (Log.DEBUG) {
            Log.d(TAG, "getResources() mResources=" + this.mResources);
        }
        if (this.mResources != null) {
            return this.mResources;
        }
        return super.getResources();
    }

    public AssetManager getAssets() {
        if (Log.DEBUG) {
            Log.d(TAG, "getAssets() mAssetManager=" + this.mAssetManager);
        }
        if (this.mAssetManager != null) {
            return this.mAssetManager;
        }
        return super.getAssets();
    }

    public Theme getTheme() {
        if (Log.DEBUG) {
            Log.d(TAG, "getTheme() mTheme=" + this.mTheme);
        }
        if (this.mTheme != null) {
            return this.mTheme;
        }
        return super.getTheme();
    }

    public Object getSystemService(String name) {
        return super.getSystemService(name);
    }
}
