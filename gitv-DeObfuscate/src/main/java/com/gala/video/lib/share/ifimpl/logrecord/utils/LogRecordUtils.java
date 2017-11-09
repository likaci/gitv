package com.gala.video.lib.share.ifimpl.logrecord.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.gala.tvapi.log.TVApiLogModel;
import com.gala.tvapi.log.TVApiRecordLog;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.preference.LogRecordPreference;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.webview.utils.WebSDKConstants;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class LogRecordUtils {
    private static final int API_MAX_LENGTH = 204800;
    public static final String EXCEPTION_F00001 = "F00001";
    public static final String EXCEPTION_F00002 = "F00002";
    public static final String EXCEPTION_F00003 = "F00003";
    public static final String EXCEPTION_F10000 = "F10000";
    private static final String MSG_LOGRECORD_NOT_READY = "暂时无法反馈，请稍后再试~";
    public static final String PINGBACK_EC = "315011";
    private static String PUBLIC_IP_NAME = "save_public_ip";
    private static String SETTING_ABOUT_FILE = "about_setting_device";
    private static final String TAG = "LogRecordUtils";
    public static int errUrlLength = 100;
    private static boolean sIsLogRecordInit = false;
    private static String sMsg = "";

    public enum SenderType {
        SendFeedback,
        SendTracker
    }

    public static void logd(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(tag, msg);
        }
    }

    public static void loge(String tag, String msg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(tag, msg);
        }
    }

    public static String getEventID() {
        return LogRecordPreference.getEventId(AppRuntimeEnv.get().getApplicationContext());
    }

    public static void setEventID(String eventID) {
        LogRecordPreference.saveEventId(AppRuntimeEnv.get().getApplicationContext(), eventID);
    }

    public static String getLastApkVersion() {
        String apkVersion = LogRecordPreference.getLastApkVersion(AppRuntimeEnv.get().getApplicationContext());
        if (StringUtils.isEmpty((CharSequence) apkVersion)) {
            apkVersion = Project.getInstance().getBuild().getVersionString();
        }
        Log.d(TAG, "last apk version = " + apkVersion);
        return apkVersion;
    }

    public static void saveLastApkVersion(String apkversion) {
        Log.d(TAG, "save last apk version = " + apkversion);
        LogRecordPreference.saveLastApkVersion(AppRuntimeEnv.get().getApplicationContext(), apkversion);
    }

    public static String getPublicIp(Context mContext) {
        CharSequence ip = new AppPreference(mContext, SETTING_ABOUT_FILE).get(PUBLIC_IP_NAME, "");
        return StringUtils.isEmpty(ip) ? AppRuntimeEnv.get().getDeviceIp() : ip;
    }

    public static String getTVApiRecord() {
        String apiRecordLog = "";
        StringBuffer sb = new StringBuffer();
        for (TVApiLogModel model : TVApiRecordLog.getTVApiRecordLogList()) {
            sb.append(model.getUrl() + "\n");
            sb.append(model.getResponse() + "\n");
        }
        apiRecordLog = sb.toString();
        Log.d(TAG, " api record length = " + apiRecordLog.length());
        if (apiRecordLog.length() > API_MAX_LENGTH) {
            return apiRecordLog.substring(0, API_MAX_LENGTH);
        }
        return apiRecordLog;
    }

    public static String getDevicesInfo(Context mContext) {
        String ipAddress = getPublicIp(mContext);
        String macAddr = DeviceUtils.getMacAddr();
        String deviceModel = Build.MODEL;
        String version = Project.getInstance().getBuild().getVersionString();
        String vrsUUID = Project.getInstance().getBuild().getVrsUUID();
        String clientVersion = AppClientUtils.getClientVersion();
        String playerModulesVersion = new AppPreference(mContext, "module_version").get("module_version");
        StringBuilder sb = new StringBuilder();
        sb.append("IpAddress:" + ipAddress).append("\n");
        sb.append("MACAddress:" + macAddr).append("\n");
        sb.append("DeviceModel:" + deviceModel).append("\n");
        sb.append("Version:" + version).append("\n");
        sb.append("VrsUUID:" + vrsUUID).append("\n");
        sb.append("ClientVersion:" + clientVersion).append("\n");
        sb.append("PlayerModulesVersion:" + playerModulesVersion).append("\n");
        return sb.toString();
    }

    public static String getDevicesInfoForQR(Context mContext) {
        String ipAddress = getPublicIp(mContext);
        String macAddr = DeviceUtils.getMacAddr();
        String deviceModel = Build.MODEL.toString();
        String version = Project.getInstance().getBuild().getVersionString();
        String vrsUUID = Project.getInstance().getBuild().getVrsUUID();
        String clientVersion = AppClientUtils.getClientVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("IpAddress:" + ipAddress).append(",\n");
        sb.append("MACAddress:" + macAddr).append(",\n");
        sb.append("DeviceModel:" + deviceModel).append(",\n");
        sb.append("Version:" + version).append(",\n");
        sb.append("VrsUUID:" + vrsUUID).append(",\n");
        sb.append("ClientVersion:" + clientVersion);
        return sb.toString();
    }

    public static Map<String, String> getDevicesInfoForQR(Map<String, String> map, Context mContext) {
        map.put(WebSDKConstants.PARAM_KEY_UID, GetInterfaceTools.getIGalaAccountManager().getUID());
        map.put(WebConstants.USER_TYPE, GetInterfaceTools.getIGalaAccountManager().getUserTypeForH5() + "");
        map.put(WebConstants.IS_LITCHI, GetInterfaceTools.getIGalaAccountManager().getIsLitchiVipForH5() + "");
        map.put("uuid", Project.getInstance().getBuild().getVrsUUID());
        map.put(WebSDKConstants.PARAM_KEY_P2, Project.getInstance().getBuild().getPingbackP2());
        map.put(WebSDKConstants.PARAM_KEY_HWVER, Build.MODEL.replace(" ", "-"));
        map.put(WebSDKConstants.PARAM_KEY_MAC, DeviceUtils.getMacAddr());
        map.put(WebConstants.AV, Project.getInstance().getBuild().getVersionString());
        return map;
    }

    public static String getVersionCode() {
        StringBuffer sb = new StringBuffer(Project.getInstance().getBuild().getVersionString());
        CharSequence uuid = Project.getInstance().getBuild().getVrsUUID();
        if (!StringUtils.isEmpty(uuid)) {
            int length = uuid.length();
            if (length >= 5) {
                sb.append("(").append(uuid.substring(length - 5, length)).append(")");
            }
        }
        return sb.toString();
    }

    public static String getFeedbackUrl(Map<String, String> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://cms.ptqy.gitv.tv/common/tv/feedback/feedback.html").append("?");
        for (Entry<String, String> entry : map.entrySet()) {
            sb.append((String) entry.getKey());
            sb.append(SearchCriteria.EQ);
            sb.append(URLEncoder.encode((String) entry.getValue()));
            sb.append("&");
        }
        Log.d(TAG, "feedback url =  " + sb.toString());
        return sb.toString().replaceAll("\\+", "%20");
    }

    public static void showLogRecordNotAlreadyToast(final Context context) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                QToast.makeTextAndShow(context, LogRecordUtils.MSG_LOGRECORD_NOT_READY, 2000);
            }
        });
    }

    public static String getMsg() {
        String result = sMsg;
        sMsg = "";
        return result;
    }

    public static void setMsg(String msg) {
        sMsg = msg;
    }

    public static boolean isLogRecordInit() {
        return sIsLogRecordInit;
    }

    public static void setLogRecordInit(boolean isLogRecordInit) {
        sIsLogRecordInit = isLogRecordInit;
    }
}
