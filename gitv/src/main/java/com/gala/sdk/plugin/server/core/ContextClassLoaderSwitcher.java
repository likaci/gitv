package com.gala.sdk.plugin.server.core;

import android.content.Context;
import android.content.ContextWrapper;
import com.gala.sdk.plugin.Log;
import com.gala.sdk.plugin.server.utils.ObjectFiledHolder;
import com.gala.sdk.plugin.server.utils.ObjectMethodHolder;

public class ContextClassLoaderSwitcher {
    private static final String TAG = "ContextClassLoaderSwitcher";

    public ClassLoader createClassLoaderProxy(Context context, ClassLoader customLoader) {
        if (Log.DEBUG) {
            Log.w(TAG, "createClassLoaderProxy (" + context + ", " + customLoader + ")");
        }
        try {
            return new ClassLoaderProxy((ClassLoader) getClassLoaderField(context).get(), customLoader);
        } catch (Throwable throwable) {
            if (!Log.DEBUG) {
                return null;
            }
            Log.w(TAG, "createClassLoaderProxy() fail!", throwable);
            return null;
        }
    }

    private ObjectFiledHolder<ClassLoader> getClassLoaderField(Context context) throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
        Object obj = context;
        if (context instanceof ContextWrapper) {
            obj = new ObjectMethodHolder(context, "getBaseContext", new Class[0]).invoke(new Object[0]);
        }
        Object mPackageInfo = new ObjectFiledHolder(obj, "mPackageInfo").get();
        new ObjectMethodHolder(mPackageInfo, "getClassLoader", new Class[0]).invoke(new Object[0]);
        return new ObjectFiledHolder(mPackageInfo, "mClassLoader");
    }
}
