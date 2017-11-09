package com.gala.video.lib.share.uikit.data.data.processor.Item;

import android.text.TextUtils;
import android.util.Log;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import java.util.HashMap;

public class CornerBuildTool {
    private static final String CORNER_3D = "share_corner_3d";
    public static final String CORNER_AD = "share_corner_focus_image_ad";
    private static final String CORNER_DIANBO = "share_corner_fufeidianbo";
    private static final String CORNER_DOLBY = "share_corner_dolby";
    private static final String CORNER_DUBO = "share_corner_dubo";
    private static final String CORNER_END_LIVING = "share_corner_end_living";
    private static final String CORNER_LEFT_BOTTOM_BG = "share_corner_bg_left";
    private static final String CORNER_LIVING = "share_corner_living";
    private static final String CORNER_NOTICE = "share_corner_notice";
    private static final String CORNER_PLAYING_GIF_ANIMATION = "share_detail_gif_playing";
    private static final String CORNER_PLAYING_GIF_STATIC = "share_detail_gif_playing_6";
    private static final String CORNER_RANK = "share_corner_rank_";
    private static final String CORNER_VIP = "share_corner_vip";
    private static final String CORNER_YONGQUAN = "share_corner_yongquan";
    private static final String CORNER_ZHUANTI = "share_corner_zhuanti";
    private static boolean mDisableGifAnim = Project.getInstance().getControl().disableGifAnimForDetailPage();

    public enum CornerType {
        VIP,
        DUBO,
        LIVE,
        PLAYLIST,
        STREAM,
        SCORE,
        MM_DD,
        XXS_SET,
        X_SET
    }

    public enum ItemCornerType {
        ImportantVideo,
        OtherVideo,
        SingleStage,
        SingleSet,
        Album,
        Source,
        Live,
        PlayList,
        Group,
        Default
    }

    public static void buildCorner(ChannelLabel label, short layoutId, HashMap<String, HashMap<String, String>> cuteViewDatas, short cardType, int index, boolean isSort) {
        if (layoutId != UIKitConfig.CIRCLE_NOTITLE_LAYOUT && layoutId != UIKitConfig.CIRCLE_TITLE_LAYOUT && layoutId != UIKitConfig.OBLIQUE_LAYOUT) {
            ItemCornerType itemTypeCorner = getItenTypeCorner(label);
            getLTCorner(label, itemTypeCorner, cuteViewDatas);
            getRTCorner(label, itemTypeCorner, cuteViewDatas);
            if (isShowRank(isSort, index, cardType)) {
                getRankCorner(cuteViewDatas, label, index);
            } else if (cardType != UIKitConfig.CARD_TYPE_TOBE_ONLINE) {
                getLBCorner(label, itemTypeCorner, cuteViewDatas);
                getRBCorner(label, itemTypeCorner, cuteViewDatas);
            }
        }
    }

    public static void buildAdCorner(HashMap<String, HashMap<String, String>> cuteViewDatas) {
        HashMap<String, String> data = new HashMap();
        data.put("value", CORNER_AD);
        cuteViewDatas.put("ID_CORNER_L_T", data);
    }

    private static void getRankCorner(HashMap<String, HashMap<String, String>> cuteViewDatas, ChannelLabel label, int index) {
        if (DataBuildTool.getItemType(label) != ItemDataType.TV_TAG_ALL) {
            HashMap<String, String> data = new HashMap();
            data.put("value", CORNER_RANK + (index + 1));
            cuteViewDatas.put(UIKitConfig.ID_CORNER_RANK, data);
        }
    }

    private static boolean isShowRank(boolean isSort, int index, short cardType) {
        if (cardType == UIKitConfig.CARD_TYPE_FLOW && isSort && index < 10) {
            return true;
        }
        return false;
    }

    private static void getLTCorner(ChannelLabel label, ItemCornerType itemTypeCorner, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        CornerType type = null;
        switch (itemTypeCorner) {
            case ImportantVideo:
            case OtherVideo:
            case SingleStage:
            case SingleSet:
            case Album:
            case Source:
            case Live:
                type = CornerType.VIP;
                break;
        }
        HashMap<String, String> data = buildCornerMap(label, type);
        if (data != null) {
            cuteViewDatas.put("ID_CORNER_L_T", data);
        }
    }

    private static void getRTCorner(ChannelLabel label, ItemCornerType itemTypeCorner, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        CornerType type = null;
        switch (itemTypeCorner) {
            case ImportantVideo:
            case OtherVideo:
            case SingleStage:
            case SingleSet:
            case Album:
            case Source:
                type = CornerType.DUBO;
                break;
            case Live:
                type = CornerType.LIVE;
                break;
            case PlayList:
            case Group:
                type = CornerType.PLAYLIST;
                break;
        }
        HashMap<String, String> data = buildCornerMap(label, type);
        if (data != null) {
            cuteViewDatas.put("ID_CORNER_R_T", data);
        }
    }

    private static void getRBCorner(ChannelLabel label, ItemCornerType itemTypeCorner, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        CornerType type = null;
        switch (itemTypeCorner) {
            case ImportantVideo:
                type = CornerType.SCORE;
                break;
            case SingleStage:
            case Source:
                type = CornerType.MM_DD;
                break;
            case SingleSet:
                type = CornerType.X_SET;
                break;
            case Album:
                type = CornerType.XXS_SET;
                break;
        }
        HashMap<String, String> data = buildCornerMap(label, type);
        if (data != null) {
            switch (type) {
                case SCORE:
                    cuteViewDatas.put("ID_SCORE", data);
                    return;
                case MM_DD:
                case XXS_SET:
                case X_SET:
                    cuteViewDatas.put(UIKitConfig.ID_DESC_R_B, data);
                    return;
                default:
                    return;
            }
        }
    }

    private static void getLBCorner(ChannelLabel label, ItemCornerType itemTypeCorner, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        if (itemTypeCorner == ItemCornerType.ImportantVideo) {
            boolean supportDolby = AppClientUtils.isSupportDolby() && label.isDubi == 1;
            boolean support3d;
            if (label.isD3 == 1) {
                support3d = true;
            } else {
                support3d = false;
            }
            HashMap<String, String> map1;
            HashMap<String, String> map3;
            if (supportDolby && support3d) {
                map1 = new HashMap();
                map1.put("value", CORNER_DOLBY);
                cuteViewDatas.put("ID_CORNER_L_B_1", map1);
                HashMap<String, String> map2 = new HashMap();
                map2.put("value", CORNER_3D);
                cuteViewDatas.put("ID_CORNER_L_B_2", map2);
                map3 = new HashMap();
                map3.put("value", CORNER_LEFT_BOTTOM_BG);
                map3.put("w", "149");
                cuteViewDatas.put("ID_CORNER_BG_LEFT", map3);
            } else if (supportDolby && !support3d) {
                map1 = new HashMap();
                map1.put("value", CORNER_DOLBY);
                cuteViewDatas.put("ID_CORNER_L_B_1", map1);
                map3 = new HashMap();
                map3.put("value", CORNER_LEFT_BOTTOM_BG);
                map3.put("w", "101");
                cuteViewDatas.put("ID_CORNER_BG_LEFT", map3);
            } else if (!supportDolby && support3d) {
                map1 = new HashMap();
                map1.put("value", CORNER_3D);
                cuteViewDatas.put("ID_CORNER_L_B_1", map1);
                map3 = new HashMap();
                map3.put("value", CORNER_LEFT_BOTTOM_BG);
                map3.put("w", "76");
                cuteViewDatas.put("ID_CORNER_BG_LEFT", map3);
            }
        }
    }

    private static HashMap<String, String> buildCornerMap(ChannelLabel label, CornerType type) {
        if (type == null) {
            return null;
        }
        String corner = null;
        switch (type) {
            case VIP:
                corner = getVipCorner(label);
                break;
            case DUBO:
                corner = getDuBoCorner(label);
                break;
            case LIVE:
                return getLiveCorner(label);
            case PLAYLIST:
                corner = getPlayListCorner(label);
                break;
        }
        if (corner != null) {
            HashMap<String, String> map = new HashMap();
            map.put("value", corner);
            return map;
        }
        switch (type) {
            case SCORE:
                corner = getScoreCorner(label);
                break;
            case MM_DD:
                corner = getMMDDCorner(label);
                break;
            case XXS_SET:
                corner = getXXSSetCorner(label);
                break;
            case X_SET:
                corner = getXSetCorner(label);
                break;
        }
        if (corner == null) {
            return null;
        }
        map = new HashMap();
        map.put("text", corner);
        return map;
    }

    private static HashMap<String, String> getLiveCorner(ChannelLabel label) {
        if (!hasLiveCorner(label)) {
            return null;
        }
        HashMap<String, String> map = new HashMap();
        map.put(UIKitConfig.KEY_LIVE_PLAYING_TYPE, label.getLivePlayingType().name());
        map.put(UIKitConfig.KEY_LIVE_RES_BEFORE, CORNER_NOTICE);
        map.put(UIKitConfig.KEY_LIVE_RES_ING, CORNER_LIVING);
        map.put(UIKitConfig.KEY_LIVE_RES_END, CORNER_END_LIVING);
        map.put(UIKitConfig.KEY_LIVE_START_TIME, formatTime(label.itemKvs.LiveEpisode_StartTime));
        map.put(UIKitConfig.KEY_LIVE_END_TIME, formatTime(label.itemKvs.LiveEpisode_EndTime));
        return map;
    }

    public static void buildGifPlayingCorner(ItemInfoModel itemInfoModel, boolean showGif) {
        if (itemInfoModel.getCuteViewDatas() == null) {
            itemInfoModel.setCuteViewDatas(new HashMap());
        }
        HashMap<String, String> bgMap;
        String lastWidth;
        HashMap<String, String> lb1HashMap;
        HashMap<String, String> lb2HashMap;
        if (showGif) {
            bgMap = (HashMap) itemInfoModel.getCuteViewDatas().get("ID_CORNER_BG_LEFT");
            if (bgMap == null) {
                bgMap = new HashMap();
            } else {
                lastWidth = itemInfoModel.getCuteViewData("ID_CORNER_BG_LEFT", "w");
                if (lastWidth != null) {
                    bgMap.put(UIKitConfig.KEY_LAST_WIDTH, lastWidth);
                }
            }
            bgMap.put("value", CORNER_LEFT_BOTTOM_BG);
            bgMap.put("w", "76");
            itemInfoModel.getCuteViewDatas().put("ID_CORNER_BG_LEFT", bgMap);
            lb1HashMap = (HashMap) itemInfoModel.getCuteViewDatas().get("ID_CORNER_L_B_1");
            if (lb1HashMap != null) {
                lb1HashMap.put("visible", "0");
            }
            lb2HashMap = (HashMap) itemInfoModel.getCuteViewDatas().get("ID_CORNER_L_B_2");
            if (lb2HashMap != null) {
                lb2HashMap.put("visible", "0");
            }
            HashMap<String, String> gifMap = new HashMap();
            if (mDisableGifAnim) {
                gifMap.put("value", CORNER_PLAYING_GIF_STATIC);
            } else {
                gifMap.put("value", CORNER_PLAYING_GIF_ANIMATION);
            }
            itemInfoModel.getCuteViewDatas().put(UIKitConfig.ID_PLAYING_GIF, gifMap);
            return;
        }
        bgMap = (HashMap) itemInfoModel.getCuteViewDatas().get("ID_CORNER_BG_LEFT");
        if (bgMap != null) {
            lastWidth = itemInfoModel.getCuteViewData("ID_CORNER_BG_LEFT", UIKitConfig.KEY_LAST_WIDTH);
            if (lastWidth != null) {
                bgMap.put("w", lastWidth);
            } else {
                itemInfoModel.getCuteViewDatas().put("ID_CORNER_BG_LEFT", null);
            }
        }
        lb1HashMap = (HashMap) itemInfoModel.getCuteViewDatas().get("ID_CORNER_L_B_1");
        if (lb1HashMap != null) {
            lb1HashMap.put("visible", "1");
        }
        lb2HashMap = (HashMap) itemInfoModel.getCuteViewDatas().get("ID_CORNER_L_B_2");
        if (lb2HashMap != null) {
            lb2HashMap.put("visible", "1");
        }
        itemInfoModel.getCuteViewDatas().put(UIKitConfig.ID_PLAYING_GIF, null);
    }

    private static String formatTime(String time) {
        if (StringUtils.isEmpty((CharSequence) time)) {
            return null;
        }
        try {
            return String.valueOf(DateLocalThread.parseYH(time).getTime());
        } catch (Exception e) {
            Log.e("LiveCorner", "formatTime error, time=" + time);
            return null;
        }
    }

    private static String getVipCorner(ChannelLabel label) {
        switch (label.payMark) {
            case 1:
                return CORNER_VIP;
            case 2:
                return CORNER_DIANBO;
            case 3:
                return CORNER_YONGQUAN;
            default:
                return null;
        }
    }

    private static String getDuBoCorner(ChannelLabel label) {
        if (label.exclusive == 1) {
            return CORNER_DUBO;
        }
        return null;
    }

    private static String getPlayListCorner(ChannelLabel label) {
        return CORNER_ZHUANTI;
    }

    private static boolean hasLiveCorner(ChannelLabel label) {
        LivePlayingType liveType = label.getLivePlayingType();
        return liveType == LivePlayingType.BEFORE || liveType == LivePlayingType.PLAYING || liveType == LivePlayingType.END;
    }

    private static String getScoreCorner(ChannelLabel label) {
        return label.score;
    }

    private static String getXXSSetCorner(ChannelLabel label) {
        if (label.tvCount != label.latestOrder && label.latestOrder != 0) {
            return String.format("更新至 %s 集", new Object[]{Integer.valueOf(label.latestOrder)});
        } else if (label.tvCount != label.latestOrder || label.tvCount == 0) {
            return null;
        } else {
            return String.format("%s 集全", new Object[]{Integer.valueOf(label.tvCount)});
        }
    }

    private static String getXSetCorner(ChannelLabel label) {
        if (label.order <= 0) {
            return null;
        }
        return String.format("第 %d 集", new Object[]{Integer.valueOf(label.order)});
    }

    private static String getMMDDCorner(ChannelLabel label) {
        String date = label.currentPeriod;
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        date = date.trim();
        if (TextUtils.isEmpty(date) || date.length() <= 4) {
            return "";
        }
        StringBuilder format = new StringBuilder(5);
        char[] dateArr = date.substring(4, date.length()).toCharArray();
        String mark = "-";
        int length = dateArr.length;
        for (int i = 0; i < length; i++) {
            format.append(dateArr[i]);
            if (i == 1) {
                format.append("-");
            }
        }
        if (format.length() < 5) {
            return "";
        }
        return format.toString() + "期";
    }

    public static ItemCornerType getItenTypeCorner(ChannelLabel label) {
        switch (label.getType()) {
            case VIDEO:
                if (label.isSeries == 1) {
                    if (label.sourceId.equals("") || label.sourceId.equals("0")) {
                        return ItemCornerType.SingleSet;
                    }
                    return ItemCornerType.SingleStage;
                } else if (label.channelId == 1 || label.channelId == 2 || label.channelId == 4 || label.channelId == 15) {
                    return ItemCornerType.ImportantVideo;
                } else {
                    return ItemCornerType.OtherVideo;
                }
            case ALBUM:
                if (label.isSeries == 1) {
                    if (label.sourceId.equals("") || label.sourceId.equals("0")) {
                        return ItemCornerType.Album;
                    }
                    return ItemCornerType.Source;
                }
                break;
            case COLLECTION:
                return ItemCornerType.PlayList;
            case LIVE:
                return ItemCornerType.Live;
            case RESOURCE_GROUP:
                return ItemCornerType.Group;
        }
        return ItemCornerType.Default;
    }
}
