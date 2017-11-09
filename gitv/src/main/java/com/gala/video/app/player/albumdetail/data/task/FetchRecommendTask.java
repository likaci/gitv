package com.gala.video.app.player.albumdetail.data.task;

import com.gala.sdk.player.data.VrsChannelId;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public class FetchRecommendTask {
    private static final int PLAYLIST_SIZE = 18;
    private static final String TAG = "FetchRecommendTask";
    private String area;
    private int channelId;
    private String isFree;
    private AlbumInfo mAlbumInfo;
    private IFetchRecommendListener mListener;
    private String mTvQid;

    public interface IFetchRecommendListener {
        void onFailed(ApiException apiException);

        void onSuccess(List<Album> list);
    }

    private class MyCallback implements IVrsCallback<ApiResultRecommendListQipu> {
        private MyCallback() {
        }

        public void onException(ApiException e) {
            LogRecordUtils.loge(FetchRecommendTask.TAG, "MyCallback.onException()" + e);
            FetchRecommendTask.this.mListener.onFailed(e);
        }

        public void onSuccess(ApiResultRecommendListQipu result) {
            if (result != null) {
                LogRecordUtils.logd(FetchRecommendTask.TAG, "FetchRecommendTask.onSuccess: data=" + ListUtils.getCount(result.getAlbumList()));
                FetchRecommendTask.this.mListener.onSuccess(result.getAlbumList());
                return;
            }
            LogRecordUtils.loge(FetchRecommendTask.TAG, "result is null");
            FetchRecommendTask.this.mListener.onFailed(null);
        }
    }

    public FetchRecommendTask(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
        this.isFree = GetInterfaceTools.getIDynamicQDataProvider().isSupportVip() ? "0" : "1";
        this.mTvQid = this.mAlbumInfo.getAlbum().tvQid;
        this.channelId = this.mAlbumInfo.getAlbum().chnId;
        this.area = VrsChannelId.getArea(this.channelId);
    }

    public void setTaskListener(IFetchRecommendListener listener) {
        this.mListener = listener;
    }

    public void execute() {
        LogRecordUtils.logd(TAG, "onRun() tvId=" + this.mTvQid + ", area=" + this.area + ", isFree=" + this.isFree + PingbackConstants.CHANNEL_ID + this.channelId);
        VrsHelper.guessLikeAlbums.callSync(new MyCallback(), "1", String.valueOf(18), this.mTvQid, this.mTvQid, this.area, String.valueOf(this.channelId), this.isFree);
    }
}
