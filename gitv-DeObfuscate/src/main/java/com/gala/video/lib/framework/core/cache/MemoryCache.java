package com.gala.video.lib.framework.core.cache;

import com.gala.video.lib.framework.core.cache.ICache.Visitor;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryCache<T> implements ICache<T> {
    private static final String TAG = "MemoryCache";
    private final int mCapacity;
    private final HashMap<String, T> mLruMap = new LinkedHashMap<String, T>(16, 0.75f, true) {
        protected boolean removeEldestEntry(Entry<String, T> entry) {
            return size() > MemoryCache.this.mCapacity;
        }
    };

    public MemoryCache(int capacity) {
        this.mCapacity = capacity;
    }

    public synchronized void put(String key, T value) {
        this.mLruMap.put(key, value);
    }

    public synchronized T get(String key) {
        return this.mLruMap.get(key);
    }

    public synchronized void remove(String key) {
        T value = this.mLruMap.remove(key);
    }

    public synchronized void update(Map<String, T> map) {
        if (map != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "update(" + map + ")");
            }
            this.mLruMap.clear();
            for (String key : map.keySet()) {
                this.mLruMap.put(key, map.get(key));
            }
        }
    }

    public synchronized List<T> obtainAll() {
        List<T> results;
        results = new ArrayList();
        HashMap<String, T> temp = new LinkedHashMap(this.mLruMap);
        for (String key : temp.keySet()) {
            results.add(temp.get(key));
        }
        return results;
    }

    public synchronized void clear() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clear()");
        }
        this.mLruMap.clear();
    }

    public synchronized void accept(Visitor<T> visitor) {
        HashMap<String, T> temp = new HashMap(this.mLruMap);
        for (String key : temp.keySet()) {
            visitor.visit(key, temp.get(key));
        }
    }
}
