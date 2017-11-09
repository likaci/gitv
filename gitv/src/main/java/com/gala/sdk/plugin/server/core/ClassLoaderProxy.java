package com.gala.sdk.plugin.server.core;

import com.gala.sdk.plugin.Log;

public class ClassLoaderProxy extends ClassLoader {
    private static final String TAG = "ClassLoaderProxy";
    private final ClassLoader mCustomLoader;

    public ClassLoaderProxy(ClassLoader parent, ClassLoader customer) {
        super(parent);
        this.mCustomLoader = customer;
    }

    public Class<?> loadClass(String className) throws ClassNotFoundException {
        Class<?> clazz = null;
        if (this.mCustomLoader != null) {
            try {
                clazz = this.mCustomLoader.loadClass(className);
            } catch (ClassNotFoundException e) {
                if (Log.DEBUG) {
                    Log.w(TAG, "loadClass(" + className + ") fail!");
                }
                clazz = getSystemClassLoader().loadClass(className);
            }
        }
        if (clazz == null) {
            return super.loadClass(className);
        }
        return clazz;
    }
}
