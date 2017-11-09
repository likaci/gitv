package com.mcto.ads.internal.net;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import com.mcto.ads.internal.common.CupidGlobal;
import com.mcto.ads.internal.common.CupidUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class HttpPostAsyncClient extends AsyncTask<String, Void, Void> {
    private static final String PINGBACK_PREFIX = "scp2.gif=";
    private static final int POST_TIMEOUT_CONNECTION = 10000;
    private static final int POST_TIMEOUT_SOCKET = 10000;

    private String getPingbackUrl() {
        String pingbackConfigUrl = CupidGlobal.getPingbackUrl();
        return pingbackConfigUrl != null ? pingbackConfigUrl : PingbackConstants.PINGBACK_URL;
    }

    protected Void doInBackground(String... params) {
        if (params.length != 0) {
            String jsonStr = params[0];
            if (CupidUtils.isValidStr(jsonStr)) {
                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
                HttpConnectionParams.setSoTimeout(httpParameters, 10000);
                HttpClient httpClient = new DefaultHttpClient(httpParameters);
                try {
                    HttpPost httpPost = new HttpPost(getPingbackUrl());
                    StringEntity data = new StringEntity(PINGBACK_PREFIX + jsonStr);
                    httpPost.addHeader("content-type", "application/x-www-form-urlencoded");
                    httpPost.setHeader("User-Agent", Build.MODEL);
                    httpPost.setEntity(data);
                    httpClient.execute(httpPost);
                } catch (Exception ex) {
                    Log.e("a71_ads_client", "doInBackground(): send pingback error:", ex);
                } catch (AssertionError e) {
                    Log.e("a71_ads_client", "doInBackground(): send pingback error:", e);
                }
            }
        }
        return null;
    }
}
