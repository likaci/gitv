package com.gala.video.lib.framework.core.utils.reflect;

import android.util.Log;
import java.lang.reflect.Method;

public class ObjectMethodHolder implements IMethodHolder {
    private static final String TAG = "ObjectMethodHolder";
    private boolean mCached;
    private Object mInstance;
    private Method mMethod;
    private String mMethodName;
    private Class<?>[] mMethodParams;
    private boolean mTried;
    private Object mValue;

    public ObjectMethodHolder(Object instance, String methodName, Class<?>... params) {
        if (instance == null || methodName == null) {
            throw new IllegalArgumentException("Wrong argument for ObjectMethodHolder(" + instance + ", " + methodName + ")");
        }
        this.mInstance = instance;
        this.mMethodName = methodName;
        this.mMethodParams = params;
    }

    public Object getValue(Object... args) {
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
                Log.w(TAG, "getValue(" + argsString.toString() + ") error! " + this.mInstance.getClass(), e);
            }
        }
        if (!this.mCached) {
            this.mCached = true;
            if (this.mMethod != null) {
                try {
                    this.mValue = this.mMethod.invoke(this.mInstance, args);
                } catch (Exception e2) {
                    Log.w(TAG, "getValue(" + argsString.toString() + ") error!", e2);
                }
            }
        }
        StringBuilder valueBuilder = new StringBuilder();
        if (this.mValue == null) {
            valueBuilder.append("NULL");
        } else {
            valueBuilder.append(this.mValue.getClass().getName()).append("{").append(this.mValue).append("}");
        }
        Log.v(TAG, "<< getValue() mMethodName=" + this.mMethodName + ", mCached=" + this.mCached + ", mMethod=" + this.mMethod + " return " + valueBuilder.toString());
        return this.mValue;
    }
}
