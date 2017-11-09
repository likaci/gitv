package com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.webview.utils.WebSDKConstants;
import org.json.JSONObject;

public abstract class BaseAction {
    private final String TAG = "openplay/broadcast/BaseAction";

    public interface LoadingCallback {
        boolean iscancel();

        void onCancel();

        void onFail();

        void onNetworkAvaliable();

        void onSuccess();

        void onSuccess(String str);
    }

    public abstract void process(Context context, Intent intent, LoadingCallback loadingCallback);

    public boolean checkParamsValidity(JSONObject playDict) {
        if (playDict != null) {
            try {
                String customer = playDict.optString(WebSDKConstants.PARAM_KEY_CUSTOMER);
                LogUtils.m1568d("openplay/broadcast/BaseAction", "customer = " + customer);
                if (!TextUtils.isEmpty(customer)) {
                    return true;
                }
            } catch (Exception e) {
                LogUtils.m1571e("openplay/broadcast/BaseAction", "[UNKNOWN-EXCEPTION] [reason: exception occurred when checkParamsValidity-customer.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
                e.printStackTrace();
                return false;
            }
        }
        LogUtils.m1571e("openplay/broadcast/BaseAction", "[INVALID-PARAMTER] [reason:missing field--customer][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
        return false;
    }
}
