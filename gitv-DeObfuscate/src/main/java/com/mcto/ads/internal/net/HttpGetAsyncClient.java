package com.mcto.ads.internal.net;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import cn.com.mma.mobile.tracking.api.Countly;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.mcto.ads.internal.common.CupidContext;
import com.mcto.ads.internal.thirdparty.ThirdPartyConfig;
import com.mcto.ads.internal.thirdparty.ThirdPartyUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpGetAsyncClient extends AsyncTask<String, Void, Void> {
    private static final int EXECUTABLE_PARAM_COUNT = 4;
    private static final int HTTP_SEND_HTTPERR = 2;
    private static final int HTTP_SEND_PARAMERR = 3;
    private static final int HTTP_SEND_SUCCESS = 0;
    private static final int HTTP_SEND_TIMEOUT = 1;
    private static final int REQ_COUNT_LAST = -1;
    private IAdsCallback callback;
    private CupidContext cupidContext;
    private boolean enableThirdSdk;
    private ThirdPartyConfig thirdPartyConfig;
    private int trackingTimeout = 10000;
    private String trackingUrl;

    public HttpGetAsyncClient(IAdsCallback callback, ThirdPartyConfig config, boolean enableThird, CupidContext cupidContext) {
        this.callback = callback;
        this.thirdPartyConfig = config;
        this.enableThirdSdk = enableThird;
        this.cupidContext = cupidContext;
        if (cupidContext != null) {
            this.trackingTimeout = cupidContext.getTrackingTimeout();
        }
    }

    protected Void doInBackground(String... params) {
        if (params.length >= 4) {
            try {
                String adId = params[1];
                String type = params[2];
                TrackingParty party = TrackingParty.build(params[3]);
                this.trackingUrl = params[0];
                try {
                    URI uri = new URI(this.trackingUrl);
                    if (uri.getHost() == null) {
                        onTrackingSent(adId, party, 2, -1, 0);
                    } else if (needSendWithMMA(party)) {
                        sendWithMMA(type);
                    } else {
                        send(adId, party, uri);
                    }
                } catch (URISyntaxException e) {
                    onTrackingSent(adId, party, 2, -1, 0);
                }
            } catch (Exception e2) {
                Log.e("a71_ads_client", "doInBackground(): params error:", e2);
            }
        }
        return null;
    }

    private boolean needSendWithMMA(TrackingParty party) {
        return this.enableThirdSdk && ThirdPartyUtils.enableMma(this.trackingUrl, this.thirdPartyConfig) && party.equals(TrackingParty.THIRD);
    }

    private void sendWithMMA(String type) {
        try {
            if (type.equals("impression")) {
                Countly.sharedInstance().onExpose(this.trackingUrl);
            } else if (type.equals("click")) {
                Countly.sharedInstance().onClick(this.trackingUrl);
            } else {
                Countly.sharedInstance().onExpose(this.trackingUrl);
            }
            Log.d("a71_ads_client", "sendWithMMA(): success:" + this.trackingUrl);
        } catch (Exception ex) {
            Log.e("a71_ads_client", "sendWithMMA(): failed:" + this.trackingUrl, ex);
        }
    }

    private void send(String adId, TrackingParty party, URI uri) {
        HttpParams httpParameters = new BasicHttpParams();
        HttpClientParams.setRedirecting(httpParameters, false);
        HttpConnectionParams.setSoTimeout(httpParameters, this.trackingTimeout);
        HttpConnectionParams.setConnectionTimeout(httpParameters, this.trackingTimeout);
        HttpClient httpClient = new DefaultHttpClient(httpParameters);
        int requestDuration = 0;
        try {
            HttpGet httpGet = new HttpGet(uri);
            httpGet.setHeader("Host", uri.getHost());
            httpGet.setHeader("User-Agent", Build.MODEL);
            long startTime = new Date().getTime();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            requestDuration = (int) (new Date().getTime() - startTime);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 500 || statusCode == 404) {
                onTrackingSent(adId, party, 2, -1, requestDuration);
            } else if (!needCheckResponse(party)) {
                onTrackingSent(adId, party, 0, -1, requestDuration);
            } else if (getResponseData(httpResponse).contains(ScreenSaverPingBack.SEAT_KEY_OK)) {
                onTrackingSent(adId, party, 0, -1, requestDuration);
            } else {
                onTrackingSent(adId, party, 3, -1, requestDuration);
            }
        } catch (ConnectTimeoutException e) {
            onTrackingSent(adId, party, 1, -1, requestDuration);
        } catch (SocketTimeoutException e2) {
            onTrackingSent(adId, party, 1, -1, requestDuration);
        } catch (Exception e3) {
            onTrackingSent(adId, party, 2, -1, requestDuration);
        } catch (AssertionError e4) {
            onTrackingSent(adId, party, 2, -1, requestDuration);
        }
    }

    private boolean needCheckResponse(TrackingParty party) {
        if (this.cupidContext == null) {
            return false;
        }
        if (TrackingParty.CUPID == party || TrackingParty.ADX == party) {
            return true;
        }
        String pingbackType = (String) this.cupidContext.getThirdPingbackParams(this.trackingUrl).get("p");
        if (pingbackType == null || pingbackType.compareTo("qxt") != 0) {
            return false;
        }
        return true;
    }

    private String getResponseData(HttpResponse httpResponse) {
        String responseData = "";
        if (httpResponse == null) {
            return responseData;
        }
        try {
            BufferedReader breader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
            StringBuilder builder = new StringBuilder();
            while (true) {
                String line = breader.readLine();
                if (line == null) {
                    return builder.toString();
                }
                builder.append(line).append('\n');
            }
        } catch (IOException e) {
            return responseData;
        }
    }

    private void onTrackingSent(String adId, TrackingParty party, int result, int requestCount, int requestDuration) {
        if (this.callback != null) {
            Log.d("a71_ads_client", "onTrackingSent():" + adId + "," + result + "," + party + "," + requestDuration + "," + this.trackingUrl);
            Map<String, String> notification = new HashMap();
            notification.put(PingbackConstants.TRACKING_URL, this.trackingUrl);
            notification.put(PingbackConstants.REQUEST_COUNT, String.valueOf(requestCount));
            notification.put(PingbackConstants.REQUEST_DURATION, String.valueOf(requestDuration));
            String actType = "";
            if (result == 0) {
                if (TrackingParty.CUPID == party) {
                    actType = PingbackConstants.ACT_TRACKING_SUCCESS;
                } else if (TrackingParty.ADX == party) {
                    actType = PingbackConstants.ACT_ADX_TRACKING_SUCCESS;
                } else if (TrackingParty.THIRD == party) {
                    actType = PingbackConstants.ACT_THIRD_TRACKING_SUCCESS;
                }
            } else if (1 == result) {
                if (TrackingParty.CUPID == party) {
                    actType = PingbackConstants.ACT_TRACKING_TIMEOUT;
                } else if (TrackingParty.ADX == party) {
                    actType = PingbackConstants.ACT_ADX_TRACKING_TIMEOUT;
                } else if (TrackingParty.THIRD == party) {
                    actType = PingbackConstants.ACT_THIRD_TRACKING_TIMEOUT;
                }
            } else if (2 == result) {
                if (TrackingParty.CUPID == party) {
                    actType = PingbackConstants.ACT_TRACKING_HTTP_ERROR;
                } else if (TrackingParty.ADX == party) {
                    actType = PingbackConstants.ACT_ADX_TRACKING_HTTP_ERROR;
                } else if (TrackingParty.THIRD == party) {
                    actType = PingbackConstants.ACT_THIRD_TRACKING_HTTP_ERROR;
                }
            } else if (3 == result) {
                if (TrackingParty.CUPID == party) {
                    actType = PingbackConstants.ACT_TRACKING_PARAM_ERROR;
                } else if (TrackingParty.ADX == party) {
                    actType = PingbackConstants.ACT_ADX_TRACKING_PARAM_ERROR;
                } else if (TrackingParty.THIRD == party) {
                    actType = PingbackConstants.ACT_THIRD_TRACKING_PARAM_ERROR;
                }
            }
            this.callback.addTrackingEventCallback(Integer.parseInt(adId), party, actType, notification);
        }
    }
}
