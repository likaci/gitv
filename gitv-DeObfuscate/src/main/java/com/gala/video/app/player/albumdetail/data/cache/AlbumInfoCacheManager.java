package com.gala.video.app.player.albumdetail.data.cache;

import android.util.Log;
import com.gala.tvapi.tv2.model.Album;

public class AlbumInfoCacheManager {
    private static final String TAG = "AlbumDetail/AlbumInfo/AlbumInfoCacheManager";
    private static Album sAlbum;
    private static AlbumInfoCacheManager sInstance;
    private long TIMEOUT = 300000;
    private long mLastTime = -1;
    private String mSourceUpdateTime = "";

    private AlbumInfoCacheManager() {
    }

    public static synchronized AlbumInfoCacheManager getInstance() {
        AlbumInfoCacheManager albumInfoCacheManager;
        synchronized (AlbumInfoCacheManager.class) {
            if (sInstance == null) {
                sInstance = new AlbumInfoCacheManager();
            }
            albumInfoCacheManager = sInstance;
        }
        return albumInfoCacheManager;
    }

    public Album getCacheAlbum(String albumId) {
        if (this.mLastTime != -1 && System.currentTimeMillis() - this.mLastTime < this.TIMEOUT) {
            return getCacheModelByAlbumId(albumId);
        }
        sAlbum = null;
        return null;
    }

    public synchronized void addToCache(Album model) {
        sAlbum = model;
        this.mLastTime = System.currentTimeMillis();
        this.mSourceUpdateTime = sAlbum.time;
    }

    public Album getCacheModelByAlbumId(String albumId) {
        if (sAlbum == null || !albumId.equals(sAlbum.qpId)) {
            return null;
        }
        return sAlbum;
    }

    public String getSourceUpdateTime() {
        Log.v(TAG, "sourceUpdateTime = getSourceUpdateTime" + this.mSourceUpdateTime);
        return this.mSourceUpdateTime;
    }
}
