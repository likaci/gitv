package com.gala.video.app.player.albumdetail.data.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultTrailersList;
import com.gala.tvapi.type.ContentType;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FetchTrailerTask {
    private static final String TAG = "FetchTrailerTask";
    private static final int mPageSize = 60;
    private AlbumInfo mAlbumInfo;
    private List<Album> mFullEpisodeList = new CopyOnWriteArrayList();
    private IFetchTrailerTaskListener mListener;
    private int mPage = 1;
    private int mTotal = 0;
    private String mTvId = "";

    public interface IFetchTrailerTaskListener {
        void onFailed(ApiException apiException);

        void onSuccess(List<Album> list);
    }

    private class FirstCallback implements IApiCallback<ApiResultTrailersList> {
        private FirstCallback() {
        }

        public void onException(ApiException arg0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(FetchTrailerTask.TAG, "result onException");
            }
            FetchTrailerTask.this.mListener.onFailed(arg0);
        }

        public void onSuccess(ApiResultTrailersList result) {
            if (result == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(FetchTrailerTask.TAG, "result is null");
                }
                FetchTrailerTask.this.mListener.onFailed(null);
            } else if (ListUtils.isEmpty(result.data)) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(FetchTrailerTask.TAG, "episode data is empty");
                }
                FetchTrailerTask.this.mListener.onFailed(null);
            } else {
                List<Album> trailers = result.getAlbumList();
                if (ListUtils.getCount((List) trailers) > 0) {
                    for (Album album : trailers) {
                        if (album.getContentType() != ContentType.FEATURE_FILM) {
                            album.qpId = FetchTrailerTask.this.mTvId;
                            FetchTrailerTask.this.mFullEpisodeList.add(album);
                        }
                    }
                }
                FetchTrailerTask.this.mListener.onSuccess(FetchTrailerTask.this.mFullEpisodeList);
                int firstCount = ListUtils.getCount(result.getAlbumList());
                FetchTrailerTask.this.mTotal = result.total;
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(FetchTrailerTask.TAG, "episode data is ready firstCount" + firstCount + " total" + FetchTrailerTask.this.mTotal);
                }
            }
        }
    }

    private class SecondCallback implements IApiCallback<ApiResultTrailersList> {
        private SecondCallback() {
        }

        public void onException(ApiException arg0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(FetchTrailerTask.TAG, "result onException");
            }
            FetchTrailerTask.this.mListener.onFailed(arg0);
        }

        public void onSuccess(ApiResultTrailersList result) {
            if (result == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(FetchTrailerTask.TAG, "result is null");
                }
            } else if (!ListUtils.isEmpty(result.data)) {
                FetchTrailerTask.this.mFullEpisodeList.addAll(result.getAlbumList());
                FetchTrailerTask.this.mListener.onSuccess(FetchTrailerTask.this.mFullEpisodeList);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int secondCount = ListUtils.getCount(result.getAlbumList());
                int sumCount = ListUtils.getCount(FetchTrailerTask.this.mFullEpisodeList);
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(FetchTrailerTask.TAG, "second data is ready mPage is " + FetchTrailerTask.this.mPage + " msecondCount = " + secondCount + " sumCount" + sumCount);
                }
                if (sumCount < FetchTrailerTask.this.mTotal && secondCount != 0) {
                    FetchTrailerTask.this.mPage = FetchTrailerTask.this.mPage + 1;
                    TVApi.episodeVideo.call(new SecondCallback(), FetchTrailerTask.this.mTvId, String.valueOf(FetchTrailerTask.this.mPage), String.valueOf(60));
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(FetchTrailerTask.TAG, "episode data is end");
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1571e(FetchTrailerTask.TAG, "episode data is empty");
            }
        }
    }

    public FetchTrailerTask(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
    }

    public void setTaskListener(IFetchTrailerTaskListener listener) {
        this.mListener = listener;
    }

    public void execute() {
        this.mTvId = this.mAlbumInfo.getTvId();
        LogRecordUtils.logd(TAG, ">> onRun, mTvQid=" + this.mTvId);
        TVApi.episodeVideo.call(new FirstCallback(), this.mTvId, String.valueOf(this.mPage), String.valueOf(60));
    }
}
