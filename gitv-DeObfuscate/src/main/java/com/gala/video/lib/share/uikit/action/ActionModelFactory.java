package com.gala.video.lib.share.uikit.action;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.Star;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.CarouselHistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.data.UcenterRecordAllData;
import com.gala.video.lib.share.uikit.action.model.AdActionModel;
import com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel;
import com.gala.video.lib.share.uikit.action.model.ApplicationActionModel;
import com.gala.video.lib.share.uikit.action.model.BannerAdActionModel;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.action.model.CarouseHistoryActionModel;
import com.gala.video.lib.share.uikit.action.model.CarouselActionModel;
import com.gala.video.lib.share.uikit.action.model.ChannelActionModel;
import com.gala.video.lib.share.uikit.action.model.DailyActionModel;
import com.gala.video.lib.share.uikit.action.model.H5ActionModel;
import com.gala.video.lib.share.uikit.action.model.LiveChannelActionModel;
import com.gala.video.lib.share.uikit.action.model.MsgCenterActionModel;
import com.gala.video.lib.share.uikit.action.model.PersonActionModel;
import com.gala.video.lib.share.uikit.action.model.PlayListActionModel;
import com.gala.video.lib.share.uikit.action.model.PlstGroupActionModel;
import com.gala.video.lib.share.uikit.action.model.RecommendAppActionModel;
import com.gala.video.lib.share.uikit.action.model.RecordActionModel;
import com.gala.video.lib.share.uikit.action.model.ResourceGroupActionModel;
import com.gala.video.lib.share.uikit.action.model.SettingsActionModel;
import com.gala.video.lib.share.uikit.action.model.StarActionModel;
import com.gala.video.lib.share.uikit.action.model.SubscribeCollectionActionModel;
import com.gala.video.lib.share.uikit.action.model.TVTagActionModel;
import com.gala.video.lib.share.uikit.action.model.UcenterRecordAllActionModel;
import com.gala.video.lib.share.uikit.action.model.VipH5ActionModel;
import com.gala.video.lib.share.uikit.action.model.VipVideoActionModel;
import com.gala.video.lib.share.uikit.data.data.Model.SettingModel;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;
import com.gala.video.lib.share.uikit.loader.data.AppStore;
import com.gala.video.lib.share.uikit.loader.data.BannerAd;

public class ActionModelFactory {
    public static BaseActionModel createAdActionModel(HomeFocusImageAdModel adModel) {
        if (adModel == null) {
            return null;
        }
        ItemDataType itemDataType = adModel.getItemType();
        switch (itemDataType) {
            case FOCUS_IMAGE_AD:
                BaseActionModel baseActionModel = new AdActionModel(itemDataType);
                baseActionModel.buildActionModel(adModel);
                return baseActionModel;
            default:
                return null;
        }
    }

    public static BaseActionModel createBannerAdActionModel(BannerAd adModel) {
        if (adModel == null) {
            return null;
        }
        ItemDataType itemDataType = ItemDataType.BANNER_IMAGE_AD;
        switch (itemDataType) {
            case BANNER_IMAGE_AD:
                BaseActionModel baseActionModel = new BannerAdActionModel(itemDataType);
                baseActionModel.buildActionModel(adModel);
                return baseActionModel;
            default:
                return null;
        }
    }

    public static BaseActionModel createStarActionModel(Star star) {
        if (star == null) {
            return null;
        }
        ItemDataType itemDataType = ItemDataType.STAR;
        switch (itemDataType) {
            case STAR:
                BaseActionModel baseActionModel = new StarActionModel(itemDataType);
                baseActionModel.buildActionModel(star);
                return baseActionModel;
            default:
                return null;
        }
    }

    public static BaseActionModel createSettingActionModel(SettingModel settingModel) {
        return new SettingsActionModel(ItemDataType.SETTING).buildActionModel(settingModel);
    }

    public static BaseActionModel createAppActionModel(AppStore appStoreModel) {
        return new ApplicationActionModel(ItemDataType.APP).buildActionModel(appStoreModel);
    }

    public static BaseActionModel createChannelActionModel(Channel channel) {
        return new ChannelActionModel(ItemDataType.CHANNEL).buildActionModel(channel);
    }

    public static BaseActionModel createCarouselChannelActionModel(ChannelLabel label) {
        return createActionModel(label);
    }

    public static BaseActionModel createCarouseHistoryActionModel(CarouselHistoryInfo carouselHistoryInfo) {
        return new CarouseHistoryActionModel(ItemDataType.LIVE_CHANNEL).buildActionModel(carouselHistoryInfo);
    }

    public static BaseActionModel createUcenterRecordAllModel(UcenterRecordAllData data) {
        return new UcenterRecordAllActionModel(ItemDataType.UCENTER_RECORD_ALL).buildActionModel(data);
    }

    public static BaseActionModel createActionModel(ChannelLabel label) {
        if (label == null) {
            return null;
        }
        ItemDataType itemDataType = DataBuildTool.getItemType(label);
        BaseActionModel baseActionModel = null;
        switch (itemDataType) {
            case ALBUM:
            case VIDEO:
            case TRAILERS:
            case LIVE:
                baseActionModel = new AlbumVideoLiveActionModel(itemDataType, label);
                break;
            case ENTER_ALL:
                baseActionModel = new BaseActionModel(itemDataType);
                break;
            case LIVE_CHANNEL:
                baseActionModel = new LiveChannelActionModel(itemDataType);
                break;
            case CAROUSEL:
                baseActionModel = new CarouselActionModel(itemDataType, label);
                break;
            case DAILY:
                baseActionModel = new DailyActionModel(itemDataType);
                break;
            case H5:
                baseActionModel = new H5ActionModel(itemDataType);
                break;
            case PERSON:
                baseActionModel = new PersonActionModel(itemDataType);
                break;
            case PLAY_LIST:
                baseActionModel = new PlayListActionModel(itemDataType, label);
                break;
            case RECORD:
            case SEARCH_RECORD:
                baseActionModel = new RecordActionModel(itemDataType);
                break;
            case TV_TAG:
            case TV_TAG_ALL:
                baseActionModel = new TVTagActionModel(itemDataType);
                break;
            case RESOURCE_GROUP:
                baseActionModel = new ResourceGroupActionModel(itemDataType);
                break;
            case PLST_GROUP:
                baseActionModel = new PlstGroupActionModel(itemDataType);
                break;
            case RECOMMEND_APP:
                baseActionModel = new RecommendAppActionModel(itemDataType);
                break;
            case SEARCH:
                baseActionModel = new BaseActionModel(itemDataType);
                break;
            case SUBSCRIBE:
            case COLLECTION:
                baseActionModel = new SubscribeCollectionActionModel(itemDataType);
                break;
            case VIP_VIDEO:
                baseActionModel = new VipVideoActionModel(itemDataType);
                break;
            case JUMP_TO_H5:
            case VIP_BUY:
                baseActionModel = new VipH5ActionModel(itemDataType);
                break;
            case MSGCENTER:
                baseActionModel = new MsgCenterActionModel(itemDataType);
                break;
        }
        if (baseActionModel == null) {
            return baseActionModel;
        }
        baseActionModel.buildActionModel(label);
        return baseActionModel;
    }
}
