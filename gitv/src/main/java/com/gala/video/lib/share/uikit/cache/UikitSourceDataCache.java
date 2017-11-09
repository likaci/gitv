package com.gala.video.lib.share.uikit.cache;

import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.lib.framework.core.cache.CacheLoader;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.loader.data.AppStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UikitSourceDataCache {
    private static Map<String, Map<String, Integer>> sAllBannerPosInfoMap = new HashMap();
    private static final String sAppListPath = (sPath + "app.dem");
    private static final String sAppListPath_1 = (sPath + "app_1.dem");
    private static int sBannerAdId;
    private static List<CardInfoModel> sBannerAdList;
    private static CacheLoader sCacheLoader = new CacheLoader(3);
    private static final String sChannelListPath = (sPath + "channellist.dem");
    private static final String sChannelListPath_1 = (sPath + "channellist_1.dem");
    private static List<String> sHasAddBannerAdPosInfoList = new ArrayList();
    private static List<TabModel> sLocalTabModelList = new ArrayList();
    private static final String sPath = (AppRuntimeEnv.get().getApplicationContext().getFilesDir().getAbsolutePath() + "/home/");

    public static CardInfoModel readChannelList() {
        List<CardInfoModel> list = sCacheLoader.get(sChannelListPath);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return (CardInfoModel) list.get(0);
    }

    public static void writeChannelList(CardInfoModel card) {
        List<CardInfoModel> list = new ArrayList(1);
        list.add(card);
        sCacheLoader.save(list, false, sChannelListPath);
    }

    public static CardInfoModel readAppList() {
        List<CardInfoModel> list = sCacheLoader.get(sAppListPath);
        if (list == null || list.size() <= 0) {
            return null;
        }
        return (CardInfoModel) list.get(0);
    }

    public static void writeAppList(CardInfoModel card) {
        List<CardInfoModel> list = new ArrayList(1);
        list.add(card);
        sCacheLoader.save(list, false, sAppListPath);
    }

    public static void writeBannerAdId(int ad) {
        sBannerAdId = ad;
    }

    public static int readBannerAdId() {
        return sBannerAdId;
    }

    public static void addBannerAdPosFromTvServer(String adZonedId, Map<String, Integer> map) {
        sAllBannerPosInfoMap.put(adZonedId, map);
    }

    public static void addBannerAdPosFromAdServer(String adZonedId) {
        sHasAddBannerAdPosInfoList.add(adZonedId);
    }

    public static List<String> getBannerAdZonedIdListFromeAdServer() {
        return sHasAddBannerAdPosInfoList;
    }

    public static Map<String, Integer> getAllBannerMap(String resourceGroupId) {
        return (Map) sAllBannerPosInfoMap.get(resourceGroupId);
    }

    public static Map<String, Integer> getInvocationBannerMap(String resourceGroupId) {
        Map<String, Integer> map = (Map) sAllBannerPosInfoMap.get(resourceGroupId);
        Map<String, Integer> invocationMap = new HashMap();
        if (map == null) {
            return invocationMap;
        }
        for (Entry<String, Integer> entry : map.entrySet()) {
            invocationMap.put(entry.getKey(), entry.getValue());
        }
        if (invocationMap == null) {
            invocationMap = new HashMap();
        }
        for (String adZoneId : sHasAddBannerAdPosInfoList) {
            if (invocationMap.containsKey(adZoneId)) {
                invocationMap.remove(adZoneId);
            }
        }
        return invocationMap;
    }

    public static void writeBannerAdList(List<CardInfoModel> list) {
        sBannerAdList = list;
    }

    public static List<CardInfoModel> readBannerAdList() {
        return sBannerAdList;
    }

    public static void writeChannelDataList(List<Channel> list) {
        sCacheLoader.save(list, false, sChannelListPath_1);
    }

    public static List<Channel> readChannelDataList() {
        List<Channel> list = sCacheLoader.get(sChannelListPath_1);
        return (list == null || list.size() <= 0) ? null : list;
    }

    public static List<AppStore> readAppDataList() {
        List<AppStore> applist = sCacheLoader.get(sAppListPath_1);
        return (applist == null || applist.size() <= 0) ? null : applist;
    }

    public static void writeAppDataList(List<AppStore> applist) {
        sCacheLoader.save(applist, false, sAppListPath_1);
    }

    public static void setLocalTabModelList(List<TabModel> tabModelList) {
        sLocalTabModelList = tabModelList;
    }
}
