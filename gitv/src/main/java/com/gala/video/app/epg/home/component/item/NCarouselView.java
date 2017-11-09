package com.gala.video.app.epg.home.component.item;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.gala.video.app.epg.home.component.item.CarouseContract.Presenter;
import com.gala.video.app.epg.home.component.item.CarouseContract.View;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.uikit.view.ICarouselView;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;

public class NCarouselView extends FrameLayout implements View, IViewLifecycle<Presenter>, ICarouselView {
    private static final String TAG = "NCarouselView";
    private ImageView mCoverImageView;
    private boolean mIsAttached;
    private Presenter presenter;

    public NCarouselView(Context context) {
        this(context, null);
    }

    public NCarouselView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NCarouselView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mIsAttached = false;
        init(context);
        Log.i(TAG, "create NCarouselView@" + Integer.toHexString(hashCode()));
    }

    private void init(Context context) {
        setClickable(true);
        setFocusable(true);
        this.mCoverImageView = new ImageView(context);
        this.mCoverImageView.setScaleType(ScaleType.FIT_XY);
        addView(this.mCoverImageView, new LayoutParams(-1, -1));
        setTag(R.id.focus_end_scale, Float.valueOf(1.0f));
    }

    public void setScaleX(float scaleX) {
    }

    public void setScaleY(float scaleX) {
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mIsAttached = true;
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIsAttached = false;
        if (this.presenter != null) {
            this.presenter.onDetachedFromWindow();
        }
    }

    public boolean isAttached() {
        return this.mIsAttached;
    }

    public void onBind(Presenter presenter) {
        if (presenter != null) {
            this.presenter = presenter;
            presenter.setView(getContext(), this);
            presenter.addObserver();
            LogUtils.d(TAG, "onBind");
        }
    }

    public void onUnbind(Presenter presenter) {
        if (presenter != null) {
            this.presenter.removeObserver();
            presenter.onUnBind();
            LogUtils.d(TAG, "onUnBind");
        }
    }

    public void onShow(Presenter presenter) {
        if (presenter != null) {
            presenter.onShow();
        }
    }

    public void onHide(Presenter object) {
    }

    public android.view.View getPlayCoverView() {
        return this.mCoverImageView;
    }

    public void getLocation(int[] currentLocation) {
        getLocationOnScreen(currentLocation);
    }

    public Animator alphaAnimationPlayCover() {
        return alphaAnimation(this.mCoverImageView, 1.0f, 0.2f, 500);
    }

    public void setCoverImage(Drawable mCoverImage) {
        this.mCoverImageView.setImageDrawable(mCoverImage);
    }

    public boolean isCoverAttached() {
        return isAttached();
    }

    public int getCoverWidth() {
        return getWidth();
    }

    public int getCoverHeight() {
        return getHeight();
    }

    public void coverRequestFocus() {
        requestFocus();
    }

    public void setCoverClickListener(OnClickListener listener) {
        setOnClickListener(listener);
    }

    public void setCoverAlpha(float alpha) {
        this.mCoverImageView.setAlpha(alpha);
    }

    private void setCarouselItemBackground(Drawable drawable) {
        if (VERSION.SDK_INT >= 16) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }

    private ObjectAnimator alphaAnimation(final android.view.View view, float start, float end, long time) {
        view.setLayerType(2, null);
        ObjectAnimator oa = ObjectAnimator.ofFloat(view, "alpha", new float[]{start, end});
        oa.setDuration(time);
        oa.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.post(new Runnable() {
                    public void run() {
                        view.setLayerType(0, null);
                    }
                });
            }
        });
        oa.start();
        return oa;
    }
}
