package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

public class TextCanvas extends CanvasView {
    private Drawable mBackground;
    private Rect mBackgroundPadding = new Rect();
    private Context mContext;
    private Paint mPaint;
    private String mText;
    private Rect mTextBounds = new Rect();
    private int mTextColor = -1;
    private int mTextSize = 13;

    public TextCanvas(Context context) {
        this.mContext = context;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setStyle(Style.FILL);
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setTextSize(int Size) {
        this.mTextSize = Size;
    }

    public int getTextSize() {
        return this.mTextSize;
    }

    public int getTextColor() {
        return this.mTextColor;
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    public void setBackground(int resId) {
        this.mBackground = this.mContext.getResources().getDrawable(resId);
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public String getText() {
        return this.mText;
    }

    public void draw(Canvas canvas) {
        canvas.save();
        this.mPaint.setColor(this.mTextColor);
        this.mPaint.setTextSize((float) this.mTextSize);
        this.mPaint.getTextBounds(this.mText, 0, this.mText.length(), this.mTextBounds);
        int backgroundPadding = 0;
        if (this.mBackground != null) {
            this.mBackground.getPadding(this.mBackgroundPadding);
            backgroundPadding = this.mBackgroundPadding.left;
        }
        int height = getHeight() > 0 ? getHeight() : this.mTextBounds.height();
        int left = getMarginLeft();
        int top = getMarginTop();
        int right = (((this.mTextBounds.width() + left) + backgroundPadding) + getPaddingRight()) + getPaddingLeft();
        int bottom = ((top + height) + getPaddingTop()) + getPaddingBottom();
        if (this.mBackground != null) {
            this.mBackground.setBounds(left, top, right, bottom);
            this.mBackground.draw(canvas);
        }
        canvas.drawText(this.mText, (float) (((left + backgroundPadding) - this.mTextBounds.left) + getPaddingLeft()), (float) (((((height - this.mTextBounds.height()) / 2) + top) - this.mTextBounds.top) + getPaddingTop()), this.mPaint);
        canvas.restore();
    }
}
