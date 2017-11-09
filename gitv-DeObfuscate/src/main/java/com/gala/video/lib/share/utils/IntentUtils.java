package com.gala.video.lib.share.utils;

import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;

public class IntentUtils {
    private static final String TAG = "IntentUtils";

    public static String getActionName(String oldActionName) {
        LogUtils.m1568d(TAG, "oldActionName=" + oldActionName);
        if (StringUtils.isEmpty((CharSequence) oldActionName)) {
            return "";
        }
        String oldPackageName = AppEnvConstant.DEF_PKG_NAME;
        CharSequence newPackageName = "";
        try {
            newPackageName = Project.getInstance().getBuild().getPackageName();
        } catch (Exception e) {
            LogUtils.m1571e(TAG, e.toString());
        }
        LogUtils.m1568d(TAG, "oldPackageName=" + oldPackageName + ",newPackageName=" + newPackageName);
        if (oldPackageName.equals(newPackageName) || StringUtils.isEmpty(newPackageName)) {
            return oldActionName;
        }
        String newActionName = oldActionName.replace(oldPackageName, newPackageName);
        LogUtils.m1568d(TAG, "newAction=" + newActionName);
        return newActionName;
    }
}
