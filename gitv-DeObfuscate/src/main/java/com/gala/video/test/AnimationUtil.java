package com.gala.video.test;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;

public class AnimationUtil {
    public static final int ANIMATION_DURATION = 200;
    private static int DEFAULT_DURATION = 500;
    private static final float DEF_ZOOM_RATIO = 1.05f;

    class C18631 implements AnimationListener {
        private final /* synthetic */ View val$view;

        C18631(View view) {
            this.val$view = view;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            this.val$view.setVisibility(8);
        }
    }

    class C18652 extends AnimatorListenerAdapter {
        private final /* synthetic */ View val$view;

        C18652(View view) {
            this.val$view = view;
        }

        public void onAnimationEnd(Animator animation) {
            View view = this.val$view;
            final View view2 = this.val$view;
            view.post(new Runnable() {
                public void run() {
                    view2.setLayerType(0, null);
                }
            });
        }
    }

    class C18673 extends AnimatorListenerAdapter {
        private final /* synthetic */ View val$view;

        C18673(View view) {
            this.val$view = view;
        }

        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            View view = this.val$view;
            final View view2 = this.val$view;
            view.post(new Runnable() {
                public void run() {
                    view2.setLayerType(0, null);
                }
            });
        }
    }

    class C18694 extends AnimatorListenerAdapter {
        private final /* synthetic */ View val$view;

        C18694(View view) {
            this.val$view = view;
        }

        public void onAnimationEnd(Animator animation) {
            View view = this.val$view;
            final View view2 = this.val$view;
            view.post(new Runnable() {
                public void run() {
                    view2.setLayerType(0, null);
                }
            });
        }
    }

    class C18715 extends AnimatorListenerAdapter {
        private final /* synthetic */ View val$view;

        C18715(View view) {
            this.val$view = view;
        }

        public void onAnimationEnd(Animator animation) {
            View view = this.val$view;
            final View view2 = this.val$view;
            view.post(new Runnable() {
                public void run() {
                    view2.setLayerType(0, null);
                }
            });
        }
    }

    class C18726 implements AnimatorListener {
        private final /* synthetic */ View val$view;

        C18726(View view) {
            this.val$view = view;
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            this.val$view.setEnabled(true);
            if (!this.val$view.isFocused()) {
                this.val$view.setScaleX(1.0f);
                this.val$view.setScaleY(1.0f);
            }
        }

        public void onAnimationCancel(Animator animation) {
        }
    }

    public static void fadeInAnimation(View view, float startAlpha, int duration) {
        Animation animation = new AlphaAnimation(startAlpha, 1.0f);
        animation.setDuration((long) duration);
        view.startAnimation(animation);
    }

    public static void fadeInAnimation(View view, float startAlpha) {
        fadeInAnimation(view, startAlpha, DEFAULT_DURATION);
    }

    public static void fadeOutAnimation(View view, float startAlpha) {
        fadeOutAnimation(view, startAlpha, DEFAULT_DURATION, false);
    }

    public static void fadeOutAnimation(View view, float startAlpha, boolean isGone) {
        fadeOutAnimation(view, startAlpha, DEFAULT_DURATION, isGone);
    }

    public static void fadeOutAnimation(View view, float startAlpha, int duration, boolean isGone) {
        Animation animation = new AlphaAnimation(startAlpha, 0.0f);
        animation.setDuration((long) duration);
        if (isGone) {
            animation.setAnimationListener(new C18631(view));
        }
        view.startAnimation(animation);
    }

    public static void fadeIn3Animation(View view) {
        fadeInAnimation(view, 0.3f, DEFAULT_DURATION);
    }

    public static void fadeIn5Animation(View view) {
        fadeInAnimation(view, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, DEFAULT_DURATION);
    }

    public static Animation getFadeInAnimation(float startAlpha) {
        Animation animation = new AlphaAnimation(startAlpha, 1.0f);
        animation.setDuration((long) DEFAULT_DURATION);
        return animation;
    }

    public static Animation get3FadeInAnimation() {
        Animation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration((long) DEFAULT_DURATION);
        return animation;
    }

    public static Animation get5FadeInAnimation() {
        Animation animation = new AlphaAnimation(ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 1.0f);
        animation.setDuration((long) DEFAULT_DURATION);
        return animation;
    }

    public static void zoomAnimation(View view, float t, int duration) {
        zoomAnimation(view, view.getScaleX(), t, view.getScaleY(), t, duration);
    }

    public static void zoomAnimation(View view, float fromX, float toX, float fromY, float toY, int duration) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{fromX, toX});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{fromY, toY});
        view.setLayerType(2, null);
        AnimatorSet set = new AnimatorSet();
        set.setDuration((long) duration);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new C18652(view));
        set.start();
    }

    public static void alphaAnimation(View view, float start, float end, long time) {
        view.setLayerType(2, null);
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", new float[]{start, end});
        oa.setDuration(time);
        oa.addListener(new C18673(view));
        oa.start();
    }

    public static void scaleAnimation(View view, float start, float end, long time) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{start, end});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{start, end});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(time);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new C18694(view));
        set.start();
    }

    public static void scaleAnimation(View view, float start, float end, long time, boolean hwLayer) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{start, end});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{start, end});
        if (hwLayer) {
            view.setLayerType(2, null);
        }
        AnimatorSet set = new AnimatorSet();
        set.setDuration(time);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new C18715(view));
        set.start();
    }

    public static void clickScaleAnimation(View view) {
        view.setEnabled(false);
        float scaleX = view.getScaleX();
        float scaleY = view.getScaleY();
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{scaleX, 0.9f, scaleX});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{scaleY, 0.9f, scaleY});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new C18726(view));
        set.start();
    }

    public static void animationFromXML(Context context, View view, int rId) {
        view.startAnimation(AnimationUtils.loadAnimation(context, rId));
    }

    public static void animationFromXML(Context context, View view, int rId, AnimationListener listener) {
        Animation animation = AnimationUtils.loadAnimation(context, rId);
        animation.setAnimationListener(listener);
        view.startAnimation(animation);
    }

    public static void zoomInAnimation(View v) {
        zoomInAnimation(v, DEF_ZOOM_RATIO);
    }

    public static void zoomOutAnimation(View v) {
        zoomOutAnimation(v, DEF_ZOOM_RATIO);
    }

    public static void zoomInAnimation(View v, float ratio) {
        ValueAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", new float[]{1.0f, ratio});
        ValueAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", new float[]{1.0f, ratio});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(new Animator[]{anim1, anim2});
        set.start();
    }

    public static void zoomOutAnimation(View v, float ratio) {
        ValueAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", new float[]{ratio, 1.0f});
        ValueAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", new float[]{ratio, 1.0f});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(new Animator[]{anim1, anim2});
        set.start();
    }

    public static float getDefaultZoomRatio() {
        return DEF_ZOOM_RATIO;
    }

    public static void translateAnimationX(View view, float fromX, float toX, int duration, Interpolator interpolator) {
        translateAnimation(view, fromX, toX, 0.0f, 0.0f, duration, interpolator);
    }

    public static void translateAnimationY(View view, float fromY, float toY, int duration, Interpolator interpolator) {
        translateAnimation(view, 0.0f, 0.0f, fromY, toY, duration, interpolator);
    }

    public static void translateAnimation(View view, float fromX, float toX, float fromY, float toY, int duration, Interpolator interpolator) {
        TranslateAnimation anim = new TranslateAnimation(1, fromX, 1, toX, 1, fromY, 1, toY);
        anim.setInterpolator(interpolator);
        anim.setDuration((long) duration);
        view.startAnimation(anim);
    }

    public static void startCinemaRockBell(View view) {
        RotateAnimation r1 = new RotateAnimation(0.0f, 15.0f, 1, 0.75f, 1, 0.0f);
        r1.setRepeatCount(14);
        r1.setFillAfter(true);
        r1.setDuration(130);
        RotateAnimation r2 = new RotateAnimation(-15.0f, 0.0f, 1, 0.75f, 1, 0.0f);
        r2.setFillAfter(true);
        r2.setDuration(130);
        r2.setRepeatCount(14);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(r1);
        set.addAnimation(r2);
        set.setRepeatMode(2);
        view.startAnimation(set);
    }
}
