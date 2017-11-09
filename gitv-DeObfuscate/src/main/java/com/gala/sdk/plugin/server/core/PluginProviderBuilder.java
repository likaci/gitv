package com.gala.sdk.plugin.server.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.AppInfo;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.storage.PluginInfo;
import com.gala.sdk.plugin.server.utils.FileUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils;
import com.gala.sdk.plugin.server.utils.PluginDebugUtils.DEBUG_PROPERTY;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PluginProviderBuilder {
    private static final String TAG = "PluginProviderBuilder";
    private static PluginProviderBuilder sInstance;
    private final AppInfo mAppInfo;
    private final Context mHostApplicationContext;
    private final ContextClassLoaderSwitcher mSwitcher = new ContextClassLoaderSwitcher();

    public static synchronized PluginProviderBuilder initizlie(Context mHostApplication, AppInfo appInfo) {
        PluginProviderBuilder pluginProviderBuilder;
        synchronized (PluginProviderBuilder.class) {
            if (sInstance == null) {
                sInstance = new PluginProviderBuilder(mHostApplication, appInfo);
            }
            pluginProviderBuilder = sInstance;
        }
        return pluginProviderBuilder;
    }

    private PluginProviderBuilder(Context mHostApplication, AppInfo appInfo) {
        this.mHostApplicationContext = mHostApplication;
        this.mAppInfo = appInfo;
    }

    public AbsPluginProvider createEmptyProvider(PluginInfo info) throws Exception {
        AbsPluginProvider emptyProvider = new EmptyPluginProvider(info);
        Map<String, String> params = new HashMap();
        params.put(AbsPluginProvider.PARAM_KEY_LIB_DIR, info.getLibFolder());
        params.put(AbsPluginProvider.PARAM_KEY_FILE_PATH, info.getPath());
        emptyProvider.initialize(this.mHostApplicationContext, this.mHostApplicationContext, params, this.mAppInfo);
        return emptyProvider;
    }

    public synchronized AbsPluginProvider createDefaultProvider(PluginInfo info) throws Throwable {
        Map<String, String> params;
        params = new HashMap();
        params.put(AbsPluginProvider.PARAM_KEY_LIB_DIR, info.getLibFolder());
        params.put(AbsPluginProvider.PARAM_KEY_FILE_PATH, info.getPath());
        return createPluginProviderInstance(info, params);
    }

    private AbsPluginProvider createPluginProviderInstance(PluginInfo info, Map<String, String> params) throws Throwable {
        if (Log.DEBUG) {
            Log.m430d(TAG, "createPluginProviderInstance() begin.");
        }
        boolean loadClassFailedDebug = PluginDebugUtils.needThrowable(DEBUG_PROPERTY.LOAD_CLASS_FAILED);
        boolean constructorInvisibleDebug = PluginDebugUtils.needThrowable(DEBUG_PROPERTY.CONSTRUCTOR_INVISIBLE);
        boolean instanceFailed = PluginDebugUtils.needThrowable(DEBUG_PROPERTY.INSTANCE_FAILED);
        String className = info.getClassName();
        try {
            Context pluginContext = createPluginContext(info);
            AbsPluginProvider pluginProvider = (AbsPluginProvider) pluginContext.getClassLoader().loadClass(className).newInstance();
            pluginProvider.initialize(this.mHostApplicationContext, pluginContext, params, this.mAppInfo);
            if (loadClassFailedDebug) {
                throw new ClassNotFoundException(className + " not found!!(for debug!)");
            } else if (constructorInvisibleDebug) {
                throw new IllegalAccessException(className + " constructorInvisible!!(for debug!)");
            } else if (instanceFailed) {
                throw new InstantiationException(className + " instance can not be created!!(for debug!)");
            } else {
                if (Log.DEBUG) {
                    Log.m430d(TAG, "createPluginProviderInstance() return " + pluginProvider);
                }
                return pluginProvider;
            }
        } catch (Exception e) {
            Log.m437w(TAG, "createPluginProviderInstance() fail!", e);
            throw e;
        } catch (Error error) {
            Log.m437w(TAG, "createPluginProviderInstance() error!", error);
            throw error;
        }
    }

    private Context createPluginContext(PluginInfo info) throws Exception {
        String path = info.getPath();
        if (Log.DEBUG) {
            Log.m430d(TAG, "createPluginContext() return " + this.mHostApplicationContext + path);
        }
        if (FileUtils.exists(path)) {
            Context pluginContext = new ContextProxy(this.mHostApplicationContext, path, this.mSwitcher.createClassLoaderProxy(this.mHostApplicationContext, createDexClassLoader(info)));
            if (pluginContext != null && Log.DEBUG) {
                Log.m430d(TAG, "createPluginContext() return " + pluginContext.getPackageCodePath());
            }
            return pluginContext;
        }
        if (Log.DEBUG) {
            Log.m430d(TAG, "why go here");
        }
        throw new Exception("pathOrPackageName not has");
    }

    @SuppressLint({"NewApi"})
    public static String getOptimizeDir(Context context, String pluginId) {
        File dexOutputDir;
        String dir = "";
        if (VERSION.SDK_INT >= 21) {
            if (Log.DEBUG) {
                Log.m430d(TAG, "createDexClassLoader() LOLLIPOP.");
            }
            dexOutputDir = context.getCodeCacheDir();
        } else {
            dexOutputDir = context.getDir(pluginId, 0);
        }
        return dexOutputDir.getAbsolutePath();
    }

    private DexClassLoader createDexClassLoader(PluginInfo info) {
        if (Log.DEBUG) {
            Log.m430d(TAG, "createDexClassLoader() begin.");
        }
        String dexOutputPath = getOptimizeDir(this.mHostApplicationContext, info.getId());
        if (Log.DEBUG) {
            Log.m430d(TAG, "createDexClassLoader() return " + dexOutputPath);
        }
        DexClassLoader loader = new DexClassLoader(info.getPath(), dexOutputPath, null, this.mHostApplicationContext.getClassLoader());
        if (Log.DEBUG) {
            Log.m430d(TAG, "createDexClassLoader() return " + loader);
        }
        return loader;
    }
}
