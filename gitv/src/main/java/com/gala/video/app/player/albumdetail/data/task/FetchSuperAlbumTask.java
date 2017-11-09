package com.gala.video.app.player.albumdetail.data.task;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultPlayListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.util.ArrayList;
import java.util.List;

public class FetchSuperAlbumTask {
    private static final int MAX_COUNT = 24;
    private static final String TAG = "Detail/FetchSuperAlbumTask";
    private AlbumInfo mAlbumInfo;
    private IFetchSuperAlbumTaskListener mListener;
    private int superId = 0;

    public interface IFetchSuperAlbumTaskListener {
        void onFailed(ApiException apiException);

        void onSuccess(List<Album> list);
    }

    public FetchSuperAlbumTask(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
        this.superId = albumInfo.getAlbum().superId;
    }

    public void setTaskListener(IFetchSuperAlbumTaskListener listener) {
        this.mListener = listener;
    }

    public void execute() {
        LogRecordUtils.logd(TAG, ">> onRun: albumId=" + this.mAlbumInfo.getAlbumId() + this.mAlbumInfo.getAlbum().superId);
        if (this.superId == 0) {
            this.mListener.onFailed(null);
            return;
        }
        VrsHelper.playListQipu.call(new IVrsCallback<ApiResultPlayListQipu>() {
            public void onSuccess(ApiResultPlayListQipu result) {
                if (result == null) {
                    LogRecordUtils.loge(FetchSuperAlbumTask.TAG, "onException:result is null");
                    FetchSuperAlbumTask.this.mListener.onFailed(null);
                    return;
                }
                List resultList = result.getAlbumList();
                List list = new ArrayList();
                LogRecordUtils.logd(FetchSuperAlbumTask.TAG, ">>onSuccess=" + ListUtils.getCount(resultList));
                if (!ListUtils.isEmpty(resultList)) {
                    for (int i = 0; i < resultList.size(); i++) {
                        Album album = (Album) resultList.get(i);
                        if (album.getContentType() == ContentType.FEATURE_FILM) {
                            list.add(album);
                            if (list.size() == 24) {
                                break;
                            }
                        }
                    }
                }
                LogRecordUtils.logd(FetchSuperAlbumTask.TAG, ">>onSuccess filter=" + ListUtils.getCount(list));
                FetchSuperAlbumTask.this.mListener.onSuccess(list);
            }

            public void onException(ApiException e) {
                LogRecordUtils.loge(FetchSuperAlbumTask.TAG, "onException: code=" + e.getCode() + ", msg=" + e.getMessage());
                FetchSuperAlbumTask.this.mListener.onFailed(e);
            }
        }, Integer.toString(this.mAlbumInfo.getAlbum().superId), "0");
    }
}
