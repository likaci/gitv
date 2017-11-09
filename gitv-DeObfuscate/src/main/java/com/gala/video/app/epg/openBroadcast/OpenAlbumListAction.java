package com.gala.video.app.epg.openBroadcast;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import org.json.JSONObject;

public class OpenAlbumListAction extends BaseAction {
    private final String TAG = "OpenAlbumListAction";

    private enum LIST_TYPE {
        channel,
        history,
        favorite,
        vip,
        hot
    }

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            LogUtils.m1574i("OpenAlbumListAction", "process(context,intent)");
            JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(intent.getExtras());
            if (!checkParamsValidity(playDict)) {
                LogUtils.m1571e("OpenAlbumListAction", "checkParamsValidity is false. ");
                if (loadingCallback != null) {
                    loadingCallback.onFail();
                    LogUtils.m1571e("OpenAlbumListAction", "loadingCallback.onCancel()...");
                }
            } else if (playDict != null) {
                if (loadingCallback != null) {
                    loadingCallback.onSuccess();
                }
                dealAlbumList(context, playDict);
            }
        } catch (Exception e) {
            LogUtils.m1571e("OpenAlbumListAction", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenAlbumListAction process.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
            e.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.m1571e("OpenAlbumListAction", "loadingCallback.onFail();");
            }
        }
    }

    private void dealAlbumList(Context context, JSONObject dict) {
        LogUtils.m1574i("OpenAlbumListAction", "dealAlbumList");
        String listType = dict.optString("listType");
        String chnId = dict.optString("chnId");
        String chnName = dict.optString("chnName");
        if (LIST_TYPE.vip.name().equalsIgnoreCase(listType)) {
            AlbumUtils.startChannelNewVipPageOpenApi(context, -1);
        } else if (LIST_TYPE.history.name().equalsIgnoreCase(listType)) {
            AlbumUtils.startPlayhistoryActivityOpenApi(context, -1);
        } else if (LIST_TYPE.favorite.name().equalsIgnoreCase(listType)) {
            AlbumUtils.startFavouriteActivityOpenApi(context, -1);
        } else if (LIST_TYPE.channel.name().equals(listType)) {
            AlbumUtils.startChannelPageOpenApi(context, Integer.parseInt(chnId), chnName, 0, -1);
        } else if (LIST_TYPE.hot.name().equals(listType)) {
            AlbumUtils.startChannelPageOpenApi(context, 10009, "热播榜", 0, -1);
        } else {
            LogUtils.m1571e("OpenAlbumListAction", "invalid listType =  " + listType);
        }
    }

    public boolean checkParamsValidity(JSONObject playDict) {
        if (super.checkParamsValidity(playDict)) {
            String listType = playDict.optString("listType");
            LogUtils.m1568d("OpenAlbumListAction", "listType = " + listType);
            if (TextUtils.isEmpty(listType)) {
                LogUtils.m1571e("OpenAlbumListAction", "[INVALID-PARAMTER] [action:ACTION_ALBUMLIST][reason:missing field--listType][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
            } else if (!LIST_TYPE.channel.name().equals(listType)) {
                return true;
            } else {
                String chnId = playDict.optString("chnId");
                String chnName = playDict.optString("chnName");
                LogUtils.m1568d("OpenAlbumListAction", "chnId = " + chnId);
                LogUtils.m1568d("OpenAlbumListAction", "chnName = " + chnName);
                if (!TextUtils.isEmpty(chnId) && !TextUtils.isEmpty(chnName)) {
                    return true;
                }
                LogUtils.m1571e("OpenAlbumListAction", "[INVALID-PARAMTER] [action:ACTION_ALBUMLIST][reason:missing field--chnId|chnName][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
                return false;
            }
        }
        return false;
    }
}
