package com.gala.sdk.plugin.server.utils;

import java.lang.reflect.Field;

public class ObjectFiledHolder<T> {
    private Field mField;
    private String mFieldName;
    private boolean mIsInited;
    private Object mObject;

    public ObjectFiledHolder(Object object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException("obj cannot be null");
        }
        this.mObject = object;
        this.mFieldName = fieldName;
    }

    private void prepare() {
        if (!this.mIsInited) {
            this.mIsInited = true;
            Class<?> c = this.mObject.getClass();
            while (c != null) {
                try {
                    Field f = c.getDeclaredField(this.mFieldName);
                    f.setAccessible(true);
                    this.mField = f;
                    c = c.getSuperclass();
                    return;
                } catch (Exception e) {
                    c = c.getSuperclass();
                } catch (Throwable th) {
                    c = c.getSuperclass();
                }
            }
        }
    }

    public T get() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (this.mField == null) {
            throw new NoSuchFieldException("get() fail! object=" + this.mObject + ", field=" + this.mFieldName);
        }
        try {
            return this.mField.get(this.mObject);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
    }

    public void set(T value) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();
        if (this.mField == null) {
            throw new NoSuchFieldException();
        }
        this.mField.set(this.mObject, value);
    }
}
