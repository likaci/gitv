package com.push.pushservice.pingback;

import android.os.Build;
import android.os.Build.VERSION;
import com.gala.sdk.player.Locale;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.webview.utils.WebSDKConstants;
import com.push.pushservice.BuildConfig;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.net.HttpUtils;
import com.push.pushservice.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class PingBackAgent {
    public static final int MSG_KEEPLIVE = -4;
    public static final int MSG_NULL = -2;
    public static final int MSG_OK = 0;
    public static final int MSG_SYS = -3;
    private static final String TAG = "PingBackAgent";
    public static final int TOPIC_NULL = -1;

    private static void sendHostListStatistics(String errorCode, String deviceId, String globalDeviceId) {
        try {
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair(Keys.T, "relist"));
            params.add(new BasicNameValuePair("p1", "3_31_312"));
            params.add(new BasicNameValuePair("u", deviceId));
            params.add(new BasicNameValuePair("pushv", BuildConfig.SDK_VERSION));
            params.add(new BasicNameValuePair("result", errorCode));
            params.add(new BasicNameValuePair("pu", ""));
            params.add(new BasicNameValuePair("clientmd", Build.MODEL));
            String url = HttpUtils.appendParams(DataConst.PINGBACK_ADDRESS, params);
            LogUtils.logd(TAG, "sendHostListStatistics url = " + url);
            LogUtils.logd(TAG, "sendHostListStatistics res = " + HttpUtils.doGetRequestForString(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendConnectionStatistics(String errorCode, String deviceId, String globalDeviceId) {
        try {
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair(Keys.T, "pushcnt"));
            params.add(new BasicNameValuePair("p1", "3_31_312"));
            params.add(new BasicNameValuePair("u", deviceId));
            params.add(new BasicNameValuePair("pu", ""));
            params.add(new BasicNameValuePair("pushv", BuildConfig.SDK_VERSION));
            params.add(new BasicNameValuePair("result", errorCode));
            params.add(new BasicNameValuePair("clientmd", Build.MODEL));
            String url = HttpUtils.appendParams(DataConst.PINGBACK_ADDRESS, params);
            LogUtils.logd(TAG, "sendConnectionStatistics url = " + url);
            LogUtils.logd(TAG, "sendConnectionStatistics res = " + HttpUtils.doGetRequestForString(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMessageStatistics(int errorCode, String deviceId, int netType, long msgId, String globalDeviceId) {
        try {
            List<NameValuePair> params = new ArrayList();
            params.add(new BasicNameValuePair("p1", "3_31_312"));
            params.add(new BasicNameValuePair("u", deviceId));
            params.add(new BasicNameValuePair("os", VERSION.RELEASE));
            params.add(new BasicNameValuePair("v", ""));
            params.add(new BasicNameValuePair("stime", String.valueOf(System.currentTimeMillis() / 1000)));
            params.add(new BasicNameValuePair("net_work", String.valueOf(netType)));
            params.add(new BasicNameValuePair(WebSDKConstants.PARAM_KEY_MOD, Locale.CHINESE_SIMPLIFIED));
            params.add(new BasicNameValuePair("ua_model", Build.MODEL));
            params.add(new BasicNameValuePair("sdk__ver", BuildConfig.SDK_VERSION));
            params.add(new BasicNameValuePair(Keys.MSG_ID, String.valueOf(msgId)));
            params.add(new BasicNameValuePair("reason", String.valueOf(errorCode)));
            String url = HttpUtils.appendParams(DataConst.MESSAGE_PINGBACK_ADDRESS, params);
            LogUtils.logd(TAG, "sendMessageStatistics url = " + url);
            LogUtils.logd(TAG, "sendMessageStatistics res = " + HttpUtils.doGetRequestForString(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendMessageStatisticsAsync(int errorCode, String deviceId, int netType, long msgId, String globalDeviceId) {
        final int i = errorCode;
        final String str = deviceId;
        final int i2 = netType;
        final long j = msgId;
        final String str2 = globalDeviceId;
        new Thread() {
            public void run() {
                PingBackAgent.sendMessageStatistics(i, str, i2, j, str2);
            }
        }.start();
    }

    public static void sendHostListStatisticsAsync(final String errorCode, final String deviceId, final String globalDeviceId) {
        new Thread() {
            public void run() {
                PingBackAgent.sendHostListStatistics(errorCode, deviceId, globalDeviceId);
            }
        }.start();
    }

    public static void sendConnectionStatisticsAsync(final String errorCode, final String deviceId, final String globalDeviceId) {
        new Thread() {
            public void run() {
                PingBackAgent.sendConnectionStatistics(errorCode, deviceId, globalDeviceId);
            }
        }.start();
    }
}
