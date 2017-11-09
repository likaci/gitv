package com.gala.video.app.epg.openapi.feature.favorite;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.NetworkHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.OperationType;

public class ClearFavoriteCommand extends ServerCommand<Void> {
    private static final String TAG = "ClearFavoriteCommand";

    private class MyListener extends NetworkHolder implements IVrsCallback<ApiResultCode> {
        public int code;

        private MyListener() {
            this.code = 1;
        }

        public void onException(ApiException apiException) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(ClearFavoriteCommand.TAG, "onException(" + apiException + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(apiException));
            this.code = 7;
        }

        public void onSuccess(ApiResultCode arg0) {
            ClearFavoriteCommand.this.setNeedNetwork(true);
            this.code = 0;
        }
    }

    protected ClearFavoriteCommand(Context context, int target, int operation, int dataType) {
        super(context, target, operation, dataType);
    }

    public ClearFavoriteCommand(Context context) {
        super(context, 10003, OperationType.OP_CLEAR_FAVORITE, 30000);
        setNeedNetwork(true);
    }

    protected boolean isLogin() {
        return GetInterfaceTools.getIGalaAccountManager().isLogin(getContext());
    }

    protected Bundle onProcess(Bundle inParams) {
        MyListener listener = new MyListener();
        if (isLogin()) {
            UserHelper.clearCollect.callSync(listener, GetInterfaceTools.getIGalaAccountManager().getAuthCookie());
        } else {
            UserHelper.clearCollectForAnonymity.callSync(listener, AppRuntimeEnv.get().getDefaultUserId());
        }
        if (!listener.isNetworkValid()) {
            increaseAccessCount();
        }
        return OpenApiResultCreater.createResultBundle(listener.code);
    }
}
