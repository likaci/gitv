package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.widget.ImageView.ScaleType;
import com.gala.imageprovider.a;
import java.io.IOException;
import java.lang.ref.WeakReference;
import pl.droidsonroids.gif.InputSource.AssetSource;
import pl.droidsonroids.gif.InputSource.ResourcesSource;
import pl.droidsonroids.gif.annotations.Beta;

@SuppressLint({"NewApi"})
public class GifTextureView extends TextureView {
    private static final ScaleType[] sScaleTypeArray = new ScaleType[]{ScaleType.MATRIX, ScaleType.FIT_XY, ScaleType.FIT_START, ScaleType.FIT_CENTER, ScaleType.FIT_END, ScaleType.CENTER, ScaleType.CENTER_CROP, ScaleType.CENTER_INSIDE};
    private boolean mFreezesAnimation;
    private InputSource mInputSource;
    private RenderThread mRenderThread;
    private ScaleType mScaleType = ScaleType.FIT_CENTER;
    private float mSpeedFactor = 1.0f;
    private final Matrix mTransform = new Matrix();

    static /* synthetic */ class C22101 {
        static final /* synthetic */ int[] $SwitchMap$android$widget$ImageView$ScaleType = new int[ScaleType.values().length];

        static {
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_CROP.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.CENTER_INSIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_CENTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_END.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_START.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.FIT_XY.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                $SwitchMap$android$widget$ImageView$ScaleType[ScaleType.MATRIX.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
        }
    }

    @Beta
    public interface PlaceholderDrawListener {
        void onDrawPlaceholder(Canvas canvas);
    }

    static class RenderThread extends Thread implements SurfaceTextureListener {
        final ConditionVariable isSurfaceValid = new ConditionVariable();
        private GifInfoHandle mGifInfoHandle = new GifInfoHandle();
        private final WeakReference<GifTextureView> mGifTextureViewReference;
        private IOException mIOException;
        long[] mSavedState;

        RenderThread(GifTextureView gifTextureView) {
            super("GifRenderThread");
            this.mGifTextureViewReference = new WeakReference(gifTextureView);
        }

        public void run() {
            try {
                GifTextureView gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
                if (gifTextureView != null) {
                    this.mGifInfoHandle = gifTextureView.mInputSource.open();
                    this.mGifInfoHandle.setOptions('\u0001', gifTextureView.isOpaque());
                    gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
                    if (gifTextureView == null) {
                        this.mGifInfoHandle.recycle();
                        return;
                    }
                    super.setSurfaceTextureListener(this);
                    boolean isAvailable = gifTextureView.isAvailable();
                    this.isSurfaceValid.set(isAvailable);
                    if (isAvailable) {
                        gifTextureView.post(new Runnable() {
                            public void run() {
                                gifTextureView.updateTextureViewSize(RenderThread.this.mGifInfoHandle);
                            }
                        });
                    }
                    this.mGifInfoHandle.setSpeedFactor(gifTextureView.mSpeedFactor);
                    while (!isInterrupted()) {
                        try {
                            this.isSurfaceValid.block();
                            SurfaceTexture surfaceTexture = gifTextureView.getSurfaceTexture();
                            if (surfaceTexture != null) {
                                Surface surface = new Surface(surfaceTexture);
                                try {
                                    this.mGifInfoHandle.bindSurface(surface, this.mSavedState);
                                } finally {
                                    surface.release();
                                    surfaceTexture.release();
                                }
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                    this.mGifInfoHandle.recycle();
                    this.mGifInfoHandle = new GifInfoHandle();
                }
            } catch (IOException e2) {
                this.mIOException = e2;
            }
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            GifTextureView gifTextureView = (GifTextureView) this.mGifTextureViewReference.get();
            if (gifTextureView != null) {
                gifTextureView.updateTextureViewSize(this.mGifInfoHandle);
            }
            this.isSurfaceValid.open();
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            this.isSurfaceValid.close();
            this.mGifInfoHandle.postUnbindSurface();
            return false;
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        void dispose(GifTextureView gifTextureView, PlaceholderDrawListener drawer) {
            this.isSurfaceValid.close();
            super.setSurfaceTextureListener(drawer != null ? new PlaceholderDrawingSurfaceTextureListener(drawer) : null);
            this.mGifInfoHandle.postUnbindSurface();
            interrupt();
        }
    }

    public GifTextureView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    public GifTextureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs != null) {
            int attributeIntValue = attrs.getAttributeIntValue("http://schemas.android.com/apk/res/android", "scaleType", -1);
            if (attributeIntValue >= 0 && attributeIntValue < sScaleTypeArray.length) {
                this.mScaleType = sScaleTypeArray[attributeIntValue];
            }
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attrs, a.a, defStyleAttr, defStyleRes);
            this.mInputSource = findSource(obtainStyledAttributes);
            super.setOpaque(obtainStyledAttributes.getBoolean(1, false));
            obtainStyledAttributes.recycle();
            this.mFreezesAnimation = GifViewUtils.isFreezingAnimation(this, attrs, defStyleAttr, defStyleRes);
        } else {
            super.setOpaque(false);
        }
        if (!isInEditMode()) {
            this.mRenderThread = new RenderThread(this);
            if (this.mInputSource != null) {
                this.mRenderThread.start();
            }
        }
    }

    public void setSurfaceTextureListener(SurfaceTextureListener surfaceTextureListener) {
        throw new UnsupportedOperationException("Changing SurfaceTextureListener is not supported");
    }

    public SurfaceTextureListener getSurfaceTextureListener() {
        return null;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        throw new UnsupportedOperationException("Changing SurfaceTexture is not supported");
    }

    private static InputSource findSource(TypedArray textureViewAttributes) {
        TypedValue typedValue = new TypedValue();
        if (!textureViewAttributes.getValue(0, typedValue)) {
            return null;
        }
        if (typedValue.resourceId != 0) {
            String resourceTypeName = textureViewAttributes.getResources().getResourceTypeName(typedValue.resourceId);
            if (GifViewUtils.SUPPORTED_RESOURCE_TYPE_NAMES.contains(resourceTypeName)) {
                return new ResourcesSource(textureViewAttributes.getResources(), typedValue.resourceId);
            }
            if (!"string".equals(resourceTypeName)) {
                throw new IllegalArgumentException("Expected string, drawable, mipmap or raw resource type. '" + resourceTypeName + "' is not supported");
            }
        }
        return new AssetSource(textureViewAttributes.getResources().getAssets(), typedValue.string.toString());
    }

    private void setSuperSurfaceTextureListener(SurfaceTextureListener listener) {
        super.setSurfaceTextureListener(listener);
    }

    public void setOpaque(boolean opaque) {
        if (opaque != isOpaque()) {
            super.setOpaque(opaque);
            setInputSource(this.mInputSource);
        }
    }

    protected void onDetachedFromWindow() {
        this.mRenderThread.dispose(this, null);
        super.onDetachedFromWindow();
        SurfaceTexture surfaceTexture = getSurfaceTexture();
        if (surfaceTexture != null) {
            surfaceTexture.release();
        }
    }

    public synchronized void setInputSource(InputSource inputSource) {
        setInputSource(inputSource, null);
    }

    @Beta
    public synchronized void setInputSource(InputSource inputSource, PlaceholderDrawListener placeholderDrawListener) {
        this.mRenderThread.dispose(this, placeholderDrawListener);
        this.mInputSource = inputSource;
        this.mRenderThread = new RenderThread(this);
        if (inputSource != null) {
            this.mRenderThread.start();
        }
    }

    public void setSpeed(float factor) {
        this.mSpeedFactor = factor;
        this.mRenderThread.mGifInfoHandle.setSpeedFactor(factor);
    }

    public IOException getIOException() {
        if (this.mRenderThread.mIOException != null) {
            return this.mRenderThread.mIOException;
        }
        return GifIOException.fromCode(this.mRenderThread.mGifInfoHandle.getNativeErrorCode());
    }

    public void setScaleType(ScaleType scaleType) {
        this.mScaleType = scaleType;
        updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }

    public ScaleType getScaleType() {
        return this.mScaleType;
    }

    private void updateTextureViewSize(GifInfoHandle gifInfoHandle) {
        float f = 1.0f;
        Matrix matrix = new Matrix();
        float width = (float) getWidth();
        float height = (float) getHeight();
        float width2 = ((float) gifInfoHandle.getWidth()) / width;
        float height2 = ((float) gifInfoHandle.getHeight()) / height;
        RectF rectF = new RectF(0.0f, 0.0f, (float) gifInfoHandle.getWidth(), (float) gifInfoHandle.getHeight());
        RectF rectF2 = new RectF(0.0f, 0.0f, width, height);
        switch (C22101.$SwitchMap$android$widget$ImageView$ScaleType[this.mScaleType.ordinal()]) {
            case 1:
                matrix.setScale(width2, height2, width / 2.0f, height / 2.0f);
                break;
            case 2:
                f = 1.0f / Math.min(width2, height2);
                matrix.setScale(width2 * f, f * height2, width / 2.0f, height / 2.0f);
                break;
            case 3:
                if (((float) gifInfoHandle.getWidth()) > width || ((float) gifInfoHandle.getHeight()) > height) {
                    f = Math.min(1.0f / width2, 1.0f / height2);
                }
                matrix.setScale(width2 * f, f * height2, width / 2.0f, height / 2.0f);
                break;
            case 4:
                matrix.setRectToRect(rectF, rectF2, ScaleToFit.CENTER);
                matrix.preScale(width2, height2);
                break;
            case 5:
                matrix.setRectToRect(rectF, rectF2, ScaleToFit.END);
                matrix.preScale(width2, height2);
                break;
            case 6:
                matrix.setRectToRect(rectF, rectF2, ScaleToFit.START);
                matrix.preScale(width2, height2);
                break;
            case 7:
                return;
            case 8:
                matrix.set(this.mTransform);
                matrix.preScale(width2, height2);
                break;
        }
        super.setTransform(matrix);
    }

    public void setImageMatrix(Matrix matrix) {
        setTransform(matrix);
    }

    public void setTransform(Matrix transform) {
        this.mTransform.set(transform);
        updateTextureViewSize(this.mRenderThread.mGifInfoHandle);
    }

    public Matrix getTransform(Matrix transform) {
        if (transform == null) {
            transform = new Matrix();
        }
        transform.set(this.mTransform);
        return transform;
    }

    public Parcelable onSaveInstanceState() {
        this.mRenderThread.mSavedState = this.mRenderThread.mGifInfoHandle.getSavedState();
        return new GifViewSavedState(super.onSaveInstanceState(), this.mFreezesAnimation ? this.mRenderThread.mSavedState : null);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof GifViewSavedState) {
            GifViewSavedState gifViewSavedState = (GifViewSavedState) state;
            super.onRestoreInstanceState(gifViewSavedState.getSuperState());
            this.mRenderThread.mSavedState = gifViewSavedState.mStates[0];
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public void setFreezesAnimation(boolean freezesAnimation) {
        this.mFreezesAnimation = freezesAnimation;
    }
}
