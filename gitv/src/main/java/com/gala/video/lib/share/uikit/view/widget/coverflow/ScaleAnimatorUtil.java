package com.gala.video.lib.share.uikit.view.widget.coverflow;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.os.SystemClock;
import android.view.View;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.List;

public class ScaleAnimatorUtil {
    private final ArrayList<Animator> animatorList = new ArrayList();
    private int duration;
    private AnimatorListener mAnimatorListener;
    private ObjectAnimator mCurAnimatorX;
    private ObjectAnimator mCurAnimatorY;
    private TimeInterpolator mInterpolator;
    private long mStartTime;
    private boolean mStarted;

    public void setDuration(int ms) {
        this.duration = ms;
    }

    public void setInterpolator(TimeInterpolator value) {
        if (value != null) {
            this.mInterpolator = value;
        } else {
            this.mInterpolator = new LinearInterpolator();
        }
    }

    public void start() {
        int size = this.animatorList.size();
        if (size > 0) {
            this.mStarted = true;
            this.mStartTime = SystemClock.elapsedRealtime();
            for (int i = 0; i < size; i++) {
                ((Animator) this.animatorList.get(i)).start();
            }
        }
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public boolean isRunning() {
        if (this.mCurAnimatorX != null && this.mCurAnimatorX != null) {
            return this.mCurAnimatorY.isRunning() || this.mCurAnimatorY.isRunning();
        } else {
            if (this.mCurAnimatorX != null) {
                return this.mCurAnimatorX.isRunning();
            }
            if (this.mCurAnimatorY != null) {
                return this.mCurAnimatorY.isRunning();
            }
            return false;
        }
    }

    public void cancel() {
        List<Animator> list = (List) this.animatorList.clone();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ((Animator) list.get(i)).cancel();
        }
        this.mStarted = false;
    }

    public void end() {
        List<Animator> list = (List) this.animatorList.clone();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ((Animator) list.get(i)).end();
        }
        this.mStarted = false;
    }

    public void zoomAnimation(View view, boolean focused, float start, float end, boolean needHardwareLayer) {
        final ObjectAnimator ax = ObjectAnimator.ofFloat(view, "scaleX", new float[]{start, end});
        final ObjectAnimator ay = ObjectAnimator.ofFloat(view, "scaleY", new float[]{start, end});
        this.mCurAnimatorX = ax;
        this.mCurAnimatorY = ay;
        if (needHardwareLayer) {
            view.setLayerType(2, null);
        }
        ax.setDuration((long) this.duration);
        ax.setInterpolator(this.mInterpolator);
        ay.setDuration((long) this.duration);
        ay.setInterpolator(this.mInterpolator);
        ax.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                ScaleAnimatorUtil.this.animatorList.remove(ax);
            }
        });
        final boolean z = needHardwareLayer;
        final boolean z2 = focused;
        final View view2 = view;
        ay.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (ScaleAnimatorUtil.this.mAnimatorListener != null) {
                    ScaleAnimatorUtil.this.mAnimatorListener.onAnimationStart(animation);
                }
            }

            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (z && !z2) {
                    view2.post(new Runnable() {
                        public void run() {
                            view2.setLayerType(0, null);
                        }
                    });
                }
                ScaleAnimatorUtil.this.animatorList.remove(ay);
                if (ScaleAnimatorUtil.this.mAnimatorListener != null) {
                    ScaleAnimatorUtil.this.mAnimatorListener.onAnimationEnd(animation);
                }
            }

            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                if (ScaleAnimatorUtil.this.mAnimatorListener != null) {
                    ScaleAnimatorUtil.this.mAnimatorListener.onAnimationCancel(animation);
                }
            }
        });
        this.animatorList.add(ax);
        this.animatorList.add(ay);
        long d = (((long) this.duration) + this.mStartTime) - SystemClock.elapsedRealtime();
        if (d <= 0) {
            this.mStarted = false;
        }
        if (this.mStarted) {
            ax.setDuration(d);
            ay.setDuration(d);
            ax.start();
            ay.start();
        }
    }

    public void addListener(AnimatorListener animatorListener) {
        this.mAnimatorListener = animatorListener;
    }
}
