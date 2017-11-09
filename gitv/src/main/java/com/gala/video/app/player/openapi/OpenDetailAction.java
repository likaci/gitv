package com.gala.video.app.player.openapi;

import android.content.Context;
import android.text.TextUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.IntentConfig.BroadcastAction;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.BroadcastManager;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.JumpKind;
import org.json.JSONObject;

public class OpenDetailAction extends BasePlayAndDetailAction {
    private final String TAG = "openplay/broadcast/OpenDetailAction";

    String playAndDetail(Context context, Album album) {
        LogUtils.i("openplay/broadcast/OpenDetailAction", "===playAndDetail===");
        String page = "";
        JumpKind jumpType = GetInterfaceTools.getAlbumInfoHelper().getJumpType(album);
        LogUtils.i("openplay/broadcast/OpenDetailAction", "jumpType =  " + jumpType);
        switch (jumpType) {
            case PLAY:
            case PLAY_LIST:
                if (hasPlayPermission()) {
                    page = "playvideo";
                    LogUtils.i("openplay/broadcast/OpenDetailAction", "dealDetailAcion()--hasPlayPermission --startPlay");
                    startPlay(context.getApplicationContext(), album);
                    return page;
                }
                page = "home";
                LogUtils.i("openplay/broadcast/OpenDetailAction", "dealDetailAcion()--no PlayPermission --startHomeActivity");
                startHomeActivity(context);
                return page;
            case DETAILS:
                page = "detail";
                AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
                builder.setAlbumInfo(album);
                builder.setFrom("openAPI");
                builder.setBuySource("openAPI");
                builder.setTabSource("其他");
                builder.setClearTaskFlag(true);
                GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(context.getApplicationContext(), builder);
                return page;
            default:
                return page;
        }
    }

    boolean hasPlayPermission() {
        if (BroadcastManager.instance().findBroadcastActionByKey(BroadcastAction.ACTION_PLAY_VIDEO) == null) {
            return false;
        }
        return true;
    }

    void startHomeActivity(Context context) {
        CreateInterfaceTools.createEpgEntry().startHomeActivity(context, false);
    }

    public boolean checkParamsValidity(JSONObject playDict) {
        if (super.checkParamsValidity(playDict)) {
            String chnIdStr = playDict.optString("chnId");
            if (TextUtils.isEmpty(chnIdStr)) {
                chnIdStr = playDict.optString("vrsChnId");
                LogUtils.i("openplay/broadcast/OpenDetailAction", "===vrsChnId====chnIdStr:" + chnIdStr);
            }
            if (TextUtils.isEmpty(chnIdStr)) {
                LogUtils.e("openplay/broadcast/OpenDetailAction", "[INVALID-PARAMTER] [action:ACTION_DETAIL][reason:missing field--chnId][playInfo:" + playDict.toString() + AlbumEnterFactory.SIGN_STR);
            } else {
                LogUtils.i("openplay/broadcast/OpenDetailAction", "===checkParamsValidity===chnIdStr ==" + chnIdStr);
                return true;
            }
        }
        return false;
    }
}
