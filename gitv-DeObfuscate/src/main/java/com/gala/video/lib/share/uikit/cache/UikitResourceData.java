package com.gala.video.lib.share.uikit.cache;

import java.util.Map;
import java.util.TreeMap;

public class UikitResourceData {
    private boolean mIsLock = false;
    private TreeMap<Integer, UikitCardListData> mPageCache = new TreeMap();

    public void setLock(boolean isLock) {
        this.mIsLock = isLock;
    }

    public boolean isLock() {
        return this.mIsLock;
    }

    public UikitCardListData getPageCache(int pageNo) {
        return (UikitCardListData) this.mPageCache.get(Integer.valueOf(pageNo));
    }

    public void addPageCache(int pageNo, UikitCardListData data) {
        this.mPageCache.put(Integer.valueOf(pageNo), data);
    }

    public Map<Integer, UikitCardListData> getPageCacheAll() {
        return this.mPageCache;
    }

    public int lastPageNo() {
        return ((Integer) this.mPageCache.lastEntry().getKey()).intValue();
    }

    public void removePage(int pageNo) {
        this.mPageCache.remove(Integer.valueOf(pageNo));
    }

    public int cardsCount() {
        if (this.mPageCache.size() <= 0) {
            return 0;
        }
        int count = 0;
        for (Integer intValue : this.mPageCache.keySet()) {
            UikitCardListData data = (UikitCardListData) this.mPageCache.get(Integer.valueOf(intValue.intValue()));
            if (!(data == null || data.getCardList() == null)) {
                count += data.getCardList().size();
            }
        }
        return count;
    }
}
