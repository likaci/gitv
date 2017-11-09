package com.gala.video.lib.framework.core.utils.reflect;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClassMethodHolder implements IMethodHolder {
    private static final String TAG = "ClassMethodHolder";
    private boolean mCached;
    private Class<?> mClazz;
    private Method mMethod;
    private String mMethodName;
    private Class<?>[] mMethodParams;
    private boolean mTried;
    private Object mValue;

    public ClassMethodHolder(Class<?> clazz, String methodName, Class<?>... params) {
        if (clazz == null || methodName == null) {
            throw new IllegalArgumentException("Wrong argument for ClassMethodHolder(" + clazz + ", " + methodName + ")");
        }
        this.mClazz = clazz;
        this.mMethodName = methodName;
        this.mMethodParams = params;
    }

    public ClassMethodHolder(String className, String methodName, Class<?>... params) {
        if (className == null || methodName == null) {
            throw new IllegalArgumentException("Wrong argument for ClassMethodHolder(" + className + ", " + methodName + ")");
        }
        Class<?> clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ExceptionInInitializerError e) {
            Log.e(TAG, "ClassMethodHolder(" + className + ", " + methodName + ")", e);
        } catch (ClassNotFoundException e2) {
            Log.e(TAG, "ClassMethodHolder(" + className + ", " + methodName + ")", e2);
        } catch (LinkageError e3) {
            Log.e(TAG, "ClassMethodHolder(" + className + ", " + methodName + ")", e3);
        }
        this.mClazz = clazz;
        this.mMethodName = methodName;
        this.mMethodParams = params;
    }

    public Object getValue(Object... args) {
        if (!this.mTried) {
            this.mTried = true;
            try {
                this.mMethod = this.mClazz.getMethod(this.mMethodName, this.mMethodParams);
            } catch (NoSuchMethodException e) {
                Log.w(TAG, "getValue(" + args + ") error! " + this.mClazz, e);
            }
        }
        if (!this.mCached) {
            this.mCached = true;
            if (this.mMethod != null) {
                try {
                    this.mValue = this.mMethod.invoke(this.mClazz, args);
                } catch (IllegalArgumentException e2) {
                    Log.w(TAG, "getValue(" + args + ") error!", e2);
                } catch (IllegalAccessException e3) {
                    Log.w(TAG, "getValue(" + args + ") error!", e3);
                } catch (InvocationTargetException e4) {
                    Log.w(TAG, "getValue(" + args + ") error!", e4);
                } catch (NullPointerException e5) {
                    Log.w(TAG, "getValue(" + args + ") error!", e5);
                }
            }
        }
        Log.v(TAG, "getValue() mMethodName=" + this.mMethodName + ", mCached=" + this.mCached + ", mMethod=" + this.mMethod + " return " + this.mValue);
        return this.mValue;
    }
}
