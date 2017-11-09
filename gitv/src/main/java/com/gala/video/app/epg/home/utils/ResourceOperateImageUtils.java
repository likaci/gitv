package com.gala.video.app.epg.home.utils;

import android.content.Context;
import com.gala.sdk.player.PlayParams;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.ResourceOperatePingbackModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataUtils;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.data.tool.DataBuildTool;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.utils.TagUtils;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.solotab.SoloTabEnterUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class ResourceOperateImageUtils {
    private static final String TAG = "utils/ResourceOperateImageUtils";
    private static List<ItemDataType> mItemDataTypeList = new ArrayList();

    static {
        mItemDataTypeList.add(ItemDataType.ALBUM);
        mItemDataTypeList.add(ItemDataType.VIDEO);
        mItemDataTypeList.add(ItemDataType.LIVE);
        mItemDataTypeList.add(ItemDataType.LIVE_CHANNEL);
        mItemDataTypeList.add(ItemDataType.H5);
        mItemDataTypeList.add(ItemDataType.PERSON);
        mItemDataTypeList.add(ItemDataType.PLAY_LIST);
        mItemDataTypeList.add(ItemDataType.TV_TAG);
        mItemDataTypeList.add(ItemDataType.TV_TAG_ALL);
        mItemDataTypeList.add(ItemDataType.RESOURCE_GROUP);
    }

    public static void onClick(Context context, ChannelLabel label, ResourceOperatePingbackModel pingbackModel) {
        if (label == null) {
            LogUtils.w(TAG, "onClick, current ChannelLabel is null");
            return;
        }
        ItemDataType itemDataType = DataBuildTool.getItemType(label);
        if (itemDataType == null) {
            LogUtils.w(TAG, "onClick, current ItemDataType is null");
        } else if (NetworkStatePresenter.getInstance().checkStateIllegal()) {
            ItemKvs itemKvs = label.getItemKvs();
            String title = DataBuildTool.getPrompt(label);
            switch (itemDataType) {
                case ALBUM:
                case VIDEO:
                case LIVE:
                    ItemUtils.openDetailOrPlay(context, label, title, pingbackModel.getS2(), "", null);
                    return;
                case LIVE_CHANNEL:
                    if (Project.getInstance().getControl().isOpenCarousel()) {
                        ChannelCarousel channelCarousel = new ChannelCarousel();
                        channelCarousel.tableNo = (long) label.tableNo;
                        channelCarousel.id = StringUtils.parse(label.itemId, 0);
                        channelCarousel.name = title;
                        LogUtils.d(TAG, "LIVE_CHANNEL: id = " + channelCarousel.id + ", tableNo = " + channelCarousel.tableNo + ", name = " + channelCarousel.name);
                        CarouselPlayParamBuilder carouselPlayParamBuilder = new CarouselPlayParamBuilder();
                        carouselPlayParamBuilder.setChannel(channelCarousel);
                        carouselPlayParamBuilder.setFrom(pingbackModel.getS2());
                        carouselPlayParamBuilder.setTabSource("");
                        GetInterfaceTools.getPlayerPageProvider().startCarouselPlayerPage(context, carouselPlayParamBuilder);
                        return;
                    }
                    LogUtils.d(TAG, "onClick , not support carousel");
                    onClickForNotSupportJump(context);
                    return;
                case H5:
                    String h5Url = DataBuildTool.getH5Url(label);
                    WebIntentParams params = new WebIntentParams();
                    params.pageUrl = h5Url;
                    params.from = pingbackModel.getS2();
                    params.enterType = pingbackModel.getEnterType();
                    params.incomesrc = pingbackModel.getIncomesrc();
                    GetInterfaceTools.getWebEntry().gotoCommonWebActivity(context, params);
                    return;
                case PERSON:
                    AlbumUtils.startSearchPeoplePage(context, title, label.itemId, pingbackModel.getS2());
                    return;
                case PLAY_LIST:
                    PlayParams playParams = new PlayParams();
                    playParams.playListId = label.id;
                    ItemUtils.openDetailOrPlay(context, label, title, pingbackModel.getS2(), "", playParams);
                    return;
                case TV_TAG:
                    TVTags tvTags = label.itemKvs.getTVTag();
                    int jump = label.getItemKvs().jump;
                    TabModel tvTagTabModel = tvTags != null ? TabProvider.getInstance().getTabModel(tvTags.channelId) : null;
                    if (tvTags == null) {
                        LogUtils.w(TAG, "onClick, itemDataType = TV_TAG, TVTags(ChannelLabel.ItemKvs.getTVTag()) data is null");
                        return;
                    } else if (ListUtils.getCount(tvTags.tags) == 0 && jump == 2 && tvTagTabModel != null) {
                        SoloTabEnterUtils.start(context, tvTagTabModel, "tab_" + HomePingbackSender.getInstance().getTabName(), pingbackModel.getS2());
                        return;
                    } else {
                        AlbumUtils.startChannelMultiDataPage(context, TagUtils.getIds(tvTags.tags), tvTags.channelId, pingbackModel.getS2(), "");
                        return;
                    }
                case TV_TAG_ALL:
                    TVTags tvAllTags = label.itemKvs.getTVTag();
                    int jumpAll = label.getItemKvs().jump;
                    TabModel tvTagAllTabModel = tvAllTags != null ? TabProvider.getInstance().getTabModel(tvAllTags.channelId) : null;
                    if (tvAllTags == null) {
                        LogUtils.w(TAG, "onClick, itemDataType = TV_TAG_ALL, TVTags(ChannelLabel.ItemKvs.getTVTag()) data is null");
                        return;
                    } else if (ListUtils.getCount(tvAllTags.tags) == 0 && jumpAll == 2 && tvTagAllTabModel != null) {
                        SoloTabEnterUtils.start(context, tvTagAllTabModel, "tab_" + HomePingbackSender.getInstance().getTabName(), pingbackModel.getS2());
                        return;
                    } else {
                        AlbumUtils.startChannelMultiDataPage(context, TagUtils.getIds(tvAllTags.tags), tvAllTags.channelId, pingbackModel.getS2(), "");
                        return;
                    }
                case RESOURCE_GROUP:
                    MultiSubjectEnterUtils.start(context, label.itemId, "", pingbackModel.getS2());
                    return;
                default:
                    return;
            }
        } else {
            LogUtils.w(TAG, "onClick, net work illegal");
        }
    }

    public static boolean isSupportResType(ChannelLabel label) {
        if (label == null) {
            return false;
        }
        ItemDataType itemDataType = DataBuildTool.getItemType(label);
        if (mItemDataTypeList.contains(itemDataType)) {
            return true;
        }
        LogUtils.w(TAG, "isSupportResType, not support Resource type :" + itemDataType);
        return false;
    }

    public static boolean isSupportJump(ChannelLabel label) {
        if (label == null) {
            return false;
        }
        int gotoRes;
        ItemKvs itemKvs = label.getItemKvs();
        if (itemKvs != null) {
            gotoRes = itemKvs.goto_resource;
        } else {
            gotoRes = 0;
        }
        if (gotoRes != 0) {
            return true;
        }
        return false;
    }

    public static void onClickForNotSupportJump(Context context) {
        QToast.makeTextAndShow(context, R.string.screen_saver_click_error_hint, 2000);
    }

    public static ResourceOperatePingbackModel getPingbackModel(ChannelLabel label, OperationImageType type) {
        return new ResourceOperatePingbackModel();
    }

    public static String getRValue(ChannelLabel label) {
        String r = "";
        if (label == null) {
            LogUtils.w(TAG, "getRValue, channel label is null");
            return r;
        }
        ItemDataType itemDataType = DataBuildTool.getItemType(label);
        if (itemDataType == null) {
            LogUtils.w(TAG, "getRValue, current ItemDataType is null");
            return r;
        }
        switch (itemDataType) {
            case ALBUM:
                if (label.getVideo() != null) {
                    r = label.getVideo().qpId;
                    break;
                }
                break;
            case VIDEO:
                if (label.getVideo() != null) {
                    r = label.getVideo().qpId;
                    break;
                }
                break;
            case LIVE:
                r = label.itemId;
                break;
            case LIVE_CHANNEL:
                r = label.itemId;
                break;
            case H5:
                r = "H5_" + DataBuildTool.getPrompt(label);
                break;
            case PERSON:
                r = label.itemId;
                break;
            case PLAY_LIST:
                r = label.itemId;
                break;
            case TV_TAG:
                if (label.itemKvs != null) {
                    TVTags tvTags = label.itemKvs.getTVTag();
                    if (tvTags != null) {
                        Channel channel = AlbumInfoFactory.getChannelByChannelId(tvTags.channelId);
                        List<TwoLevelTag> twoLevelTagList = null;
                        if (channel != null) {
                            twoLevelTagList = channel.tags;
                        }
                        r = HomePingbackDataUtils.getListPageTagName(tvTags, twoLevelTagList, "_");
                        break;
                    }
                }
                break;
            case TV_TAG_ALL:
                r = label.itemKvs != null ? label.itemKvs.tvShowName : "";
                break;
            case RESOURCE_GROUP:
                r = label.itemId;
                break;
        }
        return r;
    }
}
