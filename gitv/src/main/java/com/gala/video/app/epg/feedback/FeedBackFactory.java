package com.gala.video.app.epg.feedback;

import android.content.Context;
import android.util.Log;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.core.ApiExceptionModelFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackFactory.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.ApiExceptionModel;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;

public class FeedBackFactory extends Wrapper {
    private static final String TAG = "EPG/home/FeedBackFactory";

    public FeedBackModel createFeedBack(ApiExceptionModel provider) {
        FeedBackModel model = new FeedBackModel();
        Context context = AppRuntimeEnv.get().getApplicationContext();
        if (!StringUtils.isEmpty(provider.getApiCode())) {
            model.setErrorCode(provider.getApiCode());
        } else if (StringUtils.isEmpty(provider.getHttpCode())) {
            model.setErrorCode(null);
        } else {
            model.setErrorCode(provider.getHttpCode());
        }
        model.setErrorMsg(getErrorMessage(provider));
        if (StringUtils.isEmpty(provider.getExceptionName())) {
            model.setExceptionName("null");
        } else {
            model.setExceptionName(provider.getExceptionName());
        }
        if (StringUtils.isEmpty(provider.getErrorUrl())) {
            model.setUrl(null);
        } else {
            model.setUrl(provider.getErrorUrl());
        }
        model.setErrorLog(provider.getErrorLog());
        model.setErrorLog(GetInterfaceTools.getILogRecordProvider().getLogCore().getLogFromLogcatBuffer(300));
        if (provider.getErrorEvent() == ErrorEvent.C_ERROR_NONET || provider.getErrorEvent() == ErrorEvent.C_ERROR_INTERNET) {
            model.setShowQR(false);
        }
        if (StringUtils.isEmpty(provider.getApiName())) {
            Log.v(TAG, "provider.getApiName() is null");
        } else {
            model.setApiName(provider.getApiName());
        }
        return model;
    }

    public FeedBackModel createFeedBack(ApiException e) {
        return createFeedBack(ApiExceptionModelFactory.createApiExceptionModel(e));
    }

    private String getErrorMessage(ApiExceptionModel model) {
        if (!StringUtils.isEmpty(model.getErrorMessage())) {
            return model.getErrorMessage();
        }
        ErrorCodeModel errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(model.getApiCode());
        if (errorModel != null) {
            return errorModel.getContent();
        }
        Context context = AppRuntimeEnv.get().getApplicationContext();
        ErrorEvent event = model.getErrorEvent();
        if (event == null) {
            LogUtils.e(TAG, "getErrorMessage()---event is null");
            return context.getString(R.string.devcheck_json_exception);
        }
        switch (event) {
            case C_ERROR_MAC:
                return context.getString(R.string.device_cannot_use) + "";
            case C_ERROR_E000054:
                return context.getString(R.string.common_api_error_msg, new Object[]{model.getApiCode()});
            case C_ERROR_E000012:
                return context.getString(R.string.common_data_error_msg, new Object[]{model.getApiCode()});
            case C_ERROR_E000001:
                if (!StringUtils.isEmpty(model.getErrorMessage())) {
                    return model.getErrorMessage();
                }
                return context.getString(R.string.common_api_error_msg, new Object[]{model.getApiCode()});
            case C_ERROR_E_OTHER:
                return context.getString(R.string.common_api_error_msg, new Object[]{model.getApiCode()});
            case C_ERROR_JSON:
                return context.getString(R.string.devcheck_json_exception);
            case C_ERROR_HTTP:
                return context.getString(R.string.devcheck_http_exception) + model.getHttpCode();
            case C_ERROR_SERVER:
                return context.getString(R.string.server_error);
            case C_ERROR_INTERNET:
                return context.getString(Project.getInstance().getResProvider().getCannotConnInternet());
            case C_ERROR_NONET:
                return context.getString(R.string.no_network);
            case C_ERROR_DATAISNULL:
                return context.getString(R.string.devcheck_json_exception);
            default:
                LogUtils.e(TAG, "getErrorMessage()---default--");
                return context.getString(R.string.devcheck_json_exception);
        }
    }
}
