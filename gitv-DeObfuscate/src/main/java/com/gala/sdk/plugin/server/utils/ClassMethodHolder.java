package com.gala.sdk.plugin.server.utils;

import com.gala.sdk.plugin.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class ClassMethodHolder implements IMethodHolder {
    private static final String TAG = "ClassMethodHolder";
    private static boolean sLoggable = Log.DEBUG;
    private boolean mCacheValue;
    private boolean mCached;
    private Class<?> mClazz;
    private Method mMethod;
    private final String mMethodName;
    private Class<?>[] mMethodParams;
    private boolean mTried;
    private Object mValue;

    public ClassMethodHolder(Class<?> clazz, String methodName, Class<?>... params) {
        this((Class) clazz, true, methodName, (Class[]) params);
    }

    public ClassMethodHolder(Class<?> clazz, boolean cacheValue, String methodName, Class<?>... params) {
        if (clazz == null || methodName == null) {
            throw new IllegalArgumentException("Wrong argument for ClassMethodHolder(" + clazz + ", " + methodName + ")");
        }
        this.mClazz = clazz;
        this.mMethodName = methodName;
        this.mMethodParams = params;
    }

    public ClassMethodHolder(String className, String methodName, Class<?>... params) {
        this(className, true, methodName, (Class[]) params);
    }

    public ClassMethodHolder(String className, boolean cacheValue, String methodName, Class<?>... params) {
        if (className == null || methodName == null) {
            throw new IllegalArgumentException("Wrong argument for ClassMethodHolder(" + className + ", " + methodName + ")");
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ExceptionInInitializerError e) {
            Log.m433e(TAG, "ClassMethodHolder(" + className + ", " + methodName + ")", e);
        } catch (ClassNotFoundException e2) {
            Log.m433e(TAG, "ClassMethodHolder(" + className + ", " + methodName + ")", e2);
        } catch (LinkageError e3) {
            Log.m433e(TAG, "ClassMethodHolder(" + className + ", " + methodName + ")", e3);
        }
        this.mClazz = clazz;
        this.mMethodName = methodName;
        this.mMethodParams = params;
    }

    public Object invoke(Object... args) {
        if (!this.mTried) {
            this.mTried = true;
            try {
                this.mMethod = this.mClazz.getMethod(this.mMethodName, this.mMethodParams);
            } catch (NoSuchMethodException e) {
                Log.m437w(TAG, "invoke(" + args + ") error! " + this.mClazz, e);
            }
        }
        if (!(this.mCached && this.mCacheValue)) {
            this.mCached = true;
            if (this.mMethod != null) {
                try {
                    this.mValue = this.mMethod.invoke(this.mClazz, args);
                } catch (IllegalArgumentException e2) {
                    Log.m437w(TAG, "invoke(" + args + ") error!", e2);
                } catch (IllegalAccessException e3) {
                    Log.m437w(TAG, "invoke(" + args + ") error!", e3);
                } catch (InvocationTargetException e4) {
                    Log.m437w(TAG, "invoke(" + args + ") error!", e4);
                } catch (NullPointerException e5) {
                    Log.m437w(TAG, "invoke(" + args + ") error!", e5);
                }
            }
        }
        if (sLoggable) {
            Log.m434v(TAG, "invoke() mMethodName=" + this.mMethodName + ", mCached=" + this.mCached + ", mCacheValue=" + this.mCacheValue + ", mMethod=" + this.mMethod + " return " + this.mValue);
        }
        return this.mValue;
    }
}
