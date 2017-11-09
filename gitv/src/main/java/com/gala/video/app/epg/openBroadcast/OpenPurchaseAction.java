package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import com.gala.video.lib.share.utils.DevicesInfo;
import org.json.JSONObject;

public class OpenPurchaseAction extends BaseAction {
    private final String TAG = "OpenPurchaseAction";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.d("OpenPurchaseAction", "process(context,intent)");
            JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(intent.getExtras());
            if (!checkParamsValidity(playDict)) {
                LogUtils.e("OpenPurchaseAction", "checkParamsValidity is false. ");
                if (loadingCallback != null) {
                    loadingCallback.onFail();
                    LogUtils.e("OpenPurchaseAction", "loadingCallback.onCancel()...");
                }
            } else if (playDict != null) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                checkAuthIdAndApiKey(context, loadingCallback);
            }
        } catch (Exception e) {
            LogUtils.e("OpenPurchaseAction", "process---exception = " + e.getMessage());
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("OpenPurchaseAction", "loadingCallback.onFail();");
            }
        }
    }

    void startPurchase(Context context) {
        LogUtils.i("OpenPurchaseAction", "startPurchase");
        WebIntentParams params = new WebIntentParams();
        params.incomesrc = "openapi";
        GetInterfaceTools.getWebEntry().startPurchasePage(context, params);
    }

    void checkAuthIdAndApiKey(final Context context, final LoadingCallback loadingCallback) {
        if (TVApiBase.getTVApiProperty().checkAuthIdAndApiKeyAvailable()) {
            LogUtils.d("OpenPurchaseAction", "checkAuthIdAndApiKeyAvailable = true");
            startPurchase(context);
            return;
        }
        LogUtils.d("OpenPurchaseAction", "checkAuthIdAndApiKeyAvailable = false, start DeviceCheck...");
        TVApi.deviceCheckP.callSync(new IApiCallback<ApiResultDeviceCheck>() {
            public void onSuccess(ApiResultDeviceCheck apiResult) {
                LogUtils.d("OpenPurchaseAction", "onDeviceCheck-onSuccess");
                OpenPurchaseAction.this.startPurchase(context);
            }

            public void onException(ApiException e) {
                LogUtils.e("OpenPurchaseAction", "onDeviceCheck-onException e=" + e);
                e.printStackTrace();
                if (!NetworkUtils.isNetworkAvaliable()) {
                    LogUtils.e("OpenPurchaseAction", "TVApi--IApiCallback--NetworkUtils.isNetworkAvaliable() =false");
                    loadingCallback.onNetworkAvaliable();
                }
                loadingCallback.onFail();
            }
        }, DevicesInfo.getDevicesInfoJson(AppRuntimeEnv.get().getApplicationContext()));
    }
}
