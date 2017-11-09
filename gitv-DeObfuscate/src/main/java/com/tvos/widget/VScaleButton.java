package com.tvos.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;

public class VScaleButton extends FrameLayout {
    private int mAnimationDuration;
    private float mAnimationRatio;
    private View mContentView;
    private OnFocusChangeListener mOnFocusChangeListener;
    private VImageView mVImageView;

    class C20811 implements OnFocusChangeListener {
        C20811() {
        }

        public void onFocusChange(View view, boolean focused) {
            if (focused) {
                VScaleButton.this.startZoomInAnimation();
            } else {
                VScaleButton.this.startZoomOutAnimation();
            }
            if (VScaleButton.this.mOnFocusChangeListener != null) {
                VScaleButton.this.mOnFocusChangeListener.onFocusChange(view, focused);
            }
        }
    }

    public VScaleButton(Context context) {
        this(context, null);
    }

    public VScaleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mAnimationRatio = 1.1f;
        this.mAnimationDuration = 200;
        initView();
    }

    public VScaleButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mAnimationRatio = 1.1f;
        this.mAnimationDuration = 200;
        initView();
    }

    private void initView() {
        this.mVImageView = new VImageView(getContext());
        this.mVImageView.setScaleType(ScaleType.FIT_XY);
        addView(this.mVImageView);
        setFocusable(true);
        setClickable(true);
        super.setOnFocusChangeListener(new C20811());
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener) {
        this.mOnFocusChangeListener = listener;
    }

    public void setSelector(int resId) {
        setBackgroundResource(resId);
    }

    public void setBackgroundImageResource(int resId) {
        this.mVImageView.setImageResource(resId);
    }

    public void setBackgroundImageDrawable(Drawable drawable) {
        this.mVImageView.setImageDrawable(drawable);
    }

    public void setBackgroundImageBitmap(Bitmap bitmap) {
        this.mVImageView.setImageBitmap(bitmap);
    }

    public void setBackgroundImageURI(Uri uri) {
        this.mVImageView.setImageURI(uri);
    }

    public void setBackgroundImageAlpha(int alpha) {
        this.mVImageView.setImageAlpha(alpha);
    }

    public void setBackgroundImageColor(int color) {
        this.mVImageView.setBackgroundColor(color);
    }

    public VImageView getBackgroundImageView() {
        return this.mVImageView;
    }

    public void setContentView(View view) {
        if (this.mContentView != null) {
            removeView(this.mContentView);
        }
        this.mContentView = view;
        addView(this.mContentView, new LayoutParams(-1, -1));
    }

    public void setAnimationRatio(float ratio) {
        this.mAnimationRatio = ratio;
    }

    public void setAnimationDuration(int duration) {
        this.mAnimationDuration = duration;
    }

    protected void startZoomInAnimation() {
        AnimatorSet set = new AnimatorSet();
        r1 = new Animator[2];
        r1[0] = ObjectAnimator.ofFloat(this, "scaleX", new float[]{getScaleX(), this.mAnimationRatio});
        r1[1] = ObjectAnimator.ofFloat(this, "scaleY", new float[]{getScaleY(), this.mAnimationRatio});
        set.playTogether(r1);
        set.setDuration((long) this.mAnimationDuration).start();
    }

    protected void startZoomOutAnimation() {
        AnimatorSet set = new AnimatorSet();
        r1 = new Animator[2];
        r1[0] = ObjectAnimator.ofFloat(this, "scaleX", new float[]{getScaleX(), 1.0f});
        r1[1] = ObjectAnimator.ofFloat(this, "scaleY", new float[]{getScaleY(), 1.0f});
        set.playTogether(r1);
        set.setDuration((long) this.mAnimationDuration).start();
    }

    public Drawable getDrawable() {
        return this.mVImageView.getDrawable();
    }
}
