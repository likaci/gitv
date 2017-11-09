package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetChannelRecommendForTabCommand extends BaseGetResourceCommand {
    private final String TAG = "GetChannelRecommendForTabCommand";

    public GetChannelRecommendForTabCommand(Context context, int maxCount) {
        super(context, TargetType.TARGET_CHANNEL, 20003, DataType.DATA_RECOMMENDATION_FOR_TAB, maxCount);
    }

    public String getResourceId(Bundle params) {
        return LocalUserTags.getChannelResourceIdForTabRecommend(ServerParamsHelper.parseChannel(params).getUserTags());
    }

    protected Media createSdkMedia(Bundle bundle, ChannelLabel channelLabel, int index) {
        Media media = OpenApiUtils.createSdkMedia(channelLabel);
        if (media == null) {
            Log.e("GetChannelRecommendForTabCommand", "createSdkMedia media is null");
            return null;
        }
        String mImageUrl = media.getPicUrl();
        if (StringUtils.isEmpty(channelLabel.itemImageUrl)) {
            mImageUrl = PicSizeUtils.getUrlWithSize(PhotoSize._480_270, channelLabel.imageUrl);
        } else {
            mImageUrl = channelLabel.itemImageUrl;
        }
        media.setPicUrl(mImageUrl);
        return media;
    }
}
