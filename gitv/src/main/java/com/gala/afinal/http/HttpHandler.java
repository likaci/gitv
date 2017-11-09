package com.gala.afinal.http;

import android.os.SystemClock;
import com.gala.afinal.core.AsyncTask;
import com.gala.imageprovider.private.l;
import com.gala.imageprovider.private.m;
import com.gala.imageprovider.private.n;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;
import org.cybergarage.http.HTTPStatus;

public class HttpHandler<T> extends AsyncTask<Object, Object, Object> implements l {
    private static final int UPDATE_FAILURE = 3;
    private static final int UPDATE_LOADING = 2;
    private static final int UPDATE_START = 1;
    private static final int UPDATE_SUCCESS = 4;
    private final AjaxCallBack<T> callback;
    private String charset;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private int executionCount = 0;
    private boolean isResume = false;
    private final m mFileEntityHandler = new m();
    private final n mStrEntityHandler = new n();
    private String targetUrl = null;
    private long time;

    public HttpHandler(AbstractHttpClient client, HttpContext context, AjaxCallBack<T> callback, String charset) {
        this.client = client;
        this.context = context;
        this.callback = callback;
        this.charset = charset;
    }

    private void makeRequestWithRetries(HttpUriRequest request) throws IOException {
        IOException iOException;
        int i;
        IOException iOException2;
        if (this.isResume && this.targetUrl != null) {
            long length;
            File file = new File(this.targetUrl);
            if (file.isFile() && file.exists()) {
                length = file.length();
            } else {
                length = 0;
            }
            if (length > 0) {
                request.setHeader("RANGE", "bytes=" + length + "-");
            }
        }
        IOException iOException3 = null;
        HttpRequestRetryHandler httpRequestRetryHandler = this.client.getHttpRequestRetryHandler();
        boolean z = true;
        while (z) {
            try {
                if (!isCancelled()) {
                    HttpResponse execute = this.client.execute(request, this.context);
                    if (!isCancelled()) {
                        handleResponse(execute);
                        return;
                    }
                    return;
                }
                return;
            } catch (UnknownHostException e) {
                publishProgress(Integer.valueOf(3), e, Integer.valueOf(0), "unknownHostException：can't resolve host");
                return;
            } catch (IOException e2) {
                iOException3 = e2;
                int i2 = this.executionCount + 1;
                this.executionCount = i2;
                z = httpRequestRetryHandler.retryRequest(iOException3, i2, this.context);
            } catch (NullPointerException e3) {
                iOException = new IOException("NPE in HttpClient" + e3.getMessage());
                i = this.executionCount + 1;
                this.executionCount = i;
                iOException2 = iOException;
                z = httpRequestRetryHandler.retryRequest(iOException, i, this.context);
                iOException3 = iOException2;
            } catch (Exception e4) {
                iOException = new IOException("Exception" + e4.getMessage());
                i = this.executionCount + 1;
                this.executionCount = i;
                iOException2 = iOException;
                z = httpRequestRetryHandler.retryRequest(iOException, i, this.context);
                iOException3 = iOException2;
            }
        }
        if (iOException3 != null) {
            throw iOException3;
        }
        throw new IOException("未知网络错误");
    }

    protected Object doInBackground(Object... params) {
        if (params != null && params.length == 3) {
            this.targetUrl = String.valueOf(params[1]);
            this.isResume = ((Boolean) params[2]).booleanValue();
        }
        try {
            publishProgress(Integer.valueOf(1));
            makeRequestWithRetries((HttpUriRequest) params[0]);
        } catch (IOException e) {
            publishProgress(Integer.valueOf(3), e, Integer.valueOf(0), e.getMessage());
        }
        return null;
    }

    protected void onProgressUpdate(Object... values) {
        switch (Integer.valueOf(String.valueOf(values[0])).intValue()) {
            case 1:
                if (this.callback != null) {
                    this.callback.onStart();
                    break;
                }
                break;
            case 2:
                if (this.callback != null) {
                    this.callback.onLoading(Long.valueOf(String.valueOf(values[1])).longValue(), Long.valueOf(String.valueOf(values[2])).longValue());
                    break;
                }
                break;
            case 3:
                if (this.callback != null) {
                    this.callback.onFailure((Throwable) values[1], ((Integer) values[2]).intValue(), (String) values[3]);
                    break;
                }
                break;
            case 4:
                if (this.callback != null) {
                    this.callback.onSuccess(values[1]);
                    break;
                }
                break;
        }
        super.onProgressUpdate(values);
    }

    public boolean isStop() {
        return this.mFileEntityHandler.a();
    }

    public void stop() {
        this.mFileEntityHandler.a();
    }

    private void handleResponse(HttpResponse response) {
        StatusLine statusLine = response.getStatusLine();
        if (statusLine.getStatusCode() >= 300) {
            String str = "response status error code:" + statusLine.getStatusCode();
            if (statusLine.getStatusCode() == HTTPStatus.INVALID_RANGE && this.isResume) {
                str = str + " \n maybe you have download complete.";
            }
            publishProgress(Integer.valueOf(3), new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase()), Integer.valueOf(statusLine.getStatusCode()), str);
            return;
        }
        try {
            HttpEntity entity = response.getEntity();
            Object obj = null;
            if (entity != null) {
                this.time = SystemClock.uptimeMillis();
                if (this.targetUrl != null) {
                    obj = this.mFileEntityHandler.a(entity, this, this.targetUrl, this.isResume);
                } else {
                    n nVar = this.mStrEntityHandler;
                    obj = n.a(entity, this, this.charset);
                }
            }
            publishProgress(Integer.valueOf(4), obj);
        } catch (IOException e) {
            publishProgress(Integer.valueOf(3), e, Integer.valueOf(0), e.getMessage());
        }
    }

    public void callBack(long count, long current, boolean mustNoticeUI) {
        if (this.callback != null && this.callback.isProgress()) {
            if (mustNoticeUI) {
                publishProgress(Integer.valueOf(2), Long.valueOf(count), Long.valueOf(current));
                return;
            }
            long uptimeMillis = SystemClock.uptimeMillis();
            if (uptimeMillis - this.time >= ((long) this.callback.getRate())) {
                this.time = uptimeMillis;
                publishProgress(Integer.valueOf(2), Long.valueOf(count), Long.valueOf(current));
            }
        }
    }
}
