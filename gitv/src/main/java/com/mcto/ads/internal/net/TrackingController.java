package com.mcto.ads.internal.net;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.util.Log;
import com.mcto.ads.constants.Interaction;
import com.mcto.ads.internal.common.CupidUtils;
import com.mcto.ads.internal.thirdparty.ThirdPartyConfig;
import java.util.Date;
import org.cybergarage.soap.SOAP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrackingController {
    public static String updateCupidTrackingUrl(String url, long debugTime) {
        String newUrl = url;
        Uri uri = Uri.parse(url);
        String type = uri.getQueryParameter("a");
        String orderItemId = uri.getQueryParameter("d");
        String videoEventId = uri.getQueryParameter("r");
        String impressionId = uri.getQueryParameter("c");
        Long serverNow = Long.valueOf(new Long(new Date().getTime() / 1000).longValue() + (debugTime / 1000));
        String timestampParam = uri.getQueryParameter(TrackingConstants.TRACKING_KEY_TIMESTAMP);
        if (CupidUtils.isValidStr(timestampParam)) {
            newUrl = newUrl.replaceFirst("b=" + timestampParam, "b=" + serverNow);
        } else {
            newUrl = newUrl + "&b=" + serverNow;
        }
        String checksum = CupidUtils.md5(type + serverNow + impressionId + orderItemId + videoEventId + "cupid");
        String checksumParam = uri.getQueryParameter("s");
        String checksumReplace = "s=" + checksum;
        if (CupidUtils.isValidStr(checksumParam)) {
            newUrl = newUrl.replaceFirst("s=" + checksumParam, checksumReplace);
        } else {
            newUrl = newUrl + "&" + checksumReplace;
        }
        String md5VerParam = uri.getQueryParameter(TrackingConstants.TRACKING_KEY_ENCRYPTION_VER);
        String md5VerReplace = "ve=1";
        if (CupidUtils.isValidStr(md5VerParam)) {
            return newUrl.replaceFirst("ve=" + md5VerParam, md5VerReplace);
        }
        return newUrl + "&" + md5VerReplace;
    }

    public static String updateAdxTrackingUrl(String url, long debugTime) {
        String newUrl = url;
        Uri uri = Uri.parse(url);
        String type = uri.getQueryParameter("a");
        String chargingPrice = uri.getQueryParameter("v");
        String impressionId = uri.getQueryParameter("c");
        String videoEventId = uri.getQueryParameter("r");
        String campaignId = uri.getQueryParameter("d");
        String creativeId = uri.getQueryParameter("q");
        Long serverNow = Long.valueOf(new Long(new Date().getTime() / 1000).longValue() + (debugTime / 1000));
        String timestampParam = uri.getQueryParameter(TrackingConstants.TRACKING_KEY_TIMESTAMP);
        if (CupidUtils.isValidStr(timestampParam)) {
            newUrl = newUrl.replaceFirst("b=" + timestampParam, "b=" + serverNow);
        } else {
            newUrl = newUrl + "&b=" + serverNow;
        }
        String checksum = CupidUtils.md5(type + chargingPrice + serverNow + impressionId + videoEventId + campaignId + creativeId + "qax-track");
        String checksumParam = uri.getQueryParameter("s");
        String checksumReplace = "s=" + checksum;
        if (CupidUtils.isValidStr(checksumParam)) {
            newUrl = newUrl.replaceFirst("s=" + checksumParam, checksumReplace);
        } else {
            newUrl = newUrl + "&" + checksumReplace;
        }
        String md5VerParam = uri.getQueryParameter(TrackingConstants.TRACKING_KEY_ENCRYPTION_VER);
        String md5VerReplace = "ve=1";
        if (CupidUtils.isValidStr(md5VerParam)) {
            return newUrl.replaceFirst("ve=" + md5VerParam, md5VerReplace);
        }
        return newUrl + "&" + md5VerReplace;
    }

    public static String updateThirdPartyTrackingUrl(String url, long debugTime) {
        String newUrl = url;
        String serverTimeNow = String.valueOf(new Date().getTime() + debugTime);
        return newUrl.replace("__timeStamp__", serverTimeNow).replace("[timestamp]", serverTimeNow).replace("__TS__", serverTimeNow).replace("[randnum]", serverTimeNow).replace("[M_timestamp]", serverTimeNow);
    }

    public static void onEventCommon(String json, String event, boolean enableThirdSdk) {
        Log.d("a71_ads_client", "onEventCommon(): event: " + event);
        if (json != null && !json.equals("")) {
            long debugTime = 0;
            ThirdPartyConfig thirdPartyConfig = new ThirdPartyConfig();
            try {
                String trackingType;
                JSONObject object = new JSONObject(json);
                JSONObject eventObj = object.optJSONObject(event);
                if (event.equals("click")) {
                    trackingType = "click";
                } else {
                    trackingType = "event";
                }
                JSONObject envObj = object.optJSONObject("env");
                if (envObj.has(Interaction.KEY_DEBUG_TIME)) {
                    debugTime = envObj.getLong(Interaction.KEY_DEBUG_TIME);
                }
                if (envObj.has("mmaSwitch")) {
                    ThirdPartyConfig thirdPartyConfig2 = new ThirdPartyConfig(envObj.getInt("mmaSwitch"));
                }
                if (eventObj != null) {
                    Log.d("a71_ads_client", "onEventCommon():" + event + ": " + eventObj.toString());
                    for (String party : new String[]{"cupid", "adx", "thirdParty"}) {
                        try {
                            JSONArray trackingList = eventObj.optJSONArray(party);
                            if (trackingList != null) {
                                int size = trackingList.length();
                                for (int i = 0; i < size; i++) {
                                    HttpGetAsyncClient client = new HttpGetAsyncClient(null, thirdPartyConfig, enableThirdSdk, null);
                                    String tracking = trackingList.getString(i);
                                    String partyValue = "";
                                    if (party.equals("cupid")) {
                                        tracking = updateCupidTrackingUrl(tracking, debugTime);
                                        partyValue = TrackingParty.CUPID.value();
                                    } else if (party.equals("adx")) {
                                        tracking = updateAdxTrackingUrl(tracking, debugTime);
                                        partyValue = TrackingParty.ADX.value();
                                    } else if (party.equals("thirdParty")) {
                                        tracking = updateThirdPartyTrackingUrl(tracking, debugTime);
                                        partyValue = TrackingParty.THIRD.value();
                                    }
                                    if (VERSION.SDK_INT < 11) {
                                        client.execute(new String[]{tracking, "", trackingType, partyValue});
                                    } else {
                                        client.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{tracking, "", trackingType, partyValue});
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("a71_ads_client", "onEventCommon(): exception " + party + SOAP.DELIM + e.getMessage());
                        }
                    }
                }
            } catch (JSONException e2) {
                Log.d("a71_ads_client", "onEventCommon(): exception:" + e2.getMessage());
            }
        }
    }
}
