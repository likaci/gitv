package com.gala.video.lib.share.common.configs;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Process;
import android.text.TextUtils;
import android.view.View;
import com.gala.video.BuildConfig;
import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.util.List;

public final class AppClientUtils {
    private static final String BUILD_TOOL = "BUILD_TOOL";
    private static final String DEBUG = "DEBUG";
    private static final String[] ERROR_CODES = new String[]{"100", "201", "201", "202", "204", "315001", "201", "201", "201", "201", "315001", "315001", "315001", "315001", "315001", "315001", "100", "315002", "315001", "315001", "315002", "315001", "315001", "100", "315003", "315002", "315002", "201", "315003", "315003", "315003", "315003", "315003", "315003", "315003", "315003", "315003", "202"};
    private static final String[] RESPONSE_CODES = new String[]{"E000000", "E000006", "E000007", "E000011", "E000012", "E000013", "E000020", "E000021", "E000022", "E000023", "E000025", "E000026", "E000027", "E000028", "E000029", "E000030", "E000031", "E000033", "E000034", "E000035", "E000036", "E000038", "E000039", "E000040", "E000041", "E000042", "E000044", "E000048", "E000057", "E000058", "E000059", "E000060", "E000061", "E000062", "E000063", "E000064", "E000065", "E000066"};
    private static final String TAG = "SysUtils";

    public enum BuildTool {
        ANT,
        GRADLE
    }

    public static void exitApp() {
        Process.killProcess(Process.myPid());
    }

    public static String getClientVersion() {
        return Project.getInstance().getBuild().getVersionString();
    }

    public static String getErrorCode(String key) {
        if (TextUtils.isEmpty(key)) {
            return "";
        }
        for (int i = 0; i < RESPONSE_CODES.length; i++) {
            if (key.equals(RESPONSE_CODES[i])) {
                return ERROR_CODES[i];
            }
        }
        return "";
    }

    public static String getResourcePkgName() {
        return getBuildTool() == BuildTool.ANT ? AppEnvConstant.DEF_PKG_NAME : AppRuntimeEnv.get().getApplicationContext().getApplicationInfo().packageName;
    }

    public static boolean isDebugMode() {
        boolean isDebug = false;
        try {
            isDebug = Class.forName("com.gala.video.BuildConfig").getField(DEBUG).getBoolean(null);
        } catch (Exception e) {
            LogUtils.i(TAG, "isDebugMode", e);
        }
        return isDebug;
    }

    private static BuildTool getBuildTool() {
        BuildTool buildTool = BuildTool.ANT;
        try {
            if (BuildConfig.BUILD_TOOL.equals(Class.forName("com.gala.video.BuildConfig").getField(BUILD_TOOL).get(null).toString())) {
                return BuildTool.GRADLE;
            }
            return buildTool;
        } catch (Exception e) {
            LogUtils.i(TAG, "getBuildTool", e);
            return buildTool;
        }
    }

    public static String getVersion(Context context, boolean isShow, String tvInternalVersion, String thirdVersion, String minVersion) {
        if (context == null) {
            try {
                throw new NullPointerException("mContext is null.");
            } catch (NameNotFoundException e) {
                throw new RuntimeException("getVersionString()" + e.toString());
            }
        }
        String versionName;
        if (isShow) {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } else {
            versionName = tvInternalVersion;
        }
        return versionName + "." + thirdVersion + "." + minVersion;
    }

    public static String getVersionHeader() {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        if (context == null) {
            try {
                throw new NullPointerException("mContext is null.");
            } catch (NameNotFoundException e) {
                throw new RuntimeException("getVersionString()" + e.toString());
            }
        }
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
    }

    public static String getCookie(Context context) {
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(context)) {
            return GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
        }
        return AppRuntimeEnv.get().getDefaultUserId();
    }

    public static boolean isSupportDolby() {
        return Project.getInstance().getConfig().isEnableDolby() && SystemConfigPreference.isPlayerCoreSupportDolby(AppRuntimeEnv.get().getApplicationContext());
    }

    public static boolean isSupportH265() {
        return Project.getInstance().getBuild().isEnableH265() && SystemConfigPreference.isPlayerCoreSupportH265(AppRuntimeEnv.get().getApplicationContext());
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (view != null) {
            if (VERSION.SDK_INT < 16) {
                view.setBackgroundDrawable(drawable);
            } else {
                view.setBackground(drawable);
            }
        }
    }

    public static boolean isBaseActivity(Context context, String className) {
        boolean ret = false;
        ActivityManager manager = (ActivityManager) context.getSystemService("activity");
        List curTask = manager.getRunningTasks(1);
        List<RunningTaskInfo> allTasks = manager.getRunningTasks(10);
        LogRecordUtils.logd(TAG, "isBaseActivity curTask=" + curTask);
        LogRecordUtils.logd(TAG, "isBaseActivity allTasks=" + allTasks);
        if (!ListUtils.isEmpty(curTask)) {
            String baseClassName = ((RunningTaskInfo) curTask.get(0)).baseActivity.getClassName();
            ret = StringUtils.equals(baseClassName, className);
            LogRecordUtils.logd(TAG, "isBaseActivity ret=" + ret + ", baseClassName=" + baseClassName + ", className=" + className);
        }
        LogRecordUtils.logd(TAG, "isBaseActivity ret=" + ret);
        return ret;
    }
}
