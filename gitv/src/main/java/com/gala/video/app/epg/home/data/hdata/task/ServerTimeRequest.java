package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultSysTime;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class ServerTimeRequest extends BaseRequestTask {
    private static final String TAG = ServerTimeRequest.class.getName();

    public void invoke() {
        LogUtils.d(TAG, "perform system time request");
        TVApi.sysTime.callSync(new IApiCallback<ApiResultSysTime>() {
            public void onException(ApiException e) {
                String str;
                LogUtils.e(ServerTimeRequest.TAG, "request server time exception:", e);
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
                String str2 = "pfec";
                if (e == null) {
                    str = "";
                } else {
                    str = e.getCode();
                }
                addItem = addItem.addItem(str2, str);
                str2 = Keys.ERRURL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getUrl();
                }
                addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "sysTime");
                str2 = Keys.ERRDETAIL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getMessage();
                }
                addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            }

            public void onSuccess(ApiResultSysTime sysTime) {
                long serverTimeMillis = StringUtils.parse(sysTime.data.sysTime, -1) * 1000;
                if (serverTimeMillis < 0) {
                    serverTimeMillis = System.currentTimeMillis();
                }
                DeviceUtils.updateServerTimeMillis(serverTimeMillis);
                LogUtils.d(ServerTimeRequest.TAG, "request server time success");
            }
        }, new String[0]);
    }

    public void onOneTaskFinished() {
        LogUtils.d(TAG, "request server time task finished");
    }
}
