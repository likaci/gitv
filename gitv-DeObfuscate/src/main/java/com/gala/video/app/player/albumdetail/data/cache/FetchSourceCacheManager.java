package com.gala.video.app.player.albumdetail.data.cache;

import com.gala.tvapi.tv2.model.Album;

public class FetchSourceCacheManager {
    private static final String TAG = "AlbumDetail/AlbumInfo/FetchSourceCacheManager";
    private static Album sAlbum;
    private static FetchSourceCacheManager sInstance;
    private long TIMEOUT = 300000;
    private long mLastTime = -1;

    private FetchSourceCacheManager() {
    }

    public static synchronized FetchSourceCacheManager getInstance() {
        FetchSourceCacheManager fetchSourceCacheManager;
        synchronized (FetchSourceCacheManager.class) {
            if (sInstance == null) {
                sInstance = new FetchSourceCacheManager();
            }
            fetchSourceCacheManager = sInstance;
        }
        return fetchSourceCacheManager;
    }

    public Album getCacheAlbum(String tvId) {
        if (this.mLastTime != -1 && System.currentTimeMillis() - this.mLastTime < this.TIMEOUT) {
            return getCacheModelByTvId(tvId);
        }
        sAlbum = null;
        return null;
    }

    public synchronized void addToCache(Album model) {
        sAlbum = model;
        this.mLastTime = System.currentTimeMillis();
    }

    public Album getCacheModelByTvId(String tvId) {
        if (sAlbum == null || !tvId.equals(sAlbum.tvQid)) {
            return null;
        }
        return sAlbum;
    }
}
