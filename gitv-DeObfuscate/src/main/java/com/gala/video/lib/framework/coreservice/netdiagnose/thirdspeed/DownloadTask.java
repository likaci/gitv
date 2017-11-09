package com.gala.video.lib.framework.coreservice.netdiagnose.thirdspeed;

import com.gala.video.lib.framework.core.utils.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.cybergarage.http.HTTP;

public class DownloadTask {
    private DownloadStateListener mDownloadStateListener;
    private String mDownloadUrl;
    private boolean mIsCancelled = false;
    private boolean mIsRunning = false;
    private long mStartTime;
    private long mTempFileSize = 0;
    private long mTotalRead;

    public DownloadTask(String downloadUrl, DownloadStateListener downloadStateListener) {
        this.mDownloadUrl = downloadUrl;
        this.mDownloadStateListener = downloadStateListener;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void download(java.lang.String r6) {
        /*
        r5 = this;
        r5.initParam();
        r2 = 1;
        r5.mIsRunning = r2;
        r1 = 0;
        r1 = r5.getInputStream(r6);	 Catch:{ Exception -> 0x0017 }
        if (r1 == 0) goto L_0x0010;
    L_0x000d:
        r5.copyStream(r1);	 Catch:{ Exception -> 0x0017 }
    L_0x0010:
        com.gala.video.lib.framework.core.utils.io.FileUtil.safeClose(r1);
    L_0x0013:
        r2 = 0;
        r5.mIsRunning = r2;
        return;
    L_0x0017:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x002a }
        r2 = r5.mDownloadStateListener;	 Catch:{ all -> 0x002a }
        r3 = r5.mDownloadUrl;	 Catch:{ all -> 0x002a }
        r4 = r0.getMessage();	 Catch:{ all -> 0x002a }
        r2.onDownloadFailed(r3, r4);	 Catch:{ all -> 0x002a }
        com.gala.video.lib.framework.core.utils.io.FileUtil.safeClose(r1);
        goto L_0x0013;
    L_0x002a:
        r2 = move-exception;
        com.gala.video.lib.framework.core.utils.io.FileUtil.safeClose(r1);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.framework.coreservice.netdiagnose.thirdspeed.DownloadTask.download(java.lang.String):void");
    }

    private void initParam() {
        this.mStartTime = System.currentTimeMillis();
        this.mTotalRead = 0;
        this.mIsRunning = true;
    }

    private InputStream getInputStream(String url) throws ClientProtocolException, IOException {
        LogUtils.m1568d("", "download url : " + url);
        HttpParams httpParams = new BasicHttpParams();
        httpParams.setParameter("Expires", Integer.valueOf(-1));
        httpParams.setParameter(HTTP.CACHE_CONTROL, "no_cache");
        httpParams.setParameter("Pragma", HTTP.NO_CACHE);
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);
        HttpConnectionParams.setSoTimeout(httpParams, 3000);
        HttpEntity entity = new DefaultHttpClient(httpParams).execute(new HttpGet(url)).getEntity();
        this.mTempFileSize = entity.getContentLength();
        if (this.mTempFileSize > 0) {
            return entity.getContent();
        }
        return null;
    }

    private void copyStream(InputStream in) throws IOException {
        byte[] buf = new byte[1024];
        while (!this.mIsCancelled) {
            int ch = in.read(buf);
            if (ch != -1) {
                this.mTotalRead += (long) ch;
            } else {
                return;
            }
        }
    }

    public void startDownload() {
        download(this.mDownloadUrl);
        if (!this.mIsCancelled) {
            this.mDownloadStateListener.onDownloadComplete(this.mDownloadUrl, System.currentTimeMillis() - this.mStartTime, this.mTotalRead);
        }
    }

    public void cancel() {
        this.mIsCancelled = true;
        this.mDownloadStateListener.onDownloadCancled(this.mDownloadUrl, System.currentTimeMillis() - this.mStartTime, this.mTotalRead);
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }
}
