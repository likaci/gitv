package com.gala.video.app.player.ui.widget.views;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.app.player.ui.widget.views.InteractItemView.IStateChangedListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.concurrent.atomic.AtomicBoolean;

public class InteractView extends FrameLayout {
    private static final int MSG_ITEM_TRANSLATE = 0;
    private static final int MSG_TEXT_ANIMATION_END = 1;
    private static final String TAG = "InteractView";
    private static final int TEXT_ANIMATION_DURATION = 2000;
    private View mInteractBtn;
    private View mInteractLayout;
    private InteractItemView mLastInteractItemView;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        private int mPraiseValue = 0;

        public void handleMessage(Message msg) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(InteractView.TAG, "handleMessage<<(msg=" + msg + ")");
            }
            switch (msg.what) {
                case 0:
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(InteractView.TAG, "handleMessage animationEnabled=" + InteractView.this.mPraiseText.animationEnabled());
                    }
                    if (InteractView.this.mPraiseText.animationEnabled()) {
                        this.mPraiseValue += ((Integer) msg.obj).intValue();
                        InteractView.this.mPraiseText.setText("人气+" + this.mPraiseValue);
                        if (InteractView.this.mPraiseText.getVisibility() != 0) {
                            InteractView.this.mPraiseText.setVisibility(0);
                            InteractView.this.mPraiseText.startPraiseAnimation();
                            return;
                        }
                        return;
                    }
                    return;
                case 1:
                    this.mPraiseValue = 0;
                    InteractView.this.mPraiseText.reset();
                    InteractView.this.mPraiseText.setVisibility(8);
                    return;
                default:
                    return;
            }
        }
    };
    private OnTranslateListener mOnTranslateListener;
    private MyTextView mPraiseText;

    private class MyTextView extends TextView {
        private final AtomicBoolean mAnimationEnabled = new AtomicBoolean(true);

        public MyTextView(Context context) {
            super(context);
        }

        public void setAnimationEnabled(boolean enabled) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(InteractView.TAG, "setAnimationEnabled=" + enabled);
            }
            this.mAnimationEnabled.set(enabled);
        }

        public boolean animationEnabled() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(InteractView.TAG, "animationEnabled=" + this.mAnimationEnabled.get());
            }
            return this.mAnimationEnabled.get();
        }

        public void startPraiseAnimation() {
            final ValueAnimator scaleAnimation = ValueAnimator.ofFloat(new float[]{getScaleX(), getScaleX() * 1.3f});
            scaleAnimation.setDuration(2000);
            scaleAnimation.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (MyTextView.this.animationEnabled()) {
                        MyTextView.this.setPivotX((float) MyTextView.this.getWidth());
                        MyTextView.this.setPivotY((float) MyTextView.this.getHeight());
                        MyTextView.this.setScaleX(((Float) animation.getAnimatedValue()).floatValue());
                        MyTextView.this.setScaleY(((Float) animation.getAnimatedValue()).floatValue());
                        MyTextView.this.setAlpha(ThreeDimensionalParams.TEXT_SCALE_FOR_3D + (animation.getAnimatedFraction() * ThreeDimensionalParams.TEXT_SCALE_FOR_3D));
                        return;
                    }
                    scaleAnimation.cancel();
                }
            });
            scaleAnimation.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                    InteractView.this.mMainHandler.sendEmptyMessage(1);
                }

                public void onAnimationCancel(Animator animation) {
                }
            });
            scaleAnimation.start();
        }

        public void reset() {
            setText("");
            setPivotX((float) getWidth());
            setPivotY((float) getHeight());
            setScaleX(1.0f);
            setScaleY(1.0f);
        }
    }

    public interface OnTranslateListener {
        void onStartTranslate(int i);
    }

    public InteractView(Context context) {
        super(context);
        initView();
    }

    public InteractView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public InteractView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public void setOnTranslateListener(OnTranslateListener listener) {
        this.mOnTranslateListener = listener;
    }

    private void initView() {
        this.mInteractLayout = LayoutInflater.from(getContext()).inflate(R.layout.player_layout_live_interact_view, null);
        addView(this.mInteractLayout, computeInteractLayoutParams());
        this.mInteractBtn = this.mInteractLayout.findViewById(R.id.interact_btn);
        this.mInteractBtn.requestFocus();
        this.mInteractBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(InteractView.TAG, "onClick() mLastInteractItemView=" + InteractView.this.mLastInteractItemView);
                }
                if (InteractView.this.mPraiseText == null) {
                    InteractView.this.mPraiseText = new MyTextView(InteractView.this.getContext());
                    InteractView.this.mPraiseText.setTextSize(0, (float) InteractView.this.getResources().getDimensionPixelOffset(R.dimen.live_praise_text_min_size));
                    InteractView.this.mPraiseText.setGravity(17);
                    InteractView.this.mPraiseText.setTextColor(InteractView.this.getResources().getColor(R.color.praise_text_color));
                    InteractView.this.mPraiseText.setVisibility(8);
                    InteractView.this.addView(InteractView.this.mPraiseText, InteractView.this.computeInteractTextLayoutParams());
                }
                if (InteractView.this.mLastInteractItemView == null || !InteractView.this.mLastInteractItemView.handledClick()) {
                    final InteractItemView newItem = new InteractItemView(InteractView.this.getContext());
                    newItem.setDrawableResId(R.drawable.player_ic_heart);
                    newItem.setScaleType(ScaleType.CENTER_INSIDE);
                    newItem.setStateChangedListener(new IStateChangedListener() {
                        public void onStateChanged(int state) {
                            if (state == 2) {
                                InteractView.this.notifyOnTranslate(newItem);
                                Message message = Message.obtain();
                                message.what = 0;
                                message.obj = Integer.valueOf(newItem.getPraiseValue());
                                InteractView.this.mMainHandler.sendMessage(message);
                            }
                        }
                    });
                    InteractView.this.addView(newItem, InteractView.this.computeInteractItemLayoutParams());
                    InteractView.this.mLastInteractItemView = newItem;
                }
                InteractView.this.mLastInteractItemView.onClick();
            }
        });
    }

    private LayoutParams computeInteractLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, -2, 85);
        layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.live_praise_margin_right);
        layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.live_praise_margin_bottom);
        return layoutParams;
    }

    private LayoutParams computeInteractItemLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(this.mInteractBtn.getWidth(), this.mInteractBtn.getHeight(), 85);
        MarginLayoutParams parentParams = (MarginLayoutParams) this.mInteractLayout.getLayoutParams();
        layoutParams.rightMargin = ((this.mInteractLayout.getWidth() - this.mInteractBtn.getWidth()) / 2) + parentParams.rightMargin;
        layoutParams.bottomMargin = this.mInteractLayout.getHeight() + parentParams.bottomMargin;
        return layoutParams;
    }

    private LayoutParams computeInteractTextLayoutParams() {
        LayoutParams layoutParams = new LayoutParams(-2, this.mInteractLayout.getHeight(), 85);
        MarginLayoutParams params = (MarginLayoutParams) this.mInteractLayout.getLayoutParams();
        layoutParams.rightMargin = (this.mInteractLayout.getWidth() + params.rightMargin) + getResources().getDimensionPixelOffset(R.dimen.live_praise_text_margin_right);
        layoutParams.bottomMargin = params.bottomMargin;
        return layoutParams;
    }

    private void notifyOnTranslate(InteractItemView view) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyOnTranslate<<(view=" + view + ")");
        }
        if (!(this.mOnTranslateListener == null || view == null)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "notifyOnTranslate...[translateListener=" + this.mOnTranslateListener + ", clickCount=" + view.getClickCount() + AlbumEnterFactory.SIGN_STR);
            }
            this.mOnTranslateListener.onStartTranslate(view.getClickCount());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "notifyOnTranslate>>()");
        }
    }

    public void setVisibility(int visibility) {
        if (this.mPraiseText != null) {
            this.mPraiseText.setAnimationEnabled(visibility == 0);
        }
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (!(child == null || child == this.mPraiseText)) {
                child.setVisibility(visibility);
            }
        }
        super.setVisibility(visibility);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchKeyEvent<<(event=" + event + ")");
        }
        if (event.getAction() == 0 && (23 == event.getKeyCode() || 66 == event.getKeyCode())) {
            handled = this.mInteractBtn.performClick();
        } else {
            handled = super.dispatchKeyEvent(event);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchKeyEvent>>() return " + handled);
        }
        return handled;
    }
}
