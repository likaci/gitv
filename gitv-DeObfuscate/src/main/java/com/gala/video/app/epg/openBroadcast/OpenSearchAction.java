package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.search.QSearchActivity;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import com.gala.video.lib.share.utils.PageIOUtils;

public class OpenSearchAction extends BaseAction {
    private final String TAG = "OpenSearchAction";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.m1568d("OpenSearchAction", "process(context,intent)");
            if (checkParamsValidity(OpenPlayIntentUtils.parsePlayInfo(intent.getExtras()))) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                dealSearch(context);
                return;
            }
            LogUtils.m1571e("OpenSearchAction", "checkParamsValidity is false. ");
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.m1571e("OpenSearchAction", "loadingCallback.onCancel()...");
            }
        } catch (Exception e) {
            LogUtils.m1571e("OpenSearchAction", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenSearchAction process.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.m1571e("OpenSearchAction", "loadingCallback.onFail();");
            }
        }
    }

    private void dealSearch(Context context) {
        PageIOUtils.activityIn(context, new Intent(context, QSearchActivity.class));
    }
}
