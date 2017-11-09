package com.gala.video.app.player.ui.widget.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class TimeLineView extends View {
    private int mMarginTop;
    private int mOffset;
    private int mSize;
    private int mStratSpan;
    private int mTimeSpan;

    public TimeLineView(Context context) {
        super(context);
    }

    public TimeLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TimeLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOffset(int offset) {
        this.mOffset = offset;
    }

    public void setPageSize(int size) {
        this.mSize = size;
    }

    public void setMarginTop(int marginTop) {
        this.mMarginTop = marginTop;
    }

    public void setTimeSpan(int timeSpan) {
        this.mTimeSpan = timeSpan;
    }

    public void setTimeStartSpan(int startSpan) {
        this.mStratSpan = startSpan;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#454344"));
        switch (this.mSize) {
            case 1:
                canvas.drawLine(0.0f, (float) this.mMarginTop, (float) this.mStratSpan, (float) this.mMarginTop, paint);
                return;
            case 2:
                canvas.drawLine(0.0f, (float) this.mMarginTop, (float) this.mStratSpan, (float) this.mMarginTop, paint);
                canvas.drawLine((float) (this.mStratSpan + this.mOffset), (float) this.mMarginTop, (float) ((this.mStratSpan + this.mOffset) + this.mTimeSpan), (float) this.mMarginTop, paint);
                return;
            case 3:
                canvas.drawLine(0.0f, (float) this.mMarginTop, (float) this.mStratSpan, (float) this.mMarginTop, paint);
                canvas.drawLine((float) (this.mStratSpan + this.mOffset), (float) this.mMarginTop, (float) ((this.mStratSpan + this.mOffset) + this.mTimeSpan), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 2)) + this.mTimeSpan), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mOffset * 2)) + (this.mTimeSpan * 2)), (float) this.mMarginTop, paint);
                return;
            case 4:
                canvas.drawLine(0.0f, (float) this.mMarginTop, (float) this.mStratSpan, (float) this.mMarginTop, paint);
                canvas.drawLine((float) (this.mStratSpan + this.mOffset), (float) this.mMarginTop, (float) ((this.mStratSpan + this.mOffset) + this.mTimeSpan), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 2)) + this.mTimeSpan), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mOffset * 2)) + (this.mTimeSpan * 2)), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 3)) + (this.mTimeSpan * 2)), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mOffset * 3)) + (this.mTimeSpan * 3)), (float) this.mMarginTop, paint);
                return;
            case 5:
                canvas.drawLine(0.0f, (float) this.mMarginTop, (float) this.mStratSpan, (float) this.mMarginTop, paint);
                canvas.drawLine((float) (this.mStratSpan + this.mOffset), (float) this.mMarginTop, (float) ((this.mStratSpan + this.mOffset) + this.mTimeSpan), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 2)) + this.mTimeSpan), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mOffset * 2)) + (this.mTimeSpan * 2)), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 3)) + (this.mTimeSpan * 2)), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mOffset * 3)) + (this.mTimeSpan * 3)), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 4)) + (this.mTimeSpan * 3)), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mOffset * 4)) + (this.mTimeSpan * 4)), (float) this.mMarginTop, paint);
                canvas.drawLine((float) ((this.mStratSpan + (this.mOffset * 5)) + (this.mTimeSpan * 4)), (float) this.mMarginTop, (float) ((this.mStratSpan + (this.mTimeSpan * 5)) + (this.mOffset * 4)), (float) this.mMarginTop, paint);
                return;
            default:
                return;
        }
    }
}
