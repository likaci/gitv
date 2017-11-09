package com.gala.video.lib.share.uikit.data.data.processor.Item;

import android.util.Log;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.CardStyle;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.utils.Precondition;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemFilter {
    private static final String TAG = "ItemFilter";
    private static HashMap<Short, ItemDataType[]> filter = new HashMap();

    static {
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_FLOW), new ItemDataType[]{ItemDataType.ALBUM, ItemDataType.VIDEO, ItemDataType.LIVE_CHANNEL, ItemDataType.LIVE, ItemDataType.PERSON, ItemDataType.PLAY_LIST, ItemDataType.RESOURCE_GROUP, ItemDataType.TV_TAG, ItemDataType.TV_TAG_ALL, ItemDataType.H5, ItemDataType.PLST_GROUP, ItemDataType.RECOMMEND_APP});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_COVER_FLOW), new ItemDataType[]{ItemDataType.ALBUM, ItemDataType.VIDEO, ItemDataType.LIVE_CHANNEL, ItemDataType.LIVE, ItemDataType.PERSON, ItemDataType.PLAY_LIST, ItemDataType.RESOURCE_GROUP, ItemDataType.TV_TAG, ItemDataType.H5, ItemDataType.PLST_GROUP, ItemDataType.RECOMMEND_APP});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_TIME_LINE), new ItemDataType[]{ItemDataType.ALBUM, ItemDataType.VIDEO, ItemDataType.LIVE_CHANNEL, ItemDataType.LIVE, ItemDataType.PERSON, ItemDataType.PLAY_LIST, ItemDataType.RESOURCE_GROUP, ItemDataType.TV_TAG, ItemDataType.H5, ItemDataType.PLST_GROUP, ItemDataType.RECOMMEND_APP});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_TOBE_ONLINE), new ItemDataType[]{ItemDataType.ALBUM, ItemDataType.VIDEO});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_ONE), new ItemDataType[]{ItemDataType.VIDEO});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_APP_AND_SETTING), new ItemDataType[]{ItemDataType.SUBSCRIBE, ItemDataType.COLLECTION});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_CAROUSEL), new ItemDataType[]{ItemDataType.ALBUM, ItemDataType.VIDEO, ItemDataType.LIVE_CHANNEL, ItemDataType.LIVE, ItemDataType.PERSON, ItemDataType.PLAY_LIST, ItemDataType.RESOURCE_GROUP, ItemDataType.TV_TAG, ItemDataType.H5, ItemDataType.PLST_GROUP, ItemDataType.CAROUSEL, ItemDataType.RECOMMEND_APP});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_HISTORY_AND_DAILY), new ItemDataType[]{ItemDataType.ALBUM, ItemDataType.VIDEO, ItemDataType.LIVE_CHANNEL, ItemDataType.LIVE, ItemDataType.PERSON, ItemDataType.PLAY_LIST, ItemDataType.RESOURCE_GROUP, ItemDataType.TV_TAG, ItemDataType.H5, ItemDataType.RECORD, ItemDataType.PLST_GROUP, ItemDataType.DAILY, ItemDataType.RECOMMEND_APP});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_GRID_CARD), new ItemDataType[]{ItemDataType.LIVE_CHANNEL});
        filter.put(Short.valueOf(UIKitConfig.CARD_TYPE_VIP), new ItemDataType[]{ItemDataType.VIP_BUY, ItemDataType.VIP_VIDEO, ItemDataType.JUMP_TO_H5, ItemDataType.MSGCENTER});
    }

    public static boolean isSupport(short cardType, ItemDataType itemDataType) {
        ItemDataType[] types = (ItemDataType[]) filter.get(Short.valueOf(cardType));
        if (types != null && types.length > 0) {
            for (ItemDataType itemDataType2 : types) {
                if (itemDataType == itemDataType2) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<ChannelLabel> filtrateItems(List<ChannelLabel> labels, CardStyle cardStyle, int backSize, boolean shouldBack) {
        List<ChannelLabel> items = new ArrayList();
        int i = 0;
        while (i < labels.size()) {
            ChannelLabel label = (ChannelLabel) labels.get(i);
            ItemDataType type = DataBuildTool.getItemType(label);
            boolean isSupport = true;
            if (!isSupport(cardStyle.type, type)) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 基础匹配");
                isSupport = false;
            } else if (!isSupposeCard(label, cardStyle, type)) {
                isSupport = false;
            } else if (!isSupportItem(label, type, i, items, false)) {
                isSupport = false;
            }
            if (isSupport) {
                if (type == ItemDataType.TV_TAG_ALL) {
                    backSize++;
                }
                items.add(label);
            }
            if (!shouldBack || isSupport || i >= backSize) {
                i++;
            } else {
                Log.d(TAG, "item's id = " + label.itemId + ", index = " + i + ", backSize = " + backSize);
                return null;
            }
        }
        return items;
    }

    private static boolean isSupposeCard(ChannelLabel label, CardStyle cardStyle, ItemDataType type) {
        if (cardStyle.type == UIKitConfig.CARD_TYPE_TOBE_ONLINE) {
            if (label.itemKvs == null || Precondition.isEmpty(label.itemKvs.showTime)) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 即将上线card过滤没有showTime的数据");
                return false;
            }
        } else if (cardStyle.type == UIKitConfig.CARD_TYPE_TIME_LINE) {
            if (!((label.itemKvs != null && !Precondition.isEmpty(label.itemKvs.showTime)) || type == ItemDataType.ALBUM || type == ItemDataType.VIDEO || type == ItemDataType.LIVE)) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 时间轴card过滤没有showTime的非ALBUM、VIDEO、LIVE数据");
                return false;
            }
        } else if (cardStyle.type == UIKitConfig.CARD_TYPE_COVER_FLOW) {
            if (label.itemKvs == null || Precondition.isEmpty(label.itemKvs.defImg_size) || (Precondition.isEmpty(label.itemKvs.extraImage) && Precondition.isEmpty(label.itemKvs.imageGif) && Precondition.isEmpty(label.itemImageUrl))) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is CoverFlow过滤没有配置图片");
                return false;
            }
        } else if (cardStyle.type == UIKitConfig.CARD_TYPE_VIP && label.itemKvs != null && label.itemKvs.isDisableInNoLogin.equals("2") && !GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 未登录账号不显示");
            return false;
        }
        return true;
    }

    private static boolean isSupportItem(ChannelLabel label, ItemDataType type, int i, List<ChannelLabel> items, boolean hasCarousel) {
        if (type == ItemDataType.TV_TAG_ALL) {
            if (i != 0) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 全部入口只能配置在第一个");
                return false;
            }
        } else if (type == ItemDataType.TV_TAG) {
            if (label.itemKvs.getTVTag().channelId == 10002 && GetInterfaceTools.getPlayerConfigProvider().isDisable4KH264()) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 4k过滤");
                return false;
            }
        } else if (type == ItemDataType.ALBUM || type == ItemDataType.VIDEO) {
            if (!DataBuildTool.checkRegionAvailable(label)) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 地域播控");
                return false;
            }
        } else if (type == ItemDataType.LIVE) {
            if (!label.checkLive()) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 直播相关过滤逻辑this.channels != null && this.channels.size() > 0 && this.getLiveFlowerList() != null && this.getLiveFlowerList().size() > 0 && this.getLivePlayingType() != LivePlayingType.END;");
                return false;
            }
        } else if (type == ItemDataType.H5) {
            if (Precondition.isEmpty(label.itemKvs.pageUrl) && Precondition.isEmpty(label.itemPageUrl)) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is H5过滤没有配置跳转url");
                return false;
            }
        } else if (type == ItemDataType.CAROUSEL) {
            if (i != 0) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 轮播没有配置在第一个");
                return false;
            } else if (!Project.getInstance().getBuild().isSupportSmallWindowPlay()) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 不支持小窗口");
                return false;
            } else if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 支持多进程");
                return false;
            } else if (Project.getInstance().getControl().isOpenCarousel()) {
                Log.d(TAG, "item's id = " + label.itemId + ", 轮播计数");
            } else {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 没有打开轮播");
                return false;
            }
        } else if (type == ItemDataType.LIVE_CHANNEL) {
            if (!Project.getInstance().getControl().isOpenCarousel() || label.boss != 0) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 没有打开轮播or Label.boss!=0");
                return false;
            } else if (hasCarousel && items.size() == 1) {
                ((ChannelLabel) items.get(0)).itemId = label.itemId;
                ((ChannelLabel) items.get(0)).tableNo = label.tableNo;
                ((ChannelLabel) items.get(0)).name = label.name;
                ((ChannelLabel) items.get(0)).channels = label.channels;
            }
        } else if (type == ItemDataType.RECOMMEND_APP) {
            if (!Project.getInstance().getBuild().isSupportRecommendApp()) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 打包过滤");
                return false;
            } else if (!label.itemKvs.appkey.equalsIgnoreCase("chinapokerapp") && !label.itemKvs.appkey.equalsIgnoreCase("childapp")) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 推荐app只能指定的两个");
                return false;
            } else if (label.itemKvs.appkey.equalsIgnoreCase("childapp")) {
                url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getChildAppUrl();
                if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) {
                    Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 配置过滤, getChildAppUrl = " + url);
                    return false;
                }
            } else if (label.itemKvs.appkey.equalsIgnoreCase("chinapokerapp")) {
                url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getChinaPokerAppUrl();
                if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) {
                    Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 配置过滤, getChinaPokerAppUrl = " + url);
                    return false;
                }
            }
        } else if (type == ItemDataType.PLST_GROUP) {
            if (Precondition.isEmpty(label.itemKvs.dataid)) {
                Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 专题回顾没有配置id");
                return false;
            }
        } else if (type == ItemDataType.MSGCENTER && !Project.getInstance().getBuild().isOpenMessageCenter()) {
            Log.d(TAG, "item's id = " + label.itemId + ", the reason for the filter is 打包关闭消息中心");
            return false;
        }
        return true;
    }
}
