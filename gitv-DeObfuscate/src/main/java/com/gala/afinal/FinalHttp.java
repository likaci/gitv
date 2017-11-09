package com.gala.afinal;

import com.gala.afinal.http.AjaxCallBack;
import com.gala.afinal.http.AjaxParams;
import com.gala.afinal.http.HttpHandler;
import com.gala.afinal.http.RetryHandler;
import com.gala.afinal.http.SyncRequestHandler;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPInputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.SyncBasicHttpContext;
import org.cybergarage.http.HTTP;
import org.cybergarage.xml.XML;

public class FinalHttp {
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final Executor executor = Executors.newFixedThreadPool(httpThreadCount, sThreadFactory);
    private static int httpThreadCount = 3;
    private static int maxConnections = 10;
    private static int maxRetries = 5;
    private static final ThreadFactory sThreadFactory = new C00281();
    private static int socketTimeout = 10000;
    private String charset = XML.CHARSET_UTF8;
    private final Map<String, String> clientHeaderMap;
    private final DefaultHttpClient httpClient;
    private final HttpContext httpContext;

    static class C00281 implements ThreadFactory {
        private final AtomicInteger mCount = new AtomicInteger(1);

        C00281() {
        }

        public final Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "FinalHttp #" + this.mCount.getAndIncrement());
            thread.setPriority(4);
            return thread;
        }
    }

    class C00292 implements HttpRequestInterceptor {
        C00292() {
        }

        public void process(HttpRequest request, HttpContext httpContext) {
            if (!request.containsHeader(FinalHttp.HEADER_ACCEPT_ENCODING)) {
                request.addHeader(FinalHttp.HEADER_ACCEPT_ENCODING, FinalHttp.ENCODING_GZIP);
            }
            for (String str : FinalHttp.this.clientHeaderMap.keySet()) {
                request.addHeader(str, (String) FinalHttp.this.clientHeaderMap.get(str));
            }
        }
    }

    class C00303 implements HttpResponseInterceptor {
        C00303() {
        }

        public void process(HttpResponse response, HttpContext httpContext) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                Header contentEncoding = entity.getContentEncoding();
                if (contentEncoding != null) {
                    for (HeaderElement name : contentEncoding.getElements()) {
                        if (name.getName().equalsIgnoreCase(FinalHttp.ENCODING_GZIP)) {
                            response.setEntity(new InflatingEntity(response.getEntity()));
                            return;
                        }
                    }
                }
            }
        }
    }

    static class InflatingEntity extends HttpEntityWrapper {
        public InflatingEntity(HttpEntity wrapped) {
            super(wrapped);
        }

        public InputStream getContent() throws IOException {
            return new GZIPInputStream(this.wrappedEntity.getContent());
        }

        public long getContentLength() {
            return -1;
        }
    }

    public FinalHttp() {
        HttpParams basicHttpParams = new BasicHttpParams();
        ConnManagerParams.setTimeout(basicHttpParams, (long) socketTimeout);
        ConnManagerParams.setMaxConnectionsPerRoute(basicHttpParams, new ConnPerRouteBean(maxConnections));
        ConnManagerParams.setMaxTotalConnections(basicHttpParams, 10);
        HttpConnectionParams.setSoTimeout(basicHttpParams, socketTimeout);
        HttpConnectionParams.setConnectionTimeout(basicHttpParams, socketTimeout);
        HttpConnectionParams.setTcpNoDelay(basicHttpParams, true);
        HttpConnectionParams.setSocketBufferSize(basicHttpParams, 8192);
        HttpProtocolParams.setVersion(basicHttpParams, HttpVersion.HTTP_1_1);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager threadSafeClientConnManager = new ThreadSafeClientConnManager(basicHttpParams, schemeRegistry);
        this.httpContext = new SyncBasicHttpContext(new BasicHttpContext());
        this.httpClient = new DefaultHttpClient(threadSafeClientConnManager, basicHttpParams);
        this.httpClient.addRequestInterceptor(new C00292());
        this.httpClient.addResponseInterceptor(new C00303());
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(maxRetries));
        this.clientHeaderMap = new HashMap();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    public HttpContext getHttpContext() {
        return this.httpContext;
    }

    public void configCharset(String charSet) {
        if (charSet != null && charSet.trim().length() != 0) {
            this.charset = charSet;
        }
    }

    public void configCookieStore(CookieStore cookieStore) {
        this.httpContext.setAttribute("http.cookie-store", cookieStore);
    }

    public void configUserAgent(String userAgent) {
        HttpProtocolParams.setUserAgent(this.httpClient.getParams(), userAgent);
    }

    public void configTimeout(int timeout) {
        HttpParams params = this.httpClient.getParams();
        ConnManagerParams.setTimeout(params, (long) timeout);
        HttpConnectionParams.setSoTimeout(params, timeout);
        HttpConnectionParams.setConnectionTimeout(params, timeout);
    }

    public void configSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", sslSocketFactory, 443));
    }

    public void configRequestExecutionRetryCount(int count) {
        this.httpClient.setHttpRequestRetryHandler(new RetryHandler(count));
    }

    public void addHeader(String header, String value) {
        this.clientHeaderMap.put(header, value);
    }

    public void get(String url, AjaxCallBack<? extends Object> callBack) {
        get(url, null, callBack);
    }

    public void get(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(url, params)), null, callBack);
    }

    public void get(String url, Header[] headers, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        HttpUriRequest httpGet = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) {
            httpGet.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, httpGet, null, callBack);
    }

    public Object getSync(String url) {
        return getSync(url, null);
    }

    public Object getSync(String url, AjaxParams params) {
        return sendSyncRequest(this.httpClient, this.httpContext, new HttpGet(getUrlWithQueryString(url, params)), null);
    }

    public Object getSync(String url, Header[] headers, AjaxParams params) {
        HttpUriRequest httpGet = new HttpGet(getUrlWithQueryString(url, params));
        if (headers != null) {
            httpGet.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, httpGet, null);
    }

    public void post(String url, AjaxCallBack<? extends Object> callBack) {
        post(url, null, callBack);
    }

    public void post(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        post(url, paramsToEntity(params), null, callBack);
    }

    public void post(String url, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType, callBack);
    }

    public <T> void post(String url, Header[] headers, AjaxParams params, String contentType, AjaxCallBack<T> callBack) {
        HttpUriRequest httpPost = new HttpPost(url);
        if (params != null) {
            httpPost.setEntity(paramsToEntity(params));
        }
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, httpPost, contentType, callBack);
    }

    public void post(String url, Header[] headers, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        HttpUriRequest addEntityToRequestBase = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) {
            addEntityToRequestBase.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase, contentType, callBack);
    }

    public Object postSync(String url) {
        return postSync(url, null);
    }

    public Object postSync(String url, AjaxParams params) {
        return postSync(url, paramsToEntity(params), null);
    }

    public Object postSync(String url, HttpEntity entity, String contentType) {
        return sendSyncRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPost(url), entity), contentType);
    }

    public Object postSync(String url, Header[] headers, AjaxParams params, String contentType) {
        HttpUriRequest httpPost = new HttpPost(url);
        if (params != null) {
            httpPost.setEntity(paramsToEntity(params));
        }
        if (headers != null) {
            httpPost.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, httpPost, contentType);
    }

    public Object postSync(String url, Header[] headers, HttpEntity entity, String contentType) {
        HttpUriRequest addEntityToRequestBase = addEntityToRequestBase(new HttpPost(url), entity);
        if (headers != null) {
            addEntityToRequestBase.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, addEntityToRequestBase, contentType);
    }

    public void put(String url, AjaxCallBack<? extends Object> callBack) {
        put(url, null, callBack);
    }

    public void put(String url, AjaxParams params, AjaxCallBack<? extends Object> callBack) {
        put(url, paramsToEntity(params), null, callBack);
    }

    public void put(String url, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase(new HttpPut(url), entity), contentType, callBack);
    }

    public void put(String url, Header[] headers, HttpEntity entity, String contentType, AjaxCallBack<? extends Object> callBack) {
        HttpUriRequest addEntityToRequestBase = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null) {
            addEntityToRequestBase.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, addEntityToRequestBase, contentType, callBack);
    }

    public Object putSync(String url) {
        return putSync(url, null);
    }

    public Object putSync(String url, AjaxParams params) {
        return putSync(url, paramsToEntity(params), null);
    }

    public Object putSync(String url, HttpEntity entity, String contentType) {
        return putSync(url, null, entity, contentType);
    }

    public Object putSync(String url, Header[] headers, HttpEntity entity, String contentType) {
        HttpUriRequest addEntityToRequestBase = addEntityToRequestBase(new HttpPut(url), entity);
        if (headers != null) {
            addEntityToRequestBase.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, addEntityToRequestBase, contentType);
    }

    public void delete(String url, AjaxCallBack<? extends Object> callBack) {
        sendRequest(this.httpClient, this.httpContext, new HttpDelete(url), null, callBack);
    }

    public void delete(String url, Header[] headers, AjaxCallBack<? extends Object> callBack) {
        HttpUriRequest httpDelete = new HttpDelete(url);
        if (headers != null) {
            httpDelete.setHeaders(headers);
        }
        sendRequest(this.httpClient, this.httpContext, httpDelete, null, callBack);
    }

    public Object deleteSync(String url) {
        return deleteSync(url, null);
    }

    public Object deleteSync(String url, Header[] headers) {
        HttpUriRequest httpDelete = new HttpDelete(url);
        if (headers != null) {
            httpDelete.setHeaders(headers);
        }
        return sendSyncRequest(this.httpClient, this.httpContext, httpDelete, null);
    }

    public HttpHandler<File> download(String url, String target, AjaxCallBack<File> callback) {
        return download(url, null, target, false, callback);
    }

    public HttpHandler<File> download(String url, String target, boolean isResume, AjaxCallBack<File> callback) {
        return download(url, null, target, isResume, callback);
    }

    public HttpHandler<File> download(String url, AjaxParams params, String target, AjaxCallBack<File> callback) {
        return download(url, params, target, false, callback);
    }

    public HttpHandler<File> download(String url, AjaxParams params, String target, boolean isResume, AjaxCallBack<File> callback) {
        HttpGet httpGet = new HttpGet(getUrlWithQueryString(url, params));
        HttpHandler<File> httpHandler = new HttpHandler(this.httpClient, this.httpContext, callback, this.charset);
        httpHandler.executeOnExecutor(executor, httpGet, target, Boolean.valueOf(isResume));
        return httpHandler;
    }

    protected <T> void sendRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType, AjaxCallBack<T> ajaxCallBack) {
        if (contentType != null) {
            uriRequest.addHeader(HTTP.CONTENT_TYPE, contentType);
        }
        new HttpHandler(client, httpContext, ajaxCallBack, this.charset).executeOnExecutor(executor, uriRequest);
    }

    protected Object sendSyncRequest(DefaultHttpClient client, HttpContext httpContext, HttpUriRequest uriRequest, String contentType) {
        if (contentType != null) {
            uriRequest.addHeader(HTTP.CONTENT_TYPE, contentType);
        }
        return new SyncRequestHandler(client, httpContext, this.charset).sendRequest(uriRequest);
    }

    public static String getUrlWithQueryString(String url, AjaxParams params) {
        if (params == null) {
            return url;
        }
        return url + "?" + params.getParamString();
    }

    private HttpEntity paramsToEntity(AjaxParams params) {
        if (params != null) {
            return params.getEntity();
        }
        return null;
    }

    private HttpEntityEnclosingRequestBase addEntityToRequestBase(HttpEntityEnclosingRequestBase requestBase, HttpEntity entity) {
        if (entity != null) {
            requestBase.setEntity(entity);
        }
        return requestBase;
    }
}
