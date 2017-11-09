package com.gala.video.lib.share.uikit.loader.data;

import android.util.Log;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ResourceGroup;
import com.gala.tvapi.vrs.result.ApiResultGroupDetail;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.uikit.cache.UikitSourceDataCache;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.data.provider.NetCheckDataProvider;
import com.gala.video.lib.share.uikit.data.provider.VipProvider;
import com.gala.video.lib.share.uikit.loader.IUikitDataFetcherCallback;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class GroupDetailRequest {
    private static final String TAG = "GroupDetailRequest";
    private static Map<String, Map<Integer, String>> sTimeStampMap = new Hashtable(20);

    public static void callGroupDetail(String sourceId, int pageNo, int uikitEngineId, boolean isVip, boolean isNeedFetch, IUikitDataFetcherCallback callback) {
        if (isNeedFetch) {
            setUpdateTime(sourceId, pageNo, "");
        }
        Log.d(TAG, "request success-groupDetailPage-" + sourceId + "-" + pageNo + "-" + uikitEngineId);
        final String str = sourceId;
        final int i = pageNo;
        final int i2 = uikitEngineId;
        final IUikitDataFetcherCallback iUikitDataFetcherCallback = callback;
        final boolean z = isVip;
        VrsHelper.groupDetailPage.callSync(new IVrsCallback<ApiResultGroupDetail>() {
            public void onSuccess(ApiResultGroupDetail apiResultGroupDetail) {
                if (apiResultGroupDetail == null) {
                    return;
                }
                if (apiResultGroupDetail.latest) {
                    Log.d(GroupDetailRequest.TAG, "request success-groupDetailPage-" + str + "-" + i + "-" + i2 + "- not update");
                    iUikitDataFetcherCallback.onSuccess(null, apiResultGroupDetail.json);
                } else if (apiResultGroupDetail.data == null || apiResultGroupDetail.data.items == null || apiResultGroupDetail.data.items.size() <= 0) {
                    GroupDetailRequest.setUpdateTime(str, i, "");
                    iUikitDataFetcherCallback.onSuccess(null, "");
                } else {
                    String back = null;
                    if (apiResultGroupDetail.data.kvs != null) {
                        back = apiResultGroupDetail.data.kvs.tvBackgroundUrl;
                    }
                    List<CardInfoModel> cardInfoModelList = CardInfoBuildTool.buildCardList(apiResultGroupDetail, str, i, i2, z);
                    if (cardInfoModelList == null || cardInfoModelList.size() == 0) {
                        GroupDetailRequest.setUpdateTime(str, i, "");
                        iUikitDataFetcherCallback.onSuccess(null, back);
                        return;
                    }
                    CardInfoModel model;
                    GroupDetailRequest.setUpdateTime(str, i, apiResultGroupDetail.timestamp);
                    for (CardInfoModel model2 : cardInfoModelList) {
                        Log.d(GroupDetailRequest.TAG, " group request card info model-layout id-" + model2.cardLayoutId + "-title-" + model2.getTitle() + "-source id-" + str);
                    }
                    if (apiResultGroupDetail.data.kvs != null) {
                        String places = apiResultGroupDetail.data.kvs.banner_place_holder;
                        String ids = apiResultGroupDetail.data.kvs.banner_content_id;
                        GroupDetailRequest.parseBannerAd(str, ids, places);
                        if (!(places == null || places.isEmpty() || ids == null || ids.isEmpty())) {
                            String[] ad_places = places.split(",");
                            String[] ad_ids = ids.split(",");
                            int start_place = 0;
                            int end_place = 8;
                            if (i > 1) {
                                start_place = (i - 1) * 8;
                                end_place = i * 8;
                            }
                            if (ad_places.length == ad_ids.length) {
                                for (int i = 0; i < ad_places.length; i++) {
                                    int place = StringUtils.parseInt(ad_places[i]);
                                    if (place > start_place && place <= end_place) {
                                        int position = place % 9;
                                        model2 = new CardInfoModel();
                                        model2.isVIPTag = z;
                                        model2.cardLayoutId = 1023;
                                        CardInfoBuildTool.buildBannerCard(model2, null);
                                        model2.mSource = "banner";
                                        model2.setId(ad_ids[i]);
                                        model2.mCardId = ad_ids[i];
                                        model2.mPageNo = i;
                                        model2.mUikitEngineId = i2;
                                        if (position < 1) {
                                            cardInfoModelList.add(0, model2);
                                        } else if (position >= cardInfoModelList.size()) {
                                            cardInfoModelList.add(model2);
                                        } else {
                                            cardInfoModelList.add(position - 1, model2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    iUikitDataFetcherCallback.onSuccess(cardInfoModelList, back);
                    if (i == 1) {
                        List<ChannelLabel> channelLabels = ((ResourceGroup) apiResultGroupDetail.data.items.get(0)).items;
                        if (channelLabels != null && channelLabels.size() > 0) {
                            for (ChannelLabel label : channelLabels) {
                                if (label.getType() == ResourceType.VIDEO) {
                                    NetCheckDataProvider.getInstance().setData(label);
                                    if (z) {
                                        VipProvider.getInstance().add(label);
                                    }
                                } else {
                                    if (label.getType() == ResourceType.ALBUM && z) {
                                        VipProvider.getInstance().add(label);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            public void onException(ApiException e) {
                Log.e(GroupDetailRequest.TAG, "request failed-groupDetailPage-" + str + "-" + i + "-" + i2);
                iUikitDataFetcherCallback.onFailed();
                GroupDetailRequest.setUpdateTime(str, i, "");
            }
        }, sourceId, getUpdateTime(sourceId, pageNo), String.valueOf(pageNo));
    }

    private static void setUpdateTime(String sourceId, int pageNo, String time) {
        synchronized (sTimeStampMap) {
            Map<Integer, String> map = (Map) sTimeStampMap.get(sourceId);
            if (map == null) {
                Map<Integer, String> timeMap = new HashMap(1);
                timeMap.put(Integer.valueOf(pageNo), time);
                sTimeStampMap.put(sourceId, timeMap);
            } else {
                map.put(Integer.valueOf(pageNo), time);
            }
        }
    }

    private static String getUpdateTime(String sourceId, int pageNo) {
        String str;
        synchronized (sTimeStampMap) {
            Map<Integer, String> map = (Map) sTimeStampMap.get(sourceId);
            if (map == null) {
                str = "";
            } else {
                str = (String) map.get(Integer.valueOf(pageNo));
                if (str == null) {
                    str = "";
                }
            }
        }
        return str;
    }

    private static void parseBannerAd(String sourceId, String id, String place) {
        if (!StringUtils.isEmpty((CharSequence) id) && !StringUtils.isEmpty((CharSequence) place)) {
            String[] ids = id.split(",");
            String[] places = place.split(",");
            if (!StringUtils.isEmpty(ids) && !StringUtils.isEmpty(places) && ids.length == places.length) {
                Map<String, Integer> map = new HashMap(id.length());
                for (int i = 0; i < ids.length; i++) {
                    int position = StringUtils.parse(places[i], -1);
                    if (map.containsValue(Integer.valueOf(position))) {
                        LogUtils.m1568d(TAG, "parseBannerAd, ad position has already in map, position = " + position);
                    } else {
                        map.put(ids[i], Integer.valueOf(position));
                    }
                }
                UikitSourceDataCache.addBannerAdPosFromTvServer(sourceId, map);
                LogUtils.m1568d("xiaomi ", "parseBannerAd, group id : " + sourceId + " ad position info : " + map);
            }
        }
    }
}
