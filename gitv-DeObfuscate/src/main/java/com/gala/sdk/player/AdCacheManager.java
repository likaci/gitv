package com.gala.sdk.player;

public interface AdCacheManager {
    public static final String AD_CACHE_PATH_SUFFIX = "/qcache/data/ad_cache";
    public static final String SHARED_PREF_STARTUP_AD_CACHE_PATH = "startup_ad_cache_path";

    public static class AdCacheItem {
        private int mAdCacheType;
        private String mUrl;

        public AdCacheItem(String url, int type) {
            this.mUrl = url;
            this.mAdCacheType = type;
        }

        public int getAdCacheType() {
            return this.mAdCacheType;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("AdCacheItem@").append(Integer.toHexString(hashCode())).append("{");
            stringBuilder.append("mAdCacheType=").append(this.mAdCacheType);
            stringBuilder.append(", mUrl=").append(this.mUrl);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public static final class AdCacheType {
        public static final int ALL = 255;
        public static final int APP_STARTUP_AD = 1;
        public static final int MOVIE_AD = 2;
    }

    public interface IAdCacheStrategy {
        int getCacheAdType();

        String getCachePath();

        long getMaxCacheMBytes();

        long getMinFreeMBytes();
    }

    void addAdCacheStrategy(IAdCacheStrategy iAdCacheStrategy);

    void addTask(AdCacheItem adCacheItem);

    String getCacheFilePath(AdCacheItem adCacheItem);

    String getRootPath(int i);

    boolean isCached(AdCacheItem adCacheItem);

    void setCurrentRunningState(int i);
}
