package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;

public class FocusView extends View {
    private static final String TAG = FocusView.class.getSimpleName();
    private Rect mDrawRect = new Rect();
    private Drawable mDrawable;
    private int mInvisiableMarginTop;
    private int mMarginLeft = 0;
    private int mMarginRight = 0;
    private int mScreenWidth;

    public FocusView(Context context) {
        super(context);
        init();
    }

    public FocusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FocusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if ("4.4.4".equals(VERSION.RELEASE) && "MiTV3S-48".equals(Build.MODEL)) {
            setLayerType(1, null);
        } else {
            setLayerType(0, null);
        }
        this.mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    public void setMarginLeft(int marginLeft) {
        this.mMarginLeft = marginLeft;
    }

    public void setMarginRight(int marginRight) {
        this.mMarginRight = marginRight;
    }

    public void setInvisiableMarginTop(int invisiableMarginTop) {
        this.mInvisiableMarginTop = invisiableMarginTop;
    }

    public void setBackgroundDrawable(Drawable drawable) {
        this.mDrawable = drawable;
    }

    public Drawable getBackground() {
        return this.mDrawable;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (getX() + ((float) getWidth()) < ((float) (this.mScreenWidth - this.mMarginRight))) {
            canvas.clipRect(((float) this.mMarginLeft) - getX(), ((float) this.mInvisiableMarginTop) - getY(), (float) getRight(), (float) getBottom());
        } else {
            canvas.clipRect(((float) this.mMarginLeft) - getX(), ((float) this.mInvisiableMarginTop) - getY(), (((float) this.mScreenWidth) - getX()) - ((float) this.mMarginRight), (float) getBottom());
        }
        drawBg(canvas);
        canvas.restore();
    }

    private void drawBg(Canvas canvas) {
        if (this.mDrawable != null) {
            this.mDrawRect.set(getLeft(), getTop(), getRight(), getBottom());
            this.mDrawable.setBounds(this.mDrawRect);
            this.mDrawable.draw(canvas);
        }
    }
}
