package com.gala.video.lib.framework.core.cache;

import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import java.util.ArrayList;
import java.util.List;

public class CacheLoader<T> implements IDataCache<T> {
    private static final String TAG = "Cache";
    private List<T> mCacheList = new ArrayList();
    private int mCacheType;

    public CacheLoader(int type) {
        this.mCacheType = type;
    }

    public synchronized void save(List<T> data, boolean append, String path) {
        if (data != null) {
            if (data.size() > 0) {
                if (this.mCacheType == 3) {
                    writeDataToCache(data, path);
                } else if (this.mCacheType == 0) {
                    if (!append) {
                        this.mCacheList.clear();
                    }
                    this.mCacheList.addAll(data);
                } else {
                    if (!append) {
                        this.mCacheList.clear();
                    }
                    this.mCacheList.addAll(data);
                    writeDataToCache(this.mCacheList, path);
                }
            }
        }
    }

    public synchronized List<T> get(String path) {
        List<T> readDataFromCache;
        if (this.mCacheType == 3) {
            readDataFromCache = readDataFromCache(path);
        } else if (this.mCacheType == 0) {
            readDataFromCache = this.mCacheList;
        } else {
            if (ListUtils.isEmpty(this.mCacheList)) {
                this.mCacheList.addAll(readDataFromCache(path));
            }
            readDataFromCache = this.mCacheList;
        }
        return readDataFromCache;
    }

    private List<T> readDataFromCache(String path) {
        List<T> list = null;
        try {
            list = (List) SerializableUtils.read(path);
            LogUtils.m1571e(TAG, "read from cache successful,path = " + path);
            return list;
        } catch (Exception e) {
            LogUtils.m1572e(TAG, "read from cache failed", e);
            return list;
        }
    }

    private void writeDataToCache(List<T> datas, String path) {
        try {
            SerializableUtils.write(datas, path);
            LogUtils.m1568d(TAG, "write to cache successful,path = " + path);
        } catch (Exception e) {
            LogUtils.m1571e(TAG, "write to cache  failed");
        }
    }
}
