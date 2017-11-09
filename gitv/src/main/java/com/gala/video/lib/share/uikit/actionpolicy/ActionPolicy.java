package com.gala.video.lib.share.uikit.actionpolicy;

import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.OnFirstLayoutListener;
import com.gala.video.albumlist.widget.BlocksView.OnFocusLostListener;
import com.gala.video.albumlist.widget.BlocksView.OnFocusPositionChangedListener;
import com.gala.video.albumlist.widget.BlocksView.OnItemAnimatorFinishListener;
import com.gala.video.albumlist.widget.BlocksView.OnItemClickListener;
import com.gala.video.albumlist.widget.BlocksView.OnItemFocusChangedListener;
import com.gala.video.albumlist.widget.BlocksView.OnItemStateChangeListener;
import com.gala.video.albumlist.widget.BlocksView.OnMoveToTheBorderListener;
import com.gala.video.albumlist.widget.BlocksView.OnScrollListener;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.share.uikit.core.BinderViewHolder;

public abstract class ActionPolicy extends OnScrollListener implements OnItemClickListener, OnItemFocusChangedListener, OnFocusLostListener, OnItemStateChangeListener, OnFirstLayoutListener, OnItemAnimatorFinishListener, OnAttachStateChangeListener, OnFocusPositionChangedListener, OnMoveToTheBorderListener {
    public void onFirstLayout(ViewGroup parent) {
    }

    public void onItemDetached(ViewGroup parent, ViewHolder holder) {
    }

    public void onItemAttached(ViewGroup parent, ViewHolder holder) {
    }

    public void onFocusLost(ViewGroup parent, ViewHolder holder) {
    }

    public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
    }

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
    }

    public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
    }

    public void onItemAnimatorFinished(ViewGroup parent) {
    }

    public void onMoveToTheBorder(ViewGroup parent, View child, int direction) {
    }

    public void onViewAttachedToWindow(View view) {
    }

    public void onViewDetachedFromWindow(View view) {
    }

    protected BinderViewHolder<?, ?> cast(ViewHolder vh) {
        return (BinderViewHolder) vh;
    }

    protected BlocksView cast(ViewGroup view) {
        return (BlocksView) view;
    }
}
