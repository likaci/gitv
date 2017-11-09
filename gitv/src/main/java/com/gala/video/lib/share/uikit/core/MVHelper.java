package com.gala.video.lib.share.uikit.core;

import android.view.View;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.protocol.ServiceManager;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MVHelper {
    protected static final String TAG = "Tangram-MVHelper";
    private HashMap<Class<? extends View>, Method> mBindMap = new HashMap(128);
    private HashMap<Class<? extends View>, Method> mHideMap = new HashMap(128);
    private HashMap<Class<? extends View>, Method> mShowMap = new HashMap(128);
    private List<Class<? extends View>> mTypeViewMap = new ArrayList(64);
    private HashMap<Class<? extends View>, Method> mUnBindMap = new HashMap(128);

    public void register(Class<? extends View> viewClz) {
        if (!this.mTypeViewMap.contains(viewClz)) {
            this.mTypeViewMap.add(viewClz);
            loadMethod(viewClz);
        }
    }

    private void loadMethod(Class<? extends View> viewClz) {
        for (Method method : viewClz.getDeclaredMethods()) {
            if (method.getName().equals("bind")) {
                this.mBindMap.put(viewClz, method);
            } else if (method.getName().equals("unbind")) {
                this.mUnBindMap.put(viewClz, method);
            } else if (method.getName().equals(DBColumns.IS_NEED_SHOW)) {
                this.mShowMap.put(viewClz, method);
            } else if (method.getName().equals("hide")) {
                this.mHideMap.put(viewClz, method);
            }
        }
    }

    public boolean isValid(Item item, ServiceManager serviceManager) {
        return true;
    }

    public <T> void bindView(T item, View view) {
        if (view instanceof IViewLifecycle) {
            ((IViewLifecycle) view).onBind(item);
        } else {
            invoke((Method) this.mBindMap.get(view.getClass()), item, view);
        }
    }

    public <T> void unbindView(T item, View view) {
        if (view instanceof IViewLifecycle) {
            ((IViewLifecycle) view).onUnbind(item);
        } else {
            invoke((Method) this.mUnBindMap.get(view.getClass()), item, view);
        }
    }

    public <T> void showView(T item, View view) {
        if (view instanceof IViewLifecycle) {
            ((IViewLifecycle) view).onShow(item);
        } else {
            invoke((Method) this.mShowMap.get(view.getClass()), item, view);
        }
    }

    public <T> void hideView(T item, View view) {
        if (view instanceof IViewLifecycle) {
            ((IViewLifecycle) view).onHide(item);
        } else {
            invoke((Method) this.mShowMap.get(view.getClass()), item, view);
        }
    }

    private <T> void invoke(Method method, T item, View view) {
        if (method != null) {
            try {
                method.invoke(view, new Object[]{item});
            } catch (Exception e) {
                e.printStackTrace();
                throw new IllegalArgumentException("invoke,method" + method + ",item" + item + ",view=" + view);
            }
        }
    }
}
