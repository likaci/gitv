package com.mcto.ads;

import android.net.Uri;
import com.mcto.ads.constants.ClickThroughType;
import com.mcto.ads.constants.DeliverType;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.mcto.ads.internal.model.AdInfo;
import com.xcrash.crashreporter.utils.CrashConst;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CupidAd {
    private static final String APP_QIPU_ID_KEY = "qipuid";
    public static final String CREATIVE_TYPE_APPSTORE = "appstore";
    public static final String CREATIVE_TYPE_BANNER_PIC = "banner_pic";
    public static final String CREATIVE_TYPE_COMMON_OVERLAY = "common_overlay";
    public static final String CREATIVE_TYPE_COMMON_PAUSE = "common_pause";
    public static final String CREATIVE_TYPE_CORNER = "corner";
    public static final String CREATIVE_TYPE_EXIT = "exit";
    public static final String CREATIVE_TYPE_IMAGE = "image";
    public static final String CREATIVE_TYPE_IMAGE_START_SCREEN = "image_start_screen";
    public static final String CREATIVE_TYPE_MOBILE_FLOW = "mobile_flow";
    public static final String CREATIVE_TYPE_MOBILE_FLOW_PAIR = "mobile_flow_pair";
    public static final String CREATIVE_TYPE_MOBILE_GIANT_SCREEN = "mobile_giant_screen";
    public static final String CREATIVE_TYPE_MOVIE_TICKET = "movieticket";
    public static final String CREATIVE_TYPE_NATIVE_VIDEO = "native_video";
    public static final String CREATIVE_TYPE_PAUSE = "pause";
    public static final String CREATIVE_TYPE_QISHOW = "qishow";
    public static final String CREATIVE_TYPE_READ = "read";
    public static final String CREATIVE_TYPE_RELATED_APP = "relatedapp";
    public static final String CREATIVE_TYPE_SCREENSAVER = "screensaver";
    public static final String CREATIVE_TYPE_SEARCH_BIDDING = "search_bidding";
    public static final String CREATIVE_TYPE_VIDEO_COMPOUND_M3U8 = "video_compound_m3u8";
    public static final String CREATIVE_TYPE_VIDEO_M3U8 = "video_m3u8";
    public static final String CREATIVE_TYPE_VIDEO_SLOT_M3U8 = "video_slot_m3u8";
    public static final String CREATIVE_TYPE_VIDEO_SPLIT = "video_split";
    public static final String TEMPLATE_TYPE_COMMON_PAUSE = "common_pause";
    public static final String TEMPLATE_TYPE_GPAD_INTERSTITIALS = "gpad_interstitials";
    public static final String TEMPLATE_TYPE_GPHONE_INTERSTITIALS = "gphone_interstitials";
    public static final String TEMPLATE_TYPE_GTV_INTERSTITIALS = "gtv_interstitials";
    public static final String TEMPLATE_TYPE_HEADLINE_NATIVE_IMAGE = "headline_native_image";
    public static final String TEMPLATE_TYPE_MOBILE_FLOW = "mobile_flow";
    public static final String TEMPLATE_TYPE_MOBILE_FLOW_PAIR = "mobile_flow_pair";
    public static final String TEMPLATE_TYPE_MOBILE_FOCUS = "mobile_focus";
    public static final String TEMPLATE_TYPE_MOBILE_PAUSE = "mobile_pause";
    public static final String TEMPLATE_TYPE_NATIVE_MULTI_IMAGE = "native_multi_image";
    public static final String TEMPLATE_TYPE_NATIVE_VIDEO = "native_video";
    public static final String TEMPLATE_TYPE_TV_BANNER = "tv_banner";
    public static final String TEMPLATE_TYPIE_NATIVE_IMAGE = "native_image";
    private Map<String, Object> adExtras;
    private int adId;
    private ClickThroughType clickThroughType;
    private String clickThroughUrl;
    private Map<String, Object> creativeObject;
    private String creativeType;
    private String creativeUrl;
    private DeliverType deliverType;
    private int dspType;
    private int duration;
    private int offsetInSlot;
    private int skippableTime;
    private String templateType;
    private String timeSlice;
    private String tunnelData;

    public CupidAd(AdInfo adInfo, String tunnelData, CupidContext cupidContext) {
        if (adInfo != null) {
            this.adId = adInfo.getAdId();
            this.offsetInSlot = adInfo.getOffsetInSlot();
            this.duration = adInfo.getDuration();
            this.dspType = adInfo.getDspType();
            this.deliverType = adInfo.getDeliverType();
            this.skippableTime = adInfo.getSkippableTime();
            this.clickThroughType = ClickThroughType.build(adInfo.getClickThroughType());
            this.timeSlice = adInfo.getTimePosition();
            this.clickThroughUrl = adInfo.getClickThroughUrl();
            this.creativeType = adInfo.getCreativeType();
            this.templateType = adInfo.getTemplateType();
            this.creativeObject = adInfo.getCreativeObject();
            this.creativeUrl = adInfo.getCreativeUrl();
            this.adExtras = adInfo.getAdExtras();
            this.tunnelData = tunnelData;
            if (cupidContext != null) {
                String debugTime = String.valueOf(cupidContext.getDebugTime());
                if (debugTime != null) {
                    this.clickThroughUrl = this.clickThroughUrl.replace(JsonBundleConstants.LP_DEBUG_TIME, debugTime);
                }
                if (this.creativeObject != null && this.creativeObject.containsKey(JsonBundleConstants.DETAIL_PAGE)) {
                    Object detailPageObj = this.creativeObject.get(JsonBundleConstants.DETAIL_PAGE);
                    if (detailPageObj != null) {
                        String detailPage = String.valueOf(detailPageObj);
                        if (!detailPage.equals("null")) {
                            this.creativeObject.put(JsonBundleConstants.DETAIL_PAGE, detailPage.replace(JsonBundleConstants.LP_DEBUG_TIME, debugTime));
                        }
                    }
                }
            }
        }
    }

    public int getAdId() {
        return this.adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }

    public int getOffsetInSlot() {
        return this.offsetInSlot;
    }

    public int getoffsetInSlot() {
        return this.offsetInSlot;
    }

    public void setOffsetInSlot(int offsetInSlot) {
        this.offsetInSlot = offsetInSlot;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDspType() {
        return this.dspType;
    }

    public void setDspType(int dspType) {
        this.dspType = dspType;
    }

    public DeliverType getDeliverType() {
        return this.deliverType;
    }

    public void setDeliverType(DeliverType deliverType) {
        this.deliverType = deliverType;
    }

    public int getSkippableTime() {
        return this.skippableTime;
    }

    public void setSkippableTime(int skippableTime) {
        this.skippableTime = skippableTime;
    }

    public ClickThroughType getClickThroughType() {
        return this.clickThroughType;
    }

    public void setClickThroughType(ClickThroughType clickThroughType) {
        this.clickThroughType = clickThroughType;
    }

    public String getClickThroughUrl() {
        if (this.clickThroughUrl == null) {
            return "";
        }
        return this.clickThroughUrl.replace(JsonBundleConstants.PL_CLICK_TIME, String.valueOf(new Date().getTime()));
    }

    public String getTimeSlice() {
        return this.timeSlice;
    }

    public String getDetailPageUrl() {
        String detailPage = "";
        if (this.creativeObject == null) {
            return detailPage;
        }
        detailPage = (String) this.creativeObject.get(JsonBundleConstants.DETAIL_PAGE);
        if (detailPage != null) {
            detailPage = detailPage.replace(JsonBundleConstants.PL_CLICK_TIME, String.valueOf(new Date().getTime()));
        }
        return detailPage;
    }

    public void setClickThroughUrl(String clickThroughUrl) {
        this.clickThroughUrl = clickThroughUrl;
    }

    public void setCreativeType(String creativeType) {
        this.creativeType = creativeType;
    }

    public String getCreativeType() {
        return this.creativeType;
    }

    public String getTemplateType() {
        return this.templateType;
    }

    public void setCreativeObject(Map<String, Object> creativeObject) {
        if (creativeObject != null) {
            this.creativeObject = new HashMap(creativeObject);
        }
    }

    public Map<String, Object> getCreativeObject() {
        if (this.creativeObject == null) {
            return new HashMap();
        }
        return this.creativeObject;
    }

    public void setCreativeUrl(String creativeUrl) {
        this.creativeUrl = creativeUrl;
    }

    public String getCreativeUrl() {
        if (this.creativeUrl == null) {
            return "";
        }
        return this.creativeUrl;
    }

    public void setAdExtras(Map<String, Object> adExtras) {
        if (adExtras != null) {
            this.adExtras = new HashMap(adExtras);
        }
    }

    public Map<String, Object> getAdExtras() {
        if (this.adExtras == null) {
            return new HashMap();
        }
        return this.adExtras;
    }

    public String getTunnelData() {
        return this.tunnelData;
    }

    public void setTunnelData(String tunnelData) {
        this.tunnelData = tunnelData;
    }

    public String getAppQipuId() {
        if (this.clickThroughType != ClickThroughType.GAMECENTER && this.clickThroughType != ClickThroughType.MOVIECENTER) {
            return "";
        }
        String qipuId = null;
        if (this.clickThroughUrl != null) {
            qipuId = Uri.parse(this.clickThroughUrl).getQueryParameter("qipuid");
        }
        return qipuId == null ? "" : qipuId;
    }

    public Map<String, String> resolveClickUri(String uriStr) {
        Map<String, String> result = new HashMap();
        if (uriStr != null && ClickThroughType.INNER_START == this.clickThroughType) {
            Uri uri = Uri.parse(uriStr);
            String scheme = uri.getScheme();
            if (scheme != null && scheme.compareTo(CupidUtils.strReverse("dipuc_iyiqi")) == 0) {
                String host = uri.getHost();
                String query = uri.getQuery();
                if (!(host == null || query == null)) {
                    result.put(CrashConst.KEY_HOST, host);
                    result.put("query", query);
                }
            }
        }
        return result;
    }
}
