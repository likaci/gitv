package com.gala.video.app.epg.openapi.feature.viprights;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetActivationStateCommand extends ServerCommand<Intent> {
    private static final String TAG = "GetActivationStateCommand";

    private class MyListener extends NetworkHolder implements IApiCallback<ApiResultCode> {
        private int mCode = 1;
        private int mState = 0;

        private void setCode(int code) {
            this.mCode = code;
        }

        private void setState(int state) {
            this.mState = state;
        }

        public int getCode() {
            return this.mCode;
        }

        public int getState() {
            return this.mState;
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(GetActivationStateCommand.TAG, "MyListener.onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultCode apiResult) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(GetActivationStateCommand.TAG, "MyListener.onSuccess(" + apiResult.code + ")");
            }
            setNetworkValid(true);
            setCode(0);
            String result = apiResult.code;
            PingBackInitParams params;
            if ("N100001".equals(result)) {
                params = PingBack.getInstance().getPingbackInitParams();
                params.sIsVipAct = "0";
                PingBack.getInstance().initialize(GetActivationStateCommand.this.getContext(), params);
                setState(1);
            } else if ("N100002".equals(result)) {
                GetInterfaceTools.getIGalaVipManager().setActivationFeedbackState(0);
                params = PingBack.getInstance().getPingbackInitParams();
                params.sIsVipAct = "1";
                PingBack.getInstance().initialize(GetActivationStateCommand.this.getContext(), params);
                setState(GetInterfaceTools.getIGalaVipManager().getAccountActivationState());
            }
        }
    }

    public GetActivationStateCommand(Context context) {
        super(context, TargetType.TARGET_VIP_RIGHTS, 20003, DataType.DATA_ACTIVATION_STATE);
        setNeedNetwork(false);
    }

    protected Bundle onProcess(Bundle params) {
        Bundle result;
        int state = GetInterfaceTools.getIGalaVipManager().getActivationState();
        if (state >= 0) {
            increaseAccessCount();
            result = OpenApiResultCreater.createResultBundle(0);
        } else if (OpenApiNetwork.isNetworkAvaliable()) {
            MyListener listener = new MyListener();
            TVApi.queryState.callSync(listener, new String[0]);
            state = listener.getState();
            result = OpenApiResultCreater.createResultBundle(listener.getCode());
            if (listener.isNetworkValid()) {
                increaseAccessCount();
            }
        } else {
            state = 0;
            result = OpenApiResultCreater.createResultBundle(4);
        }
        ServerParamsHelper.setActivationState(result, state);
        ServerParamsHelper.setCommandContinue(result, false);
        return result;
    }
}
