package com.gala.afinal.http;

import android.os.SystemClock;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashSet;
import javax.net.ssl.SSLHandshakeException;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.cybergarage.http.HTTP;

public class RetryHandler implements HttpRequestRetryHandler {
    private static final int RETRY_SLEEP_TIME_MILLIS = 1000;
    private static HashSet<Class<?>> exceptionBlacklist = new HashSet();
    private static HashSet<Class<?>> exceptionWhitelist = new HashSet();
    private final int maxRetries;

    static {
        exceptionWhitelist.add(NoHttpResponseException.class);
        exceptionWhitelist.add(UnknownHostException.class);
        exceptionWhitelist.add(SocketException.class);
        exceptionBlacklist.add(InterruptedIOException.class);
        exceptionBlacklist.add(SSLHandshakeException.class);
    }

    public RetryHandler(int maxRetries) {
        this.maxRetries = maxRetries;
    }

    public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
        boolean z = true;
        Boolean bool = (Boolean) context.getAttribute("http.request_sent");
        boolean z2 = bool != null && bool.booleanValue();
        if (executionCount > this.maxRetries) {
            z2 = false;
        } else if (exceptionBlacklist.contains(exception.getClass())) {
            z2 = false;
        } else if (exceptionWhitelist.contains(exception.getClass())) {
            z2 = true;
        } else if (z2) {
            z2 = true;
        } else {
            z2 = true;
        }
        if (z2) {
            HttpUriRequest httpUriRequest = (HttpUriRequest) context.getAttribute("http.request");
            if (httpUriRequest == null || HTTP.POST.equals(httpUriRequest.getMethod())) {
                z = false;
            }
        } else {
            z = z2;
        }
        if (z) {
            SystemClock.sleep(1000);
        } else {
            exception.printStackTrace();
        }
        return z;
    }
}
