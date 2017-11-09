package com.gala.video.app.epg.home.data.core;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.uikit.data.data.Model.ApiExceptionModel;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;

public class ApiExceptionModelFactory {
    private static final String SPECIAL_CODE_E100001 = "E100001";
    private static final String SPECIAL_CODE_E100002 = "E100002";
    private static final String SPECIAL_CODE_E100003 = "E100003";
    private static final String TAG = "EPG/home/ApiExceptionModelFactory";

    public static ApiExceptionModel createApiExceptionModel(ApiException e) {
        ApiExceptionModel provider = new ApiExceptionModel();
        if (e != null) {
            String code = e.getCode();
            if (SPECIAL_CODE_E100001.equals(code) || SPECIAL_CODE_E100002.equals(code) || SPECIAL_CODE_E100003.equals(code)) {
                provider.setErrorMessage(e.getMessage());
            }
            provider.setApiCode(code);
            provider.setHttpCode(e.getHttpCode());
            provider.setErrorUrl(e.getUrl());
            provider.setExceptionName(e.getExceptionClassName());
            provider.setErrorLog(GetInterfaceTools.getILogRecordProvider().getLogCore().getLogFromLogcatBuffer(300));
            dealApiException(provider);
        }
        return provider;
    }

    public static ApiExceptionModel createApiExceptionModel(ApiException e, boolean needLog) {
        ApiExceptionModel provider = createApiExceptionModel(e);
        GetInterfaceTools.getILogRecordProvider().notifySaveLogFile();
        return provider;
    }

    public static void saveDevApiExceptionMsg(ApiException e) {
        DeviceCheckModel deviceCheckResult = DeviceCheckModel.getInstance();
        if (e != null) {
            String code = e.getCode();
            LogUtils.d(TAG, "get exception code : " + code);
            if (SPECIAL_CODE_E100001.equals(code) || SPECIAL_CODE_E100002.equals(code) || SPECIAL_CODE_E100003.equals(code)) {
                deviceCheckResult.setErrorMessage(e.getMessage());
            }
            deviceCheckResult.setApiCode(e.getCode());
            deviceCheckResult.setHttpCode(e.getHttpCode());
            deviceCheckResult.setErrorUrl(e.getUrl());
            deviceCheckResult.setExceptionName(e.getExceptionClassName());
            deviceCheckResult.setApiName(e.getApiName());
            GetInterfaceTools.getILogRecordProvider().notifySaveLogFile();
        }
    }

    public static DeviceCheckModel createDevApiExceptionModel() {
        DeviceCheckModel deviceCheckResult = DeviceCheckModel.getInstance();
        dealApiException(deviceCheckResult);
        return deviceCheckResult;
    }

    private static void dealApiException(ApiExceptionModel provider) {
        String httpCode = provider.getHttpCode();
        String apiCode = provider.getApiCode();
        ErrorEvent errorEvent = ErrorEvent.C_SUCCESS;
        LogUtils.d(TAG, "dealApiException httpcode = " + httpCode + ", api code = " + apiCode);
        if (ErrorEvent.HTTP_CODE_SUCCESS.equals(httpCode)) {
            if ("E000054".equals(apiCode)) {
                errorEvent = ErrorEvent.C_ERROR_E000054;
            } else if ("E000012".equals(apiCode)) {
                errorEvent = ErrorEvent.C_ERROR_E000012;
            } else if (ErrorEvent.API_CODE_FAIL_SERVICE.equals(apiCode)) {
                errorEvent = ErrorEvent.C_ERROR_E000001;
            } else if ("-100".equals(apiCode)) {
                errorEvent = ErrorEvent.C_ERROR_JSON;
            } else {
                errorEvent = ErrorEvent.C_ERROR_E_OTHER;
            }
        } else if ("-50".equals(httpCode)) {
            int state = NetWorkManager.getInstance().getNetState();
            LogUtils.d(TAG, "dealApiException() get net state:" + state);
            if (state == 0) {
                errorEvent = ErrorEvent.C_ERROR_NONET;
            } else if (state == 3 || state == 4) {
                errorEvent = ErrorEvent.C_ERROR_INTERNET;
            } else {
                errorEvent = ErrorEvent.C_ERROR_SERVER;
            }
        } else if (ErrorEvent.API_CODE_GET_MAC_FAILD.equals(apiCode)) {
            errorEvent = ErrorEvent.C_ERROR_MAC;
        } else {
            errorEvent = ErrorEvent.C_ERROR_HTTP;
        }
        provider.setErrorEvent(errorEvent);
    }
}
