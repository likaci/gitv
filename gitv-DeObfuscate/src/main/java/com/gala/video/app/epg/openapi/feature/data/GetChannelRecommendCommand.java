package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalChannel;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Log;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;

public class GetChannelRecommendCommand extends BaseGetResourceCommand {
    private static final String TAG = "GetChannelRecommendCommand";
    private int mChannelSpec = 2;

    public GetChannelRecommendCommand(Context context, int maxCount) {
        super(context, TargetType.TARGET_CHANNEL, 20003, DataType.DATA_RECOMMENDATION, maxCount);
    }

    public String getResourceId(Bundle params) {
        LocalChannel channel = ServerParamsHelper.parseChannel(params);
        this.mChannelSpec = LocalUserTags.getChannelSpec(channel.getUserTags());
        return LocalUserTags.getChannelResourceIdForRecommend(channel.getUserTags());
    }

    protected Media createSdkMedia(Bundle bundle, ChannelLabel channelLabel, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "createSdkMedia()");
        }
        Media media = OpenApiUtils.createSdkMedia(channelLabel);
        if (media == null) {
            Log.m1622e(TAG, "createSdkMedia media is null");
            return null;
        }
        String textContent = channelLabel.getPrompt();
        if (this.mChannelSpec == 1) {
            textContent = GetInterfaceTools.getCornerProvider().getTitle(channelLabel, QLayoutKind.LANDSCAPE);
        } else if (this.mChannelSpec == 2) {
            textContent = GetInterfaceTools.getCornerProvider().getTitle(channelLabel, QLayoutKind.PORTRAIT);
        }
        media.setTitle(textContent);
        return media;
    }
}
