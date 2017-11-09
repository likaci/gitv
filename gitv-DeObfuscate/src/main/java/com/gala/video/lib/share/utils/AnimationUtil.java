package com.gala.video.lib.share.utils;

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
import android.view.animation.AnimationUtils;
import android.view.animation.CycleInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import com.gala.video.lib.share.C1632R;
import org.xbill.DNS.WKSRecord.Service;

public class AnimationUtil {
    public static final int ANIMATION_DURATION = 200;
    private static int ANIMATION_X = C1632R.id.share_ll_toast;
    private static int ANIMATION_Y = C1632R.id.share_txt_toastmsg;
    private static int DEFAULT_DURATION = 500;
    private static final float DEF_ZOOM_RATIO = 1.05f;
    public static final int SHAKE_X = C1632R.id.shake_x;
    public static final int SHAKE_Y = C1632R.id.shake_y;
    private static final LinearInterpolator sLinearInterpolator = new LinearInterpolator();

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

    public static void fadeOutAnimation(final View view, float startAlpha, int duration, boolean isGone) {
        Animation animation = new AlphaAnimation(startAlpha, 0.0f);
        animation.setDuration((long) duration);
        if (isGone) {
            animation.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    view.setVisibility(8);
                }
            });
        }
        view.startAnimation(animation);
    }

    public static void zoomAnimation(View view, boolean focused, float scale, int microsecond) {
        zoomAnimation(view, focused, scale, microsecond, true);
    }

    public static void zoomLeftAnimation(View view, boolean focused, float scale, int microsecond) {
        view.setPivotX(0.0f);
        view.setPivotY(((float) view.getHeight()) / 2.0f);
        zoomAnimation(view, focused, scale, microsecond, true);
    }

    public static void clearZoomAnimation(View view) {
        if (view != null) {
            ObjectAnimator ax = (ObjectAnimator) view.getTag(ANIMATION_X);
            if (ax != null) {
                ax.cancel();
            }
            ObjectAnimator ay = (ObjectAnimator) view.getTag(ANIMATION_Y);
            if (ay != null) {
                ay.cancel();
            }
        }
    }

    public static void zoomAnimation(final View view, final boolean focused, float scale, int microsecond, boolean needHardWare) {
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
        if (needHardWare) {
            view.setLayerType(2, null);
        }
        ax.setDuration((long) microsecond);
        ax.setInterpolator(sLinearInterpolator);
        view.setTag(ANIMATION_X, ax);
        ax.start();
        ay.setDuration((long) microsecond);
        ay.setInterpolator(sLinearInterpolator);
        ay.addListener(new AnimatorListenerAdapter() {

            class C18501 implements Runnable {
                C18501() {
                }

                public void run() {
                    view.setLayerType(0, null);
                }
            }

            public void onAnimationCancel(Animator animation) {
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
            }

            public void onAnimationEnd(Animator animation) {
                if (!focused) {
                    view.post(new C18501());
                    view.setTag(AnimationUtil.ANIMATION_X, null);
                    view.setTag(AnimationUtil.ANIMATION_Y, null);
                }
            }
        });
        view.setTag(ANIMATION_Y, ay);
        ay.start();
    }

    public static void zoomAnimationSoftwareRender(final View view, final boolean focused, float scale, int microsecond) {
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
        view.setLayerType(1, null);
        ax.setDuration((long) microsecond);
        ax.setInterpolator(sLinearInterpolator);
        view.setTag(ANIMATION_X, ax);
        ax.start();
        ay.setDuration((long) microsecond);
        ay.setInterpolator(sLinearInterpolator);
        ay.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                if (!focused) {
                    view.setTag(AnimationUtil.ANIMATION_X, null);
                    view.setTag(AnimationUtil.ANIMATION_Y, null);
                }
            }
        });
        view.setTag(ANIMATION_Y, ay);
        ay.start();
    }

    public static void zoomAnimation(View view, float t, int duration) {
        zoomAnimation(view, view.getScaleX(), t, view.getScaleY(), t, duration);
    }

    public static void zoomAnimation(final View view, float fromX, float toX, float fromY, float toY, int duration) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{fromX, toX});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{fromY, toY});
        AnimatorSet set = new AnimatorSet();
        set.setDuration((long) duration);
        set.playTogether(new Animator[]{ox, oy});
        if (duration != 0 && (fromY < toY || fromX < toX)) {
            view.setLayerType(2, null);
            set.addListener(new AnimatorListenerAdapter() {

                class C18531 implements Runnable {
                    C18531() {
                    }

                    public void run() {
                        view.setLayerType(0, null);
                    }
                }

                public void onAnimationEnd(Animator animation) {
                    view.post(new C18531());
                }
            });
        }
        set.start();
    }

    public static void alphaAnimation(final View view, float start, float end, long time) {
        view.setLayerType(2, null);
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", new float[]{start, end});
        oa.setDuration(time);
        oa.addListener(new AnimatorListenerAdapter() {

            class C18551 implements Runnable {
                C18551() {
                }

                public void run() {
                    view.setLayerType(0, null);
                }
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.post(new C18551());
            }
        });
        oa.start();
    }

    public static void scaleAnimation(final View view, float start, float end, long time) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{start, end});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{start, end});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(time);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new AnimatorListenerAdapter() {

            class C18571 implements Runnable {
                C18571() {
                }

                public void run() {
                    view.setLayerType(0, null);
                }
            }

            public void onAnimationEnd(Animator animation) {
                view.post(new C18571());
            }
        });
        set.start();
    }

    public static void scaleAnimation(final View view, float start, float end, long time, boolean hwLayer) {
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{start, end});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{start, end});
        if (hwLayer) {
            view.setLayerType(2, null);
        }
        AnimatorSet set = new AnimatorSet();
        set.setDuration(time);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new AnimatorListenerAdapter() {

            class C18591 implements Runnable {
                C18591() {
                }

                public void run() {
                    view.setLayerType(0, null);
                }
            }

            public void onAnimationEnd(Animator animation) {
                view.post(new C18591());
            }
        });
        set.start();
    }

    public static void clickScaleAnimation(final View view) {
        view.setEnabled(false);
        float scaleX = view.getScaleX();
        float scaleY = view.getScaleY();
        ObjectAnimator ox = ObjectAnimator.ofFloat(view, "scaleX", new float[]{scaleX, 0.9f, scaleX});
        ObjectAnimator oy = ObjectAnimator.ofFloat(view, "scaleY", new float[]{scaleY, 0.9f, scaleY});
        AnimatorSet set = new AnimatorSet();
        set.setDuration(200);
        set.playTogether(new Animator[]{ox, oy});
        set.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                view.setEnabled(true);
                if (!view.isFocused()) {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                }
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
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

    public static void startZoomAnimation(View view, boolean hasFocus, float scale, boolean hwLayer) {
        if (view != null) {
            if (hasFocus) {
                view.bringToFront();
            }
            zoomAnimation(view, hasFocus, scale, 200, hwLayer);
        }
    }

    public static void shakeAnimation(Context context, final View view, int direction, long duration, float cycle, float px) {
        TranslateAnimation animation;
        TranslateAnimation ax = (TranslateAnimation) view.getTag(SHAKE_X);
        TranslateAnimation ay = (TranslateAnimation) view.getTag(SHAKE_Y);
        if (direction == 33 || direction == Service.CISCO_FNA) {
            if (ay == null) {
                if (ax != null) {
                    view.clearAnimation();
                }
                animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, px);
                view.setTag(SHAKE_Y, animation);
            } else {
                return;
            }
        } else if (ax == null) {
            if (ay != null) {
                view.clearAnimation();
            }
            animation = new TranslateAnimation(0.0f, px, 0.0f, 0.0f);
            view.setTag(SHAKE_X, animation);
        } else {
            return;
        }
        animation.setDuration(duration);
        animation.setInterpolator(new CycleInterpolator(cycle));
        animation.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                view.setTag(AnimationUtil.SHAKE_X, null);
                view.setTag(AnimationUtil.SHAKE_Y, null);
            }
        });
        view.startAnimation(animation);
    }
}
