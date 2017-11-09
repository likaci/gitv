package tv.gitv.ptqy.security.fingerprint.Utils;

import android.text.TextUtils;
import android.util.Log;
import com.push.pushservice.constants.DataConst;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
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
import org.cybergarage.http.HTTP;
import tv.gitv.ptqy.security.fingerprint.callback.OnRequestBackListener;
import tv.gitv.ptqy.security.fingerprint.constants.Consts;

public class HttpUtils {
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
            e2.printStackTrace();
        }
        return client;
    }

    public static String appendParams(String url, List<NameValuePair> params) {
        if (params == null || params.size() == 0 || TextUtils.isEmpty(url)) {
            return url;
        }
        if (!url.endsWith("?")) {
            url = new StringBuilder(String.valueOf(url)).append("?").toString();
        }
        return new StringBuilder(String.valueOf(url)).append(URLEncodedUtils.format(params, "UTF-8")).toString();
    }

    public static HttpEntity doGetRequest(String url, boolean isHttps) {
        HttpEntity httpEntity = null;
        if (!TextUtils.isEmpty(url)) {
            boolean https = false;
            if (isHttps && url.startsWith(DataConst.HTTPS_TAG)) {
                https = true;
            }
            try {
                HttpResponse res = getHttpClient(https).execute(new HttpGet(url));
                if (res.getStatusLine().getStatusCode() == 200) {
                    httpEntity = res.getEntity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return httpEntity;
    }

    public static String params2payload(Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            for (String key : params.keySet()) {
                sb.append(key);
                sb.append('=');
                sb.append((String) params.get(key));
                sb.append('&');
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String doPostRequest(String url, String payload, OnRequestBackListener listener) {
        try {
            URL url1 = new URL(url);
            StringBuilder stringBuilder = new StringBuilder();
            try {
                InputStream in;
                HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                conn.setRequestMethod(HTTP.POST);
                conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(10000);
                OutputStream out = conn.getOutputStream();
                out.write(payload.getBytes());
                out.flush();
                out.close();
                if (200 == conn.getResponseCode()) {
                    in = conn.getInputStream();
                    if (listener != null) {
                        listener.onRequestSuccess();
                    }
                } else {
                    in = conn.getErrorStream();
                    if (listener != null) {
                        listener.onRequestFailure();
                    }
                }
                byte[] buffer = new byte[1024];
                while (true) {
                    int count = in.read(buffer);
                    if (count == -1) {
                        in.close();
                        conn.disconnect();
                        Log.i(Consts.TAG, "[DFP] Response : " + stringBuilder.toString());
                        return stringBuilder.toString();
                    }
                    stringBuilder.append(new String(buffer, 0, count));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            }
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
            return "";
        }
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
