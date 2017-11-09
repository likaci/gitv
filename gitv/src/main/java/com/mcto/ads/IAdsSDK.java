package com.mcto.ads;

import android.content.Context;
import com.mcto.ads.constants.AdCard;
import com.mcto.ads.constants.AdEvent;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public interface IAdsSDK {
    void flushCupidPingback();

    List<Map<String, String>> getAdCreativesByAdSource(String str);

    List<Map<String, String>> getAdCreativesByServerResponse(Context context, String str);

    String getAdDataWithAdSource(String str, long j, String str2, String str3, String str4);

    int getAdIdByAdZoneId(String str);

    List<CupidAd> getAdSchedules(int i);

    CupidAd getCupidAdByAdZoneIdAndTimeSlice(int i, String str, String str2);

    CupidAd getCupidAdByQipuId(int i, int i2);

    CupidAd getCupidAdByQipuIdAndAdZoneId(int i, int i2, String str);

    CupidAd getCupidAdByQipuIdAndAdZoneId(int i, String str);

    Map<String, Object> getCupidExtras();

    Map<String, Object> getCupidExtras(int i);

    String getCupidInteractionData(int i, int i2);

    String getDspSessionId(int i);

    String getFinalUrl();

    String getFinalUrl(int i);

    List<CupidFutureSlot> getFutureSlots();

    List<CupidFutureSlot> getFutureSlots(int i);

    List<CupidAdSlot> getSlotSchedules();

    List<CupidAdSlot> getSlotSchedules(int i);

    List<CupidAdSlot> getSlotsByType(int i);

    List<CupidAdSlot> getSlotsByType(int i, int i2);

    CupidAd getTargetedCupidAd(int i);

    void onAdCardShow(int i, AdCard adCard);

    void onAdCardShowWithProperties(int i, AdCard adCard, Map<String, Object> map);

    void onAdClicked(int i);

    void onAdClosed(int i);

    void onAdCompleted(int i);

    void onAdEvent(int i, AdEvent adEvent, Map<String, Object> map);

    void onAdFirstQuartile(int i);

    void onAdLike(int i, int i2);

    void onAdSecondQuartile(int i);

    void onAdSkipped(int i);

    void onAdStarted(int i);

    void onAdThirdQuartile(int i);

    void onAdUnlike(int i, int i2);

    int onHandleCupidInteractionData(String str);

    void onMobileFlowShow(int i);

    void onRequestMobileServer();

    void onRequestMobileServerFailed();

    int onRequestMobileServerSucceededWithAdData(String str, String str2, String str3) throws JSONException;

    int onRequestMobileServerSucceededWithAdData(String str, String str2, String str3, boolean z) throws JSONException;

    void setSdkStatus(Map<String, Object> map);

    void updateAdProgress(int i, int i2);

    void updateVVProgress(int i);
}
