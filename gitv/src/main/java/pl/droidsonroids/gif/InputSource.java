package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public abstract class InputSource {

    public static class AssetFileDescriptorSource extends InputSource {
        private final AssetFileDescriptor mAssetFileDescriptor;

        public AssetFileDescriptorSource(AssetFileDescriptor assetFileDescriptor) {
            super();
            this.mAssetFileDescriptor = assetFileDescriptor;
        }

        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetFileDescriptor);
        }
    }

    public static final class AssetSource extends InputSource {
        private final AssetManager mAssetManager;
        private final String mAssetName;

        public AssetSource(AssetManager assetManager, String assetName) {
            super();
            this.mAssetManager = assetManager;
            this.mAssetName = assetName;
        }

        final GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mAssetManager.openFd(this.mAssetName));
        }
    }

    public static final class ByteArraySource extends InputSource {
        private final byte[] bytes;

        public ByteArraySource(byte[] bytes) {
            super();
            this.bytes = bytes;
        }

        final GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.bytes);
        }
    }

    public static final class DirectByteBufferSource extends InputSource {
        private final ByteBuffer byteBuffer;

        public DirectByteBufferSource(ByteBuffer byteBuffer) {
            super();
            this.byteBuffer = byteBuffer;
        }

        final GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.byteBuffer);
        }
    }

    public static final class FileDescriptorSource extends InputSource {
        private final FileDescriptor mFd;

        public FileDescriptorSource(FileDescriptor fileDescriptor) {
            super();
            this.mFd = fileDescriptor;
        }

        final GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mFd);
        }
    }

    public static final class FileSource extends InputSource {
        private final String mPath;

        public FileSource(File file) {
            super();
            this.mPath = file.getPath();
        }

        public FileSource(String filePath) {
            super();
            this.mPath = filePath;
        }

        final GifInfoHandle open() throws GifIOException {
            return new GifInfoHandle(this.mPath);
        }
    }

    public static final class InputStreamSource extends InputSource {
        private final InputStream inputStream;

        public InputStreamSource(InputStream inputStream) {
            super();
            this.inputStream = inputStream;
        }

        final GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.inputStream);
        }
    }

    public static class ResourcesSource extends InputSource {
        private final int mResourceId;
        private final Resources mResources;

        public ResourcesSource(Resources resources, int resourceId) {
            super();
            this.mResources = resources;
            this.mResourceId = resourceId;
        }

        GifInfoHandle open() throws IOException {
            return new GifInfoHandle(this.mResources.openRawResourceFd(this.mResourceId));
        }
    }

    public static final class UriSource extends InputSource {
        private final ContentResolver mContentResolver;
        private final Uri mUri;

        public UriSource(ContentResolver contentResolver, Uri uri) {
            super();
            this.mContentResolver = contentResolver;
            this.mUri = uri;
        }

        final GifInfoHandle open() throws IOException {
            return GifInfoHandle.openUri(this.mContentResolver, this.mUri);
        }
    }

    abstract GifInfoHandle open() throws IOException;

    private InputSource() {
    }

    final GifDrawable build(GifDrawable oldDrawable, ScheduledThreadPoolExecutor executor, boolean isRenderingAlwaysEnabled, GifOptions options) throws IOException {
        return new GifDrawable(createHandleWith(options), oldDrawable, executor, isRenderingAlwaysEnabled);
    }

    final GifInfoHandle createHandleWith(GifOptions options) throws IOException {
        GifInfoHandle open = open();
        open.setOptions(options.inSampleSize, options.inIsOpaque);
        return open;
    }
}
