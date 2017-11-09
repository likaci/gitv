package com.gala.video.app.epg.openapi.feature.account;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.PassportTVHelper;
import com.gala.tvapi.vrs.result.ApiResultCode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.impl.Params.OperationType;

public class LogoutCommand extends ServerCommand<Void> {
    private static final String TAG = "LogoutCommand";

    private class MyListener implements IVrsCallback<ApiResultCode> {
        private int mCode;

        private MyListener() {
        }

        public int getCode() {
            return this.mCode;
        }

        public void onException(ApiException exception) {
            Log.d(LogoutCommand.TAG, "onException()");
            this.mCode = 1;
        }

        public void onSuccess(ApiResultCode result) {
            Log.d(LogoutCommand.TAG, "onSuccess()");
            this.mCode = 0;
        }
    }

    public LogoutCommand(Context context) {
        super(context, 10002, OperationType.OP_LOGOUT, 30000);
    }

    public Bundle onProcess(Bundle params) {
        MyListener listener = new MyListener();
        PassportTVHelper.logout.callSync(listener, GetInterfaceTools.getIGalaAccountManager().getAuthCookie());
        int code = listener.getCode();
        if (code == 0) {
            GetInterfaceTools.getIGalaAccountManager().logOut(getContext(), "", LoginConstant.LGTTYPE_EXCEPTION);
        }
        increaseAccessCount();
        return OpenApiResultCreater.createResultBundle(code);
    }
}
