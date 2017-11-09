package com.gala.video.lib.share.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.Platform;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.util.ArrayList;

public class DataUtils {
    private DataUtils() {
    }

    public static String albumInfoToString(Album album) {
        if (album == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Album@").append(Integer.toHexString(album.hashCode())).append("{");
        builder.append(", getInitIssueTime = ").append(album.getInitIssueTime());
        builder.append(", islive = ").append(album.isLive);
        builder.append(", isFlower = ").append(album.isFlower);
        builder.append(", getContentType = ").append(album.getContentType());
        builder.append(", play time = ").append(album.playTime);
        builder.append(", total time = ").append(album.len);
        builder.append(", focus =").append(album.focus);
        builder.append(", qpId=").append(album.qpId);
        builder.append(", tvQid=").append(album.tvQid);
        builder.append(", vid=").append(album.vid);
        builder.append(", getType()=").append(album.getType());
        builder.append(", albumName=").append(album.name);
        builder.append(", tvName=").append(album.tvName);
        builder.append(", sourceCode=").append(album.sourceCode);
        builder.append(", score=").append(album.score);
        builder.append(", isPurchase()=").append(album.isPurchase());
        builder.append(", isAlbumVip()/isVipForAccount()=").append(album.isVipForAccount());
        builder.append(", isCounpon=").append(album.isCoupon());
        builder.append(", isExclusive()=").append(album.isExclusivePlay());
        builder.append(", isVipForAccount()=").append(album.isVipForAccount());
        builder.append(", isSinglePlay()=").append(album.isSinglePay());
        builder.append(", isSeries()=").append(album.isSeries());
        builder.append(", playOrder=").append(album.order);
        builder.append(", pic=").append(album.pic);
        builder.append(", tvPic=").append(album.tvPic);
        builder.append(", issueTime=").append(album.time);
        builder.append(", playCount=").append(album.pCount);
        builder.append(", playLength=").append(album.len);
        builder.append(", tvCount=").append(album.tvCount);
        builder.append(", tag=").append(album.tag);
        builder.append(", is3D()=").append(album.is3D());
        builder.append(", isDolby()=").append(album.isDolby());
        builder.append(", definition list=").append(album.stream);
        builder.append(", chnId=").append(album.chnId);
        builder.append(", chnName=").append(album.chnName);
        builder.append(", startTime=").append(album.startTime);
        builder.append(", endTime=").append(album.endTime);
        builder.append(", playTime=").append(album.playTime);
        builder.append(", canDownload=").append(album.canDownload());
        builder.append(", isDownload=").append(album.isDownload);
        builder.append(", bkt=").append(album.bkt);
        builder.append(", area=").append(album.area);
        builder.append(", strategy=").append(album.strategy);
        builder.append(",isFlower=").append(album.isFlower);
        builder.append(", subType=").append(album.subType);
        builder.append(", subKey=").append(album.subKey);
        builder.append(", desc=").append(album.desc);
        builder.append(", getAlbumSubName=").append(album.getAlbumSubName());
        builder.append(", cast=").append(album.cast);
        if (album.cast != null) {
            builder.append(", host=").append(album.cast.host);
            builder.append(", actor=").append(album.cast.actor);
            builder.append(", toString=").append(album.cast.toString());
        }
        if (album.vipInfo != null) {
            builder.append(", vipInfo=epIsCoupon").append(album.vipInfo.epIsCoupon);
            builder.append(", vipInfo=epIsPkg").append(album.vipInfo.epIsPkg);
            builder.append(", vipInfo=epIsTvod").append(album.vipInfo.epIsTvod);
            builder.append(", vipInfo=epIsVip").append(album.vipInfo.epIsVip);
            builder.append(", vipInfo=isVip").append(album.vipInfo.isVip);
            builder.append(", vipInfo=isCoupon").append(album.vipInfo.isCoupon);
            builder.append(", vipInfo=isPkg").append(album.vipInfo.isPkg);
            builder.append(", vipInfo=isTvod").append(album.vipInfo.isTvod);
            builder.append(", toString").append(album.vipInfo.toString());
        }
        builder.append("}");
        return builder.toString();
    }

    public static String channelLabelToString(ChannelLabel label) {
        if (label == null) {
            return "ChannelLabel@NULL";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("ChannelLabel@").append(Integer.toHexString(label.hashCode())).append("{");
        builder.append(", id=").append(label.id);
        builder.append(", getType=").append(label.getType());
        builder.append(", name=").append(label.name);
        builder.append(", channelId=").append(label.channelId);
        builder.append(", channelName=").append(label.channelName);
        builder.append(", itemId=").append(label.itemId);
        builder.append(", itemName=").append(label.itemName);
        builder.append(", albumQipuId=").append(label.albumQipuId);
        builder.append(", tvQipuId=").append(label.tvQipuId);
        builder.append(", sourceId=").append(label.sourceId);
        builder.append("}");
        return builder.toString();
    }

    public static String channelToString(Channel channel) {
        if (channel == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("Channel@").append(Integer.toHexString(channel.hashCode())).append("{");
        builder.append(", id=").append(channel.id);
        builder.append(", name=").append(channel.name);
        builder.append(", layout=").append(channel.layout);
        builder.append(", spec=").append(channel.spec);
        builder.append(", type=").append(channel.type);
        builder.append(", qipu=").append(channel.qipuId);
        builder.append("}");
        return builder.toString();
    }

    public static JSONObject parseToJsonObject(String str) {
        if (StringUtils.isEmpty((CharSequence) str)) {
            return null;
        }
        try {
            return JSON.parseObject(str);
        } catch (Exception e) {
            LogUtils.e("DataUtils", "parseToJsonObject failure:" + e + ", json:" + str);
            return null;
        }
    }

    public static Album parseToAlbum(String json) {
        if (StringUtils.isEmpty((CharSequence) json)) {
            return null;
        }
        try {
            return (Album) JSON.parseObject(json, Album.class);
        } catch (Exception e) {
            LogUtils.e("DataUtils", "parseToAlbum failure:" + e + ", json:" + json);
            return null;
        }
    }

    public static ArrayList<Album> parseToAlbumList(String json) {
        if (StringUtils.isEmpty((CharSequence) json)) {
            return null;
        }
        try {
            return (ArrayList) JSON.parseArray(json, Album.class);
        } catch (Exception e) {
            LogUtils.e("DataUtils", "parseToAlbumList failure:" + e + ", json:" + json);
            return null;
        }
    }

    public static <T> T parseToObject(String json, Class<T> clazz) {
        if (StringUtils.isEmpty((CharSequence) json)) {
            return null;
        }
        try {
            return JSON.parseObject(json, (Class) clazz);
        } catch (Exception e) {
            LogUtils.e("DataUtils", "parseToObject failure:" + e + ", json:" + json);
            return null;
        }
    }

    public static <T> ArrayList<T> parseToList(String json, Class<T> clazz) {
        if (StringUtils.isEmpty((CharSequence) json)) {
            return null;
        }
        try {
            return (ArrayList) JSON.parseArray(json, (Class) clazz);
        } catch (Exception e) {
            LogUtils.e("DataUtils", "parseToList failure:" + e + ", json:" + json);
            return null;
        }
    }

    public static final String createReverForNetDoctor(BitStream bitStream) {
        int drType = bitStream.getDynamicRangeType();
        StringBuilder builder = new StringBuilder();
        builder.append("t=").append(DeviceUtils.getServerTimeMillis());
        builder.append("&src=").append(Platform.TV.getPlatformCode());
        builder.append("&agenttype=").append(Platform.TV.getAgentType());
        builder.append("&ptid=").append(Platform.TV.getPlatformCode());
        builder.append("&k_ft1=").append(8422145);
        JSONObject reverObject = new JSONObject();
        reverObject.put("uargs", builder.toString());
        reverObject.put("stype", Integer.valueOf(drType));
        return reverObject.toJSONString();
    }
}
