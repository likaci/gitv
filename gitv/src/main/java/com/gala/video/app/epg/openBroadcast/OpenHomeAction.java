package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import org.json.JSONObject;

public class OpenHomeAction extends BaseAction {
    private final String TAG = "OpenHomeAction";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.d("OpenHomeAction", "process(context,intent)");
            JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(intent.getExtras());
            if (!checkParamsValidity(playDict)) {
                LogUtils.e("OpenHomeAction", "checkParamsValidity is false. ");
                if (loadingCallback != null) {
                    loadingCallback.onFail();
                    LogUtils.e("OpenHomeAction", "loadingCallback.onCancel()...");
                }
            } else if (playDict != null) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                startHome(context);
            }
        } catch (Exception e) {
            LogUtils.e("OpenHomeAction", "process---exception = " + e.getMessage());
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("OpenHomeAction", "loadingCallback.onFail();");
            }
        }
    }

    void startHome(Context context) {
        LogUtils.i("OpenHomeAction", "startHome");
        CreateInterfaceTools.createEpgEntry().startHomeActivity(context, false);
    }
}
