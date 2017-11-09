package com.gala.afinal.annotation.view;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import com.gala.afinal.exception.ViewException;
import java.lang.reflect.Method;

public class EventListener implements OnClickListener, OnLongClickListener, OnItemClickListener, OnItemLongClickListener, OnItemSelectedListener {
    private String clickMethod;
    private Object handler;
    private String itemClickMethod;
    private String itemLongClickMehtod;
    private String itemSelectMethod;
    private String longClickMethod;
    private String nothingSelectedMethod;

    public EventListener(Object handler) {
        this.handler = handler;
    }

    public EventListener click(String method) {
        this.clickMethod = method;
        return this;
    }

    public EventListener longClick(String method) {
        this.longClickMethod = method;
        return this;
    }

    public EventListener itemLongClick(String method) {
        this.itemLongClickMehtod = method;
        return this;
    }

    public EventListener itemClick(String method) {
        this.itemClickMethod = method;
        return this;
    }

    public EventListener select(String method) {
        this.itemSelectMethod = method;
        return this;
    }

    public EventListener noSelect(String method) {
        this.nothingSelectedMethod = method;
        return this;
    }

    public boolean onLongClick(View v) {
        return invokeLongClickMethod(this.handler, this.longClickMethod, v);
    }

    public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        return invokeItemLongClickMethod(this.handler, this.itemLongClickMehtod, arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3));
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        invokeItemSelectMethod(this.handler, this.itemSelectMethod, arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3));
    }

    public void onNothingSelected(AdapterView<?> arg0) {
        invokeNoSelectMethod(this.handler, this.nothingSelectedMethod, arg0);
    }

    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        invokeItemClickMethod(this.handler, this.itemClickMethod, arg0, arg1, Integer.valueOf(arg2), Long.valueOf(arg3));
    }

    public void onClick(View v) {
        invokeClickMethod(this.handler, this.clickMethod, v);
    }

    private static Object invokeClickMethod(Object handler, String methodName, Object... params) {
        if (handler == null) {
            return null;
        }
        try {
            Method declaredMethod = handler.getClass().getDeclaredMethod(methodName, new Class[]{View.class});
            if (declaredMethod != null) {
                return declaredMethod.invoke(handler, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean invokeLongClickMethod(Object handler, String methodName, Object... params) {
        if (handler == null) {
            return false;
        }
        try {
            Method declaredMethod = handler.getClass().getDeclaredMethod(methodName, new Class[]{View.class});
            if (declaredMethod != null) {
                Object invoke = declaredMethod.invoke(handler, params);
                if (invoke != null) {
                    return Boolean.valueOf(invoke.toString()).booleanValue();
                }
                return false;
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Object invokeItemClickMethod(Object handler, String methodName, Object... params) {
        if (handler == null) {
            return null;
        }
        try {
            Method declaredMethod = handler.getClass().getDeclaredMethod(methodName, new Class[]{AdapterView.class, View.class, Integer.TYPE, Long.TYPE});
            if (declaredMethod != null) {
                return declaredMethod.invoke(handler, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean invokeItemLongClickMethod(Object handler, String methodName, Object... params) {
        if (handler == null) {
            throw new ViewException("invokeItemLongClickMethod: handler is null :");
        }
        try {
            Method declaredMethod = handler.getClass().getDeclaredMethod(methodName, new Class[]{AdapterView.class, View.class, Integer.TYPE, Long.TYPE});
            if (declaredMethod != null) {
                Object invoke = declaredMethod.invoke(handler, params);
                return Boolean.valueOf(invoke == null ? false : Boolean.valueOf(invoke.toString()).booleanValue()).booleanValue();
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static Object invokeItemSelectMethod(Object handler, String methodName, Object... params) {
        if (handler == null) {
            return null;
        }
        try {
            Method declaredMethod = handler.getClass().getDeclaredMethod(methodName, new Class[]{AdapterView.class, View.class, Integer.TYPE, Long.TYPE});
            if (declaredMethod != null) {
                return declaredMethod.invoke(handler, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object invokeNoSelectMethod(Object handler, String methodName, Object... params) {
        if (handler == null) {
            return null;
        }
        try {
            Method declaredMethod = handler.getClass().getDeclaredMethod(methodName, new Class[]{AdapterView.class});
            if (declaredMethod != null) {
                return declaredMethod.invoke(handler, params);
            }
            throw new ViewException("no such method:" + methodName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
