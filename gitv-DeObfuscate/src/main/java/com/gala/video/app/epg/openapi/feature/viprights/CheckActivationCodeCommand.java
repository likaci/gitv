package com.gala.video.app.epg.openapi.feature.viprights;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.tvapi.vrs.BOSSHelper;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.ParamsHelper;

public class CheckActivationCodeCommand extends ServerCommand<Intent> {
    private static final String TAG = "CheckActivationCodeCommand";

    private class MyListener extends NetworkHolder implements IVrsCallback<ApiResultCode> {
        private int mCode = 1;
        private String mState = "";

        private void setCode(int code) {
            this.mCode = code;
        }

        private void setState(String state) {
            this.mState = state;
        }

        public int getCode() {
            return this.mCode;
        }

        public String getState() {
            return this.mState;
        }

        public void onException(ApiException exception) {
            boolean z;
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(CheckActivationCodeCommand.TAG, "MyListener.onException(" + exception + ")");
            }
            if (OpenApiNetwork.isNetworkInvalid(exception)) {
                z = false;
            } else {
                z = true;
            }
            setNetworkValid(z);
            String code = exception.getCode();
            if ("".equals(code)) {
                setCode(7);
                setState(exception.getHttpCode());
                return;
            }
            setCode(0);
            setState(code);
        }

        public void onSuccess(ApiResultCode apiResult) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(CheckActivationCodeCommand.TAG, "MyListener.onSuccess(" + apiResult.code + ")");
            }
            setNetworkValid(true);
            setCode(0);
            setState(apiResult.code);
        }
    }

    public CheckActivationCodeCommand(Context context) {
        super(context, TargetType.TARGET_VIP_RIGHTS, 20003, 30000);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle inParams) {
        String code = ParamsHelper.parseActivateCode(inParams);
        MyListener listener = new MyListener();
        BOSSHelper.checkActivationCode.callSync(listener, code, GetInterfaceTools.getIGalaAccountManager().getAuthCookie());
        if (listener.isNetworkValid()) {
            increaseAccessCount();
        }
        Bundle result = OpenApiResultCreater.createResultBundle(listener.getCode());
        ParamsHelper.setString(result, listener.getState());
        return result;
    }
}
