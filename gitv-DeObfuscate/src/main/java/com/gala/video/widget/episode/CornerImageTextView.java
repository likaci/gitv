package com.gala.video.widget.episode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;

public class CornerImageTextView extends TextView {
    private static float sTopLeftCornerRatio = -1.0f;
    private final String TAG;
    private Rect mActualTextRect;
    private Rect mBackgroundPadRect;
    private Context mContext;
    private int mCornerImageMargin;
    private int mMinimumHeight;
    private int mMinimumWidhth;
    private RequestFocusDelegator mRequestFocusDelegator;
    private Bitmap mTopLeftBitmap;
    private Rect mTopLeftRect;
    private Bitmap mTopRightBitmap;
    private Rect mTopRightRect;

    interface RequestFocusDelegator {
        boolean requestFocus(CornerImageTextView cornerImageTextView, int i, Rect rect);
    }

    public CornerImageTextView(Context context) {
        this(context, null);
    }

    public CornerImageTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CornerImageTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mTopLeftRect = new Rect();
        this.mTopRightRect = new Rect();
        this.mActualTextRect = new Rect();
        this.mBackgroundPadRect = new Rect();
        this.mContext = context;
        this.TAG = "CornerImageTextView@" + Integer.toHexString(hashCode());
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
        this.mActualTextRect.set(this.mBackgroundPadRect.left, this.mBackgroundPadRect.top, getMeasuredWidth() - this.mBackgroundPadRect.left, getMeasuredHeight() - this.mBackgroundPadRect.top);
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            return specSize;
        }
        int result = this.mMinimumWidhth;
        if (specMode == Integer.MIN_VALUE) {
            return Math.max(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == 1073741824) {
            return specSize;
        }
        int result = this.mMinimumWidhth;
        if (specMode == Integer.MIN_VALUE) {
            return Math.max(result, specSize);
        }
        return result;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mTopLeftBitmap != null) {
            if (this.mTopLeftRect.isEmpty()) {
                int realHeight = this.mActualTextRect.height();
                this.mTopLeftRect.set(this.mActualTextRect.left + this.mCornerImageMargin, this.mActualTextRect.top + this.mCornerImageMargin, (this.mActualTextRect.left + this.mCornerImageMargin) + ((int) (((((double) ((float) realHeight)) * 5.0d) / 18.0d) * ((double) sTopLeftCornerRatio))), (this.mActualTextRect.top + this.mCornerImageMargin) + ((int) ((((double) ((float) realHeight)) * 5.0d) / 18.0d)));
            }
            canvas.drawBitmap(this.mTopLeftBitmap, null, this.mTopLeftRect, null);
        }
        if (this.mTopRightBitmap != null) {
            if (this.mTopRightRect.isEmpty()) {
                realHeight = this.mActualTextRect.height();
                this.mTopRightRect.set(this.mActualTextRect.right - ((int) (((double) realHeight) * 0.75d)), this.mActualTextRect.top, this.mActualTextRect.right, this.mActualTextRect.top + ((int) (((double) realHeight) * 0.75d)));
            }
            canvas.drawBitmap(this.mTopRightBitmap, null, this.mTopRightRect, null);
        }
        super.onDraw(canvas);
    }

    private void resetTopLeftRatio() {
        if (this.mTopLeftBitmap != null) {
            float width = (float) this.mTopLeftBitmap.getWidth();
            float height = (float) this.mTopLeftBitmap.getHeight();
            if (((double) sTopLeftCornerRatio) < 0.0d) {
                sTopLeftCornerRatio = width / height;
            }
        }
    }

    public void setTopLeftCornerImage(int topLeftID) {
        this.mTopLeftBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), topLeftID);
        resetTopLeftRatio();
    }

    public void setTopRightCornerImage(int topRightID) {
        this.mTopRightBitmap = BitmapFactory.decodeResource(this.mContext.getResources(), topRightID);
    }

    public void setTopLeftCornerImage(Bitmap topLeft) {
        this.mTopLeftBitmap = topLeft;
        resetTopLeftRatio();
    }

    public void setTopRightCornerImage(Bitmap topRight) {
        this.mTopRightBitmap = topRight;
    }

    public void setCornerImagePadding(Rect padding) {
        this.mBackgroundPadRect.set(padding);
    }

    public void setCornerImageMargin(int left, int top, int right, int bottom) {
        this.mCornerImageMargin = left;
    }

    public void setMinimumWidth(int width) {
        this.mMinimumWidhth = width;
    }

    public void setMinimumHeight(int height) {
        this.mMinimumHeight = height;
    }

    public boolean requestFocus(int direction, Rect previouslyFocusedRect) {
        if (this.mRequestFocusDelegator != null) {
            return this.mRequestFocusDelegator.requestFocus(this, direction, previouslyFocusedRect);
        }
        return super.requestFocus(direction, previouslyFocusedRect);
    }

    void setRequestFocusDelegator(RequestFocusDelegator delegator) {
        this.mRequestFocusDelegator = delegator;
    }

    boolean superRequestFocus(int direction, Rect previouslyFocusedRect) {
        return super.requestFocus(direction, previouslyFocusedRect);
    }
}
