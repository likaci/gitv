package com.gala.video.lib.share.uikit.data.data.processor;

import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.Star;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ResourceGroup;
import com.gala.tvapi.vrs.result.ApiResultGroupDetail;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.CarouselHistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.Model.SettingConfig;
import com.gala.video.lib.share.uikit.data.data.Model.SettingModel;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardMap;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardStyle;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Item;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Row;
import com.gala.video.lib.share.uikit.data.data.cache.LayoutCache;
import com.gala.video.lib.share.uikit.data.data.processor.Item.ItemFilter;
import com.gala.video.lib.share.uikit.data.data.processor.Item.ItemInfoBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.Item.ItemParams;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.uikit.loader.data.AppRequest;
import com.gala.video.lib.share.uikit.loader.data.AppStore;
import com.gala.video.lib.share.uikit.loader.data.BannerAd;
import com.gala.video.lib.share.utils.Precondition;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class CardInfoBuildTool {
    private static final String TAG = "CardInfoBuildTool";
    private static short pxShort26 = ResourceUtil.getPxShort(26);
    private static short pxShort60 = ResourceUtil.getPxShort(60);

    public static List<CardInfoModel> buildCardList(ApiResultGroupDetail resultGroupDetail, String sourceId, int pageNo, int uikitEngineId, boolean isVipTag) {
        if (resultGroupDetail == null || resultGroupDetail.data == null) {
            return null;
        }
        List<CardInfoModel> list = new ArrayList();
        List<ResourceGroup> resourceGroups = resultGroupDetail.data.items;
        if (resourceGroups != null && resourceGroups.size() > 0) {
            for (int i = 0; i < resourceGroups.size(); i++) {
                CardInfoModel model = buildCardInfo((ResourceGroup) resourceGroups.get(i), sourceId, pageNo, uikitEngineId, false, isVipTag);
                if (model != null) {
                    if (checkCard(model)) {
                        list.add(model);
                    } else {
                        Log.d(TAG, "itemList为空，过滤card，cardID=" + model.mCardId);
                    }
                }
            }
        }
        Log.d(TAG, "buildCardList, sourceId = " + sourceId + ", pageNo = " + pageNo + ", uikitEngineId = " + uikitEngineId + ", list.size = " + list.size());
        return list;
    }

    public static CardInfoModel buildRecommendVideoCard(CardInfoModel cardInfoModel, List<Album> albums, boolean isEnterAll) {
        if (albums == null || albums.size() == 0) {
            return cardInfoModel;
        }
        for (int i = 0; i < albums.size(); i++) {
            ((Album) albums.get(i)).albumFrom = AlbumFrom.RECOMMAND_VIDEO;
        }
        return buildAlbumCard(cardInfoModel, albums, isEnterAll);
    }

    public static CardInfoModel buildAlbumCard(CardInfoModel cardInfoModel, List<Album> albums, boolean isEnterAll) {
        if (albums == null || albums.size() == 0) {
            return cardInfoModel;
        }
        CardStyle style;
        int i;
        List<ChannelLabel> list = new ArrayList();
        CardMap cardMap = LayoutCache.getInstance().getCard();
        if (isEnterAll) {
            style = cardMap.get(cardInfoModel.cardLayoutId_entryAll);
            if (style == null) {
                Log.d(TAG, "style获取失败, cardLayoutId = " + cardInfoModel.cardLayoutId_entryAll);
                return null;
            }
            CardInfoModel cardInfo = new CardInfoModel();
            cardInfo.cardLayoutId = cardInfoModel.cardLayoutId_entryAll;
            cardInfo.mSource = cardInfoModel.mSource;
            cardInfo.mPageNo = cardInfoModel.mPageNo;
            cardInfo.mUikitEngineId = cardInfoModel.mUikitEngineId;
            cardInfo.mCardId = cardInfoModel.mCardId;
            setCardLayout(cardInfo, style);
            cardInfo.setTitle(cardInfoModel.getTitle());
            cardInfoModel = cardInfo;
        } else {
            style = cardMap.get(Integer.valueOf(cardInfoModel.cardLayoutId).intValue());
            if (cardInfoModel.tvTagAll != null) {
                list.add(cardInfoModel.tvTagAll);
            }
        }
        for (i = 0; i < albums.size(); i++) {
            ChannelLabel label = DataBuildTool.albumToChannelLabel((Album) albums.get(i));
            if (label != null) {
                list.add(label);
            }
        }
        ItemInfoModel[][] itemInfoModels = getItems(style, list.size());
        replaceTvTagAll(style.rows, list);
        i = 0;
        for (int x = 0; x < style.rows.size(); x++) {
            Row r = (Row) style.rows.get(x);
            for (int y = 0; y < r.items.size() && i < list.size(); y++) {
                Item item = (Item) r.items.get(y);
                ItemInfoModel iim = null;
                while (iim == null && i < list.size()) {
                    iim = ItemInfoBuildTool.buildItem((ChannelLabel) list.get(i), item, (short) cardInfoModel.cardLayoutId, style.type, false, i, false, false);
                    i++;
                }
                itemInfoModels[x][y] = iim;
            }
        }
        cardInfoModel.setItemInfoModels(itemInfoModels);
        Log.d(TAG, "buildAlbumCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    public static CardInfoModel buildStarsCard(CardInfoModel cardInfoModel, List<Star> list, boolean isEnterAll) {
        if (list == null || list.size() == 0) {
            return cardInfoModel;
        }
        CardStyle style;
        CardMap cardMap = LayoutCache.getInstance().getCard();
        if (isEnterAll) {
            style = cardMap.get(cardInfoModel.cardLayoutId_entryAll);
            if (style == null) {
                Log.d(TAG, "star style获取失败, cardLayoutId = " + cardInfoModel.cardLayoutId_entryAll);
                return null;
            }
            CardInfoModel cardInfo = new CardInfoModel();
            cardInfo.cardLayoutId = cardInfoModel.cardLayoutId_entryAll;
            cardInfo.mSource = cardInfoModel.mSource;
            cardInfo.mPageNo = cardInfoModel.mPageNo;
            cardInfo.mUikitEngineId = cardInfoModel.mUikitEngineId;
            cardInfo.mCardId = cardInfoModel.mCardId;
            setCardLayout(cardInfo, style);
            cardInfo.setTitle(cardInfoModel.getTitle());
            cardInfoModel = cardInfo;
        } else {
            style = cardMap.get(Integer.valueOf(cardInfoModel.cardLayoutId).intValue());
        }
        ItemInfoModel[][] itemInfoModels = getItems(style, list.size());
        int i = 0;
        for (int x = 0; x < style.rows.size(); x++) {
            Row r = (Row) style.rows.get(x);
            int y = 0;
            while (y < r.items.size() && i < list.size()) {
                Item item = (Item) r.items.get(y);
                ItemInfoModel iim = null;
                while (iim == null && i < list.size()) {
                    if (x != style.rows.size() - 1 || y != r.items.size() - 1 || i + 1 >= list.size() || isEnterAll || cardInfoModel.tvTagAll == null || DataBuildTool.getItemType(cardInfoModel.tvTagAll) != ItemDataType.ENTER_ALL) {
                        iim = ItemInfoBuildTool.buildStarItem((Star) list.get(i), item);
                        i++;
                    } else {
                        iim = ItemInfoBuildTool.buildItem(cardInfoModel.tvTagAll, item, (short) cardInfoModel.cardLayoutId, style.type, false, i, false, false);
                    }
                }
                itemInfoModels[x][y] = iim;
                y++;
            }
        }
        cardInfoModel.setItemInfoModels(itemInfoModels);
        Log.d(TAG, "buildStarCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    public static CardInfoModel buildHistoryCard(CardInfoModel cardInfoModel, List<Album> list) {
        List<Album> tempList;
        if (list.size() > 5) {
            tempList = new ArrayList(6);
            tempList.addAll(list.subList(0, 5));
            tempList.add(null);
        } else {
            tempList = new ArrayList(list.size() + 1);
            tempList.addAll(list);
            tempList.add(null);
        }
        list = tempList;
        CardStyle style = LayoutCache.getInstance().getCard().get(Integer.valueOf(cardInfoModel.cardLayoutId).intValue());
        ItemInfoModel[][] itemInfoModels = getItems(style, list.size());
        int i = 0;
        for (int x = 0; x < style.rows.size(); x++) {
            Row r = (Row) style.rows.get(x);
            if (!cardInfoModel.canShort && r.items.size() > list.size() - i && style.type != UIKitConfig.CARD_TYPE_COVER_FLOW && style.type != UIKitConfig.CARD_TYPE_BLANK) {
                Log.d(TAG, "不可缺");
                break;
            }
            for (int y = 0; y < r.items.size() && i < list.size(); y++) {
                Item item = (Item) r.items.get(y);
                ItemInfoModel iim = null;
                while (iim == null && i < list.size()) {
                    iim = ItemInfoBuildTool.buildHistoryItem((Album) list.get(i), item, (short) cardInfoModel.cardLayoutId, cardInfoModel.getCardType(), i, false, false);
                    i++;
                }
                itemInfoModels[x][y] = iim;
            }
        }
        cardInfoModel.setItemInfoModels(itemInfoModels);
        Log.d(TAG, "buildHistoryCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    private static CardInfoModel buildSettingCard(CardInfoModel cardInfoModel, CardStyle style, SettingModel[] settingModels) {
        ItemInfoModel[][] itemInfoModels = getItems(style, settingModels.length);
        int i = 0;
        for (int x = 0; x < style.rows.size(); x++) {
            Row r = (Row) style.rows.get(x);
            for (int y = 0; y < r.items.size(); y++) {
                if (i < settingModels.length) {
                    Item item = (Item) r.items.get(y);
                    ItemInfoModel iim = null;
                    while (iim == null && i < settingModels.length) {
                        iim = ItemInfoBuildTool.buildSettingItem(item, settingModels[i], cardInfoModel.isVIPTag);
                        i++;
                    }
                    itemInfoModels[x][y] = iim;
                }
            }
        }
        cardInfoModel.setItemInfoModels(itemInfoModels);
        Log.d(TAG, "buildSettingCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    public static CardInfoModel buildCarouselHistoryCard(CardInfoModel cardInfoModel, CardStyle style) {
        GetInterfaceTools.getICarouselHistoryCacheManager().loadLocalToMemory();
        List channelInfoList = GetInterfaceTools.getICarouselHistoryCacheManager().getCarouselHistoryList();
        if (ListUtils.isEmpty(channelInfoList)) {
            Log.d(TAG, "buildCarouselHistoryCard(null), cardInfoModel = " + cardInfoModel);
        } else {
            Log.d(TAG, "channelInfoList size = " + channelInfoList.size());
            ItemInfoModel[][] itemInfoModels = getItems(style, channelInfoList.size());
            int i = 0;
            for (int x = 0; x < style.rows.size(); x++) {
                Row r = (Row) style.rows.get(x);
                for (int y = 0; y < r.items.size() && i < channelInfoList.size(); y++) {
                    Item item = (Item) r.items.get(y);
                    ItemInfoModel iim = null;
                    while (iim == null && i < channelInfoList.size()) {
                        iim = ItemInfoBuildTool.buildCarouselChannelItem((CarouselHistoryInfo) channelInfoList.get(i), item, cardInfoModel.isVIPTag);
                        i++;
                    }
                    itemInfoModels[x][y] = iim;
                }
            }
            cardInfoModel.setItemInfoModels(itemInfoModels);
            Log.d(TAG, "buildCarouselHistoryCard, cardInfoModel = " + cardInfoModel.getItemInfoModels());
        }
        return cardInfoModel;
    }

    public static CardInfoModel buildChannelListCard(CardInfoModel cardInfoModel, List<Channel> list) {
        CardStyle style = LayoutCache.getInstance().getCard().get(Integer.valueOf(cardInfoModel.cardLayoutId).intValue());
        ItemInfoModel[][] itemInfoModels = getItems(style, list.size());
        int i = 0;
        for (int x = 0; x < style.rows.size(); x++) {
            Row r = (Row) style.rows.get(x);
            for (int y = 0; y < r.items.size() && i < list.size(); y++) {
                Item item = (Item) r.items.get(y);
                ItemInfoModel iim = null;
                while (iim == null && i < list.size()) {
                    iim = ItemInfoBuildTool.buildChannelItem((Channel) list.get(i), item, cardInfoModel.isVIPTag);
                    i++;
                }
                itemInfoModels[x][y] = iim;
            }
        }
        cardInfoModel.setItemInfoModels(itemInfoModels);
        Log.d(TAG, "buildChannelListCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    public static CardInfoModel buildAppCard(CardInfoModel cardInfoModel, List<AppStore> apps) {
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (Precondition.isNull(result)) {
            cardInfoModel.setTitle("全部应用");
        } else {
            int app = result.getAppCard();
            if (app == 2) {
                cardInfoModel.setTitle("应用推荐");
            } else if (app == 3) {
                cardInfoModel.setTitle("应用");
            } else {
                cardInfoModel.setTitle("全部应用");
            }
        }
        CardStyle style = LayoutCache.getInstance().getCard().get(Integer.valueOf(cardInfoModel.cardLayoutId).intValue());
        ItemInfoModel[][] itemInfoModels = getItems(style, apps.size());
        int i = 0;
        for (int x = 0; x < style.rows.size(); x++) {
            Row r = (Row) style.rows.get(x);
            for (int y = 0; y < r.items.size() && i < apps.size(); y++) {
                Item item = (Item) r.items.get(y);
                ItemInfoModel iim = null;
                while (iim == null && i < apps.size()) {
                    iim = ItemInfoBuildTool.buildAppItem((AppStore) apps.get(i), item, cardInfoModel.isVIPTag);
                    i++;
                }
                itemInfoModels[x][y] = iim;
            }
        }
        cardInfoModel.setItemInfoModels(itemInfoModels);
        Log.d(TAG, "buildAppCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    public static CardInfoModel buildDefaultSettingCard(int cardLayoutId, int uikitEngineId) {
        CardStyle style = LayoutCache.getInstance().getCard().get(cardLayoutId);
        CardInfoModel cardInfo = new CardInfoModel();
        cardInfo.cardLayoutId = cardLayoutId;
        cardInfo.mSource = Source.CONFIGURATION;
        cardInfo.mUikitEngineId = uikitEngineId;
        cardInfo.setTitle("设置");
        setCardLayout(cardInfo, style);
        return buildSettingCard(cardInfo, style, SettingConfig.getDefaultSettingModels());
    }

    public static CardInfoModel buildBannerCard(CardInfoModel cardInfoModel, BannerAd banners) {
        CardStyle style = LayoutCache.getInstance().getCard().get(Integer.valueOf(cardInfoModel.cardLayoutId).intValue());
        setCardLayout(cardInfoModel, style);
        if (banners != null) {
            ItemInfoModel[][] itemInfoModels = getItems(style, 1);
            int i = 0;
            for (int x = 0; x < style.rows.size(); x++) {
                Row r = (Row) style.rows.get(x);
                for (int y = 0; y < r.items.size() && i < 1; y++) {
                    Item item = (Item) r.items.get(y);
                    ItemInfoModel iim = null;
                    while (iim == null && i < 1) {
                        iim = ItemInfoBuildTool.buildBannerItem(banners, item, cardInfoModel.isVIPTag);
                        i++;
                    }
                    itemInfoModels[x][y] = iim;
                }
            }
            cardInfoModel.setItemInfoModels(itemInfoModels);
        }
        Log.d(TAG, "buildBannerCard, cardInfoModel = " + cardInfoModel);
        return cardInfoModel;
    }

    private static CardInfoModel buildCardInfo(ResourceGroup group, String sourceId, int pageNo, int uikitEngineId, boolean hasBack, boolean isVipTag) {
        if (group == null || group.groupKvs == null || group.groupKvs.vrs_template_code == null) {
            return null;
        }
        String cardLayoutId = group.groupKvs.vrs_template_code;
        if (cardLayoutId.equals("")) {
            return null;
        }
        CardStyle style = LayoutCache.getInstance().getCard().get(Integer.valueOf(cardLayoutId).intValue());
        if (style == null) {
            Log.d(TAG, "style获取失败, cardLayoutId = " + cardLayoutId);
            return null;
        }
        CardInfoModel cardInfo = new CardInfoModel();
        cardInfo.isVIPTag = isVipTag;
        cardInfo.cardLayoutId = Integer.valueOf(cardLayoutId).intValue();
        cardInfo.mSource = DataBuildTool.getSourceType(group.groupKvs.source, style.type);
        cardInfo.mPageNo = pageNo;
        cardInfo.mUikitEngineId = uikitEngineId;
        cardInfo.mCardId = group.id;
        setCardLayout(cardInfo, style);
        cardInfo.setTitle(group.groupKvs.card_name);
        if (cardInfo.mSource == null || cardInfo.mSource.equals("none")) {
            ItemParams itemParams = buildItems(cardInfo, style, group.items, group.groupKvs.canShort(), cardLayoutId, hasBack, group.groupKvs.isSort(), isVipTag, style.backId != (short) 0);
            if (!itemParams.isBack || hasBack) {
                cardInfo.setItemInfoModels(itemParams.items);
            } else {
                Log.d(TAG, "pageid = " + sourceId + ",Card " + group.id + " degeneration, " + cardLayoutId + " degenerate to " + style.backId);
                group.groupKvs.vrs_template_code = String.valueOf(style.backId);
                return buildCardInfo(group, sourceId, pageNo, uikitEngineId, true, isVipTag);
            }
        } else if (cardInfo.mSource.equals(Source.CONFIGURATION)) {
            return buildSettingCard(cardInfo, style, SettingConfig.getHomeSettingModels());
        } else {
            if (cardInfo.mSource.equals(Source.CAROUSEL_HISTORY) && Project.getInstance().getControl().isOpenCarousel()) {
                return buildCarouselHistoryCard(cardInfo, style);
            }
            if (cardInfo.mSource.equals(Source.CHANNEL_LIST)) {
                List<Channel> channelList = UikitSourceDataCache.readChannelDataList();
                if (channelList != null) {
                    return buildChannelListCard(cardInfo, channelList);
                }
            } else if (cardInfo.mSource.equals(Source.APPLICATION)) {
                if (AppRequest.checkApp() == 0) {
                    return null;
                }
                List<AppStore> appList = UikitSourceDataCache.readAppDataList();
                if (appList != null) {
                    return buildAppCard(cardInfo, appList);
                }
            } else if (cardInfo.mSource.equals("setting")) {
                return buildSettingCard(cardInfo, style, SettingConfig.getMineSettingModels());
            } else {
                if (cardInfo.mSource.equals("history")) {
                    cardInfo.canShort = group.groupKvs.canShort();
                }
            }
        }
        if ((cardInfo.mSource.equals(Source.SUPER_ALBUM) || cardInfo.mSource.equals("star") || cardInfo.mSource.equals(Source.TRAILERS) || cardInfo.mSource.equals("recommend") || cardInfo.mSource.equals(Source.ABOUT_TOPIC)) && group.items != null && group.items.size() > 1 && DataBuildTool.getItemType((ChannelLabel) group.items.get(1)) == ItemDataType.ENTER_ALL && !Precondition.isEmpty(group.groupKvs.enterall_layout_id)) {
            cardInfo.tvTagAll = (ChannelLabel) group.items.get(1);
            try {
                cardInfo.cardLayoutId_entryAll = Integer.valueOf(group.groupKvs.enterall_layout_id).intValue();
            } catch (Exception e) {
            }
        }
        Log.d(TAG, "buildCommonCard,source = " + cardInfo.mSource + ", cardInfoModel = " + cardInfo);
        return cardInfo;
    }

    private static void setCardLayout(CardInfoModel cardInfoModel, CardStyle style) {
        cardInfoModel.setBodyHeight(style.body_h);
        cardInfoModel.setBodyMarginBottom(style.body_mg_b);
        cardInfoModel.setBodyMarginLeft(style.body_mg_l);
        cardInfoModel.setBodyMarginRight(style.body_mg_r);
        cardInfoModel.setBodyMarginTop(style.body_mg_t);
        cardInfoModel.setBodyPaddingBottom(style.body_pd_b);
        cardInfoModel.setBodyPaddingRight(style.body_pd_r);
        cardInfoModel.setBodyPaddingLeft(style.body_pd_l);
        cardInfoModel.setBodyPaddingTop(style.body_pd_t);
        cardInfoModel.setCardType(style.type);
        cardInfoModel.setScale(style.scale);
        cardInfoModel.setDefaultFocus(style.default_focus);
        cardInfoModel.setHeaderHeight(style.header_h);
        cardInfoModel.setHeaderPaddingBottom(style.header_pd_b);
        cardInfoModel.setHeaderPaddingTop(style.header_pd_t);
        cardInfoModel.setHeaderPaddingRight(style.header_pd_r);
        cardInfoModel.setHeaderPaddingLeft(style.header_pd_l);
        cardInfoModel.setShowPosition(style.show_position);
        cardInfoModel.setSpaceH(style.space_h);
        cardInfoModel.setSpaceV(style.space_v);
        cardInfoModel.setWidth(style.f2039w);
        cardInfoModel.setId(String.valueOf(cardInfoModel.hashCode()));
    }

    private static ItemInfoModel[][] getItems(CardStyle style, int size) {
        checkRows(style, size);
        int x = style.rows.size();
        ItemInfoModel[][] items = new ItemInfoModel[x][];
        for (int i = 0; i < x; i++) {
            items[i] = new ItemInfoModel[((Row) style.rows.get(i)).items.size()];
        }
        return items;
    }

    private static void checkRows(CardStyle style, int size) {
        if (style.row_nolimit == (short) 1) {
            Row row = (Row) style.rows.get(0);
            if (row.items != null && row.items.size() > 0) {
                for (size -= row.items.size(); size > 0; size -= row.items.size()) {
                    style.rows.add(row);
                }
            }
        }
    }

    private static void replaceTvTagAll(List<Row> rows, List<ChannelLabel> labels) {
        if (labels != null && labels.size() > 0) {
            ItemDataType itemDataType = DataBuildTool.getItemType((ChannelLabel) labels.get(0));
            if (itemDataType == ItemDataType.TV_TAG_ALL || itemDataType == ItemDataType.ENTER_ALL) {
                ChannelLabel tvTagAll = (ChannelLabel) labels.get(0);
                labels.remove(0);
                int itemSize = 0;
                for (int rowLine = 0; rowLine < rows.size(); rowLine++) {
                    itemSize += ((Row) rows.get(rowLine)).items.size();
                }
                if (itemDataType == ItemDataType.TV_TAG_ALL) {
                    if (itemSize > labels.size()) {
                        labels.add(tvTagAll);
                    } else {
                        labels.add(itemSize - 1, tvTagAll);
                    }
                } else if (itemDataType == ItemDataType.ENTER_ALL && itemSize < labels.size()) {
                    labels.add(itemSize - 1, tvTagAll);
                }
            }
        }
    }

    private static ItemParams buildItems(CardInfoModel cardInfo, CardStyle style, List<ChannelLabel> labels, boolean canShort, String cardLayoutId, boolean hasBack, boolean isSort, boolean isVipTag, boolean shouldBack) {
        ItemParams params = new ItemParams();
        int backSize = 0;
        if (style.backId != (short) 0) {
            try {
                backSize = ((Row) style.rows.get(0)).items.size();
            } catch (Exception e) {
                return null;
            }
        }
        boolean z = shouldBack && !hasBack;
        List<ChannelLabel> items = ItemFilter.filtrateItems(labels, style, backSize, z);
        if (items == null) {
            params.isBack = true;
            return params;
        } else if (items.size() == 0) {
            return params;
        } else {
            ItemInfoModel[][] itemInfoModels = getItems(style, items.size());
            replaceTvTagAll(style.rows, items);
            int i = 0;
            int maxX = style.rows.size();
            for (int x = 0; x < maxX; x++) {
                Row r = (Row) style.rows.get(x);
                if (!canShort && r.items.size() > items.size() - i && style.type != UIKitConfig.CARD_TYPE_COVER_FLOW && style.type != UIKitConfig.CARD_TYPE_BLANK) {
                    Log.d(TAG, "不可缺");
                    break;
                }
                int maxY = r.items.size();
                for (int y = 0; y < maxY; y++) {
                    Item itemStyle = (Item) r.items.get(y);
                    if (i >= items.size()) {
                        break;
                    }
                    itemInfoModels[x][y] = ItemInfoBuildTool.buildItem((ChannelLabel) items.get(i), itemStyle, Short.valueOf(cardLayoutId).shortValue(), style.type, hasBack, i, isSort, isVipTag);
                    i++;
                }
            }
            resetCardBodyMgB(cardInfo, itemInfoModels);
            params.items = itemInfoModels;
            return params;
        }
    }

    private static void resetCardBodyMgB(CardInfoModel cardInfo, ItemInfoModel[][] itemInfoModels) {
        try {
            if (itemInfoModels[itemInfoModels.length - 1][0] == null) {
                boolean titleOut = itemInfoModels[itemInfoModels.length - 2][0].getStyle().startsWith("titleout");
                if (titleOut) {
                    if (pxShort26 != cardInfo.getBodyMarginBottom()) {
                        cardInfo.setBodyMarginBottom(pxShort26);
                        Log.d(TAG, "resetCardBodyMgB, titleOut=" + titleOut + "," + cardInfo);
                    }
                } else if (pxShort60 != cardInfo.getBodyMarginBottom()) {
                    cardInfo.setBodyMarginBottom(pxShort60);
                    Log.d(TAG, "resetCardBodyMgB, titleOut=" + titleOut + "," + cardInfo);
                }
            }
        } catch (Exception e) {
        }
    }

    private static boolean checkCard(CardInfoModel cardInfoModel) {
        if (cardInfoModel != null) {
            if (cardInfoModel.mSource != null && !cardInfoModel.mSource.equals("none")) {
                return true;
            }
            ItemInfoModel[][] itemInfoModels = cardInfoModel.getItemInfoModels();
            if (itemInfoModels != null && itemInfoModels.length > 0 && itemInfoModels[0].length > 0 && itemInfoModels[0][0] != null) {
                return true;
            }
        }
        return false;
    }
}
