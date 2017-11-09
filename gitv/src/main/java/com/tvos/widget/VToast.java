package com.tvos.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;

public class VToast {
    private static final long DEF_ANIMATOR_DURATION = 250;
    private static final long DEF_DURATION = 3000;
    private static final float DEF_ICON_SIZE = 50.4f;
    private static final float DEF_MARGIN_BOTTOM = 75.2f;
    private static final float DEF_SCALE_RATIO = 1.1f;
    private static final float DEF_TEXT_SIZE = 18.0f;
    private static final String TAG = "VToast";
    private Context mContext = null;
    private Handler mDismissHandler = new Handler();
    private Runnable mDismissRunnable = new Runnable() {
        public void run() {
            if (VToast.this.mRootView != null) {
                VToast.this.dismiss();
            }
        }
    };
    private FrameLayout mIconContainer = null;
    private ImageView mIconView = null;
    private boolean mIsShowing = false;
    private FrameLayout mRootView = null;
    private TextView mTextView = null;
    private LinearLayout mToast = null;
    private WindowManager mWm = null;

    public VToast(Context context) {
        this.mContext = context.getApplicationContext();
        this.mWm = (WindowManager) this.mContext.getSystemService("window");
        this.mRootView = new FrameLayout(this.mContext);
        this.mToast = new LinearLayout(this.mContext);
        LayoutParams lp = new LayoutParams(-2, dp2px(DEF_ICON_SIZE));
        lp.gravity = 81;
        lp.bottomMargin = dp2px(DEF_MARGIN_BOTTOM);
        this.mToast.setLayoutParams(lp);
        this.mToast.setMinimumHeight(dp2px(DEF_ICON_SIZE));
        this.mToast.setMinimumWidth(dp2px(DEF_ICON_SIZE));
        this.mToast.setBackgroundColor(-16777216);
        this.mRootView.addView(this.mToast);
        this.mIconContainer = new FrameLayout(this.mContext);
        this.mIconContainer.setLayoutParams(new LinearLayout.LayoutParams(dp2px(DEF_ICON_SIZE), dp2px(DEF_ICON_SIZE)));
        this.mIconContainer.setBackgroundColor(Color.rgb(14, Opcodes.IFEQ, 26));
        this.mToast.addView(this.mIconContainer);
        this.mIconView = new ImageView(this.mContext);
        LayoutParams lp2 = new LayoutParams(dp2px(30.8f), dp2px(30.8f));
        lp2.gravity = 17;
        this.mIconView.setLayoutParams(lp2);
        this.mIconContainer.addView(this.mIconView);
        this.mTextView = new TextView(this.mContext);
        this.mTextView.setGravity(17);
        this.mTextView.setLayoutParams(new LinearLayout.LayoutParams(-2, -1));
        this.mTextView.setPadding(dp2px(25.0f), 0, dp2px(25.0f), 0);
        this.mTextView.setTextSize((float) dp2px(DEF_TEXT_SIZE));
        this.mTextView.setTextColor(Color.rgb(241, 241, 241));
        this.mToast.addView(this.mTextView);
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public VToast setIcon(Bitmap icon) {
        this.mIconView.setImageBitmap(icon);
        return this;
    }

    public VToast setIcon(Drawable icon) {
        this.mIconView.setImageDrawable(icon);
        return this;
    }

    public VToast setIcon(int resId) {
        this.mIconView.setImageResource(resId);
        return this;
    }

    public VToast setText(String text) {
        this.mTextView.setText(text);
        return this;
    }

    public VToast setText(int resId) {
        this.mTextView.setText(resId);
        return this;
    }

    public void show() {
        if (this.mIsShowing) {
            dismissDelay();
            return;
        }
        this.mWm.addView(this.mRootView, createLayoutParams());
        animateIn();
        this.mIsShowing = true;
        dismissDelay();
    }

    private void dismissDelay() {
        this.mDismissHandler.removeCallbacks(this.mDismissRunnable);
        this.mDismissHandler.postDelayed(this.mDismissRunnable, DEF_DURATION);
    }

    private void animateIn() {
        this.mToast.setAlpha(0.0f);
        this.mIconContainer.setTranslationX((float) (-dp2px(DEF_ICON_SIZE)));
        this.mIconView.setVisibility(4);
        this.mTextView.setTranslationX((float) (-dp2px(DEF_ICON_SIZE)));
        ObjectAnimator animator1 = translateYAnimator(this.mToast, DEF_ANIMATOR_DURATION, (float) dp2px(DEF_ICON_SIZE), 0.0f);
        ObjectAnimator animator2 = alphaAnimator(this.mToast, DEF_ANIMATOR_DURATION, 0.0f, 1.0f);
        AnimatorSet set1 = new AnimatorSet();
        set1.play(animator1).with(animator2);
        ObjectAnimator iconAnimator = translateXAnimator(this.mIconContainer, DEF_ANIMATOR_DURATION, (float) (-dp2px(DEF_ICON_SIZE)), 0.0f);
        ObjectAnimator textAnimator = translateXAnimator(this.mTextView, DEF_ANIMATOR_DURATION, (float) (-dp2px(DEF_ICON_SIZE)), 0.0f);
        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(new Animator[]{iconAnimator, textAnimator});
        Animator scaleUp = scaleAnimator(this.mIconView, DEF_ANIMATOR_DURATION, 0.0f, 1.1f);
        Animator scaleDown = scaleAnimator(this.mIconView, DEF_ANIMATOR_DURATION, 1.1f, 1.0f);
        final AnimatorSet set3 = new AnimatorSet();
        set3.play(scaleDown).after(scaleUp);
        set1.start();
        set1.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                set2.start();
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
        set2.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                VToast.this.mIconView.setVisibility(0);
                set3.start();
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    private void animateOut() {
        Animator translate = translateYAnimator(this.mToast, DEF_ANIMATOR_DURATION, 0.0f, DEF_ICON_SIZE);
        Animator alpha = alphaAnimator(this.mToast, DEF_ANIMATOR_DURATION, 1.0f, 0.0f);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(new Animator[]{translate, alpha});
        set.start();
        set.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                VToast.this.mWm.removeViewImmediate(VToast.this.mRootView);
                VToast.this.mIsShowing = false;
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    public void dismiss() {
        if (this.mIsShowing) {
            this.mDismissHandler.removeCallbacks(this.mDismissRunnable);
            animateOut();
        }
    }

    private ObjectAnimator translateYAnimator(View v, long duration, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationY", new float[]{from, to});
        animator.setDuration(duration);
        return animator;
    }

    private ObjectAnimator translateXAnimator(View v, long duration, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "translationX", new float[]{from, to});
        animator.setDuration(duration);
        return animator;
    }

    private ObjectAnimator alphaAnimator(View v, long duration, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "alpha", new float[]{from, to});
        animator.setDuration(duration);
        return animator;
    }

    private Animator scaleAnimator(View v, long duration, float from, float to) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(v, "scaleX", new float[]{from, to});
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(v, "scaleY", new float[]{from, to});
        set.playTogether(new Animator[]{animatorX, animatorY});
        set.setDuration(duration);
        return set;
    }

    private WindowManager.LayoutParams createLayoutParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = 2003;
        params.format = 1;
        params.flags = 56;
        return params;
    }

    private int dp2px(float dpValue) {
        return (int) ((dpValue * this.mContext.getResources().getDisplayMetrics().density) + ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
    }
}
