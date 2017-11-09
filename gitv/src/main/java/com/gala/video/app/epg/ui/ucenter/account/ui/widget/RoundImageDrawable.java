package com.gala.video.app.epg.ui.ucenter.account.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import com.gala.video.app.epg.R;

public class RoundImageDrawable extends Drawable {
    Bitmap mBitmap;
    private Context mContext;
    private Paint mPaint;
    private int mRadio = 100;
    private RectF oval;

    public RoundImageDrawable(Context context, Bitmap bitmap, int viewWidth, int day) {
        this.mContext = context;
        this.mRadio = (10 - day) * 36;
        int width = bitmap.getWidth();
        int height = bitmap.getWidth();
        Matrix matrix = new Matrix();
        matrix.setScale(((float) viewWidth) / ((float) width), ((float) viewWidth) / ((float) width));
        this.mBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        BitmapShader bitmapShader = new BitmapShader(this.mBitmap, TileMode.CLAMP, TileMode.CLAMP);
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setShader(bitmapShader);
    }

    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
    }

    public int getIntrinsicHeight() {
        return this.mBitmap.getHeight();
    }

    public int getIntrinsicWidth() {
        return this.mBitmap.getWidth();
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) (this.mBitmap.getWidth() / 2), (float) (this.mBitmap.getHeight() / 2), (float) (this.mBitmap.getWidth() / 2), this.mPaint);
        this.oval = new RectF((float) getDimen(R.dimen.dimen_10dp), (float) getDimen(R.dimen.dimen_10dp), (float) getDimen(R.dimen.dimen_196dp), (float) getDimen(R.dimen.dimen_196dp));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) getDimen(R.dimen.dimen_7dp));
        paint.setColor(this.mContext.getResources().getColor(R.color.gala_write));
        paint.setStyle(Style.STROKE);
        canvas.drawArc(this.oval, 270.0f, (float) (-this.mRadio), false, paint);
    }

    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.mPaint.setColorFilter(cf);
    }

    public int getOpacity() {
        return 0;
    }

    private int getDimen(int id) {
        if (this.mContext != null) {
            return (int) this.mContext.getResources().getDimension(id);
        }
        return 0;
    }
}
