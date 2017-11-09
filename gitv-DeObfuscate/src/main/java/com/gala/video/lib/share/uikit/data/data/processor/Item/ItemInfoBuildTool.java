package com.gala.video.lib.share.uikit.data.data.processor.Item;

import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.Star;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.CarouselHistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.action.ActionModelFactory;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.Model.SettingModel;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Item;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.loader.data.AppStore;
import com.gala.video.lib.share.uikit.loader.data.BannerAd;
import com.gala.video.lib.share.utils.Precondition;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.HashMap;

public class ItemInfoBuildTool {
    public static ItemInfoModel buildItem(ChannelLabel label, Item itemStyle, short cardLayoutId, short cardType, boolean hasBack, int index, boolean isSort, boolean isVipTag) {
        ItemDataType itemDataType = DataBuildTool.getItemType(label);
        if (cardType == UIKitConfig.CARD_TYPE_GRID_CARD) {
            return buildCarouselChannelItem(label, itemStyle, isVipTag);
        }
        if (cardType == UIKitConfig.CARD_TYPE_VIP || itemDataType == ItemDataType.SUBSCRIBE || itemDataType == ItemDataType.COLLECTION) {
            return buildVipItem(label, itemStyle, cardType);
        }
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        CornerBuildTool.buildCorner(label, cardLayoutId, cuteViewDatas, cardType, index, isSort);
        TitleBuildTool.buildTitle(label, cuteViewDatas, itemDataType);
        ImgBuildTool.buildImg(label, itemStyle, itemDataType, cuteViewDatas, hasBack);
        buildSpecialData(cardType, itemDataType, label, cuteViewDatas);
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(itemDataType, cardType).shortValue());
        setLayoutInfo(itemStyle, itemInfoModel, isVipTag);
        reSetCoverFlow(cardType, label, itemInfoModel, itemStyle);
        itemInfoModel.setActionModel(ActionModelFactory.createActionModel(label));
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        return itemInfoModel;
    }

    private static ItemInfoModel buildVipItem(ChannelLabel label, Item item, short cardType) {
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        TitleBuildTool.buildTitle(label.itemKvs.tvShowName, cuteViewDatas);
        ImgBuildTool.buildImg(label.itemKvs.tvIcon, cuteViewDatas);
        ImgBuildTool.buildBg(label.itemKvs.tvPic, cuteViewDatas);
        setLayoutInfo(item, itemInfoModel, false);
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(DataBuildTool.getItemType(label), cardType).shortValue());
        itemInfoModel.setActionModel(ActionModelFactory.createActionModel(label));
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        return itemInfoModel;
    }

    public static ItemInfoModel buildHistoryItem(Album album, Item item, short cardLayoutId, short cardType, int index, boolean isSort, boolean isVipTag) {
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        ChannelLabel label = DataBuildTool.albumToChannelLabel(album);
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        setLayoutInfo(item, itemInfoModel, isVipTag);
        if (album == null) {
            TitleBuildTool.buildTitle("全部记录", cuteViewDatas);
            itemInfoModel.setItemType(UIKitConfig.ITEM_TYPE_HISTORY_ALL_ENTRY);
            itemInfoModel.setHeight((short) (itemInfoModel.getHeight() - ResourceUtil.getPx(34)));
        } else {
            ImgBuildTool.buildHistoryImg(album, item, cuteViewDatas);
            TitleBuildTool.buildHistoryTitle(album, cuteViewDatas);
            CornerBuildTool.buildCorner(label, cardLayoutId, cuteViewDatas, cardType, index, isSort);
            itemInfoModel.setActionModel(ActionModelFactory.createActionModel(DataBuildTool.albumToChannelLabel(album)));
            itemInfoModel.setItemType(DataBuildTool.getNewTyep(DataBuildTool.getItemType(label), cardType).shortValue());
            HashMap<String, String> sourceMap = new HashMap();
            sourceMap.put(UIKitConfig.KEY_SOURCE, "history");
            cuteViewDatas.put(UIKitConfig.SPECIAL_DATA, sourceMap);
        }
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        return itemInfoModel;
    }

    public static ItemInfoModel buildStarItem(Star star, Item item) {
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        ImgBuildTool.buildImg(ImgBuildTool.resizeImage(star.cover, "_300_300"), cuteViewDatas);
        TitleBuildTool.buildTitle(star.name, cuteViewDatas);
        setLayoutInfo(item, itemInfoModel, false);
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(ItemDataType.PERSON, UIKitConfig.CARD_TYPE_FLOW).shortValue());
        itemInfoModel.setActionModel(ActionModelFactory.createStarActionModel(star));
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        return itemInfoModel;
    }

    public static ItemInfoModel buildBannerItem(BannerAd bannerAd, Item item, boolean isVipTag) {
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        if (bannerAd.needAdBadge) {
            CornerBuildTool.buildAdCorner(cuteViewDatas);
        }
        ImgBuildTool.buildImg(bannerAd.imageUrl, cuteViewDatas);
        TitleBuildTool.buildTitle(bannerAd.title, cuteViewDatas);
        itemInfoModel.setActionModel(ActionModelFactory.createBannerAdActionModel(bannerAd));
        setLayoutInfo(item, itemInfoModel, isVipTag);
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(ItemDataType.BANNER_IMAGE_AD, UIKitConfig.CARD_TYPE_FLOW).shortValue());
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        return itemInfoModel;
    }

    public static ItemInfoModel buildChannelItem(Channel channel, Item item, boolean isVipTag) {
        if (channel.id.equals(String.valueOf(10002)) && GetInterfaceTools.getPlayerConfigProvider().isDisable4KH264()) {
            return null;
        }
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        ImgBuildTool.buildImg(channel.icon, cuteViewDatas);
        TitleBuildTool.buildTitle(channel.name, cuteViewDatas);
        itemInfoModel.setActionModel(ActionModelFactory.createChannelActionModel(channel));
        setLayoutInfo(item, itemInfoModel, isVipTag);
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(ItemDataType.CHANNEL, UIKitConfig.CARD_TYPE_FLOW).shortValue());
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        return itemInfoModel;
    }

    public static ItemInfoModel buildAppItem(AppStore app, Item item, boolean isVipTag) {
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        ImgBuildTool.buildImg(app.app_image_url, cuteViewDatas);
        TitleBuildTool.buildTitle(app.app_name, cuteViewDatas);
        setLayoutInfo(item, itemInfoModel, isVipTag);
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(ItemDataType.APP, UIKitConfig.CARD_TYPE_APP_AND_SETTING).shortValue());
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        itemInfoModel.setActionModel(ActionModelFactory.createAppActionModel(app));
        return itemInfoModel;
    }

    public static ItemInfoModel buildSettingItem(Item item, SettingModel settingModel, boolean isVipTag) {
        if (settingModel.type != 7 || MultiScreen.get().isSupportMS()) {
            ItemInfoModel itemInfoModel = new ItemInfoModel();
            HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
            setLayoutInfo(item, itemInfoModel, isVipTag);
            itemInfoModel.setItemType(DataBuildTool.getNewTyep(ItemDataType.SETTING, UIKitConfig.CARD_TYPE_APP_AND_SETTING).shortValue());
            ImgBuildTool.buildImg(settingModel.img, cuteViewDatas);
            TitleBuildTool.buildTitle(settingModel.name, cuteViewDatas);
            itemInfoModel.setCuteViewDatas(cuteViewDatas);
            itemInfoModel.setActionModel(ActionModelFactory.createSettingActionModel(settingModel));
            return itemInfoModel;
        }
        Log.d("ItemFilter", "不支持多屏");
        return null;
    }

    public static ItemInfoModel buildCarouselChannelItem(ChannelLabel label, Item item, boolean isVipTag) {
        ItemInfoModel itemInfoModel = buildCarouselChannelItem(null, String.valueOf(label.tableNo), item, isVipTag);
        itemInfoModel.setActionModel(ActionModelFactory.createCarouselChannelActionModel(label));
        TitleBuildTool.buildTitle(label, itemInfoModel.getCuteViewDatas(), ItemDataType.LIVE_CHANNEL);
        return itemInfoModel;
    }

    public static ItemInfoModel buildCarouselChannelItem(CarouselHistoryInfo carouselHistoryInfo, Item item, boolean isVipTag) {
        ItemInfoModel itemInfoModel = buildCarouselChannelItem(carouselHistoryInfo.getCarouselChannelName(), carouselHistoryInfo.getCarouselChannelNo(), item, isVipTag);
        itemInfoModel.setActionModel(ActionModelFactory.createCarouseHistoryActionModel(carouselHistoryInfo));
        return itemInfoModel;
    }

    private static ItemInfoModel buildCarouselChannelItem(String name, String no, Item item, boolean isVipTag) {
        ItemInfoModel itemInfoModel = new ItemInfoModel();
        HashMap<String, HashMap<String, String>> cuteViewDatas = new HashMap();
        if (name != null) {
            TitleBuildTool.buildTitle(name, cuteViewDatas);
        }
        itemInfoModel.setItemType(DataBuildTool.getNewTyep(ItemDataType.LIVE_CHANNEL, UIKitConfig.CARD_TYPE_GRID_CARD).shortValue());
        HashMap<String, String> channelId = new HashMap();
        if (no.length() == 1) {
            no = "0" + no;
        }
        channelId.put("text", no);
        cuteViewDatas.put(UIKitConfig.ID_CHANNEL_ID, channelId);
        itemInfoModel.setCuteViewDatas(cuteViewDatas);
        setLayoutInfo(item, itemInfoModel, isVipTag);
        return itemInfoModel;
    }

    private static void setLayoutInfo(Item item, ItemInfoModel itemInfoModel, boolean isVipTag) {
        itemInfoModel.setId(String.valueOf(itemInfoModel.hashCode()));
        itemInfoModel.setHeight(item.f2040h);
        itemInfoModel.setWidth(item.f2041w);
        itemInfoModel.setSpaceH(item.space_h);
        itemInfoModel.setSpaceV(item.space_v);
        itemInfoModel.setScale(item.scale);
        String skinEndsWith = getSkinEndsWith(isVipTag);
        itemInfoModel.setStyle(StringUtils.append(item.style, skinEndsWith));
        itemInfoModel.setSkinEndsWith(skinEndsWith);
    }

    public static String getSkinEndsWith(boolean isVipTag) {
        if (isVipTag) {
            return UIKitConfig.STYLE_VIP;
        }
        return null;
    }

    private static String getQpId(ChannelLabel label) {
        if (label.getType() == ResourceType.LIVE) {
            return label.itemId;
        }
        return StringUtils.isEmpty(label.albumQipuId) ? label.tvQipuId : label.albumQipuId;
    }

    private static void reSetCoverFlow(short cardType, ChannelLabel label, ItemInfoModel itemInfoModel, Item item) {
        if (cardType == UIKitConfig.CARD_TYPE_COVER_FLOW && !Precondition.isNull(label.itemKvs) && !Precondition.isEmpty(label.itemKvs.defImg_size)) {
            int[] size = DataBuildTool.getImageSize(label.itemKvs.defImg_size);
            if (!Precondition.isEmpty(size) && size.length == 2) {
                itemInfoModel.setWidth((short) ((size[0] * item.f2040h) / size[1]));
                itemInfoModel.setHeight(item.f2040h);
            }
        }
    }

    private static void buildSpecialData(short cardType, ItemDataType itemDataType, ChannelLabel label, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        HashMap<String, String> specialDataMap;
        if (cardType == UIKitConfig.CARD_TYPE_TIME_LINE) {
            specialDataMap = new HashMap();
            if (itemDataType == ItemDataType.LIVE) {
                specialDataMap.put(UIKitConfig.KEY_SPECIAL_DATA_TIMELINETITLE, DataBuildTool.getLiveTime(label));
            } else {
                specialDataMap.put(UIKitConfig.KEY_SPECIAL_DATA_TIMELINETITLE, DataBuildTool.getVideoTime(label));
            }
            cuteViewDatas.put(UIKitConfig.SPECIAL_DATA, specialDataMap);
        } else if (cardType == UIKitConfig.CARD_TYPE_TOBE_ONLINE) {
            specialDataMap = new HashMap();
            specialDataMap.put(UIKitConfig.KEY_SPECIAL_DATA_CHNID, label.getVideo().chnId + "");
            specialDataMap.put(UIKitConfig.KEY_SPECIAL_DATA_FEATUREFILM, label.getContentType() == ContentType.FEATURE_FILM ? "1" : "0");
            specialDataMap.put(UIKitConfig.KEY_SPECIAL_DATA_QPID, getQpId(label));
            specialDataMap.put(UIKitConfig.KEY_SPECIAL_DATA_TIMELINETITLE, label.itemKvs.showTime);
            cuteViewDatas.put(UIKitConfig.SPECIAL_DATA, specialDataMap);
        } else if (cardType == UIKitConfig.CARD_TYPE_FLOW) {
            HashMap<String, String> map1 = new HashMap();
            map1.put(UIKitConfig.KEY_SPECIAL_DATA_TVID, label.getVideo().tvQid);
            map1.put(UIKitConfig.KEY_SPECIAL_DATA_QPID, label.getVideo().qpId);
            cuteViewDatas.put(UIKitConfig.KEY_DETAIL_SPECIAL_DATA, map1);
        }
    }
}
