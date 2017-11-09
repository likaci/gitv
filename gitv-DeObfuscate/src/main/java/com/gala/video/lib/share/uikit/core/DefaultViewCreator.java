package com.gala.video.lib.share.uikit.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import java.lang.reflect.InvocationTargetException;

public class DefaultViewCreator<V extends View> {
    protected static final String TAG = "ViewCreator";
    private Class<V> mClz;
    private V view;

    public DefaultViewCreator(Class<V> clz) {
        this.mClz = clz;
    }

    public V create(Context context, ViewGroup parent) {
        try {
            this.view = (View) this.mClz.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            return this.view;
        } catch (InstantiationException e) {
            handleException(e);
            throw new RuntimeException("Failed to create View of class: " + this.mClz.getName());
        } catch (IllegalAccessException e2) {
            handleException(e2);
            throw new RuntimeException("Failed to create View of class: " + this.mClz.getName());
        } catch (InvocationTargetException e3) {
            handleException(e3);
            throw new RuntimeException("Failed to create View of class: " + this.mClz.getName());
        } catch (NoSuchMethodException e4) {
            handleException(e4);
            throw new RuntimeException("Failed to create View of class: " + this.mClz.getName());
        }
    }

    private void handleException(Exception e) {
        throw new RuntimeException(e);
    }
}
