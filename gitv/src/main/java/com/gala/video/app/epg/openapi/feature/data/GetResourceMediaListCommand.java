package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetResourceMediaListCommand extends BaseGetResourceCommand {
    public GetResourceMediaListCommand(Context context, int maxCount) {
        super(context, TargetType.TARGET_RESOURCE_MEDIA, 20003, DataType.DATA_MEDIA_LIST, maxCount);
    }

    public String getResourceId(Bundle params) {
        return OpenApiUtils.decodeClassTag(ServerParamsHelper.parseResourceId(params));
    }

    protected Media createSdkMedia(Bundle bundle, ChannelLabel channelLabel, int index) {
        Media media = OpenApiUtils.createSdkMedia(channelLabel);
        if (!StringUtils.isEmpty(channelLabel.itemImageUrl)) {
            media.setPicUrl(channelLabel.itemImageUrl);
        }
        return media;
    }
}
