package com.gala.video.app.epg.home.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.constants.ChannelId;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.component.PingbackDataSource;
import com.gala.video.app.epg.home.data.AdItemData;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataModel.SearchRecordType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataUtils;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.widget.tabmanager.TabManagerActivity;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.utils.TagUtils;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.subjectreview.QSubjectUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsParams;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.push.mqttv3.internal.ClientDefaults;
import java.util.ArrayList;
import java.util.List;

public class HomeItemUtils {
    private static String TAG = "HomeItemUtils";

    public static void onItemClick(Context context, PingbackDataSource itemPingbackDataSource, PingbackDataSource cardPingbackDataSource, PingbackDataSource tabPingbackDataSource, HomePingbackDataModel homePingbackDataModel) {
        if (itemPingbackDataSource != null && context != null && cardPingbackDataSource != null && tabPingbackDataSource != null) {
            ItemData data = itemPingbackDataSource.mData instanceof ItemData ? (ItemData) itemPingbackDataSource.mData : null;
            if (data != null) {
                try {
                    SearchRecordType mSearchRecordType = homePingbackDataModel.getSearchRecordType();
                    Log.d(TAG, data.toString());
                    ChannelLabel label = data.mLabel;
                    ItemDataType itemDataType = data.getItemType();
                    if (NetworkStatePresenter.getInstance().checkStateIllegal() || itemDataType == ItemDataType.APP) {
                        PlayParams playParams;
                        CarouselPlayParamBuilder carouselPlayParamBuilder;
                        switch (itemDataType) {
                            case ALBUM:
                            case VIDEO:
                            case LIVE:
                                ItemUtils.openDetailOrPlay(context, label, data.getTitle(), getPingbackS2(PingBackUtils.getTabSrc()), "", null);
                                playParams = null;
                                return;
                            case LIVE_CHANNEL:
                                ChannelCarousel channelCarousel = new ChannelCarousel();
                                channelCarousel.tableNo = (long) data.getTabNo();
                                channelCarousel.id = StringUtils.parse(data.getLiveId(), 0);
                                channelCarousel.name = data.getTitle();
                                LogUtils.d(TAG, "channelCarousel: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                                carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                                carouselPlayParamBuilder.setChannel(channelCarousel);
                                carouselPlayParamBuilder.setFrom(getPingbackS2(PingBackUtils.getTabSrc()));
                                carouselPlayParamBuilder.setTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
                                GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                                playParams = null;
                                return;
                            case CAROUSEL:
                                carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                                carouselPlayParamBuilder.setChannel(label.getChannelCarousel());
                                carouselPlayParamBuilder.setFrom(getPingbackS2(PingBackUtils.getTabSrc()));
                                carouselPlayParamBuilder.setTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
                                GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                                playParams = null;
                                return;
                            case DAILY:
                                int pos = data.getNewParamsPos();
                                List<DailyLabelModel> dailyLabelModelList = data.getNewParams();
                                List<TabDataItem> tabDataItemList = new ArrayList();
                                if (dailyLabelModelList != null && dailyLabelModelList.size() > 0) {
                                    for (DailyLabelModel daily : dailyLabelModelList) {
                                        TabDataItem tab = CreateInterfaceTools.createModelHelper().convertToTabDataItem(daily);
                                        if (tab != null) {
                                            tabDataItemList.add(tab);
                                        }
                                    }
                                }
                                if (tabDataItemList != null) {
                                    if (tabDataItemList.size() > pos) {
                                        NewsParams newsParams = new NewsParams(tabDataItemList, pos);
                                        NewsDetailPlayParamBuilder builder = new NewsDetailPlayParamBuilder();
                                        builder.setFrom("news");
                                        builder.setChannelName(GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getDailyName());
                                        builder.setNewParams(newsParams);
                                        builder.setTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
                                        GetInterfaceTools.getPlayerPageProvider().startNewsDetailPlayerPage(context, builder);
                                        playParams = null;
                                        return;
                                    }
                                }
                                break;
                            case H5:
                                WebIntentParams params = new WebIntentParams();
                                params.pageUrl = data.pageUrl;
                                params.from = getPingbackS2(PingBackUtils.getTabSrc());
                                params.enterType = 13;
                                params.buyFrom = "rec";
                                GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                                playParams = null;
                                return;
                            case PERSON:
                                AlbumUtils.startSearchPeoplePage(context, data.getTitle(), label.itemId, HomePingbackSender.getInstance().getTabName() + "_明星");
                                playParams = null;
                                return;
                            case PLAY_LIST:
                                playParams = new PlayParams();
                                playParams.playListId = label.id;
                                ItemUtils.openDetailOrPlay(context, label, data.getTitle(), getPingbackS2(PingBackUtils.getTabSrc()), "", playParams);
                                return;
                            case RECORD:
                                if (mSearchRecordType != null) {
                                    switch (mSearchRecordType) {
                                        case NONE:
                                            ItemUtils.startVideoPlay(context, data.getHistoryInfoAlbum(), "8", "");
                                            playParams = null;
                                            return;
                                        case RECORD:
                                            AlbumUtils.startFootPlayhistoryPage(context);
                                            playParams = null;
                                            return;
                                        case SEARCH:
                                            playParams = null;
                                            return;
                                        default:
                                            playParams = null;
                                            return;
                                    }
                                }
                                break;
                            case SEARCH:
                                SearchEnterUtils.startSearchActivity(context);
                                playParams = null;
                                return;
                            case SEARCH_RECORD:
                                if (mSearchRecordType != null) {
                                    switch (mSearchRecordType) {
                                        case NONE:
                                            ItemUtils.startVideoPlay(context, data.getHistoryInfoAlbum(), "8", "");
                                            playParams = null;
                                            return;
                                        case RECORD:
                                            AlbumUtils.startFootPlayhistoryPage(context);
                                            playParams = null;
                                            return;
                                        case SEARCH:
                                            SearchEnterUtils.startSearchActivity(context);
                                            playParams = null;
                                            return;
                                        default:
                                            playParams = null;
                                            return;
                                    }
                                }
                                break;
                            case SETTING:
                                playParams = null;
                                return;
                            case TV_TAG:
                                TVTags tvTags = label.itemKvs.getTVTag();
                                if (tvTags != null) {
                                    Channel channel = AlbumInfoFactory.getChannelByChannelId(tvTags.channelId);
                                    List<TwoLevelTag> twoLevelTagList = null;
                                    if (channel != null) {
                                        twoLevelTagList = channel.tags;
                                    }
                                    AlbumUtils.startChannelMultiDataPage(context, TagUtils.getIds(tvTags.tags), tvTags.channelId, HomePingbackSender.getInstance().getTabName() + "_" + HomePingbackDataUtils.getListPageTagName(tvTags, twoLevelTagList, "_"), "");
                                    playParams = null;
                                    return;
                                }
                                LogUtils.w(TAG, "onItemClick, itemDataType = TV_TAG, TVTags(ChannelLabel.ItemKvs.getTVTag()) data is null");
                                playParams = null;
                                return;
                            case TV_TAG_ALL:
                                TVTags tvAllTags = label.itemKvs.getTVTag();
                                if (tvAllTags != null) {
                                    AlbumUtils.startChannelMultiDataPage(context, TagUtils.getIds(tvAllTags.tags), tvAllTags.channelId, HomePingbackSender.getInstance().getTabName() + "_" + label.itemKvs.tvShowName, "");
                                    playParams = null;
                                    return;
                                }
                                break;
                            case CHANNEL:
                                String from = HomePingbackSender.getInstance().getTabName() + "_" + data.getTitle();
                                String tabrec = "tab_" + HomePingbackSender.getInstance().getTabName();
                                if (data.getChannel() != null) {
                                    AlbumUtils.startChannelPage(context, data.getChannel(), from);
                                    playParams = null;
                                    return;
                                } else if (data.getChnId() == ChannelId.CHANNEL_ID_CAROUSEL) {
                                    carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                                    carouselPlayParamBuilder.setChannel(null);
                                    carouselPlayParamBuilder.setFrom(from);
                                    carouselPlayParamBuilder.setTabSource(tabrec);
                                    GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                                    playParams = null;
                                    return;
                                } else {
                                    AlbumUtils.startChannelPage(context, data.getChnId(), from, "", true);
                                    playParams = null;
                                    return;
                                }
                            case RESOURCE_GROUP:
                                MultiSubjectEnterUtils.start(context, label.itemId, "tabrec", HomePingbackSender.getInstance().getTabName() + "_rec");
                                playParams = null;
                                return;
                            case PLST_GROUP:
                                QSubjectUtils.startQSubjectActivity(context, data.plId, HomePingbackSender.getInstance().getTabName() + "_专题回顾");
                                playParams = null;
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
                                    }
                                }
                                if (promotionMessage != null) {
                                    LogRecordUtils.logd(TAG, "OnItemClick: promotionMessage -> " + promotionMessage.toString());
                                }
                                if (!ItemUtils.startApp(context, promotionAppInfo)) {
                                    QToast.makeTextAndShow(context, ResourceUtil.getStr(R.string.download_app_start_fail), 3000);
                                    playParams = null;
                                    return;
                                }
                                break;
                            case NONE:
                                playParams = null;
                                return;
                        }
                        playParams = null;
                    }
                } catch (Throwable e) {
                    LogUtils.d(TAG, "click exception ", e);
                }
            }
        }
    }

    private static String getPingbackS2(String str) {
        String s2 = "";
        try {
            s2 = str.substring(4) + "_rec";
        } catch (Exception e) {
        }
        return s2;
    }

    public static void onClickAdItem(Context context, AdItemData adItemData, HomeAdPingbackModel model) {
        if (adItemData == null) {
            LogUtils.e(TAG, "onClickAdItem, ad item data is null!!!");
            return;
        }
        if (model == null) {
            LogUtils.w(TAG, "onClickAdItem, home ad pingback model is null");
        }
        AdClickType adClickType = adItemData.getAdClickType();
        if (adClickType == null) {
            LogUtils.e(TAG, "onClickAdItem, ad click type is null!!!");
            onClickForNoFocusImageAdData(context);
            return;
        }
        try {
            switch (adClickType) {
                case DEFAULT:
                case H5:
                    if (StringUtils.isEmpty(adItemData.getClickThroughInfo())) {
                        onClickForNoFocusImageAdData(context);
                        return;
                    }
                    WebIntentParams params = new WebIntentParams();
                    params.pageUrl = adItemData.getClickThroughInfo();
                    params.enterType = model != null ? model.getH5EnterType() : HomeAdPingbackModel.DEFAULT_H5_ENTER_TYPE;
                    params.from = model != null ? model.getH5From() : "";
                    GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                    return;
                case IMAGE:
                    CharSequence imgUrl = adItemData.getClickThroughInfo();
                    if (StringUtils.isEmpty(imgUrl)) {
                        onClickForNoFocusImageAdData(context);
                        return;
                    }
                    CharSequence localImagePath = DownloaderAPI.getDownloader().getLocalPath(new FileRequest(imgUrl));
                    LogUtils.w(TAG, "onClickAd, local image path, " + localImagePath);
                    if (StringUtils.isEmpty(localImagePath)) {
                        onClickForNoFocusImageAdData(context);
                        return;
                    }
                    Intent intent = new Intent(ActionSet.ACT_AD_IMAGE_SHOW);
                    intent.putExtra("adimageUrl", imgUrl);
                    PageIOUtils.activityIn(context, intent);
                    return;
                case PLAY_LIST:
                    if (StringUtils.isEmpty(adItemData.getPlId())) {
                        onClickForNoFocusImageAdData(context);
                        return;
                    } else {
                        GetInterfaceTools.getWebEntry().gotoSubject(context, adItemData.getPlId(), "", model != null ? model.getPlFrom() : "", "");
                        return;
                    }
                case VIDEO:
                    Album albumInfo = new Album();
                    albumInfo.qpId = adItemData.getAlbumId();
                    albumInfo.tvQid = adItemData.getTvId();
                    BasePlayParamBuilder builder = new BasePlayParamBuilder();
                    PlayParams playParam = new PlayParams();
                    playParam.isHomeAd = true;
                    playParam.sourceType = SourceType.OUTSIDE;
                    builder.setPlayParams(playParam);
                    builder.setAlbumInfo(albumInfo);
                    builder.setPlayOrder(0);
                    builder.setClearTaskFlag(false);
                    builder.setFrom(model != null ? model.getVideoFrom() : "");
                    builder.setBuySource(model != null ? model.getVideoBuySource() : "");
                    builder.setTabSource(model != null ? model.getVideoTabSource() : "");
                    GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
                    return;
                case CAROUSEL:
                    ChannelCarousel channelCarousel = new ChannelCarousel();
                    channelCarousel.tableNo = Long.parseLong(adItemData.getCarouselNo());
                    channelCarousel.id = Long.parseLong(adItemData.getCarouselId());
                    channelCarousel.name = adItemData.getCarouselName();
                    LogUtils.d(TAG, "onClickAdItem, channelCarousel: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                    CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                    carouselPlayParamBuilder.setChannel(channelCarousel);
                    carouselPlayParamBuilder.setFrom(model != null ? model.getCarouselFrom() : "");
                    carouselPlayParamBuilder.setTabSource(model != null ? model.getCarouselTabSource() : "");
                    GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                    return;
                default:
                    onClickForNoFocusImageAdData(context);
                    return;
            }
        } catch (NumberFormatException e) {
            LogUtils.e(TAG, "onClickAdItem, NumberFormatException: " + e);
            onClickForNoFocusImageAdData(context);
        } catch (Exception e2) {
            LogUtils.e(TAG, "onClickAdItem, exception: " + e2);
            onClickForNoFocusImageAdData(context);
        }
    }

    public static void onTabSettingClick(Context context, String from) {
        if (!NetworkStatePresenter.getInstance().checkStateIllegal()) {
            return;
        }
        if (ListUtils.isEmpty(TabProvider.getInstance().getTabInfo()) || ListUtils.isEmpty(TabProvider.getInstance().getTabHideInfo())) {
            QToast.makeTextAndShow(context, R.string.desktop_data_acquire, 3000);
            return;
        }
        Intent intent = new Intent(context, TabManagerActivity.class);
        intent.putExtra("from", from);
        if (!(context instanceof Activity)) {
            intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
        }
        PageIOUtils.activityIn(context, intent);
    }

    private static void onClickForNoFocusImageAdData(Context context) {
        QToast.makeTextAndShow(context, R.string.screen_saver_click_error_hint, 2000);
    }
}
