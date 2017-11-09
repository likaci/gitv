package com.gala.video.app.player.ui.widget.listview;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.RelativeLayout;
import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.LogUtils;

public abstract class AbsDetailListViewItem extends RelativeLayout implements OnFocusChangeListener {
    protected static final int SCALE_ANIM_DURATION = 200;
    protected String TAG = "AbsDetailListViewItem";
    protected String content;
    protected Context mContext;
    protected int mInnerPadding;
    protected int mItemFocusedBgResId;
    protected int mItemHeight;
    protected int mItemNormalBgResId;
    protected int mItemWidth;
    protected int mListItemDividerDbResId;
    protected int mListItemDividerHeight;
    protected float mScaleAnimRatio = 1.03f;

    public abstract void setAlbum(Album album);

    public abstract void setPlaying(boolean z);

    public AbsDetailListViewItem(Context context) {
        super(context);
        this.mContext = context;
    }

    public AbsDetailListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    public AbsDetailListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    protected int getDrawablePadding() {
        if (this.mItemNormalBgResId <= 0) {
            return 0;
        }
        Rect rect = new Rect();
        getContext().getResources().getDrawable(this.mItemFocusedBgResId).getPadding(rect);
        int pl = rect.top;
        LogUtils.m1568d(this.TAG, "getDrawablePadding return=" + pl);
        return pl;
    }

    protected void zoomIn(View view) {
        ValueAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", new float[]{1.0f, this.mScaleAnimRatio});
        ValueAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", new float[]{1.0f, this.mScaleAnimRatio});
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(200).playTogether(new Animator[]{animScaleX, animScaleY});
        animSet.start();
    }

    protected void zoomOut(View view) {
        ValueAnimator animScaleX = ObjectAnimator.ofFloat(view, "scaleX", new float[]{this.mScaleAnimRatio, 1.0f});
        ValueAnimator animScaleY = ObjectAnimator.ofFloat(view, "scaleY", new float[]{this.mScaleAnimRatio, 1.0f});
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(200).playTogether(new Animator[]{animScaleX, animScaleY});
        animSet.start();
    }

    public void setChannelInfo(CarouselChannelDetail carouselChannelDetail) {
    }

    public void clearItem() {
    }

    public void setProgrammeName(String name) {
    }

    public void setProgrammeTime(String formatTime) {
    }

    public void setIsLive(int i) {
    }

    public void setStartTime(String start) {
    }

    public void setEndTime(String end) {
    }

    public void setIsSpread(boolean isSpread) {
    }
}
