package com.gala.video.lib.share.ifimpl.imsg.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.SystemClock;
import android.provider.BaseColumns;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.TVApiConfig;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.property.TVApiProperty;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ICommonApiCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.configs.ServerConfig;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class IMsgUtils {
    public static final String ACTION_FALSE = "false";
    public static final String ACTION_FROM_APP = "showDiolog";
    public static final String ACTION_START = "start";
    public static final String ACTION_TRUE = "true";
    public static final long DEFAULT_INVALID_TIME = 1296000000;
    public static final int DEFAULT_NO_IMSG = -100;
    public static final String IMSG_APPID = "appid";
    public static final String IMSG_CONTENT = "content";
    public static final String IMSG_FILTER = "com.tv.system.imsg.action.MESSAGE";
    public static final String IMSG_TYPE = "type";
    public static final String PUSH_SERVICE_NAME = ".pushdaemonservice";
    public static final String TAG = "imsg/IMsgCenter";
    public static final int TYPE_LOG = 61;
    public static final int TYPE_NORMAL = 39;
    public static final int TYPE_REMIND = 60;
    private static boolean isShowDialog = true;
    public static boolean isSupportSubscribe = true;
    public static short sAppId;
    public static String sAppVersion = "7.10";
    public static Context sContext = AppRuntimeEnv.get().getApplicationContext();
    public static String sPkgName;

    public static class DBColumns implements BaseColumns {
        public static final String AID = "aid";
        public static final int AID_INDEX = 13;
        public static final String ALBUM = "album";
        public static final int ALBUM_INDEX = 23;
        public static final String ASC_SORT_ORDER = "_id asc";
        public static final String BUTTON_NAME = "bname";
        public static final int BUTTON_NAME_INDEX = 8;
        public static final String CHANNEL_ID = "channel";
        public static final int CHANNEL_ID_INDEX = 19;
        public static final String CONTENT = "msgContent";
        public static final int CONTENT_INDEX = 25;
        public static final String COUPON_KEY = "coupon_key";
        public static final int COUPON_KEY_INDEX = 28;
        public static final String COUPON_SIGN = "coupon_sign";
        public static final int COUPON_SIGN_INDEX = 29;
        public static final String DB_NAME = "IMsg.db";
        public static final int DB_VERSION = 9;
        public static final String DEFAULT_SORT_ORDER = "_id desc";
        public static final String DESCRIPTION = "des";
        public static final int DESCRIPTION_INDEX = 7;
        public static final String HURL = "url";
        public static final int HURL_INDEX = 11;
        public static final String IMSG_TABLE_NAME = "imsg";
        public static final String IS_DETAIL = "detail";
        public static final int IS_DETAIL_INDEX = 9;
        public static final String IS_NEED_SHOW = "show";
        public static final int IS_NEED_SHOW_INDEX = 24;
        public static final String IS_READ = "read";
        public static final int IS_READ_INDEX = 15;
        public static final String IS_SERIES = "series";
        public static final int IS_SERIES_INDEX = 17;
        public static final String JUMP = "jump";
        public static final int JUMP_INDEX = 10;
        public static final String LEVEL = "level";
        public static final int LEVEL_INDEX = 3;
        public static final String LOCAL_TIME = "localTime";
        public static final int LOCAL_TIME_INDEX = 22;
        public static final String MIN_VERSION = "version";
        public static final int MIN_VERSION_INDEX = 5;
        public static final String MSG_ID = "MSG_ID";
        public static final int MSG_ID_INDEX = 21;
        public static final String PIC = "pic";
        public static final int PIC_INDEX = 6;
        public static final String PLID = "plid";
        public static final int PLID_INDEX = 12;
        public static final String SOURCE_CODE = "source";
        public static final int SOURCE_CODE_INDEX = 18;
        public static final String STYLE = "style";
        public static final int STYLE_INDEX = 26;
        public static final String TEMPLATE_ID = "tid";
        public static final int TEMPLATE_ID_INDEX = 1;
        public static final String TIME = "time";
        public static final String TITLE = "title";
        public static final int TTITLE_INDEX = 2;
        public static final String TVID = "tvid";
        public static final int TVID_INDEX = 14;
        public static final int TVTYPE_INDEX = 16;
        public static final String TV_TYPE = "tv_type";
        public static final String TYPE = "type";
        public static final int TYPE_INDEX = 4;
        public static final String URL_WINDOW = "url_window";
        public static final int URL_WINDOW_INDEX = 27;
        public static final String VALID_TILL = "valid_till";
        public static final int VALID_TILL_INDEX = 30;
        public static final String VIPINFO = "VIP";
        public static final int VIPINFO_INDEX = 20;
    }

    public static class IMsgType {
        public static final int ALL = 0;
        public static final int OTHER = -1;
        public static final int PROMOTION = 1;
        public static final int RECOMMEND = 2;
        public static final int REMIND = 3;

        public static int getTypeType(int index) {
            switch (index) {
                case 0:
                    return 0;
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                default:
                    return -1;
            }
        }

        public static String getTypeName(int index) {
            int id;
            switch (index) {
                case 0:
                    id = C1632R.string.msg_label1;
                    break;
                case 1:
                    id = C1632R.string.msg_label2;
                    break;
                case 2:
                    id = C1632R.string.msg_label3;
                    break;
                case 3:
                    id = C1632R.string.msg_label4;
                    break;
                default:
                    id = C1632R.string.msg_label4;
                    break;
            }
            return ResourceUtil.getStr(id);
        }
    }

    public static class JumpPage {
        public static final int COUPON = 6;
        public static final int DETAIL = 3;
        public static final int H5 = 1;
        public static final int OTHER = 5;
        public static final int PLAY = 4;
        public static final int PLID = 2;
    }

    public static class ShowType {
        public static final int HORIZONTAL = 1;
        public static final int VERTICAL = 2;
    }

    public static class TemplateId {
        public static final int title_jump = 2;
        public static final int title_pic_des_jump = 1;
    }

    public static boolean isShowDialog() {
        return isShowDialog;
    }

    public static synchronized void setShowDialog(boolean isShowDialog) {
        synchronized (IMsgUtils.class) {
            isShowDialog = isShowDialog;
        }
    }

    public static void init() {
        Log.d("imsg/imsginitutils", InterfaceKey.SHARE_IT);
        initTVApi();
        initPingback(AppRuntimeEnv.get().getApplicationContext());
        getAppVersion(TVApiBase.getTVApiProperty().getVersion());
    }

    private static void initTVApi() {
        TVApiConfig.setDomain(Project.getInstance().getBuild().getDomainName());
        TVApiProperty property = TVApiBase.getTVApiProperty();
        property.setDebugFlag(ServerConfig.isTVApiDebugEnabled());
        property.setOSVersion(VERSION.RELEASE.toString());
        property.setCheckYinHe(Project.getInstance().getBuild().shouldAuthMac());
        property.setContext(AppRuntimeEnv.get().getApplicationContext());
        property.setShowLiveFlag(Project.getInstance().getBuild().isShowLive());
        property.setShowVipFlag(GetInterfaceTools.getIDynamicQDataProvider().isSupportVip());
        property.setCacheDeviceCheckFlag(Project.getInstance().getBuild().shouldCacheDeviceCheck());
        property.setIpAddress(DeviceUtils.getIpAddress());
        property.setHardware(DeviceUtils.getHardwareInfo());
        property.setMemorySize(String.valueOf(DeviceUtils.getTotalMemory()));
        TVApiBase.createRegisterKey(DeviceUtils.getMacAddr(), Project.getInstance().getBuild().getVrsUUID(), Project.getInstance().getBuild().getVersionString());
    }

    private static void initPingback(Context context) {
        PingBackInitParams params = new PingBackInitParams();
        params.sDomain = Project.getInstance().getBuild().getDomainName();
        params.sAppVersion = Project.getInstance().getBuild().getVersionString();
        params.sIsNewUser = SystemConfigPreference.isNewUser(context);
        params.sAnonymityId = TVApiBase.getTVApiProperty().getAnonymity();
        params.sIsSendYinHePingBack = Project.getInstance().getBuild().isLitchi();
        params.sIsDebug = Project.getInstance().getBuild().isPingbackDebug();
        params.sIsSmallWindowDisable = !Project.getInstance().getBuild().isSupportSmallWindowPlay();
        PingBack.getInstance().initialize(context, params);
    }

    public static void getSystem() {
        String url = "http://pdata.video.ptqy.gitv.tv/k";
        Log.d(TAG, "systemTime http://pdata.video.ptqy.gitv.tv/k");
        final long startTime = SystemClock.elapsedRealtime();
        ApiFactory.getCommonApi().call("http://pdata.video.ptqy.gitv.tv/k", new ICommonApiCallback() {
            public void onException(Exception arg0, String arg1) {
            }

            public void onSuccess(String response) {
                if (response != null) {
                    JSONObject object = JSON.parseObject(response);
                    if (object != null) {
                        String time = object.getString(Keys.f2035T);
                        Log.d(IMsgUtils.TAG, "System Time" + time);
                        TVApiBase.getTVApiProperty().setServerTime(Long.parseLong(time) * 1000);
                        TVApiBase.getTVApiProperty().setLaunchTime(SystemClock.elapsedRealtime());
                        Log.d(IMsgUtils.TAG, (SystemClock.elapsedRealtime() - startTime) + response);
                    }
                }
            }
        }, false, "systemTime");
    }

    public static short getAppId(String p2) {
        if (p2.equals("3123")) {
            sAppId = (short) 1020;
        } else if (p2.equals("3121")) {
            sAppId = (short) 1021;
        } else {
            sAppId = (short) 1022;
        }
        return sAppId;
    }

    public static String getAppVersion() {
        return sAppVersion;
    }

    public static String getAppVersion(String version) {
        String[] v = version.split("\\.");
        if (v.length >= 2) {
            sAppVersion = v[0] + "." + v[1];
        } else {
            sAppVersion = "6.5";
        }
        return sAppVersion;
    }

    public static boolean isOutApp(Context context) {
        int pid = Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context.getSystemService("activity");
        if (!ListUtils.isEmpty(mActivityManager.getRunningAppProcesses())) {
            for (RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid && PUSH_SERVICE_NAME.equals(appProcess.processName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getDialogPos(boolean isOutApp) {
        return 4;
    }

    public static boolean isServiceLive(Context context) {
        try {
            for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (PUSH_SERVICE_NAME.equals(appProcess.processName)) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isAppLive(Context context) {
        if (sPkgName == null) {
            return true;
        }
        try {
            for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses()) {
                if (sPkgName.equals(appProcess.processName)) {
                    List<RunningTaskInfo> tasks = ((ActivityManager) context.getSystemService("activity")).getRunningTasks(1);
                    if (tasks.isEmpty()) {
                        continue;
                    } else {
                        ComponentName topActivity = ((RunningTaskInfo) tasks.get(0)).topActivity;
                        Log.d(TAG, "topPkgName = " + topActivity.getPackageName() + ", sPkgName = " + sPkgName);
                        if (topActivity.getPackageName().contains(sPkgName)) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
