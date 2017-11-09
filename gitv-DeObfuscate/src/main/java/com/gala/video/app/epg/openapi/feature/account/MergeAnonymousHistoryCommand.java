package com.gala.video.app.epg.openapi.feature.account;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.OperationType;

public class MergeAnonymousHistoryCommand extends ServerCommand<Void> {
    private static final String TAG = "MergeAnonymousHistoryCommand";

    private class MyListener implements IVrsCallback<ApiResultCode> {
        private int mResultCode;

        private MyListener() {
            this.mResultCode = 1;
        }

        public Bundle getResultBundle() {
            return OpenApiResultCreater.createResultBundle(this.mResultCode);
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(MergeAnonymousHistoryCommand.TAG, "onException(" + exception + ") " + (exception != null ? exception.getCode() : null));
            }
            this.mResultCode = 7;
        }

        public void onSuccess(ApiResultCode apiResultCode) {
            if (apiResultCode.isSuccessfull()) {
                this.mResultCode = 0;
                GetInterfaceTools.getIHistoryCacheManager().synchronizeHistoryListFromCloud();
            } else {
                this.mResultCode = 7;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(MergeAnonymousHistoryCommand.TAG, "onSuccess(" + this.mResultCode + ")");
            }
        }
    }

    public MergeAnonymousHistoryCommand(Context context) {
        super(context, 10002, OperationType.OP_MERGE_HISTORY, 30000);
    }

    public Bundle onProcess(Bundle params) {
        MyListener listener = new MyListener();
        UserHelper.mergeHistory.callSync(listener, GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), AppRuntimeEnv.get().getDefaultUserId());
        Bundle bundle = listener.getResultBundle();
        increaseAccessCount();
        return bundle;
    }
}
