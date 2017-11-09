package com.gala.video.app.epg.openapi.feature;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.GlobalWatcher;
import com.gala.video.lib.share.ifimpl.openplay.service.IAccessWatcher;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import com.gala.video.lib.share.utils.DevicesInfo;
import com.qiyi.tv.client.impl.Params.TargetType;

public class DeviceAuthCommand extends ServerCommand<Void> {
    private static final int HTTPCODE_EXCEPTION_MAX = 600;
    private static final int HTTPCODE_EXCEPTION_MIN = 300;
    private static final long OAA_DEFAULT_INTERVAL = 0;
    private static final long OAA_DURATION = 3600000;
    private static final long OAA_MAX_COUNT = 1;
    private static final String TAG = "DeviceAuthCommand";
    private final DeviceCheckModel mDevice = DeviceCheckModel.getInstance();
    private final IAccessWatcher mErrorToken;

    private class MyListener implements IApiCallback<ApiResultDeviceCheck> {
        public ApiException exception;
        public boolean increaseAccessCount;

        private MyListener() {
            this.exception = null;
        }

        public void onSuccess(ApiResultDeviceCheck apiResult) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(DeviceAuthCommand.TAG, "authDevice.onSuccess() success!");
            }
            this.increaseAccessCount = true;
            DeviceCheck devCheck = apiResult.data;
            DeviceAuthCommand.this.mDevice.setDevCheck(devCheck);
            if (devCheck != null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(DeviceAuthCommand.TAG, "authDevice() pass ip=" + devCheck.ip);
                }
                AppRuntimeEnv.get().setDeviceIp(devCheck.ip);
                PingBackParams params = new PingBackParams();
                params.add(Keys.T, Values.value16).add("r", Values.value00001).add("rt", "13").add("st", "0");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                DeviceAuthCommand.this.mDevice.setErrorEvent(ErrorEvent.C_SUCCESS);
                DeviceAuthCommand.this.mDevice.setApiKey(devCheck.apiKey);
                DeviceAuthCommand.this.mDevice.setHomeResId(devCheck.resIds);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(DeviceAuthCommand.TAG, "authDevice() faill for devChek is empty!");
            }
        }

        public void onException(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(DeviceAuthCommand.TAG, "authDevice.onException() faill! ", e);
            }
            if (e != null) {
                LogUtils.d(DeviceAuthCommand.TAG, "authDevice.onException() faii:[" + e.getCode() + "], [" + e.getHttpCode() + "], " + e.getMessage());
                this.exception = e;
            }
            this.increaseAccessCount = !OpenApiNetwork.isNetworkInvalid(e);
        }
    }

    public DeviceAuthCommand(Context context) {
        super(context, TargetType.TARGET_AUTH, 20000, 30000);
        setNeedNetwork(true);
        this.mErrorToken = new GlobalWatcher(3600000, OAA_MAX_COUNT, 0);
    }

    public synchronized Bundle onProcess(Bundle params) {
        Bundle result;
        String serverCode = null;
        synchronized (this) {
            result = new Bundle();
            MyListener listener = new MyListener();
            if (!this.mDevice.isDevCheckPass()) {
                authDevice(listener);
            }
            int httpCode = StringUtils.parse(listener.exception == null ? null : listener.exception.getHttpCode(), -1);
            if (listener.exception != null) {
                serverCode = listener.exception.getCode();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "process() device auth " + this.mDevice.isDevCheckPass() + ", increase=" + listener.increaseAccessCount + ", apiException = " + listener.exception + ", httpCode = " + httpCode);
            }
            int code = 2;
            if (this.mDevice.isDevCheckPass()) {
                code = 0;
            } else if (!listener.increaseAccessCount) {
                code = 4;
            } else if (isServerError(serverCode)) {
                this.mErrorToken.increaseAccessCount();
                code = 10;
            } else if (isHttpCodeException(httpCode)) {
                code = httpCode;
            }
            if (listener.increaseAccessCount && OpenApiNetwork.isNetworkAvaliableWithBlocking()) {
                increaseAccessCount();
            }
            ServerParamsHelper.setResultCode(result, code);
        }
        return result;
    }

    private boolean isServerError(String serverCode) {
        boolean serverError = false;
        if (serverCode != null && serverCode.startsWith("E")) {
            serverError = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isServerError(" + serverCode + ") return " + serverError);
        }
        return serverError;
    }

    private void authDevice(IApiCallback<ApiResultDeviceCheck> listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "authDevice() begin.");
        }
        TVApi.deviceCheckP.callSync(listener, DevicesInfo.getDevicesInfoJson(AppRuntimeEnv.get().getApplicationContext()));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "authDevice() end.");
        }
    }

    protected boolean isAllowedAccess() {
        boolean allowed = super.isAllowedAccess();
        boolean specialAllowed = this.mErrorToken.isAllowedAccess();
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isAllowedAccess() allowed=" + allowed + ", special=" + specialAllowed);
        }
        return allowed && specialAllowed;
    }

    private boolean isHttpCodeException(int httpCode) {
        if (httpCode >= 600 || httpCode < 300) {
            return false;
        }
        return true;
    }
}
