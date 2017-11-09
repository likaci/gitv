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
import pl.droidsonroids.gif.InputSource.AssetFileDescriptorSource;
import pl.droidsonroids.gif.InputSource.AssetSource;
import pl.droidsonroids.gif.InputSource.ByteArraySource;
import pl.droidsonroids.gif.InputSource.DirectByteBufferSource;
import pl.droidsonroids.gif.InputSource.FileDescriptorSource;
import pl.droidsonroids.gif.InputSource.FileSource;
import pl.droidsonroids.gif.InputSource.InputStreamSource;
import pl.droidsonroids.gif.InputSource.ResourcesSource;
import pl.droidsonroids.gif.InputSource.UriSource;
import pl.droidsonroids.gif.annotations.Beta;

public abstract class GifDrawableInit<T extends GifDrawableInit<T>> {
    private ScheduledThreadPoolExecutor mExecutor;
    private InputSource mInputSource;
    private boolean mIsRenderingTriggeredOnDraw = true;
    private GifDrawable mOldDrawable;
    private GifOptions mOptions = new GifOptions();

    protected abstract T self();

    public T sampleSize(int sampleSize) {
        this.mOptions.setInSampleSize(sampleSize);
        return self();
    }

    public GifDrawable build() throws IOException {
        if (this.mInputSource != null) {
            return this.mInputSource.build(this.mOldDrawable, this.mExecutor, this.mIsRenderingTriggeredOnDraw, this.mOptions);
        }
        throw new NullPointerException("Source is not set");
    }

    public T with(GifDrawable drawable) {
        this.mOldDrawable = drawable;
        return self();
    }

    public T threadPoolSize(int threadPoolSize) {
        this.mExecutor = new ScheduledThreadPoolExecutor(threadPoolSize);
        return self();
    }

    public T taskExecutor(ScheduledThreadPoolExecutor executor) {
        this.mExecutor = executor;
        return self();
    }

    public T renderingTriggeredOnDraw(boolean isRenderingTriggeredOnDraw) {
        this.mIsRenderingTriggeredOnDraw = isRenderingTriggeredOnDraw;
        return self();
    }

    public T setRenderingTriggeredOnDraw(boolean isRenderingTriggeredOnDraw) {
        return renderingTriggeredOnDraw(isRenderingTriggeredOnDraw);
    }

    @Beta
    public T options(GifOptions options) {
        this.mOptions.setFrom(options);
        return self();
    }

    public T from(InputStream inputStream) {
        this.mInputSource = new InputStreamSource(inputStream);
        return self();
    }

    public T from(AssetFileDescriptor assetFileDescriptor) {
        this.mInputSource = new AssetFileDescriptorSource(assetFileDescriptor);
        return self();
    }

    public T from(FileDescriptor fileDescriptor) {
        this.mInputSource = new FileDescriptorSource(fileDescriptor);
        return self();
    }

    public T from(AssetManager assetManager, String assetName) {
        this.mInputSource = new AssetSource(assetManager, assetName);
        return self();
    }

    public T from(ContentResolver contentResolver, Uri uri) {
        this.mInputSource = new UriSource(contentResolver, uri);
        return self();
    }

    public T from(File file) {
        this.mInputSource = new FileSource(file);
        return self();
    }

    public T from(String filePath) {
        this.mInputSource = new FileSource(filePath);
        return self();
    }

    public T from(byte[] bytes) {
        this.mInputSource = new ByteArraySource(bytes);
        return self();
    }

    public T from(ByteBuffer byteBuffer) {
        this.mInputSource = new DirectByteBufferSource(byteBuffer);
        return self();
    }

    public T from(Resources resources, int resourceId) {
        this.mInputSource = new ResourcesSource(resources, resourceId);
        return self();
    }

    public InputSource getInputSource() {
        return this.mInputSource;
    }

    public GifDrawable getOldDrawable() {
        return this.mOldDrawable;
    }

    public ScheduledThreadPoolExecutor getExecutor() {
        return this.mExecutor;
    }

    public boolean isRenderingTriggeredOnDraw() {
        return this.mIsRenderingTriggeredOnDraw;
    }

    public GifOptions getOptions() {
        return this.mOptions;
    }
}
