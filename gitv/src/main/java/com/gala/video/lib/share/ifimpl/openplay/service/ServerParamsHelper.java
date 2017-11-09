package com.gala.video.lib.share.ifimpl.openplay.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalAlbum;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalChannel;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalPlaylist;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalVideo;
import com.qiyi.tv.client.data.Album;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Playlist;
import com.qiyi.tv.client.data.Video;
import com.qiyi.tv.client.feature.account.UserInfo;
import com.qiyi.tv.client.feature.account.VipInfo;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.impl.ClientInfo;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.ParamsHelper;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;
import java.util.List;

public class ServerParamsHelper {
    private static final String TAG = "ServerParamsHelper";

    public static ClientInfo parseClientInfo(Bundle bundle) {
        return ParamsHelper.parseClientInfo(bundle);
    }

    public static Intent parseIntent(Bundle bundle) {
        return ParamsHelper.parseIntent(bundle);
    }

    public static int parseIntentFlag(Bundle bundle) {
        return ParamsHelper.parseIntentFlag(bundle);
    }

    public static void setIntentFlag(Bundle bundle, int flag) {
        ParamsHelper.setIntentFlag(bundle, flag);
    }

    public static int parseResultCode(Bundle params) {
        return ParamsHelper.parseResultCode(params);
    }

    public static void setResultCode(Bundle params, int code) {
        ParamsHelper.setResultCode(params, code);
    }

    public static <T> T parseResultData(Bundle params) {
        Utils.dumpBundle("parseResultData()", params);
        return ParamsHelper.parseResultData(params);
    }

    public static <T extends Parcelable> void setResultData(Bundle params, T data) {
        Log.d(TAG, "setResultData(" + data + ")");
        if (data == null) {
            ParamsHelper.setResultData(params, (Parcelable) data);
        } else if (data instanceof Media) {
            Parcelable media = (Media) data;
            if (data instanceof LocalAlbum) {
                media = ((LocalAlbum) data).getSdkAlbum();
            } else if (data instanceof LocalVideo) {
                media = ((LocalVideo) data).getSdkVideo();
            } else if (data instanceof LocalPlaylist) {
                media = ((LocalPlaylist) data).getSdkPlaylist();
            }
            ParamsHelper.setResultData(params, media);
        } else if (data instanceof Channel) {
            Parcelable channel = (Channel) data;
            if (data instanceof LocalChannel) {
                channel = ((LocalChannel) data).getSdkChannel();
            }
            ParamsHelper.setResultData(params, channel);
        } else {
            ParamsHelper.setResultData(params, (Parcelable) data);
        }
        Utils.dumpBundle("setResultData()", params);
    }

    public static <T extends Parcelable> void setResultData(Bundle params, ArrayList<T> dataList) {
        Log.d(TAG, "setResultData(" + dataList + ")");
        if (dataList != null) {
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                LogUtils.d(TAG, "setResultData() [" + i + "]=" + dataList.get(i));
            }
        }
        if (params != null) {
            params.setClassLoader(ServerParamsHelper.class.getClassLoader());
            if (dataList == null || dataList.size() <= 0) {
                ParamsHelper.setResultData(params, (ArrayList) dataList);
            } else if (dataList.get(0) instanceof Media) {
                setResultDataOfMedia(params, dataList);
            } else if (dataList.get(0) instanceof Channel) {
                setResultDataOfChannel(params, dataList);
            } else {
                ParamsHelper.setResultData(params, (ArrayList) dataList);
            }
        }
        Utils.dumpBundle("setResultData()", params);
    }

    public static void setResultDataOfArrayString(Bundle params, ArrayList<String> dataList) {
        Log.d(TAG, "setResultDataOfArrayString(" + dataList + ")");
        ParamsHelper.setResultDataOfArrayString(params, dataList);
        Utils.dumpBundle("setResultDataOfArrayString()", params);
    }

    private static <T extends Parcelable> void setResultDataOfMedia(Bundle params, ArrayList<Media> dataList) {
        Log.d(TAG, "setResultDataOfMedia(" + dataList + ")");
        if (params != null) {
            ArrayList temp = new ArrayList(dataList.size());
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                Media one = (Media) dataList.get(i);
                if (one instanceof LocalAlbum) {
                    one = ((LocalAlbum) one).getSdkAlbum();
                } else if (one instanceof LocalVideo) {
                    one = ((LocalVideo) one).getSdkVideo();
                } else if (one instanceof LocalPlaylist) {
                    one = ((LocalPlaylist) one).getSdkPlaylist();
                }
                temp.add(one);
            }
            params.setClassLoader(ServerParamsHelper.class.getClassLoader());
            ParamsHelper.setResultData(params, temp);
        }
        Utils.dumpBundle("setResultDataOfMedia()", params);
    }

    private static <T extends Parcelable> void setResultDataOfChannel(Bundle params, ArrayList<Channel> dataList) {
        Log.d(TAG, "setResultDataOfChannel(" + dataList + ")");
        if (params != null) {
            ArrayList temp = new ArrayList(dataList.size());
            int size = dataList.size();
            for (int i = 0; i < size; i++) {
                Channel one = (Channel) dataList.get(i);
                if (one instanceof LocalChannel) {
                    one = ((LocalChannel) one).getSdkChannel();
                }
                temp.add(one);
            }
            params.setClassLoader(ServerParamsHelper.class.getClassLoader());
            ParamsHelper.setResultData(params, temp);
        }
        Utils.dumpBundle("setResultDataOfChannel()", params);
    }

    public static void setKeyword(Bundle bundle, String keyword) {
        ParamsHelper.setKeyword(bundle, keyword);
    }

    public static String parseKeyword(Bundle bundle) {
        return ParamsHelper.parseKeyword(bundle);
    }

    public static void setChannel(Bundle bundle, Channel channel) {
        if (channel instanceof LocalChannel) {
            ParamsHelper.setChannel(bundle, ((LocalChannel) channel).getSdkChannel());
        } else {
            ParamsHelper.setChannel(bundle, channel);
        }
    }

    public static LocalChannel parseChannel(Bundle bundle) {
        LocalChannel channel = new LocalChannel(ParamsHelper.parseChannel(bundle));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "parseChannel() return " + channel);
        }
        return channel;
    }

    public static void setMedia(Bundle bundle, Media media) {
        if (media instanceof LocalPlaylist) {
            ParamsHelper.setMedia(bundle, ((LocalPlaylist) media).getSdkPlaylist());
        } else if (media instanceof LocalAlbum) {
            ParamsHelper.setMedia(bundle, ((LocalAlbum) media).getSdkAlbum());
        } else if (media instanceof LocalVideo) {
            ParamsHelper.setMedia(bundle, ((LocalVideo) media).getSdkVideo());
        } else {
            ParamsHelper.setMedia(bundle, media);
        }
    }

    public static Media parseMedia(Bundle bundle) {
        Media media = ParamsHelper.parseMedia(bundle);
        if (media instanceof Playlist) {
            return new LocalPlaylist((Playlist) media);
        }
        if (media instanceof Album) {
            return new LocalAlbum((Album) media);
        }
        if (media instanceof Video) {
            return new LocalVideo((Video) media);
        }
        return media;
    }

    public static void setFilterTags(Bundle bundle, List<String> filterFlags) {
        ParamsHelper.setFilterTags(bundle, filterFlags);
    }

    public static List<String> parseFilterTags(Bundle bundle) {
        return ParamsHelper.parseFilterTags(bundle);
    }

    public static void setClassTag(Bundle bundle, String classTag) {
        ParamsHelper.setClassTag(bundle, classTag);
    }

    public static String parseClassTag(Bundle bundle) {
        return ParamsHelper.parseClassTag(bundle);
    }

    public static int parseCount(Bundle bundle) {
        return ParamsHelper.parseCount(bundle);
    }

    public static void setMaxCount(Bundle bundle, int maxCount) {
        ParamsHelper.setMaxCount(bundle, maxCount);
    }

    public static int parseMaxCount(Bundle bundle) {
        return ParamsHelper.parseMaxCount(bundle);
    }

    public static String parseOnlyLongVideo(Bundle bundle) {
        return ParamsHelper.parseOnlyLongVideo(bundle);
    }

    public static void setSort(Bundle bundle, String sort) {
        ParamsHelper.setSort(bundle, sort);
    }

    public static String parseSort(Bundle bundle) {
        return ParamsHelper.parseSort(bundle);
    }

    public static void setPosition(Bundle bundle, int position) {
        ParamsHelper.setPosition(bundle, position);
    }

    public static int parsePosition(Bundle bundle) {
        return ParamsHelper.parsePosition(bundle);
    }

    public static void setTitle(Bundle bundle, String title) {
        ParamsHelper.setTitle(bundle, title);
    }

    public static String parseTitle(Bundle bundle) {
        return ParamsHelper.parseTitle(bundle);
    }

    public static String parseActivateCode(Bundle bundle) {
        return ParamsHelper.parseActivateCode(bundle);
    }

    public static void setOperationTarget(Bundle bundle, int target) {
        ParamsHelper.setOperationTarget(bundle, target);
    }

    public static int parseOperationTarget(Bundle bundle) {
        return ParamsHelper.parseOperationTarget(bundle);
    }

    public static void setOperationType(Bundle bundle, int operationType) {
        ParamsHelper.setOperationType(bundle, operationType);
    }

    public static int parseOperationType(Bundle bundle) {
        return ParamsHelper.parseOperationType(bundle);
    }

    public static void setOperationDataType(Bundle bundle, int dataType) {
        ParamsHelper.setOperationDataType(bundle, dataType);
    }

    public static int parseOperationDataType(Bundle bundle) {
        return ParamsHelper.parseOperationDataType(bundle);
    }

    public static void setPageNo(Bundle bundle, int pageNo) {
        ParamsHelper.setPageNo(bundle, pageNo);
    }

    public static int parsePageNo(Bundle bundle) {
        return ParamsHelper.parsePageNo(bundle);
    }

    public static void setPageSize(Bundle bundle, int pageSize) {
        ParamsHelper.setPageSize(bundle, pageSize);
    }

    public static int parsePageSize(Bundle bundle) {
        return ParamsHelper.parsePageSize(bundle);
    }

    public static void setPageMaxSize(Bundle bundle, int pageMaxSize) {
        ParamsHelper.setPageMaxSize(bundle, pageMaxSize);
    }

    public static int parsePageMaxSize(Bundle bundle) {
        return ParamsHelper.parsePageMaxSize(bundle);
    }

    public static void setPlayState(Bundle bundle, int state) {
        ParamsHelper.setPlayState(bundle, state);
    }

    public static int parsePlayState(Bundle bundle) {
        return ParamsHelper.parsePlayState(bundle);
    }

    public static void setCommandContinue(Bundle bundle, boolean continueCommand) {
        ParamsHelper.setCommandContinue(bundle, continueCommand);
    }

    public static boolean parseCommandContinue(Bundle bundle) {
        return ParamsHelper.parseCommandContinue(bundle);
    }

    public static UserInfo parseLoginUserInfo(Bundle bundle) {
        return ParamsHelper.parseLoginUserInfo(bundle);
    }

    public static void setLoginStatus(Bundle bundle, int status) {
        ParamsHelper.setLoginStatus(bundle, status);
    }

    public static void setHistoryChangedAction(Bundle bundle, int action) {
        ParamsHelper.setHistoryChangedAction(bundle, action);
    }

    public static void setFavoriteChangedAction(Bundle bundle, int action) {
        ParamsHelper.setFavoriteChangedAction(bundle, action);
    }

    public static int parsePictureSize(Bundle bundle) {
        return ParamsHelper.parsePictureSize(bundle);
    }

    public static List<String> parsePictureUrl(Bundle bundle) {
        return ParamsHelper.parsePictureUrl(bundle);
    }

    public static void setPictureUrl(Bundle bundle, ArrayList<String> urls) {
        ParamsHelper.setPictureUrl(bundle, urls);
    }

    public static String parseResourceId(Bundle params) {
        return ParamsHelper.parseResourceId(params);
    }

    public static int parsePictureType(Bundle params) {
        return ParamsHelper.parsePictureType(params);
    }

    public static void setResourcePictureUrl(Bundle bundle, String url) {
        ParamsHelper.setResourcePictureUrl(bundle, url);
    }

    public static void setQrCodeUrl(Bundle bundle, String qrCodeUrl) {
        ParamsHelper.setQrCodeUrl(bundle, qrCodeUrl);
    }

    public static int parseChannelId(Bundle params) {
        return ParamsHelper.parseChannelId(params);
    }

    public static String parseApiKey(Bundle bundle) {
        return ParamsHelper.parseApiKey(bundle);
    }

    public static void setUUID(Bundle bundle, String uuid) {
        ParamsHelper.setUUID(bundle, uuid);
    }

    public static void setTvPackageName(Bundle bundle, String packageName) {
        ParamsHelper.setTvPackageName(bundle, packageName);
    }

    public static boolean parseIsFullScreen(Bundle bundle) {
        return ParamsHelper.parseIsFullScreen(bundle);
    }

    public static void setIsFullScreen(Bundle bundle, boolean isFullScreen) {
        ParamsHelper.setIsFullScreen(bundle, isFullScreen);
    }

    public static boolean parseIsSkipHeaderTailer(Bundle params) {
        return ParamsHelper.parseIsSkipHeaderTailer(params);
    }

    public static void setIsSkipHeaderTailer(Bundle result, boolean isSkip) {
        ParamsHelper.setSkipHeaderTailer(result, isSkip);
    }

    public static int parseStreamType(Bundle params) {
        return ParamsHelper.parseStreamType(params);
    }

    public static void setStreamType(Bundle result, int streamType) {
        ParamsHelper.setStreamType(result, streamType);
    }

    public static boolean fromThirdUser(Bundle params) {
        return ParamsHelper.fromThirdUser(params);
    }

    public static PlayParams parsePlayParams(Bundle params) {
        return ParamsHelper.parsePlayParams(params);
    }

    public static int parseAppCategory(Bundle params) {
        return ParamsHelper.parseAppCategory(params);
    }

    public static AppInfo parseAppInfo(Bundle params) {
        return ParamsHelper.parseAppInfo(params);
    }

    public static void setActivationState(Bundle params, int state) {
        ParamsHelper.setActivationState(params, state);
    }

    public static int parseHomeTabType(Bundle params) {
        return ParamsHelper.parseHomeTabType(params);
    }

    public static void setVipInfo(Bundle params, VipInfo vipInfo) {
        ParamsHelper.setVipInfo(params, vipInfo);
    }
}
