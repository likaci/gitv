package com.gala.video.lib.framework.core.utils.io;

import com.gala.video.lib.framework.core.exception.api.APINetworkException;
import com.gala.video.lib.framework.core.exception.api.APIResultEmptyException;
import com.gala.video.lib.framework.core.exception.error.ErrorConnectInUIThreadException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.message.BufferedHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class HttpUtil {
    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int SOCKET_BUFFER_SIZE = 8192;
    private static final int SO_TIMEOUT = 8000;
    private static final String TAG = "IOUtils";
    private static final int TRY_GET_COUNT = 2;
    private boolean debug = true;
    private List<String> headers = null;
    private Map<String, String> mHeader = new HashMap();
    private Map<String, String> mParams = new HashMap();
    private String mUrl = null;
    private List<NameValuePair> params = null;
    private int tries = 0;

    public HttpUtil setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public HttpUtil(String url) {
        setUrl(url);
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setPara(String key, Object value) {
        if (this.params == null) {
            this.params = new ArrayList();
        }
        if (value == null) {
            value = "";
        }
        this.params.add(new BasicNameValuePair(key, value.toString()));
    }

    @Deprecated
    public void setHeader(String key, Object value) {
        if (this.headers == null) {
            this.headers = new ArrayList();
        }
        if (value == null) {
            value = "";
        }
        this.headers.add(key + SOAP.DELIM + value.toString());
    }

    public void setHeader(Map<String, String> header) {
        if (header != null) {
            this.mHeader.putAll(header);
        }
    }

    public void setCookie(String cookie) {
        if (!StringUtils.isEmpty((CharSequence) cookie)) {
            this.mHeader.put("Set-Cookie", cookie);
        }
    }

    public void addParams(String key, String value) {
        if (!StringUtils.isEmpty((CharSequence) key) && !StringUtils.isEmpty((CharSequence) value)) {
            this.mParams.put(key, value);
        }
    }

    public String get() {
        if (ThreadUtils.getUIThreadId() == Thread.currentThread().getId()) {
            throw new ErrorConnectInUIThreadException();
        }
        CharSequence str = retryGet();
        if (!StringUtils.isEmpty(str)) {
            return str;
        }
        throw new APIResultEmptyException();
    }

    private String retryGet() {
        log("http get : " + this.mUrl);
        Exception e = null;
        while (this.tries < 2) {
            try {
                if (this.mParams.size() == 0) {
                    return requestByGet2(this.mUrl);
                }
                return requestByPost2(this.mUrl);
            } catch (Exception ex) {
                this.tries++;
                ex.printStackTrace();
                LogUtils.e(TAG, "http exception: tried 【" + this.tries + "】。" + ex.getMessage());
                e = ex;
            }
        }
        throw new APINetworkException(e);
    }

    private void log(String msg) {
        if (this.debug) {
            LogUtils.d(TAG, msg);
        }
    }

    public static String requestByGet(String url, List<String> headers) throws ClientProtocolException, IOException {
        LogUtils.i("HttpUtils", "requestByGet url = " + url);
        HttpGet request = new HttpGet(url);
        DefaultHttpClient httpClient = getHttpClident();
        if (headers != null) {
            request.setHeaders(getHeaders(headers));
        }
        HttpResponse response = httpClient.execute(request);
        if (response.getStatusLine().getStatusCode() == 200) {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        }
        return null;
    }

    public String requestByGet2(String path) throws Exception {
        HttpURLConnection httpURLConnection = getHttpURLConnection(new URL(path.trim()), HTTP.GET);
        if (httpURLConnection.getResponseCode() == 200) {
            return readStreamToString(httpURLConnection.getInputStream());
        }
        return null;
    }

    public static String requestByPost(String url, List<NameValuePair> params, List<String> headers) throws ClientProtocolException, IOException {
        HttpPost request = new HttpPost(url);
        DefaultHttpClient httpClient = getHttpClident();
        request.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        if (headers != null) {
            request.setHeaders(getHeaders(headers));
        }
        HttpResponse response = httpClient.execute(request);
        int code = response.getStatusLine().getStatusCode();
        if (code < 200 || code >= 300) {
            return null;
        }
        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }

    public String requestByPost2(String path) throws Exception {
        HttpURLConnection httpURLConnection = getHttpURLConnection(new URL(path.trim()), HTTP.POST);
        PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
        printWriter.write(parseParams());
        printWriter.flush();
        int responseCode = httpURLConnection.getResponseCode();
        if (responseCode < 200 || responseCode >= 300) {
            return null;
        }
        return readStreamToString(httpURLConnection.getInputStream());
    }

    private static Header[] getHeaders(List<String> headers) {
        if (headers == null || headers.isEmpty()) {
            return null;
        }
        Header[] result = new Header[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            CharArrayBuffer buffer = new CharArrayBuffer(((String) headers.get(i)).length() + 8);
            buffer.append((String) headers.get(i));
            result[i] = new BufferedHeader(buffer);
        }
        return result;
    }

    public static DefaultHttpClient getHttpClident() {
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
        HttpConnectionParams.setSocketBufferSize(httpParams, 8192);
        HttpClientParams.setRedirecting(httpParams, true);
        HttpProtocolParams.setUserAgent(httpParams, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        return new DefaultHttpClient(httpParams);
    }

    private String parseParams() {
        CharSequence params = "";
        if (this.mParams == null) {
            return params;
        }
        for (String key : this.mParams.keySet()) {
            params = params + key + SearchCriteria.EQ + ((String) this.mParams.get(key)) + "&";
        }
        if (StringUtils.isEmpty(params) || !params.endsWith("&")) {
            return params;
        }
        return params.substring(0, params.length() - 1);
    }

    private HttpURLConnection getHttpURLConnection(URL url, String method) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(method);
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.setReadTimeout(SO_TIMEOUT);
        httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        httpURLConnection.setRequestProperty("Charset", "UTF-8");
        if (this.mHeader != null) {
            for (String key : this.mHeader.keySet()) {
                httpURLConnection.setRequestProperty(key, (String) this.mHeader.get(key));
            }
        }
        return httpURLConnection;
    }

    private String readStreamToString(InputStream is) {
        IOException e;
        Throwable th;
        String result = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                byte[] buffer = new byte[8192];
                while (true) {
                    int len = is.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    baos.write(buffer, 0, len);
                }
                baos.flush();
                result = baos.toString();
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        byteArrayOutputStream = baos;
                    }
                }
                if (baos != null) {
                    baos.close();
                }
                byteArrayOutputStream = baos;
            } catch (IOException e3) {
                e2 = e3;
                byteArrayOutputStream = baos;
                try {
                    e2.printStackTrace();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e22) {
                            e22.printStackTrace();
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    return result;
                } catch (Throwable th2) {
                    th = th2;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e222) {
                            e222.printStackTrace();
                            throw th;
                        }
                    }
                    if (byteArrayOutputStream != null) {
                        byteArrayOutputStream.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                byteArrayOutputStream = baos;
                if (is != null) {
                    is.close();
                }
                if (byteArrayOutputStream != null) {
                    byteArrayOutputStream.close();
                }
                throw th;
            }
        } catch (IOException e4) {
            e222 = e4;
            e222.printStackTrace();
            if (is != null) {
                is.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.close();
            }
            return result;
        }
        return result;
    }
}
