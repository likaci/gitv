package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.MaxCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.data.Media;
import java.util.List;

public abstract class BaseGetResourceCommand extends MaxCommand<List<Media>> {
    private static final String TAG = "BaseGetResourceCommand";

    private class MyListener extends ResultListHolder<Media> implements IVrsCallback<ApiResultChannelLabels> {
        private Bundle mBundle = null;

        public MyListener(Bundle params, int maxCount) {
            super(maxCount);
            this.mBundle = params;
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(BaseGetResourceCommand.TAG, "onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultChannelLabels channelLabels) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(BaseGetResourceCommand.TAG, "onSuccess(" + channelLabels + ")");
            }
            setNetworkValid(true);
            List<ChannelLabel> channelLabelList = channelLabels.getChannelLabels().items;
            int length = channelLabelList.size();
            for (int i = 0; i < length; i++) {
                Media media = BaseGetResourceCommand.this.createSdkMedia(this.mBundle, (ChannelLabel) channelLabelList.get(i), i);
                if (media != null) {
                    add(media);
                } else {
                    LogUtils.m1577w(BaseGetResourceCommand.TAG, "onSuccess() cannot translate to sdk media!!!");
                }
                if (isReachMax()) {
                    LogUtils.m1577w(BaseGetResourceCommand.TAG, "onSuccess() reach max size !!!" + this);
                    return;
                }
            }
        }
    }

    protected abstract Media createSdkMedia(Bundle bundle, ChannelLabel channelLabel, int i);

    public abstract String getResourceId(Bundle bundle);

    public BaseGetResourceCommand(Context context, int target, int operation, int dataType, int maxCount) {
        super(context, target, operation, dataType, maxCount);
        setNeedNetwork(true);
    }

    public Bundle onProcess(Bundle params) {
        Bundle result;
        int maxCount = ServerParamsHelper.parseMaxCount(params);
        if (maxCount < 0) {
            maxCount = getMaxCount();
        } else if (maxCount > getMaxCount()) {
            maxCount = getMaxCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() maxCount=" + maxCount);
        }
        MyListener listener = new MyListener(params, maxCount);
        String isFree = "0";
        String resourceId = getResourceId(params);
        if (TextUtils.isEmpty(resourceId)) {
            result = OpenApiResultCreater.createResultBundle(6);
        } else {
            VrsHelper.channelLabels.callSync(listener, resourceId, isFree);
            result = listener.getResult();
            if (listener.isNetworkValid()) {
                increaseAccessCount();
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "process() resourceId=" + resourceId + ", isFree=" + isFree);
        }
        return result;
    }
}
