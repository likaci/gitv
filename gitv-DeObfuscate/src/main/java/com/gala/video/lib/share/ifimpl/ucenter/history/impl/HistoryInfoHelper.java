package com.gala.video.lib.share.ifimpl.ucenter.history.impl;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.PayMarkType;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HistoryInfoHelper {
    private static Comparator<HistoryInfo> ASCEND_COMPARATOR = new C17441();
    public static final int CLOUD_RELOAD_INTERVAL = 3600000;
    private static Comparator<HistoryInfo> DESCEND_COMPARATOR = new C17452();
    private static final int LONG_VIDEO_DURATION = 1200;
    public static final int MAX_MEMORY_COUNT = 300;
    public static final int MAX_SHOW_COUNT = 200;
    public static final int MSG_CACHE_CLEAR = 108;
    public static final int MSG_CACHE_CLEAR_ALL = 114;
    public static final int MSG_CACHE_DELETE = 107;
    public static final int MSG_CACHE_GET_HISTORY = 106;
    public static final int MSG_CACHE_PUT = 2;
    public static final int MSG_CACHE_SYNC = 111;
    public static final int MSG_CAHCE_CLEAR_LOGIN_DB = 110;
    public static final int MSG_CLOUD_CHECK_UPDATE = 109;
    public static final int MSG_CLOUD_CLEAR = 103;
    public static final int MSG_CLOUD_CLEAR_ALL = 115;
    public static final int MSG_CLOUD_DELETE = 102;
    public static final int MSG_CLOUD_PUT = 113;
    public static final int MSG_CLOUD_SYNC = 101;
    public static final int MSG_DB_RELOAD = 1;
    public static final int MSG_HISTORY_CLEAR_ALL = 3;
    public static final int MSG_HISTORY_DELETE_ONE_HISTORY = 7;
    public static final int MSG_HISTORY_LOAD_LIST = 4;
    public static final int MSG_HISTORY_LOAD_LIST_CLOUD = 6;
    public static final int MSG_HISTORY_LOAD_LIST_NOLOGIN = 8;
    public static final int MSG_HISTORY_MERGE = 1;
    public static final int MSG_HISTORY_UPLOAD = 2;
    public static final int MSG_MERGE = 112;
    public static final int NSG_HISTORY_CLEAR_FOR_USER = 5;
    public static final int RELOAD_DELAY = 5000;
    public static final int REQUEST_PAGE_SIZE = 100;
    public static final String TAG = "HistoryCacheManager";
    private static Context mContext = AppRuntimeEnv.get().getApplicationContext();

    static class C17441 implements Comparator<HistoryInfo> {
        C17441() {
        }

        public int compare(HistoryInfo r1, HistoryInfo r2) {
            if (r1.getAddTime() > r2.getAddTime()) {
                return 1;
            }
            if (r1.getAddTime() < r2.getAddTime()) {
                return -1;
            }
            return 0;
        }
    }

    static class C17452 implements Comparator<HistoryInfo> {
        C17452() {
        }

        public int compare(HistoryInfo r1, HistoryInfo r2) {
            if (r2.getAddTime() > r1.getAddTime()) {
                return 1;
            }
            if (r2.getAddTime() < r1.getAddTime()) {
                return -1;
            }
            return 0;
        }
    }

    public static boolean isLongVideo(Album album) {
        if (album == null) {
            return true;
        }
        long duration = StringUtils.parse(album.len, -1);
        if (duration < 1200 && album.qpId != null && album.qpId.equals(album.tvQid)) {
            return false;
        }
        if (album.qpId != null && album.qpId.endsWith("09")) {
            return false;
        }
        if (album.contentType == 1 || duration >= 1200) {
            return true;
        }
        return false;
    }

    public static boolean isLongVideo(HistoryInfo historyInfo) {
        if (historyInfo != null) {
            return isLongVideo(historyInfo.getAlbum());
        }
        return false;
    }

    public static String getAlbumKey(HistoryInfo info) {
        return AppClientUtils.getCookie(mContext) + "-" + info.getAlbum().qpId;
    }

    public static String getAlbumKey(String id) {
        return AppClientUtils.getCookie(mContext) + "-" + id;
    }

    public static String getTvKey(HistoryInfo info) {
        return AppClientUtils.getCookie(mContext) + "-" + info.getTvId();
    }

    public static String getTvKey(String id) {
        return AppClientUtils.getCookie(mContext) + "-" + id;
    }

    public static Map<String, HistoryInfo> getAlbumMap(List<HistoryInfo> list) {
        HashMap<String, HistoryInfo> map = new LinkedHashMap(list.size());
        for (HistoryInfo one : list) {
            map.put(getAlbumKey(one), one);
        }
        return map;
    }

    public static Map<String, HistoryInfo> getTvMap(List<HistoryInfo> list) {
        HashMap<String, HistoryInfo> map = new LinkedHashMap(list.size());
        for (HistoryInfo one : list) {
            map.put(getTvKey(one), one);
        }
        return map;
    }

    public static List<HistoryInfo> ascendReorder(List<HistoryInfo> list) {
        Collections.sort(list, ASCEND_COMPARATOR);
        return list;
    }

    public static List<HistoryInfo> sort(List<HistoryInfo> list) {
        Collections.sort(list, DESCEND_COMPARATOR);
        List<HistoryInfo> temp = list;
        if (list.size() > 200) {
            temp = list.subList(0, 200);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getLastest(" + list.size() + ") return " + temp.size());
        }
        return temp;
    }

    public static List<HistoryInfo> merge(List<HistoryInfo> list1, List<HistoryInfo> list2) {
        List<HistoryInfo> mergeList;
        if (list1 != null && list2 != null) {
            mergeList = new ArrayList(list1.size() + list2.size());
            mergeList.addAll(list1);
            mergeList.addAll(list2);
            List<HistoryInfo> resultList = ascendReorder(mergeList);
            Map<String, HistoryInfo> map = new HashMap();
            for (HistoryInfo item : resultList) {
                if (item.getAlbum().tvQid != null) {
                    map.put(item.getAlbum().tvQid, item);
                }
            }
            Map<String, HistoryInfo> mapQp = new HashMap(map.size());
            for (String key : map.keySet()) {
                if (map.get(key) != null) {
                    mapQp.put(((HistoryInfo) map.get(key)).getQpId(), map.get(key));
                }
            }
            resultList.clear();
            for (String key2 : mapQp.keySet()) {
                mergeList.add(mapQp.get(key2));
            }
            return mergeList;
        } else if (list1 == null && list2 != null) {
            mergeList = new ArrayList(list2.size());
            mergeList.addAll(list2);
            return mergeList;
        } else if (list2 != null || list1 == null) {
            return new ArrayList(1);
        } else {
            mergeList = new ArrayList(list1.size());
            mergeList.addAll(list1);
            return mergeList;
        }
    }

    public static int convertPayMarkForDb(PayMarkType type) {
        if (type == null) {
            return 0;
        }
        switch (type) {
            case NONE_MARK:
                return 0;
            case VIP_MARK:
                return 1;
            case PAY_ON_DEMAND_MARK:
                return 2;
            case COUPONS_ON_DEMAND_MARK:
                return 3;
            default:
                return 0;
        }
    }
}
