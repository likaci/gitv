package com.gala.video.lib.share.uikit.data.data.processor;

import com.alibaba.fastjson.JSONArray;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.model.PltRegionCtrls;
import com.gala.tvapi.vrs.model.RegionCtrls;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.tvapi.vrs.model.Video;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.gala.video.lib.share.utils.Precondition;
import com.qiyi.tv.client.feature.common.MediaFactory;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataBuildTool {

    private enum IpType {
        TRUE,
        FALSE,
        UNKNOW
    }

    public static ItemDataType getItemType(ChannelLabel label) {
        ResourceType type = label.getType();
        if (type == ResourceType.DIY) {
            if (label.itemKvs != null) {
                ItemKvs kv = label.itemKvs;
                if (!Precondition.isEmpty(kv.tvtag)) {
                    TVTags tags = label.itemKvs.getTVTag();
                    if (tags == null || (tags.tags == null && tags.channelId == 0)) {
                        return ItemDataType.NONE;
                    }
                    if (kv.isFirst == 1) {
                        return ItemDataType.TV_TAG_ALL;
                    }
                    return ItemDataType.TV_TAG;
                } else if (!Precondition.isEmpty(kv.tvfunction)) {
                    String fun = kv.tvfunction.trim();
                    if (fun.equalsIgnoreCase(ItemDataType.SEARCH.getValue())) {
                        return ItemDataType.SEARCH;
                    }
                    if (fun.equalsIgnoreCase(ItemDataType.RECORD.getValue())) {
                        if (Project.getInstance().getBuild().isHomeVersion()) {
                            return ItemDataType.NONE;
                        }
                        return ItemDataType.RECORD;
                    } else if (fun.equalsIgnoreCase(ItemDataType.SEARCH_RECORD.getValue())) {
                        if (Project.getInstance().getBuild().isHomeVersion()) {
                            return ItemDataType.NONE;
                        }
                        return ItemDataType.SEARCH_RECORD;
                    } else if (fun.equalsIgnoreCase(ItemDataType.DAILY.getValue())) {
                        if (!Project.getInstance().getBuild().isSupportSmallWindowPlay() || TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN) {
                            return ItemDataType.NONE;
                        }
                        return ItemDataType.DAILY;
                    } else if (fun.equalsIgnoreCase(ItemDataType.APP.getValue())) {
                        return ItemDataType.APP;
                    } else {
                        if (fun.equalsIgnoreCase(ItemDataType.SETTING.getValue())) {
                            return ItemDataType.SETTING;
                        }
                        if (fun.equalsIgnoreCase(ItemDataType.CHANNEL.getValue())) {
                            return ItemDataType.CHANNEL;
                        }
                        if (fun.equalsIgnoreCase(ItemDataType.CAROUSEL.getValue())) {
                            if (TVApiBase.getTVApiProperty().getPlatform() != PlatformType.TAIWAN) {
                                return ItemDataType.CAROUSEL;
                            }
                            return ItemDataType.NONE;
                        } else if (fun.equalsIgnoreCase(ItemDataType.PLST_GROUP.getValue())) {
                            if (TVApiBase.getTVApiProperty().getPlatform() != PlatformType.TAIWAN) {
                                return ItemDataType.PLST_GROUP;
                            }
                            return ItemDataType.NONE;
                        } else if (fun.equalsIgnoreCase(ItemDataType.RECOMMEND.getValue())) {
                            return ItemDataType.RECOMMEND;
                        } else {
                            if (fun.equalsIgnoreCase(ItemDataType.SUPER_ALBUM.getValue())) {
                                return ItemDataType.SUPER_ALBUM;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.STAR.getValue())) {
                                return ItemDataType.STAR;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.TRAILERS.getValue())) {
                                return ItemDataType.TRAILERS;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.RECOMMEND_APP.getValue())) {
                                return ItemDataType.RECOMMEND_APP;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.VIP_BUY.getValue())) {
                                return ItemDataType.VIP_BUY;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.VIP_VIDEO.getValue())) {
                                return ItemDataType.VIP_VIDEO;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.COLLECTION.getValue())) {
                                return ItemDataType.COLLECTION;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.SUBSCRIBE.getValue())) {
                                return ItemDataType.SUBSCRIBE;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.JUMP_TO_H5.getValue())) {
                                return ItemDataType.JUMP_TO_H5;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.MSGCENTER.getValue())) {
                                return ItemDataType.MSGCENTER;
                            }
                            if (fun.equalsIgnoreCase(ItemDataType.ENTER_ALL.getValue())) {
                                return ItemDataType.ENTER_ALL;
                            }
                        }
                    }
                }
            }
            return ItemDataType.H5;
        } else if (type == ResourceType.COLLECTION) {
            return ItemDataType.PLAY_LIST;
        } else {
            if (type == ResourceType.LIVE) {
                return ItemDataType.LIVE;
            }
            if (type == ResourceType.PERSON) {
                return ItemDataType.PERSON;
            }
            if (type == ResourceType.ALBUM) {
                return ItemDataType.ALBUM;
            }
            if (type == ResourceType.VIDEO) {
                return ItemDataType.VIDEO;
            }
            if (type == ResourceType.LIVE_CHANNEL) {
                return ItemDataType.LIVE_CHANNEL;
            }
            if (type == ResourceType.RESOURCE_GROUP) {
                return ItemDataType.RESOURCE_GROUP;
            }
            return ItemDataType.NONE;
        }
    }

    public static String getPrompt(ChannelLabel label) {
        if (!Precondition.isEmpty(label.itemShortDisplayName)) {
            return label.itemShortDisplayName;
        }
        if (!Precondition.isEmpty(label.itemName)) {
            return label.itemName;
        }
        if (!Precondition.isEmpty(label.shortTitle)) {
            return label.shortTitle;
        }
        if (label.itemKvs == null || Precondition.isEmpty(label.itemKvs.tvShowName)) {
            return label.name;
        }
        return label.itemKvs.tvShowName;
    }

    public static boolean checkRegionAvailable(ChannelLabel label) {
        if (label.pltRegionCtrls != null) {
            String[] ipLoc = DeviceCheckModel.getInstance().getIpLoc();
            if (!Precondition.isEmpty(ipLoc) && ipLoc.length >= 5) {
                PltRegionCtrls pltRegionCtrls = label.pltRegionCtrls;
                List<RegionCtrls> regionCtrls = pltRegionCtrls.regionCtrls;
                if (regionCtrls != null && regionCtrls.size() > 0) {
                    for (RegionCtrls regionCtrl : regionCtrls) {
                        switch (checkIpLoc(regionCtrl.status, regionCtrl.getCityIds(), ipLoc[4])) {
                            case FALSE:
                                if (!LogUtils.mIsDebug) {
                                    return false;
                                }
                                LogUtils.d("checkRegionAvailable", "name = " + label.itemName + ", id = " + label.itemId + ", reason = [ status = " + regionCtrl.status + ", cityids = " + Arrays.toString(regionCtrl.getCityIds()) + ", iploc[4]= " + ipLoc[4]);
                                return false;
                            case TRUE:
                                return true;
                            default:
                        }
                    }
                    for (RegionCtrls regionCtrl2 : regionCtrls) {
                        switch (checkIpLoc(regionCtrl2.status, regionCtrl2.getProvinceIds(), ipLoc[3])) {
                            case FALSE:
                                if (!LogUtils.mIsDebug) {
                                    return false;
                                }
                                LogUtils.d("checkRegionAvailable", "name = " + label.name + ", id = " + label.itemId + ", reason = [ status = " + regionCtrl2.status + ", getProvinceIds = " + Arrays.toString(regionCtrl2.getProvinceIds()) + ", iploc[3]= " + ipLoc[3]);
                                return false;
                            case TRUE:
                                return true;
                            default:
                        }
                    }
                    for (RegionCtrls regionCtrl22 : regionCtrls) {
                        switch (checkIpLoc(regionCtrl22.status, regionCtrl22.getCountryIds(), ipLoc[1])) {
                            case FALSE:
                                if (!LogUtils.mIsDebug) {
                                    return false;
                                }
                                LogUtils.d("checkRegionAvailable", "name = " + label.name + ", id = " + label.itemId + ", reason = [ status = " + regionCtrl22.status + ", getCountryIds = " + Arrays.toString(regionCtrl22.getCountryIds()) + ", iploc[1]= " + ipLoc[1]);
                                return false;
                            case TRUE:
                                return true;
                            default:
                        }
                    }
                    for (RegionCtrls regionCtrl222 : regionCtrls) {
                        switch (checkIpLoc(regionCtrl222.status, regionCtrl222.getAreaIds(), ipLoc[2])) {
                            case FALSE:
                                if (!LogUtils.mIsDebug) {
                                    return false;
                                }
                                LogUtils.d("checkRegionAvailable", "name = " + label.name + ", id = " + label.itemId + ", reason = [ status = " + regionCtrl222.status + ", getAreaIds = " + Arrays.toString(regionCtrl222.getAreaIds()) + ", iploc[1]= " + ipLoc[2]);
                                return false;
                            case TRUE:
                                return true;
                            default:
                        }
                    }
                }
                if (LogUtils.mIsDebug) {
                    boolean defaultStatus = pltRegionCtrls.defaultStatus == 1 || pltRegionCtrls.defaultStatus == 4;
                    if (!defaultStatus) {
                        LogUtils.d("checkRegionAvailable", "name = " + label.name + ", id = " + label.itemId + ", reason = [ defaultStatus = " + pltRegionCtrls.defaultStatus);
                    }
                }
                if (pltRegionCtrls.defaultStatus == 1 || pltRegionCtrls.defaultStatus == 4) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }

    private static IpType checkIpLoc(int status, String[] list, String ipLoc) {
        boolean blackFlag = false;
        if (list != null && list.length > 0) {
            for (String str : list) {
                if (str.equals(ipLoc)) {
                    if (status == 1 || status == 4) {
                        return IpType.TRUE;
                    }
                    blackFlag = true;
                }
            }
            if (blackFlag) {
                return IpType.FALSE;
            }
        }
        return IpType.UNKNOW;
    }

    public static String getVideoTime(ChannelLabel label) {
        if (label.itemKvs != null && !Precondition.isEmpty(label.itemKvs.showTime)) {
            return label.itemKvs.showTime;
        }
        String time = Precondition.isEmpty(label.issueTimeStamp) ? label.issueTime : label.issueTimeStamp;
        long timeL = DateLocalThread.getTime(time);
        return timeL != 1 ? DateLocalThread.parseTimeForHomeCard(timeL) : time;
    }

    public static String getLiveTime(ChannelLabel label) {
        if (label.itemKvs != null && !Precondition.isEmpty(label.itemKvs.showTime)) {
            return label.itemKvs.showTime;
        }
        if (label.itemKvs != null) {
            try {
                return DateLocalThread.formatM(DateLocalThread.parseYH(label.itemKvs.LiveEpisode_StartTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        try {
            return DateLocalThread.formatM(new Date(Long.parseLong(label.startTime)));
        } catch (Exception e2) {
            e2.printStackTrace();
            return "";
        }
    }

    public static int[] getImageSize(String size) {
        try {
            String[] s = size.split("_");
            if (!Precondition.isEmpty(s) && s.length == 2) {
                return new int[]{Integer.parseInt(s[0]), Integer.parseInt(s[1])};
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static String getH5Url(ChannelLabel label) {
        if (!Precondition.isNull(label.itemKvs) && !Precondition.isEmpty(label.itemKvs.pageUrl)) {
            return label.itemKvs.pageUrl;
        }
        if (Precondition.isEmpty(label.itemPageUrl)) {
            return "";
        }
        return label.itemPageUrl;
    }

    public static String getSourceType(String str, short cardType) {
        if (StringUtils.isEmpty((CharSequence) str)) {
            return "none";
        }
        if (str.equals(Source.ABOUT_TOPIC) && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return Source.ABOUT_TOPIC;
        }
        if (str.equals(Source.APPLICATION) && cardType == UIKitConfig.CARD_TYPE_APP_AND_SETTING) {
            return Source.APPLICATION;
        }
        if (str.equals(Source.CONFIGURATION) && cardType == UIKitConfig.CARD_TYPE_APP_AND_SETTING) {
            return Source.CONFIGURATION;
        }
        if (str.equals(Source.CHANNEL_LIST) && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return Source.CHANNEL_LIST;
        }
        if (str.equals(Source.CAROUSEL_HISTORY) && cardType == UIKitConfig.CARD_TYPE_GRID_CARD) {
            return Source.CAROUSEL_HISTORY;
        }
        if (str.equals(Source.SUPER_ALBUM) && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return Source.SUPER_ALBUM;
        }
        if (str.equals("star") && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return "star";
        }
        if (str.equals("recommend") && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return "recommend";
        }
        if (str.equals(Source.TRAILERS) && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return Source.TRAILERS;
        }
        if (str.equals("history") && cardType == UIKitConfig.CARD_TYPE_FLOW) {
            return "history";
        }
        if (str.equals("setting") && cardType == UIKitConfig.CARD_TYPE_APP_AND_SETTING) {
            return "setting";
        }
        return "none";
    }

    public static ChannelLabel albumToChannelLabel(Album album) {
        int i = 2;
        int i2 = 1;
        if (album == null) {
            return null;
        }
        ChannelLabel label = new ChannelLabel();
        label.albumFrom = album.albumFrom;
        label.sourceId = album.sourceCode;
        if (album.type == AlbumType.VIDEO.getValue()) {
            label.itemType = MediaFactory.TYPE_VIDEO;
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
            }
        }
        if (!album.isPurchase()) {
            i = 0;
        }
        label.boss = i;
        if (album.getContentType() != ContentType.FEATURE_FILM && album.albumFrom == AlbumFrom.TRAILERS) {
            label.itemType = "DIY";
            label.itemKvs = new ItemKvs();
            label.itemKvs.tvfunction = Source.TRAILERS;
        }
        if (!(album.stream == null || album.stream.isEmpty())) {
            if (album.stream.contains("1080P")) {
                i = 1;
            } else {
                i = 0;
            }
            label.is1080P = i;
            if (!album.stream.contains("720p_dolby")) {
                i2 = 0;
            }
            label.isDubi = i2;
        }
        try {
            String[] categoryNames = album.tag.split(",");
            if (categoryNames != null && categoryNames.length > 0) {
                label.categoryNames = new ArrayList();
                for (Object add : categoryNames) {
                    label.categoryNames.add(add);
                }
            }
        } catch (Exception e) {
        }
        label.albumName = album.name;
        label.exclusive = album.exclusive;
        label.channelId = album.chnId;
        label.itemName = album.name;
        if (album.albumFrom == AlbumFrom.RECOMMAND_VIDEO) {
            label.imageUrl = album.videoImageUrl;
        } else {
            label.postImage = album.tvPic;
            label.imageUrl = album.pic;
        }
        label.isSeries = album.isSeries;
        label.name = album.tvName;
        label.score = album.score;
        label.tvCount = album.tvsets;
        label.isD3 = album.is3D;
        label.latestOrder = album.tvCount;
        label.issueTimeStamp = album.initIssueTime;
        label.issueTime = album.initIssueTime;
        label.channelName = album.chnName;
        label.duration = album.len;
        label.shortTitle = album.shortName;
        label.itemShortDisplayName = album.tvName;
        label.currentPeriod = album.time;
        label.order = album.order;
        label.payMark = TVApiTool.getPayMarkValue(album.getPayMarkType());
        label.contentType = TVApiTool.getContentTypeValue(album.getContentType());
        return label;
    }

    public static Short getNewTyep(ItemDataType itemDataType, short cardType) {
        if (cardType == UIKitConfig.CARD_TYPE_TOBE_ONLINE) {
            return Short.valueOf(UIKitConfig.ITEM_TYPE_SUBSCRIBE);
        }
        if (cardType == UIKitConfig.CARD_TYPE_GRID_CARD && itemDataType == ItemDataType.LIVE_CHANNEL) {
            return Short.valueOf(UIKitConfig.ITEM_TYPE_CAROUSELCHANNEL);
        }
        if (cardType == UIKitConfig.CARD_TYPE_VIP) {
            return Short.valueOf(UIKitConfig.ITEM_TYPE_VIP);
        }
        switch (itemDataType) {
            case RECORD:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_RECORD);
            case DAILY:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_DAILYNEWS);
            case APP:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_APP);
            case SETTING:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_SETTING);
            case CHANNEL:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_CHANNELLIST);
            case CAROUSEL:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_CAROUSELPLAYER);
            case TV_TAG_ALL:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_ALLENTRY);
            default:
                return Short.valueOf(UIKitConfig.ITEM_TYPE_STANDARD);
        }
    }
}
