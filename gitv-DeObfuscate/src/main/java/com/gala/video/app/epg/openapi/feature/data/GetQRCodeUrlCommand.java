package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.project.Project;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetQRCodeUrlCommand extends ServerCommand<String> {
    private static final String QRCODE_URL = "http://ota.iqiyi.com/jump.jsp";
    private static final String TAG = "GetQRCodeCommand";

    private class MyListener extends NetworkHolder implements IApiCallback<ApiResultDeviceCheck> {
        public int code;
        public String qrCodeUrl;

        private MyListener() {
        }

        public void onSuccess(ApiResultDeviceCheck apiResult) {
            boolean isHasHuaweiQuickMark = false;
            this.code = 0;
            if (apiResult.data != null) {
                isHasHuaweiQuickMark = apiResult.data.isHasHuaweiQuickMark();
            }
            if (isHasHuaweiQuickMark) {
                this.qrCodeUrl = GetQRCodeUrlCommand.QRCODE_URL;
            } else {
                this.qrCodeUrl = null;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetQRCodeUrlCommand.TAG, "onSuccess(), apiResultCode = " + apiResult.code + ", code = " + this.code + ", qrCodeUrl = " + this.qrCodeUrl);
            }
        }

        public void onException(ApiException apiException) {
            this.code = 7;
            this.qrCodeUrl = null;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GetQRCodeUrlCommand.TAG, "onException(), exception = " + apiException);
            }
        }
    }

    public GetQRCodeUrlCommand(Context context) {
        super(context, TargetType.TARGET_QR_CODE, 20003, DataType.DATA_URL);
    }

    protected Bundle onProcess(Bundle inParams) {
        MyListener listener = new MyListener();
        String versionString = Project.getInstance().getBuild().getVersionString();
        String modelString = Build.MODEL.toString();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "versionString = " + versionString + ", modelString = " + modelString);
        }
        TVApi.dynamicQ.callSync(listener, versionString, modelString, String.valueOf(0), String.valueOf(0), String.valueOf(1), String.valueOf(0), String.valueOf(0), String.valueOf(0), String.valueOf(0));
        Bundle bundle = new Bundle();
        ServerParamsHelper.setResultCode(bundle, listener.code);
        ServerParamsHelper.setQrCodeUrl(bundle, listener.qrCodeUrl);
        return bundle;
    }
}
