package com.tvos.apps.utils;

import android.content.Context;

public class ResourcesUtils {
    public static final String ANIM = "anim";
    public static final String ARRAY = "array";
    public static final String ATTR = "attr";
    public static final String COLOR = "color";
    public static final String DIMEN = "dimen";
    public static final String DRAWABLE = "drawable";
    public static final String ID = "id";
    public static final String LAYOUT = "layout";
    public static final String STRING = "string";
    public static final String STYLE = "style";

    public static int getResourceId(Context context, String className, String name) {
        try {
            Class requiredClass = null;
            for (Class clazz : Class.forName(context.getPackageName() + ".R").getClasses()) {
                if (clazz.getSimpleName().equals(className)) {
                    requiredClass = clazz;
                    break;
                }
            }
            if (requiredClass == null || name == null) {
                return 0;
            }
            return requiredClass.getField(name).getInt(requiredClass);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getString(Context context, String resName) {
        return context.getResources().getString(getResourceId(context, "string", resName));
    }
}
