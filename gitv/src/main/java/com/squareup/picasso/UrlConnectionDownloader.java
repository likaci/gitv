package com.squareup.picasso;

import android.content.Context;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build.VERSION;
import android.util.Base64;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.squareup.picasso.Downloader.Response;
import com.squareup.picasso.Downloader.ResponseException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;

public class UrlConnectionDownloader implements Downloader {
    static final String RESPONSE_SOURCE = "X-Android-Response-Source";
    static volatile Object cache;
    private static final Object lock = new Object();
    private final Context context;

    private static class ResponseCacheIcs {
        private ResponseCacheIcs() {
        }

        static Object install(Context context) throws IOException {
            File cacheDir = Utils.createDefaultCacheDir(context);
            HttpResponseCache cache = HttpResponseCache.getInstalled();
            if (cache == null) {
                return HttpResponseCache.install(cacheDir, Utils.calculateDiskCacheSize(cacheDir));
            }
            return cache;
        }
    }

    public UrlConnectionDownloader(Context context) {
        this.context = context.getApplicationContext();
    }

    private String getAuthorization(String url) {
        if (url == null) {
            return null;
        }
        if (url.startsWith(WebConstants.WEB_SITE_BASE_HTTP)) {
            url = url.substring(7);
        } else if (!url.startsWith(WebConstants.WEB_SITE_BASE_HTTPS)) {
            return null;
        } else {
            url = url.substring(8);
        }
        int index = url.indexOf("@");
        if (index == -1) {
            return null;
        }
        String userName = url.substring(0, index);
        if (userName.indexOf(SOAP.DELIM) == -1) {
            userName = new StringBuilder(String.valueOf(userName)).append(SOAP.DELIM).toString();
        }
        return "Basic " + Base64.encodeToString(userName.getBytes(), 2);
    }

    protected HttpURLConnection openConnection(Uri path) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(path.toString()).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(20000);
        String auth = getAuthorization(path.toString());
        if (auth != null) {
            connection.setRequestProperty("Authorization", auth);
        }
        return connection;
    }

    public Response load(Uri uri, boolean localCacheOnly) throws IOException {
        if (VERSION.SDK_INT >= 14) {
            installCacheIfNeeded(this.context);
        }
        HttpURLConnection connection = openConnection(uri);
        connection.setUseCaches(true);
        if (localCacheOnly) {
            connection.setRequestProperty(HTTP.CACHE_CONTROL, "only-if-cached,max-age=2147483647");
        }
        int responseCode = connection.getResponseCode();
        if (responseCode >= 300) {
            connection.disconnect();
            throw new ResponseException(new StringBuilder(String.valueOf(responseCode)).append(" ").append(connection.getResponseMessage()).toString());
        }
        long contentLength = (long) connection.getHeaderFieldInt(HTTP.CONTENT_LENGTH, -1);
        return new Response(connection.getInputStream(), Utils.parseResponseSourceHeader(connection.getHeaderField(RESPONSE_SOURCE)), contentLength);
    }

    private static void installCacheIfNeeded(Context context) {
        if (cache == null) {
            try {
                synchronized (lock) {
                    if (cache == null) {
                        cache = ResponseCacheIcs.install(context);
                    }
                }
            } catch (IOException e) {
            }
        }
    }
}
