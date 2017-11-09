package com.gala.afinal;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import com.gala.afinal.annotation.view.EventListener;
import com.gala.afinal.annotation.view.Select;
import com.gala.afinal.annotation.view.ViewInject;
import java.lang.reflect.Field;

public abstract class FinalActivity extends Activity {

    public enum Method {
        Click,
        LongClick,
        ItemClick,
        itemLongClick
    }

    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initInjectedView(this);
    }

    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        initInjectedView(this);
    }

    public void setContentView(View view) {
        super.setContentView(view);
        initInjectedView(this);
    }

    public static void initInjectedView(Activity activity) {
        initInjectedView(activity, activity.getWindow().getDecorView());
    }

    public static void initInjectedView(Object injectedSource, View sourceView) {
        Field[] declaredFields = injectedSource.getClass().getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            for (Field field : declaredFields) {
                try {
                    field.setAccessible(true);
                    if (field.get(injectedSource) == null) {
                        ViewInject viewInject = (ViewInject) field.getAnnotation(ViewInject.class);
                        if (viewInject != null) {
                            field.set(injectedSource, sourceView.findViewById(viewInject.id()));
                            setListener(injectedSource, field, viewInject.click(), Method.Click);
                            setListener(injectedSource, field, viewInject.longClick(), Method.LongClick);
                            setListener(injectedSource, field, viewInject.itemClick(), Method.ItemClick);
                            setListener(injectedSource, field, viewInject.itemLongClick(), Method.itemLongClick);
                            Select select = viewInject.select();
                            if (!TextUtils.isEmpty(select.selected())) {
                                setViewSelectListener(injectedSource, field, select.selected(), select.noSelected());
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void setViewSelectListener(Object injectedSource, Field field, String select, String noSelect) throws Exception {
        Object obj = field.get(injectedSource);
        if (obj instanceof View) {
            ((AbsListView) obj).setOnItemSelectedListener(new EventListener(injectedSource).select(select).noSelect(noSelect));
        }
    }

    private static void setListener(Object injectedSource, Field field, String methodName, Method method) throws Exception {
        if (methodName != null && methodName.trim().length() != 0) {
            Object obj = field.get(injectedSource);
            switch (method) {
                case Click:
                    if (obj instanceof View) {
                        ((View) obj).setOnClickListener(new EventListener(injectedSource).click(methodName));
                        return;
                    }
                    return;
                case ItemClick:
                    if (obj instanceof AbsListView) {
                        ((AbsListView) obj).setOnItemClickListener(new EventListener(injectedSource).itemClick(methodName));
                        return;
                    }
                    return;
                case LongClick:
                    if (obj instanceof View) {
                        ((View) obj).setOnLongClickListener(new EventListener(injectedSource).longClick(methodName));
                        return;
                    }
                    return;
                case itemLongClick:
                    if (obj instanceof AbsListView) {
                        ((AbsListView) obj).setOnItemLongClickListener(new EventListener(injectedSource).itemLongClick(methodName));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
