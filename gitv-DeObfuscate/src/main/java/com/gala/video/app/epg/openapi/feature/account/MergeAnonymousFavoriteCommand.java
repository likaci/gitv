package com.gala.video.app.epg.openapi.feature.account;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.OperationType;

public class MergeAnonymousFavoriteCommand extends ServerCommand<Void> {
    private static final String TAG = "MergeAnonymousFavoriteCommand";

    private static class MyListener implements IVrsCallback<ApiResultCode> {
        private int mResultCode;

        private MyListener() {
            this.mResultCode = 1;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(MergeAnonymousFavoriteCommand.TAG, "onException(" + exception + ") " + (exception != null ? exception.getCode() : null));
            }
            this.mResultCode = 7;
        }

        public void onSuccess(ApiResultCode apiResultCode) {
            if (apiResultCode.isSuccessfull()) {
                this.mResultCode = 0;
            } else {
                this.mResultCode = 7;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(MergeAnonymousFavoriteCommand.TAG, "onSuccess(" + this.mResultCode + ")");
            }
        }
    }

    public MergeAnonymousFavoriteCommand(Context context) {
        super(context, 10002, OperationType.OP_MERGE_FAVORITE, 30000);
    }

    public Bundle onProcess(Bundle params) {
        MyListener listener = new MyListener();
        UserHelper.mergeCollects.callSync(listener, GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), GetInterfaceTools.getIGalaAccountManager().getUID());
        Bundle bundle = OpenApiResultCreater.createResultBundle(listener.getResultCode());
        increaseAccessCount();
        return bundle;
    }
}
