package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import org.json.JSONObject;

public class OpenSearchResultAction extends BaseAction {
    private final String TAG = "OpenSearchResultAction";

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.d("OpenSearchResultAction", "process(context,intent)");
            JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(intent.getExtras());
            if (checkParamsValidity(playDict)) {
                String keyword = playDict.optString("keyword");
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                dealSearchResult(context, keyword);
                return;
            }
            LogUtils.e("OpenSearchResultAction", "checkParamsValidity is false. ");
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("OpenSearchResultAction", "loadingCallback.onCancel()...");
            }
        } catch (Exception e) {
            LogUtils.e("OpenSearchResultAction", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenSearchResultAction process.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("OpenSearchResultAction", "loadingCallback.onFail();");
            }
        }
    }

    private void dealSearchResult(Context context, String keyword) {
        LogUtils.i("OpenSearchResultAction", "dealSearchResult keyword = " + keyword);
        if (!StringUtils.isEmpty((CharSequence) keyword)) {
            AlbumUtils.startSearchResultPage(context, -1, keyword, 1, "", -1, null);
        }
    }

    public boolean checkParamsValidity(JSONObject playDict) {
        if (super.checkParamsValidity(playDict)) {
            if (!TextUtils.isEmpty(playDict.optString("keyword"))) {
                return true;
            }
            LogUtils.e("OpenSearchResultAction", "[INVALID-PARAMTER] [action:ACTION_SEARCHRESULT][reason:missing field--keyword][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
        }
        return false;
    }
}
