package com.gala.video.app.player.openBroadcast;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import org.json.JSONObject;

public abstract class BasePlayAndDetailAction extends BaseAction {
    private static int HISTORY_VALUE_FOR_ALBUM_ID = -1;
    private static int HISTORY_VALUE_INVALID = Integer.MIN_VALUE;
    private final String TAG = "openplay/broadcast/BasePlayAndDetailAction";

    abstract String playAndDetail(Context context, Album album);

    public void process(Context context, Intent intent, LoadingCallback loadingCallback) {
        try {
            Log.d("openplay/broadcast/BasePlayAndDetailAction", "process(context,intent)");
            JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(intent.getExtras());
            if (!checkParamsValidity(playDict)) {
                LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "checkParamsValidity is false. ");
                if (loadingCallback != null) {
                    loadingCallback.onFail();
                    LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "loadingCallback.onCancel()...");
                }
            } else if (playDict != null) {
                String videoId = playDict.optString("videoId");
                if (TextUtils.isEmpty(videoId)) {
                    videoId = playDict.optString("vrsAlbumId");
                    LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===videoId====vrsAlbumId:" + videoId);
                }
                String episodeId = playDict.optString("episodeId");
                if (TextUtils.isEmpty(episodeId)) {
                    episodeId = playDict.optString("vrsTvId");
                    LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===episodeId====chnIdStr:" + episodeId);
                }
                String history = playDict.optString("history");
                LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===history====history:" + history);
                int playTime = HISTORY_VALUE_INVALID;
                if (!TextUtils.isEmpty(history)) {
                    long playTimeMilliseconds = Long.MIN_VALUE;
                    try {
                        playTimeMilliseconds = Long.parseLong(history);
                    } catch (NumberFormatException e) {
                        LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "[INVALID-PARAMTER] [action:BasePlayAndDetailAction][reason:invalid field--history:" + history + "][NumberFormatException:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
                    }
                    if (playTimeMilliseconds == -1) {
                        playTime = HISTORY_VALUE_FOR_ALBUM_ID;
                    } else if (playTimeMilliseconds >= 0) {
                        playTime = (playTimeMilliseconds < 0 || playTimeMilliseconds > 1000) ? ((int) playTimeMilliseconds) / 1000 : 1;
                    }
                }
                String actionSuffix = splitAction(context, intent != null ? intent.getAction() : "");
                LogUtils.d("openplay/broadcast/BasePlayAndDetailAction", "===actionSuffix====: " + actionSuffix);
                LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===playTime====playTime:" + playTime);
                if (playTime == HISTORY_VALUE_FOR_ALBUM_ID && actionSuffix.equals("ACTION_DETAIL")) {
                    LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===fetchFullAlbumInfo====videoId = " + videoId + " ;playTime = " + playTime);
                    fetchFullAlbumInfo(context, videoId, playTime, loadingCallback);
                    return;
                }
                LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===fetchFullAlbumInfo====episodeId = " + episodeId + " ;playTime = " + playTime);
                fetchFullAlbumInfo(context, episodeId, playTime, loadingCallback);
            }
        } catch (Exception e2) {
            LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "[UNKNOWN-EXCEPTION] [reason:exception occurred when OpenDetailAction process.][Exception:" + e2.getMessage() + AlbumEnterFactory.SIGN_STR);
            e2.printStackTrace();
            if (loadingCallback != null) {
                loadingCallback.onFail();
                LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "loadingCallback.onFail();");
            }
        }
    }

    private String splitAction(Context context, String action) {
        if (context == null || TextUtils.isEmpty(action)) {
            return "";
        }
        return action.replace(context.getPackageName() + ".action.", "");
    }

    public boolean checkParamsValidity(JSONObject playDict) {
        if (super.checkParamsValidity(playDict)) {
            String videoId = playDict.optString("videoId");
            if (TextUtils.isEmpty(videoId)) {
                videoId = playDict.optString("vrsAlbumId");
                LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===videoId====vrsAlbumId:" + videoId);
            }
            String episodeId = playDict.optString("episodeId");
            if (TextUtils.isEmpty(episodeId)) {
                episodeId = playDict.optString("vrsTvId");
                LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "===episodeId====vrsTvId:" + episodeId);
            }
            Log.d("openplay/broadcast/BasePlayAndDetailAction", "checkParamsValidity ---videoId = " + videoId + "---episodeId = " + episodeId);
            if (!TextUtils.isEmpty(videoId) && !TextUtils.isEmpty(episodeId)) {
                return true;
            }
            LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "[INVALID-PARAMTER] [action:BasePlayAndDetailAction][reason:missing field--videoId|episodeId][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
        }
        return false;
    }

    protected void startPlay(Context context, Album albumInfo) {
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        PlayParams playParam = new PlayParams();
        playParam.sourceType = SourceType.OUTSIDE;
        builder.setPlayParams(playParam);
        builder.setAlbumInfo(albumInfo);
        builder.setPlayOrder(1);
        builder.setFrom("openAPI");
        builder.setBuySource("openAPI");
        builder.setTabSource("其他");
        builder.setClearTaskFlag(true);
        LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "DetailIntentUtils.startPlayer... albumInfo= " + albumInfo.toString() + "builder = " + builder.toString());
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
    }

    void fetchFullAlbumInfo(final Context context, String id, final int playTime, final LoadingCallback loadingCallback) {
        TVApi.albumInfo.call(new IApiCallback<ApiResultAlbum>() {
            public void onSuccess(ApiResultAlbum apiResultAlbum) {
                if (loadingCallback != null) {
                    LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "dealPlayAndDetailAction()--onSuccess---LoadingActivity.isInterrupted()= " + loadingCallback.iscancel());
                    if (loadingCallback.iscancel()) {
                        return;
                    }
                }
                Album album = apiResultAlbum.data;
                if (album == null) {
                    LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "album is null. return");
                    if (loadingCallback != null) {
                        loadingCallback.onFail();
                        return;
                    }
                    return;
                }
                if (playTime > 0) {
                    album.playTime = playTime;
                }
                String page = BasePlayAndDetailAction.this.playAndDetail(context, album);
                LogUtils.i("openplay/broadcast/BasePlayAndDetailAction", "dealDetailAcion()--playTime = " + playTime + " ;album.playTime =  " + album.playTime + " ;album.tvQid = " + album.tvQid + " ;album.qpId = " + album.qpId + " ;album.type = " + album.type + " ;album.toString() = " + album.toString());
                if (loadingCallback != null) {
                    loadingCallback.onSuccess(page);
                }
            }

            public void onException(ApiException arg0) {
                LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "TVApi--IApiCallback--onException = " + arg0.toString());
                if (loadingCallback != null) {
                    if (!NetworkUtils.isNetworkAvaliable()) {
                        LogUtils.e("openplay/broadcast/BasePlayAndDetailAction", "TVApi--IApiCallback--NetworkUtils.isNetworkAvaliable() =false");
                        loadingCallback.onNetworkAvaliable();
                    }
                    loadingCallback.onFail();
                }
            }
        }, id);
    }
}
