package com.gala.video.app.epg.ui.ucenter.account.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class UCenterFlashView extends LinearLayout {
    private static final String LOG_TAG = "EPG/UCenter/UCenterFlashView";
    private ImageView mFlashView;
    private Handler mHandler;
    private RectF mLeftRectF;
    private int mLength;
    private RectF mMiddleRectF;
    private PaintFlagsDrawFilter mPaintFlagsDrawFilter;
    private Path mPath;
    private float mRadius;
    private RectF mRightRectF;
    private int mWidth;
    private Runnable r;

    public UCenterFlashView(Context context) {
        this(context, null);
    }

    public UCenterFlashView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCenterFlashView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mWidth = ResourceUtil.getDimen(R.dimen.dimen_200dp);
        this.mRadius = (float) ResourceUtil.getDimen(R.dimen.dimen_28dp);
        this.mLength = ResourceUtil.getDimen(R.dimen.dimen_234dp);
        this.mHandler = new Handler(Looper.getMainLooper());
        init();
    }

    private void init() {
        setLayerType(1, null);
        setWillNotDraw(false);
        this.mFlashView = new ImageView(getContext());
        LayoutParams params = new LayoutParams(this.mWidth, this.mWidth);
        params.gravity = 16;
        this.mFlashView.setScaleType(ScaleType.FIT_XY);
        this.mFlashView.setImageResource(R.drawable.epg_ucenter_login_btn_light);
        addView(this.mFlashView, params);
        ImageView view2 = new ImageView(getContext());
        LayoutParams params2 = new LayoutParams(this.mLength, this.mWidth);
        params2.gravity = 16;
        addView(view2, params2);
        ImageView view3 = new ImageView(getContext());
        LayoutParams params3 = new LayoutParams(this.mWidth, this.mWidth);
        params3.gravity = 16;
        view3.setVisibility(4);
        addView(view3, params3);
        this.mPath = new Path();
        this.mLeftRectF = new RectF();
        this.mMiddleRectF = new RectF();
        this.mRightRectF = new RectF();
        this.mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0, 3);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mPath.moveTo(this.mRadius, this.mRadius);
        this.mLeftRectF.set((float) this.mWidth, ((float) (this.mWidth / 2)) - (this.mRadius * 2.0f), ((float) this.mWidth) + (this.mRadius * 2.0f), (float) (this.mWidth / 2));
        this.mPath.addArc(this.mLeftRectF, 90.0f, 180.0f);
        this.mPath.moveTo(0.0f, this.mRadius);
        int leftCircleStart = this.mLength - ((int) this.mRadius);
        this.mMiddleRectF.set(((float) this.mWidth) + this.mRadius, ((float) (this.mWidth / 2)) - (this.mRadius * 2.0f), (float) (this.mWidth + leftCircleStart), (float) (this.mWidth / 2));
        this.mPath.addRect(this.mMiddleRectF, Direction.CCW);
        this.mPath.moveTo((float) leftCircleStart, this.mRadius);
        this.mRightRectF.set(((float) (this.mWidth + leftCircleStart)) - this.mRadius, ((float) (this.mWidth / 2)) - (this.mRadius * 2.0f), ((float) (this.mWidth + leftCircleStart)) + this.mRadius, (float) (this.mWidth / 2));
        this.mPath.addArc(this.mRightRectF, -90.0f, 180.0f);
        canvas.setDrawFilter(this.mPaintFlagsDrawFilter);
        try {
            canvas.clipPath(this.mPath);
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, ">>>>>", e);
            setVisibility(4);
        }
    }

    public void startAnimation() {
        if (this.r == null) {
            this.r = new Runnable() {
                public void run() {
                    UCenterFlashView.this.startAnimation((UCenterFlashView.this.mWidth * 2) + UCenterFlashView.this.mLength, UCenterFlashView.this.mWidth / 2);
                }
            };
        }
        this.mHandler.postDelayed(this.r, 250);
    }

    private void startAnimation(int x, int y) {
        TranslateAnimation translateAnimation = new TranslateAnimation(0.0f, (float) x, 0.0f, (float) (-y));
        translateAnimation.setDuration(1500);
        translateAnimation.setRepeatMode(1);
        translateAnimation.setInterpolator(new AccelerateInterpolator());
        translateAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                UCenterFlashView.this.mFlashView.clearAnimation();
                UCenterFlashView.this.mFlashView.setVisibility(4);
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.mFlashView.startAnimation(translateAnimation);
    }

    public void stopAnimation() {
        if (this.r != null) {
            this.mHandler.removeCallbacks(this.r);
        }
        this.mFlashView.clearAnimation();
        this.mFlashView.setVisibility(4);
    }

    public void unBindAnimation() {
        if (this.r != null) {
            this.mHandler.removeCallbacks(this.r);
        }
        this.mFlashView.clearAnimation();
        this.mFlashView.setVisibility(4);
        this.r = null;
    }
}
