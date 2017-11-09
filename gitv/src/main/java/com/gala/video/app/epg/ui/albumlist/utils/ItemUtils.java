package com.gala.video.app.epg.ui.albumlist.utils;

import android.content.Context;
import android.text.TextUtils;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.tvapi.vrs.model.IChannelItem;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.appdownload.utils.AppUtils;
import com.gala.video.app.epg.home.component.item.corner.LiveCornerUtils;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.LivePlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    private static final String TAG = "EPG/album4/ItemUtils";

    public static void openDetailOrPlay(Context context, IData info, String from, PlayParams params, String buySource) {
        Album album = info.getAlbum();
        if (DataInfoProvider.isCardShowing(info)) {
            int cardType = DataInfoProvider.getCardType(album);
            LogUtils.e(TAG, "openDetailOrPlay --- card---cardType=" + cardType);
            switch (cardType) {
                case 1:
                case 3:
                case 4:
                    goToDetailPage(context, album, from, params, buySource);
                    return;
                case 2:
                case 5:
                    GetInterfaceTools.getWebEntry().gotoSubjectNotice(context, album.prevues, from, buySource);
                    return;
                case 6:
                    openDetailOrPlay(context, album, from, params, buySource);
                    return;
                case 7:
                    gotoSubject(context, album.qpId, album.name, from, buySource);
                    return;
                default:
                    return;
            }
        }
        openDetailOrPlay(context, album, from, params, buySource);
    }

    public static void openDetailOrPlay(Context context, Album album, String from, PlayParams params, String buySource) {
        if (album != null) {
            LogUtils.e(TAG, "openDetailOrPlay --- Album---from=" + from + "---buysource=" + buySource);
            onClickInAlbum(context, album, from, params, false, buySource);
        }
    }

    public static void openDetailOrPlay(Context context, ChannelLabel item, String title, String from, String buySource, PlayParams params) {
        if (item != null) {
            LogUtils.e(TAG, "openDetailOrPlay --- ChannelLabel---from=" + from + "---buysource=" + buySource);
            ResourceType type = item.getType();
            if (ResourceType.COLLECTION.equals(type)) {
                IChannelItem label = item.getResourceItem();
                if (label != null) {
                    String name = "";
                    if (!TextUtils.isEmpty(title)) {
                        name = title;
                    } else if (!TextUtils.isEmpty(item.itemPrompt)) {
                        name = item.itemPrompt;
                    } else if (!TextUtils.isEmpty(item.prompt)) {
                        name = item.prompt;
                    } else if (TextUtils.isEmpty(item.itemShortDisplayName)) {
                        name = item.itemName;
                    } else {
                        name = item.itemShortDisplayName;
                    }
                    gotoSubject(context, label.plId, name, from, buySource);
                    return;
                }
                LogUtils.e(TAG, "openDetailOrPlay --- label is null ---");
            } else if (ResourceType.VIDEO.equals(type) || ResourceType.ALBUM.equals(type)) {
                onClickInAlbum(context, item.getVideo(), from, params, false, buySource);
            } else if (ResourceType.LIVE.equals(type)) {
                if (LiveCornerUtils.isLiveEndData(item)) {
                    QToast.makeTextAndShow(context, R.string.live_end, 500);
                    return;
                }
                List liveList = item.getLiveAlbumList();
                if (ListUtils.isEmpty(liveList)) {
                    LogUtils.e(TAG, "openDetailOrPlay ---ResourceType.LIVE--- liveList = null");
                    return;
                }
                Album album = (Album) liveList.get(0);
                ArrayList<Album> flowerList = (ArrayList) item.getLiveFlowerList();
                if (album == null) {
                    LogUtils.e(TAG, "openDetailOrPlay ---ResourceType.LIVE--- album = null");
                } else if (StringUtils.isEmpty(album.tv_livecollection)) {
                    LivePlayParamBuilder builder = new LivePlayParamBuilder();
                    builder.setLiveAlbum(album).setFlowerList(flowerList).setFrom(from).setBuySource(buySource).setTabSource(PingBackUtils.getTabSrc());
                    GetInterfaceTools.getPlayerPageProvider().startLivePlayerPage(context, builder);
                } else {
                    LogUtils.e(TAG, "openDetailOrPlay ---album.tv_livecollection:" + album.tv_livecollection);
                    GetInterfaceTools.getWebEntry().gotoSubjectForLive(context, album, flowerList, from, buySource);
                }
            } else if (ResourceType.DIY.equals(type)) {
                WebIntentParams w = new WebIntentParams();
                w.pageUrl = item.itemPageUrl;
                w.enterType = 13;
                w.buyFrom = "";
                w.from = from;
                GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, w);
            } else if (ResourceType.RESOURCE_GROUP.equals(type)) {
                MultiSubjectEnterUtils.start(context, item.itemId, "listrec", from);
            } else {
                LogUtils.e(TAG, "openDetailOrPlay --- do nothing !!!!!!!channellabel.type=" + type);
            }
        }
    }

    public static void openDetailOrPlay(Context context, ChannelPlayListLabel item, String from, String buySource) {
        if (item != null) {
            LogUtils.e(TAG, "openDetailOrPlay --- ChannelPlayListLabel---");
            gotoSubject(context, item.id, item.name, from, buySource);
        }
    }

    public static void openDetailOrPlayForBodan(Context context, Album item, String from, PlayParams params, String buySource) {
        if (item != null) {
            LogUtils.e(TAG, "openDetailOrPlayForBodan --- Album---");
            onClickInAlbum(context, item, from, params, true, buySource);
        }
    }

    public static void gotoSubject(Context context, String id, String name, String from, String buySource) {
        GetInterfaceTools.getWebEntry().gotoSubject(context, id, name, from, buySource);
    }

    private static void onClickInAlbum(Context context, Album album, String from, PlayParams params, boolean isBodan, String buySource) {
        switch (GetInterfaceTools.getAlbumInfoHelper().getJumpType(album)) {
            case PLAY:
                goToPlay(context, album, from, params, isBodan, buySource);
                return;
            case DETAILS:
                goToDetailPage(context, album, from, params, buySource);
                return;
            case PLAY_LIST:
                gotoSubject(context, album.qpId, album.name, from, buySource);
                return;
            default:
                return;
        }
    }

    public static void openPlayForBodan(Context context, Album album, String from, PlayParams params, String buySource) {
        goToPlay(context, album, from, params, true, buySource);
    }

    private static void goToDetailPage(Context context, Album album, String from, PlayParams params, String buySource) {
        AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
        builder.setAlbumInfo(album);
        builder.setFrom(from);
        builder.setBuySource(buySource);
        builder.setTabSource(PingBackUtils.getTabSrc());
        builder.setPlayParam(params);
        GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(context, builder);
    }

    private static void goToPlay(Context context, Album album, String from, PlayParams params, boolean isInBodan, String buySource) {
        BasePlayParamBuilder builder;
        if (params == null) {
            builder = new BasePlayParamBuilder();
            PlayParams playParam = new PlayParams();
            playParam.sourceType = SourceType.COMMON;
            builder.setPlayParams(playParam);
            builder.setTabSource(PingBackUtils.getTabSrc());
            builder.setFrom(from);
            builder.setAlbumInfo(album);
            if (album.isSeries() && !album.isSourceType()) {
                builder.setPlayOrder(album.order);
            }
            GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
            return;
        }
        params.from = from;
        builder = new BasePlayParamBuilder();
        builder.setPlayParams(params);
        builder.setClearTaskFlag(false);
        builder.setBuySource(buySource);
        builder.setTabSource(PingBackUtils.getTabSrc());
        if (!isInBodan) {
            builder.setPlayOrder(album.order);
            builder.setAlbumInfo(album);
        }
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
    }

    public static void startVideoPlay(Context context, Album mAlbum, String from, String buySource) {
        if (mAlbum == null) {
            LogUtils.e(TAG, "startVideoPlay mAlbum is null!");
            return;
        }
        switch (GetInterfaceTools.getAlbumInfoHelper().getHistoryJumpKind(mAlbum)) {
            case HISTORY_PLAY:
                BasePlayParamBuilder builder = new BasePlayParamBuilder();
                PlayParams playParam = new PlayParams();
                playParam.sourceType = SourceType.COMMON;
                builder.setPlayParams(playParam);
                builder.setAlbumInfo(mAlbum);
                builder.setBuySource(buySource);
                builder.setTabSource(PingBackUtils.getTabSrc());
                builder.setFrom(from);
                GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
                return;
            case HISTORY_DETAILS:
                goToDetailPage(context, mAlbum, from, null, buySource);
                return;
            default:
                LogUtils.e(TAG, "startVideoPlay --- do nothing !!!!!!!");
                return;
        }
    }

    public static boolean startApp(Context context, PromotionAppInfo promotionAppInfo) {
        if (promotionAppInfo == null) {
            LogRecordUtils.loge(TAG, "startApp promotionAppInfo is null.");
            return false;
        }
        LogRecordUtils.logd(TAG, "startApp: promotionAppInfo -> " + promotionAppInfo.toString());
        if (AppUtils.isInstalled(context, promotionAppInfo.getAppPckName())) {
            LogRecordUtils.logd(TAG, "startApp: installed");
            return AppUtils.startApp(context, promotionAppInfo.getAppPckName());
        }
        LogRecordUtils.logd(TAG, "startApp: downloadApp");
        return AppUtils.downloadApp(context, promotionAppInfo);
    }
}
