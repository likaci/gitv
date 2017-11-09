package pl.droidsonroids.gif;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.xbill.DNS.Message;

final class GifInfoHandle {
    private volatile long gifInfoPtr;

    private static native void bindSurface(long j, Surface surface, long[] jArr);

    private static native void free(long j);

    private static native long getAllocationByteCount(long j);

    private static native String getComment(long j);

    private static native int getCurrentFrameIndex(long j);

    private static native int getCurrentLoop(long j);

    private static native int getCurrentPosition(long j);

    private static native int getDuration(long j);

    private static native int getFrameDuration(long j, int i);

    private static native int getHeight(long j);

    private static native int getLoopCount(long j);

    private static native long getMetadataByteCount(long j);

    private static native int getNativeErrorCode(long j);

    private static native int getNumberOfFrames(long j);

    private static native long[] getSavedState(long j);

    private static native long getSourceLength(long j);

    private static native int getWidth(long j);

    private static native void glTexImage2D(long j, int i, int i2);

    private static native void glTexSubImage2D(long j, int i, int i2);

    private static native void initTexImageDescriptor(long j);

    private static native boolean isAnimationCompleted(long j);

    private static native boolean isOpaque(long j);

    static native long openByteArray(byte[] bArr) throws GifIOException;

    static native long openDirectByteBuffer(ByteBuffer byteBuffer) throws GifIOException;

    static native long openFd(FileDescriptor fileDescriptor, long j) throws GifIOException;

    static native long openFile(String str) throws GifIOException;

    static native long openStream(InputStream inputStream) throws GifIOException;

    private static native void postUnbindSurface(long j);

    private static native long renderFrame(long j, Bitmap bitmap);

    private static native boolean reset(long j);

    private static native long restoreRemainder(long j);

    private static native int restoreSavedState(long j, long[] jArr, Bitmap bitmap);

    private static native void saveRemainder(long j);

    private static native void seekToFrame(long j, int i, Bitmap bitmap);

    private static native void seekToFrameGL(long j, int i);

    private static native void seekToTime(long j, int i, Bitmap bitmap);

    private static native void setLoopCount(long j, char c);

    private static native void setOptions(long j, char c, boolean z);

    private static native void setSpeedFactor(long j, float f);

    private static native void startDecoderThread(long j);

    private static native void stopDecoderThread(long j);

    static {
        LibraryLoader.loadLibrary(null);
    }

    GifInfoHandle() {
    }

    GifInfoHandle(FileDescriptor fd) throws GifIOException {
        this.gifInfoPtr = openFd(fd, 0);
    }

    GifInfoHandle(byte[] bytes) throws GifIOException {
        this.gifInfoPtr = openByteArray(bytes);
    }

    GifInfoHandle(ByteBuffer buffer) throws GifIOException {
        this.gifInfoPtr = openDirectByteBuffer(buffer);
    }

    GifInfoHandle(String filePath) throws GifIOException {
        this.gifInfoPtr = openFile(filePath);
    }

    GifInfoHandle(InputStream stream) throws GifIOException {
        if (stream.markSupported()) {
            this.gifInfoPtr = openStream(stream);
            return;
        }
        throw new IllegalArgumentException("InputStream does not support marking");
    }

    GifInfoHandle(AssetFileDescriptor afd) throws IOException {
        try {
            this.gifInfoPtr = openFd(afd.getFileDescriptor(), afd.getStartOffset());
        } finally {
            try {
                afd.close();
            } catch (IOException e) {
            }
        }
    }

    static GifInfoHandle openUri(ContentResolver resolver, Uri uri) throws IOException {
        if ("file".equals(uri.getScheme())) {
            return new GifInfoHandle(uri.getPath());
        }
        return new GifInfoHandle(resolver.openAssetFileDescriptor(uri, "r"));
    }

    final synchronized long renderFrame(Bitmap frameBuffer) {
        return renderFrame(this.gifInfoPtr, frameBuffer);
    }

    final void bindSurface(Surface surface, long[] savedState) {
        bindSurface(this.gifInfoPtr, surface, savedState);
    }

    final synchronized void recycle() {
        free(this.gifInfoPtr);
        this.gifInfoPtr = 0;
    }

    final synchronized long restoreRemainder() {
        return restoreRemainder(this.gifInfoPtr);
    }

    final synchronized boolean reset() {
        return reset(this.gifInfoPtr);
    }

    final synchronized void saveRemainder() {
        saveRemainder(this.gifInfoPtr);
    }

    final synchronized String getComment() {
        return getComment(this.gifInfoPtr);
    }

    final synchronized int getLoopCount() {
        return getLoopCount(this.gifInfoPtr);
    }

    final void setLoopCount(int loopCount) {
        if (loopCount < 0 || loopCount > Message.MAXLENGTH) {
            throw new IllegalArgumentException("Loop count of range <0, 65535>");
        }
        synchronized (this) {
            setLoopCount(this.gifInfoPtr, (char) loopCount);
        }
    }

    final synchronized long getSourceLength() {
        return getSourceLength(this.gifInfoPtr);
    }

    final synchronized int getNativeErrorCode() {
        return getNativeErrorCode(this.gifInfoPtr);
    }

    final void setSpeedFactor(float factor) {
        if (factor <= 0.0f || Float.isNaN(factor)) {
            throw new IllegalArgumentException("Speed factor is not positive");
        }
        if (factor < 4.656613E-10f) {
            factor = 4.656613E-10f;
        }
        synchronized (this) {
            setSpeedFactor(this.gifInfoPtr, factor);
        }
    }

    final synchronized int getDuration() {
        return getDuration(this.gifInfoPtr);
    }

    final synchronized int getCurrentPosition() {
        return getCurrentPosition(this.gifInfoPtr);
    }

    final synchronized int getCurrentFrameIndex() {
        return getCurrentFrameIndex(this.gifInfoPtr);
    }

    final synchronized int getCurrentLoop() {
        return getCurrentLoop(this.gifInfoPtr);
    }

    final synchronized void seekToTime(int position, Bitmap buffer) {
        seekToTime(this.gifInfoPtr, position, buffer);
    }

    final synchronized void seekToFrame(int frameIndex, Bitmap buffer) {
        seekToFrame(this.gifInfoPtr, frameIndex, buffer);
    }

    final synchronized long getAllocationByteCount() {
        return getAllocationByteCount(this.gifInfoPtr);
    }

    final synchronized long getMetadataByteCount() {
        return getMetadataByteCount(this.gifInfoPtr);
    }

    final synchronized boolean isRecycled() {
        return this.gifInfoPtr == 0;
    }

    protected final void finalize() throws Throwable {
        try {
            recycle();
        } finally {
            super.finalize();
        }
    }

    final synchronized void postUnbindSurface() {
        postUnbindSurface(this.gifInfoPtr);
    }

    final synchronized boolean isAnimationCompleted() {
        return isAnimationCompleted(this.gifInfoPtr);
    }

    final synchronized long[] getSavedState() {
        return getSavedState(this.gifInfoPtr);
    }

    final synchronized int restoreSavedState(long[] savedState, Bitmap mBuffer) {
        return restoreSavedState(this.gifInfoPtr, savedState, mBuffer);
    }

    final synchronized int getFrameDuration(int index) {
        throwIfFrameIndexOutOfBounds(index);
        return getFrameDuration(this.gifInfoPtr, index);
    }

    final void setOptions(char sampleSize, boolean isOpaque) {
        setOptions(this.gifInfoPtr, sampleSize, isOpaque);
    }

    final synchronized int getWidth() {
        return getWidth(this.gifInfoPtr);
    }

    final synchronized int getHeight() {
        return getHeight(this.gifInfoPtr);
    }

    final synchronized int getNumberOfFrames() {
        return getNumberOfFrames(this.gifInfoPtr);
    }

    final synchronized boolean isOpaque() {
        return isOpaque(this.gifInfoPtr);
    }

    final void glTexImage2D(int target, int level) {
        glTexImage2D(this.gifInfoPtr, target, level);
    }

    final void glTexSubImage2D(int target, int level) {
        glTexSubImage2D(this.gifInfoPtr, target, level);
    }

    final void startDecoderThread() {
        startDecoderThread(this.gifInfoPtr);
    }

    final void stopDecoderThread() {
        stopDecoderThread(this.gifInfoPtr);
    }

    final void initTexImageDescriptor() {
        initTexImageDescriptor(this.gifInfoPtr);
    }

    final void seekToFrameGL(int index) {
        throwIfFrameIndexOutOfBounds(index);
        seekToFrameGL(this.gifInfoPtr, index);
    }

    private void throwIfFrameIndexOutOfBounds(int index) {
        float numberOfFrames = (float) getNumberOfFrames(this.gifInfoPtr);
        if (index < 0 || ((float) index) >= numberOfFrames) {
            throw new IndexOutOfBoundsException("Frame index is not in range <0;" + numberOfFrames + '>');
        }
    }
}
