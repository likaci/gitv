package com.gala.video.app.player.albumdetail.data.task;

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

public class FetchPeripheralTask {
    private static final int PLAYLIST_SIZE = 60;
    private static final String TAG = "FetchPeripheralTask";
    private String area;
    private int channelId;
    private String isFree;
    private AlbumInfo mAlbumInfo;
    private IFetchPeripheralListener mListener;
    private String mTvQid;

    public interface IFetchPeripheralListener {
        void onFailed(ApiException apiException);

        void onSuccess(List<Album> list);
    }

    private class MyCallback implements IVrsCallback<ApiResultRecommendListQipu> {
        private MyCallback() {
        }

        public void onException(ApiException e) {
            LogRecordUtils.loge(FetchPeripheralTask.TAG, "MyCallback.onException()" + e);
            FetchPeripheralTask.this.mListener.onFailed(e);
        }

        public void onSuccess(ApiResultRecommendListQipu result) {
            if (result != null) {
                LogRecordUtils.logd(FetchPeripheralTask.TAG, "FetchPeripheralTask.onSuccess: data=" + ListUtils.getCount(result.getAlbumList()));
                FetchPeripheralTask.this.mListener.onSuccess(result.getAlbumList());
                return;
            }
            LogRecordUtils.loge(FetchPeripheralTask.TAG, "result is null");
            FetchPeripheralTask.this.mListener.onFailed(null);
        }
    }

    public FetchPeripheralTask(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
        this.isFree = GetInterfaceTools.getIDynamicQDataProvider().isSupportVip() ? "0" : "1";
        this.mTvQid = this.mAlbumInfo.getAlbum().tvQid;
        this.channelId = this.mAlbumInfo.getAlbum().chnId;
        this.area = "t_zebra";
    }

    public void setTaskListener(IFetchPeripheralListener listener) {
        this.mListener = listener;
    }

    public void execute() {
        LogRecordUtils.logd(TAG, "onRun() tvId=" + this.mTvQid + ", area=" + this.area + ", isFree=" + this.isFree + PingbackConstants.CHANNEL_ID + this.channelId);
        VrsHelper.guessLikeAlbums.callSync(new MyCallback(), "1", String.valueOf(60), this.mTvQid, this.mTvQid, this.area, String.valueOf(this.channelId), this.isFree);
    }
}
