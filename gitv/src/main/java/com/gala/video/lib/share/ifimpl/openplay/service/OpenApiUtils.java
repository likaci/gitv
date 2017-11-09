package com.gala.video.lib.share.ifimpl.openplay.service;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.ChannelPool;
import com.gala.tvapi.tv2.model.ThreeLevelTag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.CornerMark;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.type.UserType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalAlbum;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalChannel;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalPlaylist;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalVideo;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IAddInstanceHolder;
import com.gala.video.lib.share.utils.DataUtils;
import com.qiyi.tv.client.Utils;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.UserTags;
import java.text.DecimalFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

public class OpenApiUtils {
    private static final Format CLASS_TAG_FORMATTER = new DecimalFormat("000000000000");
    private static final long CLASS_TAG_OFFSET = 1000000000000L;
    private static final String CLASS_TAG_PREFIX = "CT1";
    private static final String TAG = "OpenApiUtils";
    private static final int TV_ALBUM = 1;
    private static final int TV_VIDEO = 0;

    private OpenApiUtils() {
    }

    public static void addCommand(List<String> features, IAddInstanceHolder[] ADD_INSTANCE_COMMANDS) {
        if (!ListUtils.isEmpty((List) features) && ADD_INSTANCE_COMMANDS != null && ADD_INSTANCE_COMMANDS.length >= 1) {
            for (AddInstanceHolder holder : ADD_INSTANCE_COMMANDS) {
                if (features.contains(holder.key)) {
                    ServerCommand<?> command = holder.command;
                    if (command != null) {
                        command.setWatcher(new ProcessWatcher(holder.duration, holder.count, holder.interval));
                        OpenApiManager.instance().addCommand(command);
                    }
                }
            }
        }
    }

    public static Channel createChannel(com.qiyi.tv.client.data.Channel channel) {
        Channel serverChannel = new Channel();
        serverChannel.id = "" + channel.getId();
        serverChannel.name = channel.getName();
        UserTags userTags = ((LocalChannel) channel).getUserTags();
        serverChannel.back = LocalUserTags.getBkUrl(userTags);
        serverChannel.layout = LocalUserTags.getLayout(userTags);
        serverChannel.focus = LocalUserTags.getChannelResourceIdForRecommend(userTags);
        serverChannel.recRes = LocalUserTags.getChannelResourceIdForRecommendOther(userTags);
        serverChannel.recTag = LocalUserTags.getChannelResourceIdForClassTags(userTags);
        serverChannel.run = LocalUserTags.getChannelPlayDirectly(userTags);
        serverChannel.type = LocalUserTags.getChannelType(userTags);
        serverChannel.qipuId = LocalUserTags.getChannelQipu(userTags);
        return serverChannel;
    }

    public static com.qiyi.tv.client.data.Channel createSdkChannel(Channel channel) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkChannel() from " + DataUtils.channelToString(channel));
        }
        LocalChannel channel4Sdk = new LocalChannel();
        channel4Sdk.setId(StringUtils.parse(channel.id, -1));
        channel4Sdk.setName(channel.name);
        channel4Sdk.setPicUrl(channel.picUrl);
        channel4Sdk.setIconUrl(channel.icon);
        LocalUserTags.setBkUrl(channel4Sdk.getUserTags(), channel.back);
        LocalUserTags.setLayout(channel4Sdk.getUserTags(), channel.layout);
        LocalUserTags.setChannelResourceIdForRecommend(channel4Sdk.getUserTags(), channel.focus);
        LocalUserTags.setChannelResourceIdForRecommendOther(channel4Sdk.getUserTags(), channel.recRes);
        LocalUserTags.setChannelResourceIdForClassTags(channel4Sdk.getUserTags(), channel.recTag);
        LocalUserTags.setChannelPlayDirectly(channel4Sdk.getUserTags(), channel.run);
        LocalUserTags.setChannelType(channel4Sdk.getUserTags(), channel.type);
        LocalUserTags.setChannelQipu(channel4Sdk.getUserTags(), channel.qipuId);
        LocalUserTags.setChannelSpec(channel4Sdk.getUserTags(), channel.spec);
        if (channel.prosVals != null && channel.prosVals.size() > 0) {
            LocalUserTags.setChannelResourceIdForTabRecommend(channel4Sdk.getUserTags(), ((ChannelPool) channel.prosVals.get(0)).poolResId);
        }
        ArrayList<String> tags = new ArrayList();
        if (channel.tags != null) {
            for (TwoLevelTag twoLevelTag : channel.tags) {
                if (!(twoLevelTag == null || twoLevelTag.tags == null)) {
                    for (ThreeLevelTag threeLevelTag : twoLevelTag.tags) {
                        tags.add(threeLevelTag.n);
                        tags.add(threeLevelTag.v);
                    }
                }
            }
        }
        LocalUserTags.setChannelFilterTags(channel4Sdk.getUserTags(), tags);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkChannel() return " + channel4Sdk);
        }
        return channel4Sdk;
    }

    public static Album createAlbum(Media media) {
        int i = 1;
        int i2 = 0;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createAlbum(" + media + ")");
        }
        Album album = null;
        if (media instanceof LocalAlbum) {
            LocalAlbum info = (LocalAlbum) media;
            album = new Album();
            album.qpId = info.getId();
            album.name = info.getName();
            album.pic = info.getPicUrl();
            album.chnId = info.getChannelId();
            album.tvQid = info.getVideoId();
            album.focus = info.getComment();
            if (info.isSeries()) {
                i2 = 1;
            }
            album.isSeries = i2;
            album.startTime = info.getStartTime();
            album.order = info.getPlayOrder();
            album.sourceCode = info.getSourceCode();
            album.addTime = info.getHistoryAddTime();
            album.tvCount = info.getTvCount();
            album.playTime = info.getPlayTime();
            album.type = 1;
            album.vid = Utils.getVid(info);
        } else if (media instanceof LocalVideo) {
            LocalVideo info2 = (LocalVideo) media;
            album = new Album();
            album.qpId = info2.getAlbumId();
            album.name = info2.getName();
            album.pic = info2.getPicUrl();
            album.chnId = info2.getChannelId();
            album.tvQid = info2.getId();
            if (!info2.isSeries()) {
                i = 0;
            }
            album.isSeries = i;
            album.startTime = info2.getStartTime();
            album.order = info2.getPlayOrder();
            album.sourceCode = info2.getSourceCode();
            album.addTime = info2.getHistoryAddTime();
            album.playTime = info2.getPlayTime();
            album.type = 0;
            album.vid = Utils.getVid(info2);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createAlbum() return " + album);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createAlbum() return " + DataUtils.albumInfoToString(album));
        }
        return album;
    }

    private static int getCornerHint(CornerMark cornerMark) {
        if (cornerMark == null) {
            return 0;
        }
        switch (cornerMark) {
            case CORNERMARK_1080P:
                return 3;
            case CORNERMARK_3D:
                return 5;
            case CORNERMARK_4K:
                return 6;
            case CORNERMARK_720P:
                return 1;
            case CORNERMARK_DOLBY:
                return 4;
            case CORNERMARK_EXCLUSIVEPLAY:
                return 8;
            case CORNERMARK_H265:
                return 2;
            case CORNERMARK_VIP:
                return 7;
            default:
                return 0;
        }
    }

    public static Media createSdkMedia(Album info) {
        if (info == null) {
            return null;
        }
        return createSdkMedia(info, info.tvPic, info.pic);
    }

    private static Media createSdkMedia(Album info, String tvPic, String pic) {
        boolean z = true;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkMedia(" + info + ", " + -1 + ")" + DataUtils.albumInfoToString(info));
        }
        Media media = null;
        if (info != null) {
            if (info.getType() == AlbumType.ALBUM) {
                Media album = new LocalAlbum();
                album.setId(info.qpId);
                album.setName(info.name);
                album.setPicUrl(getOpenApiAlbumImageUrl(info));
                album.setChannelId(info.chnId);
                album.setVideoId(info.tvQid);
                album.setComment(info.focus);
                if (info.isSeries == 0) {
                    z = false;
                }
                album.setIsSeries(z);
                album.setStartTime(info.startTime);
                album.setPlayOrder(info.order);
                album.setCornerHint(getCornerHint(info.getCornerMark()));
                album.setSourceCode(info.sourceCode);
                album.setScore(info.score);
                album.setFocus(info.focus);
                album.setTotalTime(parse(info.len, -1));
                album.setPlayTime(info.playTime);
                album.setPlayCount(info.pCount);
                album.setTvCount(info.tvCount);
                album.setHistoryAddTime(info.addTime);
                LocalUserTags.setType(album.getUserTags(), info.type);
                media = album;
            } else if (info.getType() == AlbumType.VIDEO) {
                Media video = new LocalVideo();
                video.setId(info.tvQid);
                video.setName(info.name);
                video.setPicUrl(getOpenApiAlbumImageUrl(info));
                video.setChannelId(info.chnId);
                video.setAlbumId(info.qpId);
                video.setComment(info.focus);
                if (info.isSeries == 0) {
                    z = false;
                }
                video.setIsSeries(z);
                video.setStartTime(info.startTime);
                video.setPlayOrder(info.order);
                video.setCornerHint(getCornerHint(info.getCornerMark()));
                video.setSourceCode(info.sourceCode);
                video.setScore(info.score);
                video.setFocus(info.focus);
                video.setTotalTime(parse(info.len, -1));
                video.setPlayTime(info.playTime);
                video.setPlayCount(info.pCount);
                video.setHistoryAddTime(info.addTime);
                LocalUserTags.setType(video.getUserTags(), info.type);
                media = video;
            }
        }
        if (media != null) {
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkMedia() return " + media);
        }
        return media;
    }

    public static ChannelLabel createChannelLabel(Media media) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createChannelLabel(" + media + ")");
        }
        ChannelLabel label = null;
        if (media instanceof LocalPlaylist) {
            LocalPlaylist playlist = (LocalPlaylist) media;
            label = new ChannelLabel();
            label.id = playlist.getId();
            label.name = playlist.getName();
            label.imageUrl = playlist.getPicUrl();
            label.itemId = LocalUserTags.getItemId(playlist.getUserTags());
            label.itemImageUrl = LocalUserTags.getTvPic(playlist.getUserTags());
            label.channelId = LocalUserTags.getChannelId(playlist.getUserTags());
            label.sourceId = LocalUserTags.getSourceId(playlist.getUserTags());
            label.itemType = LocalUserTags.getItemType(playlist.getUserTags());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createChannelLabel() return " + label);
        }
        return label;
    }

    public static Media createSdkMedia(ChannelLabel info) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkMedia(" + info + ")" + DataUtils.channelLabelToString(info));
        }
        Media media = null;
        if (info != null && info.getType() == ResourceType.COLLECTION) {
            Media playlist = new LocalPlaylist();
            playlist.setId(info.id);
            playlist.setName(info.name);
            playlist.setPicUrl(info.imageUrl);
            playlist.setSourceCode(info.sourceId);
            LocalUserTags.setItemId(playlist.getUserTags(), info.itemId);
            LocalUserTags.setTvPic(playlist.getUserTags(), info.itemImageUrl);
            LocalUserTags.setChannelId(playlist.getUserTags(), info.channelId);
            LocalUserTags.setSourceId(playlist.getUserTags(), info.sourceId);
            LocalUserTags.setItemType(playlist.getUserTags(), info.itemType);
            media = playlist;
        } else if (info != null && (info.getType() == ResourceType.ALBUM || info.getType() == ResourceType.VIDEO)) {
            media = createSdkMedia(info.getVideo(), info.postImage, info.imageUrl);
        }
        if (media != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "createSdkMedia() prompt " + info.itemPrompt);
            }
            media.setFromSdk(true);
            media.setItemPrompt(info.getPrompt());
            media.setScore(info.score);
            if (!StringUtils.isEmpty(info.shortTitle)) {
                media.setTitle(info.shortTitle);
            } else if (StringUtils.isEmpty(info.getPrompt())) {
                media.setTitle(info.name);
            } else {
                media.setTitle(info.getPrompt());
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkMedia() return " + media);
        }
        return media;
    }

    @Deprecated
    public static String getOpenApiAlbumImageUrl(String tvPic, String pic) {
        if (!StringUtils.isEmpty((CharSequence) tvPic)) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._354_490, tvPic);
        }
        if (StringUtils.isEmpty((CharSequence) pic)) {
            return null;
        }
        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, pic);
    }

    private static String getOpenApiAlbumImageUrl(Album album) {
        if (album.getType() == AlbumType.PEOPLE) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic);
        } else if ((GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album) == AlbumKind.SIGLE_VIDEO || !GetInterfaceTools.getAlbumInfoHelper().isSingleType(album)) && (GetInterfaceTools.getCornerProvider().isSpecialChannel(album.chnId) || 6 == album.chnId)) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic);
        } else {
            return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, StringUtils.isEmpty(album.pic) ? album.tvPic : album.pic);
        }
    }

    private static long parse(String str, long defaultValue) {
        long value = defaultValue;
        try {
            value = Long.parseLong(str);
        } catch (NumberFormatException e) {
            LogUtils.w(TAG, "parse(" + str + ") error!");
        }
        return value;
    }

    public static String encodeClassTag(String decodedTag) {
        return CLASS_TAG_PREFIX + CLASS_TAG_FORMATTER.format(Long.valueOf(CLASS_TAG_OFFSET - (parse(decodedTag, 0) * 2)));
    }

    public static String decodeClassTag(String encodedTag) {
        return "" + ((CLASS_TAG_OFFSET - parse(encodedTag.substring(CLASS_TAG_PREFIX.length()), 0)) / 2);
    }

    public static boolean isVipUser(Context context) {
        boolean vip = false;
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType != null) {
            if (userType.isPlatinum() || userType.isLitchi()) {
                vip = true;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "isVipUser() return " + vip);
            }
        }
        return vip;
    }

    public static void fillUserFilterTags(List<String> userFilters, List<String> filterTags, List<String> finalNames, List<String> finalValues) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fillFilterTags(" + userFilters + ", " + filterTags + ")");
        }
        if (userFilters != null && filterTags != null) {
            if (finalNames != null || finalValues != null) {
                int size = filterTags.size() / 2;
                for (int i = 0; i < size; i++) {
                    String name = (String) filterTags.get(i * 2);
                    String value = (String) filterTags.get((i * 2) + 1);
                    if (userFilters.contains(name)) {
                        if (finalNames != null) {
                            finalNames.add(name);
                        }
                        if (finalValues != null) {
                            finalValues.add(value);
                        }
                    }
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "fillFilterTags() finalNames=" + finalNames + ", finalValues=" + finalValues);
                }
            }
        }
    }

    public static String getUserFilterValues(List<String> userFilters, List<String> filterTags) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getUserFilterValues(" + userFilters + ", " + filterTags + ")");
        }
        String tagValues = "";
        if (userFilters == null || filterTags == null) {
            tagValues = null;
        } else {
            List<String> finalValues = new ArrayList();
            fillUserFilterTags(userFilters, filterTags, null, finalValues);
            int valueSize = finalValues.size();
            if (valueSize == 1) {
                tagValues = (String) finalValues.get(0);
            } else if (valueSize > 1) {
                for (int i = 0; i < valueSize - 1; i++) {
                    tagValues = tagValues + ((String) finalValues.get(i)) + ",";
                }
                tagValues = tagValues + ((String) finalValues.get(valueSize - 1));
            } else {
                tagValues = null;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getUserFilterValues() return " + tagValues);
        }
        return tagValues;
    }

    public static int parseErrorCode(String code) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "parseErrorCode(" + code + ")");
        }
        int errorCode = 7;
        if ("E000012".equals(code)) {
            errorCode = 0;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "parseErrorCode() return " + errorCode);
        }
        return errorCode;
    }
}
