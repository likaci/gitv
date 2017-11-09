package com.gala.video.albumlist4.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import com.gitvdemo.video.R;
import org.xbill.DNS.WKSRecord.Service;

public class AnimationUtils {
    public static final int ANIMATION_X = 2131165186;
    public static final int ANIMATION_Y = 2131165187;
    public static final int SHAKE_X = 2131165184;
    public static final int SHAKE_Y = 2131165185;

    public static Animator shakeAnimation(int range, long duration, AnimatorUpdateListener listener) {
        PropertyValuesHolder ofKeyframe = PropertyValuesHolder.ofKeyframe("offsetTopAndBottom", new Keyframe[]{Keyframe.ofInt(0.0f, 0), Keyframe.ofInt(0.1f, -range), Keyframe.ofInt(0.26f, range), Keyframe.ofInt(0.42f, -range), Keyframe.ofInt(0.58f, range), Keyframe.ofInt(0.74f, -range), Keyframe.ofInt(0.9f, range), Keyframe.ofInt(1.0f, 0)});
        Animator ofPropertyValuesHolder = ValueAnimator.ofPropertyValuesHolder(new PropertyValuesHolder[]{ofKeyframe});
        ofPropertyValuesHolder.setDuration(500);
        ofPropertyValuesHolder.addUpdateListener(listener);
        return ofPropertyValuesHolder;
    }

    public static void shakeAnimation(Context context, final View view, int direction) {
        Animation translateAnimation;
        final int layerType = view.getLayerType();
        TranslateAnimation translateAnimation2 = (TranslateAnimation) view.getTag(R.raw.cardlayout);
        TranslateAnimation translateAnimation3 = (TranslateAnimation) view.getTag(R.raw.cardlayout_v1);
        if (direction == 33 || direction == Service.CISCO_FNA) {
            if (translateAnimation3 == null) {
                if (translateAnimation2 != null) {
                    view.clearAnimation();
                }
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, 4.0f);
                view.setTag(R.raw.cardlayout_v1, translateAnimation);
            } else {
                return;
            }
        } else if (translateAnimation2 == null) {
            if (translateAnimation3 != null) {
                view.clearAnimation();
            }
            translateAnimation = new TranslateAnimation(0.0f, 4.0f, 0.0f, 0.0f);
            view.setTag(R.raw.cardlayout, translateAnimation);
        } else {
            return;
        }
        if (layerType != 2) {
            view.setLayerType(2, null);
        }
        translateAnimation.setDuration(500);
        translateAnimation.setInterpolator(new CycleInterpolator(3.0f));
        translateAnimation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                view.setTag(R.raw.cardlayout, null);
                view.setTag(R.raw.cardlayout_v1, null);
                view.post(new Runnable(this) {
                    final /* synthetic */ AnonymousClass1 a;

                    {
                        this.a = r1;
                    }

                    public void run() {
                        view.setLayerType(layerType, null);
                    }
                });
            }
        });
        view.startAnimation(translateAnimation);
    }

    public static void zoomAnimation(final View view, final boolean focused, float scale, int microsecond, boolean needSoftwareLayer) {
        ObjectAnimator objectAnimator = (ObjectAnimator) view.getTag(R.raw.itemstyle_v1);
        ObjectAnimator objectAnimator2 = (ObjectAnimator) view.getTag(R.raw.login_succ);
        if (objectAnimator != null) {
            objectAnimator.end();
        }
        if (objectAnimator2 != null) {
            objectAnimator2.end();
        }
        view.setTag(R.raw.itemstyle_v1, null);
        view.setTag(R.raw.login_succ, null);
        if (focused) {
            objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleX", new float[]{1.0f, scale});
            objectAnimator = ObjectAnimator.ofFloat(view, "scaleY", new float[]{1.0f, scale});
        } else {
            objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleX", new float[]{scale, 1.0f});
            objectAnimator = ObjectAnimator.ofFloat(view, "scaleY", new float[]{scale, 1.0f});
        }
        if (needSoftwareLayer) {
            view.setLayerType(2, null);
        }
        objectAnimator2.setDuration((long) microsecond);
        objectAnimator2.setInterpolator(new LinearInterpolator());
        view.setTag(R.raw.itemstyle_v1, objectAnimator2);
        objectAnimator2.start();
        objectAnimator.setDuration((long) microsecond);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (!focused) {
                    view.setTag(R.raw.itemstyle_v1, null);
                    view.setTag(R.raw.login_succ, null);
                }
            }
        });
        view.setTag(R.raw.login_succ, objectAnimator);
        objectAnimator.start();
    }

    public static Animation loadAnimation(Context context, int id) {
        return android.view.animation.AnimationUtils.loadAnimation(context, id);
    }
}
