package com.gala.video.app.player.data;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DetailDataCacheManager {
    private static final String CACHE_PAGE_MODEL_NAME = "detail/detail_cache/detailPageModel.dem";
    private static final String CACHE_TIME_SP = "detailcachetime";
    private static final int DEFAULT_CHANNEL_ID = -1;
    private static final String DETAIL_DATA_CACHE = "detail/detail_cache/";
    private static final int MAX_COUNT_ALBUM_CACHE = 5;
    private static final long OVER_TIME = 3600000;
    private static final String TAG = "Detail/Data/DetailDataCacheManager";
    private static List<DetailBannerData> mBannerList = new CopyOnWriteArrayList();
    private static int mBannerResultId = 0;
    private static Album mCurrentDetailInfo;
    private static DetailDataCacheManager sInstance;
    private final List<Integer> channelIdlist = new ArrayList() {
    };
    private List<Album> mCachedAlbums = new ArrayList();
    private String mFilePath = "";
    private Map<Integer, String> mPageModelCacheMap = new HashMap();

    private DetailDataCacheManager() {
        this.channelIdlist.add(Integer.valueOf(1));
        this.channelIdlist.add(Integer.valueOf(2));
        this.channelIdlist.add(Integer.valueOf(6));
        this.channelIdlist.add(Integer.valueOf(7));
        this.channelIdlist.add(Integer.valueOf(3));
        this.channelIdlist.add(Integer.valueOf(15));
        this.channelIdlist.add(Integer.valueOf(4));
        this.mFilePath = AppRuntimeEnv.get().getApplicationContext().getFilesDir() + "/" + "detail/detail_cache/";
    }

    public static synchronized DetailDataCacheManager instance() {
        DetailDataCacheManager detailDataCacheManager;
        synchronized (DetailDataCacheManager.class) {
            if (sInstance == null) {
                sInstance = new DetailDataCacheManager();
            }
            detailDataCacheManager = sInstance;
        }
        return detailDataCacheManager;
    }

    public void clearBannerData() {
        mBannerList.clear();
        mBannerResultId = 0;
    }

    public void putBannerData(DetailBannerData data) {
        mBannerList.add(data);
    }

    public void putBannerResultId(int resultId) {
        mBannerResultId = resultId;
    }

    public int getBannerResultId() {
        return mBannerResultId;
    }

    public DetailBannerData getBannerData(int position) {
        LogRecordUtils.logd(TAG, "getBannerData" + position);
        for (DetailBannerData data : mBannerList) {
            if (data.getPosition() == position) {
                return data;
            }
        }
        return null;
    }

    public void clearCurrentDetailInfo() {
        mCurrentDetailInfo = null;
    }

    public void saveCurrentDetailInfo(Album data) {
        mCurrentDetailInfo = data;
    }

    public Album getCurrentDetailInfo() {
        return mCurrentDetailInfo;
    }

    public boolean isGroupDetailOverTime(String resid) {
        boolean overTime = false;
        CharSequence cacheTimeString = new AppPreference(ResourceUtil.getContext(), CACHE_TIME_SP).get(String.valueOf(resid));
        if (StringUtils.isEmpty(cacheTimeString)) {
            LogRecordUtils.logd(TAG, "isGroupDetailOverTime, cacheTimeString invalid");
        } else {
            long nowTime = DeviceUtils.getServerTimeMillis();
            long cacheTime = Long.valueOf(cacheTimeString).longValue();
            if (3600000 + cacheTime < nowTime) {
                overTime = true;
            }
            LogRecordUtils.logd(TAG, "isGroupDetailOverTime, cacheTime=" + cacheTime + ", nowTime" + nowTime + ", overTime =" + overTime);
        }
        return overTime;
    }

    public synchronized void putDetailGroupTime(String id) {
        LogRecordUtils.logd(TAG, "putDetailGroupTime in ,id = " + id);
        new AppPreference(ResourceUtil.getContext(), CACHE_TIME_SP).save(String.valueOf(id), String.valueOf(DeviceUtils.getServerTimeMillis()));
    }

    public void setAlbumCache(Album album) {
        Album find = getCachedAlbum(album);
        if (find != null) {
            this.mCachedAlbums.remove(find);
        }
        if (this.mCachedAlbums.size() >= 5) {
            this.mCachedAlbums = this.mCachedAlbums.subList(0, 4);
        }
        this.mCachedAlbums.add(album);
    }

    public Album getCachedAlbum(Album album) {
        if (album == null) {
            return null;
        }
        for (Album each : this.mCachedAlbums) {
            if (each.qpId.equals(album.qpId) && each.tvQid.equals(album.tvQid)) {
                return each;
            }
        }
        return null;
    }

    public Album getCachedAlbumByTVID(String tvid) {
        if (tvid == null) {
            return null;
        }
        for (Album each : this.mCachedAlbums) {
            if (tvid.equals(each.tvQid)) {
                return each;
            }
        }
        return null;
    }
}
