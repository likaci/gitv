package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import com.gala.video.lib.share.utils.DevicesInfo;
import org.json.JSONObject;

public class OpenSubjectAction extends BaseAction {
    private final String TAG = "openplay/broadcast/OpenSubjectAction";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.d("openplay/broadcast/OpenSubjectAction", "process(context,intent)");
            JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(intent.getExtras());
            if (!checkParamsValidity(playDict)) {
                LogUtils.e("openplay/broadcast/OpenSubjectAction", "checkParamsValidity is false. ");
                if (loadingCallback != null) {
                    loadingCallback.onFail();
                    LogUtils.e("openplay/broadcast/OpenSubjectAction", "loadingCallback.onCancel()...");
                }
            } else if (playDict != null) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                checkAuthIdAndApiKey(context, playDict, loadingCallback);
            }
        } catch (Exception e) {
            LogUtils.e("openplay/broadcast/OpenSubjectAction", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenSubjectAction process.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("openplay/broadcast/OpenSubjectAction", "loadingCallback.onFail();");
            }
        }
    }

    private void startSubjectActivity(Context context, String plId, String plName, String from) {
        GetInterfaceTools.getWebEntry().gotoSubject(context, plId, plName, from, "openAPI");
    }

    public boolean checkParamsValidity(JSONObject playDict) {
        if (super.checkParamsValidity(playDict)) {
            String subjectId = playDict.optString("subjectId");
            LogUtils.i("openplay/broadcast/OpenSubjectAction", "subjectId = " + subjectId);
            if (!TextUtils.isEmpty(subjectId)) {
                return true;
            }
            LogUtils.e("openplay/broadcast/OpenSubjectAction", "[INVALID-PARAMTER] [action:ACTION_SUBJECT][reason:missing field--subjectId][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
        }
        return false;
    }

    void dealSubject(Context context, JSONObject dict) {
        LogUtils.i("openplay/broadcast/OpenSubjectAction", "dealSubjecttest");
        String subjectId = dict.optString("subjectId");
        String subjectName = dict.optString("subjectName");
        if (!TextUtils.isEmpty(subjectId)) {
            startSubjectActivity(context, subjectId, subjectName, "openAPI");
        }
    }

    void checkAuthIdAndApiKey(final Context context, final JSONObject dict, final LoadingCallback loadingCallback) {
        if (TVApiBase.getTVApiProperty().checkAuthIdAndApiKeyAvailable()) {
            LogUtils.d("openplay/broadcast/OpenSubjectAction", "checkAuthIdAndApiKeyAvailable = true");
            dealSubject(context, dict);
            return;
        }
        LogUtils.d("openplay/broadcast/OpenSubjectAction", "checkAuthIdAndApiKeyAvailable = false, start DeviceCheck...");
        TVApi.deviceCheckP.callSync(new IApiCallback<ApiResultDeviceCheck>() {
            public void onSuccess(ApiResultDeviceCheck apiResult) {
                LogUtils.d("openplay/broadcast/OpenSubjectAction", "onDeviceCheck-onSuccess");
                OpenSubjectAction.this.dealSubject(context, dict);
            }

            public void onException(ApiException e) {
                LogUtils.e("openplay/broadcast/OpenSubjectAction", "onDeviceCheck-onException e=" + e);
                e.printStackTrace();
                if (!NetworkUtils.isNetworkAvaliable()) {
                    LogUtils.e("openplay/broadcast/OpenSubjectAction", "TVApi--IApiCallback--NetworkUtils.isNetworkAvaliable() =false");
                    loadingCallback.onNetworkAvaliable();
                }
                loadingCallback.onFail();
            }
        }, DevicesInfo.getDevicesInfoJson(AppRuntimeEnv.get().getApplicationContext()));
    }
}
