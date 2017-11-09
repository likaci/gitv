package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.views.ISeekBar.OnSeekBarChangeListener;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class HaloSeekBar extends View implements ISeekBar {
    private static final String TAG = "Player/Ui/HaloSeekBar";
    protected int mBackgroundColor;
    protected Rect mBackgroundRect;
    private int mFristProgress;
    private int mHeight;
    private boolean mIsDragging;
    private int mMarginTop;
    private int mMax;
    private int mMiddleColor;
    private int mOffset;
    private OnSeekBarChangeListener mOnSeekBarChangeListener;
    private int mPaddingLeft;
    private int mPaddingRight;
    protected Paint mPaint;
    private int mProgress;
    protected int mProgressColor;
    private int mProgressHeigt;
    private int mRadius;
    protected Rect mRectProgress;
    protected Rect mRectSecondaryProgress;
    private int mSecondProgress;
    private float mSecondSection;
    private int mSecondaryProgress;
    protected int mSecondaryProgressColor;
    private float mSection;
    private int mSeekBarWidth;
    protected Bitmap mThumb;
    private int mThumbColor;
    private int mThumbLength;
    protected NinePatch mThumbNinePatch;
    private int mThumbOffset;

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int progress;
        int secondaryProgress;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.progress = in.readInt();
            this.secondaryProgress = in.readInt();
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.progress);
            out.writeInt(this.secondaryProgress);
        }
    }

    public HaloSeekBar(Context context) {
        this(context, null);
    }

    public HaloSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HaloSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsDragging = false;
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.HaloSeekBar);
        this.mProgress = array.getInteger(R.styleable.HaloSeekBar_progress, 0);
        this.mSecondaryProgress = array.getInteger(R.styleable.HaloSeekBar_secondaryProgress, 0);
        this.mMax = array.getInteger(R.styleable.HaloSeekBar_max, 100);
        this.mSecondaryProgressColor = array.getColor(R.styleable.HaloSeekBar_secondaryProgressColor, 1719828865);
        this.mBackgroundColor = array.getColor(R.styleable.HaloSeekBar_backgroud, 1283621249);
        this.mProgressColor = array.getColor(R.styleable.HaloSeekBar_progressColor, 1528064);
        this.mMiddleColor = Color.parseColor("#10c607");
        this.mThumbColor = Color.parseColor("#96ff00");
        array.recycle();
        this.mThumbLength = context.getResources().getDimensionPixelSize(R.dimen.dimen_200dp);
        this.mPaint = new Paint();
    }

    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.progress = this.mProgress;
        ss.secondaryProgress = this.mSecondaryProgress;
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        setProgress(ss.progress);
        setSecondaryProgress(ss.secondaryProgress);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        this.mHeight = (int) getResources().getDimension(R.dimen.dimen_12dp);
        setMeasuredDimension(width, height);
    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (LogUtils.mIsDebug) {
            LogUtils.i(TAG, "onLayout changed=" + changed + " (" + left + "," + top + "," + right + "," + bottom + ")");
        }
        if (changed) {
            initProgress();
            postInvalidate();
        }
    }

    public void setThumb(int resId) {
    }

    public void setThumbOffset(int offset) {
        if (this.mThumbOffset != offset) {
            updateProgressPos();
            postInvalidate();
        }
    }

    public synchronized void setMax(int max) {
        if (max < 0) {
            max = 0;
        }
        if (max != this.mMax) {
            this.mMax = max;
            if (this.mProgress > max) {
                this.mProgress = max;
            }
            updateProgress();
            updateSecondaryProgress();
            postInvalidate();
        }
    }

    public synchronized int getMax() {
        return this.mMax;
    }

    public int getProgressHeight() {
        if (LogUtils.mIsDebug) {
            LogUtils.i(TAG, "getProgressHeight = " + this.mHeight);
        }
        return this.mHeight;
    }

    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > this.mMax) {
            progress = this.mMax;
        }
        if (progress != this.mProgress) {
            this.mProgress = progress;
            updateProgress();
            invalidate();
        }
    }

    public synchronized void setSecondaryProgress(int secondaryProgress) {
        if (secondaryProgress < 0) {
            secondaryProgress = 0;
        }
        if (secondaryProgress > this.mMax) {
            secondaryProgress = this.mMax;
        }
        if (secondaryProgress != this.mSecondaryProgress) {
            this.mSecondaryProgress = secondaryProgress;
            updateSecondaryProgress();
            postInvalidate();
        }
    }

    public synchronized int getProgress() {
        return this.mProgress;
    }

    public synchronized int getSecondProgress() {
        return this.mSecondaryProgress;
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener l) {
        this.mOnSeekBarChangeListener = l;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPaint.setAntiAlias(true);
        this.mSection = ((float) this.mProgress) / ((float) this.mMax);
        this.mFristProgress = (int) (this.mSection * ((float) this.mSeekBarWidth));
        this.mPaint.setColor(this.mBackgroundColor);
        canvas.drawRect(this.mBackgroundRect, this.mPaint);
        this.mPaint.setColor(this.mSecondaryProgressColor);
        canvas.drawRect(this.mRectSecondaryProgress, this.mPaint);
        Paint thumbPaint = new Paint(1);
        int progress = (int) (((float) this.mSeekBarWidth) * this.mSection);
        float f = 0.0f;
        thumbPaint.setShader(new LinearGradient(0.0f, f, (float) progress, (float) this.mHeight, new int[]{this.mProgressColor, this.mMiddleColor, this.mThumbColor}, null, TileMode.CLAMP));
        canvas.drawRect(0.0f, 0.0f, (float) progress, (float) this.mHeight, thumbPaint);
        if (LogUtils.mIsDebug) {
            LogUtils.i(TAG, "onDraw() progress=" + progress);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case 0:
                if (LogUtils.mIsDebug) {
                    LogUtils.i(TAG, "onTouchEvent ACTION_DOWN");
                }
                onStartTrackingTouch();
                trackTouchEvent(event);
                break;
            case 1:
                if (LogUtils.mIsDebug) {
                    LogUtils.i(TAG, "onTouchEvent ACTION_UP");
                }
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                postInvalidate();
                onStopTrackingTouch();
                break;
            case 2:
                if (LogUtils.mIsDebug) {
                    LogUtils.i(TAG, "onTouchEvent ACTION_MOVE");
                }
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    onProgressChanged(this, getProgress(), true);
                    break;
                }
                break;
        }
        return true;
    }

    private void onProgressChanged(ISeekBar seekBar, int progress, boolean fromUser) {
        this.mIsDragging = true;
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
        }
    }

    private void initProgress() {
        this.mSeekBarWidth = getWidth();
        this.mPaddingLeft = getPaddingLeft();
        this.mPaddingRight = getPaddingRight();
        updateProgressPos();
    }

    private void updateProgressPos() {
        this.mRectProgress = new Rect(0, 0, 0, this.mHeight);
        this.mRectSecondaryProgress = new Rect(0, 0, 0, this.mHeight);
        this.mBackgroundRect = new Rect(0, 0, this.mSeekBarWidth, this.mHeight);
        updateProgress();
        updateSecondaryProgress();
    }

    private void updateProgress() {
        if (this.mRectProgress == null) {
            return;
        }
        if (this.mMax > 0) {
            this.mRectProgress.right = (this.mSeekBarWidth * this.mProgress) / this.mMax;
            return;
        }
        this.mRectProgress.right = 0;
    }

    private void updateSecondaryProgress() {
        if (this.mRectSecondaryProgress != null) {
            this.mRectSecondaryProgress.right = (this.mSeekBarWidth * this.mSecondaryProgress) / this.mMax;
        }
    }

    private void onStartTrackingTouch() {
        this.mIsDragging = true;
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    private void onStopTrackingTouch() {
        this.mIsDragging = false;
        if (this.mOnSeekBarChangeListener != null) {
            this.mOnSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    private void trackTouchEvent(MotionEvent event) {
        float scale;
        int width = getWidth();
        int available = (width - this.mPaddingLeft) - this.mPaddingRight;
        int x = (int) event.getX();
        if (x < this.mPaddingLeft) {
            scale = 0.0f;
        } else if (x > width - this.mPaddingRight) {
            scale = 1.0f;
        } else {
            scale = ((float) (x - this.mPaddingLeft)) / ((float) available);
        }
        setProgress((int) (0.0f + (((float) getMax()) * scale)));
    }

    public int getProgressTop() {
        return 0;
    }
}
