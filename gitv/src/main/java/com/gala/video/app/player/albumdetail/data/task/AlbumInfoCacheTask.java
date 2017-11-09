package com.gala.video.app.player.albumdetail.data.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.player.albumdetail.data.cache.AlbumInfoCacheManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.DataUtils;

public class AlbumInfoCacheTask {
    private static final String TAG = "AlbumDetail/AlbumInfo/AlbumInfoCacheTask";
    private static Object mLock = new Object();
    private static AlbumInfoCacheTask mTask;
    private String mAlbumId;

    public interface IAlbumInfoCacheTaskListener {
        void onFailed(ApiException apiException);

        void onSuccess(Album album);
    }

    public static synchronized AlbumInfoCacheTask getInstance(String albumId) {
        AlbumInfoCacheTask albumInfoCacheTask;
        synchronized (AlbumInfoCacheTask.class) {
            if (mTask == null || !StringUtils.equals(mTask.mAlbumId, albumId)) {
                mTask = new AlbumInfoCacheTask(albumId);
            }
            albumInfoCacheTask = mTask;
        }
        return albumInfoCacheTask;
    }

    private AlbumInfoCacheTask(String albumId) {
        this.mAlbumId = albumId;
    }

    public void getAlbum(final IAlbumInfoCacheTaskListener listener) {
        synchronized (mLock) {
            Album album = AlbumInfoCacheManager.getInstance().getCacheAlbum(this.mAlbumId);
            if (album != null) {
                listener.onSuccess(album);
            } else {
                LogUtils.d(TAG, "album is null,now requrest network");
                TVApi.albumInfo.callSync(new IApiCallback<ApiResultAlbum>() {
                    public void onSuccess(ApiResultAlbum result) {
                        Album album = result.data;
                        AlbumInfoCacheManager.getInstance().addToCache(album);
                        LogUtils.d(AlbumInfoCacheTask.TAG, "onSuccess: fetched info=" + DataUtils.albumInfoToString(album));
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(AlbumInfoCacheTask.TAG, "end -onRun: album = " + album.isVipForAccount() + "||" + album.isSinglePay() + "||" + album.isCoupon());
                        }
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(AlbumInfoCacheTask.TAG, "end -onRun: local album=" + album);
                        }
                        listener.onSuccess(album);
                    }

                    public void onException(ApiException e) {
                        LogUtils.d(AlbumInfoCacheTask.TAG, "onException: code=" + e.getCode() + ", msg=" + e.getMessage());
                        listener.onFailed(e);
                    }
                }, this.mAlbumId);
            }
        }
    }
}
