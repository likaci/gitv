package com.gala.video.app.epg.ui.multisubject.util;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.IChannelItem;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.AdItemData;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.utils.HomeItemUtils;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectUtils.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class MultiSubjectUtils extends Wrapper {
    private static final String TAG = "EPG/multisubject/MutilSubjectClickUtils";

    public void OnItemClick(Context context, DataSource dataSource, IMultiSubjectInfoModel infoModel) {
        if (context != null && dataSource != null && infoModel != null) {
            ItemData data = (ItemData) dataSource;
            String from = infoModel.getFrom();
            Log.d(TAG, data.toString());
            ChannelLabel label = data.mLabel;
            ItemDataType itemDataType = data.getItemType();
            String tabsrc = "";
            if (TextUtils.isEmpty(HomePingbackSender.getInstance().getTabName())) {
                tabsrc = context.getResources().getString(C0508R.string.other_group);
            } else {
                tabsrc = "tab_" + HomePingbackSender.getInstance().getTabName();
            }
            LogUtils.m1568d(TAG, ">> OnItemClick: itemDataType = " + itemDataType + ",from " + from);
            PlayParams playParams;
            CarouselPlayParamBuilder carouselPlayParamBuilder;
            switch (itemDataType) {
                case ALBUM:
                case VIDEO:
                case LIVE:
                case RECOMMEND:
                case SUPER_ALBUM:
                    playParams = new PlayParams();
                    playParams.h5PlayType = infoModel.getPlayType();
                    playParams.playListId = infoModel.getItemId();
                    ItemUtils.openDetailOrPlay(context, label, data.getTitle(), from, "", playParams);
                    return;
                case LIVE_CHANNEL:
                    ChannelCarousel channelCarousel = new ChannelCarousel();
                    channelCarousel.tableNo = (long) data.getTabNo();
                    channelCarousel.id = StringUtils.parse(data.getLiveId(), 0);
                    channelCarousel.name = data.getTitle();
                    LogUtils.m1568d(TAG, "channelCarousel: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                    carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                    carouselPlayParamBuilder.setChannel(channelCarousel);
                    carouselPlayParamBuilder.setFrom(from);
                    carouselPlayParamBuilder.setTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
                    GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                    return;
                case CAROUSEL:
                    carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                    carouselPlayParamBuilder.setChannel(label.getChannelCarousel());
                    carouselPlayParamBuilder.setFrom(from);
                    carouselPlayParamBuilder.setTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
                    GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                    return;
                case H5:
                    WebIntentParams params = new WebIntentParams();
                    params.pageUrl = data.pageUrl;
                    params.from = from;
                    params.enterType = infoModel.getEnterType();
                    params.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
                    params.buyFrom = infoModel.getBuyFrom();
                    GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                    return;
                case PERSON:
                case STAR:
                    AlbumUtils.startSearchPeoplePage(context, data.getTitle(), label.itemId, from);
                    return;
                case PLAY_LIST:
                    IChannelItem channelItem = label.getResourceItem();
                    String name = "";
                    if (channelItem != null) {
                        String title = data.getTitle();
                        if (!TextUtils.isEmpty(title)) {
                            name = title;
                        } else if (!TextUtils.isEmpty(label.itemPrompt)) {
                            name = label.itemPrompt;
                        } else if (!TextUtils.isEmpty(label.prompt)) {
                            name = label.prompt;
                        } else if (TextUtils.isEmpty(label.itemShortDisplayName)) {
                            name = label.itemName;
                        } else {
                            name = label.itemShortDisplayName;
                        }
                        WebIntentParams webIntentParams = new WebIntentParams();
                        webIntentParams.id = channelItem.plId;
                        webIntentParams.name = name;
                        webIntentParams.from = from;
                        webIntentParams.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
                        if (TextUtils.equals(infoModel.getPlayType(), MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI)) {
                            webIntentParams.resGroupId = infoModel.getItemId();
                        }
                        GetInterfaceTools.getWebEntry().gotoSubject(context, webIntentParams);
                        return;
                    }
                    return;
                case RESOURCE_GROUP:
                    MultiSubjectEnterUtils.start(context, label.itemId, "detail", from);
                    return;
                case TRAILERS:
                    CardModel cardModel = infoModel.getCardModel();
                    List<Album> albumList = new ArrayList();
                    long now = SystemClock.elapsedRealtime();
                    LogRecordUtils.logd(TAG, ">> getItemModelList");
                    for (ItemModel item : cardModel.getItemModelList()) {
                        if (item != null) {
                            albumList.add(item.getData().getVideo());
                        }
                    }
                    LogRecordUtils.logd(TAG, ">> getItemModelList " + (SystemClock.elapsedRealtime() - now) + " ms");
                    playParams = new PlayParams();
                    playParams.continuePlayList = albumList;
                    playParams.playListId = "";
                    playParams.playIndex = infoModel.getPlayIndex();
                    playParams.playListName = "片花卡段";
                    playParams.sourceType = SourceType.DETAIL_TRAILERS;
                    playParams.from = from;
                    BasePlayParamBuilder builder = new BasePlayParamBuilder();
                    builder.setPlayParams(playParams);
                    builder.setClearTaskFlag(false);
                    builder.setBuySource("");
                    builder.setTabSource(tabsrc);
                    LogRecordUtils.logd(TAG, ">> getItemModelList <<");
                    GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
                    return;
                case BANNER_IMAGE_AD:
                    HomeAdPingbackModel model = new HomeAdPingbackModel();
                    model.setH5EnterType(4);
                    model.setH5From("ad_jump");
                    model.setPlFrom("ad_jump");
                    model.setVideoFrom("ad_jump");
                    model.setVideoTabSource(tabsrc);
                    model.setCarouselFrom("ad_jump");
                    model.setCarouselTabSource(tabsrc);
                    HomeItemUtils.onClickAdItem(context, (AdItemData) data, model);
                    return;
                case RECOMMEND_APP:
                    LogRecordUtils.logd(TAG, "OnItemClick: key -> " + data.getRecommendAppKey());
                    PromotionAppInfo promotionAppInfo = null;
                    PromotionMessage promotionMessage = null;
                    if (data.getRecommendAppKey().equals("chinapokerapp")) {
                        promotionMessage = PromotionCache.instance().getChinaPokerAppPromotion();
                        if (promotionMessage != null) {
                            promotionAppInfo = promotionMessage.getDocument().getAppInfo();
                            promotionAppInfo.setAppType(2);
                        }
                    } else if (data.getRecommendAppKey().equals("childapp")) {
                        promotionMessage = PromotionCache.instance().getChildPromotion();
                        if (promotionMessage != null) {
                            promotionAppInfo = promotionMessage.getDocument().getAppInfo();
                            promotionAppInfo.setAppType(1);
                        }
                    }
                    if (promotionMessage != null) {
                        LogRecordUtils.logd(TAG, "OnItemClick: promotionMessage -> " + promotionMessage.toString());
                    }
                    if (!ItemUtils.startApp(context, promotionAppInfo)) {
                        QToast.makeTextAndShow(context, ResourceUtil.getStr(C0508R.string.download_app_start_fail), 3000);
                        return;
                    }
                    return;
                case NONE:
                    return;
                default:
                    return;
            }
        }
    }

    public void resetSubscribeFocusIndex() {
    }
}
