package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.widget.MediaController.MediaPlayerControl;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import pl.droidsonroids.gif.transforms.CornerRadiusTransform;
import pl.droidsonroids.gif.transforms.Transform;

public class GifDrawable extends Drawable implements Animatable, MediaPlayerControl {
    final Bitmap mBuffer;
    private final Rect mDstRect;
    final ScheduledThreadPoolExecutor mExecutor;
    final InvalidationHandler mInvalidationHandler;
    final boolean mIsRenderingTriggeredOnDraw;
    volatile boolean mIsRunning;
    final ConcurrentLinkedQueue<AnimationListener> mListeners;
    final GifInfoHandle mNativeInfoHandle;
    long mNextFrameRenderTime;
    protected final Paint mPaint;
    private final RenderTask mRenderTask;
    ScheduledFuture<?> mRenderTaskSchedule;
    private int mScaledHeight;
    private int mScaledWidth;
    private final Rect mSrcRect;
    private ColorStateList mTint;
    private PorterDuffColorFilter mTintFilter;
    private Mode mTintMode;
    private Transform mTransform;

    public GifDrawable(Resources res, int id) throws NotFoundException, IOException {
        this(res.openRawResourceFd(id));
        float densityScale = GifViewUtils.getDensityScale(res, id);
        this.mScaledHeight = (int) (((float) this.mNativeInfoHandle.getHeight()) * densityScale);
        this.mScaledWidth = (int) (densityScale * ((float) this.mNativeInfoHandle.getWidth()));
    }

    public GifDrawable(AssetManager assets, String assetName) throws IOException {
        this(assets.openFd(assetName));
    }

    public GifDrawable(String filePath) throws IOException {
        this(new GifInfoHandle(filePath), null, null, true);
    }

    public GifDrawable(File file) throws IOException {
        this(file.getPath());
    }

    public GifDrawable(InputStream stream) throws IOException {
        this(new GifInfoHandle(stream), null, null, true);
    }

    public GifDrawable(AssetFileDescriptor afd) throws IOException {
        this(new GifInfoHandle(afd), null, null, true);
    }

    public GifDrawable(FileDescriptor fd) throws IOException {
        this(new GifInfoHandle(fd), null, null, true);
    }

    public GifDrawable(byte[] bytes) throws IOException {
        this(new GifInfoHandle(bytes), null, null, true);
    }

    public GifDrawable(ByteBuffer buffer) throws IOException {
        this(new GifInfoHandle(buffer), null, null, true);
    }

    public GifDrawable(ContentResolver resolver, Uri uri) throws IOException {
        this(GifInfoHandle.openUri(resolver, uri), null, null, true);
    }

    protected GifDrawable(InputSource inputSource, GifDrawable oldDrawable, ScheduledThreadPoolExecutor executor, boolean isRenderingTriggeredOnDraw, GifOptions options) throws IOException {
        this(inputSource.createHandleWith(options), oldDrawable, executor, isRenderingTriggeredOnDraw);
    }

    GifDrawable(GifInfoHandle gifInfoHandle, GifDrawable oldDrawable, ScheduledThreadPoolExecutor executor, boolean isRenderingTriggeredOnDraw) {
        boolean z = true;
        this.mIsRunning = true;
        this.mNextFrameRenderTime = Long.MIN_VALUE;
        this.mDstRect = new Rect();
        this.mPaint = new Paint(6);
        this.mListeners = new ConcurrentLinkedQueue();
        this.mRenderTask = new RenderTask(this);
        this.mIsRenderingTriggeredOnDraw = isRenderingTriggeredOnDraw;
        if (executor == null) {
            executor = GifRenderingExecutor.getInstance();
        }
        this.mExecutor = executor;
        this.mNativeInfoHandle = gifInfoHandle;
        Bitmap bitmap = null;
        if (oldDrawable != null) {
            synchronized (oldDrawable.mNativeInfoHandle) {
                if (!oldDrawable.mNativeInfoHandle.isRecycled() && oldDrawable.mNativeInfoHandle.getHeight() >= this.mNativeInfoHandle.getHeight() && oldDrawable.mNativeInfoHandle.getWidth() >= this.mNativeInfoHandle.getWidth()) {
                    oldDrawable.shutdown();
                    bitmap = oldDrawable.mBuffer;
                    bitmap.eraseColor(0);
                }
            }
        }
        if (bitmap == null) {
            this.mBuffer = Bitmap.createBitmap(this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight(), Config.ARGB_8888);
        } else {
            this.mBuffer = bitmap;
        }
        if (VERSION.SDK_INT >= 12) {
            bitmap = this.mBuffer;
            if (gifInfoHandle.isOpaque()) {
                z = false;
            }
            bitmap.setHasAlpha(z);
        }
        this.mSrcRect = new Rect(0, 0, this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight());
        this.mInvalidationHandler = new InvalidationHandler(this);
        this.mRenderTask.doWork();
        this.mScaledWidth = this.mNativeInfoHandle.getWidth();
        this.mScaledHeight = this.mNativeInfoHandle.getHeight();
    }

    public void recycle() {
        shutdown();
        this.mBuffer.recycle();
    }

    private void shutdown() {
        this.mIsRunning = false;
        this.mInvalidationHandler.removeMessages(-1);
        this.mNativeInfoHandle.recycle();
    }

    public boolean isRecycled() {
        return this.mNativeInfoHandle.isRecycled();
    }

    public int getIntrinsicHeight() {
        return this.mScaledHeight;
    }

    public int getIntrinsicWidth() {
        return this.mScaledWidth;
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        if (!this.mNativeInfoHandle.isOpaque() || this.mPaint.getAlpha() < 255) {
            return -2;
        }
        return -1;
    }

    public void start() {
        synchronized (this) {
            if (this.mIsRunning) {
                return;
            }
            this.mIsRunning = true;
            startAnimation(this.mNativeInfoHandle.restoreRemainder());
        }
    }

    void startAnimation(long lastFrameRemainder) {
        if (this.mIsRenderingTriggeredOnDraw) {
            this.mNextFrameRenderTime = 0;
            this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0);
            return;
        }
        cancelPendingRenderTask();
        this.mRenderTaskSchedule = this.mExecutor.schedule(this.mRenderTask, Math.max(lastFrameRemainder, 0), TimeUnit.MILLISECONDS);
    }

    public void reset() {
        this.mExecutor.execute(new 1(this, this));
    }

    public void stop() {
        synchronized (this) {
            if (this.mIsRunning) {
                this.mIsRunning = false;
                cancelPendingRenderTask();
                this.mNativeInfoHandle.saveRemainder();
                return;
            }
        }
    }

    private void cancelPendingRenderTask() {
        if (this.mRenderTaskSchedule != null) {
            this.mRenderTaskSchedule.cancel(false);
        }
        this.mInvalidationHandler.removeMessages(-1);
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public String getComment() {
        return this.mNativeInfoHandle.getComment();
    }

    public int getLoopCount() {
        return this.mNativeInfoHandle.getLoopCount();
    }

    public void setLoopCount(int loopCount) {
        this.mNativeInfoHandle.setLoopCount(loopCount);
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "GIF: size: %dx%d, frames: %d, error: %d", new Object[]{Integer.valueOf(this.mNativeInfoHandle.getWidth()), Integer.valueOf(this.mNativeInfoHandle.getHeight()), Integer.valueOf(this.mNativeInfoHandle.getNumberOfFrames()), Integer.valueOf(this.mNativeInfoHandle.getNativeErrorCode())});
    }

    public int getNumberOfFrames() {
        return this.mNativeInfoHandle.getNumberOfFrames();
    }

    public GifError getError() {
        return GifError.fromCode(this.mNativeInfoHandle.getNativeErrorCode());
    }

    public static GifDrawable createFromResource(Resources res, int resourceId) {
        try {
            return new GifDrawable(res, resourceId);
        } catch (IOException e) {
            return null;
        }
    }

    public void setSpeed(float factor) {
        this.mNativeInfoHandle.setSpeedFactor(factor);
    }

    public void pause() {
        stop();
    }

    public int getDuration() {
        return this.mNativeInfoHandle.getDuration();
    }

    public int getCurrentPosition() {
        return this.mNativeInfoHandle.getCurrentPosition();
    }

    public void seekTo(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position is not positive");
        }
        this.mExecutor.execute(new 2(this, this, position));
    }

    public void seekToFrame(int frameIndex) {
        if (frameIndex < 0) {
            throw new IndexOutOfBoundsException("Frame index is not positive");
        }
        this.mExecutor.execute(new 3(this, this, frameIndex));
    }

    public Bitmap seekToFrameAndGet(int frameIndex) {
        if (frameIndex < 0) {
            throw new IndexOutOfBoundsException("Frame index is not positive");
        }
        Bitmap currentFrame;
        synchronized (this.mNativeInfoHandle) {
            this.mNativeInfoHandle.seekToFrame(frameIndex, this.mBuffer);
            currentFrame = getCurrentFrame();
        }
        this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0);
        return currentFrame;
    }

    public Bitmap seekToPositionAndGet(int position) {
        if (position < 0) {
            throw new IllegalArgumentException("Position is not positive");
        }
        Bitmap currentFrame;
        synchronized (this.mNativeInfoHandle) {
            this.mNativeInfoHandle.seekToTime(position, this.mBuffer);
            currentFrame = getCurrentFrame();
        }
        this.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0);
        return currentFrame;
    }

    public boolean isPlaying() {
        return this.mIsRunning;
    }

    public int getBufferPercentage() {
        return 100;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return getNumberOfFrames() > 1;
    }

    public boolean canSeekForward() {
        return getNumberOfFrames() > 1;
    }

    public int getAudioSessionId() {
        return 0;
    }

    public int getFrameByteCount() {
        return this.mBuffer.getRowBytes() * this.mBuffer.getHeight();
    }

    @SuppressLint({"NewApi"})
    public long getAllocationByteCount() {
        long allocationByteCount = this.mNativeInfoHandle.getAllocationByteCount();
        if (VERSION.SDK_INT >= 19) {
            return allocationByteCount + ((long) this.mBuffer.getAllocationByteCount());
        }
        return allocationByteCount + ((long) getFrameByteCount());
    }

    public long getMetadataAllocationByteCount() {
        return this.mNativeInfoHandle.getMetadataByteCount();
    }

    public long getInputSourceByteCount() {
        return this.mNativeInfoHandle.getSourceLength();
    }

    public void getPixels(int[] pixels) {
        this.mBuffer.getPixels(pixels, 0, this.mNativeInfoHandle.getWidth(), 0, 0, this.mNativeInfoHandle.getWidth(), this.mNativeInfoHandle.getHeight());
    }

    public int getPixel(int x, int y) {
        if (x >= this.mNativeInfoHandle.getWidth()) {
            throw new IllegalArgumentException("x must be < width");
        } else if (y < this.mNativeInfoHandle.getHeight()) {
            return this.mBuffer.getPixel(x, y);
        } else {
            throw new IllegalArgumentException("y must be < height");
        }
    }

    protected void onBoundsChange(Rect bounds) {
        this.mDstRect.set(bounds);
        if (this.mTransform != null) {
            this.mTransform.onBoundsChange(bounds);
        }
    }

    @SuppressLint({"WrongCall"})
    public void draw(Canvas canvas) {
        Object obj;
        if (this.mTintFilter == null || this.mPaint.getColorFilter() != null) {
            obj = null;
        } else {
            this.mPaint.setColorFilter(this.mTintFilter);
            obj = 1;
        }
        if (this.mTransform == null) {
            canvas.drawBitmap(this.mBuffer, this.mSrcRect, this.mDstRect, this.mPaint);
        } else {
            this.mTransform.onDraw(canvas, this.mPaint, this.mBuffer);
        }
        if (obj != null) {
            this.mPaint.setColorFilter(null);
        }
        if (this.mIsRenderingTriggeredOnDraw && this.mIsRunning && this.mNextFrameRenderTime != Long.MIN_VALUE) {
            long max = Math.max(0, this.mNextFrameRenderTime - SystemClock.uptimeMillis());
            this.mNextFrameRenderTime = Long.MIN_VALUE;
            this.mExecutor.remove(this.mRenderTask);
            this.mRenderTaskSchedule = this.mExecutor.schedule(this.mRenderTask, max, TimeUnit.MILLISECONDS);
        }
    }

    public final Paint getPaint() {
        return this.mPaint;
    }

    public int getAlpha() {
        return this.mPaint.getAlpha();
    }

    public void setFilterBitmap(boolean filter) {
        this.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    @Deprecated
    public void setDither(boolean dither) {
        this.mPaint.setDither(dither);
        invalidateSelf();
    }

    public void addAnimationListener(AnimationListener listener) {
        this.mListeners.add(listener);
    }

    public boolean removeAnimationListener(AnimationListener listener) {
        return this.mListeners.remove(listener);
    }

    public ColorFilter getColorFilter() {
        return this.mPaint.getColorFilter();
    }

    public Bitmap getCurrentFrame() {
        Bitmap copy = this.mBuffer.copy(this.mBuffer.getConfig(), this.mBuffer.isMutable());
        if (VERSION.SDK_INT >= 12) {
            copy.setHasAlpha(this.mBuffer.hasAlpha());
        }
        return copy;
    }

    private PorterDuffColorFilter updateTintFilter(ColorStateList tint, Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        return new PorterDuffColorFilter(tint.getColorForState(getState(), 0), tintMode);
    }

    public void setTintList(ColorStateList tint) {
        this.mTint = tint;
        this.mTintFilter = updateTintFilter(tint, this.mTintMode);
        invalidateSelf();
    }

    public void setTintMode(Mode tintMode) {
        this.mTintMode = tintMode;
        this.mTintFilter = updateTintFilter(this.mTint, tintMode);
        invalidateSelf();
    }

    protected boolean onStateChange(int[] iArr) {
        if (this.mTint == null || this.mTintMode == null) {
            return false;
        }
        this.mTintFilter = updateTintFilter(this.mTint, this.mTintMode);
        return true;
    }

    public boolean isStateful() {
        return super.isStateful() || (this.mTint != null && this.mTint.isStateful());
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean visible2 = super.setVisible(visible, restart);
        if (!this.mIsRenderingTriggeredOnDraw) {
            if (visible) {
                if (restart) {
                    reset();
                }
                if (visible2) {
                    start();
                }
            } else if (visible2) {
                stop();
            }
        }
        return visible2;
    }

    public int getCurrentFrameIndex() {
        return this.mNativeInfoHandle.getCurrentFrameIndex();
    }

    public int getCurrentLoop() {
        int currentLoop = this.mNativeInfoHandle.getCurrentLoop();
        return (currentLoop == 0 || currentLoop < this.mNativeInfoHandle.getLoopCount()) ? currentLoop : currentLoop - 1;
    }

    public boolean isAnimationCompleted() {
        return this.mNativeInfoHandle.isAnimationCompleted();
    }

    public int getFrameDuration(int index) {
        return this.mNativeInfoHandle.getFrameDuration(index);
    }

    public void setCornerRadius(float cornerRadius) {
        this.mTransform = new CornerRadiusTransform(cornerRadius);
    }

    public float getCornerRadius() {
        if (this.mTransform instanceof CornerRadiusTransform) {
            return ((CornerRadiusTransform) this.mTransform).getCornerRadius();
        }
        return 0.0f;
    }

    public void setTransform(Transform transform) {
        this.mTransform = transform;
    }

    public Transform getTransform() {
        return this.mTransform;
    }
}
