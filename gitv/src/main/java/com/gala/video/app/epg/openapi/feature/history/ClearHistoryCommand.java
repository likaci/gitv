package com.gala.video.app.epg.openapi.feature.history;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.OperationType;

public class ClearHistoryCommand extends ServerCommand<Void> {
    private static final String TAG = "ClearHistoryCommand";

    private class MyListener extends NetworkHolder implements IVrsCallback<ApiResultCode> {
        public int code = 1;

        private MyListener() {
        }

        public void onException(ApiException apiException) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(ClearHistoryCommand.TAG, "onException(" + apiException + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(apiException));
            this.code = 7;
        }

        public void onSuccess(ApiResultCode apiResultCode) {
            setNetworkValid(true);
            this.code = 0;
        }
    }

    protected ClearHistoryCommand(Context context, int target, int operation, int dataType) {
        super(context, target, operation, dataType);
        setNeedNetwork(true);
    }

    public ClearHistoryCommand(Context context) {
        super(context, 10001, OperationType.OP_CLEAR_HISTORY, 30000);
        setNeedNetwork(true);
    }

    protected boolean isLogin() {
        return GetInterfaceTools.getIGalaAccountManager().isLogin(getContext());
    }

    protected Bundle onProcess(Bundle inParams) {
        GetInterfaceTools.getIHistoryCacheManager().clear();
        return OpenApiResultCreater.createResultBundle(0);
    }
}
