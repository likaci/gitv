package com.gala.video.app.epg.home.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.constants.ChannelId;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataUtils;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.widget.menufloatlayer.MenuFloatLayerSettingActivity;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.utils.TagUtils;
import com.gala.video.app.epg.ui.applist.AppListUtils;
import com.gala.video.app.epg.ui.imsg.MsgCenterActivity;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.setting.ConcernWeChatActivity;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.app.epg.ui.setting.utils.UpgradeSettingUtils;
import com.gala.video.app.epg.ui.solotab.SoloTabEnterUtils;
import com.gala.video.app.epg.ui.subjectreview.QSubjectUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.SysPropUtils;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsParams;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.IActionJump.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionAppInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.pingback.AlbumDetailPingbackUtils;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackUtils;
import com.gala.video.lib.share.uikit.action.data.AppActionData;
import com.gala.video.lib.share.uikit.action.data.CommonAdData;
import com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel;
import com.gala.video.lib.share.uikit.action.model.ApplicationActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.action.model.CarouseHistoryActionModel;
import com.gala.video.lib.share.uikit.action.model.CarouselActionModel;
import com.gala.video.lib.share.uikit.action.model.ChannelActionModel;
import com.gala.video.lib.share.uikit.action.model.DailyActionModel;
import com.gala.video.lib.share.uikit.action.model.H5ActionModel;
import com.gala.video.lib.share.uikit.action.model.LiveChannelActionModel;
import com.gala.video.lib.share.uikit.action.model.PersonActionModel;
import com.gala.video.lib.share.uikit.action.model.PlayListActionModel;
import com.gala.video.lib.share.uikit.action.model.PlstGroupActionModel;
import com.gala.video.lib.share.uikit.action.model.RecommendAppActionModel;
import com.gala.video.lib.share.uikit.action.model.RecordActionModel;
import com.gala.video.lib.share.uikit.action.model.ResourceGroupActionModel;
import com.gala.video.lib.share.uikit.action.model.SettingsActionModel;
import com.gala.video.lib.share.uikit.action.model.StarActionModel;
import com.gala.video.lib.share.uikit.action.model.SubscribeBtnActionModel;
import com.gala.video.lib.share.uikit.action.model.TVTagActionModel;
import com.gala.video.lib.share.uikit.action.model.UcenterRecordAllActionModel;
import com.gala.video.lib.share.uikit.action.model.VipH5ActionModel;
import com.gala.video.lib.share.uikit.action.model.VipVideoActionModel;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.Item.TitleBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class ActionJump extends Wrapper {
    private static final String TAG = ActionJump.class.getSimpleName();
    protected static boolean sDebugable;

    static {
        boolean z = true;
        if (SysPropUtils.getInt("log.action.debug", 0) != 1) {
            z = false;
        }
        sDebugable = z;
    }

    public void onItemClickAD(Context context, CommonAdData adData) {
        if (adData != null) {
            switch (adData.getItemDataType()) {
                case FOCUS_IMAGE_AD:
                    onClickForFocusImageAd(context, adData);
                    if (sDebugable) {
                        LogUtils.m1568d(TAG, "onItemClickAD, send focus image ad click pingback, adId = " + adData.getAdId());
                        return;
                    }
                    return;
                case BANNER_IMAGE_AD:
                    onClickForBannerImageAd(context, adData);
                    if (sDebugable) {
                        LogUtils.m1568d(TAG, "onItemClickAD, send banner image ad click pingback, adId = " + adData.getAdId());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public void onItemClick(Context context, BaseActionModel actionModel) {
        if (actionModel != null && context != null) {
            try {
                ItemDataType itemDataType = actionModel.getItemType();
                NetworkStatePresenter.getInstance().setContext(context);
                if (NetworkStatePresenter.getInstance().checkStateIllegal() || itemDataType == ItemDataType.APP) {
                    Channel channel;
                    List<TwoLevelTag> twoLevelTagList;
                    String tagName;
                    String title;
                    switch (itemDataType) {
                        case ALBUM:
                        case VIDEO:
                        case LIVE:
                            AlbumVideoLiveActionModel avlModel = (AlbumVideoLiveActionModel) actionModel;
                            ItemUtils.openDetailOrPlay(context, avlModel.getLabel(), avlModel.getTitle(), PingbackUtils.getPageS2(context, "_rec"), PingbackUtils.getPageS1(context), PingbackUtils.getPlayParmas(context, avlModel.getIntentModel()));
                            return;
                        case TRAILERS:
                            AlbumVideoLiveActionModel avlModelTra = (AlbumVideoLiveActionModel) actionModel;
                            String tabsrc = AlbumDetailPingbackUtils.getInstance().getTabSrc();
                            PlayParams playParams = PingbackUtils.getPlayParmas(context, avlModelTra.getIntentModel());
                            if (playParams == null) {
                                playParams = new PlayParams();
                            }
                            playParams.continuePlayList = ((AlbumVideoLiveActionModel) actionModel).getIntentModel().getAlbumList();
                            playParams.playListId = "";
                            playParams.playIndex = ((AlbumVideoLiveActionModel) actionModel).getIntentModel().getPlayIndex();
                            playParams.playListName = "片花卡段";
                            String sourceType = ((AlbumVideoLiveActionModel) actionModel).getIntentModel().getSourceType();
                            if (Source.TRAILERS.equals(sourceType)) {
                                playParams.sourceType = SourceType.DETAIL_TRAILERS;
                                playParams.isDetailTrailer = true;
                            } else if (Source.ABOUT_TOPIC.equals(sourceType)) {
                                playParams.sourceType = SourceType.DETAIL_RELATED;
                                playParams.isDetailRelated = true;
                            }
                            playParams.from = PingbackUtils.getPageS2(context, null);
                            BasePlayParamBuilder playerbuilder = new BasePlayParamBuilder();
                            playerbuilder.setPlayParams(playParams);
                            playerbuilder.setClearTaskFlag(false);
                            playerbuilder.setBuySource("");
                            playerbuilder.setTabSource(tabsrc);
                            LogRecordUtils.logd(TAG, ">> getItemModelList <<");
                            GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, playerbuilder);
                            return;
                        case LIVE_CHANNEL:
                            ChannelLabel labelLc;
                            if (actionModel instanceof LiveChannelActionModel) {
                                labelLc = ((LiveChannelActionModel) actionModel).getLabel();
                            } else {
                                labelLc = ((CarouseHistoryActionModel) actionModel).getLabel();
                            }
                            ChannelCarousel channelCarousel = new ChannelCarousel();
                            channelCarousel.tableNo = (long) labelLc.tableNo;
                            channelCarousel.id = StringUtils.parse(labelLc.itemId, 0);
                            channelCarousel.name = DataBuildTool.getPrompt(labelLc);
                            if (sDebugable) {
                                LogUtils.m1568d(TAG, "channelCarousel: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                            }
                            String carouselS2 = PingbackUtils.getPageS2(context, "_rec");
                            CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                            carouselPlayParamBuilder.setChannel(channelCarousel);
                            carouselPlayParamBuilder.setFrom(carouselS2);
                            carouselPlayParamBuilder.setTabSource(PingbackUtils.getTabSource(context));
                            GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                            return;
                        case CAROUSEL:
                            ChannelLabel labelc = ((CarouselActionModel) actionModel).getLabel();
                            String carouselSmallWindowS2 = PingbackUtils.getPageS2(context, "_rec");
                            CarouselPlayParamBuilder carouselPlayParamBuilderC = new CarouselPlayParamBuilder();
                            carouselPlayParamBuilderC.setChannel(labelc.getChannelCarousel());
                            carouselPlayParamBuilderC.setFrom(carouselSmallWindowS2);
                            carouselPlayParamBuilderC.setTabSource(PingbackUtils.getTabSource(context));
                            GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilderC);
                            return;
                        case DAILY:
                            DailyActionModel dailyActionModel = (DailyActionModel) actionModel;
                            int pos = dailyActionModel.getNewParamsPos();
                            List<DailyLabelModel> dailyLabelModelList = dailyActionModel.getDailyLabelModelList();
                            List<TabDataItem> tabDataItemList = new ArrayList();
                            if (dailyLabelModelList != null && dailyLabelModelList.size() > 0) {
                                for (DailyLabelModel daily : dailyLabelModelList) {
                                    TabDataItem tab = CreateInterfaceTools.createModelHelper().convertToTabDataItem(daily);
                                    if (tab != null) {
                                        tabDataItemList.add(tab);
                                    }
                                }
                            }
                            if (tabDataItemList != null && tabDataItemList.size() > pos) {
                                NewsParams newsParams = new NewsParams(tabDataItemList, pos);
                                NewsDetailPlayParamBuilder builder = new NewsDetailPlayParamBuilder();
                                String s2ForDaily = "news";
                                builder.setFrom("news");
                                builder.setChannelName(GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getDailyName());
                                builder.setNewParams(newsParams);
                                builder.setTabSource(PingbackUtils.getTabSource(context));
                                GetInterfaceTools.getPlayerPageProvider().startNewsDetailPlayerPage(context, builder);
                                return;
                            }
                            return;
                        case H5:
                            H5ActionModel h5ActionModel = (H5ActionModel) actionModel;
                            WebIntentParams params = new WebIntentParams();
                            ChannelLabel labelH5 = h5ActionModel.getLabel();
                            String h5S2 = PingbackUtils.getPageS2(context, "_rec");
                            params.pageUrl = DataBuildTool.getH5Url(labelH5);
                            params.from = h5S2;
                            params.enterType = 13;
                            params.buyFrom = "rec";
                            params.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
                            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                            return;
                        case VIP_VIDEO:
                            boolean hasVipTab = TabProvider.getInstance().hasVipTab();
                            TabModel vipTabModel = TabProvider.getInstance().getTabModel(1000002);
                            if (!hasVipTab || vipTabModel == null) {
                                LogUtils.m1577w(TAG, "onItemClick, VIP_VIDEO, vip tabModel:" + vipTabModel + ", hasVipTab : " + hasVipTab);
                                VipVideoActionModel vipVideoActionModel = (VipVideoActionModel) actionModel;
                                if (vipVideoActionModel.isOpenapi()) {
                                    AlbumUtils.startChannelNewVipPageOpenApi(context, vipVideoActionModel.getFlag());
                                    return;
                                } else {
                                    AlbumUtils.startChannelNewVipPage(context, LoginConstant.S2_LIBRARY, LoginConstant.S1_LOGIN);
                                    return;
                                }
                            }
                            SoloTabEnterUtils.start(context, vipTabModel, "tab" + HomePingbackSender.getInstance().getTabName(), LoginConstant.S2_LIBRARY);
                            return;
                        case JUMP_TO_H5:
                        case VIP_BUY:
                            VipH5ActionModel vipH5ActionModel = (VipH5ActionModel) actionModel;
                            WebIntentParams vipParams = new WebIntentParams();
                            vipParams.pageUrl = vipH5ActionModel.getLabel().itemPageUrl;
                            vipParams.enterType = 3;
                            vipParams.buyFrom = "become_vip";
                            vipParams.incomesrc = PingBackCollectionFieldUtils.getIncomeSrc();
                            if (TextUtils.equals(ItemDataType.VIP_BUY.getValue(), itemDataType.getValue())) {
                                vipParams.from = "account";
                            } else {
                                vipParams.from = "viprights";
                            }
                            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, vipParams);
                            return;
                        case STAR:
                            StarActionModel starActionModel = (StarActionModel) actionModel;
                            AlbumUtils.startSearchPeoplePage(context, starActionModel.getTitle(), starActionModel.getItemId(), PingbackUtils.getPageS2(context, "_明星"));
                            return;
                        case PERSON:
                            PersonActionModel personModel = (PersonActionModel) actionModel;
                            AlbumUtils.startSearchPeoplePage(context, personModel.getTitle(), personModel.getItemId(), PingbackUtils.getPageS2(context, "_明星"));
                            return;
                        case PLAY_LIST:
                            PlayListActionModel plModel = (PlayListActionModel) actionModel;
                            ItemUtils.openDetailOrPlay(context, plModel.getLabel(), plModel.getTitle(), PingbackUtils.getPageS2(context, "_rec"), "", plModel.getPlayParams());
                            return;
                        case RECORD:
                        case SEARCH_RECORD:
                            RecordActionModel recordActionModel = (RecordActionModel) actionModel;
                            int searchRecordType = recordActionModel.getSearchRecordType();
                            if (searchRecordType == 10 || searchRecordType == 11) {
                                ItemUtils.startVideoPlay(context, recordActionModel.getHistoryInfoAlbum(), "8", "");
                                return;
                            } else if (searchRecordType == 12) {
                                AlbumUtils.startFootPlayhistoryPage(context);
                                return;
                            } else {
                                return;
                            }
                        case SEARCH:
                            SearchEnterUtils.startSearchActivity(context);
                            return;
                        case SUBSCRIBE:
                            AlbumUtils.startFootSubscribePage(context);
                            return;
                        case COLLECTION:
                            AlbumUtils.startFootFavouritePage(context);
                            return;
                        case SETTING:
                            onSetItemClick(((SettingsActionModel) actionModel).getSettingsData().getSettinsType(), context);
                            return;
                        case TV_TAG:
                            TVTagActionModel tvTagModel = (TVTagActionModel) actionModel;
                            TVTags tvTags = tvTagModel.getTvTags();
                            int jump = tvTagModel.getItemKvs().jump;
                            TabModel tvTagTabModel = tvTags != null ? TabProvider.getInstance().getTabModel(tvTags.channelId) : null;
                            if (tvTags != null) {
                                channel = AlbumInfoFactory.getChannelByChannelId(tvTags.channelId);
                                twoLevelTagList = null;
                                if (channel != null) {
                                    twoLevelTagList = channel.tags;
                                }
                                tagName = HomePingbackDataUtils.getListPageTagName(tvTags, twoLevelTagList, "_");
                                title = TitleBuildTool.getTitle(tvTagModel.getLabel(), ItemDataType.TV_TAG);
                                if (ListUtils.getCount(tvTags.tags) == 0 && jump == 2 && tvTagTabModel != null) {
                                    SoloTabEnterUtils.start(context, tvTagTabModel, "tab_" + HomePingbackSender.getInstance().getTabName(), PingbackUtils.getPageS2(context, "_solo" + tvTagTabModel.getTitle()));
                                    return;
                                }
                                if (StringUtils.isEmpty((CharSequence) title)) {
                                    title = tagName;
                                }
                                AlbumUtils.startChannelMultiDataPage(context, TagUtils.getIds(tvTags.tags), tvTags.channelId, PingbackUtils.getPageS2(context, title), "");
                                return;
                            }
                            LogUtils.m1577w(TAG, "onItemClick, itemDataType = " + ItemDataType.TV_TAG + ", TVTags(ItemKvs.getTVTag()) data is null");
                            return;
                        case TV_TAG_ALL:
                            TVTagActionModel tvAllTagModel = (TVTagActionModel) actionModel;
                            TVTags tvAllTags = tvAllTagModel.getTvTags();
                            int jumpAll = tvAllTagModel.getItemKvs().jump;
                            TabModel tvAllTagTabModel = tvAllTags != null ? TabProvider.getInstance().getTabModel(tvAllTags.channelId) : null;
                            if (tvAllTags != null) {
                                channel = AlbumInfoFactory.getChannelByChannelId(tvAllTags.channelId);
                                twoLevelTagList = null;
                                if (channel != null) {
                                    twoLevelTagList = channel.tags;
                                }
                                tagName = HomePingbackDataUtils.getListPageTagName(tvAllTags, twoLevelTagList, "_");
                                title = TitleBuildTool.getTitle(tvAllTagModel.getLabel(), ItemDataType.TV_TAG_ALL);
                                if (ListUtils.getCount(tvAllTags.tags) == 0 && jumpAll == 2 && tvAllTagTabModel != null) {
                                    SoloTabEnterUtils.start(context, tvAllTagTabModel, "tab_" + HomePingbackSender.getInstance().getTabName(), PingbackUtils.getPageS2(context, "_solo" + tvAllTagTabModel.getTitle()));
                                    return;
                                }
                                if (StringUtils.isEmpty((CharSequence) title)) {
                                    title = tagName;
                                }
                                AlbumUtils.startChannelMultiDataPage(context, TagUtils.getIds(tvAllTags.tags), tvAllTags.channelId, PingbackUtils.getPageS2(context, title), "");
                                return;
                            }
                            LogUtils.m1577w(TAG, "onItemClick, itemDataType = " + ItemDataType.TV_TAG_ALL + ", TVTags(ItemKvs.getTVTag()) data is null");
                            return;
                        case CHANNEL:
                            ChannelActionModel channelModel = (ChannelActionModel) actionModel;
                            TabModel curChannelTabModel = TabProvider.getInstance().getTabModel(channelModel.getChnId());
                            String s2ForChannel = PingbackUtils.getPageS2(context, "_" + channelModel.getChannel().name);
                            if (curChannelTabModel != null) {
                                SoloTabEnterUtils.start(context, curChannelTabModel, "tab_" + HomePingbackSender.getInstance().getTabName(), HomePingbackSender.getInstance().getTabName() + "_solo" + channelModel.getChannel().name);
                                return;
                            }
                            LogUtils.m1568d(TAG, "onItemClick, CHANNEL, current channel tab model is null");
                            if (channelModel.getChannel() != null) {
                                AlbumUtils.startChannelPage(context, channelModel.getChannel(), s2ForChannel);
                                return;
                            } else if (channelModel.getChnId() == ChannelId.CHANNEL_ID_CAROUSEL) {
                                GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, channelModel.getCarouselPlayParamBuilder());
                                return;
                            } else {
                                AlbumUtils.startChannelPage(context, channelModel.getChnId(), s2ForChannel, "", true);
                                return;
                            }
                        case RESOURCE_GROUP:
                            ResourceGroupActionModel rgaModel = (ResourceGroupActionModel) actionModel;
                            MultiSubjectEnterUtils.start(context, rgaModel.getItemId(), rgaModel.getBuySource(), PingbackUtils.getPageS2(context, "_rec"));
                            return;
                        case PLST_GROUP:
                            QSubjectUtils.startQSubjectActivity(context, ((PlstGroupActionModel) actionModel).getPlId(), PingbackUtils.getPageS2(context, "_专题回顾"));
                            return;
                        case RECOMMEND_APP:
                            ChannelLabel label = ((RecommendAppActionModel) actionModel).getChannelLable();
                            LogRecordUtils.logd(TAG, "OnItemClick: key -> " + getRecommendAppKey(label));
                            PromotionAppInfo promotionAppInfo = null;
                            PromotionMessage promotionMessage = null;
                            if (getRecommendAppKey(label).equals("chinapokerapp")) {
                                promotionMessage = PromotionCache.instance().getChinaPokerAppPromotion();
                                if (promotionMessage != null) {
                                    promotionAppInfo = promotionMessage.getDocument().getAppInfo();
                                    promotionAppInfo.setAppType(2);
                                }
                            } else if (getRecommendAppKey(label).equals("childapp")) {
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
                        case APP:
                            AppActionData data = ((ApplicationActionModel) actionModel).getData();
                            switch (data.getApplicationType()) {
                                case 1:
                                    onAppLocalClick(context, data.getAppPackageName());
                                    return;
                                case 2:
                                    onAppOnlineClick(context, data.getAppPackageName(), data.getAppId());
                                    return;
                                case 3:
                                    onAppALLClick(context);
                                    return;
                                case 4:
                                    onAppStoreClick(context, data);
                                    return;
                                default:
                                    return;
                            }
                        case SUBSCRIBE_BTN:
                            onSubscribeBtnClick(context, (SubscribeBtnActionModel) actionModel);
                            return;
                        case NONE:
                            return;
                        case MSGCENTER:
                            PageIOUtils.activityIn(context, new Intent(context, MsgCenterActivity.class));
                            return;
                        case UCENTER_RECORD_ALL:
                            if (((UcenterRecordAllActionModel) actionModel).getUcenterRecordAllData().isNeedToLogin()) {
                                GetInterfaceTools.getLoginProvider().startLoginActivity(context, LoginConstant.S1_FROM_ALLMYHIS, 3);
                                return;
                            } else {
                                AlbumUtils.startFootPlayhistoryPage(context);
                                return;
                            }
                        default:
                            return;
                    }
                    LogUtils.m1569d(TAG, "click exception ", e);
                }
            } catch (Throwable e) {
                LogUtils.m1569d(TAG, "click exception ", e);
            }
        }
    }

    private void onSubscribeBtnClick(final Context context, SubscribeBtnActionModel actionModel) {
        final String qpid = actionModel.getQpId();
        final int subscribeType = actionModel.getSubscribeType();
        if (sDebugable) {
            LogUtils.m1568d(TAG, TAG + "----initBtnClickListener,onClick== mQpId=" + qpid + ",subscribeType=" + subscribeType);
        }
        if (subscribeType == 0) {
            if (GetInterfaceTools.getIGalaAccountManager().isLogin(ResourceUtil.getContext())) {
                GetInterfaceTools.getISubscribeProvider().addSubscribe(new IVrsCallback<ApiResult>() {
                    public void onSuccess(ApiResult arg0) {
                        if (ActionJump.sDebugable) {
                            LogUtils.m1568d(ActionJump.TAG, ActionJump.TAG + "-----addSubscribe,onSuccess== mQpId=" + qpid + ",subscribeType=" + subscribeType);
                        }
                        ActionJump.this.showToast(context, "预约成功，影片上线时会通知您哦~", 2500);
                    }

                    public void onException(ApiException arg0) {
                        if (ActionJump.sDebugable) {
                            LogUtils.m1568d(ActionJump.TAG, ActionJump.TAG + "------addSubscribe,onException== mQpId=" + qpid + ",subscribeType=" + subscribeType);
                        }
                        ActionJump.this.showToast(context, "预约失败，请稍后再试~", 2500);
                    }
                }, qpid);
                return;
            }
            showToast(context, "登录后可预约影片，并在上线时通知您哦~", 2500);
            GetInterfaceTools.getLoginProvider().startLoginActivity(context, "order", 2);
        } else if (subscribeType == 1 || subscribeType == 2) {
            GetInterfaceTools.getISubscribeProvider().cancelSubscribe(new IVrsCallback<ApiResult>() {
                public void onSuccess(ApiResult arg0) {
                    if (ActionJump.sDebugable) {
                        LogUtils.m1568d(ActionJump.TAG, ActionJump.TAG + "----cancelSubscribe,onSuccess== mQpId=" + qpid + ",subscribeType=" + subscribeType);
                    }
                    ActionJump.this.showToast(context, "已取消预约", 1000);
                }

                public void onException(ApiException arg0) {
                    if (ActionJump.sDebugable) {
                        LogUtils.m1568d(ActionJump.TAG, ActionJump.TAG + "----cancelSubscribe,onException== mQpId=" + qpid + ",subscribeType=" + subscribeType);
                    }
                    ActionJump.this.showToast(context, "取消预约失败，请稍后再试~", 2500);
                }
            }, qpid);
        } else if (subscribeType == -1) {
            if (sDebugable) {
                LogUtils.m1568d(TAG, TAG + "----不支持预约，mQpId=" + qpid + ",subscribeType=" + subscribeType);
            }
            showToast(context, "此节目暂不支持预约", 2500);
        } else if (subscribeType == 3) {
            if (sDebugable) {
                LogUtils.m1568d(TAG, TAG + "-----正在热播，mQpId=" + qpid + ",subscribeType=" + subscribeType);
            }
            onItemClick(context, actionModel.getChannelLabelActionModel());
        }
    }

    private void showToast(Context context, String str, int duration) {
        QToast.makeTextAndShow(context, (CharSequence) str, duration);
    }

    private String getRecommendAppKey(ChannelLabel label) {
        ItemKvs kvs = label.getItemKvs();
        if (kvs != null) {
            String url;
            if (kvs.appkey.equalsIgnoreCase("childapp")) {
                url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getChildAppUrl();
                if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) {
                    return "";
                }
            }
            if (kvs.appkey.equalsIgnoreCase("chinapokerapp")) {
                url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getChinaPokerAppUrl();
                if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) {
                    return "";
                }
            }
            if (kvs.appkey.equalsIgnoreCase("chinapokerapp") || kvs.appkey.equalsIgnoreCase("childapp")) {
                return kvs.appkey;
            }
        }
        return "";
    }

    public void onSetItemClick(int setType, Context context) {
        if (sDebugable) {
            Log.d(TAG, "setType = " + setType);
        }
        switch (setType) {
            case 1:
                UpgradeSettingUtils.onUpgradeClick(context);
                return;
            case 2:
                SettingUtils.startAboutSettingActivity(context);
                return;
            case 3:
                GetInterfaceTools.getLoginProvider().startUcenterActivityFromCardSetting(context);
                return;
            case 4:
                SettingUtils.startCommonSettingActivity(context, null);
                return;
            case 5:
                GetInterfaceTools.getWebEntry().startFaqActivity(context);
                return;
            case 6:
                SettingUtils.starFeedbackSettingActivity(context, null);
                return;
            case 7:
                GetInterfaceTools.getWebEntry().gotoMultiscreenActivity(context);
                return;
            case 8:
                PageIOUtils.activityIn(context, new Intent(context, ConcernWeChatActivity.class));
                return;
            case 11:
                PageIOUtils.activityIn(context, new Intent(context, MenuFloatLayerSettingActivity.class));
                return;
            case 12:
                SettingUtils.startNetworkSettingActivity(context, null);
                return;
            case 13:
                SettingUtils.startTabManageActivity(context);
                return;
            case 14:
                SettingUtils.startPlaySettingActivity(context, null);
                return;
            default:
                return;
        }
    }

    public void onAppLocalClick(Context context, String appPackageName) {
        AppStoreUtils.onAppLocalClick(context, appPackageName);
    }

    public void onAppStoreClick(Context context, AppActionData data) {
        AppStoreUtils.onAppStoreClick(context, data);
    }

    public void onAppOnlineClick(Context context, String appPackageName, int appId) {
        AppStoreUtils.onAppOnlineClick(context, appPackageName, appId);
    }

    public void onAppALLClick(Context context) {
        AppListUtils.startAppListActivity(context, "tab_appstore");
    }

    private void onClickForFocusImageAd(Context context, CommonAdData adData) {
        if (adData != null) {
            AdsClientUtils.getInstance().onAdClicked(adData.getAdId());
            HomeAdPingbackModel model = new HomeAdPingbackModel();
            model.setH5EnterType(16);
            model.setH5From("ad_jump");
            model.setPlFrom("ad_jump");
            model.setVideoFrom("ad_jump");
            model.setVideoTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
            model.setVideoBuySource("");
            model.setCarouselFrom("ad_jump");
            model.setCarouselTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
            onClickAdItem(context, adData, model);
        } else if (sDebugable) {
            LogUtils.m1568d(TAG, "onClickForFocusImageAd, focus image ad item data is null");
        }
    }

    private void onClickForBannerImageAd(Context context, CommonAdData adData) {
        if (adData != null) {
            AdsClientUtils.getInstance().onAdClicked(adData.getAdId());
            HomeAdPingbackModel model = new HomeAdPingbackModel();
            model.setH5EnterType(16);
            model.setH5From("ad_jump");
            model.setPlFrom("ad_jump");
            model.setVideoFrom("ad_jump");
            model.setVideoTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
            model.setVideoBuySource("");
            model.setCarouselFrom("ad_jump");
            model.setCarouselTabSource("tab_" + HomePingbackSender.getInstance().getTabName());
            onClickAdItem(context, adData, model);
        } else if (sDebugable) {
            LogUtils.m1568d(TAG, "onClickForBannerImageAd, Banner image ad item data is null");
        }
    }

    public void onClickAdItem(Context context, CommonAdData adItemData, HomeAdPingbackModel model) {
        if (adItemData != null) {
            if (model == null && sDebugable) {
                LogUtils.m1577w(TAG, "onClickAdItem, home ad pingback model is null");
            }
            AdClickType adClickType = adItemData.getAdClickType();
            if (adClickType == null) {
                if (sDebugable) {
                    LogUtils.m1571e(TAG, "onClickAdItem, ad click type is null!!!");
                }
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
                        if (sDebugable) {
                            LogUtils.m1577w(TAG, "onClickAd, local image path, " + localImagePath);
                        }
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
                        if (sDebugable) {
                            LogUtils.m1568d(TAG, "onClickAdItem, channelCarousel: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                        }
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
                LogUtils.m1571e(TAG, "onClickAdItem, NumberFormatException: " + e);
                onClickForNoFocusImageAdData(context);
            } catch (Exception e2) {
                LogUtils.m1571e(TAG, "onClickAdItem, exception: " + e2);
                onClickForNoFocusImageAdData(context);
            }
        } else if (sDebugable) {
            LogUtils.m1571e(TAG, "onClickAdItem, ad item data is null!!!");
        }
    }

    private void onClickForNoFocusImageAdData(Context context) {
        QToast.makeTextAndShow(context, C0508R.string.screen_saver_click_error_hint, 3000);
    }

    private boolean hasCurrentTab(int channelId) {
        List<TabModel> tabModelList = TabProvider.getInstance().getTabInfo();
        List<TabModel> tabModelHideList = TabProvider.getInstance().getTabHideInfo();
        if (!ListUtils.isEmpty((List) tabModelList)) {
            for (TabModel tabModel : tabModelList) {
                if (tabModel != null && channelId == tabModel.getChannelId()) {
                    return true;
                }
            }
        }
        if (!ListUtils.isEmpty((List) tabModelHideList)) {
            for (TabModel tabModel2 : tabModelHideList) {
                if (tabModel2 != null && channelId == tabModel2.getChannelId()) {
                    return true;
                }
            }
        }
        return false;
    }
}
