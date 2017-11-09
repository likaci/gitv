package com.gala.video.app.epg.openapi.feature.open;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.model.AlbumOpenApiModel;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalChannel;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiChannelMap;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.impl.Params.TargetType;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;
import java.util.List;

public class OpenChannelCommand extends ServerCommand<Intent> {
    private static final String TAG = "OpenChannelCommand";

    public OpenChannelCommand(Context context) {
        super(context, TargetType.TARGET_CHANNEL, 20001, 30000);
    }

    public Bundle onProcess(Bundle params) {
        LocalChannel channel = ServerParamsHelper.parseChannel(params);
        if (channel == null) {
            return OpenApiResultCreater.createResultBundle(6);
        }
        List<String> userFilters = ServerParamsHelper.parseFilterTags(params);
        String sort = ServerParamsHelper.parseSort(params);
        String classTag = ServerParamsHelper.parseClassTag(params);
        int subChannelId = StringUtils.parse(classTag, -1);
        int channelId = channel.getId();
        String channelName = ServerParamsHelper.parseTitle(params);
        int intentFlag = ServerParamsHelper.parseIntentFlag(params);
        if (intentFlag < 0) {
            intentFlag = Utils.INTENT_FLAG_DEFAULT;
        }
        int count = ServerParamsHelper.parseCount(params);
        AlbumOpenApiModel model = new AlbumOpenApiModel();
        if (!ListUtils.isEmpty((List) userFilters) || !TextUtils.isEmpty(sort)) {
            if (userFilters == null) {
                userFilters = new ArrayList();
            }
            if (!TextUtils.isEmpty(sort)) {
                userFilters.add(sort);
            }
            ArrayList<String> filterTags = LocalUserTags.getChannelFilterTags(channel.getUserTags());
            OpenApiUtils.fillUserFilterTags(userFilters, filterTags, new ArrayList(), new ArrayList());
            model.setDataTagName(null);
            model.setDataTagId(OpenApiUtils.getUserFilterValues(userFilters, filterTags));
            model.setDataTagType(IAlbumConfig.LABEL_MENU);
        } else if (subChannelId >= 0) {
            model.setDataTagName(ServerParamsHelper.parseTitle(params));
            model.setDataTagId("" + OpenApiChannelMap.decodeChannelId(subChannelId));
            model.setDataTagType(IAlbumConfig.LABEL_CHANNEL);
        } else if (!TextUtils.isEmpty(classTag)) {
            model.setDataTagName(ServerParamsHelper.parseTitle(params));
            model.setDataTagId(OpenApiUtils.decodeClassTag(classTag));
            model.setDataTagType(IAlbumConfig.LABEL_LABEL);
        }
        model.setChannelName(channelName);
        model.setChannelId(channelId);
        model.setIntentFlag(intentFlag);
        model.setLoadLimitSize(count);
        String labelIdString = model.getDataTagId();
        LogUtils.m1568d(TAG, "labelIdString:" + labelIdString);
        if (labelIdString == null || "".equals(labelIdString)) {
            AlbumUtils.startChannelPageOpenApi(getContext(), channelId, channelName, count, intentFlag);
        } else {
            AlbumUtils.startChannelPageOpenApi(getContext(), model);
        }
        Bundle result = OpenApiResultCreater.createResultBundle(0);
        ServerParamsHelper.setCommandContinue(result, false);
        increaseAccessCount();
        if (!LogUtils.mIsDebug) {
            return result;
        }
        LogUtils.m1568d(TAG, "process() AlbumOpenApiModel=" + model);
        return result;
    }
}
