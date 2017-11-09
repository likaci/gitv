package com.squareup.picasso;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.NetworkInfo;
import com.squareup.picasso.Downloader.Response;
import com.squareup.picasso.Picasso.LoadedFrom;
import java.io.IOException;
import java.io.InputStream;

class NetworkBitmapHunter extends BitmapHunter {
    static final int DEFAULT_RETRY_COUNT = 2;
    private static final int MARKER = 65536;
    private final Downloader downloader;
    int retryCount = 2;

    public NetworkBitmapHunter(Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action, Downloader downloader) {
        super(picasso, dispatcher, cache, stats, action);
        this.downloader = downloader;
    }

    Bitmap decode(Request data) throws IOException {
        Response response = this.downloader.load(data.uri, this.retryCount == 0);
        if (response == null) {
            return null;
        }
        this.loadedFrom = response.cached ? LoadedFrom.DISK : LoadedFrom.NETWORK;
        Bitmap bitmap = response.getBitmap();
        if (bitmap != null) {
            return bitmap;
        }
        InputStream is = response.getInputStream();
        if (is == null) {
            return null;
        }
        if (response.getContentLength() == 0) {
            Utils.closeQuietly(is);
            throw new IOException("Received response with 0 content-length header.");
        }
        if (this.loadedFrom == LoadedFrom.NETWORK && response.getContentLength() > 0) {
            this.stats.dispatchDownloadFinished(response.getContentLength());
        }
        try {
            bitmap = decodeStream(is, data);
            return bitmap;
        } finally {
            Utils.closeQuietly(is);
        }
    }

    boolean shouldRetry(boolean airplaneMode, NetworkInfo info) {
        boolean hasRetries;
        if (this.retryCount > 0) {
            hasRetries = true;
        } else {
            hasRetries = false;
        }
        if (!hasRetries) {
            return false;
        }
        this.retryCount--;
        if (info == null || info.isConnected()) {
            return true;
        }
        return false;
    }

    boolean supportsReplay() {
        return true;
    }

    private Bitmap decodeStream(InputStream stream, Request data) throws IOException {
        InputStream markStream = new MarkableInputStream(stream);
        stream = markStream;
        long mark = markStream.savePosition(65536);
        Options options = BitmapHunter.createBitmapOptions(data);
        boolean calculateSize = BitmapHunter.requiresInSampleSize(options);
        boolean isWebPFile = Utils.isWebPFile(stream);
        markStream.reset(mark);
        if (isWebPFile) {
            byte[] bytes = Utils.toByteArray(stream);
            if (calculateSize) {
                BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
                BitmapHunter.calculateInSampleSize(data.targetWidth, data.targetHeight, options);
            }
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        }
        if (calculateSize) {
            BitmapFactory.decodeStream(stream, null, options);
            BitmapHunter.calculateInSampleSize(data.targetWidth, data.targetHeight, options);
            markStream.reset(mark);
        }
        Bitmap bitmap = BitmapFactory.decodeStream(stream, null, options);
        if (bitmap != null) {
            return bitmap;
        }
        throw new IOException("Failed to decode stream.");
    }
}
