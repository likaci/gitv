package com.tvos.apps.utils.sys;

import android.util.Log;
import com.tvos.apps.utils.ReflectionUtil;

public class ChildrenModeUtils {
    private static final String CLASS_NAME_CHILDRENMODECONFIGMANAGER = "com.nvidia.childrenmode.ChildrenModeConfigManager";
    private static final String TAG = ChildrenModeUtils.class.getSimpleName();

    public static boolean isChildrenModeEnabled() {
        boolean enabled = false;
        try {
            enabled = ((Boolean) ReflectionUtil.invokeStaticMethod(CLASS_NAME_CHILDRENMODECONFIGMANAGER, "IsChildrenModeEnabled", new Object[0])).booleanValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "Children mode " + (enabled ? "is" : "is not") + " enabled.");
        return enabled;
    }

    public static void setGameRestrictStatus(String packageName, boolean isRestricted) {
        Log.d(TAG, "setGameRestrictStatus, packageName = " + packageName + " ; isRestricted = " + isRestricted);
        try {
            Class.forName(CLASS_NAME_CHILDRENMODECONFIGMANAGER).getMethod("setStatus", new Class[]{String.class, Boolean.TYPE}).invoke(null, new Object[]{packageName, Boolean.valueOf(isRestricted)});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isGameRestricted(String packageName) {
        boolean enabled = false;
        if (isChildrenModeEnabled()) {
            try {
                enabled = ((Boolean) ReflectionUtil.invokeStaticMethod(CLASS_NAME_CHILDRENMODECONFIGMANAGER, "getStatus", new Object[]{packageName})).booleanValue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "isGameRestricted" + (enabled ? "is" : "is not") + " enabled.");
        return enabled;
    }
}
