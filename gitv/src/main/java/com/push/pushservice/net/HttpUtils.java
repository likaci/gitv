package com.push.pushservice.net;

import android.text.TextUtils;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.utils.LogUtils;
import java.io.IOException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
    private static final String TAG = "HttpUtils";
    private static HttpConfigure mHttpConfigure = new HttpConfigure();

    private static HttpClient getHttpClient(boolean isHttps) {
        HttpClient client;
        HttpParams params = new BasicHttpParams();
        try {
            int soTime = mHttpConfigure.getHttpSoTimeOutTime();
            if (soTime > 0) {
                HttpConnectionParams.setSoTimeout(params, soTime);
            }
            int connectTime = mHttpConfigure.getHttpConnectTimeOutTime();
            if (connectTime > 0) {
                HttpConnectionParams.setConnectionTimeout(params, connectTime);
            }
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, "UTF-8");
        } catch (Exception e) {
            LogUtils.loge("getHttpClient setBasic e = " + e);
            e.printStackTrace();
        }
        if (isHttps) {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
            client = new DefaultHttpClient(new ThreadSafeClientConnManager(params, registry), params);
        } else {
            client = new DefaultHttpClient(params);
        }
        try {
            int retryTimes = mHttpConfigure.getHttpRetryTimes();
            if (retryTimes > 0) {
                ((AbstractHttpClient) client).setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(retryTimes, true));
            }
        } catch (Exception e2) {
            LogUtils.loge("getHttpClient setRetry e = " + e2);
            e2.printStackTrace();
        }
        return client;
    }

    public static String appendParams(String url, List<NameValuePair> params) {
        if (params == null || params.size() == 0 || TextUtils.isEmpty(url)) {
            return url;
        }
        if (!url.endsWith("?")) {
            url = url + "?";
        }
        return url + URLEncodedUtils.format(params, "UTF-8");
    }

    public static HttpEntity doGetRequest(String url, boolean isHttps) {
        HttpEntity httpEntity = null;
        if (!TextUtils.isEmpty(url)) {
            boolean https = false;
            if (isHttps && url.startsWith(DataConst.HTTPS_TAG)) {
                https = true;
            }
            try {
                LogUtils.logd(TAG, "doGetRequest url = " + url + " isHttps = " + isHttps + " https = " + https);
                HttpResponse res = getHttpClient(https).execute(new HttpGet(url));
                int code = res.getStatusLine().getStatusCode();
                LogUtils.logd(TAG, "doGetRequest response code = " + code);
                if (code == 200) {
                    httpEntity = res.getEntity();
                }
            } catch (ClientProtocolException e) {
                LogUtils.logd(TAG, "doGetRequest ClientProtocolException e = " + e);
                e.printStackTrace();
            } catch (IOException e2) {
                LogUtils.logd(TAG, "doGetRequest IOException e = " + e2);
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                LogUtils.logd(TAG, "doGetRequest IllegalStateException e = " + e3);
                e3.printStackTrace();
            } catch (Exception e4) {
                LogUtils.logd(TAG, "doGetRequest Exception e = " + e4);
                e4.printStackTrace();
            }
        }
        return httpEntity;
    }

    public static String doGetRequestForString(String url) {
        return doGetRequestForString(url, false);
    }

    public static String doGetRequestForString(String url, boolean isHttps) {
        String str = null;
        HttpEntity entity = doGetRequest(url, isHttps);
        if (entity != null) {
            try {
                str = EntityUtils.toString(entity, "UTF-8");
            } catch (Exception e) {
                LogUtils.logd(TAG, "doGetRequestForString Exception e = " + e);
                e.printStackTrace();
            }
        }
        return str;
    }

    public static String doGetRequestForString(String url, List<NameValuePair> params) {
        return doGetRequestForString(appendParams(url, params));
    }

    public static String doHttpsGetRequestForString(String url, List<NameValuePair> params) {
        return doGetRequestForString(appendParams(url, params), true);
    }
}
