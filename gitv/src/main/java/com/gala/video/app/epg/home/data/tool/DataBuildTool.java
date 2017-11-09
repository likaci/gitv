package com.gala.video.app.epg.home.data.tool;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.model.PltRegionCtrls;
import com.gala.tvapi.vrs.model.RegionCtrls;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.utils.Precondition;
import java.util.Arrays;
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
        if (Precondition.isEmpty(label.shortTitle)) {
            return label.name;
        }
        return label.shortTitle;
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

    public static String resizeImage(String url, String size) {
        if (!Precondition.isEmpty(url)) {
            int i = url.lastIndexOf(".");
            if (i >= 0) {
                StringBuilder builder = new StringBuilder(url);
                builder.insert(i, size);
                return builder.toString();
            }
        }
        return "";
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
}
