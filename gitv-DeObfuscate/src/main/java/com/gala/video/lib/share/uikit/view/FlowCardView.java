package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout.LayoutParams;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.card.CoverFlowCard;
import com.gala.video.lib.share.uikit.contract.CoverFlowContract.Presenter;
import com.gala.video.lib.share.uikit.contract.CoverFlowContract.View;
import com.gala.video.lib.share.uikit.item.CoverFlowItem;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.view.widget.coverflow.OnScrollListener;

public final class FlowCardView extends FlowCardHScrollView implements IViewLifecycle<Presenter>, View {
    private static final String TAG = "FlowCardView";
    private boolean hasOnBind = false;
    private boolean hasOnShow = false;
    private Presenter mPresenter;
    private OnScrollListener mScrollListener = new C18351();

    class C18351 implements OnScrollListener {
        C18351() {
        }

        public void onLayoutChanged(android.view.View child, int index, int visibility) {
        }

        public void onScrollStateChanged(boolean start) {
            if (!start) {
                FlowCardView.this.checkImage();
            }
        }

        public void onChildVisibilityChange(android.view.View child, int index, int curVisibility, int oldVisibility) {
        }
    }

    public FlowCardView(Context context) {
        super(context);
    }

    public FlowCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onBind(Presenter presenter) {
        this.mPresenter = presenter;
        stopScroll();
        addOnScrollListener(this.mScrollListener);
        setPadding(presenter.getCardModel().getBodyPaddingLeft(), 0, presenter.getCardModel().getBodyPaddingRight(), 0);
        int itemCount = ListUtils.getCount(presenter.getItems());
        if (itemCount == 0) {
            Log.d(TAG, " CoverFlowItem@ onBind return");
            return;
        }
        int i;
        presenter.setView(this);
        int childCount = getChildCount();
        Log.d(TAG, " CoverFlowItem@" + presenter.hashCode() + " onBind, CoverFlowCard@" + ((CoverFlowItem) presenter).getParent().hashCode() + ", FlowCardView@" + hashCode() + ",itemCount" + itemCount + ",childCount=" + childCount);
        for (i = 0; i < itemCount; i++) {
            android.view.View view;
            Item item = presenter.getItem(i);
            if (i < childCount) {
                view = getChildAt(i);
            } else {
                view = presenter.createView();
                view.setFocusable(true);
                addView(view);
            }
            LayoutParams params = new LayoutParams(-2, -2);
            params.width = item.getWidth();
            params.height = item.getHeight();
            view.setLayoutParams(params);
            ((IViewLifecycle) view).onBind(item);
        }
        for (i = childCount - 1; i >= itemCount; i--) {
            view = getChildAt(i);
            if (view != null) {
                if (view.isFocused()) {
                    requestFocus();
                }
                removeView(view);
            }
        }
        if (itemCount > 0) {
            setFocusable(true);
        } else {
            setFocusable(false);
        }
        this.hasOnBind = true;
        this.hasOnShow = false;
    }

    public void onUnbind(Presenter presenter) {
        this.mPresenter = presenter;
        stopScroll();
        removeDelayedMessage();
        removeOnScrollListener(this.mScrollListener);
        Log.d(TAG, " CoverFlowItem@" + presenter.hashCode() + " onUnbind, CoverFlowCard@" + ((CoverFlowItem) presenter).getParent().hashCode() + ", FlowCardView@" + hashCode() + ",itemCount" + ListUtils.getCount(this.mPresenter.getItems()) + ",childCount=" + getChildCount());
        for (int i = 0; i < getChildCount(); i++) {
            Item item = presenter.getItem(i);
            android.view.View view = getChildAt(i);
            if (!(item == null || view == null)) {
                ((IViewLifecycle) view).onUnbind(item);
            }
        }
        this.hasOnBind = false;
        this.hasOnShow = false;
    }

    public void onShow(Presenter presenter) {
        this.mPresenter = presenter;
        Log.d(TAG, " CoverFlowItem@" + presenter.hashCode() + " onShow, CoverFlowCard@" + ((CoverFlowItem) presenter).getParent().hashCode() + ", FlowCardView@" + hashCode() + ",itemCount" + ListUtils.getCount(this.mPresenter.getItems()) + ",childCount=" + getChildCount());
        checkCoverFlowMsg();
        this.hasOnShow = true;
        checkImage();
    }

    public void onHide(Presenter presenter) {
        this.mPresenter = presenter;
        int childCount = getChildCount();
        Log.d(TAG, " CoverFlowItem@" + presenter.hashCode() + " onHide, CoverFlowCard@" + ((CoverFlowItem) presenter).getParent().hashCode() + ", FlowCardView@" + hashCode() + ",itemCount" + ListUtils.getCount(this.mPresenter.getItems()) + ",childCount=" + childCount);
        removeDelayedMessage();
        this.hasOnShow = false;
        for (int i = 0; i < childCount; i++) {
            Item item = presenter.getItem(i);
            android.view.View view = getChildAt(i);
            if (!(item == null || view == null)) {
                ((IViewLifecycle) view).onHide(item);
            }
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        checkImage();
    }

    private void checkImage() {
        if (this.hasOnBind && this.mPresenter != null && this.hasOnShow) {
            int size = getChildCount();
            for (int i = 0; i < size; i++) {
                android.view.View view = getChildAt(i);
                Item item = this.mPresenter.getItem(i);
                if (!(item == null || view == null)) {
                    if (getShowingViewList().contains(view)) {
                        ((IViewLifecycle) view).onShow(item);
                    } else {
                        ((IViewLifecycle) view).onHide(item);
                    }
                }
            }
        }
    }

    protected void onHideView(android.view.View view) {
        if (this.mPresenter != null) {
            Item item = this.mPresenter.getItem(indexOfChild(view));
            if (item != null && view != null) {
                ((IViewLifecycle) view).onHide(item);
            }
        }
    }

    public int getFocusIndex() {
        return super.getFocusIndex();
    }

    public void addOnScrollListener(OnScrollListener listener) {
        super.addOnScrollListener(listener);
    }

    public void removeOnScrollListener(OnScrollListener listener) {
        super.removeOnScrollListener(listener);
    }

    public void notifyDataSetChange(Presenter presenter) {
        this.mPresenter = presenter;
        if (this.mPresenter != null) {
            Log.d(TAG, " CoverFlowItem@" + this.mPresenter.hashCode() + "notifyDataSetChange, CoverFlowCard@" + ((CoverFlowItem) this.mPresenter).getParent().hashCode() + ", FlowCardView@" + hashCode() + ",itemCount" + ListUtils.getCount(this.mPresenter.getItems()) + ",childCount=" + getChildCount());
            if (this.hasOnBind) {
                Log.d(TAG, " CoverFlowItem@" + this.mPresenter.hashCode() + "notifyDataSetChange,hasOnBind.begin");
                onUnbind(this.mPresenter);
                onBind(this.mPresenter);
                onShow(this.mPresenter);
                checkCoverFlowMsg();
            }
        }
    }

    private void checkCoverFlowMsg() {
        if (this.mPresenter != null) {
            ((CoverFlowCard) ((CoverFlowItem) this.mPresenter).getParent()).checkCoverFlowMsg();
        }
    }

    protected void invalidateFocus(android.view.View v, boolean hasFocus) {
        if (v != null) {
            CardFocusHelper mgr = CardFocusHelper.getMgr(getContext());
            if (hasFocus) {
                if (mgr != null) {
                    CardFocusHelper.setMarginLR(getContext(), getPaddingLeft(), getPaddingRight());
                    mgr.viewGotFocus(v);
                }
            } else if (mgr != null) {
                CardFocusHelper.setMarginLR(getContext(), 0, 0);
                mgr.viewLostFocus(v);
            }
        }
    }
}
