package com.gala.video.lib.share.ifimpl.openplay.service;

import com.gala.video.lib.framework.core.utils.StringUtils;
import com.qiyi.tv.client.data.UserTags;
import java.util.ArrayList;

public class LocalUserTags {
    private static final String EXTRA_ALBUM_PIC_URL = "com.qiyi.tv.sdk.extra.EXTRA_ALBUM_PIC_URL";
    private static final String EXTRA_BK_URL = "com.qiyi.tv.internal.extra.EXTRA_BK_URL";
    private static final String EXTRA_CHANNEL_ID = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_ID";
    private static final String EXTRA_CHANNEL_PLAY_DIRECTLY = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_PLAY_DIRECTLY";
    private static final String EXTRA_CHANNEL_QIPU = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_QIPU";
    private static final String EXTRA_CHANNEL_RESOURCE_ID_FOR_CLASS_TAG = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_RESOURCE_ID_FOR_CLASS_TAG";
    private static final String EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND";
    private static final String EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND_OTHER = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND_OTHER";
    private static final String EXTRA_CHANNEL_RESOURCE_ID_FOR_TAB_RECOMMEND = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_RESOURCE_ID_FOR_TAB_RECOMMEND";
    private static final String EXTRA_CHANNEL_SPEC = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_SPEC";
    private static final String EXTRA_CHANNEL_TYPE = "com.qiyi.tv.internal.extra.EXTRA_CHANNEL_TYPE";
    private static final String EXTRA_COVER_PIC_URL = "com.qiyi.tv.sdk.extra.EXTRA_COVER_PIC_URL";
    private static final String EXTRA_FILTER_TAGS = "com.qiyi.tv.internal.extra.EXTRA_FILTER_TAGS";
    private static final String EXTRA_ITEM_ID = "com.qiyi.tv.internal.extra.EXTRA_ITEM_ID";
    private static final String EXTRA_ITEM_TYPE = "com.qiyi.tv.internal.extra.EXTRA_ITEM_TYPE";
    private static final String EXTRA_LAYOUT = "com.qiyi.tv.internal.extra.EXTRA_LAYOUT";
    public static final String EXTRA_MEDIA_PIC_URL = "com.qiyi.tv.sdk.extra.EXTRA_MEDIA_PIC_URL";
    private static final String EXTRA_PIC = "com.qiyi.tv.internal.extra.EXTRA_PIC";
    public static final String EXTRA_RESOURCE_480_270 = "com.qiyi.tv.sdk.extra.EXTRA_RECOMMEND_COMMON_RECT";
    public static final String EXTRA_RESOURCE_495_495 = "com.qiyi.tv.sdk.extra.EXTRA_RECOMMEND_COMMON_SQUARE";
    public static final String EXTRA_RESOURCE_570_570 = "com.qiyi.tv.sdk.extra.EXTRA_RECOMMEND_EXTRUDE_570_570";
    public static final String EXTRA_RESOURCE_950_470 = "com.qiyi.tv.sdk.extra.EXTRA_RECOMMEND_EXTRUDE_950_470";
    public static final String EXTRA_RESOURCE_DEFAULT_SIZE = "com.qiyi.tv.sdk.extra.EXTRA_RESOURCE_DEFAULT_SIZE";
    private static final String EXTRA_SOURCE_ID = "com.qiyi.tv.internal.extra.EXTRA_SOURCE_ID";
    private static final String EXTRA_SUB_KEY = "com.qiyi.tv.sdk.extar.EXTRA_SUB_KEY";
    private static final String EXTRA_SUB_TYPE = "com.qiyi.tv.sdk.extar.EXTRA_SUB_TYPE";
    private static final String EXTRA_TV_PIC = "com.qiyi.tv.internal.extra.EXTRA_TV_PIC";
    private static final String EXTRA_TYPE = "com.qiyi.tv.internal.extra.EXTRA_TYPE";

    public static void setSourceId(UserTags userTags, String sourceId) {
        userTags.putString(EXTRA_SOURCE_ID, sourceId);
    }

    public static String getSourceId(UserTags userTags) {
        return userTags.getString(EXTRA_SOURCE_ID);
    }

    public static void setBkUrl(UserTags userTags, String bkUrl) {
        userTags.putString(EXTRA_BK_URL, bkUrl);
    }

    public static String getBkUrl(UserTags userTags) {
        return userTags.getString(EXTRA_BK_URL);
    }

    public static void setLayout(UserTags userTags, int layout) {
        userTags.putString(EXTRA_LAYOUT, Integer.toString(layout));
    }

    public static int getLayout(UserTags userTags) {
        return StringUtils.parse(userTags.getString(EXTRA_LAYOUT), -1);
    }

    public static void setChannelResourceIdForClassTags(UserTags userTags, String id) {
        userTags.putString(EXTRA_CHANNEL_RESOURCE_ID_FOR_CLASS_TAG, id);
    }

    public static String getChannelResourceIdForClassTags(UserTags userTags) {
        return userTags.getString(EXTRA_CHANNEL_RESOURCE_ID_FOR_CLASS_TAG);
    }

    public static void setChannelResourceIdForRecommend(UserTags userTags, String id) {
        userTags.putString(EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND, id);
    }

    public static String getChannelResourceIdForRecommend(UserTags userTags) {
        return userTags.getString(EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND);
    }

    public static void setChannelResourceIdForRecommendOther(UserTags userTags, String id) {
        userTags.putString(EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND_OTHER, id);
    }

    public static String getChannelResourceIdForRecommendOther(UserTags userTags) {
        return userTags.getString(EXTRA_CHANNEL_RESOURCE_ID_FOR_RECOMMEND_OTHER);
    }

    public static void setChannelResourceIdForTabRecommend(UserTags userTags, String poolResId) {
        userTags.putString(EXTRA_CHANNEL_RESOURCE_ID_FOR_TAB_RECOMMEND, poolResId);
    }

    public static String getChannelResourceIdForTabRecommend(UserTags userTags) {
        return userTags.getString(EXTRA_CHANNEL_RESOURCE_ID_FOR_TAB_RECOMMEND);
    }

    public static void setChannelPlayDirectly(UserTags userTags, int play) {
        userTags.putString(EXTRA_CHANNEL_PLAY_DIRECTLY, Integer.toString(play));
    }

    public static int getChannelPlayDirectly(UserTags userTags) {
        return StringUtils.parse(userTags.getString(EXTRA_CHANNEL_PLAY_DIRECTLY), -1);
    }

    public static void setChannelFilterTags(UserTags userTags, ArrayList<String> tags) {
        userTags.putStringArrayList(EXTRA_FILTER_TAGS, tags);
    }

    public static ArrayList<String> getChannelFilterTags(UserTags userTags) {
        return userTags.getStringArrayList(EXTRA_FILTER_TAGS);
    }

    public static void setChannelType(UserTags userTags, int channelType) {
        userTags.putString(EXTRA_CHANNEL_TYPE, String.valueOf(channelType));
    }

    public static int getChannelType(UserTags userTags) {
        return StringUtils.parse(userTags.getString(EXTRA_CHANNEL_TYPE), -1);
    }

    public static void setChannelSpec(UserTags userTags, int channelSpec) {
        userTags.putString(EXTRA_CHANNEL_SPEC, String.valueOf(channelSpec));
    }

    public static int getChannelSpec(UserTags userTags) {
        return StringUtils.parse(userTags.getString(EXTRA_CHANNEL_SPEC), 2);
    }

    public static void setChannelQipu(UserTags userTags, String qipu) {
        userTags.putString(EXTRA_CHANNEL_QIPU, String.valueOf(qipu));
    }

    public static String getChannelQipu(UserTags userTags) {
        return userTags.getString(EXTRA_CHANNEL_QIPU);
    }

    public static void setItemId(UserTags userTags, String itemId) {
        userTags.putString(EXTRA_ITEM_ID, itemId);
    }

    public static String getItemId(UserTags userTags) {
        return userTags.getString(EXTRA_ITEM_ID);
    }

    public static void setChannelId(UserTags userTags, int channelId) {
        userTags.putString(EXTRA_CHANNEL_ID, String.valueOf(channelId));
    }

    public static int getChannelId(UserTags userTags) {
        return StringUtils.parse(userTags.getString(EXTRA_CHANNEL_ID), -1);
    }

    public static void setPic(UserTags userTags, String pic) {
        userTags.putString(EXTRA_PIC, pic);
    }

    public static String getPic(UserTags userTags) {
        return userTags.getString(EXTRA_PIC);
    }

    public static void setTvPic(UserTags userTags, String tvPic) {
        userTags.putString(EXTRA_TV_PIC, tvPic);
    }

    public static String getTvPic(UserTags userTags) {
        return userTags.getString(EXTRA_TV_PIC);
    }

    public static void setType(UserTags userTags, int type) {
        userTags.putString(EXTRA_TYPE, String.valueOf(type));
    }

    public static int getType(UserTags userTags) {
        return StringUtils.parse(userTags.getString(EXTRA_TYPE), -1);
    }

    public static void setItemType(UserTags userTags, String itemType) {
        userTags.putString(EXTRA_ITEM_TYPE, itemType);
    }

    public static String getItemType(UserTags userTags) {
        return userTags.getString(EXTRA_ITEM_TYPE);
    }

    public static void setResource495_495(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_RESOURCE_495_495, pictureUrl);
    }

    public static String getResource495_495(UserTags userTags) {
        return userTags.getString(EXTRA_RESOURCE_495_495);
    }

    public static void setResource480_270(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_RESOURCE_480_270, pictureUrl);
    }

    public static String getResource480_270(UserTags userTags) {
        return userTags.getString(EXTRA_RESOURCE_480_270);
    }

    public static void setResource570_570(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_RESOURCE_570_570, pictureUrl);
    }

    public static String getResource570_570(UserTags userTags) {
        return userTags.getString(EXTRA_RESOURCE_570_570);
    }

    public static void setResource950_470(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_RESOURCE_950_470, pictureUrl);
    }

    public static String getResource950_470(UserTags userTags) {
        return userTags.getString(EXTRA_RESOURCE_950_470);
    }

    public static void setResourceDefaultSize(UserTags userTags, String imageUrl) {
        userTags.putString(EXTRA_RESOURCE_DEFAULT_SIZE, imageUrl);
    }

    public static String getResourceDefaultSize(UserTags userTags) {
        return userTags.getString(EXTRA_RESOURCE_DEFAULT_SIZE);
    }

    public static void setMediaPic(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_MEDIA_PIC_URL, pictureUrl);
    }

    public static String getMediaPic(UserTags userTags) {
        return userTags.getString(EXTRA_MEDIA_PIC_URL);
    }

    public static String getCoverPicture(UserTags userTags) {
        return userTags.getString(EXTRA_COVER_PIC_URL);
    }

    public static void setCoverPicture(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_COVER_PIC_URL, pictureUrl);
    }

    public static String getAlbumPicture(UserTags userTags) {
        return userTags.getString(EXTRA_ALBUM_PIC_URL);
    }

    public static void setAlbumPicture(UserTags userTags, String pictureUrl) {
        userTags.putString(EXTRA_ALBUM_PIC_URL, pictureUrl);
    }

    public static String getSubKey(UserTags userTags) {
        return userTags.getString(EXTRA_SUB_KEY);
    }

    public static void setSubKey(UserTags userTags, String subKey) {
        userTags.putString(EXTRA_SUB_KEY, subKey);
    }

    public static String getSubType(UserTags userTags) {
        return userTags.getString(EXTRA_SUB_TYPE);
    }

    public static void setSubType(UserTags userTags, String subType) {
        userTags.putString(EXTRA_SUB_TYPE, subType);
    }
}
