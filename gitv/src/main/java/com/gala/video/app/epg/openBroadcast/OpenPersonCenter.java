package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;

public class OpenPersonCenter extends BaseAction {
    private final String TAG = "OpenPersonCenter";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.d("OpenPersonCenter", "process(context,intent)");
            if (checkParamsValidity(OpenPlayIntentUtils.parsePlayInfo(intent.getExtras()))) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                dealPersonCenter(context);
                return;
            }
            LogUtils.e("OpenPersonCenter", "checkParamsValidity is false. ");
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("OpenPersonCenter", "loadingCallback.onCancel()...");
            }
        } catch (Exception e) {
            LogUtils.e("OpenPersonCenter", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenPersonCenter process.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("OpenPersonCenter", "loadingCallback.onFail();");
            }
        }
    }

    private void dealPersonCenter(Context context) {
        LogUtils.d("OpenPersonCenter", "dealPersonCenter");
        GetInterfaceTools.getLoginProvider().startLoginActivityOpenApi(context, -1);
    }
}
