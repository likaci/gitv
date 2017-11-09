package com.gala.video.widget.util;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.ScaleAnimation;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;

public final class AnimationUtils {
    private static final float DEF_ZOOM_RATIO = 1.05f;
    private static final int ZOOM_ANIM_DURATION = 200;
    private static boolean sUseAnimator = true;

    private AnimationUtils() {
    }

    public static void zoomIn(View v) {
        zoomIn(v, DEF_ZOOM_RATIO);
    }

    public static void zoomOut(View v) {
        zoomOut(v, DEF_ZOOM_RATIO);
    }

    public static void zoomIn(View v, float ratio) {
        if (sUseAnimator) {
            ValueAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", new float[]{1.0f, ratio});
            ValueAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", new float[]{1.0f, ratio});
            AnimatorSet set = new AnimatorSet();
            set.setDuration(200);
            set.playTogether(new Animator[]{anim1, anim2});
            set.start();
            return;
        }
        ScaleAnimation scale = new ScaleAnimation(1.0f, ratio, 1.0f, ratio, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
        scale.setDuration(200);
        scale.setFillAfter(true);
        v.startAnimation(scale);
    }

    public static void zoomOut(View v, float ratio) {
        if (sUseAnimator) {
            ValueAnimator anim1 = ObjectAnimator.ofFloat(v, "scaleX", new float[]{ratio, 1.0f});
            ValueAnimator anim2 = ObjectAnimator.ofFloat(v, "scaleY", new float[]{ratio, 1.0f});
            AnimatorSet set = new AnimatorSet();
            set.setDuration(200);
            set.playTogether(new Animator[]{anim1, anim2});
            set.start();
            return;
        }
        ScaleAnimation scale = new ScaleAnimation(ratio, 1.0f, ratio, 1.0f, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 1, ThreeDimensionalParams.TEXT_SCALE_FOR_3D);
        scale.setDuration(200);
        scale.setFillAfter(true);
        v.startAnimation(scale);
    }

    public static float getDefaultZoomRatio() {
        return DEF_ZOOM_RATIO;
    }

    public static void setUseAnimator(boolean useAnimator) {
        sUseAnimator = useAnimator;
    }
}
