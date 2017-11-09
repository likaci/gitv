package com.gala.video.app.epg.screensaver;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants;

abstract class FadeAnim {
    protected Runnable mFadeInRunnable = new C07741();
    protected Runnable mFadeOutRunnable = new C07762();
    protected Handler mHandler = new Handler(Looper.getMainLooper());
    protected int mIndex = 0;
    protected int mShowingCount = 0;

    class C07741 implements Runnable {
        C07741() {
        }

        public void run() {
            FadeAnim fadeAnim = FadeAnim.this;
            int currentIndex = fadeAnim.mIndex;
            fadeAnim.mIndex = currentIndex + 1;
            if (FadeAnim.this.getNext(currentIndex)) {
                fadeAnim = FadeAnim.this;
                fadeAnim.mShowingCount++;
                FadeAnim.this.beforeFadeIn(currentIndex);
                AlphaAnimation fadeIn = new AlphaAnimation(0.0f, 1.0f);
                fadeIn.setDuration((long) FadeAnim.this.getFadeInDuration());
                FadeAnim.this.getAnimView().startAnimation(fadeIn);
                FadeAnim.this.afterFadeIn(currentIndex);
                FadeAnim.this.mHandler.postDelayed(FadeAnim.this.mFadeOutRunnable, (long) FadeAnim.this.getShowingDuration(currentIndex));
            } else if (FadeAnim.this.isEndWithoutPlay(currentIndex)) {
                FadeAnim.this.onEndWithoutPlay();
            } else {
                FadeAnim.this.mHandler.postDelayed(FadeAnim.this.mFadeInRunnable, (long) FadeAnim.this.getFadeInInterval());
            }
        }
    }

    class C07762 implements Runnable {

        class C07751 implements AnimationListener {
            C07751() {
            }

            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                FadeAnim.this.onEndWithPlay();
            }

            public void onAnimationRepeat(Animation animation) {
            }
        }

        C07762() {
        }

        public void run() {
            FadeAnim.this.beforeFadeOut();
            AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
            fadeOut.setDuration((long) FadeAnim.this.getFadeOutDuration());
            boolean isLast = FadeAnim.this.isLoopLast(FadeAnim.this.mShowingCount);
            if (isLast) {
                fadeOut.setAnimationListener(new C07751());
            }
            FadeAnim.this.getAnimView().startAnimation(fadeOut);
            if (!isLast) {
                FadeAnim.this.mHandler.postDelayed(FadeAnim.this.mFadeInRunnable, (long) FadeAnim.this.getFadeOutDuration());
            }
            FadeAnim.this.afterFadeOut(FadeAnim.this.mShowingCount);
        }
    }

    protected abstract View getAnimView();

    protected abstract boolean getNext(int i);

    protected abstract boolean isEndWithoutPlay(int i);

    protected abstract boolean isLoopLast(int i);

    protected abstract void onEndWithPlay();

    protected abstract void onEndWithoutPlay();

    FadeAnim() {
    }

    protected int getFadeInInterval() {
        return 1;
    }

    protected int getShowingDuration(int currentIndex) {
        return ScreenSaverConstants.ELAPSE_STATIC;
    }

    protected int getFadeInDuration() {
        return 2000;
    }

    protected int getFadeOutDuration() {
        return 2000;
    }

    protected void beforeFadeIn(int currentIndex) {
    }

    protected void afterFadeIn(int currentIndex) {
    }

    protected void beforeFadeOut() {
    }

    protected void afterFadeOut(int showingCount) {
    }

    public void clearShowingCount() {
        this.mShowingCount = 0;
        this.mIndex = 0;
    }
}
