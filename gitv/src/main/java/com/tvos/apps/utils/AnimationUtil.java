package com.tvos.apps.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import com.gitvdemo.video.R;

public class AnimationUtil {
    private static int ANIMATION_X = R.array.voice_more_app_array;
    private static int ANIMATION_Y = R.array.voice_new_live_array;

    class AnonymousClass1 extends AnimatorListenerAdapter {
        private final /* synthetic */ boolean val$focused;
        private final /* synthetic */ View val$view;

        AnonymousClass1(boolean z, View view) {
            this.val$focused = z;
            this.val$view = view;
        }

        public void onAnimationEnd(Animator animation) {
            if (!this.val$focused) {
                this.val$view.setLayerType(0, null);
                this.val$view.setTag(AnimationUtil.ANIMATION_X, null);
                this.val$view.setTag(AnimationUtil.ANIMATION_Y, null);
            }
        }
    }

    public static void startFadein(View view, float start, float end, long duration) {
        Animation animation = new AlphaAnimation(start, end);
        animation.setDuration(duration);
        view.startAnimation(animation);
    }

    public static void zoomAnimation(View view, boolean focused, float scale, int microsecond) {
        zoomAnimation(view, focused, scale, microsecond, true);
    }

    public static void zoomAnimation(View view, boolean focused, float scale, int microsecond, boolean needSoftwareLayer) {
        ObjectAnimator ax = (ObjectAnimator) view.getTag(ANIMATION_X);
        ObjectAnimator ay = (ObjectAnimator) view.getTag(ANIMATION_Y);
        if (ax != null) {
            ax.end();
        }
        if (ay != null) {
            ay.end();
        }
        view.setTag(ANIMATION_X, null);
        view.setTag(ANIMATION_Y, null);
        if (focused) {
            ax = ObjectAnimator.ofFloat(view, "scaleX", new float[]{1.0f, scale});
            ay = ObjectAnimator.ofFloat(view, "scaleY", new float[]{1.0f, scale});
        } else {
            ax = ObjectAnimator.ofFloat(view, "scaleX", new float[]{scale, 1.0f});
            ay = ObjectAnimator.ofFloat(view, "scaleY", new float[]{scale, 1.0f});
        }
        if (needSoftwareLayer) {
            view.setLayerType(2, null);
        }
        ax.setDuration((long) microsecond);
        ax.setInterpolator(new LinearInterpolator());
        view.setTag(ANIMATION_X, ax);
        ax.start();
        ay.setDuration((long) microsecond);
        ay.setInterpolator(new LinearInterpolator());
        ay.addListener(new AnonymousClass1(focused, view));
        view.setTag(ANIMATION_Y, ay);
        ay.start();
    }

    public static void setAlphaAnimation(View view, float start, float end, long time) {
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", new float[]{start, end});
        oa.setDuration(time);
        oa.start();
    }

    public static void setScaleAnimation(View view, float start, float end, long time) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{start, end});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{start, end});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(time);
        set.playTogether(new Animator[]{ox, oy});
        set.start();
    }

    public static void scaleView(View v, float t, int duration) {
        v.setLayerType(1, null);
        AnimatorSet set = new AnimatorSet();
        r1 = new Animator[2];
        r1[0] = ObjectAnimator.ofFloat(v, "scaleX", new float[]{v.getScaleX(), t});
        r1[1] = ObjectAnimator.ofFloat(v, "scaleY", new float[]{v.getScaleY(), t});
        set.playTogether(r1);
        set.setDuration((long) duration).start();
    }
}
