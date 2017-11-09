package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalChannel;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.MaxCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiResultCreater;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.ArrayList;
import java.util.List;

public class GetChannelMediaListCommand extends MaxCommand<List<Media>> {
    private static final int CHANNEL_TYPE_REAL = 0;
    private static final int CHANNEL_TYPE_TOPIC = 1;
    private static final int CHANNEL_TYPE_VIRTUAL = 2;
    private static final String TAG = "GetChannelMediaListCommand";

    private class MyListener extends ResultListHolder<Media> implements IApiCallback<ApiResultAlbumList> {
        public MyListener(int maxCount) {
            super(maxCount);
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(GetChannelMediaListCommand.TAG, "MyListener.onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultAlbumList albumList) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(GetChannelMediaListCommand.TAG, "MyListener.onSuccess(" + albumList + ")");
            }
            setNetworkValid(true);
            if (albumList != null && albumList.data != null) {
                for (Album info : albumList.data) {
                    Media media = OpenApiUtils.createSdkMedia(info);
                    if (media != null) {
                        add(media);
                    } else {
                        LogUtils.w(GetChannelMediaListCommand.TAG, "MyListener.onSuccess() cannot translate to sdk media!!!");
                    }
                    if (isReachMax()) {
                        LogUtils.w(GetChannelMediaListCommand.TAG, "MyListener.onSuccess() reach max size !!!" + this);
                        return;
                    }
                }
            }
        }
    }

    private class MyQipuListener extends ResultListHolder<Media> implements IVrsCallback<ApiResultPlayListQipu> {
        public MyQipuListener(int maxCount) {
            super(maxCount);
        }

        public void onException(ApiException exception) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(GetChannelMediaListCommand.TAG, "MyQipuListener.onException(" + exception + ")");
            }
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            setCode(7);
        }

        public void onSuccess(ApiResultPlayListQipu albumList) {
            if (LogUtils.mIsDebug) {
                LogUtils.w(GetChannelMediaListCommand.TAG, "MyQipuListener.onSuccess(" + albumList + ")");
            }
            setNetworkValid(true);
            if (albumList == null) {
                return;
            }
            if (ListUtils.isEmpty(albumList.getAlbumList())) {
                LogUtils.w(GetChannelMediaListCommand.TAG, "MyQipuListener.onSuccess() Didn't get data from server.");
                return;
            }
            for (Album info : albumList.getAlbumList()) {
                Media media = OpenApiUtils.createSdkMedia(info);
                if (media != null) {
                    add(media);
                } else {
                    LogUtils.w(GetChannelMediaListCommand.TAG, "MyQipuListener.onSuccess() cannot translate to sdk media!!!");
                }
                if (isReachMax()) {
                    LogUtils.w(GetChannelMediaListCommand.TAG, "MyQipuListener.onSuccess() reach max size !!!" + this);
                    return;
                }
            }
        }
    }

    public GetChannelMediaListCommand(Context context, int maxCount) {
        super(context, TargetType.TARGET_CHANNEL, 20003, DataType.DATA_MEDIA_LIST, maxCount);
        setNeedNetwork(true);
    }

    public Bundle onProcess(Bundle params) {
        int maxCount = ServerParamsHelper.parseMaxCount(params);
        if (maxCount > getMaxCount()) {
            maxCount = getMaxCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "process() maxCount=" + maxCount);
        }
        LocalChannel channel = ServerParamsHelper.parseChannel(params);
        int channelType = LocalUserTags.getChannelType(channel.getUserTags());
        if (channelType == 1) {
            return getPlaylistMedia(params, channel, channelType, maxCount);
        }
        return getChannelMedia(params, channel, channelType, maxCount);
    }

    private Bundle getPlaylistMedia(Bundle params, LocalChannel channel, int channelType, int maxCount) {
        String isFree = OpenApiUtils.isVipUser(getContext()) ? "0" : "1";
        String qipu = LocalUserTags.getChannelQipu(channel.getUserTags());
        MyQipuListener listener = new MyQipuListener(maxCount);
        VrsHelper.playListQipu.callSync(listener, qipu, isFree);
        Bundle result = listener.getResult();
        if (listener.isNetworkValid()) {
            increaseAccessCount();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getPlaylistMedia() maxCount=" + maxCount + ", qipu=" + qipu + ", channelType=" + channelType);
        }
        return result;
    }

    private Bundle getChannelMedia(Bundle params, LocalChannel channel, int channelType, int maxCount) {
        Bundle result;
        String chnId = channelType == 0 ? "" + channel.getId() : null;
        String virtualChnId = channelType == 2 ? "" + channel.getId() : null;
        int pageNo = ServerParamsHelper.parsePageNo(params);
        int pageSize = ServerParamsHelper.parsePageSize(params);
        MyListener listener = new MyListener(maxCount);
        List<String> userFilters = ServerParamsHelper.parseFilterTags(params);
        String sort = ServerParamsHelper.parseSort(params);
        if (ListUtils.isEmpty((List) userFilters) && TextUtils.isEmpty(sort)) {
            TVApi.albumList.callSync(listener, chnId, virtualChnId, "", "" + pageNo, "" + pageSize);
            result = listener.getResult();
            if (listener.isNetworkValid()) {
                increaseAccessCount();
            }
        } else {
            if (userFilters == null) {
                userFilters = new ArrayList();
            }
            if (!TextUtils.isEmpty(sort)) {
                userFilters.add(sort);
            }
            List filterTags = LocalUserTags.getChannelFilterTags(channel.getUserTags());
            if (ListUtils.isEmpty(filterTags)) {
                result = OpenApiResultCreater.createResultBundle(6);
            } else {
                if (OpenApiUtils.getUserFilterValues(userFilters, filterTags) == null) {
                    result = OpenApiResultCreater.createResultBundle(6);
                } else {
                    TVApi.albumList.callSync(listener, chnId, virtualChnId, OpenApiUtils.getUserFilterValues(userFilters, filterTags), "" + pageNo, "" + pageSize);
                    result = listener.getResult();
                    if (listener.isNetworkValid()) {
                        increaseAccessCount();
                    }
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getChannelMedia() pageNo=" + pageNo + ", pageSize=" + pageSize + ", maxCount=" + maxCount + ", chnId=" + chnId + ", virtualChnId=" + virtualChnId + ", channelType=" + channelType);
        }
        return result;
    }
}
