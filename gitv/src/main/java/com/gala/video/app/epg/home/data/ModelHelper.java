package com.gala.video.app.epg.home.data;

import com.alibaba.fastjson.JSONArray;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Star;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.tvapi.vrs.model.Video;
import com.gala.video.app.epg.home.component.item.corner.HomeCornerProvider;
import com.gala.video.app.epg.home.data.tool.DataBuildTool;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.IModelHelper.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.PageModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.qiyi.tv.client.feature.common.MediaFactory;

public class ModelHelper extends Wrapper {
    public ItemModel convertAlbumToItemModel(Album album) {
        boolean z = true;
        ItemModel itemModel = new ItemModel();
        if (album != null) {
            int i;
            ChannelLabel label = new ChannelLabel();
            label.sourceId = album.sourceCode;
            if (album.type == AlbumType.VIDEO.getValue()) {
                label.itemType = MediaFactory.TYPE_VIDEO;
                itemModel.setItemType(ItemDataType.VIDEO);
                label.tvQipuId = album.tvQid;
                label.vid = album.vid;
                label.albumQipuId = album.qpId;
                if (album.vipInfo != null) {
                    if (album.vipInfo.epIsVip == 1) {
                        label.purchaseType = 1;
                    } else if (album.vipInfo.epIsTvod == 1) {
                        label.purchaseType = 2;
                    } else if (album.vipInfo.epIsCoupon == 1) {
                        label.purchaseType = 3;
                    }
                    label.boss = 2;
                }
                if (!(album.drm == null || album.drm.equals("1"))) {
                    String[] tmp = album.drm.split(",");
                    JSONArray array = new JSONArray();
                    if (tmp != null && tmp.length > 0) {
                        for (String s : tmp) {
                            if (s.equals("2")) {
                                array.add(Integer.valueOf(3));
                            } else if (s.equals("3")) {
                                array.add(Integer.valueOf(5));
                            }
                        }
                    }
                    label.drmTypes = array.toJSONString();
                }
            } else {
                label.itemType = MediaFactory.TYPE_ALBUM;
                itemModel.setItemType(ItemDataType.ALBUM);
                label.video = new Video();
                label.video.qipuId = album.tvQid;
                label.video.vid = album.vid;
                label.albumQipuId = album.qpId;
                if (album.vipInfo != null) {
                    if (album.vipInfo.isVip == 1) {
                        label.purchaseType = 1;
                    } else if (album.vipInfo.isTvod == 1) {
                        label.purchaseType = 2;
                    } else if (album.vipInfo.isCoupon == 1) {
                        label.purchaseType = 3;
                    }
                    label.boss = 2;
                }
            }
            if (!(album.stream == null || album.stream.isEmpty())) {
                if (album.stream.contains("1080P")) {
                    i = 1;
                } else {
                    i = 0;
                }
                label.is1080P = i;
                if (album.stream.contains("720p_dolby")) {
                    i = 1;
                } else {
                    i = 0;
                }
                label.isDubi = i;
            }
            label.exclusive = album.exclusive;
            label.channelId = album.chnId;
            label.albumName = album.name;
            label.postImage = album.tvPic;
            label.imageUrl = album.pic;
            label.isSeries = album.isSeries;
            label.name = album.tvName;
            label.score = album.score;
            label.tvCount = album.tvsets;
            label.isD3 = album.is3D;
            label.latestOrder = album.tvCount;
            label.issueTimeStamp = album.initIssueTime;
            label.channelName = album.chnName;
            label.duration = album.len;
            label.shortTitle = album.shortName;
            label.itemShortDisplayName = album.tvName;
            label.currentPeriod = album.time;
            label.order = album.order;
            label.payMark = TVApiTool.getPayMarkValue(album.getPayMarkType());
            label.contentType = TVApiTool.getContentTypeValue(album.getContentType());
            itemModel.setData(label);
            itemModel.setPic(label.imageUrl);
            itemModel.setTvPic(label.postImage);
            itemModel.setItemPic(label.itemImageUrl);
            itemModel.setTitle(DataBuildTool.getPrompt(label));
            if (label.boss <= 1) {
                z = false;
            }
            itemModel.setIsVip(z);
            itemModel.setChannelId(label.channelId);
            itemModel.setQpId(album.qpId);
            itemModel.setTvId(album.tvQid);
        }
        return itemModel;
    }

    public ItemModel convertStarToItemModel(Star star) {
        ItemModel itemModel = new ItemModel();
        if (star != null) {
            itemModel.setItemType(ItemDataType.STAR);
            itemModel.setQpId(star.id);
            itemModel.setTvId(star.id);
            itemModel.setTitle(star.name);
            itemModel.setItemPic(star.cover);
            itemModel.setPic(star.cover);
            itemModel.setTvPic(star.cover);
            ChannelLabel label = new ChannelLabel();
            label.itemId = star.id;
            itemModel.setData(label);
            itemModel.setItemType(ItemDataType.STAR);
            itemModel.setItemPic(DataBuildTool.resizeImage(star.cover, "_300_300"));
        }
        return itemModel;
    }

    public ItemModel convertTabModelToItemModel(TabModel tabModel) {
        ItemModel itemModel = new ItemModel();
        if (tabModel != null) {
            itemModel.setTabSrc(tabModel.getTitle());
            itemModel.setIsVipTab(tabModel.isVipTab());
        }
        return itemModel;
    }

    public DataSource convertToDataSource(HomeModel model) {
        String className = model.getClass().getSimpleName();
        if (className.equals(ItemModel.class.getSimpleName())) {
            return convertItemModelToDataSource((ItemModel) model);
        }
        if (className.equals(TabModel.class.getSimpleName())) {
            return convertTabModelToDataSource((TabModel) model);
        }
        if (className.equals(PageModel.class.getSimpleName())) {
            return convertPageModelToDataSource((PageModel) model);
        }
        if (className.equals(CardModel.class.getSimpleName())) {
            return convertCardModelToDataSource((CardModel) model);
        }
        if (className.equals(BannerImageAdModel.class.getSimpleName())) {
            return convertBannerImageAdModelToDataSource((BannerImageAdModel) model);
        }
        if (className.equals(HomeFocusImageAdModel.class.getSimpleName())) {
            return convertHomeFocusImageAdModelToDataSource((HomeFocusImageAdModel) model);
        }
        return null;
    }

    private DataSource convertItemModelToDataSource(ItemModel model) {
        ItemData data = new ItemData();
        if (model != null) {
            data.setChannel(model.getChannel());
            data.setTitle(model.getTitle());
            data.setImage(model.getItemPic());
            data.setHigh(model.getHigh());
            data.setWidth(model.getWidth());
            data.mStatus = model.getWidgetChangeStatus();
            data.mPostImageUrl = model.getTvPic();
            data.mTVImageUrl = model.getPic();
            data.setChnId(model.getChannelId());
            data.setItemType(model.getItemType());
            data.setIconUrl(model.getIcon());
            if (model.getItemType() == ItemDataType.LIVE_CHANNEL) {
                data.isCarouselChannel = true;
            }
            data.carouselChannelId = String.valueOf(model.getTableNo());
            data.circleHasTitle = model.isTitle();
            data.setOnlineTime(model.getOnlineTime());
            data.isVipTab = model.isVipTab();
            data.setLiveId(model.getLiveId());
            data.setTabNo(model.getTableNo());
            data.mLabel = model.getData();
            data.pingbackTabSrc = model.getTabSrc();
            data.pageUrl = model.getUrl();
            data.plId = model.playListId;
            if (model.getData() != null) {
                HomeCornerProvider.handleItemCorner(data, model);
            }
            if (!StringUtils.isEmpty(model.getDesL1RBString())) {
                data.desL1RBString = model.getDesL1RBString();
            }
            data.setDataIndex(model.getDataIndex());
            data.setIconResId(model.getNormalIconId());
            data.setIconFocusedResId(model.getFocusedIconId());
            data.mGif = model.mGifPic;
            data.mGifWidth = model.mGifWidth;
            data.mGifHigh = model.mGifHigh;
            data.isPlaying = model.mIsPlaying;
            data.setRecommendAppKey(model.getRecommendAppKey());
        }
        return data;
    }

    private DataSource convertTabModelToDataSource(TabModel model) {
        TabData data = new TabData();
        data.setChannelId(model.getChannelId());
        data.setIsNew(model.isNew());
        data.setTitle(model.getTitle());
        data.setWidgetChangeStatus(model.getWidgetChangeStatus());
        data.setIsFocusTab(model.isFocusTab());
        data.setIsVipTab(model.isVipTab());
        data.setIsChannelTab(model.isChannelTab());
        data.setResourceId(model.getResourceGroupId());
        return data;
    }

    private DataSource convertPageModelToDataSource(PageModel model) {
        PageData data = new PageData();
        data.setIsNew(model.isNew());
        data.setResourceId(model.getResourceId());
        data.setWidgetChangeStatus(model.getWidgetChangeStatus());
        return data;
    }

    public TabDataItem convertToTabDataItem(DailyLabelModel model) {
        TabDataItem tab = new TabDataItem();
        try {
            tab.setLabelImageUrl(model.mLabelImageUrl);
            tab.setLabelName(model.mLabelName);
            DailyLabel label = new DailyLabel();
            label.channelId = model.mLabelId;
            label.name = model.mLabelName;
            label.tagSet = model.mLabelArea;
            tab.setLabel(label);
            tab.setTabContentAlbumsList(model.mDailyNewModelList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }

    private DataSource convertCardModelToDataSource(CardModel model) {
        CardData data = new CardData();
        data.id = model.getId();
        data.setTitle(model.getTitle());
        data.setCardLine(model.getCardLine());
        data.setCardChildCount(model.getSize());
        data.setCardTemplateId(model.getTemplateId());
        data.setCardType(model.getWidgetType());
        data.setVipTab(model.isVipTab());
        data.setAllEntry(model.hasAllEntry());
        data.setAllEntryPosition(model.getAllEntryPosition());
        return data;
    }

    private DataSource convertBannerImageAdModelToDataSource(BannerImageAdModel model) {
        BannerImageAdItemData adItemData = new BannerImageAdItemData();
        adItemData.setAdId(model.getAdId());
        adItemData.setNeedAdBadge(model.getNeedAdBadge());
        adItemData.setClickThroughType(model.getClickThroughType());
        adItemData.setClickThroughInfo(model.getClickThroughInfo());
        adItemData.setTitle(model.getTitle());
        adItemData.setImage(model.getImageUrl());
        LogUtils.d("model/BannerImageAdModel", "Imageurl :" + model.getImageUrl());
        adItemData.mGif = model.getImageUrl();
        adItemData.setPlId(model.getPlId());
        adItemData.setAlbumId(model.getAlbumId());
        adItemData.setTvId(model.getTvId());
        adItemData.setCarouselId(model.getCarouselId());
        adItemData.setCarouselNo(model.getCarouselNo());
        adItemData.setCarouselName(model.getCarouselName());
        adItemData.setFocusImageAdType(model.getAdClickType());
        adItemData.setItemType(model.getItemType());
        adItemData.setWidth(model.getWidth());
        adItemData.setHigh(model.getHigh());
        adItemData.setDefWidth(model.getDefWidth());
        adItemData.setDefHeight(model.getDefHeight());
        adItemData.mGifWidth = model.mGifWidth;
        adItemData.mGifHigh = model.mGifHigh;
        return adItemData;
    }

    private DataSource convertHomeFocusImageAdModelToDataSource(HomeFocusImageAdModel model) {
        FocusImageAdItemData adItemData = new FocusImageAdItemData();
        adItemData.setAdId(model.getAdId());
        adItemData.setClickThroughType(model.getClickThroughType());
        adItemData.setClickThroughInfo(model.getClickThroughInfo());
        adItemData.setTitle(model.getTitle());
        adItemData.setImage(model.getImageUrl());
        adItemData.setPlId(model.getPlId());
        adItemData.setAlbumId(model.getAlbumId());
        adItemData.setTvId(model.getTvId());
        adItemData.setCarouselId(model.getCarouselId());
        adItemData.setCarouselNo(model.getCarouselNo());
        adItemData.setCarouselName(model.getCarouselName());
        adItemData.setFocusImageAdType(model.getAdClickType());
        adItemData.setItemType(model.getItemType());
        adItemData.setWidth(model.getWidth());
        adItemData.setHigh(model.getHeight());
        adItemData.setAdIndex(model.getAdIndex());
        adItemData.setDefWidth(model.getDefWidth());
        adItemData.setDefHeight(model.getDefHeight());
        adItemData.setNeedAdBadge("true".equals(model.getNeedAdBadge()));
        return adItemData;
    }
}
