package com.gala.video.widget.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;

public class PageViewUtils {
    static ScaleAnimation mScaleAnimation;
    static AnimatorSet mSet;
    static View mView;

    class C18861 extends AnimatorListenerAdapter {
        C18861() {
        }

        public void onAnimationEnd(Animator animation) {
        }
    }

    public static void restore() {
        if (mSet != null) {
            mSet.cancel();
        }
        if (mView != null) {
            mView.setScaleX(1.0f);
            mView.setScaleY(1.0f);
        }
    }

    public static void scale(View view, float fromX, float toX, float fromY, float toY, int duration) {
        if (mSet != null) {
            mSet.cancel();
        }
        if (mView != null) {
            mView.setScaleX(1.0f);
            mView.setScaleY(1.0f);
        }
        AnimatorSet set = new AnimatorSet();
        view.setLayerType(2, null);
        r1 = new Animator[2];
        r1[0] = ObjectAnimator.ofFloat(view, "scaleX", new float[]{fromX, toX});
        r1[1] = ObjectAnimator.ofFloat(view, "scaleY", new float[]{fromY, toY});
        set.playTogether(r1);
        set.addListener(new C18861());
        set.setDuration((long) duration).start();
        mSet = set;
        mView = view;
    }

    public static void zoomAnimation(Context context, View view, boolean focused) {
        zoomAnimation(context, view, focused, 1.05f);
    }

    public static void zoomAnimation(Context context, View view, boolean focused, float scale) {
        ObjectAnimator ax;
        ObjectAnimator ay;
        if (focused) {
            if (view.getScaleX() < scale) {
                ax = ObjectAnimator.ofFloat(view, "scaleX", new float[]{view.getScaleX(), scale});
                ay = ObjectAnimator.ofFloat(view, "scaleY", new float[]{view.getScaleY(), scale});
            } else {
                return;
            }
        } else if (view.getScaleX() > 1.0f) {
            ax = ObjectAnimator.ofFloat(view, "scaleX", new float[]{view.getScaleX(), 1.0f});
            ay = ObjectAnimator.ofFloat(view, "scaleY", new float[]{view.getScaleY(), 1.0f});
        } else {
            return;
        }
        ax.setDuration(200);
        ax.setInterpolator(new LinearInterpolator());
        ax.start();
        ay.setDuration(200);
        ay.setInterpolator(new LinearInterpolator());
        ay.start();
    }
}
