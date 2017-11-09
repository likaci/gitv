package com.gala.video.app.epg.home.data.pingback;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.ThreeLevelTag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.vrs.model.TVTag;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.List;

public class HomePingbackDataUtils {
    public static String getQipuId(ItemData itemData) {
        if (itemData == null || itemData.mLabel == null || itemData.mLabel.getVideo() == null) {
            return "";
        }
        return itemData.mLabel.getVideo().qpId;
    }

    public static String getQipuIdForLive(ItemData itemData) {
        if (itemData == null || itemData.mLabel == null) {
            return "";
        }
        return itemData.mLabel.itemId;
    }

    public static String getChannelId(ItemData itemData) {
        if (itemData == null || itemData.mLabel == null || itemData.mLabel.getVideo() == null) {
            return "";
        }
        return String.valueOf(itemData.mLabel.getVideo().chnId);
    }

    public static String getSecondLevelChnIdForLive(ItemData itemData) {
        if (itemData == null || itemData.mLabel == null || itemData.mLabel.getLiveAlbumList().get(0) == null) {
            return "";
        }
        return ((Album) itemData.mLabel.getLiveAlbumList().get(0)).live_channelId;
    }

    public static String getSecondLevelChnIdForCarousel(ItemData itemData) {
        if (itemData == null || itemData.mLabel == null || itemData.mLabel.getChannelCarousel() == null) {
            return "";
        }
        return String.valueOf(itemData.mLabel.getChannelCarousel().id);
    }

    public static String getPlayListQipuId(ItemData itemData) {
        if (itemData == null || itemData.mLabel == null) {
            return "";
        }
        return itemData.mLabel.itemId;
    }

    public static String getListPageTagName(TVTags tvTags, List<TwoLevelTag> twoLevelTagList, String separator) {
        if (tvTags == null) {
            return "";
        }
        List<TVTag> tags = tvTags.tags;
        CharSequence listPageOrTagName = "";
        boolean hasSortTag = false;
        if (!ListUtils.isEmpty((List) twoLevelTagList)) {
            for (TwoLevelTag twoLevelTag : twoLevelTagList) {
                if (twoLevelTag != null) {
                    for (ThreeLevelTag threeLevelTag : twoLevelTag.tags) {
                        if (threeLevelTag != null) {
                            String threeLevelTagName = threeLevelTag.f1015n;
                            String threeLevelTagValue = threeLevelTag.f1016v;
                            for (TVTag tvTag : tags) {
                                String tagValue = tvTag.value;
                                if (!(tagValue == null || !tagValue.equals(threeLevelTagValue) || threeLevelTagName == null || "全部".equals(threeLevelTagName))) {
                                    if ("最近热播".equals(threeLevelTagName) || "最近更新".equals(threeLevelTagName)) {
                                        hasSortTag = true;
                                    }
                                    String listPageOrTagName2 = listPageOrTagName2 + threeLevelTagName + separator;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(listPageOrTagName)) {
            listPageOrTagName = listPageOrTagName.substring(0, listPageOrTagName.length() - 1);
        }
        if (!(StringUtils.isEmpty(listPageOrTagName) || hasSortTag)) {
            listPageOrTagName = listPageOrTagName + separator + "最近热播";
        }
        if (StringUtils.isEmpty(listPageOrTagName)) {
            return AlbumInfoFactory.getChannelNameByChannelId(tvTags.channelId);
        }
        return AlbumInfoFactory.getChannelNameByChannelId(tvTags.channelId) + separator + listPageOrTagName;
    }
}
