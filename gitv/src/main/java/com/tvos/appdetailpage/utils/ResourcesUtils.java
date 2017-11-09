package com.tvos.appdetailpage.utils;

import android.content.Context;
import android.util.Log;
import com.gala.sdk.player.constants.PlayerIntentConfig;
import com.tvos.appdetailpage.ui.AppStoreDetailActivity;
import com.tvos.apps.utils.LogUtils;

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
    private static final String TAG = "ResourcesUtils 1.4.5";
    private static String resourcePkgName = PlayerIntentConfig.URI_AUTH;

    public static void setResourcePkgName(String pkgName) {
        Log.d(TAG, "setResourcePkgName " + pkgName);
        resourcePkgName = pkgName;
    }

    public static int getResourceIdByReflection(Context context, String className, String name) {
        Class requiredClass = null;
        try {
            if (AppStoreDetailActivity.getMixFlag()) {
                requiredClass = Class.forName(resourcePkgName + ".R$" + className);
                LogUtils.e(TAG, "getResourceId: getMixFlag required class = " + requiredClass.getName());
            } else {
                for (Class clazz : Class.forName(resourcePkgName + ".R").getClasses()) {
                    if (clazz.getSimpleName().equals(className)) {
                        LogUtils.e(TAG, "getResourceId: clazz.getSimpleName().equals(className)");
                        requiredClass = clazz;
                        break;
                    }
                    LogUtils.e(TAG, "getResourceId: clazz.getSimpleName() does not equals(className)");
                }
            }
            if (requiredClass == null || name == null) {
                return 0;
            }
            int id = requiredClass.getField(name).getInt(requiredClass);
            LogUtils.e(TAG, "getResourceId: name = " + name + " requiredclass = " + requiredClass.getName() + " id = " + id);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getResourceId(Context context, String type, String name) {
        Log.d(TAG, "getResourceId, type = " + type + " , name = " + name);
        return context.getResources().getIdentifier(name, type, resourcePkgName);
    }
}
