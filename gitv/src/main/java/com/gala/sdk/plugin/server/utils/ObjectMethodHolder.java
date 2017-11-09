package com.gala.sdk.plugin.server.utils;

import com.gala.sdk.plugin.Log;
import java.lang.reflect.Method;

public class ObjectMethodHolder implements IMethodHolder {
    private static final String TAG = "ObjectMethodHolder";
    private static boolean sLoggable = Log.DEBUG;
    private boolean mCacheValue;
    private boolean mCached;
    private Object mInstance;
    private Method mMethod;
    private final String mMethodName;
    private Class<?>[] mMethodParams;
    private boolean mTried;
    private Object mValue;

    public ObjectMethodHolder(Object instance, String methodName, Class<?>... params) {
        this(instance, true, methodName, params);
    }

    public ObjectMethodHolder(Object instance, boolean cacheValue, String methodName, Class<?>... params) {
        if (instance == null || methodName == null) {
            throw new IllegalArgumentException("Wrong argument for ObjectMethodHolder(" + instance + ", " + methodName + ")");
        }
        this.mInstance = instance;
        this.mCacheValue = cacheValue;
        this.mMethodName = methodName;
        this.mMethodParams = params;
    }

    public Object invoke(Object... args) {
        if (sLoggable) {
            Log.v(TAG, ">> invoke() mMethodName=" + this.mMethodName + ", mCached=" + this.mCached + ", mCacheValue=" + this.mCacheValue + ", mMethod=" + this.mMethod);
        }
        StringBuilder argsString = new StringBuilder();
        if (args == null) {
            argsString.append("[VOID]");
        } else {
            for (Object arg : args) {
                if (arg != null) {
                    argsString.append(arg.toString()).append(",");
                }
            }
        }
        if (!this.mTried) {
            this.mTried = true;
            try {
                this.mMethod = this.mInstance.getClass().getMethod(this.mMethodName, this.mMethodParams);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "invoke(" + argsString.toString() + ") error! " + this.mInstance.getClass(), e);
            }
        }
        if (!(this.mCached && this.mCacheValue)) {
            this.mCached = true;
            if (this.mMethod != null) {
                try {
                    this.mValue = this.mMethod.invoke(this.mInstance, args);
                } catch (Exception e2) {
                    Log.w(TAG, "invoke(" + argsString.toString() + ") error!", e2);
                }
            }
        }
        if (sLoggable) {
            StringBuilder valueBuilder = new StringBuilder();
            if (this.mValue == null) {
                valueBuilder.append("NULL");
            } else {
                valueBuilder.append(this.mValue.getClass().getName()).append("{").append(this.mValue).append("}");
            }
            Log.v(TAG, "<< invoke() mMethodName=" + this.mMethodName + ", mCached=" + this.mCached + ", mCacheValue=" + this.mCacheValue + ", mMethod=" + this.mMethod + " return " + valueBuilder.toString());
        }
        return this.mValue;
    }
}
