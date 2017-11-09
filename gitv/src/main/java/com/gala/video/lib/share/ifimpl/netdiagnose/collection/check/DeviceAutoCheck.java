package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.DevicesInfo;

public class DeviceAutoCheck extends CheckTask {
    private static final String TAG = "DeviceAutoCheck";
    private boolean isRunSuccess = false;

    public DeviceAutoCheck(CheckEntity checkEntity) {
        super(checkEntity);
    }

    public boolean runCheck() {
        final long startTime = System.currentTimeMillis();
        TVApi.deviceCheck.callSync(new IApiCallback<ApiResultDeviceCheck>() {
            public void onSuccess(ApiResultDeviceCheck apiResult) {
                if (apiResult == null || apiResult.data == null) {
                    onException(new ApiException("apiResult is null"));
                } else {
                    LogUtils.e(DeviceAutoCheck.TAG, "onDevCheckSuccess():" + apiResult.data);
                    DeviceAutoCheck.this.mCheckEntity.add("DeviceAutoCheck apiResult success, use time:" + (System.currentTimeMillis() - startTime) + ", result = " + apiResult.data);
                }
                DeviceAutoCheck.this.isRunSuccess = true;
            }

            public void onException(ApiException e) {
                String str;
                PingBackParams params = new PingBackParams();
                PingBackParams add = params.add(Keys.T, "0").add("ec", "315008");
                String str2 = "pfec";
                if (e == null) {
                    str = "";
                } else {
                    str = e.getCode();
                }
                add.add(str2, str);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                LogUtils.e(DeviceAutoCheck.TAG, "startDevCheck() -> onException e:" + e);
                DeviceAutoCheck.this.mCheckEntity.add("DeviceAutoCheck onException: code=" + e.getCode() + ", msg=" + e.getMessage() + ", url=" + e.getUrl());
                DeviceAutoCheck.this.isRunSuccess = false;
            }
        }, DevicesInfo.getDevicesInfoJson(AppRuntimeEnv.get().getApplicationContext()));
        return this.isRunSuccess;
    }
}
