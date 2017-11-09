package com.gala.video.lib.framework.core.network.download.core;

import android.util.Log;
import com.gala.video.lib.framework.core.network.download.GalaDownloadException;
import com.gala.video.lib.framework.core.network.download.IGalaDownloadListener;
import com.gala.video.lib.framework.core.utils.NameExecutors;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import okhttp3.Interceptor;
import okhttp3.Interceptor.Chain;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Request;
import okhttp3.Response;

abstract class GalaBaseDownloader implements IGalaDownloader {
    protected static final String TAG = "Downloader";
    protected static ExecutorService mFixedThreadPool = NameExecutors.newFixedThreadPool(2, "GalaBaseDownloader");
    protected IGalaDownloadListener mDownloadListener;
    protected IGalaDownloadParameter mDownloadParameter;
    protected boolean mIsDownloading;
    protected Builder mOkHttpClientBuilder;

    protected class RetryIntercepter implements Interceptor {
        private int maxRetry;
        private int retryNum = 0;

        public RetryIntercepter(int maxCount) {
            this.maxRetry = maxCount;
        }

        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.d(GalaBaseDownloader.TAG, "retryNum=" + this.retryNum);
            Response response = chain.proceed(request);
            while (!response.isSuccessful() && this.retryNum < this.maxRetry) {
                this.retryNum++;
                Log.d(GalaBaseDownloader.TAG, "retryNum=" + this.retryNum);
                response = chain.proceed(request);
            }
            return response;
        }
    }

    protected abstract void callAsync();

    protected abstract void callSync();

    public GalaBaseDownloader() {
        this.mIsDownloading = false;
        this.mDownloadListener = null;
        this.mOkHttpClientBuilder = null;
        this.mDownloadParameter = null;
        this.mIsDownloading = false;
        this.mOkHttpClientBuilder = new OkHttpClient().newBuilder();
        this.mDownloadParameter = new GalaDownloadParameter();
    }

    private boolean checkParameter(IGalaDownloadListener listener) {
        this.mDownloadListener = listener;
        if (this.mDownloadListener == null) {
            Log.e(TAG, "Download listener should not be null");
            return false;
        } else if (this.mDownloadParameter.getDownloadUrl() != null && !this.mDownloadParameter.getDownloadUrl().isEmpty() && this.mDownloadParameter.getFileName() != null && !this.mDownloadParameter.getFileName().isEmpty()) {
            return true;
        } else {
            String msg = "Download url or save path is empty!";
            this.mDownloadListener.onError(new GalaDownloadException(2, msg));
            Log.e(TAG, msg);
            return false;
        }
    }

    public void callAsync(IGalaDownloadListener listener) {
        if (checkParameter(listener)) {
            callAsync();
        }
    }

    public void callSync(IGalaDownloadListener listener) {
        if (checkParameter(listener)) {
            callSync();
        }
    }

    public boolean isDownloading() {
        return this.mIsDownloading;
    }

    public IGalaDownloadParameter getDownloadParameter() {
        return this.mDownloadParameter;
    }

    protected final void onDownloadFailure(Throwable t) {
        String msg = "Downloading is failed!";
        if (t != null) {
            msg = t.getMessage();
        }
        this.mDownloadListener.onError(new GalaDownloadException(100, msg));
    }

    protected Builder getOkHttpClientBuilder() {
        return this.mOkHttpClientBuilder;
    }
}
