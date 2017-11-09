package com.gala.video.lib.share.uikit.utils;

import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import java.lang.reflect.Method;

public class MethodUtils {
    public static void invoke(Object object, String name, Object... objs) {
        if (object != null) {
            try {
                Class[] argsClass = new Class[objs.length];
                int j = objs.length;
                for (int i = 0; i < j; i++) {
                    if (objs[i] != null) {
                        if (objs[i] instanceof BlocksView) {
                            argsClass[i] = ViewGroup.class;
                        } else if (objs[i] instanceof Integer) {
                            argsClass[i] = Integer.TYPE;
                        } else if (objs[i] instanceof Boolean) {
                            argsClass[i] = Boolean.TYPE;
                        } else if (objs[i] instanceof ViewHolder) {
                            argsClass[i] = ViewHolder.class;
                        } else {
                            argsClass[i] = objs[i].getClass();
                        }
                    }
                }
                Method method = object.getClass().getMethod(name, argsClass);
                if (method.getName().equals(name)) {
                    method.invoke(object, objs);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
