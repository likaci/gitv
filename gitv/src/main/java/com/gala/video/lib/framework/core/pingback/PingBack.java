package com.gala.video.lib.framework.core.pingback;

import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import android.util.Log;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.IPinbackCallback;
import com.gala.video.api.IPingbackApi;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.UrlUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Random;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class PingBack {
    private static final String TAG = "PingbackLog";
    private static String mTestHost;
    private static final PingBack sPingBack = new PingBack();
    private final String AM71_HOST = "http://msg.71.am/tmpstats.gif?";
    private final String GALA_HOST = "http://msg.igala.com/b?";
    private final String YINHE_PINGBACK_HOST_NAME = "http://pb.bi.gitv.tv/gitv_pb/b?";
    private String mAM71CommonParams = "";
    private String mAnonymityId = "";
    private IPingbackApi mApi = ApiFactory.getPingbackApi();
    private String mAppVersion = "";
    private String mChannel = "";
    private String mCommonParams = "";
    private String mDeviceId = "";
    private String mDisplayMetrics = "";
    private String mDomain = "";
    private String mEnterMode = "0";
    private String mHostVer = "";
    private int mId = 0;
    private boolean mIsDebug = false;
    private boolean mIsNewUser = true;
    private boolean mIsSendYinHePingBack = false;
    private boolean mIsSmallWindowDisable = false;
    private String mIsVipAct = "";
    private String mMacAddress = "";
    private String mMod = "";
    private String mP2 = "";
    private String mUUID = "";

    public static class PingBackInitParams {
        public String sAnonymityId = "";
        public String sAppVersion = "";
        public String sChannel = "";
        public String sDeviceId = "";
        public String sDomain = "";
        public String sEnterMode = "0";
        public String sHostVer = "";
        public boolean sIsDebug = false;
        public boolean sIsNewUser = true;
        public boolean sIsSendYinHePingBack = false;
        public boolean sIsSmallWindowDisable = false;
        public String sIsVipAct = "";
        public String sMod = "";
        public String sP2 = "";
        public String sUUID = "";
    }

    static {
        mTestHost = "10.1.98.58";
        String host = null;
        try {
            host = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop pingback.debughost").getInputStream())).readLine();
            Log.d(TAG, "host:" + host);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (host != null && !"".equals(host)) {
            mTestHost = host;
        }
    }

    private PingBack() {
    }

    public static PingBack getInstance() {
        return sPingBack;
    }

    public void initialize(Context context, PingBackInitParams params) {
        this.mP2 = params.sP2;
        this.mMod = params.sMod;
        this.mUUID = params.sUUID;
        this.mDomain = params.sDomain;
        this.mIsDebug = params.sIsDebug;
        this.mHostVer = params.sHostVer;
        this.mChannel = params.sChannel;
        this.mIsDebug = params.sIsDebug;
        this.mIsVipAct = params.sIsVipAct;
        this.mDeviceId = params.sDeviceId;
        this.mIsNewUser = params.sIsNewUser;
        this.mEnterMode = params.sEnterMode;
        this.mAppVersion = params.sAppVersion;
        this.mAnonymityId = params.sAnonymityId;
        this.mIsSendYinHePingBack = params.sIsSendYinHePingBack;
        this.mIsSmallWindowDisable = params.sIsSmallWindowDisable;
        this.mMacAddress = DeviceUtils.getMacAddr();
        this.mDisplayMetrics = DeviceUtils.getDisplayMetrics(context);
        this.mCommonParams = "";
        this.mAM71CommonParams = "";
    }

    public PingBackInitParams getPingbackInitParams() {
        PingBackInitParams params = new PingBackInitParams();
        params.sP2 = this.mP2;
        params.sMod = this.mMod;
        params.sUUID = this.mUUID;
        params.sDomain = this.mDomain;
        params.sIsDebug = this.mIsDebug;
        params.sHostVer = this.mHostVer;
        params.sChannel = this.mChannel;
        params.sIsDebug = this.mIsDebug;
        params.sIsVipAct = this.mIsVipAct;
        params.sDeviceId = this.mDeviceId;
        params.sIsNewUser = this.mIsNewUser;
        params.sEnterMode = this.mEnterMode;
        params.sAppVersion = this.mAppVersion;
        params.sAnonymityId = this.mAnonymityId;
        params.sIsSendYinHePingBack = this.mIsSendYinHePingBack;
        params.sIsSmallWindowDisable = this.mIsSmallWindowDisable;
        return params;
    }

    public void postPingBackToLongYuan(Map<String, String> pingBackParams) {
        String pingBackUrl = "http://msg.igala.com/b?" + getCommonParams();
        if (this.mIsDebug) {
            pingBackUrl = pingBackUrl.replace("msg.igala.com", mTestHost);
        } else if (this.mDomain.length() > 3 && this.mDomain.charAt(1) == this.mDomain.charAt(3) && this.mDomain.contains("i.com")) {
            pingBackUrl = pingBackUrl.replace("gala.com", this.mDomain);
        } else {
            pingBackUrl = pingBackUrl.replace("igala.com", this.mDomain);
        }
        postPingBack(pingBackUrl, pingBackParams);
    }

    public void postPingBackToAM71(Map<String, String> pingBackParams) {
        String pingBackUrl = "http://msg.71.am/tmpstats.gif?" + getAM71CommonParams();
        if (this.mIsDebug) {
            pingBackUrl = pingBackUrl.replace("msg.71.am", mTestHost);
        } else if (this.mDomain.equals(BuildDefaultDocument.APK_DOMAIN_NAME)) {
            pingBackUrl = pingBackUrl.replace("71.am", this.mDomain);
        }
        postPingBack(pingBackUrl, pingBackParams);
    }

    private void postPingBack(String baseUrl, Map<String, String> pingBackParams) {
        if (this.mId == Integer.MAX_VALUE) {
            this.mId = 0;
        }
        LogUtils.d(TAG, " id = " + this.mId + "-post pingback");
        String pingBackUrl = baseUrl;
        String url = "";
        if (!ListUtils.isEmpty((Map) pingBackParams)) {
            synchronized (pingBackParams) {
                for (String key : pingBackParams.keySet()) {
                    url = url + "&" + key + SearchCriteria.EQ + UrlUtils.urlEncode((String) pingBackParams.get(key));
                }
                pingBackUrl = pingBackUrl + url;
            }
        }
        final String printPingBackUrl = pingBackUrl;
        final int id = this.mId;
        this.mApi.call(new IPinbackCallback() {
            public void onSuccess(String s) {
                LogUtils.d(PingBack.TAG, "id = " + id + "-success:" + printPingBackUrl);
            }

            public void onException(String s, Exception e) {
                LogUtils.e(PingBack.TAG, "id = " + id + "-failed:" + printPingBackUrl);
            }
        }, pingBackUrl);
        if (this.mIsSendYinHePingBack) {
            pingBackUrl = ("http://pb.bi.gitv.tv/gitv_pb/b?" + getCommonParams()) + url;
            final String printYinHePingBackUrl = pingBackUrl;
            this.mApi.call(new IPinbackCallback() {
                public void onSuccess(String s) {
                    LogUtils.d(PingBack.TAG, "id = " + id + "-success:" + printYinHePingBackUrl);
                }

                public void onException(String s, Exception e) {
                    Log.e(PingBack.TAG, "id = " + id + "-failed:" + printYinHePingBackUrl);
                }
            }, pingBackUrl);
        }
        this.mId++;
    }

    public String getCommonParams() {
        if (StringUtils.isEmpty(this.mCommonParams)) {
            StringBuilder builder = new StringBuilder("pf=3&p=31&p1=312&p2=");
            builder.append(this.mP2).append("&mac=").append(UrlUtils.urlEncode(getMacAddress())).append("&u=").append(this.mAnonymityId).append("&deviceid=").append(this.mDeviceId).append("&rn=").append(getSessionId()).append("&nu=").append(this.mIsNewUser ? "1" : "0").append("&v=").append(UrlUtils.urlEncode(this.mAppVersion)).append("&dt=").append(this.mUUID).append("&firmver=").append(UrlUtils.urlEncode(Build.DISPLAY)).append("&hwver=").append(UrlUtils.urlEncode(Build.MODEL.replace(" ", "-"))).append("&window_disable=").append(this.mIsSmallWindowDisable ? "1" : "0").append("&chip=").append(UrlUtils.urlEncode(DeviceUtils.getHardwareInfo())).append("&mod=").append(this.mMod).append("&memory=").append(getMemorySize()).append("&processid=").append(Process.myPid()).append("&re=").append(UrlUtils.urlEncode(this.mDisplayMetrics)).append("&os=").append(String.valueOf(VERSION.SDK_INT)).append("&core=").append(String.valueOf(DeviceUtils.getCpuCoreNums())).append("&channel=").append(this.mChannel).append("&entermode=").append(this.mEnterMode).append("&hostv=").append(this.mHostVer);
            this.mCommonParams = builder.toString();
        }
        return this.mCommonParams;
    }

    private String getAM71CommonParams() {
        if (StringUtils.isEmpty(this.mAM71CommonParams)) {
            StringBuilder builder = new StringBuilder("chip=");
            builder.append(UrlUtils.urlEncode(DeviceUtils.getHardwareInfo())).append("&mod=").append(this.mMod).append("&memory=").append(getMemorySize()).append("&deviceid=").append(this.mDeviceId).append("&uid=").append(this.mAnonymityId).append("&v=").append(UrlUtils.urlEncode(this.mAppVersion)).append("&hwver=").append(UrlUtils.urlEncode(Build.MODEL.replace(" ", "-"))).append("&uuid=").append(this.mUUID).append("&os=").append(String.valueOf(VERSION.SDK_INT)).append("&core=").append(String.valueOf(DeviceUtils.getCpuCoreNums())).append("&channel=").append(this.mChannel).append("&mac=").append(UrlUtils.urlEncode(getMacAddress())).append("&re=").append(UrlUtils.urlEncode(this.mDisplayMetrics)).append("&entermode=").append(this.mEnterMode).append("&processid=").append(Process.myPid());
            this.mAM71CommonParams = builder.toString();
        }
        return this.mAM71CommonParams;
    }

    private String getSessionId() {
        return (this.mMacAddress.toLowerCase().replaceAll("-", "").replaceAll(SOAP.DELIM, "").replaceAll("\\.", "") + (System.currentTimeMillis() / 1000)) + new Random(System.currentTimeMillis()).nextInt(9999);
    }

    private String getMemorySize() {
        int m = DeviceUtils.getTotalMemory();
        if (m > 1024) {
            return (m / 1024) + "G";
        }
        return m + "M";
    }

    private String getMacAddress() {
        if (StringUtils.isEmpty(this.mMacAddress)) {
            return "";
        }
        return this.mMacAddress.toUpperCase().replace(SOAP.DELIM, "-");
    }
}
