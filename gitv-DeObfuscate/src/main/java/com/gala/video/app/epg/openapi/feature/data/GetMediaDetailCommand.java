package com.gala.video.app.epg.openapi.feature.data;

import android.content.Context;
import android.os.Bundle;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalAlbum;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalPlaylist;
import com.gala.video.lib.share.ifimpl.openplay.service.data.LocalVideo;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.ResultListHolder;
import com.gala.video.lib.share.ifimpl.openplay.service.tools.OpenApiNetwork;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Playlist;
import com.qiyi.tv.client.impl.Params.DataType;
import com.qiyi.tv.client.impl.Params.TargetType;
import java.util.ArrayList;

public class GetMediaDetailCommand extends BaseGetMediaInfoCommand {
    private static final String TAG = "GetMediaDetailCommand";

    private static class PlayListListener extends ResultListHolder<Media> implements IVrsCallback<ApiResultPlayListQipu> {
        public int mCode = 0;
        public LocalPlaylist mPlayList;

        public PlayListListener(LocalPlaylist media) {
            this.mPlayList = media;
        }

        public void onException(ApiException exception) {
            setNetworkValid(!OpenApiNetwork.isNetworkInvalid(exception));
            LogUtils.m1568d(GetMediaDetailCommand.TAG, "PlayListListener Exception Occurs");
            this.mCode = 7;
        }

        public void onSuccess(ApiResultPlayListQipu playList) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(GetMediaDetailCommand.TAG, "onSuccess(" + playList + ")" + ", mPlayIds = ");
            }
            setNetworkValid(true);
            ArrayList<Media> mMediaList = new ArrayList();
            if (playList != null) {
                if (ListUtils.isEmpty(playList.getAlbumList())) {
                    LogUtils.m1577w(GetMediaDetailCommand.TAG, "MyQipuListener.onSuccess() Didn't get data from server.");
                } else {
                    for (Album info : playList.getAlbumList()) {
                        Media media = OpenApiUtils.createSdkMedia(info);
                        if (media == null || !((media instanceof LocalAlbum) || (media instanceof LocalVideo))) {
                            LogUtils.m1577w(GetMediaDetailCommand.TAG, "MyQipuListener.onSuccess() cannot translate to sdk media!!!");
                        } else if (media instanceof LocalAlbum) {
                            mMediaList.add(((LocalAlbum) media).getSdkAlbum());
                        } else if (media instanceof LocalVideo) {
                            mMediaList.add(((LocalVideo) media).getSdkVideo());
                        }
                    }
                    this.mPlayList.setPicUrl(playList.getPlayListQipu().tvBackgroundUrl);
                }
            }
            LogUtils.m1577w(GetMediaDetailCommand.TAG, "The Num of playlist video : " + mMediaList.size());
            this.mPlayList.setPlaylistDetail(mMediaList);
        }
    }

    public GetMediaDetailCommand(Context context) {
        super(context, TargetType.TARGET_MEDIA_DETAIL, 20003, DataType.DATA_MEDIA);
        setNeedNetwork(true);
    }

    protected String getId(Media media) {
        return media.getId();
    }

    protected Bundle onProcess(Bundle inParams) {
        Bundle outParams = new Bundle();
        Media media = ServerParamsHelper.parseMedia(inParams);
        if (!(media instanceof Playlist)) {
            return super.onProcess(inParams);
        }
        PlayListListener mPlaylistListener = new PlayListListener((LocalPlaylist) media);
        VrsHelper.playListQipu.callSync(mPlaylistListener, media.getId(), "0");
        ServerParamsHelper.setResultCode(outParams, mPlaylistListener.mCode);
        ServerParamsHelper.setResultData(outParams, mPlaylistListener.mPlayList.getSdkPlaylist());
        return outParams;
    }
}
