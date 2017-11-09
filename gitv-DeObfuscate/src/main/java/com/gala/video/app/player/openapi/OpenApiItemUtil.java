package com.gala.video.app.player.openapi;

import android.content.Context;
import android.util.Log;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.IChannelItem;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class OpenApiItemUtil {
    public static final String BUY_SOURCE = "openAPI";
    public static final String TAB_SOURCE = "其他";
    private static final String TAG = "OpenApiItemUtil";

    public static void openDetailOrPlay(Context context, ChannelLabel item, String from, int channelId, boolean clearTaskFlag, boolean isPlayList) {
        if (item != null) {
            ResourceType type = item.getType();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "openDetailOrPlay --- ChannelLabel---, type = " + type + ", clearTaskFlag = " + clearTaskFlag);
            }
            switch (type) {
                case COLLECTION:
                    openPlayList(context, item, from, channelId);
                    return;
                case VIDEO:
                case ALBUM:
                    openAlbum(context, item.getVideo(), from, null, clearTaskFlag, isPlayList);
                    return;
                default:
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "openDetailOrPlay --- do nothing !!!!!!!");
                        return;
                    }
                    return;
            }
        }
    }

    public static void openAlbum(Context context, Album album, String from, PlayParams params, boolean clearTaskFlag, boolean isPlayList) {
        openAlbum(context, album, from, params, clearTaskFlag, isPlayList, true);
    }

    public static void openAlbum(Context context, Album album, String from, PlayParams params, boolean clearTaskFlag, boolean isPlayList, boolean continueNextVideo) {
        openAlbum(context, album, from, params, clearTaskFlag, isPlayList, continueNextVideo, false);
    }

    public static void openAlbum(Context context, Album album, String from, PlayParams params, boolean clearTaskFlag, boolean isPlayList, boolean continueNextVideo, boolean hasHistory) {
        switch (GetInterfaceTools.getAlbumInfoHelper().getJumpType(album)) {
            case PLAY:
                goToPlay(context, album, from, params, clearTaskFlag, isPlayList);
                return;
            case DETAILS:
                AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
                builder.setPlayParam(params);
                builder.setAlbumInfo(album);
                builder.setFrom(from);
                builder.setBuySource("openAPI");
                builder.setTabSource("其他");
                builder.setContinueNextVideo(continueNextVideo);
                builder.setClearTaskFlag(clearTaskFlag);
                GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(context, builder);
                return;
            default:
                return;
        }
    }

    private static void openPlayList(Context context, ChannelLabel item, String from, int channelId) {
        IChannelItem label = item.getResourceItem();
        if (label != null) {
            String name = "";
            if (!StringUtils.isEmpty(item.itemPrompt)) {
                name = item.itemPrompt;
            } else if (!StringUtils.isEmpty(item.prompt)) {
                name = item.prompt;
            } else if (StringUtils.isEmpty(item.itemShortDisplayName)) {
                name = item.itemName;
            } else {
                name = item.itemShortDisplayName;
            }
            try {
                GetInterfaceTools.getWebEntry().gotoSubject(context, label.plId, name, from, "openAPI");
                return;
            } catch (Exception e) {
                Log.e(TAG, "", e);
                return;
            }
        }
        LogUtils.m1571e(TAG, "startPlayList --- label is null ---");
    }

    private static void goToPlay(Context context, Album album, String from, PlayParams params, boolean clearTaskFlag, boolean isPlayList) {
        BasePlayParamBuilder builder;
        if (params == null) {
            builder = new BasePlayParamBuilder();
            new PlayParams().sourceType = SourceType.COMMON;
            builder.setPlayParams(params);
            builder.setAlbumInfo(album);
            builder.setFrom(from);
            builder.setClearTaskFlag(clearTaskFlag);
            builder.setBuySource("openAPI");
            builder.setPlayParams(null);
            if (album.isSeries() && !album.isSourceType()) {
                builder.setPlayOrder(album.order);
            }
            GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
            return;
        }
        builder = new BasePlayParamBuilder();
        params.from = from;
        builder.setPlayParams(params);
        builder.setBuySource("openAPI");
        builder.setTabSource("其他");
        builder.setClearTaskFlag(clearTaskFlag);
        if (!isPlayList) {
            builder.setPlayOrder(album.order);
            builder.setAlbumInfo(album);
        }
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
    }
}
