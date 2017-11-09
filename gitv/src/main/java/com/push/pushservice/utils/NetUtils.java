package com.push.pushservice.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.webview.utils.WebSDKConstants;
import com.mcto.ads.internal.net.PingbackConstants;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.net.HttpUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class NetUtils {
    private static final String TAG = "NetUtils";

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return true;
        }
        NetworkInfo info = getNetworkInfo(context);
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    public static boolean isWifi(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || info.getType() != 1) {
            return false;
        }
        return true;
    }

    public static boolean isMobile(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info == null || info.getType() != 0) {
            return false;
        }
        return true;
    }

    private static NetworkInfo getNetworkInfo(Context context) {
        return ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
    }

    public static int getNetType(Context context) {
        if (context == null) {
            return 4;
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        if (connMgr == null) {
            return 4;
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            LogUtils.logd(TAG, "networkInfo == null return GPRS type");
            return 4;
        }
        int nType = networkInfo.getType();
        if (nType == 1) {
            return 1;
        }
        if (nType == 9) {
            return 5;
        }
        switch (networkInfo.getSubtype()) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 4;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 2;
            case 13:
                return 3;
            default:
                return 4;
        }
    }

    public static int getDetailNetType(Context ctx) {
        if (ctx == null) {
            return -1;
        }
        Context context = ctx.getApplicationContext();
        if (context == null) {
            return -1;
        }
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService("connectivity");
        if (connMgr == null) {
            return -1;
        }
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return 4;
        }
        int nType = networkInfo.getType();
        if (nType == 1) {
            return 1;
        }
        if (nType == 9) {
            return 13;
        }
        switch (networkInfo.getSubtype()) {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 8;
            case 5:
                return 9;
            case 6:
                return 10;
            case 7:
                return 11;
            case 8:
                return 5;
            case 9:
                return 6;
            case 10:
                return 7;
            case 11:
                return 4;
            case 12:
            case 14:
            case 15:
                return 12;
            case 13:
                return 14;
            default:
                return -1;
        }
    }

    public static boolean getPushType(Context context, String deviceId, String pushDeviceId, String clientId, String userId, int keplerAppId, String appVer, int local, int platForm) {
        List params = new ArrayList();
        params.add(new BasicNameValuePair(WebSDKConstants.PARAM_KEY_DEVICEID, DataUtil.getNotNullString(deviceId)));
        String str = "push_token";
        params.add(new BasicNameValuePair(str, "1_" + DataUtil.getNotNullString(DataUtil.getUtf8String(deviceId))));
        params.add(new BasicNameValuePair("push_type", String.valueOf(1)));
        params.add(new BasicNameValuePair(Keys.PLATFORM, String.valueOf(platForm)));
        params.add(new BasicNameValuePair("clientId", DataUtil.getNotNullString(clientId)));
        params.add(new BasicNameValuePair(WebSDKConstants.PARAM_KEY_UID, DataUtil.getNotNullString(userId)));
        params.add(new BasicNameValuePair("version", DataUtil.getNotNullString(appVer)));
        params.add(new BasicNameValuePair("os", VERSION.RELEASE));
        params.add(new BasicNameValuePair("ua", Build.MODEL));
        params.add(new BasicNameValuePair("network", String.valueOf(getDetailNetType(context))));
        params.add(new BasicNameValuePair(DataConst.APP_INFO_APP_ID, String.valueOf(keplerAppId)));
        params.add(new BasicNameValuePair("local", String.valueOf(local)));
        params.add(new BasicNameValuePair("sign", DataUtil.getNotNullString(SecretKeyUtils.sign(params, PushUtils.getSecretKey(keplerAppId)))));
        String result = HttpUtils.doHttpsGetRequestForString(DataConst.YINHE_SERVER_GET_PUSH_TYPE_URL, params);
        LogUtils.logd("getPushType result = " + result);
        if (TextUtils.isEmpty(result)) {
            LogUtils.logd("getPushType result null type = -1");
            return false;
        }
        try {
            JSONObject json = new JSONObject(result);
            if (TextUtils.equals(json.optString(PingbackConstants.CODE), IAlbumConfig.NET_ERROE_CODE)) {
                String data = json.optString("data");
                if (TextUtils.isEmpty(data)) {
                    LogUtils.logd("getPushType data null type = -1");
                    return false;
                }
                int type = new JSONObject(data).optInt("type", 0);
                boolean isPushType = isPushType(type);
                LogUtils.logd("getPushType type = " + type + " isPushType = " + isPushType);
                return isPushType;
            }
            LogUtils.logd("getPushType code error type = -1");
            return false;
        } catch (JSONException e) {
            LogUtils.logd("getPushType JSONException error");
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            LogUtils.logd("getPushType e = " + e2);
            e2.printStackTrace();
            return false;
        }
    }

    public static boolean isPushType(int pushType) {
        if ((pushType & 1) == 1) {
            return true;
        }
        return false;
    }
}
