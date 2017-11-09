package com.gala.video.lib.share.ifimpl.openplay.broadcast.action;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;

public class OpenPersonCenter extends BaseAction {
    private final String TAG = "openplay/broadcast/OpenPersonCenter";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            Log.d("openplay/broadcast/OpenPersonCenter", "process(context,intent)");
            if (checkParamsValidity(OpenPlayIntentUtils.parsePlayInfo(intent.getExtras()))) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                dealPersonCenter(context);
                return;
            }
            LogUtils.m1571e("openplay/broadcast/OpenPersonCenter", "checkParamsValidity is false. ");
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.m1571e("openplay/broadcast/OpenPersonCenter", "loadingCallback.onCancel()...");
            }
        } catch (Exception e) {
            LogUtils.m1571e("openplay/broadcast/OpenPersonCenter", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenPersonCenter process.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.m1571e("openplay/broadcast/OpenPersonCenter", "loadingCallback.onFail();");
            }
        }
    }

    private void dealPersonCenter(Context context) {
        LogUtils.m1568d("openplay/broadcast/OpenPersonCenter", "dealPersonCenter");
        GetInterfaceTools.getLoginProvider().startLoginActivityOpenApi(context, -1);
    }
}
