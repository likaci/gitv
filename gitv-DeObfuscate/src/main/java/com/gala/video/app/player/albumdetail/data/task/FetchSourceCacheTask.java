package com.gala.video.app.player.albumdetail.data.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.player.albumdetail.data.cache.FetchSourceCacheManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.DataUtils;

public class FetchSourceCacheTask {
    private static final String TAG = "AlbumDetail/AlbumInfo/FetchSourceCacheTask";
    private static Object mLock = new Object();
    private static FetchSourceCacheTask mTask;
    private String mTvid;

    public interface IFetchSourceCacheTaskListener {
        void onFailed(ApiException apiException);

        void onSuccess(Album album);
    }

    public static synchronized FetchSourceCacheTask getInstance(String tvid) {
        FetchSourceCacheTask fetchSourceCacheTask;
        synchronized (FetchSourceCacheTask.class) {
            if (mTask == null || !StringUtils.equals(mTask.mTvid, tvid)) {
                mTask = new FetchSourceCacheTask(tvid);
            }
            fetchSourceCacheTask = mTask;
        }
        return fetchSourceCacheTask;
    }

    private FetchSourceCacheTask(String tvid) {
        this.mTvid = tvid;
    }

    public void getAlbum(final IFetchSourceCacheTaskListener listener) {
        synchronized (mLock) {
            Album album = FetchSourceCacheManager.getInstance().getCacheAlbum(this.mTvid);
            if (album != null) {
                listener.onSuccess(album);
            } else {
                TVApi.albumInfo.callSync(new IApiCallback<ApiResultAlbum>() {
                    public void onSuccess(ApiResultAlbum result) {
                        Album album = result.data;
                        FetchSourceCacheManager.getInstance().addToCache(album);
                        LogUtils.m1568d(FetchSourceCacheTask.TAG, "onSuccess: fetched info=" + DataUtils.albumInfoToString(album));
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(FetchSourceCacheTask.TAG, "end -onRun: album = " + album.isVipForAccount() + "||" + album.isSinglePay() + "||" + album.isCoupon());
                        }
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(FetchSourceCacheTask.TAG, "end -onRun: local album=" + album);
                        }
                        listener.onSuccess(album);
                    }

                    public void onException(ApiException e) {
                        LogUtils.m1568d(FetchSourceCacheTask.TAG, "onException: code=" + e.getCode() + ", msg=" + e.getMessage());
                        listener.onFailed(e);
                    }
                }, this.mTvid);
            }
        }
    }
}
