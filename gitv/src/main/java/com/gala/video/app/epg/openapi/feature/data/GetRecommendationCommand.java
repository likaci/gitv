package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.model.ResId;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.List;

public class GetRecommendationCommand extends BaseGetResourceCommand {
    private static final String TAG = "GetRecommendationCommand";

    public GetRecommendationCommand(Context context, int maxCount) {
        super(context, TargetType.TARGET_RECOMMEND, 20003, DataType.DATA_MEDIA_LIST, maxCount);
    }

    public String getResourceId(Bundle params) {
        List<ResId> resources = DeviceCheckModel.getInstance().getHomeResId();
        if (resources == null) {
            return null;
        }
        int position = ServerParamsHelper.parsePosition(params);
        if (position < 0 || position >= resources.size()) {
            return null;
        }
        return ((ResId) resources.get(position)).id;
    }

    protected Media createSdkMedia(Bundle bundle, ChannelLabel channelLabel, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "createSdkMedia()");
        }
        Media media = OpenApiUtils.createSdkMedia(channelLabel);
        if (media != null) {
            LocalUserTags.setMediaPic(media.getUserTags(), media.getPicUrl());
            String imageUrl = null;
            String textContent = channelLabel.getPrompt();
            int position = ServerParamsHelper.parsePosition(bundle);
            ItemKvs itemKvs;
            if (position == 0) {
                itemKvs = channelLabel.getItemKvs();
                imageUrl = itemKvs == null ? "" : itemKvs.tv_img_950_470;
                if (StringUtils.isEmpty((CharSequence) imageUrl)) {
                    imageUrl = channelLabel.itemImageUrl;
                }
                if (itemKvs != null) {
                    LocalUserTags.setResource950_470(media.getUserTags(), itemKvs.tv_img_950_470);
                    LocalUserTags.setResource570_570(media.getUserTags(), itemKvs.tv_img_570_570);
                }
                if (itemKvs != null && !StringUtils.isEmpty(itemKvs.homepageTitle)) {
                    textContent = itemKvs.homepageTitle;
                } else if (!StringUtils.isEmpty(channelLabel.name)) {
                    textContent = channelLabel.name + "¡êo" + textContent;
                }
            } else if (position == 1) {
                imageUrl = channelLabel.itemImageUrl;
            } else if (position == 2) {
                itemKvs = channelLabel.getItemKvs();
                if (StringUtils.isEmpty(channelLabel.itemImageUrl)) {
                    imageUrl = PicSizeUtils.getUrlWithSize(PhotoSize._480_270, channelLabel.imageUrl);
                } else {
                    imageUrl = channelLabel.itemImageUrl;
                }
                LocalUserTags.setResource480_270(media.getUserTags(), imageUrl);
                if (itemKvs != null) {
                    LocalUserTags.setResource495_495(media.getUserTags(), itemKvs.tv_img_495_495);
                }
            }
            media.setPicUrl(imageUrl);
            media.setTitle(textContent);
            LocalUserTags.setResourceDefaultSize(media.getUserTags(), imageUrl);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "createSdkMedia() reture=" + media.toString());
            }
        }
        return media;
    }
}
