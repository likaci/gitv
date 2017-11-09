package com.gala.afinal.http;

import com.gala.imageprovider.p000private.C0137n;
import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.protocol.HttpContext;

public class SyncRequestHandler {
    private String charset;
    private final AbstractHttpClient client;
    private final HttpContext context;
    private final C0137n entityHandler = new C0137n();
    private int executionCount = 0;

    public SyncRequestHandler(AbstractHttpClient client, HttpContext context, String charset) {
        this.client = client;
        this.context = context;
        this.charset = charset;
    }

    private Object makeRequestWithRetries(HttpUriRequest request) throws IOException {
        int i;
        IOException iOException;
        int i2;
        IOException iOException2;
        IOException iOException3 = null;
        boolean z = true;
        HttpRequestRetryHandler httpRequestRetryHandler = this.client.getHttpRequestRetryHandler();
        while (z) {
            try {
                HttpResponse execute = this.client.execute(request, this.context);
                C0137n c0137n = this.entityHandler;
                return C0137n.m349a(execute.getEntity(), null, this.charset);
            } catch (UnknownHostException e) {
                iOException3 = e;
                i = this.executionCount + 1;
                this.executionCount = i;
                z = httpRequestRetryHandler.retryRequest(iOException3, i, this.context);
            } catch (IOException e2) {
                iOException3 = e2;
                i = this.executionCount + 1;
                this.executionCount = i;
                z = httpRequestRetryHandler.retryRequest(iOException3, i, this.context);
            } catch (NullPointerException e3) {
                iOException = new IOException("NPE in HttpClient" + e3.getMessage());
                i2 = this.executionCount + 1;
                this.executionCount = i2;
                iOException2 = iOException;
                z = httpRequestRetryHandler.retryRequest(iOException, i2, this.context);
                iOException3 = iOException2;
            } catch (Exception e4) {
                iOException = new IOException("Exception" + e4.getMessage());
                i2 = this.executionCount + 1;
                this.executionCount = i2;
                iOException2 = iOException;
                z = httpRequestRetryHandler.retryRequest(iOException, i2, this.context);
                iOException3 = iOException2;
            }
        }
        if (iOException3 != null) {
            throw iOException3;
        }
        throw new IOException("未知网络错误");
    }

    public Object sendRequest(HttpUriRequest... params) {
        try {
            return makeRequestWithRetries(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
