package com.gala.video.widget.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.ScaleAnimation;

public class ListViewPagerUtils {
    static ScaleAnimation mScaleAnimation;
    static AnimatorSet mSet;
    static View mView;

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
        set.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
            }
        });
        set.setDuration((long) duration).start();
        mSet = set;
        mView = view;
    }
}
