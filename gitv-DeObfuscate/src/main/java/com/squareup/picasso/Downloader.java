package com.squareup.picasso;

import android.graphics.Bitmap;
import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;

public interface Downloader {

    public static class Response {
        final Bitmap bitmap;
        final boolean cached;
        final long contentLength;
        final InputStream stream;

        @Deprecated
        public Response(Bitmap bitmap, boolean loadedFromCache) {
            this(bitmap, loadedFromCache, -1);
        }

        @Deprecated
        public Response(InputStream stream, boolean loadedFromCache) {
            this(stream, loadedFromCache, -1);
        }

        public Response(Bitmap bitmap, boolean loadedFromCache, long contentLength) {
            if (bitmap == null) {
                throw new IllegalArgumentException("Bitmap may not be null.");
            }
            this.stream = null;
            this.bitmap = bitmap;
            this.cached = loadedFromCache;
            this.contentLength = contentLength;
        }

        public Response(InputStream stream, boolean loadedFromCache, long contentLength) {
            if (stream == null) {
                throw new IllegalArgumentException("Stream may not be null.");
            }
            this.stream = stream;
            this.bitmap = null;
            this.cached = loadedFromCache;
            this.contentLength = contentLength;
        }

        public InputStream getInputStream() {
            return this.stream;
        }

        public Bitmap getBitmap() {
            return this.bitmap;
        }

        public long getContentLength() {
            return this.contentLength;
        }
    }

    public static class ResponseException extends IOException {
        public ResponseException(String message) {
            super(message);
        }
    }

    Response load(Uri uri, boolean z) throws IOException;
}
