package com.gala.video.app.epg.ui.ucenter.account.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;
import com.gala.video.app.epg.R;

public class VipProgressCustomView extends View {
    private static final int CIRCLE_WIDTH_DEFAULT = R.dimen.dimen_6dp;
    private static final int PROGRESS_COLOR_DEFAULT_1 = R.color.vip_progress_color1;
    private static final int PROGRESS_COLOR_DEFAULT_2 = R.color.vip_progress_color2;
    private static final int PROGRESS_COLOR_DEFAULT_3 = R.color.vip_progress_color3;
    private static final float START_ANGLE_DEFAULT = 270.0f;
    private int mCircleWidth;
    private int mColorID1;
    private int mColorID2;
    private int mColorID3;
    private LinearGradient mGradient;
    private Paint mPaint;
    private float mSweepAngle;

    public VipProgressCustomView(Context context) {
        super(context);
        init(context);
    }

    public VipProgressCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VipProgressCustomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mCircleWidth = CIRCLE_WIDTH_DEFAULT;
        this.mColorID1 = PROGRESS_COLOR_DEFAULT_1;
        this.mColorID2 = PROGRESS_COLOR_DEFAULT_2;
        this.mColorID3 = PROGRESS_COLOR_DEFAULT_3;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
    }

    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        float f = 0.0f;
        float f2 = 0.0f;
        this.mGradient = new LinearGradient(0.0f, f, f2, (float) getHeight(), new int[]{getColor(this.mColorID3), getColor(this.mColorID2), getColor(this.mColorID1)}, new float[]{0.0f, 0.7f, 1.0f}, TileMode.CLAMP);
        paint.setShader(this.mGradient);
        paint.setStrokeWidth((float) getDimen(this.mCircleWidth));
        paint.setStyle(Style.STROKE);
        canvas.drawCircle((float) (getWidth() / 2), (float) (getWidth() / 2), (float) ((getWidth() - getDimen(this.mCircleWidth)) / 2), paint);
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setStrokeWidth((float) getDimen(this.mCircleWidth));
        this.mPaint.setColor(getColor(R.color.gala_write));
        canvas.drawArc(new RectF((float) ((getDimen(this.mCircleWidth) / 2) + 0), (float) ((getDimen(this.mCircleWidth) / 2) + 0), (float) (getWidth() - (getDimen(this.mCircleWidth) / 2)), (float) (getHeight() - (getDimen(this.mCircleWidth) / 2))), START_ANGLE_DEFAULT, -this.mSweepAngle, false, this.mPaint);
    }

    public void computeProgress(int width, float sweepAngle) {
        if (width <= 0) {
            width = CIRCLE_WIDTH_DEFAULT;
        }
        this.mCircleWidth = width;
        this.mSweepAngle = sweepAngle;
        postInvalidate();
    }

    private int getColor(int id) {
        return getResources().getColor(id);
    }

    private int getDimen(int id) {
        return (int) getResources().getDimension(id);
    }
}
