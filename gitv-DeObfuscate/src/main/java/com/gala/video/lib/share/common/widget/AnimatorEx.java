package com.gala.video.lib.share.common.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AnimatorEx extends ValueAnimator implements AnimatorUpdateListener {
    private final List<AnimHolder> mHolderList = new CopyOnWriteArrayList();

    public AnimatorEx(float... values) {
        setFloatValues(values);
        addUpdateListener(this);
    }

    public void addAnimator(AnimHolder an) {
        this.mHolderList.add(an);
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        float f = valueAnimator.getAnimatedFraction();
        for (AnimHolder ah : this.mHolderList) {
            ah.doFrame(f);
        }
        if (f >= 1.0f) {
            this.mHolderList.clear();
        }
    }

    public void cancel() {
        super.cancel();
        if (this.mHolderList.size() > 0) {
            this.mHolderList.clear();
        }
    }

    public void bingo() {
        if (isStarted()) {
            super.cancel();
        }
        for (AnimHolder ah : this.mHolderList) {
            ah.doFrame(1.0f);
        }
        this.mHolderList.clear();
    }
}
