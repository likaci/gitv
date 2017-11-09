package com.gala.video.lib.share.uikit.actionpolicy;

import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.gala.cloudui.imp.ICloudViewGala;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.xbill.DNS.WKSRecord.Service;

public class PageActionPolicy extends ActionPolicy {
    private static final int EXTRA_PADDING = 500;
    private static final int MSG_ON_SCROLL = 0;
    private static final String TAG = "PageActionPolicy";
    private static HandlerThread sHandlerThread;
    private static ThreadHandler sThreadHandler;
    private boolean mKeepFocusOnTop = false;
    private Runnable mLayoutRunnable = new Runnable() {
        public void run() {
            PageActionPolicy.this.mPage.getRoot().getLayoutManager().fastLayoutChildren();
        }
    };
    private Page mPage;
    private boolean mScrolling;
    private int mTopBarHeight;

    private static class ThreadHandler extends Handler {
        public ThreadHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            PageActionPolicy actionPolicy = msg.obj;
            if (actionPolicy != null) {
                switch (msg.what) {
                    case 0:
                        actionPolicy.doOnScroll(msg);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public PageActionPolicy(Page page) {
        this.mPage = page;
        initHandler();
    }

    private synchronized void initHandler() {
        if (sHandlerThread == null || sThreadHandler == null) {
            sHandlerThread = new HandlerThread("actionPolicy-thread");
            sHandlerThread.start();
            sThreadHandler = new ThreadHandler(sHandlerThread.getLooper());
        }
    }

    public void setTopBarHeight(int topBarHeight) {
        this.mTopBarHeight = topBarHeight;
    }

    public void keepFocusOnTop(boolean keepFocusOnTop) {
        this.mKeepFocusOnTop = keepFocusOnTop;
    }

    public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
        cast(parent).setExtraPadding(ResourceUtil.getPx(500));
        if (cast(parent).getScrollType() == 17) {
            boolean z;
            BlocksView cast = cast(parent);
            if (this.mScrolling) {
                z = false;
            } else {
                z = true;
            }
            cast.setScrollOnly(z);
        } else {
            cast(parent).setScrollOnly(false);
        }
        ActionPolicy actionPolicy = getCardActionPolicy(position);
        if (actionPolicy != null) {
            actionPolicy.onFocusPositionChanged(parent, position, hasFocus);
        }
        this.mPage.dispatchUserActionPolicy("onFocusPositionChanged", parent, Integer.valueOf(position), Boolean.valueOf(hasFocus));
    }

    public void onScrollBefore(ViewGroup parent, ViewHolder holder) {
        this.mPage.dispatchUserActionPolicy("onScrollBefore", parent, holder);
        dokeepFocusOnTop(parent, holder);
    }

    public void onScrollStart(ViewGroup parent) {
        this.mScrolling = true;
        ImageProviderApi.getImageProvider().stopAllTasks();
        this.mPage.sendEventToAttachedCards("onScrollStart", true, parent);
        this.mPage.dispatchUserActionPolicy("onScrollStart", parent);
    }

    public void onScrollStop(ViewGroup parent) {
        this.mScrolling = false;
        cast(parent).setExtraPadding(ResourceUtil.getPx(500));
        cast(parent).post(this.mLayoutRunnable);
        this.mPage.show();
        this.mPage.sendEventToAttachedCards("onScrollStop", true, parent);
        this.mPage.dispatchUserActionPolicy("onScrollStop", parent);
    }

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
        this.mPage.dispatchUserActionPolicy("onItemClick", parent, holder);
        ActionPolicy actionPolicy = getCardActionPolicy(holder.getLayoutPosition());
        if (actionPolicy != null) {
            actionPolicy.onItemClick(parent, holder);
        }
    }

    public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
        ActionPolicy actionPolicy = getCardActionPolicy(holder.getLayoutPosition());
        if (actionPolicy != null) {
            actionPolicy.onItemFocusChanged(parent, holder, hasFocus);
        }
        CardFocusHelper.triggerFoucs(holder.itemView, hasFocus);
    }

    public void onItemDetached(ViewGroup parent, ViewHolder holder) {
        cast(holder).unbind();
    }

    public void onItemAttached(ViewGroup parent, ViewHolder holder) {
        if (this.mPage.isStart() && cast(parent).getScrollState() == 1) {
            cast(holder).show();
            Item item = this.mPage.getItem(holder.getLayoutPosition());
            if (item != null && item.getParent() != null) {
                item.getParent().start();
            }
        }
    }

    public void onFirstLayout(ViewGroup parent) {
        if (cast(parent).getLayoutManager().isCanScroll(true)) {
            this.mPage.showLoading();
        }
        this.mPage.sendStartEventToCards();
        this.mPage.sendEventToAttachedCards("onFirstLayout", true, parent);
        this.mPage.dispatchUserActionPolicy("onFirstLayout", parent);
    }

    public void onFocusLost(ViewGroup parent, ViewHolder holder) {
        ActionPolicy actionPolicy = getCardActionPolicy(holder.getLayoutPosition());
        if (actionPolicy != null) {
            actionPolicy.onFocusLost(parent, holder);
        }
        this.mPage.dispatchUserActionPolicy("onFocusLost", parent, holder);
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        BlocksView blocksView = cast(parent);
        boolean shouldBindView = false;
        int direction = blocksView.getDirection();
        if (direction == Service.CISCO_FNA) {
            View lastView = blocksView.getViewByPosition(blocksView.getLastAttachedPosition());
            if (lastView == null || lastView.getBottom() - blocksView.getScrollY() > blocksView.getHeight()) {
                shouldBindView = false;
            } else {
                shouldBindView = true;
            }
        } else if (direction == 33) {
            View firstView = blocksView.getViewByPosition(blocksView.getFirstAttachedPosition());
            shouldBindView = firstView != null && firstView.getTop() - blocksView.getScrollY() >= 0;
        }
        if (shouldBindView) {
            blocksView.setScrollOnly(false);
            blocksView.setExtraPadding(0);
        }
        Bundle data = new Bundle();
        data.putInt("first", firstAttachedItem);
        data.putInt("last", lastAttachedItem);
        data.putInt("total", totalItemCount);
        Message msg = sThreadHandler.obtainMessage(0);
        msg.obj = this;
        msg.setData(data);
        sThreadHandler.sendMessage(msg);
    }

    public void onMoveToTheBorder(ViewGroup parent, View child, int direction) {
        if (isGalaView(child) && this.mPage != null) {
            boolean slow = this.mPage.shouldLoadMore() && (direction == Service.CISCO_FNA || direction == 66);
            long duration = slow ? 350 : 500;
            float cycle = slow ? 1.0f : 3.0f;
            float px = slow ? 12.0f : 4.0f;
            AnimationUtil.shakeAnimation(parent.getContext(), child, direction, duration, cycle, px);
            CardFocusHelper.edgeEffect(parent.getContext(), direction, duration, cycle, px);
        }
    }

    private boolean isGalaView(View child) {
        return child instanceof ICloudViewGala;
    }

    public void onViewAttachedToWindow(View view) {
        Log.d(TAG, "onViewAttachedToWindow");
        this.mPage.show();
    }

    public void onViewDetachedFromWindow(View view) {
        Log.d(TAG, "onViewDetachedFromWindow");
        this.mPage.hide();
    }

    private ActionPolicy getCardActionPolicy(int position) {
        if (this.mPage.getItemCount() == 0 || position >= this.mPage.getItemCount()) {
            return null;
        }
        Card card = this.mPage.getItem(position).getParent();
        if (card != null) {
            return card.getActionPolicy();
        }
        return null;
    }

    private void dokeepFocusOnTop(ViewGroup parent, ViewHolder holder) {
        if (holder != null) {
            Item item = this.mPage.getItem(holder.getLayoutPosition());
            BlockLayout layout = cast(parent).getBlockLayout(holder.getLayoutPosition());
            Card card = item.getParent();
            boolean allShow = false;
            int i = holder.getLayoutPosition() - 1;
            while (i >= cast(parent).getFirstAttachedPosition() - 1 && i >= 0) {
                if (cast(parent).getBlockLayout(i) != layout) {
                    allShow = true;
                    break;
                }
                i--;
            }
            if (this.mKeepFocusOnTop) {
                int extraPadding = 0;
                if (card != null && card.hasHeader() && allShow && cast(parent).getViewByPosition(layout.getFirstPosition()).getTop() == holder.itemView.getTop()) {
                    extraPadding = (card.getHeaderItem().getHeight() + layout.getPaddingTop()) + layout.getMarginTop();
                }
                int height = (((holder.itemView.getHeight() / 2) + this.mTopBarHeight) + extraPadding) + parent.getPaddingTop();
                cast(parent).setFocusPlace(height, height);
            }
        }
    }

    private void doOnScroll(Message msg) {
        ViewGroup parent = this.mPage.getRoot();
        int firstAttachedItem = msg.getData().getInt("first");
        int lastAttachedItem = msg.getData().getInt("last");
        int totalItemCount = msg.getData().getInt("total");
        this.mPage.dispatchUserActionPolicy("onScroll", parent, Integer.valueOf(firstAttachedItem), Integer.valueOf(lastAttachedItem), Integer.valueOf(totalItemCount));
    }
}
