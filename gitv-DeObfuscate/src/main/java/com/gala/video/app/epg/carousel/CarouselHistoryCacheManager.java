package com.gala.video.app.epg.carousel;

import android.util.Log;
import com.gala.video.app.epg.home.data.model.ChannelModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.CarouselChannelProvider;
import com.gala.video.lib.framework.core.cache.MemoryCache;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.CarouselHistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.ICarouselHistoryCacheManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class CarouselHistoryCacheManager extends Wrapper {
    private static final String CAROUSEL_HISTORY_INFO_DIR = "home/home_cache/carousel_history_info.dem";
    private static final String HOME_DATA_CACHE = "home/home_cache/";
    private static final int MAX_MEMORY_COUNT = 1000;
    private static final int MAX_SHOW_COUNT = 10;
    private static final String TAG = "CarouseHistoryCacheManager";
    private static final int VALID_CHANNEL_TIME = 30;
    private static CarouselHistoryCacheManager sInstance;
    private List<CarouselHistoryInfo> mCacheList = new ArrayList();
    private List<ChannelModel> mChannelInfoList = new ArrayList();
    private MemoryCache<CarouselHistoryInfo> mMemoryCache = new MemoryCache(1000);

    public void put(CarouselHistoryInfo info) {
        Log.d(TAG, "CarouseHistoryCacheManager info = " + info);
        if (info != null && this.mMemoryCache != null) {
            long allPlayTime = 0;
            CarouselHistoryInfo temp = (CarouselHistoryInfo) this.mMemoryCache.get(info.getCarouselChannelId());
            if (temp != null) {
                allPlayTime = temp.getPlayAllTime();
                updateData(info, temp);
                deleteCacheListObject(temp);
            } else {
                temp = info;
            }
            sort(temp);
            this.mMemoryCache.clear();
            this.mMemoryCache.update(getMap(this.mCacheList));
            Log.d(TAG, "all time = " + temp.getPlayAllTime());
            if (temp.getPlayAllTime() > 30 && allPlayTime < 30) {
                int validChannelCount = 0;
                for (int i = 0; i < this.mCacheList.size(); i++) {
                    if (((CarouselHistoryInfo) this.mCacheList.get(i)).getPlayAllTime() > 30) {
                        validChannelCount++;
                    }
                }
                Log.d(TAG, "valid channel count =  " + validChannelCount);
                HomePingbackFactory.instance().createPingback(CommonPingback.CAROUSEL_VALID_CHANNEL_GENERATE_PINGBACK).addItem("c2", temp.getCarouselChannelId()).addItem("count", validChannelCount + "").addItem(Keys.f2035T, "11").addItem("ct", "160830_effectchl").setOthersNull().post();
            }
            try {
                SerializableUtils.write(this.mCacheList, CAROUSEL_HISTORY_INFO_DIR);
            } catch (IOException e) {
                Log.d(TAG, "write data to local exception = ", e);
            }
        }
    }

    private void deleteCacheListObject(CarouselHistoryInfo info) {
        if (this.mCacheList != null) {
            for (int i = 0; i < this.mCacheList.size(); i++) {
                if (((CarouselHistoryInfo) this.mCacheList.get(i)).getCarouselChannelId().equals(info.getCarouselChannelId())) {
                    this.mCacheList.remove(info);
                    return;
                }
            }
        }
    }

    private void sort(CarouselHistoryInfo info) {
        if (this.mCacheList != null) {
            int size = this.mCacheList.size();
            if (size == 0) {
                this.mCacheList.add(info);
                return;
            }
            CarouselHistoryInfo last = (CarouselHistoryInfo) this.mCacheList.get(size - 1);
            if (info.getPreference() < last.getPreference() || (info.getPreference() == last.getPreference() && info.getCarouselChannelEndTime() <= last.getCarouselChannelEndTime())) {
                this.mCacheList.add(info);
                return;
            }
            int i = 0;
            while (i < size) {
                if (((CarouselHistoryInfo) this.mCacheList.get(i)).getPreference() < info.getPreference()) {
                    this.mCacheList.add(i, info);
                    return;
                } else if (((CarouselHistoryInfo) this.mCacheList.get(i)).getPreference() != info.getPreference() || ((CarouselHistoryInfo) this.mCacheList.get(i)).getCarouselChannelEndTime() >= info.getCarouselChannelEndTime()) {
                    i++;
                } else {
                    this.mCacheList.add(i, info);
                    return;
                }
            }
        }
    }

    private void updateData(CarouselHistoryInfo info, CarouselHistoryInfo temp) {
        temp.setStartTime(info.getCarouselChannelStartTime());
        temp.setEndTime(info.getCarouselChannelEndTime());
    }

    public synchronized List<CarouselHistoryInfo> getCarouselHistoryList() {
        List<CarouselHistoryInfo> result;
        int i;
        List<CarouselHistoryInfo> infoList = new ArrayList();
        if (this.mCacheList != null) {
            for (i = 0; i < this.mCacheList.size(); i++) {
                if (isOnLineChannel((CarouselHistoryInfo) this.mCacheList.get(i))) {
                    Log.d(TAG, "the channel is online");
                    infoList.add(this.mCacheList.get(i));
                }
            }
        }
        if (infoList.size() >= 10) {
            infoList = infoList.subList(0, 10);
        }
        result = new ArrayList();
        for (i = 0; i < infoList.size(); i++) {
            if (((CarouselHistoryInfo) infoList.get(i)).getPreference() > 0) {
                result.add(infoList.get(i));
            }
        }
        Log.d(TAG, "result = " + result);
        return result;
    }

    public synchronized void loadLocalToMemory() {
        Log.d(TAG, "load local data to memory");
        if (this.mCacheList != null) {
            this.mCacheList.clear();
        }
        if (this.mMemoryCache != null) {
            this.mMemoryCache.clear();
            try {
                this.mCacheList = (List) SerializableUtils.read(CAROUSEL_HISTORY_INFO_DIR);
                this.mMemoryCache.update(getMap(this.mCacheList));
                Log.d(TAG, "cache list = " + this.mCacheList);
            } catch (Exception e) {
                Log.d(TAG, "load data from local to memory exception = ", e);
            }
        }
    }

    private List<ChannelModel> getCarouselChannelList() {
        this.mChannelInfoList = CarouselChannelProvider.getInstance().getChannelList();
        return this.mChannelInfoList;
    }

    private boolean isOnLineChannel(CarouselHistoryInfo info) {
        if (info == null) {
            return false;
        }
        getCarouselChannelList();
        if (this.mChannelInfoList == null) {
            return false;
        }
        for (int i = 0; i < this.mChannelInfoList.size(); i++) {
            if (info.getCarouselChannelId().equals(((ChannelModel) this.mChannelInfoList.get(i)).getId())) {
                return true;
            }
        }
        return false;
    }

    private Map<String, CarouselHistoryInfo> getMap(List<CarouselHistoryInfo> list) {
        if (ListUtils.isEmpty((List) list)) {
            return null;
        }
        Map<String, CarouselHistoryInfo> map = new LinkedHashMap(list.size());
        for (CarouselHistoryInfo one : list) {
            map.put(one.getCarouselChannelId(), one);
        }
        return map;
    }
}
