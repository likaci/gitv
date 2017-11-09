package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetResourcePictureUrlCommand extends ServerCommand<String> {
    private static final String TAG = "GetResourcePictureUrlCommand";

    public GetResourcePictureUrlCommand(Context context) {
        super(context, TargetType.TARGET_MEDIA, 20003, DataType.DATA_URL);
        setNeedNetwork(false);
    }

    public Bundle onProcess(Bundle params) {
        Media media = ServerParamsHelper.parseMedia(params);
        int pictureType = ServerParamsHelper.parsePictureType(params);
        int pictureSize = ServerParamsHelper.parsePictureSize(params);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onProcess() media=" + media + ", pictureType=" + pictureType + ", pictureSize=" + pictureSize);
        }
        String imageUrl = getPictureForOld(media, pictureType, pictureSize);
        if (StringUtils.isEmpty((CharSequence) imageUrl)) {
            imageUrl = getPicture(media, pictureType, pictureSize);
        }
        Bundle bundle = OpenApiResultCreater.createResultBundle(0);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "onProcess() return=" + imageUrl);
        }
        ServerParamsHelper.setResourcePictureUrl(bundle, imageUrl);
        increaseAccessCount();
        return bundle;
    }

    private String getPictureForOld(Media media, int pictureType, int pictureSize) {
        switch (pictureType) {
            case 1:
                if (pictureSize == 100) {
                    return LocalUserTags.getResource480_270(media.getUserTags());
                }
                return null;
            case 2:
                if (pictureSize == 100) {
                    return LocalUserTags.getResource495_495(media.getUserTags());
                }
                return null;
            case 3:
                if (pictureSize == 13) {
                    return LocalUserTags.getResource570_570(media.getUserTags());
                }
                if (pictureSize == 14) {
                    return LocalUserTags.getResource950_470(media.getUserTags());
                }
                return null;
            case 4:
                String imageUrl = LocalUserTags.getMediaPic(media.getUserTags());
                if (pictureSize != 100) {
                    return PicSizeUtils.exchangePictureUrl(imageUrl, pictureSize);
                }
                return imageUrl;
            default:
                return null;
        }
    }

    private String getPicture(Media media, int pictureType, int pictureSize) {
        switch (pictureType) {
            case 5:
                return getResourcePicture(media, pictureSize);
            case 6:
                return getCoverPicture(media, pictureSize);
            case 7:
                return getAlbumPicture(media, pictureSize);
            default:
                return null;
        }
    }

    private String getResourcePicture(Media media, int pictureSize) {
        switch (pictureSize) {
            case 8:
                return LocalUserTags.getResource480_270(media.getUserTags());
            case 11:
                return LocalUserTags.getResource495_495(media.getUserTags());
            case 13:
                return LocalUserTags.getResource570_570(media.getUserTags());
            case 14:
                return LocalUserTags.getResource950_470(media.getUserTags());
            default:
                return null;
        }
    }

    private String getCoverPicture(Media media, int pictureSize) {
        return PicSizeUtils.exchangePictureUrl(LocalUserTags.getCoverPicture(media.getUserTags()), pictureSize);
    }

    private String getAlbumPicture(Media media, int pictureSize) {
        return PicSizeUtils.exchangePictureUrl(LocalUserTags.getAlbumPicture(media.getUserTags()), pictureSize);
    }
}
