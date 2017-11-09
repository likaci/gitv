package com.gala.video.lib.share.uikit.data.provider;

import android.text.TextUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class DataRefreshPeriodism {
    private static final String CACHE_FILE = "home/v6.0/home_refresh_periodism.dem";
    private static final int DEFAULT_REFRESH_INTERVAL = 3600000;
    private static final int DETAIL_PERIODISM_AT_LEAST = 3;
    private static final int MINIMUM_INTERVAL = 120000;
    private static final String SPLIT_DOTE_MULTI = ",";
    private static final String SPLIT_DOTE_SINGEL = "-";
    private static final String TAG = "home/DataRefreshPeriodism";
    private static final DataRefreshPeriodism sInstance = new DataRefreshPeriodism();
    private HashMap<String, Integer> mRefreshCategory = new LinkedHashMap();
    private HashMap<Integer, List<Model>> mRefreshRules = new LinkedHashMap();

    private static class Model implements Serializable {
        private static final long serialVersionUID = 1;
        public int mEndingTime;
        public int mRefreshInterval;
        public int mStartingTime;

        private Model() {
            this.mStartingTime = 0;
            this.mEndingTime = 0;
            this.mRefreshInterval = 3600000;
        }

        public String toString() {
            return "model:(" + this.mStartingTime + ", " + this.mEndingTime + ", " + this.mRefreshInterval + ")";
        }
    }

    private DataRefreshPeriodism() {
    }

    public static DataRefreshPeriodism instance() {
        return sInstance;
    }

    public void setRules(int level, String rules) {
        if (rules != null) {
            String[] summaryPeriodism = rules.split(SPLIT_DOTE_MULTI);
            List<Model> models = new ArrayList();
            LogUtils.d(TAG, "summary periodism size = " + summaryPeriodism.length);
            for (String item : summaryPeriodism) {
                LogUtils.d(TAG, "summary periodism " + item);
                String[] detailPeriodism = item.split(SPLIT_DOTE_SINGEL);
                if (detailPeriodism != null && detailPeriodism.length == 3) {
                    Model model = new Model();
                    model.mStartingTime = StringUtils.parse(detailPeriodism[0], 0);
                    model.mEndingTime = StringUtils.parse(detailPeriodism[1], 0);
                    model.mRefreshInterval = StringUtils.parse(detailPeriodism[2], 3600000);
                    models.add(model);
                    LogUtils.d(TAG, "refresh level = " + level + " model = " + model);
                }
            }
            synchronized (this) {
                this.mRefreshRules.put(Integer.valueOf(level), models);
            }
        }
    }

    public synchronized void setCategory(int level, String resId) {
        if (TextUtils.isEmpty(resId)) {
            LogUtils.w(TAG, "group id is empty,level = " + level);
        } else {
            String[] groups = resId.split(SPLIT_DOTE_MULTI);
            if (!(groups == null || groups.length == 0)) {
                for (String item : groups) {
                    if (!TextUtils.isEmpty(item)) {
                        this.mRefreshCategory.put(item, Integer.valueOf(level));
                    }
                }
            }
        }
    }

    public synchronized int getRefreshLevel(String resId) {
        int level;
        if (this.mRefreshCategory.containsKey(resId)) {
            level = ((Integer) this.mRefreshCategory.get(resId)).intValue();
        } else {
            level = 2;
        }
        LogUtils.d(TAG, "getRefreshLevel (" + resId + ", " + level + ")");
        if (level < 1 || level > 3) {
            level = 2;
        }
        return level;
    }

    public synchronized void saveToNativeCache() {
        saveDataToLocalCache(this.mRefreshRules);
    }

    private static String formatTime(long currentTime) {
        String result = "";
        return new SimpleDateFormat("HHmm").format(new Date(currentTime));
    }

    public int getRefreshInterval(int level) {
        int interval = 3600000;
        int serverTime = StringUtils.parse(formatTime(DeviceUtils.getServerTimeMillis()), -1);
        if (serverTime == -1) {
            LogUtils.e(TAG, "get Refresh Interval error");
            return 3600000;
        }
        List rules = null;
        synchronized (this) {
            if (this.mRefreshRules == null || this.mRefreshRules.size() <= 0) {
                HashMap<Integer, List<Model>> cache = readFromNativeCache();
                if (cache != null) {
                    this.mRefreshRules = cache;
                    List<Model> rules2 = (List) this.mRefreshRules.get(Integer.valueOf(level));
                }
            } else {
                rules = (List) this.mRefreshRules.get(Integer.valueOf(level));
            }
        }
        if (!ListUtils.isEmpty((List) rules)) {
            for (Model rule : rules) {
                if (serverTime >= rule.mStartingTime && serverTime < rule.mEndingTime) {
                    interval = rule.mRefreshInterval * 1000;
                    if (interval <= 0) {
                        interval = 3600000;
                    } else if (interval < MINIMUM_INTERVAL) {
                        interval = MINIMUM_INTERVAL;
                    }
                }
            }
        }
        LogUtils.d(TAG, "current server time = " + serverTime + " level = " + level + " interval = " + interval);
        return interval;
    }

    private synchronized void saveDataToLocalCache(HashMap<Integer, List<Model>> refreshRules) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "save data refresh periodism : " + refreshRules);
        }
        try {
            SerializableUtils.write(refreshRules, "home/v6.0/home_refresh_periodism.dem");
        } catch (Exception e) {
            LogUtils.e(TAG, "save data request periodism exception :", e);
        }
    }

    private synchronized HashMap<Integer, List<Model>> readFromNativeCache() {
        HashMap<Integer, List<Model>> result;
        result = null;
        try {
            result = (HashMap) SerializableUtils.read("home/v6.0/home_refresh_periodism.dem");
            LogUtils.d(TAG, "read data request periodism from cache : " + result);
        } catch (Exception e) {
            LogUtils.e(TAG, "read data request periodism exception : " + e.getMessage());
        }
        return result;
    }
}
