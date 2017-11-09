package com.gala.video.app.player.ui.widget.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class InteractItemView extends ImageView {
    private static final int TRANSLATE_ANIMATION_DELAY = 100;
    private static final int TRANSLATE_ANIMATION_DURATION = 2000;
    private static final int TRANSLATE_ANIMATION_LINE_COUNT = 10;
    private String TAG = "InteractItemView";
    private final AtomicBoolean mAnimationAborted = new AtomicBoolean(false);
    private int mClickCount = 0;
    private int mCurrentState = 0;
    private final Handler mMainHandler;
    private IStateChangedListener mStateChangedListener;
    private Runnable mTranslateRunnable = new C15491();

    class C15491 implements Runnable {
        C15491() {
        }

        public void run() {
            InteractItemView.this.translate();
        }
    }

    private static class MyAnimatorUpdateListener<T> implements AnimatorUpdateListener {
        private T mLastAnimatorValue;

        public MyAnimatorUpdateListener(T startValue) {
            this.mLastAnimatorValue = startValue;
        }

        public final void onAnimationUpdate(ValueAnimator animation) {
            T newValue = animation.getAnimatedValue();
            onAnimationUpdate(animation, this.mLastAnimatorValue, newValue);
            this.mLastAnimatorValue = newValue;
        }

        public void onAnimationUpdate(ValueAnimator animator, T t, T t2) {
        }
    }

    private static class MyAnimatorListener implements AnimatorListener {
        private MyAnimatorListener() {
        }

        public void onAnimationStart(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationRepeat(Animator animation) {
        }
    }

    class C15513 extends MyAnimatorListener {
        C15513() {
            super();
        }

        public void onAnimationEnd(Animator animation) {
            InteractItemView.this.notifyStateChanged(3);
        }
    }

    public interface IStateChangedListener {
        public static final int STATE_FINISH = 3;
        public static final int STATE_IDLE = 0;
        public static final int STATE_READY_TO_TRANSLATE = 1;
        public static final int STATE_TRANSLATE_ING = 2;

        void onStateChanged(int i);
    }

    public InteractItemView(Context context) {
        super(context);
        this.TAG += "-" + hashCode();
        this.mMainHandler = new Handler(Looper.getMainLooper());
    }

    public void setVisibility(int visibility) {
        if (visibility != 0) {
            stopAllAnimation();
        }
        super.setVisibility(visibility);
    }

    private void stopAllAnimation() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "stopAllAnimation() currentState=" + this.mCurrentState);
        }
        this.mMainHandler.removeCallbacks(this.mTranslateRunnable);
        this.mAnimationAborted.set(true);
        if (this.mCurrentState == 1) {
            notifyStateChanged(3);
        }
    }

    public boolean onClick() {
        boolean handled;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onClick<<() currentState=" + this.mCurrentState + ", clickCount=" + this.mClickCount + ", aborted=" + this.mAnimationAborted.get());
        }
        switch (this.mCurrentState) {
            case 0:
                this.mClickCount++;
                notifyStateChanged(1);
                this.mMainHandler.postDelayed(this.mTranslateRunnable, 100);
                handled = true;
                break;
            case 1:
                handled = true;
                break;
            default:
                handled = false;
                break;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "onClick>>() return " + handled);
        }
        return handled;
    }

    private void translate() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "translate()");
        }
        notifyStateChanged(2);
        final int rate = new Random().nextInt(10);
        final ValueAnimator translate = ValueAnimator.ofInt(new int[]{0, getTop()});
        translate.setDuration(2000);
        translate.addUpdateListener(new MyAnimatorUpdateListener<Integer>(Integer.valueOf(0)) {
            public void onAnimationUpdate(ValueAnimator animator, Integer lastValue, Integer newValue) {
                if (InteractItemView.this.mAnimationAborted.get()) {
                    translate.cancel();
                    return;
                }
                int value = newValue.intValue() - lastValue.intValue();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(InteractItemView.this.TAG, "translate...() offset=" + value);
                }
                InteractItemView.this.setAlpha(1.0f - animator.getAnimatedFraction());
                InteractItemView.this.setTranslationY(InteractItemView.this.getTranslationY() - ((float) value));
                InteractItemView.this.setTranslationX(InteractItemView.this.getTranslationX() - ((((float) Math.sqrt((double) value)) * ((float) rate)) / 3.0f));
            }
        });
        translate.addListener(new C15513());
        translate.start();
    }

    private void notifyStateChanged(int targetState) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyStateChanged<<(targetState=" + targetState + "), currentState=" + this.mCurrentState + ", stateChangedListener=" + this.mStateChangedListener);
        }
        if (this.mCurrentState != targetState) {
            this.mCurrentState = targetState;
            if (this.mStateChangedListener != null) {
                this.mStateChangedListener.onStateChanged(this.mCurrentState);
            }
            if (this.mCurrentState == 3) {
                ViewGroup parent = (ViewGroup) getParent();
                if (parent != null) {
                    parent.removeView(this);
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyStateChanged>>()");
        }
    }

    public void setDrawableResId(int drawableResId) {
        setBackgroundResource(drawableResId);
    }

    public void setStateChangedListener(IStateChangedListener listener) {
        this.mStateChangedListener = listener;
    }

    public boolean handledClick() {
        boolean handled = (this.mCurrentState == 2 || this.mCurrentState == 3) ? false : true;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handledClick>>() return " + handled + ", currentState=" + this.mCurrentState);
        }
        return handled;
    }

    public int getClickCount() {
        return this.mClickCount;
    }

    public int getPraiseValue() {
        int value = 10;
        for (int i = 0; i < this.mClickCount; i++) {
            value += i * 10;
        }
        return value;
    }
}
